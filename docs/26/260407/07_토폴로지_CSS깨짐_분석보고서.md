# 토폴로지 CSS 깨짐 현상 — 분석 보고서

> 작성일: 2026-04-07
> 대상: `views-nia/dashBoard/popup/niaTopology/index.vue`
> 상태: 분석 완료 (v2 — 방향 수정)

---

## 1. 증상 및 문제 방향

토폴로지 팝업(niaTopology)의 CSS/스타일이 깨지는 현상.
순단장애·예지보전 기능을 가져오면서 이전에도 유사한 현상이 있었음.

**문제 방향**: 순단장애/예지보전의 스타일 → niaTopology 침범 (외부 → 내부)
niaTopology 자체의 스타일이 밖으로 새는 것이 아님.

---

## 2. 스타일 침범 경로 분석

### 침범 경로 1: 비scoped `<style>` 블록 전역 잔류 [핵심]

| 컴포넌트 | 파일 | scoped | 클래스 래퍼 |
|----------|------|--------|-------------|
| 예지보전 Map2D | `PredictiveMaintenance/component/Map2D.vue:185` | **아님** | `.PmmTopology2D` |
| 순단장애 Map2D | `TransientOutage/topology2d/index.vue:185` | **아님** | `.MbaTopology2D` |

두 컴포넌트 모두 `<style lang="scss">` (비scoped).
Vue SPA에서 해당 페이지 방문 시 `<head>`에 스타일이 주입되며, **다른 페이지로 이동해도 스타일이 DOM에 잔류할 수 있음** (`css.extract: false` 환경).

클래스 래퍼(`.PmmTopology2D`, `.MbaTopology2D`)로 어느 정도 격리되지만, 아래 항목은 래퍼 밖으로 유출:

### 침범 경로 2: `:root` CSS 변수 전역 유출 [확인됨]

```scss
// PredictiveMaintenance/Map2D.vue:186 — 클래스 래퍼 밖에 위치
// TransientOutage/topology2d/index.vue:186 — 동일
:root {
    --focus-color: rgba(0, 0, 0, 0.7);  // ← 전역 CSS 변수
}

.PmmTopology2D {   // 래퍼는 여기서 시작
  // ...
}
```

`:root`는 `.PmmTopology2D` 밖에 선언되어 전역으로 적용. niaTopology가 이 변수를 사용하지 않더라도 전역 오염.

### 침범 경로 3: `@keyframes` 전역 충돌 [확인됨]

CSS `@keyframes`는 **클래스 래퍼 안에 있어도 항상 전역으로 등록**됨.

| keyframe 이름 | 정의 위치 1 (예지보전/순단장애) | 정의 위치 2 (niaTopology) |
|--------------|-------------------------------|---------------------------|
| `blinking` | Map2D.vue:192 / index.vue:192 | niaTopology_map3d.scss:311 |
| `stroke` | Map2D.vue:195 / index.vue:197 | niaTopology_map3d.scss:397 |

3곳에서 동일 이름의 `@keyframes`가 정의됨. **마지막으로 로드된 정의가 우선 적용**되므로 페이지 방문 순서에 따라 애니메이션 동작이 달라질 수 있음.

현재는 정의 내용이 동일하여 눈에 띄는 차이는 없으나, 구조적으로 불안정.

### 침범 경로 4: Map2D JS 번들 CSS 충돌 [핵심]

| 컴포넌트 | 번들 | 크기 | 내장 CSS |
|----------|------|------|----------|
| niaTopology | `index_nia_bundle.js` | 676KB (20K줄) | dat.gui CSS (`.dg` 셀렉터 다수) |
| 예지보전/순단장애 | `map2d_untact.min.js` | 349KB (2줄) | 미확인 (minified) |

두 번들은 서로 다른 Map2D 빌드이며 모두 `window.Map2d`에 바인딩.

**핵심 충돌**:
- `addScript` 메서드(Base.js:633)는 중복 로딩을 방지하나, **서로 다른 파일이므로 모두 로드됨**
- 나중에 로드된 번들이 `window.Map2d`를 덮어씀
- `index_nia_bundle.js`는 dat.gui CSS를 `cssText`로 DOM에 직접 주입 (전역)
- `map2d_untact.min.js`도 자체 CSS를 주입할 가능성 있음

