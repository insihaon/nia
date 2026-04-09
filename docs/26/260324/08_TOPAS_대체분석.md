# TOPAS 스키마 대체 분석

> 분석일: 2026-03-24 (실데이터 검증 완료)
> 목적: transmissionFaultAnalytics.xml에서 주석 처리된 `topas.*` 테이블을 NIA 자체 테이블로 대체 가능한지 분석

---

## 1. TOPAS 스키마란?

TOPAS는 NIA 외부의 광전송 장비관리 시스템(ToPAS: Transport OPerations Automation System)이다.
NIA 이식 전에는 TOPAS DB에 직접 접근하여 장비/국사/케이블 정보를 조회했으나, NIA 독립 운영 환경에서는 해당 스키마에 접근할 수 없다.

NIA는 자체적으로 `NiaEmsLinkageApplication`을 통해 EMS에서 광장비 정보를 TELNET/TL1로 직접 수집하여 `nia` 스키마에 저장하고 있다.

---

## 2. 매퍼에서 참조하는 TOPAS 테이블 vs NIA 대체 테이블

### 2.1 `topas.inv_managedelement` (장비 마스터)

**매퍼 참조 위치:**
- L218: `SELECT DISTINCT IM.METYPE FROM topas.inv_managedelement IM WHERE ... = IM.SYSNAME` (주석)
- L581: 예지보전 전표에서 `metype` 조회 (주석)

**TOPAS 테이블 추정 구조:**

| 컬럼 | 용도 |
|------|------|
| `sysname` | 시스템명 (JOIN 키) |
| `metype` | 장비 타입 (ROADM, POTN, MSPP 등) |
| `officescode` | 국사 코드 |
| `officename` | 국사명 |

**NIA 대체 테이블: `nia.tb_equip_mst`**

| NIA 컬럼 | TOPAS 대응 | 대체 가능 여부 |
|----------|-----------|:-------------:|
| `sysname` | `sysname` | **가능** (동일 키) |
| `model` | `metype` | **부분 가능** (model에서 장비타입 유추 가능) |
| `tid` | - | NIA 고유 |
| - | `officescode` | **불가** (NIA에 국사 정보 없음) |
| - | `officename` | **불가** (NIA에 국사 정보 없음) |

**대체 방안:**
```sql
-- 기존 (TOPAS)
SELECT DISTINCT im.metype FROM topas.inv_managedelement im WHERE im.sysname = ?

-- 대체 (NIA) - model 컬럼으로 장비타입 유추
SELECT DISTINCT em.model FROM nia.tb_equip_mst em WHERE em.sysname = ?
```

**한계:** `model`이 `metype`과 정확히 1:1 매핑되는지 확인 필요. 또한 `officename`(국사명)은 NIA 스키마에 해당 데이터 자체가 없어 대체 불가.

---

### 2.2 `topas.inv_fm_roadm_trunk_repeater_yd` (트렁크 리피터 임시)

**매퍼 참조 위치:**
- L483: `SELECT_MBA_REPEATER_SLOT_DATA` - `substring(x.repeater_rssup, 5, 2) as rsspuSlot` (주석)
- L748: `SELECT trunk_name, from_nescode, substring(from_rssup, 3, 2)...` (주석)

**TOPAS 테이블 추정 구조:**

| 컬럼 | 용도 |
|------|------|
| `trunk_name` | 트렁크명 |
| `routenum` | 경로 번호 |
| `repeater_nescode` | 리피터 NES 코드 |
| `repeater_rssup` | 리피터 RSSUP (슬롯 정보 포함) |
| `from_nescode` | 시작 NES 코드 |
| `from_rssup` | 시작 RSSUP |
| `to_nescode` | 종점 NES 코드 |
| `to_rssup` | 종점 RSSUP |

**NIA 대체 테이블: `nia.tb_roadm_trunk_repeater`**

| NIA 컬럼 | TOPAS 대응 | 대체 가능 여부 |
|----------|-----------|:-------------:|
| `trunk_id` | - | NIA 고유 |
| `trunk_name` | `trunk_name` | **가능** |
| `routenum` | `routenum` | **가능** |
| `sysname` | `repeater_nescode` 유사 | **부분 가능** |
| `up_ptpname_bau` | `from_rssup` 유사 | **부분 가능** (포맷 다를 수 있음) |
| `up_ptpname_pau` | - | NIA 고유 |
| `down_ptpname_bau` | `to_rssup` 유사 | **부분 가능** |
| `down_ptpname_pau` | - | NIA 고유 |

