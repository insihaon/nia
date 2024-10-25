<template>
  <fragment>
    <th>
      IP주소(멀티검색)
    </th>
    <td>
      <el-input
        v-model="value"
        size="small"
        type="textarea"
        class="mt-1"
        clearable
        @change="handleChange()"
      />
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'IpAddressMulti'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'searchWrdMulti',
      value: '',
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
</style>
