import moment from 'moment'
import { getAlarmType } from '@/views-nia/js/commonFormat'
import {
  apiSelectAiDetectionInfo,
  apiSelfProcessSyslogInfo,
  apiSelfProcessTrafficInfo,
  apiSelectSopHistList,
  apiSopSyslogHistList,
  apiSendMQ,
} from '@/api/nia'

/**
 * 요청 내용 생성을 위한 데이터 수집 함수
 * @param {Object} params - 파라미터 객체
 * @param {Object} params.selectedRow - 티켓/알람 기본 정보
 * @param {Object} params.userInfo - 사용자 정보 {uid, name, agencyName}
 * @param {Object} params.options - 옵션 객체
 * @param {boolean} params.options.loadTrafficInfo - trafficInfo 로드 여부
 * @param {boolean} params.options.loadSyslogInfo - syslogInfo 로드 여부
 * @param {boolean} params.options.loadSopHist - SOP 히스토리 로드 여부
 * @param {boolean} params.options.loadAiDetection - AI 분석 정보 로드 여부
 * @returns {Promise<Object>} 수집된 데이터 객체
 */
export async function collectRequestContentData({ selectedRow, userInfo, options = {} }) {
  const {
    loadTrafficInfo = true,
    loadSyslogInfo = true,
    loadSopHist = true,
    loadAiDetection = true,
  } = options

  // sendItem 초기화
  const sendItem = {
    sender: `${userInfo.agencyName || ''} ${userInfo.name || ''}`.trim(),
    ...selectedRow,
  }

  let trafficInfo = {}
  let syslogInfo = {}
  let relatedSopList = []
  let aiDetection = null
  let isLoadAiDetectionInfo = false

  const isSyslog = selectedRow?.ticket_type === 'SYSLOG'

  try {
    // Traffic Info 로드
    if (loadTrafficInfo && !['SYSLOG', 'RT'].includes(selectedRow.ticket_type) && selectedRow.ticket_id) {
      try {
        const res = await apiSelfProcessTrafficInfo({ TICKET_ID: selectedRow.ticket_id })
        trafficInfo = res?.result[0] ?? {}
      } catch (error) {
        console.error('Traffic Info 로드 실패:', error)
      }
    }

    // Syslog Info 로드
    if (loadSyslogInfo && isSyslog && selectedRow.alarmno) {
      try {
        const res = await apiSelfProcessSyslogInfo({ ALARMNO: selectedRow.alarmno })
        syslogInfo = res?.result[0] ?? {}
      } catch (error) {
        console.error('Syslog Info 로드 실패:', error)
      }
    }

    // SOP 히스토리 로드
    if (loadSopHist) {
      try {
        let param
        let res

        switch (selectedRow.ticket_type) {
          case 'SYSLOG': {
            const { node_nm: NODE_NM, alarmloc: ALARMLOC } = syslogInfo
            param = { NODE_NM, ALARMLOC }
            if (NODE_NM || ALARMLOC) {
              res = await apiSopSyslogHistList(param)
            }
            break
          }
          case 'NTT_AI':
            res = await apiSelectSopHistList({
              TICKET_TYPE: selectedRow.ticket_type,
              NTT_TRAFFIC_TYPE: selectedRow.alarmmsg,
            })
            break
          default: {
            const { ticket_type: TICKET_TYPE, root_cause_sysnamea: ROOT_CAUSE_SYSNAMEA } = selectedRow
            if (TICKET_TYPE && ROOT_CAUSE_SYSNAMEA) {
              param = { TICKET_TYPE, ROOT_CAUSE_SYSNAMEA }
              res = await apiSelectSopHistList(param)
            }
            break
          }
        }

        relatedSopList = res?.result ?? []
      } catch (error) {
        console.error('SOP 히스토리 로드 실패:', error)
      }
    }

    // AI Detection Info 로드
    if (loadAiDetection && selectedRow.ticket_id) {
      try {
        const { fault_time: FAULT_TIME, ticket_id: TICKET_ID } = selectedRow
        const {
          root_cause_sysnamea: START_NODE,
          root_cause_porta: START_PORT,
        } = trafficInfo

        if (TICKET_ID) {
          isLoadAiDetectionInfo = true
          const param = { TICKET_ID, START_NODE, START_PORT, FAULT_TIME }
          const res = await apiSelectAiDetectionInfo(param)
          aiDetection = res?.result[0] ?? null
        }
      } catch (error) {
        console.error('AI Detection Info 로드 실패:', error)
      }
    }

    // sendItem에 추가 데이터 설정
    const { ticket_type, status } = selectedRow

    if (ticket_type !== 'RT' || status === 'INIT') {
      // AI 분석 결과 정보
      if (ticket_type === 'ATT2' && aiDetection) {
        const {
          in_bps,
          in_predict,
          in_threshold_upper,
          in_threshold_lower,
          in_anomaly,
          out_bps,
          out_predict,
          out_threshold_upper,
          out_threshold_lower,
          out_anomaly,
        } = aiDetection

        sendItem.in_bps = in_bps?.toLocaleString() || ''
        sendItem.in_predict = in_predict?.toLocaleString() || ''
        sendItem.in_threshold_upper = in_threshold_upper?.toLocaleString() || ''
        sendItem.in_threshold_lower = in_threshold_lower?.toLocaleString() || ''
        sendItem.in_anomaly = in_anomaly || ''
        sendItem.out_bps = out_bps?.toLocaleString() || ''
        sendItem.out_predict = out_predict?.toLocaleString() || ''
        sendItem.out_threshold_upper = out_threshold_upper?.toLocaleString() || ''
        sendItem.out_threshold_lower = out_threshold_lower?.toLocaleString() || ''
        sendItem.out_anomaly = out_anomaly || ''
      }

      if (['FTT', 'ATT2', 'NTT'].includes(ticket_type)) {
        sendItem.zero1_entropy = selectedRow.zero1_entropy
      }

      // 연관 SOP
      sendItem.fault_classify = relatedSopList.length !== 0 ? relatedSopList[0].fault_classify : ''
      sendItem.fault_type = relatedSopList.length !== 0 ? relatedSopList[0].fault_type : ''
      sendItem.fault_detail_content =
        relatedSopList.length !== 0 ? relatedSopList[0].fault_detail_content : ''
    }
  } catch (error) {
    console.error('데이터 수집 중 오류 발생:', error)
    throw error
  }

  return {
    sendItem,
    trafficInfo,
    syslogInfo,
    relatedSopList,
    aiDetection,
    isLoadAiDetectionInfo,
  }
}

