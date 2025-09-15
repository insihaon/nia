import { safeArray, safeString } from '@/utils'

export default {
  channels: {
    UNKNOWN: { name: 'UNKNOWN', enable: true, init: true },
    HEARTBEAT: { name: 'HEARTBEAT', enable: true, init: true },
    IPSDN_ALARM: { name: 'IPSDN_ALARM', enable: true },
    TRANS_ALARM: { name: 'TRANS_ALARM', enable: true },
    SYSTEM_MONITORING: { name: 'SYSTEM_MONITORING', enable: false }, // 현재 사용안함
  },
  message: {
    areYouSureWantToDelete: '삭제하시겠습니까?',
    areYouSureWantToSave: '저장하시겠습니까?',
    areYouSureWantToProceed: '진행하시겠습니까?',
    isInvalidOperator: '정의된 연산자가 아닙니다',
    isNotNumber: '숫자가 아닙니다',
    pleaseInputName: '이름을 입력해 주세요',
    save: '저장',
    cancel: '취소',
    error: '에러',
  },
  // authOptions: [
  //   { text: '사용자', code: 'user', value: 1, index: 0 },
  //   { text: '담당자', code: 'manager', value: 2, index: 1 },
  //   { text: '관리자', code: 'admin', value: 4, index: 2 }
  // ],
  userGrant: {
    USER: { text: '사용자', value: 1, index: 0 },
    MANAGER: { text: '담당자', value: 2, index: 1 },
    ADMIN: { text: '관리자', value: 4, index: 2 },
    // DROP:{text:"이슈편집",value:8 ,index:3},
    // ARAM:{text:"알람기능",value:16, index:4}
  },
  authManagement: {
    GRANT: { code: 'GRANT', label: '승인', index: 0 },
    REJECT: { code: 'REJECT', label: '반려', index: 1 },
    APPLY: { code: 'APPLY', label: '신청', index: 2 },
    REVOKE: { code: 'REVOKE', label: '회수', index: 3 },
  },
  authDataSet: {
    APPLY: { code: 'A', label: '요청', index: 0 },
    GRANT: { code: 'G', label: '승인', index: 1 },
    REJECT: { code: 'R', label: '반려', index: 2 },
    COMPLETE: { code: 'C', label: '완료', index: 3 },
  },
  apiAlarm: {
    onDemand: { code: 'O', state: 'OnDemand', label: '재처리' },
    batch: { code: 'B', state: 'Batch', label: '' },
    retry: { code: 'T', label: '재처리 성공' },
  },
  nia: {
    // 전송망
    transType: [
      { code: 'EMS', text: 'EMS', fnCount: (d) => safeString(d.ticket_type).trim() !== 'PF', fnFilter: (d) => safeString(d.ticket_type).trim() !== 'PF' },
      { code: 'PF', text: '광레벨', fnCount: (d) => safeString(d.ticket_type).trim() === 'PF', fnFilter: (d) => safeString(d.ticket_type).trim() === 'PF' },
    ],
    // IP-SDN
    ipType: [
      { code: 'NFTT', text: '장비부하장애', fnCount: (d) => safeString(d.ticket_type).trim() === 'NFTT', fnFilter: (d) => safeString(d.ticket_type).trim() === 'NFTT' },
      { code: 'RT', text: '장애', fnCount: (d) => safeString(d.ticket_type).trim() === 'RT', fnFilter: (d) => safeString(d.ticket_type).trim() === 'RT' },
      { code: 'FTT', text: '비장애', fnCount: (d) => safeString(d.ticket_type).trim() === 'FTT', fnFilter: (d) => safeString(d.ticket_type).trim() === 'FTT' },
      { code: 'ATT2', text: '이상 트래픽', fnCount: (d) => safeString(d.ticket_type).trim() === 'ATT2', fnFilter: (d) => safeString(d.ticket_type).trim() === 'ATT2' },
      { code: 'NTT', text: '유해 트래픽', fnCount: (d) => safeString(d.ticket_type).trim() === 'NTT', fnFilter: (d) => safeString(d.ticket_type).trim() === 'NTT' },
      { code: 'SYSLOG', text: 'SYSLOG', fnCount: (d) => safeString(d.ticket_type).trim() === 'SYSLOG', fnFilter: (d) => safeString(d.ticket_type).trim() === 'SYSLOG' },
    ],
    ipAlarmType: [
      { code: 'ALARM', text: 'ALARM', fnCount: (d) => ['ATT2', 'NTT'].includes(safeString(d.ticket_type).trim()) || safeString(d.ticket_type).trim() === 'SYSLOG', fnFilter: (d) => ['ATT2', 'NTT'].includes(safeString(d.ticket_type).trim()) || safeString(d.ticket_type).trim() === 'SYSLOG' },
      { code: 'TRAFFIC', text: 'TRAFFIC', fnCount: (d) => ['ATT2', 'NTT'].includes(safeString(d.ticket_type).trim()), fnFilter: (d) => ['ATT2', 'NTT'].includes(safeString(d.ticket_type).trim()) },
      { code: 'SYSLOG', text: 'SYSLOG', fnCount: (d) => safeString(d.ticket_type).trim() === 'SYSLOG', fnFilter: (d) => safeString(d.ticket_type).trim() === 'SYSLOG' },
    ],
    statusType: [
      { code: 'INIT', hex: '', text: '발생', fnCount: (d) => safeString(d.status).trim() === 'INIT', fnFilter: (d) => safeString(d.status).trim() === 'INIT' },
      { code: 'ACK', hex: '', text: '인지', fnCount: (d) => safeString(d.status).trim() === 'ACK', fnFilter: (d) => safeString(d.status).trim() === 'ACK' },
      { code: 'FIN', hex: '', text: '수동마감', fnCount: (d) => safeString(d.status).trim() === 'FIN', fnFilter: (d) => safeString(d.status).trim() === 'FIN' },
      { code: 'AUTO_FIN', hex: '', text: '자동마감', fnCount: (d) => safeString(d.status).trim() === 'AUTO_FIN', fnFilter: (d) => safeString(d.status).trim() === 'AUTO_FIN' },
    ],
    chatbotCommand: {
      saveExcel: { label: '엑셀저장', value: 'saveExcel' },
      tabSwitching: { label: '탭 전환', value: 'tabSwitching' },
      fin: { label: '마감', value: 'fin' },
      configTest: { label: '조치', value: 'configTest' },
      remote: { label: '원격제어', value: 'remote' },
      dataSnapshot: { label: '데이터스냅샷', value: 'dataSnapshot' },
      requestForAction: { label: '상황전파', value: 'requestForAction' },
      edit: { label: '수정', value: 'edit' },
      mailSend: { label: '메일전송', value: 'mailSend' },
      search: { label: '검색', value: 'search' },

      refresh: { label: '새로고침', value: 'refresh' },
      topologyTypeChange: { label: '토폴로지 타입 변경', value: 'topologyTypeChange' },
      wholeZoom: { label: '줌 전체보기', value: 'wholeZoom' },
      nodeZoomTest: { label: '노드 줌인 테스트', value: 'nodeZoomTest' },
      save: { label: '저장', value: 'save' },
      linkZoomTest: { label: '링크 줌인 테스트', value: 'linkZoomTest' },
      labelToggle: { label: '라벨 토글', value: 'labelToggle' },
    },
    chatbotKeyMap: {
      /*
        parameterKey는
          - parameter를 셋팅하기 위한, key이다.
          - popup과 router의 name이 일치하지 않는 문제가 있으므로 맞추기 위함이다.
          - 따라서 parameterKey는 router와 일치시킨다. 그리고 만약 router가 없으면 생성한다.
        dialogNm 은
          - dialogOpenMixin에 등록된 dialog의 key이다.
      */
      sopHistory: { popupName: 'SOP이력조회', parameterKey: 'SopHistory', dialogNm: 'sopHistory' },
      processFin: { popupName: '마감', parameterKey: 'processFin', dialogNm: 'processFin' },
      configTest: { popupName: '조치', parameterKey: 'configTest', dialogNm: 'configTest' },
      requestForAction: { popupName: '상황전파', parameterKey: 'requestForAction', dialogNm: 'requestForAction' },
      aiResponse: { popupName: 'AI 장애대응', parameterKey: 'aiResponse', dialogNm: 'aiResponse' },
      niaTopology: { popupName: '토폴로지', parameterKey: 'niaTopology', dialogNm: 'niaTopology' },
      disabilityStatusHistoryManagement: { popupName: '장애현황 및 이력관리', parameterKey: 'DisabilityStatusHistoryManagement', dialogNm: 'disabilityStatusHistoryManagement' },
    },
    chatbotIcon: {
      success: '✅',
      move: '➡️',
      noAction: '➖',
      openPopup: '↗️'
    }

  },
  ipms: {
    ipInfoOptions: [
      { value: 'CV0001', label: 'IPv4' },
      { value: 'CV0002', label: 'IPv6' },
      { value: 'SAID', label: 'SAID' },
      { value: 'SLLNUM', label: '전용번호' },
      { value: 'SCONNALIAS', label: '수용회선명' },
    ],
  },
}

export function objectToArray(obj) {
  return Object.entries(obj).map(i => i[1])
}
