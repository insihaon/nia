# Vue-Mapper GAP 분석: transmissionFaultAnalytics

> 분석일: 2026-03-24
> 분석 대상 Vue 파일:
> - `TransientOutageList.vue` (순단장애 감시)
> - `topology2d/index.vue` (순단장애 2D 토폴로지)
> - `PredictiveMaintenanceTicket.vue` (예지보전 전표)
> - `component/Map2D.vue` (예지보전 2D 토폴로지)
> - `modal/ModalRcaProcessHandling.vue` (전표 처리 모달)
> - `modal/ModalRcaBatchProcessing.vue` (TT 일괄처리 모달)
> - `modal/ModalPredictiveReviewOpinion.vue` (검토 의견 모달)

---

## 1. GAP 요약

Vue에서 참조하지만 매퍼에서 주석 처리되어 **데이터가 오지 않는** 필드 목록입니다.

### 상태 분류 기준

- **복원 가능**: DB에 테이블/컬럼이 존재하며, 주석만 해제하면 동작
- **복원 불가**: DB에 해당 테이블/컬럼이 없거나, 구조가 변경됨
- **대체 가능**: 다른 테이블/쿼리로 대체하여 데이터 공급 가능

---

## 2. TransientOutageList.vue (순단장애 감시)

### 2.1 전표 목록 (SELECT_TICKET_CUR_LIST)

| Vue 필드 | 템플릿 위치 | 현재 상태 | 분류 |
|----------|-----------|----------|:----:|
| `influencecircuit_count` | L63: `영향서비스 {{ row.influencecircuit_count \|\| 0 }}` | 매퍼에서 주석 처리 (L290) | **복원 불가** |
| `equip_cnt` | L64: `영향장비 {{ row.equip_cnt }}` | 매퍼에 해당 컬럼 없음 | **복원 불가** |
| `nature_restoration` | L562: `Number(ticket.nature_restoration)` | 매퍼에서 주석 처리 (L205) | **복원 불가** |

**상세 분석:**

#### `influencecircuit_count` - 복원 불가
- 매퍼 L290에 `INFLUENCECIRCUIT_COUNT` 주석 처리됨
- `rca.tb_rca_ticket_cnt` 테이블에는 `total_alarm_count`, `related_alarm_count`, `child_count`만 존재
- `influencecircuit_count` 컬럼은 DB에 없음
- **결론**: 별도 집계 쿼리 또는 `mba.tb_ticket_ic_mst`에서 COUNT 필요하나, 해당 테이블도 주석 처리 상태

#### `equip_cnt` - 복원 불가
- 매퍼에 `equip_cnt` 관련 SELECT 자체가 없음
- Vue에서 참조하지만 항상 빈 값으로 표시됨
- **결론**: 신규 집계 로직 필요 (장비 수 카운트 기준 정의 필요)

#### `nature_restoration` - 복원 불가
- 매퍼 L205에 주석 처리 (`T.NATURE_RESTORATION`)
- `rca.tb_rca_ticket_current` 테이블에 해당 컬럼 없음 (DB 확인)
- Vue에서 상태 색상 판단에 사용 (자연복구 시 `#70802b` 표시)
- **결론**: DB 컬럼이 삭제됨. 자연복구 상태 판단 로직 재설계 필요

### 2.2 알람 그리드 (SELECT_MBA_LOW_ALARM_LIST)

| Vue 필드 | alarmGridTable 컬럼 | 현재 상태 | 분류 |
|----------|-------------------|----------|:----:|
| `name_code` | L226: 구분 | 매퍼에서 미제공 | **복원 불가** |
| `end_time` | L227: 광레벨 수집완료 시간 | 매퍼에서 주석 처리 | **대체 가능** |
| `officename` | L228: 국사 | 매퍼에서 미제공 | **복원 불가** |
| `monitored_object` | L230: 모니터링 위치 | 매퍼에서 주석 처리 | **복원 불가** |
| `equip_type` | L231: 장비종류 | 매퍼에서 주석 처리 | **복원 불가** |
| `old_diff` | L236: 광레벨 차이(이전 시점) | 매퍼에서 주석 처리 (L504) | **복원 가능** |
| `value_level` | L598: 행 배경색 결정 | 매퍼에서 주석 처리 (L505~510) | **복원 가능** |

**상세 분석:**

#### `end_time` - 대체 가능
- 매퍼 주석에서 `T.end_time` 참조했으나, 현재 DB에는 `ocrtime` 컬럼 존재
- `ocrtime`이 실질적으로 동일한 의미 (수집 완료 시간)
- **제안**: `TO_CHAR(T.ocrtime, 'YYYY-MM-DD HH24:MI:SS') AS end_time` 추가