/**
 * Base64 HTML 디코딩 함수
 * @param {string} encodedHtml - Base64로 인코딩된 HTML 문자열
 * @returns {string} 디코딩된 HTML 문자열
 */
export function decodeBase64Html(encodedHtml) {
  try {
    return decodeURIComponent(escape(atob(encodedHtml)))
  } catch (error) {
    console.error('Base64 디코딩 실패:', error)
    return ''
  }
}

/**
 * Base64 HTML 인코딩 함수
 * @param {string} htmlContent - HTML 문자열
 * @returns {string} Base64로 인코딩된 HTML 문자열
 */
export function encodeBase64Html(htmlContent) {
  try {
    return btoa(unescape(encodeURIComponent(htmlContent)))
  } catch (error) {
    console.error('Base64 인코딩 실패:', error)
    return ''
  }
}

/**
 * 요청 내용 HTML 생성 함수
 * @param {Object} params - 파라미터 객체
 * @param {Object} params.data - collectRequestContentData()의 반환값
 * @param {Array} params.selectedUsers - 선택된 수신자 목록 [{name, email}]
 * @param {string} params.mailToSystemUrl - 시스템 URL
 * @param {Function} params.formatterTimeStamp - 날짜 포맷 함수 (optional, 기본값 제공)
 * @param {boolean} params.includeButton - 메일 전송 버튼 포함 여부 (optional, 기본값: false)
 * @param {Object} params.buttonOptions - 버튼 옵션 (optional)
 * @param {string} params.buttonOptions.className - 버튼 CSS 클래스명 (optional, 기본값: 'chatbot-mail-send-btn')
 * @param {string} params.buttonOptions.wrapperClass - 버튼 래퍼 CSS 클래스명 (optional, 기본값: 'chatbot-message-body')
 * @param {boolean} params.includeSelect - 담당 직원 선택 select 박스 포함 여부 (optional, 기본값: false)
 * @param {Array} params.userList - 사용자 목록 [{name, email}] (optional, includeSelect가 true일 때 필요)
 * @returns {string} HTML 문자열
 */
