<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          {{ isSyslog ? 'SYSLOG 장애대응' : 'AI 장애대응' }} 조치 요청서
          <hr>
        </span>
        <div class="w-full h-full d-flex">
          <div class="w-50 p-3 shadow-sm">
            <div v-if="isSyslog" class="shadow-sm p-1 mt-2" style="height: 140px">
              <span class="title">알람 상세 정보</span>
              <div class="overflow-x-auto">
                <table class="ticket-info basic">
                  <thead>
                    <tr>
                      <th
                        v-for="(col, index) in defineSyslogDetailTable.col"
                        :key="index"
                        class="whitespace-nowrap"
                        style="max-width: fit-content"
                      > <span>{{ col }}</span></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr class="repeated-item">
                      <td
                        v-for="(valueKey, index) in defineSyslogDetailTable.valueKey"
                        :key="index"
                        class="whitespace-nowrap"
                        style="min-width: fit-content;max-width: fit-content"
                      ><span>{{ syslogInfo[valueKey] ||'' }}</span></td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-loading="containerLoading" class="shadow-sm mt-1" style="height: 250px">
              <span class="title">연관 SOP 리스트</span>
              <div v-if="relatedSopList.length > 0" class="shadow-sm  h-full">
                <CompAgGrid
                  ref="sopHistAgGrid"
                  v-model="sopHistAgGrid"
                  class="w-100"
                  style="height: calc(100% - 25px);"
                />
              <!-- @rowClicked="selectedTicket" -->
              </div>
              <div v-else class="d-flex items-center justify-center text-lg font-semibold" style="height: calc(100% - 25px);">연관 SOP 데이터가 존재하지 않습니다.</div>
            </div>
            <div v-loading="containerLoading" class="shadow-sm mt-2" style="height: 250px">
              <span class="title">담당 직원 정보</span>
              <CompAgGrid
                ref="userInfoGrid"
                v-model="userInfoGrid"
                class="w-100"
                style="height: calc(100% - 25px);"
                @rowSelectionChanged="onChangeRowSelected"
              />
            </div>
          </div>
          <div
            v-if="sendItem.ticket_type !== 'RT'"
            id="mail-content"
            v-loading="containerLoading"
            class="d-flex flex-column rounded shadow-sm leading-7 p-4 pl-5 w-50 overflow-y-auto"
            contenteditable="true"
            :style="{'height': (isSyslog ? 700 : 550) + 'px', border: 'solid 1px lightgray'}"
          >
            <!-- <span class="title">장애 상세내역 및 조치 요청서</span> -->
            <div class="text-xl font-bold"><h2>장애 상세내역 및 조치 요청서</h2></div>
            <div>
              <span class="sub-title font-semibold"><h4>&middot;발신</h4></span>
              &nbsp;&nbsp;{{ sendItem.sender }}
            </div>
            <div>
              <span class="sub-title font-semibold"><h4>&middot;AI 분석 결과 정보</h4></span>
              <!-- ATT2 / 이상 트래픽 -->
              <div v-if="sendItem.ticket_type === 'ATT2' && aiDetection !== null">
                &emsp;<span>IN</span><br>
                &nbsp;&nbsp;- bps: {{ sendItem.in_bps + ' MB' }} <br>
                &nbsp;&nbsp;- Predict: {{ sendItem.in_predict + ' MB' }}<br>
                &nbsp;&nbsp;- Threshold_Upper: {{ sendItem.in_threshold_upper + ' MB' }}<br>
                &nbsp;&nbsp;- Threshold_Lower: {{ sendItem.in_threshold_lower + ' MB' }}<br>
                - Anomaly: {{ sendItem.in_anomaly + 'MB' }}<br>
                &emsp;<span>OUT</span><br>
                &nbsp;&nbsp;- bps: {{ sendItem.out_bps + ' MB' }}<br>
                &nbsp;&nbsp;- Predict : {{ sendItem.out_predict + ' MB' }}<br>
                &nbsp;&nbsp;- Threshold_Upper: {{ sendItem.out_threshold_upper + ' MB' }}<br>
                &nbsp;&nbsp;- Threshold_Lower: {{ sendItem.out_threshold_lower + ' MB' }}<br>
                &nbsp;&nbsp;- Anomaly: {{ sendItem.out_anomaly + 'MB' }}<br>
              </div>
              <!-- FTT(비장애) -->
              <div v-if="sendItem.ticket_type === 'FTT'">
                &nbsp;&nbsp;- 장애 확률 : {{ (zero1_entropy*100).toFixed(1)+'%' }}
                &nbsp;&nbsp;- 비장애 확률 : {{ (1 - zero1_entropy*100).toFixed(1)+'%' }}
              </div>

              <!-- NFTT -->
              <div v-if="sendItem.ticket_type === 'NFTT'">
                &nbsp;&nbsp;- Measured Time : {{ sendItem.measured_datetime }} <br>
                &nbsp;&nbsp;- CPU 예측값 : {{ sendItem.cpu_predicted }} <br>
                &nbsp;&nbsp;- Mem 예측값 : {{ sendItem.mem_predicted }} <br>
              </div>
            </div>
            <div>
              <span class="sub-title font-semibold"><h4>&middot;수신</h4></span>
              <span v-for="user in selectedUser" v-if="selectedUser.length > 0" :key="user.email">
                {{ user.name }}
              </span>
            </div>
            <div>
              <span class="sub-title font-semibold"><h4>&middot;작업 요청 내용</h4></span>
              <!-- SELECT_SOP_HIST_LIST -->
              <div v-if="isSyslog">
                &nbsp;&nbsp;- 발생 시간 : {{ syslogInfo.alarmtime }}<br>
                &nbsp;&nbsp;- 알람 번호 : {{ syslogInfo.alarmno }} <br>
                &nbsp;&nbsp;- 장비명 : {{ syslogInfo.node_nm }}<br>
                &nbsp;&nbsp;- 장비 번호 : {{ syslogInfo.node_num }}<br>
                &nbsp;&nbsp;- 인터페이스 : {{ syslogInfo.alarmloc }}<br>
                &nbsp;&nbsp;- 알람 메시지 : {{ syslogInfo.alarmmsg }}<br>
                &nbsp;&nbsp;- 원본 메시지 : {{ syslogInfo.etc }}<br>
                &nbsp;&nbsp;- 상세 내용 :
              </div>
              <div v-else>
                &nbsp;&nbsp;- 작업 요청 구간 : {{ trafficInfo.root_cause_sysnamea+'('+trafficInfo.root_cause_porta+')' }} → {{ trafficInfo.root_cause_sysnamez+'('+trafficInfo.root_cause_portz+')' }}<br>
                &nbsp;&nbsp;- 티켓 번호 : {{ sendItem.ticket_id }} <br>
                &nbsp;&nbsp;- 티켓 타입 : {{ sendItem.ticket_type }}<br>
                &nbsp;&nbsp;- 발생 원인 : {{ sendItem.ticket_result || sendItem.ticket_rca_result_dtl_code }}<br>
                &nbsp;&nbsp;- 발생 시간 : {{ sendItem.fault_time }}<br>
                &nbsp;&nbsp;- 상세 내용 :
              </div>
            </div>
            <div v-if="isSyslog">
              <span class="sub-title font-semibold"><h4>&middot;장애 구역</h4></span>
              <div>
                &nbsp;&nbsp;- 장애 구역:{{ sendItem.node_nm+'('+ sendItem.alarmloc +')' }}
              </div>
            </div>
            <div v-if="['NTT', 'ATT2'].includes(sendItem.ticket_type)">
              <span class="sub-title font-semibold"><h4>&middot;장애 구간</h4></span>
              <div>
                <h5>&nbsp;&nbsp;- 장애 구간:</h5> {{ trafficInfo.root_cause_sysnamea+'('+trafficInfo.root_cause_porta+')' }} → {{ trafficInfo.root_cause_sysnamez+'('+trafficInfo.root_cause_portz+')' }}
              </div>
            </div>
            <div>
              <span class="sub-title font-semibold"><h4>&middot;연관 SOP</h4></span>
              <div>
                &nbsp;&nbsp;- 장애 구분: {{ sendItem.fault_classify || '' }}<br>
                &nbsp;&nbsp;- 장애 유형: {{ sendItem.fault_type || '' }}<br>
                &nbsp;&nbsp;- 조치 내용: {{ sendItem.fault_detail_content || '' }}
              </div>
            </div>
          </div>
          <div v-else-if="sendItem.ticket_type === 'RT' && sendItem.status !== 'INIT'" class="w-50 d-flex shadow-sm items-center justify-center text-lg font-semibold" style="height: 550px;">
            발송된 조치 요청서가 존재하지 않습니다.
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" :disabled="sendItem.status != 'INIT'" @click.native="onClickEmailSender()">
            메일 전송
          </el-button>
          <el-button size="small" plain class="close-btn" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { formatterTime } from '@/views-nia/js/commonFormat'
