# NIA 프로젝트 의존성 분석

## Engine_nia_server_2021 외부 의존성

### Spring Boot 및 기본 프레임워크
- **Spring Boot** 2.6.x ~ 2.7.x - 마이크로서비스 애플리케이션 프레임워크
  - spring-boot-starter-web - REST API
  - spring-boot-starter-data-jpa - ORM
  - spring-boot-starter-data-mybatis - SQL 매핑
  - spring-boot-starter-amqp - RabbitMQ 클라이언트
  - spring-boot-starter-logging - 로깅

### 메시징 및 통신
- **RabbitMQ** (AMQP 프로토콜) - 마이크로서비스 간 비동기 메시징
  - spring-rabbit - Spring 래퍼
  - spring-amqp - AMQP 스펙 구현

  주요 큐:
  - `nia.engine.events` - 엔진 이벤트
  - `nia.preprocessor.intake` - 전처리 데이터 수집
  - `nia.performance.metrics` - 성능 메트릭
  - `nia.alarm.*` - 알림 관련 큐들
  - `nia.sdn.*` - SDN 데이터 큐
  - `nia.ai.*` - AI 분석 큐

### 데이터베이스 및 ORM
- **PostgreSQL** - 메인 데이터베이스
  - JDBC 드라이버: `org.postgresql:postgresql`
  - 버전: 11.x ~ 14.x 권장

  주요 데이터베이스 스키마:
  - `nia_engine` - 규칙 엔진 데이터
  - `nia_network` - 네트워크 토폴로지 및 장비
  - `nia_performance` - 성능 메트릭
  - `nia_alarm` - 알림 데이터
  - `nia_ai` - AI 모델 및 결과

- **MyBatis** 3.x - SQL 매핑 프레임워크
  - mybatis-spring-boot-starter
  - XML 매퍼: `src/main/resources/sqlmap/`

  주요 매퍼:
  - PerformanceMapper.xml
  - ClusterMapper.xml
  - TopologyMapper.xml
  - AlarmListMapper.xml
  - RcaSystemConfigMapper.xml

- **Hibernate/JPA** - Object-Relational Mapping
  - spring-data-jpa
  - Entity 클래스: `com.nia.*.domain.entity`

### 캐싱
- **EhCache** 3.x - 분산 캐싱
  - ehcache - 코어 라이브러리
  - spring-data-cache - Spring 통합
  - 설정: `ehcache-context.xml`

  캐시 영역:
  - `topology` - 토폴로지 캐시
  - `equipment` - 장비 정보 캐시
  - `rules` - 규칙 캐시 (10분 TTL)

- **Redis** (선택) - 분산 세션 및 캐시
  - spring-boot-starter-data-redis
  - jedis 클라이언트

### 규칙 엔진
- **Drools** 7.x - 규칙 기반 의사결정
  - drools-core
  - drools-compiler
  - kie-spring
  - Kmodule: `src/main/resources/META-INF/kmodule.xml`

### 로깅
- **Logback** 1.2.x - SLF4J 구현
  - spring-boot-starter-logging
  - 설정: `logback-spring.xml`
  - 로그 레벨: DEBUG (개발), INFO (운영)

- **Log4j 2.x** (일부 서비스)
  - log4j2.xml 설정

### 테스트
- **JUnit 4** ~ **JUnit 5** - 단위 테스트
  - junit-jupiter-api
  - junit-jupiter-engine

- **Mockito** - Mock 객체 라이브러리
  - mockito-core
  - mockito-junit-jupiter

- **Spring Test** - Spring 테스트 지원
  - spring-boot-starter-test
  - spring-test-context-framework

### 유틸리티 라이브러리
- **Lombok** - 보일러플레이트 코드 감소
  - @Data, @Getter, @Setter, @Builder

- **Jackson** - JSON 직렬화/역직렬화
  - jackson-databind
  - jackson-datatype-jsr310 (LocalDateTime)

- **Apache Commons** - 공통 유틸리티
  - commons-lang3
  - commons-codec
  - commons-io

- **Guava** - Google 유틸리티 라이브러리
  - 컬렉션, 캐싱, 함수형 프로그래밍

---

