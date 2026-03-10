import store from '@/store'
import { pxToViewport } from '@/utils'

const { body } = document
const WIDTH = 992 // refer to Bootstrap's responsive design

export default {
  watch: {
    $route(route) {
      if (this.screenDevice === 'mobile' && this.sidebar.opened) {
        store.dispatch('app/closeSideBar', { withoutAnimation: false })
      }
    }
  },
  beforeMount() {
    window.addEventListener('resize', this.$_resizeHandler)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.$_resizeHandler)
  },
  mounted() {
    this.$_resizeHandler()
  },
  methods: {
    // use $_ for mixins properties
    // https://vuejs.org/v2/style-guide/index.html#Private-property-names-essential

    $_isMobile(width) {
      return width - 1 < WIDTH
    },
    $_resizeHandler() {
      if (!document.hidden) {
        const rect = body.getBoundingClientRect()
        const isMobile = this.$_isMobile(rect.width)
        store.dispatch('app/setScreenDevice', isMobile ? 'mobile' : 'desktop')
        store.dispatch('app/setViewport', pxToViewport(rect.width))

        if (isMobile) {
          store.dispatch('app/closeSideBar', { withoutAnimation: true })
        }
      }
    }
  }
}