```
[사용자가 예지보전 페이지 방문]
  → map2d_untact.min.js 로드 → window.Map2d = 예지보전 버전
  → 예지보전 Map2D CSS가 <head>에 주입

[사용자가 대시보드로 이동 → 토폴로지 팝업 열기]
  → index_nia_bundle.js 로드 → window.Map2d = niaTopology 버전 (덮어씀)
  → 그러나 예지보전의 CSS는 <head>에 잔류 ← 충돌 원인!
  → 또는 반대: niaTopology의 Map2d가 예지보전 버전으로 이미 덮어써짐
```

### 침범 경로 5: `dat.gui` CSS 전역 주입 [확인됨]

`index_nia_bundle.js`에 포함된 dat.gui 라이브러리가 전역 CSS를 DOM에 주입:

```css
/* index_nia_bundle.js 내부 - 전역 주입 */
.dg ul { list-style: none; margin: 0; padding: 0; width: 100%; }
.dg.ac { position: fixed; top: 0; left: 0; right: 0; ... }
.dg .c input[type='text'] { border: 0; margin-top: 4px; padding: 3px; }
/* ... 수십 개의 .dg 셀렉터 */
```

이 CSS는 어떤 스코핑도 없이 전역으로 적용. `.dg` 클래스가 다른 컴포넌트에 있으면 영향.

---

## 3. 침범 흐름 요약

```
[외부: 침범 소스]                              [내부: 피해 대상]
                                              
PredictiveMaintenance/Map2D.vue               niaTopology/index.vue
TransientOutage/topology2d/index.vue          
├─ :root { --focus-color } ──────────────→ 전역 CSS 변수 오염
├─ @keyframes blinking/stroke ───────────→ 전역 keyframe 덮어씀
├─ <style> (비scoped) 잔류 ──────────────→ <head>에서 CSS 경쟁
└─ map2d_untact.min.js                    
   └─ window.Map2d 덮어씀 ──────────────→ niaTopology Map2d 인스턴스 오염
   └─ 내장 CSS 전역 주입 ───────────────→ SVG 스타일 충돌 가능

index_nia_bundle.js (niaTopology 전용)    
└─ dat.gui CSS 전역 주입 ───────────────→ .dg 셀렉터 전역 오염 (역방향)
```

---

## 4. niaTopology 자체 CSS 구조 (참고용 — 수정 대상 아님)

niaTopology의 CSS는 자체적으로 몇 가지 비이상적 구조를 가지고 있으나, 이번 CSS 깨짐의 **원인이 아님**:

| 항목 | 위치 | 설명 |
|------|------|------|
| `body` 셀렉터 | map3d.scss:42-50 | scoped + `::v-deep` 안에서 `body`를 정의하나, `body`는 `.niaTopology`의 하위가 아니므로 **실제로 적용되지 않음** |
| `componentWapper` 중복 | map3d.scss:52, 465 | 같은 파일에 2번 정의되나, 동일 스코프 내 후자 우선이므로 동작에 영향 제한적 |
| `scoped` + `::v-deep` | index.vue:897 | 이중 스코핑이지만 현재 동작하고 있는 구조 |

**이들은 리팩토링 대상이지 버그 원인이 아니다.**

---

## 5. 해결 방안 (올바른 방향: 외부 스타일 격리)

### 방안 A: 예지보전/순단장애 `<style>`에 `scoped` 추가 [권장]

**난이도**: 낮음 | **효과**: 높음 | **위험도**: 낮음

두 외부 컴포넌트의 스타일 블록에 `scoped`를 추가하여 전역 유출을 차단.

수정 대상 파일 2개:

```vue
<!-- 변경 전: PredictiveMaintenance/component/Map2D.vue:185 -->
<style lang="scss">

<!-- 변경 후 -->
<style lang="scss" scoped>
```

```vue
<!-- 변경 전: TransientOutage/topology2d/index.vue:185 -->
<style lang="scss">

<!-- 변경 후 -->
<style lang="scss" scoped>
```

**주의점**:
- Map2D 라이브러리가 JS로 생성하는 SVG 요소에는 `data-v-xxxx` 속성이 없음
- `scoped` 추가 후 해당 컴포넌트의 SVG 스타일이 적용 안 될 수 있음
- 이 경우 `.PmmTopology2D`/`.MbaTopology2D` 래퍼 안에서 `::v-deep`을 추가해야 함