## Engine_nia_optical_pm 외부 의존성

### Spring Boot
- **Spring Boot** 3.5.8 - 최신 버전
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-amqp

### Java 버전
- **Java 17** - LTS 버전, 최신 언어 기능 사용
  - Records, Sealed Classes 등 지원

### Maven 모듈 구조
```
Engine_nia_optical_pm/
├── pom.xml (부모 POM)
├── predictiveMaintenance-domain/ - 도메인 모델
├── core/ - 비즈니스 로직
└── code/ - 생성 코드 및 스캐폴딩
```

### 추가 의존성
- **데이터 과학**: Python ML 라이브러리 호출
  - TensorFlow, PyTorch 연동
  - Spring Cloud 기반 마이크로서비스
  - Micrometer - 메트릭 수집

---

## Engine_nia_ip_sdn_oproute 의존성

### 백엔드 (probe)
- **Spring Boot** 2.5.3
  - REST API 서버
  - PostgreSQL 연동

- **SDN 컨트롤러 연동**
  - OpenFlow 라이브러리
  - ONOS/OpenDaylight REST API 클라이언트

### 프론트엔드 (ui/manager)
- **Vue 3.2** - 프로그레시브 JavaScript 프레임워크
  - Composition API
  - @vueuse/core - Vue 유틸리티

- **번들러**: Webpack
  - vue-loader - Vue 파일 처리
  - babel-loader - ES6+ 트랜스파일링

- **UI 라이브러리**
  - Element Plus - Vue 3 UI 컴포넌트

---

## Front_web_2024 의존성

### 백엔드 (BE)

#### Spring Boot
- **Spring Boot** 2.7.17
  - Gradle 빌드 시스템 사용

#### 멀티 모듈 구성
```
BE/
├── app-nia/
├── app-ipms/
├── app-dataHub/
├── app-demo/
├── common-base/
├── common-web/
├── common-mq/
└── common-ws/
```

#### 공용 라이브러리 의존성
- **common-base**
  - 기본 유틸리티 클래스
  - 공통 설정 프로퍼티

- **common-web**
  - Spring Security - 인증/권한
  - Spring AOP - 횡단 관심사
  - Jackson - JSON 처리

- **common-mq**
  - RabbitMQ 설정 및 연결
  - 메시지 컨버터
  - 데드레터 큐 처리

- **common-ws**
  - Spring WebSocket
  - STOMP 프로토콜
  - 세션 관리

#### 데이터 접근
- **JPA/Hibernate** - ORM
- **MyBatis** - 복잡 쿼리용 SQL 매핑
- **QueryDSL** (선택) - 타입 안전 쿼리

#### API 문서
- **Springfox/SpringDoc** - Swagger/OpenAPI 문서
  - springdoc-openapi-ui
  - `/api-docs` 엔드포인트

### 프론트엔드 (FE)

#### Vue 2.6.11
- **vue** - 프로그레시브 프레임워크
- **vue-router** - 클라이언트 사이드 라우팅
- **vuex** - 상태 관리 (또는 Pinia)

#### UI 컴포넌트 라이브러리
- **Element UI** 2.x - 엔터프라이즈 UI 컴포넌트
  - 폼, 테이블, 대화상자, 탭 등

- **ag-Grid** - 고성능 데이터 그리드
  - 수천 행 고속 렌더링
  - 필터, 정렬, 그룹화

#### 차트 라이브러리
- **ECharts** 5.x - 강력한 시각화
  - 라인, 바, 파이, 히트맵 등
  - 100K+ 데이터 포인트 처리

- **C3.js** (레거시)
  - D3.js 래퍼

#### 통신 및 요청
- **Axios** - Promise 기반 HTTP 클라이언트
  - 요청/응답 인터셉터
  - 자동 직렬화/역직렬화

- **Socket.io** - 실시간 양방향 통신
  - WebSocket fallback
  - 자동 재연결

#### 빌드 및 개발
- **Webpack** - 모듈 번들러
  - vue-loader - Vue 파일 처리
  - babel-loader - ES6+ 변환
  - css-loader, style-loader - CSS 처리
  - file-loader - 이미지/폰트 처리

