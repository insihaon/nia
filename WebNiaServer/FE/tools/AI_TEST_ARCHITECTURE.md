# AI 기반 자동 테스트 시스템 아키텍처 (PoC)

## 🎯 목표

**AI가 DOM 스냅샷을 분석하여 Smoke Test 시나리오를 자동 생성하고, 사람이 승인한 후 자동 실행하는 시스템**

### 핵심 가치
- **테스트 작성 부담 감소**: AI가 테스트 시나리오를 자동 생성
- **승인형 프로세스**: AI 오판 리스크 통제
- **DOM 기반**: 스크린샷 비전 없이도 충분히 동작
- **증거 수집**: 실패 시 DOM/로그/스크린샷 자동 저장

---

## 🏗️ 전체 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│              AI Test Agent (PoC)                             │
│                                                               │
│  [1] DOM 수집 → [2] LLM 분석 → [3] 시나리오 생성            │
│         ↓              ↓              ↓                       │
│  ┌──────────────────────────────────────────────────────┐   │
│  │         Evidence Collector (기존 시스템)             │   │
│  │         (DOM 스냅샷 수집)                            │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                               │
│  [4] 사람 승인 → [5] Playwright 실행 → [6] 증거 수집        │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 📋 핵심 프로세스

### 1단계: DOM 스냅샷 수집
**역할**: 기존 Collector를 활용하여 DOM 스냅샷 수집

**입력**:
- Vue 앱 접속 (Playwright)
- `window.__emitEvidence('dom_snapshot')` 호출

**출력**:
- Collector에 저장된 DOM 스냅샷 JSON
- 파일 위치: `tools/ui-evidence-collector/sessions/{sessionId}/*.json`

**기존 시스템 활용**:
```javascript
// uiEvidence.js가 이미 DOM 스냅샷 수집 가능
function getDomSnapshot() {
  return {
    html: document.documentElement.outerHTML.substring(0, 100000),
    title: document.title,
    url: window.location.href,
    viewport: { width: window.innerWidth, height: window.innerHeight }
  }
}
```

---

### 2단계: DOM 분석 및 구조화
**역할**: DOM 스냅샷에서 테스트 가능한 요소 추출

**기능**:
- 버튼, 링크, 입력 필드 식별
- 폼 요소 식별
- 선택자(selector) 생성
- 요소 텍스트/라벨 추출

**출력 예시**:
```json
{
  "pageTitle": "대시보드",
  "url": "/dashboard",
  "interactiveElements": [
    {
      "type": "button",
      "text": "검색",
      "selector": "button.search-btn",
      "action": "click",
      "priority": "high"
    },
    {
      "type": "input",
      "label": "사용자명",
      "selector": "input[name='username']",
      "action": "fill",
      "priority": "medium"
    },
    {
      "type": "link",
      "text": "상세보기",
      "selector": "a.detail-link",
      "action": "click",
      "priority": "low"
    }
  ],
  "forms": [
    {
      "selector": "form#search-form",
      "fields": ["input[name='keyword']", "select[name='category']"]
    }
  ]
}
```

---

### 3단계: LLM 기반 Smoke Test 시나리오 생성
**역할**: LLM에게 DOM 분석 결과를 전달하여 최소한의 Smoke Test 시나리오 생성

**LLM 프롬프트 예시**:
```
다음은 웹 애플리케이션의 DOM 스냅샷 분석 결과입니다:

페이지 정보:
- 제목: 대시보드
- URL: /dashboard

인터랙티브 요소:
1. 버튼: "검색" (selector: button.search-btn)
2. 입력 필드: "사용자명" (selector: input[name='username'])
3. 링크: "상세보기" (selector: a.detail-link)

이 화면에서 **최소한으로 검증해야 할 Smoke Test 시나리오**를 JSON 형식으로 작성해주세요.
다음 형식을 정확히 따르세요:

{
  "scenarios": [
    {
      "id": "smoke_1",
      "description": "검색 버튼이 존재하고 클릭 가능한지 확인",
      "priority": "high",
      "steps": [
        {
          "action": "assertVisible",
          "target": "button.search-btn",
          "description": "검색 버튼이 표시되는지 확인"
        },
        {
          "action": "click",
          "target": "button.search-btn",
          "description": "검색 버튼 클릭"
        },
        {
          "action": "wait",
          "duration": 1000,
          "description": "응답 대기"
        }
      ]
    }
  ]
}

주의사항:
- Smoke Test이므로 핵심 기능만 테스트
- 복잡한 시나리오보다는 "요소 존재/표시/클릭 가능" 같은 기본 검증에 집중
- 각 시나리오는 3-5단계 이내로 간단하게
```

