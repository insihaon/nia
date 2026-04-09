# NIA 프로젝트 구조

## 디렉토리 트리 개요

```
nia_source/
├── Engine_nia_server_2021/            # 핵심 엔진 플랫폼
│   ├── src/                            # 소스 코드
│   │   ├── NiaEngineApplication/       # 규칙 엔진 (메인)
│   │   ├── NiaPreprocessorApplication/ # 데이터 전처리
│   │   ├── NiaPerformanceApplication/  # 성능 분석
│   │   ├── NiaTrafficPreprocessorApplication/ # 트래픽 전처리
│   │   ├── NiaPingPreprocessorApplication/ # Ping 데이터 전처리
│   │   ├── NiaAlarmSimulatorApplication/ # 알람 시뮬레이터
│   │   ├── NiaIpAlarmLinkageApplication/ # IP 알람 연계
│   │   ├── NiaIpEquipLinkageApplication/ # IP 장비 연계
│   │   ├── NiaIpPerfLinkageApplication/ # IP 성능 연계
│   │   ├── NiaIpPreprocessorApplication/ # IP 전처리
│   │   ├── NiaIpSdnLinkageApplication/ # SDN 연계
│   │   ├── NiaIpSdnSflowLinkageApplication/ # sFlow 연계
│   │   ├── NiaIpSdnSyslogLinkageApplication/ # Syslog 연계
│   │   ├── NiaIpSdnSyslogPreprocessorApplication/ # Syslog 전처리
│   │   ├── NiaIpSdnToAiDataLinkageApplication/ # AI 데이터 연계
│   │   ├── NiaAiPerformanceLinkageApplication/ # AI 성능 분석
│   │   ├── NiaAiResultLinkageApplication/ # AI 결과 연계
│   │   ├── NiaRcaToAiDataLinkageApplication/ # RCA-AI 데이터
│   │   ├── NiaEmsLinkageApplication/ # EMS 연계
│   │   ├── NiaLinkageApplication/     # 일반 연계
│   │   └── NiaClusterApplication/     # 클러스터 조율
│   ├── pom.xml                         # Maven POM 파일
│   └── README.md                       # 프로젝트 설명

├── Engine_nia_optical_pm/             # 광학 네트워크 예측 유지보수
│   ├── engine-ticket-mba/              # 티켓 관리 엔진 (포트 6805)
│   │   ├── src/
│   │   └── pom.xml
│   ├── linkage-mba/                    # TL1/ROADM 연계 (포트 6803)
│   │   ├── src/
│   │   └── pom.xml
│   ├── prepro-mba/                     # 전처리 엔진 (포트 6804)
│   │   ├── src/
│   │   └── pom.xml
│   ├── predictiveMaintenance-domain/   # 예측 유지보수 도메인 모델
│   │   ├── src/
│   │   └── pom.xml
│   ├── core/                           # 핵심 라이브러리
│   │   ├── src/
│   │   └── pom.xml
│   ├── code/                           # 코드 생성 도구
│   │   ├── src/
│   │   └── pom.xml
│   ├── pom.xml                         # 부모 POM (다중 모듈)
│   └── README.md

├── Engine_nia_ip_sdn_oproute/         # IP-SDN 라우팅 관리
│   ├── backend/                        # Spring Boot 백엔드
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/               # Java 소스 코드
│   │   │   │   └── resources/          # 설정, 스키마 등
│   │   │   └── test/
│   │   ├── pom.xml
│   │   └── Dockerfile
│   ├── frontend/                       # Vue 3 프론트엔드
│   │   ├── src/
│   │   │   ├── components/             # Vue 컴포넌트
│   │   │   ├── views/                  # 페이지 뷰
│   │   │   ├── services/               # API 서비스
│   │   │   └── assets/                 # 이미지, 스타일 등
│   │   ├── package.json
│   │   └── webpack.config.js
│   ├── docs/                           # 프로젝트 문서
│   └── README.md

├── Front_web_2024/                    # 2024 웹 대시보드 플랫폼
│   ├── frontend/                       # Vue 2 프론트엔드
│   │   ├── src/
│   │   │   ├── components/
│   │   │   ├── views/
│   │   │   ├── services/
│   │   │   ├── store/
│   │   │   ├── utils/
│   │   │   └── assets/
│   │   ├── dist/                       # 빌드 출력 (static 리소스)
│   │   ├── package.json
│   │   └── webpack.config.js
│   ├── backend/                        # Spring Boot 백엔드
│   │   ├── app-nia/                    # 주 애플리케이션
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── app-ipms/                   # IP 관리 서비스
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── app-dataHub/                # 데이터 허브
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── app-demo/                   # 데모 애플리케이션
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── common-base/                # 기본 라이브러리
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── common-web/                 # 웹 공통 기능
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── common-mq/                  # 메시지 큐 공통
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── common-ws/                  # WebSocket 공통
│   │   │   ├── src/
│   │   │   └── build.gradle
│   │   ├── build.gradle
│   │   └── settings.gradle
│   ├── build/                          # Gradle 빌드 디렉토리
│   ├── dist/                           # 최종 배포 JAR (FE dist + BE)
│   ├── docs/                           # 프로젝트 문서
│   ├── build.gradle
│   ├── settings.gradle
│   └── README.md

└── .moai/                              # MoAI 설정 및 문서
    ├── project/
    │   ├── product.md                  # 제품 개요
    │   ├── structure.md                # 구조 설명 (이 파일)
    │   └── tech.md                     # 기술 스택
    ├── specs/                          # SPEC 문서
    ├── docs/                           # 생성된 문서
    └── config/                         # 설정 파일
```

