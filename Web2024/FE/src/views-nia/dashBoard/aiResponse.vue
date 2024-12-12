<template>
  <div :class="{ [name]: true}" style="height : 100%">
    <div class="d-flex flex-column h-full">
      <el-card shadow="never" style="height : 40%" :body-style="{ padding: '10px' , height: 'calc(100% - 55px)' }">
        <div slot="header">
          <span><i class="el-icon-document" /> 작업 요청 구간</span>
        </div>
        <el-row class="d-flex flex-column justify-center h-100">
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
        </el-row>
      </el-card>

      <el-row :gutter="10" class="mt-2">
        <el-col :span="12">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '5px', height: 'calc(100% - 30px)' }">
            <div slot="header">
              <span><i class="el-icon-document" /> TRAFFIC 그래프(MBPS)</span>
            </div>
            <el-row v-if="!isRT">
              <CompChart :options="trafficChartMbps" class="w-100" :chart-loading="chartLoading" style="height: 300px;" />
            </el-row>
            <el-row v-else style="height: 300px" class="d-flex items-center justify-center"> 정보가 없습니다. </el-row>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '5px', height: 'calc(100% - 30px)' }">
            <div slot="header">
              <span><i class="el-icon-document" /> TRAFFIC 그래프(PPS)</span>
            </div>
            <el-row v-if="!isRT">
              <CompChart :options="trafficChartPps" class="w-100" :chart-loading="chartLoading" style="height: 300px" />
            </el-row>
            <el-row v-else style="height: 300px" class="d-flex items-center justify-center"> 정보가 없습니다. </el-row>
          </el-card>
        </el-col>
      </el-row>

      <el-row>
        <el-col align="right" class="mt-2">

          <el-button size="mini" type="primary" icon="el-icon-camera" @click.native="fn_openWindow('snapShot', _merge(selectedRow, trafficInfo))"> 데이터 스냅샷 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('requestForAction', _merge(selectedRow, trafficInfo))"> 상황전파 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('configTest', _merge(selectedRow, trafficInfo))"> 시험 </el-button>
          <el-button v-if="!disabledFin" size="mini" type="primary" @click.native="fn_openWindow('processFin', _merge(selectedRow, trafficInfo))"> 마감 </el-button>
          <el-button v-if="disabledFin" disabled size="mini" type="danger" > 마감된 티켓입니다 </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
            {{ $t('exit') }}
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiSelfProcessTrafficInfo, apiATTTrafficChart, apiNTTTrafficChart } from '@/api/nia'
import { getAlarmType, formatterTime } from '@/views-nia/js/commonFormat'
import CompChart from '@/components/chart/CompChart.vue'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'

const routeName = 'aiResponse'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CompChart, CompInquiryPannel },
  extends: Base,
  mixins: [dialogOpenMixin],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      }
    },
  },
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
      // relatedSopList: [],
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
    disabledFin() {
      return this.selectedRow?.status === 'FIN' || this.selectedRow?.status === 'AUTO_FIN'
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

      let seriesArr = []
      if (['ATT2', 'FTT'].includes(ticket_type)) {
        seriesArr = [
          {
            markLine,
            name: 'MBPS_IN',
            type: 'line',
            data: chartData.map((v) => v.fltbps_in),
            itemStyle: {
              color: '#ffcc00',
            },
          },
          {
            markLine,
            name: 'MBPS_OUT',
            type: 'line',
            data: chartData.map((v) => v.fltbps_out),
            itemStyle: {
              color: '#ff7043',
            },
          },
          {
            name: 'THRESHOLD_UPPER',
            type: 'line',
            data: chartData.map((v) => v.in_threshold_upper),
            smooth: true,
            stack: 'total', // Area Chart
            itemStyle: {
              color: 'rgba(200, 200, 200, 0.5)',
            },
            areaStyle: {
              color: 'rgba(200, 200, 200, 0.5)',
            },
            lineStyle: {
              width: 0, // 라인 제거
            },
            symbol: 'none', // 점 제거
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

  },
  created() {
    this.selectedRow = this.wdata?.params?.row
    this.trafficInfo = this.wdata?.params?.trafficInfo ?? {
      root_cause_sysnamea: '',
      root_cause_sysnamez: '',
      root_cause_porta: '',
      root_cause_portz: ''
    }
  },
  mounted() {
    if (!this.wdata?.params['trafficInfo'] !== undefined) {
      this.onLoadTrafficInfo()
    } else {
      this.onLoadTrafficChart()
    }
  },
  methods: {
    // 자가 구성 조치 구간정보 조회
    async onLoadTrafficInfo() {
      if (!this.selectedRow?.ticket_id) {
        this.error('aiResponse view : ticket_id not found')
        return
      }
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
        if (['ATT2', 'FTT'].includes(ticket_type)) {
          chartRes = await apiATTTrafficChart(param)
        } else if (ticket_type === 'NTT') {
          this._merge(param, { END_NODE, END_PORT })
          chartRes = await apiNTTTrafficChart(param)
        }
        this.trafficChartList = chartRes?.result

        console.log('MBPS Data:', this.trafficChartList.map((v) => v.measured_datetime))
      } catch (error) {
        this.error(error)
      } finally {
        this.chartLoading = false
      }
    },
    onClose() {
      /* for Override */
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/animation.scss';
.mobile .el-button--mini {
  padding: 7px 10px;
}
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
    animation: blink 0.7s ease-in-out infinite alternate;
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
