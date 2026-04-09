# UI 자동점검 PoC 아키텍처 설명

## 전체 구조

```
┌─────────────────┐
│  Vue 앱         │
│  (포트 4000)    │
│                 │
│  - 플러그인     │◄────┐
│    (uiEvidence) │     │ Socket.IO
└─────────────────┘     │ (실시간 통신)
                        │
┌─────────────────┐     │
│  Collector      │◄────┘
│  (포트 3100)    │
│                 │
│  - 증거 수집    │
│  - 파일 저장   │
└─────────────────┘
         ▲
         │
         │ HTTP 요청
         │ (브라우저 자동화)
         │
┌─────────────────┐
│  Playwright     │
│  Agent          │
│                 │
│  - 테스트 실행  │
│  - 스크린샷     │
└─────────────────┘
```

---

## 1. ui-evidence-collector (증거 수집 서버)

### 🎯 목표 달성 목적

**Vue 앱에서 발생하는 모든 증거를 실시간으로 수집하고 파일로 저장하는 서버**

- **증거 수집**: DOM 스냅샷, UI 상태, Console 로그, 에러 정보를 실시간으로 수집
- **파일 저장**: 세션별로 증거를 JSON 파일로 저장하여 나중에 분석 가능
- **세션 관리**: 각 테스트 세션별로 증거를 분리하여 저장

### 📦 설치된 라이브러리

#### 1. **socket.io** (^4.5.4)
- **역할**: 실시간 양방향 통신 (WebSocket 기반)
- **사용 목적**: Vue 앱과 Collector 서버 간 실시간 증거 전송
- **특징**:
  - 이벤트 기반 통신 (`ui:evidence` 이벤트)
  - ACK(응답 확인) 지원
  - 자동 재연결 기능

#### 2. **express** (^4.18.2)
- **역할**: Node.js 웹 프레임워크
- **사용 목적**: HTTP 서버 구축 및 REST API 제공
- **제공 기능**:
  - Health check endpoint (`/health`)
  - 세션 목록 조회 (`/sessions`)
  - 특정 세션 증거 조회 (`/sessions/:sessionId`)

#### 3. **cors** (^2.8.5)
- **역할**: Cross-Origin Resource Sharing 처리
- **사용 목적**: Vue 앱(포트 4000)에서 Collector(포트 3100)로의 요청 허용
- **필요성**: 다른 포트 간 통신을 위한 CORS 설정

#### 4. **fs-extra** (^11.1.1)
- **역할**: 파일 시스템 확장 유틸리티
- **사용 목적**: 증거 파일 저장 및 디렉토리 관리
- **제공 기능**:
  - `ensureDirSync()`: 디렉토리 자동 생성
  - `writeJson()`: JSON 파일 저장
  - `readdirSync()`: 디렉토리 읽기

### 🔄 동작 흐름

1. **서버 시작**: Express + Socket.IO 서버가 포트 3100에서 대기
2. **Vue 앱 연결**: Vue 앱의 플러그인이 Socket.IO로 연결
3. **증거 수신**: `ui:evidence` 이벤트로 증거 데이터 수신
4. **파일 저장**: `sessions/{sessionId}/{timestamp}_{stepName}.json` 형식으로 저장
5. **ACK 전송**: 저장 완료 후 `ui:evidence:ack` 이벤트로 응답

### 📁 저장 구조

```
tools/ui-evidence-collector/
└── sessions/
    └── {sessionId}/
        ├── {timestamp}_after_first_render.json
        ├── {timestamp}_after_body_check.json
        └── {timestamp}_route_change.json
```

---

## 2. ui-agent-playwright (자동화 테스트 에이전트)

### 🎯 목표 달성 목적

**브라우저를 자동으로 제어하여 Vue 앱을 테스트하고 스크린샷을 저장하는 에이전트**

- **브라우저 자동화**: 실제 브라우저를 열고 페이지를 조작
- **테스트 실행**: 미리 작성된 테스트 시나리오 실행
- **증거 트리거**: Vue 앱에 증거 수집을 요청 (`window.__emitEvidence` 호출)
- **스크린샷 저장**: 테스트 중 특정 시점의 화면을 이미지로 저장

