<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-full">
      <el-row style="overflow: hidden; flex: 0 0 140px" :gutter="10">
        <el-col :span="24" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%', overflow: 'auto' }">
            <div slot="header">
              <span><i class="el-icon-document" />&nbsp;티켓 상세 정보</span>
            </div>
            <CompAgGrid ref="ticketGrid" v-model="ticketGrid" class="w-100 flex-fill" style="height: 80px" />
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="10" class="mt-2" style="flex: 1">
        <el-col style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%' }">
            <div slot="header">
              <span> <i class="el-icon-document" />&nbsp;사용량 예측 분석 [ 분석기간 : {{ analyDate }} / 장비명 : {{ selectedRow.node_nm }} / 인터페이스명 : {{ selectedRow.alarmloc }} ] </span>
            </div>
            <div class="usagePredAnaly">
              <LineChart ref="LineChart" class="LineChart" :c-data="chartData" :plugins="[verticalLinePlugin]" :c-ver-line="chartVLData" :remove-duplicate-dates="true" />
              <div class="usageContainer">
                <div class="usagePredAnalyResultTitle">
                  <span>분석결과</span>
                </div>

                <div class="usagePredAnalyResultContainerOuter">
                  <div class="usagePredAnalyResult">
                    <div class="usagePredAnalyResultContainerInner" style="display: flex">
                      <div v-if="isTcaAlarm" class="usagePredAnalyResultBody">
                        <el-tooltip class="item" effect="dark" content="TCA 경보의 경우 장애 발생 시점이 언제인지 표시합니다" placement="top">
                          <span class="usagePredAnalyResultItem" style="text-decoration: underline"> 장애 발생 시점 </span>
                        </el-tooltip>
                        <span class="usagePredAnalyResultValue">{{ tcaModel.tca_alert_time ? tcaModel.tca_alert_time.slice(0, 10) + ' ' + tcaModel.tca_alert_time.slice(11, 16) : '' }}</span>
                      </div>
                      <div v-else class="usagePredAnalyResultBody">
                        <el-tooltip class="item" effect="dark" content="예측 경보의 경우 장애가 언제 발생될 것으로 예측되는지 표시합니다" placement="top">
                          <span class="usagePredAnalyResultItem" style="text-decoration: underline"> 임계치 도달 예측 시점 </span>
                        </el-tooltip>
                        <span class="usagePredAnalyResultValue">{{ thresholdInfo.thresholdDate }}</span>
                      </div>
                      <div class="usagePredAnalyResultBody">
                        <el-tooltip class="item" effect="dark" content="앞으로 증감이 어떻게 변동될 것으로 예측되는지 표시합니다" placement="top">
                          <span class="usagePredAnalyResultItem" style="text-decoration: underline"> 증감 예측 결과 </span>
                        </el-tooltip>
                        <span class="usagePredAnalyResultValue">{{ trendValue }}</span>
                      </div>
                      <div class="usagePredAnalyResultBody">
                        <el-tooltip class="item" effect="dark" content="예측값과 실제값 사이의 정확도(하루 이후 계산)" placement="top">
                          <span class="usagePredAnalyResultItem" style="text-decoration: underline"> 정확도 </span>
                        </el-tooltip>
                        <span v-if="accuracyValue" class="usagePredAnalyResultValue">{{ accuracyValue }}</span>
                        <span v-else class="usagePredAnalyResultValue">정확도값 없음</span>
                      </div>
                    </div>
                  </div>
                  <div class="usagePredAnalyResult">
                    <div class="usagePredAnalyResultContainerInner" style="display: flex">
                      <div class="usagePredAnalyResultBody">
                        <el-tooltip class="item" effect="dark" content="장애정보" placement="top">
                          <span class="usagePredAnalyResultItem" style="text-decoration: underline">장애정보</span>
                        </el-tooltip>
                        <span class="usagePredAnalyResultValue">
                          <span>{{ selectedRow.alarmmsg }}</span>
                        </span>
                      </div>
                      <div class="usagePredAnalyResultBody">
                        <el-tooltip class="item" effect="dark" content="장애시점의 값 차이" placement="top">
                          <span class="usagePredAnalyResultItem" style="text-decoration: underline">{{ (isTcaAlarm ? '실제값' : '예측값') + '   >   ' + (isTcaAlarm ? '임계상한값' :'임계값') }}</span>
                        </el-tooltip>
                        <span class="usagePredAnalyResultValue">
                          <span>{{ thresholdInfo.targetValue }} > {{ thresholdInfo.upperBound }} ({{ currentErrorDirection }})</span>
                        </span>
                      </div>
                      <div class="usagePredAnalyResultBody">
                        <el-tooltip class="item" effect="dark" content="TCA: 실시간으로 티켓발행, 예측: 사전예측하여 티켓발행" placement="top">
                          <span class="usagePredAnalyResultItem" style="text-decoration: underline">경보유형</span>
                        </el-tooltip>
                        <span class="usagePredAnalyResultValue">
                          <span>{{ selectedRow.ticket_rca_result_code }}</span>
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="legend">(단위 : Mbps)</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="flex: 0 0 35px">
        <el-col align="right" class="mt-2">
          <el-button size="mini" type="primary" @click.native="openTrendAnalysisPopup">트렌드분석</el-button>
          <el-button size="mini" type="primary" icon="el-icon-camera" @click.native="fn_openWindow('snapShot', _merge(selectedRow, trafficInfo))"> 데이터 스냅샷 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('requestForAction', _merge(selectedRow, trafficInfo))"> 상황전파 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('configTest', _merge(selectedRow, trafficInfo))"> 시험 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('processFin', _merge(selectedRow, trafficInfo))"> 마감 </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
            {{ $t('exit') }}
          </el-button>
        </el-col>
      </el-row>
    </div>
    <popupTrendAnalysis ref="popupTrendAnalysis"></popupTrendAnalysis>
    <errorRangeWidget ref="errorRangeWidget"></errorRangeWidget>
  </div>
