# MBA-E4 분류 코드 테이블 생성 쿼리 및 수정 계획

> 작성일: 2026-04-03

---

## 1. 테이블 생성 (DDL)

```sql
-- nia.tb_code_cd: 계층형 공통 코드 마스터
CREATE TABLE IF NOT EXISTS nia.tb_code_cd (
    category_cd VARCHAR(20)  NOT NULL,   -- 코드 카테고리 ('REASON', 'CHARGE' 등)
    lvl         INTEGER      NOT NULL,   -- 레벨 (1, 2, 3)
    higher_cd   VARCHAR(20),             -- 상위 코드 (최상위는 NULL)
    cd          VARCHAR(20)  NOT NULL,   -- 코드
    cd_nm       VARCHAR(100) NOT NULL,   -- 코드명 (화면 표시용)
    use_yn      CHAR(1)      DEFAULT 'Y',
    PRIMARY KEY (category_cd, cd)
);

CREATE INDEX idx_tb_code_cd_higher ON nia.tb_code_cd (category_cd, higher_cd);

COMMENT ON TABLE nia.tb_code_cd IS '공통 코드 마스터 (계층형)';
COMMENT ON COLUMN nia.tb_code_cd.category_cd IS '코드 카테고리';
COMMENT ON COLUMN nia.tb_code_cd.lvl IS '코드 레벨 (1=대분류, 2=중분류, 3=소분류)';
COMMENT ON COLUMN nia.tb_code_cd.higher_cd IS '상위 코드 (lvl=1이면 NULL)';
COMMENT ON COLUMN nia.tb_code_cd.cd IS '코드값';
COMMENT ON COLUMN nia.tb_code_cd.cd_nm IS '코드명 (화면 표시)';
```

---

## 2. 분류 코드 데이터 (INSERT)

### 원인분류 (REASON) — 3단계

```sql
-- LVL 1: 대분류
INSERT INTO nia.tb_code_cd (category_cd, lvl, higher_cd, cd, cd_nm) VALUES
('REASON', 1, NULL, 'R01', '전송설비'),
('REASON', 1, NULL, 'R02', '외부요인'),
('REASON', 1, NULL, 'R03', '기타');

-- LVL 2: 중분류
INSERT INTO nia.tb_code_cd (category_cd, lvl, higher_cd, cd, cd_nm) VALUES
('REASON', 2, 'R01', 'R0101', '광케이블'),
('REASON', 2, 'R01', 'R0102', '전송장비'),
('REASON', 2, 'R01', 'R0103', '전원설비'),
('REASON', 2, 'R02', 'R0201', '공사/작업'),
('REASON', 2, 'R02', 'R0202', '자연재해'),
('REASON', 2, 'R02', 'R0203', '인적사고'),
('REASON', 2, 'R03', 'R0301', '원인불명'),
('REASON', 2, 'R03', 'R0302', '자연복구'),
('REASON', 2, 'R03', 'R0303', '시험/점검');

-- LVL 3: 소분류
INSERT INTO nia.tb_code_cd (category_cd, lvl, higher_cd, cd, cd_nm) VALUES
('REASON', 3, 'R0101', 'R010101', '광케이블 단선'),
('REASON', 3, 'R0101', 'R010102', '광케이블 열화'),
('REASON', 3, 'R0101', 'R010103', '광커넥터 불량'),
('REASON', 3, 'R0101', 'R010104', '광접속 불량'),
('REASON', 3, 'R0102', 'R010201', '장비 장애'),
('REASON', 3, 'R0102', 'R010202', '보드 장애'),
('REASON', 3, 'R0102', 'R010203', '포트 장애'),
('REASON', 3, 'R0102', 'R010204', 'S/W 오류'),
('REASON', 3, 'R0103', 'R010301', '정전'),
('REASON', 3, 'R0103', 'R010302', 'UPS 장애'),
('REASON', 3, 'R0103', 'R010303', '전원계통 불량'),
('REASON', 3, 'R0201', 'R020101', '도로공사'),
('REASON', 3, 'R0201', 'R020102', '건축공사'),
('REASON', 3, 'R0201', 'R020103', '타기관 공사'),
('REASON', 3, 'R0202', 'R020201', '태풍'),
('REASON', 3, 'R0202', 'R020202', '폭우/침수'),
('REASON', 3, 'R0202', 'R020203', '지진'),
('REASON', 3, 'R0202', 'R020204', '낙뢰'),
('REASON', 3, 'R0203', 'R020301', '차량 충돌'),
('REASON', 3, 'R0203', 'R020302', '화재'),
('REASON', 3, 'R0203', 'R020303', '운용자 실수'),
('REASON', 3, 'R0301', 'R030101', '원인불명'),
('REASON', 3, 'R0302', 'R030201', '자연복구'),
('REASON', 3, 'R0303', 'R030301', '예방점검'),
('REASON', 3, 'R0303', 'R030302', '회선시험');
```

