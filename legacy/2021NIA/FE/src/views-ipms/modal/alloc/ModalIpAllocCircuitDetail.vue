<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="true"
      :append-to-body="true"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        회선 상세정보
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <!-- 회선 정보 -->
        <div class="content_result mt0">
          <h4>회선 정보</h4>
          <table class="tbl_data mt5">
            <caption>회선 정보</caption>
            <colgroup>
              <col width="14%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">고객명</th>
                <td>{{ resultVo.scustname }}</td>
                <th scope="row">전용번호</th>
                <td>{{ resultVo.sllnum }}</td>
                <th scope="row">SAID</th>
                <td>{{ resultVo.ssaid }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">상품명</th>
                <td>{{ resultVo.sipmsSvcNm }}</td>
                <th scope="row">수용국</th>
                <td>{{ resultVo.sofficename }}</td>
                <th scope="row">KT 사업용여부</th>
                <td>{{ resultVo.ssvcUseTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">설치장소(지번)</th>
                <td colspan="5">{{ resultVo.sinstalladdress }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">설치장소(도로명)</th>
                <td colspan="5">{{ resultVo.sinstallroadaddress }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 청약 정보 -->
        <div id="ordSection" class="content_result">
          <h4>청약 정보</h4>
          <table class="tbl_data mt5">
            <caption>청약 정보</caption>
            <colgroup>
              <col width="14%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">오더번호</th>
                <td>{{ resultVo.sordernum }}</td>
                <th scope="row">희망일</th>
                <td>{{ resultVo.sodrhopedt }}</td>
                <th scope="row">접수일</th>
                <td>{{ resultVo.sodrregdt }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">접수자 참고사항</th>
                <td colspan="5">{{ resultVo.sreportOpinion }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 물리시설 정보 -->
        <div class="content_result">
          <h4>물리시설 정보</h4>
          <table class="tbl_data mt5" summary="물리시설 정보">
            <caption>물리시설 정보</caption>
            <colgroup>
              <col width="20%" />
              <col width="27%" />
              <col width="23%" />
              <col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">시설명</th>
                <td>{{ resultVo.ssubscnealias }}</td>
                <th scope="row">I/F명</th>
                <td>{{ resultVo.ssubsclgipportdescription }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">장비대표 IP</th>
                <td>{{ resultVo.ssubscmstip }}</td>
                <th scope="row">대국 인터페이스 IP</th>
                <td>{{ resultVo.ssubscrouterserialip }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">자국 인터페이스 IP</th>
                <td>{{ resultVo.ssubsclgipportip }}</td>
                <th scope="row">수용회선명</th>
                <td>{{ resultVo.sconnalias }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click="close()">
          닫기
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'

const routeName = 'ModalIpAllocCircuitDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {
        bUploadFlag: false,
        commonMsg: null,
        dcreateDt: 'Fri Jul 19 15:36:53 KST 2024',
        dmodifyDt: 'Fri Jul 19 15:38:44 KST 2024',
        firstIndex: 1,
        lastIndex: 1,
        llSrchTypeCd: null,
        lvlMstSeqListVo: null,
        menuType: null,
        moveSearchWrd: '',
        moveSipVersionTypeCd: null,
        moveType: '',
        nbitmask: '28',
        nclassCnt: '0.0625000000',
        ncnt: '16',
        nfirstAddr: '1925753600',
        nfreeIpCnt: '16',
        nipAllocMstCnt: '0',
        nipAllocMstSeq: null,
        nipAssignMstSeq: '1113348',
        nipBlockMstSeq: '4687',
        nipLinkMstSeq: null,
        nipmsSvcSeq: '0',
        nlastAddr: '1925753615',
        nlvlMstSeq: '33',
        bitCount: 0,
        bitLength: 0,
        firstNonzeroIntNum: 0,
        lowestSetBit: 0,
        mag: '',
        signum: 1,
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
        pipPrefix: '114.200.171.0/28',
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
        sassignLevelCd: 'IA0004',
        sassignLevelNm: '서비스배정[미할당]',
        sassignTypeCd: 'SA0002',
        sassignTypeCdMultiStr: null,
        sassignTypeCds: null,
        sassignTypeMulti: null,
        sassignTypeNm: '홈고객(유동)',
        scomment: null,
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
        sfirstAddr: '114.200.171.0',
        sfirstAddrBinary: '01110010110010001010101100000000',
        sfirstAddrGwip: '114.200.171.1',
        sfirstIpPreferred: '114.200.171.000',
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
        sipBlock: '114.200.171.0',
        sipCreateSeqCd: 'K202407001',
        sipCreateSeqNm: null,
        sipCreateTypeCd: 'CT0001',
        sipCreateTypeNm: '공인',
        sipmsSvcNm: null,
        sipVersionTypeCd: 'CV0001',
        sipVersionTypeNm: 'IPv4',
        slacpsid: null,
        slastAddr: '114.200.171.15',
        slastAddrBinary: '01110010110010001010101100001111',
        slastAddrGwip: '114.200.171.14',
        slastIpPreferred: '114.200.171.015',
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
        sneossDdYn: 'Y',
        sneSrchTypeCd: null,
        snetmask: '255.255.255.240',
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
        ssubscnealias: null,
        ssubscnealiasNe: null,
        ssubscnealiasType: null,
        ssubscnescode: null,
        ssubscnnescode: null,
        ssubscrouterserialip: null,
        ssvcGroupCd: '383040',
        ssvcGroupCdMulti: null,
        ssvcGroupCdMultiStr: null,
        ssvcGroupNm: '강남/서부NW운용본부',
        ssvcLgroupNm: null,
        ssvcLineTypeCd: 'CL0001',
        ssvcLineTypeNm: 'KORNET',
        ssvcObjCd: 'R00515',
        ssvcObjNm: '인천국사',
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
      // {
      //   'ssvc_line_type_cd': 'CL0003',
      //   'nip_alloc_mst_seq': 865865,
      //   'nip_alloc_neoss_seq': null,
      //   'sordernum': null,
      //   'sregyn': 'Y',
      //   'sodr_close_type_cd': null,
      //   'ssaid': '36032',
      //   'sllnum': '',
      //   'sicisofficescode': 'VV0015',
      //   'slacpsid': null,
      //   'ssubscnescode': null,
      //   'ssubscmstip': '110.70.230.1',
      //   'ssubsclgipportseq': null,
      //   'ssubsclgipportdescription': null,
      //   'ssubsclgipportip': null,
      //   'ssubscrouterserialip': null,
      //   'ssubscnealias': 'GR_C-PGW#20',
      //   'sconnalias': null,
      //   'smodelname': 'CISCO',
      //   'scustname': null,
      //   'sip_version_type_cd': 'CV0001',
      //   'sip_create_type_cd': 'CT0001',
      //   'nip_assign_mst_seq': 980372,
      //   'pip_prefix': '1.100.0.0\/17',
      //   'sip_block': '1.100.0.0',
      //   'nbitmask': 17,
      //   'sfirst_addr': '1.100.0.0',
      //   'slast_addr': '1.100.127.255',
      //   'nipms_svc_seq': 0,
      //   'sassign_type_cd': 'SA1013',
      //   'sex_svc_cd': null,
      //   'ssvc_use_type_cd': 'SU0000',
      //   'sresult_cd': null,
      //   'sresult_msg': null,
      //   'sgatewayip': '1.100.127.254',
      //   'sinstalladdress': null,
      //   'sinstallroadaddress': null,
      //   'sex_push_yn': 'O',
      //   'nticket_act_seq': 0,
      //   'scomment': '',
      //   'dcreate_dt': '2022-07-12T00:41:56.000Z',
      //   'screate_id': '10149118',
      //   'dmodify_dt': '2022-07-12T00:41:56.000Z',
      //   'smodify_id': '10149118',
      //   'sreport_opinion': null,
      //   'sodrhopedt': null,
      //   'sodrregdt': null,
      //   'sofficecode': null,
      //   'ssvcUseTypeNm': 'N\/A',
      //   'sofficename': null,
      //   'sipmsSvcNm': '미분류'
      // }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.onLoadCircuitDetail(model.ipBlockMstVo)
    },
    onClose() {
      this.$emit('closeCircuitDetail', { returnFlag: 'allocTel' })
    },
    async onLoadCircuitDetail(ipBlockMstVo = null) {
      if (ipBlockMstVo === null) return
      /*
      레거시 url: ipmgmt/allocmgmt/viewDetailSubSvcMst.ajax
      회선정보조회
        case 1. 할당상세pop
          ipBlockMstVo: { nipAllocMstSeq , nipLinkMstSeq }
        case 2. 할당처리pop > 회선 정보
          ipBlockMstVo: { sllnum, ssaid, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd }
      */
      // try {
      //   const res = await api(ipBlockMstVo)
      //   this.resultVo = res.resultVo
      // } catch (error) {
      //   this.error(error)
      // }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
