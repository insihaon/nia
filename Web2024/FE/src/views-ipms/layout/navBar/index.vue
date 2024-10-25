<template>
  <div class="navbar">
    <div class="navbar-logo" @click="$router.push({ path: '/ipms/index' })">
      <img src="@/assets/images/ipms/top_logo_symbol.png">
      <img src="@/assets/images/ipms/top_logo.png">
      <div>
        <span>IP 주소 관리 시스템</span>
        <span>IP ADDRESS MANAGEMENT SYSTEM</span>
      </div>
    </div>
    <div class="top-nav-menu">
      <el-menu class="top-menu" mode="horizontal" background-color="transparent" text-color="#00839b" active-text-color="#FFFFFF">
        <el-submenu
          v-for="(route, index) in permission_routes"
          v-if="!route.hidden && route.meta"
          :key="route.path"
          :index="`${index}`"
        >
          <template slot="title">{{ route.meta.title }}</template>
          <template v-for="(child, childIndex) in route.children" v-if="route.children && child.meta">
            <!-- 1차 메뉴 -->
            <el-menu-item v-if="!child.children" :key="`${index}-${childIndex}`" :index="`${index}-${childIndex}`" @click="$router.push({ path: resolvePath(child.path, route.path) })">
              <router-link :to="resolvePath(child.path, route.path)">
                {{ child.meta.title }}
              </router-link>
            </el-menu-item>
            <!-- 2차 메뉴 -->
            <el-submenu v-else-if="child.children" :key="`${index}-${childIndex}`" :index="`${index}-${childIndex}`">
              <template slot="title">{{ child.meta.title }}</template>
              <template v-for="(childSub, chSubIndex) in child.children">
                <el-menu-item :key="`${index}-${childIndex}-${chSubIndex}`" :index="`${index}-${childIndex}-${chSubIndex}`" @click="$router.push({ path: resolvePath(childSub.path, child.path) })">
                  <router-link :to="resolvePath(childSub.path, child.path)">
                    {{ childSub.meta.title }}
                  </router-link>
                </el-menu-item>
              </template>
            </el-submenu>
          </template>
        </el-submenu>
      </el-menu>
    </div>
    <div class="right-menu">
      <div class="right-menu-item userInfo flex-h">
        <i class="el-icon-user-solid" />
        <span>{{ $store.state.user.name || '' }}님으로 로그인 되었습니다.</span>
      </div>
      <div class="right-menu-item logout flex-v" @click="logout">
        <i class="el-icon-lock" />
        <span>로그아웃</span>
      </div>
    </div>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import { mdiHistory } from '@mdi/js'
import { mapState, mapGetters } from 'vuex'
import path from 'path'
import { isExternal } from '@/utils/validate'

