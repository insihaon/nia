<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="localValue"
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

const routeName = 'IncludeYN'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: String,
      default: ''
    },
    label: {
      type: String,
      default: ''
    },
    componentKey: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
       options: [
        { label: '전체', value: 'ALL' },
        { label: 'Y', value: 'Y' },
        { label: 'N', value: 'N' },
       ],
       localValue: []
    }
  },
  computed: {
  },
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: this.componentKey, value: this.localValue }])
    }
  }
}
</script>
<style lang="scss" scoped>
  .el-select {
    width: 100%;
  }
</style>
