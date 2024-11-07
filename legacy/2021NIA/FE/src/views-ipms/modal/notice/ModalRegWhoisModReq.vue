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
    <div v-loading="viewLoading">
      <div class="popupContentTable textcenter">
        <table>
          <colgroup>
            <col width="15%" />
            <col width="85%" />
          </colgroup>
          <tbody>
            <tr>
              <th>IP주소</th>
              <td><el-input v-model="txtSearchIp" size="small" type="text" maxlength="200" clearable @keyup.enter.native="fnviewRegWhoisModReq()" /></td>
              <td><el-button type="primary" size="small" round @click="fnviewRegWhoisModReq()"> 조회</el-button></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="popupContentTable textcenter">
        <div class="popupContentTableTitle">조회결과</div>
        <table v-loading="tableLoading">
          <colgroup>
            <col width="20%" /><col width="40%" /><col width="40%" />
          </colgroup>
          <thead>
            <tr>
              <th>Whois 조회</th>
              <th>시작IP</th>
              <th>끝IP</th>
            </tr>
          </thead>
          <tbody>
            <template v-if="pagination.data.length === 0">
              <tr>
                <td colspan="3" class="textcenter">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
            </template>
            <template v-else>
              <tr v-for="(row, index) in pagination.data" :key="index">
                <td>
                  <el-button size="mini" type="primary" round plain @click="fnSearchWhois(row)"> 조회</el-button>
                </td>
                <td><a href="#none">{{ row.sfirstAddr }}</a></td>
                <td><a href="#none">{{ row.slastAddr }}</a></td>
              </tr>
            </template>
          </tbody>
        </table>
        <div v-if="pagination.data.length > 0" class="tableListWrap">
          <div class="tableListPaging" style="justify-content: center;">
            <el-pagination
              :current-page.sync="pagination.currentPage"
              :total="pagination.total"
              :page-size="pagination.pageSize"
              layout="prev, pager, next"
              @current-change="handleChangeCurPage"
              @size-change="handleChangeCurPage"
            />
          </div>
        </div>
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
              <td>{{ beforeInfo.koOrgName }}</td>
            </tr>
            <tr>
              <th>한글주소</th>
              <td>{{ beforeInfo.koOrgAddr }}</td>
            </tr>
            <tr>
              <th>우편번호</th>
              <td>{{ beforeInfo.orgPost }}</td>
            </tr>
            <tr>
              <th>영문기관명</th>
              <td>{{ beforeInfo.enOrgName }}</td>
            </tr>
            <tr>
              <th>영문주소</th>
              <td>{{ beforeInfo.enOrgAddr }}</td>
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
              <!-- <td>{{ afterInfo.sAftOrgName }}</td> -->
              <td>
                <el-input v-model="afterInfo.sAftOrgName" size="mini" type="text" maxlength="200" />
              </td>
            </tr>
            <tr>
              <th>한글주소</th>
              <td class="textflex">
                <el-input v-model="afterInfo.sAftOrgAddr" size="mini" disabled />
                <el-button type="primary" size="small" round plain @click="fnViewSeachAddrPop('viewRegWhoisModReq')">주소검색</el-button>
              </td>
            </tr>
            <tr>
              <th>한글상세주소</th>
              <td><el-input v-model="afterInfo.sAftOrgAddrDetail" size="mini" disabled /></td>
            </tr>
            <tr>
              <th>우편번호</th>
              <td><el-input v-model="afterInfo.sAftZipCode" size="mini" disabled /></td>
            </tr>
            <tr>
              <th>영문기관명</th>
              <td>
                <el-input v-model="afterInfo.sAftEOrgName" size="mini" type="text" maxlength="200" />
              </td>
            </tr>
            <tr>
              <th>영문주소</th>
              <td><el-input v-model="afterInfo.sAftEOrgAddr" size="mini" disabled /></td>
            </tr>
            <tr>
              <th>영문상세주소</th>
              <td><el-input v-model="afterInfo.sAftEOrgAddrDetail" size="mini" disabled /></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="popupContentTableBottom">
        <el-button type="primary" size="small" icon="el-icon-edit" round @click="fnInsertRegWhoisModReqSubmit()">변경 신청</el-button>
        <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
      </div>
      <ModalSearchZipCode ref="ModalSearchZipCode" @setAddrForm="setAddrForm" />
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import ModalSearchZipCode from '@/views-ipms/modal/notice/ModalSearchZipCode.vue'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalRegWhoisModReq'

