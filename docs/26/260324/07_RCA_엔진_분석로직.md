# NIA RCA 엔진 분석 로직 상세

> 작성일: 2026-03-24
> 대상: NiaEngineApplication의 근원장애 분석(RCA) 처리 과정
> 소스 기반: Drools 규칙 엔진 + Java 서비스 레이어

---

## 1. RCA 분석이란?

RCA(Root Cause Analysis)는 **여러 알람 중 진짜 원인을 찾아내는 과정**이다.

광케이블 하나가 끊기면 연결된 10개 장비에서 동시에 알람이 발생한다. 이때 운용자가 10개 알람을 하나하나 확인하지 않고, NIA가 자동으로 "광케이블 끊김이 원인"이라고 특정해서 **RT 티켓 1건**으로 발행해주는 것이 RCA의 목적이다.

---

## 2. 전체 분석 흐름

```
알람 수신 (syslog / SNMP / TL1)
    │
    ▼
┌────────────────────────────────────┐
│  NiaPreprocessorApplication        │
│  알람 파싱 + 장비 조회 + 토폴로지 매핑 │
└──────────┬─────────────────────────┘
           │ RabbitMQ
           ▼
┌────────────────────────────────────┐
│  NiaClusterApplication             │
│  1. sysname/oppSysname 기준 그룹화   │
│  2. 공통 장비 클러스터 병합            │
│  3. 타이머 만료 후 클러스터 확정        │
└──────────┬─────────────────────────┘
           │ RabbitMQ (클러스터 번호)
           ▼
┌────────────────────────────────────┐
│  NiaEngineApplication              │
│  1. 장비 타입별 분류 (ROADM/POTN/SW) │
│  2. 단일도메인 RCA (Drools 규칙)      │
│  3. 다중도메인 RCA (교차 분석)         │
│  4. RT 티켓 생성                     │
└────────────────────────────────────┘
```

---

## 3. 클러스터링 (알람 그룹화)

**소스**: `NiaClusterApplication/.../AlarmClusterServiceImpl.java`

### 3.1 클러스터 생성 기준

같은 장비(sysname) 또는 대향 장비(oppSysname)에서 발생한 알람끼리 묶는다.

```
신규 알람 수신
  │
  ├─ 기존 클러스터 순회
  │    ├─ 신규 알람의 sysname이 클러스터의 equipList에 있는가?     → YES → 기존 클러스터에 추가
  │    ├─ 신규 알람의 oppSysname이 클러스터의 equipList에 있는가?  → YES → 기존 클러스터에 추가
  │    └─ 둘 다 없으면 → 새 클러스터 생성
  │
  └─ 클러스터 병합: 여러 클러스터가 공통 sysname 또는 trunkId를 공유하면 병합
```

### 3.2 클러스터 확정 타이밍

- delay 시간 동안 새 알람이 없으면 타이머 만료 → 클러스터 확정
- 새 알람 수신 시 타이머 리셋 (대기 시간 연장)
- 확정된 클러스터는 RabbitMQ로 엔진에 전달

---

## 4. 단일도메인 RCA 분석

**소스**: `NiaEngineApplication/.../SingleDomainRcaServiceImpl.java`

클러스터를 수신하면 알람을 **장비 타입별로 3개 도메인**으로 분류한다.

```
클러스터의 BasicAlarmVo 목록
  │
  ├─ equiptype이 "OTN" 또는 "OPN" 포함 → POTN 도메인 → potnSingleRule.drl
  ├─ equiptype이 "ROADM"로 시작       → ROADM 도메인 → roadmSingleRule.drl
  └─ alarmno가 "I" 또는 "P"로 시작     → SWITCH 도메인 → switchSingleRule.drl
```

각 도메인은 **Drools 규칙 엔진**으로 분석한다. Drools는 if-else가 아닌 **패턴 매칭** 기반으로, 여러 알람의 조합을 매칭하여 장애 원인을 특정한다.

### 4.1 규칙 실행 메커니즘

- **Drools KIE** (Stateful Session): 알람을 Working Memory에 삽입하고 규칙을 실행
- **salience** (우선순위): 숫자가 높을수록 먼저 실행 (1000 → 900 → 800 → ...)
- **Flag 제어**: 한 번 규칙이 매칭되면 Flag를 세워 중복 실행 방지
- 규칙 파일 위치: `src/main/resources/rules/{roadm,potn,switchs}/single/*.drl`

---

## 5. ROADM 규칙 엔진 상세

**소스**: `rules/roadm/single/roadmSingleRule.drl`

### 5.1 장애 판단 결정 트리

