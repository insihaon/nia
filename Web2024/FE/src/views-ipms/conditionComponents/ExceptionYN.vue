<template>
  <fragment>
    <th>
      예외여부
    </th>
    <td>
      <el-select
        v-model="value"
        size="small"
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

const routeName = 'ExceptionYN'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    defaultValue: {
      type: String,
      default: null
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sexcpt_yn',
      value: '',
      options: [
        { label: '전체', value: '' },
        { label: '예외처리', value: 'Y' },
        { label: '예외처리 제외', value: 'N' },
      ]
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
      this.emitEventToParent(this.getParameter())
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
