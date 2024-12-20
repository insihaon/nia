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
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-dbl-click="hadleClickIpAssignDetail"
        :prop-on-select="handleClickTableCheck"
        :prop-max-select="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            IP 블록관리 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button size="mini" icon="el-icon-document-add" type="primary" round @click="handleOpenInsertModal('create')">신규생성</el-button>
            <el-button size="mini" icon="el-icon-plus" type="primary" round @click="handleOpenInsertModal('generate')">추가생성</el-button>
            <el-button size="mini" icon="el-icon-tickets" type="primary" round @click="handleOpenDetailModal('detail')">상세</el-button>
            <el-button size="mini" icon="el-icon-edit-outline" type="primary" round @click="handleOpenDetailModal('edit')">수정</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalIpBlockDetail ref="ModalIpBlockDetail" @reload="fnViewListIpBlockMst($refs.searchCondition.requestParameter)" />
    <ModalAddIpBlock ref="ModalAddIpBlock" @reload="fnViewListIpBlockMst($refs.searchCondition.requestParameter)" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpBlockDetail from '@/views-ipms/modal/ModalIpBlockDetail.vue'
import ModalAddIpBlock from '@/views-ipms/modal/ModalAddIpBlock.vue'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import { downloadExcel } from '@/views-ipms/js/common-function'
import moment from 'moment'
const routeName = 'IpBlockManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDetail, ModalAddIpBlock },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, propIsCheckBox: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateSeqNm', label: '생성차수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP 블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nclassCnt', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          //  formatter: (row) => {
          //   return this.formatNumber(row.nclassCnt, 0, 10)
          // }
        },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row) => moment(row.dmodifyDt).format('YYYY-MM-DD')
        },
      ],
      selectedRows: null,
      requestParam: null,
      sipCreateSeqCds: []
    }
  },
  computed: {
    componentList() {
      return [
        { key: 'SipCreateType', props: {} },
        { key: 'GenerationDegree', props: { prop_options: this.sipCreateSeqCds }, },
        { key: 'IpAddress', props: {} },
        { key: 'SsvcLineType', props: { label: '서비스망' } },
        { key: 'DateRange', props: {} },
        { key: 'SortType', props: {} },
      ]
    }
  },
  mounted() {
    setTimeout(async() => {
      await this.fnViewListIpBlockMst()
    }, 10)
    this.fnSelectSipCreateSeqCds()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListIpBlockMst(requestParameter)
    },
    async fnSelectSipCreateSeqCds() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSipCreateSeqCdsList, {})
        const sipCreateSeqCd = res.result.data.map(v => { return { value: v.code, label: v.name } })
        this.$set(this, 'sipCreateSeqCds', sipCreateSeqCd)
      } catch (error) {
        console.error(error)
      }
    },
    async fnViewListIpBlockMst(requestParameter = null) {
    const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
    const target = ({ vue: this.$refs.compTable })
    const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
    Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListCrtIPMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
        this.$nextTick(() => {
          this.$refs.compTable.$refs.table.selection.push(this.pagination.data[0])
        })
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
    handleClickTableCheck(all, cur) {
      this.selectedRows = cur
    },
    hadleClickIpAssignDetail(row) {
      this.$nextTick(() => {
        this.fnViewDetailClick(row, 'detail')
      })
    },
    onClcikRow(row) {

    },
    async fnViewDetailClick(row, type) {
      this.$refs.ModalIpBlockDetail.open({ row: row, type: type })
    },
    handleOpenDetailModal(type) {
      this.fnViewDetailClick(this.selectedRows ?? this.pagination.data[0], type)
    },
    handleOpenInsertModal(type) {
      this.viewInsertCrtIPMst(this.selectedRows ?? this.pagination.data[0], type)
    },
     viewInsertCrtIPMst(row, type) {
      const { nipBlockMstSeq } = row ?? ''
        let param
        if (type === 'create') {
          param = {
            searchUseYn: 'N'
          }
          this.$refs.ModalAddIpBlock.open({ row: '', type: type })
           return
        } else {
          param = {
            nipBlockMstSeq: nipBlockMstSeq,
            searchUseYn: 'Y'
          }
          this.$refs.ModalAddIpBlock.open({ row: param, type: type })
        }
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListCrtIPMstExcel')
    }
  }
}
</script>
<style lang="scss" scoped></style>
