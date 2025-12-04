<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-full">
      <!-- Layer 1: 요청구간, 데이터 -->
      <el-row style="overflow: hidden; flex: 1" :gutter="10">
        <el-col :span="6" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%' }">
            <div slot="header">
              <span><i class="el-icon-document" />유해트래픽 정확도</span>
            </div>
            <div style="height: 350px">
              <DoughnutChart ref="donutChart" class="chatbot-donut-chart" :chart-data="chartData" :options="chartOptions" />
            </div>
            <div style="height: 200px; margin-top: 10px">
              <div style="background-color: #1e293b; font-weight: 600; text-align: center; color: white">유해트래픽 정확도 통계</div>
              <table class="sop-stats-table">
                <tbody>
                  <tr v-for="(label, index2) in chartData.labels" :key="label">
                    <td :style="{ background: index2 === 0 ? 'red' : 'white', color: index2 === 0 ? 'white' : 'black' }">{{ label }}</td>
                    <td :style="{ background: index2 === 0 ? 'red' : 'white', color: index2 === 0 ? 'white' : 'black' }">{{ chartData.datasets[0].data[index2] }}%</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </el-card>
        </el-col>
        <el-col :span="18" style="height: 100%">
          <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%', overflow: 'auto' }">
            <div slot="header">
              <span><i class="el-icon-document" />토폴로지 Map</span>
            </div>
            <div id="nttMap"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- Layer 3: 버튼 영역 -->
      <el-row style="flex: 0 0 35px">
        <el-col align="right" class="mt-2">
          <!-- <el-button size="mini" type="primary" icon="el-icon-camera" @click.native="fn_openWindow('snapShot', _merge(selectedRow, trafficInfo))"> 데이터 스냅샷 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('requestForAction', _merge(selectedRow, trafficInfo))"> 상황전파 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('configTest', _merge(selectedRow, trafficInfo))"> 조치 </el-button>
          <el-button size="mini" type="primary" @click.native="fn_openWindow('processFin', _merge(selectedRow, trafficInfo))"> 마감 </el-button> -->
          <el-button size="mini" type="info" @click.native="visibleNttTicketDataList"> 전체 패킷 리스트 </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
            {{ $t('exit') }}
          </el-button>
        </el-col>
      </el-row>
    </div>
    <nttTicketDataList ref="nttTicketDataList" :wdata="{ selectedRow: ticketTotalData }"></nttTicketDataList>
  </div>
</template>

<script>
import { Base } from '@/min/Base'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import niaObserverMixin from '@/mixin/niaObserverMixin'
import { Doughnut } from 'vue-chartjs'
import _ from 'lodash'
import { apiSelectNttTicketTotalDataList, apiSelectRcaNttTicketDetailInfo } from '@/api/nia'
import { getChatbotTicketData, getWindowActionList } from '@/views-nia/js/commonNiaFunction'
// import { formatterTime } from '@/views-nia/js/commonFormat'
import { mapState } from 'vuex'
// import {  } from '@/views-nia/js/commonNiaFunction'
import constants from '@/min/constants'
import cytoscape from 'cytoscape'
const routeName = 'aiResponse_NTT'

