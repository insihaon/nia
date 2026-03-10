<template>
  <fragment>
    <th>
      공인/사설
    </th>
    <td>
      <div>
        <el-select
          v-model="value"
          size="small"
          @change="handleChange()"
        >
          <el-option v-if="isAllOption" value="" label="전체"></el-option>
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

const routeName = 'SipCreateType'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    isAllOption: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sipCreateTypeCd',
      value: 'CT0001',
      options: [
        { label: '공인', value: 'CT0001' },
        { label: 'Bogon', value: 'CT0003' },
        { label: '유/무선공용', value: 'CT0004' },
        { label: '사설', value: 'CT0005' }
      ]
    }
  },
  methods: {
    onResetParameter() {
      this.value = 'CT0001'
    },
    init() {
      if (this.isAllOption) {
        this.value = ''
      }
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