export default {

  name: routeName,
  components: { ModalSearchZipCode },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewLoading: false,
      tableLoading: false,
      pagination: this.setDefaultPagination(),
      sreject_rsn: '',
      viewType: '',
      txtSearchIp: '',
      resultVo: null,
      ktInfoVo: {},
      beforeInfo: {
        koOrgName: '', // sBefOrgName
        koOrgAddr: '', // sBefOrgAddr
        orgPost: '', // sBefZipCode
        enOrgName: '', // sBefEOrgName
        enOrgAddr: '', // sBefEOrgAddr
        koOrgAddrDtl: '',
        enOrgAddrDtl: ''
      },
      afterInfo: {
        sAftOrgName: '',
        sAftOrgAddr: '',
        sAftOrgAddrDetail: '',
        sAftZipCode: '',
        sAftEOrgName: '',
        sAftEOrgAddr: '',
        sAftEOrgAddrDetail: '',
      },
      searchVoItem: {},
      resultSearchWhois: null
    }
  },
  computed: {
    onChangetitle() {
      return this.viewType === 'detail' ? '신청 상세' : '수정'
    },
    getPageCount() {
      const { total, pageSize } = this.pagination
      if (total <= pageSize) {
        return 1
      } else {
        return (total % pageSize) > 0 ? parseInt(total / pageSize) + 1 : parseInt(total / pageSize)
      }
    }
  },
  mounted() {
    this.pagination.pageSize = 5
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 900
    },
    onOpen(model, actionMode) {
      this.onInitValue()
    },
    onInitValue() {
      this.txtSearchIp = ''
      this.sBefOrgName = ''

      this.afterInfo = {
        sAftOrgName: '',
        sAftOrgAddr: '',
        sAftOrgAddrDetail: '',
        sAftZipCode: '',
        sAftEOrgName: '',
        sAftEOrgAddr: '',
        sAftEOrgAddrDetail: '',
      }
    },
    setAddrForm(Addr) {
      const addrObj = {
        sAftOrgAddr: Addr.newkaddr,
        sAftZipCode: Addr.zipcode,
        sAftEOrgAddr: Addr.eaddr,
        sAftEOrgName: '',
        sAftOrgAddrDetail: Addr.detailAddress
      }
      Object.assign(this.afterInfo, addrObj)
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
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const params = { ssearchIp: this.txtSearchIp, pageUnit, pageIndex }
       try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewRegWhoisModReq, params)
        this._merge(this.ktInfoVo, res.ktInfoVo)
        if (res.resultListVoTotalCount === 0) {
          const mailObj = {
            mailType: 'Ipms-Table-Error',
            txtSearchIp: this.txtSearchIp,
          }
          this.fnSendMail(mailObj)
          return
        }
        this.pagination.data = res.resultListVo
        this.pagination.total = res.resultListVoTotalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnviewRegWhoisModReq()
    },
    async fnSearchWhois(row) { /* IP주소 Whois 조회 */
      const { sfirstAddr, slastAddr, swhoisrequestid, pip_prefix, nwhoisseq } = row
      this._merge(this.searchVoItem, row)
      const params = { ssearchIp: null, sfirstAddr, slastAddr, swhoisrequestid, pip_prefix, nwhoisseq }
      try {
        this.viewLoading = true
        const res = await apiRequestJson(ipmsJsonApis.selectSearchWhoisInfo, params)
        this.resultSearchWhois = res
        this.beforeInfo = res.infoObj
        if (res.commonMsg === 'SUCCESS') {
          if (!res.infoObj.rtnMsg) {
            this._merge(this.beforeInfo, res.infoObj)
            this._merge(this.afterInfo, res)
          } else {
            const mailObj = {
              mailType: 'Kisa-Table-Error',
              searchFirstIp: sfirstAddr,
              searchLastIp: slastAddr,
              rtnMsg: res.infoObj.rtnMsg,
            }
            this.fnSendMail(mailObj)
          }
        } else {
          onMessagePopup(this, res.commonMsg)
          return false
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.viewLoading = false
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
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '메일이 전송되었습니다.')
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          console.log(error)
        }
      })
    },
    async fnInsertRegWhoisModReqSubmit() { /* 변경신청 */
      if (this.beforeInfo.koOrgName === '' || this.beforeInfo.koOrgName === null) {
        onMessagePopup(this, 'IP주소 정보가 조회되지 않았기 때문에 변경신청이 불가합니다.')
        return
      }

      if (this.afterInfo.sAftOrgName === '' || this.afterInfo.sAftOrgName === null) {
        onMessagePopup(this, '한글기관명을 입력하세요.')
        return
      }

      if (this.afterInfo.sAftOrgAddr === '' || this.afterInfo.sAftOrgAddr === null) {
        onMessagePopup(this, '한글주소를 입력하세요.')
        return
      }

      if (this.afterInfo.sAftZipCode === '' || this.afterInfo.sAftZipCode === null) {
        onMessagePopup(this, '우편번호를 입력하세요.')
        return
      }

      if (this.afterInfo.sAftEOrgName === '' || this.afterInfo.sAftEOrgName === null) {
        onMessagePopup(this, '영문기관명을 입력하세요.')
        return
      }

      if (this.afterInfo.sAftEOrgAddr === '' || this.afterInfo.sAftEOrgAddr === null) {
        onMessagePopup(this, '영문주소를 입력하세요.')
        return
      }

      if ((/[가-힣ㄱ-ㅎㅏ-ㅣ]/g).test(this.afterInfo.sAftEOrgName)) {
        onMessagePopup(this, '영문기관명은 한글입력이 불가합니다.')
        return
      }
      const { sfirstAddr, slastAddr, pip_prefix, swhoisrequestid, nwhoisseq } = this.searchVoItem
      const tbWhoisModifyVo = { sfirstAddr, slastAddr, pip_prefix, swhoisrequestid, nwhoisseq, sStatCd: '10' }
      const { koOrgName, koOrgAddr, orgPost, enOrgName, enOrgAddr } = this.beforeInfo
      const beforeInfo = {
        sBefOrgName: koOrgName,
        sBefOrgAddr: koOrgAddr,
        sBefZipCode: orgPost,
        sBefEOrgName: enOrgName,
        sBefEOrgAddr: enOrgAddr,
      }
      const { sAftOrgName, sAftOrgAddr, sAftOrgAddrDetail, sAftZipCode, sAftEOrgName, sAftEOrgAddr, sAftEOrgAddrDetail } = this.afterInfo
      const afterInfo = { sAftOrgName, sAftOrgAddr, sAftOrgAddrDetail, sAftZipCode, sAftEOrgName, sAftEOrgAddr, sAftEOrgAddrDetail }
      Object.assign(tbWhoisModifyVo, beforeInfo, afterInfo)
      try {
        if (this.searchVoItem.snettype === 'INFRA') {
          onMessagePopup(this, '네트워크 유형이 인프라인 경우엔 기관정보를 수정하실 수 없습니다.')
          return
        }
        const res = await apiRequestModel(ipmsModelApis.insertRegWhoisModReq, tbWhoisModifyVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '성공적으로 신청 되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnSetAddr(type) {
      if (this.pagination.data.length === 0 || this.pagination.data === null) {
        return
      }
      if (type === 'reset') {
        this.afterInfo.sAftOrgName = this.beforeInfo.koOrgName
        this.afterInfo.sAftOrgAddr = this.beforeInfo.koOrgAddr
        this.afterInfo.sAftOrgAddrDetail = this.beforeInfo.koOrgAddrDtl
        this.afterInfo.sAftZipCode = this.beforeInfo.orgPost
        this.afterInfo.sAftEOrgName = this.beforeInfo.enOrgName
        this.afterInfo.sAftEOrgAddr = this.beforeInfo.enOrgAddr
        this.afterInfo.sAftEOrgAddrDetail = this.beforeInfo.enOrgAddrDtl
      } else if (type === 'toKT') {
        this.afterInfo.sAftOrgName = this.ktInfoVo.sorgname
        this.afterInfo.sAftOrgAddr = this.ktInfoVo.sadmAddr
        this.afterInfo.sAftOrgAddrDetail = this.ktInfoVo.sadmAddrDetail
        this.afterInfo.sAftZipCode = this.ktInfoVo.sadmZipcode
        this.afterInfo.sAftEOrgName = this.ktInfoVo.sadmEorgname
        this.afterInfo.sAftEOrgAddr = this.ktInfoVo.sadmEaddr
        this.afterInfo.sAftEOrgAddrDetail = this.ktInfoVo.sadmEaddrDetail
      }
    },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
</style>
