<template>
  <section class="app-main common-font" :style="{height: `calc(100vh - ${showBottombar ? '110': '60'}px)`}">

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
import EventBus from '@/utils/event-bus'

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
  .fixed-header {
    padding-right: 10px;
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
  width: 100%;
  position: relative;
  overflow: hidden;

  &>* {
    height: 100%;
    overflow: auto;
    width: 100%;
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
