<template>
  <section class="app-main common-font">
    <HistoryBar ref="historybar" />
    <div v-if="isMobile && isOpenHistorybar" class="drawer-bg" @click="handleClickOutside" />
    <transition name="fade-transform" mode="out-in" :duration="duration">
      <keep-alive :include="cachedViews">
        <router-view :key="key" :style="{ 'padding-right': isMobile ? '0px' : getHistoryOffset }" style="transition: all 0.3s" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
import { Base } from '@/min/Base.min'
import { AppOptions } from '@/class/appOptions'
import HistoryBar from '@/layout/components/historyBar/index'
import { mapState } from 'vuex'

export default {
  name: 'AppMain',
  components: { HistoryBar },
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
      showBottombar: state => state.settings.bottombar,
      historybar: state => state.app.historybar,
    }),
    isOpenHistorybar() {
      return this.historybar.opened
    },
    getHistoryOffset() {
      return this.historybar.opened ? 'var(--historybar-width)' : 'var(--historybar-default-width)'
    },
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
    handleClickOutside() {
      this.$store.dispatch('app/closeHistoryBar')
    },
  }
}
</script>

<style lang="scss">

.el-notification.right{
  bottom: 16px !important;
}

:root {
    --navTopbar-height: 0px;
    --historybar-width: 180px;
    --historybar-default-width: 10px;
    --tag-bar-height: 75px;
    --fixed-top-header-height: calc( var(--navTopbar-height) + var(--tag-bar-height));
}

// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 0px;
  }
}

.message-link {
  color: #007bff !important;
  text-decoration: none;
  float: right !important;
}

.message-link:hover {
  text-decoration: underline;
}
</style>

<style lang="scss" scoped>
.app-main {
  font-family: "NotoSansKR";
  height: calc(100vh - 80px);

  width: 100%;
  position: relative;
  overflow: hidden;
  // transition: all 0.3s;

  &>* {
    height: 100%;
    overflow: auto;
    width: 100%;
  }
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

body.el-popup-parent--hidden .app-main {
  z-index: 1;
}

.fixed-header+.app-main {
  padding-top: var(--navTopbar-height);
}

.hasTagsView {
  .fixed-header+.app-main {
    padding-top: var(--fixed-top-header-height);
  }
}

:not(.hasTagsView) .app-main{
  padding-top: var(--navTopbar-height);
}

.showBottombar {
  .app-main {
    height: calc(100vh - 100px);
  }
  .fixed-header+.app-main,
  .hasTagsView .fixed-header+.app-main{
    height: calc(100vh - 48px);
  }
  .hasTagsView .app-main {
    height: calc(100vh - 134px);
  }
}

.showBottombar:not(.viewport_lg):not(.viewport_xl) {
  .app-main {
    height: calc(100vh - 150px);
  }
  .fixed-header+.app-main,
  .hasTagsView .fixed-header+.app-main{
    height: calc(100vh - 92px);
  }
  .hasTagsView .app-main {
    height: calc(100vh - 184px);
  }
}

.hideBottombar {
  .app-main {
    height: calc(100vh - 71px)
  }
  .hasTagsView .fixed-header+.app-main,
  .fixed-header+.app-main {
    height: 100vh;
  }
  .hasTagsView .app-main {
    height: calc(100vh - var(--fixed-top-header-height));
  }
}

</style>
