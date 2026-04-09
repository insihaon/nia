# NodeFactor 수집 문제 진단 가이드

## 🔍 체크리스트 (순서대로 확인)

### 1. Spring Profile 확인 (가장 중요!)

**문제:** `StartConfig`는 `@Profile("prod")`로 설정되어 있어서, **반드시 `spring.profiles.active=prod`로 실행**되어야 합니다.

**확인 방법:**
```bash
# Docker 컨테이너에서 확인
docker exec -it ipsdn_opt_1 ps aux | grep java
# 또는
docker exec -it ipsdn_opt_1 env | grep SPRING_PROFILES_ACTIVE
```

**확인해야 할 내용:**
- JVM 옵션에 `-Dspring.profiles.active=prod`가 있는지
- 환경변수에 `SPRING_PROFILES_ACTIVE=prod`가 있는지

**Docker 실행 스크립트 확인:**
```bash
# entrypoint.sh 확인
docker exec -it ipsdn_opt_1 cat /root/ipsdn_opt/entrypoint.sh
# 또는
cat docker_build_cp/app/opr/entrypoint.sh
```

**예상 문제:**
- Profile이 `dev`로 설정되어 있으면 `StartConfig`가 로드되지 않음
- Profile이 설정되지 않으면 기본 profile로 실행되어 `StartConfig`가 로드되지 않음

---

### 2. 애플리케이션 로그 확인

**StartConfig 실행 여부 확인:**
```bash
# StartConfig가 실행되었는지 로그 확인
docker logs ipsdn_opt_1 | grep -i "StartConfig"
docker logs ipsdn_opt_1 | grep -i "automeasurement"
docker logs ipsdn_opt_1 | grep -i "Auto-Collection"
```

**예상 로그:**
- `StartConfig.run()` 실행 시 로그가 없을 수 있음 (에러가 없으면 로그 없음)
- `Auto-Collection Error` 로그가 있으면 에러 발생

**latencyCheckAllNode 호출 여부 확인:**
```bash
docker logs ipsdn_opt_1 | grep -i "latencyCheckAllNode"
docker logs ipsdn_opt_1 | grep -i "All Node Latency Measuring"
```

---

### 3. Settings 테이블 값 확인

**DB에서 직접 확인:**
```sql
SELECT id, automeasurement, measurementperiod FROM settings;
```

**확인 사항:**
- `automeasurement` = 1 (true) ✅
- `measurementperiod` 값이 적절한지 (초 단위)
  - 예: 60 = 1분마다, 3600 = 1시간마다
  - 값이 너무 크면 수집 주기가 길어짐

**주의:**
- `measurementperiod`가 NULL이면 에러 발생 가능
- `measurementperiod`가 0이면 즉시 실행 후 종료

---

### 4. Probe 서버 연결 확인

**latencyCheckAllNode에서 probe API 호출 실패 여부 확인:**
```bash
# 에러 로그 확인
docker logs ipsdn_opt_1 | grep -i "API.*Call Fail"
docker logs ipsdn_opt_1 | grep -i "Connection refused"
docker logs ipsdn_opt_1 | grep -i "timeout"
```

**각 노드의 probe 정보 확인:**
```sql
-- probe 테이블 확인
SELECT id, node_id, ipaddr, port FROM probe;
```

**확인 사항:**
- probe 테이블에 데이터가 있는지
- 각 노드에 probe가 매핑되어 있는지
- probe IP/Port가 올바른지

---

### 5. 수동 테스트

**latencyCheckAllNode 수동 호출:**
```bash
# 컨테이너 내부에서 curl로 API 호출
docker exec -it ipsdn_opt_1 curl "http://localhost:8080/ipsdn/opt/latency/allnode"
```

**또는 애플리케이션 로그 레벨을 DEBUG로 변경:**
```yaml
# application-prod.yml에 추가
logging:
  level:
    com.ipsdn_opt.app.service.StartConfig: DEBUG
    com.ipsdn_opt.app.service.CollectSvc: DEBUG
```

---

### 6. Timer 동작 확인

**코드 분석:**
```java
// StartConfig.java 라인 37-53
Timer jobScheduler = new Timer();
jobScheduler.scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            collectSvc.latencyCheckAllNode(currentDateTime);
        }
        catch(Exception e) {
            log.info("Auto-Collection Error. => " + e.getMessage());
        }
    }
}, 0, settings.getMeasurementperiod()*1000);
```

**확인 사항:**
- Timer가 생성되었는지 (에러 로그 확인)
- `measurementperiod * 1000` 값이 올바른지
- 예외가 발생해도 로그만 남기고 계속 실행되는지

---

## 🛠️ 빠른 진단 스크립트

```bash
#!/bin/bash
echo "=== NodeFactor 수집 문제 진단 ==="

echo "1. Spring Profile 확인:"
docker exec ipsdn_opt_1 ps aux | grep -i "spring.profiles.active"

echo "2. StartConfig 로그 확인:"
docker logs ipsdn_opt_1 --tail 100 | grep -i "StartConfig\|automeasurement"

echo "3. Auto-Collection 에러 확인:"
docker logs ipsdn_opt_1 --tail 500 | grep -i "Auto-Collection Error"

echo "4. latencyCheckAllNode 호출 확인:"
docker logs ipsdn_opt_1 --tail 500 | grep -i "latencyCheckAllNode\|All Node Latency"

echo "5. Probe API 호출 실패 확인:"
docker logs ipsdn_opt_1 --tail 500 | grep -i "API.*Call Fail\|Connection refused\|timeout"

echo "6. 최근 nodefactor 데이터 확인:"
echo "SELECT * FROM nodefactor ORDER BY measureddatetime DESC LIMIT 5;" | mysql -h 203.255.249.31 -P 3306 -u optroute -p optroute
```

---

## 🔧 예상 문제 및 해결책

### 문제 1: Spring Profile이 prod가 아님
**증상:** StartConfig가 실행되지 않음
**해결:** Docker 실행 시 `-Dspring.profiles.active=prod` 추가

### 문제 2: measurementperiod 값이 너무 큼
**증상:** 수집 주기가 너무 길어서 데이터가 안 보임
**해결:** DB에서 `measurementperiod` 값 확인 및 조정

### 문제 3: Probe 서버 연결 실패
**증상:** "API Call Fail" 로그 발생
**해결:** 
- Probe 서버가 실행 중인지 확인
- Probe IP/Port 확인
- 네트워크 연결 확인

### 문제 4: Settings 테이블에 데이터 없음
**증상:** `settingsRepository.findAll().get(0)` 에러
**해결:** Settings 테이블에 데이터 추가

### 문제 5: 예외 발생 후 로그만 남기고 계속 실행
**증상:** "Auto-Collection Error" 로그는 있지만 수집 안 됨
**해결:** 에러 로그 확인하여 근본 원인 해결

---

## 📝 로그 추가 권장사항

**StartConfig.java 수정 (디버깅용):**
```java
@Override
public void run(String... args) {
    log.info("StartConfig.run() 실행 시작");
    Settings settings = settingsRepository.findAll().get(0);
    log.info("Settings 조회 완료 - automeasurement: {}, measurementperiod: {}", 
        settings.getAutomeasurement(), settings.getMeasurementperiod());
    
    if(settings.getAutomeasurement()) {
        log.info("Auto-measurement 활성화됨. Timer 시작 - 주기: {}초", 
            settings.getMeasurementperiod());
        Timer jobScheduler = new Timer();
        // ... 나머지 코드
    } else {
        log.info("Auto-measurement 비활성화됨");
    }
}
```
