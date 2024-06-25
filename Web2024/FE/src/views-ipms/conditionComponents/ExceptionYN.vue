<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="10" :xl="10">
    <label>
      예외여부
    </label>
    <el-select
      v-model="value"
      size="mini"
      @change="handleChange()"
    >
      <el-option
        v-for="(option, i) in options"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'ExceptionYN'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
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
  }
}
</script>
<style lang="scss" scoped>
</style>
