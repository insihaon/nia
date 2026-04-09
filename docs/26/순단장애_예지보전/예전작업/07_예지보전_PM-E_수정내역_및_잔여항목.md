# 예지보전 PM-E 수정 내역 및 잔여 미해결 항목

> 적용일: 2026-04-03
> 근거: 06_미해결_잔여항목.md의 PM-E1~E3 (DB 확인 결과 UI/SQL 수정만으로 해결 가능)

---

## 수정 요약

| # | ID | 파일 | 수정 내용 | 상태 |
|---|-----|------|----------|:----:|
| 1 | PM-E1 | Map2D.vue | SELECT_MBA_TOPOLOGY_LIST → SELECT_PMM_TOPOLOGY_LIST 교체 | 수정 완료 |
| 2 | PM-E2 | transmissionFaultAnalytics.xml | TIME_STAMP 파라미터 ocrtime 필터 추가 | 수정 완료 |
| 3 | PM-E3 | (수정 불필요) | SQL이 이미 _daily JOIN으로 NTD 반환 — PM-E1 해결 시 자동 해결 | DB 검증 완료 |

---

## 수정 상세

### 1. PM-E1: Map2D 토폴로지 SQL 교체

```
파일: apps/Web2024/FE/src/views-nia/pages/PredictiveMaintenance/component/Map2D.vue:159
```

```javascript
// Before — MBA 토폴로지 SQL (3개 컬럼: TRUNK_NAME, SYSNAME, ROUTENUM)
const result2 = await apiRcaRequest('SELECT_MBA_TOPOLOGY_LIST', { TRUNK_NAME: ticket.trunk_name || '' })

// After — PM 토폴로지 SQL (RX_TX, SPAN_GAIN, NODE_TOTAL_DEVIATION 포함)
const result2 = await apiRcaRequest('SELECT_PMM_TOPOLOGY_LIST', { TRUNK_NAME: ticket.trunk_name || '', DIRECTION: ticket.direction || '' })
```

**이유**: PM 화면의 Map2D 컴포넌트가 MBA 토폴로지 SQL을 호출하고 있었음. `SELECT_MBA_TOPOLOGY_LIST`는 기본 토폴로지 구조(sysname, routenum)만 반환하여 PM에 필요한 RX/TX, Span/Gain, NTD 값이 누락됨.

`ticket.direction`은 Map2D의 props에서 이미 사용 가능 (line 155에서 `DIRECTION: ticket.direction`으로 사용 중).

**복구되는 기능**: PM 토폴로지 맵에서 노드 정보 표시 (RX/TX, Span/Gain, NTD)

---

### 2. PM-E2: TIME_STAMP 파라미터 필터 추가

```
파일: apps/Web2024/BE/app-nia/src/main/resources/mapper/db2nd/transmissionFaultAnalytics.xml:646~
```

```xml
<!-- Before — TIME_STAMP 무시 -->
WHERE oph.ocrtime > RCA.FC_GET_MINUS_DAY(1)
AND oph.trunk_name = #{TRUNK_NAME}
AND oph.direction  = #{DIRECTION}

<!-- After — TIME_STAMP가 있으면 해당 시점까지만 조회 -->
WHERE oph.ocrtime > RCA.FC_GET_MINUS_DAY(1)
AND oph.trunk_name = #{TRUNK_NAME}
AND oph.direction  = #{DIRECTION}
<if test="TIME_STAMP != null and TIME_STAMP != ''">
    AND oph.ocrtime <= #{TIME_STAMP}::TIMESTAMP
</if>
```

**이유**: Vue(PredictiveMaintenanceTicket.vue:245)에서 `TIME_STAMP` 파라미터를 전달하지만 SQL이 이를 무시하고 최근 1일 전체 데이터의 MAX(ocrtime)을 반환. 특정 시점의 성능 상태를 보려면 ocrtime 상한 필터가 필요.

**DB 확인**: `tb_roadm_optical_performance_hist`에 `time_stamp` 컬럼은 없으나, `ocrtime` (timestamp with time zone)이 수집 시점. 총 5,551,464건, 최신 2026-04-03 04:45:00.

