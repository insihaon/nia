# Preprocessor Module 분석 문서

## 개요
Preprocessor 모듈은 AI 모델로부터 수신한 결과 데이터를 처리하고, 저신호 성능 데이터를 필터링하여 티켓 데이터를 생성하는 역할을 수행합니다. 또한 처리된 데이터를 엔진으로 전송합니다.

## 모듈 구조

### 주요 패키지
- `kr.go.ap.prepro.mba.listener`: RabbitMQ 메시지 리스너
- `kr.go.ap.prepro.mba.service`: 핵심 비즈니스 로직
- `kr.go.ap.prepro.mba.service.ticket`: 티켓 데이터 처리
- `kr.go.ap.prepro.mba.amqp`: RabbitMQ 메시지 전송
- `kr.go.ap.prepro.mba.config`: RabbitMQ 설정

### 주요 클래스

#### 1. MbaAiResultListener
- **역할**: RabbitMQ에서 AI 모델 결과 수신
- **Queue**: `mbaAiModelResultQueue` (설정 파일에서 지정)
- **기능**: 
  - JSON 메시지 수신 및 파싱
  - `ModelResultDto` 리스트로 변환
  - `LineMonitoringHdlService`로 전달

#### 2. LineMonitoringHdlService
- **역할**: 라인 모니터링 데이터 처리
- **주요 메서드**: `lineMonitoringHdlProcessor()`
- **기능**:
  - AI 모델 결과를 `OpticalPerformanceDto`로 변환
  - 저신호 조건 필터링: `valueMax - valueMin >= 3`
  - 필터링된 데이터를 DB에 저장 (`RoadmCheckOpticalPerformanceEntity`)
  - 티켓 데이터 생성 및 전송

#### 3. TicketDataService
- **역할**: 티켓 데이터 생성 및 엔진 전송
- **주요 메서드**: `setTicketData()`
- **기능**:
  - TrunkId별로 데이터 그룹화
  - UP/DOWN 방향별 분리
  - 라우트 번호 기반 최대/최소 값 추출
  - 히스토리 데이터 조회 및 병합
  - 엔진으로 저신호 데이터 전송

#### 4. MbaEnginePrdAmqp
- **역할**: RabbitMQ를 통한 엔진으로 데이터 전송
- **Queue**: `EngineMbaTicket_Queue`
- **전송 데이터**: `EngineLowPmDataListDto`

## 데이터 처리 Flow

```
1. RabbitMQ에서 AI 모델 결과 수신
   ↓
2. MbaAiResultListener.onMessage()
   ├─ JSON 메시지 파싱
   └─ ModelResultDto 리스트로 변환
   ↓
3. LineMonitoringHdlService.lineMonitoringHdlProcessor()
   ├─ ModelResultDto → OpticalPerformanceDto 변환
   ├─ 저신호 조건 필터링 (valueMax - valueMin >= 3)
   ├─ 필터링된 데이터를 DB에 저장
   │  └─ RoadmCheckOpticalPerformanceEntity
   └─ TicketDataService.setTicketData() 호출
   ↓
4. TicketDataService.setTicketData()
   ├─ TrunkId별로 데이터 그룹화
   ├─ UP/DOWN 방향별 분리
   ├─ 라우트 번호 기반 최대/최소 값 추출
   ├─ 히스토리 데이터 조회
   │  └─ setLowSignalDataHist(): 15분 전 데이터 조회
   ├─ 히스토리 데이터와 현재 데이터 병합
   │  └─ histLowSignalCheck(): 저신호 플래그 설정
   └─ 엔진으로 데이터 전송
      └─ MbaEnginePrdAmqp.sendMessageCmd()
```

## 데이터 처리 로직

### 1. 저신호 필터링
```java
if ((optPerfDto.getValueMax() - optPerfDto.getValueMin() >= 3)) {
    lowOptPerfDtoList.add(optPerfDto);
}
```
- **조건**: 최대값과 최소값의 차이가 3 이상인 경우
- **의미**: 신호 강도 변동이 큰 구간 식별

### 2. TrunkId별 그룹화
- 동일한 TrunkId를 가진 데이터들을 그룹으로 묶어 처리
- 각 그룹별로 독립적인 티켓 데이터 생성

### 3. 방향별 처리 (UP/DOWN)
- **UP 방향**: 
  - IN 포트의 최대 라우트 번호 추출
  - REPEATER 장비의 다음 라우트 번호 데이터 추가
