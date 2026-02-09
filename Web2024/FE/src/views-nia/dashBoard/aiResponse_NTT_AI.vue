<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-full">
      <!-- Layer 1: 요청구간, 데이터 -->
      <el-row style="overflow: hidden; flex: 1" :gutter="10">
        <el-col :span="6" style="height: 100%">
          <el-card shadow="never" style="height: 50%" :body-style="{ padding: '10px', height: '100%' }">
            <div slot="header">
              <span><i class="el-icon-document" />&nbsp;&nbsp;유해트래픽 정확도 통계</span>
            </div>
            <div style="height: 200px; margin-top: 10px">
              <table class="sop-stats-table">
                <tbody>
                  <tr v-for="(label, index1) in chartData.labels" :key="label">
                    <td :style="{ background: index1 === 0 ? 'red' : 'white', color: index1 === 0 ? 'white' : 'black' }">{{ label }}</td>
                    <td :style="{ background: index1 === 0 ? 'red' : 'white', color: index1 === 0 ? 'white' : 'black' }">{{ chartData.datasets[0].data[index1] }}%</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </el-card>
          <el-card shadow="never" style="height: 50%" :body-style="{ padding: '10px', height: '100%' }">
            <div slot="header">
              <span><i class="el-icon-document" />&nbsp;&nbsp;토폴로지 패킷 정보</span>
            </div>
            <div style="height: calc(100% - 50px); margin-top: 10px; overflow: auto">
              <CompAgGrid ref="packetTableGrid" v-model="packetTableGrid" class="w-100 flex-fill" style="height: 100%" />
              <!-- <table class="sop-stats-table">
                <thead>
                  <tr>
                    <th>출발지IP</th>
                    <th>목적지IP</th>
                    <th>패킷수</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(pData) in packetTableData" :key="pData">
                    <td>{{ pData.src_ip }}</td>
                    <td>{{ pData.dst_ip }}</td>
                    <td>{{ pData.dataCount }}</td>
                  </tr>
                </tbody>
              </table> -->
            </div>
          </el-card>
        </el-col>
        <el-col :span="18" style="height: 100%">
          <el-card v-loading="isTopologyLoading" shadow="never" style="height: 100%" :body-style="{ padding: '10px', height: '100%', overflow: 'auto' }" element-loading-text="토폴로지 데이터를 불러오는 중...">
            <div slot="header">
              <span><i class="el-icon-document" /> Top5 Node 토폴로지 Map [탐지량 : {{ alarmFocusNTTAIDetailInfo.row_cnt }}] [탐지기간 : {{ formatterTimeStamp(alarmFocusNTTAIDetailInfo.oldest_timestamp, 'YY/MM/DD-HH:mm:ss') }} ~ {{ formatterTimeStamp(alarmFocusNTTAIDetailInfo.latest_timestamp, 'YY/MM/DD-HH:mm:ss') }}]</span>
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
import _ from 'lodash'
import { apiSelectNttTicketTotalDataList, apiSelectRcaNttTicketDetailInfo } from '@/api/nia'
import { getChatbotTicketData, getWindowActionList, makeOpenPopupNumberText, getInvisibleSpanParameter, getNiaRouterPathByName } from '@/views-nia/js/commonNiaFunction'
// import { formatterTime } from '@/views-nia/js/commonFormat'
import { mapState } from 'vuex'
// import {  } from '@/views-nia/js/commonNiaFunction'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import constants from '@/min/constants'
import cytoscape from 'cytoscape'
const routeName = 'aiResponse_NTT_AI'

