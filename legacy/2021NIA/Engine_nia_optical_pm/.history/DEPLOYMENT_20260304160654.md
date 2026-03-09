# 배포 가이드 문서

## 개요
본 문서는 linkage, predictiveMaintenance-domain, preprocessor 모듈의 배포에 대한 가이드를 제공합니다.

## 모듈별 배포 정보

### 1. Linkage Module (linkage-mba)

#### 포트
- **기본 포트**: 6803
- **설정 위치**: `linkage/linkage-mba/src/main/resources/application.yml`

#### 프로파일
- **local**: 개발 환경 (`application-local.yml`)
- **real**: 운영 환경 (`application-real.yml`)

#### 주요 설정 항목
```yaml
server:
  port: 6803

spring:
  ems:
    roadm_d_ip: # EMS 서버 IP 주소
    port_mmc: # TL1 포트 번호
    id: # 로그인 ID
    pw: # 로그인 비밀번호 (Jasypt 암호화 권장)
  
  rabbitmq:
    # RabbitMQ 연결 정보
```

#### 배포 전 확인사항
1. **EMS 연결 정보**: IP, 포트, 인증 정보 확인
2. **RabbitMQ 연결**: 큐 이름 및 연결 정보 확인
3. **데이터베이스 연결**: JPA 및 MyBatis 설정 확인
4. **스케줄러 활성화**: `@EnableScheduling` 확인

#### 실행 방법
```bash
# 로컬 환경
java -jar linkage-mba.jar --spring.profiles.active=local

# 운영 환경
java -jar linkage-mba.jar --spring.profiles.active=real
```

#### 의존성 확인
- RabbitMQ 서버 실행 상태
- 데이터베이스 연결 가능 여부
- EMS 서버 접근 가능 여부

---

### 2. Preprocessor Module (prepro-mba)

#### 포트
- **기본 포트**: 6804
- **설정 위치**: `preprocessor/prepro-mba/src/main/resources/application.yml`

#### 프로파일
- **local**: 개발 환경 (`application-local.yml`)
- **real**: 운영 환경 (`application-real.yml`)

#### 주요 설정 항목
```yaml
server:
  port: 6804

spring:
  rabbitmq:
    mbaAiModelResultQueue: # AI 모델 결과 수신 큐
    engineMbaTicketQueue: # 엔진 전송 큐
    # RabbitMQ 연결 정보
```

#### 배포 전 확인사항
1. **RabbitMQ 큐 설정**: 
   - `mbaAiModelResultQueue`: AI 모델 결과 수신 큐 존재 확인
   - `EngineMbaTicket_Queue`: 엔진 전송 큐 존재 확인
2. **데이터베이스 연결**: JPA 설정 확인
3. **리스너 활성화**: `@RabbitListener` 정상 동작 확인

#### 실행 방법
```bash
# 로컬 환경
java -jar prepro-mba.jar --spring.profiles.active=local

# 운영 환경
java -jar prepro-mba.jar --spring.profiles.active=real
```

#### 의존성 확인
- RabbitMQ 서버 실행 상태
- AI 모델 결과 큐에서 메시지 수신 가능 여부
- 데이터베이스 연결 가능 여부

---

### 3. PredictiveMaintenance Domain Module

#### 포트
- **기본 포트**: 6801
- **설정 위치**: `predictiveMaintenance-domain/src/main/resources/application.yml`

#### 프로파일
- **local**: 개발 환경 (`application-local.yml`)
- **real**: 운영 환경 (`application-real.yml`)

#### 주요 설정 항목
```yaml
server:
  port: 6801

client:
  base-url: # AI 모델 서버 URL
  connect-timeout-ms: 3000  # 연결 타임아웃 (기본값)
  read-timeout-ms: 5000     # 읽기 타임아웃 (기본값)

spring:
  # 데이터베이스 연결 정보
  # Jasypt 암호화 설정
```

#### 배포 전 확인사항
1. **AI 모델 서버**: URL 및 접근 가능 여부 확인
2. **데이터베이스 연결**: PostgreSQL 연결 정보 확인
3. **프로시저 존재**: 다음 프로시저들이 DB에 존재하는지 확인
   - `MBA.FC_SET_PERFORMANCE_DAILY()`
   - `MBA.FC_SET_PERFORMANCE_DAILY_NTD()`
   - `MBA.FC_SET_PERFORMANCE_DAILY_REFERENCE()`
4. **스케줄러 활성화**: `@EnableScheduling` 확인
5. **타임아웃 설정**: AI 모델 서버 응답 시간에 맞게 조정

#### 실행 방법
```bash
# 로컬 환경
java -jar predictiveMaintenance-domain.jar --spring.profiles.active=local

# 운영 환경
java -jar predictiveMaintenance-domain.jar --spring.profiles.active=real
```

#### 의존성 확인
- 데이터베이스 연결 가능 여부
- AI 모델 서버 접근 가능 여부
- 필요한 DB 프로시저 존재 여부

---

## 전체 시스템 배포 순서

### 권장 배포 순서

1. **인프라 준비**
   - RabbitMQ 서버 실행
   - 데이터베이스 서버 실행
   - 필요한 큐 생성 확인

2. **Linkage Module 배포**
   - EMS 연결 테스트
   - 데이터 수집 테스트
   - RabbitMQ 전송 테스트

3. **Preprocessor Module 배포**
   - RabbitMQ 리스너 동작 확인
   - 데이터 처리 테스트
   - 엔진 전송 테스트

