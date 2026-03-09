# PredictiveMaintenance Domain Module 분석 문서

## 개요
PredictiveMaintenance Domain 모듈은 예측 정비를 위한 일일 성능 데이터 생성, 티켓 처리, 그리고 AI 모델로의 데이터 전송을 담당하는 핵심 비즈니스 로직 모듈입니다.

## 모듈 구조

### 주요 패키지
- `kr.go.ap.pm.manager.service.schduler`: 스케줄러 서비스
- `kr.go.ap.pm.manager.service.pm`: 성능 데이터 처리
- `kr.go.ap.pm.manager.service.model`: AI 모델 데이터 전송
- `kr.go.ap.pm.manager.service.ticket`: 티켓 처리
- `kr.go.ap.pm.manager.config`: REST API 설정

### 주요 클래스

#### 1. PmSchdulerService
- **역할**: 예측 정비 작업 스케줄링
- **스케줄**:
  - `performanceDaily()`: 매일 01:05 실행 (`@Scheduled(cron = "0 5 1 * * *")`)
  - `performanceTicketCheck()`: 매일 02:05 실행 (`@Scheduled(cron = "0 5 2 * * *")`)
- **기능**: 
  - 일일 성능 데이터 생성
  - 경고 날짜 확인 및 티켓 처리

#### 2. PmDailyService
- **역할**: 일일 성능 데이터 생성 및 AI 모델 전송
- **주요 메서드**: `performanceDailyHdl()`
- **기능**:
  - 일일 성능 데이터 생성 (`createPmDailyData()`)
  - 일일 NTD 데이터 생성 (`createPmDailyNtdData()`)
  - 참조 데이터 생성 (`createPmDailyReferencedData()`)
  - AI 모델로 데이터 전송 (`aiPmDataSendService.sendPmData()`)

#### 3. AiPmDataSendService
- **역할**: AI 모델로 성능 데이터 전송
- **주요 메서드**: `sendPmData()`, `sendData()`
- **기능**:
  - DB에서 전송 대상 데이터 조회
  - REST API를 통한 AI 모델 서버로 데이터 전송
  - HTTP 상태 코드 및 응답 처리

#### 4. PmTicketService
- **역할**: 티켓 처리 오케스트레이션
- **주요 메서드**: `ticketHdlProcessor()`
- **기능**:
  - Case 1 티켓 처리
  - 경고 날짜 확인
  - Case 2 티켓 처리

#### 5. RestfulConfig
- **역할**: REST API 클라이언트 설정
- **기능**:
  - RestTemplate 빈 생성
  - HTTP 클라이언트 타임아웃 설정
  - 연결 타임아웃: 3초 (기본값)
  - 읽기 타임아웃: 5초 (기본값)

## 데이터 처리 Flow

### 일일 성능 데이터 생성 Flow

```
1. 스케줄러 실행 (매일 01:05)
   ↓
2. PmDailyService.performanceDailyHdl()
   ↓
3. createPmDailyData()
   └─ pmPreMapper.callSetPerformanceDaily()
      └─ DB 프로시저 호출: MBA.FC_SET_PERFORMANCE_DAILY()
   ↓
4. createPmDailyNtdData()
   └─ pmPreMapper.callSetPerformanceDailyNtd()
      └─ DB 프로시저 호출: MBA.FC_SET_PERFORMANCE_DAILY_NTD()
   ↓
5. createPmDailyReferencedData()
   └─ pmPreMapper.callSetPerformanceDailyReference()
      └─ DB 프로시저 호출: MBA.FC_SET_PERFORMANCE_DAILY_REFERENCE()
   ↓
6. aiPmDataSendService.sendPmData()
   ├─ DB에서 전송 대상 데이터 조회
   │  └─ pmPreMapper.selectPmModelSendDataList()
   ├─ 각 데이터에 대해 REST API 호출
   │  └─ POST /api/pmData
   └─ 응답 로깅 및 에러 처리
```

### 티켓 처리 Flow

```
1. 스케줄러 실행 (매일 02:05)
   ↓
2. PmSchdulerService.performanceTicketCheck()
   ├─ pmWarningDateService.warningDateCheck()
   └─ pmTicketService.ticketHdlProcessor()
      ├─ ticketCaseOneService.ticketCaseOneHdlProcessor()
      ├─ pmWarningDateService.warningDateCheck()
      └─ ticketCaseTwoService.ticketCaseTwoHdlProcessor()
```

## 데이터베이스 연동

### 프로시저 호출

#### 1. 일일 성능 데이터 생성
- **스키마**: `MBA`
- **프로시저명**: `FC_SET_PERFORMANCE_DAILY()`
- **호출 위치**: `PmDailyService.createPmDailyData()`
- **용도**: 일일 성능 데이터 집계 및 저장
- **저장 테이블**: `MBA.TB_ROADM_OPTICAL_PERFORMANCE_DAILY` (프로시저 내부에서 처리)

