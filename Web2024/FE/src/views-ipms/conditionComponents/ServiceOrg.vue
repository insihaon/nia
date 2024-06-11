<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      서비스
    </label>
    <el-select
      v-model="localValue"
      filterable
      :multiple="isMultiMode"
      clearable
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

const routeName = 'ServiceOrg'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: String,
      default: ''
    },
    exceptOptions: {
      type: Object,
      default() { return {} }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      word: '',
      options: [
        { label: '전체', value: 'ALL' },
        { label: '기업고객(고정)', value: 'agency' },
        { label: '홈고객(유동)', value: 'flow' },
        { label: '홈고객(고정)', value: 'fix' },
        { label: '홈고객(시설)', value: 'facility' },
        { label: '미분류서비스', value: 'unclassified' },
        { label: '홈고객(Secured IP)', value: 'secured' },
        { label: '타사이관', value: 'transfer' },
        { label: '홍콩DC구축용', value: 'hongkong' },
        { label: '대군화 시설용', value: 'cloud' },
        { label: 'Cloud', value: 'cloud' }
      ]
    }
  },
  computed: {
    localValue: {
      get() {
        return this.value
      },
      set(newValue) {
        this.$emit('set-value', newValue)
        this.$emit('update-value', [{ key: 'serviceOrg', value: newValue }])
      }
    },
    isMultiMode() {
      console.log(this.exceptOptions)
      return this.exceptOptions?.multi
    },
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
