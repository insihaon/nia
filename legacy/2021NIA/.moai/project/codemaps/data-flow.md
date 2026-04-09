# NIA 데이터 흐름 및 메시징 아키텍처

## 시스템 레벨 데이터 흐름

### 주요 데이터 경로

```
네트워크 장비/프로브
    ↓
데이터 수집계층 (Preprocessor)
    ↓ RabbitMQ
규칙 엔진 (Engine)
    ↓ RabbitMQ
연계 처리 (Linkage)
    ↓ RabbitMQ
AI 분석 (AI Services)
    ↓
데이터베이스 저장
    ↓
웹 포탈 시각화 (Front_web_2024)
```

---

## RabbitMQ 메시지 큐 토폴로지

### 큐 계층 설계

#### 레벨 1: 수집 큐 (Intake Queues)
원시 데이터 수집 지점:

```
nia.snmp.traps
├── Binding: snmp-exchange → snmp.traps
├── 메시지: SNMP Trap 데이터
├── 소비자: NiaPreprocessorApplication
└── TTL: 300초

nia.syslog.events
├── Binding: syslog-exchange → syslog.events
├── 메시지: Syslog 메시지
├── 소비자: IpSdnSyslogPreprocessor
└── TTL: 300초

nia.netflow.raw
├── Binding: netflow-exchange → netflow.raw
├── 메시지: NetFlow v5/v9 레코드
├── 소비자: TrafficPreprocessorApplication
└── TTL: 300초

nia.sflow.raw
├── Binding: sflow-exchange → sflow.raw
├── 메시지: sFlow 샘플 데이터
├── 소비자: NiaIpSflowLinkageApplication
└── TTL: 300초

nia.icmp.results
├── Binding: icmp-exchange → icmp.results
├── 메시지: ICMP/Ping 결과
├── 소비자: NiaPingPreprocessorApplication
└── TTL: 300초
```

#### 레벨 2: 처리 큐 (Processing Queues)
전처리된 데이터:

```
nia.preprocessor.output
├── 발행자: NiaPreprocessorApplication
├── 소비자: NiaEngineApplication, Linkage 서비스
├── 메시지 형식: {
     "source": "snmp",
     "timestamp": "2024-01-15T10:30:00Z",
     "deviceId": "dev001",
     "data": {...}
   }
└── 보존: 1시간 (dead letter queue 활성)

nia.traffic.processed
├── 발행자: TrafficPreprocessorApplication
├── 소비자: NiaIpPerfLinkageApplication, NiaIpSflowLinkageApplication
└── TTL: 600초

nia.performance.metrics
├── 발행자: NiaPerformanceApplication
├── 소비자: NiaAiPerformanceLinkageApplication, NiaIpPerfLinkageApplication
├── 메시지 형식: {
     "metricId": "m001",
     "deviceId": "dev001",
     "interfaceId": "eth0",
     "latency": 45.2,
     "loss": 0.5,
     "throughput": 9500,
     "timestamp": "2024-01-15T10:30:00Z"
   }
└── 분할: device-id (라우팅 키)
```

#### 레벨 3: 규칙 엔진 큐 (Engine Queues)
엔진 처리 및 의사결정:

```
nia.engine.events
├── 발행자: 모든 Preprocessor, Linkage 서비스
├── 소비자: NiaEngineApplication
├── 라우팅 키: engine.{domain}.{event-type}
│   예: engine.network.threshold_exceeded
│   예: engine.performance.anomaly_detected
└── 우선순위: 5단계 (0-critical, 4-info)

nia.engine.decisions
├── 발행자: NiaEngineApplication
├── 소비자: 관련 Linkage, Action 서비스
├── 메시지 형식: {
     "decisionId": "dec001",
     "event": {...},
     "rules_matched": [...],
     "actions": ["escalate", "notify", "isolate"],
     "confidence": 0.95
   }
├── 분할: action-type (라우팅 키)
└── 우선순위: critical > high > normal
```

#### 레벨 4: 연계 큐 (Linkage Queues)
다중 소스 데이터 연계:

