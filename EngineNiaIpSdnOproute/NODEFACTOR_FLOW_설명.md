# NodeFactor 데이터 수집 Flow 설명

## 📌 정상적인 Flow (현재 동작 방식)

### Flow 1: 애플리케이션 시작 시 자동 수집 (automeasurement=true일 때)

```
1. 애플리케이션 시작
   ↓
2. StartConfig.run()
   파일: app/src/main/java/com/ipsdn_opt/app/service/StartConfig.java
   함수: run(String... args)
   라인: 34-55
   ↓
   [조건 확인] settings.getAutomeasurement() == true 인지 확인
   ↓
3. Timer로 주기적 실행 시작
   (settings.getMeasurementperiod() * 1000 밀리초마다 반복)
   ↓
4. CollectSvc.latencyCheckAllNode()
   파일: app/src/main/java/com/ipsdn_opt/app/service/CollectSvc.java
   함수: latencyCheckAllNode(LocalDateTime currentDateTime)
   라인: 181-233
   ↓
   [각 노드별로 Thread 생성하여 병렬 처리]
   ↓
5. HTTP GET 요청 전송
   URL: http://[probe_ip]:[probe_port]/ipsdn/opt/probe/linklatency?measured_time=[시간]
   ↓
6. measureController.linkLatency()
   파일: probe/src/main/java/com/ipsdn_opt/probe/controller/measureController.java
   함수: linkLatency(String strMeasuredTime)
   라인: 28-32
   ↓
7. measureSvc.measureFactors()
   파일: probe/src/main/java/com/ipsdn_opt/probe/service/measureSvc.java
   함수: measureFactors(LocalDateTime measuredTime)
   라인: 160-193
   ↓
8. measureSvc.measureNow()
   파일: probe/src/main/java/com/ipsdn_opt/probe/service/measureSvc.java
   함수: measureNow(Probe sendProbe, List<LinkNode> links, LocalDateTime measureDateTime)
   라인: 194-324
   ↓
9. SNMP 명령 실행 (CPU/Memory 측정)
   파일: probe/src/main/java/com/ipsdn_opt/probe/service/measureSvc.java
   함수: measureNow() 내부의 Thread (라인 266-308)
   명령어: snmpwalk -v2c -c sdnkoren [노드IP] [OID]
   - CPU: iso.3.6.1.4.1.36673.100.1.2.5.1.3.1 (라인 263)
   - Memory: iso.3.6.1.4.1.36673.100.1.2.4.1.2.1 (라인 264)
   ↓
   [SNMP 결과 파싱하여 NodeFactor 객체에 값 설정]
   - nf.setCpuusage(fVal) (라인 287)
   - nf.setMemusage(fVal) (라인 291)
   ↓
10. nodeFactorRepository.save(nf)
    파일: probe/src/main/java/com/ipsdn_opt/probe/service/measureSvc.java
    함수: measureNow() 내부
    라인: 318
    ↓
    [JPA를 통해 DB에 INSERT]
    테이블: optroute.nodefactor
    DB: 203.255.249.31:3306/optroute
```

---

## ❌ 문제가 되는 Flow (매일 1시 스케줄러)

### Flow 2: 매일 새벽 1시 스케줄러 (현재 문제점)

```
1. 매일 새벽 1시 자동 실행
   ↓
2. Schedular.calculateDayDataAndOptimizeRoute()
   파일: app/src/main/java/com/ipsdn_opt/app/service/Schedular.java
   함수: calculateDayDataAndOptimizeRoute()
   라인: 25-33
   ↓
   [실행되는 작업들]
   - restSvc.controllerLogin() (라인 26)
   - collectSvc.interfaceUpdateFromIpsdn() (라인 27)
   - collectSvc.linkUpdateFromIpsdn() (라인 28)
   - optimizeRouteSvc.calculateDayData() (라인 30)
   - optimizeRouteSvc.optimizeRoute() (라인 31)
   - collectSvc.checkCollecting() (라인 32) ← 수집 상태만 확인
   ↓
   ⚠️ 문제점: latencyCheckAllNode() 호출이 없음!
   ↓
   [결과] nodefactor 데이터가 수집되지 않음
```

---

## 🔍 문제점 상세 분석

### 문제 1: 스케줄러에서 nodefactor 수집 호출 누락

**현재 코드:**
```java
// Schedular.java 라인 25-33
public void calculateDayDataAndOptimizeRoute() {
    restSvc.setCookies(restSvc.controllerLogin("http://203.255.249.31:8088"));
    collectSvc.interfaceUpdateFromIpsdn();
    collectSvc.linkUpdateFromIpsdn();
    LocalDate optDate = LocalDate.now().minusDays(1);
    optimizeRouteSvc.calculateDayData(optDate);
    optimizeRouteSvc.optimizeRoute(optDate, null, null);
    collectSvc.checkCollecting(optDate);  // ← 수집 상태만 확인, 실제 수집은 안 함
    // ❌ collectSvc.latencyCheckAllNode() 호출이 없음!
}
```

**문제 설명:**
- 매일 1시에 실행되는 스케줄러는 `checkCollecting()`만 호출합니다.
- `checkCollecting()`은 이미 수집된 데이터의 상태를 확인하는 함수입니다.
- 실제로 nodefactor를 수집하는 `latencyCheckAllNode()`는 호출되지 않습니다.

### 문제 2: nodefactor 수집은 다른 경로로만 동작

**현재 nodefactor 수집이 동작하는 유일한 경로:**
- `StartConfig.run()` → `automeasurement=true`일 때만 주기적으로 실행
- 이 방식은 애플리케이션 시작 시 설정된 주기(`measurementperiod`)로 실행됩니다.
- 매일 1시에 고정적으로 실행되지 않습니다.

---

## ✅ 해결 방법

### 방법 1: 스케줄러에 latencyCheckAllNode() 추가 (권장)

**수정할 파일:** `app/src/main/java/com/ipsdn_opt/app/service/Schedular.java`

**수정 내용:**
```java
@Scheduled(cron="0 0 1 * * ?")
public void calculateDayDataAndOptimizeRoute() {
    restSvc.setCookies(restSvc.controllerLogin("http://203.255.249.31:8088"));
    collectSvc.interfaceUpdateFromIpsdn();
    collectSvc.linkUpdateFromIpsdn();
    
    // ✅ 추가: 매일 1시에 nodefactor 수집
    LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    collectSvc.latencyCheckAllNode(currentDateTime);
    
    LocalDate optDate = LocalDate.now().minusDays(1);
    optimizeRouteSvc.calculateDayData(optDate);
    optimizeRouteSvc.optimizeRoute(optDate, null, null);
    collectSvc.checkCollecting(optDate);
}
```

---

## 📋 요약

### 정상 Flow (현재 동작)
1. **StartConfig.run()** → automeasurement=true일 때 주기적 실행
2. **CollectSvc.latencyCheckAllNode()** → 각 노드의 probe API 호출
3. **measureController.linkLatency()** → probe REST API 엔드포인트
4. **measureSvc.measureFactors()** → probe 서비스 호출
5. **measureSvc.measureNow()** → SNMP로 CPU/Memory 측정
6. **nodeFactorRepository.save()** → DB에 INSERT

### 문제점
- **Schedular.calculateDayDataAndOptimizeRoute()**는 매일 1시에 실행되지만
- **latencyCheckAllNode()**를 호출하지 않아서 nodefactor가 수집되지 않음

### 해결책
- 스케줄러에 `collectSvc.latencyCheckAllNode()` 호출 추가
