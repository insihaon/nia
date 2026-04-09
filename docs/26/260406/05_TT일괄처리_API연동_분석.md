# TT일괄처리 — API 연동 현황 분석

## 현재 상태

`ModalRcaBatchProcessing.vue`의 인지/조치/마감 버튼은 **프론트엔드 그리드만 수정**하고 API 호출이 없음.

## DB SELECT vs 그리드 컬럼 비교

### DB에서 반환하는 컬럼 (SELECT_TICKET_ALARM_BATCH_LIST)

| DB 컬럼 | alias | 그리드 prop | 출처 테이블 |
|---------|-------|------------|------------|
| TICKET_ID | TICKETNO | `ticketno` | TB_MBA_TICKET_CURRENT |
| FAULT_TIME | ALARMTIME | `alarmtime` | TB_MBA_TICKET_CURRENT |
| SYSNAMEA | SYSNAME | `sysname` | TB_MBA_TICKET_CABLE |
| TRUNK_NAME | SITE | `site` | TB_MBA_TICKET_CURRENT |
| STATUS | STATUS | `status` | TB_MBA_TICKET_CURRENT |

### DB에서 안 오는 컬럼 (프론트에서 로컬 채움)

| 그리드 prop | 이름 | 값 설정 방식 | 원래 출처 (추정) |
|------------|------|-------------|----------------|
| `causesite` | 원인국소 | ACK 시 하드코딩 `'공군작전지역국통사통'` | TOPAS TT |
| `brokencause` | 고장원인 | ACK 시 하드코딩 `'회선시험: 전송로시험'` | TOPAS TT |
| `responsibility` | 책임분류 | ACK 시 하드코딩 `'고객사유: 회선시험'` | TOPAS TT |
| `fixed` | 수리내용 | FIX 시 사용자 입력 (ModalRcaBatchFix) | TOPAS TT |
| `receivetime` | 마감일시 | FIN 시 `new Date().getTime()` | TOPAS TT |

## 기존 NIA API (USER_PROCESS_MBA)

`ModalRcaProcessHandling`에서 사용하는 단건 인지/마감 API:

| 동작 | STATUS | 저장 필드 |
|------|--------|----------|
| ACK | ACK | HANDLING_USER, HANDLING_DEPT, HANDLING_AGENCY, HANDLING_TIME, HANDLING_CONTENT |
| FIN | FIN | HANDLING_FIN_USER, HANDLING_FIN_DEPT, HANDLING_FIN_AGENCY, HANDLING_ACK_TIME, HANDLING_FIN_CONTENT |

→ `causesite`, `brokencause`, `responsibility`, `fixed` 컬럼은 **NIA DB에 없음**

## 각 버튼별 API 연동 가능성

### ACK (인지) — API 연동 가능

- `USER_PROCESS_MBA` 재사용 가능
- `STATUS = 'ACK'`, `HANDLING_CONTENT`에 인지 내용 기록
- 하드코딩 값 (`causesite` 등)은 TOPAS 전용이라 NIA DB에는 불필요
- 프론트 그리드 표시용으로만 유지하고, API는 STATUS 변경 + 사용자 정보 기록

### FIX (조치) — API 연동 가능 (주의 필요)

- `USER_PROCESS_MBA`에 FIX 전용 분기 없음
- 방안 A: ACK 상태로 `HANDLING_CONTENT`에 조치내용 기록
- 방안 B: 별도 UPDATE 쿼리 추가 (`HANDLING_CONTENT` 업데이트 전용)
- 현재 STATUS가 이미 ACK인 상태에서 조치하므로, STATUS 변경 없이 CONTENT만 업데이트가 자연스러움

### FIN (마감) — API 연동 가능

- `USER_PROCESS_MBA` 재사용 가능
- `STATUS = 'FIN'`, `HANDLING_FIN_CONTENT`에 마감 내용 기록
- `receivetime`은 서버에서 NOW()로 자동 설정됨

## 위험 요소

1. **일괄 호출 부하**: 선택된 row 수만큼 API 반복 호출 → 대량 선택 시 부하 가능
   - 대안: 백엔드에 BATCH 전용 API 추가 (TICKET_ID 배열 받아서 일괄 UPDATE)
2. **TOPAS 연동 부재**: 원래 TOPAS TT와 연동하는 기능이었으나, NIA 단독으로는 causesite/brokencause 등은 의미 없음
3. **STATUS 전이 검증**: 이미 FIN인 티켓을 다시 ACK하는 등의 예외 케이스 처리 필요

## 결론

| 버튼 | API 연동 | 방법 | 비고 |
|------|---------|------|------|
| 인지 (ACK) | 가능 | `apiUserProcess(param, 'mba')` 반복 호출 | STATUS→ACK |
| 조치 (FIX) | 가능 (주의) | HANDLING_CONTENT 업데이트 | STATUS 변경 없이 조치내용만 기록 |
| 마감 (FIN) | 가능 | `apiUserProcess(param, 'mba')` 반복 호출 | STATUS→FIN |
