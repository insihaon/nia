<template>
  <div ref="chatbot-total" :class="{ [name]: true, 'w-full h-full': true }">
    <div class="chatbot-body">
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
              v-loading="mailSendingLoading && message.content.includes('chatbot-mail-send-btn')"
              class="message-content"
              :class="{ 'mail-content-loading': mailSendingLoading && message.content.includes('chatbot-mail-send-btn') }"
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
        <button :disabled="isQuestionMode" class="utility-button" @click="isTest">간편 상황전파</button>
      </div>

      <div class="chat-input">
        <input v-model="userInput" :disabled="isQuestionMode" type="text" placeholder="질문을 입력하세요..." @keyup.enter="sendMessage" />
        <button :disabled="!userInput.trim() || isQuestionMode" @click="sendMessage">전송</button>
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
import {
  collectRequestContentData,
  generateRequestContentHtml,
  handleSendEmail,
  decodeBase64Html,
  encodeBase64Html,
} from './logic/requestForActionLogic'
import { apiSendMQ, apiSelectUserList } from '@/api/nia'

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
      userList: [],
      selectedUsers: null, // 단일 선택이므로 객체 또는 null
      mailContentHtml: '',
      mailContentData: null,
      mailSendingLoading: false,
      mailContentBodyHtml: '', // 메일 본문 HTML (button/select 제외)
      mailButtonContainerHtml: '', // button과 select 컨테이너 HTML (재사용용)
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
    if (this.isDebug) {
      hotkeys(`alt+q`, (e, h) => {
        if (chatbotPopup) {
          // 챗봇 팝업 앞으로
          this.allWaysFrontWindowChatbot()
        }
      })
      hotkeys(`alt+w`, (e, h) => {
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
        if (chatbotPopup) {
          // 채팅초기화
          this.resetChat()
        }
      })
      hotkeys(`alt+v`, (e, h) => {
        if (chatbotPopup) {
          // voice ON/OFF
          this.switchVoiceRecording()
        }
      })
      hotkeys(`alt+l`, (e, h) => {
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
  },

  beforeDestroy() {
    // 컴포넌트 파괴 시 음성인식 리소스 정리
    if (this.voiceRecognition) {
      this.voiceRecognition.destroy()
    }
  },

  methods: {
    async isTest() {
      try {
        // 사용자 목록이 없으면 로드
        if (!this.userList || this.userList.length === 0) {
          await this.onLoadUserList()
        }

        // 테스트용 데이터 생성 (현재 선택된 티켓 데이터 사용 또는 기본값)
        const selectedRow = this.alarmFocusTicketData || {
          ticket_id: 'TEST_TICKET_001',
          ticket_type: 'ATT2',
          fault_time: new Date().toISOString(),
          ticket_result: '테스트 장애',
          sender: '테스트 사용자',
        }

        const userInfo = this.$store.state.user.info || {
          uid: 'test_user',
          name: '테스트 사용자',
          agencyName: '테스트 기관',
        }

        // 데이터 수집
        const collectedData = await collectRequestContentData({
          selectedRow,
          userInfo,
          options: {
            loadTrafficInfo: !['SYSLOG', 'RT'].includes(selectedRow.ticket_type),
            loadSyslogInfo: selectedRow.ticket_type === 'SYSLOG',
            loadSopHist: true,
            loadAiDetection: true,
          },
        })

        // 시스템 URL 생성 (간단한 테스트용)
        const mailToSystemUrl = '#'

        // HTML 생성 (버튼 포함 - select 박스는 템플릿에서 직접 표시)
        const contentWithButton = generateRequestContentHtml({
          data: collectedData,
          selectedUsers: this.selectedUsers ? [this.selectedUsers] : [], // 단일 값을 배열로 변환
          mailToSystemUrl,
          formatterTimeStamp: (time, formatStr = 'YYYY-MM-DD HH:mm:ss') => {
            if (!time) return ''
            // Modal을 extends하므로 moment 사용 가능
            return this.moment(time).format(formatStr)
          },
          includeButton: true,
          buttonOptions: {
            className: 'chatbot-mail-send-btn',
            wrapperClass: 'chatbot-message-body',
          },
          includeSelect: true, // select 박스는 템플릿에서 직접 표시
          userList: this.userList,
        })

        // 본문과 button/select 컨테이너 분리
        const buttonContainerMatch = contentWithButton.match(/<div class="chatbot-message-body"[^>]*>[\s\S]*?<\/div>\s*$/)
        if (buttonContainerMatch) {
          this.mailButtonContainerHtml = buttonContainerMatch[0]
          this.mailContentBodyHtml = contentWithButton.replace(buttonContainerMatch[0], '')
        } else {
          this.mailContentBodyHtml = contentWithButton
          this.mailButtonContainerHtml = ''
        }

        // 메일 내용 및 데이터 저장
        this.mailContentHtml = contentWithButton
        this.mailContentData = collectedData

        // 메시지 전송
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: contentWithButton,
        })

        // HTML에 포함된 select 박스 동적 마운트
        this.$nextTick(() => {
          this.bindSelectChangeEvent()
        })
      } catch (error) {
        console.error('isTest 오류:', error)
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: `<div class="chatbot-message-body">테스트 실행 중 오류가 발생했습니다: ${error.message}</div>`,
        })
      }
    },

    async handleMailSendClick(event) {
      event.preventDefault()
      const button = event.target.closest('.chatbot-mail-send-btn')
      if (!button) return

      // base64 인코딩된 HTML 디코딩
      const encodedHtml = button.getAttribute('data-html-content')
      if (!encodedHtml) {
        this.$alert('메일 내용을 찾을 수 없습니다.', '알림', {
          confirmButtonText: '확인',
        })
        return
      }

      const htmlContent = decodeBase64Html(encodedHtml)

      // 선택된 사용자가 없으면 알림
      if (!this.selectedUsers) {
        this.$alert('담당 직원을 선택해주세요.', '알림', {
          confirmButtonText: '확인',
        })
        return
      }

      // 티켓 정보
      const ticketInfo = this.alarmFocusTicketData || {
        ticket_id: 'TEST_TICKET_001',
        ticket_type: 'ATT2',
        ticket_result: '테스트 장애',
      }

      // 공통 메일 전송 함수 사용 (단일 선택이지만 배열로 변환)
      await handleSendEmail({
        htmlContent,
        receiverUsers: [this.selectedUsers], // 단일 값을 배열로 변환
        ticketInfo,
        userInfo: this.$store.state.user.info,
        isBase64Encoded: false,
        apiSendMQ,
        onLoading: () => {
          this.mailSendingLoading = true
        },
        onSuccess: (res) => {
          this.mailSendingLoading = false
          this.$alert(`메일 전송에 ${res.success ? '성공' : '실패'} 하였습니다.`, '알림', {
            confirmButtonText: '확인',
          })

          // 성공 메시지 표시
          if (res.success) {
            this.$store.dispatch('chatbot/botPushAnswerMessage', {
              content: `<div class="chatbot-message-body">메일이 성공적으로 전송되었습니다.</div>`,
            })
          }
        },
        onError: (error) => {
          this.mailSendingLoading = false
          console.error('메일 전송 오류:', error)
          this.$alert(error.message || '메일 전송 중 오류가 발생했습니다.', '오류', {
            confirmButtonText: '확인',
          })
        },
      })
    },

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

    async onLoadUserList() {
      try {
        const res = await apiSelectUserList()
        this.userList = res?.result ?? []
      } catch (error) {
        console.error('사용자 목록 로드 실패:', error)
      }
    },

    onSelectedUsersChange() {
      // 선택된 사용자 변경 시 HTML 업데이트
      if (this.mailContentData) {
        this.updateMailContentHtml()
      }
    },

    bindSelectChangeEvent() {
      // HTML에 포함된 select 박스를 동적으로 마운트
      this.$nextTick(() => {
        const selectWrappers = document.querySelectorAll('.chatbot-user-select-wrapper')
        selectWrappers.forEach((wrapper) => {
          // 기존에 마운트된 el-select가 있으면 완전히 제거 (el-tag 문제 방지)
          const existingSelect = wrapper.querySelector('.el-select')
          if (existingSelect) {
            // Vue 인스턴스가 있으면 제거
            if (existingSelect.__vue__) {
              const vueInstance = existingSelect.__vue__
              if (vueInstance.$parent) {
                vueInstance.$parent.$destroy()
              }
            }
            // DOM에서 제거
            existingSelect.remove()
          }

          // data 속성에서 데이터 가져오기
          const userListEncoded = wrapper.getAttribute('data-user-list')
          const selectedUsersEncoded = wrapper.getAttribute('data-selected-users')

          if (!userListEncoded || !selectedUsersEncoded) {
            return
          }

          try {
            // Base64 디코딩
            const userListJson = decodeBase64Html(userListEncoded)
            const selectedUsersJson = decodeBase64Html(selectedUsersEncoded)
            const userList = JSON.parse(userListJson)
            const selectedUserEmails = JSON.parse(selectedUsersJson)

            // 선택된 사용자 객체 (단일 선택이므로 첫 번째 값만 사용)
            const selectedUser = selectedUserEmails.length > 0
              ? userList.find((user) => user.email === selectedUserEmails[0])
              : null

            // Vue 컴포넌트 동적 생성
            // eslint-disable-next-line vue/one-component-per-file
            const SelectComponent = this.$root.constructor.extend({
              data() {
                return {
                  localSelectedUser: selectedUser,
                  localUserList: userList,
                }
              },
              render(h) {
                return h(
                  'el-select',
                  {
                    props: {
                      value: this.localSelectedUser,
                      multiple: false, // 단일 선택으로 명시
                      filterable: false,
                      size: 'mini',
                      placeholder: '담당 직원을 선택하세요',
                      clearable: true, // 선택 해제 가능
                    },
                    style: {
                      width: '100%',
                    },
                    on: {
                      input: (value) => {
                        this.localSelectedUser = value
                        // 부모 컴포넌트의 selectedUsers 업데이트 (단일 값)
                        if (this.$parent && this.$parent.selectedUsers !== undefined) {
                          this.$parent.selectedUsers = value
                          this.$parent.updateMailContentHtml()
                        }
                      },
                    },
                  },
                  this.localUserList.map((user) => {
                    return h('el-option', {
                      key: user.email, // key 명시적으로 설정
                      props: {
                        label: `${user.name} (${user.email})`,
                        value: user,
                      },
                      scopedSlots: {
                        default: () => [
                          h('span', { style: { float: 'left' } }, user.name),
                          h('span', { style: { float: 'right', color: '#8492a6', fontSize: '13px' } }, user.email),
                        ],
                      },
                    })
                  })
                )
              },
            })

            // 새 Vue 인스턴스 생성 및 마운트
            const selectInstance = new SelectComponent({
              parent: this,
            })
            selectInstance.$mount()
            wrapper.appendChild(selectInstance.$el)
          } catch (error) {
            console.error('Select 컴포넌트 마운트 실패:', error)
          }
        })
      })
    },

    updateMailContentHtml() {
      if (!this.mailContentData) return

      // 본문만 재생성 (button/select 컨테이너는 재사용)
      const mailToSystemUrl = '#'
      const bodyHtml = generateRequestContentHtml({
        data: this.mailContentData,
        selectedUsers: this.selectedUsers ? [this.selectedUsers] : [], // 단일 값을 배열로 변환
        mailToSystemUrl,
        formatterTimeStamp: (time, formatStr = 'YYYY-MM-DD HH:mm:ss') => {
          if (!time) return ''
          return this.moment(time).format(formatStr)
        },
        includeButton: false, // button/select는 재사용하므로 생성하지 않음
        includeSelect: false,
        userList: [],
      })

      // 본문과 button/select 컨테이너 결합
      const updatedHtml = bodyHtml + this.mailButtonContainerHtml
      this.mailContentHtml = updatedHtml
      this.mailContentBodyHtml = bodyHtml

      // 채팅 메시지 업데이트 (마지막 메시지가 메일 내용인 경우)
      this.$nextTick(() => {
        const messages = this.alarmFocusMode_chatMessages
        if (messages.length > 0 && messages[messages.length - 1].content.includes('chatbot-mail-send-btn')) {
          // 마지막 메시지 내용 업데이트
          const lastMessage = messages[messages.length - 1]
          lastMessage.content = updatedHtml
          // select 박스 재마운트 (el-tag 문제 방지를 위해 완전히 재생성)
          this.bindSelectChangeEvent()
        }
      })
    },

    updateSelectSelectedUsers() {
      // select 박스의 data-selected-users 속성만 업데이트
      this.$nextTick(() => {
        const selectWrappers = document.querySelectorAll('.chatbot-user-select-wrapper')
        selectWrappers.forEach((wrapper) => {
          // 단일 선택이므로 이메일 배열로 변환 (기존 로직과 호환)
          const selectedUserEmails = this.selectedUsers ? [this.selectedUsers.email] : []
          const selectedUsersJson = JSON.stringify(selectedUserEmails)
          const selectedUsersEncoded = encodeBase64Html(selectedUsersJson)
          wrapper.setAttribute('data-selected-users', selectedUsersEncoded)

          // 이미 마운트된 el-select가 있으면 값 업데이트
          const selectInstance = wrapper.querySelector('.el-select')
          if (selectInstance && selectInstance.__vue__) {
            const vueInstance = selectInstance.__vue__
            if (vueInstance.$parent && vueInstance.$parent.localSelectedUser !== undefined) {
              vueInstance.$parent.localSelectedUser = this.selectedUsers
            }
          }
        })

        // button의 data-html-content도 업데이트 (본문이 변경되었으므로)
        const buttons = document.querySelectorAll('.chatbot-mail-send-btn')
        buttons.forEach((button) => {
          const encodedHtml = encodeBase64Html(this.mailContentHtml)
          const safeEncodedHtml = encodedHtml.replace(/"/g, '&quot;').replace(/'/g, '&#39;')
          button.setAttribute('data-html-content', safeEncodedHtml)
        })
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

          this.fn_openWindow(popupName, { isChatbotGenerated: true })

          const dialogKey = Object.keys(this.dialogList).find((key) => key === matchMap.popup)
          text += `<br>${constants.nia.chatbotIcon.openPopup} ${this.dialogList[dialogKey].pageTitle} 팝업을 활성화했습니다. `
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
        // 이미 HTML이 포함된 경우 (chatbot-command-header, chatbot-mail-send-btn 등이 있는 경우) 그대로 반환
        if (content.includes('chatbot-command-header') || content.includes('chatbot-mail-send-btn')) {
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
      // select 박스 클릭은 이벤트 전파 중단
      if (event.target.closest('.chatbot-user-select-wrapper') || event.target.classList.contains('chatbot-user-select')) {
        event.stopPropagation()
        return
      }

      // 메일 전송 버튼 클릭 처리
      if (event.target.classList.contains('chatbot-mail-send-btn')) {
        await this.handleMailSendClick(event)
        return
      }

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

::v-deep .chatbot-mail-send-btn {
  background-color: #409EFF;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  margin-top: 10px;
  transition: background-color 0.3s;
}

::v-deep .chatbot-mail-send-btn:hover {
  background-color: #66b1ff;
}

::v-deep .chatbot-mail-send-btn:active {
  background-color: #3a8ee6;
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
