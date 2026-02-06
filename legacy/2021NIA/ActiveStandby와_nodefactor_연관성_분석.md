# ActiveStandby 문제와 nodefactor 수집의 연관성 분석

## 🔍 솔직한 분석 결과

### 결론: **직접적인 연관은 없습니다**

---

## 📊 실행 경로 비교

### nodefactor 수집 Flow (정상 Flow)
```
StartConfig.run() 
  ↓ (애플리케이션 시작 시 1회 실행)
Timer 설정 (measurementperiod 주기로 실행)
  ↓
collectSvc.latencyCheckAllNode()
  ↓
nodeRepository.findAll() ← DB 연결 필요
  ↓
Probe API 호출
  ↓
nodefactor 저장
```

### ActiveStandby 문제가 발생하는 Flow
```
Schedular.calculateDayDataAndOptimizeRoute()
  ↓ (매일 새벽 1시 실행)
collectSvc.linkUpdateFromIpsdn()
  ↓
linkRepository.findAll() ← DB 연결 필요
  ↓
linkRepository.saveAll() ← ActiveStandby 필드 에러 발생
```

---

## ❌ 직접적인 연관이 없는 이유

1. **별도의 실행 경로**
   - `latencyCheckAllNode`: `StartConfig`의 Timer에서 실행
   - `linkUpdateFromIpsdn`: `Schedular`의 스케줄러에서 실행
   - 서로 독립적으로 실행됨

2. **별도의 트랜잭션**
   - 각각 별도의 메서드로 실행
   - 하나가 실패해도 다른 하나에 직접 영향 없음

3. **에러 발생 시점이 다름**
   - `ActiveStandby` 에러: `linkUpdateFromIpsdn()` 실행 시
   - nodefactor 수집 실패: `latencyCheckAllNode()` 실행 시 (DB 연결 실패)

---

## 🤔 간접적인 연관 가능성 (낮음)

### 가능성 1: DB 연결 풀 공유
- 같은 DB 연결 풀을 사용할 수 있음
- `linkUpdateFromIpsdn()`에서 DB 연결 실패 시 연결 풀에 영향 가능
- 하지만 각 메서드는 독립적으로 실행되므로 직접 영향은 낮음

### 가능성 2: 애플리케이션 전체 에러
- `linkUpdateFromIpsdn()`에서 예외가 발생하면 애플리케이션 전체에 영향 가능
- 하지만 `Schedular`는 예외 처리가 되어 있어서 다른 작업에 영향은 적음

---

## ✅ 실제 nodefactor 수집 문제의 원인

사용자가 확인한 로그:
```
Auto-Collection Error. => Could not open JPA EntityManager for transaction; 
nested exception is org.hibernate.exception.JDBCConnectionException: 
Unable to acquire JDBC Connection
```

**이것이 실제 원인:**
- `latencyCheckAllNode()` 실행 시 DB 연결 실패
- `nodeRepository.findAll()` 호출 시 JDBC 연결을 얻을 수 없음
- 따라서 nodefactor 수집이 안 됨

**ActiveStandby 문제는:**
- `linkUpdateFromIpsdn()` 실행 시 발생하는 별도 문제
- nodefactor 수집과는 직접 연관 없음

---

## 📝 정리

### ActiveStandby 문제 해결의 의미:
- ✅ `linkUpdateFromIpsdn()` 에러 해결
- ✅ `Schedular.calculateDayDataAndOptimizeRoute()` 정상 실행
- ❌ **하지만 nodefactor 수집 문제와는 직접 연관 없음**

### 실제 nodefactor 수집 문제:
- ❌ DB 연결 실패 (`JDBCConnectionException`)
- ❌ `latencyCheckAllNode()` 실행 시 DB 연결을 얻을 수 없음
- ❌ 이것이 근본 원인

---

## 🎯 결론

**ActiveStandby 문제 해결 ≠ nodefactor 수집 문제 해결**

두 문제는:
- ✅ **별개의 문제**
- ✅ **별도의 실행 경로**
- ✅ **별도의 원인**

**nodefactor 수집 문제를 해결하려면:**
- DB 연결 문제를 해결해야 함
- `JDBCConnectionException` 원인 파악 필요
- DB 서버 상태, 네트워크, 연결 풀 설정 확인 필요
