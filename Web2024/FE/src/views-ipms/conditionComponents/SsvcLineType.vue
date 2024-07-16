<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <!-- LEVEL 1 -->
    <el-select
      v-model="localValue[key1]"
      :multiple="isMultiByLvl(1)"
      collapse-tags
      size="mini"
      @change="handleChangeLvl1"
    >
      <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="toggleAll(1)">전체</span></el-option>
      <el-option
        v-for="(option, i) in lvlOptions[key1]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 2 -->
    <el-select
      v-if="[2, 3].includes(lvl)"
      v-model="localValue[key2]"
      :disabled="localValue[key1] === '' || localValue[key1] === 'ALL'"
      :multiple="isMultiByLvl(2)"
      collapse-tags
      size="mini"
      @change="handleChangeLvl2"
    >
      <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="toggleAll(2)">전체</span></el-option>
      <el-option
        v-for="(option, i) in lvlOptions[key2]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 3 -->
    <el-select
      v-if="lvl === 3"
      v-model="localValue[key3]"
      :disabled="isDisabledLvlThree"
      :multiple="isMultiByLvl(3)"
      collapse-tags
      size="mini"
      @change="handleChangeLvl3"
    >
      <el-option
        v-for="(option, i) in lvlOptions[key3]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import { onMessagePopup } from '@/utils/index'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'
import _ from 'lodash'

const key1 = 1
const key2 = 2
const key3 = 3

