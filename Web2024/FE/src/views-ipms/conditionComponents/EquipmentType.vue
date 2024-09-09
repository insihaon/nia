<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
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
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'EquipmentType'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '장비타입'
    },
    prop_options: {
      type: Array,
      default: null
    },
    prop_parameterKey: {
      type: String,
      default: 'sfcltType'
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: []
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
