<template>
  <el-header id="header-menu" class="dark bg-slate-900 flex h-20 p-0 w-full items-center">
    <div class="flex w-full h-full justify-between">
      <div class="flex items-center h-full" :class="{ 'headerWrapLogo': !isMobile }">
        <div v-if="isMobile" id="hamburger-container" class="d-flex" :class="{ 'justify-center': isActive, 'justify-end': !isActive }" @click="toggleSideBar">
          <i :class="{ 'el-icon-s-unfold': isActive, 'el-icon-s-fold': !isActive }" />
        </div>
        <div id="systeminfo-container">
          <div id="system-name" @click="onClickHeaderLogo()">NIA KOREN</div>
        </div>
        <!-- only parent -->
        <nav v-if="!isMobile" id="menu-bar" class="h-full">
          <el-menu class="flex h-full" :background-color="bgColor" :text-color="textColor" style="border-right: 0px">
            <el-menu-item
              v-for="route in permission_routes"
              v-if="isHidden(route) && route.meta"
              id="menu-item"
              :key="route.path"
              style="line-height: 4rem"
              class="h-full text-white font-semibold transition-colors"
            >
              <router-link v-if="route.meta" :to="route.path">
                {{ route.meta.title }}
              </router-link>
            </el-menu-item>
          </el-menu>
        </nav>
      </div>
      <div v-if="!isMobile && isViewport('>', 'md')" id="other-container" class="flex items-center">
        <div id="function-container">
          <svg-icon class="mr-2" type="mdi" :path="path" @click.native="toggleHistoryBar" />
        </div>
        <div id="user-info">
          <div class="d-flex items-baseline">
            <i class="el-icon-alarm-clock" />
            <span class="text-xs">{{ currentTime }}</span>
          </div>
          <div id="user" class="d-flex items-baseline justify-between">
            <div class="d-flex items-baseline">
              <i class="el-icon-user" />
              <span>{{ $store.state.user.name }}</span>
            </div>
            <span class="text-xs mr-2 cursor-pointer" @click="$refs.ModaluserSettings.open()">정보수정</span>
          </div>
        </div>
        <div id="logout" class="d-flex flex-column items-center" @click="$router.push({ path: '/login' })">
          <i class="el-icon-unlock text-3xl" />
          <button class="button h-5 px-3 bg-transparent text-white rounded" :disabled="buttonDisabled">
            <!-- @click="$router.push({ path: '/login' })" -->
            LogOut
          </button>
        </div>
      </div>
      <div v-else>
        <el-dropdown class="h-100" trigger="click" style="font-size: 26px">
          <div class="h-100 d-flex items-center" style="margin-right:10px">
            <i style="background: white;border-radius: 20px;padding: 5px;" class="el-icon-user" />
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="$refs.ModaluserSettings.open()">정보수정</el-dropdown-item>
            <el-dropdown-item divided @click.native="logout">
              <span style="display:block;">Log Out</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <!-- all child -->
    <div id="sub-menu">
      <el-menu id="top-inner" class="flex h-full text-white" style="border-right: 0px">
        <child-item v-for="(route, index) in permission_routes" :key="route.path" :base-path="route.path" :item="route" :idx="index.toString()" />
      </el-menu>
    </div>
    <ModaluserSettings ref="ModaluserSettings" :fullscreen="isViewport('<', 'sm')" />
  </el-header>
</template>

<script>
import { Base } from '@/min/Base.min'
import ChildItem from './ChildItem'
import { mdiHistory } from '@mdi/js'
import { mapState, mapGetters } from 'vuex'
import ModaluserSettings from '@/views-nia/userManagement/ModaluserSettings'

