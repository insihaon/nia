# NodeFactor 수집 문제 재분석

## 🔍 지금까지 확인한 사실

### 확인된 사항:
1. ✅ `latencyCheckAllNode` 호출 로그 **없음**
2. ✅ Probe API 호출 로그 **없음**
3. ⚠️ Auto-Collection Error가 **6개월마다** 발생
   - 에러: `JDBCConnectionException: Unable to acquire JDBC Connection`
4. ✅ Settings 테이블: `automeasurement = 1` (정상)
5. ✅ DB 연결: **간헐적으로** 실패 (가끔가끔 안 됨)

---

## 🤔 문제 분석

### 핵심 질문: "nodeFactor의 오류라고 확실히 할만한 부분이 있었나?"

**답: 아니요. 확실한 증거는 없습니다.**

### 왜 확실하지 않은가?

1. **latencyCheckAllNode 호출 로그가 없다는 것의 의미:**
   - 호출이 안 되었다는 의미일 수도 있고
   - 호출은 되었지만 로그가 남지 않았다는 의미일 수도 있음
   - 에러가 발생해서 catch 블록에서만 로그가 남았을 수도 있음

2. **Auto-Collection Error가 6개월마다 발생:**
   - Timer는 동작하고 있다는 의미
   - 하지만 DB 연결 실패로 실행이 안 됨
   - **6개월마다**라는 것은 대부분의 경우는 정상 동작했다는 의미일 수도

3. **DB 연결이 간헐적으로 실패:**
   - 대부분의 경우는 정상 동작할 수 있음
   - 가끔 실패하는 것은 정상일 수도 있음

---

## 📊 가능한 시나리오

### 시나리오 1: 정상 동작 중 (가능성 높음)
- Timer는 정상 동작
- 대부분의 경우 DB 연결 성공
- `latencyCheckAllNode` 정상 실행
- nodefactor 정상 수집
- **단지 로그가 남지 않았을 뿐**

**확인 방법:**
```sql
-- 최근 nodefactor 데이터 확인
SELECT * FROM nodefactor 
ORDER BY measureddatetime DESC 
LIMIT 20;
```

### 시나리오 2: 간헐적 실패 (가능성 중간)
- Timer는 정상 동작
- 대부분의 경우 정상 동작
- 가끔 DB 연결 실패로 수집 안 됨
- 6개월마다 에러 발생 = 가끔 실패하는 것

**확인 방법:**
```sql
-- 최근 6개월간 nodefactor 데이터 확인
SELECT DATE(measureddatetime) as date, COUNT(*) as count
FROM nodefactor 
WHERE measureddatetime >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
GROUP BY DATE(measureddatetime)
ORDER BY date DESC;
```

### 시나리오 3: 완전 실패 (가능성 낮음)
- Timer는 동작하지만
- DB 연결이 항상 실패
- nodefactor 수집이 전혀 안 됨

**확인 방법:**
```sql
-- 12월 24일 이후 데이터 확인
SELECT * FROM nodefactor 
WHERE measureddatetime >= '2024-12-25'
ORDER BY measureddatetime DESC;
```

---

## ✅ 실질적으로 확인해야 할 것

### 1. DB에서 실제 데이터 확인 (가장 중요!)

```sql
-- 최근 nodefactor 데이터 확인
SELECT * FROM nodefactor 
ORDER BY measureddatetime DESC 
LIMIT 20;

-- 12월 24일 이후 데이터 개수 확인
SELECT COUNT(*) 
FROM nodefactor 
WHERE measureddatetime >= '2024-12-25';

-- 일별 수집 개수 확인
SELECT DATE(measureddatetime) as date, COUNT(*) as count
FROM nodefactor 
WHERE measureddatetime >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
GROUP BY DATE(measureddatetime)
ORDER BY date DESC;
```

**이것이 가장 확실한 증거입니다!**

### 2. 로그 레벨 확인

현재 로그 레벨이 INFO인데, `latencyCheckAllNode`가 성공하면 로그가 남지 않을 수 있음.

**확인 방법:**
```bash
# 성공 로그가 있는지 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "All Node Latency Measuring Complete" | tail -20
```

### 3. StartConfig 실행 여부 확인

```bash
# StartConfig가 실행되었는지 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "StartConfig" | head -10
```

---

## 🎯 결론

### "실질적으로 보이는 문제는 없는 거지?"

**답: DB에서 실제 데이터를 확인해야 확실합니다.**

**확인해야 할 것:**
1. ✅ **DB에서 최근 nodefactor 데이터 확인** (가장 중요!)
2. ✅ 일별 수집 개수 확인
3. ✅ 12월 24일 이후 데이터 확인

**만약 DB에 데이터가 있다면:**
- ✅ 정상 동작 중
- ✅ 로그만 안 남았을 뿐
- ✅ 문제 없음

**만약 DB에 데이터가 없다면:**
- ❌ 실제로 수집이 안 되고 있음
- ❌ DB 연결 문제 또는 다른 문제
- ❌ 추가 조사 필요

---

## 📝 다음 단계

**가장 먼저 해야 할 것:**
```sql
-- 이것만 확인하면 됩니다!
SELECT * FROM nodefactor 
ORDER BY measureddatetime DESC 
LIMIT 20;
```

**결과에 따라:**
- 데이터가 있으면 → 정상 동작 중 (문제 없음)
- 데이터가 없으면 → 실제 문제 (추가 조사 필요)
