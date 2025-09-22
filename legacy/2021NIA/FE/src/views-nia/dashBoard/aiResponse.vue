<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-full">
      <!-- Layer 1: 요청구간, 데이터 -->
      <el-row style="overflow: hidden" :style="{ flex: isShowChartTicketType ? '0 1 50%' : '1' }" :gutter="10">
        <el-col :span="12" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%' }">
            <div slot="header">
              <span><i class="el-icon-document" /> 작업 요청 구간</span>
            </div>
            <el-row class="d-flex flex-column justify-center h-100">
              <div class="node-section d-flex justify-center">
                <img src="@/assets/images/nia/node/switch.png" />
                <div class="blinking mt-8" />
                <img src="@/assets/images/nia/node/switch.png" />
              </div>
              <div class="node-info d-flex justify-evenly">
                <div style="width: 390px">
                  <div style="float: left">
                    <div>{{ trafficInfo.root_cause_sysnamea }}</div>
                    <div>
                      <span v-if="isShowChartTicketType">({{ trafficInfo.root_cause_porta }})</span>
                    </div>
                  </div>
                  <div style="float: right">
                    <div>{{ !isShowChartTicketType ? trafficInfo.root_cause_sysnamea : trafficInfo.root_cause_sysnamez }}</div>
                    <div>
                      <span v-if="isShowChartTicketType">({{ trafficInfo.root_cause_portz }})</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="12" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%' }">
            <div slot="header">
              <span><i class="el-icon-document" />티켓 정보</span>
            </div>
            <ul style="text-align: left; overflow: auto; height: calc(100% - 40px)">
              <li>알람번호 : {{ selectedRow.alarmno }}</li>
              <li>장애 발생시간 : {{ selectedRow.alarmtime }}</li>
              <li>티켓ID : {{ selectedRow.ticket_id }}</li>
              <li>상태 : {{ selectedRow.status }}</li>
              <li>티켓타입 : {{ selectedRow.ticket_type }}</li>
              <li>장애유형 : {{ selectedRow.fault_type }}</li>
              <li>장애정보 : {{ selectedRow.alarmmsg }}</li>
              <li>알람 원본메시지 : {{ selectedRow.alarmmsg_original }}</li>
              <li>장애내용 : {{ selectedRow.ticket_rca_result_code }}</li>
              <li>장애원인 : {{ selectedRow.ticket_rca_result_dtl_code }}</li>
              <li>장비ID : {{ selectedRow.node_num }}</li>
              <li>장비명 : {{ selectedRow.node_nm }}</li>
              <li>인터페이스명 : {{ selectedRow.alarmloc }}</li>
              <li>근원알람개수 : {{ selectedRow.total_related_alarm_cnt }}</li>
              <li>IP주소 : {{ selectedRow.ip_addr }}</li>
              <li>AI결과피드백 : {{ selectedRow.ai_accuracy }}</li>
            </ul>
          </el-card>
        </el-col>
      </el-row>

      <!-- Layer 2: chart 영역  -->
      <!-- class="mt-2" -->
      <el-row v-if="isShowChartTicketType" :style="{ flex: isShowChartTicketType ? '1 1 calc(50% - 35px)' : '0 0 0%' }" :gutter="10" class="mt-2">
        <el-col :span="12" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '5px', height: 'calc(100% - 30px)' }">
            <div slot="header">
              <span><i class="el-icon-document" /> TRAFFIC 그래프(MBPS)</span>
            </div>
            <el-row>
              <CompChart ref="trafficChartMbps" :options="trafficChartMbps" class="w-100" :chart-loading="chartLoading" style="height: 300px" />
            </el-row>
            <!-- <el-row style="height: 300px" class="d-flex items-center justify-center"> 정보가 없습니다. </el-row> -->
          </el-card>
        </el-col>

        <el-col :span="12" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '5px', height: 'calc(100% - 30px)' }">
            <div slot="header">
              <span><i class="el-icon-document" /> TRAFFIC 그래프(PPS)</span>
            </div>
            <el-row>
              <CompChart ref="trafficChartPps" :options="trafficChartPps" class="w-100" :chart-loading="chartLoading" style="height: 300px" />
            </el-row>
            <!-- <el-row style="height: 300px" class="d-flex items-center justify-center"> 정보가 없습니다. </el-row> -->
          </el-card>
        </el-col>
      </el-row>

      <!-- Layer 3: 버튼 영역 -->
      <el-row style="flex: 0 0 35px">
        <el-col align="right" class="mt-2">
          <el-button size="mini" type="primary" icon="el-icon-camera" @click.native="fn_openWindow('snapShot', _merge(selectedRow, trafficInfo))"> 데이터 스냅샷 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('requestForAction', _merge(selectedRow, trafficInfo))"> 상황전파 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('configTest', _merge(selectedRow, trafficInfo))"> 조치 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('processFin', _merge(selectedRow, trafficInfo))"> 마감 </el-button>
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
import { formatterTime } from '@/views-nia/js/commonFormat'
import CompChart from '@/components/chart/CompChart.vue'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { mapState } from 'vuex'
import constants from '@/min/constants'
import { getAlarmFocusTicketData, getWindowActionList } from '@/views-nia/js/commonNiaFunction'