const routeName = 'NavBar'
export default {
  name: routeName,
  src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
  components: { ChildItem, ModaluserSettings },
  extends: Base,
  data() {
    return {
      bgColor: '#1e293b',
      textColor: '#bfcbd9',
      buttonDisabled: false,
      popoverVisible: false,
      path: mdiHistory,
      timeInterval: null,
      currentTime: null,
    }
  },
  computed: {
    isActive() {
      return this.sidebar.opened
    },
    ...mapGetters(['permission_routes', 'sidebar']),
  },
  created() {
    setInterval(() => {
      this.currentTime = this.toStringTime(Date.now(), 'YYYY.MM.DD A hh:mm:ss')
    }, 1000)
  },
  mounted() {
    this.setTime()
    const header = document.querySelector('#sub-menu')
    const menuItem = document.querySelectorAll('#menu-item')
    menuItem?.forEach((el) => {
      el.addEventListener('mouseover', (event) => {
        header.classList.add('open')
      })
    })
    header?.addEventListener('mouseover', function (event) {
      header.classList.add('open')
    })
    header?.addEventListener('mouseout', function (event) {
      header.classList.remove('open')
    })
  },
  methods: {
    isHidden(route) {
      if (route.path === '/manager') {
        return this.hasGrant(this.CONSTANTS.userGrant.ADMIN.value)
      } else {
        return !route.hidden
      }
    },
    hasGrant(grant) {
      const userAuth = Number(this.$store.state.user.info.lvl)
      return ((userAuth || 1) & grant) === grant
    },
    onClickHeaderLogo() {
      this.$router.push({ path: '/dashboard/index' })
    },
    toggleHistoryBar() {
      this.$store.dispatch('app/toggleHistoryBar')
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    setTime() {
      this.currentTime = this.toStringTime(Date.now(), 'YYYY.MM.DD A hh:mm:ss')
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';

#header-menu {
  transition: all 0.4s;
  background-color: rgb(30 41 59);
  #hamburger-container {
    color: white;
    font-size: 20px;
    padding: 0px 21px;
    height: 100%;
    align-items: center;
    &:hover {
      background: rgb(0 0 0 / 15%);
    }
  }
  #systeminfo-container {
    display: flex;
    align-items: center;
    font-size: 21px;
    font-weight: bold;
    padding-top: 5px;
    .lottie {
      width: 50px;
      height: 50px;
      // background: #101827;
      background: transparent;
    }
    #system-name {
      color: rgb(255, 255, 255);
    }
    #route-name {
      margin-left: 5px;
      font-size: 18px;
      color: #bebebe;
    }
  }
  #menu-bar {
    margin-left: 40px;
    min-width: 1350px;
    ul, li {
      transition: all 0.4s;
      letter-spacing: 1px;
    }
    li {
      text-align: center;
    }
  }
  ::v-deep #other-container {
    #user-info {
      height: 60px;
      width: 190px;
      border-left: 1px solid rgb(255 255 255 / 34%);
      i {
        margin-right: 5px;
        width: 28px;
        height: 28px;
        display: flex;
        border-radius: 50%;
        align-items: center;
        justify-content: center;
        transition: all 0.4s;
      }
    }
    #user {
      border-top: 1px solid rgb(255 255 255 / 34%);
    }
    #logout {
      width: 60px;
      height: 60px;
      border-left: 1px solid rgb(255 255 255 / 34%);
      &:hover {
        cursor: pointer;
      }
    }
    #function-container {
      svg,
      i {
        transition: all 0.4s;
        &:hover {
          scale: 1.2;
          font-size: 20px;
          cursor: pointer;
        }
      }
    }
    i,
    svg,
    span {
      color: white;
    }
  }
  #menu-item {
    font-size: 16px;
    min-width: 150px;
    transition: all 0.4s;
    padding: 0px !important;
    a:after {
      position: absolute;
      top: 100%;
      left: 0;
      width: 100%;
      height: 1px;
      background: $aiTemplateDefault;
      content: '';
      opacity: 0;
      transition: height 0.3s, opacity 0.3s, transform 0.3s;
      transform: translateY(-10px);
    }
    &:hover {
      a:after,
      a:focus:after {
        height: 4px;
        opacity: 1;
        transform: translateY(-3px);
      }
    }
  }
  #sub-menu {
    z-index: 3;
    height: 0px;
    width: 100%;
    padding-left: 170px;
    overflow: hidden;
    background-color: #eef0f3;
    transition: height, 0.25s linear;
    #top-inner {
      min-width: 1400px;
      padding-top: 8px;
      background-color: #eef0f3;
      li {
        min-width: 150px;
        text-align: center;
        padding: 5px;
        border-left: solid 1px #91939975;
      }
    }
  }
  .open#sub-menu {
    height: 250px;
    transition: height, 0.25s linear;
    box-shadow: 0 3px 3px rgba(0, 0, 0, 0.1);
  }

  &:hover {
    color: $aiTemplateDefault;
    background: #fff;
    box-shadow: 0 3px 3px rgba(0, 0, 0, 0.1);
    #menu-bar {
      ul {
        background-color: #fff !important;
      }
      li {
        color: $aiTemplateDefault !important;
        background-color: #fff !important;
      }
    }
    ::v-deep #other-container {
      .button,
      i,
      svg,
      span {
        color: $aiTemplateDefault !important;
      }
      #logout,
      #user-info {
        border-left: 1px solid $aiTemplateDefault;
      }
      #user {
        border-top: 1px solid $aiTemplateDefault;
      }
    }
    #hamburger-container {
      color: $aiTemplateDefault;
    }

    #system-name {
      background-clip: text;
      -webkit-background-clip: text;
      color: transparent !important;
      background-image: linear-gradient(180deg, $niaMainTitle) !important;
    }

    #route-name {
      margin-left: 5px;
      font-size: 18px;
      color: rgb(15, 15, 85) !important;

      span {
        color: #bebebe !important;
      }
    }
  }
}
</style>
