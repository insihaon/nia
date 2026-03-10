<template>
  <fragment>
    <th>
      Block 크기별 조건
    </th>
    <td class="textflex">
      <el-select
        v-model="targetValue"
        size="small"
        @change="emitEventToParent([{ key: 'skindCd', value: targetValue }])"
      >
        <el-option
          v-for="(option, i) in targetOptions"
          :key="i"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
      <el-input v-model="word" size="small" clearable style="width: 120px;" @change="emitEventToParent([{ key: 'ngubunCnt', value: word }])" @input="onChangeInput" />
      <el-select
        v-model="compareValue"
        size="small"
        @change="emitEventToParent([{ key: 'ssign', value: compareValue }])"
      >
        <el-option
          v-for="(option, i) in compareOptions"
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

const routeName = 'ConditionByBlocksize'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
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
  // skindCd, ngubunCnt, ssign
  methods: {
    onResetParameter() {
      this.word = ''
      this.targetValue = ''
      this.compareValue = ''
    },
    getParameter() {
      return [
        { key: 'skindCd', value: this.targetValue },
        { key: 'ngubunCnt', value: this.word },
        { key: 'ssign', value: this.compareValue },
      ]
    },
    setParameter(params) {
      this.targetValue = params['skindCd'] ?? ''
      this.compareValue = params['ssign'] ?? ''
      this.ngubunCnt = params['word'] ?? ''
    },
    onChangeInput(val) {
      this.word = val.replace(/\D/g, '')
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
