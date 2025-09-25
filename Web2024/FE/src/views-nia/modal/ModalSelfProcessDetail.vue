<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :top.sync="top"
        :width="domElement.maxWidth + `px`"
        :height="domElement.minHeight + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          자가 {{ pageType }} 이력 상세보기
          <hr>
        </span>
        <el-row class="w-full">
          <el-col class="p-1">
            <el-card shadow="never" :body-style="{'padding': '10px'}">
              <div slot="header">
                <div>
                  <span><i class="el-icon-document" /> 알람 정보</span>
                </div>
              </div>
              <el-col>
                <table class="ticket-info basic">
                  <thead>
                    <tr>
                      <th>
                        <span>{{ processType !== 'S' ? '티켓' : '알람' }}번호</span>
                      </th>
                      <th><span>타입</span></th>
                      <th><span>발생시간</span></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr class="repeated-item">
                      <td>{{ selectedRow.ticket_id }}</td>
                      <td>{{ getTicketType(null, null, selectedRow.self_process_type) }}</td>
                      <td>{{ formatterTime(selectedRow.occur_time) }}</td>
                    </tr>
                  </tbody>
                </table>
              </el-col>
            </el-card>
            <el-card shadow="never" :body-style="{'padding': '10px'}" >
              <div slot="header">
                <div ref="sopList">
                  <span><i class="el-icon-data-line" /> {{ pageType === '최적화' ? '트래픽 상세 정보' : '연관 SOP 리스트' }}</span>
                </div>
              </div>
              <el-col v-if="pageType === '최적화'">
                <el-row :gutter="10" >
                  <el-col :span="12">
                    <div slot="header">
                      <span><i class="el-icon-document" /> TRAFFIC 그래프(MBPS)</span>
                    </div>
                    <CompChart :options="trafficChartMbps" :chart-loading="chartLoading" style="width : 370px" class="h-64 w-100" />
                  </el-col>
                  <el-col :span="12">
                    <div slot="header">
                      <span><i class="el-icon-document" /> TRAFFIC 그래프(PPS)</span>
                    </div>
                    <CompChart :options="trafficChartPps" :chart-loading="chartLoading" style="width : 370px" class="h-64 w-100" />
                  </el-col>
                </el-row>
              </el-col>

              <el-col v-else class="shadow-sm p-1 mt-2">
                <div v-if="sopHistList.length > 0" class="overflow-auto" style="max-height: 250px">
                  <CompAgGrid ref="sopList" v-model="sopAgGrid" class="w-100" style="height: 200px" />
                </div>
                <div v-else class="d-flex items-center justify-center text-lg font-semibold" style="height: 100px">연관 SOP 데이터가 존재하지 않습니다.</div>
              </el-col>
            </el-card>

            <el-card v-if="selectedRow.ticket_type !== 'SYSLOG'" shadow="never" :body-style="{'padding': '10px'}" class="mt-1">
              <div slot="header">
                <div>
                  <span><i class="el-icon-document" /> AI 탐지 정보</span>
                </div>
              </div>
              <el-col>
                <table v-if="selectedRow.ticket_type === 'FTT'" class="ticket-info basic">
                  <thead>
                    <tr>
                      <th><span>장애 확률</span></th>
                      <th><span>비장애 확률</span></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr class="repeated-item">
                      <td>{{ selectedRow.zero1_entropy ? `${(selectedRow.zero1_entropy * 100).toFixed(1)}%` : '' }}</td>
                      <td>{{ selectedRow.zero1_entropy ? `${((1 - selectedRow.zero1_entropy) * 100).toFixed(1)}%` : '' }}</td>
                    </tr>
                  </tbody>
                </table>
                <table v-else-if="aiDetection !== null" class="ticket-info basic">
                  <thead>
                    <tr>
                      <th><span>/</span></th>
                      <th colspan="2"><span>IN</span></th>
                      <th colspan="2"><span>OUT</span></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr class="repeated-item">
                      <td>BPS</td>
                      <td colspan="2">{{ aiDetection.in_bps + 'MB' }}</td>
                      <td colspan="2">{{ aiDetection.out_bps + 'MB' }}</td>
                    </tr>

                    <tr class="repeated-item">
                      <td>PREDICT</td>
                      <td colspan="2">{{ aiDetection.in_predict + 'MB' }}</td>
                      <td colspan="2">{{ aiDetection.out_predict + 'MB' }}</td>
                    </tr>

                    <tr class="repeated-item">
                      <td>THRESHOLD_UPPER</td>
                      <td colspan="2">{{ aiDetection.in_threshold_upper + 'MB' }}</td>
                      <td colspan="2">{{ aiDetection.out_threshold_upper + 'MB' }}</td>
                    </tr>

                    <tr class="repeated-item">
                      <td>ANOMALY</td>
                      <td colspan="2">{{ aiDetection.in_anomaly }}</td>
                      <td colspan="2">{{ aiDetection.out_anomaly }}</td>
                    </tr>
                  </tbody>
                </table>
                <div v-else class="d-flex items-center justify-center text-lg font-semibold" style="height: 100px">AI 탐지 정보가 존재하지 않습니다.</div>
              </el-col>
            </el-card>
            <el-card v-else shadow="never" :body-style="{'padding': '10px'}" class="mt-1">
              <div slot="header">
                <div>
                  <span><i class="el-icon-document" /> 알람 상세 정보</span>
                </div>
              </div>
              <el-col>
                <el-table
                  ref="table"
                  size="mini"
                  :data="[syslogInfo]"
                  class="w-100"
                  :height="90"
                  border
                  fit
                >
                  <el-table-column
                    v-for="col in defineSyslogDetailTable"
                    :key="col.prop"
                    :prop="col.prop"
                    :label="col.name"
                    :width="col.width"
                  />
                </el-table>
              </el-col>
            </el-card>

            <el-card shadow="never" :body-style="{'padding': '10px'}" class="mt-1">
              <div slot="header">
                <div>
                  <span><i class="el-icon-document" /> 설명</span>
                </div>
              </div>
              <el-col>
                <div class="d-flex flex-column p-2 items-center text-xs text-center">
                  <span>&nbsp;{{ pageType === '최적화' ? '티켓' : '알람' }}번호 :{{ selectedRow.ticket_id || '' }}</span>
                  <span>&nbsp;티켓의 정보를 참고하여 "수동 마감" 처리를 원하실 경우</span>
                  <span>&nbsp;하단의 <button style="color: white; background-color: rgb(31, 51, 92); border: none; font-size: 12px; padding: 3px">AI 장애대응(수동)</button>버튼을 클릭하여
                    "수동 마감" 하시기 바랍니다.</span>
                  <span>&nbsp;티켓의 정보가 상단의 정보와 일치하여 "자동 마감"을 원하신다면
                    <button style="color: white; background-color: rgb(31, 51, 92); border: none; font-size: 12px; padding: 3px">닫기</button> 버튼을 클릭하시기 바랍니다.
                  </span>
                </div>
              </el-col>
            </el-card>
          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer">
          <hr>
          <el-button size="mini" icon="el-icon-s-ticket" @click.native="onOpenAiResponse"> AI 장애대응(수동) </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="close()">
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
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import {
  apiSelfProcessTrafficInfo,
  apiATTTrafficChart,
  apiNTTTrafficChart,
  apiSelectAiDetectionInfo,
  apiSelfProcessSyslogInfo,
  apiSelectSopHistList,
  apiSopSyslogHistList,
} from '@/api/nia'
import { getTicketType, formatterTime } from '@/views-nia/js/commonFormat'
import CompChart from '@/components/chart/CompChart.vue'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'