## 서브프로젝트별 구조

### 1. nia-server-2021 (핵심 엔진)

#### 목적
21개의 독립적인 마이크로서비스를 통해 네트워크 데이터의 수집, 전처리, 분석 및 알람 생성을 담당합니다.

#### 핵심 모듈 구조

**데이터 수집 및 전처리 계층**
- NiaPreprocessorApplication: 일반 데이터 정규화 및 전처리
- NiaIpPreprocessorApplication: IP 네트워크 데이터 전처리
- NiaTrafficPreprocessorApplication: 트래픽 데이터 전처리
- NiaPingPreprocessorApplication: Ping 응답 시간 데이터 전처리
- NiaIpSdnSyslogPreprocessorApplication: Syslog 메시지 파싱 및 정규화

**분석 엔진 계층**
- NiaEngineApplication: 규칙 기반 핵심 처리 엔진 (중앙 허브)
- NiaPerformanceApplication: 성능 메트릭 분석 엔진
- NiaAiPerformanceLinkageApplication: AI 성능 분석 연계

**알람 처리 계층**
- NiaAlarmSimulatorApplication: 테스트용 알람 시뮬레이터
- NiaIpAlarmLinkageApplication: IP 알람 정책 및 자동 연계

**외부 시스템 연계 계층**
- NiaIpEquipLinkageApplication: IP 장비 제어 및 설정 동기화
- NiaIpPerfLinkageApplication: IP 성능 데이터 수집 및 연계
- NiaEmsLinkageApplication: EMS 시스템 통합
- NiaLinkageApplication: 일반 외부 시스템 연계

**SDN/AI 데이터 연계 계층**
- NiaIpSdnLinkageApplication: OpenFlow/NETCONF 기반 SDN 제어
- NiaIpSdnSflowLinkageApplication: sFlow 수집기와 연계
- NiaIpSdnSyslogLinkageApplication: 네트워크 장비 Syslog 수집
- NiaIpSdnToAiDataLinkageApplication: AI 엔진용 데이터 변환
- NiaAiResultLinkageApplication: AI 분석 결과 수신 및 처리
- NiaRcaToAiDataLinkageApplication: RCA 데이터를 AI로 전송

**클러스터/조율 계층**
- NiaClusterApplication: 다중 노드 간 조율 및 동기화
- NiaLinkageApplication: 서비스 간 통신 및 워크플로우 조율

#### 주요 파일 위치

- **메인 애플리케이션**: `src/NiaEngineApplication/` (포트 6800)
- **전처리 서비스**: `src/NiaPreprocessorApplication/` (포트 6801)
- **설정 파일**: 각 애플리케이션의 `application.yml`
- **데이터베이스 스키마**: `src/main/resources/db/`
- **테스트**: `src/test/java/`
- **빌드**: `pom.xml` (Maven 다중 모듈)

#### 빌드 및 배포

