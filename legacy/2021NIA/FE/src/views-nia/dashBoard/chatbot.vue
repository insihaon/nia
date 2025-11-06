<template>
  <div ref="chatbot-total" :class="{ [name]: true, 'w-full h-full': true }">
    <div class="chatbot-body">
      <div class="chat-header">
        <h3>
          어시스턴트 <span :style="{ color: isQuestionMode ? 'red' : 'green' }">[{{ isQuestionMode ? 'OFF' : 'ON' }}]</span>
        </h3>
        <div v-if="isQuestionMode">질문을 입력하고 답변을 받아보세요</div>
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
        <DoughnutChart v-if="alarmFocusSopDataList.length > 0" ref="donutChart" class="chatbot-donut-chart" :chart-data="chartData" :options="chartOptions" />
        <div v-for="(message, index) in alarmFocusMode_chatMessages" :key="index" :class="['message', message.type]">
          <div v-if="index === 0" class="message-content sopAnalysisBody">
            <div v-if="alarmFocusSopDataList.length > 0">
              <div style="background-color: #1e293b; font-weight: 600; text-align: center; color: white">{{ alarmFocusTicketData.node_nm }} 장비에 대한 SOP 조치내용 통계</div>
              <table class="sop-stats-table">
                <tbody>
                  <tr>
                    <td>전체SOP</td>
                    <td>{{ alarmFocusSopDataList.length }}개</td>
                  </tr>
                  <tr v-for="(label, index2) in chartData.labels" :key="label">
                    <td>{{ label }}</td>
                    <td>{{ chartData.datasets[0].data[index2] }}개</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div v-else>
              <div style="background-color: #ee6666; font-weight: 600; text-align: center; color: white">{{ warningSignText }} {{ alarmFocusTicketData.node_nm }} SOP 통계 미보유 {{ warningSignText }}</div>
              <br />
              SOP이력이 없어서 통계 데이터가 제공되지 않습니다.
            </div>
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
        <!-- <button :disabled="isQuestionMode" class="utility-button" :style="{ 'background-color': isRecording ? '#ff4949' : '#e5e7eb' }" @click="switchVoiceRecording">음성인식({{ isRecording ? 'ON' : 'OFF' }})</button> -->
        <button :disabled="isQuestionMode" class="utility-button" :style="{ 'background-color': recognizing ? '#ff4949' : '#e5e7eb' }" @click="toggleWebSpeechApiRecognition">음성인식 WSA({{ recognizing ? 'ON' : 'OFF' }})</button>
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
import { Doughnut } from 'vue-chartjs'
import { searchMessaging, errorMessaging1, errorMessaging2, errorMessaging3 } from '@/store/modules/chatbot.js'
import { getChatbotMdiObject, getNiaRouteNameByPath, getNiaRouteTitleByPath, getSpanFormatMessageForDB, getMatchMapOfspanFormatMessage, isSpanFormatChatMessage } from '@/views-nia/js/commonNiaFunction'
import { apiIpAlarmList } from '@/api/nia'
import constants from '@/min/constants'
import hotkeys from 'hotkeys-js'

const routeName = 'chatbot'

