# Linkage Module 분석 문서

## 개요
Linkage 모듈은 EMS(Element Management System)와의 연동을 담당하는 모듈로, TL1 프로토콜을 통해 장비로부터 성능 데이터를 수집하고 AI 모델로 전송하는 역할을 수행합니다.

## 모듈 구조

### 주요 패키지
- `kr.go.ap.linkage.mba.scheduler`: 스케줄러 서비스
- `kr.go.ap.linkage.mba.service`: 핵심 비즈니스 로직
- `kr.go.ap.linkage.mba.telnet`: TL1 통신 클라이언트
- `kr.go.ap.linkage.mba.amqp`: RabbitMQ 메시지 전송
- `kr.go.ap.linkage.mba.config`: RabbitMQ 설정

### 주요 클래스

#### 1. LinkageSchdulerService
- **역할**: 15분마다 데이터 수집 작업을 스케줄링
- **스케줄**: `@Scheduled(cron = "0 0/15 * * * *")` - 매 15분마다 실행
- **기능**: `EmsMmcHdlService.pmCollectHdl()` 호출

#### 2. EmsMmcHdlService
- **역할**: EMS 시스템과의 통신 및 데이터 수집 처리
- **주요 메서드**:
  - `pmCollectHdl()`: 전체 수집 프로세스 실행
  - `sipcMmc()`: SIPC(Shelf Information) 수집
  - `pmMmc()`: PM(Performance Monitoring) 데이터 수집
  - `getClient()`: TL1 클라이언트 연결 관리 (Thread-safe)

#### 3. ParsingHdlService
- **역할**: TL1 응답 메시지 파싱 및 데이터 변환
- **주요 기능**:
  - SIPC 응답 파싱: READY 상태의 Shelf 정보 추출
  - PM 응답 파싱: 성능 데이터(RX/TX 값) 추출 및 DB 저장
  - OCR 시간 계산: 15분 단위로 시간 절삭

#### 4. Tl1SocketClient
- **역할**: TL1 프로토콜 기반 Socket 통신 클라이언트
- **기능**:
  - Socket 연결 및 로그인/로그아웃 처리
  - TL1 명령 전송 및 응답 수신
  - 타임아웃 관리 (읽기 타임아웃: 20초)

#### 5. MbaAiModelPrdAmqp
- **역할**: RabbitMQ를 통한 AI 모델로 데이터 전송
- **Queue**: `MbaAiModel_Queue`
- **전송 데이터**: `ModelSendDto` (OCR 시간 포함)

## 데이터 수집 Flow

```
1. 스케줄러 실행 (15분마다)
   ↓
2. EmsMmcHdlService.pmCollectHdl()
   ↓
3. sipcMmc() - SIPC 정보 수집
   ├─ DB에서 6300 모델 장비 조회
   ├─ TL1 명령: "RTRV-SIPC:{TID}-SH1::::;"
   ├─ 응답 파싱 (ParsingHdlService)
   └─ READY 상태 Shelf 정보를 DB에 저장 (EqmntSipcEntity)
   ↓
4. pmMmc() - PM 데이터 수집
   ├─ 10초 대기 (Thread.sleep(10_000))
   ├─ DB에서 SIPC 정보 조회
   ├─ TL1 명령: "RTRV-PM:{TID}-{SystemName}::::SIGNAL=AMPPWR,TYPE=15M,INTERVAL=CURR;"
   ├─ 응답 파싱 (ParsingHdlService)
   ├─ PM 데이터를 DB에 저장 (PmEntity)
   ├─ 전처리 프로시저 호출: pmMapper.callPmPrepro(ocrTime)
   ├─ Linkage 키 업데이트: commonMapper.updateLinkageYdKey()
   └─ RabbitMQ로 AI 모델에 데이터 전송 (ModelSendDto)
```

## 데이터베이스 테이블

### 조회 테이블
- **NIA.TB_EQUIP_MST** (`EqmntEntity`)
  - **용도**: 6300 모델 장비 조회
  - **주요 컬럼**: sysname, tid, model
  - **사용 위치**: `EmsMmcHdlService.sipcMmc()` - 장비 목록 조회

### 저장 테이블

#### 1. SIPC 데이터
- **스키마**: `NIA`
- **테이블명**: `TB_EQUIP_SIPC`
- **Entity**: `EqmntSipcEntity`
- **Repository**: `EqmntSipcRepository`
- **Primary Key**: `tid`, `sysname` (복합키)
- **저장 데이터**: 
  - `tid`: 장비 TID
  - `sysname`: 시스템명 (SH1, SH2 등)
- **저장 방식**: 
  - 기존 데이터 삭제 후 저장 (`deleteByEqmntSipcKey_Tid()`)
  - READY 상태의 Shelf만 저장

#### 2. PM 데이터
- **스키마**: `NIA`
- **테이블명**: `TB_PERFORMACE_MST`
- **Entity**: `PmEntity`
- **Repository**: `PmRepository`
- **Primary Key**: `sysname`, `port`, `ocrtime` (복합키)
- **저장 데이터**: 
  - `sysname`: 시스템명
  - `port`: 포트명 (예: S.1-ADD_IN)
  - `unit`: 유닛명 (예: MRSA-2A)
  - `tmper`: 시간 주기 (15M)
  - `rxCur`, `rxMin`, `rxMax`, `rxAve`: RX 성능 값
  - `txCur`, `txMin`, `txMax`, `txAve`: TX 성능 값
  - `ocrtime`: 수집 시간 (15분 단위 절삭)
