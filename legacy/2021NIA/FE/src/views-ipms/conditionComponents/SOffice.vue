<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
      @change="handleChange()"
    >
      <el-option label="전체" value="" />
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
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'SOffice'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '수용국'
    },
    prop_parameterKey: {
      type: String,
      default: 'sofficecode'
    },
    prop_options: {
      type: Array,
      default: null
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: this.$store.state.ipms.tempOfficeList
    }
  },
  mounted () {
    if (this.prop_options === null) {
      this.onLoadOfficeList()
    }
    Eventbus.$on(EventType.changeLvl1, (params) => { this.onLoadOfficeList(params) })
    Eventbus.$on(EventType.changeLvl2, (params) => { this.onLoadOfficeList(params) })
    Eventbus.$on(EventType.changeLvl3, (params) => { this.onLoadOfficeList(params) })
  },
  beforeDestroy() {
    Eventbus.$off(EventType.changeLvl1)
    Eventbus.$off(EventType.changeLvl2)
    Eventbus.$off(EventType.changeLvl3)
  },
  methods: {
    async onLoadOfficeList(params = {}) {
      this.value = ''
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectOfficeList, params)
        this.options = res.tbIpAssignSubVos.map(v => { return { value: v.sofficecode, label: v.sofficename } })
      } catch (error) {
        this.error(error)
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