**동작 방식**:
- TIME_STAMP가 NULL이면 기존과 동일 (최근 1일의 최신 데이터)
- TIME_STAMP가 있으면 해당 시점 이전의 최신 데이터 반환

**복구되는 기능**: 시점별 PM 성능 데이터 조회

---

### 3. PM-E3: node_total_deviation — 수정 불필요 (DB 검증 완료)

**원래 우려**: `SELECT_PMM_TOPOLOGY_LIST`가 `node_total_deviation`을 반환하지 않아 Vue에서 NaN 발생

**DB 검증 결과**: SQL이 이미 `mba.tb_roadm_optical_performance_daily` 테이블을 LEFT JOIN하여 `node_total_deviation`을 정상 반환하고 있었음.

```sql
-- transmissionFaultAnalytics.xml:670~678
latest_daily AS (
    SELECT d.*, ROW_NUMBER() OVER (...) AS rn
    FROM mba.tb_roadm_optical_performance_daily d
    WHERE d.trunk_name = #{TRUNK_NAME} AND d.direction = #{DIRECTION} AND d.in_out = 'IN'
)
-- line 695
COALESCE(ROUND(MAX(ld.node_total_deviation)::numeric, 1), 0) AS NODE_TOTAL_DEVIATION
```

**실제 쿼리 실행 결과** (대전ETRI--부산부경대, DOWN):

| sysname | routenum | node_total_deviation |
|---------|:--------:|:--------------------:|
| 대전ETRI | 0 | 0 |
| 중계_영동중_0801B | 1 | 2.5 |
| 부산부경대 | 10 | 0 |

`daily` 테이블: 46,804건, 최신 2026-04-02. NTD 값이 정상적으로 계산됨.

**실제 원인**: PM-E1에서 Map2D가 `SELECT_MBA_TOPOLOGY_LIST`(NTD 미포함)를 호출하고 있었기 때문에 NTD가 누락된 것. PM-E1 수정으로 자동 해결.

---

## 수정 파일 목록

```
apps/Web2024/FE/src/views-nia/pages/PredictiveMaintenance/component/Map2D.vue
apps/Web2024/BE/app-nia/src/main/resources/mapper/db2nd/transmissionFaultAnalytics.xml
```

---

# 잔여 미해결 항목 (엔진/DB 작업 필요)

> 06_미해결_잔여항목.md 기준, PM-E1~E3 해결 후 남은 5건

## 요약

| 구분 | 건수 | 비고 |
|------|:----:|------|
| 순단장애 (MBA) | ~~5건~~ → **4건** | MBA-E1 해결, MBA-E2~E5 잔여 |
| 예지보전 (PM) | 0건 | 전부 해결 |
| **합계** | **4건** | |

---

## MBA-E1. 반복기 슬롯 필터링 (SELECT_MBA_REPEATER_SLOT_DATA)

**현상**: 토폴로지 노드/링크 클릭 시 경보 필터링 미작동
**차단 기능**: 순단장애 토폴로지 경보 필터링

### 문제의 본질 — monitored_object 자체가 없다

Vue 필터 로직(TransientOutageList.vue:275~330)의 핵심:

```javascript
const monitoredInfo = row.monitored_object   // ← 이 필드가 핵심
const slot = monitoredInfo.substr(monitoredInfo.indexOf('slot=') + 5).split('/')[0]
```

**DB 확인 결과: `monitored_object` 컬럼이 DB 전체에 존재하지 않음.**

이것은 TOPAS EMS에서 사용하던 형식이다:
```
예: 10.82.49.182/rack=1/shelf=1/slot=3/port=1(IN)
```

현재 NIA 시스템의 알람 테이블(`mba.tb_roadm_low_optical_performance`)에는 이 형식 대신 별도 컬럼으로 분리되어 있다:

