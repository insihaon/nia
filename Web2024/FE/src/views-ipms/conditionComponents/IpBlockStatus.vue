<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <td>
      <div :class="{ [name]: true }">
        <el-select
          v-model="values"
          size="small"
          :multiple="isMulti"
          collapse-tags
          @change="handleChange()"
        >
          <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="handleClickAll">전체</span></el-option>
          <el-option
            v-for="(option, i) in options"
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
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'IpBlockStatus'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: 'IP 블록상태'
    },
    isMulti: {
      type: Boolean,
      default: false
    },
    prop_options: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      values: [],
      options: [
        { label: '미배정', value: 'IA0001' },
        { label: '예비배정', value: 'IA0002' },
        { label: '배정[미할당]', value: 'IA0003' },
        { label: '서비스배정[미할당]', value: 'IA0004' },
        { label: '할당예약', value: 'IA0005' },
        { label: '할당', value: 'IA0006' },
      ],
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(option => option.value).filter(v => v !== '')
    },
  },
  // sassignLevelVd
  methods: {
    onResetParameter() {
      this.values = this.isMulti ? [] : ''
    },
    init() {
      this.values = this.isMulti ? [] : ''
      if (this.prop_options !== null) {
        this.options = this.prop_options
      }
      this.emitEventToParent(this.getParameter())
    },
    handleChange() {
      if (this.isMulti) {
        this.updateSelectionWithAll()
      }
      this.emitEventToParent(this.getParameter())
    },
    handleClickAll() {
      if (this.isMulti) {
        this.toggleAll()
      }
    },
    getParameter() {
      let key = 'sassignLevelCd'
      let value = this.values
      if (this.isMulti) {
        key = 'sassignLevelCdMultiStr'
        value = this.values.filter(v => v !== '').join(';')
      }
      return [{ key, value }]
    },
    setParameter(params) {
      if (this.isMulti) {
        this.values = params?.sassignLevelCdMultiStr?.split(';') ?? ''
      } else {
        this.values = params?.sassignLevelCd ?? ''
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
