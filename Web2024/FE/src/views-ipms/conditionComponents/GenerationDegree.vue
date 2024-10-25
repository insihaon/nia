<template>
  <fragment>
    <th>
      생성차수
    </th>
    <td>
      <el-select
        v-model="value"
        collapse-tags
        filterable
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

const routeName = 'GenerationDegree'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sipCreateSeqCd',
      value: '',
      options: [
        { label: '전체', value: '' },
        { label: 'G202411001', value: 'G202411001' },
        { label: 'G202412000', value: 'G202412000' },
        { label: 'K199401000', value: 'K199401000' },
        { label: 'K199402000', value: 'K199402000' },
        { label: 'K199501000', value: 'K199501000' },
        { label: 'K199601000', value: 'K199601000' },
        { label: 'K199602000', value: 'K199602000' },
        { label: 'K199603000', value: 'K199603000' },
        { label: 'K199604000', value: 'K199604000' },
        { label: 'K199701000', value: 'K199701000' },
        { label: 'K199702000', value: 'K199702000' },
        { label: 'K199703000', value: 'K199703000' },
        { label: 'K199704000', value: 'K199704000' },
        { label: 'K199705000', value: 'K199705000' },
        { label: 'K199706000', value: 'K199706000' },
        { label: 'K199707000', value: 'K199707000' }
      ],
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
