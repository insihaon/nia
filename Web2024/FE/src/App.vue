<template>
  <div id="app" :class="appClass">
    <RouterView />
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
      expire: (state) => state.app.expire
    }),
    appClass() {
      return this.outline ? 'outline' : ''
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
