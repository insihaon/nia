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
import { apiSendMQ, apiSelectUserList } from '@/api/nia'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'

import { mapState } from 'vuex'
import constants from '@/min/constants'
import { getChatbotTicketData, getWindowActionList, getInvisibleSpanParameter, getNiaRouterPathByName, makeOpenPopupNumberText } from '@/views-nia/js/commonNiaFunction'

import _ from 'lodash'

import niaObserverMixin from '@/mixin/niaObserverMixin'
import {
  collectRequestContentData,
  generateRequestContentHtml,
  handleSendEmail,
} from './logic/requestForActionLogic'

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