import TEST_TICKET_SAMPLE from '@/views-nia/js/simulationData/TEST_TICKET_SELECT_NTT_TICKET_TOTAL_DATA_LIST.json'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: {
    nttTicketDataList: () => import('@/views-nia/dashBoard/nttTicketDataList'),
    DoughnutChart: {
      extends: Doughnut,
      props: {
        chartData: {
          type: Object,
          required: true,
        },
        options: {
          type: Object,
          default: () => ({}),
        },
      },
      mounted() {
        // 이곳은 donutChart의 mounted
        const clonedData = this._cloneChartData(this.chartData)
        const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
        this.renderChart(clonedData, clonedOptions)
      },
      watch: {
        chartData: {
          deep: true,
          handler(newVal) {
            const clonedData = this._cloneChartData(newVal)
            const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
            this.renderChart(clonedData, clonedOptions)
          },
        },
        options: {
          deep: true,
          handler(newVal) {
            const clonedData = this._cloneChartData(this.chartData)
            const clonedOptions = this._cloneOptionsPreservingFunctions(newVal)
            this.renderChart(clonedData, clonedOptions)
          },
        },
      },
      methods: {
        _cloneOptionsPreservingFunctions(options) {
          const generateLabelsFn = options && options.legend && options.legend.labels && options.legend.labels.generateLabels
          const clonedOptions = JSON.parse(JSON.stringify(options || {}))
          if (generateLabelsFn) {
            if (!clonedOptions.legend) clonedOptions.legend = {}
            if (!clonedOptions.legend.labels) clonedOptions.legend.labels = {}
            clonedOptions.legend.labels.generateLabels = generateLabelsFn
          }
          return clonedOptions
        },
        _cloneChartData(data) {
          return JSON.parse(JSON.stringify(data))
        },
        optionUpdate() {
          const clonedData = this._cloneChartData(this.chartData)
          const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
          this.renderChart(clonedData, clonedOptions)
        },
        chartUpdate() {
          const clonedData = this._cloneChartData(this.chartData)
          const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
          this.renderChart(clonedData, clonedOptions)
        },
      },
    },
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
      selectedRow: null,
      ticketTotalData: [],
      chartData: {
        labels: [],
        datasets: [
          {
            backgroundColor: ['#1569C7', '#FFAD99', '#FAFAD2', '#FFB6C1', '#BDBADF'],
            data: [0, 0, 0, 0, 0],
          },
        ],
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        title: {
          display: true,
          text: '',
          fontSize: 16,
          fontStyle: 'bold',
          fontColor: '#333',
        },
        legend: {
          position: 'left',
          align: 'center',
          labels: {
            usePointStyle: true,
            padding: 20,
            generateLabels: function (chart) {
              const data = chart.data
              if (data.labels.length && data.datasets.length) {
                const counts = data.datasets[0].data
                return data.labels.map(function (label, i) {
                  return {
                    // text: label + ': ' + counts[i] + '%',
                    text: label,
                    fillStyle: data.datasets[0].backgroundColor[i],
                    hidden: isNaN(counts[i]) || counts[i] === 0,
                    index: i,
                  }
                })
              }
              return []
            },
          },
        },
      },
    }
  },
  computed: {
    ...mapState({
      aiResponseEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.aiResponse.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },
  },
  watch: {
    // aiResponseEventText(nVal, oVal) {
    //   if (this.isModal) {
    //     switch (
    //       nVal
    //       // case constants.nia.chatbotCommand.dataSnapshot.action:
    //       //   this.fn_openWindow('snapShot', this._merge(this.selectedRow, this.trafficInfo))
    //       //   break
    //       // case constants.nia.chatbotCommand.requestForAction.action:
    //       //   this.fn_openWindow('requestForAction', this._merge(this.selectedRow, this.trafficInfo))
    //       //   break
    //       // case constants.nia.chatbotCommand.configTest.action:
    //       //   this.fn_openWindow('configTest', this._merge(this.selectedRow, this.trafficInfo))
    //       //   break
    //       // case constants.nia.chatbotCommand.fin.action:
    //       //   this.fn_openWindow('processFin', this._merge(this.selectedRow, this.trafficInfo))
    //       //   break
    //     ) {
    //     }
    //     // this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.aiResponse.parameterKey })
    //   }
    // },
  },

  created() {
    this.selectedRow = this.wdata?.params
  },
  async mounted() {
    await this.setTicketDataForChatbotTicketData()
    await this.setDonutChartData()

    window.v.nttMap = this.initTopology()
    this.ticketTotalData = await this.loadNttTicketTotalDataList()
    const groupedData = this.processNetworkData(this.ticketTotalData)
    this.makeTopologyEmlements(groupedData)

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    visibleNttTicketDataList() {
      this.$refs.nttTicketDataList.setVisible()
    },
    async setDonutChartData() {
      let data
      if (this.selectedRow.ticket_id === 'NTT_SIMULATION') {
        data = { normal_traffic_ratio: '0', tcp_syn_flooding_ratio: '80', land_attack_ratio: '5', ping_of_death_ratio: '5', udp_flooding_ratio: '10' }
      } else {
        const res = await apiSelectRcaNttTicketDetailInfo({ ticket_id: this.selectedRow.ticket_id })
        data = res.result[0]
        delete data.traffic_type
      }

      // 1. 객체의 항목들을 { key, value } 형태의 배열로 변환하고 값(value)을 숫자로 파싱
      const allRatios = Object.entries(data).map(([key, value]) => ({
        key: key,
        value: Number(value),
      }))

      // 2. value를 기준으로 내림차순(큰 값부터) 정렬
      allRatios.sort((a, b) => b.value - a.value)

      // 3. 정렬된 결과를 labels와 data 배열로 분리하여 구성
      const sortedLabels = allRatios.map((item) => {
        const key = item.key

        if (key === 'normal_traffic_ratio') {
          return '정상트래픽'
        } else {
          // 2. 나머지 항목: _ratio 제거 후 언더바를 공백으로 변환
          return key
            .replace(/_ratio$/, '') // _ratio 접미사 제거
            .replace(/_/g, ' ') // 언더바를 공백으로 변경
            .toUpperCase()
        }
      })

      const sortedValues = allRatios.map((item) => item.value)

      // 4. chartData에 적용
      this.chartData.labels = sortedLabels
      this.chartData.datasets[0].data = sortedValues

      console.log('Transformed Labels:', this.chartData.labels)
      console.log('Sorted Data:', this.chartData.datasets[0].data)
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

    // dst_ip별로 그룹화된 데이터를 받아서 Cytoscape에 토폴로지 요소를 생성
    // groupedData 형식: { dst_ip: [{ src_ip, dataCount, items }, ...], ... }
    makeTopologyEmlements(groupedData) {
      if (!groupedData || Object.keys(groupedData).length === 0) {
        return
      }

      const dstIpList = Object.keys(groupedData)
      const colsPerRow = 3 // 가로축에 배치할 dst_ip 개수
      const horizontalSpacing = 600 // 가로축 dst_ip들 사이의 간격
      const verticalSpacing = 600 // 세로축 dst_ip들 사이의 간격
      const startX = 300 // 시작 x 좌표
      const startY = 200 // 시작 y 좌표
      const radius = 150 // src_ip들이 배치될 원의 반지름

      // 각 dst_ip에 대해 처리
      dstIpList.forEach((dst_ip, dstIndex) => {
        const srcIpList = groupedData[dst_ip]

        // 그리드 위치 계산 (가로 3개씩 배치)
        const col = dstIndex % colsPerRow // 가로 위치 (0, 1, 2)
        const row = Math.floor(dstIndex / colsPerRow) // 세로 위치 (0, 1, 2, ...)
        const dstX = startX + col * horizontalSpacing
        const dstY = startY + row * verticalSpacing

        // dst_ip 노드 추가
        if (!window.v.nttMap.$id(dst_ip).length) {
          window.v.nttMap.add({
            group: 'nodes',
            data: {
              id: dst_ip,
              name: dst_ip,
              ipType: 'destination',
            },
            position: { x: dstX, y: dstY },
          })
        }

        // src_ip 노드들을 원형으로 배치
        srcIpList.forEach((item, srcIndex) => {
          const { src_ip, dataCount } = item
          const angle = (2 * Math.PI * srcIndex) / srcIpList.length // 원형 배치를 위한 각도
          const srcX = dstX + radius * Math.cos(angle)
          const srcY = dstY + radius * Math.sin(angle)

          // src_ip 노드 추가 (이미 존재하지 않는 경우만)
          if (!window.v.nttMap.$id(src_ip).length) {
            window.v.nttMap.add({
              group: 'nodes',
              data: {
                id: src_ip,
                name: src_ip,
                ipType: 'source',
              },
              position: { x: srcX, y: srcY },
            })
          }

          // 엣지(연결) 생성
          window.v.nttMap.add({
            group: 'edges',
            data: {
              id: `${src_ip}_to_${dst_ip}`,
              source: src_ip,
              target: dst_ip,
              count: dataCount + 'Packets',
              weight: 3,
            },
          })
        })
      })
    },

    initTopology() {
      return cytoscape({
        container: document.getElementById('nttMap'), // 렌더링할 HTML 컨테이너
        elements: [],
        style: [
          {
            selector: 'node',
            style: {
              label: 'data(name)',
              'text-valign': 'bottom',
              color: '#fff',
              'text-outline-width': 1,
              'text-outline-color': '#888',
              'background-color': 'green',
              'text-margin-y': '4px',
              'font-size': '15px',
            },
          },
          {
            selector: 'edge',
            style: {
              'curve-style': 'bezier',
              'target-arrow-shape': 'triangle', // 방향을 나타내는 화살표
              'line-color': 'red',
              color: 'white',
              width: function (ele) {
                return ele.data('weight') + 'px'
              },
              label: 'data(count)', // 엣지에 빈도수 표시
              'font-size': '10px',
              'target-arrow-color': 'red',
            },
          },
          {
            selector: '[ipType = "destination"]',
            style: {
              'background-color': '#dc3545', // 목적지 IP는 빨간색으로 강조
            },
          },
          {
            selector: '.selected',
            style: {
              'border-width': '2px',
              'overlay-padding': '10px',
              'overlay-opacity': 0.2,
              'overlay-color': '#ff6666',
              'transition-property': 'overlay-padding, overlay-opacity, overlay-color',
              'transition-duration': '0.7s',
              'transition-timing-function': 'ease-in-out',
            },
          },
        ],
        layout: {
          name: 'preset',
          animate: false,
        },
        zoom: 0.5,
        minZoom: 0.5,
        maxZoom: 3,
        pan: { x: 0, y: 0 },
        wheelSensitivity: 0.2,
      })
    },

    // 네트워크 데이터를 dst_ip별로 그룹화하여 반환
    // 반환 형식: { dst_ip: [{ src_ip, dataCount, items }, ...], ... }
    processNetworkData(arrayListA) {
      if (!arrayListA || arrayListA.length === 0) {
        return {}
      }

      // 먼저 src_ip-dst_ip 쌍으로 그룹화
      const connectionGroups = arrayListA.reduce((acc, item) => {
        const key = `${item.src_ip}-${item.dst_ip}`
        if (!acc[key]) {
          acc[key] = []
        }
        acc[key].push(item)
        return acc
      }, {})

      // dst_ip별로 재그룹화
      const dstIpGroups = {}
      for (const key in connectionGroups) {
        const [src_ip, dst_ip] = key.split('-')
        const items = connectionGroups[key]
        const dataCount = items.length

        if (!dstIpGroups[dst_ip]) {
          dstIpGroups[dst_ip] = []
        }
        dstIpGroups[dst_ip].push({ src_ip, dataCount, items })
      }

      return dstIpGroups
    },

    async loadNttTicketTotalDataList() {
      if (this.selectedRow.ticket_id === 'NTT_SIMULATION') {
        const [firstKey] = Object.keys(TEST_TICKET_SAMPLE)
        return TEST_TICKET_SAMPLE[firstKey]
      } else {
        const res = await apiSelectNttTicketTotalDataList({ TICKET_ID: this.selectedRow.ticket_id })
        return res.result
      }
    },

    async popupShowCommand() {
      // if (!this.isFocusModeButNotFocus) {
      //   this.$store.dispatch('chatbot/botPushAnswerMessage', {
      //     content:
      //       `<div class="chatbot-command-header">AI 장애대응화면</div>
      //     AI에서 지정한 임계치를 초과한 시점의 실제 트래픽과 AI 임계치가 어느정도인지 차트를 통하여 확인할 수 있습니다.
      //     <br>${constants.nia.chatbotIcon.Information} 차트 라벨을 클릭으로 차트를 표시하거나 숨깁니다.
      //     ${constants.nia.chatbotIcon.Information} 장애 시점은 MBPS 차트에서 확인할 수 있으며 OUT, IN 중 장애가 발생한 정보만 초기에 표시합니다.
      //     ${constants.nia.chatbotIcon.Information} BPS는 대역폭을 확인하여 대역폭 포화를 감지합니다.
      //     ${constants.nia.chatbotIcon.Information} PPS는 Packet 개수로 DDOS공격을 확인합니다.<br>
      //     ` + (await getWindowActionList(constants.nia.chatbotKeyMap.aiResponse.dialogNm, constants.nia.chatbotKeyMap.aiResponse.popupName)),
      //   })
      // }
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

.aiResponse_NTT {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */
}

#nttMap {
  border: none;
  background-color: black;
  border-radius: 12px; /* 부드러운 모서리 */
  padding: 0px 10px; /* 차트와 주변 여백 확보 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 은은한 그림자 효과 */
  height: 670px;
  width: 100%;
}

.aiResponse_NTT #doughnut-chart {
  height: 550px !important;
}

::v-deep .chatbot-donut-chart {
  border: none;
  background-color: #fcfcfc; /* 아주 옅은 배경색 */
  border-radius: 12px; /* 부드러운 모서리 */
  padding: 0px 10px; /* 차트와 주변 여백 확보 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 은은한 그림자 효과 */
  height: 300px;
  width: 100%;
}

.sop-stats-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 6px;

  td {
    padding: 4px 6px;
    border-bottom: 1px solid #e2e8f0;
    font-size: 12px;
    color: #334155;

    &:first-child {
      width: 60%;
      font-weight: 600;
    }

    &:last-child {
      text-align: right;
      color: #0f172a;
    }
  }
}
</style>