```
nia.linkage.correlation
├── 발행자: NiaEngineApplication
├── 소비자: IP/SDN Linkage 서비스
├── 목적: 성능 + 토폴로지 + 알림 상관관계
└── TTL: 1800초 (30분)

nia.sdn.data
├── 발행자: SDN 컨트롤러
├── 소비자: NiaIpSdnLinkageApplication, IpSdnSflowLinkage
├── 메시지: {
     "controller": "onos",
     "timestamp": "2024-01-15T10:30:00Z",
     "flows": [...],
     "devices": [...],
     "topology": {...}
   }
└── 분할: device-id

nia.ai.input
├── 발행자: NiaIpPerfLinkageApplication, 기타 Linkage
├── 소비자: NiaAiPerformanceLinkageApplication
├── 메시지: 정규화된 특성 벡터
└── 배치 크기: 1000 메시지 또는 300초
```

#### 레벨 5: 알림 큐 (Alert Queues)
알림 발행 및 분배:

```
nia.alarm.critical
├── 발행자: NiaEngineApplication, Linkage 서비스
├── 소비자: NiaIpAlarmLinkageApplication, Front_web_2024 app-nia
├── TTL: 604800초 (7일, 히스토리)
└── 보관: PostgreSQL (nia_alarm 스키마)

nia.alarm.warning
├── 소비자: 웹 포탈, 이메일 서비스
├── TTL: 259200초 (3일)
└── 보관: PostgreSQL (nia_alarm_history)

nia.alarm.info
├── 소비자: 로깅 서비스
├── TTL: 86400초 (1일)
└── 보관: 로그 파일만

nia.alarm.acknowledgement
├── 발행자: Front_web_2024 (사용자 승인)
├── 소비자: NiaAlarmSimulatorApplication, RCA 서비스
└── 메시지: {
     "alarmId": "alm001",
     "acknowledgedBy": "admin",
     "timestamp": "2024-01-15T10:30:00Z",
     "remark": "Known issue, being resolved"
   }
```

#### 레벨 6: AI 분석 큐 (AI Queues)
머신러닝 모델 입력/출력:

```
nia.ai.train
├── 발행자: 성능 + 알림 데이터 통합
├── 소비자: Python ML 파이프라인
├── 메시지: {
     "features": [...],
     "labels": [...],
     "timestamp_range": ["2024-01-01", "2024-01-15"]
   }
└── 일정: 매일 야간 (UTC 0:00)

nia.ai.predict
├── 발행자: NiaAiPerformanceLinkageApplication
├── 소비자: AI 예측 서비스
├── 응답 큐: nia.ai.results
└── 타임아웃: 5초

nia.ai.results
├── 발행자: AI 서비스
├── 소비자: NiaAiResultLinkageApplication
├── 메시지: {
     "predictionId": "pred001",
     "anomaly_score": 0.87,
     "predicted_failure": true,
     "confidence": 0.92
   }
└── 보관: PostgreSQL (nia_ai_results)

nia.rca.data
├── 발행자: AI 분석 결과
├── 소비자: RCA(근본 원인 분석) 서비스
└── 메시지: RCA 학습용 특성 데이터
```

---

## HTTP 요청/응답 흐름

### 웹 포탈 → 백엔드 통신 (Front_web_2024)

#### 시나리오 1: 성능 대시보드 조회

```
클라이언트 요청:
GET /api/nia/performance?device=dev001&timeRange=1h
Authorization: Bearer {token}

경로:
1. app-nia (Spring Boot) 수신
   └─ PerformanceController.getMetrics()

2. 데이터 조회 계층
   └─ PerformanceService.queryMetrics(deviceId, timeRange)

3. 데이터베이스 쿼리
   └─ MyBatis: PerformanceMapper.selectMetrics(deviceId, from, to)

4. 캐시 확인 (EhCache)
   └─ Cache key: "perf:dev001:1h"
   └─ TTL: 5분

5. 응답 생성
{
  "deviceId": "dev001",
  "metrics": [
    {
      "timestamp": "2024-01-15T10:00:00Z",
      "latency": 45.2,
      "loss": 0.5,
      "throughput": 9500,
      "availability": 99.8
    },
    ...
  ],
  "aggregated": {
    "avgLatency": 46.1,
    "maxLoss": 2.1,
    "avgThroughput": 9450
  }
}
```

#### 시나리오 2: 네트워크 토폴로지 조회

