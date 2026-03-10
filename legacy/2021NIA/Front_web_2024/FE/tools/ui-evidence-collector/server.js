const express = require('express')
const http = require('http')
const socketIo = require('socket.io')
const cors = require('cors')
const fs = require('fs-extra')
const path = require('path')

const PORT = process.env.PORT || 3100
const SESSIONS_DIR = path.join(__dirname, 'sessions')

// sessions 디렉토리 생성
fs.ensureDirSync(SESSIONS_DIR)

const app = express()
const server = http.createServer(app)

// CORS 설정
const io = socketIo(server, {
  cors: {
    origin: '*', // 개발 환경에서는 모든 origin 허용
    methods: ['GET', 'POST'],
    credentials: true
  }
})

app.use(cors())
app.use(express.json())

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ status: 'ok', port: PORT })
})

// 세션 목록 조회
app.get('/sessions', (req, res) => {
  try {
    const sessions = fs.readdirSync(SESSIONS_DIR)
      .filter(dir => fs.statSync(path.join(SESSIONS_DIR, dir)).isDirectory())
    res.json({ sessions })
  } catch (error) {
    res.status(500).json({ error: error.message })
  }
})

// 특정 세션의 증거 파일 목록 조회
app.get('/sessions/:sessionId', (req, res) => {
  try {
    const sessionId = req.params.sessionId
    const sessionDir = path.join(SESSIONS_DIR, sessionId)
    if (!fs.existsSync(sessionDir)) {
      return res.status(404).json({ error: 'Session not found' })
    }
    const files = fs.readdirSync(sessionDir)
      .filter(file => fs.statSync(path.join(sessionDir, file)).isFile())
    res.json({ sessionId, files })
  } catch (error) {
    res.status(500).json({ error: error.message })
  }
})

io.on('connection', (socket) => {
  console.log(`[Collector] Client connected: ${socket.id}`)

  socket.on('ui:evidence', async (payload, callback) => {
    try {
      const {
        sessionId,
        stepName,
        timestamp,
        url,
        userAgent,
        domSnapshot,
        uiState,
        console: consoleLogs,
        errors,
        extra
      } = payload

      if (!sessionId || !stepName) {
        const error = 'sessionId and stepName are required'
        console.error(`[Collector] Error: ${error}`)
        if (callback) callback({ success: false, error })
        return
      }

      // 세션 디렉토리 생성
      const sessionDir = path.join(SESSIONS_DIR, sessionId)
      fs.ensureDirSync(sessionDir)

      // 증거 파일명 생성 (timestamp_stepName.json)
      const safeStepName = stepName.replace(/[^a-zA-Z0-9_-]/g, '_')
      const fileName = `${timestamp}_${safeStepName}.json`
      const filePath = path.join(sessionDir, fileName)

      // 증거 데이터 저장
      const evidenceData = {
        sessionId,
        stepName,
        timestamp,
        url,
        userAgent,
        domSnapshot,
        uiState,
        console: consoleLogs || [],
        errors: errors || [],
        extra: extra || {},
        collectedAt: new Date().toISOString()
      }

      await fs.writeJson(filePath, evidenceData, { spaces: 2 })

      console.log(`[Collector] Evidence saved: ${sessionId}/${fileName}`)

      // ACK 전송
      if (callback) {
        callback({ success: true, filePath: `${sessionId}/${fileName}` })
      } else {
        socket.emit('ui:evidence:ack', { success: true, filePath: `${sessionId}/${fileName}` })
      }
    } catch (error) {
      console.error(`[Collector] Error saving evidence:`, error)
      const errorResponse = { success: false, error: error.message }
      if (callback) {
        callback(errorResponse)
      } else {
        socket.emit('ui:evidence:ack', errorResponse)
      }
    }
  })

  socket.on('disconnect', () => {
    console.log(`[Collector] Client disconnected: ${socket.id}`)
  })
})

server.listen(PORT, () => {
  console.log(`[Collector] Server running on port ${PORT}`)
  console.log(`[Collector] Sessions directory: ${SESSIONS_DIR}`)
})
