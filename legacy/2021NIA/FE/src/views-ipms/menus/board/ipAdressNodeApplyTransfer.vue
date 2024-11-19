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
            조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewInsertNode()">신청</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalNodeTransferDetail ref="ModalNodeTransferDetail" @reload="fnViewListIpBlockMst" />
    <ModalNodeTransferInsert ref="ModalNodeTransferInsert" @reload="fnViewListIpBlockMst" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalNodeTransferDetail from '@/views-ipms/modal/ModalNodeTransferDetail.vue'
import ModalNodeTransferInsert from '@/views-ipms/modal/ModalNodeTransferInsert.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'IpAdressNodeApplyTransfer'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalNodeTransferDetail, ModalNodeTransferInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'seq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        {
          prop: 'defore',
          label: '변경전',
          children: [
            { prop: 'beforeSsvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'beforeSsvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'beforeSsvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: 'after',
          label: '변경후',
          children: [
            { prop: 'afterSsvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'afterSsvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'afterSsvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        { prop: 'sUserNm', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dCreateDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row) => this.moment(row.dCreateDt).format('YYYY-MM-DD')
        },
        { prop: 'dCompleteDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row) => row.dCompleteDt ? this.moment(row.dCompleteDt).format('YYYY-MM-DD') : ''
         },
        { prop: 'progressStatusNm', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'IpAddress', props: { isShowSelectBox: false } },
        { key: 'ApplyStatus', props: { prop_parameterKey: 'progressStatus' } },
        {
          key: 'SortType', props: {
            label: '등록기간',
            sortTypeDefaultVal: 'dcreate_dt',
            sortOrdrDefaultVal: 'DESC',
            prop_options: [
              { label: '신청일', value: 'dcreate_dt' },
              { label: '신청번호', value: 'seq' },
              { label: '처리일시', value: 'dcomplete_dt' },
            ]
          }
        },
      ],
      resultListVo: []
    }
  },
  mounted() {
    setTimeout(() => {
      this.fnViewListIpBlockMst()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListIpBlockMst(requestParameter)
    },
    async fnViewListIpBlockMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListNode, parameter)
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
      this.fnViewListIpBlockMst()
    },
     onClcikRow(row) {
       this.fnViewDetailNode(row)
    },
     async fnViewDetailNode(row) {
      try {
        const { seq } = row
        const nodeVo = { seq: seq }
        const res = await apiRequestModel(ipmsModelApis.viewDetailNode, nodeVo)
        if (res.result.data) {
          this.$refs.ModalNodeTransferDetail.open({ row: res.result.data })
        }
      } catch (error) {
        console.error(error)
      }
    },
    fnViewInsertNode() {
      this.$refs.ModalNodeTransferInsert.open()
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListNodeExcel')
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
