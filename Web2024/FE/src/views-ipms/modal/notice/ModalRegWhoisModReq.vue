<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
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
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        Whois 정보 변경 신청 등록
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th scope="row">IP주소</th>
                <td><el-input id="txtSearchIp" v-model="txtSearchIp" size="mini" type="text" class="txt w50" maxlength="200" /></td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="btn_area mt10 my-1">
          <el-button size="mini" @click="fnviewRegWhoisModReq()"> 조회</el-button>
        </div>

        <div class="content_result">
          <h4>조회결과</h4>
          <table id="contentTable" class="tbl_list mt5" summary="조회결과">
            <caption>조회결과</caption>
            <colgroup>
              <col width="20%" /><col width="40%" /><col width="40%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">Whois 조회</th>
                <th scope="col">시작IP</th>
                <th scope="col">끝IP</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="!resultVo">
                <tr v-if="!resultVo" class="subbg last">
                  <td v-if="!resultVo" class="first" colspan="3">조회된 결과 목록이 존재하지 않습니다.</td>
                </tr>
              </template>

              <template v-else>
                <tr v-for="(item, index) in resultVo.data" :key="item.swhoisrequestid" :class="{'last': index === resultVo.data.length - 1, 'subbg': (index % 2) !== 0}">
                  <td class="first">
                    <div class="btn_area" style="float: none;">
                      <el-button class="button_w" size="mini" @click="fnSearchWhois(item.sfirstAddr, item.slastAddr, item.swhoisrequestid, item.pip_prefix, item.nwhoisseq, item.snettype)"> 조회</el-button>
                    </div>
                  </td>
                  <td><a href="#none"><span class="spanFirstAddr">{{ item.sfirstAddr }}</span></a></td>
                  <td><a href="#none"><span class="spanLastAddr">{{ item.slastAddr }}</span></a></td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>KISA WHOIS - IP 주소 사용기관 정보 (변경 전)</h4>
          <table class="tbl_data" summary="변경전">
            <caption>변경전</caption>
            <colgroup>
              <col width="20%" /><col width="80%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">한글기관명</th>
                <td><span id="befOrgName">{{ sBefOrgName }}</span></td>
              </tr>
              <tr>
                <th class="first" scope="row">한글주소</th>
                <td><span id="befOrgAddr">{{ sBefOrgAddr }}</span></td>
              </tr>
              <tr>
                <th class="first" scope="row">우편번호</th>
                <td><span id="befOrgPost"> {{ sBefZipCode }}</span></td>
              </tr>
              <tr>
                <th class="first" scope="row">영문기관명</th>
                <td><span id="befEOrgName"> {{ sBefEOrgName }} </span></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">영문주소</th>
                <td><span id="befEOrgAddr">{{ sBefEOrgAddr }} </span></td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="my-1 float-right">
          <el-button size="mini" @click="fnSetAddr('toKT')">{{ $t('KT 정보대체') }}</el-button>
          <el-button size="mini" class="el-icon-refresh-right" @click="fnSetAddr('reset')">{{ $t('초기화') }}</el-button>
        </div>

        <div class="content_result mt5" style="padding-top: 7px;">
          <h4>KISA WHOIS - IP 주소 사용기관 정보 (변경 후)</h4>
          <table class="tbl_data" summary="변경후">
            <caption>변경 후</caption>
            <colgroup>
              <col width="20%" /><col width="80%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">한글기관명</th>
                <td>{{ sAftOrgName }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">한글주소</th>
                <td>{{ sAftOrgAddr }} <span>  <el-button size="mini" @click="fnViewSeachAddrPop('viewRegWhoisModReq')"> 주소검색 </el-button> </span></td>
              </tr>
              <tr>
                <th class="first" scope="row">한글상세주소</th>
                <td>{{ sAftOrgAddrDetail }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">우편번호</th>
                <td>{{ sAftZipCode }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문기관명</th>
                <td>{{ sAftEOrgName }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">영문주소</th>
                <td>{{ sAftEOrgAddr }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">영문상세주소</th>
                <td>{{ sAftEOrgAddrDetail }}</td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-edit" @click="fnInsertRegWhoisModReqSubmit()">{{ $t('변경 신청') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
      <ModalSearchZipCode ref="ModalSearchZipCode" @setAddrForm="setAddrForm" />

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import ModalSearchZipCode from '@/views-ipms/modal/notice/ModalSearchZipCode.vue'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

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
      sreject_rsn: '',
      viewType: '',
      txtSearchIp: '',
      resultVo: null,
      ktInfoVo: {
        sorgname: '(주) 케이티',
        sadmAddr: '경기도 성남시 분당구 불정로 90',
        sadmAddrDetail: '',
        sadmZipcode: '13606',
        sadmEorgname: 'Korea Telecom',
        sadmEaddr: '90 Buljeongro Bundang-Gu Seongnam-Si Gyeonggi-Do',
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
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.onInitValue()
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
      console.log(Addr)
      this.sAftOrgAddr = Addr.newkaddr
      this.sAftZipCode = Addr.zipcode
      this.sAftEOrgAddr = Addr.eaddr
      this.sAftOrgAddrDetail = Addr.detailAddress
    },
   fnViewSeachAddrPop(type) { /* 주소검색 on Popup */
      if (this.resultSearchWhois === null) {
        this.$message('Whois 정보 조회 후 수정 가능합니다.')
        return
      }
        this.$refs.ModalSearchZipCode.open({ type: type })
    },
    async fnviewRegWhoisModReq() { /* IP주소 조회 */
      if (this.txtSearchIp === '' || this.txtSearchIp === null) {
        this.$message('검색할 IP주소를 먼저 입력하세요.')
        return
      }
       try {
        const searchVo = {
          ssearchIp: this.txtSearchIp
        }
        const res = await apiRequestModel(ipmsModelApis.viewRegWhoisModReq, searchVo)
        if (res.result.totalCount === 0) {
          const mailObj = {
            mailType: 'Ipms-Table-Error',
            txtSearchIp: this.txtSearchIp,
          }
          this.fnSendMail(mailObj)
          return
        }
        this.resultVo = res.result
        if (res.result.totalCount === 0) {
          this.$message('조회된 결과 목록이 존재하지 않습니다.')
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnSearchWhois(sfirstAddr, slastAddr, swhoisrequestid, pip_prefix, nwhoisseq, snettype) { /* IP주소 Whois 조회 */
      try {
        const searchVo = {
          ssearchIp: null,
          sfirstAddr: sfirstAddr,
          slastAddr: slastAddr,
          swhoisrequestid: swhoisrequestid,
          pip_prefix: pip_prefix,
          nwhoisseq: nwhoisseq,
          snettype: snettype,
        }

        this.searchVoItem = searchVo

        const res = await apiRequestJson(ipmsJsonApis.selectSearchWhoisInfo, searchVo)
        this.resultSearchWhois = res.tbWhoisModifyVo
        if (res.tbWhoisModifyVo.commonMsg === 'SUCCESS') {
          if (!res.tbWhoisModifyVo.infoObj.rtnMsg) {
            const {
              sBefOrgName, sBefOrgAddr, sBefZipCode, sBefEOrgName, sBefEOrgAddr,
              sAftOrgName, sAftOrgAddr, sAftOrgAddrDetail, sAftZipCode, sAftEOrgName,
              sAftEOrgAddr, sAftEOrgAddrDetail
            } = res.tbWhoisModifyVo

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
        } else {
          this.$message('조회에 실패했습니다.')
          return false
        }
      } catch (error) {
        console.error(error)
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
          this.$message.error(`메일 전송이 실패되었습니다.`)
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
          if (res.smtpVo.commonMsg === 'SUCCESS') {
            this.$message.success(`메일이 전송되었습니다.`)
          }
        } catch (error) {
          console.log(error)
        }
      })
    },
    async fnInsertRegWhoisModReqSubmit() { /* 변경신청 */
    if (this.sBefOrgName === '' || this.sBefOrgName === null) {
      this.$message('IP주소 정보가 조회되지 않았기 때문에 변경신청이 불가합니다.')
      return
    }

    if (this.sAftOrgName === '' || this.sAftOrgName === null) {
        this.$message('한글기관명을 입력하세요.')
        return
     }

     if (this.sAftOrgAddr === '' || this.sAftOrgAddr === null) {
        this.$message('한글주소를 입력하세요.')
        return
     }

     if (this.sAftZipCode === '' || this.sAftZipCode === null) {
        this.$message('우편번호를 입력하세요.')
        return
     }

     if (this.sAftEOrgName === '' || this.sAftEOrgName === null) {
        this.$message('영문기관명을 입력하세요.')
        return
     }

     if (this.sAftEOrgAddr === '' || this.sAftEOrgAddr === null) {
        this.$message('영문주소를 입력하세요.')
        return
     }

      const regExp = /[가-힣ㄱ-ㅎㅏ-ㅣ]/
      if (regExp.test(this.sAftEOrgName)) {
        this.$message('영문기관명은 한글입력이 불가합니다.')
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
          this.$message('네트워크 유형이 인프라인 경우엔 기관정보를 수정하실 수 없습니다.')
          return
        }

        //  res = await apiRequestModel(ipmsModelApis.insertRegWhoisModReq, tbWhoisModifyVo)
        // if (res.commonMsg === 'SUCCESS') {
        //  this.$message.success({ message: `${res.commonMsg}` })
        // this.$emit('reload')
        // this.close()
        // }
      } catch (error) {
        this.$message.error({ message: `${res.commonMsg}` })
        console.error(error)
      }
    },

    fnSetAddr(type) {
      if (this.resultVo === '' || this.resultVo === null) {
        return
      }
      if (type === 'reset') {
        this.sAftOrgName = this.resultVo.sAftOrgName
        this.sAftOrgAddr = this.resultVo.sAftOrgAddr
        this.sAftOrgAddrDetail = this.resultVo.sAftOrgAddrDetail
        this.sAftZipCode = this.resultVo.sAftZipCode
        this.sAftEOrgName = this.resultVo.sAftEOrgName
        this.sAftEOrgAddr = this.resultVo.sAftEOrgAddr
        this.sAftEOrgAddrDetail = this.resultVo.sAftEOrgAddrDetail
      } else if (type === 'toKT') {
        this.sAftOrgName = this.ktInfoVo.sorgname
        this.sAftOrgAddr = this.ktInfoVo.sadmAddr
        this.sAftOrgAddrDetail = this.ktInfoVo.sadmAddrDetail
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
.ModalRegWhoisModReq{
  .el-input {
    width: 100%;
  }
}

</style>
