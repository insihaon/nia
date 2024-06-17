<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="selectedValues"
      size="mini"
      multiple
      collapse-tags
      placeholder="전체"
      @change="handleChange"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" :value="isSelectedAll" @click="toggleAll">전체</span></el-option>
      <el-option
        v-for="(option, i) in options"
        :key="i"
        :label="option.label"
        :value="option.value"
      >
        <span class="w-100 h-100 d-inline-block" :value="isSelectedAll" @click="hangleClickOption(option)">{{ option.label }}</span>
      </el-option>
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
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      isAll: false,
      model: [],
      setValues: [],
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
    isSelectedAll() {
      const allSelectedALL = this.model.filter(v => v !== 'ALL').length === this.options.length
      const onlySelectedALL = this.model.length === 1 && this.model.includes('ALL')

      return allSelectedALL || onlySelectedALL
    },
    selectedValues: {
      get() {
        return this.isSelectedAll ? ['ALL'].concat(this.options.map(v => v.value)) : this.model
      },
      set() { }
    },
  },
  // sassignLevelVd
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'sassignLevelCd', value: this.selectedValues.filter(v => v !== 'ALL') }])
    },
    hangleClickOption(option) {
      const tempValue = this._cloneDeep(this.isSelectedAll ? this.options.map(v => v.value) : this.model)
      const existIndex = tempValue.findIndex((v) => { return v === option.value })
      if (existIndex !== -1) {
        tempValue.splice(existIndex, 1)
      } else {
        tempValue.push(option.value)
      }
      this.model = tempValue
    },
    toggleAll() {
      this.model = this.isSelectedAll ? [] : [].concat(['ALL', ...this.options.map(option => option.value)])
    },
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'ngubunCnt', value: this.word }])
    },
    handleChangeCompare() {
      this.$emit('update-value', [{ key: 'ssign', value: this.compareValue }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
