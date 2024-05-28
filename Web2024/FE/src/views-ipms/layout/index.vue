<template>
  <div id="ipms" :class="[classObj]" class="layout-wrapper common-font" :style="setHeight">
    <div @click="handleClickOutside" />
    <div class="main-container" :class="[{hasTagsView:needTagsView}, AppOptions.instance.project]">
      <div :class="{'fixed-header':fixedHeader}" :style="{ width: `calc(100% - ${getOffsetWidth})`, 'padding-right': getHistoryOffset }" @dblclick="handleDoubleClick">
        <AppMain v-if="!popupLayout" ref="appmain" />
      </div>
    </div>
  </div>
</template>
<script>
import { AppOptions } from '@/class/appOptions'
import AppMain from '@/layout/components/aiTemplate/AppMain'
import { Base } from '@/min/Base.min'
import ResizeMixin from '@/layout/mixin/ResizeHandler'
import { mapState, mapGetters } from 'vuex'
import hotkeys from 'hotkeys-js'

const routeName = 'Layout'

export default {
  name: routeName,
  components: {
    AppMain,
  },
  extends: Base,
  mixins: [ResizeMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      AppOptions,
      closeNoticeSeq: [],
      popupNoticeArray: [],
    }
  },
  computed: {
        ...mapState({
          roles: state => state.user.roles,
        }),
        ...mapGetters([
          // 'permission_routes',
          // 'sidebar',
        ]),
        loginUsername() {
          const userNM = this.username ? this.username.replace(/.$/, '*') : 'UNKONWN'
          return userNM
        },
        activeMenu() {
            const route = this.$route
            const { meta, path } = route
            // if set path, the sidebar will highlight the path you set
            if (meta.activeMenu) {
              return meta.activeMenu
            }
          return path
        },
        activeTitle() {
          return this.$route?.meta?.title
        },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    ...mapState({
      sidebar: state => state.app.sidebar,
      historybar: state => state.app.historybar,
      showHistorybar: state => state.app.historybar.opened,
      screenDevice: state => state.app.screenDevice,
      viewport: state => state.app.viewport,
      windowSize: state => state.app.windowSize,
      showWindowSize: state => state.app.showWindowSize,
      showSettings: state => state.settings.showSettings,
      showBottombar: state => state.settings.bottombar,
      showSidebar: state => state.settings.menuType === 'LEFT',
      needTagsView: state => state.settings.tagsView,
      fixedHeader: state => state.settings.fixedHeader,
      fixSide: state => state.settings.fixSide,
      navbarOnly: state => state.settings.navbarOnly,
      popupLayout: state => state.settings.popupLayout,
      size: state => state.app.size,
      username: state => state.user.name,
      templateVariables: state => state.aamPersisted.templateVariables,
      commonSelectListData: state => state.aamPersisted.commonSelectListData,
    }),
    viewSize() {
      return `${this.viewport.toUpperCase()} ${this.windowSize.width}x${this.windowSize.height}`
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        showSidebar: this.showSidebar,
        mobile: this.screenDevice === 'mobile',
        navbarOnly: this.navbarOnly,

        [`viewport_${this.viewport}`]: false,
        en: this.$i18n?.locale === 'en'
      }
    },
    setHeight() {
      let widthPadding
      let heightPadding
      if (this.isViewport('<=', 'md')) {
        widthPadding = 0
        heightPadding = 0
      } else {
        widthPadding = 15
        heightPadding = 80
      }
        return {
        '--common-padding': widthPadding + 'px ' + heightPadding + 'px'
      }
    },
    getOffsetWidth() {
      if ((!this.fixSide && !this.sidebar.opened) || !this.showSidebar) {
        // fixside true: sidbar 고정 , false: 0px
        return '0px'
      } else {
        return this.sidebar.opened ? 'var(--open-sidebar-width)' : '0px' // 'var(--fix-sidebar-width)'
      }
    },
    getHistoryOffset() {
      return this.historybar.opened ? 'var(--historybar-width)' : '0px'
    }

  },
  watch: {
    $route(to, from) {
      // if (to.path !== from.path) this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  },
  async created() {
    hotkeys(`alt+b`, 'debug', function(event, handler) {
      event.preventDefault()
      window.helper.$store.dispatch('settings/changeSetting', {
        key: 'bottombar',
        value: !window.helper.$store.state.settings.bottombar
      })
    })
  },
  mounted() {
      this.subscribeEvent()
  },
  methods: {
    handleDoubleClick(event) {
    },
    handleClickOutside() {
      // this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    subscribeEvent() {
      this.addWsEventListener(this.CONSTANTS.channels.HEARTBEAT.name, this.onReceiveHeartbeat)
    },
    onReceiveHeartbeat({ channelName, socketMessage }) {
      this.$store.dispatch('user/setConnectCount', socketMessage)
      // this.log('onReceiveHeartbeat : count=' + count)
    },
    onReceiveNotice({ channelName, socketMessage }) {
      const message = JSON.parse(socketMessage.message)
      const data = message.properties

      this.$refs.preview.open(data)
    }
  }
}

</script>

<style lang="scss">
  .common-padding {
    padding: var(--common-padding);
    position: relative;
    height: 100%;
    width: 100%;
  }

  .common-font{
    font-family: "NotoSansKR";
  }

</style>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";

  .router-link-active {
    color: #93c3ed !important;
  }
  .layout-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
  }

  body.el-popup-parent--hidden .fixed-header {
    z-index: 1;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    transition: all 0.28s;
    ul.el-menu {
      border-bottom: none !important;

      .el-menu-item.is-active {
        font-weight: bold; /* 활성화된 메뉴 항목의 텍스트를 굵게 표시 */
      }
    }
  }

  .en .fixed-header {
    width: calc(100% - 250px);
  }

  .viewport-container {
    position: fixed;
    background: #409eff;
    color: white;
    right: 0;
    bottom: 0;
    z-index: 100;
    opacity: 0.8;
  }
</style>

<style lang="scss" scoped>
@import "~@/assets/css/style_main.css";

.viewport-container > .el-card__body {
  padding: 5px;

  h3 {
    text-align: -webkit-center;
    min-width: 150px;
    // background-color: #606364;
  }
}

</style>