#### 2. 일일 NTD 데이터 생성
- **스키마**: `MBA`
- **프로시저명**: `FC_SET_PERFORMANCE_DAILY_NTD()`
- **호출 위치**: `PmDailyService.createPmDailyNtdData()`
- **용도**: NTD(Node Total Deviation) 데이터 생성 및 저장

#### 3. 참조 데이터 생성
- **스키마**: `MBA`
- **프로시저명**: `FC_SET_PERFORMANCE_DAILY_REFERENCE()`
- **호출 위치**: `PmDailyService.createPmDailyReferencedData()`
- **용도**: 참조 데이터 생성 및 저장

### 데이터 조회

#### AI 모델 전송 데이터 조회
- **Mapper**: `PmPreMapper.selectPmModelSendDataList()`
- **반환 타입**: `List<PmModelSendDataDto>`
- **용도**: AI 모델 서버로 전송할 데이터 조회
- **조회 테이블**: 
  - `MBA.TB_ROADM_OPTICAL_PERFORMANCE_DAILY` (주요)
  - 기타 관련 테이블 (프로시저 내부에서 조인)

### 데이터 저장

#### 일일 성능 데이터
- **스키마**: 기본 스키마 (명시되지 않음, 프로시저에서 처리)
- **테이블명**: `tb_roadm_optical_performance_daily`
- **Entity**: `RoadmPerformanceDailyEntity`
- **Repository**: `RoadmPerformanceDailyRepository`
- **Primary Key**: `collectionDate`, `trunkId`, `trunkName`, `tid`, `sysname`, `inOut`, `routenum`, `direction` (복합키)
- **저장 데이터**: 
  - `collectionDate`: 수집 일자
  - `trunkId`, `trunkName`: 트렁크 정보
  - `tid`, `sysname`: 장비 정보
  - `inOut`: IN/OUT 구분
  - `routenum`: 라우트 번호
  - `direction`: 방향 (UP/DOWN)
  - `ptpname`: PTP 이름
  - `valueMax`: 최대값
  - `spanLoss`: 스팬 손실
  - `nodeGain`: 노드 게인
  - `nodeTotalDeviation`: 노드 총 편차
  - `ntdVariance`: NTD 분산
- **저장 방식**: 프로시저 내부에서 처리 (JPA 직접 저장 아님)

### 프로시저에서 사용하는 테이블
- **MBA.TB_ROADM_OPTICAL_PERFORMANCE_DAILY**: 일일 성능 데이터 저장 및 조회
- **CALIB**: 캘리브레이션 데이터 (프로시저 내부에서 조인)
- **RCA.FC_GET_MINUS_DAY()**: 날짜 계산 함수 사용

## REST API 통신

### 전송 정보
- **URL**: `${client.base-url}/api/pmData`
- **Method**: POST
- **Content-Type**: application/json
- **Accept**: application/json
- **Request Body**: `PmModelSendDataDto`

### 타임아웃 설정
- **연결 타임아웃**: 3초 (기본값, 설정 가능)
- **읽기 타임아웃**: 5초 (기본값, 설정 가능)
- **설정 위치**: `application.yml`의 `client.connect-timeout-ms`, `client.read-timeout-ms`

### 에러 처리
- **HTTP 에러**: 상태 코드 및 응답 본문 로깅
- **연결 실패**: 예외 발생 및 로깅
- **재시도**: 현재 구현에서는 재시도 로직 없음

## 유의사항

### 1. 스케줄러 실행 시간
- **일일 데이터 생성**: 매일 01:05 (새벽 시간대)
- **티켓 처리**: 매일 02:05 (새벽 시간대)
- **주의**: 시간대 설정 확인 필요

### 2. 프로시저 실행 순서
- 일일 데이터 생성은 순차적으로 실행
- 각 단계별 독립적인 예외 처리
- 한 단계 실패해도 다음 단계는 계속 진행

### 3. REST API 전송
- **배치 전송**: 여러 데이터를 순차적으로 전송
- **에러 처리**: 개별 데이터 전송 실패 시에도 다음 데이터 전송 계속
- **로깅**: 모든 전송 결과 상세 로깅

### 4. 데이터 일관성
- 프로시저 기반 데이터 생성으로 일관성 보장
- 트랜잭션 관리는 프로시저 내부에서 처리

### 5. 성능 고려사항
- **대용량 데이터**: 전송 대상 데이터가 많을 경우 처리 시간 고려
- **타임아웃**: AI 모델 서버 응답 시간에 따른 타임아웃 조정 필요
- **동시성**: 스케줄러는 단일 인스턴스에서 실행 가정

## 설정 파일

### application.yml
- **포트**: 6801
- **클라이언트 설정**:
  - `client.base-url`: AI 모델 서버 URL
  - `client.connect-timeout-ms`: 연결 타임아웃 (기본 3000ms)
  - `client.read-timeout-ms`: 읽기 타임아웃 (기본 5000ms)

