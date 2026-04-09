# ModalRcaProcessHandling — user.information 버그 수정

## 문제

`ModalRcaProcessHandling.vue`의 `setUserInfo()` 메서드에서 `user.information` 객체를 참조하고 있으나,
NIA Vuex store(`store/modules/user.js`)에는 `information` 속성이 존재하지 않음.

```js
// Before (런타임 에러: Cannot read properties of undefined)
handling_dept: user.information.deptName || '',
handling_agency: user.information.agencyName || '',
```

## 원인

다른 프로젝트(untact)에서 가져온 코드로, store 구조가 다름.

| 항목 | untact (원본) | NIA (현재) |
|------|-------------|-----------|
| 사용자 정보 경로 | `$store.state.user.information` | `$store.state.user.info` |

## NIA user.info 필드 확인

| 필드 | 의미 | 비고 |
|------|------|------|
| `deptName` | 소속 팀 | `DbUser.java:47` |
| `agencyName` | 분류 (NOC/EMS) | `DbUser.java:49`, 회원가입 시 라디오 선택 |

- `agencyName`은 "소속 본부"가 아니라 **"분류"** (NOC 또는 EMS)
- 기존 주석 `// 소속본부`는 부정확

## 수정 내용

```js
// After
handling_dept: user.info?.deptName || '', // 소속 팀
handling_agency: user.info?.agencyName || '', // 분류 (NOC/EMS)
```

- `user.information` → `user.info?` (optional chaining 포함)
- 주석 `소속본부` → `분류 (NOC/EMS)` 으로 정정
