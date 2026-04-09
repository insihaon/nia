# MBA-E4 분류 코드 수정 및 잔여 미해결 항목

> 적용일: 2026-04-03

---

## MBA-E4 수정 내역

### 현상

마감 처리 시 원인분류/시설분류 드롭다운이 비어있어 마감 불가.

### 원인

원본 SQL이 참조하던 `rca.code_reason`, `rca.code_facility` 테이블이 DB에 없음 (레거시 미마이그레이션).

### 수정

#### DB: nia.tb_code_cd 데이터 추가 (기존 테이블 활용)

테이블 `nia.tb_code_cd`는 이미 존재 (컬럼: `category_cd, lvl, higher_cd, cd, cd_nm, cd_desc`).

추가 데이터:

| category_cd | 건수 | 내용 |
|-------------|:----:|------|
| REASON | 37건 | 원인분류 3단계 (LV1: 3, LV2: 9, LV3: 25) |
| CHARGE | 15건 | 시설분류 2단계 (LV1: 4, LV2: 11) |

생성 쿼리: `09_MBA-E4_분류코드_생성쿼리.md` 참조

#### SQL: transmissionFaultAnalytics.xml — 5개 쿼리 교체

```xml
<!-- LV1: 직접 조회 -->
SELECT cd_nm AS ITEM FROM nia.tb_code_cd
WHERE category_cd = 'REASON' AND lvl = '1' ORDER BY cd

<!-- LV2, LV3: higher_cd JOIN으로 상위 코드명 기반 하위 조회 -->
SELECT child.cd_nm AS ITEM FROM nia.tb_code_cd child
INNER JOIN nia.tb_code_cd parent ON child.higher_cd = parent.cd AND parent.category_cd = child.category_cd
WHERE child.category_cd = 'REASON' AND child.lvl = '2' AND parent.cd_nm = #{LEVEL1}
ORDER BY child.cd
```

Vue 수정 불필요 — `loadFaultList()`가 `res.result[].item`을 사용하며 SQL alias `AS ITEM`과 일치.

### 복구되는 기능

마감 처리 시 원인분류(3단계)/시설분류(2단계) 드롭다운 표시 → 마감 처리 가능

---

## 잔여 미해결 항목 (2건)

### MBA-E2. 영향회선 리스트

- **현상**: 영향회선 탭 항상 빈 리스트
- **원인**: 관련 테이블 DB 전체에 없음
- **필요**: 영향회선 산출 엔진 개발 (별도 프로젝트, 03_보고서 참조)
- **난이도**: 높음

### MBA-E5. 장비 유형 표시 (EQUIP_TYPE)

- **현상**: 전표 상세에서 장비 유형 빈칸
- **원인**: `nia.tb_topology`에 equip_type 존재하나 sysname 포맷 불일치 (한글 vs IP)
- **필요**: `'ROADM'` 고정값 대체(간이) 또는 다단 JOIN
- **��이도**: 낮음
