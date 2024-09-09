<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-select
      v-model="sortType"
      size="mini"
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
  </el-col>
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
      sortOrdr: 'ASC'
    }
  },
  methods: {
    init() {
      if (this.prop_options !== null) {
        this.sortTypeOptions = this.prop_options
      }
      if (this.sortTypeDefaultVal !== null) {
        this.sortType = this.sortTypeDefaultVal
      }
      this.emitEventToParent([{ key: 'sortType', value: this.sortType }, { key: 'sortOrdr', value: this.sortOrdr }])
    },
    handleChangeRadio() {
      this.emitEventToParent([{ key: 'sortOrdr', value: this.sortOrdr }])
    },
    setParameter(params) {
      this.sortType = params?.sortType ?? ''
      this.sortOrdr = params?.sortOrdr ?? ''
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
