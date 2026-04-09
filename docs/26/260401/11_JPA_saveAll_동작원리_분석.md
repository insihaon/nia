# JPA saveAll 동작 원리 분석 (EngineNiaOpticalPm)

> 분석일: 2026-04-01
> 분석 대상: `ParsingHdlService.java` 의 `pmRepository.saveAll()` / `eqmntSipcRepository.saveAll()`

---

## 1. 전체 구조 요약

이 프로젝트는 **JPA(Spring Data JPA)** 와 **MyBatis**를 동시에 사용합니다.

```
┌─────────────────────────────────────────────────────────┐
│                    ParsingHdlService                    │
│                                                         │
│  ┌─────────────────┐       ┌──────────────────────┐    │
│  │ pmRepository    │       │ eqmntSipcRepository  │    │
│  │ (JPA)           │       │ (JPA)                │    │
│  └────────┬────────┘       └──────────┬───────────┘    │
│           │                           │                 │
│           ▼                           ▼                 │
│  JpaRepository<PmEntity, PmKey>   JpaRepository<...>   │
│           │                           │                 │
│           ▼                           ▼                 │
│  ┌────────────────┐       ┌───────────────────────┐    │
│  │ NIA.TB_PERFOR  │       │ NIA.TB_EQUIP_SIPC_TMP │    │
│  │ MACE_MST       │       │                       │    │
│  └────────────────┘       └───────────────────────┘    │
└─────────────────────────────────────────────────────────┘

별도로 MyBatis Mapper도 존재 (PmLinkageMapper.xml)
  └─ insertPmData: 동일 테이블에 MyBatis로 INSERT 하는 방식도 정의되어 있음
  └─ callPmPrepro: PostgreSQL 함수 호출용
```

---

## 2. Repository 인터페이스 구조

### PmRepository (PM 데이터 저장)

```java
// 커스텀 메서드 없이 JpaRepository만 상속
public interface PmRepository extends JpaRepository<PmEntity, PmKey> {
}
```

### EqmntSipcRepository (장비 쉘프 저장)

```java
public interface EqmntSipcRepository extends JpaRepository<EqmntSipcEntity, EqmntSipcKey> {
    @Transactional
    void deleteByEqmntSipcKey_Tid(String tid);  // Spring Data JPA 쿼리 메서드
}
```

**핵심**: 두 Repository 모두 `JpaRepository`를 상속하며, `saveAll()`은 **직접 구현하지 않습니다**.
`JpaRepository`가 기본 제공하는 메서드입니다.

---

## 3. saveAll() 동작 원리

### 3.1 Spring Data JPA의 saveAll 내부 동작

`JpaRepository.saveAll()`은 내부적으로 아래 순서로 동작합니다:

```
saveAll(List<Entity> entities)
  └─ 각 entity에 대해 save(entity) 반복 호출
      └─ entityInformation.isNew(entity) 판단
          ├─ true  → entityManager.persist(entity)  [INSERT]
          └─ false → entityManager.merge(entity)    [UPDATE]
```

### 3.2 isNew() 판단 기준 (핵심)

이 프로젝트는 `@EmbeddedId` (복합키)를 사용합니다.

```java
// PmEntity의 ID
@EmbeddedId
private PmKey pmKey;  // sysname + port + ocrtime
```

**판단 로직:**
- `pmKey`가 `null`이면 → **새 엔티티 (INSERT)**
- `pmKey`가 `null`이 아니면 → **기존 엔티티 (SELECT 후 INSERT 또는 UPDATE)**

이 프로젝트에서는 Builder 패턴으로 PmKey를 항상 설정하므로, `pmKey`는 항상 non-null입니다.

따라서 `saveAll()` 호출 시 내부적으로:

```
1. SELECT * FROM NIA.TB_PERFORMACE_MST WHERE sysname=? AND port=? AND ocrtime=?
2-a. 결과 없음 → INSERT (persist)
2-b. 결과 있음 → UPDATE (merge)
```

**즉, saveAll은 UPSERT처럼 동작합니다** (같은 키가 있으면 UPDATE, 없으면 INSERT).

### 3.3 실제 SQL 실행 흐름

