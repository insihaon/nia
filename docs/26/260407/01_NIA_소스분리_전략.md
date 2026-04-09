# NIA 소스 분리 전략 — Web2024 프로젝트

> 작성일: 2026-04-07
> 목적: Web2024에서 NIA 외 코드(IPMS, DataHub, Demo)를 제거하되, 향후 재결합(merge) 시 충돌을 최소화하는 전략 수립

---

## 1. 현재 구조 요약

Web2024는 **Gradle 멀티모듈 모노레포**로 4개 앱이 공존한다.

```
Web2024/
├── BE/                        # Backend (Java 8, Spring Boot 2.7.17)
│   ├── common-base/           # 공유 기반 (62 files)
│   ├── common-web/            # 공유 웹 인프라 (79 files)
│   ├── common-mq/             # RabbitMQ (5 files)
│   ├── common-ws/             # WebSocket (16 files)
│   ├── app-nia/               # ★ NIA (23 files)
│   ├── app-ipms/              # IPMS (640 files) ← 제거 대상
│   ├── app-dataHub/           # DataHub (8 files) ← 제거 대상
│   └── app-demo/              # Demo (11 files) ← 제거 대상
├── FE/                        # Frontend (Vue 2.6.11)
│   └── src/
│       ├── views-nia/         # ★ NIA 뷰 (89 files)
│       ├── views-ipms/        # IPMS 뷰 (176 files) ← 제거 대상
│       ├── views-dataHub/     # DataHub 뷰 (47 files) ← 제거 대상
│       ├── views-aiTemplate/  # Demo 뷰 (9 files) ← 제거 대상
│       ├── router/            # 앱별 라우터
│       ├── api/               # 앱별 API 모듈
│       ├── store/modules/     # 앱별 Vuex 스토어
│       ├── components/        # 공유 컴포넌트
│       ├── layout/            # 공유 레이아웃
│       └── utils/             # 공유 유틸리티
```

### 파일 규모

| 영역 | NIA (유지) | IPMS (제거) | DataHub (제거) | Demo (제거) | 공유 (유지) |
|------|-----------|------------|---------------|------------|-----------|
| BE app | 23 files | 640 files | 8 files | 11 files | 162 files |
| FE views | 89 files | 176 files | 47 files | 9 files | — |
| FE router | 1 file | 1 file | 1 file | 1 file | 1 file (공유) |
| FE api | nia.js | ipms.js | dataHub.js | — | auth.js, server.js |
| FE store | nia.js | ipms.js | dataHub.js | — | app.js, user.js 등 |

---

## 2. 제거 대상 목록

### 2.1 완전 제거 (디렉토리 단위)

안전하게 통째로 제거 가능한 항목 — 다른 앱과 의존성 없음:

| 구분 | 경로 | 파일 수 | 비고 |
|------|------|--------|------|
| BE | `BE/app-ipms/` | 640 | com.kt.ipms 패키지 전체 |
| BE | `BE/app-dataHub/` | 8 | DataHub 앱 모듈 |
| BE | `BE/app-demo/` | 11 | Demo 앱 모듈 |
| FE | `FE/src/views-ipms/` | 176 | IPMS 뷰 전체 |
| FE | `FE/src/views-dataHub/` | 47 | DataHub 뷰 전체 |
| FE | `FE/src/views-aiTemplate/` | 9 | Demo 뷰 전체 |
| FE | `FE/src/router/ipms/` | 1 | IPMS 라우터 |
| FE | `FE/src/router/dataHub/` | 1 | DataHub 라우터 |
| FE | `FE/src/router/aiTemplate/` | 1 | Demo 라우터 |
| FE | `FE/.env.ipms` | 1 | IPMS 환경변수 |
| FE | `FE/.env.datahub` | 1 | DataHub 환경변수 |
| FE | `FE/.env.aiTemplate` | 1 | Demo 환경변수 |

