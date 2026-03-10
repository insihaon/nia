<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <td>
      <el-select
        v-model="value"
        size="small"
        :disabled="disabled"
        @change="handleChange()"
      >
        <el-option
          v-for="(option, i) in options"
          :key="i"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'IncludeYN'

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
      default: '사용여부'
    },
    prop_parameterKey: {
      type: String,
      default: 'suseTypeCd'
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
       options: [
        { label: '전체', value: '' },
        { label: 'Y', value: 'Y' },
        { label: 'N', value: 'N' },
       ],
       value: ''
    }
  },
  methods: {
    onResetParameter() {
      this.value = this.defaultValue ?? ''
    },
    init() {
      if (this.defaultValue !== null) {
        this.value = this.defaultValue
      }
      if (this.prop_parameterKey && this.prop_parameterKey !== null) {
        this.parameterKey = this.prop_parameterKey
      }
      this.emitEventToParent(this.getParameter())
    },
  }
}
</script>
<style lang="scss" scoped>
  .el-select {
    width: 100%;
  }
</style>
