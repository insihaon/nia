# Map2D 번들 충돌 해결 계획

> 작성일: 2026-04-07
> 상태: 검토 대기

---

## 1. 현재 상황

### 증상

| 시나리오 | 결과 |
|----------|------|
| 대시보드 토폴로지 먼저 → 순단장애/예지보전 | 순단장애/예지보전 토폴로지 **안 보임** |
| 순단장애/예지보전 먼저 → 대시보드 토폴로지 | 대시보드 토폴로지 **스타일 깨짐** |

### 원인

세 컴포넌트가 서로 다른 Map2D 번들을 로드하지만, 동일한 `window.Map2d` 전역 변수에 등록.
나중에 로드된 번들이 먼저 로드된 번들을 덮어쓰며, `<script>` 태그와 주입된 CSS는 제거되지 않고 잔류.

---

## 2. 두 번들의 차이점 분석

### 소스 코드 차이

| 항목 | `index_nia_bundle.js` (대시보드용) | `map2d_untact.min.js` (예지보전/순단장애용) |
|------|-----------------------------------|-------------------------------------------|
| 소스 | `index_nia.js` (3,028줄) | `index_dev.js` (5,450줄) 기반 추정 |
| 크기 | 676KB | 349KB |
| d3 | v4.13.0 내장 | v4 내장 |
| dat.gui | 내장 | 내장 |
| d3-context-menu | **내장** | **미포함** (별도 로드) |

### API 호출 차이 [중요]

| API | niaTopology (대시보드) | 예지보전/순단장애 |
|-----|----------------------|------------------|
| `initialize()` | `map.initialize()` (인자 없음) | `map.initialize(elId)` (요소 ID 전달) |
| `load()` | `map.options.load(data)` (options 경유) | `map.load(mapData)` (직접 호출) |

### 데이터 포맷 차이 [중요]

```javascript
// niaTopology가 전달하는 데이터
{ nodes: [...], links: [...], config: {...} }

// 예지보전/순단장애가 전달하는 데이터
{ config: {...}, data: { nodes: [...], links: [...] } }
```

**두 번들은 서로 다른 데이터 포맷을 기대한다.** 단순 교체 시 데이터 로딩이 실패할 수 있음.

---

## 3. 해결 방안 비교

### 방안 A: 번들 통합 (index_nia_bundle.js로 통일)

```
예지보전/순단장애에서 map2d_untact.min.js → index_nia_bundle.js로 교체
+ 데이터 포맷을 index_nia 형식으로 맞춤
+ initialize() 호출에서 elId 인자 제거
```

| 장점 | 단점 |
|------|------|
| 번들 1개로 단순화 | 데이터 포맷 변환 필요 |
| window.Map2d 충돌 근본 제거 | initialize(elId) 동작 변경 검증 필요 |
| JS 로딩 크기 감소 (중복 제거) | 예지보전/순단장애 기능 회귀 테스트 필요 |

**위험도: 높음** — API 차이와 데이터 포맷 차이로 예지보전/순단장애가 깨질 가능성.

### 방안 B: 번들 통합 (map2d_untact.min.js로 통일)

```
niaTopology에서 index_nia_bundle.js → map2d_untact.min.js로 교체
+ 데이터 포맷을 untact 형식으로 맞춤
+ options.load() → map.load()로 변경
```

| 장점 | 단점 |
|------|------|
| 번들 1개로 단순화 | niaTopology의 options.load() API 변경 필요 |
| | niaTopology 데이터 포맷 변환 필요 |
| | d3-context-menu 별도 로드 추가 필요 |
| | niaTopology 기능 회귀 테스트 필요 |

**위험도: 높음** — niaTopology 코드 변경 범위가 크고, 검증이 어려움.

### 방안 C: 번들 분리 유지 + 참조 격리 [권장]

```
각 컴포넌트가 번들 로드 직후 window.Map2d를 지역 변수에 보존.
이후 지역 변수로 인스턴스를 생성하여 덮어쓰기 영향을 받지 않음.
```

| 장점 | 단점 |
|------|------|
| **기존 동작 100% 유지** | 번들 2개 유지 (JS 크기 비효율) |
| 수정 범위 최소 (각 파일 2~3줄) | window.Map2d가 여전히 덮어써짐 (참조만 보존) |
| 데이터 포맷 변경 불필요 | 근본 해결이 아닌 우회 |
| 회귀 위험 거의 없음 | |

**위험도: 낮음** — 기존 동작을 보존하면서 충돌만 회피.

### 방안 D: 번들 소스 통합 후 재빌드

```
index_nia.js와 index_dev.js를 분석하여 하나의 통합 소스로 합침.
두 데이터 포맷을 모두 지원하는 load() 함수로 통합.
```

| 장점 | 단점 |
|------|------|
| 근본적 해결 | 라이브러리 소스 수정 필요 (5,450줄 분석) |
| 번들 1개로 단순화 | 개발 시간 상당히 소요 |
| | 라이브러리 내부 이해 필요 |

**위험도: 중간 (시간 리스크 높음)**

---

## 4. 권장안: 방안 C (참조 격리)

### 근거

