export class VoiceRecognition {
  constructor(options = {}) {
    // 옵션 설정
    this.options = {
      lang: options.lang || 'ko-KR',
      continuous: options.continuous !== false, // 기본값: true
      interimResults: options.interimResults !== false, // 기본값: true
      onFinalResult: options.onFinalResult || (() => { }), // 최종 결과 콜백
      onInterimResult: options.onInterimResult || (() => { }), // 중간 결과 콜백
      onStatusChange: options.onStatusChange || (() => { }), // 상태 변경 콜백
      onStart: options.onStart || (() => { }), // 시작 콜백
      onEnd: options.onEnd || (() => { }), // 종료 콜백
      onError: options.onError || ((error) => console.error('Speech error:', error)), // 에러 콜백
      interimDebounceDelay: options.interimDebounceDelay || 300, // 중간 결과 디바운스 지연 시간 (ms)
    }

    // 내부 상태
    this.recognition = null
    this.isRecognizing = false
    this.bufferText = '' // 최종 결과 누적
    this.interimText = '' // 현재 중간 결과
    this.lastInterimText = '' // 이전 중간 결과 (비교용)
    this.interimDebounceTimer = null // 중간 결과 디바운스 타이머

    // Web Speech API 초기화
    this.init()
  }

  /**
   * Web Speech API 객체 초기화
   */
  init() {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
    if (!SpeechRecognition) {
      console.warn('이 브라우저는 음성인식을 지원하지 않습니다.')
      return false
    }

    this.recognition = new SpeechRecognition()
    this.recognition.lang = this.options.lang
    this.recognition.continuous = this.options.continuous
    this.recognition.interimResults = this.options.interimResults

    // 이벤트 핸들러 설정
    this.recognition.onstart = () => {
      this.isRecognizing = true
      this.options.onStatusChange(true)
      this.options.onStart()
      console.log('음성인식 시작')
    }

    this.recognition.onresult = (event) => {
      this.handleRecognitionResult(event)
    }

    this.recognition.onend = () => {
      this.options.onEnd()
      if (this.isRecognizing && this.options.continuous) {
        // continuous 모드이므로 자동 재시작
        try {
          this.recognition.start()
        } catch (error) {
          // 이미 시작된 경우 무시
          console.log('Recognition already started')
        }
      }
    }

    this.recognition.onerror = (event) => {
      this.handleRecognitionError(event)
    }

    return true
  }

  /**
   * 음성인식 결과 처리
   */
  handleRecognitionResult(event) {
    console.log('---- handleRecognitionResult ----')
    let interimTranscript = ''
    let finalTranscript = ''

    // 결과 파싱 - 전체 결과를 순회하여 누적
    for (let i = event.resultIndex; i < event.results.length; i++) {
      const transcript = event.results[i][0].transcript
      if (event.results[i].isFinal) {
        finalTranscript += transcript
      } else {
        interimTranscript += transcript
      }
    }

    // 최종 결과 처리
    if (finalTranscript) {
      // 이전 중간 결과 제거하고 최종 결과 추가
      this.bufferText += finalTranscript
      this.interimText = '' // 중간 결과 초기화
      this.lastInterimText = ''

      // 디바운스 타이머 취소
      if (this.interimDebounceTimer) {
        clearTimeout(this.interimDebounceTimer)
        this.interimDebounceTimer = null
      }
      this.options.onFinalResult(finalTranscript)
    }

    // 중간 결과 처리 (디바운싱 적용)
    if (interimTranscript && interimTranscript !== this.lastInterimText) {
      this.interimText = interimTranscript
      this.lastInterimText = interimTranscript

      // 디바운스 타이머가 있으면 취소
      if (this.interimDebounceTimer) {
        clearTimeout(this.interimDebounceTimer)
      }

      // 일정 시간 후에만 중간 결과 업데이트 (너무 자주 업데이트되는 것 방지)
      this.interimDebounceTimer = setTimeout(() => {
        // 중간 결과가 변경되지 않았을 때만 콜백 호출
        if (this.interimText === interimTranscript) {
          this.options.onInterimResult(interimTranscript)
        }
        this.interimDebounceTimer = null
      }, this.options.interimDebounceDelay)
    }
  }

  /**
   * 음성인식 에러 처리
   */
  handleRecognitionError(event) {
    this.options.onError(event.error)

    if (event.error === 'no-speech') {
      // 음성이 없을 때는 재시작 (continuous 모드인 경우)
      if (this.isRecognizing && this.options.continuous) {
        try {
          this.recognition.start()
        } catch (error) {
          console.log('Recognition restart failed:', error)
        }
      }
    } else if (event.error === 'aborted') {
      // 사용자가 중지한 경우
      this.isRecognizing = false
      this.options.onStatusChange(false)
    }
  }

  /**
   * 음성인식 시작
   */
  start() {
    if (!this.recognition) {
      if (!this.init()) {
        alert('이 브라우저는 음성인식을 지원하지 않습니다.')
        return false
      }
    }

    try {
      this.bufferText = ''
      this.interimText = ''
      this.lastInterimText = ''

      // 디바운스 타이머 초기화
      if (this.interimDebounceTimer) {
        clearTimeout(this.interimDebounceTimer)
        this.interimDebounceTimer = null
      }

      this.recognition.start()
      return true
    } catch (error) {
      console.error('음성인식 시작 실패:', error)
      return false
    }
  }

