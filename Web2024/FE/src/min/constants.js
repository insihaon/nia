import { safeString } from '@/utils'

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
      { code: 'ATT2', text: '이상 트래픽(Model-A)', fnCount: (d) => safeString(d.ticket_type).trim() === 'ATT2', fnFilter: (d) => safeString(d.ticket_type).trim() === 'ATT2' },
      { code: 'ATT2_AI', text: '이상 트래픽(Model-C)', fnCount: (d) => safeString(d.ticket_type).trim() === 'ATT2_AI', fnFilter: (d) => safeString(d.ticket_type).trim() === 'ATT2_AI' },
      { code: 'NTT', text: '유해 트래픽(Model-A)', fnCount: (d) => safeString(d.ticket_type).trim() === 'NTT', fnFilter: (d) => safeString(d.ticket_type).trim() === 'NTT' },
      { code: 'NTT_AI', text: '유해 트래픽(Model-C)', fnCount: (d) => safeString(d.ticket_type).trim() === 'NTT_AI', fnFilter: (d) => safeString(d.ticket_type).trim() === 'NTT_AI' },
      { code: 'SYSLOG', text: 'SYSLOG', fnCount: (d) => safeString(d.ticket_type).trim() === 'SYSLOG', fnFilter: (d) => safeString(d.ticket_type).trim() === 'SYSLOG' },
    ],
    ipAlarmType: [
      { code: 'ALARM', text: 'ALARM', fnCount: (d) => ['ATT2', 'NTT', 'ATT2_AI', 'NTT_AI'].includes(safeString(d.ticket_type).trim()) || safeString(d.ticket_type).trim() === 'SYSLOG', fnFilter: (d) => ['ATT2', 'NTT', 'ATT2_AI', 'NTT_AI'].includes(safeString(d.ticket_type).trim()) || safeString(d.ticket_type).trim() === 'SYSLOG' },
      { code: 'TRAFFIC', text: 'TRAFFIC', fnCount: (d) => ['ATT2', 'NTT', 'ATT2_AI', 'NTT_AI'].includes(safeString(d.ticket_type).trim()), fnFilter: (d) => ['ATT2', 'NTT', 'ATT2_AI', 'NTT_AI'].includes(safeString(d.ticket_type).trim()) },
      { code: 'SYSLOG', text: 'SYSLOG', fnCount: (d) => safeString(d.ticket_type).trim() === 'SYSLOG', fnFilter: (d) => safeString(d.ticket_type).trim() === 'SYSLOG' },
    ],
    statusType: [
      { code: 'INIT', hex: '', text: '발생', fnCount: (d) => safeString(d.status).trim() === 'INIT', fnFilter: (d) => safeString(d.status).trim() === 'INIT' },
      { code: 'ACK', hex: '', text: '인지', fnCount: (d) => safeString(d.status).trim() === 'ACK', fnFilter: (d) => safeString(d.status).trim() === 'ACK' },
      { code: 'FIN', hex: '', text: '수동마감', fnCount: (d) => safeString(d.status).trim() === 'FIN', fnFilter: (d) => safeString(d.status).trim() === 'FIN' },
      { code: 'AUTO_FIN', hex: '', text: '자동마감', fnCount: (d) => safeString(d.status).trim() === 'AUTO_FIN', fnFilter: (d) => safeString(d.status).trim() === 'AUTO_FIN' },
    ],
    chatbotCommand: {
      // 아래 command의 action이 DB의 action과 일치해야합니다.
      saveExcel: { label: '엑셀저장', action: 'saveExcel' },
      tabSwitching: { label: '탭 전환', action: 'tabSwitching' },
      fin: { label: '마감처리', action: 'fin' },
      configTest: { label: '조치처리', action: 'configTest' },
      remote: { label: '원격제어 실행', action: 'remote' },
      dataSnapshot: { label: '데이터스냅샷', action: 'dataSnapshot' },
      requestForAction: { label: '상황전파', action: 'requestForAction' },
      edit: { label: '수정', action: 'edit' },
      mailSend: { label: '메일전송', action: 'mailSend' },
      search: { label: '검색', action: 'search' },
      refresh: { label: '새로고침', action: 'refresh' },
      topologyTypeChange: { label: '토폴로지 타입 변경', action: 'topologyTypeChange' },
      wholeZoom: { label: '줌 전체보기', action: 'wholeZoom' },
      nodeZoomTest: { label: '노드 줌인 테스트', action: 'nodeZoomTest' },
      save: { label: '저장', action: 'save' },
      linkZoomTest: { label: '링크 줌인 테스트', action: 'linkZoomTest' },
      labelToggle: { label: '라벨 토글', action: 'labelToggle' },
      packetListAll: { label: '전체 패킷 리스트', action: 'packetListAll' },
      trendAnalysis: { label: '트렌드 분석', action: 'trendAnalysis' },
      sopEdit: { label: 'SOP 편집 팝업', action: 'sopEdit' },

      // [집중 경보]
      focusModeCheckAlarm: { label: '티켓상세확인', action: 'focusModeCheckAlarm' },

      // 대시보드
      onReceivedIpsdnTicketEvent: { label: 'IP-SDN 경보 발생', action: 'onReceivedIpsdnTicketEvent' },
      onReceivedTransTicketEvent: { label: 'Trans 경보 발생', action: 'onReceivedTransTicketEvent' },
    },
    chatbotKeyMap: {
      /*
        parameterKey는
          - parameter를 셋팅하기 위한, key이다.
          - popup과 router의 name이 일치하지 않는 문제가 있으므로 맞추기 위함이다.
          - 따라서 parameterKey는 router와 일치시킨다. 그리고 만약 router가 없으면 생성한다.
        dialogNm 은
          - dialogOpenMixin에 등록된 dialog의 key이다.
          - DB의 popup값과 일치해야한다.
      */
      sopHistory: { key: 'sopHistory', popupName: 'SOP이력조회 팝업', parameterKey: 'SopHistory', dialogNm: 'sopHistory' },
      processFin: { key: 'processFin', popupName: '마감 팝업', parameterKey: 'processFin', dialogNm: 'processFin' },
      configTest: { key: 'configTest', popupName: '조치 팝업', parameterKey: 'configTest', dialogNm: 'configTest' },
      requestForAction: { key: 'requestForAction', popupName: '상황전파 팝업', parameterKey: 'requestForAction', dialogNm: 'requestForAction' },
      aiResponse: { key: 'aiResponse', popupName: 'AI 장애대응 팝업', parameterKey: 'aiResponse', dialogNm: 'aiResponse' },
      aiResponse_NTT_AI: { key: 'aiResponse_NTT_AI', popupName: 'AI 장애대응 팝업', parameterKey: 'aiResponse_NTT_AI', dialogNm: 'aiResponse_NTT_AI' },
      aiResponse_ATT_AI: { key: 'aiResponse_ATT_AI', popupName: 'AI 장애대응 팝업', parameterKey: 'aiResponse_ATT_AI', dialogNm: 'aiResponse_ATT_AI' },
      niaTopology: { key: 'niaTopology', popupName: '토폴로지 팝업', parameterKey: 'niaTopology', dialogNm: 'niaTopology' },
      disabilityStatusHistoryManagement: { key: 'disabilityStatusHistoryManagement', popupName: '장애이력 팝업', parameterKey: 'DisabilityStatusHistoryManagement', dialogNm: 'disabilityStatusHistoryManagement' },
    },

    // chatbotDashboardPopupMap: {
    //   'sopHistory': { label: 'SOP이력', key: 'sopHistory' },
    //   'processFin': { label: '마감', key: 'processFin' },
    //   'configTest': { label: '조치', key: 'configTest' },
    //   'aiResponse': { label: '장애대응', key: 'aiResponse' },
    //   'aiResponse_ATT_AI': { label: 'AIB장애대응', key: 'aiResponse_ATT_AI' },
    //   'requestForAction': { label: '상황전파', key: 'requestForAction' },
    //   'niaTopology': { label: '토폴로지', key: 'niaTopology' },
    // },
    // chatbotIcon: {
    //   // success: '✅' ,
    //   success: '<i class="el-icon-warning" style="color: #67C23A;"></i>',
    //   move: '➡️',
    //   noAction: '➖',
    //   openPopup: '↗️',
    //   popupWarning: '❌',
    //   assistantIcon: '💬',
    //   Information: 'ℹ️',
    //   WarningSign: '⚠️',
    //   Tip: '💡'
    // },

    chatbotIcon: {
      // 성공: 초록색 (#67C23A)
      success: '<i class="el-icon-success" style="color: #67C23A; vertical-align: middle;"></i>&nbsp;',

      // 이동/화살표: 기본 텍스트 색상 또는 파란색계열 (#409EFF)
      move: '<i class="el-icon-right" style="color: #409EFF; vertical-align: middle; font-weight: bold;"></i>&nbsp;',

      // 액션 없음: 회색 (#909399)
      noAction: '<i class="el-icon-minus" style="color: #909399; vertical-align: middle;"></i>&nbsp;',

      // 팝업 열기: 파란색 (#409EFF)
      openPopup: '<i class="el-icon-top-right" style="color: #409EFF; vertical-align: middle;"></i>&nbsp;',

      // 팝업 경고/에러: 빨간색 (#F56C6C)
      popupWarning: '<i class="el-icon-error" style="color: #F56C6C; vertical-align: middle;"></i>&nbsp;',

      // 어시스턴트 아이콘: 보라색 또는 브랜드 컬러 (#7030a0)
      assistantIcon: '<i class="el-icon-chat-dot-round" style="color: #409EFF; vertical-align: middle;"></i>&nbsp;',

      // 정보: 하늘색 (#409EFF)
      Information: '<i class="el-icon-info" style="color: #409EFF; vertical-align: middle;"></i>&nbsp;',

      // 경고 표지: 주황색 (#E6A23C)
      WarningSign: '<i class="el-icon-warning" style="color: #E6A23C; vertical-align: middle;"></i>&nbsp;',

      // 팁: 노란색/금색 (#E6A23C)
      Tip: '<i class="el-icon-sunny" style="color: #F7BA2A; vertical-align: middle; font-weight: bold;"></i>&nbsp;',

      // 포커싱 (대안): 별 모양 강조 (#F7BA2A)
      focusing: '<i class="el-icon-aim" style="color: #F7BA2A; vertical-align: middle; font-weight: bold;"></i>&nbsp;',

    },

    chatbotContent: {
      processHeaderText: '<span style="font-size:15px; font-weight: 700; margin-bottom: 3px">작업 흐름</span>',
      analysisTipHeaderText: '<span style="font-size:15px; font-weight: 700; margin-bottom: 3px">분석 팁</span>'
    },

    chatType: {
      botAnswer: 'bot-answer',
      botAlert: 'bot-alert',
      user: 'user'
    },

    chatbotMode: {
      'questionMode': 'questionMode',
      'alarmFocusMode': 'alarmFocusMode'
    },
    chatbotActiontype: {
      'expert': 'expert',
      'assist': 'assist'
    },
    chatbotComment: {
      lastComment: `어떤 기능을 도와드릴까요?`,
      parameterChange: `현재 감시 중인 티켓 기준으로 정보가 자동 반영되었습니다.`
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

  RcaTicketStatus: {
    INIT: { text: '발생', code: 'INIT', index: 0, hex: '#b14948', fnFilter: d => d?.status === 'INIT' },
    ACK: { text: '인지', code: 'ACK', index: 1, hex: '#f7aa17', fnFilter: d => d?.status === 'ACK' },
    FIN: { text: '마감', code: 'FIN', index: 2, hex: '#52a43a', fnFilter: d => d?.status === 'FIN' },
    AUTO_FIN: { text: '자동마감'/* (10분이상) */, code: 'AUTO_FIN', index: 3, hex: '#adcc1e', fnFilter: d => d?.status === 'AUTO_FIN' }
    // NATURE: { text: '자동마감(10분이내)', code: 'NATURE', index: 4, hex: '#70802b', fnFilter: d => d?.status === 'NATURE' }
  },
}

export function objectToArray(obj) {
  return Object.entries(obj).map(i => i[1])
}
