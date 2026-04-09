# 경보 현황 — Store 기반 Polling 구현

## 변경 이유

BottomBar의 "경보 현황"(IP Alarm / 전송 Alarm)이 `$store.state.nia`에서 데이터를 읽지만, store에 데이터를 넣는 것은 dashboard에서만 수행. dashboard가 아닌 화면에서는 항상 0건 표시.

## Before (관리 지점 2개)

```
[초기 조회]
  dashboard mounted → apiIpAlarmList() + apiTransmissionAlarmList()
    → 로컬 data 저장 + store dispatch

[실시간]
  WebSocket → dashboard에서 수신 → 로컬 갱신 + store dispatch

[표시]
  BottomBar → store에서 읽기만 (dashboard 미진입 시 0건)
```

## After (관리 지점 1개: store)

```
[초기 조회 + Polling]
  layout mounted → store.startPolling()
    → 즉시 fetchAll (IP + 전송 알람)
    → 60초마다 fetchAll 반복
    → BottomBar는 store에서 읽으므로 모든 화면에서 표시

[dashboard 진입]
  dashboard mounted → store.initialized 확인
    → 이미 초기화됨 → store 데이터를 로컬에 복사 (API 호출 안 함)
    → 아직 안됨 → store.fetchAll() 1회 호출 후 로컬에 복사

[실시간 갱신]
  WebSocket → dashboard에서 수신 → 로컬 갱신 + store dispatch (기존과 동일)

[종료]
  layout beforeDestroy → store.stopPolling()
```

## 변경 파일

| 파일 | 변경 |
|------|------|
| `store/modules/nia.js` | `fetchAll`, `startPolling`, `stopPolling` actions 추가 |
| `layout/index.vue` | mounted에서 `startPolling`, beforeDestroy에서 `stopPolling` |
| `dashBoard/index.vue` | 초기 전체 조회 API 호출 제거, store에서 데이터 복사 |

## 상세

### store/modules/nia.js

| action | 설명 |
|--------|------|
| `fetchIpAlarmList` | `apiIpAlarmList` 호출 → `SET_IP_ALARM` commit |
| `fetchTransAlarmList` | `apiTransmissionAlarmList` 호출 → `SET_TRANS_TT` commit |
| `fetchAll` | 위 두 action 병렬 실행 |
| `startPolling` | 즉시 `fetchAll` + 60초 interval. 중복 호출 방지 (`pollTimer` 체크) |
| `stopPolling` | interval 해제 |
| `insertIpNetworkList` | 하위 호환 (dashboard WebSocket에서 직접 넣을 때) |
| `insertTransmissionNetworkList` | 하위 호환 |

### layout/index.vue

```js
mounted() {
  this.$store.dispatch('nia/startPolling')
},
beforeDestroy() {
  this.$store.dispatch('nia/stopPolling')
}
```

### dashBoard/index.vue

```js
// Before: API 직접 호출
await this.onLoadIpAlarmList()
await this.onLoadTransmissionAlarmList()

// After: store에서 데이터 가져오기
if (!this.$store.state.nia.initialized) {
  await this.$store.dispatch('nia/fetchAll')
}
this.ipNetworkList = this.$store.state.nia.ipNetworkList
this.transmissionNetworkList = this.$store.state.nia.transmissionNetworkList
```

dashboard 내 `onLoadIpAlarmList(param)`는 WebSocket 이벤트에서 개별 티켓 조회(`TICKET_ID` 파라미터)에 여전히 사용되므로 메서드 자체는 유지. 초기 전체 조회만 store에 위임.

## 추가: chatbotIconButton.vue 드래그 개선

| Before | After |
|--------|-------|
| hover만으로 아이콘이 마우스를 따라감 | 마우스다운 + 5px 이상 이동 시에만 드래그 |
| 타이머 200ms 대기 후 드래그 | 즉시 반응 (`grabOffset` 기반 자연스러운 드래그) |
| 클릭/드래그 구분 불확실 | 이동 거리로 정확히 구분 (5px 미만 = 클릭, 이상 = 드래그) |