### 2.2 수정이 필요한 공유 파일 (핵심 충돌 지점)

이 파일들은 여러 앱이 공유하므로 **수정 방식이 merge 충돌의 핵심**이다.

> **2차 분석(260407) 결과**: 실제 코드를 확인하니 충돌 지점이 예상보다 **적다**.
> - `store/index.js`는 `require.context`로 자동 import → **수정 불필요**
> - `api/index.js`는 ipms/dataHub를 import하지 않음 → **수정 불필요**

| 파일 | 수정 내용 | 충돌 위험도 | 비고 |
|------|----------|-----------|------|
| `BE/settings.gradle` | 3줄 제거 (app-ipms, datahub, demo) | ★☆☆ 낮음 | 단순 include 목록 |
| `FE/src/router/index.js` | import 3줄 + switch case 15줄 | ★★★ 높음 | 가장 복잡한 파일 |
| ~~`FE/src/store/index.js`~~ | ~~수정 불필요~~ | — | require.context 자동 로드 |
| ~~`FE/src/api/index.js`~~ | ~~수정 불필요~~ | — | ipms/dataHub 미참조 |
| `FE/src/class/appOptions.js` | projectList 1줄 + dark getter 1줄 | ★★☆ 중간 | |
| `FE/vue.config.js` | getOutputDir() 내 3개 case | ★★☆ 중간 | |
| `FE/package.json` | scripts에서 3개 키 삭제 | ★☆☆ 낮음 | JSON이라 주석 불가 |

**실제 수정 대상: 5개 파일, 약 28줄**

---

## 3. 추천 전략: 주석 비활성화 + 디렉토리 제거

### 핵심 원칙

> **디렉토리는 삭제하되, 공유 파일은 코드를 삭제하지 않고 주석 처리한다.**

이유:
- 디렉토리 삭제는 git에서 **파일 단위 add/delete**로 추적되어 merge 시 충돌이 거의 없음
- 공유 파일의 **라인 삭제**는 원본 저장소에서 해당 라인이 수정되면 **3-way merge 충돌** 발생
- 주석 처리하면 원본의 라인이 그대로 존재하므로 merge 시 **자동 해결 확률**이 높아짐

### 3.1 디렉토리 제거 (충돌 없음)

```bash
# BE — 비NIA 앱 모듈 제거
git rm -r BE/app-ipms/
git rm -r BE/app-dataHub/
git rm -r BE/app-demo/

# FE — 비NIA 뷰 제거
git rm -r FE/src/views-ipms/
git rm -r FE/src/views-dataHub/
git rm -r FE/src/views-aiTemplate/

# FE — 비NIA 라우터 제거
git rm -r FE/src/router/ipms/
git rm -r FE/src/router/dataHub/
git rm -r FE/src/router/aiTemplate/

# FE — 비NIA 환경파일 제거
git rm FE/.env.ipms
git rm FE/.env.datahub
git rm FE/.env.aiTemplate

# FE — 비NIA API/Store 모듈 제거
git rm FE/src/api/ipms.js
git rm FE/src/api/dataHub.js
git rm FE/src/store/modules/ipms.js
git rm FE/src/store/modules/dataHub.js
```

**merge 시 동작**: 원본에서 해당 파일이 수정되었더라도 `deleted by us` 로 표시되며, 재결합 시 원본 파일을 그대로 `git checkout --theirs`로 복원 가능.

### 3.2 공유 파일 — 주석 비활성화 (충돌 최소화)

#### `BE/settings.gradle`

```gradle
rootProject.name = 'web'

include 'common-base'
include 'common-web'
include 'common-mq'
include 'common-ws'
// [NIA-ONLY] include 'app-datahub'
include 'app-nia'
// [NIA-ONLY] include 'app-demo'
// [NIA-ONLY] include 'app-ipms'
```

#### `FE/src/router/index.js`

