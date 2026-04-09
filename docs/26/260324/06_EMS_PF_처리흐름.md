# NIA 광장비 데이터 처리 Flow

> 분석일: 2026-03-24
> 대상: `apps/EngineNiaServer2021` 엔진 모듈

---

## 1. 개요

NIA 시스템에서 광장비 데이터를 처리하는 두 가지 주요 Flow가 존재한다.

| Flow | 목적 | 프로토콜 | 결과물 |
|------|------|---------|--------|
| EMS 경보 Flow | RT(Root cause Ticket) 전표 발행 | HTTP GET (syslog) | `rca.tb_rca_ticket_current` |
| PF 티켓 Flow | PF(Performance) 예지보전 티켓 발행 | TELNET (TL1/MMC) | `mba.tb_roadm_predictive_maintenance_ticket` |

---

## 2. EMS 경보 Flow (RT 티켓 발행)

### 2.1 Flow 다이어그램

```
[외부 EMS]
    │
    │ HTTP GET (syslog 데이터)
    │ http://61.252.55.180:9200/tsdn_syslog/doc/
    ▼
┌──────────────────────────┐
│  NiaLinkageApplication   │ ── 경보 연동 (syslog 수신)
│  (경보 수집/파싱)          │
└──────────┬───────────────┘
           │ RabbitMQ Queue: AlarmLinkageResultQueue
           ▼
┌──────────────────────────────┐
│  NiaAlarmSimulatorApplication │ ── 경보 시뮬레이터 (테스트/재현용)
└──────────┬───────────────────┘
           │ RabbitMQ Queue
           ▼
┌──────────────────────────────┐
│  NiaPreprocessorApplication   │ ── 경보 전처리
│  - 알람 정규화                 │    (알람 파싱, 장비 조회, 토폴로지 매핑)
│  - 장비 정보 조회 (TB_EQUIP_MST)│
│  - 토폴로지 매핑 (TB_TOPOLOGY)  │
└──────────┬───────────────────┘
           │ RabbitMQ Queue
           ▼
┌──────────────────────────────┐
│  NiaClusterApplication        │ ── 클러스터링
│  - 연관 알람 그룹화             │    (동일 장애 원인 알람 묶음)
│  - 클러스터 번호 할당            │
└──────────┬───────────────────┘
           │ RabbitMQ Queue
           ▼
┌──────────────────────────────┐
│  NiaEngineApplication         │ ── RCA 엔진
│  - 근원장애 분석 (Root Cause)   │
│  - RT 티켓 생성                │
│  - 토폴로지 기반 장애 위치 특정   │
│  → rca.tb_rca_ticket_current  │
│  → rca.tb_rca_ticket_al_current│
└──────────────────────────────┘
```

### 2.2 모듈별 역할

| 모듈 | 주요 클래스 | 역할 |
|------|-----------|------|
| **NiaLinkageApplication** | `AlarmLinkageResultPrdAmqp` | 외부 EMS에서 syslog 경보를 HTTP GET으로 수신, RabbitMQ로 전달 |
| **NiaAlarmSimulatorApplication** | - | 경보 시뮬레이터 (테스트/재현 환경용) |
| **NiaPreprocessorApplication** | `NiaAlarmHdlServiceImpl`, `PasingServiceImpl`, `RoadmPasingServiceImpl`, `PotnPasingServiceImpl` | 알람 정규화, 장비정보 조회(`NIA.TB_EQUIP_MST`), 토폴로지 매핑(`NIA.TB_TOPOLOGY`), 포트 타입 확인(`NIA.TB_EQUIP_PORT`) |
| **NiaClusterApplication** | - | 연관 알람을 하나의 클러스터로 그룹화 |
| **NiaEngineApplication** | `SingleDomainRcaServiceImpl`, `RcaTicketManagerServiceImpl` | 근원장애 분석(RCA), RT 티켓 생성, 장비별 알람 라인 할당 |

### 2.3 데이터 흐름

```
syslog 원시 데이터
  → 알람 파싱 (sysname, ptpname, alarmmsg 추출)
  → 장비 조회 (NIA.TB_EQUIP_MST에서 tid, sysname, model 확인)
  → 토폴로지 매핑 (NIA.TB_TOPOLOGY에서 양단 장비 식별)
  → 알람 정규화 (rca.al_alarm_normalizer로 표준화)
  → 클러스터링 (동일 원인 알람 그룹화)
  → RCA 분석 (근원장애 특정)
  → RT 티켓 발행 (rca.tb_rca_ticket_current INSERT)

* RT 티켓 중 alarmno가 'T'인 경보가 발행됨
```

