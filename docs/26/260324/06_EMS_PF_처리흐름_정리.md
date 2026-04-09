# NIA 광장비 데이터 처리 Flow 종합 정리

> 원본: `06_EMS_PF_처리흐름.md` 기반 + 코드 분석 보완
> 작성일: 2026-03-24
> 대상: NIA 시스템을 처음 접하는 사람

---

## 1. NIA 시스템이란?

NIA(Network Intelligence Analytics)는 **광통신 네트워크 장비를 감시하고 장애를 자동 분석하는 시스템**이다.

쉽게 비유하면:
- 병원의 **환자 모니터링 시스템**과 비슷하다
- 광장비(ROADM, POTN 등)가 "환자"이고, NIA가 "간호 스테이션"이다
- 장비에서 이상 신호가 오면 자동으로 **원인을 분석**하고 **티켓(전표)**을 발행한다

---

## 2. 핵심 용어 설명

| 용어 | 의미 | 비유 |
|------|------|------|
| **EMS** | Element Management System. 광장비를 관리하는 외부 시스템 | 장비 제조사가 제공하는 관리 프로그램 |
| **RT 티켓** | Root cause Ticket. 장애 원인 분석 결과 전표 | "이 장애의 원인은 A장비 3번 포트입니다" 라는 보고서 |
| **PF 티켓** | Performance(광성능) 티켓. 광레벨 이상 감지 시 발행 | "이 구간 광성능에 이상이 있습니다" 라는 전표 (코드상 주석: "Cable Inspecter Ticket") |
| **PMM 티켓** | Predictive Maintenance(예지보전) 티켓. AI 기반 장애 **예측** 전표 | "이 장비가 곧 고장날 것 같습니다" 라는 경고장. 별도 테이블(`mba.tb_roadm_predictive_maintenance_ticket`)에 저장 |
| **RCA** | Root Cause Analysis. 근원 장애 분석 | 여러 알람 중 진짜 원인이 뭔지 찾는 과정 |
| **TL1** | Transaction Language 1. 광장비와 통신하는 프로토콜 | 광장비 전용 "대화 언어" (TELNET으로 접속) |
| **MMC** | Man-Machine Command. TL1에서 쓰는 명령어 | `RTRV-PM`(성능조회), `RTRV-ALM`(경보조회) 등 |
| **ROADM** | Reconfigurable Optical Add-Drop Multiplexer | 광신호를 합치거나 분리하는 장비 (모델명: Utrans-6300p 등) |
| **POTN** | Packet Optical Transport Network | 광전송 네트워크 장비 (모델명: OPN-3000) |
| **NTD** | Node Total Deviation. 노드 총 편차 | 장비의 광레벨이 정상 범위에서 얼마나 벗어났는지 수치 |
| **AMPPWR** | Amplifier Power. 광증폭기 출력 | 광케이블로 보내는 빛의 세기 |
| **syslog** | System Log. 장비에서 보내는 시스템 로그 | 장비가 "나 지금 이상해!" 하고 보내는 메시지 |
| **클러스터링** | 연관된 알람을 하나로 묶는 것 | 광케이블 끊기면 10개 장비에서 알람이 오는데, 이걸 "1건"으로 묶음 |

---

## 3. 두 가지 핵심 Flow 개념

### Flow A: EMS 경보 Flow (실시간 장애 대응)

**목적**: 장애가 **이미 발생**했을 때, 원인을 분석하여 RT 티켓 발행

```
비유: 119 신고 → 소방서에서 화재 원인 분석 → 보고서 작성
```

**동작 방식**:
1. 광장비에서 경보(syslog)가 발생하면 HTTP로 NIA에 전달
2. 알람을 파싱하여 어떤 장비, 어떤 포트인지 식별
3. 연관 알람을 클러스터(묶음)로 그룹화
4. RCA 엔진이 진짜 원인(Root Cause)을 분석
5. RT 티켓을 발행하여 운용자에게 알림

### Flow B: PF 티켓 Flow (예지보전 = 예측 정비)

**목적**: 장애가 발생하기 **전에** 이상 징후를 감지하여 PF 티켓 발행

```
비유: 정기 건강검진 → AI가 수치 이상 감지 → "정밀검사 필요" 통보
```

**동작 방식**:
1. 15분마다 TELNET으로 장비에 접속하여 광레벨(AMPPWR) 수집
2. 수집된 데이터를 AI 서버(Django)에 전달
3. AI가 이상 패턴을 감지하면 결과 반환
4. NTD(편차) 계산 후 임계값 초과 시 PF 티켓 발행

---

## 4. 아키텍처 구조 (마이크로서비스)

NIA는 **21개 Spring Boot 마이크로서비스**가 **RabbitMQ**(메시지 큐)로 연결된 구조이다.