- **저장 방식**: JPA `saveAll()` 사용

### 프로시저 호출
- **스키마**: `MBA`
- **프로시저명**: `FC_SET_PERFORMANCE_PREPROCESSOR(ocrTime)`
- **호출 위치**: `EmsMmcHdlService.pmMmc()` - PM 데이터 저장 후
- **파라미터**: `ocrTime` (String 타입, Timestamp 형식)
- **용도**: 수집된 PM 데이터를 전처리하여 다른 테이블에 저장

## RabbitMQ 전송

### 전송 정보
- **Queue**: `MbaAiModel_Queue` (설정 파일에서 지정)
- **메시지 형식**: JSON (Jackson2JsonMessageConverter)
- **전송 데이터**: `ModelSendDto`
  ```java
  {
    "ocrTime": "2024-01-01 10:00:00"  // 15분 단위 절삭된 시간
  }
  ```

## 유의사항

### 1. 연결 관리
- **Thread-safe**: `ReentrantLock`을 사용한 동시성 제어
- **연결 상태 확인**: `isHealthy()` 메서드로 연결 상태 검증
- **자원 정리**: `@PreDestroy`로 애플리케이션 종료 시 연결 해제

### 2. 타임아웃 처리
- **연결 타임아웃**: 15초
- **읽기 타임아웃**: 20초
- **명령 간 대기**: SIPC 수집 후 2초, PM 수집 후 3초

### 3. 에러 처리
- **예외 처리**: 각 단계별 try-catch로 독립적 처리
- **로그 기록**: 모든 에러는 상세 스택 트레이스와 함께 로깅
- **연결 실패 시**: 클라이언트 재생성 및 재연결 시도

### 4. 데이터 파싱
- **정규식 기반 파싱**: 복잡한 TL1 응답 형식 처리
- **상태 확인**: COMPLD/DENY 상태 검증
- **데이터 검증**: 필수 필드 누락 시 경고 로그 및 스킵

### 5. 성능 고려사항
- **배치 처리**: 여러 장비에 대한 순차 처리
- **DB 트랜잭션**: JPA saveAll() 사용으로 배치 저장
- **메모리 관리**: 대용량 응답 처리 시 버퍼 크기 고려 (4096 bytes)

## 설정 파일

### application.yml
- **포트**: 6803
- **EMS 설정**: 
  - `spring.ems.roadm_d_ip`: EMS 서버 IP
  - `spring.ems.port_mmc`: TL1 포트
  - `spring.ems.id`: 로그인 ID
  - `spring.ems.pw`: 로그인 비밀번호
- **RabbitMQ 설정**: RabbitMqProperties를 통한 설정 주입

## 의존성

### 주요 의존성
- Spring Boot (Scheduling, AMQP)
- RabbitMQ (메시지 큐)
- JPA (데이터베이스)
- MyBatis (프로시저 호출)
- Lombok (코드 간소화)

## 데이터베이스 테이블 요약

| 스키마 | 테이블명 | 용도 | 작업 |
|--------|----------|------|------|
| NIA | TB_EQUIP_MST | 장비 마스터 | 조회 (6300 모델 장비) |
| NIA | TB_EQUIP_SIPC | 장비 SIPC 정보 | 저장 (Shelf 정보) |
| NIA | TB_PERFORMACE_MST | 성능 데이터 마스터 | 저장 (PM 데이터) |
| MBA | FC_SET_PERFORMANCE_PREPROCESSOR | 전처리 프로시저 | 호출 (PM 데이터 전처리) |

### 테이블별 상세 정보

#### NIA.TB_EQUIP_MST
- **역할**: 장비 마스터 테이블
- **사용**: 6300 모델 장비 목록 조회
- **조회 조건**: `model LIKE '%6300%'`

#### NIA.TB_EQUIP_SIPC
- **역할**: 장비 Shelf 정보 저장
- **저장 주기**: 15분마다 (스케줄러 실행 시)
- **저장 로직**: 기존 TID 데이터 삭제 후 신규 저장
- **저장 조건**: READY 상태의 Shelf만 저장

#### NIA.TB_PERFORMACE_MST
- **역할**: 성능 모니터링 데이터 저장
- **저장 주기**: 15분마다 (스케줄러 실행 시)
- **데이터 소스**: EMS 시스템 TL1 프로토콜
- **저장 후 처리**: 전처리 프로시저 호출

## 모니터링 포인트

1. **스케줄러 실행 여부**: 15분마다 정상 실행 확인
2. **TL1 연결 상태**: 로그인 성공/실패 로그 확인
3. **데이터 수집량**: 수집된 PM 데이터 건수 확인
4. **RabbitMQ 전송**: 메시지 전송 성공/실패 로그 확인
5. **에러 발생 빈도**: 예외 로그 모니터링
6. **DB 저장 상태**: 각 테이블별 저장 건수 확인