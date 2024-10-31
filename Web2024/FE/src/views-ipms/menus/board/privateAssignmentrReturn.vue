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
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            사설AS 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewDetailPrvAs('', 'create')">신청</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalDetailPrivateAs ref="ModalDetailPrivateAs" @reload="fnViewListPrivateAs()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalDetailPrivateAs from '@/views-ipms/modal/notice/ModalDetailPrivateAs.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'PrivateAssignmentrReturn'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalDetailPrivateAs },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'nrequestAsSeq', label: 'AS번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsCtm', label: '고객명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dcreateDt', label: '신청일', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return this.moment(row.dcreateDt).format('YYYY-MM-DD HH:mm:ss') } },
        { prop: 'screateNm', label: '요청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjNm1', label: '노드1 명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjLlnum1', label: '노드1 전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjNm2', label: '노드2 명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjLlnum2', label: '노드2 전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dapvDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dapvDt ? this.moment(row.dapvDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'srequestAsTypeNm', label: '신청', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'BoardSearchCondition', props: {
            defaultValue: 'asNum',
            prop_options: [
              { label: 'AS번호', value: 'asNum' },
              { label: '고객명', value: 'asCtm' },
              { label: '요청자', value: 'credateId' },
            ]
          }
        },
        {
          key: 'ApplyStatus', props: {
          prop_parameterKey: 'srequestAsTypeCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '신청', value: 'RS0201' },
              { label: '승인', value: 'RS0202' },
              { label: '반려', value: 'RS0203' },
              { label: '배정', value: 'RS0204' },
            ]
          }
        },
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: 'DCREATE_DT',
            sortOrdrDefaultVal: 'DESC',
            label: '정렬조건', prop_options: [
              { label: '신청일', value: 'DCREATE_DT' },
              { label: 'AS번호', value: 'NREQUEST_AS_SEQ' },
              { label: '처리일시', value: 'DAPV_DT' },
            ]
          }
        },
      ],
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
        const res = await apiRequestModel(ipmsModelApis.viewListPrivateAs, parameter)
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
    onClcikRow(row) {
      this.fnViewDetailPrvAs(row, 'detail')
    },
    async fnViewDetailPrvAs(row, type) {
      if (type === 'detail') {
        if (row.nrequestAsApyTxnSeq === null || row.nrequestAsApyTxnSeq === '') {
          return
        }
        try {
          const tbRequestAsApyTxnVo = {
            nrequestAsApyTxnSeq: row.nrequestAsApyTxnSeq
          }
          const res = await apiRequestModel(ipmsModelApis.viewDetailPrivateAs, tbRequestAsApyTxnVo)
          this.$refs.ModalDetailPrivateAs.open({ row: res.result.data, type: type })
        } catch (error) {
          console.error(error)
        }
      } else {
        this.$refs.ModalDetailPrivateAs.open({ type: type })
      }
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListPrivateAsExcel')
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
