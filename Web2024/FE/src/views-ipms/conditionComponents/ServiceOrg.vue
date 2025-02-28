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
          :multiple-limit="limit"
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
      options: [],
      values: [],
      multiValue: ''
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(option => option.value).filter(values => values !== 'ALL')
    },
  },
  async mounted () {
    if (this.prop_options === null) {
      await this.onLoadServiceList({ ssvcLineTypeCd: '' })
    }
    Eventbus.$on(EventType.changeLvl1, (params) => {
      this.onLoadServiceList(params)
    })
     this.init()
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
    async onLoadServiceList(params) {
      this.values = this.isMulti ? [] : ''
      /*
      sipVersionTypeCd: 'CV0002' 일 때 서비스 집군화 하여 조회
      isFacilities: 시설용 서비스유형만 조회
      */
      Object.assign(params, { sipVersionTypeCd: this.ipms.isFacilites ? 'CV0002' : 'CV0001', isFacilities: this.ipms.isFacilites })
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectOrgSassignTypeCdList, params)
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
        if (this.values.length === 0 && this.ipms.isFacilites) {
          value = this.options.filter(v => v.value !== 'ALL').map(v => v.value).join(';')
        }
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
