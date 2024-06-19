<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-input v-model="word" size="mini" clearable @change="handleChangeWord" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'InputType'
export default {
  name: routeName,
  extends: Base,
  props: {
    label: {
      type: String,
      default: ''
    },
    propsParameterKey: {
      type: String,
      default: null
    },
    componentKey: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      word: '',
      parameterKey: 'searchWrd'
    }
  },
  created () {
    if (this.propsParameterKey !== null) {
      this.parameterKey = this.propsParameterKey
    }
  },
  methods: {
    handleChangeWord() {
      this.$emit('update-value', [{ key: this.parameterKey, value: this.word }])
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
