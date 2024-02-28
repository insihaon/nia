<template>
  <div :class="[{ [name]: true }]">
    <component :is="componentLoader" class="h-full" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import { mapState } from 'vuex'
import TopBottom from '@/layout/components/gridTemplate/TopBottom'
import LeftRight from '@/layout/components/gridTemplate/LeftRight'
import LeftBar from '@/layout/components/gridTemplate/LeftBar'
import RightBar from '@/layout/components/gridTemplate/RightBar'
const routeName = 'aiTemplate'

export default {
  name: routeName,
  components: { TopBottom, LeftRight, LeftBar, RightBar },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
    }
  },
  computed: {
    ...mapState({
      layoutType: state => state.settings.layoutType,
    }),
    componentLoader() {
      const layoutName = this.layoutType
      switch (layoutName) {
        case 'TopBottom':
          return TopBottom
        case 'LeftRight':
          return LeftRight
        case 'LeftBar':
          return LeftBar
        case 'RightBar':
          return RightBar
        default:
          return TopBottom
      }
    }
  },
  methods: {
  },
}
</script>

<style lang="scss" scoped>
.aiTemplate {
  height: 100%;
  padding: 5px;
  ::v-deep .splitter-paneL, ::v-deep .splitter-paneR {
    border-radius: 5px;
    border: solid 1px #363636;
    transition: all 0.4s;
  }
  .gridGroup {
    height: 25px;
    padding: 5px 0;
    i {
      margin-left: 5.5px;
      filter: contrast(0.5);
    }
    .gridIcon {
      cursor: pointer;
      width: 17px;
      margin-left: 4px;
      height: 17px;
      filter: contrast(0);
    }

    .gridIcon.selected {
      filter: contrast(0.8);
    }

    .gridIcon:hover {
      filter: contrast(0.5);
    }
  }
}
</style>
