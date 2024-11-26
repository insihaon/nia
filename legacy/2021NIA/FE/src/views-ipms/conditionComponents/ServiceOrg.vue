<template>
  <fragment>
    <th>
      서비스
    </th>
    <td>
      <div>
        <el-select
          ref="multiSelect"
          v-model="values"
          filterable
          :multiple="isMulti"
          collapse-tags
          size="small"
          @change="handleChange()"
          @visible-change="handleDropdownVisibility"
        >
          <el-option v-if="isAllOption" label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="handleClickAll">전체</span></el-option>
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
import { EventType } from '@/min/types'
import Eventbus from '@/utils/event-bus'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ServiceOrg'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    defaultValue: {
      type: String,
      default: null
    },
    isMulti: {
      type: Boolean,
      default: true
    },
    isAllOption: {
      type: Boolean,
      default: true
    },
    limit: {
      type: Number,
      default: null
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
      options: [
        { label: '기업고객(고정)', value: 'SA0001' },
        { label: '홈고객(유동)', value: 'SA0002' },
        { label: '홈고객(고정)', value: 'SA0003' },
        { label: '홈고객(시설)', value: 'SA0004' },
        { label: 'VR1-Routing', value: 'SA0005' },
        { label: 'VR2-Routing', value: 'SA0006' },
        { label: 'VR3-Routing', value: 'SA0007' },
        { label: '미분류서비스', value: 'SA1001' },
        { label: '홈고객(Secured IP)', value: 'SA1008' },
        { label: '타사이관', value: 'SA1009' },
        { label: '홍콩DC구축용', value: 'SA1010' },
        { label: 'Cloud', value: 'SA1011' },
        { label: '대군화 시설용', value: 'SA1014' }
      ],
      values: [],
      multiValue: ''
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(option => option.value).filter(values => values !== 'ALL')
    },
  },
  mounted () {
    Eventbus.$on(EventType.changeLvl1, (params) => {
      this.onLoadServiceList(params)
    })
  },
  beforeDestroy() {
    Eventbus.$off(EventType.changeLvl1)
  },
  // sassignTypeCd, sassignTypeCdMultiStr: SA0001;SA0002;SA0003;
  methods: {
    onResetParameter() {
      if (this.defaultValue !== null) {
        this.values = this.defaultValue
      }
      this.values = this.isMulti ? [] : ''
    },
    init() {
      this.values = this.isMulti ? [] : ''
      if (this.prop_options !== null) {
        this.options = this.prop_options
      }
      if (this.defaultValue !== null) {
        this.values = this.defaultValue
      }
      this.emitEventToParent(this.getParameter())
    },
    handleDropdownVisibility(isVisible) {
      // if (!isVisible) {
      //   // 드롭다운이 닫힐 때 실행
      //   if (this.values.length > this.limit && this.limit > 0) {
      //     this.limitReached = true
      //     this.$message.error({ message: '10개까지 선택가능' })
      //     this.$nextTick(() => {
      //       // 드롭다운을 다시 열기
      //       this.$refs.multiSelect.toggleMenu()
      //     })
      //   } else {
      //     this.limitReached = false
      //   }
      // }
    },
    handleChange() {
      if (this.isMulti) {
        this.updateSelectionWithAll()
        this.limit !== null && this.onCheckLimit('서비스')
      }
      this.emitEventToParent(this.getParameter())
    },
    handleClickAll() {
      if (this.isMulti) {
        this.toggleAll()
        this.limit !== null && this.onCheckLimit('서비스')
      }
    },
    async onLoadServiceList(params) {
      this.values = this.isMulti ? [] : ''
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSassignTypeCdList, params)
        this.options = res.tbIpAllocMstVos.map(v => { return { value: v.sassignTypeCd, label: v.sassignTypeNm } })
      } catch (error) {
        this.error(error)
      }
    },
    getParameter() {
      let key = 'sassignTypeCd'
      let value = this.values
      if (this.isMulti) {
        key = 'sassignTypeCdMultiStr'
        value = this.values.filter(v => v !== 'ALL').join(';')
      }
      return [{ key, value }]
    },
    setParameter(params) {
      setTimeout(() => {
        if (this.isMulti) {
          this.values = params?.sassignTypeCdMultiStr?.split(';') ?? []
        } else {
          this.values = params?.sassignTypeCd ?? ''
        }
      }, 30)
    }
  }
}
</script>

<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
