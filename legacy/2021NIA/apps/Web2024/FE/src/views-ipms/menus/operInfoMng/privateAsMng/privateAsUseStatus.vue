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
        :prop-on-click="handleClickRow"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            사설AS 사용현황 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalDetailAsHist ref="ModalDetailAsHist" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalDetailAsHist from '@/views-ipms/modal/ModalDetailAsHist.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'PrivateAsUseStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalDetailAsHist },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'nrequestAsSeq', label: 'AS번호', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'srequestAsTypeNm', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'srequestAsCtm', label: '고객명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'apyDt', label: '신청일', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row.apyDt ? this.moment(row.apyDt).format('YYYY-MM-DD') : '' } },
        { prop: 'apyUserNm', label: '요청자', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjNm1', label: '노드1 명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjLlnum1', label: '노드1 전용번호', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjNm2', label: '노드2 명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjLlnum2', label: '노드2 전용번호', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'smodifyNm', label: '최종수정자', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '최종수정일', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD') : '' } },
      ],
      componentList: [
        { key: 'InputType', props: { label: '사설AS 번호' } },
        { key: 'UsageYN', props: { label: '상태', prop_parameterKey: 'srequestAsTypeCd' } },
        { key: 'DateRange', props: { label: '최종수정기간' } },
      ]
    }
  },
  mounted() {
    setTimeout(() => {
      this.fnViewListPrivateAs()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListPrivateAs(requestParameter)
    },
    async fnViewListPrivateAs(requestParameter = null) {
     const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListAsHist, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListPrivateAs()
    },
    handleClickRow(row) {
      this.$refs.ModalDetailAsHist.open({ row })
    },
  }
}
</script>
<style lang="scss" scoped>
</style>
