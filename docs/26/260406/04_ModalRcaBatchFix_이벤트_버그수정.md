# ModalRcaBatchFix — closeModal 이벤트 버그 수정

## 문제

`onClose()`에서 `$emit('closeModal')`을 실행하므로, 모달이 닫히는 모든 경우에 조치 프로세스가 시작됨.

| 동작 | 기대 | 실제 (버그) |
|------|------|------------|
| 확인 버튼 (입력 있음) | 조치 진행 | 조치 진행 |
| 확인 버튼 (입력 없음) | 경고 또는 중단 | 빈값으로 조치 진행 |
| 닫기 버튼 / X 버튼 | 단순 닫기 | 조치 진행 (버그) |

## 수정 내용

- `$emit('closeModal')` → `onClose()`에서 제거
- `handleClickFix()`(확인 버튼)에서만 emit 발생
- 빈 입력 시 경고 메시지 표시 후 중단
- 불필요한 `fixValue` 중간변수 제거

```js
// Before
handleClickFix() {
  this.fixValue = this.fixProcess
  this.close()   // → onClose() → $emit('closeModal', fixValue)
},
onClose() {
  this.$emit('closeModal', this.fixValue)  // 닫기 버튼에서도 실행됨
  this.fixProcess = ''
  this.fixValue = ''
}

// After
handleClickFix() {
  if (!this.fixProcess) {
    this.$message({ type: 'warning', message: '조치사항을 입력해 주세요.' })
    return
  }
  this.$emit('closeModal', this.fixProcess)  // 확인 버튼에서만 emit
  this.close()
},
onClose() {
  this.fixProcess = ''  // 단순 초기화만
}
```

## "조치" 기능의 실제 동작 분석

`closeModal` emit 후 부모(ModalRcaBatchProcessing)의 흐름:

```
closeFixModal(fixed)
  → this.fixed = fixed
  → handleClickProcess('FIX', '조치')
    → confirm('선택한 항목을 조치 하시겠습니까?')
    → 선택된 row.fixed = this.fixed  (프론트 로컬 데이터만 변경)
    → batchTicketList 갱신
    → $message('조치 되었습니다.')
```

- API 호출 없음 — 서버/DB에 반영되지 않음
- ACK(인지), FIN(마감)도 마찬가지로 프론트엔드 그리드만 수정
- TT일괄처리 전체가 UI 프리뷰 수준으로만 동작하는 상태
