# Map2D 번들 충돌 — 근본 원인 분석

> 작성일: 2026-04-07
> 상태: 분석 완료
> 선행 문서: 08_Map2D_번들충돌_해결계획.md

---

## 1. 문제 요약

### 증상

| 시나리오 | 결과 |
|----------|------|
| 대시보드 토폴로지 먼저 → 순단장애/예지보전 | 순단장애/예지보전 토폴로지 **안 보임** |
| 순단장애/예지보전 먼저 → 대시보드 토폴로지 | 대시보드 토폴로지 **스타일 깨짐** |

### 시도한 해결책과 결과

| 방안 | 내용 | 결과 |
|------|------|------|
| CSS scoped 격리 | 외부 컴포넌트 `<style>` → `<style scoped>` | CSS 침범 해결됨 (커밋 완료) |
| 참조 격리 (방안 C) | `this._Map2dRef = window.Map2d` 보존 | **실패** — 동일 증상 지속 |

---

## 2. 참조 격리가 실패하는 이유

참조 격리는 `window.Map2d` **생성자 함수 하나**만 보존한다.
그러나 충돌은 생성자 외에 **7개의 전역 자원**에서 동시에 발생한다.

---

## 3. 전역 충돌 7가지

### 충돌 1: SVG ID 하드코딩 [심각]

세 컴포넌트 모두 `id="topology_container"`를 사용하며, 라이브러리 내부에서 이 ID를 하드코딩으로 참조한다.

```javascript
// index_nia.js:382
svg = d3.select("svg#topology_container")

// index_dev.js:1053
const svg = document.querySelector('svg#topology_container')
```

**영향**: 두 번째 Map2d 인스턴스가 `initialize()`를 호출하면, `d3.select`가 DOM 순서상 **첫 번째** `svg#topology_container`를 찾는다. 자신의 SVG가 아닌 다른 컴포넌트의 SVG에 렌더링하게 된다.

### 충돌 2: 클로저 변수 공유 [심각]

Map2d 생성자 내부에서 `svg`, `zoom`, `simulation` 등이 **클로저 변수**로 선언된다.

```javascript
// index_nia.js:83-89
let Map2d = global.Map2d = function () {
    let ctrl = global.c = this;
    let simulation, link_force, charge_force, center_force;
    let svg, g_link, g_node, g_desc;
    // ... 이후 모든 내부 함수가 이 클로저 변수를 참조
```

**영향**: `new Map2d()`를 두 번 호출하면, 두 번째 호출이 클로저 변수 `svg`를 덮어쓴다. 첫 번째 인스턴스의 내부 함수들은 여전히 같은 클로저 변수를 참조하므로, **두 번째 인스턴스의 SVG를 가리키게 된다.**

이것은 참조 격리와 무관하다. 생성자 함수 자체가 동일한 클로저 스코프를 공유하기 때문이다.

### 충돌 3: `window.d3` 덮어쓰기 [높음]

두 번들 모두 d3 v4를 내장하며, 로드 시 `window.d3`에 등록한다.

```javascript
// index_nia_bundle.js (d3 부분)
(factory((global.d3 = global.d3 || {})));
```

**영향**: 두 번째 번들이 로드되면 `window.d3`가 교체된다. 첫 번째 Map2d 인스턴스의 내부 함수들이 `d3.select()`, `d3.forceSimulation()` 등을 호출할 때 **다른 d3 인스턴스**를 사용하게 된다.

### 충돌 4: `window.onload` 덮어쓰기 [높음]

```javascript
// index_nia.js:2847
window.onload = () => { if (!window.libDev) return; ... }

// index_dev.js:5052
window.onload = () => { if(!window.libDev) return; ... }
```

**영향**: 두 번째 번들이 `window.onload`를 덮어쓰면 첫 번째 번들의 초기화 코드가 실행되지 않는다.

참고: 현재 `window.libDev`가 설정되지 않으므로 즉시 return하여 실질적 영향은 제한적이나, 구조적으로 위험하다.

### 충돌 5: `d3.event` 전역 상태 [높음]

d3 v4에서 `d3.event`는 **전역 싱글턴**이다. 현재 처리 중인 이벤트의 컨텍스트를 저장한다.

```javascript
// index_nia.js:364
let font_size = 20 / (d3.event.transform.k)

// index_nia.js:476
if (ctrl.zoom_scale != d3.event.transform.k)
```

**영향**: 한 인스턴스의 줌/드래그 이벤트 핸들러가 실행되는 동안 `d3.event`가 설정되는데, 다른 인스턴스의 핸들러가 동시에 실행되면 잘못된 이벤트 컨텍스트를 참조한다.

### 충돌 6: dat.GUI 중복 패널 [중간]

```javascript
// index_nia.js:2938
let gui = new dat.GUI();

// index_dev.js:5173
let gui = new dat.GUI();
```

**영향**: 각 번들이 로드될 때마다 dat.GUI 패널이 DOM에 추가된다. 동일한 옵션 객체를 제어하는 중복 패널이 생성되어 UI가 혼란스러워진다.