| TOPAS (레거시) | NIA (현재) | 비고 |
|---------------|-----------|------|
| `monitored_object` (합쳐진 문자열) | **없음** | 전체 DB에 미존재 |
| → `slot=N` 추출 | `ptpname` (SH1-S.1, SH1-S.2) | 슬롯 식별 |
| → `port=N(IN/OUT)` 추출 | `in_out` (IN, OUT) | 방향 식별 |
| `repeater_rssup` → rsspuSlot ('02','17') | `up_ptpname_bau` / `down_ptpname_bau` | 중계기 방향 매핑 |

SQL의 `SELECT_MBA_LOW_ALARM_LIST`에서도 `monitored_object`가 주석 처리(line 490)되어 반환하지 않는다.

**즉, rsspuSlot이 없어서 필터링이 안 되는 게 아니라, 필터링의 대상(`monitored_object`)자체가 현재 시스템에 없으므로 `getMbaAlarmFilter` 전체가 작동하지 않는 상태다.**

### rsspuSlot vs ptpname 비교 분석

#### 1. TOPAS 방식 (rsspuSlot)

```
rsspuSlot = substring(repeater_rssup, 5, 2)  →  '02' 또는 '17'

매핑 규칙:
  rsspuSlot='02' → 상향 slot=3,  하향 slot=16
  rsspuSlot='17' → 상향 slot=16, 하향 slot=3

필터링 대상: monitored_object 문자열에서 slot=N 추출
```

- TOPAS EMS가 장비의 RSSPU(Repeater Shelf Slot Processing Unit) 식별자를 관리
- 하나의 중계기에 RSSPU 모듈이 '02' 또는 '17' 위치에 설치
- RSSPU 위치에 따라 물리적 slot 번호가 결정됨
- **전제 조건**: `monitored_object` 형식의 알람 데이터, TOPAS DB

#### 2. NIA 방식 (ptpname)

```
nia.tb_roadm_trunk_repeater 중계기 데이터 (17건):

패턴 A (10건): up_ptpname_bau=SH1-S.1, down_ptpname_bau=SH1-S.2
패턴 B  (7건): up_ptpname_bau=SH1-S.2, down_ptpname_bau=SH1-S.1

알람 데이터: ptpname = SH1-S.1 또는 SH1-S.2 (648건씩)
```

- `SH1-S.1` = Shelf 1, Slot 1
- `SH1-S.2` = Shelf 1, Slot 2
- `up_ptpname_bau` = 상향(UP) 방향 BAU 포트
- `down_ptpname_bau` = 하향(DOWN) 방향 BAU 포트
- **전제 조건**: `ptpname` + `in_out` 분리 컬럼의 알람 데이터, NIA DB

#### 3. 순단장애 서비스 관점에서의 차이

순단장애 서비스의 경보 필터링 목적:
> 토폴로지 맵에서 특정 노드/링크를 클릭하면, 해당 장비의 관련 경보만 필터링하여 표시

| 관점 | rsspuSlot (TOPAS) | ptpname (NIA) |
|------|-------------------|---------------|
| **식별 방식** | RSSPU 모듈 위치(02/17) → slot 번호(3/16) 간접 매핑 | ptpname(S.1/S.2)으로 직접 식별 |
| **방향 판별** | rsspuSlot + routenum 방향 조합으로 slot 결정 | `up_ptpname_bau` = 상향, `down_ptpname_bau` = 하향으로 직관적 |
| **알람 매칭** | `monitored_object`에서 slot 파싱 | `ptpname` 컬럼 직접 비교 |
| **데이터 의존** | TOPAS DB 필요 (현재 없음) | NIA DB에 이미 존재 |
| **코드 복잡도** | slot 번호 하드코딩 + 조건 분기 | 단순 문자열 비교 |
| **확장성** | RSSPU 모듈 변경 시 매핑 테이블 수정 필요 | ptpname이 장비에서 직접 오므로 자동 반영 |

#### 4. 핵심 결론

**rsspuSlot 방식을 복원하는 것은 무의미하다.** 이유:
1. 필터링 대상인 `monitored_object` 형식이 NIA DB에 없다
2. 알람 데이터가 `ptpname` + `in_out`으로 분리되어 있다
3. rsspuSlot → slot 매핑은 TOPAS 시스템 고유 규약이며, NIA에서는 ptpname이 동일 역할을 한다

