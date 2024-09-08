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
        :label="option.name"
        :value="option.code"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'
import { apiRequestOffice } from '@/api/ipms'

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
    apiPath: {
      type: String,
      default: '/ipmgmt/linemgmt'
    },
    voName: {
      type: String,
      default: null
    },
    valueKey: {
      type: Object,
      default: () => {
        return { cd: 'sofficecode', nm: 'sofficename' }
      }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: this.$store.state.ipms.tempOfficeList
    }
  },
  async mounted () {
    if (this.prop_options === null) {
      this.onLoadOfficeList()
    }
    await Eventbus.$on(EventType.changeLvl1, (params) => { this.onLoadOfficeList(params) })
    await Eventbus.$on(EventType.changeLvl2, (params) => { this.onLoadOfficeList(params) })
    await Eventbus.$on(EventType.changeLvl3, (params) => { this.onLoadOfficeList(params) })
    await Eventbus.$on(EventType.setSavedParameter, (params) => {
      this.setParameter(params)
    })
  },
  beforeDestroy() {
    Eventbus.$off(EventType.changeLvl1)
    Eventbus.$off(EventType.changeLvl2)
    Eventbus.$off(EventType.changeLvl3)
    Eventbus.$off(EventType.setSavedParameter)
  },
  methods: {
    async onLoadOfficeList(params = {}) {
      if (!this.voName) {
        this.error('Non VoName')
        return
      }
      this.value = ''
      const apiPath = this.apiPath
      const voName = this.voName
      const cd = this.valueKey.cd
      const nm = this.valueKey.nm
      try {
        const res = await apiRequestOffice(apiPath, params)
        this.options = res[voName].map(v => { return { code: v[cd], name: v[nm] } })
      } catch (error) {
        this.error(error)
      }
    },
    setParameter(params) {
      setTimeout(() => {
        this.value = params[this.prop_parameterKey] ?? ''
      }, 100)
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
