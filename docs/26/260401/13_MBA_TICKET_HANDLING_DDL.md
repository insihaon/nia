# MBA 순단장애 처리 상태 테이블 DDL

> 생성일: 2026-04-01
> 목적: 순단장애(MBA) 전표의 접수(ACK)/완료(FIN) 처리 정보를 관리하는 테이블 생성
> 배경: `rca.tb_rca_ticket_handling_status_current` 의 MBA 스키마 대응 테이블 (기존 P0 이슈 해결)

---

## 1. 테이블명 설계

기존 MBA 테이블 네이밍 컨벤션:

| 기존 테이블 | 용도 |
|------------|------|
| `MBA.TB_MBA_TICKET_CURRENT` | 전표 메인 |
| `MBA.TB_MBA_TICKET_CABLE` | 전표 케이블 (A-Z 구간) |
| **`MBA.TB_MBA_TICKET_HANDLING`** | **전표 처리 상태 (신규)** |

---

## 2. CREATE TABLE

```sql
-- ============================================================
-- MBA.TB_MBA_TICKET_HANDLING
-- 순단장애 전표 처리 상태 테이블
-- 대응: rca.tb_rca_ticket_handling_status_current
-- ============================================================

CREATE TABLE MBA.TB_MBA_TICKET_HANDLING (
    -- PK
    TICKET_ID               VARCHAR(50)     NOT NULL,

    -- 전표 분류
    TICKET_TYPE             VARCHAR(20),                        -- 'PF' (Performance Fault)

    -- 처리 상태
    STATUS                  VARCHAR(20)     NOT NULL,           -- INIT / ACK / FIN / AUTO_FIN / NATURE
    AUTOMATION              VARCHAR(10),                        -- 자동처리 여부

    -- 접수(ACK) 처리 정보
    HANDLING_USER           VARCHAR(100),                       -- 접수 처리자
    HANDLING_DEPT           VARCHAR(200),                       -- 접수 소속팀
    HANDLING_AGENCY         VARCHAR(200),                       -- 접수 소속본부
    HANDLING_TIME           TIMESTAMP,                          -- 접수 처리 시각
    HANDLING_CONTENT        TEXT,                                -- 접수 처리 내용

    -- 마감(FIN) 처리 정보
    HANDLING_FIN_USER       VARCHAR(100),                       -- 마감 처리자
    HANDLING_FIN_DEPT       VARCHAR(200),                       -- 마감 소속팀
    HANDLING_FIN_AGENCY     VARCHAR(200),                       -- 마감 소속본부
    HANDLING_ACK_TIME       TIMESTAMP,                          -- 마감 시각 (UI에서 HANDLING_ACK_TIME으로 참조)
    HANDLING_FIN_CONTENT    TEXT,                                -- 마감 처리 내용

    -- 분석 결과
    RCA_ACCURACY            VARCHAR(20),                        -- RCA 정확도 평가

    -- 원인분류 (CableCut용이나 확장성을 위해 포함)
    REASON_LEVEL1           VARCHAR(100),
    REASON_LEVEL2           VARCHAR(100),
    REASON_LEVEL3           VARCHAR(100),

    -- 책임분류
    CHARGE_LEVEL1           VARCHAR(100),
    CHARGE_LEVEL2           VARCHAR(100),

    -- 마감분류
    FIN_LEVEL1              VARCHAR(100),
    FIN_LEVEL2              VARCHAR(100),
    FIN_LEVEL3              VARCHAR(100),

    -- 관리 컬럼
    INSERT_TIME             TIMESTAMP       DEFAULT NOW(),      -- 레코드 생성 시각 (시간 필터링용)
    UPDATE_TIME             TIMESTAMP       DEFAULT NOW(),      -- 레코드 수정 시각

    -- 제약조건
    CONSTRAINT PK_MBA_TICKET_HANDLING PRIMARY KEY (TICKET_ID),
    CONSTRAINT FK_MBA_TICKET_HANDLING_TICKET
        FOREIGN KEY (TICKET_ID)
        REFERENCES MBA.TB_MBA_TICKET_CURRENT (TICKET_ID)
        ON DELETE CASCADE
);

-- 인덱스
CREATE INDEX IDX_MBA_TICKET_HANDLING_STATUS ON MBA.TB_MBA_TICKET_HANDLING (STATUS);
CREATE INDEX IDX_MBA_TICKET_HANDLING_INSERT_TIME ON MBA.TB_MBA_TICKET_HANDLING (INSERT_TIME);

-- 코멘트
COMMENT ON TABLE MBA.TB_MBA_TICKET_HANDLING IS '순단장애 전표 처리 상태';
COMMENT ON COLUMN MBA.TB_MBA_TICKET_HANDLING.TICKET_ID IS '전표 ID (FK → TB_MBA_TICKET_CURRENT)';
COMMENT ON COLUMN MBA.TB_MBA_TICKET_HANDLING.STATUS IS '처리 상태: INIT(신규), ACK(접수), FIN(완료), AUTO_FIN(자동완료), NATURE(자연복구)';
COMMENT ON COLUMN MBA.TB_MBA_TICKET_HANDLING.HANDLING_TIME IS '접수(ACK) 처리 시각';
COMMENT ON COLUMN MBA.TB_MBA_TICKET_HANDLING.HANDLING_ACK_TIME IS '마감(FIN) 처리 시각';
COMMENT ON COLUMN MBA.TB_MBA_TICKET_HANDLING.INSERT_TIME IS '레코드 생성 시각 — SELECT_TICKET_CUR_LIST에서 시간 범위 필터링에 사용';
```

