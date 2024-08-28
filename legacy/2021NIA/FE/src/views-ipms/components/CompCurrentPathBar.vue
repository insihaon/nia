<template>
  <transition name="fade-transform" mode="out-in" :duration="duration">
    <div
      v-if="$route.name !== 'IpmsMain'"
      id="content"
    >
      <div class="page_tit">
        <h2>{{ getCurPageSubTitle }}{{ getCurPageTitle }}</h2>
        <p class="path">
          <span class="home">HOME</span>
          <span>{{ getCurPageParentTitle }}</span>
          <strong>{{ getCurPageTitle }}</strong>
        </p>
      </div>
    </div>
  </transition>
</template>
<script>
import { Base } from '@/min/Base.min'
import { mapGetters } from 'vuex'

const routeName = 'CompCurrentPathBar'

export default {
  name: routeName,
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
    }
  },
  computed: {
    ...mapGetters([
      'permission_routes'
    ]),
    duration() {
      return this.$store.state.tagsView.lazyCachedViews.includes(this.$route.name) ? 200 : 500
    },
    getCurPageTitle() {
      return this.$route?.meta?.title ?? ''
    },
    getCurPageSubTitle() {
      return this.$route?.meta?.subTitle ?? ''
    },
    getCurPageParentTitle() {
      const parantPath = this.$route.path.split('/')[1]
      const parantRoute = this.permission_routes.find(v => v.path === `/${parantPath}`)
      return parantRoute?.meta?.title ?? ''
    },
  },
}
</script>
<style lang="scss" scoped></style>