### 충돌 7: d3-tip 전역 툴팁 [중간]

```javascript
// index_nia.js:533-534
link_source_tip = d3.tip()
link_dest_tip = d3.tip()
```

**영향**: d3-tip은 툴팁 DOM 요소를 `<body>`에 직접 추가한다. 두 인스턴스가 각각 생성하면 중복 툴팁이 겹쳐 표시되거나 위치가 어긋난다.

---

## 4. 충돌 전체 구조도

```
[번들 A: index_nia_bundle.js]          [번들 B: map2d_untact.min.js]
    ↓ 로드                                  ↓ 로드
window.d3 = d3_A ──────────────────→ window.d3 = d3_B (덮어씀!)
window.Map2d = Map2d_A ───────────→ window.Map2d = Map2d_B (덮어씀!)
window.onload = fn_A ─────────────→ window.onload = fn_B (덮어씀!)
dat.GUI 패널 A 생성 ──────────────→ dat.GUI 패널 B 생성 (중복!)
d3-tip 툴팁 A 생성 ──────────────→ d3-tip 툴팁 B 생성 (중복!)

    ↓ initialize()                         ↓ initialize()
svg = d3.select("#topology_container")  svg = d3.select("#topology_container")
    → SVG 요소 #1을 잡음                     → SVG 요소 #1을 잡음 (같은 것!)
    
클로저: svg = SVG#1 ──────────────→ 클로저: svg = SVG#1 (덮어씀!)
    → 인스턴스 A도 SVG#1을 바라봄          → 인스턴스 B도 SVG#1을 바라봄
    → 인스턴스 A의 SVG#2는 아무도 참조하지 않음
```

---

## 5. 해결 방안 재평가

### 방안 C (참조 격리): 불가

생성자 참조만 보존하므로 7개 전역 충돌 중 **0개**를 해결.

### 방안 A/B (번들 통합): 부분적 해결

하나의 번들만 사용하면 d3/dat.gui 덮어쓰기는 해소되나, **SVG ID 충돌**과 **클로저 변수 공유**는 여전히 발생. 동일 번들로 두 개의 Map2d 인스턴스를 만들어도 같은 문제.

### 방안 E: 라이브러리 소스 수정 [근본 해결]

충돌의 핵심인 SVG ID 하드코딩과 클로저 변수 문제를 소스에서 수정.

수정 대상:
1. `initialize()` → SVG 셀렉터를 파라미터로 받도록 변경
2. 클로저 변수 `svg` → `this.svg`로 인스턴스 바인딩
3. `d3.select("svg#topology_container")` → `d3.select(this.svgSelector)` 변경

난이도: 높음 (index_nia.js 3,028줄, index_dev.js 5,450줄 내부 구조 이해 필요)
위험도: 높음 (내부 함수가 클로저 변수를 광범위하게 참조)

### 방안 F: 동시 사용 방지 (런타임 가드) [현실적 우회]

두 토폴로지가 동시에 DOM에 존재하지 않도록 앱 레벨에서 제어.

방법:
- 페이지 이동 시 이전 Map2d 인스턴스를 명시적으로 destroy
- `beforeDestroy` 훅에서 SVG 정리, 이벤트 리스너 제거
- 또는 토폴로지 컴포넌트에서 `<keep-alive>` 사용하지 않도록 보장

난이도: 낮음
위험도: 낮음
한계: 동시 열기 자체가 불가능해짐 (기능 제약)

### 방안 G: iframe 격리 [완전 격리]

순단장애/예지보전의 토폴로지를 `<iframe>` 내부에서 렌더링하여 전역 스코프를 완전 분리.

난이도: 중간
위험도: 중간 (데이터 통신을 postMessage로 변환 필요)
효과: 모든 전역 충돌 완전 해소

---

## 6. 방안별 비교 요약

| 방안 | SVG ID | 클로저 | d3 | onload | dat.gui | 난이도 | 위험도 |
|------|--------|--------|----|----|---------|--------|--------|
| C. 참조 격리 | X | X | X | X | X | 낮음 | 낮음 |
| A/B. 번들 통합 | X | X | O | O | O | 중간 | 높음 |
| E. 소스 수정 | O | O | O | O | O | 높음 | 높음 |
| F. 동시 사용 방지 | O* | O* | O | O | O | 낮음 | 낮음 |
| G. iframe 격리 | O | O | O | O | O | 중간 | 중간 |

O = 해결, X = 미해결, O* = 동시에 존재하지 않으므로 충돌 자체가 발생하지 않음

---

## 7. 권장 순서

1. **즉시**: 방안 F (동시 사용 방지) — 페이지 전환 시 이전 Map2d 정리
2. **중기**: 방안 E (소스 수정) — SVG ID 파라미터화, 클로저→인스턴스 바인딩
3. **장기**: 번들 통합 (방안 A) — 통합 소스에서 단일 번들 생성

---

Version: 1.0.0
