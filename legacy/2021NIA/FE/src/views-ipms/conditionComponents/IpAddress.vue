<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      IP주소
    </label>
    <el-select
      v-model="localValue"
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
    <el-input v-model="word" size="mini" clearable @change="handleChangeWord" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'IpAddress'
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
      word: ''
    }
  },
  computed: {
    localValue: {
      get() {
        return this.value
      },
      set(newValue) {
        this.$emit('set-value', newValue)
        this.$emit('update-value', [{ key: 'sipVersionTypeCd', value: newValue }])
      }
    },
  },
  methods: {
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'searchWrd', value: this.word }])
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
