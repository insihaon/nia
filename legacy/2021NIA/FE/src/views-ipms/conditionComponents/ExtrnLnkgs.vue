<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      외부연계
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
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: [
        { value: '', label: '전체' },
        { value: 1, label: '미분류' },
        { value: 2, label: 'NEOSS_CODE' },
        { value: 3, label: 'NEOSS_DATA' },
        { value: 4, label: 'DAIMS_DATA' },
        { value: 5, label: 'IFOMS_DATA' },
        { value: 6, label: '통합NMS_DATA' },
        { value: 6, label: '수작업데이터' },
        { value: 6, label: 'IDC_DATA' },
        { value: 6, label: 'IDMS_DATA' }

      ],
    }
  },
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'extrnLnkgsCd', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
