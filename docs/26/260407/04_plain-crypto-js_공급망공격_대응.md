# plain-crypto-js 공급망 공격 대응 가이드

> 작성일: 2026-04-07
> 목적: plain-crypto-js 취약점을 통한 공급망 공격 사내 대응 지침에 따른 NIA 프로젝트 점검 및 조치 계획

---

## 1. 공격 개요

`plain-crypto-js`는 정상 패키지(`crypto-js`)를 사칭한 악성 npm 패키지로, 특정 버전(`1.14.1`, `0.30.4` 등)에 악성 코드가 삽입되어 있다. 이 패키지가 의존성 트리에 포함되면 암호화 키, 환경변수, 민감 데이터가 외부로 유출될 수 있다.

### 사내 점검 명령

```powershell
Get-ChildItem -Recurse -Filter "package.json" | Select-String "1.14.1", "0.30.4", "plain-crypto-js"
```

---

## 2. NIA 프로젝트 점검 결���

### 점검 범위

| 위치 | node_modules | package.json | package-lock.json |
|------|-------------|-------------|-------------------|
| `apps/Web2024/FE/` | ✅ 점검 | ✅ 점검 | ✅ 점검 |
| 루트 `node_modules/` (pg 등) | ✅ 점검 | — | — |

### 점검 결과

| 점검 항목 | 결과 |
|----------|------|
| `plain-crypto-js` 직접 의존 | **미발견** — package.json에 없음 |
| `plain-crypto-js` 간접 의존 | **미발견** — package-lock.json에 없음 |
| `plain-crypto-js` node_modules 설치 | **미발견** — 디렉토리 없음 |
| 버전 `1.14.1` 패턴 | **미발견** — package.json에 해당 버전 없음 |
| 버전 `0.30.4` 패턴 | **미발견** — package.json에 해당 버전 없음 |
| `crypto-js` npm 패키지 | **미설치** — 이전 정리에서 제거 완료 |
| 자체 암호화 구현 | **���용 중** — `tea-block.js`(FE), `TEA.java`(BE) |

### 결론: NIA 프로젝트는 현재 plain-crypto-js 공격에 **영향받지 않음**

---

## 3. 사내 점검 명령 실행 시 다량 발견되는 이유

Powershell 명령 `Select-String "1.14.1", "0.30.4"`는 **버전 문자열을 단순 텍스트 매칭**하므로 정상 패키지에서도 다량 매칭된다.

예를 들어:
- `node_modules/some-package/package.json`에 `"version": "1.14.1"` → 매칭 (하지만 정상)
- `node_modules/xxx/package.json`에 `"dependency": "^0.30.4"` → 매칭 (하지만 정상)

**핵심: `1.14.1`, `0.30.4` 매칭만으로는 위험 판단 불가. `plain-crypto-js` 문자열 매칭이 핵심이다.**

---

## 4. 권장 조치 순서

### Phase 1: 즉시 점검 (5분)

```bash
# 1. plain-crypto-js가 설치되어 있는지 확인 (가장 중요)
npm ls plain-crypto-js

# 2. node_modules에 실제 디렉토리 존재 여부
ls node_modules/plain-crypto-js 2>/dev/null && echo "위험: 발견됨!" || echo "안전: 미발견"

# 3. package-lock.json에서 확인
grep "plain-crypto-js" package-lock.json

# 4. npm audit로 알려진 취약점 확인
npm audit
```

### Phase 2: 발견 시 즉시 대응

만약 `plain-crypto-js`가 발견된다면:

```bash
# 1. 즉시 제거
npm uninstall plain-crypto-js

# 2. node_modules 전체 정리 후 재설치
rm -rf node_modules package-lock.json
npm install

# 3. 재확인
npm ls plain-crypto-js
grep "plain-crypto-js" package-lock.json

# 4. 환경변수/키 유출 가능성 점검
# - .env 파일의 API 키, DB 비밀번호 등 즉시 변경
# - 해당 서버의 환경변수 로테이션
```

### Phase 3: crypto-js 정상 사용 여부 점검

`plain-crypto-js`는 `crypto-js`를 사칭하므로, `crypto-js`를 사용하는 경우에도 점검 필요:

```bash
# crypto-js 설치 여부 확인
npm ls crypto-js

# 설치되어 있다면 무결성 확인
npm pack crypto-js --dry-run 2>&1 | head -5
```

NIA 프로젝트 현황:
- `crypto-js` → 이전 정리에서 **제거 완료** (사용하지 않음)
- 암호화 → 자체 구현(`tea-block.js`, `TEA.java`) 사용

### Phase 4: 기존 취약점 정리 (선택)

현재 `npm audit` 결과 127건의 취약점이 발견됨. 주요 항목:

| 패키지 | 심각도 | 유형 | 조치 가능 여부 |
|--------|--------|------|---------------|
| ag-grid-community/enterprise | high | Prototype Pollution | breaking change 필요 (v35) |
| axios 0.21.1 | high | CSRF, SSRF | breaking change 필요 (v1.x) |
| webpack (via @vue/cli) | high | Prototype Pollution | Vue CLI 5.x 업그레이드 필요 |
| xlsx | moderate | ReDoS | fix 없음 |
| highlight.js | critical | ReDoS | el-table-draggable 의존 |
| lint-staged | moderate | micromatch 취약 | 최신 버전 업그레이드 가능 |

> **참고**: 이 취약점들은 대부분 devDependencies이거나 breaking change가 필요한 메이저 업그레이드임.
> 공급망 공격(`plain-crypto-js`)과는 별개의 일반 취약점이므로 별도 계획으로 관리 권장.

---

## 5. 사내 보고용 요약

```
[NIA 프로젝트 plain-crypto-js 점검 결과]

점검일: 2026-04-07
점검자: sh
프로젝트: Web2024 (NIA)

1. plain-crypto-js 패키지: 미발견 (의존성 트리 전체 점검)
2. 버전 1.14.1 / 0.30.4: 해당 버전의 악성 패키지 미발견
3. crypto-js 패키지: 미설치 (이전 정리에서 제거)
4. 암호화 구현: 자체 TEA 구현 사용 (npm 외부 의존 없음)

결론: 영향 없음. 추가 조치 불필요.
```

---

## 6. 예방 조치 (이미 적용됨)

| 조치 | 상태 | 비고 |
|------|------|------|
| `crypto-js` npm 제거 | ✅ 완료 | 미사용 확인 후 제거 (260407) |
| `package-lock.json` 버전 관리 | ✅ 완료 | gitignore 해제, 커밋 추적 시작 |
| `.npmrc` 설정 | ✅ 완료 | `legacy-peer-deps=true` |
| `engines` 노드 버전 고정 | ✅ 완료 | node 22.x |
| 미사용 패키지 정리 | ✅ 완료 | 17개 패키지 제거 |

### 추가 권장 예방 조치

- [ ] CI/CD 파이프라인에 `npm audit` 자동 검사 추가
- [ ] `npm install` 시 `--ignore-scripts` 옵션 고려 (악성 postinstall 스크립트 차단)
- [ ] 정기적 의존성 점검 스케줄 수립 (월 1회)