**LLM 응답 예시**:
```json
{
  "scenarios": [
    {
      "id": "smoke_1",
      "description": "검색 버튼 존재 및 클릭 가능 확인",
      "priority": "high",
      "steps": [
        {
          "action": "assertVisible",
          "target": "button.search-btn",
          "description": "검색 버튼이 표시되는지 확인"
        },
        {
          "action": "click",
          "target": "button.search-btn",
          "description": "검색 버튼 클릭"
        }
      ]
    },
    {
      "id": "smoke_2",
      "description": "사용자명 입력 필드 존재 확인",
      "priority": "medium",
      "steps": [
        {
          "action": "assertVisible",
          "target": "input[name='username']",
          "description": "사용자명 입력 필드가 표시되는지 확인"
        },
        {
          "action": "fill",
          "target": "input[name='username']",
          "value": "test_user",
          "description": "테스트 사용자명 입력"
        }
      ]
    }
  ]
}
```

---

### 4단계: 사람 승인 (Approval)
**역할**: 생성된 시나리오를 사람이 검토하고 승인

**프로세스**:
1. 생성된 시나리오를 JSON 파일로 저장
2. 사람이 파일을 열어 검토
3. 승인/수정/거부 결정
4. 승인된 시나리오만 다음 단계로 진행

**승인 파일 형식**:
```json
{
  "sessionId": "test_1234567890",
  "pageUrl": "/dashboard",
  "generatedAt": "2024-01-15T10:30:00Z",
  "scenarios": [
    {
      "id": "smoke_1",
      "approved": true,
      "modified": false,
      "steps": [...]
    },
    {
      "id": "smoke_2",
      "approved": false,
      "reason": "불필요한 테스트"
    }
  ]
}
```

**CLI 인터페이스 예시**:
```bash
$ npm run test:ai:generate
# → 시나리오 생성 완료: scenarios/test_1234567890.json

$ npm run test:ai:approve scenarios/test_1234567890.json
# → 승인 완료. 실행하시겠습니까? (y/n)
```

---

### 5단계: Playwright 실행
**역할**: 승인된 시나리오를 Playwright로 실행

**지원 액션**:
- `click`: 요소 클릭
- `fill`: 입력 필드에 값 입력
- `select`: 드롭다운 선택
- `assertVisible`: 요소가 표시되는지 확인
- `assertText`: 요소의 텍스트 확인
- `wait`: 대기
- `navigate`: 페이지 이동

**실행 예시**:
```typescript
async function executeScenario(page: Page, scenario: Scenario) {
  for (const step of scenario.steps) {
    switch (step.action) {
      case 'click':
        await page.click(step.target)
        await collectEvidence(`click_${step.target}`)
        break

      case 'fill':
        await page.fill(step.target, step.value)
        await collectEvidence(`fill_${step.target}`)
        break

      case 'assertVisible':
        const isVisible = await page.locator(step.target).isVisible()
        if (!isVisible) {
          throw new Error(`Element not visible: ${step.target}`)
        }
        break

      case 'wait':
        await page.waitForTimeout(step.duration || 1000)
        break
    }
  }
}
```

---

### 6단계: 증거 수집
**역할**: 테스트 실행 중/실패 시 증거 자동 수집

**수집 항목**:
- DOM 스냅샷 (각 단계마다)
- 스크린샷 (실패 시)
- Console 로그
- 에러 정보
- 네트워크 요청/응답 (선택)

**저장 위치**:
```
tools/ui-evidence-collector/
└── sessions/
    └── {sessionId}/
        ├── {timestamp}_dom_snapshot.json
        ├── {timestamp}_click_button.search-btn.json
        ├── {timestamp}_test_failed.json
        └── screenshots/
            └── {timestamp}_failure.png
```

---

## 🔄 전체 실행 흐름

