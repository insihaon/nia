<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-if="isShowSelectBox"
      v-model="value"
      collapse-tags
      size="mini"
      :style="{'width': isShowInput ? '200px': '100%'}"
      @change="()=> emitEventToParent([{ key: 'sipVersionTypeCd', value }])"
    >
      <el-option v-if="isAllOption" value="" label="전체"></el-option>
      <el-option
        v-for="(option, i) in [
          { label: 'IPv4', value: 'CV0001' },
          { label: 'IPv6', value: 'CV0002' },
        ]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <el-input
      v-if="isShowInput"
      v-model="word"
      size="mini"
      clearable
      @change="()=> emitEventToParent([{ key: 'searchWrd', value: word }])"
    />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'IpAddress'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    isAllOption: {
      type: Boolean,
      default: false
    },
    defaultValue: {
      type: String,
      default: null
    },
    defaultWord: {
      type: String,
      default: null
    },
    label: {
      type: String,
      default: 'IP주소'
    },
    isShowInput: {
      type: Boolean,
      default: true
    },
    isShowSelectBox: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: 'CV0001',
      word: ''
    }
  },
  methods: {
    init() {
      if (this.defaultValue !== null) {
        this.value = this.defaultValue
      }
      if (this.defaultWord !== null) {
        this.word = this.defaultWord
      }
      const params = []
      if (this.isShowSelectBox) {
        params.push({ key: 'sipVersionTypeCd', value: this.value },)
      }
      if (this.isShowInput) {
        params.push({ key: 'searchWrd', value: this.word },)
      }
      this.emitEventToParent(params)
    },
  }
}
</script>
<style lang="scss" scoped>
</style>
