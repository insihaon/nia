<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="value"
      size="mini"
      @change="handleChange()"
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
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'EquipmentType'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '장비타입'
    },
    prop_options: {
      type: Array,
      default: null
    },
    prop_parameterKey: {
      type: String,
      default: 'sfcltType'
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: [
        { label: '전체', value: '' },
        { label: 'ACCESS-UBIQUOSS_TEST', value: 'ACCESS-UBIQUOSS_TEST' },
        { label: 'KORNET-CISCO_CRS', value: 'KORNET-CISCO_CRS' },
        { label: 'KORNET-JUNIPER', value: 'KORNET-JUNIPER' },
        { label: 'KORNET_CENTER-CISCO_CRS', value: 'KORNET_CENTER-CISCO_CRS' },
        { label: 'KORNET_CENTER-NOKIA_7950XRS', value: 'KORNET_CENTER-NOKIA_7950XRS' },
        { label: 'KORNET_GHT-JUNIPER', value: 'KORNET_GHT-JUNIPER' },
        { label: 'MOBILE-CISCO-NCS6K', value: 'MOBILE-CISCO-NCS6K' },
        { label: 'MOBILE-CISCO_7606', value: 'MOBILE-CISCO_7606' },
        { label: 'MOBILE-CISCO_CRS', value: 'MOBILE-CISCO_CRS' },
        { label: 'MOBILE-CISCO_NEXUS', value: 'MOBILE-CISCO_NEXUS' },
        { label: 'MOBILE-JUNIPER-QFX10008', value: 'MOBILE-JUNIPER-QFX10008' },
        { label: 'MOBILE-JUNIPER-QFX10008(IER)', value: 'MOBILE-JUNIPER-QFX10008(IER)' },
        { label: 'MOBILE-JUNIPER_MX960', value: 'MOBILE-JUNIPER_MX960' },
        { label: 'MOBILE_SG-CISCO_ASR1009', value: 'MOBILE_SG-CISCO_ASR1009' },
        { label: 'PREMIUM_ICOD-CISCO_NEXUS', value: 'PREMIUM_ICOD-CISCO_NEXUS' },
        { label: 'PREMIUM_IPT-JUNIPER', value: 'PREMIUM_IPT-JUNIPER' },
        { label: 'PREMIUM_PE-CISCO_CRS', value: 'PREMIUM_PE-CISCO_CRS' },
        { label: 'PREMIUM_PE-JUNIPER', value: 'PREMIUM_PE-JUNIPER' },
        { label: 'PREMIUM_iCOD-CISCO_C6500', value: 'PREMIUM_iCOD-CISCO_C6500' }
      ]
    }
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
