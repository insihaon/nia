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
      @change="handleChange"
    />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'DateRange'
export default {
  name: routeName,
  extends: Base,
  props: {
    defaultValue: {
      type: Array,
      default: null
    },
    label: {
      type: String,
      default: '작업일자'
    },
    propsParameterKey: {
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
  created () {
    if (this.defaultValue !== null && Array.isArray(this.defaultValue)) {
      this.value = this.defaultValue
    }
    if (this.propsParameterKey !== null && Array.isArray(this.propsParameterKey)) {
      this.parameterKey = this.propsParameterKey
    }
  },
  methods: {
    handleChange() {
      const [searchBgnDe, searchEndDe] = this.value
      const [bgKey, endKey] = this.parameterKey
      this.$emit('update-value', [
        { key: bgKey, value: this.moment(searchBgnDe).format('YYYY-MM-DD') },
        { key: endKey, value: this.moment(searchEndDe).format('YYYY-MM-DD') }
      ])
   }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-range-editor.el-input__inner {
  width: 100%;
}
</style>
