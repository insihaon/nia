<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-full">
      <el-row style="overflow: hidden" :style="{ flex: '0 1 50%' }" :gutter="10">
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
                      <span v-if="trafficInfo.root_cause_porta">({{ trafficInfo.root_cause_porta }})</span>
                    </div>
                  </div>
                  <div style="float: right">
                    <div>{{ trafficInfo.root_cause_sysnamea }}</div>
                    <div>
                      <span v-if="trafficInfo.root_cause_portz">({{ trafficInfo.root_cause_portz }})</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-row>
          </el-card>
        </el-col>
        <el-col :span="12" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%', overflow: 'auto' }">
            <div slot="header">
              <span><i class="el-icon-document" />티켓 정보</span>
            </div>
            <table class="ticket-info-table">
              <tbody>
                <tr>
                  <td>알람번호</td>
                  <td>{{ selectedRow.alarmno || noDataText }}</td>
                </tr>
                <tr>
                  <td>장애 발생시간</td>
                  <td>{{ selectedRow.alarmtime || noDataText }}</td>
                </tr>
                <tr>
                  <td>티켓ID</td>
                  <td>{{ selectedRow.ticket_id || noDataText }}</td>
                </tr>
                <tr>
                  <td>상태</td>
                  <td>{{ selectedRow.status || noDataText }}</td>
                </tr>
                <tr>
                  <td>티켓타입</td>
                  <td>{{ selectedRow.ticket_type || noDataText }}</td>
                </tr>
                <tr>
                  <td>장애유형</td>
                  <td>{{ selectedRow.fault_type || noDataText }}</td>
                </tr>
                <tr>
                  <td>장애정보</td>
                  <td>{{ selectedRow.alarmmsg || noDataText }}</td>
                </tr>
                <tr>
                  <td>알람 원본메시지</td>
                  <td>{{ selectedRow.alarmmsg_original || noDataText }}</td>
                </tr>
                <tr>
                  <td>장애내용</td>
                  <td>{{ selectedRow.ticket_rca_result_code || noDataText }}</td>
                </tr>
                <tr>
                  <td>장애원인</td>
                  <td>{{ selectedRow.ticket_rca_result_dtl_code || noDataText }}</td>
                </tr>
                <tr>
                  <td>장비ID</td>
                  <td>{{ selectedRow.node_num || noDataText }}</td>
                </tr>
                <tr>
                  <td>장비명</td>
                  <td>{{ selectedRow.node_nm || noDataText }}</td>
                </tr>
                <tr>
                  <td>인터페이스명</td>
                  <td>{{ selectedRow.alarmloc || noDataText }}</td>
                </tr>
                <tr>
                  <td>근원알람개수</td>
                  <td>{{ selectedRow.total_related_alarm_cnt || noDataText }}</td>
                </tr>
                <tr>
                  <td>IP주소</td>
                  <td>{{ selectedRow.ip_addr || noDataText }}</td>
                </tr>
                <tr>
                  <td>AI결과피드백</td>
                  <td>{{ selectedRow.ai_accuracy || noDataText }}</td>
                </tr>
              </tbody>
            </table>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="10" class="mt-2">
        <el-col>
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%' }">
            <div slot="header">
              <span><i class="el-icon-document" />사용량 예측 분석</span>
            </div>
            <div class="usagePredAnaly">
              <LineChart ref="LineChart" class="LineChart" :c-data="chartData" :plugins="[verticalLinePlugin]" :c-ver-line="chartVLData" :remove-duplicate-dates="true" />
              <div class="usagePredAnalyResult">
                <div class="usagePredAnalyResultTitle" style="width: 80px">
                  <span>분석결과</span>
                </div>
                <div v-if="isTcaError">
                  <span class="usagePredAnalyResultItem">장애 발생 시점</span>
                  <span class="usagePredAnalyResultValue">{{ tcaModel.tca_alert_time.slice(0, 10) + ' ' + tcaModel.tca_alert_time.slice(11, 16) }}</span>
                </div>
                <div v-else>
                  <span class="usagePredAnalyResultItem">임계치 도달 예측 시점</span>
                  <span class="usagePredAnalyResultValue">{{ thresholdDate }}</span>
                </div>
                <div>
                  <span class="usagePredAnalyResultItem">증감 예측 결과</span>
                  <span class="usagePredAnalyResultValue">{{ trendValue }}</span>
                </div>
              </div>
              <div class="usagePredAnalyBottom">
                <div class="usagePredAnalyBottomTitle" style="width: 80px; line-height: 58px">
                  <span style="width: 72px">조회조건</span>
                </div>
                <div class="usagePredAnalyBottomItem" style="width: 50%">
                  <ul>
                    <li>
                      <span :title="analyDate">분석기간 : {{ analyDate }}</span>
                    </li>
                    <li>
                      <span :title="selectedRow.node_nm">대상 장비 : {{ selectedRow.node_nm }}</span>
                    </li>
                  </ul>
                  <ul>
                    <li>
                      <span :title="currentErrorDirection + ' 트래픽'">분석항목 : {{ currentErrorDirection + ' 트래픽' }}</span>
                    </li>
                    <li>
                      <span :title="selectedRow.alarmloc">대상 IF : {{ selectedRow.alarmloc }}</span>
                    </li>
                  </ul>
                </div>

                <div style="width: 80px">
                  <div class="usagePredAnalyBottomTitle" style="width: 80px; margin-bottom: 2px">
                    <span style="width: 72px">경고임계치</span>
                  </div>
                  <div class="usagePredAnalyBottomTitle" style="width: 80px">
                    <span style="width: 72px">정확도</span>
                  </div>
                </div>

                <div style="width: 50%">
                  <div class="usagePredAnalyBottomItem">
                    <ul>
                      <li style="line-height: 29px">
                        <span v-if="currentErrorDirection == 'in'">{{ recustSetValue.inmbpsrate }}Mbps(in)</span>
                        <span v-if="currentErrorDirection == 'out'">{{ recustSetValue.outmbpsrate }}Mbps(out)</span>
                      </li>
                    </ul>
                  </div>
                  <div class="usagePredAnalyBottomItem">
                    <ul>
                      <li v-if="accuracyValue" style="line-height: 25px">
                        <span>{{ accuracyValue }}%</span>
                      </li>
                      <li v-else style="line-height: 25px">
                        <span>정확도값 없음</span>
                      </li>
                    </ul>
                  </div>
                </div>
                <div class="usagePredAnalyBottomTrendTitle" style="width: 120px" @click="openTrendAnalysisPopup()">
                  <span style="line-height: 58px"><i class="xi-search" /> 트렌드분석</span>
                </div>
              </div>
              <div class="legend">(단위 : Mbps)</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="flex: 0 0 35px">
        <el-col align="right" class="mt-2">
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
  </div>