1. 현재 두 번들은 API, 데이터 포맷, 초기화 방식이 모두 다름 → 통합 시 회귀 위험 높음
2. 방안 C는 각 파일 2~3줄 수정으로 충돌을 해소하며, 기존 동작을 100% 보존
3. 추후 번들 통합이 필요하면 방안 D를 별도 SPEC으로 진행 가능

### 수정 계획

#### 수정 1: niaTopology — 참조 보존

**파일**: `views-nia/dashBoard/popup/niaTopology/index.vue`

```javascript
// 변경 전 (created, 254-255행)
const async = false
this.addScript(['./extlib/map2d/lib/index_nia_bundle.js'], async)

// 변경 후
const async = false
this.addScript(['./extlib/map2d/lib/index_nia_bundle.js'], async)
this._Map2dRef = window.Map2d  // 로드 직후 참조 보존
```

```javascript
// 변경 전 (initMap, 384행)
if (!window.Map2d) {
  THIS.initMap()
  return
}
const map = (this.map = THIS.map = new window.Map2d())

// 변경 후
if (!this._Map2dRef) {
  this._Map2dRef = window.Map2d
}
if (!this._Map2dRef) {
  THIS.initMap()
  return
}
const map = (this.map = THIS.map = new this._Map2dRef())
```

```javascript
// 변경 전 (initMap, 395행)
map.addEventListener(window.Map2d.eventType.selectChanged, ...)

// 변경 후
map.addEventListener(this._Map2dRef.eventType.selectChanged, ...)
```

#### 수정 2: PredictiveMaintenance — 참조 보존

**파일**: `views-nia/pages/PredictiveMaintenance/component/Map2D.vue`

```javascript
// 변경 전 (created, 105-116행)
this.addScript([...js, './extlib/map2d/lib/map2d_untact.min.js'], async)

// 변경 후
this.addScript([...js, './extlib/map2d/lib/map2d_untact.min.js'], async)
this._Map2dRef = window.Map2d  // 로드 직후 참조 보존
```

```javascript
// 변경 전 (initMap, 124-128행)
if (!window.Map2d) {
  THIS.initMap()
  return
}
const map = (this.map = THIS.map = new window.Map2d())

// 변경 후
if (!this._Map2dRef) {
  this._Map2dRef = window.Map2d
}
if (!this._Map2dRef) {
  THIS.initMap()
  return
}
const map = (this.map = THIS.map = new this._Map2dRef())
```

```javascript
// 변경 전 (initMap, 131행)
map.addEventListener(window.Map2d.eventType.selectChanged, ...)

// 변경 후
map.addEventListener(this._Map2dRef.eventType.selectChanged, ...)
```

#### 수정 3: TransientOutage — 참조 보존

**파일**: `views-nia/pages/TransientOutage/topology2d/index.vue`

동일한 패턴으로 수정 (PredictiveMaintenance와 같은 구조).

---

## 5. 검증 체크리스트

수정 후 다음 시나리오를 모두 확인해야 함:

### 기본 동작 확인

- [ ] 대시보드 토폴로지 단독 열기 → 정상 렌더링
- [ ] 순단장애 토폴로지 단독 열기 → 정상 렌더링
- [ ] 예지보전 토폴로지 단독 열기 → 정상 렌더링

### 교차 사용 확인 (핵심)

- [ ] 대시보드 토폴로지 먼저 → 순단장애 열기 → **순단장애 정상 표시**
- [ ] 대시보드 토폴로지 먼저 → 예지보전 열기 → **예지보전 정상 표시**
- [ ] 순단장애 먼저 → 대시보드 토폴로지 열기 → **대시보드 정상 표시**
- [ ] 예지보전 먼저 → 대시보드 토폴로지 열기 → **대시보드 정상 표시**

### 기능 검증

- [ ] 대시보드 토폴로지: 줌인/줌아웃, 노드 클릭, 링크 클릭
- [ ] 순단장애 토폴로지: 데이터 로드, 노드 선택
- [ ] 예지보전 토폴로지: 탭 전환 (Rx/Tx, Span Loss, NTD)

---

## 6. 리스크 매트릭스

| 리스크 | 발생 확률 | 영향도 | 대응 |
|--------|----------|--------|------|
| addScript sync 로드에서 참조 보존 타이밍 이슈 | 낮음 | 높음 | async=false이므로 동기 로드 완료 후 참조 |
| 다른 코드에서 window.Map2d 직접 참조 | 낮음 | 중간 | grep으로 전체 검색하여 확인 |
| _Map2dRef 네이밍 충돌 | 매우 낮음 | 낮음 | 언더스코어 prefix로 내부 변수 표시 |
| 예지보전/순단장애 토폴로지 기능 회귀 | 매우 낮음 | 높음 | 검증 체크리스트로 확인 |

---

## 7. 향후 과제 (이번 수정 범위 밖)

번들 통합은 별도 작업으로 진행 권장:

1. `index_nia.js`와 `index_dev.js`의 정확한 차이점 분석 (3,028줄 vs 5,450줄)
2. 데이터 포맷 통일 (`{nodes, links, config}` vs `{config, data:{nodes, links}}`)
3. initialize() 파라미터 통일 (요소 ID 전달 vs 하드코딩)
4. 통합 번들 빌드 스크립트 작성
5. SVG 컨테이너 ID 하드코딩(`topology_container`) 제거 → 파라미터화

---

Version: 1.0.0
