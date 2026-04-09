# Git 레포지토리 이전 작업 가이드

> 작업 목표: `dloere/NIA_TOTAL_PROJECT` → `insihaon/nia` 이전 (전체 이력 보존)
> 작성일: 2026-04-08

---

## 문서 목차

| # | 문서 | 내용 | 상태 |
|---|------|------|------|
| 00 | [현황 분석](00_현황분석.md) | 현재 레포 구조, subtree 이력 분석, 전략 비교 | 작성 완료 |
| 01 | [사전 준비](01_사전준비.md) | 백업, 접근 권한 확인, 대용량 파일 검사 | 작성 완료 |
| 02 | [Archive 브랜치 생성](02_archive_브랜치_생성.md) | 원본 서브프로젝트 이력을 명시적 브랜치로 보존 | 작성 완료 |
| 03 | [신규 레포 Push](03_신규레포_push.md) | mirror push 실행 및 오류 대응 | 작성 완료 |
| 04 | [검증](04_검증.md) | 이력 무결성 검증 절차 | 작성 완료 |
| 05 | [전환 및 정리](05_전환_및_정리.md) | remote 전환, 안정화 기간, 기존 레포 처리 | 작성 완료 |
| 06 | [비상 복구 플랜](06_비상복구_플랜.md) | 장애 시나리오별 복구 절차 | 작성 완료 |

---

## 실행 순서 요약

```
01. 사전 준비
    └─ 백업 생성 (bundle)
    └─ 신규 레포 접근 확인
    └─ 대용량 파일 검사
         │
02. Archive 브랜치 생성
    └─ 7개 원본 이력 브랜치 생성
    └─ 커밋 수 검증
         │
03. 신규 레포 Push
    └─ remote 추가
    └─ --mirror push 실행
         │
04. 검증
    └─ 별도 클론으로 이력 비교
    └─ 커밋 수, 해시, tree 무결성 확인
         │
05. 전환 및 정리
    └─ 로컬 remote 변경
    └─ 안정화 기간 (1-2주)
    └─ 기존 레포 Archive 처리
```

---

## 핵심 이슈

이 레포는 **git subtree add**로 5개 원본 레포를 합친 통합 레포이다.
단순 push만으로는 원본 이력이 보존되지 않을 수 있으므로,
**archive 브랜치를 생성하여 원본 이력을 명시적으로 보존**하는 전략을 사용한다.

| 원본 프로젝트 | 원본 커밋 수 | archive 브랜치 |
|-------------|-------------|---------------|
| Front_web_2024 (UI) | 781 | `archive/web2024-original` |
| EngineNiaServer2021 | 221 | `archive/engine2021-original` |
| EngineNiaOpticalPm | 4 | `archive/optical-pm-original` |
| EngineNiaIpSdnOproute | 3 | `archive/ip-sdn-original` |
| legacy/2021NIA | 1,023 | `archive/legacy-2021-original` |
| library/chatbotTool | 5 | `archive/chatbot-tool-original` |
| library/topology2D | 2 | `archive/topology2d-original` |
