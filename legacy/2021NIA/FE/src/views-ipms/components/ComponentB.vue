<template>
  <div :class="{ [name]: true }">
    <el-date-picker
      v-model="searchtimeModel"
      type="daterange"
      size="mini"
      start-placeholder="시작일"
      end-placeholder="종료일"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'ComponentB'
export default {
  name: routeName,
  extends: Base,
  props: {

  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      searchtimeModel: []
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
