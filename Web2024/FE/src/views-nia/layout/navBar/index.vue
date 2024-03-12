<template>
  <el-header id="header-menu" class="dark bg-slate-900 flex h-20 p-0 w-full items-center">
    <div class="flex w-full h-full justify-between ">
      <div class="headerWrapLogo flex items-center h-full">
        <div id="systeminfo-container">
          <div id="system-name" @click="onClickHeaderLogo()">NIA KOREN </div>
          <div id="route-name"> <span>|</span> {{ activeMenuTitle }}</div>
        </div>
        <!-- only parent -->
        <nav id="menu-bar" class="h-full ml-14">
          <el-menu class="flex h-full" :background-color="bgColor" :text-color="textColor" style="border-right: 0px">
            <el-menu-item
              v-for="route in permission_routes"
              v-if="!route.hidden"
              id="menu-item"
              :key="route.path"
              style="line-height: 4rem;"
              class="h-full text-white font-semibold transition-colors"
            >
              <router-link v-if="route.meta" :to="route.path">
                {{ route.meta.title }}
              </router-link>
            </el-menu-item>
          </el-menu>
        </nav>
      </div>
      <div id="function-container" class="flex items-center">
        <svg-icon type="mdi" :path="path" @click.native="toggleHistoryBar" />
        <button
          class="button h-10 px-4 py-2 bg-transparent text-white rounded"
          :disabled="buttonDisabled"
          @click="$router.push({ path: '/login' })"
        >
          LogOut
        </button>
      </div>
    </div>
    <!-- all child -->
    <div id="sub-menu">
      <el-menu id="top-inner" class="flex h-full text-white" style="border-right: 0px">
        <child-item v-for="(route, index) in permission_routes" :key="route.path" :base-path="route.path" :item="route" :idx="index.toString()" />
      </el-menu>
    </div>
  </el-header>
</template>

<script>
import ChildItem from './ChildItem'
import { mdiHistory } from '@mdi/js'
import { mapState, mapGetters } from 'vuex'

const routeName = 'NavBar'
export default {
  name: routeName,
  components: { ChildItem },
  data() {
    return {
      bgColor: '#1e293b',
      textColor: '#bfcbd9',
      buttonDisabled: false,
      popoverVisible: false,
      path: mdiHistory,
    }
  },
  computed: {
    activeMenuTitle() {
      return this.$route?.meta?.title ?? ''
    },
    ...mapGetters([
    'permission_routes',
    ]),
    ...mapState({
    }),
  },
  mounted () {
    const header = document.querySelector('#sub-menu')
    const menuItem = document.querySelectorAll('#menu-item')
    menuItem?.forEach((el) => {
      el.addEventListener('mouseover', (event) => {
        header.classList.add('open')
      })
    })
    header?.addEventListener('mouseover', function(event) {
      header.classList.add('open')
    })
    header?.addEventListener('mouseout', function(event) {
      header.classList.remove('open')
    })
  },
  methods: {
    onClickHeaderLogo() {
      this.$router.push({ path: '/dashboard/index' })
    },
    toggleHistoryBar() {
      this.$store.dispatch('app/toggleHistoryBar')
    },
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";

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
    ul, li {
      transition: all 0.4s;
      letter-spacing: 1px;
    }
  }
  ::v-deep #function-container {
    i {
      margin-right: 5px;
      width: 35px;
      height: 35px;
      display: flex;
      border-radius: 50%;
      align-items: center;
      justify-content: center;
      transition: all 0.4s;
      &:hover {
        font-size: 20px;
        cursor: pointer;
      }
    }
    svg {
      transition: all 0.4s;
      &:hover {
        scale: 1.2;
        cursor: pointer;
      }
    }
    i, svg {
      color: white;
    }
  }
  #menu-item {
    font-size: 16px;
    padding: 0px !important;
    margin: 0 15px;
    transition: all 0.4s;
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
      a:after, a:focus:after {
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
    padding-left: 330px;
    overflow: hidden;
    background-color: #eef0f3;
    transition: height, 0.25s linear;
    #top-inner {
      padding-top: 8px;
      background-color: #eef0f3;
    }
  }
  .open#sub-menu  {
    height: 300px;
    transition: height, 0.25s linear;
    box-shadow: 0 3px 3px rgba(0,0,0,0.1);
  }

  &:hover {
    color: $aiTemplateDefault;
    background: #fff;
    box-shadow: 0 3px 3px rgba(0,0,0,0.1);
    #menu-bar {
      ul {
        background-color: #fff !important;
      }
      li {
        color: $aiTemplateDefault !important;
        background-color: #fff !important;
      }
    }
    ::v-deep #function-container {
      .button, i, svg {
        color: $aiTemplateDefault !important;
      }
    }
    #hamburger-container {
      color: $aiTemplateDefault
    }

    #system-name {
      background-clip: text;
      -webkit-background-clip: text;
      color: transparent !important;
      background-image: linear-gradient(
        180deg,
        $niaMainTitle
      ) !important;
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
