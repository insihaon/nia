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
        :prop-column="ipBlockColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="ipInfoList"
        :prop-grid-indx="1"
        :prop-on-click="handleClickRow"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            IP 이력관리 조회 결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalIpInfoDetail ref="ModalIpInfoDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalIpInfoDetail from '@/views-ipms/modal/ModalIpInfoDetail'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'IpHistManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpInfoDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        { key: 'ServiceOrg', props: { isMulti: false } },
        { key: 'SipCreateType', props: { } },
        { key: 'IpBlockStatus', props: { label: '할당상태' } },
        { key: 'CheckYear', props: {} },
        { key: 'IpAddress', props: {} },
        { key: 'InputType', props: { prop_parameterKey: 'nbitmask', label: 'BitMask' } },
        {
          key: 'InputSearchDetail',
          props: {
            label: '장비명',
            modalName: 'ModalFacilityInformation',
            valueName: 'ssubscnealias',
            prop_parameterKey: { sicisofficescodeNe: 'sofficecode', smodelnameNe: 'smodelname', ssubscmstipNe: 'ssubscmstip', ssubscnealiasNe: 'ssubscnealias' },
          }
        },
        { key: 'LineInformation', props: { } },
        { key: 'WorkSystem', props: {} },
        { key: 'DetailedWorkClassification', props: {} },
        { key: 'InputType', props: { prop_parameterKey: 'screateId', label: '작업자' } },
        { key: 'DateRange', props: {} },
      ],
      ipBlockColumns: [
        { prop: 'sworkSystem', label: '작업시스템', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'smenuNm', label: '메뉴명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipHistTaskNm', label: '상세작업분류', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'screateNm', label: '작업자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dcreateDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscnealias', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubsclgipportdescription', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListIpHistoryMst(requestParameter)
    },
    handleClickRow(row) {
      this.fnViewDetailIPMst(row)
    },
    async fnViewListIpHistoryMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListIpHistoryMst, parameter)
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
      this.fnViewListIpHistoryMst()
    },
    async fnViewDetailIPMst(row) {
      const { nipHistMstSeq, nipAssignMstSeq, nipAllocMstSeq, yyyy } = row
      try {
        const params = {
          nipHistMstSeq: nipHistMstSeq ?? '',
          nipAssignMstSeq: nipAssignMstSeq ?? '',
          nipAllocMstSeq: nipAllocMstSeq ?? '',
          yyyy: yyyy ?? ''
        }
        const res = await apiRequestModel(ipmsModelApis.viewDetailIpHistMst, params)
        if (res.result.data) {
          // lagacy에서는 ipHistoryMstVo로 받는다.
          this.$refs.ModalIpInfoDetail.open({ tbIpInfoVo: res.result.data, type: 'Hist' })
        }
      } catch (error) {
        this.error(error)
      }
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListIpHistoryMstExcel')
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
