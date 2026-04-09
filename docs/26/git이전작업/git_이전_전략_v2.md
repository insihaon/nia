# Git 레포지토리 이전 전략 v2 — File History 보존 방식

> 작성일: 2026-04-09
> 작성자: sh
> 목표: `dloere/NIA_TOTAL_PROJECT` → `insihaon/nia` 이전, **VSCode File History 연속성 보장**

---

## 0. 사전 결정: 모노레포 vs 멀티레포

### 비교 분석

| 항목 | 모노레포 (현재 방식) | 멀티레포 (프로젝트별 분리) |
|------|---------------------|-------------------------|
| **구조** | 하나의 `.git`에 모든 프로젝트 | 상위 폴더에 A/, B/, C/, D/ 각각 `.git` |
| **Cursor/Claude Code** | 루트에서 열면 전체 코드 탐색 가능 | 상위 폴더에서 열면 전체 탐색 가능하나, git 명령은 각 하위 폴더별로 분리됨 |
| **커밋/PR** | 하나의 PR로 프론트+백엔드 동시 변경 가능 | 프로젝트별 별도 PR 필요 |
| **File History** | 경로만 유지되면 전체 이력 연속 | 각 프로젝트 내에서 자연스럽게 연속 |
| **CI/CD** | 변경 감지 로직 필요 (어떤 앱이 바뀌었나) | 프로젝트별 독립 파이프라인 |
| **용량** | clone 시 전체 다운 (897MB) | 필요한 프로젝트만 clone |
| **권한 관리** | 전체 접근 or 전무 | 프로젝트별 세분화 가능 |
| **의존성 관리** | 프로젝트간 직접 참조 가능 | 패키지 매니저 또는 서브모듈 필요 |

### NIA 프로젝트 특성 분석

| 특성 | 판단 |
|------|------|
| 프로젝트 간 코드 의존성 | 낮음 — 각 엔진은 독립 Spring Boot 앱 |
| 프로젝트 간 동시 변경 빈도 | 낮음 — Web2024 UI 작업이 대부분 |
| 개발자 수 | 소수 (1-2명) |
| CI/CD 파이프라인 | 없음 (수동 배포) |
| Cursor/Claude Code 활용 | 적극 활용 중 — 전체 코드베이스 참조가 중요 |

### 결론: 모노레포 유지 권장

**이유:**
1. Cursor/Claude Code에서 전체 코드베이스를 한 번에 참조하는 것이 핵심 장점
2. 소규모 팀이라 멀티레포의 권한 분리 이점이 없음
3. 프로젝트 간 동시 변경은 드물지만, 가끔 필요할 때 모노레포가 편리
4. 멀티레포로 전환하면 기존 `.moai/`, `.claude/` 설정을 프로젝트별로 복제해야 함

**단점 수용:**
- clone 용량이 크지만, 전체 clone은 한 번만 수행
- 향후 프로젝트가 크게 늘어나면 그때 분리 검토

---

## 1. 신규 레포 목표 구조

```
insihaon/nia (root)
├── Web2024/                    ← 원본 Front_web_2024 (FE/, BE/)
│   ├── FE/
│   └── BE/
├── EngineNiaServer2021/        ← 원본 EngineNiaServer2021
├── EngineNiaOpticalPm/         ← 원본 EngineNiaOpticalPm
├── EngineNiaIpSdnOproute/      ← 원본 EngineNiaIpSdnOproute
├── legacy/
│   └── 2021NIA/                ← 원본 2021NIA (구조 유지)
├── library/
│   ├── Front/topology2D/       ← 원본 topology2D (구조 유지)
│   └── engine/chatbotTool/     ← 원본 chatbotTool (구조 유지)
├── docs/
├── .moai/
└── .claude/
```

**변경점 vs 현재:**
- `apps/Web2024/` → `Web2024/` (루트로 승격)
- `apps/EngineNiaServer2021/` → `EngineNiaServer2021/` (루트로 승격)
- `apps/EngineNiaOpticalPm/` → `EngineNiaOpticalPm/` (루트로 승격)
- `apps/EngineNiaIpSdnOproute/` → `EngineNiaIpSdnOproute/` (루트로 승격)
- `legacy/`, `library/` → 변경 없음

---

## 2. 이력 보존 전략

