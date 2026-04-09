# transmissionFaultAnalytics.xml — 쿼리 전수 점검 및 정리

> 점검일: 2026-04-06
> 결과: 1108행 → 548행 (560행, 50% 삭제)

## 작업 내용

### 1. 이미 대체되어 있으므로 삭제

| 삭제 대상 | MBA 대체 쿼리 | 비고 |
|----------|-------------|------|
| `SUB_TICKET` (sql fragment) | `SELECT_MBA_TICKET_CUR_LIST` 자체 WHERE 사용 | RCA 전용 공통 서브쿼리 |
| `SUB_TICKET_MIN_DAY` / `SUB_TICKET_MAX_DAY` | SUB_TICKET 전용 | 함께 삭제 |
| `SELECT_TICKET_CUR_LIST` | `SELECT_MBA_TICKET_CUR_LIST` | RCA 전송망 대시보드 전용 |
| `SELECT_TICKET_ROOT_ALARM_LIST` | `SELECT_MBA_TICKET_ROOT_ALARM_LIST` | RCA 경보 상세 |
| `SELECT_PMM_TOPOLOGY_LIST` 구 쿼리 블록 (76행) | 현재 쿼리 (WITH base AS ...) | TOPAS 의존 쿼리, 현재 쿼리로 대체됨 |

Java 매퍼에서도 삭제:
- `SELECT_TICKET_CUR_LIST` 인터페이스
- `SELECT_TICKET_ROOT_ALARM_LIST` 인터페이스

### 2. 대체되지 않았으나 불필요하므로 삭제

| 쿼리 | 주석 컬럼 | 판정 | 사유 |
|------|----------|------|------|
| `SELECT_MBA_LOW_ALARM_LIST` | `officescode`, `officename` | 삭제 | 테이블에 컬럼 자체 없음 (TOPAS 잔재) |
| `SELECT_MBA_TOPOLOGY_LIST` | `TRANSCIRCUITSEQ`, `TRANSCIRCUITNAME` | 삭제 | 영향회선. 테이블에 컬럼 없음 |
| `SELECT_MBA_TOPOLOGY_LIST` | `NESCODE` | 삭제 | `sysname`으로 대체됨 |
| `SELECT_MBA_TOPOLOGY_LIST` | `NAME_CODE` | 삭제 | 테이블에 컬럼 없음 |
| `SELECT_MBA_TOPOLOGY_LIST` | `EQUIP_TYPE` | 삭제 | 테이블에 컬럼 없음. 필요 시 `tb_equip_mst.model` JOIN으로 대체 가능 (예지보전 `SELECT_TICKET_PMM_LIST`에서 이미 이 패턴 사용 중) |
| `SELECT_MBA_TOPOLOGY_LIST` | `OFFICESCODE`, `OFFICENAME` | 삭제 | NIA에 사무소 데이터 없음 |
| `SELECT_TICKET_PMM_LIST` | `AND rop.monitored_object like '%IN%'` | 삭제 | 구 필터 조건, 현재 불필요 |

### 3. 엔진 개발 대기 — 주석 유지

| 쿼리 | 상태 | 사유 |
|------|------|------|
| `SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST` | `WHERE 1 = 0` + 원본 주석 유지 | 엔진 측 영향회선 테이블 개발 대기. 요구사항 참조용으로 주석 보존 |

## 정리 후 남은 쿼리 목록 (13개)

| # | 쿼리 ID | 용도 | 상태 |
|---|---------|------|------|
| 1 | `SELECT_MBA_REPEATER_SLOT_DATA` | 반복기 슬롯 데이터 | 정상 |
| 2 | `SELECT_MBA_LOW_ALARM_LIST` | 저광레벨 경보 상세 | 정상 (주석 제거됨) |
| 3 | `SELECT_MBA_TOPOLOGY_LIST` | 순단장애 토폴로지 | 정상 (주석 제거됨) |
| 4 | `SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST` | 영향회선 (미구현) | WHERE 1=0 |
| 5 | `SELECT_TICKET_PMM_LIST` | 예지보전 전표 조회 | 정상 (주석 제거됨) |
| 6 | `SELECT_PMM_TOPOLOGY_LIST` | 예지보전 토폴로지 | 정상 (구 쿼리 삭제됨) |
| 7 | `SELECT_MBA_PREDICTIVE_REVIEW` | 검토 의견 조회 | 정상 |
| 8 | `UPDATE_MBA_PREDICTIVE_REVIEW` | 검토 의견 등록/수정 | 정상 |
| 9 | `DELETE_MBA_PREDICTIVE_REVIEW` | 검토 의견 삭제 | 정상 |
| 10 | `SELECT_TICKET_ALARM_BATCH_LIST` | TT일괄처리 리스트 | 정상 |
| 11 | `SELECT_TICKET_HANDLING_CURRENT_LIST` | 처리 상태 조회 | 정상 |
| 12 | `SELECT_MBA_TICKET_CUR_LIST` | 순단장애 전표 목록 | 정상 |
| 13 | `SELECT_MBA_TICKET_ROOT_ALARM_LIST` | 순단장애 경보 상세 | 정상 |
| 14 | `USER_PROCESS_MBA` | 인지/마감 처리 | 정상 |
