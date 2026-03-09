# NIA 프로젝트 진입점 및 API 라우팅

## Engine_nia_server_2021 진입점

### 각 마이크로서비스 Main 클래스

#### 1. NiaEngineApplication
```
위치: Engine_nia_server_2021/NiaEngineApplication/src/main/java/com/nia/engine/
Main 클래스: NiaEngineApplication.java
포트: 8001
```

**REST API 엔드포인트**:
- `GET /api/engine/rules` - 활성 규칙 목록
- `POST /api/engine/rules` - 새 규칙 생성
- `PUT /api/engine/rules/{ruleId}` - 규칙 수정
- `DELETE /api/engine/rules/{ruleId}` - 규칙 삭제
- `POST /api/engine/events` - 이벤트 처리
- `GET /api/engine/decisions` - 의사결정 결과 조회
- `GET /actuator/health` - 헬스 체크

**웹소켓 엔드포인트**:
- `ws://localhost:8001/ws/events` - 실시간 이벤트 스트림

**스케줄된 작업**:
- Rule compilation (매 5분)
- Cache cleanup (매 시간)
- Health check (매 30초)

**RabbitMQ 큐 소비**:
- `nia.engine.events` - 수신 이벤트
- `nia.engine.alerts` - 알림 발행

---

#### 2. NiaPreprocessorApplication
```
위치: Engine_nia_server_2021/NiaPreprocessorApplication/src/main/java/com/nia/preprocessor/
Main 클래스: NiaPreprocessorApplication.java
포트: 8002
```

**REST API 엔드포인트**:
- `POST /api/preprocessor/intake` - 원시 데이터 수집
- `GET /api/preprocessor/status` - 전처리 상태 조회
- `GET /api/preprocessor/metrics` - 처리 메트릭
- `POST /api/preprocessor/validate` - 데이터 검증

**데이터 수집 포트**:
- UDP 162 - Trap 수신 (SNMP)
- TCP 514 - Syslog 수신

**RabbitMQ 큐 발행**:
- `nia.preprocessor.output` - 처리된 데이터

---

#### 3. NiaPerformanceApplication
```
위치: Engine_nia_server_2021/NiaPerformanceApplication/src/main/java/com/nia/performance/
Main 클래스: NiaPerformanceApplication.java
포트: 8003
```

**REST API 엔드포인트**:
- `GET /api/performance/metrics` - 성능 지표 조회 (시간 범위 쿼리)
- `GET /api/performance/metrics?from=2024-01-01&to=2024-01-31` - 범위 조회
- `GET /api/performance/alerts` - 성능 기반 알림 목록
- `POST /api/performance/anomaly` - 이상 탐지 요청
- `GET /actuator/metrics` - 애플리케이션 메트릭

**쿼리 파라미터**:
- `deviceId` - 장비 ID
- `interfaceId` - 인터페이스 ID
- `metricType` - latency, loss, throughput
- `timeRange` - 1h, 6h, 24h, 7d, 30d

**RabbitMQ 큐**:
- 수신: `nia.performance.raw`
- 발행: `nia.performance.processed`

---

#### 4. NiaIpPerfLinkageApplication
```
위치: Engine_nia_server_2021/NiaIpPerfLinkageApplication/src/main/java/com/nia/perf/
Main 클래스: NiaIpPerfLinkageApplication.java
포트: 8004
```

**REST API 엔드포인트**:
- `GET /api/ip-perf/link/{deviceId}` - 기기 성능 연계
- `GET /api/ip-perf/topology` - 토폴로지 기반 성능 분석
- `GET /api/ip-perf/impact` - 성능 영향도 분석
- `POST /api/ip-perf/correlation` - 성능 상관관계 분석

---

#### 5. NiaPingPreprocessorApplication
```
포트: 8005
```

**REST API**:
- `GET /api/ping/probe` - Ping 프로브 상태
- `POST /api/ping/probe` - 새 프로브 생성
- `GET /api/ping/results` - Ping 결과 조회

**ICMP 기반 모니터링**:
- 대상: 구성된 네트워크 범위
- 간격: 60초 (설정 가능)
- 타임아웃: 5초

---

#### 6. NiaTrafficPreprocessorApplication
```
포트: 8006
```

**데이터 수집 프로토콜**:
- NetFlow v5/v9 수신 포트: UDP 2055
- sFlow 수신 포트: UDP 6343
- IPFIX 수신 포트: UDP 4739

