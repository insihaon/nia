<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <!-- LEVEL 1 -->
    <td class="textflex">
      <div>
        <el-select
          v-model="localValue[key1]"
          :multiple="isMultiByLvl(1)"
          collapse-tags
          size="small"
          @change="()=> handleChangeLvl1(true)"
        >
          <el-option v-if="isAllLvl1" label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="toggleAll(1)">전체</span></el-option>
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
          size="small"
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
          size="small"
          @change="handleChangeLvl3"
        >
          <el-option
            v-for="(option, i) in lvlOptions[key3]"
            :key="i"
            :label="option.label"
            :value="option.value"
          />
        </el-select>
      </div>
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import { onMessagePopup } from '@/utils/index'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
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
    isAllLvl1: { /* isAll 옵션이 레벨별로 필요할 경우 array로 변경하여 처리할 것 */
      type: Boolean,
      default: true
    },
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
        2: [{
            'label': '-------',
            'value': '000000'
        }],
        3: [{
            'label': '-------',
            'value': '000000'
        }]
      },
      localValue: { 1: '', 2: '', 3: '' },
      localLabel: { 1: '', 2: '', 3: '' },
    }
  },
  // 1: ssvcLineTypeCd, ssvcLineCdMultiStr
  // 2: ssvcGroupCdMultiStr: 123;234;345; / ssvcGroupCdMulti: [] or ssvcGroupCd
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
  methods: {
    onResetParameter() {
      this.localValue = { 1: '', 2: '', 3: '' }
      this.localLabel = { 1: '', 2: '', 3: '' }
      if (this.defaultValueLvl1 !== null) {
        this.$set(this.localValue, 1, this.defaultValueLvl1)
        this.handleChangeLvl1()
      }
    },
    init() {
      /* default options, value setting */
      if (this.propsLvlOptions !== null) {
        this.lvlOptions = this.getMergedOptions(this.lvlOptions, this.propsLvlOptions)
      }
      if (this.defaultValueLvl1 !== null) {
        this.$set(this.localValue, 1, this.defaultValueLvl1)
        this.handleChangeLvl1()
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
    async setParameter(params) {
      this.$set(this.localValue, key1, this.multi.includes(key1) ? params.ssvcLineCdMultiStr.split(';') : params.ssvcLineTypeCd)
      this.$set(this.localValue, key2, this.multi.includes(key2) ? params.ssvcGroupCdMultiStr.split(';') : params.ssvcGroupCd)
      this.$set(this.localValue, key3, params.ssvcObjCd)
      await this.handleChangeLvl1(false)
      await this.handleChangeLvl2(false)
    },
    getParameter() {
      const params = []
      const lvl1Key = `ssvcLine${this.multi.includes(key1) ? 'CdMulti' : 'TypeCd'}`
      const lvl2Key = `ssvcGroupCd${this.multi.includes(key2) ? 'Multi' : ''}`
      //  ssvcGroupCdMulti: this.localValue[key2],
      const parameterKeys = [lvl1Key, lvl2Key, 'ssvcObjCd']

      parameterKeys.forEach((key, idx) => {
        if (this.lvl >= (idx + 1)) {
          let value = ''
          if (Array.isArray(this.localValue[idx + 1])) {
            params.push({ key: `${key}Str`, value: this.localValue[idx + 1].join(';') })
            // value = this.localValue[idx + 1].join(';')
          }
          value = this.localValue[idx + 1] ?? ''
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
    async handleChangeLvl1(isReset = true) {
        const isOver = this.updateSelectionWithAll(1)
        const lvlOptions = this.lvlOptions
        this.localLabel[key1] = this.lvlOptions[key1].find(v => v.value === this.localValue[key1])?.label ?? ''
        if (isOver) return
        const params = { ssvcLineTypeCd: this.localValue[key1] }
        try {
          const res = await apiRequestJson(ipmsJsonApis.selectAuthCenterList, params)
          this.lvlOptions[key2] = res?.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => { return { value: v.ssvcGroupCd, label: v.ssvcGroupNm } })
          if (isReset) {
          this.resetLocalValue(key2)
          this.resetLocalValue(key3)
        }
          this.emitEventToParent(this.getParameter())
          Eventbus.$emit(EventType.changeLvl1, { ssvcLineTypeCd: this.localValue[key1] })
        } catch (error) {
          this.error(error)
        }
    },
    async handleChangeLvl2(isReset = true) {
      const isOver = this.updateSelectionWithAll(2)
      this.localLabel[key2] = this.lvlOptions[key2].find(v => v.value === this.localValue[key2])?.label ?? ''
      if (isOver) return
      if (this.multi.includes(2) && this.localValue[key2].length > 1) return
      const params = { ssvcLineTypeCd: this.localValue[key1], ssvcGroupCd: Array.isArray(this.localValue[key2]) ? this.localValue[key2][0] : this.localValue[key2] || '' }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectAuthNodeList, params)
        this.lvlOptions[key3] = res.tbLvlBasVos?.filter(v => v.ssvcGroupNm !== '전체').map(v => { return { value: v.ssvcObjCd, label: v.ssvcObjNm } })
        if (isReset) {
          this.resetLocalValue(key3)
        }
        this.emitEventToParent(this.getParameter())
        Eventbus.$emit(EventType.changeLvl2, { ssvcLineTypeCd: this.localValue[key1], [this.getKeyLvl2()]: this.getValueLvl2() })
      } catch (error) {
        this.error(error)
      }
    },
    handleChangeLvl3() {
      this.emitEventToParent(this.getParameter())
      this.localLabel[key3] = this.lvlOptions[key3].find(v => v.value === this.localValue[key3])?.label ?? ''
      Eventbus.$emit(EventType.changeLvl3, {
        ssvcLineTypeCd: this.localValue[key1],
        [this.getKeyLvl2()]: this.getValueLvl2(),
        ssvcObjCd: this.localValue[key3]
      })
    },
    getKeyLvl2() {
      return `ssvcGroupCd${Array.isArray(this.localValue[key2]) ? 'MultiStr' : ''}`
    },
    getValueLvl2() {
      if (this.multi.includes(key2)) {
        return this.localValue[key2].join(';') + ';'
      } else {
        return this.localValue[key2]
      }
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
      return this.lvlOptions[lvl].map(option => option.value).filter(v => v !== '')
    },
    toggleAll(lvl) {
      if (this.multi.includes(lvl)) {
        this.$set(this.localValue, lvl, this.localValue[lvl]?.includes('') ? [] : ['', ...this.getFullOptions(lvl)])
        this.onCheckLimit(lvl)
      }
    },
    updateSelectionWithAll(lvl) {
      if (!this.multi.includes(lvl)) return false

      const fullOptionLen = this.getFullOptions(lvl).length
      const valueByLvlLen = this.localValue[lvl].length
      const isIncludesAll = this.localValue[lvl].includes('')
      if (valueByLvlLen === fullOptionLen && !isIncludesAll) {
        this.localValue[lvl].push('')
      } else if (isIncludesAll && valueByLvlLen !== fullOptionLen + 1) {
        this.localValue[lvl] = this.localValue[lvl]?.filter(value => value !== '')
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
.SsvcLineType {
  display: flex;
}
</style>
