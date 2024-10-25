<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <td>
      <el-select
        v-model="value"
        collapse-tags
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

const routeName = 'ExtrnLnkgs'

export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '외부연계'
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sexLinkUseTypeCd',
      value: '',
      options: [
        { value: '', label: '전체' },
        { value: 'CE0000', label: '미분류' },
        { value: 'CE0001', label: 'NEOSS_CODE' },
        { value: 'CE0002', label: 'NEOSS_DATA' },
        { value: 'CE0003', label: 'DAIMS_DATA' },
        { value: 'CE0004', label: 'IFOMS_DATA' },
        { value: 'CE0005', label: '통합NMS_DATA' },
        { value: 'CE0006', label: '수작업데이터' },
        { value: 'CE0007', label: 'IDC_DATA' },
        { value: 'CE0008', label: 'IDMS_DATA' }

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