```
클라이언트:
GET /api/nia/topology

경로:
1. app-nia
   └─ TopologyController.getTopology()

2. 여러 데이터 소스 통합
   ├─ NiaIpEquipLinkageApplication (장비 목록)
   │  └─ GET /api/ip-equip/inventory
   │
   ├─ NiaIpPerfLinkageApplication (성능 데이터)
   │  └─ GET /api/ip-perf/topology
   │
   └─ SDN 컨트롤러 (토폴로지)
      └─ REST API: http://onos:8181/api/v1/topology

3. 데이터 병합 및 정규화
   └─ TopologyService.mergeAndNormalize()

4. 응답
{
  "nodes": [
    {
      "id": "dev001",
      "name": "Core Switch 1",
      "type": "switch",
      "status": "up",
      "performance": {...}
    },
    ...
  ],
  "edges": [
    {
      "source": "dev001",
      "target": "dev002",
      "linkType": "ethernet",
      "bandwidth": 10000,
      "utilization": 45.2
    },
    ...
  ]
}
```

---

## 데이터베이스 저장 및 조회 흐름

### PostgreSQL 스키마 구조

#### 성능 메트릭 저장

```
저장 흐름:
1. RabbitMQ: nia.performance.metrics 큐 수신
   └─ Message: {
       "metricId": "m001",
       "deviceId": "dev001",
       "interfaceId": "eth0",
       "latency": 45.2,
       "loss": 0.5,
       "throughput": 9500,
       "timestamp": "2024-01-15T10:30:00Z"
     }

2. NiaPerformanceApplication 처리
   └─ PerformanceConsumer.onMessage()
   └─ PerformanceService.save()

3. Database 저장
   INSERT INTO nia_performance.metrics (
     metric_id, device_id, interface_id,
     latency, loss, throughput, timestamp
   ) VALUES (...)

4. 인덱스
   ├─ PRIMARY KEY (metric_id)
   ├─ INDEX (device_id, timestamp) - 빠른 범위 쿼리
   ├─ INDEX (interface_id, timestamp)
   └─ PARTITION BY RANGE (timestamp) - 월별 파티션
```

#### 알림 저장

```
저장 흐름:
1. RabbitMQ: nia.alarm.critical 수신
   └─ Message: 알림 데이터

2. NiaIpAlarmLinkageApplication
   └─ AlarmService.createAlarm()

3. Database 저장
   INSERT INTO nia_alarm.alarms (
     alarm_id, severity, source, description,
     created_at, status, acknowledged_at
   ) VALUES (...)

4. 히스토리 자동 보관
   └─ 7일 후 nia_alarm.alarm_history로 이동
   └─ 1년 후 아카이브 테이블로 이동
```

---

## 실시간 데이터 스트림 (WebSocket)

### 대시보드 실시간 업데이트

```
연결 수립:
1. 브라우저 → app-nia
   ws://localhost:8080/ws/dashboard

2. Spring WebSocket 핸들러
   └─ DashboardWebSocketHandler.afterConnectionEstablished()
   └─ 세션 등록 및 초기 데이터 전송

3. STOMP 구독 (선택)
   SUBSCRIBE: /topic/dashboard/dev001
   └─ 특정 장비의 실시간 데이터 수신

실시간 푸시:
1. NiaPerformanceApplication에서 메트릭 처리
   └─ performancePublisher.publishUpdate(metric)

2. RabbitMQ → app-nia 직접 푸시 (외부 큐)
   └─ Queue: nia.websocket.dashboard
   └─ Consumer: DashboardBroadcaster

3. WebSocket 푸시
   {
     "type": "metric_update",
     "deviceId": "dev001",
     "metric": {
       "latency": 45.2,
       "loss": 0.5,
       "timestamp": "2024-01-15T10:30:00Z"
     }
   }

4. 브라우저 Vue 컴포넌트 업데이트
   └─ Dashboard.vue → websocket 메시지 수신
   └─ Vuex store 업데이트
   └─ UI 리렌더링
```

---

## 요청 생명주기 예시: 알림 생성부터 시각화까지