### 핵심 원리

`git filter-repo`로 원본 레포의 커밋 이력 속 파일 경로를 **신규 레포 경로에 맞게 재작성**한 뒤 머지한다.
이렇게 하면 VSCode File History가 머지 이전 커밋까지 연속으로 추적한다.

### 프로젝트별 전략

| # | 프로젝트 | 원본 경로 | 신규 경로 | filter-repo 변환 | 난이도 |
|---|---------|----------|----------|-----------------|--------|
| 1 | Web2024 | `FE/`, `BE/` | `Web2024/FE/`, `Web2024/BE/` | `--path-rename :Web2024/` | **높음** (아래 상세) |
| 2 | EngineNiaServer2021 | 루트 | `EngineNiaServer2021/` | `--path-rename :EngineNiaServer2021/` | 낮음 |
| 3 | EngineNiaOpticalPm | 루트 | `EngineNiaOpticalPm/` | `--path-rename :EngineNiaOpticalPm/` | 낮음 |
| 4 | EngineNiaIpSdnOproute | 루트 | `EngineNiaIpSdnOproute/` | `--path-rename :EngineNiaIpSdnOproute/` | 낮음 |
| 5 | legacy/2021NIA | `apps/`, `library/` | `legacy/2021NIA/` | `--path-rename :legacy/2021NIA/` | 낮음 |
| 6 | chatbotTool | 루트 | `library/engine/chatbotTool/` | `--path-rename :library/engine/chatbotTool/` | 낮음 |
| 7 | topology2D | 루트 | `library/Front/topology2D/` | `--path-rename :library/Front/topology2D/` | 낮음 |

### Web2024 — 높은 난이도 상세

**문제:**
- 원본 레포 `Front_web_2024`의 루트에 `FE/`, `BE/` 존재
- 현재 모노레포에서 `apps/Web2024/FE/`, `apps/Web2024/BE/`로 경로 변경됨
- subtree 이후 **337개 커밋**이 `apps/Web2024/` 경로에서 추가 작업됨
- 이 337개 커밋의 이력도 `Web2024/` 경로로 변환 필요

**해결 접근:**
1. 원본 Front_web_2024 레포를 복제
2. `git filter-repo --path-rename :Web2024/` 로 전체 이력 경로 재작성
3. 현재 모노레포의 main 이력에서 `apps/Web2024/` → `Web2024/` 부분도 재작성 필요
4. 최종 머지 시 양쪽 이력이 `Web2024/` 경로로 통일되어 File History 연속

**주의:** 원본 Front_web_2024 레포를 더럽혀도 상관없으므로, 원본에서 직접 filter-repo 실행 가능

---

## 3. 실행 계획

### Phase 0: 사전 준비 ✅ (완료)

- [x] git bundle 백업 생성 (372MB)
- [x] 신규 레포 접근 확인 (빈 상태)
- [x] 대용량 파일 확인 (100MB 초과 없음)

### Phase 1: 작업 환경 준비

```
작업 디렉토리 구조:

D:\01_source_code\git_migration\     ← 이전 작업 전용 폴더
├── original_repos\                  ← 원본 레포 클론 (filter-repo용)
│   ├── Front_web_2024\
│   ├── EngineNiaServer2021\
│   ├── EngineNiaOpticalPm\
│   ├── EngineNiaIpSdnOproute\
│   ├── 2021NIA\
│   ├── chatbotTool\
│   └── topology2D\
├── new_nia\                         ← 신규 통합 레포
└── backup\                          ← 백업 파일
```

1. 원본 7개 레포를 각각 clone (--mirror 또는 --bare)
2. `git filter-repo` 설치 확인 (`pip install git-filter-repo`)
3. 원본 레포가 GitHub에 아직 존재하는지 확인

> **원본 레포 접근 불가 시:**
> 현재 모노레포의 git object에서 원본 이력을 추출할 수 있음
> `git branch archive/xxx <원본커밋해시>` 후 해당 브랜치를 별도 레포로 분리

### Phase 2: 원본 레포 경로 재작성 (filter-repo)

각 원본 레포에서 `git filter-repo`로 경로를 신규 구조에 맞게 변환:

