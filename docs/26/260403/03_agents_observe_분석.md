# agents-observe 프로그램 분석

## 개요

**agents-observe**는 Claude Code 에이전트의 실시간 관찰 대시보드이다. Claude Code의 훅 시스템을 활용하여 에이전트 세션의 전체 데이터를 캡처하고, 필터링/검색/시각화 기능을 제공한다.

- GitHub: https://github.com/simple10/agents-observe
- 역할: Claude Code 에이전트 활동의 실시간 모니터링 및 분석

## 아키텍처

```
Claude Code Hooks ──→ observe_cli.mjs (훅 스크립트)
                          │
                          ▼ HTTP POST
                     Hono Server (Node.js)
                          │
                    ┌─────┴─────┐
                    ▼           ▼
                 SQLite    WebSocket
                 (저장)    (실시간 전송)
                              │
                              ▼
                     React 19 Dashboard
                     (shadcn/ui 컴포넌트)
```

| 계층 | 기술 |
|------|------|
| Frontend | React 19 + shadcn UI |
| Backend | Node.js + Hono 프레임워크 |
| Database | SQLite (영구 저장) |
| 실시간 통신 | WebSocket |
| 배포 | Docker + docker-compose |

## API

### 이벤트 전송

```
POST http://{host}:4981/api/events
Content-Type: application/json
```

요청 본문:
```json
{
  "project": "moai",
  "timestamp": "2026-04-03T00:00:00.000Z",
  "type": "hook",
  "payload": {
    "hook": "PostToolUse",
    "toolName": "Edit",
    "source": "moai-hook",
    "status": "fired"
  }
}
```

응답: `201 Created`

### 핵심 기능

- **양방향 통신**: 서버가 추가 데이터 요청 가능 (예: 세션 slug)
- **WebSocket 구독**: 브라우저 탭별로 해당 세션 이벤트만 수신
- **자동 재연결**: WebSocket 끊김 시 3초마다 재연결 시도

## 대시보드 기능

### 실시간 시각화
- 이벤트 스트리밍 (발생 즉시 표시)
- PreToolUse → PostToolUse 병합 표시 (도구 실행 과정 추적)
- 에이전트 계층 구조 (부모-자식 관계) 시각화

### 필터링 및 검색
- 에이전트별, 도구 타입별 필터
- 전체 텍스트 검색
- 타임라인 아이콘 클릭으로 특정 이벤트 점프

### 세션 관리
- 과거 세션 브라우징 (human-readable 이름: "twinkly-hugging-dragon")
- 활성/종료 세션 상태 추적
- 프로젝트별 다중 세션 지원

## 설정 방법

### 환경 변수

| 변수 | 용도 | 기본값 |
|------|------|--------|
| `AGENTS_OBSERVE_PROJECT_SLUG` | 프로젝트 식별자 | 자동 감지 |
| `AGENTS_OBSERVE_API_BASE_URL` | API 엔드포인트 | `http://127.0.0.1:4981/api` |

### 설치 방법

**방법 1: 플러그인 (권장)**
```bash
claude plugin marketplace add simple10/agents-observe
claude plugin install agents-observe
```

**방법 2: 수동 설치**
```bash
git clone https://github.com/simple10/agents-observe.git
cd agents-observe
just setup-hooks <project-name>  # 훅 JSON 생성
just start                       # Docker 서버 시작
```

### 테스트
```bash
just test-event    # 테스트 이벤트 전송
just health        # 서버 상태 확인
```

## 활용 추천

### 현재 상태 (기본 연동)

현재 observe_cli.mjs는 **최소한의 데이터**만 전송 중:
- 훅 이름 (어떤 이벤트인지)
- 기본 메타데이터 (toolName, agentType 등)

### 추천 1: 공식 훅 스크립트 활용

agents-observe는 자체 훅 스크립트를 제공한다. `just setup-hooks moai`로 생성되는 공식 스크립트는:
- Claude Code의 모든 stdin 이벤트 데이터를 그대로 전달
- 서버가 요청하는 추가 정보(세션 transcript)도 자동 응답
- 에이전트 부모-자식 관계, 도구 실행 결과 등 풍부한 데이터

**적용 방법**: WSL2에서 `just setup-hooks moai` 실행 후, 생성된 훅 스크립트를 현재 observe_cli.mjs 대신 사용

### 추천 2: 의미 있는 이벤트 타입 전송

현재 `type: 'hook'`으로 단일 타입만 보내고 있다. 다음처럼 분류하면 대시보드에서 더 유용하다:

| type 값 | 전송 시점 | 포함 데이터 |
|---------|----------|------------|
| `tool_use` | PostToolUse | toolName, 파일 경로, 변경 라인 수 |
| `prompt` | UserPromptSubmit | 프롬프트 길이, 프롬프트 요약 |
| `agent_lifecycle` | SubagentStart/Stop | agentType, agentName, 실행 시간 |
| `task` | TaskCompleted | taskId, taskSummary, 소요 시간 |
| `session` | Stop/SessionEnd | 세션 요약, 총 도구 사용 횟수 |

### 추천 3: 전체 이벤트 포워딩 (장기)

가장 이상적인 구조는 observe_cli.mjs가 stdin 전체를 그대로 서버에 포워딩하는 것:

```javascript
// stdin 전체를 서버에 전달 (서버가 파싱 담당)
const data = JSON.stringify({
  project: 'moai',
  timestamp: new Date().toISOString(),
  type: hookName,
  payload: parsedStdin  // Claude Code 이벤트 전체 데이터
});
```

이렇게 하면 대시보드에서 도구 실행 결과, Bash 명령어 출력, 에이전트 계층 구조 등 모든 정보를 시각화할 수 있다.

## 참고

- 대시보드 접속: `http://localhost:4981` (WSL2 Docker)
- 로그 파일: `D:/claude_hook_debug.log`
- SQLite DB: Docker 컨테이너 내부 (영구 저장, 세션 이력 분석 가능)
