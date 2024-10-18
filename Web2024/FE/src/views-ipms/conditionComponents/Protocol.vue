<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      PROTOCOL
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
      @change="handleChange()"
    >
      <el-option
        v-for="(option, i) in [
          { label: 'ICMP', value: 'I' },
          { label: 'TCP', value: 'T' },
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

const routeName = 'Protocol'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sprotocolTypeCd',
      value: 'I'
    }
  },
  methods: {
    onResetParameter() {
      this.value = 'I'
    },
    setParameter(params) {
      this.value = params[this.parameterKey] ?? 'I'
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
