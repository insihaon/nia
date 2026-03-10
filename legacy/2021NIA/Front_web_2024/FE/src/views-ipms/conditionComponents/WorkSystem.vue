<template>
  <fragment>
    <th>
      작업시스템
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

const routeName = 'WorkSystem'

export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sworkSystem',
      value: '',
      options: [
        {
          value: '',
          label: '전체',
        },
        {
          value: 'IPMS',
          label: 'IPMS',
        },
        {
          value: 'NeOSS',
          label: 'NeOSS',
        },
        {
          value: 'SDN',
          label: 'SDN',
        },
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
