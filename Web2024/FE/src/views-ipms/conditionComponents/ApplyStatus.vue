<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <td>
      <div :class="{ [name]: true }">
        <el-select
          v-model="value"
          size="small"
          :popper-class="prop_class === null ? 'applyStatus' : prop_class"
          @change="handleChange()"
        >
          <el-option
            v-for="(option, i) in options"
            :key="i"
            :label="option.label"
            :value="option.value"
          />
        </el-select>
      </div>
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'ApplyStatus'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '상태'
    },
    prop_options: {
      type: Array,
      default: null
    },
    prop_parameterKey: {
      type: String,
      default: null
    },
    prop_class: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: [
        { label: '전체', value: '' },
        { label: '접수', value: 'nod001' },
        { label: '승인', value: 'nod002' },
        { label: '반려', value: 'nod003' },
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
