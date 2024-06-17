<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      상세작업분류
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

const routeName = 'DetailedWorkClassification'
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
        { value: 1, label: '생성' },
        { value: 2, label: '수정' },
        { value: 3, label: '삭제' },
        { value: 4, label: '병합' },
        { value: 5, label: '분할' },
        { value: 6, label: '배정' },
        { value: 7, label: '반납' },
        { value: 8, label: '서비스배정' },
        { value: 9, label: '할당' },
        { value: 10, label: '해지' },
        { value: 11, label: '할당예약' },
        { value: 12, label: '할당예약해지' },
      ],
    }
  },
  // nipHistTaskCd
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'nipHistTaskCd', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