```javascript
const { niaHome, niaLogin, niaRoute } = require('./nia/index')
// [NIA-ONLY] const { ipmsHome, ipmsLogin, ipmsRoute } = require('./ipms/index')
// [NIA-ONLY] const { dataHubHome, dataHubLogin, dataHubRoute } = require('./dataHub/index')
// [NIA-ONLY] const { aiTemplateHome, aiTemplateLogin, aiTemplateRoute } = require('./aiTemplate/index')

// switch문 내부도 동일하게 주석 처리
switch (project) {
  case 'nia':
    loginView = niaLogin
    projectRoute = niaRoute
    projectHome = niaHome
    break
  // [NIA-ONLY] case 'ipms':
  // [NIA-ONLY]   loginView = ipmsLogin
  // [NIA-ONLY]   projectRoute = ipmsRoute
  // [NIA-ONLY]   projectHome = ipmsHome
  // [NIA-ONLY]   break
  // ... dataHub, aiTemplate 동일
}
```

#### `FE/src/class/appOptions.js`

```javascript
// this._data.projectList = ['datahub', 'nia', 'ipms']  // [NIA-ONLY] 원본
this._data.projectList = ['nia']  // NIA 전용
```

#### `FE/vue.config.js`

```javascript
function getOutputDir() {
  switch (process.env.VUE_APP_PROJECT) {
    // [NIA-ONLY] case 'datahub': return '../BE/app-dataHub/src/main/resources/static'
    case 'nia': return '../BE/app-nia/src/main/resources/static'
    // [NIA-ONLY] case 'ipms': return '../BE/app-ipms/src/main/resources/static'
  }
}
```

#### `FE/package.json`

```json
{
  "scripts": {
    "build:nia": "vue-cli-service build --mode nia"
    // [NIA-ONLY] 아래 스크립트 비활성화됨 — JSON은 주석 불가하므로 키 자체를 제거
    // 원본 스크립트: "build:ipms", "build:datahub", "build:demo"
  }
}
```

> **주의**: `package.json`은 JSON이라 주석이 불가하다. 이 파일만 예외로 키를 삭제하되, 이 문서에 원본 키를 기록해둔다.

---

## 4. `[NIA-ONLY]` 태그 컨벤션

모든 주석에 **`[NIA-ONLY]`** 태그를 사용한다.

```
// [NIA-ONLY] <원본 코드>          ← 주석 처리된 원본 라인
// [NIA-ONLY] 원본: <설명>         ← 삭제된 내용에 대한 메모
```

**재결합 시 복원 방법**:
```bash
# 모든 [NIA-ONLY] 주석 찾기
grep -rn "\[NIA-ONLY\]" FE/src/ BE/

# 주석 해제 (수동 확인 후)
# 각 파일에서 // [NIA-ONLY] 접두사를 제거하고 원본 코드 복원
```

---

## 5. Merge 충돌 시나리오 분석

### 시나리오 A: 원본 저장소에서 IPMS 코드만 수정된 경우

| 영역 | 충돌? | 해결 방법 |
|------|------|----------|
| `BE/app-ipms/` 내 파일 수정 | 없음 | `deleted by us` → 원본 선택하면 복원 |
| `FE/src/views-ipms/` 내 파일 수정 | 없음 | 동일 |
| `FE/src/router/index.js` 수정 | **가능** | 주석 라인과 원본 라인이 다르면 수동 해결 |

### 시나리오 B: 원본에서 공유 모듈(common-*)이 수정된 경우

| 영역 | 충돌? | 해결 방법 |
|------|------|----------|
| `common-base/` 파일 수정 | 없음 | 우리가 수정하지 않으므로 자동 merge |
| `common-web/` 파일 수정 | 없음 | 동일 |

### 시나리오 C: 원본에서 NIA 코드가 수정된 경우

