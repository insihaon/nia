# 순단장애(MBA) 시스템 흐름

> 순단장애 = MBA (Momentary Breakoff Analysis)
> 실시간 광레벨 급변(3dB 이상)을 감지하여 전표를 자동 생성하고, 운용자가 인지/마감 처리하는 시스템

---

## 1. 전체 흐름 요약

```
[엔진] 광레벨 급변 감지 → RabbitMQ → 전표 생성 → WebSocket → [UI] 실시간 표시
                                                                  ↓
                                                          운용자 인지/마감 처리
                                                                  ↓
                                                         [BE] DB 저장 (PostgreSQL)
```

---

## 2. 엔진 흐름 (Engine)

프로젝트: `apps/EngineNiaOpticalPm/`

### Step 1: 저광레벨 전처리

```
파일: preprocessor/prepro-mba/src/.../service/ticket/TicketDataService.java
함수: setTicketData()

[수집 데이터 수신]
  ↓
트렁크별 UP/DOWN 방향 그룹핑
  ↓
저광레벨 판정: |VALUE_MAX - VALUE_MIN| >= 3dB
  ↓
MIN/MAX 라우트번호 추출, REPEATER 노드 필터링
  ↓
EngineLowPmDataListDto 생성
```

### Step 2: RabbitMQ 전송 (전처리기 → 엔진)

```
파일: preprocessor/prepro-mba/src/.../amqp/MbaEnginePrdAmqp.java
함수: sendMessageCmd(EngineLowPmDataListDto)

Exchange: nia.exchangeeEgineMbaTicketDirectly
Queue:    ${spring.rabbitmq.engineMbaTicketQueue}

Payload:
  - lowPmDataDtoList       (현재 저광 데이터)
  - lowPmHistDataDtoList   (과거 15분 이력)
```

### Step 3: 전표 생성

```
파일: engine/engine-ticket-mba/src/.../listener/MbaTicketMsgListener.java
함수: onMessage()
  → @RabbitListener(queues = "${spring.rabbitmq.engineMbaTicketQueue}")

파일: engine/engine-ticket-mba/src/.../service/ticket/TicketCreateService.java
함수: createMbaTicket()
  ├─ createTicketEntity()       → TB_MBA_TICKET_CURRENT 생성
  ├─ createTicketCableEntity()  → TB_MBA_TICKET_CABLE 생성 (A-Z 구간)
  └─ 저광 이력 저장             → TB_ROADM_LOW_OPTICAL_PERFORMANCE

전표 초기 상태:
  ticketType:     "PF" (Performance Fault)
  rootCauseType:  "MomentaryBreakoff"
  status:         "INIT"
```

### Step 4: UI 실시간 알림

```
파일: engine/engine-ticket-mba/src/.../amqp/EngineToUiTicketPrdAmqp.java
함수: sendMessageCmd(RcaEngineResultDto)

RcaEngineResultDto:
  - ticketId
  - eventType: "TICKET_NEW"
  - ticketType: "PF"

→ WebSocket → TransientOutageList.vue에서 수신
```

---

## 3. UI 흐름 (Frontend - Vue)

프로젝트: `apps/Web2024/FE/`

### 3.1 파일 구조

```
src/views-nia/pages/TransientOutage/
  TransientOutageList.vue                  ← 메인 화면 (전표 목록)
  modal/
    ModalRcaProcessHandling.vue            ← 인지/마감 처리 모달
    ModalRcaBatchProcessing.vue            ← TT 일괄처리 모달
    ModalRcaBatchFix.vue                   ← 일괄 조치사항 입력 모달
  topology2d/
    index.vue                              ← 2D 토폴로지 맵
    component/Map2D.vue                    ← 맵 렌더링 컴포넌트
```

### 3.2 메인 화면 흐름 (TransientOutageList.vue)