</template>

<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { apiSelectAiChartData, apiSelectAttAiTcaModel, apiSelectCurrentChartData, apiSelfProcessTrafficInfo, apiSelectRealTrafficData } from '@/api/nia'
import { mapState } from 'vuex'
import { getChatbotTicketData, getWindowActionList } from '@/views-nia/js/commonNiaFunction'
import niaObserverMixin from '@/mixin/niaObserverMixin'
import constants from '@/min/constants'

import moment from 'moment'

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
  // eslint-disable-next-line vue/no-unused-components
  components: {
    LineChart: () => import('@/views-nia/dashBoard/library/LineChart'),
    popupTrendAnalysis: () => import('@/views-nia/dashBoard/popupTrendAnalysis'),
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
      thresholdDate: '-',
      thresholdDateTime: '', // 임계치 도달 예측 시점의 원본 날짜/시간 값
      trendValue: '-',
      isTcaAlarm: false,
      recustSetValue: { llalertpredictstandvalue: 0, /* predictalarmtime: 0, */ inmbpsrate: 0, outmbpsrate: 0 },
      analyDate: '',
      currentValue: '',
      predictValue: '',
      accuracyValue: '',
      chartVLData: { currentPositionX: 0, color: 'red', nowDate: '' },
      chartData: {
        labels: [],
        datasets: [
          {
            label: '실제값',
            borderColor: '#3b82f6', // 파란색 - 신뢰할 수 있는 실제 데이터
            borderWidth: 3,
            backgroundColor: 'rgba(59, 130, 246, 0.1)',
            data: [],
            radius: 0,
            spanGaps: true,
            tension: 0.1,
          },
          {
            label: '예측값',
            borderColor: '#10b981', // 초록색 - 긍정적인 예측
            borderWidth: 3,
            backgroundColor: 'rgba(16, 185, 129, 0.1)',
            data: [],
            radius: 0,
            tension: 0.1,
          },
          {
            label: '예측 상한값',
            borderColor: '#f59e0b', // 주황색 - 주의가 필요한 상한선
            borderWidth: 2,
            backgroundColor: 'rgba(245, 158, 11, 0.2)',
            data: [],
            radius: 0,
            fill: '+1',
            tension: 0.1,
          },
          {
            label: '예측 하한값',
            borderColor: '#8b5cf6', // 보라색 - 안전한 하한선
            borderWidth: 2,
            backgroundColor: 'rgba(139, 92, 246, 0.2)',
            data: [],
            radius: 0,
            fill: '-1',
            tension: 0.1,
          },
        ],
      },
      selectedRow: [],
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
    errorDirectionisIn() {
      return this.currentErrorDirection === 'in'
    },

    CDS() {
      return this.chartData.datasets
    },

    isTcaError() {
      return Object.keys(this.tcaModel).length > 0
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
    this.loadChartData()

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
          ${constants.nia.chatbotIcon.Information} 증감예측결과 : 앞으로 증감이 어떻게 변동될 것으로 예측되는지
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
      this.CDS[1].data = [] // 예측값
      this.CDS[2].data = [] // 예측 상한값
      this.CDS[3].data = [] // 예측 하한값

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
    async fetchAiChartData() {
      if (this.selectedRow.ticket_rca_result_code === 'TCA Alarm') {
        this.isTcaAlarm = true
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
    initializeChartData(aiChartData) {
      this.resetChartData()

      this.currentErrorDirection = aiChartData[0].out_alertyn === 'Y' ? 'out' : 'in'
      this.accuracyValue = aiChartData[0].out_alertyn === 'Y' ? aiChartData[0].out_accuracy_rate : aiChartData[0].in_accuracy_rate
      this.analyDate = aiChartData[0].fit_sttime.substring(5, 7) + '.' + aiChartData[0].fit_sttime.substring(8, 10) + '~' + aiChartData[0].fit_endtime.substring(5, 7) + '.' + aiChartData[0].fit_endtime.substring(8, 10)
      this.setThresholdDate(aiChartData)
      this.setRecustSetValue(aiChartData)
    },

    // 세로선 기준 날짜/시간 결정
    determineTargetDateTime() {
      let targetDateTime = null
      let targetDateLabel = ''

      if (this.isTcaError) {
        // TCA 에러인 경우: tcaModel.tca_alert_time 사용
        targetDateTime = new Date(this.tcaModel.tca_alert_time)
        targetDateLabel = this.tcaModel.tca_alert_time.slice(0, 10) + ' ' + this.tcaModel.tca_alert_time.slice(11, 16)
      } else {
        // 일반 에러인 경우: thresholdDateTime 사용
        if (this.thresholdDateTime && this.thresholdDateTime !== '') {
          targetDateTime = new Date(this.thresholdDateTime)
          // thresholdDateTime 형식에 따라 라벨 생성 (YYYY-MM-DD HH:mm 형식 가정)
          if (this.thresholdDateTime.length >= 16) {
            targetDateLabel = this.thresholdDateTime.substring(0, 10) + ' ' + this.thresholdDateTime.substring(11, 16)
          } else {
            targetDateLabel = this.thresholdDateTime.substring(0, 10)
          }
        }
      }

      // targetDateTime이 없으면 현재 시간 사용 (기존 로직 유지)
      if (!targetDateTime) {
        targetDateTime = new Date(this.currentDate)
        targetDateLabel = this.currentDate.substring(5, 7) + '.' + this.currentDate.substring(8, 10) + ' ' + this.currentDate.substring(11, 16)
      }

      // 시간 단위로 표준화
      targetDateTime.setMinutes(0, 0, 0)

      return {
        targetDateTime: new Date(targetDateTime),
        targetDateLabel,
      }
    },

    // 차트 데이터 포인트 처리
    processChartDataPoints(aiChartData, targetDateTime) {
      let firstMax = 0
      let lastMax = 0
      let firstIndex = 0
      let lastIndex = 0
      let vlCurrentIndex = 0

      const dateFormatTargetDate = new Date(targetDateTime)
      dateFormatTargetDate.setMinutes(0, 0, 0)

      for (let i = 0; i < aiChartData.length; i++) {
        const aiData = aiChartData[i]
        const parseDate = aiData.ds.substring(5, 7) + '.' + aiData.ds.substring(8, 10) + ' '
        const tempDate = new Date(aiData.ds)
        const weekly = this.errorDirectionisIn ? aiData.in_weekly : aiData.out_weekly
        this.weeklyData[tempDate.getDay()].data = this.weeklyData[tempDate.getDay()].data + (weekly ?? 0)
        this.weeklyData[tempDate.getDay()].cnt += 1
        this.trendData.trendParseDate.push(parseDate)
        const trend = this.errorDirectionisIn ? aiData.in_trend : aiData.out_trend
        this.trendData.trendData.push((trend ?? 0).toFixed(2))

        // 모든 데이터 포인트에 날짜 라벨 추가 (중복 제거는 LineChart.js의 callback에서 처리)
        this.chartData.labels.push(parseDate)

        const dsDate = new Date(aiData.ds)
        dsDate.setMinutes(0, 0, 0)

        // 세로선 위치 계산: targetDateTime과 일치하는 데이터 포인트 찾기
        if (dsDate.getTime() === dateFormatTargetDate.getTime()) {
          vlCurrentIndex = i / aiChartData.length
        }

        // in_y, out_y 값을 기준으로 실제값/예측값 구분
        const actualValue = this.errorDirectionisIn ? aiData.in_y : aiData.out_y
        // const tcaTime = this.isTcaAlarm ? '' : ''

        // fit_date의 하루 전 날짜 계산
        const fitDateMinusOneDay = moment(aiData.fit_date).subtract(1, 'days').format('YYYY-MM-DD')

        if (this.isTcaAlarm ? new Date() >= dsDate.getTime() : fitDateMinusOneDay >= aiData.ds) {
          // 실제값이 존재하는 경우: 실제값 표시
          this.CDS[0].data.push(actualValue.toFixed(2))
          this.CDS[1].data.push(null)
          this.CDS[2].data.push(null)
          this.CDS[3].data.push(null)
        } else {
          // 실제값이 없는 경우: 예측값 표시
          // fit_date와의 관계는 유지 (firstIndex, lastIndex 계산용)
          if (aiData.fit_date === aiData.ds.substring(0, 10)) {
            if (firstIndex === 0) {
              firstIndex = i
              firstMax = this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat
            } else {
              if (firstMax < this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat) {
                firstMax = this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat
              }
            }
          }

          const predictLastDayDiffer = 6 // 15일 수집해서 7일 예측하므로, 7일 - 1
          if (this.diffDate(aiData.fit_date, aiData.ds.substring(0, 10)) === predictLastDayDiffer) {
            if (lastIndex === 0) {
              lastIndex = i
              lastMax = this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat
            } else {
              if (lastMax < this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat) {
                lastMax = this.errorDirectionisIn ? aiData.in_yhat : aiData.out_yhat
              }
            }
          }

          this.CDS[0].data.push(null)
          this.errorDirectionisIn ? this.CDS[1].data.push(aiData.in_yhat.toFixed(2)) : this.CDS[1].data.push(aiData.out_yhat.toFixed(2))
          this.errorDirectionisIn ? this.CDS[2].data.push(aiData.in_yhat_upper.toFixed(2)) : this.CDS[2].data.push(aiData.out_yhat_upper.toFixed(2))
          this.errorDirectionisIn ? this.CDS[3].data.push(aiData.in_yhat_lower.toFixed(2)) : this.CDS[3].data.push(aiData.out_yhat_lower.toFixed(2))
        }
      }

      return { firstMax, lastMax, vlCurrentIndex }
    },

    // 트렌드 값 계산
    calculateTrendValue(firstMax, lastMax) {
      firstMax = firstMax.toFixed(2) === 0.0 ? 1 : firstMax.toFixed(2)
      const tempValue = parseInt((lastMax.toFixed(2) / firstMax - 1) * 100)
      if (Math.abs(tempValue) < 10) {
        this.trendValue = '안정적 ( ' + tempValue + ' % )'
      } else {
        this.trendValue = (tempValue > 0 ? '증가 추세 ( ' : '감소 추세 ( ') + tempValue + ' % )'
      }
    },

    // 세로선 라벨 및 위치 설정
    updateChartVerticalLine(targetDateLabel, vlCurrentIndex) {
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
      this.chartVLData.currentPositionX = vlCurrentIndex
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

    async loadChartData() {
      const okLoading = await this.waitForLineChart()
      const target = { vue: this.$refs.LineChart }

      try {
        if (okLoading) {
          this.openLoading(target, { text: '차트 불러오는 중..' })
        }

        const aiChartData = await this.fetchAiChartData()
        this.initializeChartData(aiChartData)

        const { targetDateTime, targetDateLabel } = this.determineTargetDateTime()
        const { firstMax, lastMax, vlCurrentIndex } = this.processChartDataPoints(aiChartData, targetDateTime)

        this.calculateTrendValue(firstMax, lastMax)
        this.updateChartVerticalLine(targetDateLabel, vlCurrentIndex)

        console.log('Chart VL Data:', this.chartVLData)
        console.log('Chart Data Labels Length:', this.chartData.labels.length)

        this.updateChart()
        this.closeLoading(target)
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

    setThresholdDate(aiChartData) {
      if (aiChartData != null && aiChartData.length > 0) {
        const predictreachdate = aiChartData[0].out_alertyn === 'Y' ? aiChartData[0].out_alert_datetime : aiChartData[0].in_alert_datetime

        if (!predictreachdate || predictreachdate === '') {
          this.thresholdDate = '없음(30일 이내)'
          this.thresholdDateTime = '' // 원본 날짜/시간 값도 초기화
        } else {
          this.thresholdDateTime = predictreachdate // 원본 날짜/시간 값 저장
          this.thresholdDate = predictreachdate.substring(0, 4) + '.' + predictreachdate.substring(5, 7) + '.' + predictreachdate.substring(8, 10) + '(' + this.diffDate(aiChartData[0].fit_date, predictreachdate) + '일후)'
        }
      } else {
        this.thresholdDate = '없음(30일 이내)'
        this.thresholdDateTime = '' // 원본 날짜/시간 값도 초기화
      }
    },

    setRecustSetValue(aiChartData) {
      if (aiChartData != null && aiChartData.length > 0) {
        this.recustSetValue.inmbpsrate = aiChartData[0].in_threshold_value
        this.recustSetValue.outmbpsrate = aiChartData[0].out_threshold_value
      }
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
    height: 260px;
    margin-bottom: 5px;
    width: 100%;
    text-align: center;
    display: inline-block;
  }
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

.usagePredAnaly {
  position: relative;
  width: 100%;
  z-index: 1;
  overflow: hidden;
}

/* 챠트 */
div.usagePredAnaly div.usagePredAnalyChart {
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

div.usagePredAnaly div.usagePredAnalyResult {
  position: relative;
  width: 100%;
  display: inline-block;
  margin-bottom: 5px;
  text-align: center;
}
div.usagePredAnaly div.usagePredAnalyResult > div {
  position: relative;
  float: left;
  line-height: 28px;
}
div.usagePredAnaly div.usagePredAnalyResult div:first-child {
  width: 10%;
}
div.usagePredAnaly div.usagePredAnalyResult div:first-child > span {
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
div.usagePredAnaly div.usagePredAnalyResult div:not(:first-child) {
  width: 43.5%;
}
div.usagePredAnaly div.usagePredAnalyResult div:nth-child(2) {
  margin-right: 3%;
}
div.usagePredAnaly div.usagePredAnalyResult div span {
  display: inline-block;
  // float: left;
  position: relative;
  width: 50%;
  text-align: center;
}
div.usagePredAnaly div.usagePredAnalyResult span.usagePredAnalyResultItem {
  background-color: rgb(217, 217, 217);
  font-size: 12px;
  letter-spacing: -1px;
  transform: rotate(-0.03deg);
  color: rgb(64, 64, 64);
  text-shadow: 0px 0px 2px rgba(0, 0, 0, 0.25);
}
div.usagePredAnaly div.usagePredAnalyResult span.usagePredAnalyResultValue {
  background-color: #2c3345;
  text-align: center;
  color: #ffffff;
  font-size: 12px;
  letter-spacing: 0px;
  transform: rotate(-0.03deg);
}
div.usagePredAnaly div.usagePredAnalyResult span.usagePredAnalyResultValue::before {
  content: '';
  position: absolute;
  background-color: rgb(217, 217, 217);
  left: -10px;
  top: 4px;
  width: 20px;
  height: 20px;
  transform: rotate(45deg);
}
div.usagePredAnaly div.usagePredAnalyResult span.usagePredAnalyResultValue::after {
  content: '';
  position: absolute;
  background-color: #2c3345;
  right: -11px;
  top: 4px;
  width: 20px;
  height: 20px;
  transform: rotate(45deg);
}

/* 하단 영역 */
div.usagePredAnaly div.usagePredAnalyBottom {
  position: relative;
  width: 100%;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  padding-top: 6px;
  display: flex;
  align-items: top;
  justify-content: top;
}

div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomTitle {
  position: relative;
  width: 94px;
  line-height: 28px;
  margin-right: 10px;
}
div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomTitle > span {
  position: relative;
  text-align: center;
  font-size: 12px;
  transform: rotate(-0.03deg);
  font-weight: 600;
  letter-spacing: -0.25px;
  display: inline-block;
  background-color: #07acc5;
  width: 100%;
  color: #ffffff;
  border-radius: 3px;
  height: 97%;
}
div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomItem {
  position: relative;
  line-height: 28px;
  /* width:50%; */
}

div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomItem > ul {
  position: relative;
  width: 100%;
  display: inline-block;
  padding: 0;
  margin: 0;
  float: left;
  height: 50%;
}

div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomItem > ul > li {
  float: left;
  display: inline;
  line-height: 18px;
  margin-right: 10px;
}

div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomItem > ul > li::before {
  position: relative;
  float: left;
  display: inline-block;
  content: '●';
  font-size: 6px;
  margin-right: 5px;
  top: 2px;
  transform: scale(0.75);
  color: rgb(64, 64, 64);
}

div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomItem > ul > li > span {
  position: relative;
  display: inline;
  text-align: left;
  color: #818286;
  font-size: 12px;
  letter-spacing: -0.5px;
  transform: rotate(-0.03deg);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomItem > ul:nth-child(2) > li:nth-child(2) > span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 트랜드분석 버튼 */
div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomTrendTitle {
  position: relative;
  width: 110px;
  text-align: center;
  cursor: pointer;
  transition: all 0.25s;
}
div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomTrendTitle:hover {
  opacity: 0.7;
  transition: all 0.25s;
}

div.usagePredAnaly div.usagePredAnalyBottom div.usagePredAnalyBottomTrendTitle > span {
  position: relative;
  text-align: center;
  font-size: 12px;
  transform: rotate(-0.03deg);
  font-weight: 600;
  letter-spacing: -1px;
  display: inline-block;
  background-color: #409eff;
  width: 100%;
  line-height: 28px;
  color: #ffffff;
  border-radius: 3px;
  height: 97%;
}
.legend {
  position: absolute;
  right: 25px;
  top: 30px;
  font-weight: 600;
  transform: rotate(-0.03deg);
  font-size: 10px;
  color: #003f33;
}

// 티켓 정보 테이블
.ticket-info-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 6px;
  overflow: auto;
  height: calc(100% - 40px);

  td {
    padding: 4px 6px;
    border-bottom: 1px solid #e2e8f0;
    font-size: 12px;
    color: #334155;

    &:first-child {
      width: 40%;
      font-weight: 600;
    }

    &:last-child {
      color: #0f172a;
    }
  }
}
.chartjs-render-monitor {
  touch-action: none;
}
</style>
