# NIA 모듈 및 마이크로서비스 카탈로그

## Engine_nia_server_2021 서비스 목록 (21개)

### 코어 엔진 서비스 (2개)

#### 1. NiaEngineApplication (포트: 8001)
**책임**: 규칙 기반 이벤트 처리 및 의사결정

공개 인터페이스:
- `/api/engine/rules` - 규칙 관리
- `/api/engine/events` - 이벤트 처리
- `/api/engine/decisions` - 의사결정 결과

의존성:
- Drools 규칙 엔진
- PostgreSQL (ClusterMapper, RcaSystemConfigMapper)
- RabbitMQ (이벤트 큐)

구현: `src/main/java/com/nia/engine/`
- RuleEngine.java - 핵심 규칙 처리
- EventProcessor.java - 이벤트 핸들링
- DecisionService.java - 의사결정 로직

#### 2. NiaPreprocessorApplication (포트: 8002)
**책임**: 원시 네트워크 데이터 수집 및 정제

공개 인터페이스:
- `/api/preprocessor/intake` - 데이터 수집
- `/api/preprocessor/status` - 상태 조회

의존성:
- RabbitMQ (여러 큐 수신)
- PostgreSQL

구현:
- DataIntakeService.java - 데이터 수집
- ValidationService.java - 데이터 검증
- TransformationService.java - 데이터 변환

---

### 성능 모니터링 서비스 (3개)

#### 3. NiaPerformanceApplication (포트: 8003)
**책임**: 네트워크 성능 메트릭 처리

공개 인터페이스:
- `/api/performance/metrics` - 성능 지표 조회
- `/api/performance/alerts` - 성능 기반 알림

의존성:
- RabbitMQ (성능 데이터 큐)
- PostgreSQL (PerformanceMapper)
- Redis (캐싱)

주요 메트릭:
- 지연시간 (Latency)
- 손실률 (Loss Rate)
- 처리량 (Throughput)
- 가용성 (Availability)

#### 4. NiaIpPerfLinkageApplication (포트: 8004)
**책임**: IP 기반 성능 데이터 연계

공개 인터페이스:
- `/api/ip-perf/link` - 성능 연계 조회
- `/api/ip-perf/topology` - 토폴로지 조회

의존성:
- NiaPerformanceApplication
- NiaEngineApplication
- PostgreSQL (토폴로지 데이터)

구현:
- PerformanceLinkage.java - 성능 연계 로직
- TopologyResolver.java - 토폴로지 분석

#### 5. NiaPingPreprocessorApplication (포트: 8005)
**책임**: 핑 기반 연결성 모니터링

공개 인터페이스:
- `/api/ping/probe` - 핑 프로브 관리
- `/api/ping/results` - 핑 결과 조회

---

### 트래픽 분석 서비스 (3개)

#### 6. NiaTrafficPreprocessorApplication (포트: 8006)
**책임**: 트래픽 데이터 수집 및 전처리

의존성:
- NetFlow/sFlow 데이터 소스
- RabbitMQ

#### 7. NiaIpSflowLinkageApplication (포트: 8007)
**책임**: sFlow 데이터 집계 및 분석

공개 인터페이스:
- `/api/sflow/flows` - 플로우 데이터 조회
- `/api/sflow/topology` - 토폴로지 분석

#### 8. NiaIpSdnSflowLinkageApplication (포트: 8008)
**책임**: SDN 환경 sFlow 통합

의존성:
- SDN 컨트롤러 API
- NiaIpSflowLinkageApplication

---

### IP 네트워크 서비스 (4개)

#### 9. NiaIpEquipLinkageApplication (포트: 8009)
**책임**: IP 장비 인벤토리 및 토폴로지 관리

공개 인터페이스:
- `/api/ip-equip/inventory` - 장비 목록
- `/api/ip-equip/links` - 장비 간 링크

의존성:
- PostgreSQL (장비 데이터)

#### 10. NiaIpCountryCodeLinkageApplication (포트: 8010)
**책임**: IP 지역 정보 매핑

공개 인터페이스:
- `/api/ip-geo/lookup` - IP 지역 조회

#### 11. NiaIpAlarmLinkageApplication (포트: 8011)
**책임**: IP 네트워크 기반 알림 생성

공개 인터페이스:
- `/api/ip-alarm/create` - 알림 생성
- `/api/ip-alarm/list` - 알림 목록

의존성:
- RabbitMQ (알림 큐)
- PostgreSQL (NiaAlarmMapper, NiaEquipMapper)

#### 12. NiaIpPreprocessorApplication (포트: 8012)
**책임**: IP 관련 데이터 전처리

---

### SDN 통합 서비스 (4개)

#### 13. NiaIpSdnLinkageApplication (포트: 8013)
**책임**: SDN 컨트롤러 데이터 통합

공개 인터페이스:
- `/api/sdn/controller` - SDN 컨트롤러 정보
- `/api/sdn/flows` - SDN 플로우 관리

의존성:
- OpenFlow 컨트롤러 (ONOS, OpenDaylight)
- REST API 통합

#### 14. NiaIpSdnSyslogLinkageApplication (포트: 8014)
**책임**: SDN 장비 Syslog 수집 및 분석

의존성:
- Syslog 서버
- RabbitMQ

#### 15. NiaIpSdnSyslogPreprocessorApplication (포트: 8015)
**책임**: Syslog 데이터 전처리 및 구조화

