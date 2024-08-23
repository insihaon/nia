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
        사설AS 상세
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <h4>AS번호 사용기관 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th scope="row">고객명</th>
                <td> <el-input v-model="srequestAsCtm" size="mini" />  </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="content_result">
          <h4>AS번호 정보</h4>
          <table class="tbl_data">
            <colgroup>
              <col width="15%" />
              <col width="20%" />
              <col width="15%" />
              <col width="15%" />
              <col width="15%" />
              <col width="20%" />
            </colgroup>
            <tbody>
              <tr class="view top">
                <th class="first" scope="row">상태</th>
                <td>{{ resultVo.srequestAsTypeNm }}</td>
                <th scope="row">요청자</th>
                <td>{{ resultVo.screateNm }}</td>
                <th scope="row">요청일</th>
                <td>
                  <el-date-picker v-model="dcreateDt" type="datetime" readonly size="mini" format="yyyy-MM-dd" />
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">처리자</th>
                <td colspan="1">{{ resultVo.sapvuserNm }}</td>
                <th scope="row">처리일</th>
                <td colspan="3">
                  <el-date-picker v-model="dapvDt" type="datetime" size="mini" readonly format="yyyy-MM-dd" />
                </td>
              </tr>
            </tbody>
          </table>

          <table class="tbl_list entry mt5 node_info">
            <colgroup>
              <col width="15%" />
              <col width="42.5%" />
              <col width="42.5%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col"></th>
                <th scope="col">노드1</th>
                <th scope="col">노드2</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th class="first" scope="row">노드</th>
                <td> <el-input v-model="srequestAsObjNm1" size="mini" /></td>
                <td> <el-input v-model="srequestAsObjNm2" size="mini" /></td>
              </tr>
              <tr>
                <th class="first" scope="row">전용번호</th>
                <td> <el-input v-model="srequestAsObjLlnum1" size="mini" /></td>
                <td> <el-input v-model="srequestAsObjLlnum2" size="mini" /></td>
              </tr>
              <tr>
                <th class="first" scope="row">개통일</th>
                <td>
                  <el-date-picker v-model="drequestAsObjOpenDt1" type="datetime" size="mini" format="yyyy-MM-dd" />
                </td>
                <td>
                  <el-date-picker v-model="drequestAsObjOpenDt2" type="datetime" size="mini" format="yyyy-MM-dd" />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">보유IP 주소 블록</th>
                <td><el-input v-model="srequestAsObjIpBlock1" size="mini" /></td>
                <td><el-input v-model="srequestAsObjIpBlock2" size="mini" /></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">기타사항</th>
                <td colspan="2">
                  <textarea
                    v-model="scomment"
                    rows="5"
                    class="w98"
                    readonly="readonly"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="content_result">
          <h4>고객측 AS 담당자 정보</h4>
          <table class="tbl_data entry mt5 d">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top last">
                <th class="first" scope="row">이름</th>
                <td> <el-input v-model="sasTpicNm" size="mini" /></td>
                <th scope="row">기관명</th>
                <td> <el-input v-model="sasTpicOrg" size="mini" /></td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>매출 효과</h4>
          <table class="tbl_list mt5">
            <colgroup>
              <col width="15%" />
              <col width="42.5%" />
              <col width="42.5%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col"></th>
                <th scope="col">기존</th>
                <th scope="col">변경</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th class="first" scope="row">속도/가입 회선 수</th>
                <td><el-input v-model="sexistSpLine" size="mini" /></td>
                <td><el-input v-model="saltSpLine" size="mini" /></td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">매출액 (단위:만원)</th>
                <td> <el-input v-model="nexistSales" size="mini" /></td>
                <td><el-input v-model="naltSales" size="mini" /></td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-document-checked float-left" @click="close()">{{ $t('할당') }}</el-button>
        <el-button size="mini" class="float-left" @click="close()">{{ $t('반납') }}</el-button>
        <el-button size="mini" class="float-left" @click="close()">{{ $t('반려') }}</el-button>

        <el-button size="mini" @click="close()">{{ $t('신청 취소') }}</el-button>
        <el-button size="mini" class="el-icon-edit-outline" @click="fnUpdatePrvAsSubmit()">{{ $t('수정') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalDetailPrivateAs'

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
      srequestAsCtm: '',
      dcreateDt: '',
      dapvDt: '',
      srequestAsObjNm1: '',
      srequestAsObjNm2: '',
      srequestAsObjLlnum1: '',
      srequestAsObjLlnum2: '',
      drequestAsObjOpenDt1: '',
      drequestAsObjOpenDt2: '',
      scomment: '',
      sasTpicNm: '',
      sasTpicOrg: '',
      sexistSpLine: '',
      saltSpLine: '',
      nexistSales: '',
      naltSales: '',
    }
  },
  computed: {
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
      this.resultVo = model.row
      const { srequestAsCtm, dcreateDt, dapvDt, srequestAsObjNm1, srequestAsObjNm2, srequestAsObjLlnum1, srequestAsObjLlnum2, drequestAsObjOpenDt1, drequestAsObjOpenDt2, scomment, sasTpicNm, sasTpicOrg, sexistSpLine, saltSpLine, nexistSales, naltSales } = this.resultVo

      this.srequestAsCtm = srequestAsCtm
      this.dcreateDt = dcreateDt
      this.dapvDt = dapvDt
      this.srequestAsObjNm1 = srequestAsObjNm1
      this.srequestAsObjNm2 = srequestAsObjNm2
      this.srequestAsObjLlnum1 = srequestAsObjLlnum1
      this.srequestAsObjLlnum2 = srequestAsObjLlnum2
      this.drequestAsObjOpenDt1 = drequestAsObjOpenDt1
      this.drequestAsObjOpenDt2 = drequestAsObjOpenDt2
      this.scomment = scomment
      this.sasTpicNm = sasTpicNm
      this.sasTpicOrg = sasTpicOrg
      this.sexistSpLine = sexistSpLine
      this.saltSpLine = saltSpLine
      this.nexistSales = nexistSales
      this.naltSales = naltSales
    },
    async fnUpdatePrvAsSubmit() {
      if (this.srequestAsCtm === null || this.srequestAsCtm === '') {
        this.$message('고객명을 입력하세요')
        return
      }

      if (this.srequestAsObjNm1 === null || this.srequestAsObjNm1 === '') {
        this.$message('노드1명을 입력하세요')
        return
      }

      if (this.sasTpicNm === null || this.sasTpicNm === '') {
        this.$message('고객측 AS 담당자를 입력하세요')
        return
      }

      if (this.sasTpicOrg === null || this.sasTpicOrg === '') {
        this.$message('고객측 AS 기관을 입력하세요')
        return
      }

      if (this.sexistSpLine === null || this.sexistSpLine === '') {
        this.$message('기존 속도/가입 회선 수를 입력하세요')
        return
      }

      try {
          const tbRequestAsApyTxnVo = {
            nrequestAsApyTxnSeq: this.resultVo.nrequestAsApyTxnSeq,
            smodifyId: this.resultVo.smodifyId,
            srequestAsCtm: this.srequestAsCtm,
            srequestAsObjNm1: this.srequestAsObjNm1,
            srequestAsObjLlnum1: this.srequestAsObjLlnum1,
            srequestAsObjIpBlock1: this.srequestAsObjIpBlock1,
            srequestAsObjNm2: this.srequestAsObjNm2,
            srequestAsObjLlnum2: this.srequestAsObjLlnum2,
            srequestAsObjIpBlock2: this.srequestAsObjIpBlock2,
            scomment: this.scomment,
            sasTpicNm: this.sasTpicNm,
            sasTpicOrg: this.sasTpicOrg,
            sexistSpLine: this.sexistSpLine,
            saltSpLine: this.saltSpLine,
            nexistSales: this.nexistSales,
            naltSales: this.naltSales,
            ...(this.drequestAsObjOpenDt1 !== '' && { drequestAsObjOpenDt1: this.drequestAsObjOpenDt1 }),
            ...(this.drequestAsObjOpenDt2 !== '' && { drequestAsObjOpenDt2: this.drequestAsObjOpenDt2 }),
          }
        const res = await apiRequestJson(ipmsJsonApis.updatePrivateAs, tbRequestAsApyTxnVo)
          if (res.commonMsg === 'SUCCESS') {
          this.$message.success({ message: `배정신청을 정상적으로 수정 하였습니다.` })
          this.$emit('reloadData')
          }
        } catch (error) {
          this.$message.error({ message: `수정에 실패했습니다.` })
          console.log(error)
        }
    },
    fnDeleteIpAssignApy() {
      this.$confirm('IP 배정신청을 삭제 하시겠습니까?', '삭제 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          var sessionUserId = this.sessionScope.user.suserId
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: this.resultVo.nrequestAssignSeq,
          }
          const res = await apiRequestModel(ipmsModelApis.updateAssignApyTxn, TbRequestAssignMstVo)
           if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `배정신청을 정상적으로 삭제 하였습니다.` })
            this.$emit('reloadData')
            }
          } catch (error) {
            this.$message.error({ message: `삭제에 실패했습니다.` })
            console.log(error)
          }
        })
    },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
.ModalDetailPrivateAs{
  .node_info {
    td {
      text-align: left !important;
    }
  }

  .el-input {
    width: 100%;
  }
}

</style>