const routeName = 'ModalSelfProcessDetail'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CellRenderDataSetButtons, CompInquiryPannel, CompChart },
  directives: { elDragDialog },
  extends: Modal,
  mixins: [dialogOpenMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      top: '3vh',
      chartLoading: false,
      getTicketType,
      formatterTime,
      selectedRow: null,
      // SO
      trafficInfo: null,
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
      trafficChartList: [],
      // ST
      syslogInfo: {
        ruleid: '',
        alarmmsg: '',
        alarmlvl: '',
        etc: '',
        alarmno: '',
        alarmtime: '',
        nodenm: '',
        nodenu: '',
        alarmloc: '',
        status: '',
      },
      sopHistList: [],
      defineSolHistTable: {
        col: ['알람 번호', '장비명', '인터페이스', '장애구분', '장애유형', '조치내용', '원본메시지', '발생시간', '마감시간', '마감자'],
        valueKey: ['alarmno', 'node_nm', 'alarmloc', 'fault_classify', 'fault_type', 'fault_detail_content', 'etc', 'alarm_occur_time', 'handling_fin_time', 'handling_fin_user'],
      },
      defineSyslogDetailTable: [
        { type: '', prop: 'alarmno', name: '알람번호', width: 100 },
        { type: '', prop: 'alarmtime', name: '발생시간', width: 140, },
        { type: '', prop: 'node_num', name: '장비번호', width: 100, },
        { type: '', prop: 'node_nm', name: '장비명', width: 100, },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 100, },
        { type: '', prop: 'alarmmsg', name: '알람메시지', width: 150, },
        { type: '', prop: 'etc', name: '원본메시지', width: 230, },
      ],
    }
  },
  computed: {
    sopAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
      const sopColumns = [
        { type: '', prop: 'alarmno', name: '알람 번호', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'node_nm', name: '장비명', width: 50, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false /* , formatter: getAlarmType */ },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'fault_classify', name: '장애구분', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'fault_type', name: '장애유형', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'etc', name: '원본메시지', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'alarm_occur_time', name: '발생시간', width: 50, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'handling_fin_time', name: '마감시간', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'handling_fin_user', name: '마감자', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
      ]
      const syslogColumns = [
        { type: '', prop: 'alarmno', name: '알람 번호', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'alarmtime', name: '알람 시간', width: 50, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false /* , formatter: getAlarmType */ },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'node_num', name: '장비 ID', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'node_nm', name: '장비명', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'alarmmsg', name: '장애내용', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 50, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'etc', name: '원본 메시지', width: 100, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
      ]
      const columns = this.selectedRow.ticket_type === 'SYSLOG' ? syslogColumns : sopColumns
      return { options, columns, data: this.sopHistList }
    },
    trafficChartPps() {
      const { ticket_type } = this.selectedRow
      const chartData = this.trafficChartList
      const xAxisKey = ['ATT2', 'FTT'].includes(ticket_type) ? 'measured_datetime' : 'collect_time'
      const markLine = {
        symbol: ['none', 'none'],
        label: { show: false },
        data: [{ xAxis: this.selectedRow?.fault_time || '' }],
      }

      let seriesArr = []
      if (['ATT2', 'FTT'].includes(ticket_type)) {
        seriesArr = [
          {
            markLine,
            name: 'PPS_IN',
            type: 'line',
            data: chartData.map((v) => v.fltpps_in),
          },
          {
            markLine,
            name: 'PPS_OUT',
            type: 'line',
            data: chartData.map((v) => v.fltpps_out),
          },
        ]
      } else {
        seriesArr = [
          {
            markLine,
            name: 'STRCOUNTS',
            type: 'line',
            data: chartData.map((v) => v.strcounts),
          },
          {
            name: 'STRBYTES_COL',
            type: 'line',
            data: chartData.map((v) => v.strbytes_col),
          },
        ]
      }

      return {
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          top: '5%', // 상단에 위치
          left: 'center', // 중앙 정렬
          orient: 'horizontal', // 가로 방향 정렬
        },
        dataZoom: [{ type: 'inside' }],
        xAxis: {
          type: 'category',
          data: chartData.map((v) => formatterTime(v[xAxisKey])),
        },
        yAxis: {
          type: 'value',
        },
        series: seriesArr,
      }
    },

    trafficChartMbps() {
      const { ticket_type } = this.selectedRow
      const chartData = this.trafficChartList
      const xAxisKey = ['ATT2', 'FTT'].includes(ticket_type) ? 'measured_datetime' : 'collect_time'
      const markLine = {
        symbol: ['none', 'none'],
        label: { show: false },
        data: [{ xAxis: this.selectedRow?.fault_time || '' }],
      }

      const colorMap = {
        MBPS_IN: '#ffcc00',
        IN_THRESHOLD_UPPER: 'rgba(255, 204, 0, 0.3)',
        MBPS_OUT: '#ff7043',
        OUT_THRESHOLD_UPPER: 'rgba(255, 112, 67, 0.3)',
      }

      let seriesArr = []
      let topLegend = []
      let bottomLegend = []
      if (['ATT2', 'FTT'].includes(ticket_type)) {
        seriesArr = [
          {
            markLine,
            name: 'MBPS_IN',
            type: 'line',
            data: chartData.map((v) => v.fltbps_in),
            itemStyle: { color: colorMap.MBPS_IN },
          },
          {
            markLine,
            name: 'MBPS_OUT',
            type: 'line',
            data: chartData.map((v) => v.fltbps_out),
            itemStyle: { color: colorMap.MBPS_OUT },
          },
          {
            name: 'IN_THRESHOLD_UPPER',
            type: 'line',
            data: chartData.map((v) => v.in_threshold_upper),
            smooth: true,
            itemStyle: { color: colorMap.IN_THRESHOLD_UPPER },
            areaStyle: { color: colorMap.IN_THRESHOLD_UPPER },
            lineStyle: { width: 0, },
            symbol: 'none', // 점 제거
          },
          {
            name: 'OUT_THRESHOLD_UPPER',
            type: 'line',
            data: chartData.map((v) => v.out_threshold_upper),
            smooth: true,
            itemStyle: { color: colorMap.OUT_THRESHOLD_UPPER },
            areaStyle: { color: colorMap.OUT_THRESHOLD_UPPER },
            lineStyle: { width: 0 },
            symbol: 'none',
          },
        ]
        topLegend = ['MBPS_IN', 'MBPS_OUT'] // 위쪽 레전드 항목
        bottomLegend = ['IN_THRESHOLD_UPPER', 'OUT_THRESHOLD_UPPER'] // 아래쪽 레전드 항목
      } else {
        seriesArr = [
          {
            markLine,
            name: 'STRCOUNTS',
            type: 'line',
            data: chartData.map((v) => v.strcounts),
            itemStyle: { color: '#4575bc' },
          },
          {
            name: 'STRBYTES_COL',
            type: 'line',
            data: chartData.map((v) => v.strbytes_col),
            itemStyle: { color: '#8dc2e5' },
          },
        ]
        topLegend = ['STRCOUNTS']
        bottomLegend = ['STRBYTES_COL']
      }

      return {
        tooltip: { trigger: 'axis', },
        legend: [
          {
            data: topLegend,
            top: '3%',
            orient: 'horizontal',
          },
          {
            data: bottomLegend,
            top: '12%',
            orient: 'horizontal',
          },
        ],
        dataZoom: [{ type: 'inside' }],
        xAxis: {
          type: 'category',
          data: chartData.map((v) => formatterTime(v[xAxisKey])),
        },
        yAxis: { type: 'value', },
        series: seriesArr,
      }
    },
    pageType() {
      return this.selectedRow?.SELF_PROCESS_GROUP === 'SO' ? '최적화' : '회복' || ''
    },
    processType() {
      return this.selectedRow.self_process_type
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 800
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.top = this.isMobile ? '0vh' : '3vh'
      // if (model?.isMail) {

      // } else {
        this.selectedRow = model?.row
      // }
      let widthByPageType
      if (this.processType === 'S') {
        this.onLoadSyslogInfo()
        widthByPageType = 800
      } else {
        this.onLoadTrafficInfo()
        widthByPageType = 900
      }
      if (!this.isMobile) {
        this.domElement.maxWidth = widthByPageType
      }
    },
    // 자가 최적화
    async onLoadTrafficInfo() {
      try {
        const res = await apiSelfProcessTrafficInfo({ TICKET_ID: this.selectedRow.ticket_id })
        this.trafficInfo = res?.result[0] ?? {}
      } catch (error) {
        this.error(error)
      } finally {
        this.onLoadTrafficChart()
      }
    },
    async onLoadTrafficChart() {
      const { fault_time: FAULT_TIME, ticket_id: TICKET_ID, ticket_type } = this.selectedRow
      const { root_cause_sysnamea: START_NODE, root_cause_sysnamez: END_NODE, root_cause_porta: START_PORT, root_cause_portz: END_PORT } = this.trafficInfo

      const param = { TICKET_ID, START_NODE, START_PORT, FAULT_TIME }
      try {
        this.chartLoading = true
        let chartRes
        if (ticket_type === 'ATT2') {
          chartRes = await apiATTTrafficChart(param)
        } else if (ticket_type === 'NTT') {
          this._merge(param, { END_NODE, END_PORT })
          chartRes = await apiNTTTrafficChart(param)
        }
        const aiRes = await apiSelectAiDetectionInfo(param)
        this.trafficChartList = chartRes?.result
        this.aiDetection = aiRes?.result[0] ?? null
      } catch (error) {
        this.error(error)
      } finally {
        this.chartLoading = false
      }
    },
    // 자가 회복
    async onLoadSyslogInfo() {
      try {
        const res = await apiSelfProcessSyslogInfo({ ALARMNO: this.selectedRow.ticket_id })
        this.syslogInfo = res?.result[0] ?? {}
      } catch (error) {
        this.error(error)
      } finally {
        this.onLoadSopHistList()
      }
    },
    async onLoadSopHistList() {
      const target = ({ vue: this.$refs.sopList })
      try {
        this.openLoading(target)
        let res
        if (this.selectedRow.ticket_type === 'SYSLOG') {
          const { node_nm: NODE_NM, alarmmsg: ALARMMSG } = this.syslogInfo
          res = await apiSopSyslogHistList({ NODE_NM, ALARMMSG })
        } else {
          const { ticket_type: TICKET_TYPE, root_cause_sysnamea: ROOT_CAUSE_SYSNAMEA } = this.selectedRow
          res = await apiSelectSopHistList({ TICKET_TYPE, ROOT_CAUSE_SYSNAMEA })
        }
        this.sopHistList = res?.result
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async onOpenAiResponse() {
      const row = this.selectedRow

      if (row.ticket_type === 'SYSLOG') {
        this._merge(row, this.syslogInfo)
        this.fn_openWindow('requestForAction', row)
      } else {
        row.paramTrafficInfo = this.trafficInfo
        this.fn_openWindow('aiResponse', row)
      }
      this.close()
    },
    onClose() {
      this.sopHistList = []
    },
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-dialog__header {
  height: 65px;
}
::v-deep .el-dialog__body {
  height: calc(100% - 120px);
  padding: 0 15px 0 15px;
  overflow: auto;
}

::v-deep .el-dialog__footer {
  height: 55px !important;
}
.ticket-info {
  border-radius: 5px;
  box-shadow: 0px 5px 8px -4px rgba(0, 0, 0, 0.2);
  th {
    text-align: center;
    font-weight: 700;
    border-radius: 5px;
  }
  td {
    border-radius: 5px;
  }
}
::v-deep .el-form-item__label {
  width: 90px;
  margin-left: 5px;
}
::v-deep .el-form-item__content {
  width: calc(100% - 90px);
}
</style>