const routeName = 'NavBar'
export default {
  name: routeName,
  src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
  components: {},
  extends: Base,
  data() {
    return {}
  },
  computed: {
    ...mapGetters(['permission_routes']),
  },
  created() {
    // const header = document.querySelector('#gnb')
    // header?.addEventListener('mouseover', (event) => {
    //   console.log('a')
    // })
  },
  mounted() {
    console.log()
  },
  methods: {
    onMouseOver(event, menuIdx) {
      document.querySelector('#gnb').children[0].children[menuIdx].classList.add('on')
    },
    onMouseOut(event, menuIdx) {
      document.querySelector('#gnb').children[0].children[menuIdx].classList.remove('on')
    },
    resolvePath(routePath, basePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(basePath)) {
        return basePath
      }
      return path.resolve(basePath, routePath)
    },
  },
}
</script>
<style lang="scss" scoped>
// 상단 타이틀바
.navbar {
  height: 70px;
  overflow: hidden;
  position: relative;
  background: #252531;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 0px 30px rgba(0,0,0,0.05);
  img {
    max-width: fit-content;
  }
  &-logo{
    padding-left:30px; padding-right:50px; box-sizing:border-box;
    flex-wrap: nowrap;
    display: flex;
    align-items: center;
    gap:10px;
    cursor: pointer;
    >div{
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      span{
        display:inline-block;
        cursor: pointer;
        width:100%;
        color:#FFFFFF;
        white-space: nowrap;
        user-select: none;
        &:first-child{
          font-size:18.5px; font-weight:400;
        }
        &:last-child{
          font-size:8.7px; font-weight:300;
        }
      }
    }
  }
  .hamburger-container {
    line-height: 66px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }
  .breadcrumb-container {
    float: left;
  }
  .top-nav-menu{
    height:70px;
    width:100%;
    line-height: 70px;
    user-select: none;
  }
  .right-menu {
    height: 100%;
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    flex-direction: row;
    align-items: center;
    padding:0;

    // 유저정보
    div.userInfo{
      height:100%;
      white-space: nowrap;
      color:#FFFFFF;
      box-sizing: border-box;
      padding:0 15px;
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      cursor: default;
      user-select: none;
      border-left:1px solid rgba(255,255,255,0.1);
      i{
        font-size:20px;
        position:relative;
        margin-right:5px;
      }
      span{
        display: inline-block;
        font-size:14px;
      }
    }
    // 로그아웃 버튼
    div.logout{
      display: flex;
      flex-direction: column;
      width:70px;
      min-width: 70px;
      height:100%;
      min-height:100%;
      white-space: nowrap;
      text-align: center;
      box-sizing: border-box;
      color:#FFFFFF;
      cursor:pointer;
      transition: all 0.25s;
      align-items: center;
      justify-content: center;
      border-left:1px solid rgba(255,255,255,0.1);
      &:hover{
        background-color:rgba(255,255,255,0.1);
        i{
          color:#FFFFFF;
        }
        span{
          color:#FFFFFF;
        }
      }
      i{
        font-size:20px;
        color:rgba(255,255,255,0.5);
        transition: all 0.25s;
      }
      span{
        color:rgba(255,255,255,0.5);
        display: inline-block;
        font-size:14px;
        transition: all 0.25s;
      }
    }
  }
}

</style>
<style>
.el-menu--horizontal{
  /* background:linear-gradient(rgba(60,60,120,1), rgba(60,60,120,0), rgba(60,60,120,0)) !important; */
}
.el-menu--popup{
  top:-5px !important;
  padding:10px 0px 20px 0px !important;
  border-radius: 0px 0px 24px 24px !important;
  /* overflow: hidden; */
  /* background:linear-gradient(rgba(60,60,120,1), rgba(60,60,120,0)) !important; */
  background-color:#252531 !important;
  /* filter:drop-shadow(0px 20px 10px rgba(60,60,120,0.2)); */
  box-shadow: none !important;
}
.top-nav-menu div.el-submenu__title{
  font-size:18px;
  padding-top:0;
  padding-bottom:0;
  height:70px !important;
  line-height:70px !important;
  border:none !important;
}
.top-nav-menu div.el-submenu__title>i{
  /* 메뉴우측 화살표 아이콘 감추기 */
  display: none !important;
}
.top-nav-menu div.el-submenu__title i.menuIcon{
  color:#FFFFFF; position:relative; top:-1px; font-size:20px; opacity: 0.75; font-weight: 200;
}
.top-nav-menu div.el-submenu__title:hover i.menuIcon{
  opacity: 1;
}
.top-nav-menu div.el-submenu__title:hover{
  background-color:transparent !important;
  color:#FFFFFF !important;
}
.top-nav-menu li.is-opened div.el-submenu__title i.menuIcon{
  opacity: 1 !important;
}
.top-nav-menu div.el-submenu__title:hover i{
  color:#FFFFFF !important;
}

.top-menu{
  height:70px; border:none !important;
  display: flex; flex-wrap: nowrap;
}
.top-menu li{
  height:70px;
  padding:0 10px;
}
/* 1차 메뉴 */
div.top-nav-menu>ul.top-menu>li.el-submenu>div.el-submenu__title{
  font-size:18px !important;
}
/* 2차 메뉴 */
div.el-menu--horizontal>ul.el-menu>li.el-menu-item,
div.el-menu--horizontal>ul.el-menu>li.el-submenu>div.el-submenu__title{
  font-size:15px !important;
  text-align: center !important;
  font-weight: 500 !important;
  text-indent: 0px !important;
  margin-left:10px;
  margin-right:10px;
  background-color:transparent !important;
  border-radius:20px !important;
}
div.el-menu--horizontal>ul.el-menu>li.el-menu-item:hover,
div.el-menu--horizontal>ul.el-menu>li.el-submenu>div.el-submenu__title:hover{
  background:#FFFFFF !important;
  color:#3c3c77 !important;
}
</style>
