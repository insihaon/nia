<template>
  <fragment>
    <th>
      시설용 IP
    </th>
    <td>
      <el-checkbox v-model="value" @change="handleCheck" />
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'FacilitiesOption'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: false,
      parameterKey: 'isViewFacilites'
    }
  },
  methods: {
    init() {
      this.value = this.ipms?.isFacilitesAssign ?? false
      this.emitEventToParent(this.getParameter())
    },
    handleCheck(value) {
      // this.value = value
      this.$store.dispatch('ipms/setIsFacilitesOption', value)
      this.emitEventToParent([{ key: this.parameterKey, value }])
    },
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-checkbox__inner {
  border: 1px solid #16d9ff;
}
</style>
