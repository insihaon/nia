<template>
  <!-- <div :class="{ [name]: true }"> -->
  <div class="CompVirtualList">
    <el-select
      ref="selectBox"
      v-model="modelValue"
      :filter-method="onFilterValue"
      :popper-class="dataList.length < 10 ? 'non-data virtual-list':'virtual-list'"
      autocomplete="off"
      filterable
      :disabled="disabled"
      :allow-create="allowCreate"
      no-match-text=" "
      no-data-text=" "
      default-first-option
      @change="handleSelectChange(type)"
      @focus="onFocus"
      @visible-change="onVisibleChange"
    >
      <virtual-list
        :ref="type"
        style="overflow-y: auto; width: 300px;"
        :style="{ height:height }"
        :data-key="'value'"
        :data-sources="userSearchFilter"
        :data-component="itemComponent"
      />
    </el-select>
  </div>
  <!-- </div> -->
</template>

<script>
import Item from './CompItem.vue'
import { Base } from '@/min/Base.min'
import VirtualList from 'vue-virtual-scroll-list'
import _ from 'lodash'

const routeName = 'CompVirtualList'

export default {
  name: routeName,
  components: { VirtualList },
  extends: Base,
  props: {
    dataList: {
      type: Array,
      default() {
        return []
      }
    },
    filterItem: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    allowCreate: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      itemComponent: Item,
      scrollPosition: 0,
      filterData: [],
      filterValue: '',
      modelValue: this.filterItem
    }
  },
  computed: {
    height() {
      return this.dataList.length === 1 ? '50px' : '260px'
    },
    userSearchFilter() {
      const { filterData, dataList, filterValue } = this
      return filterValue.length === 0 ? dataList : filterData
    }
  },
  watch: {
    filterItem(newValue) {
      this.modelValue = newValue
    }
  },
  methods: {
    handleSelectChange(type) {
      this.filterValue = ''
      this.$emit('onChangeSelectValue', { type: type, value: this.modelValue })
    },
    onFilterValue(filterValue) {
      this.$refs[this.type]?.scrollToOffset()
      this.onFilterDebounce(filterValue, this)
    },
    onFilterDebounce: _.debounce(function(filterValue, THIS) {
      THIS.$refs[THIS.type]?.scrollToOffset()
      THIS.filterValue = filterValue
      THIS.filterData = THIS.dataList.filter(v => v?.value.toUpperCase().includes((filterValue ?? '').toUpperCase()))
    }, 500),
    onFocus() {
      this.filterValue = ''
    },
    onVisibleChange(visible) {
      const { THIS } = this
      if (visible) {
        // scroll position restore
        setTimeout(() => {
          // this.openLoading(target)
          if (THIS.scrollPosition !== 0) {
            this.$refs[this.type].$el.scrollTop = THIS.scrollPosition
          } else {
            const index = this.dataList.findIndex(node => node.value === this.filterItem)
            THIS.$refs[THIS.type].scrollToIndex(index)
          }
        }, 150)
      } else {
        // scroll position save
        THIS.scrollPosition = this.$refs[this.type].$el.scrollTop
      }
    }
  }
}
</script>
<style lang="scss">
.virtual-list {
  min-width: fit-content !important;
  .el-scrollbar__bar.is-vertical {
    display: none;
  }
  .el-scrollbar{
    // height: 270px;
    .el-select-dropdown__list{
      height: 100%;
    }
  }
  .el-scrollbar__wrap{
    overflow-x: hidden;
    overflow-y: scroll;
    margin-bottom: 0px !important;
  }
}
.non-data.virtual-list {
  width: 200px;
  .el-scrollbar{
    height: fit-content;

    .el-select-dropdown__list > div {
      height: fit-content !important;
    }
  }
}
</style>