4. **PredictiveMaintenance Domain Module 배포**
   - 스케줄러 동작 확인
   - 프로시저 실행 테스트
   - AI 모델 서버 통신 테스트

### 배포 후 검증

#### Linkage Module
- [ ] 스케줄러가 15분마다 정상 실행되는지 확인
- [ ] EMS 연결이 정상적으로 이루어지는지 확인
- [ ] PM 데이터가 DB에 정상 저장되는지 확인
- [ ] RabbitMQ로 메시지가 정상 전송되는지 확인

#### Preprocessor Module
- [ ] RabbitMQ 리스너가 정상 동작하는지 확인
- [ ] AI 모델 결과를 정상 수신하는지 확인
- [ ] 저신호 데이터 필터링이 정상 동작하는지 확인
- [ ] 엔진으로 데이터가 정상 전송되는지 확인

#### PredictiveMaintenance Domain Module
- [ ] 일일 스케줄러가 정상 실행되는지 확인 (01:05)
- [ ] 티켓 처리 스케줄러가 정상 실행되는지 확인 (02:05)
- [ ] DB 프로시저가 정상 실행되는지 확인
- [ ] AI 모델 서버로 데이터가 정상 전송되는지 확인

---

## 환경별 설정 관리

### Jasypt 암호화
모든 모듈에서 민감한 정보(비밀번호 등)는 Jasypt를 사용하여 암호화하는 것을 권장합니다.

```yaml
spring:
  jasypt:
    encryptor:
      bean: jasyptStringEncryptor
      property:
        prefix: ENC(
        suffix: )
```

### 로깅 설정
각 모듈은 프로파일별 로깅 설정을 가지고 있습니다:
- `logback-local.properties`: 로컬 환경 로깅
- `logback-real.properties`: 운영 환경 로깅

---

## 모니터링 및 로그

### 로그 위치
각 모듈의 로그는 다음 위치에서 확인할 수 있습니다:
- **Linkage**: `linkage/mba/logs/`
- **Preprocessor**: `preprocessor/mba/logs/`
- **PredictiveMaintenance**: `predictiveMaintenance/logs/`

### 주요 모니터링 항목

1. **애플리케이션 상태**
   - 프로세스 실행 여부
   - 포트 리스닝 상태
   - 메모리 사용량

2. **스케줄러 실행**
   - 스케줄러 실행 로그
   - 실행 시간 및 소요 시간

3. **외부 시스템 연결**
   - EMS 연결 상태
   - RabbitMQ 연결 상태
   - 데이터베이스 연결 상태
   - AI 모델 서버 연결 상태

4. **데이터 처리**
   - 데이터 수집 건수
   - 데이터 처리 건수
   - 에러 발생 건수

---

## 트러블슈팅

### Linkage Module

#### 문제: EMS 연결 실패
- **원인**: IP, 포트, 인증 정보 오류
- **해결**: 설정 파일 확인 및 EMS 서버 상태 확인

#### 문제: RabbitMQ 전송 실패
- **원인**: 큐 미생성 또는 연결 정보 오류
- **해결**: RabbitMQ 큐 생성 및 연결 정보 확인

### Preprocessor Module

#### 문제: 메시지 수신 실패
- **원인**: 큐 이름 불일치 또는 리스너 미동작
- **해결**: 큐 이름 확인 및 리스너 로그 확인

#### 문제: 데이터 처리 실패
- **원인**: 데이터 형식 오류 또는 DB 연결 실패
- **해결**: 메시지 형식 확인 및 DB 연결 상태 확인

### PredictiveMaintenance Domain Module

#### 문제: 프로시저 실행 실패
- **원인**: 프로시저 미존재 또는 권한 부족
- **해결**: DB에서 프로시저 존재 여부 및 권한 확인

#### 문제: AI 모델 서버 통신 실패
- **원인**: URL 오류 또는 타임아웃
- **해결**: URL 확인 및 타임아웃 설정 조정

---

## 롤백 계획

### 배포 실패 시 롤백 절차

1. **이전 버전 JAR 파일로 교체**
2. **애플리케이션 재시작**
3. **기능 동작 확인**
4. **로그 확인 및 문제 분석**

### 데이터 일관성 확인

- 배포 전 데이터 백업 권장
- 배포 후 데이터 무결성 확인
- 프로시저 실행 결과 검증

---

## 성능 고려사항

### 리소스 요구사항
- **메모리**: 각 모듈별 최소 512MB 권장
- **CPU**: 스케줄러 실행 시 CPU 사용량 모니터링
- **네트워크**: 외부 시스템과의 통신 대역폭 고려

### 동시 실행
- 각 모듈은 독립적으로 실행 가능
- 동일 서버에서 실행 시 리소스 충돌 주의
- 스케줄러 실행 시간 겹침 방지

---

## 보안 고려사항

1. **인증 정보 암호화**: Jasypt 사용 권장
2. **네트워크 보안**: 방화벽 규칙 설정
3. **로그 보안**: 민감 정보 로깅 금지
4. **접근 제어**: 필요한 포트만 개방

---

## 배포 체크리스트

### 배포 전
- [ ] 모든 설정 파일 검토
- [ ] 의존성 서비스 실행 상태 확인
- [ ] 데이터베이스 연결 테스트
- [ ] 외부 시스템 연결 테스트
- [ ] 이전 버전 백업

### 배포 중
- [ ] 단계별 배포 진행
- [ ] 각 단계별 검증 수행
- [ ] 로그 모니터링

### 배포 후
- [ ] 기능 동작 확인
- [ ] 성능 모니터링
- [ ] 에러 로그 확인
- [ ] 사용자 피드백 수집
