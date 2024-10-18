<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      조회년도
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
      @change="handleChange()"
    >
      <el-option
        v-for="(option, i) in [
          { label: '2024', value: '2024' },
          { label: '2023', value: '2023' },
        ]"
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

const routeName = 'CheckYear'

export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'yyyy',
      value: '2024'
    }
  },
  methods: {
    onResetParameter() {
      this.value = '2024'
    },
    setParameter(params) {
      this.value = params[this.parameterKey] ?? '2024'
    }
  },
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