```
salience 1000 (최우선)
  └─ NVRAM 장애
     조건: alarmmsg == "NVRAM_FAIL"
     결과: 기타장애 (EtcFail)

salience 900
  ├─ FAN 모듈 탈장
  │  조건: alarmmsg가 "COW_FAN_M1_FAIL" ~ "M4_FAIL" 중 하나
  │  결과: 유닛장애 (UnitFail) - "FAN 모듈 탈장"
  │
  └─ 유닛(카드) 탈장 (4가지 케이스)
     조건: alarmmsg == "CARD_OUT" + unit 타입으로 분기
     ├─ unit이 BAUp, LAUp, MRSA-2A, PAUp, WSUp-2   → "공통부 유닛 탈장"
     ├─ unit이 CCU, RCU, WCU                       → "제어부 유닛 탈장"
     ├─ unit이 OT2A-2, OT2A-4, OT2HU_2, OT2U_2 등 → "채널부 유닛 탈장"
     └─ unit이 SCU                                 → "통신부 유닛 탈장"
     결과: 모두 유닛장애 (UnitFail)

salience 800
  ├─ WSS 모듈 장애
  │  조건: alarmmsg == "WSS_ERR"
  │  결과: 유닛장애 (UnitFail)
  │
  ├─ IPC 통신 장애
  │  조건: alarmmsg == "IPC_FAIL"
  │  결과: 유닛장애 (UnitFail) - "유닛 통신 장애"
  │
  ├─ 전원부 랙 장애 (★ 복합 조건)
  │  조건: 동일 sysname에서 6개 알람이 모두 존재해야 함
  │    $a1: unit="CCU", alarmmsg="PDU_PWR_A_FAIL"
  │    $a2: unit="CCU", alarmmsg="FAN_PWR_A_FAIL"
  │    $a3: unit="CCU", alarmmsg="RACK_PWR_A_FAIL"
  │    $a4: unit="WCU", alarmmsg="PDU_PWR_A_FAIL"
  │    $a5: unit="WCU", alarmmsg="FAN_PWR_A_FAIL"
  │    $a6: unit="WCU", alarmmsg="RACK_PWR_A_FAIL"
  │  결과: 전원장애 (PowerFail) - "전원부 랙 장애"
  │  (B계도 동일 구조로 별도 규칙 존재)
  │
  └─ 유닛 FAIL 계열
     조건: 다양한 FAIL 타입 + unit 매칭
     결과: 유닛장애 (UnitFail)

salience 700
  ├─ WDM NNI 양방향 링크 장애 (★ 토폴로지 활용)
  │  조건:
  │    $a1: alarmmsg="WDM_LOS" + NNI unit 타입
  │    $a2: sysname=$a1.topology.oppSysname (대향 장비)
  │         ptpname=$a1.topology.oppPtpName (대향 포트)
  │         alarmmsg="WDM_LOS"
  │  의미: 양쪽 장비 모두에서 LOS 발생 → 중간 링크(케이블) 문제
  │  결과: 링크장애 (Linkcut) - "WDM NNI 수신단 양방향 링크 장애"
  │
  └─ 전원부 셀프 장애
     조건: 3개 전원 알람 매칭 (랙 장애보다 적은 조건)
     결과: 전원장애 (PowerFail)

salience 600
  ├─ WDM NNI 단방향 링크 장애
  │  조건: alarmmsg="WDM_LOS" + NNI unit 타입 (대향 알람 없음)
  │  의미: 한쪽만 LOS → 수신단 문제
  │  결과: 링크장애 (Linkcut)
  │
  └─ 전원부 팬 장애
     조건: unit="WCU", alarmmsg="FAN_PWR_A/B_FAIL"
     결과: 전원장애 (PowerFail)

salience 500
  ├─ WDM 신호 감쇠
  │  조건: alarmmsg가 "WDM_PWR_LOW" 또는 "WDM_LOCAL_SD"
  │  결과: 링크장애 (Linkcut) - "신호 감쇠"
  │
  └─ WDM 신호 증가
     조건: alarmmsg == "WDM_PWR_HIGH"
     결과: 링크장애 (Linkcut) - "신호 증가"

salience 400
  └─ UNI 양방향 링크 장애 (★ 토폴로지 활용)
     조건: UNI 포트에서 양쪽 장비 모두 LOS
     결과: 링크장애 (Linkcut) - "UNI 수신단 양방향 링크 장애"

salience 300
  └─ UNI 단방향 링크 장애
     조건: UNI_LOCAL_LOS (대향 알람 없음)
     결과: 링크장애 (Linkcut)

salience 200
  └─ UNI 신호 감쇠/증가
     조건: alarmmsg가 "UNI_PWR_LOW" 또는 "UNI_PWR_HIGH"
     결과: 링크장애 (Linkcut)
```

