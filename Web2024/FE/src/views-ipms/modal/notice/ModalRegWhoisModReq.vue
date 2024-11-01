<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="Whois 정보 변경 신청 등록"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="false"
    :append-to-body="true"
    :modal="modal"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <div class="popupContentTable">
      <table>
        <colgroup>
          <col width="15%" />
          <col width="85%" />
        </colgroup>
        <tbody>
          <tr>
            <th>IP주소</th>
            <td><el-input v-model="txtSearchIp" size="small" type="text" maxlength="200" /></td>
            <td><el-button type="primary" size="small" round @click="fnviewRegWhoisModReq()"> 조회</el-button></td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable" style="height: calc(100% - 100px)">
      <div class="popupContentTableTitle">조회결과</div>
      <el-col ref="tableContainer" :span="24">
        <compTable
          ref="compTable"
          :prop-name="name"
          :prop-table-height="'100%'"
          :prop-data="pagination.data"
          :prop-pagination-data.sync="pagination"
          :prop-column="tableColumns"
          :prop-is-pagination="true"
          prop-grid-menu-id="inputSpeed"
          :prop-grid-indx="1"
          :text-des="false"
          :prop-on-page-change="handleChangeCurPage"
          :prop-on-page-size-change="handleChangeCurPage"
        ></compTable>
      </el-col>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">KISA WHOIS - IP 주소 사용기관 정보 (변경 전)</div>
      <table>
        <colgroup>
          <col width="20%" /><col width="80%" />
        </colgroup>
        <tbody>
          <tr>
            <th>한글기관명</th>
            <td>{{ sBefOrgName }}</td>
          </tr>
          <tr>
            <th>한글주소</th>
            <td>{{ sBefOrgAddr }}</td>
          </tr>
          <tr>
            <th>우편번호</th>
            <td>{{ sBefZipCode }}</td>
          </tr>
          <tr>
            <th>영문기관명</th>
            <td>{{ sBefEOrgName }}</td>
          </tr>
          <tr>
            <th>영문주소</th>
            <td>{{ sBefEOrgAddr }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click="fnSetAddr('toKT')">KT 정보대체</el-button>
      <el-button type="primary" size="small" icon="el-icon-refresh-right" round @click="fnSetAddr('reset')">초기화</el-button>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">KISA WHOIS - IP 주소 사용기관 정보 (변경 후)</div>
      <table>
        <colgroup>
          <col width="20%" /><col width="80%" />
        </colgroup>
        <tbody>
          <tr>
            <th>한글기관명</th>
            <td>{{ sAftOrgName }}</td>
          </tr>
          <tr>
            <th>한글주소</th>
            <td class="flex">
              {{ sAftOrgAddr }}
              <el-button type="primary" size="small" round @click="fnViewSeachAddrPop('viewRegWhoisModReq')">주소검색</el-button>
            </td>
          </tr>
          <tr>
            <th>한글상세주소</th>
            <td>{{ sAftOrgAddrDetail }}</td>
          </tr>
          <tr>
            <th>우편번호</th>
            <td>{{ sAftZipCode }}</td>
          </tr>
          <tr>
            <th>영문기관명</th>
            <td>{{ sAftEOrgName }}</td>
          </tr>
          <tr>
            <th>영문주소</th>
            <td>{{ sAftEOrgAddr }}</td>
          </tr>
          <tr>
            <th>영문상세주소</th>
            <td>{{ sAftEOrgAddrDetail }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-edit" round @click="fnInsertRegWhoisModReqSubmit()">변경 신청</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
    <ModalSearchZipCode ref="ModalSearchZipCode" @setAddrForm="setAddrForm" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalSearchZipCode from '@/views-ipms/modal/notice/ModalSearchZipCode.vue'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalRegWhoisModReq'

export default {

  name: routeName,
  components: { CompTable, ModalSearchZipCode },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        {
          prop: '', label: 'Whois 조회', align: 'center', width: '200', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              type: 'info',
              on: { click: () => {
                this.fnSearchWhois(row.sfirstAddr, row.slastAddr, row.swhoisrequestid, row.pip_prefix, row.nwhoisseq)
            } } }, '조회')
          }
         },
        { prop: 'sfirstAddr', label: '시작 IP', align: 'center', width: '400', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝 IP', align: 'center', width: '400', sortable: true, columnVisible: true, showOverflow: true },
      ],
      sreject_rsn: '',
      viewType: '',
      txtSearchIp: '',
      resultVo: null,
      ktInfoVo: {
        sorgname: '',
        sadmAddr: '',
        sadmAddrDetail: '',
        sadmZipcode: '',
        sadmEorgname: '',
        sadmEaddr: '',
        sadmEaddrDetail: '',
      },
      sBefOrgName: '',
      sBefOrgAddr: '',
      sBefZipCode: '',
      sBefEOrgName: '',
      sBefEOrgAddr: '',
      resultListVo: [],
      sAftOrgName: '',
      sAftOrgAddr: '',
      sAftOrgAddrDetail: '',
      sAftZipCode: '',
      sAftEOrgName: '',
      sAftEOrgAddr: '',
      sAftEOrgAddrDetail: '',
      searchVoItem: {},
      resultSearchWhois: null
    }
  },
  computed: {
    onChangetitle() {
      return this.viewType === 'detail' ? '신청 상세' : '수정'
    }
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 900
    },
    onOpen(model, actionMode) {
      this.onInitValue()
      this.pagination.data = []
    },
    onInitValue() {
      this.txtSearchIp = ''
      this.sAftOrgAddr = ''
      this.sAftOrgAddrDetail = ''
      this.sAftZipCode = ''
      this.sBefOrgName = ''
      this.sAftEOrgAddr = ''
      this.sAftEOrgName = ''
      this.resultVo = null
    },
    setAddrForm(Addr) {
      this.sAftOrgAddr = Addr.newkaddr
      this.sAftZipCode = Addr.zipcode
      this.sAftEOrgAddr = Addr.eaddr
      this.sAftOrgAddrDetail = Addr.detailAddress
    },
    fnViewSeachAddrPop(type) { /* 주소검색 on Popup */
      if (this.resultSearchWhois === null) {
         onMessagePopup(this, 'Whois 정보 조회 후 수정 가능합니다.')
        return
      }
        this.$refs.ModalSearchZipCode.open({ type: type })
    },

    async fnviewRegWhoisModReq() { /* IP주소 조회 */
      if (this.txtSearchIp === '' || this.txtSearchIp === null) {
         onMessagePopup(this, '검색할 IP주소를 먼저 입력하세요.')
        return
      }

      const searchVo = {
        ssearchIp: this.txtSearchIp
      }
      const parameter = searchVo
      const target = ({ vue: this.$refs.content })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewRegWhoisModReq, parameter)
        if (res.result.totalCount === 0) {
          const mailObj = {
            mailType: 'Ipms-Table-Error',
            txtSearchIp: this.txtSearchIp,
          }
          this.fnSendMail(mailObj)
          return
        }
          this.pagination.data = res.result.data ?? []
          this.pagination.total = res.totalCount
          this.ktInfoVo = res.ktInfoVo /* ip 조회시 kt 정보 set */
        if (res.result.totalCount === 0) {
           onMessagePopup(this, '조회된 결과 목록이 존재하지 않습니다.')
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnSearchWhois()
    },
    async fnSearchWhois(sfirstAddr, slastAddr, swhoisrequestid, pip_prefix, nwhoisseq,) { /* IP주소 Whois 조회 */
      let res
      const searchVo = {
        ssearchIp: null,
        sfirstAddr: sfirstAddr,
        slastAddr: slastAddr,
        swhoisrequestid: swhoisrequestid,
        pip_prefix: pip_prefix,
        nwhoisseq: nwhoisseq,
      }

      const target = ({ vue: this.$refs.content })
      try {
        this.openLoading(target)

          this.searchVoItem = searchVo

        res = await apiRequestJson(ipmsJsonApis.selectSearchWhoisInfo, searchVo)

          this.pagination.data = res.tbWhoisModifyVo ?? []
          this.pagination.total = res.totalCount
          this.resultSearchWhois = res.tbWhoisModifyVo /* 정보조회된 결과  */
          if (res.tbWhoisModifyVo.commonMsg !== 'SUCCESS') {
             onMessagePopup(this, `${res.tbWhoisModifyVo.commonMsg}`)
            return
          }
          if (!res.tbWhoisModifyVo.infoObj.rtnMsg) {
            const {
              sBefOrgName, sBefOrgAddr, sBefZipCode, sBefEOrgName, sBefEOrgAddr,
              sAftOrgName, sAftOrgAddr, sAftOrgAddrDetail, sAftZipCode, sAftEOrgName,
              sAftEOrgAddr, sAftEOrgAddrDetail
            } = res.tbWhoisModifyVo.infoObj

            this.sBefOrgName = sBefOrgName
            this.sBefOrgAddr = sBefOrgAddr
            this.sBefZipCode = sBefZipCode
            this.sBefEOrgName = sBefEOrgName
            this.sBefEOrgAddr = sBefEOrgAddr

            this.sAftOrgName = sAftOrgName
            this.sAftOrgAddr = sAftOrgAddr
            this.sAftOrgAddrDetail = sAftOrgAddrDetail
            this.sAftZipCode = sAftZipCode
            this.sAftEOrgName = sAftEOrgName
            this.sAftEOrgAddr = sAftEOrgAddr
            this.sAftEOrgAddrDetail = sAftEOrgAddrDetail
          } else {
            const mailObj = {
              mailType: 'Kisa-Table-Error',
              searchFirstIp: sfirstAddr,
              searchLastIp: slastAddr,
              rtnMsg: res.tbWhoisModifyVo.infoObj.rtnMsg,
            }
            this.fnSendMail(mailObj)
          }
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    fnSendMail(mailObj) { /* 관리자에게 메일 전송 */
      const onSendMailAdmin = '[관리자에게 메일 보내기]'
      const msgDB = `IP주소관리시스템 DB에서 해당 IP주소가 검색되지 않습니다.`
      const msgKISA = `KISA Whois에 등록된 IP주소가 아닙니다.`
      let msgType

      if (mailObj.mailType === 'Ipms-Table-Error') {
        msgType = msgDB
      } else if (mailObj.mailType === 'Kisa-Table-Error') {
        msgType = msgKISA
      }

      const msg =
      `${msgType}
      IP주소를 다시 확인하시고 조회해 보시기 바랍니다
      IP주소를 정확하게 입력했는데도 불구하고 조회되지 않는 경우
      아래 ${onSendMailAdmin} 버튼을 누르시면 관리자가 조회 불가 사유를 확인하여 연락드리도록 하겠습니다.`

      this.$confirm(msg, '알림', {
        confirmButtonText: '관리자에게 메일보내기',
        cancelButtonText: '닫기'
      }).then(async() => {
        if (!mailObj) {
          onMessagePopup(this, '메일 전송이 실패되었습니다.')
          return
        }
        try {
          let smtpVo
          if (mailObj.mailType === 'Ipms-Table-Error') {
            smtpVo = {
              mailType: 'Ipms-Table-Error',
              searchIp: mailObj.txtSearchIp
            }
          } else if (mailObj.mailType === 'Kisa-Table-Error') {
            smtpVo = {
              searchFirstIp: mailObj.searchFirstIp,
              searchLastIp: mailObj.searchLastIp,
              whoisRtnMsg: mailObj.rtnMsg,
            }
          }

          const res = await apiRequestJson(ipmsJsonApis.sendMail, smtpVo)
          if (res.smtpVo.commonMsg === 'SUCCESS') {
           onMessagePopup(this, `메일이 전송되었습니다.`)
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          console.log(error)
        }
      })
    },
    async fnInsertRegWhoisModReqSubmit() { /* 변경신청 */
    if (this.sBefOrgName === '' || this.sBefOrgName === null) {
       onMessagePopup(this, 'IP주소 정보가 조회되지 않았기 때문에 변경신청이 불가합니다.')
      return
    }

    if (this.sAftOrgName === '' || this.sAftOrgName === null) {
         onMessagePopup(this, '한글기관명을 입력하세요.')
        return
     }

     if (this.sAftOrgAddr === '' || this.sAftOrgAddr === null) {
         onMessagePopup(this, '한글주소를 입력하세요.')
        return
     }

     if (this.sAftZipCode === '' || this.sAftZipCode === null) {
         onMessagePopup(this, '우편번호를 입력하세요.')
        return
     }

     if (this.sAftEOrgName === '' || this.sAftEOrgName === null) {
         onMessagePopup(this, '영문기관명을 입력하세요.')
        return
     }

     if (this.sAftEOrgAddr === '' || this.sAftEOrgAddr === null) {
         onMessagePopup(this, '영문주소를 입력하세요.')
        return
     }

      const regExp = /[가-힣ㄱ-ㅎㅏ-ㅣ]/
      if (regExp.test(this.sAftEOrgName)) {
         onMessagePopup(this, '영문기관명은 한글입력이 불가합니다.')
        return
      }
      let res
      try {
         const tbWhoisModifyVo = {
          sfirstAddr: this.searchVoItem.sfirstAddr,
          slastAddr: this.searchVoItem.slastAddr,
          pip_prefix: this.searchVoItem.pip_prefix,
          swhoisrequestid: this.searchVoItem.swhoisrequestid,
          nwhoisseq: this.searchVoItem.nwhoisseq,

          sBefOrgName: this.txtSearchIp,
          sBefOrgAddr: this.txtSearchIp,
          sBefZipCode: this.txtSearchIp,
          sBefEOrgName: this.txtSearchIp,
          sBefEOrgAddr: this.txtSearchIp,

          sAftOrgName: this.sAftOrgName,
          sAftOrgAddr: this.sAftOrgAddr,
          sAftOrgAddrDetail: this.sAftOrgAddrDetail,
          sAftZipCode: this.sAftZipCode,
          sAftEOrgName: this.sAftEOrgName,
          sAftEOrgAddr: this.sAftEOrgAddr,
          sAftEOrgAddrDetail: this.sAftEOrgAddrDetail,
          sStatCd: '10'
        }

        if (this.searchVoItem.snettype === 'INFRA') {
           onMessagePopup(this, '네트워크 유형이 인프라인 경우엔 기관정보를 수정하실 수 없습니다.')
          return
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      }
    },

    fnSetAddr(type) {
      if (this.ktInfoVo === null || this.ktInfoVo === '') return

      if (type === 'reset') {
        this.sAftOrgName = ''
        this.sAftOrgAddr = ''
        this.sAftOrgAddrDetail = ''
        this.sAftZipCode = ''
        this.sAftEOrgName = ''
        this.sAftEOrgAddr = ''
        this.sAftEOrgAddrDetail = ''
      } else if (type === 'toKT') {
        this.sAftOrgName = this.ktInfoVo.sorgname
        this.sAftOrgAddr = this.ktInfoVo.sadmAddr
        this.sAftOrgAddrDetail = this.ktInfoVo.sadmAddrDetail
        this.sAftZipCode = this.ktInfoVo.sadmZipcode
        this.sAftEOrgName = this.ktInfoVo.sadmEorgname
        this.sAftEOrgAddr = this.ktInfoVo.sadmEaddr
        this.sAftEOrgAddrDetail = this.ktInfoVo.sadmEaddrDetail
      }
    },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>

</style>
