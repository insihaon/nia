<template>
  <div :class="[classObj]" class="layout-wrapper common-font" :style="setHeight">
    <SideBar ref="sidebar" />
    <div v-if="isMobile && isActive" class="drawer-bg" @click="handleClickOutside" />
    <div class="main-container" :class="[{ hasTagsView: needTagsView }, AppOptions.instance.project]">
      <div :class="{ 'fixed-header': fixedHeader }">
        <NavBar ref="navbar" />
        <div v-if="$route.name !== 'NiaMain'" class="cur-title-container" :style="{ 'background-color': $route.name.includes('Monitoring') ? '#1f335d' : '#fff', color: $route.name.includes('Monitoring') ? '#fff' : '#141414' }">
          <div>
            <span><i class="el-icon-document" />홈 > {{ getCurPageParentTitle }} > </span>
            <span>{{ getCurPageTitle }}</span>
          </div>
        </div>
        <AppMain v-if="!popupLayout" ref="appmain" :style="{ height: appMainHeight }" />
        <BottomBar ref="bottombar" />
      </div>
    </div>
    <WindowBase v-for="window in $store.getters.windows" :key="window.id" :type="window.type" :wdata="window" :target="window.target" />
  </div>
</template>

<script>
import { AppOptions } from '@/class/appOptions'
import AppMain from '@/layout/components/nia/AppMain'
import { Base } from '@/min/Base.min'
import NavBar from './navBar/index'
import SideBar from './sideBar/index'
import BottomBar from './BottomBar'
import WindowBase from '@/views-nia/layout/components/WindowBase'
import ResizeMixin from '@/layout/mixin/ResizeHandler'

import { mapState, mapGetters } from 'vuex'

export const _ = { AppOptions }

const routeName = 'Layout'

export default {
  name: routeName,
  components: {
    AppMain,
    NavBar,
    SideBar,
    BottomBar,
    WindowBase,
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
    ...mapGetters(['permission_routes']),

    appMainHeight() {
      return `calc(100vh - ${this.$route.name === 'NiaMain' ? '110' : '155'}px)`
    },

    getCurPageTitle() {
      return this.$route?.meta?.title ?? ''
    },
    getCurPageParentTitle() {
      const parantPath = this.$route.path.split('/')[1]
      const parantRoute = this.permission_routes.find((v) => v.path === `/${parantPath}`)
      return parantRoute?.meta?.title ?? ''
    },
    loginUsername() {
      const userNM = this.username ? this.username.replace(/.$/, '*') : 'UNKONWN'
      return userNM
    },
    isActive() {
      return this.sidebar.opened
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
      roles: (state) => state.user.roles,
      sidebar: (state) => state.app.sidebar,
      historybar: (state) => state.app.historybar,
      showHistorybar: (state) => state.app.historybar.opened,
      fixedHeader: (state) => state.settings.fixedHeader,
      screenDevice: (state) => state.app.screenDevice,
      showSettings: (state) => state.settings.showSettings,
      needTagsView: (state) => state.settings.tagsView,
      popupLayout: (state) => state.settings.popupLayout,
      size: (state) => state.app.size,
      username: (state) => state.user.name,
      templateVariables: (state) => state.aamPersisted.templateVariables,
      commonSelectListData: (state) => state.aamPersisted.commonSelectListData,
    }),
    classObj() {
      return {
        // hideSidebar: this.sidebar.opened,
        // openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.screenDevice === 'mobile',
        [`viewport_${this.viewport}`]: false,
        en: this.$i18n?.locale === 'en',
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
        '--common-padding': widthPadding + 'px ' + heightPadding + 'px',
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
        value: true,
      })
    },
    userDetail() {},
  },
}
</script>
<style lang="scss">
@import '~@/styles/nia_confirm.scss';
.common-padding {
  padding: 15px /* var(--common-padding) */;
  position: relative;
  height: 100%;
  width: 100%;
}

.common-font {
  font-family: 'NotoSansKR';
}
</style>

<style lang="scss" scoped>
@import '~@/styles/mixin.scss';
@import '~@/styles/variables.scss';
@import '~@/assets/css/nia_style_main.css';

.router-link-active {
  color: #93c3ed !important;
}
.layout-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }
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
.cur-title-container {
  width: 100%;
  height: 45px;
  padding: 15px 20px 5px 20px;
  div {
    padding-bottom: 5px;
    border-bottom: solid 1px #eeeeee;
    i {
      padding-right: 5px;
    }
    span:nth-child(1) {
      font-weight: 500;
      font-size: 13px;
    }
    span:nth-child(2) {
      font-weight: 600;
      font-size: 15px;
    }
  }
}

.container {
  text-align: center;
  position: relative;
  width: 300px;
  margin: 0 auto;
  cursor: pointer;
}
</style>
