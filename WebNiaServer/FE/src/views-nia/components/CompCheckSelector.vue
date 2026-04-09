<template>
  <el-select
    ref="checkSelector"
    v-model="selectLabel"
    :collapse-tags="true"
    :style="setInsertStyle"
    :disabled="item.disabled || item.readonly"
    reserve-keyword
    multiple
    size="mini"
    :placeholder="item.placeholder"
    filterable
    remote
    @change="(value)=> devEmit('selectedChange', value)"
  >
    <el-option
      v-if="eval('item?.setting?.allOption?.toggle')"
      label="전체"
      value="ALL"
    >
      <span
        style="width: 100%; height: 100%; display: block;"
        :value="toggleAll"
        @click.prevent="onClickAll"
      >
        전체
      </span>
    </el-option>
    <el-option
      v-for="(option, i) in item.options"
      :key="i"
      :label="option.label"
      :value="option.value"
    >
      <span
        style="width: 100%; height: 100%; display: block;"
        @click.prevent="onClickOption(option)"
      >
        {{ option.label }}
      </span>
    </el-option>
  </el-select>
</template>

<script>
const routeName = 'CompCheckSelector'
import { Base } from '@/min/Base.min'
import ComponentTesterMixins from '@/test/ComponentTesterMixins'

export default {
  name: routeName,
  components: {},
  extends: Base,
  mixins: [ComponentTesterMixins],
  props: {
    item: {
      type: Object,
      default() {
        return {}
      }
    },
    searchModel: {
      type: Array,
      default() { return [] }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`
    }
  },
  computed: {
    isSettingAllOption() {
      return this.item?.setting?.allOption?.toggle
    },

    model: {
      get() {
        // if (this.isSettingAllOption && this.searchModel.length === this.fullOptions.length) {
        //   this.devEmit('update:searchModel', ['ALL'])
        // }
        return this.searchModel
      },
      set(m) {
        const nM = m.filter((d) => d !== 'ALL')
        if (this.isSettingAllOption && nM.length === this.fullOptions.length) {
          if (this.item.valueConsistsOf === 'LEAF_PRIORITY') {
            this.devEmit('update:searchModel', this.fullOptions)
          } else {
            this.devEmit('update:searchModel', ['ALL'])
          }
        } else {
          this.devEmit('update:searchModel', nM)
        }
      }
    },
    selectLabel: {
      get() {
        return this.isSettingAllOption && this.toggleAll ? ['ALL'].concat(this.fullOptions) : this.model
      },
      set() { /* 사용안하지만 정의는 필요함 */ }
    },
    setInsertStyle() {
      return {
        '--visible-affix-tag': this.isSettingAllOption && this.toggleAll ? 'none' : 'block'
      }
    },

    fullOptions() { return this.item?.options?.map((v) => { return v.value }) },
    toggleAll() {
      if (this.item.valueConsistsOf === 'LEAF_PRIORITY') {
        return this.model.length === this.item.options.length
      } else {
        return this.model.length === 1 && this.model[0] === 'ALL'
      }
    }
  },

  watch: {
  },

  mounted() {
  },
  methods: {
    getDefaultProps() {
    },

    eval(p) {
      // eslint-disable-next-line no-eval
      return eval('this.' + p)
    },
    onClickOption(option) {
      const tempModel = this._cloneDeep(this.toggleAll ? this.fullOptions : this.model)
      const existIndex = tempModel.findIndex((v) => { return v === option.value })
      if (existIndex !== -1) {
        tempModel.splice(existIndex, 1)
      } else {
        tempModel.push(option.value)
      }
      this.model = tempModel
    },
    onClickAll() {
      this.model = this.toggleAll ? [] : this.fullOptions
    }
  }

}

</script>

<style lang="scss">
.el-tag__close.el-icon-close{
  display: none;
}

.el-select__tags {
  span{
    .el-tag--info:nth-child(2){
      display: var(--visible-affix-tag) ;
    }
  }
}

</style>
