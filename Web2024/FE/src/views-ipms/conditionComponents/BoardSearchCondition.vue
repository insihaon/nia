<template>
  <fragment>
    <th>
      조회조건
    </th>
    <td class="textflex">
      <el-select
        v-model="value"
        size="small"
        @change="handleChange('searchCnd', value)"
      >
        <el-option
          v-for="(option, i) in options"
          :key="i"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
      <el-input v-model="word" size="small" :disabled="prop_disabled_condition(value)" clearable @change="handleChange('searchWrd', word)" />
    </td>
  </fragment>
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
    },
    prop_disabled_condition: {
      type: Function,
      default: () => {}
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
    onResetParameter() {
      this.value = this.defaultValue ?? 'title'
      this.word = ''
    },
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