- **Babel** 7.x - JavaScript 트랜스파일러
  - @babel/core
  - @babel/preset-env
  - @babel/plugin-proposal-class-properties

#### 개발 도구
- **npm** - 패키지 관리자
  - `npm install` - 의존성 설치
  - `npm run dev` - 개발 서버 실행
  - `npm run build` - 프로덕션 빌드

- **Node.js** 14.x ~ 18.x

#### 유틸리티
- **lodash** - JavaScript 유틸리티 라이브러리
- **moment.js** - 날짜 시간 처리
- **dayjs** - 경량 moment 대체
- **js-cookie** - 쿠키 관리

---

## 내부 의존성 매트릭스

### Engine_nia_server_2021 서비스 간 의존성

| 의존하는 서비스 | 의존되는 서비스 | 방식 | 설명 |
|---|---|---|---|
| Linkage 서비스들 | NiaEngine | RabbitMQ | 규칙 엔진 결과 수신 |
| NiaIpAlarmLinkage | NiaEngine | RabbitMQ | 알림 이벤트 |
| NiaAiPerformance | NiaPerformance | RabbitMQ | 성능 데이터 |
| NiaAiResult | AI 서비스 | REST API | 모델 학습/예측 |
| Front_web_2024 | 모든 Engine_nia_server_2021 | REST API | 데이터 조회 및 관리 |
| Engine_nia_optical_pm | Engine_nia_server_2021 | RabbitMQ | 데이터 통합 |

### 공통 데이터 저장소

모든 서비스는 **PostgreSQL** 단일 데이터베이스를 공유합니다:
- 서비스별 데이터 격리: 스키마 기반 (예: `nia_engine.*`, `nia_alarm.*`)
- 트랜잭션 일관성: ACID 준수
- 쿼리 복잡성: MyBatis XML 매퍼 사용

### 외부 시스템 연동

| 외부 시스템 | 연동 서비스 | 프로토콜 | 목적 |
|---|---|---|---|
| 네트워크 장비 | NiaPreprocessor | SNMP, Syslog | 데이터 수집 |
| sFlow/NetFlow | Traffic 서비스 | UDP | 트래픽 데이터 |
| SDN 컨트롤러 | IpSdnLinkage | OpenFlow API | 네트워크 제어 |
| Syslog 서버 | IpSdnSyslog | UDP 514 | 로그 수집 |
| AI/ML 파이프라인 | AiPerformance | gRPC/REST | 모델 연동 |

---

## 버전 호환성

### Spring Boot 버전 전략
- **2.6.x ~ 2.7.x**: Engine_nia_server_2021, Front_web_2024 (안정 버전)
- **3.5.8**: Engine_nia_optical_pm (최신 기능)

마이그레이션 계획:
1. Engine_nia_server_2021 → Spring Boot 3.x (Java 17)
2. Front_web_2024 → Vue 3.x, Spring Boot 3.x

### Java 버전
- **Java 8 ~ 11**: Engine_nia_server_2021 (보안 업데이트)
- **Java 17**: Engine_nia_optical_pm (최신 LTS)

마이그레이션 순서:
1. 새 프로젝트: Java 17+
2. 기존 서비스: Java 11 권장
3. Legacy: Java 8 EOL (2026년)

---

## 성능 최적화 의존성

### 데이터베이스
- **HikariCP** - 커넥션 풀 관리
  - 기본 풀 크기: 10, 최대: 30
  - 연결 타임아웃: 30초

### 캐싱 전략
- **EhCache**: 로컬 캐시 (토폴로지, 규칙)
- **Redis** (선택): 분산 캐시, 세션 저장소

### 모니터링
- **Micrometer** - 메트릭 수집
- **Prometheus** - 메트릭 저장소
- **Grafana** - 시각화

---

## 보안 의존성

### 인증 및 권한
- **Spring Security** - 세션 기반 인증
- **JWT** (선택) - 토큰 기반 인증

### 암호화
- **Spring Security Crypto** - 패스워드 암호화
- **Bouncy Castle** - 암호화 라이브러리 (선택)

### 감사 로깅
- **Audit Logging** - 사용자 행동 추적
- **Database Audit** - PostgreSQL 감사 로그
