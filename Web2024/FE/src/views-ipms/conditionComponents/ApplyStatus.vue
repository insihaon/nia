<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="value"
      size="mini"
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

const routeName = 'ApplyStatus'
export default {
  name: routeName,
  extends: Base,
  props: {
    label: {
      type: String,
      default: '상태'
    },
    propsOptions: {
      type: Array,
      default: null
    },
    parameterKey: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: [
        { label: '전체', value: '' },
        { label: '접수', value: 'nod001' },
        { label: '승인', value: 'nod002' },
        { label: '반려', value: 'nod003' },
      ]
    }
  },
  computed: {
  },
  created () {
    if (this.propsOptions !== null) {
      this.options = this.propsOptions
    }
  },
  methods: {
    handleChange() {
      if (this.parameterKey !== null) return
      this.$emit('update-value', [{ key: this.parameterKey, value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
  .el-select {
    width: 100%;
  }
</style>
