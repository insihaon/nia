<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
      @save-excel="handleClickExcelBtn"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-is-cell-click-check="true"
        :prop-max-select="tableDatas.length"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-dbl-click="hadleClickIpAllocDetail"
        :prop-on-select="handleClickTableCheck"
        @update:propCellClick="handleClickCell"
      >
        <template slot="text-description">
          <span>
            IP 할당 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="mt-1 d-flex justify-end">
            <el-button icon="el-icon-check" type="primary" size="mini" @click="fnViewCheckTacsIpBlock_">IP블럭 중복체크</el-button>
            <el-button icon="el-icon-thumb" style="background: #2b5890;" type="primary" size="mini" @click="fnInsertAlcBtnClick">할당</el-button>
            <el-button size="mini" @click="fnMergeBtnClick">병합</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
    <ModalIpBlockDivision ref="ModalIpBlockDivision" @reload="fnViewListIpAllocMst($refs.searchCondition.requestParameter)" />
    <ModalIpAllocInsert ref="ModalIpAllocInsert" />
    <ModalIpAllocDetail ref="ModalIpAllocDetail" @alocCallBtnClick="fnInsertAlcBtnClick" />
    <ModalDetailSummary ref="ModalDetailSummary" />
    <!-- IP블록병합 -->
    <ModalIpAssignMerge ref="ModalIpAssignMerge" @reload="fnViewListIpAllocMst($refs.searchCondition.requestParameter)" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpAllocInsert from '@/views-ipms/modal/alloc/ModalIpAllocInsert.vue'
import ModalIpAllocDetail from '@/views-ipms/modal/alloc/ModalIpAllocDetail.vue'
import ModalCheckTacsIpBlock from '@/views-ipms/modal/ModalCheckTacsIpBlock.vue'
import ModalIpBlockDivision from '@/views-ipms/modal/ModalIpBlockDivision.vue'
import ModalDetailSummary from '@/views-ipms/modal/ModalDetailSummary.vue'

import ModalIpAssignMerge from '@/views-ipms/modal/assign/ModalIpAssignMerge.vue'

