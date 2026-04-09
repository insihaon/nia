<template>
  <div :class="[{ [name]: true }]">
    <!-- rightbar/top/bottom -->
    <SplitPane :min-percent="0" :default-percent="sidePaneSize" split="vertical" @resize="(val)=> resize(val, 'sidePaneSize')">
      <template slot="paneL">
        <SplitPane split="horizontal" :default-percent="topPaneSize" @resize="(val)=> resize(val, 'topPaneSize')">
          <template slot="paneL">
            <filterBar position="TOP" />
            <slot name="top-container" />
          </template>
          <template slot="paneR">
            <filterBar position="BOTTOM" />
            <slot name="bottom-container" />
          </template>
        </SplitPane>
      </template>
      <template slot="paneR">

        <slot name="right-container" />
      </template>
    </SplitPane>
  </div>

</template>
<script>
import { Base } from '@/min/Base.min'
import { mapState } from 'vuex'
import filterBar from '@/layout/components/filterBar'

const routeName = 'GridRightBar'

export default {
  name: routeName,
  components: { filterBar },
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
    })
  },
  methods: {
    resize(val, key) {
      this.$store.dispatch('settings/changeSetting', {
        key,
        value: val
      })
    },
  },
}
</script>

<style lang="scss" scoped>
</style>
