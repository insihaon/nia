# NIA HTTPS 설정 가이드

## 개요

app-nia(Front_web_2024)에 HTTPS를 적용한 구성 문서.
기존 HTTP를 유지하면서 HTTPS 포트를 추가하는 **병행 방식**으로 구성되어 있다.

| 항목 | 값 |
|------|-----|
| 적용 대상 | app-nia (Front_web_2024) |
| HTTP 포트 | 기존 유지 (oper: 8080, dev: 8090) |
| HTTPS 포트 | 8081 |
| 인증서 방식 | 자체 서명 (Self-signed) |
| SSL 처리 | Spring Boot 내장 Tomcat |
| 활성화 제어 | `myconf.https.enabled` (yml) |

---

## 접속 URL

| 환경 | HTTP | HTTPS |
|------|------|-------|
| 운영 (DDNS) | http://ai.koren.kr:8080 | https://ai.koren.kr:8081 |
| 운영 (IP) | http://116.89.191.47:8080 | https://116.89.191.47:8081 |
| 로컬 (dev) | http://localhost:8090 | https://localhost:8081 |
| FE dev | http://localhost:4000 | 프록시 설정으로 HTTPS 백엔드 연결 |

---

## 인증서 정보

### 파일 목록

| 파일 | 경로 | 용도 |
|------|------|------|
| `keystore-https.jks` | `app-nia/src/main/resources/` | HTTPS용 신규 인증서 (git 추적 O) |
| `keystore.jks` | `app-nia/src/main/resources/` | 기존 dataHub용 인증서 (미사용, git 추적 X) |
| `rootCA.pem` | `app-nia/src/main/resources/` | 기존 Root CA (미사용, git 추적 X) |

### keystore-https.jks 상세

| 항목 | 값 |
|------|-----|
| 별칭 (alias) | `nia-https` |
| 비밀번호 | `nia2025ssl` |
| 타입 | JKS |
| 알고리즘 | SHA256withRSA, 2048bit |
| CN | `ai.koren.kr` |
| 유효기간 | 2026.03.19 ~ 2036.03.16 (10년) |

### SAN (Subject Alternative Name)

이 인증서는 아래 주소에서 경고 없이(자체 서명 경고 제외) 사용 가능하다.

- `dns:ai.koren.kr`
- `dns:localhost`
- `ip:116.89.191.47`
- `ip:127.0.0.1`

### 기존 인증서(keystore.jks)를 사용하지 않는 이유

- 비밀번호가 app-nia 기준으로 확인 불가 (dataHub 전용: `gjqm12!!`)
- SAN/CN에 특정 도메인이 없는 범용 인증서 (Internet Widgits Pty Ltd)
- 동일 파일이 app-nia와 app-dataHub에 복사되어 있어 관리가 불명확

---

## 변경 파일 목록

### 신규 생성

| 파일 | 설명 |
|------|------|
| `common-web/.../config/HttpsConnectorConfig.java` | HTTPS 커넥터 등록 (조건부 활성화) |
| `app-nia/.../resources/keystore-https.jks` | 자체 서명 인증서 |

### 수정

| 파일 | 변경 내용 |
|------|----------|
| `app-nia/.../resources/application.yml` | `myconf.https` 기본 설정 추가 (enabled: false) |
| `app-nia/.../resources/application-oper.yml` | `myconf.https.enabled: true` |
| `app-nia/.../resources/application-dev.yml` | `myconf.https.enabled: true` |
| `FE/vue.config.js` | HTTPS 프록시 예시 (주석) |
| `.gitignore` | `keystore-https.jks` 예외 처리 (`!**/keystore-https.jks`) |

---

## 설정 구조

### application.yml (기본값, 비활성화)

```yaml
myconf:
  https:
    enabled: false
    port: 8081
    key-store: classpath:keystore-https.jks
    key-store-password: nia2025ssl
    key-alias: nia-https
    key-store-type: JKS
```

### application-oper.yml / application-dev.yml (활성화)

```yaml
myconf:
  https:
    enabled: true
```

프로필별 yml에서 `enabled: true`만 선언하면 나머지 설정은 기본 application.yml에서 상속된다.

### HTTPS 비활성화

yml에서 `myconf.https.enabled: false`로 변경하면 HTTPS 커넥터가 등록되지 않는다.
기존 HTTP 동작에는 영향 없음.

---

## 동작 원리

```
┌──────────────────────────────────────────┐
│         Spring Boot (app-nia)            │
│                                          │
│  [기존] server.port                      │
│    HTTP :8080 (oper) / :8090 (dev)       │
│                                          │
│  [추가] HttpsConnectorConfig             │
│    HTTPS :8081 (keystore-https.jks)      │
│    @ConditionalOnExpression 으로 제어     │
└──────────────────────────────────────────┘
```

