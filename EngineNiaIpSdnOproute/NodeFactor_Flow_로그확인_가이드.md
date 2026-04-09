# NodeFactor Flow 로그 확인 가이드

## 📋 추가된 로그 태그

모든 로그는 `[NodeFactor Flow]` 태그로 시작합니다.

---

## 🔍 Docker에서 로그 확인 방법

### 1. 전체 Flow 로그 확인 (App 서버)

```bash
# 전체 NodeFactor Flow 로그 확인
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\]"

# 최근 100줄에서 확인
docker logs ipsdn_opt_1 2>&1 | tail -100 | grep "\[NodeFactor Flow\]"

# 실시간 모니터링
docker logs -f ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\]"
```

### 2. 단계별 로그 확인

#### Step 1: StartConfig 실행
```bash
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*StartConfig"
```

#### Step 2: Settings 확인
```bash
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*Settings\|automeasurement"
```

#### Step 3: latencyCheckAllNode 호출
```bash
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*latencyCheckAllNode"
```

#### Step 4: HTTP 요청
```bash
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*HTTP 요청"
```

### 3. Probe 서버 로그 확인

```bash
# Probe 컨테이너 이름 확인 필요 (예: ipsdn_probe_1)
docker logs [probe_container_name] 2>&1 | grep "\[NodeFactor Flow\]"

# measureController 호출 확인
docker logs [probe_container_name] 2>&1 | grep "\[NodeFactor Flow\].*linkLatency"

# measureSvc 실행 확인
docker logs [probe_container_name] 2>&1 | grep "\[NodeFactor Flow\].*measureSvc"

# SNMP 명령 실행 확인
docker logs [probe_container_name] 2>&1 | grep "\[NodeFactor Flow\].*SNMP"

# DB 저장 확인
docker logs [probe_container_name] 2>&1 | grep "\[NodeFactor Flow\].*DB"
```

---

## 📊 Flow 단계별 로그 예시

### Step 1: 애플리케이션 시작
```
[NodeFactor Flow] StartConfig.run() 시작
[NodeFactor Flow] Settings 조회 완료 - automeasurement: true, measurementperiod: 300초
[NodeFactor Flow] automeasurement=true 확인, Timer 시작 (주기: 300초)
```

### Step 2: Timer 실행
```
[NodeFactor Flow] Timer 실행 - latencyCheckAllNode() 호출 시작 (시간: 2025-01-01T12:00:00)
[NodeFactor Flow] CollectSvc.latencyCheckAllNode() 시작 - 측정시간: 2025-01-01T12:00:00
[NodeFactor Flow] 노드 조회 완료 - 총 10개 노드
```

### Step 3: HTTP 요청
```
[NodeFactor Flow] HTTP 요청 전송 - Node: Node-01, URL: http://192.168.1.10:8080/ipsdn/opt/probe/linklatency?measured_time=2025-01-01T12:00:00
[NodeFactor Flow] HTTP 요청 성공 - Node: Node-01, Status: true
```

### Step 4: Probe 측 처리
```
[NodeFactor Flow] measureController.linkLatency() 호출 - measured_time: 2025-01-01T12:00:00
[NodeFactor Flow] measureSvc.measureFactors() 시작 - 측정시간: 2025-01-01T12:00:00
[NodeFactor Flow] Probe 조회 완료 - Probe ID: 1, Node ID: 1
[NodeFactor Flow] LinkNode 조회 완료 - 총 5개 링크
[NodeFactor Flow] measureSvc.measureNow() 호출 시작
[NodeFactor Flow] measureSvc.measureNow() 시작 - Probe ID: 1, 측정시간: 2025-01-01T12:00:00, 링크 수: 5
[NodeFactor Flow] CommandServer 설정 완료 - Server: 192.168.1.100
[NodeFactor Flow] Node 정보 조회 완료 - Node: Node-01, MgmtAddr: 192.168.1.1
```

