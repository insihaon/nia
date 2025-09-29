<template>
  <div :class="{ [name]: true }">
    <LeftBar class="h-full">
      <template v-if="isViewport('>', 'md')" #leftbar-container>
        <div class="h-20 text-center mt-1">
          <span style="z-index: 1" class="font-bold text-lg whitespace-nowrap"> AI관제 시스템 처리량 </span>
          <div class="d-flex p-2 justify-center items-center">
            <span class="font-semibold whitespace-nowrap pr-2">검색</span>
            <el-radio-group v-model="systemChartCondition.dayType" size="mini" class="d-flex" @change="onLoadDashboardStatistics()">
              <el-radio-button label="DAY">일별</el-radio-button>
              <el-radio-button label="MONTH">월별</el-radio-button>
            </el-radio-group>
            <el-date-picker v-model="systemChartCondition.date" style="width: 150px" size="mini" :picker-options="pickerOptions" :type="systemChartCondition.dayType === 'DAY' ? 'date' : 'month'" />
            <el-button icon="el-icon-search" size="mini" style="padding: 7px 7px" @click="onLoadDashboardStatistics()" />
          </div>
        </div>
        <hr />
        <div style="height: calc(70% - 5rem)">
          <CompChart :options="ticketOptions" class="relative h-64" style="top: -1rem" />
          <CompChart :options="collectOptions" class="relative h-72" style="top: -7rem" />
          <CompChart :options="servingOptions" class="relative h-64" style="top: -12rem" />
        </div>
        <hr />
        <div class="h-20 text-center">
          <div class="font-bold text-lg whitespace-nowrap mt-2">자가 처리 현황</div>
          <div class="d-flex p-2 justify-center items-center">
            <span class="font-semibold whitespace-nowrap pr-2">검색</span>
            <el-radio-group v-model="selfChartCondition.statisticsType" size="mini" class="d-flex" @change="onLoadSelfProcessStatistics()">
              <el-radio-button label="hour">시간별</el-radio-button>
              <el-radio-button label="day">일별</el-radio-button>
              <el-radio-button label="month">월별</el-radio-button>
            </el-radio-group>
            <el-date-picker v-model="selfChartCondition.date" style="width: 150px" size="mini" :type="getSelfProDateType()" />
            <el-button icon="el-icon-search" size="mini" style="padding: 7px 7px" @click="onLoadSelfProcessStatistics()" />
          </div>
        </div>
        <hr />
        <div style="height: calc(30% - 5rem)">
          <CompChart :options="selfProcessOptions" class="h-full" style="min-width: 360px" @click="onClickChart" />
        </div>
      </template>
      <template slot="top-container">
        <filterBar position="TOP">
          <template slot="function-container">
            <div class="filter-container">
              <div class="title">IP-SDN</div>
              <div class="filter-group">
                <div class="d-flex mr-1">
                  <span class="item-title mr-1">검색</span>
                  <el-input v-model="ipspnTextSearch" size="mini" clearable placeholder="테이블내의 내용을 검색합니다." @input="(value) => onChangeTextSearch('ipsdn', value)" />
                </div>
                <template v-for="(filter, keyName) in ipFilterGroup.filters">
                  <div v-if="keyName" :key="filter.filterTitle" class="d-flex mr-1">
                    <div class="item-title">
                      {{ filter.filterTitle || '' }}
                    </div>
                    <ul>
                      <!-- :class="{'filterBtn': !filterIconList.includes(keyName), 'filterIcon d-flex':filterIconList.includes(keyName)}" -->
                      <li
                        v-for="(item, index) in filter.getArray()"
                        :key="index"
                        class="checkItem d-flex items-center checked ml-1"
                        :style="{ 'background-color': item.hex, color: item.color }"
                        @click="onClickFilterItem('ip', filter.filterName, item.code)"
                      >
                        <i :class="item.selected ? 'el-icon-success' : 'el-icon-circle-check'" />
                        <div class="filter-text">{{ item.text + '(' + item.count + ')' }}</div>
                      </li>
                    </ul>
                  </div>
                </template>
              </div>
            </div>
          </template>
        </filterBar>
        <CompAgGrid ref="ipAgGrid" v-model="ipAgGrid" class="w-100 flex-fill" @rowClicked="selectedTicket" @rowDoubleClicked="agGridRowDoubleClicked" />
        <!-- top-container content -->
      </template>
      <template slot="bottom-container">
        <filterBar position="BOTTOM">
          <template slot="function-container">
            <div class="filter-container">
              <div class="title">전송망</div>
              <div class="filter-group">
                <div class="d-flex">
                  <span class="item-title mr-1">검색</span>
                  <el-input v-model="transTextSearch" size="mini" clearable placeholder="검색어를 입력하세요" @input="(value) => onChangeTextSearch('trans', value)" />
                </div>
                <template v-for="(filter, keyName) in transFilterGroup.filters">
                  <div v-if="keyName" :key="filter.filterTitle" class="d-flex mr-1">
                    <div class="item-title ml-2">
                      {{ filter.filterTitle || '' }}
                    </div>
                    <ul v-if="keyName" :key="keyName">
                      <!-- :class="{'filterBtn': !filterIconList.includes(keyName), 'filterIcon d-flex':filterIconList.includes(keyName)}" -->
                      <li
                        v-for="(item, index) in filter.getArray()"
                        :key="index"
                        class="checkItem d-flex items-center checked ml-1"
                        :style="{ 'background-color': item.hex, color: item.color }"
                        @click="onClickFilterItem('trans', filter.filterName, item.code)"
                      >
                        <i :class="item.selected ? 'el-icon-success' : 'el-icon-circle-check'" />
                        <div class="filter-text">{{ item.text + '(' + item.count + ')' }}</div>
                      </li>
                    </ul>
                  </div>
                </template>
              </div>
            </div>
          </template>
        </filterBar>
        <CompAgGrid ref="transmissionAgGrid" v-model="transmissionAgGrid" class="w-100 flex-fill" @rowDoubleClicked="agGridRowDoubleClicked" />
      </template>
    </LeftBar>
  </div>
