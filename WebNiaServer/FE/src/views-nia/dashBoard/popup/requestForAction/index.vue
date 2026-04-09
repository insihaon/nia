<template>
  <div v-loading="requestForActionLoading" :class="{ [name]: true }" style="height: 100%">
    <el-row class="w-full">
      <el-col class="p-1" :span="isMobile ? 24 : 12">
        <el-card shadow="never" :body-style="{ padding: '10px' }" style="height: 150px">
          <div slot="header">
            <div>
              <span><i class="el-icon-document" /> {{ isSyslog ? '알람' : '티켓' }} 상세 정보</span>
            </div>
          </div>
          <el-col>
            <el-table v-if="isSyslog" ref="table" size="mini" :data="[syslogInfo]" class="w-100" :height="90" border fit>
              <el-table-column v-for="col in defineSyslogDetailTable" :key="col.prop" :prop="col.prop" :label="col.name" :width="col.width" />
            </el-table>
            <el-table v-else ref="table" size="mini" :data="[sendItem]" class="w-100" :height="80" border fit>
              <el-table-column v-for="col in defineTicketDetailTable" :key="col.prop" :prop="col.prop" :formatter="col.formatter" :label="col.name" :width="col.width" />
            </el-table>
          </el-col>
        </el-card>
        <el-card shadow="never" :body-style="{ padding: '10px', height: '250px' }" class="mt-1">
          <div slot="header">
            <div>
              <span><i class="el-icon-document" /> 연관 SOP 리스트</span>
            </div>
          </div>
          <el-col class="style : 30%">
            <el-table ref="sopTable" :size="'mini'" :data="relatedSopList" class="w-100" :height="230" border fit>
              <el-table-column v-for="col in sopHistColumn" :key="col.name" :prop="col.prop" :label="col.name" :width="col.width" :formatter="col.formatter" />
            </el-table>
          </el-col>
        </el-card>
        <el-card shadow="never" :body-style="{ padding: '10px', height: '500px' }" class="mt-1">
          <div slot="header">
            <div>
              <div><i class="el-icon-document" /> 담당 직원 정보</div>
            </div>
          </div>
          <el-col style="height: 100%">
            <el-table ref="employeeTable" :size="'mini'" :data="userList" class="w-100" :height="480" border fit @selection-change="onChangeRowSelected">
              <el-table-column type="selection" align="center" width="40" />
              <el-table-column v-for="col in userInfoColumn" :key="col.prop" :prop="col.prop" :label="col.name" :width="col.width" />
            </el-table>
          </el-col>
        </el-card>
      </el-col>
      <el-col class="p-1" :span="isMobile ? 24 : 12">
        <el-card shadow="never" :body-style="{ padding: '10px' }">
          <div slot="header">
            <div>
              <div><i class="el-icon-document" /> 요청 내용</div>
            </div>
          </div>
          <el-col>
            <div
              id="mail-content"
              v-loading="containerLoading"
              contenteditable="true"
              class="w-100 d-flex flex-column rounded shadow-sm leading-7 pl-5 w-50 overflow-y-auto text-left"
              :style="{ height: 945 + 'px', border: 'solid 1px lightgray' }"
              @input="onMailContentChange"
              v-html="mailContentHtml"
            ></div>
          </el-col>
        </el-card>
      </el-col>

      <!-- </el-row> -->
      <!-- </el-row> -->
    </el-row>
    <el-row>
      <el-col align="right" class="mt-1">
        <el-button v-if="isSyslog" size="mini" type="primary" icon="el-icon-camera" @click.native="fn_openWindow('snapShot', selectedRow)"> 데이터 스냅샷 </el-button>
        <el-button v-if="isSyslog" size="mini" type="primary" @click.native="fn_openWindow('configTest', selectedRow)"> 시험 </el-button>
        <el-button size="mini" icon="el-icon-edit-outline" @click.native="onClickFin()">
          {{ sendItem.status == 'FIN' || sendItem.status == 'AUTO_FIN' ? '수정' : '마감' }}
        </el-button>
        <!-- :disabled="sendItem.status != 'INIT'" -->
        <el-button size="mini" @click.native="onClickEmailSender()"> 메일 전송 </el-button>
        <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
          {{ $t('exit') }}
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { Base } from '@/min/Base'
// import elDragDialog from '@/directive/el-drag-dialog'
import { formatterTime, getAlarmType } from '@/views-nia/js/commonFormat'
import { apiSendMQ, apiSelectUserList, apiSelectAiDetectionInfo, apiSelfProcessSyslogInfo, apiSelfProcessTrafficInfo, apiSelectSopHistList, apiSopSyslogHistList } from '@/api/nia'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'

