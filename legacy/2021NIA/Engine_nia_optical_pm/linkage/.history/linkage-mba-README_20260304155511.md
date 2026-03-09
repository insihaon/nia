# linkage-mba 프로젝트 분석 문서

## 📋 프로젝트 개요

**linkage-mba**는 Spring Boot 기반의 네트워크 장비 성능 모니터링 및 데이터 수집 애플리케이션입니다.

- **프로젝트 타입**: Spring Boot 애플리케이션 (Maven)
- **Java 버전**: 17
- **패키징**: JAR
- **메인 클래스**: `kr.go.ap.LinkageMbaApplication`

## 🔌 포트 설정 및 확인 방법

### 포트 설정 위치

서버 포트는 다음 파일에서 설정됩니다:

```yaml
# src/main/resources/application.yml
server:
  port: 6803
```

**기본 포트**: `6803`

### 포트 확인 방법

#### 1. 설정 파일 확인
- `application.yml` 파일의 `server.port` 값 확인

#### 2. 애플리케이션 시작 로그 확인
애플리케이션 실행 시 다음과 같은 로그가 출력됩니다:
```
Tomcat started on port(s): 6803 (http)
```

#### 3. 환경 변수로 오버라이드
실행 시 포트를 변경할 수 있습니다:
```bash
java -jar linkage-mba-1.0-SNAPSHOT.jar --server.port=8080
```

#### 4. 프로파일별 설정
- `application-local.yml`, `application-real.yml`에는 포트 설정이 없음
- 기본 `application.yml`의 6803 포트가 사용됨

## 📁 프로젝트 구조

```
linkage-mba/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── kr/go/ap/
│   │   │       ├── LinkageMbaApplication.java          # 메인 애플리케이션
│   │   │       └── linkage/mba/
│   │   │           ├── amqp/
│   │   │           │   └── MbaAiModelPrdAmqp.java      # RabbitMQ 메시지 발행
│   │   │           ├── config/
│   │   │           │   └── RabbitMqConfig.java         # RabbitMQ 설정
│   │   │           ├── property/
│   │   │           │   └── RabbitMqProperties.java     # RabbitMQ 속성
│   │   │           ├── scheduler/
│   │   │           │   └── LinkageSchdulerService.java # 스케줄러 서비스
│   │   │           ├── service/
│   │   │           │   ├── EmsMmcHdlService.java       # EMS 통신 처리
│   │   │           │   └── ParsingHdlService.java      # TL1 메시지 파싱
│   │   │           └── telnet/
│   │   │               ├── Tl1SocketClient.java        # TL1 소켓 클라이언트
│   │   │               └── Tl1TelnetClient.java        # TL1 텔넷 클라이언트
│   │   └── resources/
│   │       ├── application.yml                         # 기본 설정
│   │       ├── application-local.yml                   # 로컬 프로파일 설정
│   │       ├── application-real.yml                    # 운영 프로파일 설정
│   │       ├── logback-config-spring.xml               # 로깅 설정
│   │       └── mybatis-config.xml                      # MyBatis 설정
│   └── test/                                           # 테스트 코드
└── pom.xml                                             # Maven 설정
```

## 🎯 주요 기능

### 1. 스케줄러 기반 성능 데이터 수집
- **스케줄**: 15분마다 실행 (`@Scheduled(cron = "0 0/15 * * * *")`)
- **기능**: EMS 장비로부터 성능 데이터(PM) 수집
- **구현**: `LinkageSchdulerService` → `EmsMmcHdlService.pmCollectHdl()`

### 2. RabbitMQ 메시지 큐 연동
- **포트**: 6786
- **Exchange**: `nia.exchangeMbaAiModelDirectly`
- **Queue**: `nia.MbaAiModelIndexDirectly`
- **Routing Key**: `MbaAiModelIndexDirectly`
- **용도**: 수집한 성능 데이터를 AI 모델 처리 큐로 전송

### 3. PostgreSQL 데이터베이스 연동
- **포트**: 6544
- **Connection Pool**: HikariCP
- **최대 Pool Size**: 5
- **용도**: 장비 정보 및 성능 데이터 저장

### 4. EMS 통신 (TL1 프로토콜)
- **프로토콜**: TL1 (Transaction Language 1)
- **연결 방식**: Socket 통신
- **포트**: 
  - MMC: 42001
  - EVT: 42002
- **기능**:
  - SIPC 정보 수집 (`RTRV-SIPC` 명령)
  - 성능 데이터 수집 (`RTRV-PM` 명령)

### 5. 설정 암호화
- **라이브러리**: Jasypt
- **용도**: 데이터베이스 및 RabbitMQ 인증 정보 암호화
- **형식**: `ENC(암호화된값)`

## 🔧 빌드 방법

### 사전 요구사항
- Java 17 이상
- Maven 3.6 이상
- 네트워크 접근 가능 (의존성 다운로드)

### 빌드 명령어

#### 1. 기본 빌드 (테스트 포함)
```bash
cd linkage-mba
mvn clean package
```

#### 2. 테스트 제외 빌드
```bash
mvn clean package -DskipTests
```

#### 3. 의존성 다운로드만 수행
```bash
mvn dependency:resolve
```

