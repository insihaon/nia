<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <!-- LEVEL 1 -->
    <el-select
      v-model="localValue1"
      collapse-tags
      size="mini"
      @change="handleChangeLvl1"
    >
      <el-option label="전체" value="" />
      <el-option
        v-for="(option, i) in lvlOptions1"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 2 -->
    <el-select
      v-model="localValue2"
      :disabled="localValue1 === ''"
      collapse-tags
      size="mini"
      @change="handleChangeLvl2"
    >
      <el-option label="전체" value="" />
      <el-option
        v-for="(option, i) in lvlOptions2"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 3 -->
    <el-select
      v-model="localValue3"
      :disabled="localValue2 === ''"
      collapse-tags
      size="mini"
      @change="handleChangeLvl3"
    >
      <el-option label="전체" value="" />
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
      localValue1: '',
      localValue2: '',
      localValue3: ''
    }
  },
  computed: {
  },
  methods: {
    onResetParameter() {
      this.localValue1 = ''
      this.localValue2 = ''
      this.localValue3 = ''
    },
    init() {
      this.$emit('update-value', this.getParameter())
    },
    handleChangeLvl1() {
      const params = { ssvcHgroupCd: this.localValue1 }
      /*
      const res = await api(params)
      this.lvlOptions2 = res.result
      */
     this.resetLocalValue(1)
     this.resetLocalValue(2)
     this.emitEventToParent(this.getParameter())
    },
    handleChangeLvl2() {
      const params = { ssvcMainClsCode: this.localValue2 }
      /*
      const res = await api(params)
      this.lvlOptions3 = res.result
      */
     this.resetLocalValue(2)
     this.emitEventToParent(this.getParameter())
    },
    handleChangeLvl3() {
      this.emitEventToParent(this.getParameter())
    },
     getParameter() {
      return [
        { key: 'ssvcHgroupCd', value: this.localValue1 },
        { key: 'ssvcMainClsCode', value: this.localValue2 },
        { key: 'ssvcSubClsCode', value: this.localValue3 }
      ]
    },
    resetLocalValue(lvl) {
      switch (lvl) {
        case 1:
          this.localValue2 = ''
          break
        case 2:
          this.localValue3 = ''
          break
        default:
          break
      }
    },
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
