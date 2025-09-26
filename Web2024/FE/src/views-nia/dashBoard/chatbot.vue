<template>
  <div ref="chatbot-total" :class="{ [name]: true, 'w-full h-full': true }">
    <div class="chatbot-body">
      <div class="chat-header">
        <h3>
          어시스턴트 <span :style="{ color: isQuestionMode ? 'red' : 'green' }">[{{ isQuestionMode ? 'OFF' : 'ON' }}]</span>
        </h3>
        <p v-if="isQuestionMode">질문을 입력하고 답변을 받아보세요</p>
        <p v-else>
          [TICKET_ID: {{ alarmFocusMode_TicketData.ticket_id }}] [전표유형: {{ getTicketTypeHangle(alarmFocusMode_TicketData.ticket_type) }}] [장비명: {{ alarmFocusMode_TicketData.node_nm }}] [인터페이스명: {{ alarmFocusMode_TicketData.alarmloc }}]
        </p>
      </div>

      <div v-if="isQuestionMode" ref="chatMessagesBox" class="chat-messages">
        <div v-for="(message, index) in questionMode_chatMessages" :key="index" :class="['message', message.type]">
          <div v-if="message.type !== botAlertText || isActiveBotAlert">
            <div class="message-content" @click="handlePathClick($event, message.content)" v-html="formatMessage(message.content)"></div>
            <div class="message-time">
              {{ message.time }}
            </div>
          </div>
        </div>
      </div>
      <div v-else ref="chatMessagesBox" class="chat-messages">
        <DoughnutChart v-if="alarmFocusSopDataList.length > 0" ref="donutChart" :chart-data="chartData" :options="chartOptions" style="height: 160px; width: 380px; border: 3px solid" />
        <div v-for="(message, index) in alarmFocusMode_chatMessages" :key="index" :class="['message', message.type]">
          <div v-if="index === 0" class="message-content">
            <span v-if="alarmFocusSopDataList.length > 0">
              {{ alarmFocusTicketData.node_nm }} 장비에 대한 SOP 조치내용 통계<br />
              전체SOP개수 : {{ alarmFocusSopDataList.length }}개<br />
              <!-- <span v-for="(label, index2) in chartData.labels" :key="index2"> {{ label }} : {{ chartData.datasets[index2].data.length + '개' }} </span> -->
            </span>
            <span v-else>SOP이력이 없어서 SOP통계자료는 제공되지않습니다</span>
          </div>
          <div v-if="message.type !== botAlertText || isActiveBotAlert">
            <div class="message-content" @click="handlePathClick($event, message.content)" v-html="formatMessage(message.content)"></div>
            <div class="message-time">
              {{ message.time }}
            </div>
          </div>
        </div>
      </div>

      <div class="utility-buttons">
        <button :disabled="isQuestionMode" class="utility-button" @click="openSop">SOP화면</button>
        <button :disabled="isQuestionMode" class="utility-button" @click="openConfigTest">조치화면</button>
        <button :disabled="isQuestionMode" class="utility-button" :style="{ 'background-color': isActiveBotAlert ? '#ff4949' : '#e5e7eb' }" @click="toggleIsActiveBotAlert">{{ isActiveBotAlert ? '경보 표시' : '경보 미표시' }}</button>
        <button :disabled="isQuestionMode" class="utility-button" @click="actionSwitch">{{ actionType === 'expert' ? '전문가모드' : '안내모드' }}</button>
        <button :disabled="isQuestionMode" class="utility-button" @click="resetChat">채팅초기화</button>
      </div>

      <div class="chat-input">
        <input v-model="userInput" :disabled="isQuestionMode" type="text" placeholder="질문을 입력하세요..." @keyup.enter="sendMessage" />
        <button :disabled="!userInput.trim() || isQuestionMode" @click="sendMessage">전송</button>
        <button v-if="isDebug" @click="makeSimulationAlert">테스트 경보</button>
      </div>
    </div>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { Doughnut } from 'vue-chartjs'