**대체 방안:**
```sql
-- 기존 (TOPAS) - rsspuSlot 추출
SELECT substring(x.repeater_rssup, 5, 2) as rsspuSlot
FROM topas.inv_fm_roadm_trunk_repeater_yd x
WHERE x.trunk_name = ? AND X.ROUTENUM = 1

-- 대체 (NIA) - up_ptpname_bau에서 슬롯 추출 (포맷 확인 필요)
SELECT substring(r.up_ptpname_bau, 5, 2) as rsspuSlot
FROM nia.tb_roadm_trunk_repeater r
WHERE r.trunk_name = ? AND r.routenum = 1
```

**한계:** `repeater_rssup`과 `up_ptpname_bau`의 문자열 포맷이 동일한지 실데이터 확인 필요.

---

### 2.3 `topas.inv_fm_roadm_trunk_yd` (트렁크 정보 임시)

**매퍼 참조 위치:**
- L748~750: SELECT_PMM_TOPOLOGY_LIST 주석 내에서 트렁크의 양단 장비/서브셸프 매핑

**TOPAS 테이블 추정 구조:**

| 컬럼 | 용도 |
|------|------|
| `trunk_name` | 트렁크명 |
| `from_nescode` | 시작 NES 코드 |
| `from_rssup` | 시작 RSSUP (서브셸프) |
| `to_nescode` | 종점 NES 코드 |
| `to_rssup` | 종점 RSSUP (서브셸프) |

**NIA 대체:** `nia.tb_roadm_trunk_repeater`의 `sysname` + `up/down_ptpname_bau/pau`로 부분 대체 가능.

---

### 2.4 `topas.tb_oca1` (광케이블)

**매퍼 참조 위치:**
- L80, 365, 415: OCA 케이블 정보 조회 (주석)
- CIT2 관련 `ocaname` 표시

**NIA 대체:** NIA 스키마에 OCA 테이블 없음. **대체 불가.**

---

### 2.5 `topas.tb_office` (국사)

**매퍼 참조 위치:**
- L368~369: `topas.tb_office OFF_A/OFF_Z ON ... = OFF_A.OFFICESCODE` (주석)

**NIA 대체:** NIA 스키마에 국사 테이블 없음. **대체 불가.**

---

## 3. 대체 가능성 매트릭스

| TOPAS 테이블 | 매퍼 사용 필드 | NIA 대체 테이블 | 대체 가능 | 비고 |
|-------------|--------------|----------------|:---------:|------|
| `topas.inv_managedelement` | `metype` | `nia.tb_equip_mst.model` | **부분** | model→metype 매핑 검증 필요 |
| `topas.inv_managedelement` | `officename` | - | **불가** | NIA에 국사 정보 없음 |
| `topas.inv_fm_roadm_trunk_repeater_yd` | `rsspuSlot` | `nia.tb_roadm_trunk_repeater` | **부분** | 문자열 포맷 검증 필요 |
| `topas.inv_fm_roadm_trunk_yd` | from/to nescode, rssup | `nia.tb_roadm_trunk_repeater` | **부분** | 양단 매핑 검증 필요 |
| `topas.tb_oca1` | `ocaname`, OCA 정보 | - | **불가** | NIA에 OCA 테이블 없음 |
| `topas.tb_office` | `officename` | - | **불가** | NIA에 국사 테이블 없음 |

---

## 4. 실데이터 검증 쿼리

아래 쿼리로 NIA 테이블의 실데이터를 확인하여 대체 가능 여부를 판단할 수 있다.

### 4.1 model → metype 매핑 확인

```sql
-- NIA 장비 마스터의 model 값 분포 확인
SELECT model, COUNT(*) AS cnt
FROM nia.tb_equip_mst
GROUP BY model
ORDER BY cnt DESC;
```

## 4.1 결과: model 분포 (검증 완료)

```
model                  | cnt
-----------------------+----
Utrans-6300p-H3        | 17   ← 중계기 (리피터)
Utrans-6300p-V9        |  8   ← 단국장치 (ROADM)
OPN-3000               |  4   ← OPN 장비
```

**판정:** TOPAS의 `metype`은 `ROADM`, `POTN`, `MSPP` 같은 장비 유형 분류이고, NIA의 `model`은 `Utrans-6300p-V9` 같은 제품 모델명이다.
- **직접 1:1 대응은 불가**하지만, `V9` = 단국장치(ROADM), `H3` = 중계기(Repeater) 패턴으로 유추 가능
- TOPAS 원본 시스템의 `metype` 값과 정확히 같지는 않으므로, Vue 표시용으로는 `model` 값을 그대로 쓰거나 매핑 테이블을 만들어야 함

