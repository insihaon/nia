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
        Whois 정보 변경 {{ onChangetitle }}
        <hr>
      </span>

      <div id="content" class="layer">
        <template v-if="viewType === 'detail'">
          <div class="content_result mt5">
            <h4>IP 정보</h4>
            <table class="tbl_data" summary="IP정보">
              <caption>IP정보</caption>
              <colgroup>
                <col width="20%" /><col width="30%" /><col width="20%" /><col width="30%" />
              </colgroup>
              <tbody>
                <tr class="top last">
                  <th class="first" scope="row">시작 IP</th>
                  <td><span id="befOrgName">{{ resultVo.sfirstAddr }}</span></td>
                  <th class="last" scope="row">끝 IP</th>
                  <td><span id="befOrgName">{{ resultVo.slastAddr }}</span></td>
                </tr>
              </tbody>
            </table>
          </div>
        </template>

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
                <td><span id="befOrgName">{{ resultVo.sBefOrgName }}</span></td>
              </tr>
              <tr>
                <th class="first" scope="row">한글주소</th>
                <td><span id="befOrgAddr">{{ resultVo.sBefOrgAddr }}</span></td>
              </tr>
              <tr>
                <th class="first" scope="row">우편번호</th>
                <td><span id="befOrgPost"> {{ resultVo.sBefZipCode }}</span></td>
              </tr>
              <tr>
                <th class="first" scope="row">영문기관명</th>
                <td><span id="befEOrgName"> {{ resultVo.sBefEOrgName }} </span></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">영문주소</th>
                <td><span id="befEOrgAddr">{{ resultVo.sBefEOrgAddr }} </span></td>
              </tr>
            </tbody>
          </table>
        </div>

        <template v-if="viewType === 'edit'">
          <div class="my-1 float-right">
            <el-button size="mini" @click="fnSetAddr('toKT')">{{ $t('KT 정보대체') }}</el-button>
            <el-button size="mini" class="el-icon-refresh-right" @click="fnSetAddr('reset')">{{ $t('초기화') }}</el-button>
          </div>
        </template>

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
                <td>{{ sAftOrgAddr }}</td>
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

        <!-- <template v-if="userGrade 관리자"> -->
        <div id="divRejectRsn" class="content_result mt5" style="padding-top: 7px;">
          <h4>반려사유</h4>
          <table class="tbl_data" summary="반려사유">
            <tbody>
              <tr class="top last">
                <td>
                  <textarea
                    id="txtRejectRsn"
                    v-model="sreject_rsn"
                    style="height: 100px; width:99%; resize: none;"
                    maxlength="5000"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- </template> -->

      </div>

      <div slot="footer" class="dialog-footer">
        <!-- 관리자 : 신청 -> 변경신청취소, 수정, 승인/반려  | 사용자 - 신청 -> 변경신청취소, 수정 -->
        <el-button size="mini" class="float-left" @click="fnApprRegWhoisModReqSubmit('A')">{{ $t('승인') }}</el-button>
        <el-button size="mini" class="float-left" @click="fnApprRegWhoisModReqSubmit('R')">{{ $t('반려') }}</el-button>
        <el-button size="mini" @click="fnDeleteRegWhoisModReqSubmit()">{{ $t('변경신청취소') }}</el-button>
        <template v-if="viewType === 'detail'">
          <el-button size="mini" class="el-icon-edit" @click="onChangeMode()">{{ $t('수정') }}</el-button>
        </template>
        <template v-if="viewType === 'edit'">
          <el-button size="mini" class="el-icon-edit-outline" style="background-color:#2e3574; color : #fff" @click="fnUpdateRegWhoisModReqSubmit()">{{ $t('저장') }}</el-button>
        </template>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalDetailWhoisMod'

