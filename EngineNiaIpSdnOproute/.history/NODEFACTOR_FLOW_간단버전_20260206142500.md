# NodeFactor 수집 Flow (간단 버전)

## ✅ 정상 Flow (현재 동작)

```
[애플리케이션 시작]
    ↓
StartConfig.run()
    ↓
[automeasurement=true 확인]
    ↓
CollectSvc.latencyCheckAllNode()
    ↓
[각 노드별 HTTP 요청]
http://probe_ip:port/ipsdn/opt/probe/linklatency
    ↓
measureController.linkLatency()
    ↓
measureSvc.measureFactors()
    ↓
measureSvc.measureNow()
    ↓
[SNMP 명령 실행]
snmpwalk -v2c -c sdnkoren [노드IP] [OID]
    ↓
[CPU/Memory 값 파싱]
nf.setCpuusage()
nf.setMemusage()
    ↓
nodeFactorRepository.save(nf)
    ↓
[DB INSERT 완료]
optroute.nodefactor 테이블에 저장
```

---

## ❌ 문제 Flow (매일 1시 스케줄러)

```
[매일 새벽 1시]
    ↓
Schedular.calculateDayDataAndOptimizeRoute()
    ↓
[실행되는 작업들]
- interfaceUpdateFromIpsdn()
- linkUpdateFromIpsdn()
- calculateDayData()
- optimizeRoute()
- checkCollecting() ← 수집 상태만 확인
    ↓
❌ latencyCheckAllNode() 호출 없음!
    ↓
[결과] nodefactor 데이터 수집 안 됨
```

---

## 📝 파일 위치 정리

| 단계 | 파일명 | 함수명 | 라인 |
|------|--------|--------|------|
| 1. 시작 | `StartConfig.java` | `run()` | 34-55 |
| 2. 수집 호출 | `CollectSvc.java` | `latencyCheckAllNode()` | 181-233 |
| 3. API 엔드포인트 | `measureController.java` | `linkLatency()` | 28-32 |
| 4. 서비스 호출 | `measureSvc.java` | `measureFactors()` | 160-193 |
| 5. 실제 측정 | `measureSvc.java` | `measureNow()` | 194-324 |
| 6. SNMP 실행 | `measureSvc.java` | `measureNow()` 내부 Thread | 266-308 |
| 7. DB 저장 | `measureSvc.java` | `nodeFactorRepository.save()` | 318 |

---

## 🔧 해결 방법

**수정 파일:** `Schedular.java`

**추가할 코드:**
```java
// 라인 29 다음에 추가
LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
collectSvc.latencyCheckAllNode(currentDateTime);
```
