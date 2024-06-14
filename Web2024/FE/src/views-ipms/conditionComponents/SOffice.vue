<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      수용국
    </label>
    <el-select
      v-model="localValue"
      collapse-tags
      size="mini"
    >
      <el-option
        v-for="(option, i) in officeOptions"
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

const routeName = 'SOffice'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      localValue: '',
      officeOptions: this.$store.state.ipms.tempOfficeList
    }
  },
  computed: {

  },
  mounted () {
    Eventbus.$on(EventType.changeLvl1, (params) => { this.onLoadOfficeList(params) })
    Eventbus.$on(EventType.changeLvl2, (params) => { this.onLoadOfficeList(params) })
  },
  beforeDestroy() {
    Eventbus.$off(EventType.changeLvl1)
    Eventbus.$off(EventType.changeLvl2)
  },
  methods: {
    onLoadOfficeList(params) {
      /*
      const res = await api(params)
      this.officeOptions = res.result (options set)
      */
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
