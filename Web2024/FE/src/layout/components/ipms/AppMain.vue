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
    selectedColNewItem(params, type) {
      EventBus.$emit('selectedNewCol', params, 'modalSelected')
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
</style>

<style lang="scss" scoped>
.app-main {
  font-family: "NotoSansKR";
  width: 100%;
  height: calc(100% - 50px);
  // padding: 0px 50px;
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
</style>