```
┌─ 수집 계층 ─────────────────────────────────────────┐
│  NiaLinkageApp        : syslog 수신 (HTTP GET)       │
│  NiaEmsLinkageApp     : TL1/TELNET 접속 (15분 주기)   │
│  NiaIpSdnSyslogApp    : IP장비 syslog 수신            │
└──────────────────────────┬──────────────────────────┘
                     RabbitMQ ▼
┌─ 전처리 계층 ───────────────────────────────────────┐
│  NiaPreprocessorApp   : 알람 파싱, 장비 조회, 토폴로지 │
│  NiaAlarmSimulatorApp : 테스트용 시뮬레이터             │
└──────────────────────────┬──────────────────────────┘
                     RabbitMQ ▼
┌─ 분석 계층 ─────────────────────────────────────────┐
│  NiaClusterApp        : 연관 알람 그룹화              │
│  NiaAiPerformanceApp  : AI 서버에 데이터 전달          │
│  NiaAiResultApp       : AI 분석 결과 수신             │
│  NiaPerformanceApp    : 성능 데이터 집계 (일별/이력)    │
└──────────────────────────┬──────────────────────────┘
                     RabbitMQ ▼
┌─ 엔진 계층 ─────────────────────────────────────────┐
│  NiaEngineApp         : RCA 분석 → RT 티켓 발행       │
│                       : NTD 계산 → PF 티켓 발행       │
└──────────────────────────┬──────────────────────────┘
                     RabbitMQ ▼
┌─ UI 계층 ───────────────────────────────────────────┐
│  Front_web_2024       : 운용자 대시보드 (Vue 2.6)      │
└─────────────────────────────────────────────────────┘
```

### RabbitMQ 큐 구조 (코드 확인)

| 큐 이름 | 생산자 → 소비자 | 데이터 |
|---------|---------------|--------|
| `Cluster_RabbitTemplate` | Preprocessor → Cluster | 파싱된 알람 |
| `Engine_RabbitTemplate` | Cluster → Engine | 클러스터 키 |
| `EngineToUiTicket_RabbitTemplate` | Engine → Web UI | RT/PF 티켓 |
| `NiaPerformanceToAiSendIndexDirectly` | EmsLinkage → AI연동 | 광레벨 데이터 |
| `NiaPerformanceIndexDirectly` | AI결과 → Performance | AI 분석 결과 |

---

## 5. EMS 경보 Flow 상세 (RT 티켓 발행)

### 5.1 Flow 다이어그램

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

### 5.2 모듈별 역할

| 모듈 | 주요 클래스 | 역할 |
|------|-----------|------|
| **NiaLinkageApplication** | `AlarmLinkageResultPrdAmqp` | 외부 EMS에서 syslog 경보를 HTTP GET으로 수신, RabbitMQ로 전달 |
| **NiaAlarmSimulatorApplication** | - | 경보 시뮬레이터 (테스트/재현 환경용) |
| **NiaPreprocessorApplication** | `NiaAlarmHdlServiceImpl`, `PasingServiceImpl`, `RoadmPasingServiceImpl`, `PotnPasingServiceImpl` | 알람 정규화, 장비정보 조회(`NIA.TB_EQUIP_MST`), 토폴로지 매핑(`NIA.TB_TOPOLOGY`), 포트 타입 확인(`NIA.TB_EQUIP_PORT`) |
| **NiaClusterApplication** | - | 연관 알람을 하나의 클러스터로 그룹화 |
| **NiaEngineApplication** | `SingleDomainRcaServiceImpl`, `RcaTicketManagerServiceImpl` | 근원장애 분석(RCA), RT 티켓 생성, 장비별 알람 라인 할당 |

### 5.3 알람 전처리 과정

`NiaPreprocessorApplication`에서 장비 타입별로 파싱을 분기한다:

```
장비 모델명 확인
  ├─ "OPN-3000"          → PotnPasingService  (POTN 장비)
  ├─ "Utrans-6300p-H3"   → RoadmPasingService (ROADM 장비)
  └─ "Utrans-6300p-V9"   → RoadmPasingService (ROADM 장비)
```

**ROADM 파싱** (`RoadmPasingServiceImpl`)에서는:
- `alarmloc` 필드를 `-`로 split하여 슬롯/포트 번호 추출
- 슬롯 타입(O401SLU, OMX24U, OLPA-3 등)으로 UNI/NNI 포트 구분
- `TB_TOPOLOGY` 테이블에서 반대편 장비(peer) 정보 조회

### 5.4 RCA 분석 과정

`SingleDomainRcaServiceImpl`에서 장비 도메인별 규칙을 적용한다:

```
클러스터 입력 → 장비 타입별 분류
  ├─ ROADM  → roadmAlarmRule.roadmAlarmRule()
  ├─ POTN   → potnSingleRule.potnAlarmRule()
  └─ IP/SW  → switchAlarmRule.switchAlarmRule()
       ↓
  멀티도메인 교차 분석 (multiDomainRcaService)
       ↓
  RT 티켓 생성 (rcaTicketManager.createRcaTicket())
```

### 5.5 데이터 흐름

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

## 6. PF 티켓 Flow 상세 (예지보전 티켓 발행)

### 6.1 Flow 다이어그램

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

### 6.2 모듈별 역할

