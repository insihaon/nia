<template>
  <div :class="[{ [name]: true }]">
    <!-- top/bottom -->
    <split-pane :min-percent="5" split="horizontal" :default-percent="topPaneSize" @resize="resize">
      <template slot="paneL">
        <filterBar position="TOP" />
        <slot name="top-container" />
      </template>
      <template slot="paneR">
        <filterBar position="BOTTOM" />
        <slot name="bottom-container" />
      </template>
    </split-pane>
  </div>

</template>
<script>
import { Base } from '@/min/Base.min'
import { mapState } from 'vuex'
import filterBar from '@/layout/components/filterBar'

const routeName = 'GridTopBottom'

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
      topPaneSize: state => state.settings.topPaneSize
    })
  },
  methods: {
    resize(val) {
        window.helper.$store.dispatch('settings/changeSetting', {
        key: 'topPaneSize',
        value: val
      })
    }
  },
}
</script>

<style lang="scss" scoped>

</style>