import { apiSendMail, apiSelectAiDetectionInfo, apiSelfProcessSyslogInfo, apiSelfProcessTrafficInfo, apiSopHistList, apiSopSyslogHistList, apiSelectUserList } from '@/api/nia'

import _ from 'lodash'

const routeName = 'ModalNTF'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      containerLoading: false,
      selectedRow: null,
      selectedUser: [],
      sendItem: {},
      trafficInfo: {},
      relatedSopList: [],
      userList: [],
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
        out_anomaly: ''
      },
      syslogInfo: {
        ruleid: '',
        alarmmsg: '',
        alarmlvl: '',
        etc: '',
        alarmno: '',
        alarmtime: '',
        node_nm: '',
        node_num: '',
        alarmloc: '',
      },
      defineSyslogDetailTable: {
        col: ['알람 번호', '발생 시간', '장비 번호', '장비명', '인터페이스', '알람메시지', '원본 메시지'],
        valueKey: ['alarmno', 'alarmtime', 'node_num', 'node_nm', 'alarmloc', 'alarmmsg', 'etc']
      },
      selectedReceiverUser: []
    }
  },
  computed: {
    sopHistAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
      const syslogCol = [
        { type: '', prop: 'alarmno', name: '알람번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'alarm_occur_time', name: '발생시간', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss') } },
        { type: '', prop: 'fault_classify', name: '장애구분', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_type', name: '장애유형', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'etc_content', name: '기타조치내용', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'node_num', name: '장비번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'node_nm', name: '장비명', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 160, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'alarmmsg', name: '알람메시지', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'etc', name: 'syslog 원본메시지', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
      ]
      const columns = [
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'ticket_type', name: '티켓유형', width: 50, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false/* , formatter: getAlarmType */ },
        { type: '', prop: 'root_cause_porta', name: '장애구분', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'fault_classify', name: '장애유형', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'etc_content', name: '기타사항', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'root_cause_sysnamea', name: '장비ID', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'root_cause_sysnamea', name: '장비이름', width: 50, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'ip_addra', name: '마스터 IP', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'root_cause_porta', name: '장비I/F', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'handling_fin_user', name: '마감자', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'handling_fin_time', name: '마감시간', width: 150, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true, formatter: (row) => { return formatterTime(row.handling_fin_time) } },
      ]
      const isSyslog = this.isSyslog
      return { options, columns: isSyslog ? syslogCol : columns, data: this.relatedSopList }
    },
    userInfoGrid() {
      const options = { name: this.name, checkable: true, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: true }
        const columns = [
          { type: '', prop: 'name', name: '이름', width: 120, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'agency_name', name: '분류', width: 120, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'email', name: 'E-mail', width: 200, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        ]
        return { options, columns, data: this.userList }
    },
    isSyslog() {
      return this.selectedRow.ticket_type === 'SYSLOG'
    }
  },
  mounted () {
    const { uid, name, mobile, email, agencyName } = this.$store.state.user.info
    this.sendItem['sender'] = `${agencyName} ${name}`
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 1200
      this.closeOnClickModal = false
    },
    async onOpen(model, actionMode) {
      this.selectedRow = model?.row
      this._merge(this.sendItem, this.selectedRow)

      try {
        this.containerLoading = true
        if (!['SYSLOG', 'RT'].includes(this.selectedRow.ticket_type)) {
          await this.onLoadTrafficInfo()
        }
        if (this.isSyslog) {
          await this.onLoadSyslogInfo()
        }
        await this.onLoadAiDetectionInfo()
        await this.onLoadSopHistList()
        this.onLoadUserList()
        this.setTemplateContent()
      } catch (error) {
        this.error(error)
      } finally {
        this.containerLoading = false
      }
    },
    setTemplateContent() {
      const { ticket_type, status } = this.selectedRow

      if (ticket_type === 'RT' && status !== 'INIT') return

      // AI 분석 결과 정보
      if (ticket_type === 'ATT2') {
        const { in_bps, in_predict, in_threshold_upper, in_threshold_lower, in_anomaly
          , out_bps, out_predict, out_threshold_upper, out_threshold_lower, out_anomaly } = this.aiDetection
        // IN
        this.sendItem['in_bps'] = in_bps.toLocaleString()
        this.sendItem['in_predict'] = in_predict.toLocaleString()
        this.sendItem['in_threshold_upper'] = in_threshold_upper.toLocaleString()
        this.sendItem['in_threshold_lower'] = in_threshold_lower.toLocaleString()
        this.sendItem['in_anomaly'] = in_anomaly
        // OUT
        this.sendItem['out_bps'] = out_bps.toLocaleString()
        this.sendItem['out_predict'] = out_predict.toLocaleString()
        this.sendItem['out_threshold_upper'] = out_threshold_upper.toLocaleString()
        this.sendItem['out_threshold_lower'] = out_threshold_lower.toLocaleString()
        this.sendItem['out_anomaly'] = out_anomaly
      }
      if (ticket_type === 'FTT') {
        this.sendItem['zero1_entropy'] = this.selectedRow.zero1_entropy
      }
      if (ticket_type === 'NFTT') {
        // SELECT_NODEFACTOR_ONE
        // const { measured_datetime, cpu_predicted, mem_predicted } = this.nodeFactor
        // this.sendItem['measured_datetime'] = measured_datetime
        // this.sendItem['cpu_predicted'] = cpu_predicted
        // this.sendItem['mem_predicted'] = mem_predicted
      }
      // 작업 요청 내용

      // 연관 SOP
      this.sendItem['fault_classify'] = this.relatedSopList.length !== 0 ? this.relatedSopList[0].fault_classify : ''
      this.sendItem['fault_type'] = this.relatedSopList.length !== 0 ? this.relatedSopList[0].fault_type : ''
      this.sendItem['fault_detail_content'] = this.relatedSopList.length !== 0 ? this.relatedSopList[0].fault_detail_content : ''
    },
    async onLoadTrafficInfo() {
      try {
        const res = await apiSelfProcessTrafficInfo({ TICKET_ID: this.selectedRow.ticket_id })
        this.trafficInfo = res?.result ?? {}
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadSyslogInfo() {
      try {
        const res = await apiSelfProcessSyslogInfo({ ALARMNO: this.selectedRow.ticket_id })
        this.syslogInfo = res?.result ?? {}
      } catch (error) {
        this.error(error)
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
    async onLoadSopHistList() {
      let param
      try {
        let res
        if (this.isSyslog) {
          const { node_nm: NODE_NM, alarmloc: ALARMLOC } = this.syslogInfo
          param = { NODE_NM, ALARMLOC }
          res = await apiSopSyslogHistList(param)
        } else {
          const { ticket_id: TICKET_ID, ticket_type: TICKET_TYPE, root_cause_sysnamea: ROOT_CAUSE_SYSNAMEA } = this.selectedRow
          param = TICKET_TYPE === 'ATT2' ? { TICKET_TYPE, ROOT_CAUSE_SYSNAMEA } : { TICKET_ID }
          res = await apiSopHistList(param)
        }
        this.relatedSopList = res?.result ?? []
      } catch (error) {
        console.error(error)
      }
    },
    onChangeRowSelected(rows) {
      this.selectedUser = rows
    },
    async onLoadAiDetectionInfo() {
      const { fault_time: FAULT_TIME, ticket_id: TICKET_ID, ticket_type } = this.selectedRow
      const { root_cause_sysnamea: START_NODE, root_cause_sysnamez: END_NODE, root_cause_porta: START_PORT, root_cause_portz: END_PORT } = this.trafficInfo

      const param = { TICKET_ID, START_NODE, START_PORT, FAULT_TIME }
      try {
        const res = await apiSelectAiDetectionInfo(param)
        this.aiDetection = res?.result[0] ?? null
      } catch (error) {
        this.error(error)
      } finally {
        this.chartLoading = false
      }
    },
    async onClickEmailSender() {
     const { uid, name } = this.$store.state.user.info
     const receiverUser = this.$refs.userInfoGrid.getSelectedRows()
     if (receiverUser.length === 0) {
      this.$alert('담당 직원을 선택해주세요.', '알림', {
        confirmButtonText: '확인'
      })
      return
     }
     const contentEl = document?.querySelector('#mail-content')
     const param = {
      mail: {
        subject: this.moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss초') + ' 장비 조치 요청서',
        content: contentEl.getInnerHTML(),
        receiverUser: receiverUser.map(v => v.email).join(', ').replace(/\s{0,}[\r\n]/gi, '\r\n')
      },
      ticketInfo: this._merge({}, this.sendItem, {
        eventType: 'REQUEST_CHANGE_TICKET_STATUS',
        status: 'ACK',
        user_ids: uid,
        detail: 'DETAIL',
        mail_content: contentEl.getInnerHTML(),
        handling_ack_user: name,
        ticket_result: this.selectedRow.ticket_result || this.selectedRow.ticket_rca_result_code,
      })
    }
      try {
        const res = await apiSendMail(param)
        this.$alert(`메일 전송에 ${res.success ? '성공' : '실패'} 하였습니다.`, '알림', {
          confirmButtonText: '확인'
        })
      } catch (error) {
        this.error(error)
      }
    },
    onClose() {
      this.selectedUser = []
     },
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-dialog {
  margin-top: 3vh !important;
}
::v-deep .el-loading-spinner {
  display: flex;
  justify-content: center;
}
.sub-title {
  font-size: 1rem;
}
</style>