#### `monitored_object` - 복원 불가
- 과거에는 `monitored_object`라는 컬럼이 있었으나, 현재 `mba.tb_roadm_low_optical_performance`에 해당 컬럼 없음
- `tid + '-' + ptpname + '-' + in_out` 조합으로 생성 가능할 수 있으나 정확한 포맷 확인 필요
- **Vue에서 매우 중요하게 사용**: 슬롯/포트 파싱하여 토폴로지 노드 필터링에 활용 (L275~286)

#### `old_diff` - 복원 가능
- 매퍼 L504에 주석: `OT.value_cur AS old_value_cur` (15분 전 시점 데이터)
- 주석 L512~517의 self-join으로 15분 전 데이터 비교 로직 존재
- DB 테이블에 데이터는 존재함
- **제안**: self-join 주석 해제하여 `old_diff` = `ABS(T.value_cur - OT.value_cur)` 복원

#### `value_level` - 복원 가능
- 매퍼 L505~510의 CASE WHEN 주석에서 산출
- `old_diff` 복원 시 함께 복원 가능
- Vue에서 알람 그리드 행 배경색 결정에 사용 (critical/major/minor)
- **제안**: `old_diff` 복원 후 CASE WHEN 로직 함께 복원

#### `name_code` - 복원 불가
- 과거 토폴로지/장비 정보에서 가져왔을 것으로 추정
- 현재 매퍼에 해당 JOIN 없음
- `mba.tb_roadm_trunk_topology`에도 `name_code` 컬럼 없음

#### `officename`, `equip_type` - 복원 불가
- `topas` 스키마의 장비/국사 정보 필요 (`topas.inv_managedelement` 등)
- 해당 스키마 테이블이 주석 처리된 상태

### 2.3 영향회선 리스트 (SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST)

| Vue 필드 | influenceGridTable 컬럼 | 현재 상태 | 분류 |
|----------|----------------------|----------|:----:|
| `transcircuitname` | 전용회선명 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `llnum` | 전용회선번호 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `custname` | 고객명 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `svcmain` | 서비스 대분류 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `svcsub` | 서비스 소분류 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `svcnet` | 서비스망 종류 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `mgmtofficea/z` | A/Z측 관리국소 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `instlocationa/z` | A/Z측 설치국소 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `instaddra/z` | A/Z측 설치위치 | 매퍼 전체가 `SELECT NULL WHERE 1=0` | **복원 불가** |

**상세 분석:**

- 매퍼의 `SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST`는 `SELECT NULL AS TRANSCIRCUITSEQ WHERE 1 = 0` (L539~569)
- 주석 내에는 `mba.tb_ticket_ic_mst` 테이블 사용
- **DB 확인 결과**: `mba.tb_ticket_ic_mst` 테이블이 DB에 존재하지 않음
- **결론**: 영향회선 기능 전체가 비활성 상태. 테이블 자체가 없으므로 복원 불가

### 2.4 리피터 슬롯 (SELECT_MBA_REPEATER_SLOT_DATA)

| Vue 필드 | 현재 상태 | 분류 |
|----------|----------|:----:|
| `rsspuslot` | 매퍼가 `SELECT NULL WHERE 1=0` (L479~485) | **복원 불가** |

- 주석 내에 `topas.inv_fm_roadm_trunk_repeater_yd` 참조
- `topas` 스키마 테이블은 현재 DB 조회 범위 밖
- Vue에서 토폴로지 필터링에 사용 (L296, L314)
- **결론**: `topas` 스키마 접근 가능 여부 확인 필요

### 2.5 TT 일괄처리 (SELECT_TICKET_ALARM_BATCH_LIST)

| Vue 필드 | ModalRcaBatchProcessing 컬럼 | 현재 상태 | 분류 |
|----------|---------------------------|----------|:----:|
| `ticketno` | TT No. | 매퍼가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `alarmtime` | 발생일시 | 매퍼가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `sysname` | 시스템명 | 매퍼가 `SELECT NULL WHERE 1=0` | **복원 불가** |
| `site` | 발생국소 | 매퍼가 `SELECT NULL WHERE 1=0` | **복원 불가** |

- 주석 내에 `rca.tb_rca_ticket_topas_tt_current` + `rca.tb_alarm_cur_mst` JOIN
- `rca.tb_alarm_cur_mst`는 DB에 존재하나, `rca.tb_rca_ticket_topas_tt_current`는 DB 테이블 목록에 없음
- **결론**: TT 일괄처리 기능 비활성 상태

---

## 3. PredictiveMaintenanceTicket.vue (예지보전)

### 3.1 전표 목록 (SELECT_TICKET_PMM_LIST)

