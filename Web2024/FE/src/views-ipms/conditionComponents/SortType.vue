<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      정렬
    </label>
    <el-select
      v-model="sortType"
      size="mini"
    >
      <el-option
        v-for="(option, i) in soreTypeOptions"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
    <el-radio v-model="sortOrdr" label="ASC">오름차순</el-radio>
    <el-radio v-model="sortOrdr" label="DESC">내림차순</el-radio>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'SortType'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: String,
      default: ''
    },
    exceptOptions: { /* 예외처리 option */
      type: Object,
      default() { return {} }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      soreTypeOptions: [
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
  computed: {
  },
  methods: {
    handleChangeSelect() {
      this.$emit('update-value', [{ key: 'sortType', value: this.sortType }])
    },
    handleChangeRadio() {
      this.$emit('update-value', [{ key: 'sortOrdr', value: this.sortOrdr }])
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
