<template>
  <div :class="{ [name]: true }">
    <el-input v-model="searchInputModel" size="mini" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'ComponentC'
export default {
  name: routeName,
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      searchInputModel: ''
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.ComponentC {
  height: auto;
  ::v-deep .el-range-editor.el-input__inner {
    width: 100%;
  }
}
</style>
