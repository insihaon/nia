<template>
  <div :class="[classObj]" class="layout-wrapper common-font" :style="setHeight">
    <div v-if="screenDevice==='mobile'" class="drawer-bg" @click="handleClickOutside" />
    <div class="main-container" :class="[{hasTagsView:needTagsView}, AppOptions.instance.project]">
      <div :class="{ 'fixed-header':fixedHeader }">
        <NavBar ref="navbar" />
        <AppMain v-if="!popupLayout" ref="appmain" />
        <BottomBar ref="bottombr" />
      </div>
    </div>
    <el-card v-if="showWindowSize" class="viewport-container" shadow="always">
      <h6>{{ viewSize }}</h6>
    </el-card>
    <CompTrafficAnalysisModal ref="CompTrafficAnalysisModal" :fullscreen="isViewport('<', 'sm')" />

  </div>
</template>

<script>
import { AppOptions } from '@/class/appOptions'
import AppMain from '@/layout/components/nia/AppMain'
import { Base } from '@/min/Base.min'
import ResizeMixin from '@/layout/mixin/ResizeHandler'
import NavBar from './navBar/index'
import BottomBar from './BottomBar'
import CompTrafficAnalysisModal from '@/views-nia/modal/CompTrafficAnalysisModal'

import { mapState, mapGetters } from 'vuex'

export const _ = { AppOptions }

const routeName = 'Layout'

export default {
  name: routeName,
  components: {
    AppMain,
    NavBar,
    BottomBar,
    CompTrafficAnalysisModal,
  },
  extends: Base,
  mixins: [ResizeMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      AppOptions: AppOptions,
      closeNoticeSeq: [],
      popupNoticeArray: [],
    }
  },
  computed: {
    ...mapState({
      roles: state => state.user.roles,
    }),
    ...mapGetters([
      'permission_routes',
      'sidebar',
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
      return this.$route?.meta?.title ?? ''
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    ...mapState({
      historybar: state => state.app.historybar,
      showHistorybar: state => state.app.historybar.opened,
      fixedHeader: state => state.settings.fixedHeader,
      screenDevice: state => state.app.screenDevice,
      viewport: state => state.app.viewport,
      windowSize: state => state.app.windowSize,
      showWindowSize: state => state.app.showWindowSize,
      showSettings: state => state.settings.showSettings,
      needTagsView: state => state.settings.tagsView,
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
        // hideSidebar: this.sidebar.opened,
        // openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.screenDevice === 'mobile',
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
  },
  mounted() {
    //  this.subscribeEvent()
    this.$nextTick(() => {
      this.setShowBottombar()
    })
  },
  methods: {
    handleOpenEditModal(row, type) {
        this.$refs.ModaluserSettings.open({ row: row, type: type })
    },
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
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
    },
    setShowBottombar() {
      window.helper?.$store.dispatch('settings/changeSetting', {
        key: 'bottombar',
        value: true
      })
    },
    userDetail() {

    }
  }
}
</script>
<style lang="scss">
@import "~@/assets/css/style_main.css";
  .common-padding{
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
    width: 100%;
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
  .container {
  text-align: center;
  position: relative;
  width: 300px;
  margin: 0 auto;
  cursor: pointer;
}
.viewport-container {
  position: fixed;
  background: #409eff;
  color: white;
  right: 0;
  bottom: 0;
  z-index: 100;
  opacity: 0.8;
  ::v-deep .el-card__body{
    padding: 5px;
    h3 {
      text-align: -webkit-center;
      min-width: 150px;
    }
  }
}
</style>

