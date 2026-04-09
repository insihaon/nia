# MBA-E5 장비 유형 표시 수정 및 잔여 미해결 항목

> 적용일: 2026-04-03

---

## MBA-E5 수정 내역

### 현상

전표 상세에서 장비 유형(ROOT_CAUSE_EQUIP_TYPEA/Z) 정보가 항상 빈칸.

### 원인

SQL에서 `NULL AS ROOT_CAUSE_EQUIP_TYPEA`, `NULL AS ROOT_CAUSE_EQUIP_TYPEZ`로 하드코딩.
`nia.tb_topology`에 `equip_type` 컬럼이 존재하나 sysname 포맷 불일치 (한글 vs IP)로 JOIN 불가.

### 수정

```
파일: transmissionFaultAnalytics.xml — SELECT_MBA_TICKET_CUR_LIST
```

```xml
<!-- Before -->
, NULL    AS ROOT_CAUSE_EQUIP_TYPEA
, NULL    AS ROOT_CAUSE_EQUIP_TYPEZ

<!-- After -->
, 'ROADM' AS ROOT_CAUSE_EQUIP_TYPEA
, 'ROADM' AS ROOT_CAUSE_EQUIP_TYPEZ
```

### 근거

DB 확인 결과:
- `mba.tb_roadm_low_optical_performance.roadm_code` = 'ROADM'만 존재
- NIA 순단장애 전표는 ROADM 장비에서만 발생
- `nia.tb_topology.equip_type` = 'ROADM', 'POTN' 중 MBA는 ROADM만 해당

Vue 수정 불필요.

### 복구되는 기능

전표 상세에서 장비 유형 'ROADM' 표시

---

## 잔여 미해결 항목 (1건)

### MBA-E2. 영향회선 리스트

- **현상**: 영향회선 탭 항상 빈 리스트
- **원인**: 관련 테이블 DB 전체에 없음
- **필요**: 영향회선 산출 엔진 개발 (별도 프로젝트, 03_보고서 참조)
- **난이도**: 높음
- **비고**: 이 항목은 UI/SQL 수정으로 해결할 수 없으며, 영향회선 산출 로직을 가진 엔진 개발이 선행되어야 함
