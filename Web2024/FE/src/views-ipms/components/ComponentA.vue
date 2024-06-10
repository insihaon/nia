<template>
  <div :class="{ [name]: true }">
    <el-select
      v-model="searchSelectModel"
      collapse-tags
      filterable
      clearable
      size="mini"
      :placeholder="item.placeholder"
      reserve-keyword
      remote
    >
      <el-option
        v-for="(option, i) in item.options"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'ComponentA'
export default {
  name: routeName,
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      searchSelectModel: '',
      options: [
        { label: '공인', value: 1 },
        { label: 'Bogon', value: 2 },
        { label: '유/무선공용', value: 3 }
      ]
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.ComponentA {
  height: auto;
  ::v-deep .el-range-editor.el-input__inner {
    width: 100%;
  }
}
</style>
