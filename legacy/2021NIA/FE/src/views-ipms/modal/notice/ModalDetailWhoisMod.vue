<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="'Whois 정보 변경 '+onChangetitle"
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
    <div v-if="viewType === 'detail'" class="popupContentTable">
      <div class="popupContentTableTitle">IP 정보</div>
      <table v-loading="tableLoading">
        <colgroup>
          <col width="20%" /><col width="30%" /><col width="20%" /><col width="30%" />
        </colgroup>
        <tbody>
          <tr>
            <th>시작 IP</th>
            <td>{{ resultVo.sfirstAddr }}</td>
            <th>끝 IP</th>
            <td>{{ resultVo.slastAddr }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">KISA WHOIS - IP 주소 사용기관 정보 (변경 전)</div>
      <table v-loading="tableLoading">
        <colgroup>
          <col width="20%" /><col width="80%" />
        </colgroup>
        <tbody>
          <tr>
            <th>한글기관명</th>
            <td>{{ resultVo.sBefOrgName }}</td>
          </tr>
          <tr>
            <th>한글주소</th>
            <td>{{ resultVo.sBefOrgAddr }}</td>
          </tr>
          <tr>
            <th>우편번호</th>
            <td>{{ resultVo.sBefZipCode }}</td>
          </tr>
          <tr>
            <th>영문기관명</th>
            <td>{{ resultVo.sBefEOrgName }}</td>
          </tr>
          <tr class="last">
            <th>영문주소</th>
            <td>{{ resultVo.sBefEOrgAddr }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="viewType === 'edit'" class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click="fnSetAddr('toKT')">KT 정보대체</el-button>
      <el-button type="primary" size="small" icon="el-icon-refresh-right" round @click="fnSetAddr('reset')">초기화</el-button>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">KISA WHOIS - IP 주소 사용기관 정보 (변경 후)</div>
      <table v-loading="tableLoading">
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
            <td>{{ sAftOrgAddr }}</td>
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
    <div v-if="(isStatApply && isMng) || resultVo.sStatCd === '30'" class="popupContentTable">
      <div class="popupContentTableTitle">반려사유</div>
      <table>
        <tbody>
          <tr>
            <td>
              <textarea v-model="sreject_rsn" maxlength="5000" :readonly="resultVo.sStatCd === '30'" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <template v-if="isStatApply && isMng">
        <el-button type="primary" size="small" round @click="fnApprRegWhoisModReqSubmit('A')">승인</el-button>
        <el-button type="primary" size="small" round @click="fnApprRegWhoisModReqSubmit('R')">반려</el-button>
      </template>
      <template v-if="isStatApply">
        <el-button type="primary" size="small" round @click="fnDeleteRegWhoisModReqSubmit()">변경신청취소</el-button>
        <el-button v-if="viewType === 'detail'" type="primary" size="small" icon="el-icon-edit" round @click="onChangeMode()">수정</el-button>
      </template>
      <el-button v-if="viewType === 'edit'" type="primary" size="small" icon="el-icon-edit-outline" round @click="fnUpdateRegWhoisModReqSubmit()">저장</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'
import { mapState } from 'vuex'

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
      tableLoading: false,
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
      sStatCd: ''
    }
  },
  computed: {
    ...mapState({
      adminYn: state => state.ipms.adminYn,
      userId: state => state.user.info.suserId,
      suserGradeCd: state => state.user.info.suserGradeCd,
    }),
    onChangetitle() {
      return this.viewType === 'detail' ? '신청 상세' : '수정'
    },
    ...mapState({
      suserGradeCd: state => state.ipms.suserGradeCd,
    }),
    isStatApply() { // 신청상태
      return this.resultVo.sStatCd === '10'
    },
    isMng() { // 관리자
      return this.suserGradeCd === 'UR0001'
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
      this.viewType = model.type

      if (model.row) {
        setTimeout(() => {
         this.fnViewDetailWhoisMod(model.row)
        }, 10)
      }
    },
    async fnViewDetailWhoisMod(row) {
      const target = ({ vue: this.$refs.content })
      if (row.nmodify_apply_seq === '' || row.nmodify_apply_seq === null) {
        return
      }
      try {
        this.tableLoading = true
        const tbWhoisModifyVo = {
          nmodify_apply_seq: row.nmodify_apply_seq
        }
          const res = await apiRequestModel(ipmsModelApis.viewDetailWhoisModReq, tbWhoisModifyVo)
          this.resultVo = res.result.data
          this.onSetValue()
        } catch (error) {
          console.error(error)
        } finally {
          this.tableLoading = false
        }
    },
    onSetValue() {
      const { sreject_rsn, sAftOrgName, sAftOrgAddr, sAftOrgAddrDetail, sAftZipCode, sAftEOrgName, sAftEOrgAddr, sAftEOrgAddrDetail } = this.resultVo
      this.sreject_rsn = sreject_rsn
      this.sAftOrgName = sAftOrgName
      this.sAftOrgAddr = sAftOrgAddr
      this.sAftOrgAddrDetail = sAftOrgAddrDetail
      this.sAftZipCode = sAftZipCode
      this.sAftEOrgName = sAftEOrgName
      this.sAftEOrgAddr = sAftEOrgAddr
      this.sAftEOrgAddrDetail = sAftEOrgAddrDetail

      this.sStatCd = this.resultVo.sStatCd
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
             onMessagePopup(this, '반려시에는 반려사유를 입력해야 합니다.')
            return
          }
        } else if (typeNm === '승인') {
          if (this.resultVo.transyn === 'N') {
             onMessagePopup(this, `이미 해지된 IP주소이므로 승인이 불가합니다. 반려 혹은 변경신청취소만 가능합니다.`)
            return
          }
        }
        try {
          this.tableLoading = true
          const tbWhoisModfiyVo = {
            nmodify_apply_seq: `${this.resultVo.nmodify_apply_seq}`,
            sStatCd: stat,
            nwhoisseq: this.resultVo.nwhoisseq,
            ssaid: this.resultVo.ssaid,
            sreject_rsn: this.sreject_rsn
          }
          const res = await apiRequestJson(ipmsJsonApis.updateWhoisModReqAppr, tbWhoisModfiyVo)
            if (res.tbWhoisModifyVo.commonMsg === 'SUCCESS') {
              onMessagePopup(this, `WHOIS 정보 변경 신청 내역이 정상적으로 ${typeNm} 되었습니다.`)
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            console.log(error)
        } finally {
          this.tableLoading = false
        }
      })
    },
   async fnUpdateRegWhoisModReqSubmit() { /* 수정(등록) */
     if (this.resultVo.transyn === 'N') {
         onMessagePopup(this, '이미 해지된 IP 주소이므로 수정이 불가합니다.')
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
         onMessagePopup(this, '우편주소를 입력하세요.')
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
          onMessagePopup(this, 'WHOIS 정보 변경 신청 내역이 정상적으로 수정되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
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
            onMessagePopup(this`WHOIS 정보 변경 신청 내역이 정상적으로 취소 되었습니다.`)
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
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

</style>
