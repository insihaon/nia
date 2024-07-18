<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="hadleClickIpAllocDetail"
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
            <el-button icon="el-icon-check" type="primary" size="mini" @click="handleClickIpBlockCheck">IP블럭 중복체크</el-button>
            <el-button icon="el-icon-thumb" style="background: #2b5890;" type="primary" size="mini" @click="handleClickIpAllocInsert">할당</el-button>
            <el-button size="mini">병합</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
    <ModalIpBlockDivision ref="ModalIpBlockDivision" />
    <ModalIpAllocInsert ref="ModalIpAllocInsert" />
    <ModalIpAllocDetail ref="ModalIpAllocDetail" />
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

const routeName = 'IpAllocation'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDivision, ModalIpAllocDetail, ModalCheckTacsIpBlock, ModalIpAllocInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: {} },
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
        { prop: 'nsummaryCnt', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'division', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              class: row.sassignLevelCd === 'IA0004' ? '' : 'red',
              on: { click: () => {
                this.$refs.ModalIpBlockDivision.open({ row })
            } } }, row.sassignLevelCd === 'IA0004' ? '분할' : '불가')
          }
        },
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: [
        // {
        //   dmodifyDt: '2022-07-12',
        //   sassignLevelCd: 'IA0004',
        //   nbitMask: 26,
        //   nipAllocMstCnt: 1,
        //   nipAssignMstSeq: 980372,
        //   nsummaryCnt: '0',
        //   pipPrefix: '1.100.0.0/17',
        //   sassignLevelNm: '할당',
        //   sassignTypeNm: 'POOL-M2M',
        //   scomment: null,
        //   sintgrmYn: 'Y',
        //   sipCreateTypeNm: '공인',
        //   sipCreateTypeCd: 'CT0001',
        //   sllnum: '',
        //   snull0Yn: 'Y',
        //   ssubsclgipportdescription: null,
        //   ssubscnealias: 'GR_C-PGW#20',
        //   ssvcGroupNm: 'DATA망',
        //   ssvcLineTypeNm: 'MOBILE',
        //   ssvcLineTypeCd: 'CL0003',
        //   ssvcObjNm: 'DATA망(구로)',
        //   sipVersionTypeCd: 'CV0001',
        //   nlvlMstSeq: '20309',
        // },
        {
        bUploadFlag: false,
        commonMsg: null,
        dcreateDt: 'Tue Jul 12 09:41:23 KST 2022',
        dmodifyDt: 'Tue Jul 12 09:41:56 KST 2022',
        firstIndex: 1,
        lastIndex: 1,
        llSrchTypeCd: null,
        lvlMstSeqListVo: null,
        menuType: null,
        moveSearchWrd: '',
        moveSipVersionTypeCd: null,
        moveType: '',
        nbitmask: '17',
        nclassCnt: '128.0000000000',
        ncnt: '32768',
        nfirstAddr: '23330816',
        nfreeIpCnt: '32768',
        nipAllocMstCnt: '1',
        nipAllocMstSeq: null,
        nipAssignMstSeq: '980372',
        nipBlockMstSeq: '2743',
        nipLinkMstSeq: null,
        nipmsSvcSeq: '0',
        nlastAddr: '23363583',
        nlvlMstSeq: '20306',
        nroutingChkMstSeq: null,
        nsubnetmask: null,
        nsummaryCnt: '0',
        nticketActSeq: null,
        nuseIpCnt: '0',
        nwhoisSeq: null,
        pageIndex: 1,
        pageSize: 0,
        pageUnit: 0,
        paramMap: null,
        pifSerialIp: null,
        pifSerialIpSrch: null,
        pipPrefix: '1.100.0.0/17',
        recordCountPerPage: 10,
        rowNo: 1,
        saifname: null,
        sAlcSrchTypeCd: null,
        salocationcode: null,
        salocationcodeNm: null,
        samstip: null,
        sanealias: null,
        saofficescode: null,
        saofficescodeNm: null,
        sassignLevelCd: 'IA0006',
        sassignLevelNm: '할당',
        sassignTypeCd: 'SA1013',
        sassignTypeCdMultiStr: null,
        sassignTypeCds: null,
        sassignTypeMulti: null,
        sassignTypeNm: 'POOL-M2M',
        scomment: null,
        sconnAlias: null,
        sconnalias: null,
        screateEmail: null,
        screateId: '10149118',
        screateNm: null,
        scustName: null,
        searchBgnCd: '',
        searchBgnDe: '',
        searchCnd: '',
        searchEndCd: '',
        searchEndDe: '',
        searchUseYn: '',
        searchWrd: '',
        sexPushYn: null,
        sexSvcCd: null,
        sexSvcNm: null,
        sfirstAddr: '1.100.0.0',
        sfirstAddrBinary: '00000001011001000000000000000000',
        sfirstAddrGwip: '1.100.0.1',
        sfirstIpPreferred: '001.100.000.000',
        sgatewayip: null,
        sGubun: null,
        sicisofficescode: null,
        sicisofficescodeNe: null,
        sicisofficesname: null,
        sifipSrch: null,
        sintgrmYn: 'Y',
        sipAllocExTypeCd: 'AE0000',
        sipAllocExTypeNm: null,
        sipAssignSubNm: null,
        sipBlock: '1.100.0.0',
        sipCreateSeqCd: 'M202010001',
        sipCreateSeqNm: null,
        sipCreateTypeCd: 'CT0001',
        sipCreateTypeNm: '공인',
        sipmsSvcNm: null,
        sipVersionTypeCd: 'CV0001',
        sipVersionTypeNm: 'IPv4',
        slacpsid: null,
        slastAddr: '1.100.127.255',
        slastAddrBinary: '00000001011001000111111111111111',
        slastAddrGwip: '1.100.127.254',
        slastIpPreferred: '001.100.127.255',
        sllnum: '',
        sllnumSrch: null,
        slocationcodeNmSrch: null,
        smodelname: null,
        smodelnameNe: null,
        smodifyEmail: null,
        smodifyId: '10149118',
        smodifyNm: null,
        smstipSrch: null,
        snealiasSrch: null,
        sneossDdYn: 'N',
        sneSrchTypeCd: null,
        snetmask: '255.255.128.0',
        sNextHop: null,
        snull0Yn: 'Y',
        sofficecode: null,
        sofficename: null,
        sofficescodeSrch: null,
        sordernum: null,
        sortOrdr: '',
        sortType: '',
        spageType: null,
        sregyn: null,
        ssaid: null,
        sssvcMgroupNm: null,
        ssubsclgipportdescription: null,
        ssubsclgipportip: null,
        ssubscmstip: null,
        ssubscmstipNe: null,
        ssubscnealias: 'GR_C-PGW#20',
        ssubscnealiasNe: null,
        ssubscnealiasType: null,
        ssubscnescode: null,
        ssubscnnescode: null,
        ssubscrouterserialip: null,
        ssvcGroupCd: 'VV0010',
        ssvcGroupCdMulti: null,
        ssvcGroupCdMultiStr: null,
        ssvcGroupNm: 'DATA망',
        ssvcLgroupNm: null,
        ssvcLineTypeCd: 'CL0003',
        ssvcLineTypeNm: 'MOBILE',
        ssvcObjCd: 'VV0015',
        ssvcObjNm: 'DATA망(구로)',
        ssvcUseTypeCd: null,
        ssvcUseTypeNm: null,
        svalidCheck: null,
        szifname: null,
        szlocationcode: null,
        szlocationcodeNm: null,
        szmstip: null,
        sznealias: null,
        szofficescode: null,
        szofficescodeNm: null,
        totalCount: 0,
        typeFlag: '',
      }, {
        bUploadFlag: false,
        commonMsg: null,
        dcreateDt: 'Mon May 29 17:56:15 KST 2023',
        dmodifyDt: 'Mon May 29 17:56:32 KST 2023',
        firstIndex: 1,
        lastIndex: 1,
        llSrchTypeCd: null,
        lvlMstSeqListVo: null,
        menuType: null,
        moveSearchWrd: '',
        moveSipVersionTypeCd: null,
        moveType: '',
        nbitmask: '26',
        nclassCnt: '0.2500000000',
        ncnt: '64',
        nfirstAddr: '23363584',
        nfreeIpCnt: '64',
        nipAllocMstCnt: '0',
        nipAllocMstSeq: null,
        nipAssignMstSeq: '1108037',
        nipBlockMstSeq: '2743',
        nipLinkMstSeq: null,
        nipmsSvcSeq: '0',
        nlastAddr: '23363647',
        nlvlMstSeq: '20309',
        nroutingChkMstSeq: null,
        nsubnetmask: null,
        nsummaryCnt: null,
        nticketActSeq: null,
        nuseIpCnt: '0',
        nwhoisSeq: null,
        pageIndex: 1,
        pageSize: 0,
        pageUnit: 0,
        paramMap: null,
        pifSerialIp: null,
        pifSerialIpSrch: null,
        pipPrefix: '1.100.128.0/26',
        recordCountPerPage: 10,
        rowNo: 0,
        saifname: null,
        sAlcSrchTypeCd: null,
        salocationcode: null,
        salocationcodeNm: null,
        samstip: null,
        sanealias: null,
        saofficescode: null,
        saofficescodeNm: null,
        sassignLevelCd: 'IA0004',
        sassignLevelNm: '서비스배정[미할당]',
        sassignTypeCd: 'SA1013',
        sassignTypeCdMultiStr: null,
        sassignTypeCds: null,
        sassignTypeMulti: null,
        sassignTypeNm: 'POOL-M2M',
        scomment: '',
        sconnAlias: null,
        sconnalias: null,
        screateEmail: null,
        screateId: '10150810',
        screateNm: null,
        scustName: null,
        searchBgnCd: '',
        searchBgnDe: '',
        searchCnd: '',
        searchEndCd: '',
        searchEndDe: '',
        searchUseYn: '',
        searchWrd: '',
        sexPushYn: null,
        sexSvcCd: null,
        sexSvcNm: null,
        sfirstAddr: '1.100.128.0',
        sfirstAddrBinary: '00000001011001001000000000000000',
        sfirstAddrGwip: '1.100.128.1',
        sfirstIpPreferred: '001.100.128.000',
        sgatewayip: null,
        sGubun: null,
        sicisofficescode: null,
        sicisofficescodeNe: null,
        sicisofficesname: null,
        sifipSrch: null,
        sintgrmYn: null,
        sipAllocExTypeCd: 'AE0000',
        sipAllocExTypeNm: null,
        sipAssignSubNm: null,
        sipBlock: '1.100.128.0',
        sipCreateSeqCd: 'M202010001',
        sipCreateSeqNm: null,
        sipCreateTypeCd: 'CT0001',
        sipCreateTypeNm: '공인',
        sipmsSvcNm: null,
        sipVersionTypeCd: 'CV0001',
        sipVersionTypeNm: 'IPv4',
        slacpsid: null,
        slastAddr: '1.100.128.63',
        slastAddrBinary: '00000001011001001000000000111111',
        slastAddrGwip: '1.100.128.62',
        slastIpPreferred: '001.100.128.063',
        sllnum: null,
        sllnumSrch: null,
        slocationcodeNmSrch: null,
        smodelname: null,
        smodelnameNe: null,
        smodifyEmail: null,
        smodifyId: '10150810',
        smodifyNm: null,
        smstipSrch: null,
        snealiasSrch: null,
        sneossDdYn: 'N',
        sneSrchTypeCd: null,
        snetmask: '255.255.255.192',
        sNextHop: null,
        snull0Yn: null,
        sofficecode: null,
        sofficename: null,
        sofficescodeSrch: null,
        sordernum: null,
        sortOrdr: '',
        sortType: '',
        spageType: null,
        sregyn: null,
        ssaid: null,
        sssvcMgroupNm: null,
        ssubsclgipportdescription: null,
        ssubsclgipportip: null,
        ssubscmstip: null,
        ssubscmstipNe: null,
        ssubscnealias: null,
        ssubscnealiasNe: null,
        ssubscnealiasType: null,
        ssubscnescode: null,
        ssubscnnescode: null,
        ssubscrouterserialip: null,
        ssvcGroupCd: 'VV0010',
        ssvcGroupCdMulti: null,
        ssvcGroupCdMultiStr: null,
        ssvcGroupNm: 'DATA망',
        ssvcLgroupNm: null,
        ssvcLineTypeCd: 'CL0003',
        ssvcLineTypeNm: 'MOBILE',
        ssvcObjCd: 'VV0015',
        ssvcObjNm: 'DATA망(구로)',
        ssvcUseTypeCd: null,
        ssvcUseTypeNm: null,
        svalidCheck: null,
        szifname: null,
        szlocationcode: null,
        szlocationcodeNm: null,
        szmstip: null,
        sznealias: null,
        szofficescode: null,
        szofficescodeNm: null,
        totalCount: 0,
        typeFlag: '',
      }
      ],
      selectedRows: []
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    },
    handleClickCell(params) {
      if (params.column.label === '분할') return
      this.$refs.ModalIpAllocDetail.open({ row: params.row })
    },
    hadleClickIpAllocDetail(row) {
      // this.$refs.ModalIpAllocDetail.open(row)
    },
    handleClickTableCheck(all, cur) {
      this.selectedRows = all
    },
    handleClickIpBlockCheck() {
      const rows = this.selectedRows
      if (rows.length === 0) {
        onMessagePopup(this, 'IP블럭 중복체크할 대상이 없습니다. 선택해주세요.')
        return
      }
      if (rows.length > 1) {
        onMessagePopup(this, 'IP블럭 중복체크할 대상을 다건 선택 할 수 없습니다. 확인해주세요.')
        return
      }
      const { sipVersionTypeCd, ssvcLineTypeCd, nipAssignMstSeq } = rows[0]
      if (sipVersionTypeCd !== 'CV0001') {
        onMessagePopup(this, 'IP블럭 중복체크는 IPv4만 가능합니다.')
        return
      }
      if (ssvcLineTypeCd !== 'CL0001' && ssvcLineTypeCd !== 'CL0002' && ssvcLineTypeCd !== 'CL0003') {
        onMessagePopup(this, 'IP블럭 중복체크는 KOREAN, PREMIUM, MOBILE망만 가능합니다.')
        return
      }
      // const res = await api({ nipAssignMstSeq })
      this.$refs.ModalCheckTacsIpBlock.open({ /* tacsResponse: res */ row: rows[0] })
    },
    handleClickIpAllocInsert() {
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
    }
   },
}
</script>
<style lang="scss" scoped></style>