| 모듈 | 주요 클래스/메서드 | 역할 |
|------|-----------------|------|
| **NiaEmsLinkageApplication** | `RoadmEmsMmcServiceImpl.roadmPmMMC()` | TELNET/TL1로 EMS에 접속, `RTRV-PM` 명령으로 15분 단위 광레벨(AMPPWR) 수집. 수집 후 `mba.fc_set_performance_preprocessor()` 호출하여 전처리 |
| **NiaAiPerformanceLinkageApplication** | - | 수집된 성능 데이터를 HTTP POST로 Django AI 서버에 전달 |
| **codej DJANGO Server** | `/PerformanceData`, `/PerformanceResult` | AI 모델로 광레벨 이상 감지 및 장애 예측 (외부 시스템) |
| **NiaAiResultLinkageApplication** | - | AI 분석 결과를 수신하여 DB 저장 |
| **NiaPerformanceApplication** | `RcaSystem` | 성능 데이터 집계, 일별 광성능 산출, 이력 관리 |
| **NiaEngineApplication** | `RcaTicketProfileServiceImpl` | PF(예지보전) 티켓 생성, NTD(Node Total Deviation) 기반 이상 판단 |

### 6.3 TL1/MMC 명령어 체계

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

### 6.4 PF 티켓 (예지보전) 판단 로직

`RcaTicketProfileServiceImpl`에서 **프로필 테이블**(NT1_TT_PERF_INFO)을 조회한다:

```
RT 티켓 발행 후 → profileCheck() 호출
  ├─ 네트워크 타입 결정: ROADM/POTN="전송", SWITCH="IP"
  ├─ NT1_TT_PERF_INFO 조회 (장비ID + 장애코드 + 시간)
  ├─ 프로필 있으면 → autoRecovery 상태 적용, PF 티켓 업데이트
  └─ 프로필 없으면 → 티켓 상태 INIT 유지 (5분 후 재확인)
```

**스케줄러**: 5분 간격(`0 2/5 * * * *`)으로 미처리 티켓을 재확인한다.

### 6.5 데이터 흐름

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

## 7. 두 Flow의 공통점과 차이점

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

## 8. 전체 아키텍처 요약

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

---

## 9. 식별된 문제점 / 취약점

### (1) TL1 클라이언트 동시성 문제
- `RoadmEmsTL1Client`가 **싱글톤**인데 공유 TELNET 인스턴스를 사용
- 여러 스레드가 동시에 접근하면 명령어가 섞일 수 있음
- `synchronized` 블록이나 커넥션 풀이 없음

### (2) TL1 통신의 블로킹 방식
- `Thread.sleep(1000)` 루프로 응답 대기 (10분 타임아웃)
- CPU 낭비 + 스레드 점유 → 장비 수 증가 시 성능 병목

### (3) 알람 위치 파싱의 취약성
- `alarmloc.split("-")`로 슬롯/포트를 추출하는데, 예상과 다른 형식이 오면 `ArrayIndexOutOfBoundsException` 가능
- 하드코딩된 슬롯 타입 목록 (O401SLU, OMX24U 등) → 새 장비 추가 시 코드 수정 필요

### (4) 프로필 테이블 미조회 시 대응 부재
- `NT1_TT_PERF_INFO`에서 프로필을 못 찾으면 티켓이 INIT 상태로 방치
- 5분 스케줄러가 계속 재조회하지만, 프로필 데이터가 없으면 **영원히 INIT 상태**

### (5) Syslog 자동처리 하드코딩
- `IFMGR_IF_UP_4`, `OSPF_OPR_LINK_UP_4` 같은 규칙이 코드에 직접 기입
- 규칙 변경 시 코드 수정 + 재배포 필요 (설정 파일이나 DB 기반이 아님)

### (6) 중복 티켓 처리 복잡도
- RT 티켓 병합(merge) 로직이 부모/자식 관계로 복잡
- 상태 전이(INIT → MERGE → FIN)가 코드 전반에 분산되어 있어 추적이 어려움

---

## 10. 전체 데이터 흐름 요약

```
[실시간 경보]                          [주기적 성능 수집]
EMS syslog (HTTP)                    EMS TELNET (TL1, 15분)
      │                                    │
      ▼                                    ▼
   알람 파싱                            광레벨 수집 (AMPPWR)
      │                                    │
      ▼                                    ▼
   장비/토폴로지 매핑                     AI 서버로 전달 (Django)
      │                                    │
      ▼                                    ▼
   클러스터링 (연관 알람 묶음)             AI 이상 감지 분석
      │                                    │
      ▼                                    ▼
   RCA 분석 (근원 원인 특정)             성능 집계 (일별/이력)
      │                                    │
      └──────────┐              ┌──────────┘
                 ▼              ▼
           NiaEngineApplication
           ┌─────────────────────┐
           │  RT 티켓 (장애 원인)  │
           │  PF 티켓 (장애 예측)  │
           └──────────┬──────────┘
                      ▼
              운용자 대시보드 (Vue)
```

핵심은 **두 Flow 모두 최종적으로 `NiaEngineApplication`에서 합류**한다는 점이다. RT는 "이미 발생한 장애의 원인", PF는 "앞으로 발생할 장애의 예측"이라는 차이만 있다.