```
# 전체 빌드
mvn clean package

# 특정 모듈 빌드
mvn clean package -pl :nia-engine-application

# 실행 (예: 엔진)
java -jar nia-engine-application/target/nia-engine-application-1.0.0.jar
```

### 2. nia_optical_pm (광학 네트워크 예측 유지보수)

#### 목적
광학 네트워크 장비의 성능을 모니터링하고 머신러닝 기반의 예측 유지보수를 제공합니다.

#### 모듈 구조

**엔진-티켓 관리 (engine-ticket-mba, 포트 6805)**
- 광학 네트워크의 알람 및 티켓 생성/관리
- 포트 상태 및 성능 모니터링

**연계-ROADM 제어 (linkage-mba, 포트 6803)**
- TL1 프로토콜 기반 광학 장비(ROADM) 제어
- 광학 파라미터 조회 및 설정

**전처리 엔진 (prepro-mba, 포트 6804)**
- 광학 성능 데이터 수집 및 정규화
- 광학 파라미터 전처리

**도메인 모델 (predictiveMaintenance-domain)**
- 예측 유지보수 비즈니스 로직
- 머신러닝 모델 통합

**핵심 라이브러리 (core)**
- 공유 유틸리티 및 기본 기능

#### 주요 파일 위치

- **포트 6805**: `engine-ticket-mba/src/main/java/`
- **포트 6803**: `linkage-mba/src/main/java/` (TL1 클라이언트)
- **포트 6804**: `prepro-mba/src/main/java/`
- **설정**: 각 모듈의 `src/main/resources/application.yml`
- **데이터베이스**: `predictiveMaintenance-domain/src/main/resources/db/`

#### 빌드 및 배포

```
# 전체 빌드
mvn clean package

# 개별 모듈 빌드
mvn clean package -pl :engine-ticket-mba
mvn clean package -pl :linkage-mba
mvn clean package -pl :prepro-mba

# 실행
java -jar engine-ticket-mba/target/engine-ticket-mba-1.0.0.jar
java -jar linkage-mba/target/linkage-mba-1.0.0.jar
java -jar prepro-mba/target/prepro-mba-1.0.0.jar
```

### 3. NIA IP-SDN oproute (IP-SDN 라우팅 관리)

#### 목적
SDN 기반의 IP 네트워크 라우팅 정책 관리 및 최적화를 담당합니다.

#### 구조

**백엔드 (Spring Boot 2.5.3)**
- `backend/src/main/java/`: Spring Boot 애플리케이션
- 포트: 8080 (기본값)
- 기능:
  - IP 라우팅 정책 API
  - NETCONF/SSH 기반 장비 제어
  - 토폴로지 관리
  - 트래픽 엔지니어링 정책

**프론트엔드 (Vue 3.2)**
- `frontend/src/`: Vue 3 애플리케이션
- 기능:
  - 라우팅 정책 시각화
  - 네트워크 토폴로지 맵
  - 성능 메트릭 대시보드
  - 정책 관리 UI

#### 주요 파일 위치

- **백엔드 소스**: `backend/src/main/java/`
- **프론트엔드 소스**: `frontend/src/`
- **백엔드 설정**: `backend/src/main/resources/application.yml`
- **프론트엔드 빌드**: `frontend/dist/`
- **데이터베이스**: PostgreSQL (설정: `backend/src/main/resources/application.yml`)

#### 빌드 및 배포

```
# 백엔드 빌드
cd backend
mvn clean package

# 프론트엔드 빌드
cd frontend
npm install
npm run build

# 통합 실행
java -jar backend/target/nia-oproute-1.0.0.jar
# 프론트엔드는 build/dist에서 static 파일로 제공됨
```

### 4. web_2024 (2024 웹 대시보드 플랫폼)

#### 목적
NIA 엔진의 모든 데이터를 시각화하고 조작하는 통합 웹 인터페이스를 제공합니다.

#### 구조

**프론트엔드 (Vue 2.6.11)**
- `frontend/src/`: Vue 2 애플리케이션
- 주요 기능:
  - 네트워크 성능 대시보드
  - 트래픽 분석 및 시각화
  - 알람 관리 및 처리 UI
  - 네트워크 토폴로지 맵
  - 다양한 분석 리포트

**백엔드 모듈 구조 (Gradle 다중 모듈)**

