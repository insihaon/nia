# ActiveStandby 필드 문제 해결 완료

## ✅ 수정 내용

### 1. Link 모델에 ActiveStandby 필드 추가

**파일:** `app/src/main/java/com/ipsdn_opt/app/model/Link.java`

**추가된 내용:**
```java
private Boolean activestandby; // ActiveStandby 필드 추가

// 생성자에서 기본값 설정
public Link() {
    this.activestandby = false; // 기본값 설정
}
public Link(long sif_id, long rif_id, Integer ospfmetric, boolean usaged) {
    // ... 기존 코드 ...
    this.activestandby = false; // 기본값 설정
}
```

### 2. linkUpdateFromIpsdn() 메서드 수정

**파일:** `app/src/main/java/com/ipsdn_opt/app/service/CollectSvc.java`

**수정된 내용:**
```java
links.forEach(l -> {
    l.setUsaged(false);
    // ActiveStandby 필드가 null이면 기본값 설정
    if(l.getActivestandby() == null) {
        l.setActivestandby(false);
    }
});
```

---

## 📋 다음 단계

### 1. 빌드 및 배포

```bash
# 프로젝트 빌드
cd app
mvn clean package

# JAR 파일 확인
ls -lh target/app-0.0.1-SNAPSHOT.jar
```

### 2. Docker 컨테이너 재배포

기존에 사용하던 스크립트로 재배포:
```bash
# 서버에서 실행
IPADDR='103.22.222.7'
PORT='22080'
JARFILENAME='app-0.0.1-SNAPSHOT.jar'

sshpass -p '.....' scp -P ${PORT} -o StrictHostKeyChecking=no root@${IPADDR}:~/ipsdn_opt/app/target/${JARFILENAME} ./ipsdn_opt_app.jar

docker stop ipsdn_opt_1
docker rm ipsdn_opt_1
docker build -t ipsdn_opt .
docker run -d -it -p8088:8080 -p22088:22 --restart always --name ipsdn_opt_1 ipsdn_opt
```

### 3. 배포 후 확인

```bash
# 에러 로그 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "ActiveStandby\|JDBCConnectionException" | tail -20

# linkUpdateFromIpsdn 실행 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "Link is Updated from IP-SDN" | tail -10
```

---

## 🔍 추가 확인 사항

### DB 연결 문제 해결 여부 확인

`ActiveStandby` 필드 문제는 해결했지만, 여전히 DB 연결 문제가 있을 수 있습니다.

**확인 방법:**
```bash
# DB 연결 에러 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "JDBCConnectionException\|Unable to acquire JDBC Connection" | tail -20

# latencyCheckAllNode 호출 확인
docker logs ipsdn_opt_1 2>&1 | grep -i "latencyCheckAllNode\|All Node Latency Measuring" | tail -20
```

**만약 여전히 DB 연결 문제가 있다면:**
- DB 서버 상태 확인
- DB 연결 풀 설정 확인
- 네트워크 연결 확인

---

## 📝 참고사항

### ActiveStandby 필드 타입

- Java: `Boolean` (래퍼 타입 사용 - null 허용)
- DB: `TINYINT(1)` 또는 `BOOLEAN` (NOT NULL)
- 기본값: `false` (0)

### 기존 데이터 처리

기존 `link` 테이블의 데이터에 `ActiveStandby` 값이 없을 수 있으므로:
- `linkUpdateFromIpsdn()` 실행 시 기존 링크들도 `ActiveStandby = false`로 설정됨
- 새로운 링크는 생성자에서 자동으로 `false` 설정됨

---

## ✅ 예상 결과

1. ✅ `linkUpdateFromIpsdn()` 실행 시 `ActiveStandby` 필드 에러 해결
2. ✅ `Schedular.calculateDayDataAndOptimizeRoute()` 정상 실행
3. ✅ DB 연결 문제가 해결되면 `latencyCheckAllNode()` 정상 실행
4. ✅ nodefactor 수집 정상 동작
