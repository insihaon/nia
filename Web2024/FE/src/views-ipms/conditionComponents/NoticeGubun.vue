<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      공지구분
    </label>
    <el-select
      v-model="value"
      @change="handleChange()"
    >
      <el-option
        v-for="(option, i) in [
          { label: '전체', value: '' },
          { label: '일반', value: 'BM0001' },
          { label: '긴급', value: 'BM0002' },
        ]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'NoticeGubun'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sboardTypeSubCd',
      value: ''
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
