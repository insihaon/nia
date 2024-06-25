<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 배정 조회결과
          </span>
        </template>
      </comptable></el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
const routeName = 'IpAssign'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SipCreateType', props: {} },
        { key: 'GenerationDegree', props: {} },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitmask' } },
        { key: 'DateRange', props: {} },
        { key: 'IpAddress', props: {} },
        { key: 'ServiceOrg', props: { limit: 10 } },
        { key: 'IpBlockStatus', props: { label: '배정상태' } },
        { key: 'SortType', props: {} },
        { key: 'IncludeYN', props: { label: 'Summary 포함 여부', prop_parameterKey: 'snull0Yn' } },
        { key: 'IncludeYN', props: { label: 'DB-라우팅 일치 여부', prop_parameterKey: 'sintgrmYn' } },
        { key: 'InputType', props: { label: '라우팅 중복 개수', prop_parameterKey: 'nsummaryCnt' } },
      ],
      tableColumns: [
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '배정상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      tableDatas: []
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  }
}
</script>
<style lang="scss" scoped></style>