| 모듈 | 포트 | 목적 |
|------|------|------|
| app-nia | 8080 | 주 NIA 애플리케이션 |
| app-ipms | 8081 | IP 관리 서비스(KT 전용) |
| app-dataHub | 8082 | 데이터 허브/통합 |
| app-demo | 8083 | 데모 애플리케이션 |
| common-base | - | 기본 유틸리티 라이브러리 |
| common-web | - | 웹 공통 기능 라이브러리 |
| common-mq | - | 메시지 큐 공통 라이브러리 |
| common-ws | - | WebSocket 공통 라이브러리 |

#### 주요 파일 위치

- **프론트엔드**: `frontend/src/`
- **프론트엔드 빌드 결과**: `frontend/dist/`
- **백엔드 메인**: `backend/app-nia/src/main/java/`
- **백엔드 설정**: `backend/app-nia/src/main/resources/application.yml`
- **공통 라이브러리**: `backend/common-*/src/main/java/`
- **데이터베이스 스키마**: `backend/app-nia/src/main/resources/db/`

#### 빌드 및 배포

```
# 프론트엔드 빌드
cd frontend
npm install
npm run build

# 백엔드 빌드 (프론트엔드 dist를 static 리소스로 포함)
cd backend
gradle clean build

# 실행
java -jar backend/build/libs/web-2024-1.0.0.jar
```

#### 빌드 산결물

- **최종 JAR**: `dist/web-2024-1.0.0.jar`
- **내부 구조**: JAR 내부에 프론트엔드의 `dist/`가 `static/` 리소스로 포함
- **접근 경로**: `http://localhost:8080/` (정적 리소스) 및 API 엔드포인트

## 주요 진입점(Entry Point)

### nia-server-2021
- **메인 엔진**: `com.nia.engine.NiaEngineApplication`
- **전처리**: `com.nia.preprocessor.NiaPreprocessorApplication`

### nia_optical_pm
- **티켓 엔진**: `com.nia.optical.ticket.TicketMbaApplication` (포트 6805)
- **ROADM 연계**: `com.nia.optical.linkage.RoadmLinkageApplication` (포트 6803)
- **전처리**: `com.nia.optical.prepro.PreprocessorMbaApplication` (포트 6804)

### NIA IP-SDN oproute
- **백엔드**: `com.nia.oproute.OprouteApplication`

### web_2024
- **메인 앱**: `com.nia.web.NiaWebApplication`

## 데이터 흐름

```
네트워크 장비
   ↓
전처리 서비스들 (NiaPreprocessor, NiaIpPreprocessor 등)
   ↓
NiaEngineApplication (중앙 규칙 엔진)
   ↓
분석 서비스들 (NiaPerformance, NiaAiPerformance 등)
   ↓
웹 대시보드 (web_2024) / 외부 시스템 (EMS, SDN)
```

## 도메인별 패키지 구조

### nia-server-2021 내부 패키지 구조 (예시)

```
com.nia
├── engine
│   ├── controller/      # REST API 컨트롤러
│   ├── service/         # 비즈니스 로직
│   ├── dao/             # 데이터 접근 계층
│   ├── entity/          # JPA 엔티티
│   ├── dto/             # 데이터 전송 객체
│   └── config/          # 설정 클래스
├── preprocessor
│   ├── service/         # 전처리 로직
│   ├── parser/          # 데이터 파싱
│   └── transformer/     # 데이터 변환
└── common
    ├── utils/           # 유틸리티 클래스
    ├── constants/       # 상수 정의
    ├── exception/       # 예외 클래스
    └── config/          # 공통 설정
```

## 빌드 도구

- **nia-server-2021**: Maven (pom.xml)
- **nia_optical_pm**: Maven (pom.xml)
- **NIA IP-SDN oproute**: Maven(백엔드) + Webpack(프론트엔드)
- **web_2024**: Gradle(백엔드) + Webpack(프론트엔드)

## 데이터베이스

모든 서브프로젝트는 **PostgreSQL**을 중앙 데이터베이스로 사용합니다:
- **호스트**: localhost (개발) 또는 설정된 DB 서버
- **기본 포트**: 5432
- **드라이버**: PostgreSQL JDBC Driver
- **ORM**: JPA/MyBatis
