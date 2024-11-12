<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: 100%"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            배치 연동 이력 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
const routeName = 'BatchLinkHistStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      componentList: [
        { key: 'InputType', props: { label: '인터페이스 ID', prop_parameterKey: 'sifId' } },
        { key: 'IncludeYN', props: { label: '작업 종료여부', prop_parameterKey: 'sbatchEndYn' } },
        { key: 'DateRange', props: { label: 'DATA 입력 \n시작/종료시각' } }
      ],
    }
  },
  computed: {
    tableColumns() {
      return [
        { prop: 'sifId', label: '인터페이스 ID', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'ssysNm', label: '관련 시스템명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'stbNm', label: '관련 테이블명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'scomment', label: '작업내역', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sstepStatus', label: '작업내역 상세', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sbatchEndYn', label: '작업 종료여부', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'dstartDt', label: 'DATA 입력 시작시각', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row?.dstartDt ? this.moment(row.dstartDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'dendDt', label: 'DATA 입력 종료시각', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row?.dendDt ? this.moment(row.dendDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
      ]
    }
  },
  mounted() {
    setTimeout(() => {
      this.fnViewListBatchHistMst()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListBatchHistMst(requestParameter)
    },
    async fnViewListBatchHistMst(requestParameter = null) {
      const params = requestParameter ?? this.$refs.searchCondition.requestParameter
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListBatchHistMst, params)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListBatchHistMst()
    },
  }
}
</script>
<style lang="css" scoped>
</style>
