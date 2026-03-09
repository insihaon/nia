# NIA 기술 스택

## 기술 스택 개요

NIA는 마이크로서비스 아키텍처를 기반으로 다양한 최신 기술들을 조합하여 구축되었습니다. 프로젝트별로 상이한 스택을 사용하여 각 서브프로젝트의 특성에 맞춘 최적화를 수행합니다.

## 전사 기술 스택 공통

### 프로그래밍 언어
- **Java**: 메인 프로젝트의 핵심 언어 (Java 8, 11, 17 혼용)
- **JavaScript/TypeScript**: 프론트엔드 애플리케이션
- **Python**: 데이터 분석 및 머신러닝(선택적)

### 데이터베이스
- **PostgreSQL**: 중앙 관계형 데이터베이스
  - 모든 서브프로젝트가 동일하게 사용
  - JDBC 드라이버를 통한 Java 연동
  - 대규모 네트워크 데이터 저장 및 분석
  - 포트: 5432 (기본)
  - 연동 라이브러리: JDBC Driver, JPA, MyBatis

### 메시징 및 이벤트
- **RabbitMQ**: 마이크로서비스 간 비동기 통신
  - AMQP 프로토콜 기반
  - 포트: 5672 (메시징), 15672 (관리 UI)
  - Spring AMQP를 통한 통합

### 보안
- **Spring Security**: 인증 및 인가
- **Jasypt**: 민감 데이터 암호화
  - application.yml의 데이터베이스 패스워드 암호화
  - API 키 등 보안 정보 보호
- **BouncyCastle**: SSL/TLS 및 암호화 지원

### 빌드 도구
- **Maven**: nia-server-2021, nia_optical_pm, Engine_nia_ip_sdn_oproute 백엔드
- **Gradle**: web_2024 백엔드
- **Webpack/Vue CLI**: 프론트엔드 빌드

### 컨테이너화
- **Docker**: 각 마이크로서비스 컨테이너화
  - Dockerfile 제공
  - Docker Compose로 로컬 테스트 환경 구성
- **Kubernetes**: 운영 환경 배포 및 관리

## 서브프로젝트별 기술 스택

### 1. nia-server-2021 (핵심 엔진 플랫폼)

#### 백엔드 기술 스택

| 영역 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **프레임워크** | Spring Boot | 2.3.8~2.7.17 | 마이크로서비스 기반 애플리케이션 |
| **프로그래밍 언어** | Java | 8-11 | 메인 비즈니스 로직 |
| **빌드 도구** | Maven | 3.6+ | 의존성 관리 및 빌드 자동화 |
| **데이터베이스** | PostgreSQL | 12+ | 데이터 저장소 |
| **ORM** | Spring Data JPA / MyBatis | - | 데이터베이스 접근 |
| **메시징** | Spring AMQP | 2.x | RabbitMQ 통합 |
| **로깅** | Log4j2 | 2.x | 구조화된 로깅 |
| **보안** | Jasypt | 3.x | 암호화 |
| **API 문서** | Springfox/Swagger | 3.x | API 명세 자동 생성 |

#### 스프링 부트 마이크로서비스 특성

- **21개 독립 애플리케이션**: 각각 단독 포트로 실행
- **자동 설정**: application.yml에서 데이터베이스, RabbitMQ, 로깅 설정
- **내장 톰캣**: 별도 애플리케이션 서버 불필요
- **엑추에이터**: /actuator/health, /actuator/metrics 모니터링

#### 주요 의존성

```
Spring Boot Starter Web       - REST API 구현
Spring Boot Starter Data JPA  - 데이터베이스 접근
Spring Boot Starter AMQP      - RabbitMQ 메시징
Spring Boot Starter Logging   - Log4j2 기반 로깅
Spring Security              - 인증/인가
Jasypt Spring Boot            - 데이터 암호화
```

#### 빌드 및 실행

```bash
# 의존성 다운로드 및 빌드
mvn clean package

# 개별 서비스 실행 예시
java -jar target/nia-engine-application-1.0.0.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/nia \
  --spring.datasource.username=nia_user \
  --spring.rabbitmq.host=localhost \
  --server.port=6800

# 프로파일 기반 실행 (dev, test, prod)
java -jar target/nia-engine-application-1.0.0.jar --spring.profiles.active=prod
```

### 2. nia_optical_pm (광학 네트워크 예측 유지보수)

#### 기술 스택

