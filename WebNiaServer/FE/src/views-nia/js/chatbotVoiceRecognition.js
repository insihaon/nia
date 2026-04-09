export class VoiceRecognition {
  constructor(vueInstance, options = {}) {
    this.vue = vueInstance
    this.options = {
      inputField: options.inputField || 'userInput',
      statusField: options.statusField || 'recognizing',
      lang: options.lang || 'ko-KR',
      autoSendDelay: options.autoSendDelay || 1000,
      ...options
    }

    this.recognition = null
    this.isRecognizing = false
    this.interimText = ''
    this.baseText = ''
    this.autoSendTimer = null

    // 숫자 변환 맵 (짧은 발음 대응)
    this.numberMap = {
      '일': '1', '하나': '1', '첫번째': '1', '일번': '1',
      '이': '2', '둘': '2', '둘번째': '2', '이번': '2',
      '삼': '3', '셋': '3', '셋번째': '3', '삼번': '3',
      '사': '4', '넷': '4', '넷번째': '4', '사번': '4',
      '오': '5', '다섯': '5', '다섯번째': '5', '오번': '5',
      '육': '6', '여섯': '6', '여섯번째': '6', '육번': '6',
      '칠': '7', '일곱': '7', '일곱번째': '7', '칠번': '7',
      '팔': '8', '여덟': '8', '여덟번째': '8', '팔번': '8',
      '구': '9', '아홉': '9', '아홉번째': '9', '구번': '9',
      '십': '10', '열': '10', '열번째': '10', '십번': '10'
    }

    this.exceptionMap = {
      '좋지': '조치'
    }

    this.fullException = { ...this.numberMap, ...this.exceptionMap }

    this.init()
    this.setupWatcher()
  }

  init() {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
    if (!SpeechRecognition) return

    this.recognition = new SpeechRecognition()
    this.recognition.lang = this.options.lang
    this.recognition.continuous = true
    this.recognition.interimResults = true
    // 이 부분을 추가하여 엔진이 더 작은 단위로 결과를 쪼개서 보내도록 유도합니다.
    this.recognition.maxAlternatives = 1

    this.recognition.onstart = () => this.handleState(true)
    this.recognition.onend = () => {
      if (this.isRecognizing) {
        this.recognition.start()
      } else {
        this.handleState(false)
      }
    }
    this.recognition.onresult = (e) => this.handleResult(e)
  }

  handleState(isStarted) {
    this.isRecognizing = isStarted
    this.vue[this.options.statusField] = isStarted
    if (isStarted) {
      this.baseText = this.vue[this.options.inputField] || ''
    }
  }

  handleResult(event) {
    let interim = ''
    for (let i = event.resultIndex; i < event.results.length; i++) {
      const result = event.results[i]
      let transcript = result[0].transcript.trim()

      if (result.isFinal) {
        // [개선] 확정된 짧은 단어가 숫자 관련일 경우 즉시 변환
        transcript = this.parseNumericCommand(transcript)
        this.baseText += transcript + ' '
      } else {
        interim += transcript
      }
    }
    this.interimText = interim

    // 화면 반영
    const fullText = (this.baseText + this.interimText).trim()
    this.vue[this.options.inputField] = fullText

    if (fullText.length > 0) {
      this.resetAutoSendTimer()
    }
  }

  /**
   * 숫자로 시작하는 짧은 명령어를 '1번' 형태로 강제 변환
   */
  parseNumericCommand(text) {
    // "일 번", "이 번" 처럼 띄어쓰기 된 경우 붙여줌
    let processed = text.replace(/\s(번|번째)/g, '$1')

    for (const [key, value] of Object.entries(this.fullException)) {
      // 문장이 해당 숫자로 시작하거나, 그 숫자 자체일 때
      if (processed.startsWith(key)) {
        // "일번" -> "1번", "일" -> "1"
        processed = processed.replace(new RegExp(`^${key}`), value)
        break
      }
    }
    return processed
  }

  resetAutoSendTimer() {
    this.clearAutoSendTimer()
    if (this.isRecognizing && this.options.autoSendDelay > 0) {
      this.autoSendTimer = setTimeout(() => {
        const val = this.vue[this.options.inputField]
        if (val && val.trim().length > 0) {
          this.clear()
          if (typeof this.vue.sendMessage === 'function') {
            this.vue.sendMessage()
            this.toggle()
          }
        }
      }, this.options.autoSendDelay)
    }
  }

  clearAutoSendTimer() {
    if (this.autoSendTimer) {
      clearTimeout(this.autoSendTimer)
      this.autoSendTimer = null
    }
  }

  clear() {
    this.baseText = ''
    this.interimText = ''
    this.clearAutoSendTimer()
  }

  toggle() {
    if (this.isRecognizing) {
      this.isRecognizing = false
      this.recognition.stop()
    } else {
      this.recognition.start()
    }
  }

  setupWatcher() {
    this.vue.$watch(this.options.inputField, (newVal) => {
      if (!this.isRecognizing) this.baseText = newVal || ''
    })
  }

  destroy() {
    this.isRecognizing = false
    if (this.recognition) this.recognition.stop()
    this.clear()
  }
}