```bash
# 예: Front_web_2024 → Web2024/
cd original_repos/Front_web_2024
git filter-repo --path-rename ':Web2024/'

# 예: EngineNiaServer2021 → EngineNiaServer2021/
cd original_repos/EngineNiaServer2021
git filter-repo --path-rename ':EngineNiaServer2021/'

# 예: chatbotTool → library/engine/chatbotTool/
cd original_repos/chatbotTool
git filter-repo --path-rename ':library/engine/chatbotTool/'

# 예: topology2D → library/Front/topology2D/
cd original_repos/topology2D
git filter-repo --path-rename ':library/Front/topology2D/'

# 예: 2021NIA → legacy/2021NIA/
cd original_repos/2021NIA
git filter-repo --path-rename ':legacy/2021NIA/'
```

### Phase 3: 신규 레포 구성 및 머지

```bash
# 1. 빈 레포 초기화
mkdir new_nia && cd new_nia
git init
git commit --allow-empty -m "Initial commit"

# 2. 각 원본 레포를 remote로 추가하고 머지 (순서 중요)
# --- Web2024 (가장 큰 프로젝트, 먼저) ---
git remote add web2024 ../original_repos/Front_web_2024
git fetch web2024
git merge web2024/main --allow-unrelated-histories -m "Merge Web2024 with full history"
git remote remove web2024

# --- Engine2021 ---
git remote add engine2021 ../original_repos/EngineNiaServer2021
git fetch engine2021
git merge engine2021/main --allow-unrelated-histories -m "Merge EngineNiaServer2021 with full history"
git remote remove engine2021

# --- 나머지 5개 동일 패턴 ---
# OpticalPm, IpSdn, 2021NIA, chatbotTool, topology2D
```

### Phase 4: 모노레포 후속 커밋 이식

현재 모노레포에서 subtree 이후 추가된 **337개 커밋**을 신규 레포로 가져와야 한다.

**방법 A: cherry-pick (권장)**

```bash
# 현재 모노레포에서 subtree 이후 커밋 목록 추출
git log --oneline --reverse 09ba0426..main > commits_to_cherry_pick.txt

# 신규 레포에서 cherry-pick
# 경로 변환 필요: apps/Web2024/ → Web2024/ 등
```

> 문제: cherry-pick 시 경로 불일치로 충돌 발생 가능
> 해결: 모노레포 main도 filter-repo로 경로 재작성 후 cherry-pick

**방법 B: 모노레포 main 전체를 filter-repo로 경로 변환 (권장)**

```bash
# 모노레포 복제본에서
git filter-repo \
  --path-rename 'apps/Web2024/:Web2024/' \
  --path-rename 'apps/EngineNiaServer2021/:EngineNiaServer2021/' \
  --path-rename 'apps/EngineNiaOpticalPm/:EngineNiaOpticalPm/' \
  --path-rename 'apps/EngineNiaIpSdnOproute/:EngineNiaIpSdnOproute/' \
  --path-rename 'Front_web_2024/:Web2024/'
```

이 방법이면 모노레포의 337개 후속 커밋도 경로가 자동 변환되어,
원본 이력 머지와 연결이 자연스러움.

### Phase 5: 최종 머지 전략

```
시간순 이력 그래프 (목표):

원본 Web2024 이력          모노레포 후속 이력
(Web2024/FE/...)           (Web2024/FE/...)
  A ← B ← C ← ... ←──── merge ←── D ← E ← ... ← HEAD
                              ↑
                    allow-unrelated-histories
```

**File History 동작:**
- `Web2024/FE/src/views-nia/SomeFile.vue`의 History 조회 시
- HEAD → ... → E → D → merge → C → B → A (원본 최초 커밋까지 연속)

### Phase 6: 검증

```bash
# 1. 파일 수 비교
find . -type f | wc -l                    # 신규 레포
# vs 원본 모노레포의 파일 수

# 2. File History 연속성 테스트 (핵심)
git log --follow -- Web2024/FE/src/main.js
# → 원본 레포의 최초 커밋까지 이력이 보여야 함

# 3. 각 프로젝트별 커밋 수 확인
git log --oneline -- Web2024/ | wc -l
git log --oneline -- EngineNiaServer2021/ | wc -l
# 등등
```

### Phase 7: 신규 레포 Push 및 전환

