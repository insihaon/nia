<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      생성차수
    </label>
    <el-select
      v-model="localValue"
      collapse-tags
      filterable
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

const routeName = 'GenerationDegree'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      word: '',
      options: [
        { label: '전체', value: 'ALL' },
        { label: 'G202411001', value: 'G202411001' },
        { label: 'G202412000', value: 'G202412000' },
        { label: 'K199401000', value: 'K199401000' },
        { label: 'K199402000', value: 'K199402000' },
        { label: 'K199501000', value: 'K199501000' },
        { label: 'K199601000', value: 'K199601000' },
        { label: 'K199602000', value: 'K199602000' },
        { label: 'K199603000', value: 'K199603000' },
        { label: 'K199604000', value: 'K199604000' },
        { label: 'K199701000', value: 'K199701000' },
        { label: 'K199702000', value: 'K199702000' },
        { label: 'K199703000', value: 'K199703000' },
        { label: 'K199704000', value: 'K199704000' },
        { label: 'K199705000', value: 'K199705000' },
        { label: 'K199706000', value: 'K199706000' },
        { label: 'K199707000', value: 'K199707000' }
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
        this.$emit('update-value', [{ key: 'degreeCd', value: newValue }])
      }
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
