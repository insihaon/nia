<template>
  <el-dialog
    title="MOSS"
    :visible.sync="dialogVisible"
    width="30%"
    lock-scroll
    modal
    center
    fullscreen
    custom-class="chatPopup"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
    :before-close="handleClose"
  >
    <div class="chat-wrap theme-2">
      <div class="chat-wrap-header">
        <div class="chat-wrap-header-title">MOSS</div>
        <div class="chat-wrap-header-btn">
          <i class="xi-trash-o" @click="rmoveAllData" />
        </div>
        <div class="chat-wrap-header-btn" @click="closeChatPopup">
          <i class="xi-close-thin" />
        </div>
      </div>
      <!-- 컨텐츠 -->
      <div ref="chatWrapContent" v-loading="chatLoading" class="chat-wrap-content">
        <div v-for="(msg, index) in messages" :key="index" class="msg-item" :class="{'question-item' : msg.type=='question'}">
          <div class="msg-item-header-text" :class="{'large' : msg.type!='question'}">
            <i v-if="msg.type!='question'" class="xi-user-o" />{{ msg.title }}
          </div>
          <div v-if="msg.content!=''" class="msg-item-results" v-html="msg.content" />
          <div v-if="msg.buttons != undefined && msg.buttons.length > 0" class="msg-item-btns">
            <Button v-for="(btn, index2) in msg.buttons" :key="index2" :class="getBtnClass(btn)" @click="questionBtnClickHandler(btn)">
              {{ btn.label }}
            </Button>
          </div>
        </div>

      </div>

      <transition name="fade">
        <div v-show="showInput" class="chat-wrap-footer">
          <input ref="chatFooterInput" v-model="inputText" type="text" class="chat-footer-input" :placeholder="inputPlaceholderText" @keyup.enter="chatFooterInputEnter">
          <i class="xi-send" @click="chatFooterInputEnter" />
        </div>
      </transition>
      <!-- <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">취소</el-button>
        <el-button type="primary" @click="dialogVisible = false">확인</el-button>
      </span> -->
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PopupChat',
  data() {
    return {
      dialogVisible: false,
      chatLoading: true,
      inputText: '',
      inputPlaceholderText: '',
      showChatPopup: false,
      messages: [],
      showInput: false
    }
  },
  mounted() {
    this.initMessage()
  },
  methods: {
    closeChatPopup() {
      this.showChatPopup = false
      this.messages = []
    },
    initMessage() {
      setTimeout(() => {
        this.addMsgItem('고객님 무엇이 궁금하시나요?', '', [
          { 'label': '동해물과', 'customClass': 'default', 'value': '11' },
          { 'label': '백두산이', 'customClass': 'default', 'value': '22' },
          { 'label': '마르고', 'customClass': 'default', 'value': '33' },
          { 'label': '닳도록', 'customClass': 'default', 'value': '44' },
          { 'label': '하느님이', 'customClass': 'c-red', 'value': '55' },
          { 'label': 'PING 테스트', 'customClass': 'c-blue', 'value': '66', 'action': 'input' }
        ])
      }, 500)
    },
    setScrollToBottom() {
      if (this.$refs.chatWrapContent !== undefined) {
        this.$nextTick(() => {
          this.$refs.chatWrapContent.scrollTo({ top: this.$refs.chatWrapContent.scrollHeight, behavior: 'smooth' })
        })
      }
    },
    addMsgItem(title, content, buttons) {
      this.messages.push({
        'type': 'message',
        'title': title,
        'content': content,
        'buttons': buttons
      })
      this.chatLoading = false
      this.setScrollToBottom()
    },
    addQuestionItem(text) {
      this.messages.push({
        'type': 'question',
        'title': text,
        'content': '',
        'buttons': []
      })
      this.setScrollToBottom()
    },
    getBtnClass(btn) {
      var returnClass = 'question-btn'
      if (btn.customClass !== undefined) {
        returnClass += ' ' + btn.customClass
      }
      return returnClass
    },
    questionBtnClickHandler(btn) {
      if (btn.action === 'input') {
        this.addQuestionItem(btn.label)
        this.showInput = true
        this.$nextTick(() => {
          this.inputPlaceholderText = '아이피를 입력하세요'
          this.$refs.chatFooterInput.focus()
        })
      } else {
        this.addQuestionItem(btn.label)

        setTimeout(() => {
          var tmpHtml = '<table>'
          tmpHtml += '<tr><th>해더1</th><th>해더2</th><th>해더3</th><th>해더4</th><th>해더5</th><th>해더6</th><th>해더7</th><th>해더8</th><th>해더9</th><th>해더10</th></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '<tr><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td></tr>'
          tmpHtml += '</table>'

          this.addMsgItem('고객님 무엇이 궁금하시나요?', tmpHtml, [
            { 'label': '동해물과', 'customClass': 'default', 'value': '1' },
            { 'label': '백두산이', 'customClass': 'default', 'value': '2' },
            { 'label': '마르고', 'customClass': 'default', 'value': '3' },
            { 'label': '닳도록', 'customClass': 'default', 'value': '4' },
            { 'label': '하느님이', 'customClass': 'c-red', 'value': '5' },
            { 'label': '보우하사', 'customClass': 'c-blue', 'value': '6' }
          ])
        }, 1000)
      }
    },
    chatFooterInputEnter(e) {
      this.showInput = false
      this.addQuestionItem(this.inputText)
      setTimeout(() => {
        this.inputText = ''
        this.addMsgItem('PING 테스트 결과', '<pre>Ping 192.168.123.104 32바이트 데이터 사용:\n192.168.123.104의 응답: 바이트=32 시간<1ms TTL=64\n\n192.168.123.104에 대한 Ping 통계:\n    패킷: 보냄 = 5, 받음 = 5, 손실 = 0 (0% 손실),\n왕복 시간(밀리초):\n    최소 = 0ms, 최대 = 0ms, 평균 = 0ms</pre>', [
          { 'label': '동해물과', 'customClass': 'default', 'value': '11' },
          { 'label': 'PING 테스트', 'customClass': 'c-blue', 'value': '66', 'action': 'input' }
        ])
      }, 1000)
    },
    rmoveAllData() {
      this.showInput = false
      this.inputText = ''
      this.messages = []
      this.initMessage()
    },
    show() {
      this.dialogVisible = true
    },
    handleClose(done) {
      this.$confirm('Are you sure to close this dialog?')
      .then(_ => {
        done()
      })
      .catch(_ => {})
    }
  }
}
</script>
<style lang="scss" scoped>
.chat-wrap{
  position:fixed;
  width:100vw;
  height:100vh;
  box-sizing: border-box;
  background-color:#f4f7fa;
  display: flex;
  flex-direction: column;
  font-family: "Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
  >div.chat-wrap-header{
    background-color:#FFFFFF;
    min-height:50px;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
    padding: 0 1em;
    gap: 0.5em;
    >div.chat-wrap-header-title{
      color:#FFFFFF;
      font-weight: 600;
      font-size: 1.2em;
      margin-right:auto;
    }
    >div.chat-wrap-header-btn{
      color:#FFFFFF;
      border-radius: 100%;
      min-width: 2em;
      min-height: 2em;
      display: flex;
      justify-content: center;
      align-items: center;
      transition: all 0.25s;
    }
    >div.chat-wrap-header-btn:active{
      background-color:#FFFFFF;
      color:#000000;
    }
  }
  div.chat-wrap-content{
    height:100%;
    overflow-x: hidden;
    overflow-y: auto;
    padding: 1em 1.5em;
    scroll-behavior: smooth;
    &::-webkit-scrollbar {
      width: 0.5em;
      height:0.5em;
    }
    &::-webkit-scrollbar-track {
      background-color: transparent;
      margin-top:1em;
      margin-bottom:1em;
    }
    &::-webkit-scrollbar-thumb {
      background-color: rgba(0,0,0,0.25);
      border-radius: 0.25em;
    }
    &::-webkit-scrollbar-button {
      display: none;
    }

    >div.msg-item{
      margin-top: 1em;
      margin-bottom:3em;
      transform-origin: top left;
      animation-name: showMsgAni;
      animation-duration: 0.25s;
      animation-timing-function: cubic-bezier(0.175, 0.885, 0.32, 1.275);
      >div.msg-item-header-text{
        color:#000000;
        >i{
          position:relative;
          top:1px;
        }
      }
      >div.large{
        font-size:1.3em;
        font-weight: 600;
      }
      >div.msg-item-btns{
        display: flex;
        flex-direction: row;
        gap:0.5em;
        flex-wrap: wrap;
        justify-content: flex-start;
        align-items: flex-start;
        padding-top:0.5em;
        >Button.question-btn{
          border:none;
          padding: 0.3em 1em;
          border-radius: 1em;
          background-color:#FFFFFF;
          box-shadow: 0.1em 0.1em 0.5em rgba(0,0,0,0.1);
          font-size:1em;
          font-weight: 500;
          transition: 0.1s;
          font-family: "Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
        }
        >Button.c-red{
          background-color:#fe2e36;
          color:#FFFFFF;
        }
        >Button.c-blue{
          background-color:#0099ff;
          color:#FFFFFF;
        }
        Button:active{
          filter:invert(1);
        }
      }
      >div.msg-item-results{
        padding: 1em;
        border-radius: 1.0em;
        background-color:#FFFFFF;
        box-shadow: 0.1em 0.1em 0.5em rgba(0,0,0,0.1);
        margin-top:0.5em;
        font-weight: 1em;
        font-weight: 500;
        line-height:140%;
        overflow: scroll;
        max-height:200px;
        font-family: "Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
        >pre{
          font-family: "Pretendard Variable", Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo", "Noto Sans KR", "Malgun Gothic", "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", sans-serif;
          font-size:0.8em;
          color:#000000;
          font-weight: 600;
        }
        >table{
          border-collapse: collapse;
          tr:nth-child(even){
            background-color: #fbfbfb;
          }
          th{
            border:1px solid #f1f1f1;
            padding: 0.2em 0.5em 0.1em 0.5em;
            text-align: center;

            white-space: nowrap;
            font-size: 0.8em;
            border-bottom-color:#cccccc;
            background-color:#f9f9f9;
            border-top:2px solid #484848;
          }
          td{
            border:1px solid #f1f1f1;
            padding: 0.2em 0.5em 0.1em 0.5em;
            text-align: center;
            white-space: nowrap;
            font-size: 0.8em;
            font-weight: 400;
          }
        }
      }
    }
    >div.question-item{
      display: flex;
      justify-content: flex-end !important;
      transform-origin: top right;
      animation-name: showQuestionMsgAni;
      animation-duration: 0.25s;
      animation-timing-function: cubic-bezier(0.175, 0.885, 0.32, 1.275);
      user-select: none;
      >div.msg-item-header-text{
        background-color:#222222;
        color:#FFFFFF;
        font-size:1.1em !important;
        padding: 0.4em 1em;
        font-weight: 600;
        right:0;
        border-radius: 1em 0em 1em 1em;
      }
    }
    @keyframes showMsgAni {
      from{
        transform: scale(0);
      }
      to{
        transform: scale(1);
      }
    }
    @keyframes showQuestionMsgAni {
      from{
        transform: scale(0);
      }
      to{
        transform: scale(1);
      }
    }
  }
  >div.chat-wrap-footer{
    display: flex;
    background-color:#FFFFFF;
    min-height:50px;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
    padding: 0 1em;
    gap:1em;
    transform-origin: center bottom;
    >input[type=text]{
      background-color:#f7f7f7;
      border:none;
      line-height:2em;
      height:2em;
      border-radius: 1em;
      outline:none;
      box-sizing: border-box;
      padding: 0 2em;
      font-size:1em;
      color:#000000;
      width:100%;
    }
    >i {
      color:#888888;
      border-radius: 100%;
      min-width: 2em;
      min-height: 2em;
      display: flex;
      justify-content: center;
      align-items: center;
      transition: all 0.25s;
      &:active{
        background-color:#888888;
        color:#ffffff;
      }
    }
  }
  >div.chatLoading{
    > i{
      color:#fe2e36 !important;
      font-size:1.5em !important;
    }
  }
}
.theme-1 > div.chat-wrap-header{
  background-color:#0099ff;
}
.theme-2 > div.chat-wrap-header{
  background-color:#fe2e36;
}
.theme-3 > div.chat-wrap-header{
  background-color:#999999;
}
</style>

<style>
@import url(//fonts.googleapis.com/earlyaccess/notosanskr.css);
body{
  padding:30px;
  margin:0;
  font-family: "Noto Sans KR", sans-serif !important;
}
.chatPopup{
  padding:0 !important;
}
.chatPopup > div.el-dialog__header{
  display: none;
}
.chatPopup > div.el-dialog__body{
  padding: 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.5s ease;
  transform: scaleY(1);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transition: all 0.5s ease;
  transform: scaleY(0);
}
@keyframes slideDown {
  from{
    transform: translateY(-50px);
  }
  to{
    transform: translateY(0px);
  }
}
.slideDown{
  animation-name: slideDown;
  animation-duration: 0.25s;
}
</style>
