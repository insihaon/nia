# 재결합(Merge) 가이드 — Web2024 NIA 분리 후 복원

> 작성일: 2026-04-07
> 목적: NIA 전용 분리 후, 원본 저장소와 다시 합칠 때 주의해야 할 지점 정리

---

## 요약

- **삭제된 디렉토리/파일**: git이 `deleted by us`로 처리 → `checkout --theirs`로 일괄 복원 (자동)
- **수정된 공유 파일 5개**: 아래 표의 내용을 수동 복원 (총 28줄, ~10분)

---

## 1. 디렉토리/파일 복원 (자동 — 충돌 없음)

merge 시 삭제된 파일은 `deleted by us`로 표시된다. 아래 명령으로 일괄 복원:

```bash
# BE 앱 모듈 복원
git checkout --theirs BE/app-ipms/
git checkout --theirs BE/app-dataHub/
git checkout --theirs BE/app-demo/

# FE 뷰 복원
git checkout --theirs FE/src/views-ipms/
git checkout --theirs FE/src/views-dataHub/
git checkout --theirs FE/src/views-aiTemplate/

# FE 라우터 복원
git checkout --theirs FE/src/router/ipms/
git checkout --theirs FE/src/router/dataHub/
git checkout --theirs FE/src/router/aiTemplate/

# FE 환경파일 복원
git checkout --theirs FE/.env.ipms
git checkout --theirs FE/.env.datahub
git checkout --theirs FE/.env.aiTemplate

# FE API/Store 모듈 복원
git checkout --theirs FE/src/api/ipms.js
git checkout --theirs FE/src/api/dataHub.js
git checkout --theirs FE/src/store/modules/ipms.js
git checkout --theirs FE/src/store/modules/dataHub.js
```

---

## 2. 공유 파일 수동 복원 (5개 파일, 28줄)

### 파일 1: `BE/settings.gradle`

**복원**: 아래 3줄을 다시 추가

```gradle
rootProject.name = 'web'
include 'common-base'
include 'common-web'
include 'common-mq'
include 'common-ws'
include 'app-datahub'          ← 추가
include 'app-nia'
include 'app-demo'             ← 추가
include 'app-ipms'             ← 추가
```

---

### 파일 2: `FE/src/router/index.js` ⚠️ 가장 주의

**복원할 내용**:

(A) import 영역 (19~23번줄 부근) — 3줄 복원:

```javascript
const { dataHubHome, dataHubLogin, dataHubRoute } = require('./dataHub/index')
import { aiTemplateHome, aiTemplateLogin, aiTemplateRoute } from './aiTemplate/index'
const { niaHome, niaLogin, niaRoute } = require('./nia/index')
const { ipmsHome, ipmsLogin, ipmsRoute } = require('./ipms/index')
```

(B) switch문 (28~51번줄 부근) — 15줄 복원:

```javascript
switch (project) {
  case 'datahub':                    ← 추가
    loginView = dataHubLogin         ← 추가
    projectRoute = dataHubRoute      ← 추가
    projectHome = dataHubHome        ← 추가
    break                            ← 추가
  case 'ai':                         ← 추가
    loginView = aiTemplateLogin      ← 추가
    projectRoute = aiTemplateRoute   ← 추가
    projectHome = aiTemplateHome     ← 추가
    break                            ← 추가
  case 'nia':
    loginView = niaLogin
    projectRoute = niaRoute
    projectHome = niaHome
    break
  case 'ipms':                       ← 추가
    loginView = ipmsLogin            ← 추가
    projectRoute = ipmsRoute         ← 추가
    projectHome = ipmsHome           ← 추가
    break                            ← 추가
  default:
    break
}
```

**주의**: 원본에서 이 switch문 근처를 수정했다면 충돌 발생 가능.
→ 원본의 switch 구조를 기준으로 하고, NIA case만 우리 쪽 버전 유지.

---

### 파일 3: `FE/src/class/appOptions.js`

**복원할 내용**: 2줄

(A) projectList (31번줄 부근):
```javascript
// 분리 후:
projectList: ['nia'],
// 복원:
projectList: ['datahub', 'nia', 'ipms'],
```

(B) dark getter (73번줄 부근):
```javascript
// 분리 후:
get dark() {
  return false
}
// 복원:
get dark() {
  return ['ipms'].includes(this._data.project)
}
```

---

### 파일 4: `FE/vue.config.js`

**복원할 내용**: `getOutputDir()` 함수 내 3개 case 추가 (85~106번줄 부근)

```javascript
function getOutputDir() {
  let dir = 'dist'
  if (process.env.VUE_APP_MOCK !== 'FE') {
    switch (process.env.VUE_APP_PROJECT) {
      case 'datahub':                                              ← 추가
        dir = '../BE/app-dataHub/src/main/resources/static'        ← 추가
        break                                                      ← 추가
      case 'demo':                                                 ← 추가
        dir = '../BE/app-demo/src/main/resources/static'           ← 추가
        break                                                      ← 추가
      case 'nia':
        dir = '../BE/app-nia/src/main/resources/static'
        break
      case 'ipms':                                                 ← 추가
        dir = '../BE/app-ipms/src/main/resources/static'           ← 추가
        break                                                      ← 추가
    }
  }
```

---

### 파일 5: `FE/package.json`

**복원할 내용**: scripts에 3개 키 추가

```json
{
  "scripts": {
    "build:datahub": "vue-cli-service build --mode datahub",
    "build:demo": "vue-cli-service build --mode demo",
    "build:ipms": "vue-cli-service build --mode ipms"
  }
}
```

---

## 3. 복원 후 검증 체크리스트

```bash
# BE 빌드 (전체)
cd BE && ./gradlew clean build

# FE 빌드 (각 앱)
cd FE
npm run build:nia
npm run build:ipms
npm run build:datahub
npm run build:demo

# 로컬 실행 확인
```

---

## 4. 한눈에 보기

| 파일 | 복원 내용 | 라인 수 | 충돌 가능성 |
|------|----------|--------|-----------|
| `settings.gradle` | 3줄 추가 | 3 | 낮음 |
| `router/index.js` | import 3줄 + switch 15줄 | 18 | **높음** (switch문) |
| `appOptions.js` | projectList + dark getter | 2 | 중간 |
| `vue.config.js` | getOutputDir 3개 case | 6 | 중간 |
| `package.json` | scripts 3개 키 | 3 | 낮음 |
| **합계** | | **28줄** | **~10분** |

> 핵심: **router/index.js**만 주의하면 나머지는 기계적 복원이다.