```
pmRepository.saveAll(pmEntitiyList)  // 예: 50건의 PmEntity

내부적으로:
  for each entity in list:
    1. SELECT (해당 복합키로 존재 확인)
    2. INSERT 또는 UPDATE

결과: 50건 × 2 = 최대 100개의 SQL 실행
```

---

## 4. Entity - 테이블 매핑 상세

### PmEntity → NIA.TB_PERFORMACE_MST

```java
@Entity
@Table(name = "TB_PERFORMACE_MST", schema = "NIA")
public class PmEntity {

    @EmbeddedId
    private PmKey pmKey;        // 복합키 (sysname + port + ocrtime)

    private String unit;        // 유닛 (MRSA-2A 등)
    private String tmper;       // 측정주기 (15M)
    private double rxCur;       // 수신 현재값
    private double rxMin;       // 수신 최소값
    private double rxMax;       // 수신 최대값
    private double rxAve;       // 수신 평균값
    private double txCur;       // 송신 현재값
    private double txMin;       // 송신 최소값
    private double txMax;       // 송신 최대값
    private double txAve;       // 송신 평균값
}
```

```java
@Embeddable
public class PmKey {            // 복합 기본키
    private String sysname;     // 장비명
    private String port;        // 포트 (S.1-ADD_IN 등)
    private Timestamp ocrtime;  // 수집 시각 (15분 단위 절삭)
}
```

**DB 테이블 구조:**

| 컬럼 | 타입 | PK | 설명 |
|------|------|:--:|------|
| SYSNAME | VARCHAR | PK | 장비 시스템명 |
| PORT | VARCHAR | PK | 포트명 |
| OCRTIME | TIMESTAMP | PK | 수집 시각 (15분 단위) |
| UNIT | VARCHAR | | 유닛명 |
| TMPER | VARCHAR | | 측정 주기 |
| RXCUR | DOUBLE | | 수신 현재값 (dBm) |
| RXMIN | DOUBLE | | 수신 최소값 |
| RXMAX | DOUBLE | | 수신 최대값 |
| RXAVE | DOUBLE | | 수신 평균값 |
| TXCUR | DOUBLE | | 송신 현재값 (dBm) |
| TXMIN | DOUBLE | | 송신 최소값 |
| TXMAX | DOUBLE | | 송신 최대값 |
| TXAVE | DOUBLE | | 송신 평균값 |

### EqmntSipcEntity → NIA.TB_EQUIP_SIPC_TMP

```java
@Entity
@Table(name = "TB_EQUIP_SIPC_TMP", schema = "NIA")
public class EqmntSipcEntity {

    @EmbeddedId
    private EqmntSipcKey eqmntSipcKey;  // 복합키 (tid + system_name)
}
```

```java
@Embeddable
public class EqmntSipcKey {
    private String tid;            // 장비 TID (IP주소)
    private String systemName;     // 쉘프명 (SH1, SH2 등)
}
```

---

## 5. ParsingHdlService 전체 처리 흐름

### 5.1 SIPC 파싱 (장비 쉘프 구성)

```
EmsMmcHdlService.pmCollectHdl()
  │ TL1 명령: RTRV-SIPC
  ▼
ParsingHdlService.parsingHdl("sipc", mmcMsg)
  │
  ├─ 1. TID 추출 (IP주소, 예: "61.252.53.34")
  ├─ 2. TL1 응답 파싱 → SH별 상태 확인
  │     └─ READY 상태인 쉘프만 EqmntSipcEntity로 생성
  │        (DENY거나 READY 없으면 기본값 SH1 사용)
  │
  ├─ 3. 기존 데이터 삭제 (DELETE-INSERT 패턴)
  │     └─ eqmntSipcRepository.deleteByEqmntSipcKey_Tid(tid)
  │        → DELETE FROM NIA.TB_EQUIP_SIPC_TMP WHERE TID = ?
  │
  └─ 4. 새 데이터 저장
        └─ eqmntSipcRepository.saveAll(eqmntSipcEntityList)
           → INSERT INTO NIA.TB_EQUIP_SIPC_TMP (TID, SYSTEM_NAME) VALUES (?, ?)
```

**참고**: SIPC는 DELETE 후 INSERT (전체 교체) 패턴입니다.

### 5.2 PM 파싱 (광성능 데이터)

