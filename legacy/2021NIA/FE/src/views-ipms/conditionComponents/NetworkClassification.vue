<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <!-- LEVEL 1 -->
    <el-select
      v-model="localValue[0]"
      collapse-tags
      size="mini"
      @change="handleChangeLvl1"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll(0)">전체</span></el-option>
      <el-option
        v-for="(option, i) in lvlOptions1"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 2 -->
    <el-select
      v-model="localValue[1]"
      :disabled="localValue[0] === '' || localValue[0] === 'ALL'"
      collapse-tags
      size="mini"
      @change="handleChangeLvl2"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll(1)">전체</span></el-option>
      <el-option
        v-for="(option, i) in lvlOptions2"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 3 -->
    <el-select
      v-model="localValue[2]"
      :disabled="localValue[1] === '' || localValue[1] === 'ALL'"
      collapse-tags
      size="mini"
      @change="handleChangeLvl3"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll(2)">전체</span></el-option>
      <el-option
        v-for="(option, i) in lvlOptions3"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'
import _ from 'lodash'

const routeName = 'NetworkClassification'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
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
      lvlOptions1: [
        { label: '유선(KORNET,PREMIUM)', value: 'CL0001' },
        { label: '무선(MOBOLE)', value: 'CL0002' },
        { label: 'MOBILE', value: 'CL0003' },
        { label: '국가망(GNS)', value: 'CL0004' },
        { label: 'VPN', value: 'CL0005' },
        { label: '기타', value: 'CL0006' }
      ],
        lvlOptions2: [
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
        lvlOptions3: [
          { label: 'DialUP', value: 'CL0010' },
          { label: 'BcN', value: 'CL0011' },
          { label: 'GIGA office', value: 'CL0013' },
          { label: 'IP-VPN', value: 'CL0014' },
          { label: 'ISDN', value: 'CL0015' },
          { label: 'biz KORNET-Express 서비스', value: 'CL0016' },
          { label: 'biz KORNET-Hotline', value: 'CL0017' },
      ],
      localValue: { 0: 'ALL', 1: '', 2: '' },
    }
  },
  computed: {
    // isDisabledLvlThree() {
    //   return this.localValue[0] === 'ALL' || this.localValue[1] === 'ALL' || this.localValue[1] === ''
    // }
  },
  methods: {
    init() {
      this.$emit('update-value', this.getParameter())
    },
    handleChangeLvl1() {
      const params = { ssvcLineTypeCd: this.localValue[1] }
      /*
      const res = await api(params)
      this.lvlOptions[key2] = res.result
      */
     this.resetLocalValue(1)
     this.resetLocalValue(2)
     this.emitEventToParent(this.getParameter())
    },
    handleChangeLvl2() {
      const params = { ssvcLineTypeCd: this.localValue[1], ssvcGroupCd: this.localValue[2] }
      /*
      const res = await api(params)
      this.lvlOptions[key3] = res.result
      */
     this.resetLocalValue(2)
     this.emitEventToParent(this.getParameter())
    },
    handleChangeLvl3() {
      this.emitEventToParent(this.getParameter())
    },
     getParameter() {
      const params = []
      const parameterKeys = ['ssvcHgroupCd', 'ssvcMainClsCode', 'ssvcSubClsCode']

      parameterKeys.forEach((key, idx) => {
        if (this.lvl >= (idx)) {
          let value = ''
          if (Array.isArray(this.localValue[idx])) {
            value = this.localValue[idx].join(';')
          } else {
            value = this.localValue[idx] ?? ''
          }
          params.push({ key, value })
        }
      })

      return params
    },

    resetLocalValue(lvl) {
      this.$set(this.localValue, lvl, 'ALL')
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
