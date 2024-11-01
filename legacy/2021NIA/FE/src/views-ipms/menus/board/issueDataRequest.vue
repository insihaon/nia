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
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        :prop-on-click="onClcikRow"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            요구사항 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewDetailReq('', 'create')">글쓰기</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalReqDetail ref="ModalReqDetail" @reload="fnViewListReq($refs.searchCondition.requestParameter)" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalReqDetail from '@/views-ipms/modal/notice/ModalReqDetail.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'IssueDataRequest'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalReqDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'seq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardDivision', label: '요청사항구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardTitle', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sUserNm', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardDcreateDt', label: '등록일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardDesireDate', label: '희망완료일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardExpectedDate', label: '완료예정일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardImportance', label: '중요도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardProgress', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'ApplyStatus', props: {
          label: '요청사항 구분',
          prop_parameterKey: 'RboardDivision',
          prop_options: [
              { label: '전체', value: '' },
              { label: '오류 버그 수정', value: 'RES001' },
              { label: '기능 개발 요청', value: 'RES002' },
              { label: '자료 요청', value: 'RES003' },
              { label: '연동 요청', value: 'RES004' },
            ]
          }
        },
        { key: 'BoardSearchCondition', props: {} },
        {
          key: 'ApplyStatus', props: {
          label: '진행상태',
          prop_parameterKey: 'RboardProgress',
          prop_options: [
              { label: '전체', value: '' },
              { label: '요청사항 접수', value: 'RES005' },
              { label: '접수 반려', value: 'RES006' },
              { label: '조치 진행 중', value: 'RES006' },
              { label: '조치 완료', value: 'RES007' },
            ]
          }
        },
        { key: 'DateRange', props: { label: '등록기간' } },
      ],
    }
  },
  mounted() {
    this.fnViewListReq()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListReq(requestParameter)
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListReq()
    },
    async fnViewListReq(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListReq, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onClcikRow(row, type) {
     this.fnViewDetailReq(row, 'detail')
    },
    async fnViewDetailReq(row, type) {
      if (type === 'detail') {
        this.$refs.ModalReqDetail.open({ row: row, type: type })
      } else {
        this.$refs.ModalReqDetail.open({ type: type })
      }
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListReqExcel')
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
