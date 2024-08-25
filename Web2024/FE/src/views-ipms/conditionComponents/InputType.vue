<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-input v-model="value" size="mini" clearable @change="handleChange()" @input="onChangeInput" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'InputType'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: ''
    },
    prop_parameterKey: {
      type: String,
      default: null
    },
    defaultValue: {
      type: String,
      default: null
    },
    valueType: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      parameterKey: 'searchWrd'
    }
  },
  methods: {
    init() {
      if (this.prop_parameterKey && this.prop_parameterKey !== null) {
        this.parameterKey = this.prop_parameterKey
      }
      if (this.defaultValue && this.defaultValue !== null) {
        this.value = this.defaultValue
      }
      this.emitEventToParent(this.getParameter())
    },
    onChangeInput(val) {
      if (this.valueType === 'number') {
        this.value = val.replace(/\D/g, '')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
