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
        요구사항 상세
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
          <template v-if="viewType !== 'create'">
            <template v-if="viewType === 'detail' || viewType === 'edit'">
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
            </template>
          </template>

        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="fnDeletePrvAsSubmit()">{{ $t('신청 취소') }}</el-button>
        <el-button size="mini" class="el-icon-edit" @click="onChangeMode()">{{ $t('수정') }}</el-button>
        <el-button size="mini" class="el-icon-edit-outline" style="background-color:#2e3574; color : #fff" @click="fnUpdatePrvAsSubmit()">{{ $t('수정') }}</el-button>
        <el-button v-if="viewType === 'create'" size="mini" class="el-icon-edit-outline" @click="fnViewInsertPrvAs()">{{ $t('등록') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalReqDetail'

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
      srequestAsObjIpBlock1: '',
      srequestAsObjIpBlock2: '',
      scomment: '',
      sasTpicNm: '',
      sasTpicOrg: '',
      sexistSpLine: '',
      saltSpLine: '',
      nexistSales: '',
      naltSales: '',
      viewType: null
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
      this.viewType = model.type
      if (model.type === 'detail') {
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
      } else {
        this.resultVo = {}
      }
    },
    fnReturnAsTxmSubmit() { /* TbRequestAsApyTxn 반납  */
      this.$confirm('반납 하시겠습니까?', '반납 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const tbRequestAsApyTxnVo = {
            nrequestAsApyTxnSeq: this.resultVo.nrequestAssignSeq,
            srequestAsTypeCd: 'RS0204',
            sapvuserId: 'suserId'
          }
          const res = await apiRequestJson(ipmsJsonApis.updateNrequestAsSeqYn, tbRequestAsApyTxnVo)
            if (res.tbRequestAsApyTxnVo.commonMsg === 'SUCCESS') {
             this.fnReturnAsMstSubmit(this.resultVo.nrequestAsSeq)
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