import { mapState } from 'vuex'
import constants from '@/min/constants'
import { getChatbotTicketData, getWindowActionList, getInvisibleSpanParameter, getNiaRouterPathByName, makeOpenPopupNumberText } from '@/views-nia/js/commonNiaFunction'

import _ from 'lodash'

import niaObserverMixin from '@/mixin/niaObserverMixin'
import moment from 'moment'

/**
 * 요청 내용 생성을 위한 데이터 수집 함수
 */
async function collectRequestContentData({ selectedRow, userInfo, options = {} }) {
  const {
    loadTrafficInfo = true,
    loadSyslogInfo = true,
    loadSopHist = true,
    loadAiDetection = true,
  } = options

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
    if (loadTrafficInfo && !['SYSLOG', 'RT'].includes(selectedRow.ticket_type) && selectedRow.ticket_id) {
      try {
        const res = await apiSelfProcessTrafficInfo({ TICKET_ID: selectedRow.ticket_id })
        trafficInfo = res?.result[0] ?? {}
      } catch (error) {
        console.error('Traffic Info 로드 실패:', error)
      }
    }

    if (loadSyslogInfo && isSyslog && selectedRow.alarmno) {
      try {
        const res = await apiSelfProcessSyslogInfo({ ALARMNO: selectedRow.alarmno })
        syslogInfo = res?.result[0] ?? {}
      } catch (error) {
        console.error('Syslog Info 로드 실패:', error)
      }
    }

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

    const { ticket_type, status } = selectedRow

    if (ticket_type !== 'RT' || status === 'INIT') {
      if (ticket_type === 'ATT2' && aiDetection) {
        const {
          in_bps, in_predict, in_threshold_upper, in_threshold_lower, in_anomaly,
          out_bps, out_predict, out_threshold_upper, out_threshold_lower, out_anomaly,
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
 */
function decodeBase64Html(encodedHtml) {
  try {
    return decodeURIComponent(escape(atob(encodedHtml)))
  } catch (error) {
    console.error('Base64 디코딩 실패:', error)
    return ''
  }
}

/**
 * Base64 HTML 인코딩 함수
 */
function encodeBase64Html(htmlContent) {
  try {
    return btoa(unescape(encodeURIComponent(htmlContent)))
  } catch (error) {
    console.error('Base64 인코딩 실패:', error)
    return ''
  }
}

/**
 * 요청 내용 HTML 생성 함수
 */
function generateRequestContentHtml({
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

  html += '<div>'
  html += '<span class="sub-title font-semibold"><h4>&middot;발신</h4></span>'
  html += '<span style="margin-left: 20px">' + (sendItem.sender || '') + '</span>'
  html += '</div>'

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
        startNode + '(' + startPort + ')' + ' → ' + endNode + '(' + endPort + ')' +
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

  if (isSyslog) {
    html += '<div>'
    html += '<span class="sub-title font-semibold"><h4>&middot;장애 구역</h4></span>'
    html +=
      '<div><span style="margin-left: 20px">- 장애 구역:' +
      (sendItem.node_nm || '') + '(' + (sendItem.alarmloc || '') + ')' +
      '</span></div>'
    html += '</div>'
  }

  if (['NTT', 'ATT2'].includes(sendItem.ticket_type)) {
    html += '<div>'
    html += '<span class="sub-title font-semibold"><h4>&middot;장애 구간</h4></span>'
    const startNode = trafficInfo.root_cause_sysnamea || ''
    const startPort = trafficInfo.root_cause_porta || ''
    const endNode = trafficInfo.root_cause_sysnamez || ''
    const endPort = trafficInfo.root_cause_portz || ''
    html +=
      '<div>' + startNode + '(' + startPort + ')' + ' → ' + endNode + '(' + endPort + ')' + '</div>'
    html += '</div>'
  }

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

  if (includeButton) {
    const buttonClassName = buttonOptions.className || 'chatbot-mail-send-btn'
    const wrapperClass = buttonOptions.wrapperClass || 'chatbot-message-body'

    const encodedHtml = encodeBase64Html(html)
    const safeEncodedHtml = encodedHtml.replace(/"/g, '&quot;').replace(/'/g, '&#39;')

    let buttonContainerHtml = '<div class="' + wrapperClass + '" style="display: flex; align-items: center; gap: 10px; margin-top: 10px;">'

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
 */
async function sendRequestContentEmail({
  htmlContent,
  receiverUsers,
  ticketInfo,
  userInfo,
  subject,
  apiSendMQ: customApiSendMQ = apiSendMQ,
}) {
  if (!receiverUsers || receiverUsers.length === 0) {
    throw new Error('담당 직원을 선택해주세요.')
  }

  const mailSubject =
    subject || moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss초') + ' 장비 조치 요청서'

  const cleanedContent = htmlContent.replace(/display:\s*none;?/gi, '')

  const receiverUserEmails = receiverUsers
    .map((v) => v.email)
    .join(', ')
    .replace(/\s{0,}[\r\n]/gi, '\r\n')

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

  const res = await customApiSendMQ('sendMail', param)
  return res
}

/**
 * 메일 전송 래퍼 함수 (콜백 지원)
 */
async function handleSendEmail({
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
    if (onLoading) {
      onLoading()
    }

    let decodedHtml = htmlContent
    if (isBase64Encoded) {
      decodedHtml = decodeBase64Html(htmlContent)
    }

    const res = await sendRequestContentEmail({
      htmlContent: decodedHtml,
      receiverUsers,
      ticketInfo,
      userInfo,
      apiSendMQ: customApiSendMQ,
    })

    if (onSuccess) {
      onSuccess(res)
    }
  } catch (error) {
    if (onError) {
      onError(error)
    } else {
      console.error('메일 전송 오류:', error)
      throw error
    }
  }
}

const routeName = constants.nia.chatbotKeyMap.requestForAction.parameterKey

export default {
  name: routeName,
  extends: Base,
  mixins: [dialogOpenMixin, niaObserverMixin],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      },
    },
    popup: { type: String, default: 'N' },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      containerLoading: false,
      requestForActionLoading: false,
      selectedRow: null,
      selectedUser: [],
      sendItem: {},
      trafficInfo: {},
      relatedSopList: [],
      userList: [],
      mailToSystemUrl: null,
      isLoadAiDetectionInfo: false,
      mailContentHtml: '',
      aiDetection: {
        in_threshold_upper: '',
        out_predict: '',
        out_threshold_lower: '',
        out_threshold_upper: '',
        in_predict: '',
        in_threshold_lower: '',
        in_bps: '',
        in_anomaly: '',
        out_bps: '',
        out_anomaly: '',
      },
      syslogInfo: [],
      ticketInfo: [],
      defineSyslogDetailTable: [
        { type: '', prop: 'alarmno', name: '알람번호', width: 100 },
        {
          type: '',
          prop: 'alarmtime',
          name: '발생시간',
          width: 140,
          formatter: (row) => {
            return this.formatterTimeStamp(row.request_time, 'YYYY/MM/DD-HH:mm:ss')
          },
        },
        { type: '', prop: 'node_num', name: '장비번호', width: 100 },
        { type: '', prop: 'node_nm', name: '장비명', width: 100 },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 100 },
        { type: '', prop: 'alarmmsg', name: '알람메시지', width: 150 },
        { type: '', prop: 'etc', name: '원본메시지', width: 230 },
      ],
      defineTicketDetailTable: [
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 150 },
        {
          type: '',
          prop: 'fault_time',
          name: '처리시간',
          width: 140,
          formatter: (row) => {
            return this.formatterTimeStamp(row.request_time, 'YYYY/MM/DD-HH:mm:ss')
          },
        },
        { type: '', prop: 'clos_time', name: '마감시간', width: 140 },
        { type: '', prop: 'ticket_type', name: '티켓유형', width: 100 },
        { type: '', prop: 'ticket_result', name: '장애내용', width: 120 },
        { type: '', prop: 'root_cause_sysnamea', name: '장비ID', width: 100 },
        { type: '', prop: 'root_cause_porta', name: '인터페이스', width: 100 },
      ],
      selectedReceiverUser: [],
    }
  },
  computed: {
    sopHistColumn() {
      const syslogCol = [
        { type: '', prop: 'alarmno', name: '알람번호', width: 100 },
        {
          type: '',
          prop: '',
          name: '발생시간',
          width: 180,
          formatter: (row) => {
            return this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss')
          },
        },
        { type: '', prop: 'fault_classify', name: '장애구분', width: 100 },
        { type: '', prop: 'fault_type', name: '장애유형', width: 100 },
        { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100 },
        { type: '', prop: 'etc_content', name: '기타조치내용', width: 250 },
        { type: '', prop: 'node_num', name: '장비번호', width: 100 },
        { type: '', prop: 'node_nm', name: '장비명', width: 100 },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 160 },
        { type: '', prop: 'alarmmsg', name: '알람메시지', width: 150 },
        { type: '', prop: 'etc', name: 'syslog 원본메시지', width: 250 },
      ]
      const columns = [
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 150 },
        {
          type: '',
          prop: '',
          name: '처리시간',
          width: 180,
          formatter: (row) => {
            return this.formatterTimeStamp(row.request_time, 'YYYY/MM/DD-HH:mm:ss')
          },
        },
        { type: '', prop: 'ticket_type', name: '티켓유형', width: 80 /* , formatter: getAlarmType */ },
        { type: '', prop: 'root_cause_porta', name: '장애구분', width: 100 },
        { type: '', prop: 'fault_classify', name: '장애유형', width: 100 },
        { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100 },
        { type: '', prop: 'etc_content', name: '기타사항', width: 100 },
        { type: '', prop: 'root_cause_sysnamea', name: '장비ID', width: 100 },
        { type: '', prop: 'root_cause_sysnamea', name: '장비이름', width: 100 },
        { type: '', prop: 'ip_addra', name: '마스터 IP', width: 100 },
        { type: '', prop: 'root_cause_porta', name: '장비I/F', width: 100 },
        { type: '', prop: 'handling_fin_user', name: '마감자', width: 100 },
        {
          type: '',
          prop: 'handling_fin_time',
          name: '마감시간',
          width: 150,
          formatter: (row) => {
            return formatterTime(row.handling_fin_time)
          },
        },
      ]
      return this.isSyslog ? syslogCol : columns
    },
    userInfoColumn() {
      return [
        { type: '', prop: 'name', name: '이름', width: 140 },
        { type: '', prop: 'agency_name', name: '분류', width: 150 },
        { type: '', prop: 'email', name: 'E-mail', width: 250 },
      ]
    },
    isSyslog() {
      return this.selectedRow?.ticket_type === 'SYSLOG' ?? false
    },
    getMailToSystemUrl() {
      let host = ''
      const profileActive = this.$store.state.app.server.profile
      let key = ''
      let value
      if (this.selectedRow?.ticket_type === 'SYSLOG') {
        key = 'alarmno'
        value = this.selectedRow?.alarmno
      } else {
        key = 'ticket_id'
        value = this.selectedRow?.ticket_id
      }
      const self_process_group = key === 'ticket_id' ? this.trafficInfo?.self_process_group : this.syslogInfo?.self_process_group
      switch (profileActive) {
        case 'dev':
        case 'local':
          host = 'localhost'
          break
        case 'test':
          host = 'incodej-lab.iptime.org'
          break
        case 'oper':
          host = '116.89.191.47'
          break

        default:
          break
      }
      return `http://${host}:4002/#/operationStatusScreen/selfProcessingDashboard/?${key}=${value}&self_process_group=${self_process_group}`
    },
    getAlarmtxt() {
      return getAlarmType(this.sendItem)
    },
    ...mapState({
      requestForActionEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.requestForAction.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },
  },
  watch: {
    requestForActionEventText(nVal, oVal) {
      if (this.isModal) {
        switch (nVal) {
          case constants.nia.chatbotCommand.edit.action:
            this.onClickFin()
            break
          case constants.nia.chatbotCommand.mailSend.action:
            this.onClickEmailSender()
            break
        }

        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.requestForAction.parameterKey })
      }
    },
  },
  created() {
    this.onLoadUserList()
    this.selectedRow = this.wdata.params
  },
  async mounted() {
    await this.setTicketDataForChatbotTicketData()

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    async setTicketDataForChatbotTicketData(isSwitchingTicket) {
      if (isSwitchingTicket) this.wdata.params.isChatbotGenerated = isSwitchingTicket
      const chatbotData = await getChatbotTicketData(this.wdata)
      if (chatbotData) {
        this.selectedRow = chatbotData
        this.$emit('update:wdataParams', chatbotData)
      }

      try {
        this.containerLoading = true

        // 로직 함수를 사용하여 데이터 수집
        const userInfo = this.$store.state.user.info
        const collectedData = await collectRequestContentData({
          selectedRow: this.selectedRow,
          userInfo,
          options: {
            loadTrafficInfo: !['SYSLOG', 'RT'].includes(this.selectedRow.ticket_type),
            loadSyslogInfo: this.isSyslog,
            loadSopHist: true,
            loadAiDetection: true,
          },
        })

        // 수집된 데이터를 컴포넌트 데이터에 할당
        this.sendItem = collectedData.sendItem
        this.trafficInfo = collectedData.trafficInfo
        this.syslogInfo = collectedData.syslogInfo
        this.relatedSopList = collectedData.relatedSopList
        this.aiDetection = collectedData.aiDetection
        this.isLoadAiDetectionInfo = collectedData.isLoadAiDetectionInfo

        // HTML 생성
        this.mailContentHtml = generateRequestContentHtml({
          data: collectedData,
          selectedUsers: this.selectedUser,
          mailToSystemUrl: this.getMailToSystemUrl,
          formatterTimeStamp: (time, formatStr) => this.formatterTimeStamp(time, formatStr),
        })
      } catch (error) {
        this.error(error)
      } finally {
        this.containerLoading = false
      }
    },

    async popupShowCommand() {
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content:
            '<div class="chatbot-command-header">상황전파 팝업 안내</div>' +
            '<div class="chatbot-message-body">' +
              '신속한 상황 공유를 위해 <b>장애 내역을 메일로 전파</b>하고,<br>조치 결과에 대한 <b>마감</b>을 진행하는 화면입니다.' +
              '<br><br>' +
              constants.nia.chatbotIcon.Information + '장애내용은 티켓과 SOP 정보를 통해 자동 설정했습니다.' +
              '<div class="chatbot-process">' +
                constants.nia.chatbotContent.processHeaderText + '<br><br>' +
                '1. <b>요청 내용</b> 확인 → 2. <b>담당자</b> 선택 → 3. <b>메일 전송</b>' +
              '</div>' +
            '</div>' +
            (await getWindowActionList(constants.nia.chatbotKeyMap.requestForAction.dialogNm, constants.nia.chatbotKeyMap.requestForAction.popupName,
              makeOpenPopupNumberText(3, constants.nia.chatbotKeyMap.configTest.key) +
              makeOpenPopupNumberText(4, constants.nia.chatbotKeyMap.sopHistory.key) +
              makeOpenPopupNumberText(5, constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.key)
            ))
        })
      }
    },
    onMailContentChange(event) {
      // contenteditable에서 변경사항이 발생하면 mailContentHtml 업데이트
      if (event && event.target) {
        this.mailContentHtml = event.target.innerHTML
      } else {
        const contentEl = document?.querySelector('#mail-content')
        if (contentEl) {
          this.mailContentHtml = contentEl.innerHTML
        }
      }
    },
    async onLoadUserList() {
      try {
        const res = await apiSelectUserList()
        this.userList = res?.result ?? {}
      } catch (error) {
        this.error(error)
      }
    },
    onChangeRowSelected(rows) {
      this.selectedUser = rows
      // 수신자 변경 시 HTML 업데이트
      this.updateMailContentHtml()
    },
    updateMailContentHtml() {
      // 수신자 변경 시 HTML 재생성
      const collectedData = {
        sendItem: this.sendItem,
        trafficInfo: this.trafficInfo,
        syslogInfo: this.syslogInfo,
        relatedSopList: this.relatedSopList,
        aiDetection: this.aiDetection,
        isLoadAiDetectionInfo: this.isLoadAiDetectionInfo,
      }

      this.mailContentHtml = generateRequestContentHtml({
        data: collectedData,
        selectedUsers: this.selectedUser,
        mailToSystemUrl: this.getMailToSystemUrl,
        formatterTimeStamp: (time, formatStr) => this.formatterTimeStamp(time, formatStr),
      })
    },
    onClickFin() {
      const addInfo = this.selectedRow.ticket_type === 'SYSLOG' ? this.syslogInfo : this.sendItem
      const row = Object.assign(this.selectedRow, addInfo)
      this.fn_openWindow('processFin', row)
    },
    async onClickEmailSender() {
      const receiverUser = this.$refs.employeeTable.selection
      const contentEl = document?.querySelector('#mail-content')
      const htmlContent = contentEl ? contentEl.innerHTML : this.mailContentHtml

      // 공통 메일 전송 함수 사용
      await handleSendEmail({
        htmlContent,
        receiverUsers: receiverUser,
        ticketInfo: this.sendItem,
        userInfo: this.$store.state.user.info,
        isBase64Encoded: false,
        apiSendMQ,
        onLoading: () => {
          this.requestForActionLoading = true
        },
        onSuccess: (res) => {
          this.$alert(`메일 전송에 ${res.success ? '성공' : '실패'} 하였습니다.`, '알림', {
            confirmButtonText: '확인',
            customClass: 'nia-message-box',
          })
        },
        onError: (error) => {
          if (error.message === '담당 직원을 선택해주세요.') {
            this.$alert(error.message, '알림', {
              confirmButtonText: '확인',
              customClass: 'nia-message-box',
            })
          } else {
            this.error(error)
          }
        },
      }).finally(() => {
        this.requestForActionLoading = false
      })
    },
    onClose() {
      this.selectedUser = []
    },
  },
}
</script>

<style lang="scss" scoped>
.sub-title {
  font-size: 1rem;
}

//  ::v-deep .el-table th {
//   background: rgba(211, 211, 211, 0.22) !important;
// }
</style>
