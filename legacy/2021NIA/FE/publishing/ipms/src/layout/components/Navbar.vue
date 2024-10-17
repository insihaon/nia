<template>
  <div class="navbar">
    <div class="navbar-logo">
      <img src="../../assets/images/top_logo_symbol.png">
      <img src="../../assets/images/top_logo.png">
      <div>
        <span>IP 주소 관리 시스템</span>
        <span>IP ADDRESS MANAGEMENT SYSTEM</span>
      </div>
    </div>
    <!-- 페이지 로케이션 -->
    <!-- <breadcrumb class="breadcrumb-container" /> -->

    <div class="left-menu">
      <el-menu class="top-menu" mode="horizontal" background-color="transparent" text-color="#00839b" active-text-color="#FFFFFF" @select="menuSelectHandler">
        <el-submenu index="1">
          <template slot="title">IP배정관리</template>
          <el-menu-item index="1-1">IP블록관리</el-menu-item>
          <el-menu-item index="1-2">IP배정관리2</el-menu-item>
          <el-menu-item index="1-3">팝업샘플</el-menu-item>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">IP할당관리</template>
          <el-menu-item index="2-1">IP할당관리1</el-menu-item>
          <el-menu-item index="2-2">IP할당관리2</el-menu-item>
          <el-menu-item index="2-3">IP할당관리3</el-menu-item>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">IP정보관리</template>
          <el-menu-item index="3-1">IP정보관리1</el-menu-item>
          <el-menu-item index="3-2">IP정보관리2</el-menu-item>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">IP통계관리</template>
          <el-menu-item index="4-1">IP통계관리1</el-menu-item>
          <el-menu-item index="4-2">IP통계관리2</el-menu-item>
          <el-menu-item index="4-3">IP통계관리3</el-menu-item>
        </el-submenu>
        <el-submenu index="5">
          <template slot="title">DB관리</template>
          <el-menu-item index="5-1">DB관리1</el-menu-item>
          <el-menu-item index="5-2">DB관리2</el-menu-item>
          <el-menu-item index="5-3">DB관리3</el-menu-item>
        </el-submenu>
        <el-submenu index="6">
          <template slot="title">게시판</template>
          <el-menu-item index="6-1">게시판1</el-menu-item>
          <el-menu-item index="6-2">게시판2</el-menu-item>
          <el-menu-item index="6-3">게시판3</el-menu-item>
          <el-menu-item index="6-4">게시판4</el-menu-item>
        </el-submenu>
        <el-submenu index="7">
          <template slot="title">운용정보관리</template>
          <el-menu-item index="7-1">운용정보관리1</el-menu-item>
          <el-menu-item index="7-2">운용정보관리2</el-menu-item>
          <el-menu-item index="7-3">운용정보관리3</el-menu-item>
          <el-menu-item index="7-4">운용정보관리4</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>

    <div class="right-menu">
      <div class="right-menu-item userInfo flex-h">
        <i class="xi-user" />
        <span>홍길동님으로 로그인 되었습니다</span>
      </div>
      <div class="right-menu-item logout flex-v" @click="logout">
        <i class="el-icon-lock" />
        <span>로그아웃</span>
      </div>
    </div>

  </div>
</template>

<script>
/* eslint-disable */
export default {
  name: "Navbar",
  components: {
    // Breadcrumb,
    // Hamburger
  },
  data() {
    return {
    }
  },
  computed: {
  },
  created() {
  },
  beforeDestroy() {
  },
  methods: {
    logout() {
      this.$router.push({ path: '/' })
    },
    menuSelectHandler(item) {
      console.log(item)
      switch(item){
        case "1-1" :
          this.$router.push({ path: '/page_1' })
          break;
        case "1-3" :
          this.$router.push({ path: '/page_popup' })
          break;
      }
    },
  }
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

  &-logo{
    padding-left:30px; padding-right:50px; box-sizing:border-box;
    flex-wrap: nowrap;
    display: flex;
    align-items: center;
    gap:10px;
    >div{
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      span{
        display:inline-block;
        width:100%;
        color:#FFFFFF;
        white-space: nowrap;
        user-select: none;
        cursor:default;
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
  .left-menu{
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
  overflow: hidden;
  /* background:linear-gradient(rgba(60,60,120,1), rgba(60,60,120,0)) !important; */
  background-color:#252531 !important;
  /* filter:drop-shadow(0px 20px 10px rgba(60,60,120,0.2)); */
  box-shadow: none !important;
}
.left-menu div.el-submenu__title{
  font-size:18px;
  padding-top:0;
  padding-bottom:0;
  height:70px !important;
  line-height:70px !important;
  border:none !important;
}
.left-menu div.el-submenu__title>i{
  display: none !important;
}
.left-menu div.el-submenu__title i.menuIcon{
  color:#FFFFFF; position:relative; top:-1px; font-size:20px; opacity: 0.75; font-weight: 200;
}
.left-menu div.el-submenu__title:hover i.menuIcon{
  opacity: 1;
}
.left-menu div.el-submenu__title:hover{
  background-color:transparent !important;
  color:#FFFFFF !important;
}
.left-menu li.is-opened div.el-submenu__title i.menuIcon{
  opacity: 1 !important;
}
.left-menu div.el-submenu__title:hover i{
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
li.el-menu-item{
  font-size:15px !important;
  text-align:center !important;
  text-indent: 0px !important;
  border-radius:20px !important;
  margin-left:10px; margin-right:10px;
  background-color:transparent !important;
}
li.el-menu-item:hover{
  /* background-color:#3c3c77 !important; */
  background:#FFFFFF !important;
  /* linear-gradient( 90deg, rgba(60,60,119,0), rgba(60,60,119,1), rgba(60,60,119,1), rgba(60,60,119,0)) !important; */
  color:#3c3c77 !important;
}
div.el-submenu__title{
  font-size:18px !important; 
}
</style>