| 영역 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **프레임워크** | Spring Boot | 3.5.8 | 최신 스프링 기능 활용 |
| **프로그래밍 언어** | Java | 17 | 최신 자바 기능 (레코드, 패턴 매칭) |
| **빌드 도구** | Maven | 3.8+ | 다중 모듈 관리 |
| **데이터베이스** | PostgreSQL | 12+ | 광학 성능 데이터 저장 |
| **메시징** | Spring AMQP | 2.x | 모듈 간 통신 |
| **ML 라이브러리** | TensorFlow / PyTorch | - | 예측 모델 (Python 서빙) |
| **프로토콜** | TL1 | - | 광학 장비 제어 |
| **프로토콜** | NETCONF | - | 장비 설정 관리 |

#### 다중 모듈 구조

```
pom.xml (부모)
├── engine-ticket-mba (포트 6805)
│   ├── 알람/티켓 생성 및 관리
│   └── 포트 상태 추적
├── linkage-mba (포트 6803)
│   ├── TL1 클라이언트 구현
│   └── ROADM 제어 및 설정
├── prepro-mba (포트 6804)
│   ├── 광학 파라미터 수집
│   └── 데이터 정규화
├── predictiveMaintenance-domain
│   ├── ML 모델 통합
│   └── 예측 로직 구현
├── core
│   └── 공유 라이브러리
└── code
    └── 코드 생성 도구
```

#### 주요 특징

- **Spring Boot 3.5.8**: 최신 Spring 기능 및 성능 향상
- **Java 17**: 최신 자바 언어 기능 활용
- **다중 마이크로서비스**: 포트별로 독립적인 서비스 구성
- **ML 통합**: Python 기반 머신러닝 모델 서빙

#### 빌드 및 실행

```bash
# 전체 빌드
mvn clean package

# 개별 모듈 실행
java -jar engine-ticket-mba/target/*.jar --server.port=6805
java -jar linkage-mba/target/*.jar --server.port=6803
java -jar prepro-mba/target/*.jar --server.port=6804
```

### 3. Engine_nia_ip_sdn_oproute (IP-SDN 라우팅 관리)

#### 백엔드 (Spring Boot)

| 영역 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **프레임워크** | Spring Boot | 2.5.3 | SDN 제어 API |
| **프로그래밍 언어** | Java | 11 | 메인 로직 |
| **빌드 도구** | Maven | 3.6+ | 의존성 관리 |
| **데이터베이스** | PostgreSQL | 12+ | 라우팅 정책 저장 |
| **SSH 라이브러리** | JSch | - | 장비 원격 제어 |
| **NETCONF** | NETCONF Library | - | 표준 프로토콜 지원 |
| **로깅** | SLF4J + Logback | - | 구조화 로깅 |

#### 프론트엔드 (Vue 3)

| 영역 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **프레임워크** | Vue | 3.2+ | UI 프레임워크 |
| **UI 라이브러리** | Bootstrap Vue 3 | 3.x | 컴포넌트 라이브러리 |
| **빌드 도구** | Webpack | 5.x | 모듈 번들링 |
| **자산 관리** | Vue CLI | 5.x | 프로젝트 템플릿 및 빌드 |
| **상태 관리** | Pinia / Vuex | 4.x | 전역 상태 관리 |

#### 빌드 및 배포

```bash
# 프론트엔드 빌드
cd frontend
npm install
npm run build
# 결과: dist/ 디렉토리 생성

# 백엔드 빌드
cd backend
mvn clean package

# 실행
java -jar backend/target/nia-oproute-1.0.0.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/nia_oproute \
  --spring.datasource.username=nia_user
```

#### 네트워크 프로토콜 지원

- **OpenFlow**: 표준 SDN 프로토콜
- **NETCONF**: 네트워크 설정 프로토콜
- **SSH/RSA**: 원격 장비 제어
- **SNMP**: 장비 모니터링

### 4. web_2024 (2024 웹 대시보드 플랫폼)

#### 프론트엔드 (Vue 2)

| 영역 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **프레임워크** | Vue | 2.6.11 | UI 프레임워크 |
| **UI 라이브러리** | Element UI | 2.13+ | 대시보드 컴포넌트 |
| **UI 라이브러리** | Quasar | 1.15+ | 반응형 디자인 |
| **데이터 테이블** | ag-Grid | 28.2+ | 고성능 데이터 그리드 |
| **차트 라이브러리** | ECharts | 5.5+ | 실시간 데이터 시각화 |
| **그래프 라이브러리** | Cytoscape | - | 네트워크 토폴로지 맵 |
| **빌드 도구** | Webpack | 5.x | 모듈 번들링 |
| **자산 관리** | Vue CLI | 4.x | 개발 환경 |