```bash
git remote add origin https://github.com/insihaon/nia
git push -u origin main
```

---

## 4. 위험 분석 및 복구 플랜

### 위험 요소

| 위험 | 심각도 | 발생 조건 | 대응 |
|------|--------|----------|------|
| 원본 레포 접근 불가 | 중 | GitHub 레포 삭제/비공개 | 현재 모노레포 object에서 추출 가능 |
| filter-repo 경로 충돌 | 중 | 같은 파일명이 다른 프로젝트에 존재 | 프로젝트별 고유 prefix로 해결됨 |
| merge 충돌 | 중 | 경로 겹침 | 프로젝트별 분리된 경로라 충돌 가능성 낮음 |
| Web2024 이력 불연속 | 높 | `Front_web_2024/` → `apps/Web2024/` → `Web2024/` 이중 변환 | 방법 B(모노레포 전체 filter-repo) 사용 |
| 커밋 해시 변경 | 낮 | filter-repo 사용 시 필연적 | 새 레포이므로 기존 해시 참조 없음 |

### 복구 플랜

**최악의 경우에도 원본은 안전:**
1. 현재 모노레포 (`dloere/NIA_TOTAL_PROJECT`)는 건드리지 않음
2. bundle 백업 (`NIA_TOTAL_PROJECT_backup_20260409.bundle`) 보유
3. 모든 작업은 별도 작업 폴더 (`git_migration/`)에서 수행

**단계별 롤백:**
- Phase 2 실패 → 원본 레포 다시 clone하여 재시도
- Phase 3 실패 → `new_nia/` 폴더 삭제 후 재시도
- Phase 4 실패 → Phase 3까지 되돌아가서 다른 방법 시도
- Phase 7 실패 → 신규 레포 비우고 재push

---

## 5. 예상 작업 시간

| Phase | 예상 소요 | 비고 |
|-------|----------|------|
| Phase 1 | 10분 | clone 7개 레포 |
| Phase 2 | 15분 | filter-repo 7회 실행 |
| Phase 3 | 15분 | merge 7회 실행 |
| Phase 4 | 30분 | 모노레포 이력 이식 (가장 까다로움) |
| Phase 5 | 10분 | 최종 머지 |
| Phase 6 | 15분 | 검증 |
| Phase 7 | 5분 | push |
| **합계** | **~2시간** | 문제없을 경우 |

---

## 6. 핵심 결정 사항

### 결정 1: 모노레포 vs 멀티레포

**→ 모노레포 유지** (Section 0 참조)

### 결정 2: Web2024 이력 이식 방법

**→ 방법 B 권장** (모노레포 전체 filter-repo)

이유: cherry-pick 337개는 경로 충돌 리스크가 높고, filter-repo로 한 번에 변환하는 것이 안전함

### 결정 3: 원본 레포 처리

**→ filter-repo로 직접 변환** (원본 더럽혀도 무방하다는 확인 받음)

### 결정 4: 커밋 해시 변경 수용

**→ 수용** (새 레포이므로 기존 해시 참조 없음)

---

## 7. 최종 실행 순서 요약

```
Phase 1: 환경 준비
    └─ 작업 폴더 생성
    └─ 7개 원본 레포 clone
    └─ git-filter-repo 설치
         │
Phase 2: 원본 이력 경로 재작성
    └─ 7개 레포 각각 filter-repo 실행
    └─ 경로 변환 결과 검증
         │
Phase 3: 신규 레포에 원본 이력 머지
    └─ 빈 레포 초기화
    └─ 7개 원본 이력 순차 머지
         │
Phase 4: 모노레포 후속 이력 이식
    └─ 모노레포 복사본 filter-repo (경로 변환)
    └─ 후속 337커밋 이식
         │
Phase 5: 최종 머지 및 정리
    └─ docs/, .moai/, .claude/ 등 비코드 파일 추가
    └─ 최종 구조 확인
         │
Phase 6: 검증
    └─ File History 연속성 테스트
    └─ 파일 수/내용 비교
    └─ VSCode에서 직접 확인
         │
Phase 7: Push 및 전환
    └─ insihaon/nia에 push
    └─ 로컬 remote 변경
```

---

Version: 2.0.0
Previous: docs/26/git이전작업/ (v1 — archive 브랜치 방식)