import { allocTableDatas } from './sample.js'
import { fnViewCheckTacsIpBlock } from '@/views-ipms/js/common-function'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'IpAllocation'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision, ModalIpAllocDetail, ModalCheckTacsIpBlock, ModalIpAllocInsert, ModalIpAssignMerge, ModalDetailSummary },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },

        { key: 'SOffice', props: { apiPath: '/ipmgmt/linemgmt', voName: 'tbIpAssignSubVos', valueKey: { cd: 'sofficecode', nm: 'sofficename' } } },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { limit: 10 } },
        { key: 'IpAddress', props: {} },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitMask' } },
        { key: 'LineInformation', props: {} },
        {
          key: 'IpBlockStatus', props: {
            label: '할당상태', prop_options: [
              { label: '서비스배정[미할당]', value: 'IA0004' },
              { label: '할당예약', value: 'IA0005' },
              { label: '할당', value: 'IA0006' },
            ]
          }
        },
        { key: 'DateRange', props: { } },
        {
          key: 'InputSearchDetail',
          props: {
            label: '장비명',
            modalName: 'ModalFacilityInformation',
            valueName: 'ssubscnealias',
            prop_parameterKey: { sicisofficescodeNe: 'sofficecode', smodelnameNe: 'smodelname', ssubscmstipNe: 'ssubscmstip', ssubscnealiasNe: 'ssubscnealias' },
          }
        },
        { key: 'SortType', props: { } },
        { key: 'IncludeYN', props: { label: 'Summary 포함 여부', prop_parameterKey: 'snull0Yn' } },
        { key: 'IncludeYN', props: { label: 'DB-라우팅 일치여부', prop_parameterKey: 'sintgrmYn' } },
        { key: 'InputType', props: { label: '라우팅 중복 개수', prop_parameterKey: 'nsummaryCnt' } },
      ],
      tableColumns: [
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nipAllocMstCnt', label: '회선', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscnealias', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubsclgipportdescription', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'snull0Yn', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sintgrmYn', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nsummaryCnt', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              class: row.nsummaryCnt > 0 ? 'red' : '',
              on: { click: () => {
                this.$refs.ModalDetailSummary.open({ row })
            } } }, row.nsummaryCnt)
          }
        },
        { prop: 'division', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              class: row.sassignLevelCd === 'IA0004' ? '' : 'red',
              on: { click: () => {
                if (row.sassignLevelCd === 'IA0004') {
                  this.fnViewInsertDivAsgnIPMst(row)
                }
            } } }, row.sassignLevelCd === 'IA0004' ? '분할' : '불가')
          }
        },
        { prop: 'scomment', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: allocTableDatas,
      selectedRows: []
    }
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnViewListIpAllocMst(requestParameter)
    },
    async fnViewListIpAllocMst(requestParameter) {
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListIpAllocMst, requestParameter)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleClickCell(params) {
      // if (['분할', '라우팅 중복 개수'].includes(params.column.label)) return
      // this.fnViewDetailAlcIpMst(params.row)
    },
    fnViewDetailAlcIpMst(row) {
      this.selectedRows = [row]
      this.$refs.ModalIpAllocDetail.open({ row })
    },
    hadleClickIpAllocDetail(row) {
      this.fnViewDetailAlcIpMst(row)
    },
    handleClickTableCheck(all, cur) {
      this.selectedRows = all
    },
    async fnViewInsertDivAsgnIPMst(row) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewInsertDivAsgnIPMst, { nipAssignMstSeq: row.nipAssignMstSeq, typeFlag: 'Aloc' })
        if (res.result.data) {
          this.$refs.ModalIpBlockDivision.open({ result: res.result.data })
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnViewCheckTacsIpBlock_() {
      fnViewCheckTacsIpBlock(this, this.selectedRows)
    },
    fnInsertAlcBtnClick() {
      const rows = this.selectedRows
      if (rows.length === 0) {
        onMessagePopup(this, '할당 할 대상이 없습니다.')
        return
      }
      /* 체크대상여부 확인 */
      const res = rows.map((row, i) => {
        /* Step 01. 서비스배정상태 체크 */
        if (row.sassignLevelCd !== 'IA0004') {
          onMessagePopup(this, '할당 대상 블록 중 서비스배정이 아닌 블록이 있습니다.')
          return false
        }
        /* Step 02. 계위 및 서비스 유형 동일 선택 체크 */
        const { sassignTypeCd, nlvlMstSeq, nbitMask, sneossDdYn, ssvcLineTypeCd, sipCreateTypeCd, sipVersionTypeCd } = row

        let linkYn = 'N'
        let linkYn2 = 'N'
        if (['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd)) {
          if (sneossDdYn === 'N' && [29, 30].includes(nbitMask)) {
            linkYn = 'Y'
          }
        }
        for (let j = 0; j < i; j++) {
          if (sassignTypeCd !== rows[j].sassignTypeCd || nlvlMstSeq !== rows[j].nlvlMstSeq) {
            onMessagePopup(this, '선택하신 할당 대상 블록의 계위/서비스 정보가 동일하지 않습니다. 확인해주세요.')
            return false
          }
          if (['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd)) {
            if (rows[j].sneossDdYn === 'N' && [29, 30].includes(rows[j].nbitMask)) {
              linkYn2 = 'Y'
            }
          }
        }
        if (linkYn !== linkYn2) {
          onMessagePopup(this, '선택하신 할당 대상 블록의 할당구분(회선/시설/링크) 정보가 동일하지 않습니다. 확인해주세요.')
          return false
        }
        if ((sipVersionTypeCd === 'CV0001' && nbitMask < 16) || (sipVersionTypeCd === 'CV0002' && nbitMask < 48)) {
          onMessagePopup(this, '/16(IPv4), /48(IPv6) 보다 큰 IP블록은 할당할 수 없습니다.')
          return false
        }
        if (ssvcLineTypeCd === 'CL0005' && sipCreateTypeCd !== 'CT0001') {
          onMessagePopup(this, 'VPN망은 공인 IP블록만 할당할 수 있습니다.')
          return false
        }
        return true
      })
      res.every(r => r === true) && this.$refs.ModalIpAllocInsert.open({ ipAllocOperMstVos: rows })
    },
    fnMergeBtnClick() {
      const checkedList = this.selectedRows
      const { nipBlockMstSeq, sassignLevelCd, sassignTypeCd, nlvlMstSeq, sipCreateTypeCd } = checkedList[0]
      let validateText = ''
      if (checkedList.length === 0) {
        validateText = '병합할 대상이 없습니다.'
      } else if (checkedList.length === 1) {
        validateText = '병합할 대상은 최소 2개이상 선택해 주시기 바랍니다.'
      }
      checkedList.forEach(row => {
        if (nipBlockMstSeq !== row.nipBlockMstSeq) {
          validateText = '병합할 대상 정보들의 생성 유형이 동일하지 않습니다.'
        }
        if (sassignLevelCd !== row.sassignLevelCd) {
          validateText = '병합할 대상 정보들의 작업 상태가 동일하지 않습니다.'
        }
        if (sassignTypeCd !== row.sassignTypeCd) {
          validateText = '병합할 대상 정보들의 서비스가 동일하지 않습니다.'
        }
        if (nlvlMstSeq !== row.nlvlMstSeq) {
          validateText = '병합할 대상 정보들의 계위(조직)정보가 동일하지 않습니다.'
        }
        if (sipCreateTypeCd !== row.sipCreateTypeCd) {
          validateText = '병합할 대상 정보들의 생성 유형이 동일하지 않습니다.'
        }
        if (row.sassignLevelCd === 'IA0005' || row.sassignLevelCd === 'IA0006') {
          validateText = '병합할 대상 정보들의 작업 상태가 할당확정/할당예약 일 경우 병합할 수 없습니다.'
        }
      })
      if (validateText.length > 0) {
        onMessagePopup(this, validateText)
        return
      }
      const tbIpAssignMstListVo = { typeFlag: 'Aloc', tbIpAssignMstVos: [] }
      checkedList.forEach(row => { tbIpAssignMstListVo.tbIpAssignMstVos.push({ nipAssignMstSeq: row.nipAssignMstSeq }) })
      this.$refs.ModalIpAssignMerge.open({ tbIpAssignMstListVo })
    },
    async handleClickExcelBtn(params) {
      /* legacy param
      {
        pageIndex: 1
        pageUnit: 10
        sicisofficescodeNe: XXXXXX
        ssubscnealiasNe:
        smodelnameNe:
        ssubscmstipNe:
        ssubscnnescode:
        sllnum:
        ssaid:
        sordernum:
        PageLoad:
        ssvcLineTypeCd: CL0001
        ssvcGroupCd:
        ssvcGroupCdMultiStr: 383040;
        ssvcObjCd: R00454
        sofficecode:
        sipCreateTypeCd: CT0001
        sassignTypeCd:
        sassignTypeCdMultiStr:
        sipVersionTypeCd: CV0001
        searchWrd:
        nbitmask:
        llSrchTypeCd: llnum
        sassignLevelCd: IA0004
        searchBgnDe:
        searchEndDe:
        sortType: PIP_PREFIX
        sortOrdr: ASC
        snull0Yn:
        sintgrmYn:
        nsummaryCnt:
      }
      /ipmgmt/allocmgmt/viewListIpAllocMstExcel.json
      */
      /*  try {
        const res = await apiExcel('/ipmgmt/allocmgmt/viewListIpAllocMstExcel.json', params)
     } catch (error) {
        this.error(error)
     } */
    }
   },
}
</script>
<style lang="scss" scoped></style>
