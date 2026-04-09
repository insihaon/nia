# TT일괄처리 — API 연동 구현

## 변경 파일

| 파일 | 변경 내용 |
|------|----------|
| `transmissionFaultAnalytics.xml` | SELECT에 HANDLING JOIN 추가, UPSERT에 REASON/CHARGE_LEVEL1 조건부 추가 |
| `ModalRcaBatchProcessing.vue` | API 연동, TOPAS 하드코딩 제거, 그리드 컬럼 정리 |
| `ModalRcaBatchFix.vue` | closeModal 이벤트 버그 수정 (04 문서 참조) |

## DB 검증 결과 (2026-04-06)

### 테이블 데이터 확인

| 테이블 | 상태 | 비고 |
|--------|------|------|
| `mba.tb_mba_ticket_current` | 데이터 존재 | INIT 상태 다수 |
| `mba.tb_mba_ticket_handling` | 데이터 0건 | 아직 인지/마감 처리 이력 없음 |
| `nia.tb_code_cd (REASON)` | 3건 | 전송설비, 외부요인, 기타 |
| `nia.tb_code_cd (CHARGE)` | 4건 | 전송, 선로, 전원, 기타 |

### 쿼리 검증

| 쿼리 | 결과 |
|------|------|
| SELECT (HANDLING JOIN) | 정상 — 5건 반환 |
| UPSERT (EXPLAIN) | 정상 — 실행계획 확인 |

## 순단장애(MBA) vs 선로장애(CableCut) 조건 분석

기존 단건 마감 팝업(`ModalRcaProcessHandling`)의 `root_cause_type` 분기:

| `root_cause_type` | `isMba` | `isCable` | 원인분류/시설분류 | LEVEL 파라미터 |
|---|---|---|---|---|
| `MomentaryBreakoff` (순단장애) | true | false | UI 숨김, 선택 불가 | 전송하지 않음 |
| `CableCut` (선로장애) | false | true | el-select 활성화 | REASON_LEVEL1~3, CHARGE_LEVEL1~2 전송 |
| 기타 | false | false | el-select 활성화 | 전송 |

**TT일괄처리 모달은 순단장애(TransientOutageList) 전용** → 대상 티켓이 전부 MBA

→ **원인분류/시설분류를 할당하면 안 됨** (기존 단건 인지/마감과 동일하게 미할당)

## 변경 상세

### 1. 백엔드: SELECT_TICKET_ALARM_BATCH_LIST — HANDLING JOIN 추가

```sql
-- 추가된 컬럼 (DB에 저장된 처리 이력을 그리드에 반영)
, T.TICKET_TYPE                                          AS TICKET_TYPE
, H.REASON_LEVEL1                                        AS BROKENCAUSE
, H.CHARGE_LEVEL1                                        AS RESPONSIBILITY
, H.HANDLING_CONTENT                                     AS FIXED
, TO_CHAR(H.HANDLING_ACK_TIME, 'YYYY-MM-DD HH24:MI:SS') AS RECEIVETIME

-- 추가된 JOIN
LEFT OUTER JOIN MBA.TB_MBA_TICKET_HANDLING H ON T.TICKET_ID = H.TICKET_ID
```

### 2. 백엔드: USER_PROCESS_MBA — REASON/CHARGE_LEVEL1 조건부 추가

```xml
<if test="REASON_LEVEL1 != null">, REASON_LEVEL1</if>
<if test="CHARGE_LEVEL1 != null">, CHARGE_LEVEL1</if>
```

기존 영향도:

| 호출처 | REASON_LEVEL1 전송? | 영향 |
|--------|-------------------|------|
| 단건 인지/마감 — MBA (순단장애) | 전송 안 함 | `null` → if 스킵 → 영향 없음 |
| 단건 인지/마감 — CableCut (선로장애) | 전송함 (기존 코드 424~430행) | **기존에 전송은 하고 있었으나 쿼리에서 안 받고 있었음** → 이번 수정으로 정상화 |
| 일괄처리 — MBA | 전송 안 함 | `null` → if 스킵 → 영향 없음 |

### 3. 프론트: ModalRcaBatchProcessing.vue

#### TOPAS 하드코딩 값 전부 제거

| Before (TOPAS) | After |
|----------------|-------|
| `'공군작전지역국통사통'` (causesite) | 제거 |
| `'회선시험: 전송로시험'` (brokencause) | 제거 |
| `'고객사유: 회선시험'` (responsibility) | 제거 |

#### 그리드 컬럼 정리

| 컬럼 | 상태 | 이유 |
|------|------|------|
| `causesite` (원인국소) | 주석처리 | 엔진 미개발 (상세 주석 포함) |
| `brokencause` (고장원인) | 주석처리 | 순단장애(MBA)에서 미사용 |
| `responsibility` (책임분류) | 주석처리 | 순단장애(MBA)에서 미사용 |
| `status` (상태) | 신규 추가 | 인지/마감 후 상태 변경 즉시 확인용 |
| `fixed` (수리내용) | 유지 | HANDLING_CONTENT에서 조회 |
| `receivetime` (마감일시) | 유지 | HANDLING_ACK_TIME에서 조회 |

#### API 연동 방식

| 버튼 | API | STATUS | 파라미터 |
|------|-----|--------|---------|
| 인지 (ACK) | `apiUserProcess(param, 'mba')` | ACK | 사용자 정보만 (LEVEL 미할당) |
| 조치 (FIX) | `apiUserProcess(param, 'mba')` | ACK | HANDLING_CONTENT = 조치내용 |
| 마감 (FIN) | `apiUserProcess(param, 'mba')` | FIN | 마감 사용자 정보 |

처리 후 `loadBatchTicketList()` 리로드로 그리드에 즉시 반영.

## 엔진 개발이 필요한 항목

| 컬럼 | 현재 | 엔진 개발 후 |
|------|------|-------------|
| 원인국소 (`causesite`) | 주석처리 (NIA DB에 컬럼 자체 없음) | 엔진이 원인국소를 DB에 저장하면 그리드 활성화 |
| 고장원인 (`brokencause`) | 순단장애에서 미사용 | 엔진이 자동 판정하면 활성화 가능 |
| 책임분류 (`responsibility`) | 순단장애에서 미사용 | 엔진이 자동 판정하면 활성화 가능 |

**참고**: 고장원인/책임분류는 선로장애(CableCut)에서는 사용자가 수동 선택하는 구조가 이미 구현되어 있음 (ModalRcaProcessHandling의 classificationForm). 순단장애용 자동 판정이 필요한 경우에만 엔진 개발 대상.
