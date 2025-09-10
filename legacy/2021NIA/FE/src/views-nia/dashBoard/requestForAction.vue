<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <!-- <el-row class="w-full d-flex flex-column"> -->
    <!-- <el-row class="d-flex p-1">
        <i class="el-icon-document mr-2 text-base" />
        {{ isSyslog ? 'SYSLOG 장애대응' : 'AI 장애대응' }} 조치 요청서
        <hr>
      </el-row> -->
    <el-row class="w-full">
      <el-col class="p-1" :span="isMobile ? 24 : 12">
        <el-card shadow="never" :body-style="{ padding: '10px' }">
          <div slot="header">
            <div>
              <span><i class="el-icon-document" /> {{ isSyslog ? '알람' : '티켓' }} 상세 정보</span>
            </div>
          </div>
          <el-col class="style : 20%">
            <el-table v-if="isSyslog" ref="table" size="mini" :data="[syslogInfo]" class="w-100" :height="90" border fit>
              <el-table-column v-for="col in defineSyslogDetailTable" :key="col.prop" :prop="col.prop" :label="col.name" :width="col.width" />
            </el-table>
            <el-table v-else ref="table" size="mini" :data="[sendItem]" class="w-100" :height="90" border fit>
              <el-table-column v-for="col in defineTicketDetailTable" :key="col.prop" :prop="col.prop" :formatter="col.formatter" :label="col.name" :width="col.width" />
            </el-table>
          </el-col>
        </el-card>

        <el-card shadow="never" :body-style="{ padding: '10px', height: '200px' }" :class="{ 'mt-1': isSyslog }">
          <div slot="header">
            <div>
              <span><i class="el-icon-document" /> 연관 SOP 리스트</span>
            </div>
          </div>
          <el-col class="style : 30%">
            <el-table ref="sopTable" :size="'mini'" :data="relatedSopList" class="w-100" :height="250" border fit>
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

          <el-col class="style : 50%">
            <el-table ref="employeeTable" :size="'mini'" :data="userList" class="w-100" :height="400" border fit>
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
            <div id="mail-content" v-loading="containerLoading" class="w-100 d-flex flex-column rounded shadow-sm leading-7 pl-5 w-50 overflow-y-auto text-left" contenteditable="true" :style="{ height: 945 + 'px', border: 'solid 1px lightgray' }">
              <div style="display: none"><a :href="getMailToSystemUrl">바로가기</a><br /></div>
              <div class="text-xl font-bold"><h2>장애 상세내역 및 조치 요청서</h2></div>
              <div>
                <span class="sub-title font-semibold"><h4>&middot;발신</h4></span>
                &nbsp;&nbsp;{{ sendItem.sender }}
              </div>
              <div>
                <span class="sub-title font-semibold"><h4>&middot;AI 분석 결과 정보</h4></span>
                <span class="font-italic font-semibold"> {{ getAlarmtxt }} </span>
                <div v-if="sendItem.ticket_type === 'ATT2'">
                  &emsp;<span>IN</span><br />
                  &nbsp;&nbsp;- mbps: {{ aiDetection !== null ? sendItem.in_bps + ' MB' : '' }} <br />
                  &nbsp;&nbsp;- Predict: {{ aiDetection !== null ? sendItem.in_predict + ' MB' : '' }}<br />
                  &nbsp;&nbsp;- Threshold_Upper: {{ aiDetection !== null ? sendItem.in_threshold_upper + ' MB' : '' }}<br />
                  &nbsp;&nbsp;- Threshold_Lower: {{ aiDetection !== null ? sendItem.in_threshold_lower + ' MB' : '' }}<br />
                  - Anomaly: {{ aiDetection !== null ? sendItem.in_anomaly + '' : '' }}<br />
                  &emsp;<span>OUT</span><br />
                  &nbsp;&nbsp;- mbps: {{ aiDetection !== null ? sendItem.out_bps + ' MB' : '' }}<br />
                  &nbsp;&nbsp;- Predict : {{ aiDetection !== null ? sendItem.out_predict + ' MB' : '' }}<br />
                  &nbsp;&nbsp;- Threshold_Upper: {{ aiDetection !== null ? sendItem.out_threshold_upper + ' MB' : '' }}<br />
                  &nbsp;&nbsp;- Threshold_Lower: {{ aiDetection !== null ? sendItem.out_threshold_lower + ' MB' : '' }}<br />
                  &nbsp;&nbsp;- Anomaly: {{ aiDetection !== null ? sendItem.out_anomaly + '' : '' }}<br />
                </div>

                <div v-if="sendItem.ticket_type === 'ATT2' || sendItem.ticket_type === 'NTT'">
                  <span class="font-semibold">&nbsp;&nbsp;&middot;장애 유무 판단 확률</span><br />
                  &nbsp;&nbsp;&nbsp;- 유효 확률 : {{ sendItem.zero1_entropy ? ((1 - sendItem.zero1_entropy) * 100).toFixed(1) + '%' : '' }}<br />
                  &nbsp;&nbsp;&nbsp;- 무효 확률 : {{ sendItem.zero1_entropy ? (sendItem.zero1_entropy * 100).toFixed(1) + '%' : '' }}
                </div>

                <div v-if="sendItem.ticket_type === 'FTT'">
                  &nbsp;&nbsp;- 장애 확률 : {{ sendItem.zero1_entropy ? (sendItem.zero1_entropy * 100).toFixed(1) + '%' : '' }}<br />
                  &nbsp;&nbsp;- 비장애 확률 : {{ sendItem.zero1_entropy ? ((1 - sendItem.zero1_entropy) * 100).toFixed(1) + '%' : '' }}
                </div>

                <div v-if="sendItem.ticket_type === 'NFTT'">
                  &nbsp;&nbsp;- Measured Time : {{ sendItem.measured_datetime }} <br />
                  &nbsp;&nbsp;- CPU 예측값 : {{ sendItem.cpu_predicted }} <br />
                  &nbsp;&nbsp;- Mem 예측값 : {{ sendItem.mem_predicted }} <br />
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
                <div v-if="isSyslog">
                  &nbsp;&nbsp;- 발생 시간 : {{ formatterTimeStamp(syslogInfo.alarmtime) }}<br />
                  &nbsp;&nbsp;- 알람 번호 : {{ syslogInfo.alarmno }} <br />
                  &nbsp;&nbsp;- 장비명 : {{ syslogInfo.node_nm }}<br />
                  &nbsp;&nbsp;- 장비 번호 : {{ syslogInfo.node_num }}<br />
                  &nbsp;&nbsp;- 인터페이스 : {{ syslogInfo.alarmloc }}<br />
                  &nbsp;&nbsp;- 알람 메시지 : {{ syslogInfo.alarmmsg }}<br />
                  &nbsp;&nbsp;- 원본 메시지 : {{ syslogInfo.etc }}<br />
                  &nbsp;&nbsp;- 상세 내용 :
                </div>
                <div v-else>
                  &nbsp;&nbsp;- 작업 요청 구간 : {{ (trafficInfo.root_cause_sysnamea || '') + '(' + (trafficInfo.root_cause_porta || '') + ')' }} → {{ (trafficInfo.root_cause_sysnamez || '') + '(' + (trafficInfo.root_cause_portz || '') + ')' }}<br />
                  &nbsp;&nbsp;- 티켓 번호 : {{ sendItem.ticket_id }} <br />
                  &nbsp;&nbsp;- 티켓 타입 : {{ sendItem.ticket_type }}<br />
                  &nbsp;&nbsp;- 발생 원인 : {{ sendItem.ticket_result || sendItem.ticket_rca_result_dtl_code }}<br />
                  &nbsp;&nbsp;- 발생 시간 : {{ formatterTimeStamp(sendItem.fault_time) }}<br />
                  &nbsp;&nbsp;- 상세 내용 :
                </div>
              </div>
              <div v-if="isSyslog">
                <span class="sub-title font-semibold"><h4>&middot;장애 구역</h4></span>
                <div>&nbsp;&nbsp;- 장애 구역:{{ sendItem.node_nm + '(' + sendItem.alarmloc + ')' }}</div>
              </div>
              <div v-if="['NTT', 'ATT2'].includes(sendItem.ticket_type)">
                <span class="sub-title font-semibold"><h4>&middot;장애 구간</h4></span>
                <div>{{ trafficInfo.root_cause_sysnamea + '(' + trafficInfo.root_cause_porta + ')' }} → {{ trafficInfo.root_cause_sysnamez + '(' + trafficInfo.root_cause_portz + ')' }}</div>
              </div>
              <div>
                <span class="sub-title font-semibold"><h4>&middot;연관 SOP</h4></span>
                <div>
                  &nbsp;&nbsp;- 장애 구분: {{ sendItem.fault_classify || '' }}<br />
                  &nbsp;&nbsp;- 장애 유형: {{ sendItem.fault_type || '' }}<br />
                  &nbsp;&nbsp;- 조치 내용: {{ sendItem.fault_detail_content || '' }}
                </div>
              </div>
            </div>
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
        <el-button v-if="isSyslog" size="mini" type="primary" @click.native="fn_openWindow('processFin', selectedRow)"> 마감 </el-button>
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
import { apiSendMQ, apiSelectAiDetectionInfo, apiSelfProcessSyslogInfo, apiSelfProcessTrafficInfo, apiSelectSopHistList, apiSopSyslogHistList, apiSelectUserList } from '@/api/nia'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'

