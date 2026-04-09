# MBA-E3 TT 일괄처리 수정 및 잔여 미해결 항목

> 적용일: 2026-04-03

---

## MBA-E3 수정 내역

### 현상

"TT 일괄처리" 모달(ModalRcaBatchProcessing.vue)에서 관련 경보 목록이 항상 빈 배열.

### 원인

원본 SQL이 참조하던 테이블들이 DB에 없음:
- `rca.tb_rca_ticket_topas_tt_current` — TOPAS TT 연동 테이블 (미존재)
- `rca.tb_alarm_cur_mst.TICKETNO` 기반 JOIN — TOPAS TT 번호 매칭 (NIA에 해당 없음)

### 수정

동일 `trunk_name`의 관련 전표를 `mba.tb_mba_ticket_current`에서 직접 조회하도록 SQL 교체.

```
파일: transmissionFaultAnalytics.xml — SELECT_TICKET_ALARM_BATCH_LIST
```

```sql
-- Before: WHERE 1 = 0 (TOPAS 테이블 미존재)

-- After: 동일 trunk_name의 전표 목록 (자기 자신 제외)
SELECT
    T.TICKET_ID AS TICKET_ID,
    T.TICKET_ID AS TICKETNO,
    TO_CHAR(T.FAULT_TIME, 'YYYY-MM-DD HH24:MI:SS') AS ALARMTIME,
    COALESCE(CABLE.SYSNAMEA, '') AS SYSNAME,
    T.TRUNK_NAME AS SITE,
    T.STATUS AS STATUS
FROM MBA.TB_MBA_TICKET_CURRENT T
LEFT OUTER JOIN (...) CABLE ON ...
WHERE T.TRUNK_NAME = (SELECT TRUNK_NAME FROM ... WHERE TICKET_ID = #{TICKET_ID})
  AND T.TICKET_ID <> #{TICKET_ID}
ORDER BY T.FAULT_TIME DESC
```

Vue(ModalRcaBatchProcessing.vue)는 수정 불필요 — 그리드 컬럼(`ticketno`, `alarmtime`, `sysname`, `site`)이 SQL alias와 일치.

### 복구되는 기능

TT 일괄처리 모달에서 동일 트렁크의 관련 전표 목록 표시

---

## 잔여 미해결 항목 (3건)

### MBA-E2. 영향회선 리스트

- **현상**: 영향회선 탭 항상 빈 리스트
- **원인**: 관련 테이블 DB 전체에 없음
- **필요**: 영향회선 산출 엔진 개발 (별도 프로젝트, 03_보고서 참조)
- **난이도**: 높음

### MBA-E4. 원인분류/시설분류 코드

- **현상**: 마감 시 분류 드롭다운 빈 목록 → 마감 불가
- **원인**: 코드 마스터 테이블 DB 전체에 없음 (저장 컬럼은 `tb_mba_ticket_handling`에 존재)
- **필요**: 코드 마스터 테이블 생성 + 분류 코드 데이터 INSERT (DBA 작업)
- **난이도**: 중간

### MBA-E5. 장비 유형 표시 (EQUIP_TYPE)

- **현상**: 전표 상세에서 장비 유형 빈칸
- **원인**: `nia.tb_topology`에 equip_type 존재하나 sysname 포맷 불일치 (한글 vs IP)
- **필요**: `'ROADM'` 고정값 대체(간이) 또는 다단 JOIN
- **난이도**: 낮음
