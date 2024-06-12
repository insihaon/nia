<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width: 100px">
      {{ exceptOptions.label }}
    </label>
    <!-- LEVEL 1 -->
    <el-select
      v-model="localValue[key1]"
      :multiple="isMultiByLvl(1)"
      collapse-tags
      size="mini"
      @change="handleChangeLvl1"
    >
      <el-option
        v-for="(option, i) in lvlOptions[key1]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 2 -->
    <el-select
      v-if="[2, 3].includes(exceptOptions.lvl)"
      v-model="localValue[key2]"
      :disabled="localValue[key1] === ''"
      :multiple="isMultiByLvl(2)"
      collapse-tags
      size="mini"
      @change="handleChangeLvl2"
    >
      <el-option
        v-for="(option, i) in lvlOptions[key2]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <!-- LEVEL 3 -->
    <el-select
      v-if="exceptOptions.lvl === 3"
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

const key1 = '1'
const key2 = '2'
const key3 = '3'

const routeName = 'SsvcLineType'
export default {
  name: routeName,
  extends: Base,
  props: {
    exceptOptions: { /* 예외처리 option */
      type: Object,
      default() {
        return {
          label: '계위',
          lvl: 1,
          multi: []
        }
      }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      key1, key2, key3,
      lvlOptions: {
        1: [
          { label: '전체', value: '' },
          { label: 'KORNET', value: 'CL0001' },
          { label: 'PREMIUM', value: 'CL0002' },
          { label: 'MOBILE', value: 'CL0003' },
          { label: 'GNS', value: 'CL0004' },
          { label: 'VPN', value: 'CL0005' },
          { label: 'ICC', value: 'CL0006' },
          { label: '미분류', value: 'CL0007' },
          { label: 'SCHOOLNET', value: 'CL00085' }
        ],
        2: this.$store.state.ipms.tempAuthCenterList,
        3: this.$store.state.ipms.tempNodeList,
      },
      localValue: { 1: '', 2: '', 3: '' },
    }
  },
  // 1: ssvcLineTypeCd
  // 2: ssvcGroupCdMulti: 123;234;345; or ssvcGroupCd
  // 3: ssvcObjCd
  computed: {
    isDisabledLvlThree() {
      let result = false
      const valueLvl2 = this.localValue[key2]
      if (valueLvl2.length === 0 || valueLvl2.length > 1) {
        result = true
      }
      return result
    }
  },
  mounted() {
    if (this.exceptOptions.multi?.length > 0) {
      const multiOp = this.exceptOptions.multi
      Object.keys(this.localValue).forEach(key => {
        if (multiOp.includes(key)) {
          // this.localValue[key] = []
          this.$set(this.localValue, key, [])
        }
      })
    }
  },
  methods: {
    isMultiByLvl(lvl) {
      return this.exceptOptions.multi?.includes(lvl)
    },
    handleChangeLvl1() {
      const params = { ssvcLineTypeCd: this.localValue[key1] }
      /*
      const res = await api(params)
      this.lvlOptions[key2] = res.result
      */
     this.resetLocalValue(key2)
     this.resetLocalValue(key3)
     this.emitEvent('ssvcLineTypeCd', key1)
     Eventbus.$emit(EventType.changeLvl, { ssvcLineTypeCd: this.localValue[key1] })
    },
    handleChangeLvl2() {
      const params = { ssvcLineTypeCd: this.localValue[key1], ssvcGroupCd: this.localValue[key2] }
      /*
      const res = await api(params)
      this.lvlOptions[key3] = res.result
      */
      this.resetLocalValue(key3)
      const keyLvl2 = `ssvcGroupCd$ ${Array.isArray(this.localValue[key2]) ? 'Multi' : ''}`
      this.emitEvent(keyLvl2, key2)
      Eventbus.$emit(EventType.changeLvl, { ssvcLineTypeCd: this.localValue[key1], keyLvl2: this.localValue[key2] })
    },
    handleChangeLvl3() {
      this.emitEvent('ssvcObjCd', key3)
    },
    resetLocalValue(lvl) {
      const multiOp = this.exceptOptions.multi ?? []
      if (multiOp.length > 0 && multiOp.includes(lvl)) {
        // this.localValue[lvl] = []
        this.$set(this.localValue, lvl, [])
      } else {
        // this.localValue[lvl] = ''
        this.$set(this.localValue, lvl, '')
      }
    },
    emitEvent(emitKey, lvl) {
      this.$emit(emitKey, this.localValue[lvl])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