export function generateRequestContentHtml({
  data,
  selectedUsers = [],
  mailToSystemUrl = '',
  formatterTimeStamp = (time, formatStr = 'YYYY-MM-DD HH:mm:ss') => {
    if (!time) return ''
    return moment(time).format(formatStr)
  },
  includeButton = false,
  buttonOptions = {},
  includeSelect = false,
  userList = [],
}) {
  const { sendItem, trafficInfo, syslogInfo, relatedSopList, aiDetection, isLoadAiDetectionInfo } = data

  const isSyslog = sendItem.ticket_type === 'SYSLOG'
  const alarmTypeText = getAlarmType(sendItem)

  let html = '<div style="display: none"><a href="' + mailToSystemUrl + '">바로가기</a><br /></div>'
  html += '<div class="text-xl font-bold"><h2>장애 상세내역 및 조치 요청서</h2></div>'

  // 발신
  html += '<div>'
  html += '<span class="sub-title font-semibold"><h4>&middot;발신</h4></span>'
  html += '<span style="margin-left: 20px">' + (sendItem.sender || '') + '</span>'
  html += '</div>'

  // AI 분석 결과 정보
  html += '<div>'
  if (isLoadAiDetectionInfo) {
    html += '<div>'
    html += '<span class="sub-title font-semibold"><h4>&middot;AI 분석 결과 정보</h4></span>'
    html += '<span class="font-italic font-semibold" style="margin-left: 20px">' + alarmTypeText + '</span>'

    if (sendItem.ticket_type === 'ATT2') {
      html += '<div>'
      html += '&emsp;<span>IN</span><br />'
      html +=
        '<span style="margin-left: 20px">- mbps: ' +
        (aiDetection !== null ? sendItem.in_bps + ' MB' : '') +
        ' <br /></span>'
      html +=
        '<span style="margin-left: 20px">- Predict: ' +
        (aiDetection !== null ? sendItem.in_predict + ' MB' : '') +
        '<br /></span>'
      html +=
        '<span style="margin-left: 20px">- Threshold_Upper: ' +
        (aiDetection !== null ? sendItem.in_threshold_upper + ' MB' : '') +
        '<br /></span>'
      html +=
        '<span style="margin-left: 20px">- Threshold_Lower: ' +
        (aiDetection !== null ? sendItem.in_threshold_lower + ' MB' : '') +
        '<br /></span>'
      html +=
        '- Anomaly: ' + (aiDetection !== null ? sendItem.in_anomaly + '' : '') + '<br />'
      html += '&emsp;<span>OUT</span><br />'
      html +=
        '<span style="margin-left: 20px">- mbps: ' +
        (aiDetection !== null ? sendItem.out_bps + ' MB' : '') +
        '<br /></span>'
      html +=
        '<span style="margin-left: 20px">- Predict : ' +
        (aiDetection !== null ? sendItem.out_predict + ' MB' : '') +
        '<br /></span>'
      html +=
        '<span style="margin-left: 20px">- Threshold_Upper: ' +
        (aiDetection !== null ? sendItem.out_threshold_upper + ' MB' : '') +
        '<br /></span>'
      html +=
        '<span style="margin-left: 20px">- Threshold_Lower: ' +
        (aiDetection !== null ? sendItem.out_threshold_lower + ' MB' : '') +
        '<br /></span>'
      html +=
        '<span style="margin-left: 20px">- Anomaly: ' +
        (aiDetection !== null ? sendItem.out_anomaly + '' : '') +
        '<br /></span>'
      html += '</div>'
    }
    html += '</div>'
  }

  // 장애 유무 판단 확률
  if (sendItem.ticket_type === 'ATT2' || sendItem.ticket_type === 'NTT') {
    html += '<div>'
    html += '<span class="font-semibold"><span style="margin-left: 20px">&middot;장애 유무 판단 확률</span><br /></span>'
    const validProb = sendItem.zero1_entropy
      ? ((1 - sendItem.zero1_entropy) * 100).toFixed(1) + '%'
      : ''
    const invalidProb = sendItem.zero1_entropy
      ? (sendItem.zero1_entropy * 100).toFixed(1) + '%'
      : ''
    html += '<span style="margin-left: 20px">&nbsp;- 유효 확률 : ' + validProb + '<br /></span>'
    html += '<span style="margin-left: 20px">&nbsp;- 무효 확률 : ' + invalidProb + '</span>'
    html += '</div>'
  }

  // FTT 장애 확률
  if (sendItem.ticket_type === 'FTT') {
    html += '<div>'
    const faultProb = sendItem.zero1_entropy
      ? (sendItem.zero1_entropy * 100).toFixed(1) + '%'
      : ''
    const nonFaultProb = sendItem.zero1_entropy
      ? ((1 - sendItem.zero1_entropy) * 100).toFixed(1) + '%'
      : ''
    html += '<span style="margin-left: 20px">- 장애 확률 : ' + faultProb + '<br /></span>'
    html += '<span style="margin-left: 20px">- 비장애 확률 : ' + nonFaultProb + '</span>'
    html += '</div>'
  }

  // NFTT 정보
  if (sendItem.ticket_type === 'NFTT') {
    html += '<div>'
    html +=
      '<span style="margin-left: 20px">- Measured Time : ' +
      (sendItem.measured_datetime || '') +
      ' <br /></span>'
    html +=
      '<span style="margin-left: 20px">- CPU 예측값 : ' + (sendItem.cpu_predicted || '') + ' <br /></span>'
    html +=
      '<span style="margin-left: 20px">- Mem 예측값 : ' + (sendItem.mem_predicted || '') + ' <br /></span>'
    html += '</div>'
  }
  html += '</div>'

  // 수신
  html += '<div>'
  html += '<span class="sub-title font-semibold"><h4>&middot;수신</h4></span>'
  if (selectedUsers.length > 0) {
    selectedUsers.forEach((user) => {
      html += '<span style="margin-left : 20px">' + (user.name || '') + '</span>'
    })
  } else {
    html += '<span style="margin-left : 20px">수신자 없음</span>'
  }
  html += '</div>'

  // 작업 요청 내용
  html += '<div>'
  html += '<span class="sub-title font-semibold"><h4>&middot;작업 요청 내용</h4></span>'
  if (isSyslog) {
    html += '<div>'
    html +=
      '<span style="margin-left: 20px">- 발생 시간 : ' +
      formatterTimeStamp(syslogInfo.alarmtime) +
      '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 알람 번호 : ' + (syslogInfo.alarmno || '') + ' <br /></span>'
    html +=
      '<span style="margin-left: 20px">- 장비명 : ' + (syslogInfo.node_nm || '') + '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 장비 번호 : ' + (syslogInfo.node_num || '') + '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 인터페이스 : ' + (syslogInfo.alarmloc || '') + '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 알람 메시지 : ' + (syslogInfo.alarmmsg || '') + '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 원본 메시지 : ' + (syslogInfo.etc || '') + '<br /></span>'
    html += '<span style="margin-left: 20px">- 상세 내용 :</span>'
    html += '</div>'
  } else {
    html += '<div>'
    if (!['NTT_AI'].includes(sendItem.ticket_type)) {
      const startNode = trafficInfo.root_cause_sysnamea || ''
      const startPort = trafficInfo.root_cause_porta || ''
      const endNode = trafficInfo.root_cause_sysnamez || ''
      const endPort = trafficInfo.root_cause_portz || ''
      html +=
        '<span style="margin-left: 20px">- 작업 요청 구간 : ' +
        startNode +
        '(' +
        startPort +
        ')' +
        ' → ' +
        endNode +
        '(' +
        endPort +
        ')' +
        '<br /></span>'
    }
    html +=
      '<span style="margin-left: 20px">- 티켓 번호 : ' + (sendItem.ticket_id || '') + '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 티켓 타입 : ' + (sendItem.ticket_type || '') + '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 발생 원인 : ' +
      (sendItem.ticket_result || sendItem.ticket_rca_result_dtl_code || '') +
      '<br /></span>'
    html +=
      '<span style="margin-left: 20px">- 발생 시간 : ' +
      formatterTimeStamp(sendItem.fault_time) +
      '<br /></span>'
    html += '<span style="margin-left: 20px">- 상세 내용 :</span>'
    html += '</div>'
  }
  html += '</div>'

  // 장애 구역 (SYSLOG)
  if (isSyslog) {
    html += '<div>'
    html += '<span class="sub-title font-semibold"><h4>&middot;장애 구역</h4></span>'
    html +=
      '<div><span style="margin-left: 20px">- 장애 구역:' +
      (sendItem.node_nm || '') +
      '(' +
      (sendItem.alarmloc || '') +
      ')' +
      '</span></div>'
    html += '</div>'
  }

  // 장애 구간 (NTT, ATT2)
  if (['NTT', 'ATT2'].includes(sendItem.ticket_type)) {
    html += '<div>'
    html += '<span class="sub-title font-semibold"><h4>&middot;장애 구간</h4></span>'
    const startNode = trafficInfo.root_cause_sysnamea || ''
    const startPort = trafficInfo.root_cause_porta || ''
    const endNode = trafficInfo.root_cause_sysnamez || ''
    const endPort = trafficInfo.root_cause_portz || ''
    html +=
      '<div>' +
      startNode +
      '(' +
      startPort +
      ')' +
      ' → ' +
      endNode +
      '(' +
      endPort +
      ')' +
      '</div>'
    html += '</div>'
  }

  // 연관 SOP
  html += '<div>'
  html += '<span class="sub-title font-semibold"><h4>&middot;연관 SOP</h4></span>'
  html += '<div>'
  html +=
    '<span style="margin-left: 20px">- 장애 구분: ' + (sendItem.fault_classify || '') + '<br /></span>'
  html += '<span style="margin-left: 20px">- 장애 유형: ' + (sendItem.fault_type || '') + '<br /></span>'
  html +=
    '<span style="margin-left: 20px">- 조치 내용: ' + (sendItem.fault_detail_content || '') + '</span>'
  html += '</div>'
  html += '</div>'

  // 메일 전송 버튼과 select 박스 추가 (includeButton이 true인 경우, button과 select는 항상 함께 생성)
  if (includeButton) {
    const buttonClassName = buttonOptions.className || 'chatbot-mail-send-btn'
    const wrapperClass = buttonOptions.wrapperClass || 'chatbot-message-body'

    // HTML 내용을 base64로 인코딩
    const encodedHtml = encodeBase64Html(html)
    const safeEncodedHtml = encodedHtml.replace(/"/g, '&quot;').replace(/'/g, '&#39;')

    // 버튼과 select 박스를 함께 배치할 컨테이너 생성
    let buttonContainerHtml = '<div class="' + wrapperClass + '" style="display: flex; align-items: center; gap: 10px; margin-top: 10px;">'

    // 담당 직원 선택 select 박스 추가 (includeSelect가 true이고 userList가 있는 경우)
    if (includeSelect && userList && userList.length > 0) {
      const selectedUserEmails = selectedUsers.map((u) => u.email)
      const userListData = userList.map((user) => ({
        email: user.email || '',
        name: user.name || '',
      }))

      const userListJson = JSON.stringify(userListData)
      const selectedUsersJson = JSON.stringify(selectedUserEmails)
      const userListEncoded = encodeBase64Html(userListJson)
      const selectedUsersEncoded = encodeBase64Html(selectedUsersJson)

      buttonContainerHtml +=
        '<div class="chatbot-user-select-wrapper" data-user-list="' +
        userListEncoded +
        '" data-selected-users="' +
        selectedUsersEncoded +
        '" data-select-id="chatbot-select-' +
        Date.now() +
        '-' +
        Math.random().toString(36).substr(2, 9) +
        '" style="flex: 1; max-width: 300px;"></div>'
    }

    // 버튼 HTML 생성
    buttonContainerHtml +=
      '<button class="' +
      buttonClassName +
      '" data-html-content="' +
      safeEncodedHtml +
      '">메일 전송</button></div>'

    html += buttonContainerHtml
  }

  return html
}

/**
 * 메일 전송 함수
 * @param {Object} params - 파라미터 객체
 * @param {string} params.htmlContent - 편집된 HTML 내용
 * @param {Array} params.receiverUsers - 수신자 목록 [{email, name}]
 * @param {Object} params.ticketInfo - 티켓 정보
 * @param {Object} params.userInfo - 발신자 정보 {uid, name}
 * @param {string} params.subject - 메일 제목 (optional)
 * @param {Function} params.apiSendMQ - API 함수 (optional, 기본값 제공)
 * @returns {Promise<Object>} API 응답
 */
export async function sendRequestContentEmail({
  htmlContent,
  receiverUsers,
  ticketInfo,
  userInfo,
  subject,
  apiSendMQ: customApiSendMQ = apiSendMQ,
}) {
  // 수신자 검증
  if (!receiverUsers || receiverUsers.length === 0) {
    throw new Error('담당 직원을 선택해주세요.')
  }

  // 메일 제목 생성
  const mailSubject =
    subject || moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss초') + ' 장비 조치 요청서'

  // HTML 내용에서 display: none 제거
  const cleanedContent = htmlContent.replace(/display:\s*none;?/gi, '')

  // 수신자 이메일 목록 생성
  const receiverUserEmails = receiverUsers
    .map((v) => v.email)
    .join(', ')
    .replace(/\s{0,}[\r\n]/gi, '\r\n')

  // 파라미터 구성
  const param = {
    mail: {
      subject: mailSubject,
      content: cleanedContent,
      receiverUser: receiverUserEmails,
    },
    ticketInfo: {
      ...ticketInfo,
      eventType: 'REQUEST_CHANGE_TICKET_STATUS',
      status: 'ACK',
      user_ids: userInfo.uid,
      detail: 'DETAIL',
      mail_content: cleanedContent,
      handling_ack_user: userInfo.name,
      ticket_result: ticketInfo.ticket_result || ticketInfo.ticket_rca_result_code,
    },
  }

  // 메일 전송
  const res = await customApiSendMQ('sendMail', param)
  return res
}

/**
 * 이메일 문자열을 파싱하여 사용자 객체 배열로 변환
 * @param {string} emailString - 쉼표로 구분된 이메일 문자열
 * @returns {Array} 사용자 객체 배열 [{email, name}]
 */
export function parseReceiverEmails(emailString) {
  if (!emailString || typeof emailString !== 'string') {
    return []
  }

  // 쉼표로 분리하고 공백 제거
  const emails = emailString
    .split(',')
    .map((email) => email.trim())
    .filter((email) => email.length > 0)

  // 이메일 형식 간단 검증
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

  return emails
    .filter((email) => emailRegex.test(email))
    .map((email) => ({
      email,
      name: email.split('@')[0], // 이메일 앞부분을 이름으로 사용
    }))
}

/**
 * 메일 전송 래퍼 함수 (콜백 지원)
 * @param {Object} params - 파라미터 객체
 * @param {string} params.htmlContent - HTML 내용
 * @param {Array} params.receiverUsers - 수신자 목록 [{email, name}]
 * @param {Object} params.ticketInfo - 티켓 정보
 * @param {Object} params.userInfo - 발신자 정보
 * @param {boolean} params.isBase64Encoded - HTML이 base64 인코딩되어 있는지 여부
 * @param {Function} params.apiSendMQ - API 함수
 * @param {Function} params.onLoading - 로딩 콜백
 * @param {Function} params.onSuccess - 성공 콜백
 * @param {Function} params.onError - 에러 콜백
 * @returns {Promise<void>}
 */
export async function handleSendEmail({
  htmlContent,
  receiverUsers,
  ticketInfo,
  userInfo,
  isBase64Encoded = false,
  apiSendMQ: customApiSendMQ = apiSendMQ,
  onLoading,
  onSuccess,
  onError,
}) {
  try {
    // 로딩 콜백 호출
    if (onLoading) {
      onLoading()
    }

    // Base64 디코딩 (필요한 경우)
    let decodedHtml = htmlContent
    if (isBase64Encoded) {
      decodedHtml = decodeBase64Html(htmlContent)
    }

    // 메일 전송
    const res = await sendRequestContentEmail({
      htmlContent: decodedHtml,
      receiverUsers,
      ticketInfo,
      userInfo,
      apiSendMQ: customApiSendMQ,
    })

    // 성공 콜백 호출
    if (onSuccess) {
      onSuccess(res)
    }
  } catch (error) {
    // 에러 콜백 호출
    if (onError) {
      onError(error)
    } else {
      console.error('메일 전송 오류:', error)
      throw error
    }
  }
}
