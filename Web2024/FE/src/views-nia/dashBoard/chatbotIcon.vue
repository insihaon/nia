<template>
  <div class="chatbot-icon-container" :style="containerStyle" @click="toggleChatbot" @mouseenter="startFollow" @mouseleave="stopFollow" @mousemove="followMouse">
    <div class="chatbot-icon">
      <span class="icon-placeholder">
        <i class="el-icon-chat-dot-square" />
      </span>
    </div>
    <div v-if="tooltipVisible" class="chatbot-tooltip">Chatbot</div>
  </div>
</template>

<script>
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import constants from '@/min/constants'

export default {
  mixins: [dialogOpenMixin],

  data() {
    return {
      tooltipVisible: false,
      isFollowing: false,
      offsetX: 20,
      offsetY: window.innerHeight - 120,
      assistantIcon: constants.nia.chatbotIcon.assistantIcon,
    }
  },
  computed: {
    containerStyle() {
      return {
        left: `${this.offsetX}px`,
        top: `${this.offsetY}px`,
      }
    },
  },
  methods: {
    toggleChatbot() {
      this.fn_openWindow('chatbot')
    },
    startFollow() {
      this.isFollowing = true
      this.tooltipVisible = true
    },
    stopFollow() {
      this.isFollowing = false
      this.tooltipVisible = false
    },
    followMouse(event) {
      if (!this.isFollowing) return
      const iconWidth = 50
      const iconHeight = 50
      this.offsetX = event.clientX - iconWidth / 2
      this.offsetY = event.clientY - iconHeight / 2
    },
  },
}
</script>

<style scoped>
.chatbot-icon-container {
  position: fixed;
  display: flex;
  align-items: center;
  z-index: 1000;
  cursor: pointer;
  transition: none;
}

.chatbot-icon {
  width: 50px;
  height: 50px;
  background-color: #1e293b;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 24px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease-in-out;
}

.chatbot-icon:hover {
  transform: scale(1.1);
}

.chatbot-tooltip {
  background-color: #333;
  color: white;
  padding: 8px 12px;
  border-radius: 5px;
  margin-left: 10px;
  white-space: nowrap;
  font-size: 14px;
  opacity: 0;
  transition: opacity 0.3s ease-in-out;
}

.chatbot-icon-container:hover .chatbot-tooltip {
  opacity: 1;
}
</style>
