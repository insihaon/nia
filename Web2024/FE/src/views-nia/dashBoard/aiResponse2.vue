<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-full">
      <el-row style="overflow: hidden" :style="{ flex: isAtt ? '0 1 50%' : '1' }" :gutter="10">
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
                      <span v-if="isAtt">({{ trafficInfo.root_cause_porta }})</span>
                    </div>
                  </div>
                  <div style="float: right">
                    <div>{{ isAtt ? trafficInfo.root_cause_sysnamea : trafficInfo.root_cause_sysnamez }}</div>
                    <div>
                      <span v-if="isAtt">({{ trafficInfo.root_cause_portz }})</span>
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

      <el-row v-if="isAtt" :gutter="10" class="mt-2">
        <LineChart ref="LineChart" class="LineChart" :c-data="chartData" :c-options="chartOptions" />
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
  </div>
</template>

<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { apiSelectAiChartData, apiSelfProcessTrafficInfo } from '@/api/nia'
import moment from 'moment'

const routeName = 'aiResponse2'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: {
    LineChart: () => import('@/views-nia/dashBoard/library/LineChart'),
  },
  extends: Base,
  mixins: [dialogOpenMixin],
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
      chartData: {
        labels: [],
        datasets: [
          {
            label: '실제값',
            borderColor: 'rgba(10,100,80,0.5)',
            borderWidth: 2,
            backgroundColor: 'rgba(10,100,80,0.5)',
            data: [],
            radius: 0,
            spanGaps: true,
          },
          {
            label: '예측값',
            borderColor: 'rgba(51,102,204,1)',
            borderWidth: 2,
            backgroundColor: 'rgba(51,102,204,1)',
            data: [],
            radius: 0,
          },
          {
            label: '예측 상한값',
            borderColor: 'rgba(0,204,200,0.15)',
            borderWidth: 2,
            backgroundColor: 'rgba(0,204,200,0.15)',
            data: [],
            radius: 0,
            fill: '+1',
          },
          {
            label: '예측 하한값',
            borderColor: 'rgba(0, 204, 200, 0.15)',
            borderWidth: 2,
            backgroundColor: 'rgba(0, 204, 200, 0.15)',
            data: [],
            radius: 0,
            fill: '-1',
          },
        ],
      },

      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          xAxes: [
            {
              ticks: {
                fontSize: 11,
                minRotation: 0,
                maxRotation: 0,
                fontColor: '#000000',
                fontFamily: 'ktb',
                callback: function (value, index, labels) {
                  return value.substring(0, 6) + ' '
                },
              },
              gridLines: {
                display: true,
              },
            },
          ],
          yAxes: [
            {
              ticks: {
                beginAtZero: false,
                fontSize: 10,
                fontColor: '#000000',
                fontFamily: 'ktb',
                callback: function (value, index, labels) {
                  return value
                },
                gridLines: {
                  display: true,
                },
              },
            },
          ],
          elements: { line: { tension: 0, fill: false }, point: { radius: 0 } },
          animation: { duration: 0 },
          legend: { display: false },
          layout: {
            padding: {
              left: 148,
              right: 148,
              top: 20,
              bottom: 10,
            },
          },
          tooltips: {
            callbacks: {
              label: (tooltipItems, data) => {
                var toolTipArray = []
                // if (data.datasets[0].data[tooltipItems.index] != null) {
                toolTipArray.push(data.datasets[0].label /* + ' : ' + data.datasets[0].data[tooltipItems.index] */)
                // }
                // if (data.datasets[1].data[tooltipItems.index] != null) {
                toolTipArray.push(data.datasets[1].label /* + ' : ' + data.datasets[1].data[tooltipItems.index] */)
                // }
                // if (data.datasets[2].data[tooltipItems.index] != null) {
                toolTipArray.push(data.datasets[2].label /* + ' : ' + data.datasets[2].data[tooltipItems.index] */)
                // }
                // if (data.datasets[3].data[tooltipItems.index] != null) {
                toolTipArray.push(data.datasets[3].label /* + ' : ' + data.datasets[3].data[tooltipItems.index] */)
                // }
                return toolTipArray
              },
              title: (tootlItems, data) => {
                return data.labels[tootlItems[0].index]
              },
            },
          },
        },
      },
    }
  },
  computed: {
    isAtt() {
      return this.selectedRow.ticket_type === 'ATT2'
    },
  },
  created() {
    this.selectedRow = this.wdata?.params
  },
  mounted() {
    this.onLoadTrafficInfo()

    if (this.isAtt) {
      this.loadChartData()
    } else {
      this.$store.dispatch('mdi/setWindowOptions', {
        id: this.wdata.id,
        options: { height: '400', width: '600' },
      })
    }
  },
  methods: {
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

    async loadChartData() {
      const target = { vue: this.$refs.LineChart }

      try {
        this.openLoading(target, { text: '차트 불러오는 중..' })

        var today = moment().format('YYYY-MM-DD HH:mm:ss')

        const res = await apiSelectAiChartData({
          NODE_NUM: this.selectedRow.node_num,
          IF_NUM: this.selectedRow.if_num,
        })
        const aiChartData = res.result

        // 데이터 배열 초기화
        this.chartData.labels = []
        this.chartData.datasets[0].data = [] // 실제값
        this.chartData.datasets[1].data = [] // 예측값
        this.chartData.datasets[2].data = [] // 예측 상한값
        this.chartData.datasets[3].data = [] // 예측 하한값

        for (const aiData of aiChartData) {
          // X축 라벨 추가 (시간 또는 날짜 등)
          const parseDate = aiData.ds.substring(5, 7) + '.' + aiData.ds.substring(8, 10) + ' ' /* + aiData.ds.substring() */
          this.chartData.labels.push(parseDate)

          if (aiData.fit_date >= aiData.ds) {
            this.chartData.datasets[0].data.push(aiData.in_y.toFixed(2)) // 실제값
            this.chartData.datasets[1].data.push(null) // 예측값
            this.chartData.datasets[2].data.push(null) // 예측 상한값
            this.chartData.datasets[3].data.push(null) // 예측 하한값
          } else {
            this.chartData.datasets[0].data.push(null)
            this.chartData.datasets[1].data.push(aiData.in_yhat.toFixed(2))
            this.chartData.datasets[2].data.push(aiData.in_yhat_upper.toFixed(2))
            this.chartData.datasets[3].data.push(aiData.in_yhat_lower.toFixed(2))
          }
        }

        // 차트 업데이트
        setTimeout(() => {
          if (this.$refs.LineChart) {
            this.closeLoading(target)
            this.$refs.LineChart.optionUpdate && this.$refs.LineChart.optionUpdate()
            this.$refs.LineChart.chartUpdate && this.$refs.LineChart.chartUpdate()
          }
        }, 100)
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
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

.aiResponse2 {
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
</style>
