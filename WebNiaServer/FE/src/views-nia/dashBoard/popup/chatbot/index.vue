<template>
  <div ref="chatbot-total" :class="{ [name]: true, 'w-full h-full': true }">
    <div class="chatbot-body" :class="{ 'chatbot-split-mode': isEmbeddedMode }" @click="focusChatInput">
      <div class="chatbot-left-panel">
        <div class="chat-header">
          <h3>
            어시스턴트 <span :style="{ color: isQuestionMode ? 'red' : 'green' }">[{{ isQuestionMode ? 'OFF' : 'ON' }}]</span>
          </h3>
          <div v-if="isQuestionMode">현재 어시스턴트는 휴면중입니다</div>
          <div v-else>
            <div v-if="alarmFocusMode_TicketData.ticket_type == 'NTT_AI'">
              <span>[TICKET_ID: {{ alarmFocusMode_TicketData.ticket_id }}] </span>
              <span>[전표유형: {{ getTicketTypeHangle(alarmFocusMode_TicketData.ticket_type) }}] </span>
              <span>[장애유형 : {{ alarmFocusNTTAIDetailInfo.traffic_type }}]</span>
              <br />
              <span>[탐지기간 : {{ formatterTimeStamp(alarmFocusNTTAIDetailInfo.oldest_timestamp, 'YY/MM/DD-HH:mm:ss') }} ~ {{ formatterTimeStamp(alarmFocusNTTAIDetailInfo.latest_timestamp, 'YY/MM/DD-HH:mm:ss') }}]</span>
              <span>[탐지량 : {{ alarmFocusNTTAIDetailInfo.row_cnt }}]</span>
            </div>
            <div v-else>
              <span v-if="alarmFocusMode_TicketData.ticket_type == 'SYSLOG'">[ALARM NO: {{ alarmFocusMode_TicketData.alarmno }}] </span>
              <span v-else>[TICKET_ID: {{ alarmFocusMode_TicketData.ticket_id }}] </span>
              <span>[전표유형: {{ getTicketTypeHangle(alarmFocusMode_TicketData.ticket_type) }}] </span>
              <br />
              <span>[IP주소: {{ alarmFocusMode_TicketData.ip_addr }}] </span>
              <span>[장비명: {{ alarmFocusMode_TicketData.node_nm }}] </span>
              <span>[인터페이스명: {{ alarmFocusMode_TicketData.alarmloc }}]</span>
            </div>
          </div>
        </div>

        <div v-if="isQuestionMode" ref="chatMessagesBox" class="chat-messages">
          <div v-for="(message, index) in questionMode_chatMessages" :key="index" :class="['message', message.type]">
            <div :style="{ display: message.type !== botAlertText || isActiveBotAlert ? visible : none }">
              <div class="message-content" @click="handlePathClick($event, message.content)" v-html="formatMessage(message.content)"></div>
              <div class="message-time">
                {{ message.time }}
              </div>
            </div>
          </div>
        </div>
        <div v-else ref="chatMessagesBox" class="chat-messages">
          <div v-loading="donutChartLoading" class="chatbot-donut-chart-container">
            <DoughnutChart v-if="alarmFocusSopDataList.length > 0" ref="donutChart1" class="chatbot-donut-chart" :chart-data="chartData" :options="chartOptions" style="margin-bottom: 10px" />
            <DoughnutChart v-if="alarmFocusTicketData.ticket_type === 'NTT_AI'" ref="donutChart2" class="chatbot-donut-chart" :chart-data="nttChartData" :options="nttChartOptions" />
          </div>
          <div v-for="(message, index) in alarmFocusMode_chatMessages" :key="index" :class="['message', message.type]">
            <div v-if="message.type !== botAlertText || isActiveBotAlert">
              <div
                ref="messageContent"
                class="message-content"
                @click="handlePathClick($event, message.content)"
                v-html="formatMessage(message.content)"
              ></div>
            <!-- <div class="message-time">
              {{ message.time }}
            </div> -->
            </div>
          </div>
        </div>

        <div class="utility-buttons">
          <button :disabled="isQuestionMode" class="utility-button" :style="{ 'background-color': isActiveBotAlert ? '#ff4949' : '#e5e7eb' }" @click="toggleIsActiveBotAlert">{{ isActiveBotAlert ? '경보 표시' : '경보 미표시' }}</button>
          <button :disabled="recognizing || isQuestionMode" class="utility-button" @click="actionSwitch">{{ actionType === 'expert' ? '전문가모드' : '안내모드' }}</button>
          <button :disabled="isQuestionMode" class="utility-button" @click="resetChat">채팅초기화</button>
          <button :disabled="isQuestionMode" class="utility-button" @click="showWindowList">창목록</button>

          <button :disabled="isQuestionMode" class="utility-button" :style="{ 'background-color': isEmbeddedMode ? '#409EFF' : '#e5e7eb', color: isEmbeddedMode ? 'white' : '#4b5563' }" @click="toggleEmbeddedMode">통합 뷰</button>
        </div>

        <div class="chat-input">
          <input ref="chatInput" v-model="userInput" :disabled="isQuestionMode" type="text" placeholder="질문을 입력하세요..." @keyup.enter="sendMessage" />
          <button :disabled="!userInput.trim() || isQuestionMode" @click="sendMessage">전송</button>
        </div>
      </div><!-- chatbot-left-panel 끝 -->

      <!-- 통합 뷰: 우측 임베디드 Vue 컴포넌트 영역 -->
      <div v-if="isEmbeddedMode" ref="rightPanel" class="chatbot-right-panel" :class="{ 'chatbot-right-panel-multi': embeddedComponents.length > 1 }">
        <template v-if="embeddedComponents.length > 0">
          <div v-for="(item, idx) in embeddedComponents" :key="idx" class="chatbot-embedded-resizable" :style="getEmbeddedWrapperStyle(item, idx)">
            <div class="chatbot-embedded-wrapper">
              <component :is="item.component" :wdata="item.wdata" />
            </div>
            <!-- 리사이즈 핸들: 4변 + 4꼭지 -->
            <div class="resize-handle resize-handle-n" @mousedown="onResizeStart($event, idx, 'n')"></div>
            <div class="resize-handle resize-handle-s" @mousedown="onResizeStart($event, idx, 's')"></div>
            <div class="resize-handle resize-handle-w" @mousedown="onResizeStart($event, idx, 'w')"></div>
            <div class="resize-handle resize-handle-e" @mousedown="onResizeStart($event, idx, 'e')"></div>
            <div class="resize-handle resize-handle-nw" @mousedown="onResizeStart($event, idx, 'nw')"></div>
            <div class="resize-handle resize-handle-ne" @mousedown="onResizeStart($event, idx, 'ne')"></div>
            <div class="resize-handle resize-handle-sw" @mousedown="onResizeStart($event, idx, 'sw')"></div>
            <div class="resize-handle resize-handle-se" @mousedown="onResizeStart($event, idx, 'se')"></div>
          </div>
        </template>
        <div v-else-if="embeddedComponent" class="chatbot-embedded-resizable" :style="embeddedSingleSize || { width: (Number(embeddedViewSize.width) + 4) + 'px', height: (Number(embeddedViewSize.height) + 4) + 'px' }">
          <div class="chatbot-embedded-wrapper">
            <component :is="embeddedComponent" :wdata="embeddedComponentData" />
          </div>
          <div class="resize-handle resize-handle-n" @mousedown="onResizeStart($event, -1, 'n')"></div>
          <div class="resize-handle resize-handle-s" @mousedown="onResizeStart($event, -1, 's')"></div>
          <div class="resize-handle resize-handle-w" @mousedown="onResizeStart($event, -1, 'w')"></div>
          <div class="resize-handle resize-handle-e" @mousedown="onResizeStart($event, -1, 'e')"></div>
          <div class="resize-handle resize-handle-nw" @mousedown="onResizeStart($event, -1, 'nw')"></div>
          <div class="resize-handle resize-handle-ne" @mousedown="onResizeStart($event, -1, 'ne')"></div>
          <div class="resize-handle resize-handle-sw" @mousedown="onResizeStart($event, -1, 'sw')"></div>
          <div class="resize-handle resize-handle-se" @mousedown="onResizeStart($event, -1, 'se')"></div>
        </div>
        <div v-else class="chatbot-embedded-empty">
          <span>NO IMAGE</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { getChatbotDonutChart } from '@/views-nia/js/donutChartBunddle'