import { searchMessaging, errorMessaging1, errorMessaging2, errorMessaging3 } from '@/store/modules/chatbot.js'
import { getNiaRouteNameByPath, getNiaRouteTitleByPath, getSpanFormatMessageForDB, getMatchMapOfspanFormatMessage, isSpanFormatChatMessage } from '@/views-nia/js/commonNiaFunction'
import constants from '@/min/constants'
import EventBus from '@/utils/event-bus'
const routeName = 'chatbot'
/* eslint-disable */
export default {
  name: routeName,
  components: {
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
        const clonedData = JSON.parse(JSON.stringify(this.chartData))
        const clonedOptions = JSON.parse(JSON.stringify(this.options || {}))
        this.renderChart(clonedData, clonedOptions)
      },
      watch: {
        chartData: {
          deep: true,
          handler(newVal) {
            const clonedData = JSON.parse(JSON.stringify(newVal))
            const clonedOptions = JSON.parse(JSON.stringify(this.options || {}))
            this.renderChart(clonedData, clonedOptions)
          },
        },
        options: {
          deep: true,
          handler(newVal) {
            const clonedData = JSON.parse(JSON.stringify(this.chartData))
            const clonedOptions = JSON.parse(JSON.stringify(newVal || {}))
            this.renderChart(clonedData, clonedOptions)
          },
        },
      },
      methods: {
        optionUpdate() {
          const clonedData = JSON.parse(JSON.stringify(this.chartData))
          const clonedOptions = JSON.parse(JSON.stringify(this.options || {}))
          this.renderChart(clonedData, clonedOptions)
        },
        chartUpdate() {
          const clonedData = JSON.parse(JSON.stringify(this.chartData))
          const clonedOptions = JSON.parse(JSON.stringify(this.options || {}))
          this.renderChart(clonedData, clonedOptions)
        },
      },
    },
  },
  directives: { elDragDialog },
  mixins: [dialogOpenMixin],

  extends: Modal,
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
      chartData: {
        labels: [],
        datasets: [
          {
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
            data: [],
          },
        ],
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'SOP 조치내용 통계',
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
          },
        },
      },
    }
  },
  computed: {
    ...mapState({
      questionMode_chatMessages: (state) => state.chatbot.questionMode_chatMessages,
      alarmFocusMode_chatMessages: (state) => state.chatbot.alarmFocusMode_chatMessages,
      alarmFocusTicketData: (state) => state.chatbot.alarmFocusTicketData,
      alarmFocusSopDataList: (state) => state.chatbot.alarmFocusSopDataList,
      currentMode: (state) => state.chatbot.currentMode,
      actionType: (state) => state.chatbot.actionType,
      lastFocusModule: (state) => state.chatbot.lastFocusModule,
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
  },
  created() {
    this.selectedRow = this.wdata.params
    this.scrollToBottom()
  },

  mounted() {
    this.setDonutChartData()
  },

  methods: {
    setDonutChartData() {
      const accD = this.alarmFocusSopDataList.reduce((acc, d) => {
        if (!acc[d.fault_type]) {
          acc[d.fault_type] = []
        }
        acc[d.fault_type].push(d)
        return acc
      }, {})
      this.chartData.labels.length = 0
      this.chartData.datasets[0].data.length = 0

      Object.keys(accD).forEach((k) => {
        this.chartData.labels.push(k)
        this.chartData.datasets[0].data.push(accD[k].length)
      })

      console.log('Chart Data:', this.chartData)
      console.log('Chart Options:', this.chartOptions)
    },

    actionSwitch() {
      this.$store.commit('chatbot/SWTICH_ACTION')
    },

    resetChat() {
      this.$store.commit('chatbot/RESET_CHAT')
    },

    getTicketTypeHangle(ticketType) {
      switch (ticketType) {
        case 'ATT2':
          return '이상트래픽'
        case 'NTT':
          return '유해트래픽'
        case 'RT':
          return '장애'
        default:
          return ticketType
      }
    },

    openPopupDisabilityStatusHistoryManagement() {
      this.fn_openWindow('disabilityStatusHistoryManagement', ticketData)
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

    makeSimulationAlert() {
      EventBus.$emit('simulateTest', {})
    },

    openSop() {
      this.fn_openWindow('sopHistory', { isChatbotGenerated: true })
    },

    openConfigTest() {
      this.fn_openWindow('configTest', { isChatbotGenerated: true })
    },

    toggleIsActiveBotAlert() {
      this.isActiveBotAlert = !this.isActiveBotAlert
    },

    async sendMessage() {
      if (!this.userInput.trim()) return

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
      }
    },

    getLastAnswerObj() {
      const botAnswer = this.getCurrentChatMessageArray.filter((m) => {
        if (m.type !== constants.nia.chatType.botAnswer) return
        if (m.content.includes(searchMessaging)) return
        if (m.content.includes(errorMessaging1)) return
        if (m.content.includes(errorMessaging2)) return
        if (m.content.includes(errorMessaging3)) return
        return true
      })

      return botAnswer.at(-1)
    },

    runSpanAction(matchMap) {
      console.log('[matchMap] >> ' + 'path : ' + matchMap.path + ', popup :' + matchMap.popup + ', action :' + matchMap.action)

      let routerParameterTargetName = ''
      let text = ''
      if (matchMap.path.length > 0) {
        const routerName = getNiaRouteNameByPath(matchMap.path)
        if (this.$router.history.current.path === matchMap.path) {
          routerParameterTargetName = routerName
          text = `<br><br>` + `${constants.nia.chatbotIcon.noAction} ${getNiaRouteTitleByPath(matchMap.path)}화면에서 명령을 실행합니다.`
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
          text += `<br>${constants.nia.chatbotIcon.noAction} ${hasWindow.name} 팝업이 선택됩니다.`
          this.$store.dispatch('mdi/bringToFrontWindow', hasWindow.id)
          newName = hasWindow.chatbotParameterKeyName
        } else {
          this.fn_openWindow(matchMap.popup, { isChatbotGenerated: true })

          const dialogKey = Object.keys(this.dialogList).find((key) => key === matchMap.popup)
          text += `<br>${constants.nia.chatbotIcon.openPopup} ${this.dialogList[dialogKey].pageTitle} 팝업을 활성화했습니다. `
          newName = this.dialogList[dialogKey].chatbotParameterKeyName
        }

        routerParameterTargetName = newName
      }

      if (matchMap.action.length > 0) {
        setTimeout(() => {
          this.$store.commit('chatbot/SWITCH_ROUTER_PARAMETER', { name: routerParameterTargetName, parameter: matchMap.action })
        }, 1000)
      }

      if (text.length === 0) text += '\n\n명령 실행에 실패했습니다.'

      return text
    },

    async SearchAndAction(userQuestion) {
      const lastAnswerObj = this.getLastAnswerObj()

      if (isSpanFormatChatMessage(lastAnswerObj.content)) {
        let matchMap = getMatchMapOfspanFormatMessage(userQuestion, lastAnswerObj.content)
        if (matchMap) {
          const actionProcessMessage = this.runSpanAction(matchMap)
          return `<b>` + matchMap.matchContext + ' 명령을 실행했습니다.</b>' + actionProcessMessage
        }
      }

      const spanFormatMessage = await getSpanFormatMessageForDB(userQuestion)
      switch (this.actionType) {
        case constants.nia.chatbotActiontype.expert:
          this.$store.dispatch('chatbot/botPushAnswerMessage', {
            content: spanFormatMessage,
          })

          const matchMap = getMatchMapOfspanFormatMessage('1', spanFormatMessage)
          if (!matchMap) throw new Error('DB의 span 형식이 잘못되었네요')
          const actionProcessMessage = this.runSpanAction(matchMap)
          return `<b>` + matchMap.matchContext + ' 명령을 실행했습니다.</b>' + actionProcessMessage
        case constants.nia.chatbotActiontype.assist:
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
        // 줄바꿈을 <br> 태그로 변환하고 HTML 태그 제거
        let formattedContent = content.replace(/\n/g, '<br>') // 줄바꿈을 <br>로 변환

        formattedContent = formattedContent.replace(/(\s+)([^<]+)\s+<span class="move-text">\[진행\]<\/span>/g, '$1$2 <a href="#" class="move-link" data-keyword="$2">[진행]</a>')

        return formattedContent
      } catch (error) {
        console.error(error)
      }
    },

    async handlePathClick(event, content) {
      if (event.target.classList.contains('move-link')) {
        event.preventDefault()
        switch (event.target.innerHTML) {
          case '[진행]':
            // 집중경보 모드 전환
            const ticketMatch = content.match(/>티켓ID: (.*?)<\/span>/)
            const ticketId = ticketMatch ? ticketMatch[1] : null
            if (!ticketId) {
              this.$alert(`예상치 못한 에러 해당 장비에 ticketId가 존재하지 않습니다. 내용 : ${content}`)
              return
            }

            window.changeFocusAlertMode(ticketId)
            break
        }
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.chatbot {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */
}

::v-deep .chatbot-body {
  display: flex;
  flex-direction: column;
  height: 100%;

  span.chatbotIcon {
    font-size: 14px;
    background: black;
    height: 25px;
    width: 25px;
    border-radius: 25px;
    text-indent: -3px;
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
</style>