**ptpname 방식으로 재설계하는 것이 올바른 방향이다.** 이유:
1. 중계기 방향 정보가 `up/down_ptpname_bau`로 이미 존재 (17건)
2. 알람의 `ptpname`과 직접 비교 가능 (S.1 ↔ S.1, S.2 ↔ S.2)
3. `monitored_object` 파싱이 불필요 — `ptpname` 컬럼 직접 사용
4. 엔진 수정 없이 SQL + Vue 수정만으로 구현 가능

### 해결 방안 (재정리)

**ptpname 기반 재설계 (권장)**:
1. SQL: `SELECT_MBA_REPEATER_SLOT_DATA`를 `nia.tb_roadm_trunk_repeater`에서 `up_ptpname_bau`, `down_ptpname_bau` 반환하도록 수정
2. SQL: `SELECT_MBA_LOW_ALARM_LIST`에 `ptpname`, `in_out` 컬럼 추가 반환
3. Vue: `getMbaAlarmFilter`에서 `monitored_object` 파싱 대신 `row.ptpname` + `row.in_out` 직접 비교
4. Vue: `isRepeaterSlot` 로직을 ptpname 기반으로 교체

**예상 로직**:
```
클릭된 노드의 방향(up/down) + 중계기 ptpname_bau 매핑
→ 알람의 ptpname과 직접 비교
→ 알람의 in_out으로 수신(IN)/송신(OUT) 판별
```

**난이도**: 중간 (엔진 불필요, SQL 2개 + Vue 함수 2개 수정)
**rsspuSlot 복원 대비 장점**: TOPAS 의존 제거, 코드 단순화, 엔진 작업 불필요

### 수정 완료 (2026-04-03)

ptpname 방식(NIA 방식)으로 재설계 완료. 수정 파일 3개:

#### SQL 1: SELECT_MBA_REPEATER_SLOT_DATA

```xml
<!-- Before: WHERE 1 = 0 (TOPAS 테이블 미존재) -->
SELECT NULL AS rsspuSlot WHERE 1 = 0

<!-- After: nia.tb_roadm_trunk_repeater에서 ptpname 반환 -->
SELECT trunk_name, routenum, sysname, up_ptpname_bau, down_ptpname_bau
FROM nia.tb_roadm_trunk_repeater
WHERE trunk_name = #{TRUNK_NAME} AND routenum NOT IN (0, 10)
ORDER BY routenum
```

#### SQL 2: SELECT_MBA_LOW_ALARM_LIST

```xml
<!-- Before: monitored_object 주석 처리 -->
<!-- , T.monitored_object AS monitored_object -->

<!-- After: ptpname, in_out 컬럼 추가 -->
, T.ptpname AS ptpname
, T.in_out AS in_out
```

#### Vue: getMbaAlarmFilter + loadRepeaterSlot 재설계

```
Before:
  - loadRepeaterSlot(): rsspuSlot 단일값 조회 → undefined (TOPAS 테이블 없음)
  - getMbaAlarmFilter(): monitored_object 파싱 → slot 번호 추출 → isRepeaterSlot() 호출
  - isRepeaterSlot(): rsspuSlot + routenum 조합으로 slot 매핑
  → monitored_object가 없어 항상 return false (필터링 미작동)

After:
  - loadRepeaterSlot(): nia.tb_roadm_trunk_repeater에서 중계기별 ptpname 맵 조회
    → repeaterMap = { sysname: { up: 'SH1-S.1', down: 'SH1-S.2' } }
  - getMbaAlarmFilter(): row.ptpname + row.in_out으로 직접 비교
    - 노드 클릭: sysname 일치하는 알람 표시
    - 링크 클릭: ROADM은 in_out(IN/OUT)으로, 중계기는 ptpname으로 방향 판별
  - isRoadmSlot(), isRepeaterSlot(): 삭제 (더 이상 불필요)
```

**복구되는 기능**: 토폴로지 노드/링크 클릭 시 해당 장비의 관련 경보만 필터링 표시

---

## ~~MBA-E1~~ — 해결 완료

