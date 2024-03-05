<template>
  <el-popover
    v-model="popoverVisible"
    placement="bottom"
    trigger="click"
    width="300"
    popper-class="menu-popover"
    effect="light"
  >
    <div class="gridGroup flex">
      <div class="function-title">LAYOUT</div>
      <div class="p-0">
        <img
          v-for="g in grid"
          :key="g.gridOption"
          :src="g.src"
          class="gridIcon"
          :style="{'transform': g.gridOption === 'RightBar' ? 'rotate(180deg)' : 'rotate(0deg)'}"
          :class="{selected: layoutType === g.gridOption}"
          @click="onChangeSettings(g.gridOption, 'layoutType')"
        >
      </div>
    </div>
    <div>
      <div class="function-title">MENU BAR</div>
      <el-radio-group v-model="menuType" size="small" @change="(val) => onChangeSettings(val, 'menuType')">
        <el-radio-button label="TOP" />
        <el-radio-button label="LEFT" />
      </el-radio-group>
    </div>
    <div v-if="showSidebar">
      <div class="function-title">HIDE SIDE BAR</div>
      <el-switch
        v-model="isFixSide"
        active-text="SHOW"
        inactive-text="HIDE"
        @change="(val) => onChangeSettings(val, 'fixSide')"
      />
    </div>
    <div>
      <div class="function-title">BOTTOM BAR</div>
      <el-switch
        v-model="isShowBottombar"
        active-text="SHOW"
        inactive-text="HIDE"
        @change="(val) => onChangeSettings(val, 'bottombar')"
      />
    </div>

    <i slot="reference" class="el-icon-more" />
  </el-popover>
</template>

<script>
import { mapState } from 'vuex'
const routeName = 'CompPopover'

export default {
  name: routeName,
  components: { },
  data() {
    return {
      name: routeName,
      popoverVisible: false,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      menuType: '',
      isShowBottombar: '',
      layoutOption: 'TopBottom',
      isFixSide: true,
      grid: [
        { src: require('@/assets/icon/grid_tb.png'), gridOption: 'TopBottom' },
        { src: require('@/assets/icon/grid_lr.png'), gridOption: 'LeftRight' },
        { src: require('@/assets/icon/grid_leftbar.png'), gridOption: 'LeftBar' },
        { src: require('@/assets/icon/grid_leftbar.png'), gridOption: 'RightBar' }
      ]
    }
  },
  computed: {
    ...mapState({
      showBottombar: state => state.settings.bottombar,
      menubarType: state => state.settings.menuType,
      showSidebar: state => state.settings.menuType === 'LEFT',
      fixSide: state => state.settings.fixSide,
      layoutType: state => state.settings.layoutType,
      hideSidebar: state => state.settings.hideSidebar,
    }),
  },
  mounted () {
    this.menuType = this.menubarType
    this.isFixSide = this.fixSide
    this.isShowBottombar = this.showBottombar
  },
  created () {
  },
  methods: {
    onChangeSettings(changeVal, key) {
      window.helper.$store.dispatch('settings/changeSetting', {
        key,
        value: changeVal
      })
      if (key === 'menuType') {
        window.location.reload()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.menu-popover {
  div {
    row-gap: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 3px 0px;
    .function-title {
      font-size: 15px;
      font-weight: 700;
    }
  }
}
.gridGroup {
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
</style>