import TEST_TICKET_SAMPLE from '@/views-nia/js/simulationData/TEST_TICKET_SELECT_NTT_TICKET_TOTAL_DATA_LIST.json'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: {
    nttTicketDataList: () => import('@/views-nia/dashBoard/nttTicketDataList'),
    CompAgGrid
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
      isTopologyLoading: false,
      packetTableData: [],
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
        customTitle: {
          display: true,
          text: '유해트래픽 정확도 통계',
          fontSize: 16,
          fontStyle: 'bold',
          fontColor: '#333',
        },
        legend: {
          position: 'right',
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
      aiResponseEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.aiResponse_NTT_AI.parameterKey],
      alarmFocusNTTAIDetailInfo: (state) => state.chatbot.alarmFocusNTTAIDetailInfo,
    }),
    isModal() {
      return !!this.wdata.params
    },
    packetTableGrid() {
      const columns = [
        { type: '', prop: 'src_ip', name: '출발지IP', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'dst_ip', name: '목적지IP', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'dataCount', name: '패킷수', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return { options, columns, data: this.packetTableData }
    }
  },
  watch: {
    aiResponseEventText(nVal, oVal) {
      if (this.isModal) {
        switch (nVal) {
          case constants.nia.chatbotCommand.packetListAll.action:
            this.visibleNttTicketDataList()
            break
        }
      }
    },
  },

  created() {
    this.selectedRow = this.wdata?.params
  },
  async mounted() {
    await this.setTicketDataForChatbotTicketData()
    await this.setDonutChartData()

    // 토폴로지 로딩 시작
    this.isTopologyLoading = true
    try {
      window.v.nttMap = this.initTopology()
      this.ticketTotalData = await this.loadNttTicketTotalDataList()
      const groupedData = this.processNetworkData(this.ticketTotalData)
      this.makeTopologyEmlements(groupedData)
      this.makePacketTableData(groupedData)
    } catch (error) {
      console.error('토폴로지 로딩 중 오류 발생:', error)
      this.$alert('토폴로지 데이터를 불러오는 중 오류가 발생했습니다.', '오류', {
        confirmButtonText: '확인',
        type: 'error',
      })
    } finally {
      // 로딩 종료
      this.isTopologyLoading = false
    }

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    makePacketTableData(groupedData) {
      this.packetTableData = Object.keys(groupedData).reduce((acc, k) => {
        groupedData[k].forEach((srcData) => {
          acc.push({ src_ip: srcData.src_ip, dst_ip: k, dataCount: srcData.dataCount })
        })

        return acc
      }, [])
    },

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
      }
    },

    // dataCount에 따라 색상을 계산하는 함수
    // 상위 30%: 진한 빨강, 중간 40%: 주황빨강, 하위 30%: 연한 빨강
    // sortedDataCounts: 정렬된 dataCount 배열 (내림차순)
    calculateEdgeColor(dataCount, sortedDataCounts) {
      if (sortedDataCounts.length === 0) {
        return '#dc2626' // 기본값: 진한 빨강
      }

      // 현재 dataCount보다 크거나 같은 값들의 개수를 세어서 percentile 계산
      const higherOrEqualCount = sortedDataCounts.filter((val) => val >= dataCount).length
      const percentile = (higherOrEqualCount / sortedDataCounts.length) * 100

      if (percentile <= 30) {
        return '#dc2626'
      } else if (percentile <= 70) {
        return '#f97316'
      } else {
        return '#facc15'
      }
    },

    // dst_ip별로 그룹화된 데이터를 받아서 Cytoscape에 토폴로지 요소를 생성
    // groupedData 형식: { dst_ip: [{ src_ip, dataCount, items }, ...], ... }
    makeTopologyEmlements(groupedData) {
      if (!groupedData || Object.keys(groupedData).length === 0) {
        return
      }

      // 모든 dataCount 수집하여 정렬 (내림차순)
      const allDataCounts = []
      Object.values(groupedData).forEach((srcIpList) => {
        srcIpList.forEach((item) => {
          allDataCounts.push(item.dataCount)
        })
      })
      const sortedDataCounts = [...allDataCounts].sort((a, b) => b - a) // 내림차순 정렬

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

          // dataCount에 따른 색상 계산
          const edgeColor = this.calculateEdgeColor(dataCount, sortedDataCounts)

          // 엣지(연결) 생성
          window.v.nttMap.add({
            group: 'edges',
            data: {
              id: `${src_ip}_to_${dst_ip}`,
              source: src_ip,
              target: dst_ip,
              count: dataCount + 'Packets',
              weight: 3,
              edgeColor: edgeColor, // 색상 값을 데이터에 저장
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
              'line-color': function (ele) {
                return ele.data('edgeColor') || '#dc2626' // 기본값: 진한 빨강
              },
              color: 'white',
              width: function (ele) {
                return ele.data('weight') + 'px'
              },
              label: 'data(count)', // 엣지에 빈도수 표시
              'font-size': '10px',
              'target-arrow-color': function (ele) {
                return ele.data('edgeColor') || '#dc2626' // 기본값: 진한 빨강
              },
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
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content:
          '<div class="chatbot-command-header">유해트래픽 AI 장애대응팝업 안내</div>' +
          '<div class="chatbot-message-body">' +
            '유해 트래픽이 발생한 기간 동안 공격을 받은 노드 TOP 5를 표시하여, 장애 상황을 빠르게 파악할 수 있는 화면입니다.' +
            '<div class="chatbot-process">' +
            constants.nia.chatbotContent.analysisTipHeaderText + '<br><br>' +
              '• <b>공격받는 노드</b> 중심으로 <b>이상 징후</b> 파악<br>' +
              '• <b>전체 패킷리스트</b>로 <b>전체 패킷 정보</b>확인<br>' +
              '• <b>유해트래픽 정확도 통계</b>로 AI모델이 유추한 정확도 확인<br>' +
              '• <b>토폴로지 색상</b> - 빨: 상위 30%, 주: 상위 40%, 노: 하위30%' +
            '</div>' +
          '</div>' +
            (await getWindowActionList(constants.nia.chatbotKeyMap.aiResponse_NTT_AI.dialogNm, constants.nia.chatbotKeyMap.aiResponse_NTT_AI.popupName,
              makeOpenPopupNumberText(2, constants.nia.chatbotKeyMap.requestForAction.key) +
              makeOpenPopupNumberText(3, constants.nia.chatbotKeyMap.sopHistory.key) +
              makeOpenPopupNumberText(4, constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.key)
            )),
        })
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

.aiResponse_NTT_AI {
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

.aiResponse_NTT_AI #doughnut-chart {
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