---

## MBA-E2. 영향회선 리스트 (SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST)

**현상**: 영향회선 탭이 항상 빈 리스트
**차단 기능**: 순단장애 영향회선 조회

**DB 상태**: 관련 테이블 DB 전체에 없음 (03_영향회선_구현불가_보고.md 참조)

**해결 방안**: 영향회선 산출 엔진 개발 필요

**난이도**: 높음 (별도 프로젝트)

---

## MBA-E3. TT 일괄처리 (SELECT_TICKET_ALARM_BATCH_LIST)

**현상**: 일괄처리 모달에서 관련 경보 목록이 비어있음
**차단 기능**: 순단장애 TT 일괄처리

**DB 상태**: `%batch%`, `%ticket_alarm%` 테이블 DB 전체에 없음

**해결 방안**:
- A) `tb_mba_ticket_current`에서 동일 trunk_name 기준으로 관련 전표 조회하는 SQL 작성
- B) 엔진에서 배치 집계 테이블 생성

**난이도**: 중간 (A안은 UI+SQL 수정만)

---

## MBA-E4. 원인분류/시설분류 코드 (5개 쿼리)

**현상**: 마감 처리 시 분류 드롭다운이 비어있어 마감 불가
**차단 기능**: 순단장애 전표 마감 처리 (가장 치명적)

**DB 상태**: 분류 코드 마스터 테이블 DB 전체에 없음. `tb_mba_ticket_handling`에 `reason_level1~3`, `charge_level1~2` 저장 컬럼은 있으나 선택 목록이 없음.

**해결 방안**: 코드 마스터 테이블 생성 + 분류 코드 데이터 INSERT (DBA 작업, 엔진 불필요)

**난이도**: 중간 (테이블 생성은 간단, 분류 체계 수집이 핵심)

---

## MBA-E5. 장비 유형 표시 (EQUIP_TYPE)

**현상**: 전표 상세에서 장비 유형이 빈칸
**차단 기능**: 장비 유형 정보 표시

**DB 상태**:
- `nia.tb_topology.equip_type`: 존재 (POTN, ROADM)
- sysname 포맷 불일치: CABLE(한글 `서울NIA`) vs topology(IP `192.168.200.210-SH1`)
- 직접 JOIN 불가

**해결 방안**:
- A) 현재 NIA 네트워크는 ROADM만 사용 → `'ROADM'` 고정값으로 대체 (간이)
- B) trunk_topology 경유 다단 JOIN
- C) CABLE 테이블에 equip_type 컬럼 추가 (엔진 적재)

**난이도**: 낮음 (A안)

---

## 우선순위

| 순위 | 항목 | 작업 | 난이도 |
|:----:|------|------|:------:|
| 1 | MBA-E4 | 코드 마스터 테이블 생성 (DBA) | 중간 |
| 2 | MBA-E5 | `'ROADM'` 고정값 또는 다단 JOIN | 낮음 |
| 3 | MBA-E3 | ticket_current에서 동일 trunk 조회 | 중간 |
| 4 | MBA-E2 | 영향회선 엔진 개발 (별도 프로젝트) | 높음 |

---

## 전체 해결 현황 (04_문서 기준 17건)

| 단계 | 건수 | 문서 | 내용 |
|------|:----:|------|------|
| 즉시 수정 (1차) | 8건 | 05_즉시수정_적용내역.md | MBA-F1~F4, PM-F1~F4 |
| 추가 수정 (DB 확인) | 1건 | 05_즉시수정_적용내역.md | MBA-F5 (STATUS.UPDATE_TIME) |
| PM 엔진항목 수정 | 2건 | **본 문서 (07)** | PM-E1, PM-E2 |
| PM 검증 완료 | 1건 | **본 문서 (07)** | PM-E3 (수정 불필요 확인) |
| MBA-E1 수정 | 1건 | **본 문서 (07)** | MBA-E1 (ptpname 방식 재설계) |
| **미해결** | **4건** | **본 문서 하단** | MBA-E2~E5 |
| **합계** | **17건** | | **해결 13건 / 미해결 4건** |
