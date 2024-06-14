<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      Block 크기별 조건
    </label>
    <el-select
      v-model="targetValue"
      size="mini"
      @change="handleChangeTarget"
    >
      <el-option
        v-for="(option, i) in targetOptions"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <el-input v-model="word" size="mini" clearable style="width: 120px;" @change="handleChangeWord" />
    <el-select
      v-model="compareValue"
      size="mini"
      @change="handleChangeCompare"
    >
      <el-option
        v-for="(option, i) in compareOptions"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'ConditionByBlocksize'
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
      targetValue: '',
      targetOptions: [
        { label: '전체', value: '' },
        { label: 'IP Block 분할대상', value: 'BIG' },
        { label: 'IP Block 병합대상', value: 'SMALL' },
        { label: 'IP Block 크기일치', value: 'SAME' },
      ],
      compareValue: '',
      compareOptions: [
        { label: '전체', value: '' },
        { label: '이상', value: 10 },
        { label: '이하', value: 20 },
        { label: '같음', value: 30 },
      ]
    }
  },
  computed: {
  },
  // skindCd, ngubunCnt, ssign
  methods: {
    handleChangeTarget() {
      this.$emit('update-value', [{ key: 'skindCd', value: this.targetValue }])
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
</style>
