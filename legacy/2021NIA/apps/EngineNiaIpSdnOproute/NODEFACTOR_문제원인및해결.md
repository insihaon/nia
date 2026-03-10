# NodeFactor 수집 문제 원인 및 해결방법

## 🔍 문제 원인 분석

### 확인된 사실:
1. ✅ `latencyCheckAllNode` 호출 로그 없음 → **호출되지 않고 있음**
2. ✅ Probe API 호출 로그 없음 → **호출되지 않고 있음**
3. ✅ Auto-Collection Error 발생 (6개월마다)
   - 에러: `Could not open JPA EntityManager for transaction; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection`
4. ✅ DB 스키마 에러: `Field 'ActiveStandby' doesn't have a default value`

### 문제 원인:

**핵심 문제: DB 연결 실패로 인해 `latencyCheckAllNode`가 실행되지 않음**

```
StartConfig.run() 실행
  ↓
Timer 시작 (measurementperiod 주기로 실행)
  ↓
collectSvc.latencyCheckAllNode() 호출 시도
  ↓
nodeRepository.findAll() 호출 (DB 연결 필요)
  ↓
❌ DB 연결 실패 (JDBCConnectionException)
  ↓
예외 발생 → catch 블록에서 "Auto-Collection Error" 로그만 남김
  ↓
latencyCheckAllNode 실행 안 됨 → nodefactor 수집 안 됨
```

---

## 🛠️ 해결 방법

### 1. DB 연결 문제 해결 (우선순위 1)

**확인 사항:**
```bash
# DB 연결 테스트
docker exec -it ipsdn_opt_1 ping -c 3 203.255.249.31

# DB 포트 확인
docker exec -it ipsdn_opt_1 telnet 203.255.249.31 3306
# 또는
docker exec -it ipsdn_opt_1 nc -zv 203.255.249.31 3306
```

**가능한 원인:**
- DB 서버 다운
- 네트워크 연결 문제
- DB 연결 풀 고갈
- DB 인증 실패

**해결 방법:**
1. DB 서버 상태 확인
2. DB 연결 설정 확인 (`application-prod.yml`)
3. DB 연결 풀 설정 확인 및 조정

---

### 2. ActiveStandby 필드 문제 해결 (우선순위 2)

**에러 내용:**
```
Field 'ActiveStandby' doesn't have a default value
```

**원인:**
- DB 스키마 변경으로 `ActiveStandby` 필드가 NOT NULL로 설정되었지만 기본값이 없음
- INSERT 시 이 필드 값을 제공하지 않아서 발생

**해결 방법:**

**방법 1: DB 스키마 수정 (권장)**
```sql
-- ActiveStandby 필드에 기본값 설정
ALTER TABLE [테이블명] MODIFY COLUMN ActiveStandby [타입] DEFAULT [기본값];

-- 또는 NULL 허용
ALTER TABLE [테이블명] MODIFY COLUMN ActiveStandby [타입] NULL;
```

**방법 2: 코드에서 기본값 제공**
- 해당 필드를 사용하는 Entity 클래스에서 기본값 설정
- INSERT 시 항상 값을 제공

**어떤 테이블인지 확인:**
```bash
# 에러 로그에서 더 많은 정보 확인
docker logs ipsdn_opt_1 2>&1 | grep -B 10 -A 10 "ActiveStandby" | tail -30
```

---

### 3. 에러 로깅 개선 (디버깅용)

**현재 문제:**
- `StartConfig`의 catch 블록에서 에러 메시지만 로그로 남기고 있음
- 전체 스택 트레이스가 없어서 정확한 원인 파악이 어려움

**개선 방법:**

```java
// StartConfig.java 수정
catch(Exception e) {
    log.error("Auto-Collection Error. => " + e.getMessage(), e); // 전체 스택 트레이스 추가
    e.printStackTrace(); // 추가 디버깅 정보
}
```

---

### 4. DB 연결 재시도 로직 추가 (선택사항)

**현재 문제:**
- DB 연결 실패 시 즉시 실패
- 재시도 로직이 없음

**개선 방법:**
- DB 연결 재시도 로직 추가
- 연결 실패 시 일정 시간 후 재시도

---

## 📋 즉시 확인할 사항

### 1. DB 서버 상태 확인
```bash
# DB 서버가 살아있는지 확인
ping 203.255.249.31

# DB 포트가 열려있는지 확인
telnet 203.255.249.31 3306
```

### 2. DB 연결 설정 확인
```bash
# application-prod.yml 확인
docker exec -it ipsdn_opt_1 cat /app/config/application-prod.yml
# 또는
cat app/src/main/resources/application-prod.yml
```

### 3. ActiveStandby 필드가 있는 테이블 확인
```sql
-- DB에서 ActiveStandby 필드가 있는 테이블 찾기
SELECT TABLE_NAME, COLUMN_NAME, IS_NULLABLE, COLUMN_DEFAULT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE COLUMN_NAME = 'ActiveStandby';
```

### 4. 최근 DB 연결 에러 상세 확인
```bash
# 더 자세한 에러 로그 확인
docker logs ipsdn_opt_1 2>&1 | grep -B 20 -A 20 "JDBCConnectionException\|Unable to acquire JDBC Connection" | tail -50
```

---

## 🎯 해결 순서

1. **DB 연결 문제 해결** (가장 중요)
   - DB 서버 상태 확인
   - 네트워크 연결 확인
   - DB 연결 설정 확인

2. **ActiveStandby 필드 문제 해결**
   - 어떤 테이블인지 확인
   - 스키마 수정 또는 코드 수정

3. **에러 로깅 개선**
   - 전체 스택 트레이스 로깅 추가
   - 더 자세한 디버깅 정보 수집

4. **재시도 로직 추가** (선택사항)
   - DB 연결 실패 시 재시도

---

## 📝 다음 단계

위 확인 사항들을 실행한 후 결과를 공유해주시면:
1. 정확한 문제 지점 파악
2. 구체적인 해결 방법 제시

가능합니다.