</template>

<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { apiSelectAiChartData, apiSelectAttAiTcaModel, apiSelfProcessTrafficInfo, apiSelectRealTrafficData } from '@/api/nia'
import { mapState } from 'vuex'
import { getChatbotTicketData, getWindowActionList } from '@/views-nia/js/commonNiaFunction'
import niaObserverMixin from '@/mixin/niaObserverMixin'
import constants from '@/min/constants'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import moment from 'moment'
import CellRenderAibuttons from '@/views-nia/components/cellRenderer/CellRenderAibuttons'

const verticalLinePlugin = {
  id: 'verticalLine',
  afterDraw(chart) {
    const cVerLine = chart.options.cVerLine
    const { ctx, chartArea } = chart

    if (!chartArea || !cVerLine) {
      console.log('Reason: chartArea or cVerLine is missing.')
      return
    }

    // ⭐⭐ 핵심 수정: width와 height를 경계 좌표로 계산합니다. ⭐⭐
    const width = chartArea.right - chartArea.left
    const height = chartArea.bottom - chartArea.top

    if (width > 0 && height > 0) {
      // x 좌표 계산: (왼쪽 경계) + (계산된 너비 * 비율)
      const x = chartArea.left + width * cVerLine.currentPositionX

      ctx.save()

      // --- 선 그리기 ---
      ctx.strokeStyle = cVerLine.color || 'red'
      ctx.lineWidth = 3
      ctx.setLineDash([5, 5])
      ctx.beginPath()
      ctx.moveTo(x, chartArea.top)
      ctx.lineTo(x, chartArea.bottom) // bottom을 사용
      ctx.stroke()

      // --- 라벨 그리기 ---
      if (cVerLine.nowDate) {
        ctx.fillStyle = cVerLine.color || 'red'
        ctx.font = 'bold 12px Arial'
        ctx.textAlign = 'center'
        ctx.fillText(cVerLine.nowDate, x, chartArea.top - 15)
      }

      ctx.restore()
    } else {
      console.log('Reason: Chart area size is zero. Check parent container size.')
    }
  },
}

