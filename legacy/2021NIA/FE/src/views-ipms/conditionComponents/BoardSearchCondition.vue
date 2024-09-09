<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      조회조건
    </label>
    <el-select
      v-model="value"
      size="mini"
      @change="handleChange('searchCnd', value)"
    >
      <el-option
        v-for="(option, i) in options"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <el-input v-model="word" size="mini" clearable @change="handleChange('searchWrd', word)" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'BoardSearchCondition'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    prop_options: {
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
      value: 'title',
      word: ''
    }
  },
  methods: {
    init() {
      if (this.prop_options !== null) {
        this.options = this.prop_options
      }
      if (this.defaultValue !== null) {
        this.value = this.defaultValue
      }
      this.emitEventToParent(this.getParameter())
    },
    handleChange(key, value) {
      this.emitEventToParent([{ key, value }])
    },
    getParameter() {
      return [{ key: 'searchCnd', value: this.value }, { key: 'searchWrd', value: this.word }]
    },
    setParameter(params) {
      this.value = params['searchCnd'] ?? ''
      this.word = params['searchWrd'] ?? ''
    }
  }
}
</script>
<style lang="scss" scoped>
  .el-select {
    width: 200px;
  }
</style>