### 📦 설치된 라이브러리

#### 1. **@playwright/test** (^1.40.0)
- **역할**: E2E(End-to-End) 테스트 프레임워크
- **사용 목적**: 브라우저 자동화 및 테스트 실행
- **주요 기능**:
  - **브라우저 제어**: Chromium, Firefox, WebKit 지원
  - **페이지 조작**: 클릭, 입력, 네비게이션 등
  - **대기 기능**: 요소 로드, 네트워크 대기 등
  - **스크린샷**: 페이지 캡처
  - **테스트 실행**: 테스트 케이스 작성 및 실행
  - **리포팅**: HTML 리포트 생성

#### 2. **@types/node** (^20.10.0)
- **역할**: TypeScript 타입 정의
- **사용 목적**: Node.js API의 TypeScript 타입 지원
- **필요성**: `playwright.config.ts`와 `tests/ui.spec.ts`가 TypeScript로 작성되어 있음

### 🔄 동작 흐름

1. **테스트 시작**: Playwright가 Chromium 브라우저 실행
2. **페이지 접속**: Vue 앱 URL에 세션 ID를 포함하여 접속 (`?ag_session=test_xxx`)
3. **페이지 로드 대기**: `waitForLoadState('networkidle')`로 완전 로드 대기
4. **증거 수집 트리거**: `page.evaluate()`로 `window.__emitEvidence('after_first_render')` 호출
5. **테스트 수행**: 요소 확인, 클릭 등 테스트 시나리오 실행
6. **스크린샷 저장**: 특정 시점의 화면을 이미지로 저장
7. **리포트 생성**: 테스트 결과를 HTML 리포트로 생성

### 📁 저장 구조

```
tools/ui-agent-playwright/
├── test-artifacts/
│   ├── {sessionId}_initial.png
│   ├── {sessionId}_final.png
│   └── {sessionId}_error.png
└── playwright-report/
    └── index.html (테스트 리포트)
```

### 🧪 테스트 케이스

현재 구현된 테스트:
1. **should collect evidence from Vue app**
   - Vue 앱 접속
   - `after_first_render` 증거 수집
   - `after_body_check` 증거 수집
   - 스크린샷 저장

2. **should handle custom session ID**
   - 커스텀 세션 ID로 접속
   - 세션 ID 확인
   - 스크린샷 저장

---

## 전체 시스템 동작 흐름

```
[1] Collector 서버 시작 (포트 3100)
    ↓
[2] Vue 앱 개발 서버 시작 (포트 4000)
    ↓ 플러그인 초기화 → Socket.IO 연결
[3] Playwright Agent 시작
    ↓
[4] Playwright가 브라우저 열기
    ↓
[5] Playwright가 Vue 앱 접속 (?ag_session=test_xxx)
    ↓
[6] Vue 앱에서 sessionId 감지
    ↓
[7] Playwright가 window.__emitEvidence('after_first_render') 호출
    ↓
[8] Vue 앱에서 Collector로 증거 전송 (Socket.IO)
    ↓
[9] Collector가 증거를 파일로 저장
    ↓
[10] Playwright가 스크린샷 저장
    ↓
[11] 테스트 완료
```

---

## 각 폴더의 역할 요약

| 폴더 | 역할 | 주요 라이브러리 | 출력물 |
|------|------|----------------|--------|
| **ui-evidence-collector** | 증거 수집 서버 | socket.io, express, cors, fs-extra | JSON 증거 파일 |
| **ui-agent-playwright** | 자동화 테스트 | @playwright/test, @types/node | 스크린샷, HTML 리포트 |

---

## 왜 두 개로 분리했나?

1. **관심사 분리**:
   - Collector: 증거 수집 및 저장 (서버)
   - Playwright: 테스트 실행 (클라이언트)

2. **독립적 실행**:
   - Collector는 여러 테스트 세션을 동시에 처리 가능
   - Playwright는 필요할 때만 실행

3. **확장성**:
   - Collector는 다른 테스트 도구와도 연동 가능
   - Playwright는 다른 서버와도 연동 가능

4. **유지보수**:
   - 각각의 목적이 명확하여 수정이 용이
