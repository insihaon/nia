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
        :prop-span-method="spanByIpmsIpblock"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            IP주소 라우팅 비교/점검
          </span>
        </template>
        <template slot="add-count">
          IPMS DB 기준 건수 : {{ (totalCount3 || 0).toLocaleString() }} 건 / 체크박스 기준 건수 : {{ (totalCount2 || 0).toLocaleString() }} 건 /
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button v-if="isShowRoutMngBtn" type="primary" size="mini" round @click="handleClickRoutChk">라우팅 수집/DB 비교 시작</el-button>
            <el-button type="primary" size="mini" round @click="handleClickRoutExcptMng">예외처리 관리</el-button>
            <el-button type="primary" size="mini" round @click="handleClickDivMerge">IP블록 (해지 후) 분할/병합</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalIpAllocInsert ref="ModalIpAllocInsert" />
    <ModalIpAllocDetail ref="ModalIpAllocDetail" />
    <ModalIpInfoDetail ref="ModalIpInfoDetail" />
    <ModalNextHopDetail ref="ModalNextHopDetail" />
    <ModalRoutChkExcptMst ref="ModalRoutChkExcptMst" @reload="fnViewListRoutChkMst($refs.searchCondition.requestParameter)" />
    <ModalRoutExcptMstDetail ref="ModalRoutExcptMstDetail" />
    <ModalRoutServiceChkMst ref="ModalRoutServiceChkMst" @reload="fnViewListRoutChkMst($refs.searchCondition.requestParameter)" />
    <ModalRoutChkMst ref="ModalRoutChkMst" @reload="fnViewListRoutChkMst($refs.searchCondition.requestParameter)" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpAllocInsert from '@/views-ipms/modal/alloc/ModalIpAllocInsert'
import ModalIpAllocDetail from '@/views-ipms/modal/alloc/ModalIpAllocDetail'
import ModalIpInfoDetail from '@/views-ipms/modal/ModalIpInfoDetail'
import ModalNextHopDetail from '@/views-ipms/modal/rout/ModalNextHopDetail'
import ModalRoutChkExcptMst from '@/views-ipms/modal/rout/ModalRoutChkExcptMst'
import ModalRoutExcptMstDetail from '@/views-ipms/modal/rout/ModalRoutExcptMstDetail'
import ModalRoutServiceChkMst from '@/views-ipms/modal/rout/ModalRoutServiceChkMst'
import ModalRoutChkMst from '@/views-ipms/modal/rout/ModalRoutChkMst'