| Vue 필드 | 템플릿 위치 | 현재 상태 | 분류 |
|----------|-----------|----------|:----:|
| `roadm_cnt` | L195: `row.roadm_cnt > 2` (원홉 캐리어 필터) | 매퍼에서 주석 처리 (L588) | **복원 가능** |
| `opinion_cnt` | L55: `검토 의견({{ row.opinion_cnt }})` | 매퍼에서 주석 처리 (L608~611) | **복원 가능** |
| `new_list` | L56: 신규 의견 아이콘 표시 | 매퍼에서 주석 처리 (L612~616) | **복원 가능** |
| `last_handling_time` | L335: 마지막 처리시간 비교 | 매퍼에서 주석 처리 (L617~620) | **복원 가능** |
| `trunk_name` | L40: 캐리어명 표시 | 매퍼에서 주석 처리 (L579) | **복원 가능** |
| `metype` | L581~584: 장비 타입 | 매퍼에서 주석 처리 | **복원 불가** |

**상세 분석:**

#### `roadm_cnt` - 복원 가능
- 매퍼 L603~607의 서브쿼리로 `mba.tb_roadm_trunk_topology`에서 COUNT
- DB 테이블 존재 확인됨
- Vue에서 "원홉 캐리어 제외" 필터에 사용 (L195)
- **제안**: 주석 해제하여 `roadm_cnt` 복원
```sql
, COALESCE((
    SELECT count(*)
    FROM mba.tb_roadm_trunk_topology
    WHERE trunk_name = pmt.trunk_name
), 0) AS roadm_cnt
```

#### `opinion_cnt` - 복원 가능
- 매퍼 L608~611의 서브쿼리로 `mba.tb_roadm_predictive_handling`에서 COUNT
- DB 테이블 존재 확인됨
- Vue에서 검토 의견 건수 표시에 사용
- **제안**: 주석 해제
```sql
, (
    SELECT COUNT(*) FROM mba.tb_roadm_predictive_handling rph
    WHERE rph.ticketno = pmt.ticketno AND rph.issue_date = pmt.issue_date AND delete_yn = 'N'
) AS opinion_cnt
```

#### `new_list` - 복원 가능
- 매퍼 L612~616의 서브쿼리
- 7일 이내 새로운 의견 시퀀스 배열
- Vue에서 "NEW" 아이콘 표시에 사용
- **제안**: 주석 해제
```sql
, (
    SELECT ARRAY_AGG(seqnum)
    FROM mba.tb_roadm_predictive_handling rph
    WHERE rph.ticketno = pmt.ticketno AND rph.issue_date = pmt.issue_date
      AND delete_yn = 'N'
      AND rph.handling_time > current_date - CAST(7 || 'day' AS INTERVAL)
) AS new_list
```

#### `last_handling_time` - 복원 가능
- 매퍼 L617~620의 서브쿼리
- Vue에서 읽음 여부 판단에 사용 (L335)
- **제안**: 주석 해제
```sql
, (
    SELECT TO_CHAR(MAX(rph.handling_time), 'YYYY-MM-DD HH24:MI:SS')
    FROM mba.tb_roadm_predictive_handling rph
    WHERE rph.ticketno = pmt.ticketno AND rph.issue_date = pmt.issue_date AND delete_yn = 'N'
) AS last_handling_time
```

#### `trunk_name` - 복원 가능
- 매퍼 L579에 `pmt.trunk_name` 주석 처리
- 내부 서브쿼리의 GROUP BY에 `pmt.trunk_name` 이미 포함
- Vue L40에서 캐리어명 표시에 사용, L245에서 토폴로지 로드 파라미터로 사용
- **주의**: 현재 매퍼에서 trunk_name은 내부 서브쿼리에서 이미 사용 중 (L598, L629), 외부 SELECT에서만 주석
- **제안**: 외부 SELECT에 `trunk_name` 추가 (이미 서브쿼리 결과에 포함되어 있으므로 주석 해제만 하면 됨)

#### `metype` - 복원 불가
- `topas.inv_managedelement` 테이블 필요
- `topas` 스키마 접근 불가 상태

### 3.2 영향회선 (SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST)

순단장애와 동일한 쿼리 사용 - **복원 불가** (위 2.3 참조)

---

## 4. 복원 가능한 항목 정리 (액션 아이템)

### 즉시 복원 가능 (주석 해제)