```
[1] Playwright로 Vue 앱 접속
    ↓
[2] DOM 스냅샷 수집 요청
    → window.__emitEvidence('dom_snapshot')
    → Collector에 저장
    ↓
[3] DOM 분석
    → DOM 스냅샷 파일 읽기
    → 인터랙티브 요소 추출
    → 구조화된 JSON 생성
    ↓
[4] LLM 시나리오 생성
    → DOM 분석 결과를 LLM에 전달
    → Smoke Test 시나리오 생성
    → JSON 파일로 저장
    ↓
[5] 사람 승인 (수동)
    → 생성된 시나리오 파일 검토
    → 승인/수정/거부 결정
    ↓
[6] Playwright 실행
    → 승인된 시나리오 실행
    → 각 단계마다 증거 수집
    ↓
[7] 결과 저장
    → 성공/실패 결과
    → 실패 시 스크린샷/DOM/로그 저장
```

---

## 📁 폴더 구조

```
tools/
├── ui-evidence-collector/        (기존 - DOM 수집)
│   └── sessions/
│       └── {sessionId}/
│           └── *.json
│
├── ui-agent-playwright/          (기존 - 테스트 실행)
│   └── tests/
│       └── ui.spec.ts
│
└── ai-test-agent/                (신규 - PoC)
    ├── package.json
    ├── src/
    │   ├── analyzers/
    │   │   └── domAnalyzer.ts         # DOM 분석 (요소 추출)
    │   │
    │   ├── planners/
    │   │   └── llmPlanner.ts          # LLM 시나리오 생성
    │   │
    │   ├── executors/
    │   │   └── playwrightExecutor.ts  # Playwright 실행
    │   │
    │   ├── llm/
    │   │   ├── openai.ts              # OpenAI 통합
    │   │   ├── anthropic.ts           # Claude 통합
    │   │   └── ollama.ts              # 로컬 LLM 통합
    │   │
    │   ├── utils/
    │   │   ├── scenarioParser.ts      # 시나리오 파싱
    │   │   └── approvalHandler.ts     # 승인 처리
    │   │
    │   └── main.ts                    # 메인 실행
    │
    ├── scenarios/                    # 생성된 시나리오 저장
    │   └── {sessionId}.json
    │
    └── tests/
        └── ai-smoke-test.spec.ts     # 통합 테스트
```

---

## 🤖 LLM 통합 방안

### 옵션 1: OpenAI GPT-4 (추천)
**장점**: 높은 정확도, 구조화된 출력
**비용**: API 사용료 발생

```typescript
import OpenAI from 'openai'

const openai = new OpenAI({ apiKey: process.env.OPENAI_API_KEY })

async function generateScenario(domAnalysis: any) {
  const response = await openai.chat.completions.create({
    model: "gpt-4",
    messages: [{
      role: "system",
      content: "당신은 웹 애플리케이션 테스트 시나리오를 작성하는 전문가입니다. DOM 분석 결과를 바탕으로 Smoke Test 시나리오를 JSON 형식으로 작성해주세요."
    }, {
      role: "user",
      content: `DOM 분석 결과:\n${JSON.stringify(domAnalysis, null, 2)}`
    }],
    response_format: { type: "json_object" }
  })

  return JSON.parse(response.choices[0].message.content)
}
```

### 옵션 2: Claude (Anthropic)
**장점**: 긴 컨텍스트, 구조화된 출력
**비용**: API 사용료 발생

```typescript
import Anthropic from '@anthropic-ai/sdk'

const anthropic = new Anthropic({ apiKey: process.env.ANTHROPIC_API_KEY })

async function generateScenario(domAnalysis: any) {
  const message = await anthropic.messages.create({
    model: "claude-3-sonnet-20240229",
    max_tokens: 4096,
    messages: [{
      role: "user",
      content: `DOM 분석 결과를 바탕으로 Smoke Test 시나리오를 JSON 형식으로 작성해주세요:\n${JSON.stringify(domAnalysis, null, 2)}`
    }]
  })

  return JSON.parse(message.content[0].text)
}
```

### 옵션 3: 로컬 LLM (Ollama) - 비용 없음
**장점**: 비용 없음, 프라이버시 보장
**단점**: 정확도가 상대적으로 낮을 수 있음

```typescript
async function generateScenarioWithOllama(domAnalysis: any) {
  const response = await fetch('http://localhost:11434/api/generate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      model: 'llama2',
      prompt: `DOM 분석 결과를 바탕으로 Smoke Test 시나리오를 JSON 형식으로 작성해주세요:\n${JSON.stringify(domAnalysis, null, 2)}`,
      stream: false
    })
  })

  const data = await response.json()
  return JSON.parse(data.response)
}
```

---

## ✅ PoC 성공 조건