#### 4. 빌드 결과물 확인
빌드 완료 후 다음 위치에 JAR 파일이 생성됩니다:
```
target/linkage-mba-1.0-SNAPSHOT.jar
```

### 빌드 옵션

#### 프로파일 지정 빌드
특정 프로파일의 리소스를 포함하여 빌드:
```bash
# 로컬 프로파일로 빌드
mvn clean package -P local

# 운영 프로파일로 빌드
mvn clean package -P real
```

#### 빌드 정보 확인
```bash
# 프로젝트 정보 확인
mvn help:effective-pom

# 의존성 트리 확인
mvn dependency:tree
```

### 빌드 문제 해결

#### 의존성 문제
부모 POM의 의존성이 필요한 경우:
```bash
# 부모 프로젝트 먼저 빌드
cd ..
mvn clean install
cd linkage-mba
mvn clean package
```

#### 메모리 부족
Maven 빌드 시 메모리 부족 오류 발생 시:
```bash
export MAVEN_OPTS="-Xmx2048m -Xms1024m"
mvn clean package
```

## 🚀 실행 방법

### 1. JAR 파일 직접 실행

#### 로컬 프로파일로 실행
```bash
java -jar target/linkage-mba-1.0-SNAPSHOT.jar --spring.profiles.active=local
```

#### 운영 프로파일로 실행
```bash
java -jar target/linkage-mba-1.0-SNAPSHOT.jar --spring.profiles.active=real
```

#### 포트 변경하여 실행
```bash
java -jar target/linkage-mba-1.0-SNAPSHOT.jar --spring.profiles.active=local --server.port=8080
```

### 2. Maven을 통한 실행

#### 로컬 프로파일
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

#### 운영 프로파일
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=real
```

### 3. 백그라운드 실행 (Linux)

```bash
nohup java -jar target/linkage-mba-1.0-SNAPSHOT.jar --spring.profiles.active=real > app.log 2>&1 &
```

### 4. 실행 확인

#### 로그 확인
애플리케이션 시작 시 다음 로그를 확인:
```
Started LinkageMbaApplication in X.XXX seconds
Tomcat started on port(s): 6803 (http)
```

#### 포트 확인
```bash
# Windows
netstat -ano | findstr :6803

# Linux
netstat -tulpn | grep :6803
# 또는
lsof -i :6803
```

## ⚙️ 설정 파일 설명

### application.yml
- **서버 포트**: 6803
- **로깅 레벨**: INFO
- **Jasypt 설정**: 암호화된 값 처리

### application-local.yml / application-real.yml
- **데이터베이스 설정**: PostgreSQL 연결 정보
- **RabbitMQ 설정**: 메시지 큐 연결 정보
- **EMS 설정**: TL1 통신을 위한 장비 정보

### 주요 설정 항목

| 설정 항목 | 설명 | 기본값 |
|---------|------|--------|
| `server.port` | HTTP 서버 포트 | 6803 |
| `spring.datasource.jdbcUrl` | PostgreSQL 연결 URL | - |
| `spring.rabbitmq.port` | RabbitMQ 포트 | 6786 |
| `spring.ems.port_mmc` | EMS MMC 포트 | 42001 |
| `spring.ems.port_evt` | EMS EVT 포트 | 42002 |

## 📦 주요 의존성

- **Spring Boot**: 애플리케이션 프레임워크
- **Spring Integration AMQP**: RabbitMQ 통합
- **PostgreSQL Driver**: 데이터베이스 연결
- **Jasypt**: 설정 암호화
- **Commons Net**: 네트워크 통신 (TL1)
- **MyBatis**: SQL 매퍼
- **Lombok**: 보일러플레이트 코드 제거

## 🔍 모니터링 및 로깅

### 로그 설정
- **설정 파일**: `logback-config-spring.xml`
- **프로파일별 로그 설정**: `log-properties/logback-*.properties`

### 스케줄러 동작 확인
- 15분마다 `EmsMmcHdlService.pmCollectHdl()` 메서드 실행
- 로그에서 "pmCollectHdl", "sipcMmc", "pmMmc" 키워드로 동작 확인 가능

## 📝 참고사항

1. **암호화된 설정값**: 데이터베이스 및 RabbitMQ 인증 정보는 Jasypt로 암호화되어 있음
2. **TL1 통신**: EMS 장비와의 통신은 TL1 프로토콜을 사용하며, Socket 기반 통신
3. **스케줄러**: `@EnableScheduling` 어노테이션으로 스케줄러 기능 활성화
4. **프로파일**: `local`과 `real` 프로파일로 환경별 설정 분리

## 🐛 문제 해결

### 포트 충돌
다른 애플리케이션이 6803 포트를 사용 중인 경우:
```bash
# 포트 사용 중인 프로세스 확인
netstat -ano | findstr :6803

# application.yml에서 포트 변경 또는 실행 시 오버라이드
--server.port=6804
```

### RabbitMQ 연결 실패
- RabbitMQ 서버(116.89.191.47:6786) 접근 가능 여부 확인
- 인증 정보 확인 (암호화된 값이 올바른지 확인)

### 데이터베이스 연결 실패
- PostgreSQL 서버(116.89.191.47:6544) 접근 가능 여부 확인
- 인증 정보 확인

---

**작성일**: 2024년  
**프로젝트 버전**: 1.0-SNAPSHOT
