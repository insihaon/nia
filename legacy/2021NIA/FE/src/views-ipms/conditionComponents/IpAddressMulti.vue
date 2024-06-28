<template>
  <el-col :class="{ [name]: true }" :span="24">
    <label>
      IP주소(멀티검색)
    </label>
    <el-input
      v-model="value"
      size="mini"
      type="textarea"
      class="mt-1"
      clearable
      @change="handleChange()"
    />
  </el-col>
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