const centerTextPlugin = {
  id: 'centerTextPlugin', // 플러그인 ID
  beforeDraw: (chart) => {
    const total = chart.data.datasets[0].data.reduce((a, b) => a + b, 0)

    if (total > 0) {
      const { ctx, width, height } = chart
      ctx.restore()

      // 1. 텍스트 스타일 설정
      const fontSize = (height / 150).toFixed(2)
      ctx.font = `${fontSize}em sans-serif`
      ctx.textBaseline = 'middle'
      ctx.fillStyle = '#333' // 텍스트 색상

      // 2. 텍스트 내용 정의
      const totalLabel = '전체'
      const totalValue = total.toLocaleString() + '개'

      // 3. 중앙 좌표 계산
      const centerX = width / 2 + 62
      const centerY = height / 2 + 15

      // 4. 레이블 그리기 (윗줄)
      let text = totalLabel
      let textX = Math.round(centerX - ctx.measureText(text).width / 2)
      ctx.fillText(text, textX, centerY - 5) // 윗줄 렌더링

      // 5. 값 그리기 (아랫줄)
      text = totalValue
      ctx.font = `${(fontSize * 0.9).toFixed(2)}em sans-serif`
      textX = Math.round(centerX - ctx.measureText(text).width / 2)
      ctx.fillText(text, textX, centerY + 15) // 아랫줄 렌더링

      ctx.save()
    }
  },
}

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
      centerTextPlugin: centerTextPlugin,
      chartData: {
        labels: [],
        datasets: [
          {
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', 'gray'],
            data: [0, 0, 0, 0],
          },
        ],
      },
      /* --- 음성인식 Start --- */
      mediaRecorder: null,
      audioChunks: [],
      audioStream: null, // 마이크에서 가져온 스트림
      isRecording: false,
      transcribedText: '여기에 텍스트가 표시됩니다.', // UI 표시용
      /* --- 음성인식 End --- */

      /* 음성인식 Web Speeach API Start*/
      recognizing: false,
      recognition: null,
      recognitionTimeoutTimer: null,
      bufferText: '',
      /* 음성인식 Web Speeach API End*/
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
                    // 라벨에 개수 추가 (예: '포트다운: 5개')
                    text: label + ': ' + counts[i] + '개',
                    fillStyle: data.datasets[0].backgroundColor[i],
                    hidden: isNaN(counts[i]) || counts[i] === 0,
                    // Chart.js가 요구하는 기타 속성들...
                    // lineCap: data.datasets[0].lineCap,
                    // lineWidth: data.datasets[0].lineWidth,
                    // strokeStyle: data.datasets[0].borderColor[i],
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
      questionMode_chatMessages: (state) => state.chatbot.questionMode_chatMessages,
      alarmFocusMode_chatMessages: (state) => state.chatbot.alarmFocusMode_chatMessages,
      alarmFocusTicketData: (state) => state.chatbot.alarmFocusTicketData,
      alarmFocusSopDataList: (state) => state.chatbot.alarmFocusSopDataList,
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
  },
  created() {
    this.selectedRow = this.wdata.params
    this.scrollToBottom()

    const chatbotPopup = getChatbotMdiObject()
    if (this.isDebug) {
      hotkeys(`alt+q`, (e, h) => {
        if (chatbotPopup) {
          this.$store.dispatch('mdi/bringToFrontWindow', chatbotPopup.id)
        }
      })
      hotkeys(`alt+w`, (e, h) => {
        if (chatbotPopup) {
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
    }
  },

  mounted() {
    this.setDonutChartData()

    this.makeWebSpeechApiObj()
  },

  methods: {
    makeWebSpeechApiObj() {
      const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
      if (SpeechRecognition) {
        this.recognition = new SpeechRecognition()
        this.recognition.lang = 'ko-KR'
        this.recognition.interimResults = true
        this.recognition.continuous = true
      } else {
        alert('이 브라우저는 음성인식을 지원하지 않습니다.')
      }
    },

    toggleWebSpeechApiRecognition() {
      if (!this.recognizing) this.startRecognition()
      else this.stopRecognition()
    },

    resetSilenceTimer() {
      if (this.recognitionTimeoutTimer) clearTimeout(this.recognitionTimeoutTimer)
      this.recognitionTimeoutTimer = setTimeout(() => {
        if (this.bufferText.trim() !== '') {
          this.userInput = this.bufferText.trim()
          this.bufferText = ''
          this.sendMessage()
        }
      }, 1000)
    },
    startRecognition() {
      this.bufferText = ''
      this.recognition.start()
      this.recognizing = true

      this.recognition.onstart = () => {
        console.log('음성인식 시작')
      }

      this.recognition.onresult = (event) => {
        const result = event.results[event.results.length - 1][0].transcript
        this.bufferText = result
        this.resetSilenceTimer()
      }

      this.recognition.onend = () => {
        if (this.recognizing) this.recognition.start() // 종료 후 자동재시작
      }

      this.recognition.onerror = (event) => {
        console.error('Speech error:', event.error)
      }
    },

    stopRecognition() {
      this.recognizing = false
      this.recognition.stop()
      if (this.recognitionTimeoutTimer) clearTimeout(this.recognitionTimeoutTimer)

      if (this.bufferText.trim() !== '') {
        alert(this.bufferText.trim())
      }
      this.bufferText = ''
    },

    async loadMediaRecorder() {
      try {
        // 표준 API: 사용자에게 마이크 접근 권한을 요청합니다.
        this.audioStream = await navigator.mediaDevices.getUserMedia({ audio: true })

        // MediaRecorder 초기화: 스트림을 인자로 받습니다.
        this.mediaRecorder = new MediaRecorder(this.audioStream)

        // 녹음 데이터가 들어올 때마다 chunks 배열에 추가합니다.
        this.mediaRecorder.ondataavailable = (event) => {
          this.audioChunks.push(event.data)
        }

        // 녹음이 중지되었을 때 최종적으로 실행할 콜백을 설정합니다.
        this.mediaRecorder.onstop = () => {
          this.handleRecordingStop()
        }

        // ⭐ VAD 로직 추가 ⭐
        this.vadContext = new (window.AudioContext || window.webkitAudioContext)()
        this.vadSource = this.vadContext.createMediaStreamSource(this.audioStream)
        this.vadAnalyzer = this.vadContext.createAnalyser()

        // 분석기 설정
        this.vadAnalyzer.fftSize = 256
        this.vadSource.connect(this.vadAnalyzer)

        // VAD 분석 루프 시작
        this.checkVoiceActivity()
      } catch (error) {
        console.error('마이크 접근 또는 초기화 실패:', error)
        this.transcribedText = '마이크 접근 권한이 필요합니다. 브라우저 설정을 확인하세요.'
      }
    },

    // [methods에 VAD 분석 루프 추가]
    checkVoiceActivity() {
      if (!this.vadAnalyzer) return

      // 분석할 빈도 데이터를 담을 배열
      const bufferLength = this.vadAnalyzer.frequencyBinCount
      const dataArray = new Uint8Array(bufferLength)

      // 주파수 영역 데이터를 얻습니다.
      this.vadAnalyzer.getByteFrequencyData(dataArray)

      // 전체 소리 레벨 계산 (간단화: 배열 요소의 평균)
      const average = dataArray.reduce((acc, val) => acc + val, 0) / bufferLength
      // 0-255 사이의 값을 0-1 사이의 값으로 정규화
      const normalizedVolume = average / 255

      // 녹음 중일 때만 침묵 감지 로직 실행
      if (this.isRecording) {
        if (normalizedVolume < this.SILENCE_THRESHOLD) {
          // 소리가 임계값 이하일 때 타이머 시작/유지
          if (!this.silenceTimer) {
            this.silenceTimer = setTimeout(() => {
              console.log('자동 종료: 3초 이상 침묵 감지.')
              this.stopRecording() // 자동 종료 및 전송
            }, this.SILENCE_DURATION)
          }
        } else {
          // 소리가 감지되면 타이머 초기화
          clearTimeout(this.silenceTimer)
          this.silenceTimer = null
        }
      }

      // 다음 프레임에서 이 함수를 다시 호출하여 루프 유지
      requestAnimationFrame(this.checkVoiceActivity)
    },

    /**
     * 녹음을 시작하고 상태를 업데이트합니다.
     */
    async startRecording() {
      // 마이크 스트림이 없다면 다시 로드 (stopRecording에서 해제되었을 경우)
      if (!this.audioStream) {
        await this.loadMediaRecorder()
      }

      if (!this.mediaRecorder || this.mediaRecorder.state === 'recording') return

      this.audioChunks = []
      this.mediaRecorder.start()

      this.isRecording = true
      this.transcribedText = '녹음 중... (3초 침묵 시 자동 전송)'

      // 녹음 시작과 동시에 VAD 타이머를 초기화하여 즉시 종료되는 것을 방지
      clearTimeout(this.silenceTimer)
      this.silenceTimer = null
    },

    /**
     * 녹음을 중지하고 onstop 이벤트를 트리거합니다.
     */
    stopRecording() {
      if (this.mediaRecorder && this.mediaRecorder.state === 'recording') {
        this.mediaRecorder.stop()
        this.isRecording = false
        this.transcribedText = '녹음 완료, 서버로 전송 준비 중...'

        // ⭐ 마이크 스트림 비활성화 ⭐
        if (this.audioStream) {
          this.audioStream.getTracks().forEach((track) => track.stop())
          this.audioStream = null
        }
      }
    },

    /**
     * 녹음 중지 후 호출되며, Blob을 생성하고 서버로 전송합니다.
     */
    handleRecordingStop() {
      this.sendToPythonServer()
    },

    // --- Blob 데이터를 파일로 다운로드하는 도우미 함수 ---
    downloadBlob(blob, filename) {
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.style.display = 'none'
      a.href = url
      a.download = filename // 파일 이름 지정
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url) // 메모리 해제
      document.body.removeChild(a)
    },

    /**
     * 녹음 데이터를 Python 서버로 전송하고 응답을 처리합니다.
     */
    async sendToPythonServer() {
      const mimeType = this.mediaRecorder.mimeType || 'audio/wav'
      const audioBlob = new Blob(this.audioChunks, { type: mimeType })

      if (audioBlob.size === 0) {
        this.transcribedText = '오디오 데이터가 녹음되지 않았습니다.'
        return
      }

      const formData = new FormData()
      // 서버에서 webm;codecs=opus와 같은 확장자를 처리하기 위해 순수 확장자만 추출하는 로직 유지
      const fileExtension = mimeType.split('/')[1].split(';')[0] || 'wav'
      formData.append('audio_file', audioBlob, `recording.${fileExtension}`)

      const URL = 'http://incodej-lab.iptime.org:25000/api/stt_process'

      try {
        this.transcribedText = '서버로 전송 중...'

        // 1. 단일 요청 실행 (await를 사용하여 동기적으로 처리)
        const response = await fetch(URL, {
          method: 'POST',
          body: formData, // FormData 객체 (오디오 파일 포함)
        })

        // 2. HTTP 상태 코드 확인 (400, 500 에러 처리)
        if (!response.ok) {
          // 서버에서 보낸 JSON 응답을 읽어 에러 메시지를 추출하려고 시도
          let errorData = {}
          try {
            errorData = await response.json()
          } catch {
            // JSON 형식이 아닐 경우 무시
          }

          const errorMessage = errorData.error || `HTTP 요청 실패, 상태 코드: ${response.status}`
          throw new Error(errorMessage)
        }

        // 3. 응답 스트림에서 JSON 데이터 추출
        const data = await response.json()

        // 4. 데이터 사용
        const recognizedText = data.text

        // UI 업데이트
        this.transcribedText = recognizedText
        console.log('인식된 텍스트:', recognizedText)
      } catch (error) {
        console.error('STT 서버 통신 오류:', error)
        // 사용자에게 오류 메시지 표시
        this.transcribedText = `오류 발생: ${error.message}`
      }
    },

    switchVoiceRecording() {
      if (this.isRecording) {
        this.isRecording = false
        this.stopRecording()
      } else {
        this.isRecording = true
        this.startRecording()
      }
    },

    setDonutChartData() {
      // faultType별 개수 계산
      const faultTypeCount = {}
      this.alarmFocusSopDataList.forEach((data) => {
        const fault_detail_content = data.fault_detail_content
        faultTypeCount[fault_detail_content] = (faultTypeCount[fault_detail_content] || 0) + 1
      })

      // 개수 기준으로 내림차순 정렬하여 상위 3개 추출
      const sortedFaultTypes = Object.entries(faultTypeCount)
        .sort(([, a], [, b]) => b - a)
        .slice(0, 3)

      // 상위 3개 faultType과 기타 개수 계산
      const top3FaultTypes = sortedFaultTypes.map(([fault_detail_content]) => fault_detail_content)
      const top3Counts = sortedFaultTypes.map(([, count]) => count)

      // 기타 개수 계산 (상위 3개가 아닌 나머지)
      const othersCount = Object.entries(faultTypeCount)
        .filter(([fault_detail_content]) => !top3FaultTypes.includes(fault_detail_content))
        .reduce((sum, [, count]) => sum + count, 0)

      // chartData 업데이트
      this.chartData.labels = [...top3FaultTypes, '기타']
      this.chartData.datasets[0].data = [...top3Counts, othersCount]

      // 색상도 동적으로 설정 (기본 색상 + 기타용 회색)
      const colors = ['#FF6384', '#36A2EB', '#FFCE56', 'gray']
      this.chartData.datasets[0].backgroundColor = colors.slice(0, this.chartData.labels.length)
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
        const matchMap = getMatchMapOfspanFormatMessage(userQuestion, lastAnswerObj.content)
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

        if (event.target.innerHTML === '[진행]') {
          // 집중경보 모드 전환
          const ticketMatch = content.match(/>티켓종류: (.*?)<\/span>/)
          const ticketType = ticketMatch ? ticketMatch[1] : null
          if (!ticketType) {
            this.$alert(`예상치 못한 에러 해당 장비에 ticketType이 존재하지 않습니다. 내용 : ${content}`, '오류', {
              confirmButtonText: '실행',
              cancelButtonText: '취소',
              customClass: 'nia-message-box',
            })
            return
          }

          let ticketId = null
          let alarmno = null

          if (ticketType === 'SYSLOG') {
            const alarmMatch = content.match(/>알람번호: (.*?)<\/span>/)
            alarmno = alarmMatch ? alarmMatch[1] : null
            if (!alarmno) {
              this.$alert(`예상치 못한 에러 해당 장비에 alarmno가 존재하지 않습니다. 내용 : ${content}`, '오류', {
                confirmButtonText: '실행',
                cancelButtonText: '취소',
                customClass: 'nia-message-box',
              })
              return
            }
          } else {
            const ticketMatch = content.match(/>티켓ID: (.*?)<\/span>/)
            ticketId = ticketMatch ? ticketMatch[1] : null
            if (!ticketId) {
              this.$alert(`예상치 못한 에러 해당 장비에 ticketId가 존재하지 않습니다. 내용 : ${content}`, '오류', {
                confirmButtonText: '실행',
                cancelButtonText: '취소',
                customClass: 'nia-message-box',
              })
              return
            }
          }

          const res = await apiIpAlarmList({
            TICKET_ID: ticketId,
            ALARMNO: alarmno,
            IS_TEST: true,
          })

          if (res && res.result.length > 0) {
            const ticket = res.result[0]
            window.changeFocusAlertMode(ticket)
          } else {
            this.$alert('해당 티켓에 대하여 집중경보 활성화에 실패하였습니다.', '오류', {
              confirmButtonText: '실행',
              cancelButtonText: '취소',
              customClass: 'nia-message-box',
            })
          }
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

::v-deep .chatbot-donut-chart {
  border: none;
  background-color: #fcfcfc; /* 아주 옅은 배경색 */
  border-radius: 12px; /* 부드러운 모서리 */
  padding: 0px 10px; /* 차트와 주변 여백 확보 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 은은한 그림자 효과 */
  height: 200px;
  width: 400px;
}

::v-deep .chatbot-command-header {
  background-color: #1e293b;
  font-weight: 600;
  text-align: center;
  color: white;
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
</style>
