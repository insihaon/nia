<template>
  <div :class="{ [name]: true, 'w-full h-full': true }">
    <div class="chatbot-container">
      <div class="chat-header">
        <h3>AI 챗봇</h3>
        <p>질문을 입력하고 AI의 답변을 받아보세요</p>
      </div>

      <div ref="chatMessages" class="chat-messages">
        <div
          v-for="(message, index) in chatMessages"
          :key="index"
          :class="['message', message.type]"
        >
          <div class="message-content" @click="handlePathClick" v-html="formatMessage(message.content)"></div>
          <div class="message-time">
            {{ message.time }}
          </div>
        </div>
      </div>

      <div class="chat-input">
        <input
          v-model="userInput"
          type="text"
          placeholder="질문을 입력하세요..."
          @keyup.enter="sendMessage"
        >
        <button
          :disabled="!userInput.trim()"
          @click="sendMessage"
        >
          전송
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import axios from 'axios'

const routeName = 'chatbot'
/* eslint-disable */
export default {
  name: routeName,
  components: {},
  directives: { elDragDialog },

  extends: Modal,
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      userInput: '',
      chatMessages: [
        {
          type: 'bot',
          content: '안녕하세요! 무엇을 도와드릴까요?',
          time: this.getCurrentTime()
        }
      ]
    }
  },
  computed: {
    ...mapState({
      systemMonitoringMap: state => state.systemMonitoring.systemMonitoringMap
    })
  },
  watch: {},
  created() {
    this.selectedRow = this.wdata.params
  },
  methods: {
    async sendMessage() {
      if (!this.userInput.trim()) return


      await axios.get('http://116.89.191.47:8001/health')

      // 사용자 메시지 추가
      this.chatMessages.push({
        type: 'user',
        content: this.userInput,
        time: this.getCurrentTime()
      })

      const userQuestion = this.userInput
      this.userInput = ''

      // 로딩 메시지 추가
      this.chatMessages.push({
        type: 'bot',
        content: '검색 중입니다...',
        time: this.getCurrentTime()
      })

      try {
        // ElasticSearch에서 검색
        const searchResult = await this.searchElasticSearch(userQuestion)

        // 로딩 메시지 제거하고 결과 메시지 추가
        this.chatMessages.pop()
        this.chatMessages.push({
          type: 'bot',
          content: searchResult,
          time: this.getCurrentTime()
        })
      } catch (error) {
        // 에러 발생 시 로딩 메시지 제거하고 에러 메시지 추가
        this.chatMessages.pop()
        this.chatMessages.push({
          type: 'bot',
          content: '죄송합니다. 검색 중 오류가 발생했습니다.',
          time: this.getCurrentTime()
        })
        console.error('ElasticSearch 검색 오류:', error)
      }

      this.scrollToBottom()
    },

    async searchElasticSearch(query) {
      try {
        // axios 인스턴스 생성하여 baseURL 설정 (Python 프록시)
        const esClient = axios.create({
          baseURL: 'http://116.89.191.47:8001/es',
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json'
          }
        })

        const response = await esClient.post('/chatbot_index/_search', {
          query: {
            multi_match: {
              query: query,
              fields: ['keyword^2'],
              type: 'best_fields',
              fuzziness: 'AUTO' // 유사한 데이터도 검색
            }
          },
          size: 5,
          highlight: {
            fields: {
              keyword: {},
              path: {}
            }
          }
        })

        const data = response.data

        if (data.hits && data.hits.hits && data.hits.hits.length > 0) {
          const hits = data.hits.hits
          let resultMessage = '검색 결과를 찾았습니다\n\n'

          hits.forEach((hit, index) => {
            const source = hit._source
            resultMessage += `${index + 1}. ${source.keyword} <span class="move-text">[이동]</span>\n`
            resultMessage += '\n'
          })

          return resultMessage
        } else {
          return '죄송합니다. 검색 결과를 찾을 수 없습니다. 다른 키워드로 검색해보세요.'
        }
      } catch (error) {
        console.error('ElasticSearch 검색 오류:', error)
        throw error
      }
    },

    getCurrentTime() {
      const now = new Date()
      return now.toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit'
      })
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessages = this.$refs.chatMessages
        if (chatMessages) {
          chatMessages.scrollTop = chatMessages.scrollHeight
        }
      })
    },

    formatMessage(content) {
      // 줄바꿈을 <br> 태그로 변환하고 HTML 태그 제거
      let formattedContent = content
        .replace(/\n/g, '<br>') // 줄바꿈을 <br>로 변환

      // [이동] 텍스트를 클릭 가능한 링크로 변환
      formattedContent = formattedContent.replace(
        /(\d+\.\s+)([^<]+)\s+<span class="move-text">\[이동\]<\/span>/g,
        '$1$2 <a href="#" class="move-link" data-keyword="$2">[이동]</a>'
      )

      return formattedContent
    },

    handlePathClick(event) {
      if (event.target.classList.contains('move-link')) {
        event.preventDefault()
        const keyword = event.target.getAttribute('data-keyword')

        // ElasticSearch에서 해당 keyword의 path를 다시 검색
        this.searchPathByKeyword(keyword)
      }
    },

    async searchPathByKeyword(keyword) {
      try {
        const response = await axios.post('http://116.89.191.47:8001/es/chatbot_index/_search', {
          'query': { 'match': { 'keyword': keyword } },
          'size': 1
        })

        const data = response.data
        if (data.hits && data.hits.hits && data.hits.hits.length > 0) {
          const path = data.hits.hits[0]._source.path
          const currentOrigin = window.location.origin
          const fullUrl = `${currentOrigin}/#${path}`

          // 현재 창에서 이동
          window.location.href = fullUrl
        }
      } catch (error) {
        console.error('경로 검색 오류:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.chatbot {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */
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
  background: #3b82f6;
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

  &.user {
    align-self: flex-end;

    .message-content {
      background: #3b82f6;
      color: white;
      border-radius: 1rem 1rem 0.25rem 1rem;
    }
  }

  &.bot {
    align-self: flex-start;

    .message-content {
      background: white;
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

.chat-input {
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
