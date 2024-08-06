<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
      @save-excel="handleClickExcelBtn"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="ipBlockColumns"
        :prop-data="ipHistList"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="ipInfoList"
        :prop-grid-indx="1"
        :prop-on-click="handleClickRow"
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
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalIpInfoDetail from '@/views-ipms/modal/ModalIpInfoDetail'

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
      ipHistList: []
    }
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnViewListIpHistoryMst(requestParameter)
    },
    handleClickRow(row) {
      this.fnViewDetailIPMst(row)
    },
    async fnViewListIpHistoryMst(params) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListIpHistoryMst, params)
        this.ipHistList = res.result.data ?? []
      } catch (error) {
        this.error(error)
      }
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
    async handleClickExcelBtn(params) {
      console.log(params)
      /* legacy param
      {
        pageIndex: 1
        pageUnit: 10
        sicisofficescodeNe: XXXXXX
        ssubscnealiasNe:
        smodelnameNe:
        ssubscmstipNe:
        ssubscnnescode: --
        sllnum:
        ssaid:
        sordernum:
        PageLoad:
        ssvcLineTypeCd: CL0001
        ssvcGroupCd:
        sassignTypeCd:
        sipCreateTypeCd: CT0001
        sassignLevelCd:
        yyyy: 2024
        sipVersionTypeCd: CV0001
        searchWrd:
        nbitmask:
        llSrchTypeCd: llnum
        sworkSystem:
        nipHistTaskCd:
        screateId:
        searchBgnDe:
        searchEndDe:
      }
      /ipmgmt/allocmgmt/viewListIpAllocMstExcel.json
      */
     /*  try {
        param: {
          "ssvcLineTypeCd": "",
          "ssvcGroupCd": "",
          "ssvcObjCd": "",
          "sassignTypeCd": "",
          "sipCreateTypeCd": "CT0001",
          "sassignLevelCd": "",
          "yyyy": "2024",
          "sipVersionTypeCd": "CV0001",
          "searchWrd": "",
          "nbitmask": "",
          "sicisofficescodeNe": "",
          "smodelnameNe": "",
          "ssubscmstipNe": "",
          "ssubscnealiasNe": "",
          "llSrchTypeCd": "llnum",
          "sllnum": "",
          "ssaid": "",
          "sordernum": "",
          "sworkSystem": "",
          "nipHistTaskCd": "",
          "screateId": "",
          "searchBgnDe": "",
          "searchEndDe": ""
        }
        const res = await apiExcel('/ipmgmt/historymgmt/viewListIpHistoryMstExcel.json', params)
     } catch (error) {
        this.error(error)
     } */
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