const routeName = 'aiResponse_ATT_AI'
export default {
  name: routeName,
  components: {
    // eslint-disable-next-line vue/no-unused-components
    CellRenderAibuttons,
    CompAgGrid,
    LineChart: () => import('@/views-nia/dashBoard/library/LineChart'),
    popupTrendAnalysis: () => import('@/views-nia/dashBoard/popupTrendAnalysis'),
    errorRangeWidget: () => import('@/views-nia/dashBoard/widget/errorRangeWidget'),
  },
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
      trafficInfo: {
        root_cause_sysnamea: '',
        root_cause_sysnamez: '',
        root_cause_porta: '',
        root_cause_portz: '',
      },
      verticalLinePlugin: verticalLinePlugin,
      currentErrorDirection: 'in',
      noDataText: '-',
      thresholdInfo: {
        thresholdDate: '-',
        thresholdDateTime: '', // 임계치 도달 예측 시점의 원본 날짜/시간 값
        targetValue: '',
        upperBound: '',
      },
      trendValue: '-',
      recustSetValue: { llalertpredictstandvalue: 0, /* predictalarmtime: 0, */ inmbpsrate: 0, outmbpsrate: 0 },
      analyDate: '',
      currentValue: '',
      predictValue: '',
      accuracyValue: '',
      chartVLData: { currentPositionX: 0, color: 'red', nowDate: '' },
      chartDataOriginList: [],
      chartData: {
        labels: [],
        datasets: [
          {
            label: '실제값',
            borderColor: '#3b82f6', // 파란색 (실제값)
            borderWidth: 3,
            backgroundColor: 'rgba(59, 130, 246, 0.1)',
            data: [],
            radius: 0,
            spanGaps: true,
            tension: 0.1,
          },
          {
            label: '-',
            borderColor: '#10b981', // 초록색 (예측값)
            borderWidth: 3,
            backgroundColor: 'rgba(16, 185, 129, 0.1)',
            data: [],
            radius: 0,
            tension: 0.1,
          },
          {
            label: '-',
            borderColor: '#f59e0b', // 주황색 (임계값)
            borderWidth: 2,
            backgroundColor: 'rgba(245, 158, 11, 0.2)',
            data: [],
            radius: 0,
            fill: false,
            tension: 0.1,
          },
        ],
      },
      selectedRow: {},
      weeklyData: [
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
      ],
      trendData: {
        trendParseDate: [],
        trendData: [],
      },
      tcaModel: {},
    }
  },
  computed: {
    ticketGrid() {
      // prettier-ignore
      const columns = [
        { type: '', prop: 'ticket_id', name: 'TICKET_ID', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmtime', name: '티켓 발생시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return this.formatterTimeStamp(row.alarmtime, 'YYYY/MM/DD-HH:mm:ss') }, },
        { type: '', prop: 'errorRange', name: '장애구간', width: 150, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '장애구간', icon: 'circle-check', type: '', action: this.openErrorRangeWidget.bind(this) } },
        { type: '', prop: 'fault_type', name: '장애유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 원인', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'node_num', name: '장비ID', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ip_addr', name: 'IP주소', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
      ]

      const options = { name: this.name, checkable: false, rowGroupPanel: false }

      return {
        options,
        columns,
        data: [this.selectedRow],
      }
    },
    errorDirectionisIn() {
      return this.currentErrorDirection === 'in'
    },

    chatbotIcon() {
      return constants.nia.chatbotIcon
    },

    CDS() {
      return this.chartData.datasets
    },

    isTcaAlarm() {
      return ['TCA Alarm', '이상트래픽 TCA 경보'].includes(this.selectedRow.ticket_rca_result_code)
    },

    currentDate() {
      var today = moment().format('YYYY-MM-DD HH:mm:ss')
      var currentMinute = parseInt(today.substring(14, 16) / 15) * 15
      return today.substring(0, 10) + 'T' + today.substring(11, 13) + ':' + String(currentMinute).padStart(2, '0') + ':00'
    },
    ...mapState({
      aiResponseEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.aiResponse_ATT_AI.parameterKey],
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
          case constants.nia.chatbotCommand.trendAnalysis.action:
            this.openTrendAnalysisPopup()
            break
        }

        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.aiResponse_ATT_AI.parameterKey })
      }
    },
  },

  created() {
    this.selectedRow = this.wdata?.params
  },
  async mounted() {
    await this.setTicketDataForChatbotTicketData()

    this.onLoadTrafficInfo()
    this.drawingChart()

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    async popupShowCommand() {
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content:
            `<div class="chatbot-command-header">이상트래픽 AI 장애대응화면</div>
          AI를 통해 해당장비의 14일간의 트래픽을 학습하고 이를 바탕으로 예측값과 예측임계값을 추론하며, 예측값이 임계값을 넘거나(예측 경보) 실제 트래픽이 임계값을 넘는경우(TCA 경보) 경보를 발행되며 해당 화면에서는 관련된 정보를 확인할 수 있습니다.
          <br>${constants.nia.chatbotIcon.Information} 분석결과 : TCA 경보의 경우 장애발생시점이 언제인지, 예측경보의 경우 장애가 언제 발생될 것으로 예측되는지 표시
          ${constants.nia.chatbotIcon.Information} 증감예측결과 : TCA 경보의 경우 예측상한값의 증감, 예측경보의 경우 예측값의 증감
          ${constants.nia.chatbotIcon.Information} 조회조건 : 트래픽 in, out여부, 장비, 포트, 분석기간 정보
          ${constants.nia.chatbotIcon.Information} 경고 임계치 : 경보 발행 기준이 되는 임계값.(현재는 과거 14일을 기준으로 값을 추정)
          ` + (await getWindowActionList(constants.nia.chatbotKeyMap.aiResponse_ATT_AI.dialogNm, constants.nia.chatbotKeyMap.aiResponse_ATT_AI.popupName)),
        })
      }
    },
    async setTicketDataForChatbotTicketData(isSwitchingTicket) {
      if (isSwitchingTicket) this.wdata.params.isChatbotGenerated = isSwitchingTicket
      const chatbotData = await getChatbotTicketData(this.wdata)
      if (chatbotData) {
        this.selectedRow = chatbotData
        this.$emit('update:wdataParams', chatbotData)

        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: constants.nia.chatbotIcon.success + constants.nia.chatbotComment.parameterChange,
        })
      }
    },

    openTrendAnalysisPopup() {
      this.$refs.popupTrendAnalysis.setVisible({ trend: this.trendData, item: '', weekly: this.weeklyData })
    },

    openErrorRangeWidget() {
      this.$refs.errorRangeWidget.setVisible({ trafficInfo: this.trafficInfo })
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
        // finally
      }
    },

    resetChartData() {
      this.chartData.labels = []
      this.CDS[0].data = [] // 실제값
      this.CDS[1].data = []
      this.CDS[2].data = []

      this.trendData.trendParseDate = []
      this.trendData.trendData = []
      this.weeklyData = [
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
        { data: 0, cnt: 0 },
      ]
    },

    // LineChart 컴포넌트가 준비될 때까지 대기
    async waitForLineChart() {
      let retryCount = 0
      const maxRetries = 50 // 5초 = 50 * 100ms

      while (!this.$refs.LineChart && retryCount < maxRetries) {
        console.log(`Waiting for LineChart... (${retryCount + 1}/${maxRetries})`)
        await new Promise((resolve) => setTimeout(resolve, 100))
        retryCount++
      }

      return !!this.$refs.LineChart
    },

    // AI 차트 데이터 조회
    async loadAiChartData() {
      if (this.isTcaAlarm) {
        const res1 = await apiSelectAttAiTcaModel({
          NODE_NUM: this.selectedRow.node_num,
          IF_NUM: this.selectedRow.if_num,
          ALARM_TIME: this.selectedRow.fault_time,
        })

        this.tcaModel = res1.result[0]
        const res2 = await apiSelectAiChartData({ MODEL_ID: Number(this.tcaModel.model_id) })

        const aiChartData = res2.result
        aiChartData[0].out_alertyn = this.tcaModel.tca_alert_direction === 'out' ? 'Y' : 'N'

        const fitDateMinusOneDay = moment(aiChartData[0].fit_date).subtract(1, 'days').format('YYYY-MM-DD')
        const res3 = await apiSelectRealTrafficData({ ipSdnIfId: aiChartData[0].ipsdn_if_id, fitDate: fitDateMinusOneDay })
        const realTraffic = res3.result

        // realTraffic 데이터를 aiChartData와 매칭하여 실제값 업데이트
        if (realTraffic && realTraffic.length > 0) {
          // realTraffic을 hour_bucket을 키로 하는 Map으로 변환 (빠른 검색을 위해)
          const realTrafficMap = new Map()
          realTraffic.forEach((item) => {
            if (item.hour_bucket) {
              // hour_bucket을 정규화하여 비교 (시간 단위로 표준화)
              const normalizedBucket = new Date(item.hour_bucket)
              normalizedBucket.setMinutes(0, 0, 0)
              realTrafficMap.set(normalizedBucket.getTime(), item)
            }
          })

          // aiChartData의 각 항목과 매칭
          aiChartData.forEach((chartItem) => {
            if (chartItem.ds) {
              const dsDate = new Date(chartItem.ds)
              dsDate.setMinutes(0, 0, 0)
              const matchingRealTraffic = realTrafficMap.get(dsDate.getTime())

              if (matchingRealTraffic) {
                // 실제값 업데이트
                if (matchingRealTraffic.in_y != null) {
                  chartItem.in_y = matchingRealTraffic.in_y
                }
                if (matchingRealTraffic.out_y != null) {
                  chartItem.out_y = matchingRealTraffic.out_y
                }
              }
            }
          })
        }

        return aiChartData
      } else {
        const res = await apiSelectAiChartData({
          NODE_NUM: this.selectedRow.node_num,
          IF_NUM: this.selectedRow.if_num,
          ALARM_TIME: this.selectedRow.alarmtime,
        })

        return res.result
      }
    },

    // 차트 데이터 초기화 및 기본 설정
    initializeChartData(aiData) {
      this.setChartLabel()
      this.resetChartData()

      this.currentErrorDirection = aiData.out_alertyn === 'Y' ? 'out' : 'in'
      this.accuracyValue = aiData.out_alertyn === 'Y' ? aiData.out_accuracy_rate : aiData.in_accuracy_rate
      this.analyDate = aiData.fit_sttime.substring(5, 7) + '.' + aiData.fit_sttime.substring(8, 10) + ' ~ ' + aiData.fit_endtime.substring(5, 7) + '.' + aiData.fit_endtime.substring(8, 10)
    },

    setChartLabel() {
      if (this.isTcaAlarm) {
        this.chartData.datasets[1].label = '임계상한값'
        this.chartData.datasets[2].label = '임계하한값'
        // TCA 경보일 때는 상/하한 밴드만 라인으로 표시 (영역 채우지 않음)
        this.chartData.datasets[1].fill = false
        this.chartData.datasets[2].fill = false
      } else {
        this.chartData.datasets[1].label = '예측값'
        this.chartData.datasets[2].label = '예측 임계값'
        // 예측 경보일 때만 예측값 ~ 예측임계값 사이 영역을 채움
        // Chart.js 기준으로, 상위 라인(임계값) 쪽에서 이전 데이터셋(예측값)까지 영역 채우기
        this.chartData.datasets[1].fill = false
        this.chartData.datasets[2].fill = '-1'
      }
    },

    // 세로선 기준 날짜/시간 결정
    drawVerticalLineLabelAndGetDateTime(thresholdDateTime) {
      let targetDateTime = null
      let targetDateLabel = ''

      targetDateTime = new Date(thresholdDateTime)
      targetDateLabel = thresholdDateTime.substring(0, 10) + ' ' + thresholdDateTime.substring(11, 16)

      // if (this.isTcaAlarm) {
      //   // TCA 에러인 경우: tcaModel.tca_alert_time 사용
      //   targetDateTime = new Date(this.tcaModel.tca_alert_time)
      //   targetDateLabel = this.tcaModel.tca_alert_time.slice(0, 10) + ' ' + this.tcaModel.tca_alert_time.slice(11, 16)
      // } else {
      //   // 일반 에러인 경우: thresholdInfo.thresholdDateTime 사용
      //   if (thresholdDateTime && thresholdDateTime !== '') {
      //     targetDateTime = new Date(thresholdDateTime)
      //     targetDateLabel = thresholdDateTime.substring(0, 10) + ' ' + thresholdDateTime.substring(11, 16)
      //   }
      // }

      // 시간 단위로 표준화
      targetDateTime && targetDateTime.setMinutes(0, 0, 0)
      targetDateLabel += this.isTcaAlarm ? '(장애발생시점)' : '(예측시점)'

      this.setChartVLTimeStamp(targetDateLabel)

      return new Date(targetDateTime)
    },

    // 차트 데이터 포인트 처리
    setCharPoints(aiData, ingredient) {
      const parseDate = aiData.ds.substring(5, 7) + '.' + aiData.ds.substring(8, 10) + ' '
      const actualValue = this.errorDirectionisIn ? aiData.in_y : aiData.out_y
      const yhatValue = this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat

      // 모든 데이터 포인트에 날짜 라벨 추가 (중복 제거는 LineChart.js의 callback에서 처리)
      this.chartData.labels.push(parseDate)

      const dsDate = new Date(aiData.ds)
      dsDate.setMinutes(0, 0, 0)

      // fit_date의 하루 전 날짜 계산
      const fitDateMinusOneDay = moment(aiData.fit_date).subtract(1, 'days').format('YYYY-MM-DD')

      // 실제값 셋팅
      if (new Date() >= dsDate.getTime()) {
        if (this.isTcaAlarm) {
          this.CDS[0].data.push(actualValue.toFixed(2))
        } else {
          if (fitDateMinusOneDay >= aiData.ds) {
            this.CDS[0].data.push(actualValue.toFixed(2))
          } else {
            if (!ingredient.isChangeFirst) {
              ingredient.isChangeFirst = true
              this.CDS[0].data.push(yhatValue)
            } else {
              this.CDS[0].data.push(null)
            }
          }
        }
      } else {
        this.CDS[0].data.push(null)
      }

      // 예측값,예측임계값 vs 임계상한,임계하한
      if (fitDateMinusOneDay >= aiData.ds) {
        this.CDS[1].data.push(null)
        this.CDS[2].data.push(null)
      } else {
        if (this.isTcaAlarm) {
          // 임계상한값, 임계하한값
          this.CDS[1].data.push(this.errorDirectionisIn ? aiData.in_yhat_upper.toFixed(2) : aiData.out_yhat_upper.toFixed(2))
          this.CDS[2].data.push(this.errorDirectionisIn ? aiData.in_yhat_lower.toFixed(2) : aiData.out_yhat_lower.toFixed(2))
        } else {
          // 예측값, 예측임계값
          this.CDS[1].data.push(yhatValue)
          this.CDS[2].data.push(this.errorDirectionisIn ? aiData.in_threshold_value : aiData.out_threshold_value)
        }
      }
    },

    // 트렌드 값 계산
    calculateTrendValue(aiData, i, ingredient) {
      let yhatValue = this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat
      if (this.isTcaAlarm) {
        yhatValue = this.errorDirectionisIn ? aiData.in_yhat_upper : aiData.out_yhat_upper
      }

      if (aiData.fit_date === aiData.ds.substring(0, 10)) {
        if (ingredient.firstIndex === 0) {
          ingredient.firstIndex = i
          ingredient.firstMax = yhatValue
        } else {
          if (ingredient.firstMax < yhatValue) ingredient.firstMax = yhatValue
        }
      }

      const predictLastDayDiffer = 6 // 15일 수집해서 7일 예측하므로, 7일 - 1
      if (this.diffDate(aiData.fit_date, aiData.ds.substring(0, 10)) === predictLastDayDiffer) {
        if (ingredient.lastIndex === 0) {
          ingredient.lastIndex = i
          ingredient.lastMax = yhatValue
        } else {
          if (ingredient.lastMax < yhatValue) ingredient.lastMax = yhatValue
        }
      }

      if (i === this.chartDataOriginList.length - 1) {
        ingredient.firstMax = ingredient.firstMax.toFixed(2) === 0.0 ? 1 : ingredient.firstMax.toFixed(2)
        const tempValue = ingredient.firstMax === 0 ? 0 : parseInt((ingredient.lastMax.toFixed(2) / ingredient.firstMax - 1) * 100)
        if (Math.abs(tempValue) < 10) {
          this.trendValue = '안정적 ( ' + tempValue + ' % )'
        } else {
          this.trendValue = (tempValue > 0 ? '증가 추세 ( ' : '감소 추세 ( ') + tempValue + ' % )'
        }
      }
    },

    // 세로선 위치 설정
    setChartVerticalLinePosition(aiData, i, ingredient) {
      const dsDate = new Date(aiData.ds)
      dsDate.setMinutes(0, 0, 0)
      // 세로선 위치 계산: targetDateTime과 일치하는 데이터 포인트 찾기
      if (dsDate.getTime() === ingredient.verticalLineTargetDate.getTime()) {
        ingredient.vlCurrentIndex = i / this.chartDataOriginList.length
        this.chartVLData.currentPositionX = ingredient.vlCurrentIndex
      }
    },

    // 차트 업데이트
    updateChart() {
      setTimeout(() => {
        if (this.$refs.LineChart) {
          // this.$refs.LineChart.optionUpdate && this.$refs.LineChart.optionUpdate()
          this.$refs.LineChart.chartUpdate && this.$refs.LineChart.chartUpdate()
          this.$refs.LineChart.makeLine && this.$refs.LineChart.makeLine()
        }
      }, 100)
    },

    async drawingChart() {
      const okLoading = await this.waitForLineChart()
      const target = { vue: this.$refs.LineChart }

      try {
        if (okLoading) this.openLoading(target, { text: '차트 불러오는 중..' })

        this.chartDataOriginList = await this.loadAiChartData()
        const verticalingredient = { vlCurrentIndex: 0, verticalLineTargetDate: null }
        const trendIngredient = { firstMax: 0, lastMax: 0, firstIndex: 0, lastIndex: 0 }
        const chartPointIngredient = { isChangeFirst: false }

        this.chartDataOriginList.forEach((aiData, i) => {
          if (i === 0) {
            this.initializeChartData(aiData)
            this.thresholdInfo = this.setThresholdInfo(aiData) // 예측 임계시점과 시간 계산
            verticalingredient.verticalLineTargetDate = this.drawVerticalLineLabelAndGetDateTime(this.thresholdInfo.thresholdDateTime)
          }
          this.setCharPoints(aiData, chartPointIngredient)
          this.setErrorTimingData(aiData, verticalingredient)
          this.setWeeklyAndTrend(aiData)
          this.setChartVerticalLinePosition(aiData, i, verticalingredient)
          this.calculateTrendValue(aiData, i, trendIngredient)
        })

        this.updateChart()
        this.closeLoading(target)
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

    setErrorTimingData(aiData, ingredient) {
      const dsDate = new Date(aiData.ds)
      dsDate.setMinutes(0, 0, 0)
      if (dsDate.getTime() === ingredient.verticalLineTargetDate.getTime()) {
        if (this.isTcaAlarm) {
          this.thresholdInfo.targetValue = this.errorDirectionisIn ? aiData.in_y : aiData.out_y
          this.thresholdInfo.upperBound = this.errorDirectionisIn ? aiData.in_yhat_upper : aiData.out_yhat_upper
        } else {
          this.thresholdInfo.targetValue = this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat
          this.thresholdInfo.upperBound = this.errorDirectionisIn ? aiData.in_threshold_value : aiData.out_threshold_value
        }
        this.thresholdInfo.targetValue = this.thresholdInfo.targetValue.toFixed(2)
        this.thresholdInfo.upperBound = this.thresholdInfo.upperBound.toFixed(2)
      }
    },

    setWeeklyAndTrend(aiData) {
      const parseDate = aiData.ds.substring(5, 7) + '.' + aiData.ds.substring(8, 10) + ' '
      const tempDate = new Date(aiData.ds)
      const weekly = this.errorDirectionisIn ? aiData.in_weekly : aiData.out_weekly
      this.weeklyData[tempDate.getDay()].data = this.weeklyData[tempDate.getDay()].data + (weekly ?? 0)
      this.weeklyData[tempDate.getDay()].cnt += 1
      const trend = this.errorDirectionisIn ? aiData.in_trend : aiData.out_trend
      this.trendData.trendData.push((trend ?? 0).toFixed(2))
      this.trendData.trendParseDate.push(parseDate)
    },

    setChartVLTimeStamp(targetDateLabel) {
      if (targetDateLabel) {
        // targetDateLabel 형식에 맞게 변환 (YYYY-MM-DD HH:mm -> MM.DD HH:mm)
        if (targetDateLabel.includes('-')) {
          const dateParts = targetDateLabel.split(' ')
          if (dateParts.length >= 1) {
            const datePart = dateParts[0] // YYYY-MM-DD
            const timePart = dateParts.length > 1 ? dateParts[1] : ''
            this.chartVLData.nowDate = datePart.substring(5, 7) + '.' + datePart.substring(8, 10) + (timePart ? ' ' + timePart : '')
          } else {
            this.chartVLData.nowDate = targetDateLabel
          }
        } else {
          this.chartVLData.nowDate = targetDateLabel
        }
      } else {
        this.chartVLData.nowDate = this.currentDate.substring(5, 7) + '.' + this.currentDate.substring(8, 10) + ' ' + this.currentDate.substring(11, 16)
      }
    },

    setThresholdInfo(aiData) {
      let predictreachdate
      let thresholdDate = '없음(30일 이내)'
      let thresholdDateTime = ''

      if (this.isTcaAlarm) {
        predictreachdate = this.tcaModel.tca_alert_time
      } else {
        predictreachdate = aiData.out_alertyn === 'Y' ? aiData.out_alert_datetime : aiData.in_alert_datetime
      }

      if (predictreachdate && predictreachdate !== '') {
        thresholdDateTime = predictreachdate // 원본 날짜/시간 값 저장
        thresholdDate = predictreachdate.substring(0, 4) + '.' + predictreachdate.substring(5, 7) + '.' + predictreachdate.substring(8, 10) + '(' + this.diffDate(aiData.fit_date, predictreachdate) + '일후)'
      }

      const newThresholdInfo = { ...this.thresholdInfo, thresholdDate, thresholdDateTime }
      return newThresholdInfo
    },

    diffDate(start, end) {
      var year = start.substring(0, 4)
      var month = start.substring(5, 7)
      var day = start.substring(8, 10)

      var pyear = end.substring(0, 4)
      var pmonth = end.substring(5, 7)
      var pday = end.substring(8, 10)
      var startDate = moment([year, month - 1, day])
      var endDate = moment([pyear, pmonth - 1, pday])
      var diff = endDate.diff(startDate, 'days')
      return diff
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

.aiResponse_ATT_AI {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */

  .LineChart {
    position: relative;
    top: 10px;
    left: 0;
    right: 0;
    bottom: 0;
    height: calc(100% - 130px);
    margin-bottom: 5px;
    width: 100%;
    text-align: center;
    display: inline-block;
  }
}

.usagePredAnaly {
  position: relative;
  width: 100%;
  z-index: 1;
  overflow: hidden;
  height: 100%;

  .usageContainer {
    display: flex;

    div.usagePredAnalyResultTitle {
      width: 80px;

      & > span {
        height: 70px;
        line-height: 70px;
        text-align: center;
        font-size: 12px;
        transform: rotate(-0.03deg);
        font-weight: 600;
        letter-spacing: -0.5px;
        display: inline-block;
        background-color: #07acc5;
        width: 90%;
        color: #ffffff;
        border-radius: 4px;
      }
    }

    .usagePredAnalyResultContainerOuter {
      width: calc(100% - 100px);

      .usagePredAnalyResult {
        position: relative;
        width: 100%;
        display: inline-block;
        margin-bottom: 5px;
        text-align: center;

        > div {
          position: relative;
          float: left;
          line-height: 28px;
        }

        div.usagePredAnalyResultBody {
          flex: 1;
        }

        div.usagePredAnalyResultContainerInner {
          width: 100%;

          div.usagePredAnalyResultBody {
            margin-right: 3%;
          }
          div span {
            display: inline-block;
            position: relative;
            width: 50%;
            text-align: center;
          }
          span.usagePredAnalyResultItem {
            background-color: rgb(217, 217, 217);
            font-size: 12px;
            letter-spacing: -1px;
            transform: rotate(-0.03deg);
            color: rgb(64, 64, 64);
            text-shadow: 0px 0px 2px rgba(0, 0, 0, 0.25);
          }
          span.tipIcon {
            display: inline;
          }
          span.usagePredAnalyResultValue {
            padding-left: 10px;
            background-color: #2c3345;
            text-align: center;
            color: #ffffff;
            font-size: 12px;
            letter-spacing: 0px;
            transform: rotate(-0.03deg);

            & > span {
              z-index: 1000;
              width: 100% !important;
            }

            &::before {
              content: '';
              position: absolute;
              background-color: rgb(217, 217, 217);
              left: -10px;
              top: 4px;
              width: 20px;
              height: 20px;
              transform: rotate(45deg);
            }
            &::after {
              content: '';
              position: absolute;
              background-color: #2c3345;
              right: -11px;
              top: 4px;
              width: 20px;
              height: 20px;
              transform: rotate(45deg);
            }
          }
        }
      }
    }
  }

  /* 챠트 */
  .usagePredAnalyChart {
    position: relative;
    top: 10px;
    left: 0;
    right: 0;
    bottom: 0;
    height: 260px;
    margin-bottom: 5px;
    width: 100%;
    text-align: center;
    display: inline-block;
  }
}

.legend {
  position: absolute;
  left: 30px;
  top: 30px;
  font-weight: 600;
  transform: rotate(-0.03deg);
  font-size: 10px;
  color: #003f33;
}
</style>
