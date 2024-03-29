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
          AI 장애대응
          <hr>
        </span>
        <div class="d-flex flex-column h-full">
          <el-tabs v-model="tabActiveName">
            <!-- @tab-click="handleClick" -->
            <el-tab-pane :label="isRT ? '자가 회복':'자가 구성'" name="first">
              <div class="d-flex flex-column h-100 rounded justify-between p-3">
                <div class="shadow-sm p-1 mt-2">
                  <span class="title"><i class="el-icon-tickets" />작업 요청 구간</span>
                  <div>
                    <div class="node-section d-flex justify-center">
                      <img src="@/assets/images/nia/node/switch.png">
                      <div class="blinking mt-8" />
                      <img src="@/assets/images/nia/node/switch.png">
                    </div>
                    <div class="node-info d-flex justify-evenly">
                      <div>
                        <div>{{ trafficInfo.root_cause_sysnamea }}</div>
                        <div v-if="!isRT">({{ trafficInfo.root_cause_porta }})</div>
                      </div>
                      <div>
                        <div>{{ isRT ? trafficInfo.root_cause_sysnamea : trafficInfo.root_cause_sysnamez }}</div>
                        <div v-if="!isRT">({{ trafficInfo.root_cause_portz }})</div>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-if="!isRT" class="shadow-sm p-1 mt-2">
                  <span class="title"><i class="el-icon-tickets" />TRAFFIC 그래프</span>
                  <div>
                    <CompChart :options="trafficChart" :chart-loading="chartLoading" class="h-64" />
                  </div>
                </div>
                <!-- <div class="shadow-sm p-1 mt-2">
                  <span class="title">연관 SOP리스트</span>
                  <div v-if="relatedSopList.length > 0" class="shadow-sm">
                    <CompAgGrid
                      ref="sopAgGrid"
                      v-model="sopAgGrid"
                      class="w-100"
                      style="height: 200px"
                    />
                  </div>
                  <div v-else class="d-flex items-center justify-center text-lg font-semibold" style="height: 100px">연관 SOP 데이터가 존재하지 않습니다.</div>
                </div>
              </div> -->
              </div></el-tab-pane>
            <el-tab-pane label="처리이력" name="second">
              <CompInquiryPannel
                ref="selectApi"
                :ag-grid="sopAgGrid"
                class="w-100 h-100 flex-fill"
              />
              <!-- @handleClickSearch="" -->
            </el-tab-pane>
          </el-tabs>

        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" @click.native="$refs.ModalNTF.open({row: selectedRow})">
            상황전파
          </el-button>
          <el-button size="small" plain class="close-btn" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
    <ModalNTF ref="ModalNTF" />
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiSelfProcessTrafficInfo, apiATTTrafficChart, apiNTTTrafficChart, apiSopHistList } from '@/api/nia'
import { getAlarmType, formatterTime } from '@/views-nia/js/commonFormat'
import ModalNTF from '@/views-nia/modal/ModalNTF.vue'
import CompChart from '@/components/chart/CompChart.vue'

const routeName = 'ModalAiResponse'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CompChart, CompInquiryPannel, ModalNTF },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tabActiveName: 'first',
      type: true,
      visible: false,
      selectedRow: null,
      chartLoading: false,
      trafficChartList: [],
      relatedSopList: [],
      trafficInfo: {
        root_cause_sysnamea: '',
        root_cause_sysnamez: '',
        root_cause_porta: '',
        root_cause_portz: '',
      },
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
    }
  },
  computed: {
    isRT() {
      return this.selectedRow?.ticket_type === 'RT'
    },
    sopAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
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
        return { options, columns, data: this.relatedSopList }
    },
    trafficChart() {
      const { ticket_type } = this.selectedRow
      const chartData = this.trafficChartList
      const xAxisKey = ticket_type === 'ATT2' ? 'measured_datetime' : 'collect_time'
      const markLine = {
          symbol: ['none', 'none'],
          label: { show: false },
          data: [{ xAxis: this.selectedRow?.fault_time || '' }]
        }
      let seriesArr = []
      if (ticket_type === 'ATT2') {
        const seriesInfo = [
          { name: 'PPS_IN', value: 'fltpps_in' },
          { name: 'PPS_OUT', value: 'fltpps_out' },
          { name: 'BPS_IN', value: 'fltbps_in' },
          { name: 'BPS_OUT', value: 'fltbps_out' },
        ]
        seriesArr = seriesInfo.map(item => {
          return {
            markLine,
            name: item.name,
            type: 'line',
            data: chartData.map(v => v[item.value])
          }
        })
      } else {
        seriesArr = [
          {
            markLine,
            name: 'STRCOUNTS',
            type: 'line',
            data: chartData.map(v => v.strcounts)
          },
          {
            name: 'STRBYTES_COL',
            type: 'line',
            data: chartData.map(v => v.strbytes_col)
          }
        ]
      }
      return {
        tooltip: {
          trigger: 'axis'
        },
        dataZoom: [
          { type: 'inside', }
        ],
        xAxis: {
          type: 'category',
          data: chartData.map(v => formatterTime(v[xAxisKey]))
        },
        yAxis: {
          type: 'value'
        },
        series: seriesArr
      }
    },
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxHeight = 600
      this.domElement.maxWidth = 800
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
      this.onLoadTrafficInfo()
      this.onLoadSopHistList()
    },
    // 자가 구성 조치 구간정보 조회
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
        this.trafficChartList = chartRes?.result
      } catch (error) {
        this.error(error)
      } finally {
        this.chartLoading = false
      }
    },
    async onLoadSopHistList() {
      const { pageSize: limit, currentPage: page } = this.paginationInfo
      const { ticket_type: TICKET_TYPE, root_cause_sysnamea: ROOT_CAUSE_SYSNAMEA } = this.selectedRow
      const param = { limit, page, TICKET_TYPE, ROOT_CAUSE_SYSNAMEA }
      try {
        const res = await apiSopHistList(param)
        this.relatedSopList = res?.result ?? []
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      }
    },
    onClose() { /* for Override */ },
    }

  }
</script>

<style lang="scss" scoped>
@import "~@/styles/animation.scss";
.node-section {
  img {
    width: 85px;
    height: 80px;
    border-radius: 50%;
    padding: 5px 10px;
    border: solid 7px #c7bdbd;
  }
  div {
    height: 5px;
    width: 195px;
    border-bottom: 5px solid #e41f1f;
    animation: blink .7s ease-in-out infinite alternate;
  }
}
.node-info {
  div {
    font-size: 12px;
    width: 110px;
    color: #cb5252;
    text-align: center;
    font-weight: bolder;
  }
}

</style>

