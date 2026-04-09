# 순단장애 / 예지보전 모달 — NIA 스타일 통일

## 변경 목적

순단장애/예지보전 모달 4개는 다른 프로젝트(untact)에서 가져온 것으로, NIA 공통 스타일(`nia-dialog`)과 불일치.
기능은 그대로 유지하면서 스타일과 작동방식만 NIA 표준에 맞춤.

## NIA 공통 모달 스타일 기준

- 클래스: `nia-dialog` (정의: `src/styles/nia.scss`)
- 테두리: `border-radius: 15px`, `border: 2px solid #3d4048`
- 헤더: 높이 38px, 흰 배경, 하단 border, bold 15px
- 바디: `background: #f2f2f2`, padding 15px
- 푸터: 흰 배경, 상단 border, 높이 50px
- 버튼: `el-button size="mini"` + `type="primary/info/danger"`
- 닫기: `type="info" icon="el-icon-close"`
- 참조: `ModalSopMng.vue`, `ModalSopMngEdit.vue` 등

## 변경 파일 및 내용

### 1. ModalRcaProcessHandling.vue (순단장애 — 인지/마감)

| 항목 | Before | After |
|------|--------|-------|
| 다이얼로그 클래스 | `untact-modal` | `nia-dialog` |
| 타이틀 | `style="transform: skew(0.03deg)"` | 인라인 스타일 제거 |
| 푸터 태그 | `<span slot="footer">` + `<hr>` | `<div slot="footer">` |
| 버튼 | `class="gray-btn"` | `size="mini" type="primary/info"` |
| 글로벌 SCSS | NanumSquare, skew, gray-btn 정의 | 최소화 (process select만 유지) |
| scoped SCSS | skew transform 다수 | 제거 (nia-dialog 위임) |

### 2. ModalRcaBatchProcessing.vue (순단장애 — TT일괄처리)

| 항목 | Before | After |
|------|--------|-------|
| 다이얼로그 클래스 | `untact-modal h-100` | `nia-dialog` |
| 타이틀 | `style="transform: skew(0.03deg)"` | 인라인 스타일 제거 |
| 푸터 태그 | `<span slot="footer">` + `<hr>` | `<div slot="footer">` |
| 버튼 | `class="gray-btn"` + 컬러 아이콘 | `size="mini" type="primary/info"` |
| SCSS | 빈 글로벌 style | scoped로 변경 |

### 3. ModalRcaBatchFix.vue (순단장애 — 조치사항 입력)

| 항목 | Before | After |
|------|--------|-------|
| 다이얼로그 클래스 | `untact-modal h-100` | `nia-dialog` |
| 타이틀 | `style="transform: skew(0.03deg)"` | 인라인 스타일 제거 |
| 푸터 태그 | `<span slot="footer">` + `<hr>` | `<div slot="footer">` |
| 버튼 | `type="primary" plain` + `class="gray-btn"` | `size="mini" type="primary/info"` |
| SCSS | 빈 글로벌 style | scoped로 변경 |

### 4. ModalPredictiveReviewOpinion.vue (예지보전 — 검토 의견)

| 항목 | Before | After |
|------|--------|-------|
| 다이얼로그 클래스 | (없음) | `nia-dialog` 추가 |
| 타이틀 아이콘 | `el-icon-tickets` (간격 없음) | `mr-2 text-base` 클래스 추가 |
| 푸터 | `<hr>` + `class="closeBtn"` | `size="mini" type="info" icon="el-icon-close"` |
| 등록 버튼 | `class="completeBtn" size="medium"` | `type="primary" size="mini"` |
| scoped SCSS | border/font/skew 하드코딩 전체 | 레이아웃 최소만 유지 (nia-dialog 위임) |

## 제거된 스타일 패턴

- `transform: skew(0.03deg)` — 전체 제거 (불필요한 렌더링 트릭)
- `font-family: 'NanumSquare'` — nia-dialog 공통에서 상속
- `gray-btn` 커스텀 클래스 — `el-button type` 속성으로 대체
- `closeBtn`, `completeBtn` — NIA 표준 el-button으로 대체
- `border: 1px solid #043644` 등 하드코딩 — nia-dialog 공통 스타일로 위임
