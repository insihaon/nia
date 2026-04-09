<template>
  <div
    class="chatbot-icon-container"
    :style="containerStyle"
    @mousedown="onMouseDown"
    @mouseenter="tooltipVisible = true"
    @mouseleave="tooltipVisible = false"
  >
    <div class="chatbot-icon">
      <span class="icon-placeholder">
        <i class="el-icon-chat-dot-square" />
      </span>
    </div>
    <div v-if="tooltipVisible && !isDragging" class="chatbot-tooltip">Chatbot</div>
  </div>
</template>

<script>
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import constants from '@/min/constants'

const DRAG_THRESHOLD = 5

export default {
  mixins: [dialogOpenMixin],

  data() {
    return {
      tooltipVisible: false,
      isDragging: false,
      isMouseDown: false,
      offsetX: 20,
      offsetY: window.innerHeight - 120,
      assistantIcon: constants.nia.chatbotIcon.assistantIcon,
      startX: 0,
      startY: 0,
      grabOffsetX: 0,
      grabOffsetY: 0
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
  beforeDestroy() {
    this.cleanup()
  },
  methods: {
    toggleChatbot() {
      this.fn_openWindow('chatbot', null, null, { height: window.innerHeight - 135 })
    },
    onMouseDown(event) {
      event.preventDefault()
      this.isMouseDown = true
      this.isDragging = false
      this.startX = event.clientX
      this.startY = event.clientY
      this.grabOffsetX = event.clientX - this.offsetX
      this.grabOffsetY = event.clientY - this.offsetY

      document.addEventListener('mousemove', this.onMouseMove)
      document.addEventListener('mouseup', this.onMouseUp)
    },
    onMouseMove(event) {
      if (!this.isMouseDown) return

      const dx = event.clientX - this.startX
      const dy = event.clientY - this.startY

      if (!this.isDragging && (dx * dx + dy * dy) < DRAG_THRESHOLD * DRAG_THRESHOLD) return

      this.isDragging = true
      this.offsetX = event.clientX - this.grabOffsetX
      this.offsetY = event.clientY - this.grabOffsetY
    },
    onMouseUp() {
      this.isMouseDown = false
      document.removeEventListener('mousemove', this.onMouseMove)
      document.removeEventListener('mouseup', this.onMouseUp)

      if (!this.isDragging) {
        this.toggleChatbot()
      }
      this.isDragging = false
    },
    cleanup() {
      document.removeEventListener('mousemove', this.onMouseMove)
      document.removeEventListener('mouseup', this.onMouseUp)
    }
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
