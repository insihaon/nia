/**
 * UI Evidence Collection Plugin
 * Vue 앱에서 DOM/로그/에러/상태를 Collector로 전송하는 플러그인
 */

import io from 'socket.io-client'

// Collector 서버 URL (환경변수 또는 기본값)
const COLLECTOR_URL = process.env.VUE_APP_COLLECTOR_URL || 'http://localhost:3100'

// Console 로그 버퍼 (최대 200개)
const MAX_CONSOLE_BUFFER = 200
const consoleBuffer = []

// 에러 수집
const errors = []

// Socket.IO 연결
let socket = null
let isConnected = false

/**
 * Session ID 가져오기 또는 생성
 * 1. URL query "ag_session" 우선
 * 2. localStorage "ag_session"
 * 3. 없으면 생성해서 저장
 */
function getSessionId() {
  // URL query에서 가져오기
  const urlParams = new URLSearchParams(window.location.search)
  let sessionId = urlParams.get('ag_session')

  if (sessionId) {
    // localStorage에 저장
    try {
      localStorage.setItem('ag_session', sessionId)
    } catch (e) {
      console.warn('[UI Evidence] Failed to save sessionId to localStorage:', e)
    }
    return sessionId
  }

  // localStorage에서 가져오기
  try {
    sessionId = localStorage.getItem('ag_session')
    if (sessionId) {
      return sessionId
    }
  } catch (e) {
    console.warn('[UI Evidence] Failed to read sessionId from localStorage:', e)
  }

  // 새로 생성
  sessionId = `session_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
  try {
    localStorage.setItem('ag_session', sessionId)
  } catch (e) {
    console.warn('[UI Evidence] Failed to save new sessionId to localStorage:', e)
  }

  return sessionId
}

/**
 * DOM 스냅샷 생성
 */
function getDomSnapshot() {
  try {
    return {
      html: document.documentElement.outerHTML.substring(0, 100000), // 최대 100KB
      title: document.title,
      url: window.location.href,
      viewport: {
        width: window.innerWidth,
        height: window.innerHeight
      }
    }
  } catch (error) {
    console.error('[UI Evidence] Failed to get DOM snapshot:', error)
    return null
  }
}

/**
 * UI 상태 수집
 */
function getUIState() {
  try {
    return {
      scrollPosition: {
        x: window.scrollX,
        y: window.scrollY
      },
      activeElement: document.activeElement ? {
        tagName: document.activeElement.tagName,
        id: document.activeElement.id,
        className: document.activeElement.className
      } : null,
      visibilityState: document.visibilityState,
      readyState: document.readyState
    }
  } catch (error) {
    console.error('[UI Evidence] Failed to get UI state:', error)
    return null
  }
}

/**
 * 증거 전송
 */
function emitEvidence(stepName, extra = {}) {
  if (!isConnected || !socket) {
    // 조용히 처리 (서버가 없을 수 있으므로)
    return
  }

  const sessionId = getSessionId()
  const timestamp = Date.now()

  const payload = {
    sessionId,
    stepName,
    timestamp,
    url: window.location.href,
    userAgent: navigator.userAgent,
    domSnapshot: getDomSnapshot(),
    uiState: getUIState(),
    console: [...consoleBuffer], // 복사본 전송
    errors: [...errors], // 복사본 전송
    extra
  }

  // ACK를 기다리는 방식으로 전송
  socket.emit('ui:evidence', payload, (response) => {
    if (response && response.success) {
      console.log(`[UI Evidence] Evidence sent: ${stepName}`, response)
    } else {
      console.error(`[UI Evidence] Failed to send evidence: ${stepName}`, response)
    }
  })
}

/**
 * Console 로그 수집
 */
function setupConsoleCollection() {
  const originalLog = console.log
  const originalWarn = console.warn
  const originalError = console.error

  console.log = function(...args) {
    originalLog.apply(console, args)
    addToConsoleBuffer('log', args)
  }

  console.warn = function(...args) {
    originalWarn.apply(console, args)
    addToConsoleBuffer('warn', args)
  }

  console.error = function(...args) {
    originalError.apply(console, args)
    addToConsoleBuffer('error', args)
    // 에러도 errors 배열에 추가
    errors.push({
      type: 'console.error',
      message: args.map(arg => String(arg)).join(' '),
      timestamp: Date.now(),
      stack: new Error().stack
    })
  }
}

/**
 * Console 버퍼에 추가
 */
function addToConsoleBuffer(level, args) {
  const logEntry = {
    level,
    message: args.map(arg => {
      try {
        if (typeof arg === 'object') {
          return JSON.stringify(arg)
        }
        return String(arg)
      } catch (e) {
        return String(arg)
      }
    }).join(' '),
    timestamp: Date.now()
  }

  consoleBuffer.push(logEntry)

  // 버퍼 크기 제한
  if (consoleBuffer.length > MAX_CONSOLE_BUFFER) {
    consoleBuffer.shift()
  }
}

/**
 * 전역 에러 수집
 */
function setupErrorCollection() {
  // window error
  window.addEventListener('error', (event) => {
    errors.push({
      type: 'window.error',
      message: event.message,
      filename: event.filename,
      lineno: event.lineno,
      colno: event.colno,
      timestamp: Date.now(),
      stack: event.error ? event.error.stack : null
    })

    // 버퍼 크기 제한
    if (errors.length > MAX_CONSOLE_BUFFER) {
      errors.shift()
    }
  })

  // unhandledrejection
  window.addEventListener('unhandledrejection', (event) => {
    errors.push({
      type: 'unhandledrejection',
      message: event.reason ? String(event.reason) : 'Unhandled Promise Rejection',
      timestamp: Date.now(),
      stack: event.reason && event.reason.stack ? event.reason.stack : null
    })

    // 버퍼 크기 제한
    if (errors.length > MAX_CONSOLE_BUFFER) {
      errors.shift()
    }
  })
}

/**
 * Collector 서버 존재 여부 확인
 */
async function checkCollectorServer() {
  try {
    const healthCheckUrl = `${COLLECTOR_URL}/health`
    const response = await fetch(healthCheckUrl, {
      method: 'GET',
      mode: 'cors',
      cache: 'no-cache',
      signal: AbortSignal.timeout(2000) // 2초 타임아웃
    })
    return response.ok
  } catch (error) {
    // 서버가 없거나 응답하지 않음
    return false
  }
}

/**
 * Socket.IO 연결 설정
 */
async function setupSocketConnection() {
  // 먼저 Collector 서버가 있는지 확인
  const serverAvailable = await checkCollectorServer()

  if (!serverAvailable) {
    console.log('[UI Evidence] Collector server not available, skipping connection')
    return
  }

  try {
    socket = io(COLLECTOR_URL, {
      transports: ['websocket', 'polling'],
      reconnection: false, // 서버가 없으면 재연결 시도하지 않음
      timeout: 3000, // 3초 타임아웃
      autoConnect: true
    })

    socket.on('connect', () => {
      isConnected = true
      console.log('[UI Evidence] Connected to collector:', COLLECTOR_URL)
    })

    socket.on('disconnect', () => {
      isConnected = false
      // 조용히 처리 (에러 아님)
    })

    socket.on('connect_error', (_error) => {
      isConnected = false
      // 에러를 조용히 처리 (서버가 없을 수 있으므로)
      // console.warn은 개발 중에만 표시
      if (process.env.NODE_ENV === 'development') {
        console.log('[UI Evidence] Collector server not available, evidence collection disabled')
      }
    })

    socket.on('ui:evidence:ack', (response) => {
      if (response && response.success) {
        console.log('[UI Evidence] ACK received:', response)
      }
    })
  } catch (error) {
    // 조용히 처리
    if (process.env.NODE_ENV === 'development') {
      console.log('[UI Evidence] Collector server not available, evidence collection disabled')
    }
  }
}

/**
 * 플러그인 설치
 */
export default {
  install(Vue, options = {}) {
    // 개발 환경에서만 활성화 (또는 환경변수로 제어)
    const isEnabled = process.env.NODE_ENV === 'development' ||
                     process.env.VUE_APP_UI_EVIDENCE_ENABLED === 'true'

    if (!isEnabled) {
      console.log('[UI Evidence] Plugin disabled (not in development mode)')
      return
    }

    console.log('[UI Evidence] Plugin installing...')

    // Socket.IO 연결 설정 (비동기)
    setupSocketConnection().catch(() => {
      // 조용히 처리
    })

    // Console 로그 수집 설정
    setupConsoleCollection()

    // 전역 에러 수집 설정
    setupErrorCollection()

    // window.__emitEvidence 등록 (Playwright에서 호출 가능하도록)
    window.__emitEvidence = emitEvidence

    // Vue 인스턴스에 메서드 추가 (선택사항)
    Vue.prototype.$emitEvidence = emitEvidence

    console.log('[UI Evidence] Plugin installed successfully')
    console.log('[UI Evidence] Session ID:', getSessionId())
  }
}
