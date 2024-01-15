<template>
  <div @click="click">
    <el-col class="fullscreen-icon" :class="{ 'vertical' : isVertical}">
      <svg-icon :icon-class="isFullscreen?'exit-fullscreen':'fullscreen'" />
    </el-col>
    <el-col><slot name="name" /></el-col>
  </div>
</template>

<script>
import screenfull from 'screenfull'

export default {
  name: 'Screenfull',
  props: {
    isVertical: {
      default: false,
      type: Boolean
    }
  },
  data() {
    return {
      isFullscreen: false
    }
  },
  mounted() {
    this.init()
  },
  beforeDestroy() {
    this.destroy()
  },
  methods: {
    click() {
      if (!screenfull.enabled) {
        this.$message({
          message: 'you browser can not work',
          type: 'warning'
        })
        return false
      }
      screenfull.toggle()
    },
    change() {
      this.isFullscreen = screenfull.isFullscreen
    },
    init() {
      if (screenfull.enabled) {
        screenfull.on('change', this.change)
      }
    },
    destroy() {
      if (screenfull.enabled) {
        screenfull.off('change', this.change)
      }
    }
  }
}
</script>

<style scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
.vertical {
  height: 15px;
  line-height: 35px;
}
.theme-dark .fullscreen-icon .svg-icon {
  opacity: 0.5;
  width: 1.2em;
  height: 1.2em;
}
</style>
