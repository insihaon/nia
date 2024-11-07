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
        :prop-on-click="onClickRow"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            WHOIS 정보 변경 신청 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewDetailWhoisMod('', 'create')">신청</el-button>
          </div>
        </template>
      </compTable>
      <ModalDetailWhoisMod ref="ModalDetailWhoisMod" @reload="fnViewListWhoisModReq($refs.searchCondition.requestParameter)" />
      <ModalRegWhoisModReq ref="ModalRegWhoisModReq" @reload="fnViewListWhoisModReq($refs.searchCondition.requestParameter)" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalDetailWhoisMod from '@/views-ipms/modal/notice/ModalDetailWhoisMod.vue'
import ModalRegWhoisModReq from '@/views-ipms/modal/notice/ModalRegWhoisModReq.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'WhoisInfoChange'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalDetailWhoisMod, ModalRegWhoisModReq },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'nmodify_apply_seq', label: '신청번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sBefOrgName', label: '기관명(변경전)', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sAftOrgName', label: '기관명(변경후)', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sApplyNm', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dApplyDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dApplyDt ? this.moment(row.dApplyDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'dApprovalDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dApprovalDt ? this.moment(row.dApprovalDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'sStatNm', label: '상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'IpAddress', props: { isShowSelectBox: false } },
        {
          key: 'ApplyStatus', props: {
          prop_parameterKey: 'searchCnd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '신청', value: '10' },
              { label: '승인', value: '20' },
              { label: '반려', value: '30' },
            ]
          }
        },
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: 'DAPPLY_DT',
            label: '정렬조건', prop_options: [
              { label: '신청일', value: 'DAPPLY_DT' },
              { label: '요청번호', value: 'NMODIFY_APPLY_SEQ' },
              { label: '처리일', value: 'DAPPROVAL_DT' },
            ]
          }
        },
      ],
    }
  },
  mounted() {
    setTimeout(() => {
      this.fnViewListWhoisModReq()
    }, 100)
  },
  methods: {
     handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListWhoisModReq(requestParameter)
    },
   async fnViewListWhoisModReq(requestParameter) {
      const target = ({ vue: this.$refs.compTable })
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListWhoisModReq, parameter)
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
      this.fnViewListWhoisModReq()
    },
    onClickRow(row, type) {
      this.fnViewDetailWhoisMod(row, 'detail')
    },
    async fnViewDetailWhoisMod(row, type) {
      if (type === 'detail') {
        this.$refs.ModalDetailWhoisMod.open({ row: row, type: type })
      } else {
        this.$refs.ModalRegWhoisModReq.open() // 컨트롤러 메소드 호출
      }
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListWhoisModReqExcel')
    }
  },
}
</script>
<style lang="scss" scoped>
