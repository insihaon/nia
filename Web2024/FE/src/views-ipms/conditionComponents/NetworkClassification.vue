<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <!-- LEVEL 1 -->
    <el-select
      v-model="localValue[key1]"
      collapse-tags
      size="mini"
      @change="handleChangeLvl1"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll(1)">전체</span></el-option>
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
      collapse-tags
      size="mini"
      @change="handleChangeLvl2"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll(2)">전체</span></el-option>
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
// import Eventbus from '@/utils/event-bus'
// import { EventType } from '@/min/types'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'
import _ from 'lodash'

const key1 = 1
const key2 = 2
const key3 = 3

const routeName = 'NetworkClassification'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    defaultValueLvl1: {
      type: String,
      default: 'ALL'
    },
    label: {
      type: String,
      default: '구분'
    },
    lvl: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      key1, key2, key3,
      lvlOptions: {
        1: [
          { label: '유선(KORNET,PREMIUM)', value: 'CL0001' },
          { label: '무선(MOBOLE)', value: 'CL0002' },
          { label: 'MOBILE', value: 'CL0003' },
          { label: '국가망(GNS)', value: 'CL0004' },
          { label: 'VPN', value: 'CL0005' },
          { label: '기타', value: 'CL0006' }
        ],
        2: [
          { label: 'ADSL', value: 'SU0001' },
          { label: 'BcN', value: 'SU0002' },
          { label: 'IDC 서비스', value: 'SU0003' },
          { label: 'ISP', value: 'SU0004' },
          { label: 'KORNET', value: 'SU0005' },
          { label: 'Legacy 서비스', value: 'SU0006' },
          { label: 'VOIP', value: 'SU0007' },
          { label: 'WiBRO', value: 'SU0008' },
          { label: '인터넷', value: 'SU0009' }
        ],
        3: [
          { label: 'DialUP', value: 'CL0010' },
          { label: 'BcN', value: 'CL0011' },
          { label: 'GIGA office', value: 'CL0013' },
          { label: 'IP-VPN', value: 'CL0014' },
          { label: 'ISDN', value: 'CL0015' },
          { label: 'biz KORNET-Express 서비스', value: 'CL0016' },
          { label: 'biz KORNET-Hotline', value: 'CL0017' },
        ],
      },
      localValue: { 1: '', 2: '', 3: '' },
    }
  },
  computed: {
    isDisabledLvlThree() {
      let result = false
      const valueLvl2 = this.localValue[key2]

        if (valueLvl2.length === 0 || this.localValue[key2] === 'ALL') {
          result = true
        }
      return result
    }
  },

  methods: {
    init() {
      /* default options, value setting */
      if (this.defaultValueLvl1 !== null) {
        this.$set(this.localValue, 1, this.defaultValueLvl1)
      }
      this.$emit('update-value', this.getParameter())
    },
    getParameter() {
      const params = []
      const parameterKeys = ['networkCd', 'srvcGroupCd', 'detailTypeCd']

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
    handleChangeLvl1() {
      const params = { ssvcLineTypeCd: this.localValue[key1] }
      /*
      const res = await api(params)
      this.lvlOptions[key2] = res.result
      */
     this.resetLocalValue(key2)
     this.resetLocalValue(key3)
     this.emitEventToParent(this.getParameter())
    },
    handleChangeLvl2() {
      const params = { ssvcLineTypeCd: this.localValue[key1], ssvcGroupCd: this.localValue[key2] }
      /*
      const res = await api(params)
      this.lvlOptions[key3] = res.result
      */
      this.resetLocalValue(key3)
      this.emitEventToParent(this.getParameter())
    },
    handleChangeLvl3() {
      this.emitEventToParent(this.getParameter())
    },
    resetLocalValue(lvl) {
      this.$set(this.localValue, lvl, '')
    },
    getFullOptions(lvl) {
      return this.lvlOptions[lvl].map(option => option.value).filter(v => v !== 'ALL')
    },
    toggleAll(lvl) {
      if (this.localValue[lvl]?.includes('ALL')) {
        this.$set(this.localValue, lvl, '')
      } else {
        this.$set(this.localValue, lvl, 'ALL')
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
