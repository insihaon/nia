<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="10" :xl="10">
    <label>
      진행상태
    </label>
    <el-select
      v-model="value"
      size="mini"
      @change="handleChange()"
    >
      <el-option
        v-for="(option, i) in options"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    &nbsp;※ 라우팅 수집을 새로하면 기존 진행상태가 초기화 됩니다.
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'Progress'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sdbIntgrmRsltCd',
      value: '',
      options: [
        { label: '전체', value: '' },
        { label: 'IP블록 크기 변경 실패', value: 'DR0001' },
        { label: 'IP블록 크기 변경 성공', value: 'DR0002' },
        { label: '할당/해지 작업 실패', value: 'DR0003' },
        { label: 'IPMS, 장비 완전 일치', value: 'DR0004' },
      ]
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
</style>
