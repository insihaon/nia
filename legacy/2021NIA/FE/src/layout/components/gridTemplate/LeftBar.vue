<template>
  <div :class="[{ [name]: true }]">
    <!-- leftbar/top/bottom -->
    <SplitPane :min-percent="0" :default-percent="resizeSidePane " split="vertical" @resize="(val)=> resize(val, 'sidePaneSize')">
      <template slot="paneL">
        <slot name="leftbar-container" />
      </template>
      <template slot="paneR">
        <SplitPane :min-percent="5" split="horizontal" :default-percent="topPaneSize" @resize="(val)=> resize(val, 'topPaneSize')">
          <template slot="paneL" class="top-container">
            <!-- <filterBar position="TOP" /> -->
            <slot name="top-container" />
          </template>
          <template slot="paneR">
            <!-- <filterBar position="BOTTOM" /> -->
            <slot name="bottom-container" />
          </template>
        </SplitPane>
      </template>
    </SplitPane>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import { mapState } from 'vuex'
// import filterBar from '@/layout/components/filterBar'

const routeName = 'GridLeftBar'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
    }
  },
  computed: {
    ...mapState({
      topPaneSize: state => state.settings.topPaneSize,
      sidePaneSize: state => state.settings.sidePaneSize
    }),
    resizeSidePane () {
      return this.isViewport('<', 'lg') ? 0 : this.sidePaneSize
    },
  },
  methods: {
     resize(val, key) {
      this.$store.dispatch('settings/changeSetting', {
        key: key,
        value: val,
      })
    }
  },
}
</script>

<style lang="scss" scoped>
</style>
