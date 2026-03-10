<template>
  <fragment>
    <th>
      상태
    </th>
    <td>
      <el-select
        v-model="value"
        size="small"
        @change="handleChange()"
      >
        <el-option
          v-for="(option, i) in options"
          :key="i"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'IpMngProgress'

export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sflag',
      value: '',
      options: [
        { label: '전체', value: '' },
        { label: '추가 신청', value: 'Y' },
        { label: '삭제 신청', value: 'N' },
        { label: '처리 완료', value: 'C' },
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