- **DOWN 방향**:
  - IN 포트의 최소 라우트 번호 추출
  - REPEATER 장비의 다음 라우트 번호 데이터 추가

### 4. 히스토리 데이터 병합
- **조회 범위**: 현재 시간 기준 15분 전 데이터
- **조건**: TrunkId, Direction, TimeRange로 필터링
- **병합 로직**: 현재 저신호 데이터와 동일한 키를 가진 히스토리 데이터에 저신호 플래그 설정

## 데이터 저장

### 1. RoadmCheckOpticalPerformanceEntity
- **저장 위치**: `RoadmCheckOpticalPerformanceRepository`
- **데이터**: 저신호 성능 데이터
- **용도**: 모니터링 및 분석

### 2. RoadmPerformanceHistEntity
- **조회 용도**: 히스토리 데이터 조회
- **조회 조건**: TrunkId, OcrTime, RouteNum

## RabbitMQ 전송

### 수신 (Listener)
- **Queue**: `mbaAiModelResultQueue`
- **메시지 형식**: JSON
- **데이터**: `List<ModelResultDto>`

### 전송 (Producer)
- **Queue**: `EngineMbaTicket_Queue`
- **메시지 형식**: JSON
- **전송 데이터**: `EngineLowPmDataListDto`
  ```java
  {
    "lowPmDataDtoList": List<OpticalPerformanceDto>,      // 현재 저신호 데이터
    "lowPmHistDataDtoList": List<OpticalPerformanceDto>   // 히스토리 데이터
  }
  ```

## 유의사항

### 1. 메시지 처리
- **에러 처리**: JSON 파싱 실패 시 로그 기록 후 스킵
- **Null 체크**: 모든 단계에서 Null 체크 수행
- **빈 리스트 처리**: 빈 데이터는 처리하지 않음

### 2. 데이터 변환
- **ModelMapper 사용**: DTO 간 변환 시 ModelMapper 활용
- **커스텀 매핑**: 일부 필드는 수동 매핑 필요 (inOut, port, tid 등)

### 3. 히스토리 데이터 처리
- **시간 범위**: 현재 시간 기준 15분 전까지
- **데이터 부재 시**: 빈 리스트 반환하여 처리 계속 진행
- **키 매칭**: TID|Ptpname|Port|InOut|OcrTime 조합으로 고유 키 생성

### 4. 라우트 번호 처리
- **최대값 제한**: 라우트 번호는 최대 10으로 제한
- **REPEATER 장비**: REPEATER 장비만 다음 라우트 번호 데이터 추가
- **예외 처리**: 최대/최소 값 추출 실패 시 경고 로그 및 기본 처리

### 5. 성능 고려사항
- **스트림 처리**: Java Stream API 활용한 효율적 데이터 처리
- **배치 저장**: JPA saveAll() 사용으로 배치 저장
- **메모리 관리**: 대용량 리스트 처리 시 주의

## 설정 파일

### application.yml
- **포트**: 6804
- **RabbitMQ 설정**: 
  - `spring.rabbitmq.mbaAiModelResultQueue`: AI 모델 결과 수신 큐
  - `spring.rabbitmq.engineMbaTicketQueue`: 엔진 전송 큐
  - RabbitMqProperties를 통한 설정 주입

## 의존성

### 주요 의존성
- Spring Boot (AMQP, JPA)
- RabbitMQ (메시지 큐)
- ModelMapper (DTO 변환)
- Jackson (JSON 처리)
- Lombok (코드 간소화)

## 모니터링 포인트

1. **메시지 수신**: RabbitMQ에서 메시지 정상 수신 여부
2. **필터링 결과**: 저신호 데이터 필터링 건수
3. **티켓 생성**: 티켓 데이터 생성 성공/실패
4. **엔진 전송**: 엔진으로 데이터 전송 성공/실패
5. **에러 발생 빈도**: 예외 로그 모니터링

## 데이터 흐름 다이어그램

```
AI 모델
  ↓ (RabbitMQ: mbaAiModelResultQueue)
MbaAiResultListener
  ↓
LineMonitoringHdlService
  ├─ 필터링 (저신호 조건)
  ├─ DB 저장 (RoadmCheckOpticalPerformanceEntity)
  └─ TicketDataService
      ├─ 그룹화 및 분리
      ├─ 히스토리 조회
      └─ 엔진 전송
          ↓ (RabbitMQ: EngineMbaTicket_Queue)
        엔진 시스템
```