```
[페이지 로드]
  mounted()
    └─ loadTicketList()                                              ← :441
         └─ apiRcaRequest('SELECT_MBA_TICKET_CUR_LIST', params)
              └─ store.dispatch('untact/insertTicketMbaList', list)   ← :447

[실시간 전표 수신 - WebSocket]
  onReceivedMbaTicketEvent(event)                                    ← :397
    └─ store.dispatch('untact/insertTicketMbaList', item)            ← :413
         └─ 화면 자동 갱신 + 경보음(순단장애.mp3) 재생

[전표 카드 클릭 - 상세 보기]
  openDetail(ticket)                                                 ← :455
    └─ loadAzList(ticket)                                            ← :472
         └─ apiRcaRequest('SELECT_MBA_TICKET_ROOT_ALARM_LIST')
              └─ A-Z 시스템 정보 표시

[전표 더블클릭 - 토폴로지 로드]
  onDoubleClickTicket(row)                                           ← :277
    └─ selectedTicket = row
         └─ topology2d/index.vue watch 트리거
              └─ loadTopology2d()                                    ← :148
                   ├─ apiRcaRequest('SELECT_MBA_TICKET_ROOT_ALARM_LIST')
                   ├─ apiRcaRequest('SELECT_MBA_TOPOLOGY_LIST')
                   └─ emit('loadList', { nodes, links })
                        ├─ loadRepeaterSlot()                        ← :507
                        │    └─ apiRcaRequest('SELECT_MBA_REPEATER_SLOT_DATA')
                        ├─ loadAlarmList()                           ← :485
                        │    └─ apiRcaRequest('SELECT_MBA_LOW_ALARM_LIST')
                        └─ loadInfluencecircuitList()                ← :494
                             └─ apiRcaRequest('SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST')
```

### 3.3 인지/마감 처리 흐름 (ModalRcaProcessHandling.vue)

```
[인지 버튼 클릭]   → onOpenModalProcess('ack', row)                  ← :545
[마감 버튼 클릭]   → onOpenModalProcess('fin', row)                  ← :545
  └─ loadAzList(ticket)
       └─ modalProcess.open({ processType, ticket, systemCoreList })
            └─ ModalRcaProcessHandling.onOpen(model, actionMode)     ← :289
                 ├─ setPageType()                                    ← :374
                 │    └─ isMba = (rootCauseType === 'MomentaryBreakoff')
                 │
                 ├─ [ack 초기가 아닌 경우] loadSavedData()            ← :346
                 │    └─ apiRcaRequest('SELECT_TICKET_HANDLING_CURRENT_LIST')
                 │
                 └─ [fin 또는 cable] loadFaultList(type, lv)         ← :321
                      ├─ apiRcaRequest('SELECT_FAULTREASON_LV1_LIST')
                      ├─ apiRcaRequest('SELECT_FAULTREASON_LV2_LIST')
                      ├─ apiRcaRequest('SELECT_FAULTREASON_LV3_LIST')
                      ├─ apiRcaRequest('SELECT_FAULTCHARGE_LV1_LIST')
                      └─ apiRcaRequest('SELECT_FAULTCHARGE_LV2_LIST')

[처리 완료 버튼 클릭]
  handleClickProcess()                                               ← :475
    └─ getProcessParam()                                             ← :407
         └─ apiUserProcess(param, 'equip')
              └─ POST /modify { sqlId: 'USER_PROCESS_MBA' }
```

### 3.4 TT 일괄처리 흐름 (ModalRcaBatchProcessing.vue)

```
[TT일괄처리 버튼 클릭]
  onOpenModalProcess('batch', row)                                   ← :545
    └─ modalRcaBatchProcessing.open({ ticket })
         └─ loadBatchTicketList()                                    ← :101
              └─ apiRcaRequest('SELECT_TICKET_ALARM_BATCH_LIST')

[조치 버튼 클릭]
  openFixModal()                                                     ← :150
    └─ ModalRcaBatchFix.open()
         └─ 사용자 조치사항 입력 (최대 120자)
              └─ handleClickFix()                                    ← :72
                   └─ emit('closeModal', fixValue)
                        └─ handleClickProcess('FIX', '조치')         ← :109
```

---

## 4. 백엔드 흐름 (BE - Spring Boot)

