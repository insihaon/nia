<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      IP주소
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
    >
      <el-option
        v-for="(option, i) in [
          { label: 'IPv4', value: 'CV0001' },
          { label: 'IPv6', value: 'CV0002' },
        ]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <el-input v-model="textValue" size="mini" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'ComponentC'
export default {
  name: routeName,
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: 'CV0001',
      textValue: ''
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.ComponentC {
  height: auto;
  ::v-deep .el-range-editor.el-input__inner {
    width: 100%;
  }
}
</style>
