<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <td>
      <el-select
        v-model="value"
        size="small"
        @change="handleChange()"
      >
        <el-option v-if="isAllOption" valua="" label="전체" />
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

const routeName = 'AuthLevel'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '권한등급'
    },
    isAllOption: {
      type: Boolean,
      default: true
    },
    defaultValue: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'suserGradeCd',
      value: '',
      options: [
        { label: '시스템 관리자', value: 'UR0001' },
        { label: '서비스망 관리자', value: 'UR0003' },
        { label: '본부운용자', value: 'UR0004' },
        { label: '노드운용자', value: 'UR0005' },
        { label: '조회자', value: 'UR0006' },
      ]
    }
  },
  methods: {
    onResetParameter() {
      this.value = this.defaultValue ?? ''
    },
    init() {
      if (this.defaultValue !== null) {
        this.value = this.defaultValue
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
