# agents-observe 포맷 호환 수정

> 작업일: 2026-04-03
> 선행 작업: 04_observe_훅_통합_작업_정리.md

---

## 1. 문제

04에서 전역 통합까지 완료했으나, 대시보드에서 두 가지가 작동하지 않았다:

1. **필터 미작동**: Prompts, Tools, Agents, Tasks, Session 등 필터 버튼을 눌러도 이벤트가 분류되지 않음
2. **아이콘 무색**: 대시보드 타임라인의 아이콘에 색상이 없음 (전부 기본색)

---

## 2. 원인 분석

agents-observe GitHub 소스코드를 분석한 결과, **서버가 기대하는 데이터 포맷과 우리가 보내는 포맷이 완전히 달랐다**.

### 서버가 기대하는 포맷 (envelope)

```json
{
  "hook_payload": {
    "hook_event_name": "PostToolUse",
    "tool_name": "Edit",
    "tool_use_id": "toolu_xxx",
    "session_id": "sess-abc",
    "tool_input": { ... },
    "tool_response": { ... }
  },
  "meta": {
    "env": {
      "AGENTS_OBSERVE_PROJECT_SLUG": "NIA"
    }
  }
}
```

### 우리가 보내던 포맷 (커스텀)

```json
{
  "project": "NIA",
  "type": "tool_use",
  "payload": {
    "hook": "PostToolUse",
    "toolName": "Edit",
    "raw": { ... }
  }
}
```

### 필드별 불일치

| 서버가 읽는 필드 | 우리가 보내던 필드 | 문제 |
|-----------------|------------------|------|
| `hook_event_name` | 없음 (payload.hook에 존재) | 서버가 type/subtype을 `unknown`으로 분류 → 필터 미작동 |
| `tool_name` (snake_case) | `toolName` (camelCase) | 서버가 도구 이름을 인식 못함 → 아이콘 무색 |
| `session_id` (최상위) | `payload.session.sessionId` (중첩) | 세션 분류 불가 |
| `tool_input` | `payload.raw.toolInput` (중첩) | 에이전트 메타데이터 추출 불가 |
| `meta.env.PROJECT_SLUG` | `project` (최상위) | 프로젝트 식별 불가 |

---

## 3. 수정한 파일

| 파일 | 위치 | 변경 |
|------|------|------|
| `observe_cli.mjs` | `C:\Users\gosungho\.claude\hooks\` | 전면 재작성 |
| `.wslconfig` | `C:\Users\gosungho\` | 신규 생성 (수동) |

---

## 4. observe_cli.mjs 수정 상세

### 설계 원칙 변경

**Before**: Claude Code stdin을 파싱하여 커스텀 포맷으로 변환 후 전송
**After**: Claude Code stdin 원본을 **있는 그대로** `hook_payload`로 감싸서 전송

agents-observe의 공식 hook.sh도 이 방식이다. stdin을 그대로 파이프할 뿐, 변환하지 않는다.

### 수정된 로직 흐름

```
1. --hook 인자, --project 인자 파싱

2. stdin에서 Claude Code 원본 JSON 읽기 (변환 없이 그대로)

3. 프로젝트 이름 결정
   ① --project 인자
   ② AGENTS_OBSERVE_PROJECT_SLUG 환경변수
   ③ observe_projects.json 매핑  ← "0. nia_source" → "NIA"
   ④ CLAUDE_PROJECT_DIR basename (fallback)

4. 호환 필드 보정 (Claude Code → agents-observe)
   - hook_event_name 주입 (--hook 인자에서)
   - toolName → tool_name (camelCase → snake_case)
   - toolInput → tool_input
   - toolOutput → tool_response
   - session.id → session_id (최상위로)

5. envelope 포맷으로 감싸기
   { hook_payload: {보정된 원본}, meta: { env: { PROJECT_SLUG } } }

6. HTTP POST → localhost:4981/api/events
```

### 키포인트: 필드 보정 코드

```javascript
// hook_event_name이 없으면 --hook 인자에서 주입
if (!hookPayload.hook_event_name) {
  hookPayload.hook_event_name = hookName;
}

// Claude Code는 camelCase, agents-observe는 snake_case
if (hookPayload.toolName && !hookPayload.tool_name) {
  hookPayload.tool_name = hookPayload.toolName;
}
if (hookPayload.toolInput && !hookPayload.tool_input) {
  hookPayload.tool_input = hookPayload.toolInput;
}
if (hookPayload.toolOutput && !hookPayload.tool_response) {
  hookPayload.tool_response = hookPayload.toolOutput;
}

