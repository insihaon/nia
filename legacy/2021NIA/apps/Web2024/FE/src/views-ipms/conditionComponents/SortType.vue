<template>
  <fragment>
    <th>
      {{ label }}
    </th>
    <td class="textflex">
      <el-select
        v-model="sortType"
        size="small"
        @change="emitEventToParent([{ key: 'sortType', value: sortType }])"
      >
        <el-option
          v-for="(option, i) in sortTypeOptions"
          :key="i"
          :label="option.label"
          :value="option.value"
        />
      </el-select>
      <el-radio v-model="sortOrdr" label="ASC" @change="handleChangeRadio">오름차순</el-radio>
      <el-radio v-model="sortOrdr" label="DESC" @change="handleChangeRadio">내림차순</el-radio>
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'SortType'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    label: {
      type: String,
      default: '정렬'
    },
    sortTypeDefaultVal: {
      type: String,
      default: null
    },
    sortOrdrDefaultVal: {
      type: String,
      default: 'DESC'
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
      sortTypeOptions: [
        // { label: '전체', value: '' },
        { label: 'IP', value: 'PIP_PREFIX' },
        { label: 'BitMask', value: 'NBITMASK' },
        { label: '작업일자', value: 'DMODIFY_DT' },
        { label: 'I/F명', value: 'SSUBSCLGIPPORTDESCRIPTION' },
      ],
      sortType: 'PIP_PREFIX',
      sortOrdr: 'DESC'
    }
  },
  methods: {
    onResetParameter() {
      this.sortType = this.sortTypeDefaultVal ?? 'PIP_PREFIX'
      this.sortOrdr = this.sortOrdrDefaultVal ?? 'DESC'
    },
    init() {
      if (this.prop_options !== null) {
        this.sortTypeOptions = this.prop_options
      }
      if (this.sortTypeDefaultVal !== null) {
        this.sortType = this.sortTypeDefaultVal
      }
      if (this.sortOrdrDefaultVal !== null) {
        this.sortOrdr = this.sortOrdrDefaultVal
      }
      this.emitEventToParent([{ key: 'sortType', value: this.sortType }, { key: 'sortOrdr', value: this.sortOrdr }])
    },
    handleChangeRadio() {
      this.emitEventToParent([{ key: 'sortOrdr', value: this.sortOrdr }])
    },
    setParameter(params) {
      this.sortType = params?.sortType ?? ''
      this.sortOrdr = params?.sortOrdr ?? 'DESC'
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep.el-radio {
  background-image: none !important;
  border-right: solid 0px;
}
</style>
