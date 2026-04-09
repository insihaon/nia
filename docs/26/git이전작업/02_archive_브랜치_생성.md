# 02. Archive 브랜치 생성 — 원본 이력 복구

> 작성일: 2026-04-08

---

## 1. 목적

git subtree add로 합쳐진 원본 서브프로젝트들의 이력을 **명시적 브랜치로 태깅**하여,
신규 레포로 push할 때 이력이 확실히 보존되도록 한다.

---

## 2. 생성할 Archive 브랜치 목록

| # | 브랜치명 | 원본 커밋 해시 | 커밋 수 | 대상 |
|---|---------|--------------|---------|------|
| 1 | `archive/web2024-original` | `5eb858de` | 781 | Front_web_2024 원본 |
| 2 | `archive/engine2021-original` | `b25ebdcf` | 221 | EngineNiaServer2021 원본 |
| 3 | `archive/optical-pm-original` | `01d81975` | 4 | EngineNiaOpticalPm 원본 |
| 4 | `archive/ip-sdn-original` | `8976f850` | 3 | EngineNiaIpSdnOproute 원본 |
| 5 | `archive/legacy-2021-original` | `e5a024c2` | 1,023 | legacy/2021NIA 원본 |
| 6 | `archive/chatbot-tool-original` | `5e6b6f48` | 5 | library/chatbotTool 원본 |
| 7 | `archive/topology2d-original` | `bb0d53b3` | 2 | library/topology2D 원본 |

---

## 3. 실행 명령어

```bash
# 작업 디렉토리: D:\01_source_code\0. nia_source

# 1. 원본 커밋 접근 가능 확인
git cat-file -t 5eb858de   # commit 이 출력되어야 함
git cat-file -t b25ebdcf
git cat-file -t 01d81975
git cat-file -t 8976f850
git cat-file -t e5a024c2
git cat-file -t 5e6b6f48
git cat-file -t bb0d53b3

# 2. Archive 브랜치 생성
git branch archive/web2024-original 5eb858de
git branch archive/engine2021-original b25ebdcf
git branch archive/optical-pm-original 01d81975
git branch archive/ip-sdn-original 8976f850
git branch archive/legacy-2021-original e5a024c2
git branch archive/chatbot-tool-original 5e6b6f48
git branch archive/topology2d-original bb0d53b3

# 3. 생성 확인
git branch -a | grep archive/
```

---

## 4. 검증

각 브랜치의 이력이 올바른지 확인:

```bash
# 각 브랜치의 커밋 수 확인
echo "=== web2024 ===" && git log --oneline archive/web2024-original | wc -l
echo "=== engine2021 ===" && git log --oneline archive/engine2021-original | wc -l
echo "=== optical-pm ===" && git log --oneline archive/optical-pm-original | wc -l
echo "=== ip-sdn ===" && git log --oneline archive/ip-sdn-original | wc -l
echo "=== legacy-2021 ===" && git log --oneline archive/legacy-2021-original | wc -l
echo "=== chatbot-tool ===" && git log --oneline archive/chatbot-tool-original | wc -l
echo "=== topology2d ===" && git log --oneline archive/topology2d-original | wc -l
```

**기대값:**

| 브랜치 | 기대 커밋 수 |
|--------|-------------|
| web2024-original | 781 |
| engine2021-original | 221 |
| optical-pm-original | 4 |
| ip-sdn-original | 3 |
| legacy-2021-original | 1,023 |
| chatbot-tool-original | 5 |
| topology2d-original | 2 |

---

## 5. 주의사항

- archive 브랜치는 **읽기 전용** — 절대 커밋하지 않음
- 이 브랜치들은 원본 서브프로젝트의 스냅샷이므로 폴더 구조가 현재와 다름
  - 예: `archive/web2024-original`의 루트는 `Front_web_2024/` 내부 구조
  - 예: `archive/engine2021-original`의 루트는 `EngineNiaServer2021/` 내부 구조
- 브랜치 이름에 `archive/` prefix를 사용하여 실제 개발 브랜치와 구분

---

## 6. 롤백 방법

문제 발생 시 archive 브랜치 삭제:

```bash
git branch -D archive/web2024-original
git branch -D archive/engine2021-original
git branch -D archive/optical-pm-original
git branch -D archive/ip-sdn-original
git branch -D archive/legacy-2021-original
git branch -D archive/chatbot-tool-original
git branch -D archive/topology2d-original
```

> 이 단계가 완료되면 → `03_신규레포_push.md` 진행
