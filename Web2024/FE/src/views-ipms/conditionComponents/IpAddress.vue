<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <td class="textflex">
      <el-select
        v-if="isShowSelectBox"
        v-model="value"
        collapse-tags
        :disabled="disabled"
        size="small"
        :style="{'width': isShowInput ? '200px': '100%'}"
        @change="onChangeSelected"
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
        size="small"
        clearable
        @input="onChangeInput"
        @change="()=> emitEventToParent([{ key: 'searchWrd', value: word }])"
      />
    </td>
  </fragment>
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
    },
    disabled: {
      type: Boolean,
      default: false
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
  watch: {
    defaultWord(n, o) {
      if (n !== o) {
        this.init()
      }
    }
  },
  methods: {
    onResetParameter() {
      this.value = 'CV0001'
      this.word = ''
    },
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
    setParameter(params) {
      if (this.isShowSelectBox) {
        this.value = params?.sipVersionTypeCd ?? ''
      }
      if (this.isShowInput) {
        this.word = params.searchWrd ?? ''
      }
    },
    onChangeSelected() {
      this.word = ''
      this.emitEventToParent([{ key: 'sipVersionTypeCd', value: this.value }])
    },
    onChangeInput(val) {
      if (this.value === 'CV0001') {
        this.word = val.replace(/[^0-9.\/]+/g, '')
      } else if (this.value === 'CV0002') {
        this.word = val.replace(/[^0-9a-fA-F:\/]+/g, '')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep.el-select .el-input{
  width: 150px !important;
}
</style>