### 데모 기준
1. **특정 화면 1~3개**에서 동작
2. **버튼 클릭** / **입력** / **간단한 이동** 테스트
3. **"요소 존재/표시됨"** 같은 가벼운 Assertion
4. **실패 시 스크린샷/DOM/로그**가 증거로 남음

### 성공 지표
- ✅ AI가 테스트 시나리오를 자동 생성
- ✅ 사람이 1번 승인하면 자동 실행
- ✅ 테스트 작성 부담이 줄어든다는 메시지 전달
- ✅ 실패 시 증거 자동 수집

---

## 🎯 구현 단계

### Phase 1: 기본 구조 (1주)
1. **DOM Analyzer 구현**
   - Collector에서 DOM 스냅샷 읽기
   - 인터랙티브 요소 추출
   - 선택자 생성

2. **Playwright Executor 구현**
   - 시나리오 JSON 파싱
   - 기본 액션 실행 (click, fill, assertVisible)
   - 증거 수집 연동

3. **수동 시나리오로 검증**
   - JSON 파일을 직접 작성하여 실행 테스트

---

### Phase 2: LLM 통합 (1주)
1. **LLM API 통합**
   - OpenAI 또는 Claude 선택
   - API 키 설정

2. **LLM Planner 구현**
   - DOM 분석 결과를 LLM에 전달
   - 시나리오 생성 프롬프트 작성
   - JSON 응답 파싱

3. **프롬프트 엔지니어링**
   - Smoke Test에 집중하도록 프롬프트 최적화
   - 출력 형식 명확히 지정

---

### Phase 3: 승인 프로세스 (3일)
1. **승인 시스템 구현**
   - 생성된 시나리오 파일 저장
   - CLI로 승인/수정/거부 처리
   - 승인된 시나리오만 실행

2. **통합 테스트**
   - 전체 플로우 테스트
   - 실제 화면에서 검증

---

### Phase 4: 개선 및 문서화 (3일)
1. **에러 처리 개선**
   - LLM 응답 파싱 실패 처리
   - 선택자 오류 처리

2. **문서화**
   - 사용 가이드 작성
   - 예시 시나리오 제공

---

## 💡 핵심 아이디어 요약

1. **DOM 기반**: 스크린샷 비전 없이 DOM만으로 충분
2. **Smoke Test 중심**: 복잡한 시나리오보다 기본 검증에 집중
3. **승인형 프로세스**: AI 오판 리스크 통제
4. **기존 시스템 활용**: Collector의 DOM 수집 기능 재사용
5. **증거 자동 수집**: 실패 시 자동으로 증거 저장

---

## 🔧 기술 스택

- **DOM 분석**: Playwright + DOM Parser
- **LLM**: OpenAI GPT-4 / Claude 3 / 로컬 LLM (Ollama)
- **테스트 실행**: Playwright (기존)
- **증거 수집**: 기존 Collector 활용
- **언어**: TypeScript

---

## 📊 예상 효과

- **테스트 작성 시간**: 70-80% 감소 (수동 작성 → AI 생성 + 승인)
- **테스트 커버리지**: Smoke Test 자동 생성으로 기본 검증 보장
- **유지보수**: 화면 변경 시 재분석만 하면 됨
- **비용**: LLM API 사용료 (로컬 LLM 사용 시 무료)

---

## ⚠️ 고려사항

1. **비용**: LLM API 사용료 (로컬 LLM 사용 시 무료)
2. **정확도**: LLM의 선택자 생성이 정확하지 않을 수 있음 → 승인 프로세스로 통제
3. **속도**: LLM 호출로 인한 지연 (수 초)
4. **프라이버시**: 외부 API 사용 시 DOM 데이터 전송

---

## 🚀 다음 단계

1. **LLM 선택**: OpenAI / Claude / 로컬 LLM 중 결정
2. **DOM Analyzer 프로토타입**: Collector 연동 테스트
3. **Playwright Executor 기본 구현**: 수동 시나리오로 검증
4. **LLM 통합**: 간단한 프롬프트로 시나리오 생성 테스트

---

## 📝 참고사항

- **기존 시스템 활용**: `ui-evidence-collector`의 DOM 수집 기능 재사용
- **단순함 유지**: PoC이므로 복잡한 기능보다는 핵심 기능에 집중
- **확장 가능성**: PoC 성공 후 Vision API, 자동 승인 등 고도화 가능
