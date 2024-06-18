<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="selectedValues"
      size="mini"
      :multiple="isMulti"
      collapse-tags
      placeholder="전체"
      @change="handleChange"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll">전체</span></el-option>
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

const routeName = 'IpBlockStatus'
export default {
  name: routeName,
  extends: Base,
  props: {
    label: {
      type: String,
      default: 'IP 블록상태'
    },
    isMulti: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedValues: [],
      options: [
        { label: '미배정', value: 'IA0001' },
        { label: '예비배정', value: 'IA0002' },
        { label: '배정[미할당]', value: 'IA0003' },
        { label: '서비스배정[미할당]', value: 'IA0004' },
        { label: '할당예약', value: 'IA0005' },
        { label: '할당', value: 'IA0006' },
      ],
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(option => option.value).filter(v => v !== 'ALL')
    },
  },
  // sassignLevelVd
  methods: {
    handleChange() {
       if (this.selectedValues.length === this.fullOptions.length && !this.selectedValues.includes('ALL')) {
        this.selectedValues.push('ALL')
      } else if (this.selectedValues.includes('ALL') && this.selectedValues.length !== this.fullOptions.length + 1) {
        this.selectedValues = this.selectedValues.filter(value => value !== 'ALL')
       }

       let key = 'sassignLevelCd'
      let value = this.localValue
      if (this.isMulti) {
        key = 'sassignLevelCdMultiStr'
        value = this.selectedValues.filter(v => v !== 'ALL').join(';')
      }
      this.$emit('update-value', [{ key, value }])
    },
    toggleAll() {
      this.selectedValues = this.selectedValues.includes('ALL') ? [] : ['ALL', ...this.fullOptions]
    },
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