```scss
// scoped 추가 후 Map2D가 동적 생성한 SVG 요소에 스타일 적용
.PmmTopology2D::v-deep {
  .nodes { ... }
  .links { ... }
}
```

### 방안 B: `:root` CSS 변수를 클래스 래퍼 안으로 이동 [즉시 가능]

```scss
// 변경 전 (전역 유출)
:root {
    --focus-color: rgba(0, 0, 0, 0.7);
}
.PmmTopology2D { ... }

// 변경 후 (래퍼 내부로 이동)
.PmmTopology2D {
    --focus-color: rgba(0, 0, 0, 0.7);   // CSS 변수는 하위 요소에 상속됨
    ...
}
```

### 방안 C: `window.Map2d` 덮어쓰기 방지 [중기]

niaTopology에서 `index_nia_bundle.js` 로드 시 Map2d 인스턴스를 지역 변수에 보관:

```javascript
// 변경 전 (index.vue:384)
if (!window.Map2d) { ... }
const map = new window.Map2d()

// 변경 후 — 로드 직후 참조 보존
created() {
  this.addScript(['./extlib/map2d/lib/index_nia_bundle.js'], false)  // sync
  this._Map2dRef = window.Map2d  // 즉시 참조 보존
}
// initMap에서 보존된 참조 사용
const map = new this._Map2dRef()
```

### 방안 D: 예지보전/순단장애의 전역 `@keyframes` 이름 변경 [중기]

```scss
// 변경 전 (PredictiveMaintenance, TransientOutage 모두)
@keyframes blinking { ... }
@keyframes stroke { ... }

// 변경 후 — 컴포넌트별 고유 접두사
@keyframes pmm-blinking { ... }    // 예지보전
@keyframes pmm-stroke { ... }

@keyframes mba-blinking { ... }    // 순단장애
@keyframes mba-stroke { ... }
```

---

## 6. 권장 조치 순서

| 순서 | 작업 | 대상 파일 | 효과 |
|------|------|----------|------|
| 1 | `:root` → 클래스 래퍼 안으로 이동 | Map2D.vue, TransientOutage/index.vue | 전역 CSS 변수 유출 차단 |
| 2 | `<style>` → `<style scoped>` + `::v-deep` 추가 | Map2D.vue, TransientOutage/index.vue | 비scoped 스타일 전역 잔류 차단 |
| 3 | 브라우저 확인 | - | 효과 검증 |
| 4 | (필요시) `@keyframes` 이름 고유화 | Map2D.vue, TransientOutage/index.vue | keyframe 충돌 방지 |
| 5 | (필요시) `window.Map2d` 참조 보존 | niaTopology/index.vue | 번들 충돌 방지 |

**원칙**: niaTopology는 건드리지 않는다. 문제의 원인인 외부 컴포넌트를 격리한다.

---

## 7. 디버깅 가이드

브라우저 DevTools에서 확인할 항목:

```
1. 예지보전 또는 순단장애 페이지를 먼저 방문한다
2. 대시보드로 이동하여 토폴로지 팝업을 연다
3. DevTools > Elements > <head> 안에서:
   - .PmmTopology2D 또는 .MbaTopology2D 관련 <style> 태그가 잔류하는지 확인
   - dat.gui 관련 .dg 셀렉터 <style> 태그 존재 여부 확인
4. 토폴로지 SVG 요소를 선택하고 Computed 탭에서:
   - 어떤 소스의 CSS가 적용되고 있는지 확인
   - 취소선(override)이 표시된 스타일의 출처 확인
5. 예지보전 페이지를 방문하지 않고 바로 토폴로지 팝업을 열었을 때와 비교
```

---

## 8. 이전 유사 현상과의 관계

순단장애·예지보전 기능을 가져왔을 때 CSS가 깨진 원인은 지금과 동일한 구조:

1. 두 컴포넌트의 비scoped 스타일이 `<head>`에 주입되어 전역으로 잔류
2. `map2d_untact.min.js`가 `window.Map2d`를 덮어써서 niaTopology의 Map2d 동작 변경
3. 동일 이름 `@keyframes`가 여러 컴포넌트에서 중복 정의

**해결 방향은 항상 동일: 외부 컴포넌트의 스타일을 격리하는 것.**

---

Version: 2.0.0