| 영역 | 충돌? | 해결 방법 |
|------|------|----------|
| `BE/app-nia/` 내 파일 수정 | **가능** | 양쪽 수정이면 수동 merge 필요 |
| `FE/src/views-nia/` 내 파일 수정 | **가능** | 동일 |

### 시나리오 D: 원본에서 새로운 앱이 추가된 경우

| 영역 | 충돌? | 해결 방법 |
|------|------|----------|
| `settings.gradle`에 새 include | **낮음** | 주석 라인과 다른 위치에 추가되므로 자동 merge 가능성 높음 |
| `router/index.js`에 새 case 추가 | **중간** | 같은 switch문 내 변경이므로 수동 해결 가능성 |

---

## 6. 재결합(Merge) 절차

향후 원본 저장소와 다시 합칠 때의 단계별 절차:

### Step 1: 원본 변경사항 확인

```bash
# 원본 remote 추가 (아직 안 했다면)
git remote add upstream <원본_저장소_URL>
git fetch upstream

# 원본의 변경 범위 확인
git diff main...upstream/main --stat
```

### Step 2: 임시 브랜치에서 merge 시도

```bash
git checkout -b merge-attempt main
git merge upstream/main --no-commit
```

### Step 3: 충돌 해결

```bash
# 삭제된 디렉토리 파일 → 원본에서 복원
git checkout --theirs BE/app-ipms/ BE/app-dataHub/ BE/app-demo/
git checkout --theirs FE/src/views-ipms/ FE/src/views-dataHub/ FE/src/views-aiTemplate/

# [NIA-ONLY] 주석이 있는 공유 파일 → 수동 해결
# 1. 원본 코드 복원 (주석 해제)
# 2. NIA 쪽에서 추가한 변경사항 유지
# 3. grep "[NIA-ONLY]" 로 남은 태그 확인

# NIA 코드 충돌 → 양쪽 변경사항 수동 merge
```

### Step 4: 검증

```bash
# 빌드 확인
cd BE && ./gradlew clean build
cd FE && npm run build:nia && npm run build:ipms
```

---

## 7. 대안 비교

| 전략 | 장점 | 단점 | merge 난이도 |
|------|------|------|-------------|
| **A. 주석 비활성화 (추천)** | 원본 라인 보존, grep으로 추적 가능 | 주석이 코드에 남음 | ★☆☆ 쉬움 |
| B. 코드 삭제 | 깔끔한 코드베이스 | merge 시 라인 단위 충돌 다수 | ★★★ 어려움 |
| C. Git 브랜치 분리 | 원본 완전 보존 | 동일 브랜치에서 작업 불가 | ★☆☆ 쉬움 |
| D. 별도 repo로 추출 | 완전한 분리 | 공유 모듈 동기화 복잡 | ★★★ 매우 어려움 |

### 추천: 전략 A (주석 비활성화)

- 디렉토리 단위 제거 → 충돌 없음 (전체 제거량의 95%)
- 공유 파일 주석 처리 → 충돌 최소화 (7개 파일만)
- `[NIA-ONLY]` 태그 → 재결합 시 추적 용이

---

## 8. 실행 체크리스트

- [ ] **사전 준비**
  - [ ] 현재 상태 커밋 확인 (clean working tree)
  - [ ] 분리 작업 전용 브랜치 생성: `git checkout -b feature/nia-only`
- [ ] **디렉토리 제거** (git rm -r)
  - [ ] BE: app-ipms, app-dataHub, app-demo
  - [ ] FE: views-ipms, views-dataHub, views-aiTemplate
  - [ ] FE: router/ipms, router/dataHub, router/aiTemplate
  - [ ] FE: .env.ipms, .env.datahub, .env.aiTemplate
  - [ ] FE: api/ipms.js, api/dataHub.js
  - [ ] FE: store/modules/ipms.js, store/modules/dataHub.js
