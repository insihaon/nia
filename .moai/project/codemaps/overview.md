# NIA 프로젝트 아키텍처 개요

## 시스템 개요

NIA (Network Intelligence and Analytics)는 분산형 마이크로서비스 기반의 네트워크 모니터링 및 분석 플랫폼입니다. Spring Boot 기반 21개의 Java 마이크로서비스와 Vue 기반의 현대적 프론트엔드로 구성되어 있습니다.

## 4개 서브프로젝트 구성

### 1. Engine_nia_server_2021
**Java 8-11, Spring Boot, Maven, RabbitMQ**

21개의 마이크로서비스로 구성된 핵심 엔진. 규칙 기반 이벤트 처리, 성능 모니터링, 트래픽 분석, 장애 감지 및 원인 분석(RCA)을 담당합니다.

주요 서비스 분류:
- **코어 엔진**: NiaEngineApplication (규칙 엔진), NiaPreprocessorApplication (전처리)
- **성능 모니터링**: NiaPerformanceApplication, NiaIpPerfLinkageApplication
- **트래픽 분석**: NiaTrafficPreprocessorApplication, NiaIpSflowLinkageApplication
- **IP 네트워크**: IpEquipLinkage, IpCountryCodeLinkage, IpAlarmLinkage
- **SDN 통합**: IpSdnLinkage, IpSdnSflow, IpSdnSyslog, IpSdnSyslogPreprocessor
- **AI/RCA**: AiPerformanceLinkage, AiResultLinkage, RcaToAiDataLinkage
- **인프라**: EmsLinkage, Linkage, Cluster, SysCheck

통신: RabbitMQ (AMQP) 큐 기반 비동기 메시징
데이터: PostgreSQL + MyBatis/JPA

### 2. Engine_nia_optical_pm
**Spring Boot 3.5.8, Java 17, Maven Multi-module**

광 네트워크 성능 모니터링 및 예측 유지보수. 3개의 마이크로서비스:
- **engine-ticket-mba** (포트 6805): 티켓 생성 및 관리 엔진
- **linkage-mba** (포트 6803): 광 네트워크 데이터 연계
- **prepro-mba** (포트 6804): 성능 데이터 전처리

구성: predictiveMaintenance-domain, core, code 모듈

### 3. Engine_nia_ip_sdn_oproute
**Spring Boot 2.5.3 + Vue 3.2**

SDN 통합 IP 라우팅 최적화. 백엔드와 프론트엔드 분리:
- **probe** (백엔드): 네트워크 프로브 데이터 수집 및 분석
- **ui/manager** (프론트엔드): 관리 대시보드

### 4. Front_web_2024
**Spring Boot 2.7.17 + Vue 2.6.11, Gradle**

통합 웹 포털 및 데이터 시각화. 4개의 Spring Boot 앱과 Vue SPA:
- **app-nia**: 핵심 NIA 모니터링 포털
- **app-ipms**: IP 성능 관리 시스템
- **app-dataHub**: 데이터 허브 및 분석
- **app-demo**: 데모 및 테스트

공용 라이브러리: common-base, common-web, common-mq, common-ws

프론트엔드: Vue 2 SPA (Element UI, ECharts, ag-Grid)

## 핵심 설계 패턴

### 마이크로서비스 아키텍처
각 서비스는 독립적인 데이터베이스를 소유하고, RabbitMQ 큐를 통해 비동기로 통신합니다.

### 이벤트 기반 아키텍처
원시 데이터 → 전처리(Preprocessor) → 규칙 엔진(Engine) → 연계 처리(Linkage) → AI 분석 → 시각화

### MVC 패턴
- **모델**: JPA/MyBatis ORM 엔티티
- **뷰**: Vue 2/3 SPA 컴포넌트
- **컨트롤러**: Spring MVC REST 엔드포인트

## 기술 스택

### 백엔드
- **프레임워크**: Spring Boot 2.7.17, 3.5.8
- **언어**: Java 8-17
- **빌드**: Maven, Gradle
- **메시징**: RabbitMQ (AMQP)
- **데이터**: PostgreSQL, MyBatis, JPA/Hibernate
- **캐싱**: EhCache, Redis
- **룰 엔진**: Drools

### 프론트엔드
- **프레임워크**: Vue 2.6.11, Vue 3.2
- **번들러**: Webpack
- **UI 라이브러리**: Element UI, ag-Grid
- **차트**: ECharts, C3.js
- **통신**: Axios

## 시스템 경계 및 통합 지점

### 데이터 흐름
1. **수집**: 네트워크 디바이스 → 프로브 수집
2. **전처리**: Preprocessor 서비스에서 데이터 정제
3. **엔진 처리**: Engine에서 규칙 기반 분석
4. **연계**: Linkage 서비스에서 다중 소스 통합
5. **AI 분석**: AI 서비스에서 고급 분석
6. **시각화**: 웹 포털에서 대시보드 표시

### 서비스 간 통신
- **동기**: REST API (HTTP)
- **비동기**: RabbitMQ 메시지 큐
- **캐싱**: 분산 캐시 (Redis/EhCache)

## 개발 모드

### Spring Boot 마이크로서비스
```
Docker 또는 로컬 환경에서 독립 실행
application.yml로 RabbitMQ, DB 연결 설정
포트: 각 서비스별 고정 포트 (예: nia-engine: 8001)
```

### 웹 프론트엔드 (Front_web_2024)
```
npm install 후 npm run dev로 개발 서버 실행
Gradle build로 Spring Boot 애플리케이션 빌드
Docker Compose로 멀티 서비스 오케스트레이션
```

## 성능 특성

- **처리량**: RabbitMQ 큐 기반으로 초당 수천 이벤트 처리
- **지연**: 이벤트 처리 전 단계에서 밀리초 수준
- **확장성**: 마이크로서비스 수평 확장 지원
- **가용성**: 클러스터 구성으로 고가용성 확보

## 주요 아키텍처 결정사항

| 결정 | 근거 | 영향 |
|------|------|------|
| 마이크로서비스 | 독립적 배포 및 확장 | 운영 복잡도 증가 |
| RabbitMQ | 비동기 느슨한 결합 | 최종 일관성 보장 필요 |
| PostgreSQL | ACID 준수, 복잡 쿼리 | 쓰기 확장성 제약 |
| Vue 2/3 혼합 | 점진적 마이그레이션 | 번들 크기 증가 |
| Spring Boot | 성숙도, 생태계 | 학습곡선 |
