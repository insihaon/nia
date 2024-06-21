<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
      @change="handleChange"
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

const routeName = 'ExtrnLnkgs'
export default {
  name: routeName,
  extends: Base,
  props: {
    label: {
      type: String,
      default: '외부연계'
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: [
        { value: '', label: '전체' },
        { value: 'CE0000', label: '미분류' },
        { value: 'CE0001', label: 'NEOSS_CODE' },
        { value: 'CE0002', label: 'NEOSS_DATA' },
        { value: 'CE0003', label: 'DAIMS_DATA' },
        { value: 'CE0004', label: 'IFOMS_DATA' },
        { value: 'CE0005', label: '통합NMS_DATA' },
        { value: 'CE0006', label: '수작업데이터' },
        { value: 'CE0007', label: 'IDC_DATA' },
        { value: 'CE0008', label: 'IDMS_DATA' }

      ],
    }
  },
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'sexLinkUseTypeCd', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