**REST API**:
- `GET /api/traffic/flows` - 플로우 데이터 조회
- `POST /api/traffic/analyze` - 트래픽 분석 요청

---

#### 7-16. 추가 마이크로서비스
모든 서비스는 유사한 구조를 따릅니다:

```
포트: 8007 ~ 8016
Main 클래스: {ServiceName}Application.java
위치: Engine_nia_server_2021/{ServiceName}Application/

REST API 패턴:
- GET /api/{service}/{resource} - 조회
- POST /api/{service}/{resource} - 생성
- PUT /api/{service}/{resource}/{id} - 수정
- DELETE /api/{service}/{resource}/{id} - 삭제
- GET /actuator/* - 작동 엔드포인트
```

---

### Engine_nia_server_2021 공통 엔드포인트

**모든 서비스에서 지원**:

```
GET /actuator/health - 서비스 헬스 상태
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "rabbit": {"status": "UP"},
    "diskSpace": {"status": "UP"}
  }
}

GET /actuator/info - 서비스 정보
{
  "app": {
    "name": "NiaEngineApplication",
    "version": "2.1.0",
    "buildTime": "2024-01-15T10:30:00Z"
  }
}

GET /actuator/metrics - 메트릭 목록
{
  "names": [
    "jvm.memory.used",
    "process.cpu.usage",
    "system.load.average"
  ]
}

GET /actuator/metrics/{metricName} - 특정 메트릭
```

---

## Engine_nia_optical_pm 진입점

### 멀티 모듈 구조

```
위치: Engine_nia_optical_pm/
부모 POM: pom.xml
```

#### 1. engine-ticket-mba
```
Main 클래스: EngineTicketMbaApplication.java
포트: 6805
```

**REST API**:
- `GET /api/tickets` - 티켓 목록
- `POST /api/tickets` - 새 티켓 생성
- `GET /api/tickets/{id}` - 티켓 상세
- `PUT /api/tickets/{id}/status` - 티켓 상태 변경
- `POST /api/tickets/{id}/assign` - 담당자 할당

**RabbitMQ 큐**:
- 수신: `nia_optical.events`
- 발행: `nia_optical.tickets`

---

#### 2. linkage-mba
```
Main 클래스: LinkageMbaApplication.java
포트: 6803
```

**REST API**:
- `GET /api/linkage/data` - 연계 데이터 조회
- `POST /api/linkage/sync` - 데이터 동기화
- `GET /api/linkage/status` - 연계 상태

---

#### 3. prepro-mba
```
Main 클래스: PreproMbaApplication.java
포트: 6804
```

**REST API**:
- `POST /api/prepro/process` - 데이터 전처리
- `GET /api/prepro/output` - 전처리 결과 조회

---

## Engine_nia_ip_sdn_oproute 진입점

### 백엔드 (probe)
```
위치: Engine_nia_ip_sdn_oproute/probe/src/main/java/
Main 클래스: ProbeApplication.java
포트: 9001
```

**REST API**:
- `GET /api/probe/devices` - 프로브 장치 목록
- `POST /api/probe/devices` - 새 장치 등록
- `GET /api/probe/data` - 수집 데이터 조회
- `POST /api/probe/collect` - 수집 명령
- `GET /api/probe/status` - 수집 상태

**데이터 수집**:
- netstat, ifconfig, route 명령 실행
- 수집 간격: 30초 (설정 가능)

---

### 프론트엔드 (ui/manager)
```
위치: Engine_nia_ip_sdn_oproute/ui/manager/
진입점: index.html
포트: 8888 (개발 서버)
```

**Vue 라우팅**:
```
메인 페이지: /
대시보드: /dashboard
라우팅 관리: /routing
토폴로지: /topology
설정: /settings
```

---

## Front_web_2024 진입점

### 백엔드 멀티 모듈 구조

#### 1. app-nia
```
위치: Front_web_2024/BE/app-nia/
Main 클래스: AppNiaApplication.java
포트: 8080
Gradle 빌드: ./gradlew bootRun
```

**REST API 기본 경로**: `/api/nia`

주요 엔드포인트:
```
GET /api/nia/dashboard - 메인 대시보드
GET /api/nia/topology - 네트워크 토폴로지
GET /api/nia/alarms - 활성 알림 목록
GET /api/nia/performance - 성능 데이터
GET /api/nia/devices - 네트워크 장비 목록
GET /api/nia/interfaces - 인터페이스 목록
POST /api/nia/config - 설정 저장
GET /api/nia/reports - 리포트 목록
POST /api/nia/export - 데이터 내보내기
```

