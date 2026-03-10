# NodeFactor 수집 문제 추가 진단

## 🔍 현재 상황 분석

### 확인된 사항:
1. ✅ Spring Profile이 prod가 아니지만, 2022년부터 정상 동작했다고 함
2. ❌ `linktraffic` API 호출 실패 로그 발견 (하지만 이것은 다른 API)

### 중요: `linktraffic`과 `latencyCheckAllNode`는 다른 API입니다!

- **`linktraffic`**: `Schedular.trafficRead()` → `CollectSvc.requestLinkTraffic()` → `http://203.255.249.31:8088/ipsdn/services/stat/linktraffic`
- **`latencyCheckAllNode`**: `StartConfig` → `CollectSvc.latencyCheckAllNode()` → `http://[probe_ip]:[probe_port]/ipsdn/opt/probe/linklatency`

---

## 📋 다음 단계: nodefactor 수집 관련 로그 확인

다음 명령어들을 실행해서 결과를 확인해주세요:

### 1. latencyCheckAllNode 호출 여부 확인
```bash
docker logs ipsdn_opt_1 2>&1 | grep -i "latencyCheckAllNode\|All Node Latency Measuring" | tail -20
```

### 2. Probe API 호출 성공/실패 확인
```bash
docker logs ipsdn_opt_1 2>&1 | grep -i "probe.*Call\|linklatency" | tail -20
```

### 3. NodeFactor 측정 완료 로그 확인
```bash
docker logs ipsdn_opt_1 2>&1 | grep -i "Node.*Measuring.*complete\|nodefactor" | tail -20
```

### 4. Auto-Collection 에러 확인
```bash
docker logs ipsdn_opt_1 2>&1 | grep -i "Auto-Collection Error" | tail -20
```

### 5. 최근 전체 로그 확인 (시간대 확인)
```bash
docker logs ipsdn_opt_1 2>&1 | tail -100
```

---

## 🔍 예상 문제 시나리오

### 시나리오 1: StartConfig가 실행되지 않음
**증상:** 
- `latencyCheckAllNode` 관련 로그가 전혀 없음
- `Auto-Collection Error` 로그도 없음

**원인:**
- Spring Profile 문제 (하지만 2022년부터 동작했다고 하니 아닐 수도)
- Settings 테이블 조회 실패
- `automeasurement` 값이 false로 변경됨

**확인 방법:**
```bash
# StartConfig 실행 여부 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "StartConfig\|automeasurement" | head -20
```

---

### 시나리오 2: latencyCheckAllNode는 호출되지만 Probe API 호출 실패
**증상:**
- `All Node Latency Measuring Complete` 로그는 있음
- `API(Latency Check) Call Fail` 로그가 있음

**원인:**
- Probe 서버가 실행되지 않음
- Probe IP/Port가 잘못됨
- 네트워크 연결 문제

**확인 방법:**
```bash
# Probe API 호출 실패 로그 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "API.*Call Fail" | grep -i "latency\|probe" | tail -20

# Probe 서버 상태 확인 (각 probe IP로)
# 예: curl http://[probe_ip]:[probe_port]/ipsdn/opt/probe/linklatency?measured_time=2025-01-01T00:00:00
```

---

### 시나리오 3: Probe API는 성공하지만 DB 저장 실패
**증상:**
- `API(Latency Check) Call Complete` 로그는 있음
- 하지만 DB에 데이터가 없음

**원인:**
- Probe 서버에서 DB 저장 실패
- Probe 서버의 DB 연결 문제

**확인 방법:**
- Probe 서버 로그 확인 필요
- Probe 서버의 DB 연결 설정 확인

---

### 시나리오 4: Timer가 실행되지 않음
**증상:**
- StartConfig는 실행되었지만
- 주기적으로 `latencyCheckAllNode`가 호출되지 않음

**원인:**
- `measurementperiod` 값이 너무 큼
- Timer 예외 발생 후 로그만 남기고 계속 실행 안 됨

**확인 방법:**
```bash
# measurementperiod 값 확인
# DB에서: SELECT measurementperiod FROM settings;

# Auto-Collection Error 로그 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "Auto-Collection Error" | tail -20
```

---

## 🛠️ 종합 진단 스크립트

다음 스크립트를 실행해서 결과를 확인해주세요:

```bash
#!/bin/bash
echo "=== NodeFactor 수집 종합 진단 ==="
echo ""

echo "1. StartConfig 실행 여부:"
docker logs ipsdn_opt_1 2>&1 | grep -i "StartConfig\|automeasurement" | head -5
echo ""

echo "2. latencyCheckAllNode 호출 여부:"
docker logs ipsdn_opt_1 2>&1 | grep -i "latencyCheckAllNode\|All Node Latency Measuring" | tail -10
echo ""

echo "3. Probe API 호출 성공:"
docker logs ipsdn_opt_1 2>&1 | grep -i "API.*Call Complete" | grep -i "latency\|probe" | tail -10
echo ""

echo "4. Probe API 호출 실패:"
docker logs ipsdn_opt_1 2>&1 | grep -i "API.*Call Fail" | grep -i "latency\|probe\|Node" | tail -10
echo ""

echo "5. Auto-Collection 에러:"
docker logs ipsdn_opt_1 2>&1 | grep -i "Auto-Collection Error" | tail -10
echo ""

echo "6. 최근 로그 (시간대 확인):"
docker logs ipsdn_opt_1 2>&1 | tail -30
```

---

## 📝 다음 단계

위 스크립트 실행 결과를 공유해주시면:
1. 정확한 문제 지점 파악
2. 구체적인 해결 방법 제시

가능합니다.