---

## 3. USER_PROCESS_MBA SQL (UPSERT)

Vue의 `getProcessParam()`이 전달하는 파라미터에 맞춘 MERGE 쿼리.
`transmissionFaultAnalytics.xml`에 추가할 SQL:

```xml
<!-- ============================================================ -->
<!-- USER_PROCESS_MBA: 순단장애 전표 접수/완료 처리 (UPSERT)        -->
<!-- Vue 호출: apiUserProcess(param, 'mba')                        -->
<!--   → sqlId = 'USER_PROCESS_MBA'                                -->
<!-- ============================================================ -->
<update id="USER_PROCESS_MBA" parameterType="hashmap">
    <!-- 1. 처리 상태 테이블 UPSERT -->
    INSERT INTO MBA.TB_MBA_TICKET_HANDLING (
        TICKET_ID
        , TICKET_TYPE
        , STATUS
        , RCA_ACCURACY
        , HANDLING_CONTENT
        <if test="STATUS == 'ACK'">
        , HANDLING_USER
        , HANDLING_DEPT
        , HANDLING_AGENCY
        , HANDLING_TIME
        </if>
        <if test="STATUS == 'FIN'">
        , HANDLING_FIN_USER
        , HANDLING_FIN_DEPT
        , HANDLING_FIN_AGENCY
        , HANDLING_ACK_TIME
        , HANDLING_FIN_CONTENT
        </if>
        , INSERT_TIME
        , UPDATE_TIME
    )
    VALUES (
        #{TICKET_ID}
        , #{TICKET_TYPE}
        , #{STATUS}
        , #{RCA_ACCURACY}
        , #{HANDLING_CONTENT}
        <if test="STATUS == 'ACK'">
        , #{HANDLING_USER}
        , #{HANDLING_DEPT}
        , #{HANDLING_AGENCY}
        , NOW()
        </if>
        <if test="STATUS == 'FIN'">
        , #{HANDLING_FIN_USER}
        , #{HANDLING_FIN_DEPT}
        , #{HANDLING_FIN_AGENCY}
        , NOW()
        , #{HANDLING_FIN_CONTENT}
        </if>
        , NOW()
        , NOW()
    )
    ON CONFLICT (TICKET_ID) DO UPDATE SET
        STATUS = #{STATUS}
        , RCA_ACCURACY = #{RCA_ACCURACY}
        , UPDATE_TIME = NOW()
        <if test="STATUS == 'ACK'">
        , HANDLING_USER = #{HANDLING_USER}
        , HANDLING_DEPT = #{HANDLING_DEPT}
        , HANDLING_AGENCY = #{HANDLING_AGENCY}
        , HANDLING_TIME = NOW()
        , HANDLING_CONTENT = #{HANDLING_CONTENT}
        </if>
        <if test="STATUS == 'FIN'">
        , HANDLING_FIN_USER = #{HANDLING_FIN_USER}
        , HANDLING_FIN_DEPT = #{HANDLING_FIN_DEPT}
        , HANDLING_FIN_AGENCY = #{HANDLING_FIN_AGENCY}
        , HANDLING_ACK_TIME = NOW()
        , HANDLING_FIN_CONTENT = #{HANDLING_FIN_CONTENT}
        </if>
    ;

    <!-- 2. 메인 전표 테이블 STATUS 동기화 -->
    UPDATE MBA.TB_MBA_TICKET_CURRENT
    SET STATUS = #{STATUS}
    WHERE TICKET_ID = #{TICKET_ID}
    ;
</update>
```

