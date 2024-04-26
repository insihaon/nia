<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
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
        <div class="d-flex flex-column h-100 rounded justify-between p-3" style="border: solid 1px #1e293b">
          <div class="shadow-sm p-1 mt-2">
            <span class="title"><i class="el-icon-tickets" />{{ pageType === '최적화' ? '티켓' : '알람' }}정보</span>
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
          </div>

          <div v-if="pageType === '최적화'" class="shadow-sm p-1 mt-2">
            <span class="title"><i class="el-icon-data-line" />트래픽 그래프</span>
            <CompChart :options="trafficChart" :chart-loading="chartLoading" class="h-64" />
          </div>
          <div v-else class="shadow-sm p-1 mt-2">
            <span class="title">연관 SOP 리스트</span>
            <div v-if="sopHistList.length > 0" class="overflow-auto" style="max-height: 250px">
              <CompAgGrid ref="sopAgGrid" v-model="sopAgGrid" class="w-100" style="height: 200px" />
            </div>
            <div v-else class="d-flex items-center justify-center text-lg font-semibold" style="height: 100px">연관 SOP 데이터가 존재하지 않습니다.</div>
          </div>

          <div v-if="processType !== 'S'" class="shadow-sm p-1 mt-2">
            <span class="title"><i class="el-icon-document" />AI 탐지 정보</span>
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
          </div>
          <div v-else class="shadow-sm p-1 mt-2">
            <span class="title">알람 상세 정보</span>
            <div class="overflow-x-auto">
              <table class="ticket-info basic">
                <thead>
                  <tr>
                    <th v-for="(col, index) in defineSyslogDetailTable.col" :key="index" class="whitespace-nowrap" style="max-width: fit-content">
                      <span>{{ col }}</span>
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr class="repeated-item">
                    <td v-for="(valueKey, index) in defineSyslogDetailTable.valueKey" :key="index" class="whitespace-nowrap" style="min-width: fit-content; max-width: fit-content">
                      <span>{{ syslogInfo[valueKey] || '' }}</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="d-flex flex-column mr-2 shadow-sm p-1 mt-2">
            <span class="title">설명</span>
            <div class="d-flex flex-column mr-2 items-center text-xs">
              <span>&nbsp;{{ pageType === '최적화' ? '티켓' : '알람' }}번호 :{{ selectedRow.ticket_id || '' }}</span>
              <span>&nbsp;티켓의 정보를 참고하여 "수동 마감" 처리를 원하실 경우</span>
              <span>&nbsp;하단의 <button style="color: white; background-color: rgb(31, 51, 92); border: none; font-size: 12px; padding: 3px">AI 장애대응(수동)</button>버튼을 클릭하여
                "수동 마감" 하시기 바랍니다.</span>
              <span>&nbsp;티켓의 정보가 상단의 정보와 일치하여 "자동 마감"을 원하신다면
                <button style="color: white; background-color: rgb(31, 51, 92); border: none; font-size: 12px; padding: 3px">닫기</button> 버튼을 클릭하시기 바랍니다.
              </span>
            </div>
          </div>
        </div>
        ㅜ
        <div slot="footer" class="dialog-footer">
          <hr>
          <el-button size="small" class="mt-2" @click.native="onOpenAiResponse"> AI 장애대응(수동) </el-button>
          <el-button size="small" class="close-btn mt-2" @click.native="close()">
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
      defineSyslogDetailTable: {
        col: ['알람 번호', '알람 시간', '장비 ID', '장비명', '장애내용', '인터페이스', '원본 메시지'],
        valueKey: ['alarmno', 'alarmtime', 'node_num', 'node_nm', 'alarmmsg', 'alarmloc', 'etc'],
      },
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
    trafficChart() {
      const { ticket_type } = this.selectedRow
      const chartData = this.trafficChartList
      const xAxisKey = ticket_type === 'ATT2' ? 'measured_datetime' : 'collect_time'
      const markLine = {
        symbol: ['none', 'none'],
        label: { show: false },
        data: [{ xAxis: this.selectedRow?.fault_time || '' }],
      }
      let seriesArr = []
      if (ticket_type === 'ATT2') {
        const seriesInfo = [
          { name: 'PPS_IN', value: 'fltpps_in' },
          { name: 'PPS_OUT', value: 'fltpps_out' },
          { name: 'BPS_IN', value: 'fltbps_in' },
          { name: 'BPS_OUT', value: 'fltbps_out' },
        ]
        seriesArr = seriesInfo.map((item) => {
          return {
            markLine,
            name: item.name,
            type: 'line',
            data: chartData.map((v) => v[item.value]),
          }
        })
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
      this.domElement.maxWidth = 600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      // if (model?.isMail) {

      // } else {
        this.selectedRow = model?.row
      // }
      let widthByPageType
      if (this.pageType === '최적화') {
        this.onLoadTrafficInfo()
        widthByPageType = 600
      } else {
        this.processType === 'S' && this.onLoadSyslogInfo()
        widthByPageType = 800
      }
      this.domElement.maxWidth = widthByPageType
    },
    // 자가 최적화
    async onLoadTrafficInfo() {
      try {
        const res = await apiSelfProcessTrafficInfo({ TICKET_ID: this.selectedRow.ticket_id })
        this.trafficInfo = res?.result ?? {}
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
        this.syslogInfo = res?.result ?? {}
      } catch (error) {
        this.error(error)
      } finally {
        this.onLoadSopHistList()
      }
    },
    async onLoadSopHistList() {
      try {
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
      }
    },
    onOpenAiResponse() {
      this.close()
      this.fn_openWindow('aiResponse', { row: this.selectedRow })
    },
    onClose() {
      this.sopHistList = []
    },
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-dialog {
  margin-top: 3vh !important;
}
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
  margin-top: 5px;
  border-radius: 5px;
  box-shadow: 0px 5px 8px -4px rgba(0, 0, 0, 0.2);
  th {
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
