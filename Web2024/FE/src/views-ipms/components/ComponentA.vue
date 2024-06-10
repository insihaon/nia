<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      공인/사설
    </label>
    <el-select
      v-model="searchSelectModel"
      collapse-tags
      filterable
      clearable
      size="mini"
      reserve-keyword
      remote
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
        { label: '유/무선공용', value: 3 },
        { label: '사설', value: 4 }
      ]
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