---

## 4. SELECT_TICKET_HANDLING_CURRENT_LIST 수정 (MBA 버전)

`transmissionFaultAnalytics.xml`의 기존 쿼리를 MBA 테이블로 변경:

```xml
<!-- 티켓 처리 상태 (MBA 버전) -->
<select id="SELECT_TICKET_HANDLING_CURRENT_LIST" parameterType="hashmap" resultType="hashmap">
    SELECT
        TICKET_ID,
        STATUS,
        AUTOMATION,
        HANDLING_USER,
        HANDLING_DEPT,
        HANDLING_AGENCY,
        TO_CHAR(HANDLING_TIME, 'YYYY-MM-DD HH24:MI:SS') AS HANDLING_TIME,
        HANDLING_CONTENT,
        HANDLING_FIN_CONTENT,
        RCA_ACCURACY,
        REASON_LEVEL1,
        REASON_LEVEL2,
        REASON_LEVEL3,
        CHARGE_LEVEL1,
        CHARGE_LEVEL2,
        FIN_LEVEL1,
        FIN_LEVEL2,
        FIN_LEVEL3
    FROM MBA.TB_MBA_TICKET_HANDLING
    <where>
        <trim prefix="(" prefixOverrides="AND|OR" suffix=")" suffixOverrides="">
            <if test="TICKET_ID != null">
                AND TICKET_ID = #{TICKET_ID}::VARCHAR
            </if>
        </trim>
    </where>
</select>
```

---

## 5. SELECT_TICKET_CUR_LIST 에서 HANDLING JOIN 복원

`12_순단장애_테이블불일치_분석.md`의 섹션 7.1 쿼리에서 NULL로 대체했던 처리 상태 컬럼을 MBA.TB_MBA_TICKET_HANDLING JOIN으로 복원:

```sql
-- [기존: NULL 대체]
-- , NULL AS HANDLING_CONTENT
-- , NULL AS HANDLING_USER
-- ...

-- [복원: MBA.TB_MBA_TICKET_HANDLING JOIN]
LEFT OUTER JOIN MBA.TB_MBA_TICKET_HANDLING STATUS
    ON STATUS.TICKET_ID = T.TICKET_ID

-- SELECT 절에서:
, STATUS.RCA_ACCURACY                                       AS RCA_ACCURACY
, STATUS.HANDLING_CONTENT                                   AS HANDLING_CONTENT
, STATUS.HANDLING_FIN_CONTENT                               AS HANDLING_FIN_CONTENT
, TO_CHAR(STATUS.HANDLING_TIME, 'YYYY-MM-DD HH24:MI:SS')   AS HANDLING_TIME
, STATUS.HANDLING_USER                                      AS HANDLING_USER
, STATUS.HANDLING_DEPT                                      AS HANDLING_DEPT
, STATUS.HANDLING_AGENCY                                    AS HANDLING_AGENCY
, TO_CHAR(STATUS.HANDLING_ACK_TIME, 'YYYY-MM-DD HH24:MI:SS') AS HANDLING_ACK_TIME
, STATUS.HANDLING_FIN_USER                                  AS HANDLING_FIN_USER
, STATUS.HANDLING_FIN_DEPT                                  AS HANDLING_FIN_DEPT
, STATUS.HANDLING_FIN_AGENCY                                AS HANDLING_FIN_AGENCY
```