export default {

  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {},
      sreject_rsn: '',
      viewType: '',
      ktInfoVo: {
        sorgname: '(주) 케이티',
        sadmAddr: '경기도 성남시 분당구 불정로 90',
        sadmAddrDetail: '',
        sadmZipcode: '13606',
        sadmEorgname: 'Korea Telecom',
        sadmEaddr: '90 Buljeongro Bundang-Gu Seongnam-Si Gyeonggi-Do',
        sadmEaddrDetail: '',
      },
      sAftOrgName: '',
      sAftOrgAddr: '',
      sAftOrgAddrDetail: '',
      sAftZipCode: '',
      sAftEOrgName: '',
      sAftEOrgAddr: '',
      sAftEOrgAddrDetail: '',
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
      this.viewType = model.type
      this.resultVo = model.row
      const { sreject_rsn, sAftOrgName, sAftOrgAddr, sAftOrgAddrDetail, sAftZipCode, sAftEOrgName, sAftEOrgAddr, sAftEOrgAddrDetail } = this.resultVo
      this.sreject_rsn = sreject_rsn
      this.sAftOrgName = sAftOrgName
      this.sAftOrgAddr = sAftOrgAddr
      this.sAftOrgAddrDetail = sAftOrgAddrDetail
      this.sAftZipCode = sAftZipCode
      this.sAftEOrgName = sAftEOrgName
      this.sAftEOrgAddr = sAftEOrgAddr
      this.sAftEOrgAddrDetail = sAftEOrgAddrDetail
    },
    onChangeMode() {
      this.viewType = 'edit'
    },
    fnSetAddr(type) {
      if (type === 'reset') {
        this.sAftOrgName = this.resultVo.sAftOrgName
        this.sAftOrgAddr = this.resultVo.sAftOrgAddr
        this.sAftOrgAddrDetail = this.resultVo.sAftOrgAddrDetail
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
    fnApprRegWhoisModReqSubmit(type) { /* 승인 , 반려 */
      let typeNm
      let stat
      if (type === 'A') {
        typeNm = '승인'
        stat = '20'
      } else if (type === 'R') {
        typeNm = '반려'
        stat = '30'
      }
      this.$confirm(`${typeNm} 하시겠습니까?`, `${typeNm} 메세지`, {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        if (typeNm === '반려') {
          if (this.sreject_rsn === '' || this.sreject_rsn === null) {
            this.$message('반려시에는 반려사유를 입력해야 합니다.')
            return
          }
        } else if (typeNm === '승인') {
          if (this.resultVo.transyn === 'N') {
            this.$message('이미 해지된 IP주소이므로 승인이 불가합니다 <br> 반려 혹은 변경신청취소만 가능합니다.')
            return
          }
        }
        try {
          const tbWhoisModfiyVo = {
            nmodify_apply_seq: `${this.resultVo.nmodify_apply_seq}`,
            sStatCd: stat,
            nwhoisseq: this.resultVo.nwhoisseq,
            ssaid: this.resultVo.ssaid,
            sreject_rsn: this.sreject_rsn
          }
          const res = await apiRequestJson(ipmsJsonApis.updateWhoisModReqAppr, tbWhoisModfiyVo)
            if (res.tbWhoisModifyVo.commonMsg === 'SUCCESS') {
              this.$message.success(`WHOIS 정보 변경 신청 내역이 정상적으로 ${typeNm} 되었습니다.`)
              this.close()
            }
          } catch (error) {
            this.$message.error({ message: `${typeNm}에 실패했습니다.` })
            console.log(error)
          }
        })
    },
   async fnUpdateRegWhoisModReqSubmit() { /* 수정(등록) */
     if (this.resultVo.transyn === 'N') {
        this.$message('이미 해지된 IP 주소이므로 수정이 불가합니다.')
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
        this.$message('우편주소를 입력하세요.')
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

     try {
        const tbWhoisModifyVo = {
          nmodify_apply_seq: this.resultVo.nmodify_apply_seq,
          sAftOrgName: this.sAftOrgName,
          sAftOrgAddr: this.sAftOrgAddr,
          sAftOrgAddrDetail: this.sAftOrgAddrDetail,
          sAftZipCode: this.sAftZipCode,
          sAftEOrgName: this.sAftEOrgName,
          sAftEOrgAddr: this.sAftEOrgAddr,
          sAftEOrgAddrDetail: this.sAftEOrgAddrDetail,
        }
        const res = await apiRequestJson(ipmsJsonApis.viewUpdateWhoisModReqVo, tbWhoisModifyVo)
        if (res.tbWhoisModifyVo.commonMsg === 'SUCCESS') {
          this.$message.success('WHOIS 정보 변경 신청 내역이 정상적으로 수정되었습니다.')
          this.close()
        }
      } catch (error) {
        console.log(error)
      }
    },
    fnDeleteRegWhoisModReqSubmit() { /* 변경신청취소 */
      this.$confirm('신청정보가 삭제됩니다. 정말 신청취소 하시겠습니까?', '변경신청취소', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const tbWhoisModfiyVo = {
            nmodify_apply_seq: this.resultVo.nmodify_apply_seq,
          }
          const res = await apiRequestJson(ipmsJsonApis.viewDeleteWhoisModReq, tbWhoisModfiyVo)
          if (res.tbWhoisModifyVo.commonMsg === 'SUCCESS') {
            this.$message.success(`WHOIS 정보 변경 신청 내역이 정상적으로 취소 되었습니다.`)
          }
        } catch (error) {
          console.log(error)
        }
      })
    },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
.ModalDetailWhoisMod{
  .el-input {
    width: 100%;
  }
}

</style>