- [ ] **공유 파일 주석 처리** ([NIA-ONLY] 태그)
  - [ ] BE/settings.gradle
  - [ ] FE/src/router/index.js
  - [ ] FE/src/store/index.js
  - [ ] FE/src/class/appOptions.js
  - [ ] FE/vue.config.js
  - [ ] FE/package.json (키 제거 + 이 문서에 원본 기록)
- [ ] **빌드 검증**
  - [ ] `cd BE && ./gradlew clean build`
  - [ ] `cd FE && npm run build:nia`
  - [ ] 로컬 실행 확인
- [ ] **커밋**
  - [ ] `git commit -m "NIA 전용 소스 분리 — 비NIA 모듈 제거 및 [NIA-ONLY] 비활성화"`

---

## 9. package.json 원본 스크립트 백업

JSON은 주석을 지원하지 않으므로, 제거되는 스크립트를 여기에 기록한다.

```json
{
  "build:ipms": "vue-cli-service build --mode ipms",
  "build:datahub": "vue-cli-service build --mode datahub",
  "build:demo": "vue-cli-service build --mode demo"
}
```

재결합 시 위 키들을 `package.json`의 `scripts` 섹션에 다시 추가하면 된다.

---

## 10. 전략 B 상세: 코드 완전 삭제 + 수동 Merge 공수 분석

> 추가일: 2026-04-07 (2차 분석)
>
> 주석 비활성화(전략 A) 대신 **코드를 깔끔하게 삭제**하고, 재결합 시 수동으로 merge하는 방안.
> 충돌 지점을 정확히 알고 있으면 수동 복원이 어렵지 않다.

### 10.1 디렉토리 제거 — 전략 A와 동일 (충돌 없음)

디렉토리/파일 단위 삭제는 전략 A와 동일하다. git이 파일 삭제로 추적하므로 merge 충돌이 없다.

### 10.2 공유 파일 — 실제 코드 기준 수정 내역

실제 코드를 확인한 결과, **수정 대상은 5개 파일, 총 28줄**이다.

#### 파일 1: `BE/settings.gradle` (3줄 삭제)

전체 10줄 중 3줄 삭제. 재결합 시 3줄 다시 추가하면 끝.

```diff
 rootProject.name = 'web'
 include 'common-base'
 include 'common-web'
 include 'common-mq'
 include 'common-ws'
-include 'app-datahub'
 include 'app-nia'
-include 'app-demo'
-include 'app-ipms'
```

**merge 복원 공수**: 3줄 추가 (1분)

---

#### 파일 2: `FE/src/router/index.js` (18줄 삭제/수정)

전체 164줄 중 18줄 변경. **가장 큰 수정 지점**.

```diff
-const { dataHubHome, dataHubLogin, dataHubRoute } = require('./dataHub/index')
-// const { aiTemplateHome, aiTemplateLogin, aiTemplateRoute } = require('./aiTemplate/index')
-import { aiTemplateHome, aiTemplateLogin, aiTemplateRoute } from './aiTemplate/index'
 const { niaHome, niaLogin, niaRoute } = require('./nia/index')
-const { ipmsHome, ipmsLogin, ipmsRoute } = require('./ipms/index')

 let loginView
 let projectRoute
 let projectHome = '/'
 switch (project) {
-  case 'datahub':
-    loginView = dataHubLogin
-    projectRoute = dataHubRoute
-    projectHome = dataHubHome
-    break
-  case 'ai': // layout, grid sample
-    loginView = aiTemplateLogin
-    projectRoute = aiTemplateRoute
-    projectHome = aiTemplateHome
-    break
   case 'nia':
     loginView = niaLogin
     projectRoute = niaRoute
     projectHome = niaHome
     break
-  case 'ipms':
-    loginView = ipmsLogin
-    projectRoute = ipmsRoute
-    projectHome = ipmsHome
-    break
   default:
     break
 }
```

**merge 복원 공수**: import 3줄 + switch case 15줄 복원 (5분)
- 단, 원본에서 이 파일이 수정되었다면 3-way merge 충돌 발생
- 충돌 시 원본의 import/switch를 그대로 채택하면 됨 (`--theirs`)

