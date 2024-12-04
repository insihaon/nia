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
    prop_options: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sipCreateSeqCd',
      value: '',
      options: [],
    }
  },
  watch: {
    prop_options: {
      handler(newVal) {
        this.options = newVal
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    init() {
      if (this.prop_options.length > 0) {
        this.options = this.prop_options
      }
      this.emitEventToParent(this.getParameter())
    },
    handleChange() {
      this.emitEventToParent(this.getParameter())
    }

  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