// session_id를 최상위에 보장
if (!hookPayload.session_id && stdin.session?.id) {
  hookPayload.session_id = stdin.session.id;
}
```

### 제거된 코드

v2에서 추가했던 아래 기능들을 **전부 제거**했다. agents-observe 서버가 자체적으로 처리하므로 클라이언트에서 할 필요가 없다:

- `classifyEventType()` - 이벤트 타입 분류 (서버의 parser가 담당)
- `extractMetadata()` - 훅별 메타데이터 추출 (서버가 원본에서 직접 추출)
- `trackAgentStart/Stop()` - 에이전트 실행시간 추적 (서버가 Pre/Post 매칭으로 계산)
- `updateSessionStats()` - 세션 통계 누적 (서버가 세션별로 자동 집계)
- `%TEMP%/observe-agents/` - 상태 파일 디렉토리 (더 이상 사용하지 않음)

---

## 5. .wslconfig 생성 (수동 작업)

WSL2의 IP가 재시작마다 바뀌는 문제를 해결하기 위해 mirrored networking 모드를 활성화했다.

**파일**: `C:\Users\gosungho\.wslconfig`

```ini
[wsl2]
networkingMode=mirrored
```

**효과**: WSL2 Docker 포트가 Windows `localhost`로 자동 노출. `172.19.x.x` 같은 동적 IP 불필요.

**적용**: PowerShell에서 `wsl --shutdown` 후 WSL 재시작.

**참고**: `.wslconfig`는 항상 `C:\Users\{username}\` 에 위치. WSL이 D드라이브에 설치되어 있어도 이 경로는 동일.

---

## 6. 대시보드 필터 매핑 (참고)

agents-observe 서버의 parser가 `hook_event_name`을 읽어서 필터를 분류한다:

| 필터 버튼 | 매칭 조건 |
|----------|----------|
| **Prompts** | `hook_event_name == "UserPromptSubmit"` |
| **Tools** | `hook_event_name`이 `PreToolUse`, `PostToolUse`, `PostToolUseFailure` 이고 `tool_name`이 `mcp__`로 시작하지 않는 것 |
| **Agents** | `hook_event_name`이 `SubagentStart`, `SubagentStop` 또는 `tool_name == "Agent"` |
| **Tasks** | `hook_event_name == "TaskCompleted"` 또는 `tool_name`이 `TaskCreate`/`TaskUpdate` |
| **Session** | `hook_event_name`이 `SessionStart`, `SessionEnd` |
| **MCP** | `tool_name`이 `mcp__`로 시작 |
| **Stop** | `hook_event_name == "Stop"` |
| **Errors** | `hook_event_name == "PostToolUseFailure"` 또는 payload에 `error` 필드 존재 |

도구별 동적 필터(Bash, Read, Edit 등)는 `tool_name` 값에서 자동 생성된다.

---

## 7. 아이콘 색상 매핑 (참고)

서버가 `tool_name` 또는 `hook_event_name`으로 아이콘 색상을 결정한다:

| 색상 | 대상 |
|------|------|
| 노란색 | `SessionStart`, `SessionEnd`, `Stop` |
| 초록색 | `UserPromptSubmit` |
| 파란색 | `Bash`, `Read`, `Write`, `Edit`, `Glob`, `Grep`, `WebSearch`, `WebFetch` |
| 보라색 | `Agent`, `SubagentStart`, `SubagentStop`, `TeammateIdle` |
| 청록색 | `TaskCreated`, `TaskCompleted` |
| 장미색 | `PermissionRequest` |
| 하늘색 | `Notification` |
| 남색 | `Elicitation`, `ElicitationResult` (MCP) |

---

## 8. 변경 전후 비교

### 로그 포맷

**Before**:
```
[NIA:tool_use:PostToolUse] 2026-04-03T10:00:05.000Z tool=Edit ~34tok calls=1
```

**After**:
```
[NIA:PostToolUse] tool=Edit sid=sess-abc 2026-04-03T10:00:05.000Z
```

### 전송 데이터 크기

**Before**: 커스텀 메타데이터 + raw stdin = 원본의 ~2배
**After**: 원본 stdin + 보정 필드만 = 원본의 ~1.05배

---

## 9. 롤백

observe_cli.mjs는 전역 파일(git 미관리)이므로 이 문서의 "04_observe_훅_통합_작업_정리.md > 파일 2" 섹션에 기록된 내용을 참고.

.wslconfig 롤백: 파일 삭제 후 `wsl --shutdown`.
