<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      상태
    </label>
    <el-select
      v-model="value"
      size="mini"
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

const routeName = 'IpMngProgress'
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
        { label: '전체', value: '' },
        { label: '추가 신청', value: 'Y' },
        { label: '삭제 신청', value: 'N' },
        { label: '처리 완료', value: 'C' },
      ]
    }
  },
  // sdbIntgrmRsltCd
  computed: {
  },
  methods: {
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'sflag', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