### 5.2 토폴로지 정보의 역할

ROADM 규칙에서 **토폴로지(topology)**는 양방향 장애 판단에 핵심이다.

```
장비A ─────── 광케이블 ─────── 장비B
  │                              │
  └─ WDM_LOS 발생               └─ WDM_LOS 발생
     topology.oppSysname = "장비B"
     topology.oppPtpName = "장비B의 포트"
```

- `topology.oppSysname`: 대향(반대편) 장비 이름
- `topology.oppPtpName`: 대향 포트 이름
- `topology.oppSlot`: 대향 슬롯
- `topology.nwType`: NNI(네트워크 간) / UNI(사용자 접속) 포트 구분

**양방향 vs 단방향 판단**:
- 양쪽 장비 모두 LOS → **양방향 장애** (중간 링크 문제, salience 높음)
- 한쪽만 LOS → **단방향 장애** (수신단 문제, salience 낮음)

---

## 6. POTN 규칙 엔진 상세

**소스**: `rules/potn/single/potnSingleRule.drl`

### 6.1 장애 판단 결정 트리

```
salience 1000
  └─ 노드 고립
     조건: alarmmsg == "NODE_ISOLATION"
     결과: 기타장애 (EtcFail)

salience 900 — 유닛 탈장 계열
  ├─ CONTROL_UNIT_OUT  → 장치장애 (NodeFail)
  ├─ FAN_UNIT_OUT      → 장치장애 (NodeFail)
  ├─ LOGIC_BOARD_OUT   → 장치장애 (NodeFail)
  ├─ SWITCH_FABRIC_OUT → 장치장애 (NodeFail)
  ├─ ETH_UNIT_OUT      → 유닛장애 (UnitFail)
  ├─ LINK_UNIT_OUT     → 유닛장애 (UnitFail)
  ├─ OTN_UNIT_OUT      → 유닛장애 (UnitFail)
  └─ STM_UNIT_OUT      → 유닛장애 (UnitFail)

  ※ 제어부/공통부 탈장 = NodeFail (장비 전체 영향)
  ※ 서비스 유닛 탈장 = UnitFail (해당 유닛만 영향)

salience 800 — 유닛 고장 계열
  ├─ CONTROL_UNIT_FAIL → 장치장애 (NodeFail)
  ├─ FAN_UNIT_FAIL     → 장치장애 (NodeFail)
  ├─ PWR_FAIL          → 전원장애 (PowerFail)
  ├─ ETH_UNIT_FAIL     → 유닛장애 (UnitFail)
  ├─ LINK_UNIT_FAIL    → 유닛장애 (UnitFail)
  ├─ OTN_UNIT_FAIL     → 유닛장애 (UnitFail)
  ├─ STM_UNIT_FAIL     → 유닛장애 (UnitFail)
  └─ SM_UNIT_FAIL / UAS_UNIT_FAIL → 유닛장애 (UnitFail)

salience 700 — MPLS 절체 + 주/예비 양방향 장애 (★ 복합 조건)
  ├─ MPLS 절체 (로컬)
  │  조건: alarmmsg == "MPLS_PROT_SWITCH_LOCAL_REQ"
  │  결과: 링크장애 (Linkcut) - "MPLS 절체 장애"
  │
  ├─ MPLS 절체 (리모트)
  │  조건: alarmmsg == "MPLS_PROT_SWITCH_REMOTE_REQ"
  │  결과: 링크장애 (Linkcut) - "MPLS 절체 대항 노드 장애"
  │
  └─ 주/예비 양방향 링크 장애 (★ 3개 알람 매칭)
     조건:
       $a1: nwType="UNI", alarmmsg="ETH_LOS" (주 경로)
       $a2: 동일 sysname, 다른 slot, alarmmsg="ETH_LOS" (예비 경로)
       $a3: oppSysname=$a1, oppSlot=$a1, alarmmsg="ETH_LOS" (대향 장비)
     의미: 주경로 + 예비경로 + 대향장비 모두 LOS → 완전 링크 끊김
     결과: 링크장애 (Linkcut) - "주/예비 수신단 양방향 링크장애"

     프로토콜별 변형:
     ├─ POTN_ETH_UNI_B_SF  (UNI, ETH_LOS)
     ├─ POTN_OTN_NNI_B_SF  (NNI, OTN_LOS)
     ├─ POTN_ODU_NNI_B_SF  (NNI, ODU_LOS)
     ├─ POTN_OCH_NNI_B_SF  (NNI, OCH_LOS)
     ├─ POTN_STM_UNI_B_SF  (UNI, STM_LOS)
     └─ POTN_WDM_NNI_B_SF  (NNI, WDM_LOS)

salience 600 — 양방향 링크 장애 (2개 알람)
  └─ 토폴로지 기반 양방향 판단
     조건:
       $a1: alarmmsg="ETH_LOS" (이쪽)
       $a2: oppSysname=$a1, oppSlot=$a1, alarmmsg="ETH_LOS" (대향)
     결과: 링크장애 (Linkcut) - "수신단 양방향 링크장애"

     프로토콜별 변형:
     ├─ POTN_ETH_UNI_SF_BI
     ├─ POTN_OTN_NNI_SF_BI
     ├─ POTN_ODU_NNI_SF_BI
     ├─ POTN_STM_UNI_SF_BI
     └─ ...

salience 500-400 — 단방향 링크 장애
  └─ 대향 장비 알람 없이 한쪽만 LOS
     결과: 링크장애 (Linkcut) - "단방향 링크 장애"
```

