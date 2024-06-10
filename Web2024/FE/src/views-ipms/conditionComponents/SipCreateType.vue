<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      공인/사설
    </label>
    <el-select
      v-model="value"
      collapse-tags
      filterable
      clearable
      size="mini"
      reserve-keyword
      remote
      @change="handleChange"
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

const routeName = 'SipCreateType'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: String,
      default: ''
    },
    exceptOptions: { /* 예외처리 option */
      type: Object,
      default() { return {} }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      options: [
        { label: '공인', value: 'CT0001' },
        { label: 'Bogon', value: 'CT0003' },
        { label: '유/무선공용', value: 'CT0004' },
        { label: '사설', value: 'CT0005' }
      ]
    }
  },
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'sipCreateTypeCd', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
