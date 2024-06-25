<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      {{ label }}
    </label>
    <el-date-picker
      v-model="value"
      type="daterange"
      size="mini"
      start-placeholder="시작일"
      end-placeholder="종료일"
      @change="handleChange()"
    />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'DateRange'

export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    defaultValue: {
      type: Array,
      default: null
    },
    label: {
      type: String,
      default: '작업일자'
    },
    prop_parameterKey: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: [],
      parameterKey: ['searchBgnDe', 'searchEndDe']
    }
  },
  methods: {
    init() {
      if (this.defaultValue !== null && Array.isArray(this.defaultValue)) {
        this.value = this.defaultValue
      }
      if (this.prop_parameterKey !== null && Array.isArray(this.prop_parameterKey)) {
        this.parameterKey = this.prop_parameterKey
      }
      this.emitEventToParent(this.getParameter())
    },
    getParameter() {
      const [searchBgnDe, searchEndDe] = this.value
      const [bgKey, endKey] = this.parameterKey
      return [
        { key: bgKey, value: searchBgnDe ? this.moment(searchBgnDe).format('YYYY-MM-DD') : '' },
        { key: endKey, value: searchEndDe ? this.moment(searchEndDe).format('YYYY-MM-DD') : '' }
      ]
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-range-editor.el-input__inner {
  width: 100%;
}
</style>