### 4.2 결과: JOIN 키 발견 (핵심 발견)

**topology의 `sysname`은 IP 주소이고, `tb_equip_mst`의 `sysname`은 한글 장비명이다.**

```
topology_sysname  | equip_mst.tid    | equip_mst.sysname | model
192.168.200.210   | 192.168.200.210  | 서울NIA            | Utrans-6300p-V9
10.82.49.182      | 10.82.49.182     | 중계_천안중_0204B    | Utrans-6300p-H3
```

- `topology.sysname` = `equip_mst.tid` (IP 주소로 매칭)
- `equip_mst.sysname`에 한글 장비명 보유 (예: 서울NIA, 대전ETRI, 중계_천안중_0204B)
- **JOIN 키: `topology.sysname = equip_mst.tid`** (20건 전수 매칭 성공)

### 4.3 결과: rsspuSlot 포맷 (대체 불가)

```
up_ptpname_bau | 포맷
SH1-S.1        | SH{shelf}-S.{slot}
SH1-S.2        |
SH5-S.1        |
SH5-S.5        |
```

TOPAS의 `repeater_rssup`에서 `substring(x, 5, 2)`로 추출하던 값은 2자리 숫자(예: `02`, `17`)였다.
NIA의 `up_ptpname_bau`는 `SH1-S.1` 형태로 **포맷이 완전히 다르다.**

- **rsspuSlot 직접 대체 불가** - 문자열 포맷 불일치
- 다만 shelf 번호(`SH1`→`1`)와 슬롯 번호(`S.1`→`1`)는 추출 가능하나, TOPAS의 `02`/`17` 같은 값과 의미가 다름

### 4.4 결과: equip_type (대체 가능)

```
equip_type
----------
POTN
ROADM
```

`nia.tb_topology.equip_type`에 `ROADM`, `POTN` 값이 존재. 이 값은 TOPAS의 `metype`과 동일한 체계.

---

## 5. 검증 결과 기반 최종 판정

### 대체 가능 확정

| 순위 | 항목 | NIA 대체 방법 | 효과 |
|:----:|------|-------------|------|
| 1 | `equip_type` (장비종류) | `nia.tb_topology.equip_type` 그대로 사용 | 알람 그리드 장비종류 표시 |
| 2 | `model` (장비타입) | `nia.tb_equip_mst.model` JOIN ON `tid = sysname(IP)` | 장비 모델 표시 (metype 대신) |
| 3 | 장비 한글명 | `nia.tb_equip_mst.sysname` JOIN ON `tid = sysname(IP)` | IP 대신 한글 장비명 표시 가능 |

### 대체 불가 확정

| 항목 | 판정 근거 | 대응 방안 |
|------|----------|----------|
| `rsspuSlot` | NIA ptpname 포맷(`SH1-S.1`)이 TOPAS rssup 포맷(`02`,`17`)과 완전히 다름 | Vue 리피터 슬롯 필터링 로직 자체를 NIA 포맷에 맞게 재설계 필요 |
| `officename` / `officescode` (국사) | NIA가 국사 정보를 수집하지 않음 | 별도 국사 마스터 테이블 구축 또는 Vue에서 해당 컬럼 숨김 |
| `ocaname` / OCA 정보 (광케이블) | NIA가 OCA 정보를 관리하지 않음 | OCA 관련 기능은 별도 시스템 연동 필요 |

---

## 6. NIA 스키마 vs TOPAS 스키마 대응 정리

```
TOPAS (외부 시스템)                 NIA (자체 수집)
─────────────────                 ─────────────────
inv_managedelement                tb_equip_mst
  .sysname          ─────────▶      .sysname
  .metype           ─── ? ──▶      .model (검증 필요)
  .officescode      ─── X ──▶      (없음)
  .officename       ─── X ──▶      (없음)

inv_fm_roadm_trunk_repeater_yd    tb_roadm_trunk_repeater
  .trunk_name       ─────────▶      .trunk_name
  .routenum         ─────────▶      .routenum
  .repeater_nescode ─── ? ──▶      .sysname (검증 필요)
  .repeater_rssup   ─── ? ──▶      .up_ptpname_bau (검증 필요)

tb_oca1 (광케이블)                 (없음) ── X
tb_office (국사)                   (없음) ── X
```
