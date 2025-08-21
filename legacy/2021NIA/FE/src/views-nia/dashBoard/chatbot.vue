<template>
  <div :class="{ [name]: true, 'w-full h-full': true }">
    <div class="chatbot-container">
      <div class="chat-header">
        <h3>chatbot</h3>
        <p>질문을 입력하고 답변을 받아보세요</p>
      </div>

      <div ref="chatMessagesBox" class="chat-messages">
        <div v-for="(message, index) in chatMessages" :key="index" :class="['message', message.type]">
          <div class="message-content" @click="handlePathClick" v-html="formatMessage(message.content)"></div>
          <div class="message-time">
            {{ message.time }}
          </div>
        </div>
      </div>

      <div class="chat-input">
        <input v-model="userInput" type="text" placeholder="질문을 입력하세요..." @keyup.enter="sendMessage" />
        <button :disabled="!userInput.trim()" @click="sendMessage">전송</button>
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
      },
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      userInput: '',
    }
  },
  computed: {
    ...mapState({
      systemMonitoringMap: (state) => state.systemMonitoring.systemMonitoringMap,
      chatMessages: (state) => state.chatbot.chatMessages,
    }),
  },
  watch: {
    chatMessages(nval, oval) {
      this.scrollToBottom()
    },
  },
  created() {
    this.selectedRow = this.wdata.params
    this.scrollToBottom()
  },
  methods: {
    async sendMessage() {
      if (!this.userInput.trim()) return

      this.$store.dispatch('chatbot/userPushQuestionMessage', { content: this.userInput })
      const userQuestion = this.userInput
      this.userInput = ''

      try {
        const searchResult = await this.searchElasticSearch(userQuestion)
        this.$store.dispatch('chatbot/botPushAnsewerMessage', {
          content: searchResult,
          isAnswer: true,
        })
      } catch (error) {
        this.$store.dispatch('chatbot/botPushAnsewerMessage', {
          content: '죄송합니다. 검색 중 오류가 발생했습니다.',
          isAnswer: true,
        })
        console.error('ElasticSearch 검색 오류:', error)
      }
    },

    async searchElasticSearch(query) {
      try {
        // axios 인스턴스 생성하여 baseURL 설정 (Python 프록시)
        const esClient = axios.create({
          baseURL: 'http://116.89.191.47:8001/es',
          timeout: 10000,
          headers: {
            'Content-Type': 'application/json',
          },
        })

        const response = await esClient.post('/chatbot_index/_search', {
          query: {
            multi_match: {
              query: query,
              fields: ['keyword^2'],
              type: 'best_fields',
              fuzziness: 'AUTO', // 유사한 데이터도 검색
            },
          },
          size: 5,
          highlight: {
            fields: {
              keyword: {},
              path: {},
            },
          },
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

    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessagesBox = this.$refs.chatMessagesBox
        if (chatMessagesBox) {
          chatMessagesBox.scrollTop = chatMessagesBox.scrollHeight
        }
      })
    },

    formatMessage(content) {
      // 줄바꿈을 <br> 태그로 변환하고 HTML 태그 제거
      let formattedContent = content.replace(/\n/g, '<br>') // 줄바꿈을 <br>로 변환

      // [이동] 텍스트를 클릭 가능한 링크로 변환
      formattedContent = formattedContent.replace(/(\d+\.\s+)([^<]+)\s+<span class="move-text">\[이동\]<\/span>/g, '$1$2 <a href="#" class="move-link" data-keyword="$2">[이동]</a>')

      formattedContent = formattedContent.replace(/(\s+)([^<]+)\s+<span class="move-text">\[진행\]<\/span>/g, '$1$2 <a href="#" class="move-link" data-keyword="$2">[진행]</a>')

      return formattedContent
    },

    handlePathClick(event) {
      if (event.target.classList.contains('move-link')) {
        event.preventDefault()
        let keyword = ''
        switch (event.target.innerHTML) {
          case '[이동]':
            keyword = event.target.getAttribute('data-keyword')
            this.searchPathByKeyword(keyword)
            break
          case '[진행]':
            keyword = event.target.getAttribute('data-keyword')
            // this.$store.commit('chatbot/SWITCH_EVENT_PARAMETER', { name, parameter })
            this.$router.push({ name: 'NiaMain' })
            break
        }
      }
    },

    async searchPathByKeyword(keyword) {
      try {
        const response = await axios.post('http://116.89.191.47:8001/es/chatbot_index/_search', {
          query: { match: { keyword: keyword } },
          size: 1,
        })

        const data = response.data
        if (data.hits && data.hits.hits && data.hits.hits.length > 0) {
          const rawPath = data.hits.hits[0]._source.path
          const questionIndex = rawPath.indexOf('?')
          let path = rawPath
          let parameter = ''
          if (questionIndex !== -1) {
            path = rawPath.substring(0, questionIndex)
            parameter = rawPath.substring(questionIndex + 1)
          }
          const routes = this.$router.options.routes2
          let name = this.getRouteNameByPath(routes, path)
          this.$store.commit('chatbot/SWITCH_EVENT_PARAMETER', { name, parameter })
          this.$router.push({ name })
        }
      } catch (error) {
        console.error('경로 검색 오류:', error)
      }
    },

    getRouteNameByPath(routes, path, prefix = '') {
      for (const route of routes) {
        // 1. 현재 라우트의 path와 일치하는지 확인
        if (prefix + route.path === path) {
          return route.name
        }

        // 2. children이 있는지 확인하고 재귀적으로 탐색
        if (route.children) {
          const foundName = this.getRouteNameByPath(route.children, path, route.path + '/')
          // 자식 라우트에서 이름이 발견되면 즉시 반환
          if (foundName) {
            return foundName
          }
        }
      }

      // 모든 라우트를 탐색했지만 일치하는 것을 찾지 못한 경우
      return null
    },
  },
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