`HttpsConnectorConfig.java`는 `@ConditionalOnExpression`으로 `myconf.https.enabled`가
`true`일 때만 Bean이 생성된다. Tomcat의 `addAdditionalTomcatConnectors`를 통해
기존 HTTP 커넥터와 병행으로 HTTPS 커넥터를 추가한다.

---

## 테스트 방법

### 1단계: 로컬 테스트 (dev 프로필)

```bash
# 빌드 후 실행
cd apps/Web2024/BE
mvn clean package -pl app-nia -am -DskipTests
java -jar app-nia/target/app-nia.jar --spring.profiles.active=dev

# HTTP 확인
curl http://localhost:8090

# HTTPS 확인 (-k: 자체 서명 인증서 허용)
curl -k https://localhost:8081

# 브라우저에서 확인
# https://localhost:8081 접속 → "안전하지 않음" 경고 → 고급 → 계속 진행
```

### 2단계: 운영 환경 테스트 (oper 프로필)

```bash
java -jar app-nia.jar --spring.profiles.active=oper

# 기존 HTTP 정상 동작 확인 (기존 서비스 영향 없음)
curl http://116.89.191.47:8080

# HTTPS 동작 확인
curl -k https://116.89.191.47:8081
curl -k https://ai.koren.kr:8081
```

### 3단계: 브라우저 테스트

1. `https://ai.koren.kr:8081` 접속
2. "연결이 비공개로 설정되어 있지 않습니다" 경고 표시 (자체 서명이므로 정상)
3. 고급 → `ai.koren.kr(안전하지 않음)(으)로 이동` 클릭
4. 페이지 정상 로드 확인
5. 로그인, API 호출 등 주요 기능 테스트

### 확인 로그

애플리케이션 시작 시 아래 로그가 출력되면 HTTPS 활성화 성공:

```
HTTPS 커넥터 활성화: port=8081, keyAlias=nia-https
```

---

## 운영 서버 방화벽

HTTPS 포트(8081)가 방화벽에서 열려 있어야 한다.

```bash
# Linux 방화벽 확인
sudo firewall-cmd --list-ports

# 8081 포트 개방 (필요 시)
sudo firewall-cmd --permanent --add-port=8081/tcp
sudo firewall-cmd --reload
```

---

## 비밀번호 보안 참고

현재 `nia2025ssl`은 평문으로 yml에 저장되어 있다.

| 위협 | 위험도 | 설명 |
|------|--------|------|
| 브라우저/네트워크에서 탈취 | 없음 | SSL 핸드셰이크에 비밀번호가 전송되지 않음 |
| 서버 파일 직접 접근 | 낮음 | yml + jks 둘 다 필요, 서버 접근 권한 필요 |
| git 저장소 탈취 | 낮음 | 자체 서명 인증서라 인증서 자체에 신뢰 가치 없음 |

자체 서명 인증서 환경에서는 평문 비밀번호로 충분하다.
공인 인증서로 전환 시 Jasypt 암호화를 적용하면 된다:

```yaml
myconf:
  https:
    key-store-password: ENC(암호화된_값)
```

---

## 향후 작업 (필요 시)

- [ ] HTTP → HTTPS 리다이렉트 추가 (HTTP 완전 제거 시)
- [ ] 공인 인증서(Let's Encrypt 등)로 교체
- [ ] HSTS 헤더 적용
- [ ] 다른 서브프로젝트(app-ipms, Engine 등) HTTPS 확장
- [ ] vue.config.js 프록시 주석 해제 (FE 개발 시 HTTPS 백엔드 연결)

---

## 인증서 재생성 명령어

인증서 만료 또는 IP/도메인 변경 시 아래 명령어로 재생성:

```bash
cd apps/Web2024/BE/app-nia/src/main/resources

# 기존 파일 삭제
rm keystore-https.jks

# 새 인증서 생성 (SAN에 필요한 도메인/IP 추가)
keytool -genkeypair \
  -alias nia-https \
  -keyalg RSA -keysize 2048 \
  -validity 3650 \
  -keystore keystore-https.jks \
  -storepass nia2025ssl \
  -keypass nia2025ssl \
  -dname "CN=ai.koren.kr, OU=NIA, O=CodeJ, L=Seoul, ST=Seoul, C=KR" \
  -ext "SAN=dns:ai.koren.kr,dns:localhost,ip:116.89.191.47,ip:127.0.0.1"
```

SAN에 새 도메인/IP를 추가하려면 `-ext` 뒤에 `dns:새도메인` 또는 `ip:새IP`를 추가한다.