---

## 6. 컬럼 매핑 검증 — Vue ↔ SQL ↔ 테이블

### 6.1 접수(ACK) 처리 시 데이터 흐름

```
Vue (getProcessParam)              SQL (USER_PROCESS_MBA)           테이블 (TB_MBA_TICKET_HANDLING)
─────────────────                  ─────────────────────           ──────────────────────────────
TICKET_ID                    →     #{TICKET_ID}                →   TICKET_ID
TICKET_TYPE                  →     #{TICKET_TYPE}              →   TICKET_TYPE
STATUS = 'ACK'               →     #{STATUS}                   →   STATUS
RCA_ACCURACY                 →     #{RCA_ACCURACY}             →   RCA_ACCURACY
HANDLING_CONTENT             →     #{HANDLING_CONTENT}         →   HANDLING_CONTENT
HANDLING_USER (store.user)   →     #{HANDLING_USER}            →   HANDLING_USER
HANDLING_DEPT (deptName)     →     #{HANDLING_DEPT}            →   HANDLING_DEPT
HANDLING_AGENCY (agencyName) →     #{HANDLING_AGENCY}          →   HANDLING_AGENCY
                                   NOW()                       →   HANDLING_TIME
```

### 6.2 마감(FIN) 처리 시 데이터 흐름

```
Vue (getProcessParam)              SQL (USER_PROCESS_MBA)           테이블 (TB_MBA_TICKET_HANDLING)
─────────────────                  ─────────────────────           ──────────────────────────────
TICKET_ID                    →     #{TICKET_ID}                →   TICKET_ID
STATUS = 'FIN'               →     #{STATUS}                   →   STATUS
RCA_ACCURACY                 →     #{RCA_ACCURACY}             →   RCA_ACCURACY
HANDLING_CONTENT             →     #{HANDLING_CONTENT}         →   HANDLING_CONTENT  (덮어쓰기)
HANDLING_FIN_USER            →     #{HANDLING_FIN_USER}        →   HANDLING_FIN_USER
HANDLING_FIN_DEPT            →     #{HANDLING_FIN_DEPT}        →   HANDLING_FIN_DEPT
HANDLING_FIN_AGENCY          →     #{HANDLING_FIN_AGENCY}      →   HANDLING_FIN_AGENCY
HANDLING_FIN_CONTENT         →     #{HANDLING_FIN_CONTENT}     →   HANDLING_FIN_CONTENT
                                   NOW()                       →   HANDLING_ACK_TIME
```

---

## 7. 적용 순서

| 순번 | 작업 | 대상 파일 | 비고 |
|:----:|------|----------|------|
| 1 | **CREATE TABLE 실행** | DB (PostgreSQL) | 이 문서의 섹션 2 SQL 실행 |
| 2 | **USER_PROCESS_MBA 추가** | transmissionFaultAnalytics.xml | 기존 P0 이슈 해결, 섹션 3 |
| 3 | **SELECT_TICKET_HANDLING_CURRENT_LIST 수정** | transmissionFaultAnalytics.xml | 섹션 4 |
| 4 | **SELECT_TICKET_CUR_LIST 수정** (HANDLING JOIN 복원) | transmissionFaultAnalytics.xml | 섹션 5 + 12번 문서 섹션 7.1 |
| 5 | Vue 변경 없음 | - | `apiUserProcess(param, 'mba')`가 이미 정확한 파라미터 전달 중 |

---

## 8. 참조

- `docs/260401/12_순단장애_테이블불일치_분석.md` — 전체 테이블 불일치 분석
- `docs/260324/01_이식검증_분석계획.md` Line 64 — USER_PROCESS_MBA 부재 P0 이슈 최초 식별
- `docs/260324/02_이식검증_수정결과.md` Line 72 — 동일 이슈 미해결 상태 기록

---

Version: 1.0.0
Last Updated: 2026-04-01