```
타임스탬프 | 단계 | 처리 | 소요시간
========================================
T+0ms    | 데이터 수집
         | 네트워크 장비에서 SNMP Trap 수신
         | NiaPreprocessorApplication UDP 162
         |
T+10ms   | 전처리
         | ValidationService 검증
         | RabbitMQ 발행: nia.preprocessor.output
         |
T+20ms   | 엔진 처리
         | NiaEngineApplication 수신
         | RuleEngine.evaluate()
         | Drools 규칙 평가
         |
T+30ms   | 의사결정
         | Drools 규칙 매칭
         | RabbitMQ 발행: nia.engine.decisions
         |
T+40ms   | 연계 처리
         | NiaIpPerfLinkageApplication 수신
         | 성능 + 토폴로지 상관관계 분석
         | RabbitMQ 발행: nia.linkage.correlation
         |
T+60ms   | 알림 생성
         | NiaIpAlarmLinkageApplication 수신
         | AlarmService.createAlarm()
         | RabbitMQ 발행: nia.alarm.critical
         |
T+70ms   | 데이터베이스 저장
         | PostgreSQL INSERT
         | nia_alarm.alarms 테이블
         |
T+80ms   | 웹소켓 푸시
         | RabbitMQ: nia.websocket.alerts 큐
         | app-nia DashboardBroadcaster
         | WebSocket 메시지 전송
         |
T+90ms   | 브라우저 렌더링
         | Vue Alarms 컴포넌트 업데이트
         | 사용자 화면에 표시
         |
========== 총 90ms 엔드-투-엔드 지연 ==========
```

---

## 배치 데이터 처리

### 밤 시간 AI 모델 학습

```
시간       | 작업
==============================================
00:00 UTC | 배치 작업 시작
          | AiBatchScheduler.trainModels()
          |
00:05     | 데이터 수집
          | 지난 24시간 메트릭 및 알림 쿼리
          | SELECT * FROM nia_performance.metrics
          | WHERE timestamp > now() - '1 day'::interval
          |
00:30     | 데이터 정규화
          | 특성 벡터 생성
          | RabbitMQ: nia.ai.train 발행
          | 메시지: 1000개씩 배치 처리
          |
01:00     | AI 모델 학습
          | Python ML 서비스 실행
          | TensorFlow/PyTorch 학습
          |
02:00     | 모델 저장
          | PostgreSQL: nia_ai.models 테이블
          | 버전 관리 (v1.0, v1.1 등)
          |
02:30     | 검증 테스트
          | 테스트 데이터셋 평가
          | 정확도 > 90% 확인
          |
03:00     | 배포
          | 새 모델을 예측 서비스로 로드
          | 구 모델 백업
          |
03:30     | 작업 완료
          | 로그 저장 및 메트릭 기록
```

---

## 오류 처리 및 Dead Letter Queue (DLQ)

### DLQ 흐름

```
정상 큐에서 처리 실패:

메시지 → nia.preprocessor.output
         (처리 시도)
         └─ 예외 발생
         └─ 재시도 3회 실패
         └─ 타임아웃 (10초)

→ Dead Letter Queue로 이동

nia.preprocessor.output.dlq
├── 메시지 이동: 최대 50개 메시지 유지
├── TTL: 86400초 (1일)
├── 모니터링: 알람 발생 (100+ 메시지)
└── 관리자 수동 재처리 대기

DLQ 메시지 예시:
{
  "original_queue": "nia.preprocessor.output",
  "message": {...},
  "error": "Database connection timeout",
  "attempts": 3,
  "first_attempt_time": "2024-01-15T10:30:00Z",
  "last_attempt_time": "2024-01-15T10:30:30Z"
}
```

---

## 성능 최적화 고려사항

### 메시징 처리량
- RabbitMQ 처리량: 초당 10,000+ 메시지
- 주요 병목: 데이터베이스 INSERT 성능
- 최적화: 배치 INSERT (1,000개씩), 파티션 테이블

### 캐싱 전략
- EhCache: 토폴로지 (5분 TTL), 규칙 (10분 TTL)
- Redis: 세션, 실시간 대시보드 캐시
- 적중률 목표: 70%+

### 데이터베이스 쿼리
- 인덱스: device_id, timestamp 복합 인덱스
- 파티션: 월별 시계열 데이터
- 쿼리 타임아웃: 30초

### WebSocket 연결
- 동시 연결: 최대 1,000명 사용자
- 메시지 압축: Gzip 활성
- 핑/퐁: 30초 간격 (연결 유지)