| 우선순위 | 쿼리 ID | 필드 | 효과 | 난이도 |
|:--------:|---------|------|------|:------:|
| **1** | SELECT_TICKET_PMM_LIST | `roadm_cnt` | 원홉 캐리어 필터 작동 | 낮음 |
| **2** | SELECT_TICKET_PMM_LIST | `opinion_cnt` | 검토 의견 건수 표시 | 낮음 |
| **3** | SELECT_TICKET_PMM_LIST | `new_list` | 신규 의견 NEW 아이콘 | 낮음 |
| **4** | SELECT_TICKET_PMM_LIST | `last_handling_time` | 의견 읽음 여부 판단 | 낮음 |
| **5** | SELECT_TICKET_PMM_LIST | `trunk_name` (외부 SELECT) | 캐리어명 표시 + 토폴로지 로드 | 낮음 |
| **6** | SELECT_MBA_LOW_ALARM_LIST | `old_diff` + self-join | 이전 시점 광레벨 차이 | 중간 |
| **7** | SELECT_MBA_LOW_ALARM_LIST | `value_level` (CASE WHEN) | 알람 심각도 배경색 | 중간 |

### 대체 방안으로 복원 가능

| 우선순위 | 쿼리 ID | 필드 | 대체 방안 | 난이도 |
|:--------:|---------|------|----------|:------:|
| **8** | SELECT_MBA_LOW_ALARM_LIST | `end_time` | `ocrtime`을 `end_time` alias로 출력 | 낮음 |

---

## 5. 복원 불가한 항목 정리

### DB 테이블/컬럼 자체가 없음

| 쿼리 ID | 필드 | 원인 | Vue 영향 |
|---------|------|------|---------|
| SELECT_TICKET_CUR_LIST | `influencecircuit_count` | 집계 원본 테이블 없음 | 영향서비스 수 항상 0 |
| SELECT_TICKET_CUR_LIST | `equip_cnt` | 매퍼에 정의 자체 없음 | 영향장비 수 항상 빈값 |
| SELECT_TICKET_CUR_LIST | `nature_restoration` | DB 컬럼 삭제됨 | 자연복구 색상 미적용 |
| SELECT_MBA_LOW_ALARM_LIST | `monitored_object` | DB 컬럼 없음 | 토폴로지 노드 필터링 불가 |
| SELECT_MBA_LOW_ALARM_LIST | `name_code` | DB 컬럼 없음 | 알람 구분 미표시 |
| SELECT_MBA_LOW_ALARM_LIST | `officename` | topas 스키마 필요 | 국사 미표시 |
| SELECT_MBA_LOW_ALARM_LIST | `equip_type` | topas 스키마 필요 | 장비종류 미표시 |
| SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST | 전체 (12개 필드) | `mba.tb_ticket_ic_mst` 테이블 없음 | 영향회선 탭 전체 비활성 |
| SELECT_MBA_REPEATER_SLOT_DATA | `rsspuslot` | topas 스키마 필요 | 리피터 슬롯 정보 없음 |
| SELECT_TICKET_ALARM_BATCH_LIST | 전체 (4개 필드) | `rca.tb_rca_ticket_topas_tt_current` 없음 | TT일괄처리 전체 비활성 |
| SELECT_TICKET_PMM_LIST | `metype` | topas 스키마 필요 | 장비타입 미표시 |

### topas 스키마 의존 항목

위 "복원 불가" 항목 중 상당수가 `topas` 스키마 접근이 되면 복원 가능합니다:
- `officename`, `equip_type`, `name_code` -> `topas.inv_managedelement`
- `rsspuslot` -> `topas.inv_fm_roadm_trunk_repeater_yd`
- `metype` -> `topas.inv_managedelement`

**확인 필요**: niadb에 `topas` 스키마가 존재하는지 별도 확인 필요

---

## 6. 권장 작업 순서

```
Phase 1 - 즉시 복원 (예지보전 기능 정상화)
  1. SELECT_TICKET_PMM_LIST: roadm_cnt, opinion_cnt, new_list, last_handling_time, trunk_name 주석 해제

Phase 2 - 알람 상세 복원 (순단장애 알람 그리드 개선)
  2. SELECT_MBA_LOW_ALARM_LIST: old_diff self-join + value_level CASE WHEN 복원
  3. SELECT_MBA_LOW_ALARM_LIST: ocrtime -> end_time alias 추가

Phase 3 - topas 스키마 확인 후 결정
  4. topas 스키마 접근 가능 여부 확인
  5. 가능 시: officename, equip_type, rsspuslot, metype 복원
  6. 불가 시: 해당 UI 요소 비활성화 또는 제거 검토

Phase 4 - 구조적 대응 (신규 개발 필요)
  7. monitored_object 생성 로직 설계 (tid + ptpname + in_out 조합)
  8. 영향회선 기능 재설계 (mba.tb_ticket_ic_mst 대체 방안)
  9. TT일괄처리 기능 재설계
```
