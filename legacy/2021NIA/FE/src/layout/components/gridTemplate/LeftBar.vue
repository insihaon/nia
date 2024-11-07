<template>
  <div :class="[{ [name]: true }]">
    <!-- leftbar/top/bottom -->
    <SplitPane :min-percent="0" :default-percent="sidePaneSize" split="vertical" @resize="(val)=> resize(val, 'sidePaneSize')">
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
    })
  },
  methods: {
    resize(val, key) {
      let value
      if (val < 25) {
        value = 25
      } else {
        value = val
      }
        window.helper.$store.dispatch('settings/changeSetting', {
        key: key,
        value: value
      })
    }
  },
}
</script>

<style lang="scss" scoped>
</style>