</template>
<script>
import { Base } from '@/min/Base'
import LeftBar from '@/layout/components/gridTemplate/LeftBar'
import filterBar from '@/layout/components/filterBar'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CompChart from '@/components/chart/CompChart.vue'
import BaseFilterGroup from '@/filters/baseFilterGroup'
import CellRenderAibuttons from '@/views-nia/components/cellRenderer/CellRenderAibuttons'
import CellRenderTicketDetail from '@/views-nia/components/cellRenderer/CellRenderTicketDetail'
import { apiIpAlarmList, apiTransmissionAlarmList, apiDashboardStatistics, apiSelfProcessStatistics } from '@/api/nia'
import { getAlarmType, getSopAiAccuracy, makeAlertMessage } from '@/views-nia/js/commonFormat'
import { AppOptions } from '@/class/appOptions'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import _ from 'lodash'
import { mapState } from 'vuex'
import { getInvisibleSpanParameter, getNiaRouterPathByName, showNumberText } from '@/views-nia/js/commonNiaFunction'

const routeName = 'NiaMain'
export default {
  name: routeName,
  // prettier-ignore
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CompChart, LeftBar, filterBar, CellRenderAibuttons, CellRenderTicketDetail, },
  extends: Base,
  mixins: [dialogOpenMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipFilterGroup: '',
      transFilterGroup: '',
      ipspnTextSearch: '',
      transTextSearch: '',
      ipNetworkList: [],
      activeTimers: {},
      transmissionNetworkList: [],
      selectedItem: [],
      systemChartCondition: {
        dayType: 'DAY',
        date: this.moment().subtract(1, 'd').format('YYYY-MM-DD'),
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
      },
      selfChartCondition: {
        statisticsType: 'hour',
        date: this.moment().format('YYYY-MM-DD'),
      },
      statistics: {},
      selfStatistics: [],
      chartloading: false,
      redrawTimer: null,
      getIpAgGridRightClickMenuItems: (event) => {
        const menuItems = [
          {
            name: '토폴로지 전체보기',
            action: () => {
              this.openNiaTopology(this.ipNetworkList)
            },
          },
          {
            name: 'AI 장애대응(신규)',
            action: () => {
              this.fn_openWindow('aiResponse2', event.node.data)
            },
          },
        ]

        return menuItems
      },
    }
  },
  computed: {
    ipAgGrid() {
      // prettier-ignore
      const columns = [
        { type: '', prop: 'alarmno', name: '알람번호', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return row.alarmno ?? '-' }, },
        { type: '', prop: 'alarmtime', name: '장애 발생시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return this.formatterTimeStamp(row.alarmtime, 'YYYY/MM/DD-HH:mm:ss') }, },
        { type: '', prop: '', name: '어시스턴트', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: this.CONSTANTS.nia.chatbotIcon.assistantIcon, icon: '', type: 'CHANGE_CHATBOT_FOCUS', action: this.iconClickChangeFocusAlertMode.bind(this) } },
        { type: '', prop: '', name: '마감', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '마감', icon: 'edit-outline', type: 'FIN', action: this.handleOpenEditModal.bind(this) }, },
        { type: '', prop: '', name: '조치', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '조치', icon: 'edit-outline', type: 'CONFIG_TEST', action: this.handleOpenEditModal.bind(this), }, },
        { type: '', prop: '', name: 'SOP이력', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: 'SOP', icon: 'circle-check', type: 'SOP', action: this.handleOpenEditModal.bind(this), }, },
        { type: '', prop: '', name: '장애대응', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '장애대응', icon: 'circle-check', type: 'ALARM', action: this.handleOpenEditModal.bind(this), }, },
        { type: '', prop: '', name: '상황전파', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '상황전파', icon: 'circle-check', type: 'NTF', action: this.handleOpenEditModal.bind(this), }, },
        { type: '', prop: 'ticket_id', name: 'TICKET_ID', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'status', name: '상태', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: this.getStatus, cellStyle: this.getCellStyle },
        { type: '', prop: 'ticket_type', name: '전표 유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true, formatter: getAlarmType },
        { type: '', prop: 'fault_type', name: '장애유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg', name: '장애정보', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg_original', name: '알람 원본메시지', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_code', name: '장애내용', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 원인', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'node_num', name: '장비ID', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'node_nm', name: '장비명', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmloc', name: '인터페이스명', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'total_related_alarm_cnt', name: '근원알람개수', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ip_addr', name: 'IP주소', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ai_accuracy', name: 'AI 결과 피드백', width: 100, fixed: false, suppressMenu: true, formatter: getSopAiAccuracy },
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return {
        options,
        columns,
        data: this.ipNetworkList,
        onDoesExternalFilterPass: (externalFilter, node) => {
          return this.onDoesExternalFilterPass(externalFilter, node, 'ip')
        },
        getRightClickMenuItems: this.getIpAgGridRightClickMenuItems,
      }
    },
    transmissionAgGrid() {
      // prettier-ignore
      const columns = [
        { type: '', prop: 'alarmno', name: '알람 번호', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'sysname', name: '장비명', width: 160, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmtime', name: '알람 발생시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmloc', name: '인터페이스명', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg', name: '알람 메시지', width: 120, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: '', name: '마감', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '마감', icon: 'edit-outline', type: 'FIN', action: this.handleOpenEditModal.bind(this) }, },
        { type: '', prop: '', name: '상황전파', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '상황전파', icon: 'circle-check', type: 'NTF', action: this.handleOpenEditModal.bind(this) }, },
        { type: '', prop: 'ticket_id', name: 'TICKET_ID', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_generation_time', name: '전표 발행시간', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'fault_time', name: '전표 마감시간', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'status', name: '상태', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: this.getStatus, cellStyle: this.getCellStyle },
        { type: '', prop: 'ticket_type', name: '전표 유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true, formatter: getAlarmType },
        { type: '', prop: 'root_cause_type', name: '장애유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg', name: '장애정보', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg_original', name: '알람 원본메시지', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_code', name: '장애내용', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 원인', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'total_related_alarm_cnt', name: '근원알람개수', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: '_', name: '상세보기', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderTicketDetail', cellRendererParams: { name: '상세보기', action: this.handleOpenTicketDetail.bind(this) }, },
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return {
        options,
        columns,
        data: this.transmissionNetworkList,
        onDoesExternalFilterPass: (externalFilter, node) => {
          return this.onDoesExternalFilterPass(externalFilter, node, 'trans')
        },
        getRightClickMenuItems: () => {
          return []
        },
      }
    },
    ticketOptions() {
      const keyByTitle = [
        { name: '장애', key: 'ticket_rt_cnt' },
        { name: '광레벨\n저하', key: 'ticket_pf_cnt' },
        { name: '이상\n트래픽', key: 'ticket_att_cnt' },
        { name: '유해\n트래픽', key: 'ticket_ntt_cnt' },
      ]
      return this.getDefaultChartOptions('티켓 발생량', keyByTitle.reverse())
    },
    collectOptions() {
      const keyByTitle = [
        { name: '광레벨\n수집', key: 'trans_perf_cnt' },
        { name: '전송\n경보수집', key: 'trans_alarm_cnt' },
        { name: 'IP시설\n연동', key: 'ip_resource_cnt' },
        { name: 'IP경보\n연동', key: 'ip_alarm_cnt' },
        { name: 'IP트래픽\n연동', key: 'ip_perf_cnt' },
        { name: 'IP SFlow\n연동', key: 'ip_sflow_cnt' },
      ]
      return this.getDefaultChartOptions('데이터 수집량', keyByTitle.reverse())
    },
    servingOptions() {
      const keyByTitle = [
        { name: '시설\n연동', key: 'link_total_resource_cnt' },
        { name: '경보\n연동', key: 'link_total_alarm_cnt' },
        { name: '트래픽\n연동', key: 'link_ip_perf_cnt' },
        { name: '광레벨\n연동', key: 'link_trans_perf_cnt' },
      ]
      return this.getDefaultChartOptions('데이터 제공량(데이터레이크 연계량)', keyByTitle.reverse())
    },
    selfProcessOptions() {
      const selfStatistics = this.selfStatistics
      return {
        legend: {
          data: ['자가최적화 총 발생', '자가최적화 건 수', '자가회복 총 발생', '자가회복 건 수'],
          top: '5%',
        },
        title: {
          // text: '자가 최적화/자가 회복',
          // left: 'center',
          textStyle: {
            fontSize: 13,
          },
        },
        dataZoom: [{ type: 'inside' }, { type: 'slider' }],
        tooltip: {},
        xAxis: {
          type: 'category',
          data: selfStatistics.map((v) => v.series_time),
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: function (value, index) {
              let result = value
              if (value >= 1000) {
                result = value / 1000 + 'K'
              } else {
                result = value.toString()
              }
              return result
            },
          },
        },
        series: [
          {
            name: '자가최적화 총 발생',
            type: 'bar',
            data: this.selfStatistics.map((v) => v.so_totalcount),
          },
          {
            name: '자가최적화 건 수',
            type: 'bar',

            data: this.selfStatistics.map((v) => v.so_count),
          },
          {
            name: '자가회복 총 발생',
            type: 'bar',
            data: this.selfStatistics.map((v) => v.st_totalcount),
          },
          {
            name: '자가회복 건 수',
            type: 'bar',
            data: this.selfStatistics.map((v) => v.st_count),
          },
        ],
      }
    },

    openWindowList() {
      return this.$store.state.mdi.windows
    },

    ...mapState({
      NiaMainEventText: (state) => state.chatbot.routerParameter.NiaMain,
      alarmFocusMode_chatMessages: (state) => state.chatbot.alarmFocusMode_chatMessages,
      alarmFocusTicketData: (state) => state.chatbot.alarmFocusTicketData,
    }),

    chatbotCommand() {
      return this.CONSTANTS.nia.chatbotCommand
    },

    chatbotKeyMap() {
      return this.CONSTANTS.nia.chatbotKeyMap
    },
  },
  watch: {
    NiaMainEventText(nVal, oVal) {
      switch (nVal) {
        case this.chatbotCommand.focusModeCheckAlarm.action:
          this.fn_openWindow('niaTopology', this.alarmFocusTicketData, null, { addX: -580 })
          this.fn_openWindow('aiResponse', this.alarmFocusTicketData, null, { addX: 580, addY: -20 })
          break
      }

      switch (nVal.actionName) {
        case this.chatbotCommand.onReceivedIpsdnTicketEvent.action:
          this.onReceivedIpsdnTicketEvent(nVal.data)
          break
        case this.chatbotCommand.onReceivedTransTicketEvent.action:
          this.onReceivedTransTicketEvent({ channelName: nVal.channelName, socketMessage: nVal.socketMessage })
          break
      }

      this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: this.$route.name })
    },

    viewport(nVal, oVal) {
      let sideSize = 20
      if (this.isViewport('<=', 'sm')) {
        sideSize = 0
      }
      this.onChangeSidePaneSize(sideSize)
    },
  },
  async mounted() {
    await this.onLoadDashboardStatistics()
    await this.onLoadSelfProcessStatistics()

    this.$nextTick(async () => {
      await this.onLoadIpAlarmList()
      await this.onLoadTransmissionAlarmList()

      this.ipFilterGroup = new BaseFilterGroup(this, {
        onFilterChanged: () => this.onFilterChanged('ip'),
        isCheckBox: false,
      })
      this.setIPFilterGroup()

      this.transFilterGroup = new BaseFilterGroup(this, {
        onFilterChanged: () => this.onFilterChanged('trans'),
        isCheckBox: false,
      })
      this.setTransFilterGroup()

      window.changeFocusAlertMode = this.changeFocusAlertMode.bind(this)
    })
  },

  beforeUnmount() {
    window.changeFocusAlertMode = null
  },
  methods: {
    openNiaTopology(param) {
      const existTopologyWindow = this.openWindowList.filter((openWindow) => {
        return openWindow.dialogNm === 'niaTopology'
      })

      if (existTopologyWindow.length > 0) {
        existTopologyWindow.forEach((w) => {
          this.$store.dispatch('mdi/removeWindow', w.id)
        })
      }

      this.fn_openWindow('niaTopology', param)
    },

    async onReceivedIpsdnTicketEvent(data) {
      // prettier-ignore
      (async () => {
        switch (data.eventType) {
          case 'TICKET_NEW':
            // prettier-ignore
            (async () => {
              window.fn_openWindow = this.fn_openWindow

              const res = await apiIpAlarmList({ TICKET_ID: data.ticketId })
              if (res) {
                  const ticketData = res.result[0]
                  const isSop = true
                  this.notifyAlert(ticketData, isSop)
              }
            })()
            break
          case 'TICKET_UPDATE':
          case 'TICKET_MERGE':
            // prettier-ignore
            (async () => {
              const ticket = this.ipNetworkList.find((v) => v.ticket_id === data.ticketId)
              if (ticket) {
                Object.assign(ticket, data.properties)
                this.ipNetworkList = _.cloneDeep(this.ipNetworkList)
              } else {
                const param = { TICKET_ID: data.ticketId, }
                const res = await apiIpAlarmList(param)
                if (res) {
                  this.ipNetworkList.splice(0, 0, res.result[0])
                } else {
                  console.error(`${data.eventType} FAIL.. TICKET_ID : ` + data.ticketId)
                }
              }
            })()
            break
          case 'TICKET_DELETE':
            // prettier-ignore
            (() => {
              const ticket = this.ipNetworkList.find((v) => v.ticket_id === data.ticketId)
              if (ticket) {
                this.ipNetworkList.remove(ticket)
              } else {
                console.error(`${data.eventType} FAIL.. TICKET_ID : ` + data.ticketId)
              }
            })()
            break
        }
      })()

      this.$store.dispatch('nia/insertIpNetworkList', this.ipNetworkList)
    },

    iconClickChangeFocusAlertMode(row) {
      if (this.alarmFocusTicketData.ticket_id === row.ticket_id) {
        this.$message('이미 어시스턴트가 활성화된 티켓입니다')
      } else {
        this.changeFocusAlertMode(row.ticket_id)
      }
    },

    changeFocusAlertMode(ticketId) {
      const agGridElement = this.$refs.ipAgGrid.$el

      this.$refs.ipAgGrid.gridApi.forEachNode((node) => {
        const rowIndex = node.rowIndex
        const rowElement = agGridElement.querySelector(`.ag-center-cols-clipper .ag-row[row-index="${rowIndex}"]`)
        rowElement && rowElement.classList.remove('highlight-row')
        if (node.data.ticket_id === ticketId) {
          if (!this.alarmFocusTicketData.ticket_id) {
            this.$store.dispatch('chatbot/newAlarmFocusChat', { ticketData: node.data })
            this.fn_openWindow('chatbot')
            rowElement.classList.add('highlight-row')
          } else if (this.alarmFocusTicketData.ticket_id === node.data.ticket_id) {
            this.$store.commit('chatbot/MODE_CHANGE', { newMode: 'alarmFocusMode' })
          } else {
            this.$confirm(
              `
              기존과 다른 Ticket입니다.
              기존에 감시중이던 🟢팝업의 ticket_id가
              <span style='color:red'>${ticketId}</span>로 변경되며 새로 데이터를 가져옵니다.
              진행하시겠습니까?`,
              '집중경보 전환',
              {
                confirmButtonText: '실행',
                cancelButtonText: '취소',
                dangerouslyUseHTMLString: true,
                customClass: 'nia-message-box',
              }
            ).then(() => {
              this.$store.dispatch('chatbot/newAlarmFocusChat', { ticketData: node.data, isNew: true })
              this.fn_openWindow('chatbot')
              rowElement.classList.add('highlight-row')
            })
          }
        }
      })
    },

    notifyAlert(ticketData, isSop) {
      window.notifyAlert_window = (p1, p2) => {
        this.notifyAlert(p1, p2)
      }

      // prettier-ignore
      this.$notify({
        title: '경보',
        dangerouslyUseHTMLString: true,
        message: makeAlertMessage(ticketData, isSop) +
          `<div style="display:flex; gap:8px; justify-content:flex-end; margin-top:8px;">` +
          /* `<button class='button--primary' onclick='window.fn_openWindow("configTest", ${JSON.stringify(ticketData)})'>진행</button>` + */
          `<button class='button--primary' onclick='window.changeFocusAlertMode(${JSON.stringify(ticketData.ticket_id)})'>집중경보</button>` +
          /* (isSop ? `<button class='button--primary' onclick='window.notifyAlert_window(${JSON.stringify(ticketData)}, false)'>취소</button>` : '') + */
        '</div>',
        customClass: 'nia-notify',
      })
    },

    onReceivedTransTicketEvent({ channelName, socketMessage }) {
      if (channelName !== 'TRANS_ALARM') return

      const data = JSON.parse(socketMessage.message)
      AppOptions.instance.useWsLog && this.log('RECEIVED SIBSCRIBE TRANS_ALARM EVENT: ', data)

      this.transmissionNetworkList = [].concat(...this.getMergedList('transmissionNetworkList', data))
      this.$store.dispatch('nia/insertTransmissionNetworkList', this.transmissionNetworkList)
    },
    getMergedList(listName, newAlarm) {
      if (!listName) return
      const temp = this._cloneDeep(this[listName])
      newAlarm.forEach((row) => {
        const mergedIndex = row.alarmno ? temp.findIndex((orgRow) => orgRow.alarmno === row.alarmno) : temp.findIndex((orgRow) => orgRow.ticket_id === row.ticket_id)
        if (mergedIndex !== -1) {
          Object.assign(temp[mergedIndex], row)
        } else {
          temp.push(row)
        }
      })
      this._orderBy(temp, ['desc', 'alarmtime'])
      return temp
    },
    onChangeTextSearch(type, value) {
      const refName = type === 'ipsdn' ? 'ipAgGrid' : 'transmissionAgGrid'
      this.$refs[refName].gridApi.setQuickFilter(value)
    },
    onClickChart(e) {
      const params = {
        DATE_TYPE: this.selfChartCondition.statisticsType,
        DATE_TIME: e.name,
        SELF_PROCESS_GROUP: e.seriesName.includes('최적화') ? 'SO' : 'ST',
      }
      const pageTitle = params.SELF_PROCESS_GROUP === 'SO' ? '자가 최적화 이력조회' : '자가 회복 이력조회'
      this.fn_openWindow('selfProcessList', params, null, { name: pageTitle })
    },
    setIPFilterGroup() {
      const listName = 'ipNetworkList'
      const btnOption = {
        isMultiSelect: true,
        allItem: true,
        ifAllthenOtherUncheck: true,
        listName,
      }

      this.ipFilterGroup.addFilter('ipStatus', '상태', this.CONSTANTS.nia.statusType, btnOption) // 상태
      this.ipFilterGroup.addFilter('ipType', '전표 유형', this.CONSTANTS.nia.ipType, btnOption) // ip망 장애 종류
      // this.ipFilterGroup.addFilter('ipAlarmType', '알람 종류', this.CONSTANTS.nia.ipAlarmType, btnOption) // ip망 알람 종류
    },
    setTransFilterGroup() {
      const listName = 'transmissionNetworkList'
      const btnOption = {
        isMultiSelect: true,
        allItem: true,
        ifAllthenOtherUncheck: true,
        listName,
      }

      this.transFilterGroup.addFilter('transStatus', '상태', this.CONSTANTS.nia.statusType, btnOption) // 상태
      this.transFilterGroup.addFilter('transType', 'TYPE', this.CONSTANTS.nia.transType, btnOption) // 전송망 장애 종류
    },
    onFilterChanged(type) {
      this.$refs[type === 'ip' ? 'ipAgGrid' : 'transmissionAgGrid'].externalFilterChanged({
        name: this.name,
      })
    },
    onClickFilterItem(filterType, name, code) {
      if (filterType === 'ip') {
        this.ipFilterGroup.onItemClick(name, code)
      } else {
        this.transFilterGroup.onItemClick(name, code)
      }
      this.$forceUpdate()
    },
    onDoesExternalFilterPass(externalFilter, node, gridType) {
      const filterGroup = gridType === 'ip' ? 'ipFilterGroup' : 'transFilterGroup'
      if (!this[filterGroup].filters) return true

      const { data: row } = node
      let resMultiCondition = true
      const multiFilterKeys = Object.keys(this[filterGroup].filters).filter((key) => this[filterGroup].filters[key].options.isMultiSelect)

      resMultiCondition = multiFilterKeys
        .map((mkey) => {
          return this[filterGroup].filters[mkey].dataArray.some((item) => {
            if (item.code !== 'All') {
              if (typeof item.fnFilter !== 'function') {
                return
              } else if (item.selected) {
                return item.fnFilter(row)
              }
            } else return item.selected
          })
        })
        .every((res) => res)
      return resMultiCondition
    },
    async onLoadIpAlarmList(param) {
      try {
        const res = await apiIpAlarmList(param)
        this.ipNetworkList = res?.result
        this.$store.dispatch('nia/insertIpNetworkList', this.ipNetworkList)

        return res
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadTransmissionAlarmList() {
      try {
        const res = await apiTransmissionAlarmList()
        this.transmissionNetworkList = res?.result
        this.$store.dispatch('nia/insertTransmissionNetworkList', this.transmissionNetworkList)
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadDashboardStatistics(reset = false) {
      const { dayType: DATE_TYPE, date } = this.systemChartCondition
      let cloneDate = this._cloneDeep(date)
      if (this.systemChartCondition.dayType === 'MONTH') {
        cloneDate = this.moment(cloneDate).subtract(1, 'M').format('YYYY-MM')
      }
      if (reset) {
        cloneDate = this.moment().subtract(1, 'd').format('YYYY-MM-DD')
      }
      const formatStr = DATE_TYPE === 'DAY' ? 'YYYY-MM-DD' : 'YYYY-MM'
      const defaultSt = {
        trans_alarm_cnt: 0,
        link_ip_perf_cnt: 0,
        link_total_alarm_cnt: 0,
        ticket_ntt_cnt: 0,
        ticket_att_cnt: 0,
        ip_alarm_cnt: 0,
        ticket_rt_cnt: 0,
        link_total_resource_cnt: 0,
        trans_perf_cnt: 0,
        ip_resource_cnt: 0,
        ticket_pf_cnt: 0,
        ip_perf_cnt: 0,
        ip_sflow_cnt: 0,
        link_trans_perf_cnt: 0,
      }
      try {
        const res = await apiDashboardStatistics({
          DATE_TYPE,
          SEARCH_DATE: this.moment(cloneDate).format(formatStr),
        })
        this.statistics = res.result[0] ?? defaultSt
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadSelfProcessStatistics() {
      const { statisticsType: STATISTICS_TYPE, date } = this.selfChartCondition
      const SERIES_TYPE = STATISTICS_TYPE === 'hour' ? 'day' : this.getSelfProDateType()
      try {
        const resSelfProcess = await apiSelfProcessStatistics({
          STATISTICS_TYPE,
          SERIES_TYPE,
          DATE: this.moment(date).add(1, 'd'),
        })
        this.selfStatistics = resSelfProcess.result ?? []
      } catch (error) {
        this.error(error)
      }
    },
    onChangeSidePaneSize(val) {
      window.helpe?.$store.dispatch('settings/changeSetting', {
        key: 'sidePaneSize',
        value: val,
      })
    },
    getDefaultChartOptions(title, keyByTitle) {
      return {
        title: {
          text: title,
          left: 'center',
          top: 40,
          textStyle: {
            fontSize: 13,
          },
          // subtext: 'Living Expenses in Shenzhen'
        },
        xAxis: {
          // type: 'category',
          type: 'value',
          // data: keyByTitle.map(v => v.name),
          axisLabel: {
            formatter: function (value, index) {
              let result = value
              if (value >= 1000000) {
                result = value / 1000000 + 'M'
              } else if (value >= 1000) {
                result = value / 1000 + 'K'
              } else {
                result = value.toString()
              }
              return result
            },
          },
          // axisLabel: { interval: 0, rotate: 20, fontWeight: 'bold', },
        },
        yAxis: {
          // type: 'value',
          type: 'category',
          data: keyByTitle.map((v) => v.name),
          // axisLabel: {
          //   formatter: function (value, index) {
          //     let result = value
          //   if (value >= 1000000) {
          //     result = (value / 1000000) + 'M'
          //   } else if (value >= 1000) {
          //     result = (value / 1000) + 'K'
          //   } else {
          //     result = value.toString()
          //   }
          //     return result
          //   }
          // }
        },
        series: [
          {
            type: 'bar',
            label: {
              show: true,
              width: 20,
              position: 'right',
              fontWeight: 'bold',
              formatter: (param) => {
                return param.data.toLocaleString()
              },
            },
            barWidth: '20',
            itemStyle: {
              color: '#6149c7',
              borderWidth: 1,
              borderColor: '#189ec0',
              borderRadius: [0, 5, 5, 0],
            },
            data: keyByTitle.map((v) => {
              return this.statistics[v.key] === null ? 0 : this.statistics[v.key]
            }),
          },
        ],
      }
    },
    getSelfProDateType() {
      let type = 'date'
      switch (this.selfChartCondition.statisticsType) {
        case 'hour':
          type = 'date'
          break
        case 'day':
          type = 'month'
          break
        case 'month':
          type = 'year'
          break
        default:
          break
      }
      return type
    },
    getStatus(row, col, value, index) {
      let result = ''
      switch (row.status) {
        case 'INIT':
          result = '발생'
          break
        case 'ACK':
          result = '인지'
          break
        case 'FIN':
          result = '수동마감'
          break
        case 'AUTO_FIN':
          result = '자동마감'

          break
        default:
          break
      }
      return result
    },
    getCellStyle(params) {
      let color = ''
      let background = ''
      switch (params.value) {
        case 'INIT':
          background = '#b14948'
          color = '#fff'
          break
        case 'ACK':
          background = '#f7aa17'
          break
        case 'FIN':
          background = '#52a43a'
          break
        case 'AUTO_FIN':
          background = '#adcc1e'
          break
        default:
          break
      }
      return { 'background-color': background, color: color, 'font-weight': 600 }
    },
    selectedTicket(param) {
      this.selectedItem = param
    },
    agGridRowDoubleClicked(selectedItems) {
      const data = this.selectedItem?.data || {}
      this.openNiaTopology(data)
    },
    handleOpenTicketDetail(row) {
      this.fn_openWindow('ticketDetail', row)
    },
    handleOpenEditModal(row, type) {
      if (type === 'SOP') {
        this.fn_openWindow('sopHistory', row)
      } else if (type === 'NTF') {
        this.fn_openWindow('requestForAction', row)
      } else if (type === 'ALARM') {
        this.fn_openWindow('aiResponse', row)
      } else if (type === 'FIN') {
        this.fn_openWindow('processFin', row)
      } else if (type === 'CONFIG_TEST') {
        this.fn_openWindow('configTest', row)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
@import '~@/styles/variables.scss';

.NiaMain {
  ::v-deep .CHANGE_CHATBOT_FOCUS-class {
    font-size: 14px;
    background: black;
    height: 25px;
    width: 25px;
    border-radius: 25px;
    margin-left: 10px;
    text-indent: -3px;
  }

  ::v-deep .splitter-pane {
    display: flex;
    min-width: 25% !important;
    flex-direction: column;
  }
  ::v-deep .el-date-picker {
    position: absolute;
    z-index: 1;
  }
  ::v-deep.splitter-pane-resizer {
    z-index: 0;
  }

  .filter-container {
    height: 100%;
    display: flex;
    align-items: center;
    .title {
      height: 100%;
      display: flex;
      font-size: 18px;
      font-weight: 600;
      padding: 0px 10px;
      align-items: center;
      letter-spacing: 1.2px;
      white-space: nowrap;
    }
    .filter-group {
      display: flex;
      flex-wrap: wrap;
      margin-left: 10px;
      .split {
        &:before {
          content: '|';
          padding-left: 5px;
          font-weight: 700;
        }
      }
      ::v-deep .el-input--mini .el-input__inner {
        height: 27px;
        width: 300px;
        background: #e8ecef;
        border: solid 1px #a3a3a3;
      }
      ::v-deep .el-input__suffix {
        top: -1px;
      }
      .item-title {
        display: flex;
        align-items: center;
        font-weight: 600;
        white-space: nowrap;
      }
      .filter-text {
        white-space: nowrap;
      }
      ul {
        display: flex;
      }
    }
  }
  ::v-deep.CompAgGrid .ag-cell {
    font-size: 12px;
  }
}
</style>