```
EmsMmcHdlService.pmCollectHdl()
  │ TL1 명령: RTRV-PM
  ▼
ParsingHdlService.parsingHdl("pm", mmcMsg)
  │
  ├─ 1. TL1 응답 파싱
  │     ├─ 시스템명(sysname) 추출
  │     └─ 각 포트별 RX/TX 값 추출 (11개 컬럼 파싱)
  │
  ├─ 2. OCR 시간 계산 (15분 단위 절삭)
  │     └─ 예: 14:37 → 14:30, 14:46 → 14:45
  │
  ├─ 3. PmEntity 생성
  │     └─ PmKey(sysname, port, ocrtime) + RX/TX 값들
  │
  └─ 4. 일괄 저장
        └─ pmRepository.saveAll(pmEntitiyList)
           → 각 엔티티별 SELECT → INSERT 또는 UPDATE
```

---

## 6. JPA saveAll vs MyBatis insertPmData 비교

이 프로젝트에는 동일 테이블에 대해 JPA와 MyBatis 두 가지 저장 방식이 공존합니다.

### PmLinkageMapper.xml (MyBatis 방식, 현재 미사용)

```xml
<insert id="insertPmData" parameterType="java.util.HashMap">
    INSERT INTO NIA.TB_PERFORMACE_MST (SYSNAME, PORT, ...)
    VALUES
    <foreach collection="pmDtoList" item="item" separator=",">
        (#{item.sysname}, #{item.port}, ...)
    </foreach>
</insert>
```

### 비교표

| 항목 | JPA saveAll (현재 사용) | MyBatis insertPmData (미사용) |
|------|----------------------|---------------------------|
| **SQL 생성** | 엔티티당 SELECT + INSERT/UPDATE | 한 번의 bulk INSERT |
| **중복 처리** | 자동 UPSERT (merge) | 주석처리된 ON CONFLICT DO NOTHING |
| **성능** | N건 → 2N개 SQL (느림) | N건 → 1개 SQL (빠름) |
| **코드 위치** | `pmRepository.saveAll()` | `pmLinkageMapper.insertPmData()` |
| **트랜잭션** | Spring @Transactional 자동 | MyBatis 세션 관리 |

---

## 7. batch-size 설정의 역할

```yaml
# application-real.yml
spring:
  datasource:
    primary:
      batch-size: 2000
```

이 설정은 Hibernate의 JDBC batch 기능을 활성화합니다:
- `saveAll()`로 2000건 이하를 저장할 때, INSERT 문들을 하나의 JDBC batch로 묶어 전송
- 네트워크 라운드트립 감소 효과
- 단, **SELECT (isNew 판단)는 batch되지 않음** — INSERT/UPDATE만 batch 대상

실제 동작:
```
saveAll(50건)
  → 50개 SELECT (각각 실행)       ← batch 미적용
  → 50개 INSERT (batch로 묶임)    ← batch 적용
  → DB에 1번의 네트워크 전송으로 50개 INSERT 실행
```

---

## 8. DataSource 설정

```yaml
spring:
  datasource:
    primary:
      driver-class-name: org.postgresql.Driver
      jdbcUrl: jdbc:postgresql://116.89.191.47:6544/niadb
```

- **DB**: PostgreSQL (niadb)
- **스키마**: NIA (TB_PERFORMACE_MST, TB_EQUIP_SIPC_TMP)
- **JPA auto-config**: Spring Boot가 `JpaRepository` 인터페이스를 스캔하여 자동으로 구현체 생성
- **별도 DataSource Config 클래스 없음**: `spring.datasource.primary` 설정만으로 auto-configuration 동작

---

## 9. 요약: saveAll이 하는 일

```
pmRepository.saveAll(pmEntitiyList)
```

위 한 줄이 실행되면:

1. Spring Data JPA가 `JpaRepository`의 기본 구현체(`SimpleJpaRepository`)를 사용
2. 리스트의 각 `PmEntity`에 대해 `save()` 반복 호출
3. 각 `save()`는 복합키(`PmKey`)로 DB에서 SELECT 하여 존재 여부 확인
4. 없으면 INSERT, 있으면 UPDATE (UPSERT 효과)
5. `batch-size: 2000` 설정으로 INSERT/UPDATE는 JDBC batch로 묶여 전송
6. 트랜잭션은 Spring이 자동 관리 (`@Transactional` 전파)

---

Version: 1.0.0
Last Updated: 2026-04-01
