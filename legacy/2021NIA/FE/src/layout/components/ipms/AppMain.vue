<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in" :duration="duration">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
import { Base } from '@/min/Base.min'
import { AppOptions } from '@/class/appOptions'
import { mapState } from 'vuex'
export default {
  name: 'AppMain',
  components: { },
  extends: Base,
  data() {
    return {
      AppOptions: AppOptions
    }
  },
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    },
    duration() {
      return this.$store.state.tagsView.lazyCachedViews.includes(this.$route.name) ? 200 : 500
    },
    ...mapState({
      showBottombar: state => state.settings.bottombar
    }),
  },
  watch: {
    $route() {
      this.addTags()
    },
  },
  mounted() {
    window.main = this
  },
  beforeDestroy() {
  },
  methods: {
    addTags() {
      const { name } = this.$route
      if (name) {
        this.$store.dispatch('tagsView/addView', this.$route)
      }
      return false
    },
  }
}
</script>

<style scoped>
@import "../../../assets/fonts/pretendard/pretendard-gov.css";
.app-main {
  height: calc(100% - 50px);
  position: relative;
  padding: 0px 30px 30px 30px;
  box-sizing: border-box;
  border: none;
  overflow: hidden;
  transition:all 0.25s !important;
}
.app-main::-webkit-scrollbar {
  width: 20px; height:20px;
}
.app-main::-webkit-scrollbar-thumb {
  background-color: rgba(0,0,0,0.1);
  border-radius: 10px;
  background-clip: padding-box;
  border: 6px solid transparent;
  transition: all 0.25s;
}
.app-main::-webkit-scrollbar-thumb:hover{
  background-color:#406AFF;
}
.app-main::-webkit-scrollbar-track {
  background-color: transparent;
  border-radius: 5px;
}
.fixed-header+.app-main {
  padding-top: 70px;
}
.externalMenuShow{
  /* padding-top : calc(70px + 70px); */
  transition:all 0.25s !important;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}
.el-table::before{
  z-index:0;
}
</style>
<style lang="scss">
@import "~@/styles/variables.scss";

.el-notification.right{
  bottom: 16px !important;
}

// : var($fixSideBarWidth)
:root {
  --open-sidebar-width: 230px;
  --fix-sidebar-width: 65px;
  --navTopbar-height: 0px;
  --historybar-width: 150px;
  --tag-bar-height: 75px;
  --fixed-top-header-height: calc( var(--navTopbar-height) + var(--tag-bar-height));
}

// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  padding-right: 0px !important;
  .fixed-header {
    padding-right: 10px;
  }
}
</style>
