# NIA 프로젝트 아키텍처 코드맵 (Codemaps)

## 개요

이 디렉토리는 NIA(Network Intelligence and Analytics) 프로젝트의 종합적인 아키텍처 설명서를 포함합니다. 개발자, 아키텍트, 운영팀이 시스템 구조를 이해하고 새로운 기능을 개발하는 데 필요한 모든 정보를 제공합니다.

## 문서 구조

### 1. overview.md - 시스템 개요 (필독)
**대상 독자**: 전체 팀

시스템의 높은 수준의 아키텍처를 설명합니다:
- 4개 서브프로젝트 소개
- 핵심 설계 패턴 (마이크로서비스, 이벤트 기반)
- 기술 스택 요약
- 주요 아키텍처 결정사항

**읽을 시간**: 10-15분
**시작점**: 새로운 팀원 온보딩

---

### 2. modules.md - 모듈 및 서비스 카탈로그
**대상 독자**: 개발자, 아키텍트

모든 마이크로서비스의 상세 목록:
- 24개 마이크로서비스 (Engine_nia_server_2021)
- 3개 Engine_nia_optical_pm 모듈
- 4개 Front_web_2024 애플리케이션 및 공용 라이브러리
- 각 서비스: 책임, 공개 인터페이스, 의존성

**읽을 시간**: 20-30분
**용도**: 특정 서비스 찾기, 기능 담당 서비스 파악

---

### 3. dependencies.md - 외부 및 내부 의존성
**대상 독자**: 개발자, 아키텍트, 운영팀

프로젝트 전체의 의존성 분석:
- Spring Boot, Java 버전 관리
- RabbitMQ, PostgreSQL 설정
- 캐싱, 로깅, 테스트 프레임워크
- 서비스 간 의존성 매트릭스
- 외부 시스템 연동 (SDN, sFlow 등)
- 버전 호환성 및 마이그레이션 계획

**읽을 시간**: 15-20분
**용도**: 의존성 충돌 해결, 버전 업그레이드 계획

---

### 4. entry-points.md - API 및 진입점
**대상 독자**: 개발자, QA 팀

모든 애플리케이션의 REST API와 스케줄된 작업:
- 각 마이크로서비스 포트 번호
- REST API 엔드포인트 상세 (메서드, 경로, 파라미터)
- WebSocket 엔드포인트
- 스케줄된 작업 및 배치 프로세스
- 인증 및 오류 응답 형식
- 빌드 및 실행 명령

**읽을 시간**: 25-35분
**용도**: API 호출, 테스트, 통합 작업

---

### 5. data-flow.md - 데이터 흐름 및 메시징
**대상 독자**: 아키텍트, 고급 개발자

시스템 전체의 데이터 이동:
- RabbitMQ 메시지 큐 토폴로지 (6개 계층)
- HTTP 요청/응답 흐름 예시
- 데이터베이스 저장 및 조회
- WebSocket 실시간 스트림
- 요청 생명주기 (엔드-투-엔드 추적)
- 배치 데이터 처리 (AI 학습)
- 오류 처리 및 Dead Letter Queue
- 성능 최적화 전략

**읽을 시간**: 30-40분
**용도**: 문제 추적, 성능 최적화, 새 기능 설계

---

## 빠른 시작 가이드

### 새로운 팀원 온보딩 (30분)
1. **overview.md** 읽기 (10분)
   → NIA가 무엇인지, 어떻게 동작하는지 이해

2. **modules.md** 빠르게 훑어보기 (10분)
   → 각 서비스의 이름과 역할 파악

3. **entry-points.md**에서 app-nia 찾기 (5분)
   → 포트, 주요 API 확인

4. **data-flow.md** 데이터 흐름 섹션 읽기 (5분)
   → 전체 시스템이 어떻게 연결되는지 이해

---

### 새 기능 개발 (1시간)
1. **overview.md**에서 관련 패턴 파악
2. **modules.md**에서 구현할 서비스 찾기
3. **entry-points.md**에서 API 엔드포인트 설계
4. **data-flow.md**에서 메시징 경로 확인
5. **dependencies.md**에서 필요한 라이브러리 확인

---

### 시스템 문제 디버깅 (30분)
1. **data-flow.md**에서 데이터 경로 추적
2. **entry-points.md**에서 관련 API 호출 확인
3. **modules.md**에서 관련 서비스 파악
4. **dependencies.md**에서 버전 호환성 확인

---

## 주요 개념 이해하기

### 마이크로서비스 아키텍처
NIA는 21개의 독립적인 Spring Boot 애플리케이션으로 구성됩니다.
각 서비스는:
- 별도의 포트에서 실행
- 독립적인 데이터베이스 스키마
- REST API와 RabbitMQ 메시징으로 통신

### 이벤트 기반 아키텍처
데이터는 여러 계층을 거쳐 처리됩니다:
```
수집 → 전처리 → 규칙 엔진 → 연계 → AI 분석 → 시각화
```
각 단계는 RabbitMQ를 통해 느슨한 결합으로 연결됩니다.

