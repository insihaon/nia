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
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            IP 배정신청 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewIPAssignApyPre()">배정신청</el-button>
          </div>
        </template>
      </compTable>
      <ModalAssignApyDetail ref="ModalAssignApyDetail" @reload="fnViewListAssignApyTxn" />
      <ModalAssignApyInsert ref="ModalAssignApyInsert" @reload="fnViewListAssignApyTxn" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalAssignApyDetail from '@/views-ipms/modal/ModalAssignApyDetail.vue'
import ModalAssignApyInsert from '@/views-ipms/modal/ModalAssignApyInsert.vue'

const routeName = 'IpResourceAssignmentApply'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalAssignApyDetail, ModalAssignApyInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'nrequestAssignSeq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'stitle', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sapyUserNm', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dapyDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => this.moment(row.dapyDt).format('YYYY-MM-DD') },
        { prop: 'srequestAssignTypeNm', label: '상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dtrtDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => row.dtrtDt ? this.moment(row.dtrtDt).format('YYYY-MM-DD') : '' },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        {
          key: 'ApplyStatus', props: {
          prop_parameterKey: 'srequestAssignTypeCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '신청', value: 'RS0301' },
              { label: '승인', value: 'RS0302' },
              { label: '반려', value: 'RS0303' },
              { label: '배정', value: 'RS0304' },
            ]
          }
        },
        { key: 'InputType', props: { label: '신청자' } },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListAssignApyTxn()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListAssignApyTxn(requestParameter)
    },
    async fnViewListAssignApyTxn(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListAssignApyTxn, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListAssignApyTxn()
    },
    onClcikRow(row) {
      this.fnViewDetailIpAssignApy(row, 'detail')
    },
    async fnViewDetailIpAssignApy(row, type) {
      try {
        const { nrequestAssignSeq } = row
        const TbRequestAssignMstVo = { nrequestAssignSeq: nrequestAssignSeq }
        const res = await apiRequestModel(ipmsModelApis.viewDetailAssignApyTxn, TbRequestAssignMstVo)
        if (res.result.data) {
          this.$refs.ModalAssignApyDetail.open({ row: res.result.data, type: type })
        }
      } catch (error) {
        console.error(error)
      }
    },
    fnViewIPAssignApyPre() {
      this.$refs.ModalAssignApyInsert.open()
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
