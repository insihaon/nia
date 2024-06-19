<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      조회조건
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
    <el-input v-model="word" size="mini" clearable @change="handleChangeWord" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'BoardSearchCondition'
export default {
  name: routeName,
  extends: Base,
  props: {
    propsOptions: {
      type: Array,
      default: null
    },
    defaultValue: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      options: [
        { label: '제목', value: 'title' },
        { label: '내용', value: 'content' },
        { label: '작성자', value: 'user' },
      ],
      value: '',
      word: ''
    }
  },
  created () {
    if (this.propsOptions !== null) {
      this.options = this.propsOptions
    }
    if (this.defaultValue !== null) {
      this.value = this.defaultValue
    }
  },
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'searchCnd', value: this.value }])
    },
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'searchWrd', value: this.word }])
    }
  }
}
</script>
<style lang="scss" scoped>
  .el-select {
    width: 200px;
  }
</style>