import { VoiceRecognition } from '@/views-nia/js/chatbotVoiceRecognition'
import { errorMessaging1 } from '@/store/modules/chatbot.js'
import { getChatbotMdiObject, getNiaRouteNameByPath, getNiaRouteTitleByPath, getSpanFormatMessageForDB, getMatchMapOfspanFormatMessage, isSpanFormatChatMessage, makeOpenPopupNumberText } from '@/views-nia/js/commonNiaFunction'
import constants from '@/min/constants'
import hotkeys from 'hotkeys-js'
import _ from 'lodash'
import html2canvas from 'html2canvas'

const routeName = 'chatbot'

export default {
  name: routeName,
  components: {
    DoughnutChart: getChatbotDonutChart(),
  },
  directives: { elDragDialog },
  extends: Modal,
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
      userInput: '',
      isActiveBotAlert: false,
      recognizing: false, // 음성인식 상태
      voiceRecognition: null, // VoiceRecognition 인스턴스
      sopEmptyDataCount: 0,
      donutChartLoading: false,
      chartData: {
        labels: [],
        datasets: [
          {
            backgroundColor: ['#1569C7', '#FFAD99', '#FAFAD2', '#FFB6C1', '#BDBADF'],
            data: [0, 0, 0, 0],
          },
        ],
      },
      nttChartData: {
        labels: [],
        datasets: [
          {
            backgroundColor: ['#1569C7', '#FFAD99', '#FAFAD2', '#FFB6C1', '#BDBADF'],
            data: [0, 0, 0, 0],
          },
        ],
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        customTitle: {
          display: true,
          text: '', // 소스에서 변경경
          fontSize: 16,
          fontStyle: 'bold',
          fontColor: '#333',
        },
        isShowCenterText: true,
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
                    text: label + ': ' + counts[i] + '개',
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
      nttChartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        customTitle: {
          display: true,
          text: '', // 소스에서 지정
          fontSize: 16,
          fontStyle: 'bold',
          fontColor: '#333',
        },
        isShowCenterText: false,
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
                    text: label + ': ' + counts[i] + '%',
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
      isEmbeddedMode: false, // 통합 뷰 모드
      embeddedComponent: null, // 현재 임베디드로 표시할 Vue 컴포넌트
      embeddedComponentData: {}, // 임베디드 컴포넌트에 전달할 wdata
      embeddedViewSize: { width: '800', height: '600' }, // 임베디드 뷰 사이즈 (dialogList 기준)
      embeddedComponents: [], // 다중 컴포넌트 배열 [{ component, wdata, size }]
      embeddedSingleSize: null, // 단일 컴포넌트 리사이즈 시 사이즈 오버라이드
      resizeState: null, // { idx, direction, startX, startY, startW, startH }
    }
  },
  computed: {
    ...mapState({
      questionMode_chatMessages: (state) => state.chatbot.questionMode_chatMessages,
      alarmFocusMode_chatMessages: (state) => state.chatbot.alarmFocusMode_chatMessages,
      alarmFocusTicketData: (state) => state.chatbot.alarmFocusTicketData,
      alarmFocusSopDataList: (state) => state.chatbot.alarmFocusSopDataList,
      alarmFocusNTTAIDetailInfo: (state) => state.chatbot.alarmFocusNTTAIDetailInfo,
      currentMode: (state) => state.chatbot.currentMode,
      actionType: (state) => state.chatbot.actionType,
      windows: (state) => state.mdi.windows,
    }),

    alarmFocusMode_TicketData() {
      return this.alarmFocusTicketData
    },

    getCurrentChatMessageArray() {
      return this.currentMode === 'questionMode' ? this.questionMode_chatMessages : this.alarmFocusMode_chatMessages
    },

    routerList() {
      return this.$router.options.routes2
    },

    isQuestionMode() {
      return this.currentMode === 'questionMode'
    },
    isDebug() {
      return this.appOptions.debug
    },

    botAlertText() {
      return constants.nia.chatType.botAlert
    },

    warningSignText() {
      return constants.nia.chatbotIcon.WarningSign
    },
  },
  watch: {
    questionMode_chatMessages(nval, oval) {
      if (this.currentMode === 'questionMode') {
        this.scrollToBottom()
      }
    },

    alarmFocusMode_chatMessages(nVal, oval) {
      if (this.currentMode === 'alarmFocusMode') {
        this.scrollToBottom()
      }
    },

    alarmFocusSopDataList: {
      handler(nVal, oval) {
        this.setDonutChartData()
      },
      deep: true,
    },

    recognizing(navl, oval) {
      if (navl) {
        if (this.actionType === constants.nia.chatbotActiontype.assist) {
          this.actionSwitch()
        }
      }
    },
  },
  created() {
    this.selectedRow = this.wdata.params
    this.scrollToBottom()

    const chatbotPopup = getChatbotMdiObject()
    // input/textarea에서도 alt+ 단축키 동작하도록 filter 오버라이드
    hotkeys.filter = (e) => {
      if (e.altKey) return true
      const tag = (e.target || e.srcElement).tagName
      return !(tag === 'INPUT' || tag === 'TEXTAREA' || tag === 'SELECT')
    }
    // 통합 뷰 embedded Vue 컴포넌트 내부에서 발생한 키 이벤트만 무시
    const isEmbeddedTarget = (e) => {
      if (!this.$refs.rightPanel) return false
      const wrappers = this.$refs.rightPanel.querySelectorAll('.chatbot-embedded-wrapper')
      return Array.from(wrappers).some(w => w.contains(e.target))
    }
    if (this.isDebug) {
      hotkeys(`alt+q`, (e, h) => {
        if (isEmbeddedTarget(e)) return
        if (chatbotPopup) {
          // 챗봇 팝업 앞으로
          this.allWaysFrontWindowChatbot()
        }
      })
      hotkeys(`alt+w`, (e, h) => {
        if (isEmbeddedTarget(e)) return
        if (chatbotPopup) {
          // 챗봇 사이즈 조정
          const heightValue = parseInt(chatbotPopup.height)
          if (heightValue >= 800 && heightValue <= 1000) {
            chatbotPopup.height = window.innerHeight - 70
            chatbotPopup.x = 10
            chatbotPopup.y = 10
          } else {
            chatbotPopup.height = '900'
            chatbotPopup.width = '600'
            chatbotPopup.x = 10
            chatbotPopup.y = window.innerHeight - chatbotPopup.height - 60
          }
        }
      })
      hotkeys(`alt+z`, (e, h) => {
        if (isEmbeddedTarget(e)) return
        if (chatbotPopup) {
          // 채팅초기화
          this.resetChat()
        }
      })
      hotkeys(`alt+v`, (e, h) => {
        if (isEmbeddedTarget(e)) return
        if (chatbotPopup) {
          // voice ON/OFF
          this.switchVoiceRecording()
        }
      })
      hotkeys(`alt+l`, (e, h) => {
        if (isEmbeddedTarget(e)) return
        if (chatbotPopup) {
          // 창 목록
          this.showWindowList()
        }
      })
    }
  },

  mounted() {
    this.setDonutChartData()
    // 음성인식 초기화
    this.voiceRecognition = new VoiceRecognition(this, {
      inputField: 'userInput', // 결과가 누적될 필드명
      statusField: 'recognizing', // 상태가 저장될 필드명
      lang: 'ko-KR',
    })
    // 외부에서 다중 뷰 표시 호출 가능하도록 노출
    window.chatbotOpenMultiView = this.openMultiViewInChatbot.bind(this)
    window.chatbotOpenView = this.openViewInChatbot.bind(this)
    window.chatbotIsEmbeddedMode = () => this.isEmbeddedMode
    window.chatbotToggleEmbeddedMode = this.toggleEmbeddedMode.bind(this)
  },

  beforeDestroy() {
    // 컴포넌트 파괴 시 음성인식 리소스 정리
    if (this.voiceRecognition) {
      this.voiceRecognition.destroy()
    }
    window.chatbotOpenMultiView = null
    window.chatbotOpenView = null
    window.chatbotIsEmbeddedMode = null
    window.chatbotToggleEmbeddedMode = null
    document.removeEventListener('mousemove', this.onResizeMove)
    document.removeEventListener('mouseup', this.onResizeEnd)
  },

  methods: {
    showWindowList() {
      let count = 0
      let text = '<div class="chatbot-command-header">열려있는 창 목록</div>'
      this.windows.forEach((w) => {
        if (w.dialogNm === 'chatbot') return
        text += makeOpenPopupNumberText(++count, w.dialogNm)
      })

      this.$store.dispatch('chatbot/botPushAnswerMessage', {
        content: text,
      })
    },

    async switchVoiceRecording() {
      this.voiceRecognition.toggle()
    },
    makeNTTDonutChartData1() {
      const data = _.cloneDeep(this.alarmFocusNTTAIDetailInfo)
      delete data.traffic_type

      const targetKeys = [
        'normal_traffic_ratio',
        'tcp_syn_flooding_ratio',
        'land_attack_ratio',
        'ping_of_death_ratio',
        'udp_flooding_ratio'
      ]

      // 2. targetKeys를 순회하며 data에서 값을 찾아 배열로 변환
      const allRatios = targetKeys.map(key => ({
        key: key,
        value: Number(data[key] || 0) // 데이터가 없을 경우를 대비해 기본값 0 설정
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

      // 4. nttChartData에 적용
      this.nttChartData.labels = sortedLabels
      this.nttChartData.datasets[0].data = sortedValues
      this.nttChartOptions.customTitle.text = '유해트래픽 정확도 통계'

      const colors = ['#1569C7', '#FFAD99', '#FAFAD2', '#FFB6C1', '#BDBADF']
      this.nttChartData.datasets[0].backgroundColor = colors.slice(0, this.nttChartData.labels.length)

      console.log('Transformed Labels:', this.nttChartData.labels)
      console.log('Sorted Data:', this.nttChartData.datasets[0].data)
    },

    makeSopDonutChart() {
      // faultType별 개수 계산
      const faultTypeCount = {}
      this.sopEmptyDataCount = 0
      this.alarmFocusSopDataList.forEach((data) => {
        const fault_detail_content = data.fault_detail_content
        if (data.fault_detail_content && data.fault_detail_content.length > 0) {
          faultTypeCount[fault_detail_content] = (faultTypeCount[fault_detail_content] || 0) + 1
        } else {
          this.sopEmptyDataCount++
        }
      })

      // 개수 기준으로 내림차순 정렬하여 상위 3개 추출
      const sortedFaultTypes = Object.entries(faultTypeCount)
        .sort(([, a], [, b]) => b - a)
        .slice(0, 4)

      // 상위 4개 faultType과 기타 개수 계산
      const top4FaultTypes = sortedFaultTypes.map(([fault_detail_content]) => fault_detail_content)
      const top4Counts = sortedFaultTypes.map(([, count]) => count)

      // 기타 개수 계산 (상위 4개가 아닌 나머지)
      const othersCount = Object.entries(faultTypeCount)
        .filter(([fault_detail_content]) => !top4FaultTypes.includes(fault_detail_content))
        .reduce((sum, [, count]) => sum + count, 0)

      // chartData 업데이트
      this.chartData.labels = [...top4FaultTypes, '기타']
      this.chartData.datasets[0].data = [...top4Counts, othersCount]

      // 색상도 동적으로 설정 (기본 색상 + 기타용 회색)
      const colors = ['#FF6384', '#36A2EB', '#FFCE56', 'gray']
      this.chartData.datasets[0].backgroundColor = colors.slice(0, this.chartData.labels.length)
      this.chartOptions.customTitle.text = '장애 조치 내용별 현황'
    },

    async setDonutChartData() {
      try {
        this.donutChartLoading = true
        await new Promise((resolve) => setTimeout(resolve, 3000))

        if (this.alarmFocusTicketData.ticket_type === 'NTT_AI') {
          this.makeSopDonutChart()
          this.makeNTTDonutChartData1()
        } else {
          this.makeSopDonutChart()
        }
      } catch (e) {
        console.error(e)
      } finally {
        this.donutChartLoading = false
      }
    },

    actionSwitch() {
      this.$store.commit('chatbot/SWTICH_ACTION')
    },

    resetChat() {
      this.$store.commit('chatbot/RESET_CHAT', { ticketData: this.alarmFocusTicketData })
      this.userInput = ''
    },

    getTicketTypeHangle(ticketType) {
      switch (ticketType) {
        case 'ATT2':
        case 'ATT2_AI':
          return '이상트래픽'
        case 'NTT':
        case 'NTT_AI':
          return '유해트래픽'
        case 'RT':
          return '장애'
        default:
          return ticketType
      }
    },

    switchMode() {
      switch (this.currentMode) {
        case 'alarmFocusMode':
          this.$store.commit('chatbot/MODE_CHANGE', { newMode: 'questionMode' })
          break
        case 'questionMode':
          if (!this.alarmFocusTicketData.ticket_id) {
            this.$alert(`대시보드에서 집중경보모드로 먼저 전환해주세요.`, '알림', {
              confirmButtonText: '확인',
              customClass: 'nia-message-box',
            })
          } else {
            this.$store.commit('chatbot/MODE_CHANGE', { newMode: 'alarmFocusMode' })
          }

          break
      }
    },

    openSop() {
      if (this.isEmbeddedMode) {
        this.openViewInChatbot('sopHistory', { isChatbotGenerated: true, params: this.alarmFocusTicketData })
      } else {
        this.fn_openWindow('sopHistory', { isChatbotGenerated: true })
      }
    },

    openConfigTest() {
      if (this.isEmbeddedMode) {
        this.openViewInChatbot('configTest', { isChatbotGenerated: true, params: this.alarmFocusTicketData })
      } else {
        this.fn_openWindow('configTest', { isChatbotGenerated: true })
      }
    },

    toggleIsActiveBotAlert() {
      this.isActiveBotAlert = !this.isActiveBotAlert
    },

    focusChatInput(e) {
      // embedded Vue 컴포넌트 내부 클릭은 제외

      if (this.$refs.rightPanel) {
        const wrappers = this.$refs.rightPanel.querySelectorAll('.chatbot-embedded-wrapper')
        if (Array.from(wrappers).some(w => w.contains(e.target))) return
      }
      if (this.$refs.chatInput && !this.isQuestionMode) {
        this.$refs.chatInput.focus()
      }
    },

    toggleEmbeddedMode() {
      this.isEmbeddedMode = !this.isEmbeddedMode
      const chatbotPopup = getChatbotMdiObject()
      if (!chatbotPopup) return

      if (this.isEmbeddedMode) {
        // 좌우 + 상하 최대화 (여백 30px)
        chatbotPopup.width = window.innerWidth - 60
        chatbotPopup.height = window.innerHeight - 60
        chatbotPopup.x = 30
        chatbotPopup.y = 30
      } else {
        // 기존 alt+w 기본 사이즈로 복원
        chatbotPopup.height = '900'
        chatbotPopup.width = '600'
        chatbotPopup.x = 10
        chatbotPopup.y = window.innerHeight - chatbotPopup.height - 60
        this.embeddedComponent = null
        this.embeddedComponentData = {}
        this.embeddedComponents = []
        this.embeddedSingleSize = null
      }
    },

    getEmbeddedWrapperStyle(item, idx) {
      // 사용자가 리사이즈한 사이즈가 있으면 우선 사용
      if (item._userWidth || item._userHeight) {
        const style = {}
        if (item._userWidth) style.width = item._userWidth + 'px'
        if (item._userHeight) style.height = item._userHeight + 'px'
        return style
      }
      // 다중 컴포넌트일 때 상/하 균등 분할, 단일일 때 기존 사이즈 사용
      if (this.embeddedComponents.length > 1) {
        return {
          width: '100%',
          height: `calc(${100 / this.embeddedComponents.length}% - ${(this.embeddedComponents.length - 1) * 4 / this.embeddedComponents.length}px)`,
        }
      }
      return {
        width: (Number(item.size?.width || 800) + 4) + 'px',
        height: (Number(item.size?.height || 600) + 4) + 'px',
      }
    },

    // 리사이즈 핸들 mousedown
    onResizeStart(event, idx, direction) {
      event.preventDefault()
      event.stopPropagation()
      const target = event.target.parentElement // .chatbot-embedded-resizable
      const rect = target.getBoundingClientRect()
      this.resizeState = {
        idx,
        direction,
        startX: event.clientX,
        startY: event.clientY,
        startW: rect.width,
        startH: rect.height,
      }
      document.addEventListener('mousemove', this.onResizeMove)
      document.addEventListener('mouseup', this.onResizeEnd)
      const cursorMap = { n: 'ns-resize', s: 'ns-resize', e: 'ew-resize', w: 'ew-resize', nw: 'nwse-resize', se: 'nwse-resize', ne: 'nesw-resize', sw: 'nesw-resize' }
      document.body.style.cursor = cursorMap[direction] || 'nwse-resize'
      document.body.style.userSelect = 'none'
    },

    onResizeMove(event) {
      if (!this.resizeState) return
      const { idx, direction, startX, startY, startW, startH } = this.resizeState
      const dx = event.clientX - startX
      const dy = event.clientY - startY
      const minW = 200
      const minH = 120

      // 방향별 너비/높이 계산: e/w는 좌우, n/s는 상하, 대각은 조합
      const resizeE = direction.includes('e')
      const resizeW = direction.includes('w')
      const resizeS = direction.includes('s')
      const resizeN = direction.includes('n')

      let newW = startW
      let newH = startH
      if (resizeE) newW = Math.max(minW, startW + dx)
      if (resizeW) newW = Math.max(minW, startW - dx)
      if (resizeS) newH = Math.max(minH, startH + dy)
      if (resizeN) newH = Math.max(minH, startH - dy)

      if (idx === -1) {
        // 단일 컴포넌트 모드
        this.embeddedSingleSize = {
          width: newW + 'px',
          height: newH + 'px',
        }
      } else {
        // 다중 컴포넌트 모드
        const item = this.embeddedComponents[idx]
        if (!item) return
        if (resizeE || resizeW) this.$set(item, '_userWidth', newW)
        if (resizeS || resizeN) this.$set(item, '_userHeight', newH)
      }
    },

    onResizeEnd() {
      this.resizeState = null
      document.removeEventListener('mousemove', this.onResizeMove)
      document.removeEventListener('mouseup', this.onResizeEnd)
      document.body.style.cursor = ''
      document.body.style.userSelect = ''
    },

    // 여러 컴포넌트를 우측 패널에 상/하 분할로 표시
    // views: [{ popupName, data }, ...]
    async openMultiViewInChatbot(views) {
      this.embeddedComponent = null
      this.embeddedComponentData = {}
      this.embeddedComponents = []

      const results = await Promise.all(
        views.map(async (view) => {
          const dialogInfo = this.dialogList[view.popupName]
          if (!dialogInfo || !dialogInfo.component) return null
          const module = await dialogInfo.component()
          return {
            component: module.default,
            wdata: { ...view.data, isChatbotEmbedded: true },
            size: { width: dialogInfo.width || '800', height: dialogInfo.height || '600' },
          }
        })
      )

      this.embeddedComponents = results.filter(Boolean)
      this.$nextTick(() => {
        this.scrollToBottom()
      })
    },

    openViewInChatbot(popupName, data) {
      // dialogList에서 동적 import를 resolve하여 embeddedComponent에 할당
      const dialogInfo = this.dialogList[popupName]
      if (!dialogInfo || !dialogInfo.component) return false

      // 다중 컴포넌트 모드 해제
      this.embeddedComponents = []
      this.embeddedSingleSize = null

      // dialogList에 정의된 사이즈 저장
      this.embeddedViewSize = {
        width: dialogInfo.width || '800',
        height: dialogInfo.height || '600',
      }

      dialogInfo.component().then((module) => {
        this.embeddedComponent = module.default
        this.embeddedComponentData = { ...data, isChatbotEmbedded: true }

        this.$nextTick(() => {
          this.scrollToBottom()
        })
      })
      return true
    },

    async sendMessage() {
      if (!this.userInput.trim()) return

      // 추가: 메시지 전송 시 음성인식 내부 버퍼도 초기화하여 꼬임 방지
      if (this.voiceRecognition) {
        this.voiceRecognition.clear()
      }

      this.$store.dispatch('chatbot/userPushQuestionMessage', { content: this.userInput })
      const userQuestion = this.userInput
      this.userInput = ''

      try {
        const searchResult = await this.SearchAndAction(userQuestion)
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: searchResult,
        })
      } catch (error) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: errorMessaging1,
        })
        console.error('ElasticSearch 검색 오류:', error)
      } finally {
        setTimeout(() => {
          this.allWaysFrontWindowChatbot()
        }, 1000)
      }
    },

    allWaysFrontWindowChatbot() {
      // 항상 챗봇을 맨 위로
      const chatbotPopup = getChatbotMdiObject()
      this.$store.dispatch('mdi/bringToFrontWindow', chatbotPopup.id)
    },

    runSpanAction(matchMap) {
      console.log('[matchMap] >> ' + 'path : ' + matchMap.path + ', popup :' + matchMap.popup + ', action :' + matchMap.action)

      let routerParameterTargetName = ''
      let text = ''
      if (matchMap.path.length > 0) {
        const routerName = getNiaRouteNameByPath(matchMap.path)
        if (this.$router.history.current.path === matchMap.path) {
          routerParameterTargetName = routerName
          text = `<br><br>` + `${constants.nia.chatbotIcon.noAction} ${getNiaRouteTitleByPath(matchMap.path)}화면에서`
        } else {
          this.$router.push({ name: routerName })
          routerParameterTargetName = routerName
          text = `<br><br>` + `${constants.nia.chatbotIcon.move} ${getNiaRouteTitleByPath(matchMap.path)}로 이동했습니다.`
        }
      }

      if (matchMap.popup.length > 0) {
        const hasWindow = this.windows.find((w) => w.dialogNm === matchMap.popup)
        let newName = ''
        if (hasWindow) {
          this.minimizeOtherPopups()
          this.openMinimizedPopup(hasWindow)

          text += `<br>${constants.nia.chatbotIcon.focusing} ${hasWindow.name} 팝업에 포커스를 맞췄습니다.`
          this.$store.dispatch('mdi/bringToFrontWindow', hasWindow.id)
          newName = hasWindow.chatbotParameterKeyName
        } else {
          this.minimizeOtherPopups()

          let popupName = matchMap.popup
          if (matchMap.popup === 'aiResponse') {
            switch (this.alarmFocusTicketData.ticket_type) {
              case 'NTT_AI':
                popupName = 'aiResponse_NTT_AI'
                break
              case 'ATT2_AI':
                popupName = 'aiResponse_ATT_AI'
                break
              default:
                popupName = 'aiResponse'
                break
            }
          }

          if (this.isEmbeddedMode) {
            this.openViewInChatbot(popupName, { isChatbotGenerated: true, params: this.alarmFocusTicketData })
          } else {
            this.fn_openWindow(popupName, { isChatbotGenerated: true })
          }

          const dialogKey = Object.keys(this.dialogList).find((key) => key === matchMap.popup)
          if (this.isEmbeddedMode) {
            text += `<br>${constants.nia.chatbotIcon.openPopup} ${this.dialogList[dialogKey].pageTitle} 화면을 표시했습니다. `
          } else {
            text += `<br>${constants.nia.chatbotIcon.openPopup} ${this.dialogList[dialogKey].pageTitle} 팝업을 활성화했습니다. `
          }
          text += `<br>${constants.nia.chatbotIcon.success} ${constants.nia.chatbotComment.parameterChange}`
          newName = this.dialogList[dialogKey].chatbotParameterKeyName
        }

        routerParameterTargetName = newName
      }

      if (matchMap.action.length > 0) {
        if (constants.nia.chatbotCommand[matchMap.action]) {
          text += `<br>${constants.nia.chatbotIcon.success} ${constants.nia.chatbotCommand[matchMap.action].label} 명령을 실행합니다.`
        } else if (constants.nia.chatbotKeyMap[matchMap.action]) {
          text += `<br>${constants.nia.chatbotIcon.success} ${constants.nia.chatbotKeyMap[matchMap.action].popupName} 명령을 실행합니다.`
        } else {
          throw new Error(`chatbotCommand또는 chatbotKeyMap에 ${matchMap.action}이 없습니다. 등록하세요`)
        }

        setTimeout(() => {
          this.$store.commit('chatbot/SWITCH_ROUTER_PARAMETER', { name: routerParameterTargetName, parameter: matchMap.action })
        }, 1000)
      }

      if (text.length === 0) text += '\n\n명령 실행에 실패했습니다.'
      return text
    },

    openMinimizedPopup(hasWindow) {
      if (hasWindow.windowState === 'minimize') {
        this.$set(hasWindow, 'windowState', 'normal')
      }
    },

    minimizeOtherPopups() {
      this.windows.forEach((w) => {
        if (w.dialogNm === 'chatbot') return
        this.$set(w, 'windowState', 'minimize')
      })
    },

    getLastSpanFormatChatMessage() {
      const botAnswer = this.getCurrentChatMessageArray.filter((m) => {
        if (m.type !== constants.nia.chatType.botAnswer) return false
        if (!m.content) return false
        if (isSpanFormatChatMessage(m.content)) return true
      })

      return botAnswer.at(-1)
    },

    async SearchAndAction(userQuestion) {
      const lastSpanFormatMessage = this.getLastSpanFormatChatMessage()

      if (isSpanFormatChatMessage(lastSpanFormatMessage.content)) {
        const matchMap = getMatchMapOfspanFormatMessage(userQuestion, lastSpanFormatMessage.content)
        if (matchMap) {
          const actionProcessMessage = this.runSpanAction(matchMap)
          return `<b>` + matchMap.matchContext + ' 명령을 실행했습니다.</b>' + actionProcessMessage
        }
      }

      const spanFormatMessage = await getSpanFormatMessageForDB(userQuestion)
      if (this.actionType === constants.nia.chatbotActiontype.expert) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: spanFormatMessage,
        })

        const matchMap = getMatchMapOfspanFormatMessage('1', spanFormatMessage)
        if (!matchMap) throw new Error('DB의 span 형식이 잘못되었네요')
        const actionProcessMessage = this.runSpanAction(matchMap)
        return `<b>` + matchMap.matchContext + ' 명령을 실행했습니다.</b>' + actionProcessMessage
      } else if (this.actionType === constants.nia.chatbotActiontype.assist) {
        return spanFormatMessage
      }
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessagesBox = this.$refs.chatMessagesBox
        if (chatMessagesBox) {
          chatMessagesBox.scrollTop = chatMessagesBox.scrollHeight
        }
      })
    },

    formatMessage(content) {
      try {
        // 이미 HTML이 포함된 경우 (chatbot-command-header 등이 있는 경우) 그대로 반환
        if (content.includes('chatbot-command-header')) {
          return content
        }

        // 줄바꿈을 <br> 태그로 변환하고 HTML 태그 제거
        let formattedContent = content.replace(/\n/g, '<br>') // 줄바꿈을 <br>로 변환
        formattedContent = formattedContent.replace(/(\s+)([^<]+)\s+<span class="move-text">\[진행\]<\/span>/g, '$1$2 <a href="#" class="move-link" data-keyword="$2">[진행]</a>')

        return formattedContent
      } catch (error) {
        console.error(error)
        return content
      }
    },

    async handlePathClick(event, content) {
      // this.captureContentToClipboard()

      // if (event.target.classList.contains('move-link')) {
      //   event.preventDefault()

      //   if (event.target.innerHTML === '[진행]') {
      //     // 집중경보 모드 전환
      //     const ticketMatch = content.match(/>티켓종류: (.*?)<\/span>/)
      //     const ticketType = ticketMatch ? ticketMatch[1] : null
      //     if (!ticketType) {
      //       this.$alert(`예상치 못한 에러 해당 장비에 ticketType이 존재하지 않습니다. 내용 : ${content}`, '오류', {
      //         confirmButtonText: '실행',
      //         cancelButtonText: '취소',
      //         customClass: 'nia-message-box',
      //       })
      //       return
      //     }

      //     let ticketId = null
      //     let alarmno = null

      //     if (ticketType === 'SYSLOG') {
      //       const alarmMatch = content.match(/>알람번호: (.*?)<\/span>/)
      //       alarmno = alarmMatch ? alarmMatch[1] : null
      //       if (!alarmno) {
      //         this.$alert(`예상치 못한 에러 해당 장비에 alarmno가 존재하지 않습니다. 내용 : ${content}`, '오류', {
      //           confirmButtonText: '실행',
      //           cancelButtonText: '취소',
      //           customClass: 'nia-message-box',
      //         })
      //         return
      //       }
      //     } else {
      //       const ticketMatch = content.match(/>티켓ID: (.*?)<\/span>/)
      //       ticketId = ticketMatch ? ticketMatch[1] : null
      //       if (!ticketId) {
      //         this.$alert(`예상치 못한 에러 해당 장비에 ticketId가 존재하지 않습니다. 내용 : ${content}`, '오류', {
      //           confirmButtonText: '실행',
      //           cancelButtonText: '취소',
      //           customClass: 'nia-message-box',
      //         })
      //         return
      //       }
      //     }

      //     const res = await apiIpAlarmList({
      //       TICKET_ID: ticketId,
      //       ALARMNO: alarmno,
      //       IS_TEST: true,
      //     })

      //     if (res && res.result.length > 0) {
      //       const ticket = res.result[0]
      //       window.changeFocusAlertMode(ticket)
      //     } else {
      //       this.$alert('해당 티켓에 대하여 집중경보 활성화에 실패하였습니다.', '오류', {
      //         confirmButtonText: '실행',
      //         cancelButtonText: '취소',
      //         customClass: 'nia-message-box',
      //       })
      //     }
      //   }
      // }
    },

    async captureContentToClipboard() {
      window.focus()
      document.body.focus()

      const el = this.$refs.messageContent
      if (!el) return

      try {
        const canvas = await html2canvas(el, {
          backgroundColor: null, // 투명 배경 유지
          scale: window.devicePixelRatio, // 선명도 개선
        })

        const blob = await new Promise((resolve) => canvas.toBlob(resolve, 'image/png'))

        await navigator.clipboard.write([new ClipboardItem({ 'image/png': blob })])

        this.$message?.success?.('클립보드에 캡처되었습니다')
        // 또는 alert("클립보드에 복사됨")
      } catch (err) {
        console.error(err)
        alert('캡처 실패 (HTTPS 환경인지 확인하세요)')
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.chatbot {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */
}

.chatbot-donut-chart-container {
  width: 400px;
}

::v-deep .chatbot-donut-chart {
  border: none;
  background-color: #fcfcfc; /* 아주 옅은 배경색 */
  border-radius: 12px; /* 부드러운 모서리 */
  padding: 0px 10px; /* 차트와 주변 여백 확보 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 은은한 그림자 효과 */
  height: 250px;
  width: 400px;
}

::v-deep .chatbot-command-header {
  background-color: #1e293b;
  font-weight: 600;
  text-align: center;
  color: white;
  margin-bottom: 10px;
}

::v-deep .chatbot-process {
  margin: 8px 0;
  padding: 8px;
  background: #f9f9f9;
  border-left: 4px solid #409EFF;
}

::v-deep .chatbot-command-section {
  margin: 10px 0;
  padding: 8px 10px;
  background: #f8fafc;
  border-left: 4px solid #67C23A; /* 안정적인 그린 */
}

::v-deep .chatbot-command-title {
  font-weight: 700;
  font-size: 13px;
  color: #303133;
  margin-bottom: 6px;
}

::v-deep .chatbot-message-body {
  margin-bottom: 10px;
}

::v-deep .chatbot-body {
  display: flex;
  flex-direction: column;
  height: 100%;

  span.chatbotIcon {
    font-size: 17px;
    background: #1e293b;
    color: white;
    height: 26px;
    width: 26px;
    display: inline-block;
    border-radius: 25px;

    .el-icon-chat-dot-square {
      display: inline-block;
      position: relative;
      top: 1px;
      left: 5px;
    }
  }
}

.chatbot-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f8fafc;
  border-radius: 0.5rem;
  overflow: hidden;
}

.chat-header {
  background: #1e293b;
  color: white;
  padding: 0.5rem;
  text-align: center;

  h3 {
    margin: 0 0 0.25rem 0;
    font-size: 1rem;
    font-weight: 600;
  }

  p {
    margin: 0;
    font-size: 0.75rem;
    opacity: 0.9;
  }
}

.chat-messages {
  flex: 1;
  padding: 1rem;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message {
  max-width: 80%;
  text-align: left;

  &.user {
    align-self: flex-end;

    .message-content {
      background: #3b82f6;
      color: white;
      border-radius: 1rem 1rem 0.25rem 1rem;
    }
  }

  &.bot-answer {
    align-self: flex-start;

    .message-content {
      background: white;
      color: #334155;
      border-radius: 1rem 1rem 1rem 0.25rem;
      border: 1px solid #e2e8f0;
    }
  }

  &.bot-alert {
    align-self: flex-start;

    .message-content {
      background: #ffcdd2;
      color: #334155;
      border-radius: 1rem 1rem 1rem 0.25rem;
      border: 1px solid #e2e8f0;
    }
  }
}

.message-content {
  padding: 0.75rem 1rem;
  font-size: 0.875rem;
  line-height: 1.4;
  word-wrap: break-word;
  white-space: normal; /* <br> 태그가 제대로 작동하도록 */

  ::v-deep .move-link {
    color: #3b82f6 !important;
    text-decoration: underline !important;
    cursor: pointer;

    &:hover {
      color: #2563eb !important;
      text-decoration: none !important;
    }
  }
}

.message-time {
  font-size: 0.75rem;
  color: #64748b;
  margin-top: 0.25rem;
  text-align: right;
}

// 통합 뷰: 좌우 분할 레이아웃
.chatbot-split-mode {
  flex-direction: row !important;

  .chatbot-left-panel {
    flex: none;
    width: 600px;
  }
}

.chatbot-left-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
  flex: 1;
  min-width: 0;
}

.chatbot-right-panel {
  flex: 1;
  height: 100%;
  overflow-y: auto;
  border-left: 1px solid #e2e8f0;
  background: #f2f2f2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.chatbot-right-panel-multi {
  flex-direction: column;
  align-items: stretch;
  justify-content: flex-start;
  gap: 4px;
  padding: 4px;
}

.chatbot-embedded-resizable {
  position: relative;
  box-sizing: border-box;
}

.chatbot-embedded-wrapper {
  background: #ffffff;
  border: 2px solid #1e293b;
  border-radius: 8px;
  overflow-y: auto;
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  padding: 5px;
}

// 리사이즈 핸들 공통
.resize-handle {
  position: absolute;
  z-index: 10;

  &:hover,
  &:active {
    background: rgba(64, 158, 255, 0.3);
  }
}

// 4변 핸들
.resize-handle-n {
  top: -4px;
  left: 0;
  width: 100%;
  height: 8px;
  cursor: ns-resize;
}

.resize-handle-s {
  bottom: -4px;
  left: 0;
  width: 100%;
  height: 8px;
  cursor: ns-resize;
}

.resize-handle-w {
  top: 0;
  left: -4px;
  width: 8px;
  height: 100%;
  cursor: ew-resize;
}

.resize-handle-e {
  top: 0;
  right: -4px;
  width: 8px;
  height: 100%;
  cursor: ew-resize;
}

// 4꼭지 핸들
.resize-handle-nw,
.resize-handle-ne,
.resize-handle-sw,
.resize-handle-se {
  width: 14px;
  height: 14px;
  z-index: 11;

  &:hover,
  &:active {
    background: transparent;
  }
}

.resize-handle-nw {
  top: -4px;
  left: -4px;
  cursor: nwse-resize;
}

.resize-handle-ne {
  top: -4px;
  right: -4px;
  cursor: nesw-resize;
}

.resize-handle-sw {
  bottom: -4px;
  left: -4px;
  cursor: nesw-resize;
}

.resize-handle-se {
  bottom: -4px;
  right: -4px;
  cursor: nwse-resize;

  &::after {
    content: '';
    position: absolute;
    bottom: 2px;
    right: 2px;
    width: 8px;
    height: 8px;
    border-right: 2px solid #94a3b8;
    border-bottom: 2px solid #94a3b8;
  }

  &:hover::after,
  &:active::after {
    border-color: #409EFF;
  }
}

.chatbot-embedded-empty {
  width: 100%;
  height: 100%;
  background: #1e293b;
  display: flex;
  justify-content: center;
  align-items: center;

  span {
    color: #ffffff;
    font-size: 1.5rem;
    font-weight: 600;
    letter-spacing: 2px;
  }
}

// 편의 기능 버튼 스타일 추가
.utility-buttons {
  display: flex;
  justify-content: space-around;
  padding: 0.5rem 1rem;
  background: white;
  border-top: 1px solid #e2e8f0;
  gap: 0.5rem; /* 버튼 사이의 간격 추가 */
}

.utility-button {
  flex: 1; /* 버튼들이 가로 공간을 균등하게 차지하도록 함 */
  padding: 0.5rem;
  background: #e5e7eb; /* 회색 배경 */
  color: #4b5563;
  border: none;
  border-radius: 0.5rem;
  font-size: 0.75rem;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s ease;

  &:hover {
    background: #d1d5db;
  }
}

.chat-input {
  height: 80px;
  display: flex;
  gap: 0.5rem;
  padding: 1rem;
  background: white;
  border-top: 1px solid #e2e8f0;

  input {
    flex: 1;
    padding: 0.75rem;
    border: 1px solid #d1d5db;
    border-radius: 0.5rem;
    font-size: 0.875rem;

    &:focus {
      outline: none;
      border-color: #3b82f6;
      box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
    }
  }

  button {
    padding: 0.75rem 1.5rem;
    background: #3b82f6;
    color: white;
    border: none;
    border-radius: 0.5rem;
    font-size: 0.875rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover:not(:disabled) {
      background: #2563eb;
    }

    &:disabled {
      background: #9ca3af;
      cursor: not-allowed;
    }
  }
}

// SOP 통계 테이블
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

/* 버튼 내 아이콘 스타일 */
.voice-btn i {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  background: #1e293b; /* 업로드하신 이미지의 검정색 느낌 */
  color: white;
  border-radius: 50%; /* 동그란 모양 */
  font-size: 12px;
}

/* 인식 중일 때 아이콘 색상 변경 (선택 사항) */
.voice-btn:not(:disabled).is-active i {
  background: white;
  color: #ff4949;
}
</style>