  /**
   * 음성인식 중지
   */
  stop() {
    this.isRecognizing = false
    this.options.onStatusChange(false)

    // 디바운스 타이머 취소
    if (this.interimDebounceTimer) {
      clearTimeout(this.interimDebounceTimer)
      this.interimDebounceTimer = null
    }

    if (this.recognition) {
      try {
        this.recognition.stop()
      } catch (error) {
        console.error('음성인식 중지 실패:', error)
      }
    }

    // 중간 결과가 있으면 최종 결과로 처리
    if (this.interimText.trim() !== '') {
      this.options.onFinalResult(this.interimText)
      this.interimText = ''
      this.lastInterimText = ''
    }

    // bufferText 초기화
    this.bufferText = ''
  }

  /**
   * 음성인식 토글 (시작/중지)
   */
  toggle() {
    if (this.isRecognizing) {
      this.stop()
    } else {
      this.start()
    }
  }

  /**
   * 현재 인식 상태 반환
   */
  getStatus() {
    return {
      isRecognizing: this.isRecognizing,
      bufferText: this.bufferText,
      interimText: this.interimText,
    }
  }

  /**
   * 리소스 정리
   */
  destroy() {
    this.stop()

    // 디바운스 타이머 정리
    if (this.interimDebounceTimer) {
      clearTimeout(this.interimDebounceTimer)
      this.interimDebounceTimer = null
    }

    this.recognition = null
    this.bufferText = ''
    this.interimText = ''
    this.lastInterimText = ''
  }
}

export function createVoiceRecognitionForVue(vueInstance, options = {}) {
  const inputField = options.inputField || 'userInput'
  const statusField = options.statusField || 'recognizing'
  let baseInputText = '' // 최종 결과가 확정된 텍스트만 저장
  let currentInterimText = '' // 현재 중간 결과

  // 현재 input 값을 읽어서 baseInputText를 동기화하는 함수
  const syncBaseInputText = () => {
    const currentInput = vueInstance[inputField] || ''
    // 중간 결과가 포함되어 있을 수 있으므로 제거
    const cleanInput = currentInput.replace(currentInterimText, '').trim()
    baseInputText = cleanInput
    currentInterimText = ''
  }

  const voiceRecognition = new VoiceRecognition({
    lang: options.lang || 'ko-KR',
    continuous: options.continuous !== false,
    interimResults: options.interimResults !== false,
    interimDebounceDelay: options.interimDebounceDelay || 300,
    onFinalResult: (text) => {
      // 최종 결과를 baseInputText에 추가
      baseInputText = (baseInputText ? baseInputText + ' ' : '') + text.trim()
      currentInterimText = '' // 중간 결과 초기화

      // inputField 업데이트 (최종 결과만)
      vueInstance[inputField] = baseInputText + ' '

      // 커스텀 콜백 호출
      if (options.onFinalResult) {
        options.onFinalResult(text, vueInstance)
      }
    },
    onInterimResult: (text) => {
      // 중간 결과를 임시로 표시 (baseInputText + 중간 결과)
      currentInterimText = text
      console.log('onInterimResult (ko1)--- text' + text)

      console.log('onInterimResult (ko1)--- text' + baseInputText)

      vueInstance[inputField] = (baseInputText ? baseInputText + ' ' : '') + text
      console.log('onInterimResult (ko1)--- vueInstance[inputField]!!!' + vueInstance[inputField])

      // 커스텀 콜백 호출
      if (options.onInterimResult) {
        options.onInterimResult(text, vueInstance)
      }
    },
    onStatusChange: (isRecognizing) => {
      // 상태 변경
      vueInstance[statusField] = isRecognizing

      // 음성인식 시작 시 현재 input 값을 기준으로 baseInputText 동기화
      if (isRecognizing) {
        syncBaseInputText()
      } else {
        // 음성인식 중지 시 중간 결과가 있으면 최종 결과로 처리
        if (currentInterimText.trim() !== '') {
          baseInputText = (baseInputText ? baseInputText + ' ' : '') + currentInterimText.trim()
          vueInstance[inputField] = baseInputText + ' '
          currentInterimText = ''
        }
      }

      // 커스텀 콜백 호출
      if (options.onStatusChange) {
        options.onStatusChange(isRecognizing, vueInstance)
      }
    },
    onStart: () => {
      // 시작 시 현재 input 값 동기화
      syncBaseInputText()

      if (options.onStart) {
        options.onStart(vueInstance)
      }
    },
    onEnd: () => {
      if (options.onEnd) {
        options.onEnd(vueInstance)
      }
    },
    onError: (error) => {
      if (options.onError) {
        options.onError(error, vueInstance)
      }
    },
  })

  // input 필드의 변경을 감지하여 baseInputText 동기화
  // 사용자가 직접 input을 수정했을 때를 감지
  if (vueInstance.$watch) {
    vueInstance.$watch(
      inputField,
      (newVal) => {
        // 음성인식 중이 아닐 때만 동기화 (음성인식 중에는 자동 업데이트되므로)
        if (!voiceRecognition.isRecognizing) {
          // 중간 결과가 포함되지 않은 순수한 값으로 동기화
          const cleanVal = (newVal || '').replace(currentInterimText, '').trim()
          baseInputText = cleanVal
          currentInterimText = ''
        }
      },
      { immediate: false }
    )
  }

  // 컴포넌트가 파괴될 때 정리
  if (vueInstance.$once) {
    vueInstance.$once('hook:beforeDestroy', () => {
      voiceRecognition.destroy()
    })
  }

  return voiceRecognition
}