### RabbitMQ 메시징
- **6개 계층**: 수집, 처리, 엔진, 연계, 알림, AI
- **우선순위**: Critical > High > Normal > Info
- **Dead Letter Queue**: 처리 실패 메시지 관리
- **TTL**: 각 큐별 메시지 보존 시간 설정

### 데이터 흐름 예시
성능 메트릭이 수집되어 대시보드에 표시되는 전체 흐름:
```
네트워크 장비
  ↓ (UDP/SNMP)
NiaPreprocessorApplication (포트 8002)
  ↓ (RabbitMQ: nia.preprocessor.output)
NiaEngineApplication (포트 8001)
  ↓ (RabbitMQ: nia.engine.decisions)
NiaIpPerfLinkageApplication (포트 8004)
  ↓ (RabbitMQ: nia.ai.input)
NiaAiPerformanceLinkageApplication (포트 8019)
  ↓ (PostgreSQL + WebSocket)
app-nia 웹 포탈 (포트 8080)
  ↓ (Vue.js + ECharts)
사용자 브라우저 대시보드
```

---

## 기술 스택 요약

| 레이어 | 기술 | 버전 | 용도 |
|--------|------|------|------|
| **언어** | Java | 8-17 | 마이크로서비스 |
| **프레임워크** | Spring Boot | 2.7.x, 3.5.x | REST API, 설정 관리 |
| **메시징** | RabbitMQ | 3.x | 비동기 통신 |
| **데이터** | PostgreSQL | 11-14 | 메인 저장소 |
| **ORM** | MyBatis, JPA | 3.x | SQL 매핑 |
| **캐싱** | EhCache | 3.x | 로컬 캐시 |
| **규칙 엔진** | Drools | 7.x | 의사결정 |
| **빌드** | Maven, Gradle | 3.6.x | 패키징 |
| **프론트** | Vue | 2.6.11, 3.2 | UI |
| **UI** | Element UI | 2.x | 컴포넌트 |
| **차트** | ECharts | 5.x | 시각화 |

---

## 자주 묻는 질문 (FAQ)

### Q: 특정 기능은 어느 서비스에서 구현되나요?
**A**: modules.md에서 "책임" 섹션을 읽고, entry-points.md에서 관련 API를 찾아보세요.

### Q: 두 서비스 간의 통신 방식은?
**A**: data-flow.md의 "RabbitMQ 메시지 큐 토폴로지" 섹션을 확인하세요.

### Q: 새 마이크로서비스를 추가하려면?
**A**: overview.md의 "마이크로서비스 아키텍처", modules.md 샘플을 참고하여 설계하세요.

### Q: 데이터베이스 스키마는?
**A**: data-flow.md의 "PostgreSQL 스키마 구조" 섹션을 참고하세요.

### Q: API 포트 번호는?
**A**: entry-points.md에서 각 서비스별 포트를 확인하세요.

### Q: 왜 Vue 2와 Vue 3가 혼재되어 있나요?
**A**: overview.md의 "주요 아키텍처 결정사항" 테이블을 참고하세요. 점진적 마이그레이션 중입니다.

---

## 문서 업데이트 정책

### 언제 업데이트하나?
- 새 마이크로서비스 추가
- 주요 아키텍처 변경
- 큐 구조 변경
- API 엔드포인트 추가/변경
- 의존성 버전 업그레이드

### 업데이트 체크리스트
1. 변경사항 해당 문서에 기록
2. 다른 문서의 크로스 레퍼런스 확인
3. 예시 코드 또는 설정 값 검증
4. 팀에 업데이트 알림

---

## 추가 자료

### 관련 문서
- `.moai/config/` - 프로젝트 설정
- `Engine_nia_server_2021/pom.xml` - Maven 의존성
- `Front_web_2024/BE/build.gradle` - Gradle 빌드 설정
- `docker-compose.yml` - 로컬 개발 환경 설정

### 외부 링크
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [RabbitMQ 튜토리얼](https://www.rabbitmq.com/getstarted.html)
- [PostgreSQL 매뉴얼](https://www.postgresql.org/docs/)
- [Vue.js 가이드](https://vuejs.org/)
- [Drools 문서](https://www.drools.org/)

---

## 기여 가이드

이 문서를 개선하고 싶으신가요?

1. 부정확한 정보를 발견한 경우:
   - 해당 문서의 섹션 확인
   - 수정 사항을 .moai/project/codemaps/ 하위 파일에 반영
   - 팀에 알림

2. 새 섹션 추가:
   - 어느 문서에 추가할지 결정
   - 같은 형식과 스타일 유지
   - 다른 문서의 크로스 레퍼런스 추가

3. 예시 추가:
   - 실제 프로젝트의 코드/설정 참고
   - 정확성 검증
   - 따라 하기 쉽도록 작성

---

**최종 업데이트**: 2024년 1월 15일
**대상 버전**: NIA 2.1.0
**관리자**: 아키텍처 팀
