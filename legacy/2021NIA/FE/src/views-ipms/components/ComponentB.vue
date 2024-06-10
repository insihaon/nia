<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      작업일자
    </label>
    <el-date-picker
      v-model="searchtimeModel"
      type="daterange"
      size="mini"
      start-placeholder="시작일"
      end-placeholder="종료일"
    />
  </el-col>
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