---

## 7. SWITCH 규칙 엔진 상세

**소스**: `rules/switchs/single/switchSingleRule.drl`

SWITCH(IP 장비) 규칙은 매우 단순하다. 토폴로지 분석 없이 알람 메시지만으로 판단한다.

```
salience 1000
  ├─ PING 통신 장애
  │  조건: alarmmsg == "PING_UNREACHABLE"
  │  결과: 스위치장애 (SwitchFail) - "PING 통신 장애"
  │
  └─ 포트 다운
     조건: alarmmsg == "PORT_DOWN"
     결과: 스위치장애 (SwitchFail) - "PORT 다운"

salience 900
  └─ SNMP 통신 장애
     조건: alarmmsg == "SNMP_TIME_OUT"
     결과: 스위치장애 (SwitchFail) - "SNMP 통신 장애"
```

---

## 8. 다중도메인 RCA (교차 분석)

**소스**: `rules/multi/MultiRule.drl`, `MultiDomainRcaServiceImpl.java`

단일도메인 분석 후, 서로 다른 도메인의 결과를 교차 검증한다.

### 8.1 규칙 예시

```
MULTI_ETH_UNI_SF_SI:
  조건:
    $r1: 단일도메인 결과가 "POTN_ETH_UNI_SF_SI" (POTN 이더넷 단방향 장애)
    $r2: 단일도메인 결과가 "SWITCH_PORT_DOWN" (스위치 포트 다운)

  의미: POTN 링크 장애 + 스위치 포트 다운이 동시 발생
       → 하나의 근본 원인 (POTN 링크 끊김)으로 통합

  결과: 두 결과를 "MULTI_ETH_UNI_SF_SI"로 합침
```

### 8.2 처리 로직

```
1. 각 단일도메인 RCA 결과를 Drools Working Memory에 삽입
2. 다중도메인 규칙 실행 (fireAllRules)
3. 매칭되면:
   - 두 결과를 하나로 통합
   - 개별 결과 제거, 통합 결과 추가
4. 매칭 안되면:
   - 각 단일도메인 결과를 그대로 유지 (독립적 장애)
```

---

## 9. RCA 결과 타입 (12가지)

**소스**: `RcaCodeInfo.java`

| 코드 | 상수명 | 의미 | 주로 발생하는 도메인 |
|------|--------|------|-------------------|
| CableCut | RCA_RESULT_CABLE_CUT | 선로장애 (광케이블 단절) | ROADM |
| Linkcut | RCA_RESULT_LINKCUT | 링크장애 (네트워크 링크 끊김) | ROADM, POTN |
| UnitFail | RCA_RESULT_UNIT_FAIL | 유닛장애 (카드/모듈 탈장/고장) | ROADM, POTN |
| NodeFail | RCA_RESULT_NODE_FAIL | 장치장애 (장비 전체 장애) | POTN |
| CommFail | RCA_RESULT_COMM_FAIL | 통신장애 (프로토콜 문제) | 공통 |
| ClockFail | RCA_RESULT_CLOCK_FAIL | 클럭장애 (동기화 문제) | ROADM, POTN |
| PowerFail | RCA_RESULT_POWER_FAIL | 전원장애 (전원 공급 문제) | ROADM, POTN |
| EtcFail | RCA_RESULT_ETC_FAIL | 기타장애 (분류 불가) | 공통 |
| CircuitFail | RCA_RESULT_CIRCUIT_FAIL | 회선장애 (서비스 회선) | POTN |
| SwitchFail | RCA_RESULT_SWITCH_FAIL | 스위치장애 (IP 장비) | SWITCH |
| TrafficFail | RCA_RESULT_TRAFFIC_FAIL | 트래픽장애 (이상 트래픽) | IP |
| FactorFail | RCA_RESULT_FACTOR_FAIL | 팩터장애 (장비 과부하) | IP |

