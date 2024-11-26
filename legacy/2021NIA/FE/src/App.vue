<template>
  <div id="app" :class="[appClass, appProject, uuid, outline]">
    <RouterView />
    <el-card v-if="showWindowSize" class="viewport-container" shadow="always">
      <h6>{{ viewSize }}</h6>
    </el-card>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import hotkeys from 'hotkeys-js'
// import { addClass, removeClass } from '@/utils'

// const { body } = document

// if (performance.navigation.type === 1) {
//   console.log(`Reload Web Application`)
// }

export default {
  name: 'App',
  computed: {
    ...mapState({
      outline: (state) => state.app.outline,
      expire: (state) => state.app.expire,
      showWindowSize: (state) => state.app.showWindowSize,
      viewport: (state) => state.app.viewport,
      windowSize: (state) => state.app.windowSize,
    }),
    appClass() {
      return this.outline ? 'outline' : ''
    },
    appProject() {
      const { AppOptions } = require('@/class/appOptions')
      const { project } = AppOptions.instance
      return project
    },
    uuid() {
      const { AppOptions } = require('@/class/appOptions')
      const { uuid } = AppOptions.instance
      return uuid
    },
    viewSize() {
      return `${this.viewport.toUpperCase()} ${this.windowSize.width}x${this.windowSize.height}`
    }
  },

  watch: {
    outline() {
      if (this.outline) {
        this.utils.addClass(document.body, 'outline')
      } else {
        this.utils.removeClass(document.body, 'outline')
      }
    }
  },

  async created() {
    this.utils = await (() => import('@/utils'))()

    hotkeys(`alt+p`, (event, handler) => this.syncServerProject())
    setTimeout(() => {
      this.$store.dispatch('app/removeViewDataInStorage')
      this.$store.commit('errorLog/DELETE_ERROR_LOG_IN_STORAGE')
      this.$store.commit('serviceLog/DELETE_SERVICE_LOG_IN_STORAGE')
    }, this.expire)
  },
  methods: {
    async syncServerProject() {
      const { AppOptions } = require('@/class/appOptions')
      const { debug, project, mock } = AppOptions.instance
      if (debug) {
        const server = await this.$store.dispatch('app/getServer', true)
        const options = { project: server.project ?? project }
        const serverProject = server?.project
        if (serverProject && serverProject === project) {
          delete options.project
        }

        if (mock) AppOptions.instance.reset(options)
        else AppOptions.instance.update(options, true)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.viewport-container {
  position: fixed;
  background: #409eff;
  color: white;
  right: 0;
  bottom: 0;
  z-index: 100;
  opacity: 0.8;
  ::v-deep .el-card__body {
    padding: 5px;
    h3 {
      text-align: -webkit-center;
      min-width: 150px;
    }
  }
}
</style>
