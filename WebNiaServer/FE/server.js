// server.js
const express = require('express')
const path = require('path') // 파일 경로 처리를 위한 모듈
const app = express()
const port = 3000

// dist 폴더의 파일들을 제공 (예: dist/index.html)
// 기본적으로 index.html이 존재하면 '/' 경로로 접근 시 자동 제공됨
app.use(express.static(path.join(__dirname, 'dist')))

// mock 서버 등록 (mock 서버 코드는 mock/mock-server.js에 작성되어 있음)
require('./mock/mock-server')(app)

app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist', 'index.html'))
})

// 3000 포트에서 서버 실행
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`)
})

