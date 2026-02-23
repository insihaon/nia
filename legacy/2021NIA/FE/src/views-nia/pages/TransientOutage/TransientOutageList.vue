te<template>
  <div :class="[{ [name]: true }]" class="d-flex" style="height: calc(100vh - 134px);">
    <div class=" p-2" style="width: 800px;">
      <div class="top-container d-flex justify-between">
        <div class="title">
          <el-icon class="el-icon-tickets pr-1" /> 순단장애 감시
        </div>
        <div class="h-100 d-flex items-center function-panel">
          <template v-for="optionItems in otherOtionsItem">
            <div :key="optionItems.key" class="h-100 d-flex items-center " @click.stop="handleClickOtherOption(optionItems.key)">
              <el-tooltip effect="dark" :content="optionItems.content" placement="bottom-start">
                <v-icon :name="optionItems.class" />
              </el-tooltip>
            </div>
          </template>
          <el-col class="total-count fr">
            <span class="total">TOTAL: {{ getTicketList().length || 0 }}</span>
          </el-col>
        </div>
      </div>
      <div ref="ticketPanel" class="ticket-panel" style="height: calc(100% - 55px)">
        <el-card
          v-for="row in getTicketList()"
          :key="row.ticket_id"
          shadow="hover"
          :class="{'filp-open': ticketCtrl[row.ticket_id] && ticketCtrl[row.ticket_id].isOpen || false}"
          :style="{'border':row.ticket_id === selectedTicket.ticket_id?'2px solid red':'0px solid'}"
          @dblclick.native="onDoubleClickTicket(row)"
          @click.native="openDetail(row)"
        >
          <div class="ticket d-flex" :style="{'height': !otherOtions.fold || ticketCtrl[row.ticket_id].isOpen ? '175px': '105px'}">
            <div class="section-l" :style="{'background-color': getBgColorByStatus(row)}">
              <div>전표 번호 #.{{ row.ticket_id }}</div>
              <div class="alarm-title">순단장애</div>
              <div>{{ getRootCauseDomain(row.root_cause_domain) }}</div>
              <div :class="{'d-none': otherOtions.fold && !ticketCtrl[row.ticket_id].isOpen}">
                <span>생성 :{{ getFormatterTime(row.generation_time) }}</span>
                <span v-if="getFormatterTime(row.generation_time)!== getFormatterTime(row.update_time)" v-show="row.update_time">갱신 :{{ getFormatterTime(row.update_time) }}</span>
                <span v-show="row.status !== 'INIT'">{{ getFormatStatus(row.status) }} :{{ getFormatterTime(row.handling_time) }}</span>
              </div>
            </div>
            <div class="section-r">
              <div class="top d-flex w-100" style="justify-content: space-between;">
                <div>
                  <div class="cableA"><span class="cable">A</span><div class="node">{{ row.mba_sysnamea }}</div></div>
                  <div style="height: 6px" />
                  <div class="cableZ"><span class="cable">Z</span><div class="node">{{ row.mba_sysnamez }}</div></div>
                </div>
                <div class="trunk">
                  <span>{{ row.trunk_name }}</span>
                  <span><v-icon :name="row.direction === 'UP'? 'arrow-left': 'arrow-right'" style="width: 40px; stroke-width: 4;color: red;" /></span>
                </div>
              </div>
              <div class="middle d-flex items-center w-100 mt-2">
                <div style="margin-left: 8px;color:red;"><v-icon name="alert-triangle" class="mr-1" />장애영향케이블</div>
                <div>영향서비스<span style="padding-left: 5px;"> {{ row.influencecircuit_count || 0 }}</span></div>
                <div style="border-right: solid 1px #d4d7d9;">영향장비 <span>{{ row.equip_cnt }}</span></div>
              </div>
              <div class="bottom d-flex">
                <div><div>최초 장애 발생 시간</div><div class="alarm-time">{{ moment(row.fault_time).format('YYYY-MM-DD HH:mm:ss') }}</div></div>
                <div class="alarm-process d-flex">
                  <div :class="{'disabled': !['INIT','ACK'].includes(row.status)}" class="ack" @click="onOpenModalProcess('ack', row)">{{ row.status === 'ACK' ? '인지 수정': '인지' }}</div>
                  <div class="fin" :class="{'disabled': !['ACK','FIN','AUTO_FIN'].includes(row.status)}" @click="onOpenModalProcess('fin', row)">{{ row.status === 'FIN' ? '마감 수정': '마감' }}</div>
                  <div class="process" @click="onOpenModalProcess('batch', row)">TT일괄처리</div>
                  <div class="map" @click="onDoubleClickTicket(row)">Map</div>
                </div>
              </div>
            </div>
          </div>
          <!-- v-if="ticketCtrl[row.ticket_id] && ticketCtrl[row.ticket_id].isOpen" -->
          <div class="failure-alarm">
            <div class="d-flex top items-center">
              <div class="w-50">A측 시스템</div>
              <div class="w-50">Z측 시스템</div>
              <i class="el-icon-close" style="font-size:17px; cursor: pointer;" @click.stop="onClickCloseDetail(row)" />
            </div>
            <div v-for="(item, index) in azList(row)" :key="index" class="w-100 d-flex az-row">
              <div class="d-flex w-50">
                <div class="d-flex items-center">
                  <v-icon name="circle" style="width: 10px;stroke-width: 4;" />
                </div>
                <div class="pl-2">
                  <div>{{ item.root_cause_sysnamea }}</div>
                  <div>{{ item.root_cause_porta || '-' }}</div>
                </div>
              </div>
              <div class="d-flex w-50">
                <div class="d-flex items-center">
                  <v-icon name="circle" style="width: 10px;stroke-width: 4;" />
                </div>
                <div class="pl-2">
                  <div>{{ item.root_cause_sysnamez }}</div>
                  <div>{{ item.root_cause_portz || '-' }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <div style="width: calc(100% - 800px)" class="h-100 p-2">
      <div class="trunkName">{{ selectedTicket.trunk_name ? '캐리어명: ' + selectedTicket.trunk_name : '' }}</div>
      <splitpanes class="default-theme h-100" horizontal>
        <pane size="250" max-size="400">
          <Comp2DTopology ref="topology2d" class="h-100" :ticket="selectedTicket" @loadList="loadList" @selectedTopologyItem="onChangeSelectedTopologyItem" />
        </pane>
        <pane size="150" max-size="400">
          <el-tabs class="border-card rca-alarm-tab h-100">
            <el-tab-pane class="h-100">
              <span slot="label"><i class="el-icon-document mr-1 ml-1" />알람 리스트</span>
              <comp-ag-grid ref="alarmGrid" v-model="alarmGridTable" style="height: calc(100% - 30px);" />
              <div class="footer">
                <el-col class="alarmLevelInfo" style="width: 350px;">
                  <el-col class="alarmLevelInfoItem" style="width: 70px;">
                    <div style="background-color: #f56c6c;" /><span>Critical</span>
                  </el-col>
                  <el-col class="alarmLevelInfoItem">
                    <div style="background-color: #fdb025;" /><span>Major</span>
                  </el-col>
                  <el-col class="alarmLevelInfoItem">
                    <div style="background-color: #fde66b;" /><span>Minor</span>
                  </el-col>
                  <el-col class="alarmLevelInfoItem" style="width: 80px;">
                    <div style="background-color: #8bd6e3;" /><span>Warning</span>
                  </el-col>
                  <el-col class="alarmLevelInfoItem">
                    <div style="background-color: #8bd1375c;" /><span>Clear</span>
                  </el-col>
                </el-col>
                <el-col class="total-count fr">
                  TOTAL: <span style="color: #f37e7e;">{{ alarmGridTable.data.length }}</span> 개
                </el-col>
                <excel-btn class="fr mr-2" style="margin-top: 2px;" @click.native="exportExcel('alarmGrid')" />
              </div>
            </el-tab-pane>
            <el-tab-pane class="h-100">
              <span slot="label"><i class="el-icon-document mr-1 ml-1" />영향회선 리스트</span>
              <comp-ag-grid ref="influenceGrid" v-model="influenceGridTable" style="height: calc(100% - 30px);" />
              <div class="footer">
                <el-col class="total-count fr">
                  TOTAL: <span style="color: #f37e7e;">{{ influencecircuitList.length }}</span> 개
                </el-col>
                <excel-btn class="fr mr-2" style="margin-top: 2px;" @click.native="exportExcel('influenceGrid')" />
              </div>
            </el-tab-pane>
          </el-tabs>
        </pane>
      </splitpanes>

      <ModalRcaProcess ref="modalProcess" />
      <ModalRcaBatchProcessing ref="modalRcaBatchProcessing" />
      <audio id="mbaAlarm">
        <source src="assets/audio/순단장애.mp3" type="audio/mpeg">
      </audio>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import { Base } from '@/min/Base.min'
import { apiRcaRequest } from '@/api/nia'
import { AppOptions } from '@/class/appOptions'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import Comp2DTopology from './topology2d'
import ModalRcaProcess from './modal/ModalRcaProcessHandling'
import ModalRcaBatchProcessing from './modal/ModalRcaBatchProcessing'
import ExcelBtn from '@/views-nia/components/CompBottomExcelBtn'
import moment from 'moment'
import 'splitpanes/dist/splitpanes.css'
import { Splitpanes, Pane } from 'splitpanes'

const routeName = 'MbaTicket'

export default {
  name: routeName,
  components: { ModalRcaProcess, ModalRcaBatchProcessing, Comp2DTopology, CompAgGrid, ExcelBtn, Splitpanes, Pane },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      filterGroup: Vue.observable({}),
      clickedNode: null,
      clickedLink: null,
      activeNames: ['1'],
      // ticketContainerHeight: '',
      otherOtions: Vue.observable({}),
      ticketCtrl: {},
      mbaTicketList: [],
      alarmList: [],
      influencecircuitList: [],
      selectedTicket: {},
      mapClickedData: null,
      singleFilter: Vue.observable({}) /* {
        FM: false,
        CONST: false
      } */
    }
  },
  computed: {
    otherOtionsItem() {
      return [
        // { key: 'search', content: '검색옵션', class: 'settings' },
        { key: 'bell', content: '알람 ON/OFF', class: this.otherOtions.bell ? 'bell' : 'bell-off' },
        { key: 'pause', content: `실시간 모니터링 ${this.otherOtions.pause ? '일시정지' : '시작'}`, class: this.otherOtions.pause ? 'pause' : 'play' },
        { key: 'fold', content: `티켓 ${this.otherOtions.fold ? '펼치기' : '접기'}`, class: this.otherOtions.fold ? 'maximize-2' : 'minimize-2' }
      ]
    },
    alarmGridTable() {
      const options = {
        name: this.name,
        checkable: false, rowGroupPanel: false, rowHeight: 25,
        getRowStyle: this.getRowStyle
      }

      const columns = [
        { type: '', prop: 'ticket_id', name: 'RCA 전표번호', suppressMenu: true, width: 125, alignItems: 'center' },
        { type: '', prop: 'name_code', name: '구분', width: 125, alignItems: 'center' },
        { type: '', prop: 'end_time', name: '광레벨 수집완료 시간(EMS)', width: 250, format: (row) => { return row.end_time ? this.getFormatterTime(row.end_time) : '' } },
        { type: '', prop: 'officename', name: '국사', width: 150 },
        { type: '', prop: 'sysname', name: '시스템명', width: 250 },
        { type: '', prop: 'monitored_object', name: '모니터링 위치', width: 250 },
        { type: '', prop: 'equip_type', name: '장비종류', width: 150 },
        { type: '', prop: 'value_max', name: '광레벨(최대)', width: 150, alignItems: 'center' },
        { type: '', prop: 'value_cur', name: '광레벨(평균)', width: 150, alignItems: 'center' },
        { type: '', prop: 'value_min', name: '광레벨(최소)', width: 150, alignItems: 'center' },
        { type: '', prop: 'diff', name: '광레벨 차이(현재 시점)', width: 150, alignItems: 'center' },
        { type: '', prop: 'old_diff', name: '광레벨 차이(이전 시점)', alignItems: 'center', width: 150, format: (row) => { return row.old_diff !== 0 && !row.old_diff ? '-' : row.old_diff } }
      ]
      return { options, columns, data: this._orderBy(this.getMbaAlarmFilter, ['end_time'], ['asc']) || [] }
    },
    influenceGridTable() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowHeight: 25 }
      const columns = [
        // { type: '', prop: 'asncircuitid1', name: '영향 회선 번호', width: 150 },
        // { type: '', prop: 'transcircuitname', name: '영향 회선명', width: 230 },
        // { type: '', prop: 'ocaseq', name: '영향 케이블 번호', width: 150, alignItems: 'center' },
        // { type: '', prop: 'ocaname', name: '영향 케이블명', width: 250, alignItems: 'center' },
        // { type: '', prop: 'optcorenum', name: '영향 케이블 코어 번호', width: 150, alignItems: 'center' }
        { type: '', prop: 'transcircuitname', name: '전용회선명', width: 150 },
        { type: '', prop: 'llnum', name: '전용회선번호', width: 150 },
        { type: '', prop: 'custname', name: '고객명', width: 250 },
        { type: '', prop: 'svcmain', name: '서비스 대분류', width: 120 },
        { type: '', prop: 'svcsub', name: '서비스 소분류', width: 120 },
        { type: '', prop: 'svcnet', name: '서비스망 종류', width: 120 },
        { type: '', prop: 'mgmtofficea', name: 'A측 관리국소', width: 150 },
        { type: '', prop: 'instlocationa', name: 'A측 설치국소', width: 150 },
        { type: '', prop: 'instaddra', name: 'A측 설치위치', width: 150 },
        { type: '', prop: 'mgmtofficez', name: 'Z측 관리국소', width: 150 },
        { type: '', prop: 'instlocationz', name: 'Z측 설치국소', width: 150 },
        { type: '', prop: 'instaddrz', name: 'Z측 설치위치', width: 150 }
      ]
      return { options, columns, data: this.influencecircuitList || [] }
    },
    getMbaAlarmFilter() {
      const filteredList = this._cloneDeep(this.alarmList)
      const tItem = this.mapClickedData
      if (tItem === null || tItem.target_type === 'svg') {
        return filteredList || []
      }
      return filteredList.filter(row => {
        let result = true
        const EVENT_TYPE = { svg: 'svg', node: 'node', link: 'link' }
        const PORT_INFO = { IN: 1, OUT: 4 }
        const roadmRoute = [0, 20]

        const monitoredInfo = row.monitored_object
        const slot = monitoredInfo.substr(monitoredInfo.indexOf('slot=') + 5).split('/')[0]
        let port
        if (new RegExp('.+-1+\\(IN\\)', 'g').test(monitoredInfo)) {
          port = PORT_INFO.IN
        } else if (new RegExp('.+-4+\\(OUT\\)', 'g').test(monitoredInfo)) {
          port = PORT_INFO.OUT
        } else {
          this.debug && console.error(`PORT 정보를 찾을 수 없음`)
          return false
        }

        // 중계기 REPEATOR
        // rsspuSlot 02이면 slot=3 하향, slot=16 상향
        // rsspuSlot 17이면 slot=16 하향, slot=3 상향

        // 단국장치 ROADM
        // slot=12 시작점 , slot=14종점
        // port=1 수신(IN) , port=4 송신(OUT)

        const repeater_slot = this.selectedTicket.rsspuSlot
        let device_name

        if (!tItem || tItem.target_type === EVENT_TYPE.svg) { // svg or none click
          return true
        } else if (tItem.target_type === EVENT_TYPE.node) { // node click
          const routenum = tItem.d.routenum
          device_name = tItem.d.device_name

          if (roadmRoute.includes(routenum)) { // ROADM
            result = this.isRoadmSlot(slot, { isComparePort: false })
          } else { // REPEATER
            result = this.isRepeaterSlot(slot, { source: tItem.source, target: tItem.target, isCompareRoutnum: false })
          }
          result = result && device_name === row.sysname
        } else if (tItem.target_type === EVENT_TYPE.link) { // link click
          let resultSource, resultTarget
          const { source, target } = tItem.d
          const repeaterOptions = { source: source, target: target, isCompareRoutnum: true, repeater_slot }
          if (roadmRoute.includes(source.routenum)) {
            resultSource = this.isRoadmSlot(slot, { isComparePort: true, port, link_type: 'source' })
          } else {
            resultSource = this.isRepeaterSlot(slot, repeaterOptions) && port === PORT_INFO.OUT
          }
          resultSource && source.device_name === row.sysname
          if (roadmRoute.includes(target.routenum)) {
            resultTarget = this.isRoadmSlot(slot, { isComparePort: true, port, link_type: 'target' })
          } else {
            resultTarget = this.isRepeaterSlot(slot, repeaterOptions) && port === PORT_INFO.IN
          }
          resultSource = resultSource && source.device_name === row.sysname
          resultTarget = resultTarget && target.device_name === row.sysname
          result = resultSource || resultTarget
        }
        return result
      })
    }
  },
  created() {
    ['bell', 'pause', 'fold'].forEach(key => {
      this.$set(this.otherOtions, key, key !== 'fold')
    })
  },
  mounted() {
    this.loadTicketList()
  },
  methods: {
    getRootCauseDomain(domain) {
      let result = ''
      if (domain === 'ROADM_LH_HW') { // ROADM8THW
        result = '화웨이 LH-ROADM'
      } else if (domain === 'ROADM_DH_HW') { // ROADM8HHW
        result = '화웨이 DH-ROADM'
      }
      return result
    },
    isRoadmSlot(slot, options) {
      let result
      const { isComparePort } = options
      const PORT = { IN: 1, OUT: 4 }
      if (!isComparePort) {
        result = ['12', '14'].includes(slot)
      } else {
        const { port, link_type } = options
        if (link_type === 'source') {
          result = port === PORT.OUT && slot === '12'
        } else if (link_type === 'target') {
          result = port === PORT.IN && slot === '14'
        }
      }
      return result
    },
    isRepeaterSlot(slot, options) {
      let result
      const { isCompareRoutnum } = options
      if (!isCompareRoutnum) {
        result = ['3', '16'].includes(slot)
      } else {
        let condition
        const { source, target, repeater_slot } = options

        if (source.routenum < target.routenum) { // 토폴로지 방향  상향 <-
          condition = { '17': '16', '02': '3' }
        } else if (source.routenum > target.routenum) { // 토폴로지 방향 하향 ->
          condition = { '17': '3', '02': '16' }
        }

        if (['17', '02'].includes(repeater_slot)) {
          result = slot === condition[repeater_slot.toString()]
        } else {
          result = false
        }
      }
      return result
    },
    subscribeEvent() {
      this.addWsEventListener(this.CONSTANTS.channels.RCA_TICKET.name, this.onReceivedMbaTicketEvent)
    },
    realTimePushTest() {
      this.onReceivedMbaTicketEvent({ channelName: 'RCA_TICKET', socketMessage: { message: '{"ticket_type":"RT","eventType":"MBA_NEW","handling_dept":null,"ai_result":0,"root_cause_domain":"CABLE","root_cause_officez":"R00441","ticket_al_id":"26027291","total_alarm_count":0,"root_cause_equip_typez":"ROADM8THW","child_count":null,"mba_sysnamea":"남청중-ROADM-0102-01_남청중1","fault_type":null,"ai_result_commant":null,"trunk_name":"남청중-혜화-W8T-1127","root_cause_checkz":0,"transcircuitseq":null,"root_cause_orga_3":"분당운용팀","root_cause_orga_4":"이천국사","handling_ack_time":null,"root_cause_orga_1":"강남/서부NW운용본부","root_cause_orga_2":"경기남부액세스운용센터","ocaseq":null,"officenamez":"혜화국사","parent_ticket_id":null,"generation_time":"2023-01-28T17:20:47.000+0000","const_no":null,"is_new_ticket":false,"handling_time":"2023-01-28T17:28:10.776+0000","root_cause_equip_typea":"ROADM8THW","rca_accuracy":null,"handling_user":"AI","status":"AUTO_FIN","circuit_yn":null,"root_cause_sysnamez":"혜화-ROADM-2523-02","post_tc":null,"handling_fin_user":"AI","root_cause_checka":0,"handling_fin_dept":null,"total_related_alarm_count":0,"influencecircuit_count":0,"row_signal_line_cnt":0,"locdescriptiona":null,"nature_restoration":"1","mba_sysnamez":"이천중-ROADM-0605-04-04","root_cause_type":"MomentaryBreakoff","ticket_rca_result_orig_dtl_code":null,"officenamea":"이천국사","update_time":"2023-01-30T17:20:47.151+0000","fault_time":"2023-01-28T17:15:00.000+0000","root_cause_metypez":null,"tt_null":false,"root_cause_sys_location":null,"ocaname":null,"handling_agency":null,"handling_fin_content":null,"handling_fin_agency":null,"root_cause_porta":null,"ticket_rca_result_dtl_code":"광레벨 순단 저하","tc_list":null,"direction":"DOWN","locdescriptionz":null,"ticket_rca_type":null,"handling_content":null,"ticket_rca_result_code":"OPTICAL_CABLE_LOW_SIGNAL","type_text":"순단","root_cause_officea":"R00665","root_cause_orgz_4":"혜화국사","root_cause_sysnamea":"이천중-ROADM-0605-04-04","root_cause_orgz_2":"서울강북액세스운용센터","root_cause_orgz_3":"광화문운용팀(혜화)","ticket_id":"23003854","root_cause_orgz_1":"강북/강원NW운용본부","cluster_no":null,"work_fail":"0","root_cause_portz":null,"root_cause_code":"NNI","derivation":false,"ticket_fm_nonfm":null,"root_cause_metypea":null,"ai_result2":null}' } })
    },
    async onReceivedMbaTicketEvent({ channelName, socketMessage }) {
      const { EventType } = this.CONSTANTS.channels.RCA_TICKET
      const message = JSON.parse(socketMessage.message)

      if (channelName !== 'RCA_TICKET' || message.eventType !== EventType.MBA_NEW.name || !this.otherOtions.pause) return

      const item = message.data[0] ?? []
      AppOptions.instance.useWsLog && this.log('RECEIVED SIBSCRIBE MBA TICKET EVENT: ', item)

      const audio = document?.getElementById('mbaAlarm')
      this.otherOtions.bell && audio?.play()

      this.initTicketCtrl(item)

      switch (message.eventType) {
        case EventType.MBA_NEW.name:
          this.$store.dispatch('untact/insertTicketMbaList', item)
          break
        default:
          break
      }
    },
    getTicketList() {
      const searchText = this.untact.fullFilterText
      let list = this._cloneDeep(this.untact.mbaTicketList?.rows || this.mbaTicketList || [])
      list = this._orderBy(list, ['update_time'], ['desc'])
      return list.filter(row => {
        return JSON.stringify(row).includes(searchText)
      })
    },
    azList(row) {
      return this.ticketCtrl[row.ticket_id]?.azList ?? []
    },
    onFilterChanged(changedFilter, code) {
      // console.log('filter changed ===>' + 'changedFilter:' + changedFilter + 'code:' + code)
    },
    onChangeCollapse(value) {
    },
    handleClickOtherOption(key) {
      if (this.otherOtions[key] !== undefined) {
        this.$set(this.otherOtions, key, !this.otherOtions[key])
        // this.otherOtions[key] = !this.otherOtions[key]
      }
    },
    async loadTicketList() {
      const target = ({ vue: this.$refs.ticketPanel })
      try {
        this.openLoading(target, { background: '#dadddf' })
        const res = await apiRcaRequest('SELECT_TICKET_CUR_LIST', { IS_MBA: true, MAX_DAYS: 14 })
        this.mbaTicketList = res?.data || []
        this.$store.dispatch('untact/insertTicketMbaList', this.mbaTicketList)
        this.initTicketCtrl()
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async openDetail(ticket) {
      if (!this.ticketCtrl[ticket.ticket_id]) {
        this.initTicketCtrl(ticket)
      }
      this.loadAzList(ticket)
    },
    onDoubleClickTicket(row) {
      this.selectedTicket = row
    },
    onChangeSelectedTopologyItem(e) {
      this.mapClickedData = e
    },
    loadList(topologyData) {
      this.loadRepeaterSlot()
      this.loadAlarmList()
      this.loadInfluencecircuitList(topologyData)
    },
    async loadAzList(ticket) {
      const ticketCtrl = this._cloneDeep(this.ticketCtrl)
      try {
        const res = await apiRcaRequest('SELECT_TICKET_ROOT_ALARM_LIST', { TICKET_TYPE: ticket.ticket_type, TICKET_ID: ticket.ticket_id, MAX_DAYS: 31 })
        ticketCtrl[ticket.ticket_id].isOpen = true/* !ticketCtrl[ticket.ticket_id].isOpen */
        ticketCtrl[ticket.ticket_id].azList = res?.data ?? []
      } catch (error) {
        this.error(error)
      } finally {
        /*  object는 dom에서 update를 감지하지 못하기 때문에 덮어 씌워주는 코드를 추가함 */
        this.ticketCtrl = ticketCtrl
      }
    },
    async loadAlarmList() {
      const ticket = this.selectedTicket
      try {
        const res = await apiRcaRequest('SELECT_MBA_LOW_ALARM_LIST', { TICKET_ID: ticket.ticket_id })
        this.alarmList = res?.data ?? []
      } catch (error) {
        this.error(error)
      }
    },
    async loadInfluencecircuitList(topologyData) {
      const ticket = this.selectedTicket
      if (!ticket.trunk_name) {
        this.influencecircuitList = []
        return
      }
      try {
        const res = await apiRcaRequest('SELECT_MBA_PMM_INFLUENCECIRCUIT_LIST', { TICKET_ID: ticket.ticket_id, TICKET_TYPE: 'MBA' })
        this.influencecircuitList = res?.data ?? []
      } catch (error) {
        this.error(error)
      }
    },
    async loadRepeaterSlot() {
      const ticket = this.selectedTicket
      try {
        const res = await apiRcaRequest('SELECT_MBA_REPEATER_SLOT_DATA', { TRUNK_NAME: ticket.trunk_name }, '/selectOne')
        Object.assign(this.selectedTicket, { rsspuSlot: res?.data?.rsspuslot })
      } catch (error) {
        this.error(error)
      }
    },
    getRoutenum(topologyData) {
      let routenum
      const { nodes, links } = topologyData

      const startSysname = links.map(v => v.root_cause_sysnamea).find(v => !links.map(v2 => v2.root_cause_sysnamez).includes(v))
      const startAl = links.find(v => v.root_cause_sysnamea === startSysname)

      const startNodeA = nodes.find(v => v.sysname === startAl.root_cause_sysnamea)
      const startNodeZ = nodes.find(v => v.sysname === startAl.root_cause_sysnamez)

      if (startNodeA.name_code === 'REPEATER') {
        routenum = startNodeA.routenum
      } else if (startNodeZ.name_code === 'REPEATER') {
        routenum = startNodeZ.routenum
      }
      return routenum
    },
    onClickCloseDetail(ticket) {
      const ticketCtrl = this._cloneDeep(this.ticketCtrl)
      ticketCtrl[ticket.ticket_id].isOpen = false
      this.ticketCtrl = ticketCtrl
    },
    initTicketCtrl(ticket = null) {
      if (ticket !== null) {
        Object.assign(this.ticketCtrl, { [ticket.ticket_id]: { isOpen: false, type: ticket.ticket_type, azList: [] } })
      } else {
        this.mbaTicketList.forEach(row => { Object.assign(this.ticketCtrl, { [row.ticket_id]: { isOpen: false, type: row.ticket_type, azList: [] } }) })
      }
    },
    async onOpenModalProcess(processType, ticket) {
      if (processType === 'batch') {
        this.$refs.modalRcaBatchProcessing.open({ ticket })
      } else {
        await this.loadAzList(ticket)
        this.$refs.modalProcess.open({ processType, ticket, systemCoreList: this.ticketCtrl[ticket.ticket_id].azList })
      }
    },
    exportExcel(tableRef) {
      const ref = this.$refs
      const timeFormat = this.toStringTime(new Date(), 'YYMMDDHHmmss')
      const title = tableRef === 'alarmGrid' ? '알람 리스트' : '영향회선 리스트'
      ref[tableRef].exportCsv(`${title}_${timeFormat}`)
    },
    getBgColorByStatus(ticket) {
      const { status } = ticket
      let bgColor = 'grey'
      if (Number(ticket.nature_restoration) && status !== 'FIN') {
        bgColor = '#70802b'
      } else {
        switch (status) {
          case 'INIT':
            bgColor = '#b14948'
            break
          case 'ACK':
            bgColor = '#F7AA17'
            break
          case 'FIN':
            bgColor = '#52A43A'
            break
          case 'AUTO_FIN':
            bgColor = '#adcc1e'
            break
          case 'NATURE':
            bgColor = '#70802b'
            break
          default:
            break
        }
      }
      return bgColor
    },
    getFormatStatus(status) {
      let code = status
      if (code === 'AUTO_FIN' || code === 'NATURE') {
        code = 'FIN'
      }
      return this.CONSTANTS.RcaTicketStatus[code]?.text ?? ''
    },
    getFormatterTime(time) {
      return moment(time).format('YYYY-MM-DD HH:mm:ss')
    },
    getRowStyle(params) {
      const level = params.data.value_level
      let bgColor = ''
      switch (level) {
        case 'critical':
          bgColor = '#f56c6c'
          break
        case 'major':
          bgColor = '#fdb025'
          break
        case 'minor':
          bgColor = '#fde66b'
          break
        case 'warning':
          bgColor = '#8bd6e3'
          break
        case 'clear':
          bgColor = '#8bd1375c'
          break
        default:
          break
      }
      // if (params.data.value_level === 'critical') { return { backgroundColor: '#ffb4b4' }
      return { backgroundColor: bgColor }
    }
  }
}
</script>

<style lang="scss">
.MbaTicket {
  font-family: 'NanumSquare';
  transform: rotate(-0.03deg);
  background-color: rgb(246, 246, 246);
  .close .el-collapse-item__header {
    border-radius: 15px;
  }
  .el-collapse-item__header {
    height: 35px;
    transition: all 0.25s;
    border-bottom: solid 1px lightgray;
    border-radius: 15px 15px 0px 0px;
  }
  .el-collapse-item__wrap {
    border-radius: 0 0 15px 15px;
  }
  .el-collapse-item__content {
    padding-bottom: 0px;
  }
  .el-collapse-item__arrow {
    font-weight: 900;
  }
  .el-card__body {
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 0px;
  }
  .el-card {
    border-radius: 20px;
    margin-bottom: 10px;
  }
  .rca-alarm-tab {
    background: #fff;
    border-radius: 0 0 17px 17px;
    margin: 0px 2px;
    .el-tabs__header {
      margin: 0px;
    }
    .el-tabs__content {
      height: calc(100% - 40px);
    }
  }
}
</style>
<style lang="scss" scoped>
.top-container {
  font-size: 20px;
  padding: 8px;
  margin-bottom: 10px;
  border-radius: 10px;
  box-shadow: 1px 3px 5px 5px #93939326;
  i {
    font-size: 20px;
  }
  .title {
    font-size: 19px;
    font-weight: 800;
  }
  .total {
    font-family: 'agGridMaterial';
    font-size: 15px;
    padding-left: 20px;
    font-weight: 900;
    color: black;
    border-left: solid 1px lightgray;
  }
  .function-panel {
    padding: 0px 5px;
    border-left: solid 1px lightgray;
    .el-tooltip {
      margin : 0px 5px;
      &:hover {
        opacity: 0.5;
      }
    }
  }
}
.trunkName {
  position: absolute;
  z-index: 1;
  color: #fff;
  right: 33px;
  top: 20px;
  font-size: 18px;
}
.ticket-panel {
  padding: 20px;
  overflow-y: scroll;
  border-radius: 20px;
  background: #dadddf;
  transition-duration: 0.5s;

  .ticket {
    font-family: 'NanumSquare';
    transform: rotate(-0.03deg);
    transition: all 0.2s;
    border-radius: 20px;
    transform-style: preserve-3d;
    .section-l {
      border-radius: 18px 0px 0px 0px;
      padding: 10px;
      color: #fff;
      width: 175px;
      display: flex;
      flex-direction: column;
      text-align: center;
      justify-content: space-around;
      font-size: 12px;
      font-weight: 600;
      background: #848486;
      div {
        transform: rotate(-0.03deg);
        span {
          white-space:nowrap;
        }
      }
      .alarm-title {
        height: 31px;
        font-size: 18px;
        font-weight: 700;
        transition: all .25s;
      }
    }
    .section-r {
      width: calc(100% - 160px);
      padding: 10px;
      background-color: #fff;
      border-radius: 0px 18px 0px 0px;
      .cableA, .cableZ {
        width: 100%;
        height: 24px;
        display: flex;
        align-items: center;
        span.cable {
          width: 16px;
          height: 16px;
          color: #fff;
          font-size: 11px;
          font-weight: 800;
          text-align: center;
          // padding-left: 15px;
          border-radius: 100%;
          background-color: #f9423e;
        }
        .node {
          width: 273px;
          height: 16px;
          padding: 0px 8px;
          color: #ce5c5b;
          font-weight: 800;
          font-size: 12px;
          margin-left: 10px;
          border-radius: 8px;
          background-color: #7a7c8d38;
          transform: rotate(-0.03deg);
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          text-align: center;
          &:hover {
            text-overflow: clip;
          }
        }
        &:after {
          content: "";
          top: 26px;
          margin-left: 7px;
          height: 16px;
          position: absolute;
          border-right: 2px dotted #f9423e;
        }
      }
      .trunk {
        display: flex;
        align-items: center;
        font-size: 14px;
        font-weight: 800;
        span:nth-child(1) {
          width: 170px;
          overflow: hidden;
          padding-right: 6px;
          white-space: nowrap;
          text-overflow: ellipsis;
          &:hover {
            white-space: normal;
          }
        }
        span {
          transform: rotate(-0.03deg);
        }
      }
      .middle {
        font-size: 12px;
        font-weight: 600;
        height: 35px;
        border-top: solid 1px #d4d7d9;
        border-bottom: solid 1px #d4d7d9;
        span {
          font-size: 20px;
          color: #345035;
        }
        div {
          display: flex;
          transform: rotate(-0.03deg);
          height: 100%;
          align-items: center;
          padding: 0px 10px;
          border-left: solid 1px #d4d7d9;
        }
      }
      .bottom {
        font-size: 13px;
        font-weight: 800;
        color: #666;
        padding: 10px;
        justify-content: space-between;
        div {
          transform: rotate(-0.03deg);
        }
        .alarm-time {
          color: #003f33;
          padding-top: 5px;
        }
        .alarm-process {
          div {
            width: 70px;
            height: 60px;
            line-height: 90px;
            background-position: 50% 11px;
            background-repeat: no-repeat;
            text-align: center;
            font-weight: 800;
            filter: grayscale(100%) brightness(1.5);
            &:hover {
              filter :none;
              color:#f9423e;
              cursor: pointer;
              transition: all 0.25s;
            }
          }
          div.disabled  {
            opacity: 1 !important;
            cursor: unset !important;
            pointer-events: none;
          }
          .ack {
            background-image: url("../../../assets/images/dr_lauren/icon_alarmButton_01.png");
          }
          .fin {
            background-image: url("../../../assets/images/dr_lauren/icon_alarmButton_02.png");
          }
          .process {
            background-image: url("../../../assets/images/dr_lauren/icon_alarmButton_03.png");
          }
          .map {
            background-image: url("../../../assets/images/dr_lauren/icon_alarmButton_04.png");
          }
        }
      }
    }
    &:hover {
      height: 175px !important;
      .alarm-title {
        font-size: 20px;
      }
    }
  }
  .failure-alarm {
    height: 0px;
    overflow-y: auto;
    overflow-x: hidden;
    // transform: rotateX(180deg) translateZ(3px);
    transition-property: all;
    transition-duration: 0.6s;
    transform: rotateX(90deg);
    transform-origin: top;
    .top {
      text-align: center;
      font-weight: 700;
      background-color: rgb(241 241 241);
      i:hover {
        transform: rotate(90deg);
        transition-duration: 0.2s;
      }
      div {
        transform: skew(0.3deg);
      }
    }
    .az-row {
      padding: 5px 15px;
      transform: skew(0.3deg);
      &:hover {
        background: rgba(158, 158, 158, 0.678);
      }
    }
  }
  .filp-open .failure-alarm {
    height: 180px;
    transform: rotateX(0deg) translateZ(0)
  }
  .filp-open .ticket {
    transform: translateZ(0);
  }
}
</style>
