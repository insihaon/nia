<template>
  <fragment>
    <th>
      PROTOCOL
    </th>
    <td class="textflex">
      <el-select
        v-model="value"
        collapse-tags
        size="small"
        @change="handleChange()"
      >
        <el-option
          v-for="(option, i) in [
            { label: 'ICMP', value: 'I' },
            { label: 'TCP', value: 'T' },
          ]"
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

const routeName = 'Protocol'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sprotocolTypeCd',
      value: 'I'
    }
  },
  methods: {
    onResetParameter() {
      this.value = 'I'
    },
    setParameter(params) {
      this.value = params[this.parameterKey] ?? 'I'
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
