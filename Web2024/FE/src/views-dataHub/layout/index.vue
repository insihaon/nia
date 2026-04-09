<template>
  <div class="layout-wrapper common-font" :style="setHeight">
    <div v-if="screenDevice==='mobile'" class="drawer-bg" @click="handleClickOutside" />
    <div class="main-container" :class="[{hasTagsView:needTagsView}, AppOptions.instance.project]">
      <div :class="{'fixed-header':fixedHeader}" @dblclick="handleDoubleClick">
        <div class="headerWrap">
          <div class="headerWrapLogo">
            <router-link :to="{ name: 'datahubMain' }">
              <span style="font-weight: bold;">{{ '데이터허브' }} </span>
              <span>관리시스템</span>
            </router-link>
          </div>
          <!-- <span class="menuTitle">{{ activeTitle }}</span> -->
          <div class="headerMenu">
            <ul>
              <li v-for="menu in dataHubRoute" v-show="!menu.hidden" :key="menu.path">
                <span>{{ menu.meta.title }}</span>
                <ul>
                  <template v-for="childMenu in menu.children">
                    <li v-show="!childMenu.hidden" v-if="hasPermission(childMenu)" :key="childMenu.path">
                      <router-link :to="{name : childMenu.name}">
                        <div>
                          <span>{{ childMenu.meta.title }}</span>
                        </div>
                      </router-link>
                    </li>
                  </template>
                </ul>
              </li>
            </ul>
          </div>
          <div class="headerUserInfo">
            <span>{{ loginUsername }}님 안녕하세요</span>
          </div>
          <!-- <div class="headerLogOut">
            <span>로그아웃</span>
          </div> -->
          <currentMenu v-if="false" />
        </div>
        <AppMain v-if="!popupLayout" />
      </div>
    </div>
  </div>
</template>

<script>
import { AppOptions } from '@/class/appOptions'
import { Base } from '@/min/Base.min'
import AppMain from '@/layout/components/dataHub/AppMain'
import currentMenu from '@/layout/components/dataHub/currentMenu'
import ResizeMixin from '@/layout/mixin/ResizeHandler'
import { dataHubRoute } from '@/router/dataHub/index'

import { mapState, mapGetters } from 'vuex'
import hotkeys from 'hotkeys-js'

import { apiGetUserIDFromUserNm } from '@/api/auth'
import { findIntersection } from '@/utils'
// import VueResizable from 'vue-resizable'

export const _ = { AppOptions }

const routeName = 'Layout'

export default {
  name: routeName,
  components: {
    AppMain,
    currentMenu,

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
        // activeTitle() {
        //   return this.$route?.meta?.title
        // },
      dataHubRoute() {
        // console.log(dataHubRoute)
        return dataHubRoute
      },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    ...mapState({
      sidebar: state => state.app.sidebar,
      screenDevice: state => state.app.screenDevice,
      viewport: state => state.app.viewport,
      windowSize: state => state.app.windowSize,
      showWindowSize: state => state.app.showWindowSize,
      showSettings: state => state.settings.showSettings,
      needTagsView: state => state.settings.tagsView,
      fixedHeader: state => state.settings.fixedHeader,
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
        // hideSidebar: this.sidebar.opened,
        // openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
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
    }
  },
  watch: {
    $route(to, from) {
      if (to.path !== from.path) this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  },

  async created() {
    hotkeys('alt+h+1', 'debug', (e, h) => { e.preventDefault(); this.onChangeHeader(1) })
    hotkeys('alt+h+2', 'debug', (e, h) => { e.preventDefault(); this.onChangeHeader(2) })
    hotkeys('alt+h+3', 'debug', (e, h) => { e.preventDefault(); this.onChangeHeader(3) })
    hotkeys('alt+h+4', 'debug', (e, h) => { e.preventDefault(); this.onChangeHeader(4) })
  },
  mounted() {
     this.subscribeEvent()
  },
  methods: {
    hasPermission(item) {
      // if (item?.meta?.grant) {
      //   console.log(item.path)
      // }

      const menuRoles = item?.meta?.grant ?? ['ROLE_ADMIN', 'ROLE_USER']
      const myRoles = this.roles ?? ['ROLE_USER']

      const intersection = findIntersection(menuRoles, myRoles)
      return intersection.length > 0
    },
    handleDoubleClick(event) {
      // var a = document.createElement('A')
      // var fileName = 'history.csv'
      // a.href = `${fileName}`
      // a.download = fileName
      // document.body.appendChild(a)
      // a.click()
      // document.body.removeChild(a)
    },
    async getNMSUID() {
      try {
        /* nms용 계정 */
        const loginID = window.localStorage.getItem('STORED_LOGIN_ID') === null ? '91187283' : window.localStorage.getItem('STORED_LOGIN_ID')
        this.$store.commit('user/SET_NMS_LOGINID', loginID)
        const res = await apiGetUserIDFromUserNm({ LOGIN_ID: loginID })
        const userID = res?.result[0].USER_ID
        this.$store.commit('user/SET_NMS_UID', userID)
      } catch (e) {
        console.error(e)
      }
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
    }
  }
}

</script>

<style lang="scss">
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
    transition: width 0.28s;
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
