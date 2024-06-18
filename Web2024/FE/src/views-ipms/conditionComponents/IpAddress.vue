<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
      @change="hangleChangeSelected"
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
    <el-input v-if="isShowInput" v-model="word" size="mini" clearable @change="handleChangeWord" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'IpAddress'
export default {
  name: routeName,
  extends: Base,
  props: {
    defaultValue: {
      type: String,
      default: null
    },
    label: {
      type: String,
      default: 'IP주소'
    },
    isShowInput: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: 'CV0001',
      word: ''
    }
  },
  computed: {
  },
  mounted () {
    if (this.defaultValue !== null) {
      this.value = this.defaultValue
    }
  },
  methods: {
    hangleChangeSelected() {
      this.$emit('update-value', [{ key: 'sipVersionTypeCd', value: this.value }])
    },
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'searchWrd', value: this.word }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