---

#### 파일 3: `FE/src/class/appOptions.js` (2줄 수정)

전체 180줄 중 2줄 변경.

```diff
-    projectList: ['datahub', 'nia', 'ipms'],
+    projectList: ['nia'],

   get dark() {
-    return ['ipms'].includes(this._data.project)
+    return false
   }
```

**merge 복원 공수**: 2줄 원복 (1분)

---

#### 파일 4: `FE/vue.config.js` (6줄 삭제)

전체 273줄 중 `getOutputDir()` 함수 내 6줄 삭제.

```diff
 function getOutputDir() {
   let dir = 'dist'
   if (process.env.VUE_APP_MOCK !== 'FE') {
     switch (process.env.VUE_APP_PROJECT) {
-      case 'datahub':
-        dir = '../BE/app-dataHub/src/main/resources/static'
-        break
-      case 'demo':
-        dir = '../BE/app-demo/src/main/resources/static'
-        break
       case 'nia':
         dir = '../BE/app-nia/src/main/resources/static'
         break
-      case 'ipms':
-        dir = '../BE/app-ipms/src/main/resources/static'
-        break
     }
   }
```

**merge 복원 공수**: 6줄(3개 case) 복원 (2분)

---

#### 파일 5: `FE/package.json` (3줄 삭제)

```diff
   "scripts": {
     "build:nia": "vue-cli-service build --mode nia",
-    "build:datahub": "vue-cli-service build --mode datahub",
-    "build:demo": "vue-cli-service build --mode demo",
-    "build:ipms": "vue-cli-service build --mode ipms",
```

**merge 복원 공수**: 3줄 추가 (1분)

---

### 10.3 총 공수 요약

| 파일 | 삭제 라인 | 재결합 시 복원 공수 | 충돌 가능성 |
|------|----------|-------------------|-----------|
| `settings.gradle` | 3줄 | 1분 | 낮음 — 라인이 독립적 |
| `router/index.js` | 18줄 | 5분 | **높음** — switch문 내 변경 |
| `appOptions.js` | 2줄 | 1분 | 중간 — 같은 라인 수정 시 |
| `vue.config.js` | 6줄 | 2분 | 중간 — switch문 내 변경 |
| `package.json` | 3줄 | 1분 | 낮음 — 키가 독립적 |
| **합계** | **28줄 (+ 디렉토리)** | **~10분** | |

### 10.4 결론: 전략 B도 충분히 현실적

- 수정 대상이 **5개 파일 28줄**뿐이므로, 코드를 깔끔하게 삭제해도 재결합 공수는 **10분 이내**
- 이 문서에 정확한 diff가 기록되어 있으므로, 복원 시 이 문서를 참고하면 됨
- 유일한 리스크: **원본에서 `router/index.js`의 switch문 근처를 대폭 수정**한 경우 → 수동 merge 필요하나, 구조를 알고 있으므로 난이도 낮음

### 10.5 전략 A vs B 최종 비교

| 항목 | 전략 A (주석 비활성화) | 전략 B (완전 삭제) |
|------|---------------------|-------------------|
| 코드 깔끔함 | 주석이 남아 약간 지저분 | 깔끔 |
| merge 자동 해결률 | 높음 (원본 라인 보존) | 중간 (삭제 라인에서 충돌) |
| 재결합 수동 공수 | ~3분 (주석 해제) | ~10분 (코드 복원) |
| 추적 용이성 | grep [NIA-ONLY]로 즉시 | 이 문서의 diff 참고 |
| 실수 가능성 | 낮음 (주석 해제만) | 약간 있음 (라인 복원 정확도) |

> **어느 전략이든 큰 차이 없음**. 코드 깔끔함을 선호하면 전략 B, 안전성을 선호하면 전략 A.