---

## 3. PF 티켓 Flow (예지보전 티켓 발행)

### 3.1 Flow 다이어그램

```
[광장비 EMS]
    │
    │ TELNET (TL1 프로토콜)
    │ MMC 명령어: RTRV-PM (성능 데이터 수집)
    ▼
┌──────────────────────────────┐
│  NiaEmsLinkageApplication     │ ── EMS 연동 (TELNET/TL1)
│  RoadmEmsMmcServiceImpl       │
│  - roadmPmMMC()               │    (RTRV-PM 명령으로 광레벨 수집)
│  - mba.fc_set_performance_preprocessor() 호출
└──────────┬───────────────────┘
           │ RabbitMQ Queue: NiaPerformanceToAiSendIndexDirectly
           ▼
┌──────────────────────────────────────┐
│  NiaAiPerformanceLinkageApplication   │ ── AI 성능 데이터 전달
│  - 수집된 광레벨 데이터를 AI 서버로 전송  │
└──────────┬───────────────────────────┘
           │ HTTP POST: /PerformanceData
           ▼
┌──────────────────────────────┐
│  codej DJANGO Server (AI)     │ ── AI 분석 (외부 시스템)
│  - 광레벨 이상 감지             │
│  - 장애 예측 모델 실행           │
└──────────┬───────────────────┘
           │ HTTP POST: /PerformanceResult
           ▼
┌──────────────────────────────────────┐
│  NiaAiResultLinkageApplication        │ ── AI 결과 수신
│  - AI 분석 결과 수신 및 저장            │
└──────────┬───────────────────────────┘
           │ RabbitMQ Queue: NiaPerformanceIndexDirectly
           ▼
┌──────────────────────────────┐
│  NiaPerformanceApplication    │ ── 성능 데이터 처리
│  - 광성능 데이터 가공            │
│  - 일별/이력 집계               │
└──────────┬───────────────────┘
           │ RabbitMQ Queue
           ▼
┌──────────────────────────────┐
│  NiaEngineApplication         │ ── 엔진 (PF 티켓 생성)
│  - 예지보전 티켓 발행            │
│  → mba.tb_roadm_predictive_maintenance_ticket
│  → mba.tb_roadm_optical_performance_hist
│  → mba.tb_roadm_optical_performance_daily
└──────────────────────────────┘
```

### 3.2 모듈별 역할

| 모듈 | 주요 클래스/메서드 | 역할 |
|------|-----------------|------|
| **NiaEmsLinkageApplication** | `RoadmEmsMmcServiceImpl.roadmPmMMC()` | TELNET/TL1로 EMS에 접속, `RTRV-PM` 명령으로 15분 단위 광레벨(AMPPWR) 수집. 수집 후 `mba.fc_set_performance_preprocessor()` 호출하여 전처리 |
| **NiaAiPerformanceLinkageApplication** | - | 수집된 성능 데이터를 HTTP POST로 Django AI 서버에 전달 |
| **codej DJANGO Server** | `/PerformanceData`, `/PerformanceResult` | AI 모델로 광레벨 이상 감지 및 장애 예측 (외부 시스템) |
| **NiaAiResultLinkageApplication** | - | AI 분석 결과를 수신하여 DB 저장 |
| **NiaPerformanceApplication** | `RcaSystem` | 성능 데이터 집계, 일별 광성능 산출, 이력 관리 |
| **NiaEngineApplication** | `RcaTicketProfileServiceImpl` | PF(예지보전) 티켓 생성, NTD(Node Total Deviation) 기반 이상 판단 |

### 3.3 TL1/MMC 명령어 체계

`NiaEmsLinkageApplication`에서 EMS에 보내는 주요 MMC 명령어:

| 명령어 | 예시 | 목적 |
|--------|------|------|
| `RTRV-PM` | `RTRV-PM:SID-NAME::::SIGNAL=AMPPWR,TYPE=15M,INTERVAL=CURR;` | 15분 단위 광레벨(AMPPWR) 수집 |
| `RTRV-SIPC` | `RTRV-SIPC:IP-SH1::::;` | SIPC(서브시스템) 정보 수집 |
| `RTRV-NETWORK` | `RTRV-NETWORK:IP::123;` | NNI 토폴로지 수집 |
| `RTRV-ALM` | `RTRV-ALM:SID-NAME::123;` | 경보 정보 수집 |
| `RTRV-SLOT` | `RTRV-SLOT:SID-NAME::123;` | 슬롯 정보 수집 |
| `RTRV-OPC` | `RTRV-OPC:SID-NAME::123;` | OPC(광 채널) 정보 수집 |
| `RTRV-SYS` | `RTRV-SYS:TID::123;` | 시스템 정보 수집 |
| `RTRV-NET` | `RTRV-NET:::;` | 네트워크 장비 목록 수집 |

### 3.4 데이터 흐름

```
TELNET TL1 접속
  → RTRV-PM 명령 (15분 단위 광레벨 수집)
  → 광레벨 파싱 (value_cur, value_max, value_min)
  → mba.fc_set_performance_preprocessor() (전처리 함수)
  → RabbitMQ → AI 서버 전달
  → AI 분석 (이상 감지, 장애 예측)
  → AI 결과 수신
  → 성능 데이터 집계 (일별, 이력)
  → NTD(Node Total Deviation) 산출
  → PF 티켓 발행 (mba.tb_roadm_predictive_maintenance_ticket INSERT)
```

---

## 4. 두 Flow의 공통점과 차이점

| 항목 | EMS 경보 Flow (RT) | PF 티켓 Flow (PM) |
|------|-------------------|-------------------|
| **수집 프로토콜** | HTTP GET (syslog) | TELNET (TL1/MMC) |
| **수집 대상** | 경보(Alarm) 이벤트 | 성능(Performance) 데이터 |
| **수집 주기** | 실시간 (이벤트 발생 시) | 15분 단위 (스케줄) |
| **중간 처리** | 파싱 → 클러스터링 → RCA | 전처리 → AI 분석 → 집계 |
| **최종 결과** | RT 티켓 (rca 스키마) | PF/PMM 티켓 (mba 스키마) |
| **AI 활용** | 선택적 (재분석) | 필수 (장애 예측) |
| **최종 엔진** | NiaEngineApplication | NiaEngineApplication |

---

## 5. 전체 아키텍처 요약

```
                        ┌─────────────────────────────────┐
                        │         외부 시스템               │
                        │  ┌─────────┐  ┌───────────────┐ │
                        │  │ EMS     │  │ codej Django  │ │
                        │  │ (syslog)│  │ (AI Server)   │ │
                        │  └────┬────┘  └───┬───────┬───┘ │
                        └───────┼────────────┼───────┼─────┘
                         HTTP GET│     POST   │  POST │
                                │            │       │
    ┌───────────────────────────┼────────────┼───────┼───────────────┐
    │ NIA Engine Server 2021    │            │       │               │
    │                           ▼            │       ▼               │
    │  ┌─────────────────┐  ┌──────────────────┐ ┌────────────────┐ │
    │  │ NiaLinkage       │  │ NiaEmsLinkage    │ │ NiaAiResult    │ │
    │  │ (syslog 수신)    │  │ (TELNET/TL1)     │ │ Linkage        │ │
    │  └────────┬────────┘  └────────┬─────────┘ └───────┬────────┘ │
    │           │ MQ                 │ MQ                 │ MQ      │
    │           ▼                    ▼                    │         │
    │  ┌─────────────────┐  ┌──────────────────┐         │         │
    │  │ NiaAlarm         │  │ NiaAiPerformance │─── POST─┘         │
    │  │ Simulator        │  │ Linkage          │                   │
    │  └────────┬────────┘  └──────────────────┘                   │
    │           │ MQ                                                │
    │           ▼                    ┌──────────────────┐           │
    │  ┌─────────────────┐          │ NiaPerformance   │           │
    │  │ NiaPreprocessor  │          │ Application      │◄── MQ ───┘
    │  │ (알람 전처리)     │          └────────┬─────────┘           │
    │  └────────┬────────┘                   │ MQ                  │
    │           │ MQ                         │                     │
    │           ▼                             ▼                     │
    │  ┌─────────────────┐          ┌──────────────────┐           │
    │  │ NiaCluster       │          │                  │           │
    │  │ (알람 클러스터링)  │── MQ ──▶│ NiaEngine        │           │
    │  └─────────────────┘          │ Application      │           │
    │                               │ (RT/PF 티켓 발행) │           │
    │                               └──────────────────┘           │
    └──────────────────────────────────────────────────────────────┘
```
