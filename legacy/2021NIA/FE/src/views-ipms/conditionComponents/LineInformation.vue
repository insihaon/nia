<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="value"
      size="mini"
      @change="handleChange"
    >
      <el-option v-if="isAllOption" label="전체" value="" />
      <el-option
        v-for="(option, i) in options"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <el-input v-model="word" size="mini" clearable @change="handleChangeWord" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'LineInformation'

export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '회선정보'
    },
    defaultValue: {
      type: String,
      default: null
    },
    prop_options: {
      type: Array,
      default: null
    },
    isAllOption: {
      type: Boolean,
      default: false
    },
    prop_parameterKey: {
      type: String,
      default: null
    },
    prop_textFixKey: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'llSrchTypeCd',
      value: 'llnum',
      word: '',
      options: [
        {
          value: 'llnum',
          label: '전용번호',
          txtKey: 'sllnum'
        },
        {
          value: 'said',
          label: 'SAID',
          txtKey: 'ssaid'
        },
        {
          value: 'ordernum',
          label: '오더번호',
          txtKey: 'sordernum'
        },
      ],
    }
  },
  methods: {
    onResetParameter() {
      this.value = this.defaultValue ?? 'llnum'
      this.word = ''
    },
    init() {
      if (this.prop_options !== null) {
        this.options = this.prop_options
      }
      if (this.defaultValue !== null) {
        this.value = this.defaultValue
      }
      if (this.prop_parameterKey !== null) {
        this.parameterKey = this.prop_parameterKey
      }
      this.handleChange()
      this.handleChangeWord()
    },
    handleChange() {
      this.word = ''
      this.emitEventToParent([{ key: this.parameterKey, value: this.value }])
    },
    handleChangeWord() {
      const params = []
      if (this.prop_textFixKey !== null) {
        params.push({ key: this.prop_textFixKey, value: this.word })
      } else {
        this.options.forEach(op => {
          if (op.value === this.value) {
            params.push({ key: op.txtKey, value: this.word })
          } else {
            params.push({ key: op.txtKey, value: '' })
          }
        })
      }
      this.emitEventToParent(params)
    },
    setParameter(params) {
      this.value = params[this.parameterKey] ?? 'llnum'
      let txtKey
      if (this.prop_textFixKey !== null) {
        txtKey = this.prop_textFixKey
      } else {
        txtKey = this.options.find(v => v.value === this.value).txtKey
      }
      this.word = params[txtKey] ?? ''
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 200px;
}
</style>