### 시설분류 (CHARGE) — 2단계

```sql
-- LVL 1: 대분류
INSERT INTO nia.tb_code_cd (category_cd, lvl, higher_cd, cd, cd_nm) VALUES
('CHARGE', 1, NULL, 'C01', '전송'),
('CHARGE', 1, NULL, 'C02', '선로'),
('CHARGE', 1, NULL, 'C03', '전원'),
('CHARGE', 1, NULL, 'C04', '기타');

-- LVL 2: 중분류
INSERT INTO nia.tb_code_cd (category_cd, lvl, higher_cd, cd, cd_nm) VALUES
('CHARGE', 2, 'C01', 'C0101', 'ROADM'),
('CHARGE', 2, 'C01', 'C0102', 'POTN'),
('CHARGE', 2, 'C01', 'C0103', 'OTN'),
('CHARGE', 2, 'C01', 'C0104', 'WDM'),
('CHARGE', 2, 'C02', 'C0201', '광케이블'),
('CHARGE', 2, 'C02', 'C0202', '광접속함'),
('CHARGE', 2, 'C02', 'C0203', '광분배함'),
('CHARGE', 2, 'C03', 'C0301', 'UPS'),
('CHARGE', 2, 'C03', 'C0302', '축전지'),
('CHARGE', 2, 'C03', 'C0303', '정류기'),
('CHARGE', 2, 'C04', 'C0401', '미분류');
```

---

## 3. 확인 쿼리

```sql
-- 데이터 건수 확인
SELECT category_cd, lvl, COUNT(*) FROM nia.tb_code_cd GROUP BY category_cd, lvl ORDER BY 1, 2;
-- 예상: REASON LVL1=3, LVL2=9, LVL3=25 / CHARGE LVL1=4, LVL2=11

-- 원인분류 1단계
SELECT cd_nm AS item FROM nia.tb_code_cd
WHERE category_cd = 'REASON' AND lvl = 1 AND use_yn = 'Y' ORDER BY cd;

-- 원인분류 2단계 (상위='전송설비' 선택 시)
SELECT child.cd_nm AS item FROM nia.tb_code_cd child
INNER JOIN nia.tb_code_cd parent ON child.higher_cd = parent.cd
WHERE child.category_cd = 'REASON' AND child.lvl = 2
  AND parent.cd_nm = '전송설비' AND child.use_yn = 'Y' ORDER BY child.cd;

-- 시설분류 1단계
SELECT cd_nm AS item FROM nia.tb_code_cd
WHERE category_cd = 'CHARGE' AND lvl = 1 AND use_yn = 'Y' ORDER BY cd;
```

---

## 4. MBA-E4 수정 계획

DB 생성 후 아래 파일을 수정:

### SQL (transmissionFaultAnalytics.xml)

5개 쿼리를 `nia.tb_code_cd`에서 조회하도록 교체:

| SQL ID | 변경 내용 |
|--------|----------|
| `SELECT_FAULTREASON_LV1_LIST` | `WHERE category_cd = 'REASON' AND lvl = 1` → cd_nm AS ITEM |
| `SELECT_FAULTREASON_LV2_LIST` | `lvl = 2 AND higher_cd = (상위 cd_nm으로 cd 조회)` → cd_nm AS ITEM |
| `SELECT_FAULTREASON_LV3_LIST` | `lvl = 3 AND higher_cd = (상위 cd_nm으로 cd 조회)` → cd_nm AS ITEM |
| `SELECT_FAULTCHARGE_LV1_LIST` | `WHERE category_cd = 'CHARGE' AND lvl = 1` → cd_nm AS ITEM |
| `SELECT_FAULTCHARGE_LV2_LIST` | `lvl = 2 AND higher_cd = (상위 cd_nm으로 cd 조회)` → cd_nm AS ITEM |

**핵심**: Vue는 `cd_nm`(텍스트)을 선택하고 그대로 LEVEL1/LEVEL2 파라미터로 전달.
SQL에서 `cd_nm` → `cd` 역조회 후 `higher_cd`로 하위 목록 조회.
`tb_mba_ticket_handling`에도 `cd_nm` 텍스트가 저장됨 (기존 동작 유지).

Vue 수정 불필요.