## 의존성

### 주요 의존성
- Spring Boot (Web, Scheduling, JPA)
- Apache HttpClient 5 (REST API 통신)
- PostgreSQL (데이터베이스)
- MyBatis (프로시저 호출)
- Jasypt (암호화)
- Lombok (코드 간소화)

## 데이터베이스 테이블 요약

| 스키마 | 테이블명 | 용도 | 작업 |
|--------|----------|------|------|
| MBA | TB_ROADM_OPTICAL_PERFORMANCE_DAILY | 일일 성능 데이터 | 저장/조회 (프로시저 처리) |
| MBA | FC_SET_PERFORMANCE_DAILY | 일일 데이터 생성 프로시저 | 호출 |
| MBA | FC_SET_PERFORMANCE_DAILY_NTD | NTD 데이터 생성 프로시저 | 호출 |
| MBA | FC_SET_PERFORMANCE_DAILY_REFERENCE | 참조 데이터 생성 프로시저 | 호출 |
| (기본 스키마) | tb_roadm_optical_performance_daily | 일일 성능 데이터 | 저장 (JPA Entity) |

### 테이블별 상세 정보

#### MBA.TB_ROADM_OPTICAL_PERFORMANCE_DAILY
- **역할**: 일일 성능 데이터 집계 및 저장
- **생성 주기**: 매일 01:05 (스케줄러 실행)
- **생성 방식**: 프로시저 `FC_SET_PERFORMANCE_DAILY()` 호출
- **데이터 소스**: 전처리된 PM 데이터
- **용도**: 일일 단위 성능 분석 및 AI 모델 전송

#### tb_roadm_optical_performance_daily
- **역할**: 일일 성능 데이터 Entity
- **스키마**: 명시되지 않음 (기본 스키마 사용 가능성)
- **저장 방식**: 프로시저 내부에서 처리 (JPA 직접 저장 아님)
- **Primary Key**: 복합키 (collectionDate, trunkId, trunkName, tid, sysname, inOut, routenum, direction)

### 프로시저 상세 정보

#### MBA.FC_SET_PERFORMANCE_DAILY()
- **호출 시점**: 매일 01:05
- **파라미터**: 없음
- **기능**: 전처리된 PM 데이터를 일일 단위로 집계하여 저장
- **저장 테이블**: `MBA.TB_ROADM_OPTICAL_PERFORMANCE_DAILY`

#### MBA.FC_SET_PERFORMANCE_DAILY_NTD()
- **호출 시점**: 매일 01:05 (일일 데이터 생성 후)
- **파라미터**: 없음
- **기능**: NTD(Node Total Deviation) 데이터 생성 및 저장

#### MBA.FC_SET_PERFORMANCE_DAILY_REFERENCE()
- **호출 시점**: 매일 01:05 (NTD 데이터 생성 후)
- **파라미터**: 없음
- **기능**: 참조 데이터 생성 및 저장

## 모니터링 포인트

1. **스케줄러 실행**: 매일 정해진 시간에 정상 실행 여부
2. **프로시저 실행**: 각 프로시저 실행 성공/실패
3. **데이터 전송**: AI 모델로 데이터 전송 성공/실패
4. **HTTP 응답**: REST API 응답 상태 코드 및 본문
5. **에러 발생 빈도**: 예외 로그 모니터링
6. **처리 시간**: 일일 데이터 생성 및 전송 소요 시간
7. **DB 프로시저 결과**: 각 프로시저 실행 결과 확인
8. **데이터 조회**: AI 모델 전송 데이터 조회 성공/실패

## 티켓 처리 상세

### Case 1 티켓 처리
- **서비스**: `TicketCaseOneService`
- **기능**: 특정 조건에 따른 티켓 생성 및 처리

### Case 2 티켓 처리
- **서비스**: `TicketCaseTwoService`
- **기능**: 다른 조건에 따른 티켓 생성 및 처리

### 경고 날짜 확인
- **서비스**: `PmWarningDateService`
- **기능**: 경고 날짜 검증 및 업데이트

## 데이터 흐름 다이어그램

```
스케줄러 (매일 01:05)
  ↓
PmDailyService
  ├─ DB 프로시저: 일일 데이터 생성
  ├─ DB 프로시저: NTD 데이터 생성
  ├─ DB 프로시저: 참조 데이터 생성
  └─ AiPmDataSendService
      ├─ DB 조회: 전송 대상 데이터
      └─ REST API: AI 모델 서버로 전송
          ↓ (POST /api/pmData)
        AI 모델 서버

스케줄러 (매일 02:05)
  ↓
PmTicketService
  ├─ Case 1 티켓 처리
  ├─ 경고 날짜 확인
  └─ Case 2 티켓 처리
```