**인증 엔드포인트**:
```
POST /api/auth/login - 사용자 로그인
POST /api/auth/logout - 로그아웃
GET /api/auth/user - 현재 사용자 정보
POST /api/auth/refresh - 토큰 갱신
```

**WebSocket**:
```
ws://localhost:8080/ws/dashboard - 실시간 대시보드 데이터
ws://localhost:8080/ws/alarms - 실시간 알림
```

---

#### 2. app-ipms
```
포트: 8081
```

**API 경로**: `/api/ipms`

엔드포인트:
```
GET /api/ipms/performance - IP 성능 메트릭
GET /api/ipms/flows - 플로우 데이터
POST /api/ipms/analyze - 성능 분석
```

---

#### 3. app-dataHub
```
포트: 8082
```

**API 경로**: `/api/datahub`

엔드포인트:
```
GET /api/datahub/data - 데이터 조회
POST /api/datahub/ingest - 데이터 수집
GET /api/datahub/export - 데이터 내보내기
```

---

#### 4. app-demo
```
포트: 8083
```

**테스트 및 데모 엔드포인트**:
```
GET /demo/sample-data - 샘플 데이터
POST /demo/simulate-alarm - 알림 시뮬레이션
GET /demo/test-topology - 테스트 토폴로지
```

---

### 프론트엔드 (FE)

```
위치: Front_web_2024/FE/
진입점: src/main.js
포트: 8000 (npm run dev)
```

**빌드 명령**:
```bash
npm install - 의존성 설치
npm run dev - 개발 서버 실행 (http://localhost:8000)
npm run build - 프로덕션 빌드
npm run build:nia - app-nia 전용 빌드
npm run build:ipms - app-ipms 전용 빌드
```

**Vue 라우터 경로**:
```
/ - 메인 페이지
/login - 로그인
/dashboard - 종합 대시보드
/topology - 네트워크 토폴로지
/alarms - 알림 관리
/performance - 성능 모니터링
/devices - 장비 관리
/reports - 리포트
/settings - 시스템 설정
/admin - 관리자 페이지
```

---

## 스케줄된 작업

### Engine_nia_server_2021

#### NiaEngineApplication
- **Rule Compilation** (매 5분)
  - 메서드: `RuleEngine.compileRules()`
  - 목적: Drools 규칙 재컴파일

- **Cache Cleanup** (매 1시간)
  - 메서드: `CacheManager.evict()`
  - 목적: 만료된 캐시 항목 제거

- **Topology Sync** (매 30분)
  - 메서드: `TopologyService.sync()`
  - 목적: 네트워크 토폴로지 갱신

#### NiaPerformanceApplication
- **Metric Aggregation** (매 1분)
  - 메서드: `MetricAggregator.aggregate()`
  - 목적: 성능 메트릭 집계

- **Alert Generation** (매 5분)
  - 메서드: `AlertService.generateAlerts()`
  - 목적: 알림 조건 평가 및 생성

---

## 클라이언트 SDK 및 도구

### 명령행 도구

**Docker Compose 배포**:
```bash
cd Front_web_2024
docker-compose up -d
```

**Gradle 빌드**:
```bash
cd Front_web_2024/BE/app-nia
./gradlew clean build
./gradlew bootRun
```

**Maven 빌드** (Engine_nia_server_2021):
```bash
cd Engine_nia_server_2021/NiaEngineApplication
mvn clean package
java -jar target/nia-engine-2.1.0.jar
```

---

## API 인증

### Spring Security 설정

모든 엔드포인트는 인증 필요:

```
POST /api/auth/login
{
  "username": "admin",
  "password": "password"
}

응답:
{
  "token": "eyJhbGc...",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "username": "admin",
    "roles": ["ROLE_ADMIN"]
  }
}
```

**요청 헤더**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

---

## 오류 응답 형식

모든 서비스에서 표준 오류 응답:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid parameter: deviceId",
  "path": "/api/performance/metrics"
}
```

**공통 상태 코드**:
- 200: 성공
- 201: 생성 성공
- 400: 잘못된 요청
- 401: 인증 실패
- 403: 권한 부족
- 404: 리소스 없음
- 500: 서버 오류