import _ from 'lodash'

const routeName = 'requestForAction'

export default {
  name: routeName,
  extends: Base,
  mixins: [dialogOpenMixin],
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
      selectedRow: null,
      selectedUser: [],
      sendItem: {},
      trafficInfo: {},
      relatedSopList: [],
      userList: [],
      mailToSystemUrl: null,
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
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 100 },
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
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 100 },
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
      const isSyslog = this.isSyslog
      return isSyslog ? syslogCol : columns
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
  },
  created() {
    this.onLoadUserList()
  },
  async mounted() {
    const { uid, name, mobile, email, agencyName } = this.$store.state.user.info
    this.$set(this.sendItem, 'sender', `${agencyName} ${name}`)

    this.selectedRow = this.wdata.params
    this._merge(this.sendItem, this.selectedRow)

    try {
      this.containerLoading = true
      if (!['SYSLOG', 'RT'].includes(this.selectedRow.ticket_type)) {
        await this.onLoadTrafficInfo()
      }
      if (this.isSyslog) {
        await this.onLoadSyslogInfo()
      }
      await this.onLoadSopHistList()
      await this.onLoadAiDetectionInfo()
    } catch (error) {
      this.error(error)
    } finally {
      this.containerLoading = false
    }
  },
  methods: {
    setTemplateContent() {
      const { ticket_type, status } = this.selectedRow

      if (ticket_type === 'RT' && status !== 'INIT') return

      // AI 분석 결과 정보
      if (ticket_type === 'ATT2' && this.aiDetection) {
        const { in_bps, in_predict, in_threshold_upper, in_threshold_lower, in_anomaly, out_bps, out_predict, out_threshold_upper, out_threshold_lower, out_anomaly } = this.aiDetection
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
      if (['FTT', 'ATT2', 'NTT'].includes(ticket_type)) {
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
      if (!this.selectedRow.ticket_id) return
      try {
        const res = await apiSelfProcessTrafficInfo({ TICKET_ID: this.selectedRow.ticket_id })
        this.trafficInfo = res?.result[0] ?? {}
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadSyslogInfo() {
      try {
        const res = await apiSelfProcessSyslogInfo({ ALARMNO: this.selectedRow.alarmno })
        this.syslogInfo = res?.result[0] ?? []
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
          if (!NODE_NM && !ALARMLOC) return
          res = await apiSopSyslogHistList(param)
        } else {
          const ticket = this.selectedRow
          console.log(ticket)
          const { /* ticket_id: TICKET_ID,  */ ticket_type: TICKET_TYPE, root_cause_sysnamea: ROOT_CAUSE_SYSNAMEA } = this.selectedRow
          if (!TICKET_TYPE || !ROOT_CAUSE_SYSNAMEA) return
          param = { TICKET_TYPE, ROOT_CAUSE_SYSNAMEA }
          res = await apiSelectSopHistList(param)
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

      if (!TICKET_ID) {
        return
      }
      const row = this.selectedRow
      const param = { TICKET_ID, START_NODE, START_PORT, FAULT_TIME }
      try {
        const res = await apiSelectAiDetectionInfo(param)
        this.aiDetection = res?.result[0] ?? null
        this.setTemplateContent()
      } catch (error) {
        this.error(error)
      } finally {
        this.chartLoading = false
      }
    },
    onClickFin() {
      const addInfo = this.selectedRow.ticket_type === 'SYSLOG' ? this.syslogInfo : this.sendItem
      const row = Object.assign(this.selectedRow, addInfo)
      this.fn_openWindow('processFin', row)
    },
    async onClickEmailSender() {
      const { uid, name } = this.$store.state.user.info
      const _THIS = this
      const receiverUser = this.$refs.employeeTable.selection
      if (receiverUser.length === 0) {
        this.$alert('담당 직원을 선택해주세요.', '알림', {
          confirmButtonText: '확인',
        })
        return
      }
      const contentEl = document?.querySelector('#mail-content')
      const param = {
        mail: {
          subject: this.moment(new Date()).format('YYYY년 MM월 DD일 HH시mm분ss초') + ' 장비 조치 요청서',
          content: contentEl.innerHTML.replace('display: none;'),
          receiverUser: receiverUser
            .map((v) => v.email)
            .join(', ')
            .replace(/\s{0,}[\r\n]/gi, '\r\n'),
        },
        ticketInfo: this._merge({}, this.sendItem, {
          eventType: 'REQUEST_CHANGE_TICKET_STATUS',
          status: 'ACK',
          user_ids: uid,
          detail: 'DETAIL',
          mail_content: contentEl.innerHTML.replace('display: none;'),
          handling_ack_user: name,
          ticket_result: this.selectedRow.ticket_result || this.selectedRow.ticket_rca_result_code,
        }),
      }
      try {
        const res = await apiSendMQ('sendMail', param)
        this.$alert(`메일 전송에 ${res.success ? '성공' : '실패'} 하였습니다.`, '알림', {
          confirmButtonText: '확인',
        })
      } catch (error) {
        this.error(error)
      }
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