프로젝트: `apps/Web2024/BE/app-nia/`

### API 호출 경로

```
Vue (apiRcaRequest)
  └─ POST /selectList
       Header: { sqlId: 'SELECT_MBA_TICKET_CUR_LIST' }
       Body: { LIMIT, FROM, TO, TICKET_ID }
         ↓
AbsDataController.postSelectList()
  파일: common-web/src/.../controller/AbsDataController.java:103
         ↓
BaseDataService.selectList(sqlId, map)
         ↓
transmissionFaultAnalytics.SELECT_MBA_TICKET_CUR_LIST(map)
  파일: app-nia/src/.../mapper/db2nd/transmissionFaultAnalytics.java
         ↓
transmissionFaultAnalytics.xml → SQL 실행
  파일: app-nia/src/main/resources/mapper/db2nd/transmissionFaultAnalytics.xml
         ↓
PostgreSQL (db2nd) → 결과 반환
```

### SQL ID 목록

| SQL ID | XML 라인 | 테이블 | 용도 |
|--------|---------|--------|------|
| `SELECT_MBA_TICKET_CUR_LIST` | :1015 | `MBA.TB_MBA_TICKET_CURRENT`, `TB_MBA_TICKET_CABLE` | 전표 목록 |
| `SELECT_MBA_TICKET_ROOT_ALARM_LIST` | :1088 | `MBA.TB_MBA_TICKET_CABLE` | 경보 상세 (A-Z) |
| `SELECT_MBA_LOW_ALARM_LIST` | :487 | `MBA.TB_ROADM_LOW_OPTICAL_PERFORMANCE` | 저광레벨 경보 |
| `SELECT_MBA_TOPOLOGY_LIST` | :524 | `MBA.TB_ROADM_TRUNK_TOPOLOGY` | 토폴로지 구조 |
| `SELECT_MBA_REPEATER_SLOT_DATA` | :479 | - | 반복기 슬롯 |
| `SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST` | :542 | - | 영향 회선 |
| `SELECT_TICKET_HANDLING_CURRENT_LIST` | :963 | `MBA.TB_MBA_TICKET_HANDLING` | 이전 처리 이력 |
| `SELECT_TICKET_ALARM_BATCH_LIST` | :876 | - | 일괄처리 경보 목록 |
| `SELECT_FAULTREASON_LV1_LIST` | :901 | - | 원인분류 1단계 |
| `SELECT_FAULTREASON_LV2_LIST` | :909 | - | 원인분류 2단계 |
| `SELECT_FAULTREASON_LV3_LIST` | :923 | - | 원인분류 3단계 |
| `SELECT_FAULTCHARGE_LV1_LIST` | :941 | - | 시설분류 1단계 |
| `SELECT_FAULTCHARGE_LV2_LIST` | :949 | - | 시설분류 2단계 |
| `USER_PROCESS_MBA` | :1105 | `TB_MBA_TICKET_HANDLING`, `TB_MBA_TICKET_CURRENT` | 인지/마감 처리 |

---

## 5. 전표 상태 흐름

```
INIT (초기)  →  ACK (인지)  →  FIN (마감)
  │                │               │
  │                ├─ 인지 사유     ├─ 원인분류 (3단계)
  │                ├─ 정확도       ├─ 시설분류 (2단계)
  │                └─ 처리 내용    ├─ 마감코드
  │                               └─ 마감 내용
  │
  └─ 경보음 재생 (순단장애.mp3)
```

---

## 6. DB 테이블

| 스키마 | 테이블 | 용도 |
|--------|--------|------|
| MBA | `TB_MBA_TICKET_CURRENT` | 전표 마스터 |
| MBA | `TB_MBA_TICKET_CABLE` | A-Z 구간 정보 |
| MBA | `TB_MBA_TICKET_HANDLING` | 처리 상태 이력 |
| MBA | `TB_ROADM_LOW_OPTICAL_PERFORMANCE` | 저광레벨 경보 이력 |
| MBA | `TB_ROADM_TRUNK_TOPOLOGY` | 캐리어 토폴로지 |
