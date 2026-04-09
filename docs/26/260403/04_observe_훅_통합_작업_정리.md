# agents-observe 대시보드 연동 - 최종 작업 정리

> 작업일: 2026-04-03
> 작성자: sh

---

## 1. 목적

Claude Code로 작업할 때 에이전트가 어떤 도구를 쓰는지, 어떤 파일을 수정하는지, 서브에이전트가 얼마나 실행되는지 등을 **실시간 대시보드**로 시각화하고 싶었다.

**agents-observe**(https://github.com/simple10/agents-observe)라는 오픈소스 프로젝트가 이 역할을 해준다. Claude Code의 훅 시스템에서 이벤트를 받아 웹 대시보드에 실시간 표시해준다.

### 최종 구현된 흐름

```
Claude Code (훅 이벤트 발생)
  │
  ├─ 전역 훅 (~/.claude/settings.json)
  │    └─ observe_cli.mjs (Node.js)
  │         └─ HTTP POST → localhost:4981/api/events
  │              └─ agents-observe 서버 (WSL2 Docker)
  │                   └─ React 대시보드에서 실시간 확인
  │
  └─ 프로젝트 훅 (.claude/settings.json)
       └─ handle-*.sh → moai hook (MoAI 워크플로우, observe와 무관)
```

### 대시보드 접속

- URL: `http://localhost:4981`
- 위치: WSL2 Docker 컨테이너
- 디버그 로그: `D:/claude_hook_debug.log`

---

## 2. 수정한 파일 전체 목록

### ⚠️ 전역 파일 (git 미관리 - 롤백 시 이 문서 참고)

| # | 파일 경로 | 작업 |
|---|----------|------|
| 1 | `C:\Users\gosungho\.claude\settings.json` | 수정 |
| 2 | `C:\Users\gosungho\.claude\hooks\observe_cli.mjs` | 신규 생성 |
| 3 | `C:\Users\gosungho\.claude\hooks\observe_projects.json` | 신규 생성 |
| 4 | `C:\Users\gosungho\.wslconfig` | 신규 생성 (WSL2 mirrored networking) |

### 프로젝트 파일 (git 관리)

| # | 파일 경로 | 작업 |
|---|----------|------|
| 5 | `.claude/hooks/moai/handle-user-prompt-submit.sh` | 수정 |
| 6 | `.claude/hooks/moai/handle-post-tool.sh` | 수정 |
| 7 | `.claude/hooks/moai/handle-stop.sh` | 수정 |
| 8 | `.claude/hooks/moai/handle-task-completed.sh` | 수정 |
| 9 | `.claude/hooks/moai/observe_cli.mjs` | 생성 → 삭제됨 (최종: 없음) |

---

## 3. 파일별 상세 - 무엇을 수정했고, 키포인트는 무엇인가

---

### 파일 1: `C:\Users\gosungho\.claude\settings.json` (전역)

**역할**: Claude Code 전역 설정. 여기 등록된 훅은 **모든 프로젝트**에서 실행된다.

**수정 내용**: `hooks` 섹션에 8개 이벤트를 추가했다.

**키포인트**: 이 파일에는 원래 hooks가 없었다. 아래 내용이 **전부 새로 추가된 것**이다.

```json
{
  "env": {
    "CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS": "1"    ← 기존 (변경 없음)
  },
  "autoUpdatesChannel": "latest",                   ← 기존 (변경 없음)
  "hooks": {                                         ← ★ 전부 새로 추가
    "UserPromptSubmit": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook UserPromptSubmit",
      "timeout": 5, "type": "command"
    }]}],
    "PostToolUse": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook PostToolUse",
      "timeout": 5, "type": "command"
    }]}],
    "PostToolUseFailure": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook PostToolUseFailure",
      "timeout": 5, "type": "command"
    }]}],
    "SubagentStart": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook SubagentStart",
      "timeout": 5, "type": "command"
    }]}],
    "SubagentStop": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook SubagentStop",
      "timeout": 5, "type": "command"
    }]}],
    "Stop": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook Stop",
      "timeout": 5, "type": "command"
    }]}],
    "TaskCompleted": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook TaskCompleted",
      "timeout": 5, "type": "command"
    }]}],
    "Notification": [{ "hooks": [{
      "command": "node \"$HOME/.claude/hooks/observe_cli.mjs\" --hook Notification",
      "timeout": 5, "type": "command"
    }]}]
  }
}
```

**롤백 방법**: `hooks` 키 전체를 삭제하면 원래 상태로 돌아감.

---

### 파일 2: `C:\Users\gosungho\.claude\hooks\observe_cli.mjs` (전역)

**역할**: Claude Code 훅 이벤트를 받아 agents-observe 대시보드 서버로 HTTP POST 전송하는 Node.js 스크립트.

**상태**: 신규 생성 (이 파일이 연동의 핵심)

**키포인트 - 주요 로직 흐름**:

```
1. 인자 파싱
   --hook UserPromptSubmit  → 어떤 이벤트인지
   --project NIA            → 프로젝트명 (선택)

2. 프로젝트 이름 결정 (우선순위)
   ① --project 인자
   ② AGENTS_OBSERVE_PROJECT_SLUG 환경변수
   ③ observe_projects.json 매핑 파일  ← "0. nia_source" → "NIA"
   ④ CLAUDE_PROJECT_DIR 디렉토리명 (fallback)

3. stdin에서 Claude Code 이벤트 데이터 읽기
   Claude Code가 JSON으로 제공하는 원본 그대로 사용

4. agents-observe 호환 필드 보정
   - hook_event_name 주입 (서버 parser가 필터 분류에 사용)
   - camelCase → snake_case 변환 (toolName → tool_name 등)
   - session_id를 최상위에 보장

5. envelope 포맷으로 감싸서 전송
   { hook_payload: {원본 stdin + 보정}, meta: { env: { PROJECT_SLUG } } }

6. HTTP POST → localhost:4981/api/events
   timeout: 3초 (실패해도 Claude Code에 영향 없음)

7. 로그 기록 → D:/claude_hook_debug.log
```

**핵심 설계 원칙**: Claude Code stdin을 **있는 그대로** 전달한다. agents-observe 서버의 parser가 `hook_event_name`, `tool_name`, `session_id` 등의 필드를 읽어 필터 분류와 아이콘 색상을 결정한다. 커스텀 변환을 하면 서버가 인식하지 못한다.

**전송 데이터 (envelope 포맷)**:

```json
{
  "hook_payload": {
    "hook_event_name": "PostToolUse",
    "tool_name": "Edit",
    "tool_use_id": "toolu_xxx",
    "session_id": "sess-abc",
    "timestamp": 1743678005000,
    "tool_input": { "file_path": "src/.../Foo.java", "old_string": "..." },
    "tool_response": "File updated successfully",
    "...기타 Claude Code 원본 필드들..."
  },
  "meta": {
    "env": {
      "AGENTS_OBSERVE_PROJECT_SLUG": "NIA"
    }
  }
}
```

**대시보드 필터 매핑** (서버의 parser가 `hook_event_name`으로 분류):

| 필터 | hook_event_name 값 |
|------|-------------------|
| Prompts | `UserPromptSubmit` |
| Tools | `PreToolUse`, `PostToolUse`, `PostToolUseFailure` (tool_name이 `mcp__`가 아닌 것) |
| Agents | `SubagentStart`, `SubagentStop` 또는 tool_name이 `Agent` |
| Tasks | `TaskCompleted` 또는 tool_name이 `TaskCreate`/`TaskUpdate` |
| Session | `SessionStart`, `SessionEnd` |
| MCP | tool_name이 `mcp__`로 시작하는 것 |
| Stop | `Stop` |
| Errors | `PostToolUseFailure` 또는 error 필드 존재 |

**아이콘 색상** (서버가 `tool_name` 또는 `hook_event_name`으로 결정):

| 색상 | 대상 |
|------|------|
| 노란색 | SessionStart, SessionEnd, Stop |
| 초록색 | UserPromptSubmit |
| 파란색 | Bash, Read, Write, Edit, Glob, Grep |
| 보라색 | Agent, SubagentStart, SubagentStop |
| 청록색 | TaskCreated, TaskCompleted |
| 장미색 | PermissionRequest |

**롤백 방법**: 파일 삭제.

---

### 파일 3: `C:\Users\gosungho\.claude\hooks\observe_projects.json` (전역)

**역할**: 디렉토리명 → 프로젝트명 매핑. 대시보드에 `0. nia_source` 대신 `NIA`로 표시하기 위함.

**상태**: 신규 생성

**전체 내용**:

```json
{
  "0. nia_source": "NIA"
}
```

**키포인트**: 새 프로젝트 추가 시 여기에 한 줄 추가하면 됨. 이 파일이 없거나 매핑이 없으면 디렉토리명이 그대로 사용됨.

**롤백 방법**: 파일 삭제.

---

### 파일 4: `C:\Users\gosungho\.wslconfig` (전역)

**역할**: WSL2 전역 설정. mirrored networking을 활성화하여 Docker 포트를 Windows localhost로 노출.

**상태**: 신규 생성

**전체 내용**:

```ini
[wsl2]
networkingMode=mirrored
```

**키포인트**: 이 설정이 없으면 WSL2 IP(172.19.x.x)가 재시작마다 바뀐다. mirrored 모드를 켜면 `localhost:4981`로 고정 접근 가능. 설정 후 반드시 `wsl --shutdown` 필요.

**롤백 방법**: 파일 삭제 후 `wsl --shutdown`.

---

### 파일 5~8: `.claude/hooks/moai/handle-*.sh` (프로젝트, 4개)

**대상 파일**:
- `handle-user-prompt-submit.sh`
- `handle-post-tool.sh`
- `handle-stop.sh`
- `handle-task-completed.sh`

**역할**: Claude Code 훅 이벤트를 받아 `moai hook` 명령어로 전달하는 래퍼 스크립트.

**수정 내용**: 아래 3줄을 **제거**했다 (4개 파일 모두 동일 패턴).

```bash
# --- 제거된 내용 (각 파일에서 동일) ---
# --- Dashboard: observe_cli.mjs (background, non-blocking) ---
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
node "$SCRIPT_DIR/observe_cli.mjs" --project moai --hook {HookName} &>/dev/null &
```

**키포인트**: 이 줄들은 원래 observe_cli.mjs를 프로젝트 레벨에서 백그라운드로 실행하던 코드였다. 이제 전역 훅에서 observe를 처리하므로 제거함. 남은 코드는 순수하게 `moai hook` 전달만 수행.

**수정 후 구조** (4개 파일 모두 동일 패턴):

```bash
#!/bin/bash
temp_file=$(mktemp)
trap 'rm -f "$temp_file"' EXIT
cat > "$temp_file"

# moai hook으로 전달 (3개 경로 시도)
if command -v moai &> /dev/null; then
    exec moai hook {event-name} < "$temp_file"
fi
if [ -f "C:/Users/gosungho/go/bin/moai" ]; then
    exec "C:/Users/gosungho/go/bin/moai" hook {event-name} < "$temp_file"
fi
exit 0
```

**롤백 방법**: git에서 각 파일 복원 (`git checkout -- .claude/hooks/moai/handle-*.sh`).

---

### 파일 8: `.claude/hooks/moai/observe_cli.mjs` (프로젝트) → 삭제됨

**역할**: 프로젝트 레벨의 observe_cli.mjs 복사본이었음.

**키포인트**: 전역에 통합했으므로 이 파일은 삭제됨. 최종 상태에서 이 파일은 **존재하지 않음**.

---

## 4. agents-observe 서버 설치 (WSL2 Docker)

대시보드 서버는 WSL2의 Docker에서 실행 중.

### WSL2 네트워크 설정 (필수 - 1회)

Windows에서 `C:\Users\gosungho\.wslconfig` 생성:

```ini
[wsl2]
networkingMode=mirrored
```

이후 PowerShell에서 `wsl --shutdown` 실행. 이 설정으로 WSL2 Docker 포트가 Windows `localhost`로 자동 노출된다. 미설정 시 WSL2 IP(172.19.x.x)가 재시작마다 바뀌어 매번 수정해야 함.

### 서버 실행

```bash
# WSL2에서 실행
git clone https://github.com/simple10/agents-observe.git
cd agents-observe
docker compose up -d
```

별도 설정 변경 없이 기본값으로 사용 중.

| 항목 | 값 |
|------|-----|
| 접속 URL | `http://localhost:4981` |
| API 엔드포인트 | `http://localhost:4981/api/events` |
| 기술 스택 | Hono(Node.js) + SQLite + React 19 + WebSocket |
| 네트워크 | WSL2 mirrored mode → localhost로 접근 |

---

## 5. 전체 데이터 흐름 요약

```
[사용자가 Claude Code에서 작업]
         │
         ▼
[Claude Code 훅 이벤트 발생]
    예: PostToolUse (Edit 도구로 파일 수정 완료)
         │
         ├─────────────────────────────────────────────┐
         │                                             │
         ▼                                             ▼
[전역 훅]                                      [프로젝트 훅]
~/.claude/settings.json                     .claude/settings.json
    "PostToolUse" 항목                          "PostToolUse" 항목
         │                                             │
         ▼                                             ▼
observe_cli.mjs                              handle-post-tool.sh
(~/.claude/hooks/)                           (.claude/hooks/moai/)
         │                                             │
         ▼                                             ▼
[프로젝트명 결정: NIA]                        [moai hook post-tool]
[이벤트 분류: tool_use]                       (MoAI 내부 워크플로우)
[메타데이터 추출]
[세션 통계 갱신]
         │
         ▼
HTTP POST → localhost:4981/api/events
         │
         ▼
[agents-observe 서버]
    SQLite 저장 + WebSocket 전송
         │
         ▼
[React 대시보드에서 실시간 확인]
```

---

## 6. 대시보드에서 확인 가능한 정보

| 카테고리 | 수집 이벤트 | 볼 수 있는 것 |
|----------|------------|--------------|
| 도구 활동 | PostToolUse, PostToolUseFailure | 어떤 도구가 어떤 파일을 수정했는지, Bash 명령어, 에러 |
| 프롬프트 | UserPromptSubmit | 사용자 입력 길이, 미리보기(150자) |
| 에이전트 | SubagentStart, SubagentStop | 에이전트 타입/이름, 실행 시간(초), 마지막 응답 미리보기 |
| 태스크 | TaskCompleted | 태스크 ID, 요약, 담당 에이전트 |
| 세션 | Stop | 세션 종료 시 누적 통계 (도구 호출수, 프롬프트수, 에이전트수, 에러수) |
| 알림 | Notification | 알림 타입, 메시지 |
| 토큰 | 전 이벤트 | 근사치 (문자수 / 3.5). 실제 토큰은 Claude Code가 제공하지 않음 |

---

## 7. 최종 파일 구조

```
C:\Users\gosungho\
  .wslconfig                                  ⚠️ 전역 (git 미관리) - WSL2 mirrored networking
  .claude\                                    ⚠️ 전역 (git 미관리)
    settings.json                             ← hooks 섹션 추가됨 (8개 이벤트)
    hooks\
      observe_cli.mjs                         ← 핵심 스크립트 (신규)
      observe_projects.json                   ← 프로젝트 이름 매핑 (신규)

D:\01_source_code\0. nia_source\             ✅ 프로젝트 (git 관리)
  .claude\hooks\moai\
    handle-user-prompt-submit.sh              ← observe 호출 제거됨
    handle-post-tool.sh                       ← observe 호출 제거됨
    handle-stop.sh                            ← observe 호출 제거됨
    handle-task-completed.sh                  ← observe 호출 제거됨

WSL2 Docker
    agents-observe 컨테이너                   ← localhost:4981

D:\claude_hook_debug.log                      디버그 로그
```

---

## 8. 롤백 가이드

### agents-observe 연동을 완전히 제거하려면:

```
1. 전역 settings.json에서 "hooks" 키 전체 삭제
   파일: C:\Users\gosungho\.claude\settings.json

2. 전역 hooks 폴더의 observe 파일 삭제
   삭제: C:\Users\gosungho\.claude\hooks\observe_cli.mjs
   삭제: C:\Users\gosungho\.claude\hooks\observe_projects.json

3. .wslconfig 삭제 (mirrored networking 해제)
   삭제: C:\Users\gosungho\.wslconfig
   이후: wsl --shutdown

4. 프로젝트 .sh 파일 복원 (git으로)
   git checkout -- .claude/hooks/moai/handle-user-prompt-submit.sh
   git checkout -- .claude/hooks/moai/handle-post-tool.sh
   git checkout -- .claude/hooks/moai/handle-stop.sh
   git checkout -- .claude/hooks/moai/handle-task-completed.sh

5. (선택) Docker 컨테이너 중지
   WSL2에서: cd agents-observe && docker compose down

6. (선택) 로그 파일 삭제
   삭제: D:\claude_hook_debug.log
```

---

## 9. 알려진 제한사항

| 항목 | 내용 |
|------|------|
| WSL2 네트워크 | mirrored mode 설정 필요 (`C:\Users\gosungho\.wslconfig`에 `networkingMode=mirrored`). 미설정 시 WSL2 IP가 재시작마다 변경됨 |
| agents-observe 설정 | git clone + docker compose up -d 로만 설치. 서버 설정은 변경하지 않은 기본값 사용 |
| 데이터 포맷 | agents-observe 서버가 기대하는 envelope 포맷(`hook_payload` + `meta`)을 정확히 맞춰야 필터/아이콘이 작동함. Claude Code의 camelCase 필드를 snake_case로 변환 필요 |
