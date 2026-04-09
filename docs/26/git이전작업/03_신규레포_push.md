# 03. 신규 레포 Push

> 작성일: 2026-04-08

---

## 1. 목적

현재 레포의 **모든 이력** (main + archive 브랜치 + 태그)을 신규 레포로 전송한다.

---

## 2. Push 전략

### 2.1 방법: `--mirror` push

```bash
# mirror push는 모든 refs (branches, tags, notes)와 
# 해당 refs에서 reachable한 모든 objects를 전송함
git push --mirror https://github.com/insihaon/nia.git
```

**`--mirror`가 전송하는 것:**
- `main` 브랜치 (1,122 커밋)
- `archive/*` 브랜치 7개 (원본 서브프로젝트 이력)
- 모든 태그 (있는 경우)
- subtree merge 커밋의 양쪽 부모 이력

**`--mirror`의 주의사항:**
- 신규 레포의 기존 ref를 **덮어씀** → 빈 레포에서만 사용
- remote HEAD도 설정됨

### 2.2 대안: 개별 브랜치 push

mirror가 부담스러운 경우 개별 push 가능:

```bash
# 1. 신규 remote 추가
git remote add nia https://github.com/insihaon/nia.git

# 2. main 브랜치 push
git push nia main

# 3. archive 브랜치 push
git push nia archive/web2024-original
git push nia archive/engine2021-original
git push nia archive/optical-pm-original
git push nia archive/ip-sdn-original
git push nia archive/legacy-2021-original
git push nia archive/chatbot-tool-original
git push nia archive/topology2d-original

# 4. 태그 push (있는 경우)
git push nia --tags
```

---

## 3. 실행 순서

### Step 1: Remote 추가

```bash
git remote add nia https://github.com/insihaon/nia.git
git remote -v
```

### Step 2: 연결 테스트

```bash
git ls-remote nia
```

### Step 3: Push 실행

```bash
# 옵션 A: mirror push (권장, 빈 레포일 때)
git push --mirror nia

# 옵션 B: 개별 push
git push nia main
git push nia --all        # 모든 브랜치
git push nia --tags       # 모든 태그
```

### Step 4: Push 후 확인

```bash
# 신규 레포에서 모든 브랜치 확인
git ls-remote nia

# 또는 GitHub 웹에서 직접 확인
# https://github.com/insihaon/nia/branches
```

---

## 4. 예상 소요 시간

| 항목 | 예상 |
|------|------|
| 전송 데이터 | ~897 MB (압축 후 줄어듦) |
| 네트워크 속도 10Mbps 기준 | ~12분 |
| 네트워크 속도 100Mbps 기준 | ~1.5분 |
| GitHub 서버 처리 | ~1-3분 |

---

## 5. 오류 대응

### 5.1 `remote: error: GH001: Large files detected`

100MB 이상 파일이 있는 경우:

```bash
# 대용량 파일 식별
git rev-list --objects --all | \
  git cat-file --batch-check='%(objecttype) %(objectname) %(objectsize) %(rest)' | \
  awk '/^blob/ && $3 > 104857600 {print $3, $4}' | \
  sort -rnk1

# 해결: Git LFS로 전환 또는 git filter-repo로 제거
# (별도 문서로 대응 계획 작성 필요)
```

### 5.2 `error: failed to push some refs`

신규 레포에 기존 커밋이 있는 경우:

```bash
# 신규 레포가 비어있는지 확인
git ls-remote nia

# 비어있지 않으면 --force 필요 (주의!)
# 또는 신규 레포를 삭제 후 재생성
```

### 5.3 인증 실패

```bash
# GitHub PAT (Personal Access Token) 사용
git remote set-url nia https://<TOKEN>@github.com/insihaon/nia.git

# 또는 SSH 사용
git remote set-url nia git@github.com:insihaon/nia.git
```

### 5.4 타임아웃 / 네트워크 오류

```bash
# git 버퍼 크기 증가
git config http.postBuffer 524288000

# 재시도 — git push는 idempotent하므로 안전하게 재시도 가능
git push nia --all
```

---

## 6. 완료 기준

- [ ] `git ls-remote nia` 에서 main + archive/* 브랜치 모두 확인
- [ ] GitHub 웹에서 main 브랜치 커밋 이력 확인 (1,122 커밋)
- [ ] GitHub 웹에서 archive 브랜치 7개 존재 확인
- [ ] 각 archive 브랜치의 커밋 수가 기대값과 일치

> 이 단계가 완료되면 → `04_검증.md` 진행