### 알람 등급

| 등급 | 의미 |
|------|------|
| 1 | CLEAR (정상 복구) |
| 3 | WARNING (경고) |
| 4 | MINOR (경미) |
| 5 | MAJOR (중대) |
| 7 | CRITICAL (심각) |

---

## 10. RCA 결과 데이터 모델

**소스**: `RcaResult.java`

```
RcaResult {
  rcaResultCode        // 매칭된 규칙명 (예: "ROADM_WDM_SF_BI")
  rcaResultTypeCode    // 장애 타입 (예: "Linkcut")
  domainCode           // 도메인 (예: "ROADM")
  faultDetailCode      // 설명 (예: "WDM NNI 수신단 양방향 링크 장애")
  clusterNo            // 원본 클러스터 번호
  alarmTime            // 첫 알람 시간
  nwType               // NNI / UNI / UNKNOWN
  relatedAlarmList     // 근본 원인이 된 알람들
  associatedAlarmList  // 관련된 부수 알람들
  multiRcaResultCodeList // 다중도메인에서 통합된 이전 결과들
}
```

---

## 11. 분석의 핵심 원리 정리

### 11.1 우선순위 기반 판단

salience가 높을수록 **더 확실한 장애**를 먼저 판단한다:

```
1000: 장비 자체 문제 (NVRAM, 노드 고립) — 가장 확실
 900: 물리적 탈장 (카드가 빠짐) — 눈에 보이는 장애
 800: 전원/유닛 고장 — 복합 조건 필요
 700: 양방향 링크 장애 (양쪽 LOS) — 토폴로지 필요
 600: 양방향 링크 장애 (2개 알람)
 500: 신호 감쇠/증가
 400: UNI 양방향 장애
 300: 단방향 장애 — 가장 불확실
```

### 11.2 토폴로지의 역할

토폴로지 없이는 "이 알람이 어디서 끊긴 건지" 알 수 없다.

```
예시: 장비A에서 WDM_LOS 발생
  ├─ 토폴로지 조회: 장비A의 대향은 장비B
  ├─ 장비B에서도 WDM_LOS 발생?
  │   ├─ YES → 양방향 장애 (중간 링크 문제) — 확실도 높음
  │   └─ NO  → 단방향 장애 (장비A 수신단 문제) — 확실도 낮음
```

### 11.3 클러스터링의 역할

클러스터링은 "관련 알람끼리 묶어서" 규칙 엔진에 넣는 역할이다.

```
광케이블 끊김 → 장비A(LOS), 장비B(LOS), 장비C(포트다운)
  ↓ 클러스터링
하나의 클러스터로 묶임 (sysname/oppSysname 기준)
  ↓ 규칙 엔진
3개 알람을 동시에 매칭 → "양방향 링크 장애" 판정
```

---

## 12. 핵심 소스 파일 목록

| 파일 | 역할 |
|------|------|
| `NiaClusterApplication/.../AlarmClusterServiceImpl.java` | 알람 클러스터링 |
| `NiaEngineApplication/.../SingleDomainRcaServiceImpl.java` | 도메인별 분류 + 규칙 엔진 호출 |
| `NiaEngineApplication/.../MultiDomainRcaServiceImpl.java` | 다중도메인 교차 분석 |
| `NiaEngineApplication/.../RcaTicketManagerServiceImpl.java` | RT/PF 티켓 생성 |
| `NiaEngineApplication/.../RcaTicketProfileServiceImpl.java` | 프로필 기반 상태 업데이트 |
| `NiaEngineApplication/.../RuleResultSetting.java` | 규칙 결과 후처리 |
| `NiaEngineApplication/resources/rules/roadm/single/roadmSingleRule.drl` | ROADM Drools 규칙 |
| `NiaEngineApplication/resources/rules/potn/single/potnSingleRule.drl` | POTN Drools 규칙 |
| `NiaEngineApplication/resources/rules/switchs/single/switchSingleRule.drl` | SWITCH Drools 규칙 |
| `NiaEngineApplication/resources/rules/multi/MultiRule.drl` | 다중도메인 Drools 규칙 |
| `NiaEngineApplication/.../RcaCodeInfo.java` | 장애 타입/티켓 타입 상수 정의 |