#### 백엔드 (Spring Boot)

| 영역 | 기술 | 버전 | 용도 |
|------|------|------|------|
| **프레임워크** | Spring Boot | 2.7.17 | REST API 서버 |
| **프로그래밍 언어** | Java | 8 | 메인 비즈니스 로직 |
| **빌드 도구** | Gradle | 7.x+ | 의존성 관리 |
| **데이터베이스** | PostgreSQL | 12+ | 데이터 저장소 |
| **메시징** | Spring AMQP | 2.x | RabbitMQ 통합 |
| **WebSocket** | Spring WebSocket | - | 실시간 데이터 푸시 |
| **보안** | Spring Security | 2.7.x | 인증/인가 |

#### Gradle 다중 모듈 구조

| 모듈 | 포트 | 역할 |
|------|------|------|
| app-nia | 8080 | 주 NIA 애플리케이션 |
| app-ipms | 8081 | IP 관리 시스템(KT용) |
| app-dataHub | 8082 | 데이터 허브/통합 |
| app-demo | 8083 | 데모 애플리케이션 |
| common-base | - | 기본 유틸리티 |
| common-web | - | 웹 공통 기능 |
| common-mq | - | 메시징 공통 |
| common-ws | - | WebSocket 공통 |

#### 빌드 및 배포

```bash
# 프론트엔드 빌드
cd frontend
npm install
npm run build
# 결과: dist/ 디렉토리

# 백엔드 빌드 (프론트엔드 포함)
cd backend
gradle clean build
# 결과: build/libs/web-2024-1.0.0.jar

# 실행
java -jar build/libs/web-2024-1.0.0.jar \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/nia \
  --spring.datasource.username=nia_user \
  --server.port=8080
```

#### 주요 기능별 기술

| 기능 | 기술 | 상세 |
|------|------|------|
| **대시보드** | ECharts + ag-Grid | 실시간 성능 메트릭 시각화 |
| **네트워크 맵** | Cytoscape.js | 토폴로지 및 관계도 표시 |
| **알람 관리** | Element UI 테이블 | 알람 조회 및 처리 UI |
| **데이터 분석** | ECharts 고급 차트 | 시계열, 분포, 비교 차트 |
| **실시간 업데이트** | WebSocket | 대시보드 실시간 갱신 |
| **권한 관리** | Spring Security | 역할 기반 접근 제어(RBAC) |

## 개발 환경 요구사항

### 필수 소프트웨어

```
Java Development Kit (JDK)
  - nia-server-2021: JDK 8 또는 11
  - nia_optical_pm: JDK 17
  - Engine_nia_ip_sdn_oproute: JDK 11
  - web_2024: JDK 8

Maven 3.6 이상 (또는 Gradle 7.x)

Node.js 14 이상 (프론트엔드 개발용)
  - npm 또는 yarn 패키지 관리자

PostgreSQL 12 이상
  - psycopg2 (Python) 또는 JDBC 드라이버

RabbitMQ 3.8 이상
  - AMQP 포트: 5672
  - 관리 UI: 15672

Git (버전 관리)

Docker & Docker Compose (선택적, 로컬 테스트 환경)

IDE (IntelliJ IDEA, Eclipse, Visual Studio Code)
```

### 개발 머신 사양

```
최소 사양:
- CPU: 4코어 이상
- RAM: 8GB 이상
- 디스크: 20GB 이상 (SSD 권장)

권장 사양:
- CPU: 8코어 이상
- RAM: 16GB 이상
- 디스크: 50GB 이상 (SSD)
```

## 빌드 명령어

### nia-server-2021

```bash
# 클린 빌드
mvn clean package

# 특정 모듈만 빌드
mvn clean package -pl :nia-engine-application

# 테스트 스킵
mvn clean package -DskipTests

# 프로파일 지정 빌드
mvn clean package -P prod

# Docker 이미지 빌드 (Dockerfile 존재 시)
docker build -t nia-engine:latest .
```

### nia_optical_pm

