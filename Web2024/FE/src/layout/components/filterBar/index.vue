<template>
  <div :class="[{ [name]: true }]">
    <div id="filter-container" class="w-full flex flex-nowrap justify-between">
      <slot id="function-container" name="function-container">ADD FILTER FUNCTION</slot>
      <div id="resize-container" class="px-1">
        <img src="@/assets/icon/reset.png" class="w-6 h-6" @click="onChangeSettings('RESET')">
        <img src="@/assets/icon/minimize.png" class="w-6 h-6" @click="onChangeSettings('MIN')">
        <img src="@/assets/icon/maximize.png" class="w-6 h-6" @click="onChangeSettings('MAX')">
      </div>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'filterBar'

export default {
  name: routeName,
  extends: Base,
  props: {
    position: {
      type: String,
      default: 'top',

    },
    sizeByType: {
      type: Object,
      default() {
        return {
          TOP: {
            MIN: 5,
            MAX: 95
          },
        /*
          vue-splitpane에서는 top size가 기준이기 때문에 bottom size는 top과 반대로 지정한다.
        */
          BOTTOM: {
            MIN: 95,
            MAX: 5
          }
        }
      },
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      defaultSize: 50,
      minSize: 5,
      maxSize: 95,
    }
  },
  methods: {
    onChangeSettings(changeType) {
      const changeVal = this.sizeByType[this.position][changeType]
      window.helper.$store.dispatch('settings/changeSetting', {
        key: 'topPaneSize',
        value: changeVal
      })
    }
  },
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";

#filter-container {
  height: 35px;
  overflow: hidden;
  align-items: center;
  background-color: #94a3b838;
  #function-container {
    padding: 0px 10px;
    display: flex;
    align-items: center;
  }
  #resize-container {
    display: flex;
    align-items: center;
    img {
      transition: all 0.3s;
      width: 25px;
      height: 25px;
      padding: 2px;
      border-radius: 5px;
      &:hover {
        cursor: pointer;
        background-color: #cbd5e1;
      }
    }
  }
}
</style>