import niaObserverMixin from '@/mixin/niaObserverMixin'

const routeName = constants.nia.chatbotKeyMap.aiResponse.parameterKey

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CompChart, CompInquiryPannel },
  extends: Base,
  mixins: [dialogOpenMixin, niaObserverMixin],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      },
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
    isShowChartTicketType() {
      if (this.selectedRow?.ticket_type) {
        return ['ATT2', 'FTT', 'NTT'].includes(this.selectedRow.ticket_type)
      } else {
        return false
      }
    },
    trafficChartPps() {
      if (!this.isShowChartTicketType) return {}

      const { ticket_type } = this.selectedRow
      const chartData = this.trafficChartList
      const xAxisKey = this.isAttFtt(ticket_type) ? 'measured_datetime' : 'collect_time'
      const markLine = {
        symbol: ['none', 'none'],
        label: { show: false },
        data: [{ xAxis: this.selectedRow?.fault_time || '' }],
      }

      let seriesArr = []
      if (this.isAttFtt(ticket_type)) {
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
      if (!this.isShowChartTicketType) return {}

      const { ticket_type } = this.selectedRow
      const chartData = this.trafficChartList
      const xAxisKey = this.isAttFtt(ticket_type) ? 'measured_datetime' : 'collect_time'
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

      if (this.isAttFtt(ticket_type)) {
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
            lineStyle: { width: 0 },
            symbol: 'none',
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

      const trafficChartMbpsOptions = {
        tooltip: { trigger: 'axis' },
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
        yAxis: { type: 'value' },
        series: seriesArr,
      }

      if (this.isAttFtt(ticket_type)) {
        const targetData = chartData.find((d) => {
          return formatterTime(d[xAxisKey]) === this.selectedRow?.fault_time
        })

        const overIn = targetData?.fltbps_in >= targetData?.in_threshold_upper
        const overOut = targetData?.fltbps_out >= targetData?.out_threshold_upper

        trafficChartMbpsOptions.legend[0]['selected'] = { MBPS_IN: overIn, MBPS_OUT: overOut }
        trafficChartMbpsOptions.legend[1]['selected'] = { IN_THRESHOLD_UPPER: overIn, OUT_THRESHOLD_UPPER: overOut }
      }

      return trafficChartMbpsOptions
    },
    ...mapState({
      aiResponseEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.aiResponse.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },
  },
  watch: {
    aiResponseEventText(nVal, oVal) {
      if (this.isModal) {
        switch (nVal) {
          case constants.nia.chatbotCommand.dataSnapshot.action:
            this.fn_openWindow('snapShot', this._merge(this.selectedRow, this.trafficInfo))
            break
          case constants.nia.chatbotCommand.requestForAction.action:
            this.fn_openWindow('requestForAction', this._merge(this.selectedRow, this.trafficInfo))
            break
          case constants.nia.chatbotCommand.configTest.action:
            this.fn_openWindow('configTest', this._merge(this.selectedRow, this.trafficInfo))
            break
          case constants.nia.chatbotCommand.fin.action:
            this.fn_openWindow('processFin', this._merge(this.selectedRow, this.trafficInfo))
            break
        }

        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.aiResponse.parameterKey })
      }
    },
  },

  created() {
    this.selectedRow = this.wdata?.params?.row
    this.trafficInfo = this.wdata?.params?.trafficInfo ?? {
      root_cause_sysnamea: '',
      root_cause_sysnamez: '',
      root_cause_porta: '',
      root_cause_portz: '',
    }
  },
  async mounted() {
    const ticketData = await getAlarmFocusTicketData(this.wdata)
    if (ticketData) {
      this.selectedRow = ticketData
      this.$emit('update:wdataParams', ticketData)
    }

    if (this.isShowChartTicketType) {
      if (!this.wdata?.params['trafficInfo']) {
        this.onLoadTrafficInfo()
      } else {
        this.onLoadTrafficChart()
      }
    } else {
      if (!this.wdata?.params['trafficInfo']) {
        this.onLoadTrafficInfo()
      }

      this.$store.dispatch('mdi/setWindowOptions', {
        id: this.wdata.id,
        options: { height: '400', width: '600' },
      })
    }

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    async popupShowCommand() {
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: await getWindowActionList(constants.nia.chatbotKeyMap.aiResponse.dialogNm, constants.nia.chatbotKeyMap.aiResponse.popupName),
        })
      }
    },
    isAttFtt(ticket_type) {
      return ['ATT2', 'FTT'].includes(ticket_type)
    },

    async onLoadTrafficInfo() {
      // 자가 구성 조치 구간정보 조회
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
      if (this.isShowChartTicketType) {
        const { fault_time: FAULT_TIME, ticket_id: TICKET_ID, ticket_type } = this.selectedRow
        const { root_cause_sysnamea: START_NODE, root_cause_sysnamez: END_NODE, root_cause_porta: START_PORT, root_cause_portz: END_PORT } = this.trafficInfo

        const param = { TICKET_ID, START_NODE, START_PORT, FAULT_TIME }
        try {
          this.chartLoading = true
          let chartRes
          if (this.isAttFtt(ticket_type)) {
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

.aiResponse {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */
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
