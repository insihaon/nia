<template>
  <fragment>
    <th style="width : 100px">
      {{ label }}
    </th>
    <td>
      <el-date-picker
        v-model="value"
        type="date"
        size="small"
        @change="handleChange()"
      />
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'DatePicker'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    defaultValue: {
      type: String,
      default: null
    },
    label: {
      type: String,
      default: '조회일자'
    },
    prop_parameterKey: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'searchBgDe', // searchSingleDate
      value: this.moment().subtract(1, 'd')
    }
  },
  methods: {
    onResetParameter() {
      this.value = this.moment().subtract(1, 'd')
    },
    getParameter() {
      return [{ key: this.parameterKey, value: this.moment(this.value).format('YYYY-MM-DD') }]
    },
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-date-editor {
  width: 100%;
}
</style>