```bash
# 전체 다중 모듈 빌드
mvn clean package

# 개별 모듈 빌드
mvn clean package -pl :engine-ticket-mba
mvn clean package -pl :linkage-mba
mvn clean package -pl :prepro-mba

# 특정 모듈의 테스트만 실행
mvn test -pl :predictiveMaintenance-domain
```

### Engine_nia_ip_sdn_oproute

```bash
# 백엔드 빌드
mvn -f backend/pom.xml clean package

# 프론트엔드 빌드
cd frontend && npm install && npm run build

# 통합 빌드
mvn clean package && cd frontend && npm run build
```

### web_2024

```bash
# 프론트엔드 빌드
cd frontend
npm install
npm run build

# 백엔드 빌드 (Gradle)
cd backend
gradle clean build

# 통합 빌드 (프론트엔드 dist 포함)
gradle clean build --project-dir=backend
```

## 배포 구성

### Docker 배포

```dockerfile
# nia-server-2021 Dockerfile 예시
FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/nia-engine-application-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 6800
```

### Docker Compose (로컬 개발 환경)

```yaml
version: '3.8'
services:
  postgresql:
    image: postgres:14
    environment:
      POSTGRES_DB: nia
      POSTGRES_USER: nia_user
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"

  rabbitmq:
    image: rabbitmq:3.11-management
    ports:
      - "5672:5672"
      - "15672:15672"

  nia-engine:
    build: ./nia-server-2021
    depends_on:
      - postgresql
      - rabbitmq
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgresql:5432/nia
      SPRING_RABBITMQ_HOST: rabbitmq
    ports:
      - "6800:6800"
```

### Kubernetes 배포

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nia-engine
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nia-engine
  template:
    metadata:
      labels:
        app: nia-engine
    spec:
      containers:
      - name: nia-engine
        image: nia-engine:1.0.0
        ports:
        - containerPort: 6800
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://postgresql:5432/nia
        - name: SPRING_RABBITMQ_HOST
          value: rabbitmq
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 6800
          initialDelaySeconds: 30
          periodSeconds: 10
```

## 모니터링 및 로깅

### 로깅 설정

```yaml
# application.yml 로깅 설정
logging:
  level:
    root: INFO
    com.nia: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/application.log
    max-size: 10MB
    max-history: 30
```

### 모니터링 포인트

```
Spring Boot Actuator
  - /actuator/health: 헬스 체크
  - /actuator/metrics: 성능 메트릭
  - /actuator/prometheus: Prometheus 메트릭 수출

RabbitMQ 관리 UI
  - http://localhost:15672 (기본 계정: guest/guest)

PostgreSQL 모니터링
  - pg_stat_statements 활성화로 쿼리 성능 추적
  - 자동 분석(ANALYZE) 및 진공 청소(VACUUM)
```

## 성능 고려사항

### 데이터베이스 최적화

- **인덱싱 전략**: 자주 쿼리되는 컬럼에 인덱스 생성
- **파티셔닝**: 대규모 네트워크 데이터는 시간 기반 파티셔닝
- **연결 풀링**: HikariCP를 통한 최적화된 연결 풀 관리

### 메시징 최적화

- **RabbitMQ 클러스터**: 고가용성을 위한 클러스터 구성
- **메시지 TTL**: 오래된 메시지 자동 삭제로 메모리 효율화
- **Dead Letter Queue**: 처리 실패한 메시지 별도 처리

### 캐싱 전략

- **Redis 캐시**: 자주 조회하는 데이터 캐싱
- **로컬 캐시**: 변경이 적은 데이터는 메모리 캐시 활용

## 버전 관리

- **Spring Boot**: 버전별 호환성 관리 (2.3.x, 2.5.x, 2.7.x, 3.5.x)
- **Java**: LTS 버전 중심 (8, 11, 17)
- **Node.js**: LTS 버전 유지 (14, 16, 18)
- **PostgreSQL**: 안정적인 버전 유지 (12, 14, 15)

## 보안 모범 사례

### 데이터 보호

- Jasypt를 통한 민감한 설정값 암호화
- PostgreSQL SSL 연결 활성화
- RabbitMQ AMQPS(암호화된 AMQP) 사용

### 코드 보안

- Spring Security를 통한 인증/인가
- OWASP Top 10 방지 (SQL Injection, XSS, CSRF 등)
- 정기적인 의존성 취약점 검사 (Dependabot)

### 운영 보안

- 컨테이너 이미지 스캔 및 취약점 점검
- Kubernetes RBAC 정책 적용
- 로그 및 감시 통해 보안 이벤트 추적