const routeName = 'SsvcLineType'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    defaultValueLvl1: {
      type: String,
      default: null
    },
    label: {
      type: String,
      default: '계위'
    },
    lvl: {
      type: Number,
      default: 1
    },
    multi: {
      type: Array,
      default() { return [] }
    },
    limit: {
      type: Object,
      default() { return { 1: null, 2: null, 3: null } }
    },
    propsLvlOptions: {
      type: Object,
      default: null
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      key1, key2, key3,
      lvlOptions: {
        1: [
          { label: 'KORNET', value: 'CL0001' },
          { label: 'PREMIUM', value: 'CL0002' },
          { label: 'MOBILE', value: 'CL0003' },
          { label: 'GNS', value: 'CL0004' },
          { label: 'VPN', value: 'CL0005' },
          { label: 'ICC', value: 'CL0006' },
          { label: '미분류', value: 'CL0007' },
          { label: 'SCHOOLNET', value: 'CL0008' }
        ],
        2: this.$store.state.ipms.tempAuthCenterList,
        3: this.$store.state.ipms.tempNodeList,
      },
      localValue: { 1: '', 2: '', 3: '' },
    }
  },
  // 1: ssvcLineTypeCd, ssvcLineCdMultiStr
  // 2: ssvcGroupCdMulti: 123;234;345; or ssvcGroupCd
  // 3: ssvcObjCd
  computed: {
    isDisabledLvlThree() {
      let result = false
      const valueLvl2 = this.localValue[key2]
      if (this.isMultiByLvl(2)) {
        if (valueLvl2.length === 0 || valueLvl2.length > 1) {
          result = true
        }
      } else {
        if (valueLvl2.length === 0) {
          result = true
        }
      }
      return result
    }
  },
  // mounted() {
  //   if (this.multi?.length > 0) {
  //     Object.keys(this.localValue).forEach(key => {
  //       if (this.multi.includes(key)) {
  //         this.$set(this.localValue, key, [])
  //       }
  //     })
  //   }
  //   this.setDefaultParameterKey()
  // },
  methods: {
    init() {
      /* default options, value setting */
      if (this.propsLvlOptions !== null) {
        this.lvlOptions = this.getMergedOptions(this.lvlOptions, this.propsLvlOptions)
      }
      if (this.defaultValueLvl1 !== null) {
        this.$set(this.localValue, 1, this.defaultValueLvl1)
      }
      /* multi value setting */
      if (this.multi?.length > 0) {
        Object.keys(this.localValue).forEach(key => {
          if (this.multi.includes(key)) {
            this.$set(this.localValue, key, [])
          }
        })
      }
      this.$emit('update-value', this.getParameter())
    },
    getParameter() {
      const params = []
      const lvl1Key = `ssvcLine${this.multi.includes(key1) ? 'CdMultiStr' : 'TypeCd'}`
      const lvl2Key = `ssvcGroupCd${this.multi.includes(key2) ? 'MultiStr' : ''}`
      const parameterKeys = [lvl1Key, lvl2Key, 'ssvcObjCd']

      parameterKeys.forEach((key, idx) => {
        if (this.lvl >= (idx + 1)) {
          let value = ''
          if (Array.isArray(this.localValue[idx + 1])) {
            value = this.localValue[idx + 1].join(';')
          } else {
            value = this.localValue[idx + 1] ?? ''
          }
          params.push({ key, value })
        }
      })
      return params
    },
    getMergedOptions(orginOption, propOption) {
      // Merge propOption into orginOption but only for matching keys
      const result = _.mergeWith(this._cloneDeep(orginOption), propOption, (objValue, srcValue, key, object, source) => {
        if (Object.hasOwn(source, key)) {
          return srcValue
        }
        return objValue
      })
      return result
    },
    isMultiByLvl(lvl) {
      const multi = this.multi ?? []
      return multi.includes(lvl)
    },
    handleChangeLvl1() {
      const isOver = this.updateSelectionWithAll(1)
      if (isOver) return
      const params = { ssvcLineTypeCd: this.localValue[key1] }
      /*
      const res = await api(params)
      this.lvlOptions[key2] = res.result
      */
     this.resetLocalValue(key2)
     this.resetLocalValue(key3)
     this.emitEventToParent(this.getParameter())
      Eventbus.$emit(EventType.changeLvl1, { ssvcLineTypeCd: this.localValue[key1] })
    },
    handleChangeLvl2() {
      const isOver = this.updateSelectionWithAll(2)
      if (isOver) return

      const params = { ssvcLineTypeCd: this.localValue[key1], ssvcGroupCd: this.localValue[key2] }
      /*
      const res = await api(params)
      this.lvlOptions[key3] = res.result
      */
      this.resetLocalValue(key3)
      // const keyLvl2 = `ssvcGroupCd${Array.isArray(this.localValue[key2]) ? 'MultiStr' : ''}`
      this.emitEventToParent(this.getParameter())
      Eventbus.$emit(EventType.changeLvl2, { ssvcLineTypeCd: this.localValue[key1], keyLvl2: this.localValue[key2] })
    },
    handleChangeLvl3() {
      this.emitEventToParent(this.getParameter())
    },
    resetLocalValue(lvl) {
      const multiOp = this.multi ?? []
      if (multiOp.length > 0 && multiOp.includes(lvl)) {
        this.$set(this.localValue, lvl, [])
      } else {
        this.$set(this.localValue, lvl, '')
      }
    },
    getFullOptions(lvl) {
      return this.lvlOptions[lvl].map(option => option.value).filter(v => v !== 'ALL')
    },
    toggleAll(lvl) {
      if (this.multi.includes(lvl)) {
        this.$set(this.localValue, lvl, this.localValue[lvl]?.includes('ALL') ? [] : ['ALL', ...this.getFullOptions(lvl)])
        this.onCheckLimit(lvl)
      }
    },
    updateSelectionWithAll(lvl) {
      if (!this.multi.includes(lvl)) return false

      const fullOptionLen = this.getFullOptions(lvl).length
      const valueByLvlLen = this.localValue[lvl].length
      const isIncludesAll = this.localValue[lvl].includes('ALL')
      if (valueByLvlLen === fullOptionLen && !isIncludesAll) {
        this.localValue[lvl].push('ALL')
      } else if (isIncludesAll && valueByLvlLen !== fullOptionLen + 1) {
        this.localValue[lvl] = this.localValue[lvl]?.filter(value => value !== 'ALL')
      }

      return this.onCheckLimit(lvl)
    },
    onCheckLimit(lvl) {
      if (this.limit[lvl] !== null && this.localValue[lvl]?.length > this.limit[lvl]) {
        onMessagePopup(this, `${this.label}는 최대 ${this.limit[lvl]}개까지 선택 가능합니다.`)
        this.$set(this.localValue, lvl, [])
        return true
      }
      return false
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