#### 16. NiaIpSdnToAiDataLinkageApplication (포트: 8016)
**책임**: SDN 데이터를 AI 분석용으로 변환

의존성:
- AI/ML 파이프라인

---

### 알림 및 RCA 서비스 (4개)

#### 17. NiaAlarmSimulatorApplication (포트: 8017)
**책임**: 테스트용 알림 생성 시뮬레이터

의존성:
- PostgreSQL (AlarmListMapper, RcaResetMapper)

#### 18. NiaIpAlarmSimulatorApplication (포트: 8018)
**책임**: IP 네트워크 알림 시뮬레이터

---

#### 19. NiaAiPerformanceLinkageApplication (포트: 8019)
**책임**: 성능 데이터를 AI 모델에 연계

공개 인터페이스:
- `/api/ai-perf/train` - 모델 학습
- `/api/ai-perf/predict` - 성능 예측

의존성:
- Python ML 서비스
- TensorFlow/PyTorch

#### 20. NiaAiResultLinkageApplication (포트: 8020)
**책임**: AI 분석 결과 통합 및 해석

#### 21. NiaRcaToAiDataLinkageApplication (포트: 8021)
**책임**: RCA(근본 원인 분석) 결과 AI 학습용 데이터 변환

---

### 인프라 및 클러스터 서비스 (3개)

#### 22. NiaClusterApplication (포트: 8022)
**책임**: 클러스터 상태 관리 및 노드 조정

공개 인터페이스:
- `/api/cluster/status` - 클러스터 상태
- `/api/cluster/nodes` - 노드 정보
- `/api/cluster/config` - 설정 관리

의존성:
- PostgreSQL (ClusterMapper)
- EhCache (상태 캐시)

#### 23. NiaLinkageApplication (포트: 8023)
**책임**: 일반적인 데이터 연계 처리

의존성:
- 다양한 소스 시스템

#### 24. NiaSysCheckApplication (포트: 8024)
**책임**: 시스템 헬스 체크 및 모니터링

---

## Engine_nia_optical_pm 모듈 (3개)

### engine-ticket-mba (포트: 6805)
**책임**: 광 네트워크 티켓 생성 및 관리

구조:
- predictiveMaintenance-domain - 도메인 모델
- core - 비즈니스 로직
- code - 생성 코드

### linkage-mba (포트: 6803)
**책임**: 광 네트워크 성능 데이터 연계

### prepro-mba (포트: 6804)
**책임**: 광 성능 데이터 전처리

---

## Engine_nia_ip_sdn_oproute 모듈

### probe (백엔드)
**책임**: 네트워크 프로브 데이터 수집

공개 인터페이스:
- `/api/probe/devices` - 프로브 장치 관리
- `/api/probe/data` - 수집 데이터 조회

### ui/manager (프론트엔드)
**책임**: SDN 라우팅 관리 대시보드

---

## Front_web_2024 애플리케이션 (4개 + 공용 라이브러리)

### app-nia (포트: 8080)
**책임**: 핵심 NIA 모니터링 포털

구성:
- Dashboard - 종합 대시보드
- Topology - 네트워크 토폴로지
- Alarms - 알림 관리
- Performance - 성능 모니터링

의존성:
- common-base, common-web, common-mq, common-ws
- Engine_nia_server_2021의 모든 서비스

### app-ipms (포트: 8081)
**책임**: IP 성능 관리 시스템

### app-dataHub (포트: 8082)
**책임**: 데이터 수집, 저장, 분석

### app-demo (포트: 8083)
**책임**: 기능 데모 및 테스트

### 공용 라이브러리

#### common-base
- 기본 유틸리티 클래스
- 공통 설정
- 로깅 설정

#### common-web
- REST API 기본 구성
- 요청/응답 처리
- 보안 설정 (인증, 권한)

#### common-mq
- RabbitMQ 연결 관리
- 메시지 직렬화/역직렬화
- 큐 정의

#### common-ws
- WebSocket 설정
- 실시간 데이터 푸시
- 세션 관리

---

## 모듈 의존성 그래프

```
┌─────────────────────────────────────────────┐
│         Front_web_2024 포탈                │
│   (app-nia, app-ipms, app-dataHub)         │
└──────────────┬──────────────────────────────┘
               │ REST API
    ┌──────────┴──────────┐
    │                     │
┌───▼────────────────┐   │
│ AI/RCA 서비스 그룹 │   │
│ - AiPerformance    │   │
│ - AiResult         │   │
│ - RcaToAiData      │   │
└────────┬───────────┘   │
         │                │
    ┌────▼────────────────▼──────────┐
    │  연계(Linkage) 서비스 그룹     │
    │  - IP 성능 연계               │
    │  - SDN 연계                   │
    │  - 일반 연계                  │
    └────┬──────────────────────────┘
         │ RabbitMQ
    ┌────▼──────────────────────────┐
    │  규칙 엔진 & 전처리           │
    │  - NiaEngine                  │
    │  - NiaPreprocessor            │
    │  - 특화 Preprocessor들        │
    └────┬──────────────────────────┘
         │ RabbitMQ
    ┌────▼──────────────────────────┐
    │  데이터 소스                   │
    │  - 네트워크 장비              │
    │  - 프로브                     │
    │  - sFlow/NetFlow              │
    │  - Syslog                     │
    └──────────────────────────────┘
```

모든 계층은 PostgreSQL 데이터베이스를 공유하며, RabbitMQ를 통해 비동기 통신합니다.