### Step 5: SNMP 실행
```
[NodeFactor Flow] SNMP 측정 시작 - Node: Node-01, OID: CPU=iso.3.6.1.4.1.36673.100.1.2.5.1.3.1, Memory=iso.3.6.1.4.1.36673.100.1.2.4.1.2.1
[NodeFactor Flow] SNMP 명령 실행 - Node: Node-01, Factor: cpu, Command: snmpwalk -v2c -c sdnkoren 192.168.1.1 iso.3.6.1.4.1.36673.100.1.2.5.1.3.1
[NodeFactor Flow] CPU 값 파싱 완료 - Node: Node-01, 원본값: 50000, 변환값: 5.0%
[NodeFactor Flow] SNMP 명령 실행 - Node: Node-01, Factor: memory, Command: snmpwalk -v2c -c sdnkoren 192.168.1.1 iso.3.6.1.4.1.36673.100.1.2.4.1.2.1
[NodeFactor Flow] Memory 값 파싱 완료 - Node: Node-01, 원본값: 6000, 변환값: 60.0%
```

### Step 6: DB 저장
```
[NodeFactor Flow] NodeFactor 객체 생성 - Node ID: 1, 측정시간: 2025-01-01T12:00:00
[NodeFactor Flow] DB 저장 시작 - NodeFactor: CPU=5.0%, Memory=60.0%
[NodeFactor Flow] DB INSERT 완료 - NodeFactor 저장 성공 (Node ID: 1, 측정시간: 2025-01-01T12:00:00)
```

---

## 🎯 문제 진단용 로그 확인

### 1. StartConfig가 실행되지 않는 경우
```bash
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*StartConfig" | head -5
```
**예상 결과:** 로그가 없으면 StartConfig가 실행되지 않음

### 2. Timer가 동작하지 않는 경우
```bash
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*Timer 실행" | tail -10
```
**예상 결과:** 로그가 없으면 Timer가 동작하지 않음

### 3. HTTP 요청 실패
```bash
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*HTTP 요청.*실패\|예외"
```
**예상 결과:** HTTP 요청 실패 원인 확인

### 4. SNMP 명령 실패
```bash
docker logs [probe_container] 2>&1 | grep "\[NodeFactor Flow\].*SNMP.*실패\|예외"
```
**예상 결과:** SNMP 명령 실패 원인 확인

### 5. DB 저장 실패
```bash
docker logs [probe_container] 2>&1 | grep "\[NodeFactor Flow\].*DB.*실패\|예외"
```
**예상 결과:** DB 저장 실패 원인 확인

---

## 📝 종합 확인 스크립트

```bash
#!/bin/bash
echo "=== NodeFactor Flow 로그 확인 ==="
echo ""

echo "1. App 서버 - StartConfig 실행:"
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*StartConfig" | tail -5
echo ""

echo "2. App 서버 - latencyCheckAllNode 호출:"
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*latencyCheckAllNode" | tail -10
echo ""

echo "3. App 서버 - HTTP 요청:"
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\].*HTTP 요청" | tail -10
echo ""

echo "4. Probe 서버 - SNMP 실행:"
docker logs [probe_container] 2>&1 | grep "\[NodeFactor Flow\].*SNMP" | tail -10
echo ""

echo "5. Probe 서버 - DB 저장:"
docker logs [probe_container] 2>&1 | grep "\[NodeFactor Flow\].*DB" | tail -10
echo ""

echo "6. 최근 전체 Flow 로그 (App 서버):"
docker logs ipsdn_opt_1 2>&1 | grep "\[NodeFactor Flow\]" | tail -20
```

---

## ⚠️ 주의사항

1. **Probe 컨테이너 이름 확인 필요**
   - 실제 probe 컨테이너 이름으로 변경해야 함
   - 예: `ipsdn_probe_1`, `probe_1` 등

2. **로그 레벨 확인**
   - `log.debug()`는 기본적으로 출력되지 않을 수 있음
   - 필요시 로그 레벨을 DEBUG로 변경

3. **로그 볼륨**
   - 로그가 많을 경우 `tail -n` 옵션 사용 권장