import { EventType } from '@/min/types'
import Eventbus from '@/utils/event-bus'
import { ipmsModelApis, ipmsJsonApis, apiRequestModel, apiRequestJson, apiRequestExcel } from '@/api/ipms'
import { onMessagePopup } from '@/utils'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'IpAdressRoutingCompare'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpAllocInsert, ModalIpAllocDetail, ModalIpInfoDetail, ModalNextHopDetail, ModalRoutChkExcptMst, ModalRoutExcptMstDetail, ModalRoutServiceChkMst, ModalRoutChkMst },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      totalCount2: 0,
      totalCount3: 0,
      componentList: [
        {
          key: 'SsvcLineType', props: { isAllLvl1: false, lvl: 3, defaultValueLvl1: 'CL0001',
          propsLvlOptions: {
              1: [
                // all false 조건 추가 해야함
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
                { label: 'MOBILE', value: 'CL0003' }
              ]
            }
          }
        },
        { key: 'ConditionByBlocksize', props: {} },
        { key: 'ServiceOrg', props: {} },
        { key: 'IpBlockStatus', props: { isMulti: true } },
        { key: 'SipCreateType', props: {} },
        { key: 'IpAddress', props: { isShowSelectBox: false } },
        { key: 'InputType', props: { prop_parameterKey: 'nbitmask', label: 'BitMask', valueType: 'number' } },
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'IP', value: 'PIP_PREFIX' },
              { label: 'BitMask', value: 'NBITMASK' }
            ]
          }
        },
        { key: 'Progress', props: {} },
        { key: 'ExceptionYN', props: { defaultValue: 'N' } },
        { key: 'IpAddressMulti', props: {} }
      ],
      isShowRoutMngBtn: true,
      // columnVisible 여부처리를 위한 computed 감지 변수, IP블록 분할/병합 처리 시 서비스 변경작업에 필요한 값
      ssvcLineTypeCd: 'CL0001',
      // IP블록 분할,병합 처리 시 서비스 변경작업에 필요한 값
      sipCreateTypeCd: 'CT0001'
    }
  },
  computed: {
    tableColumns() {
      const ssvcLineTypeCd = this.ssvcLineTypeCd ?? 'CL0001'
      return [
        {
          prop: 'level', label: '계위', children: [
            { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true },
            { prop: 'ssvcGroupNm', label: '본부', width: 140, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'ssvcObjNm', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
            { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', columnVisible: true, showOverflow: true },
            { prop: 'sassignTypeNm', label: '서비스', width: 140, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ], align: 'center', columnVisible: true, showOverflow: true,
          },
        {
          prop: 'ipms', label: 'IPMS', children: [
            { prop: 'pipmsIpPrefix', label: 'IP블록', width: 160, align: 'center', columnVisible: true, showOverflow: true,
                formatter: (row, col, value, index) => {
                  return this.$createElement('el-button', {
                    attrs: {
                      round: true, // Adding the round option
                      plain: true,
                      type: 'primary',
                      size: 'mini'
                    },
                    on: { click: () => {
                      this.fnViewDetailRoutChkMst(row.nipAssignMstSeq)
                    }
                  }
                }, row.pipmsIpPrefix)
              }
            },
            { prop: 'nipAllocMstCnt', label: '회선', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'sassignLevelNm', label: 'IP블록상태', width: 140, align: 'center', columnVisible: true, showOverflow: true },
          ], align: 'center', columnVisible: true, showOverflow: true,
        },
        {
          prop: 'routing', label: '실제 라우팅 장비', children: [
            { prop: 'pipmsIpPrefix', label: 'IP블록', width: 160, align: 'center', columnVisible: true, showOverflow: true,
              formatter: (row, col, value, index) => {
                if ([30, 29].includes(row.nroutingIpBitmask) && row.sneossDdYn === 'N') {
                  return this.$createElement('el-button', {
                    attrs: {
                      round: true, // Adding the round option
                      plain: true,
                      type: 'primary',
                      size: 'mini'
                    },
                    on: { click: () => {
                      // 링크 상세 정보
                      // param: sipNexthop, ssvcLineTypeCd, proutingIpPrefix, nroutingIpBitmask
                      this.fnViewDetailNexthop(row, 'link')
                  } } }, row.pipmsIpPrefix)
                } else {
                  return row.pipmsIpPrefix
                }
              }
            },
            { prop: 'sipNexthop', label: 'Nexthop', width: 160, align: 'center', sortable: true, columnVisible: ssvcLineTypeCd !== 'CL0003', showOverflow: true,
              formatter: (row, col, value, index) => {
                if (row.sipNexthop !== '-') {
                  return this.$createElement('el-button', {
                    attrs: {
                      round: true, // Adding the round option
                      plain: true,
                      type: 'primary',
                      size: 'mini'
                    },
                    on: { click: () => {
                      // 시설 상세 정보
                      this.fnViewDetailNexthop(row, 'host')
                  } } }, row.sipNexthop)
                } else {
                  return row.sipNexthop
                }
              }
            },
            { prop: 'scommunity', label: 'Community', align: 'center', columnVisible: ssvcLineTypeCd === 'CL0003', showOverflow: true },
            { prop: 'suseRouting', label: '사용장비', align: 'center', columnVisible: ssvcLineTypeCd === 'CL0003', showOverflow: true },
            { prop: 'sroutingUseYn', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
          ], align: 'center', columnVisible: true, showOverflow: true,
        },
        { prop: 'check', label: '체크박스', align: 'center', sortable: false, columnVisible: true, showOverflow: true, type: 'selection' },
        { prop: 'ntargetCnt', label: '분할/병합 건수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'scollect_dt', label: '장비수집일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sexcpt_yn', label: '예외처리여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            if (row.sexcptYnOrigin === 'Y') {
              return this.$createElement('el-button', {
                attrs: {
                  round: true, // Adding the round option
                  plain: true,
                  type: 'primary',
                  size: 'mini'
                },
                on: { click: () => {
                  // 예외 상세 정보
                  this.fnViewExcptDetail(row.nipAssignMstSeq)
                }
              } }, row.sexcpt_yn)
            } else {
              return row.sexcpt_yn
            }
          }
        },
        { prop: '_sallocGubun', label: 'IP할당/해지', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            let btnLabel = '-'
            if (row.sallocGubun === 'ALLOC') {
              btnLabel = 'IP 할당'
            } else if (row.sallocGubun === 'ASSIGN') {
              btnLabel = 'IP 해지'
            }
            return this.$createElement('el-button', {
              attrs: {
                round: true, // Adding the round option
                plain: true,
                type: 'primary',
                size: 'mini'
              },
              on: { click: () => {
                if (row.sallocGubun === 'ALLOC') {
                  this.fnAlloc(row)
                } else if (row.sallocGubun === 'ASSIGN') {
                  this.fnAssign(row)
                }
            } } }, btnLabel)
          }
        },
        { prop: 'sdbIntgrmRsltNm', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ]
    },
    isLineTypeCdMobile() {
      if (this.$refs?.searchCondition?.requestParameter?.ssvcLineTypeCd === 'CL0003') {
        return true
      } else {
        return false
      }
    }
  },
  mounted () {
    Eventbus.$on(EventType.changeLvl1, (params) => { this.isShowRoutMngBtn = !(params.ssvcLineTypeCd === 'CL0003') })
    setTimeout(() => {
      // this.fnViewListRoutChkMst()
    }, 200)
  },
  beforeDestroy() {
    Eventbus.$off(EventType.changeLvl1)
  },
  methods: {
    handleSearch(requestParameter) {
      this.ssvcLineTypeCd = requestParameter.ssvcLineTypeCd ?? 'CL0001'
      this.sipCreateTypeCd = requestParameter.sipCreateTypeCd ?? 'CT0001'
      this.fnViewListRoutChkMst(requestParameter)
    },
    fnViewDetailNexthop(row, spageType) {
      this.$refs.ModalNextHopDetail.open({ row, spageType })
    },
    fnViewExcptDetail(nipAssignMstSeq) {
      this.$refs.ModalRoutExcptMstDetail.open({ nipAssignMstSeq })
    },
    async fnViewListRoutChkMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListRoutChkMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
        this.totalCount2 = res.result.totalCount2
        this.totalCount3 = res.result.totalCount3
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListRoutChkMst()
    },
    async fnViewDetailRoutChkMst(nipAssignMstSeq) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailRoutChkMst, { nipAssignMstSeq })
        // this.tbIpInfoVo = res.result.data
        if (res.result.data) {
          this.$refs.ModalIpInfoDetail.open({ tbIpInfoVo: res.result.data, type: 'Rout' })
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnAlloc(row) {
      const { sipVersionTypeCd, nipmsIpBitmask, nipAssignMstSeq, nlvlMstSeq, nroutingChkMstSeq } = row
      if (sipVersionTypeCd === 'CV0001' && nipmsIpBitmask < 16) {
        onMessagePopup(this, '/16 보다 큰 IP블록은 할당할 수 없습니다.')
        return
      }
      const ipAllocOperMstVos = [{ nipAssignMstSeq }]
      // 할당 처리 후 현행화 정보 업데이트 요청 파라미터
      const tbRoutChkMstVo = { nlvlMstSeq, nroutingChkMstSeq }
      const ipAllocOperMstListVo = { ipAllocOperMstVos, tbRoutChkMstVo, menuType: 'Rout' }
      // 서비스 정보가 없을 때 처리
      if (row.sassignTypeCd === 'SA0000') {

        // confirmDialog = createLayerConfirm("confirmDialog", 'auto', 400, "서비스 지정 후 할당 작업을 진행하셔야 합니다.", function() {

        // var tbRoutChkMstVo = new Object();
        // tbRoutChkMstVo.proutingIpPrefix = pipPrefix;
        // tbRoutChkMstVo.ssvcLineTypeCd = "${searchVo.ssvcLineTypeCd}";
        // tbRoutChkMstVo.sipCreateTypeCd = $("#sipCreateTypeCd").val();
        // tbRoutChkMstVo.spageType = "SAME";

        // var param = JSON.stringify(tbRoutChkMstVo);
        // var url = baseContext + "ipmgmt/routmgmt/viewPopRoutServiceChkMst.ajax";
        // doAjaxSubmit(url, param, "html", "json", "fnViewPopRoutServiceChkMstCallback");
        // });
      } else {
        try {
         const res = await apiRequestModel(ipmsModelApis.viewPopInsertAlcIPMst, ipAllocOperMstListVo)
         if (res.result) {
          this.$refs.ModalIpAllocInsert.open({ ipAllocOperMstVos: res.result.data })
         }
        } catch (error) {
          this.error(error)
        }
      }
    },
    fnAssign(row) {
      this.$refs.ModalIpAllocDetail.open({ row, menuType: 'Rout' })
    },
    getChkedList() {
      const checkedList = []
      const tempSelection = this.$refs.compTable?.$refs?.table?.selection || []
      tempSelection.forEach(row => {
        let value = `${row.scollectDtOrigin}_${row.nlvlMstSeq}_${row.nroutingChkMstSeq}_${row.sipPrefixGubun}_${row.sexcptYnOrigin}_${row.ntargetCnt}_${row.sdbIntgrmRsltCd}`
        value = value.replaceAll('null', '')
        checkedList.push(value)
      })
      return checkedList
    },
    handleClickRoutChk() {
      const { 1: ssvcLineTypeCd, 2: ssvcGroupCd, 3: ssvcObjCd } = this.$refs.searchCondition.$refs.SsvcLineType[0].localValue
      const ssvcCds = { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd }

      const { 1: ssvcLineTypeNm, 2: ssvcGroupNm, 3: ssvcObjNm } = this.$refs.searchCondition.$refs.SsvcLineType[0].localLabel
      const ssvcNms = { ssvcLineTypeNm, ssvcGroupNm, ssvcObjNm }
      if (!ssvcCds.ssvcLineTypeCd) {
        onMessagePopup(this, '서비스망 선택 후 실제 라우팅 DB 수집이 가능합니다.')
        return
      }
      if (ssvcCds.ssvcLineTypeCd !== 'CL0003') {	// KORNET, PREMIUM
        if (!ssvcGroupCd || ssvcGroupCd === '000000') {
          onMessagePopup(this, '본부 선택 후 실제 라우팅 DB 수집이 가능합니다.')
          return
        }
        if (!ssvcCds.ssvcObjCd || ssvcObjCd === '000000') {
          onMessagePopup(this, '국사 선택 후 실제 라우팅 DB 수집이 가능합니다.')
          return
        }
      }
      this.$refs.ModalRoutChkMst.open({ ssvcCds, ssvcNms })
    },
    handleClickRoutExcptMng() {
      const checkedList = this.getChkedList()
      if (checkedList.length === 0) {
        onMessagePopup(this, '예외처리 관리할 대상을 먼저 선택하세요.')
        return
      }
      let sexcptYn_0 = ''
      let sexcptYn = ''
      let isSame = false
      let isImpossibleState = true
      checkedList.forEach((item, i) => {
        if (item.indexOf('_' >= 0)) {
          const tmp = item.split('_')
          if (i === 0) {
            sexcptYn_0 = tmp[4]
          } else {
            sexcptYn = tmp[4]
          }
          if (checkedList.length > 1) {
            if (sexcptYn_0 === sexcptYn) {
              isSame = true
            }
          } else {
            isSame = true
          }
          if (tmp[6] === 'DR0004' || tmp[6] === 'DR0002') {
            isImpossibleState = false
          }
        }
      })
      if (!isSame) {
        onMessagePopup(this, '예외처리 건 혹은 예외처리 해제 건만 동일하게 선택 가능합니다.')
        return
      }
      if (!isImpossibleState) {
        onMessagePopup(this, '[IP블록 크기 변경 성공], [IPMS,장비 완전 일치] 상태인 IP 블록은 예외 처리가 불가능 합니다.')
        return
      }
      if (sexcptYn_0 === 'Y') {
        this.confirm(`선택하신 ${checkedList.length}건을 예외처리 해제 하시겠습니까?`, '확인', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warning',
        }).then(async() => {
          try {
            const tbRoutChkMstVo = { chkListStr: checkedList, sexcpt_yn: 'N' }
            // ipmgmt/routmgmt/insertListExcptMst.json
            const res = await apiRequestJson(ipmsJsonApis.insertListExcptMst, tbRoutChkMstVo)
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '예외처리 해제가 정상적으로 처리되었습니다.')
              this.fnViewListRoutChkMst(this.$refs.searchCondition.requestParameter)
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          }
        })
      } else if (sexcptYn_0 === 'N') {
        this.confirm(`선택하신 ${checkedList.length}건을 예외처리 하시겠습니까?`, '확인', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warning',
        }).then(async() => {
            const tbRoutChkMstVo = { chkListStr: checkedList }
            this.$refs.ModalRoutChkExcptMst.open({ tbRoutChkMstVo })
        })
      } else {
        onMessagePopup(this, '예외처리 및 예외처리 해제가 불가능합니다.')
        return
      }
    },
    handleClickDivMerge() {
      const checkedList = this.getChkedList()
      if (checkedList.length === 0) {
        onMessagePopup(this, 'IP Block (해지 후) 분할/병합할 정보를 선택 후 가능합니다.')
        return
      }
      if (checkedList.length > 30) {
        onMessagePopup(this, '체크 건수는 30건까지 가능합니다.')
        return
      }
      let isDivMerge = true
      let isExcptYn = true
      let isStatus = true
      let allCnt = 0
      checkedList.forEach((item, i) => {
        if (item.indexOf('_') >= 0) {
          const tmp = item.split('_')
          if (Number(tmp[5]) === 0) { // 분할/병합 건수
            isDivMerge = false
          }
          allCnt += Number(tmp[5])
          if (tmp[4] === 'Y') { // 예외처리 여부
            isExcptYn = false
          }
          if (tmp[6] === 'DR0004') { // 진행상태
            isStatus = false
          }
        }
      })
      if (!isStatus) {
        onMessagePopup(this, '[IPMS,장비 완전 일치] 상태인 IP블록은 IP블록 (해지 후) 분할/병합이 불가능합니다.')
        return
      }
      if (!isDivMerge) {
        onMessagePopup(this, 'IP블록이 동일한 경우엔 IP블록 (해지 후) 분할/병합이 불가능합니다.')
        return
      }
      if (!isExcptYn) {
        onMessagePopup(this, '예외처리한 IP블록은  IP블록 (해지 후) 분할/병합이 불가능합니다.')
        return
      }
      if (checkedList.length > 1 && allCnt > 30) {
        onMessagePopup(this, '선택하신 건들의 분할/병합 건수가 30건 초과면 1개의 대상만 선택 후 IP블록 (해지 후) 분할/병합이 가능합니다.')
        return
      }
      this.fnSelectCheckService({ chkListStr: checkedList })
    },
    async fnSelectCheckService(param) { // IP블록 현행화 전 서비스 조회
      try {
        let confirmMsg = ''
        let tbRoutChkMstVo = {}
        const res = await apiRequestJson(ipmsJsonApis.selectCheckService, param)
        if (res.commonMsg === 'SUCCESS01') {
          confirmMsg = 'IP블록이 실제 라우팅 장비와 다를 경우에만 진행되며, IP블록상태가 [할당/할당예약]일 경우 해지 후 IP블록 분할/병합이 진행됩니다.'
          tbRoutChkMstVo = { chkListStr: param.chkListStr, menuType: 'Rout' }
          this.fnInsertListIpBlockMatchMst({ chkListStr: param.chkListStr, menuType: 'Rout' })
        } else if (res.commonMsg === 'SUCCESS02') {
          confirmMsg = '분할/병합 건 중 서비스가 동일하지 않거나 서비스 지정이 안된 건이 있습니다. 서비스 변경 후 작업을 진행하셔야 합니다.'
          tbRoutChkMstVo = {
            strIpBlock: res.strIpBlock /* 선택된 row들의 IPMS-IP블록 값 array */,
            ssvcLineTypeCd: this.ssvcLineTypeCd,
            sipCreateTypeCd: this.sipCreateTypeCd,
            menuType: 'Rout'
          }
        } else {
          onMessagePopup(this, res.commonMsg)
          return
        }
        this.confirm(confirmMsg, '확인', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warning',
        }).then(() => {
          if (res.commonMsg === 'SUCCESS01') {
            this.fnInsertListIpBlockMatchMst(tbRoutChkMstVo)
          } else if (res.commonMsg === 'SUCCESS02') {
            this.$refs.ModalRoutServiceChkMst.open({ tbRoutChkMstVo })
          }
        }).catch(action => {
        })
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertListIpBlockMatchMst(tbRoutChkMstVo) {
      try {
        const res = await apiRequestJson(ipmsJsonApis.insertListIpBlockMatchMst, tbRoutChkMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '작업이 정상적으로 처리되었습니다')
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListRoutChkMstExcel')
    },
    spanByIpmsIpblock({ row, column, rowIndex, columnIndex }) {
      const tableDatas = this.pagination.data
      const sameIpList = []
      let pass = false
      for (let index = 0; index < rowIndex; index++) {
        if (tableDatas[index].pipmsIpPrefix === row.pipmsIpPrefix) {
          pass = true
          break
        }
      }
      for (let index = rowIndex + 1; index < tableDatas.length; index++) {
        if (tableDatas[index].pipmsIpPrefix === row.pipmsIpPrefix) {
          sameIpList.push(tableDatas[index])
        }
      }
      if (![8, 9, 10].includes(columnIndex)) {
        if (pass) {
          return {
            rowspan: 0,
            colspan: 0
          }
        } else if (sameIpList.length === 0) {
          return {
            rowspan: 1,
            colspan: 1
          }
        } else {
          return {
            rowspan: sameIpList.length + 1,
            colspan: 1
          }
        }
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
