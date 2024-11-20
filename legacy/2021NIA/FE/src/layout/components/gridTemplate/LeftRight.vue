<template>
  <div :class="[{ [name]: true }]">
    <!-- left/right -->
    <SplitPane :min-percent="0" split="vertical" :default-percent="topPaneSize" @resize="resize">
      <template slot="paneL">
        <filterBar position="TOP" :size-by-type="sizeByType" />
        <slot name="left-container" />
      </template>
      <template slot="paneR">
        <filterBar position="BOTTOM" :size-by-type="sizeByType" />
        <slot name="right-container" />
      </template>
    </SplitPane>
  </div>

</template>
<script>
import { Base } from '@/min/Base.min'
import { mapState } from 'vuex'
import filterBar from '@/layout/components/filterBar'

const routeName = 'GridLeftRight'

export default {
  name: routeName,
  components: { filterBar },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      sizeByType: {
        TOP: {
          MIN: 0,
          MAX: 100
        },
        BOTTOM: {
          MIN: 100,
          MAX: 0
        }
      }
  }
  },
  computed: {
    ...mapState({
      topPaneSize: state => state.settings.topPaneSize
    })
  },
  methods: {
    resize(val) {
      console.log(val)
        this.$store.dispatch('settings/changeSetting', {
        key: 'topPaneSize',
        value: val
      })
    }
  },
}
</script>

<style lang="scss" scoped>

</style>
