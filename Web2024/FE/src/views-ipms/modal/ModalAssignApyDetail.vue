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
        IP 배정신청 상세정보
        <hr>
      </span>

      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data">
            <colgroup>
              <col width="14%" /><col width="20%" />
              <col width="13%" /><col width="20%" />
              <col width="13%" /><col width="20%" />
            </colgroup>
            <tbody>
              <tr class="view top">
                <th class="first" scope="row">제목</th>
                <td colspan="5">
                  {{ resultVo.stitle }}
                </td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">서비스망</th>
                <td>
                  {{ resultVo.ssvcLineTypeNm }}
                </td>
                <th scope="row">본부</th>
                <td>
                  {{ resultVo.ssvcGroupNm }}
                </td>
                <th scope="row">노드</th>
                <td>
                  {{ resultVo.ssvcObjNm }}
                </td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">신청자</th>
                <td>
                  {{ resultVo.sapyUserNm }}
                </td>
                <th scope="row">신청일</th>
                <td>
                  <el-date-picker v-model="resultVo.dapyDt" type="datetime" size="mini" readonly format="yyyy-MM-dd HH:mm:ss" />
                </td>
                <th scope="row">상태</th>
                <td>
                  {{ resultVo.srequestAssignTypeNm }}
                </td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">소속부서</th>
                <td colspan="5">
                  {{ resultVo.sapyUserFullDeptOrgNm }}
                </td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">요청 IP개수 (/24)</th>
                <td colspan="5">
                  {{ resultVo.napyIpCnt }}
                </td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">신청내용</th>
                <td colspan="5">
                  <textarea v-model="resultVo.scontents" size="mini" type="textarea" rows="10" readonly></textarea>
                </td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">처리일시</th>
                <td colspan="5">
                  <el-date-picker v-model="resultVo.dtrtDt" size="mini" type="datetime" readonly format="yyyy-MM-dd HH:mm:ss" />
                </td>
              </tr>

              <!-- <template v-if="sessionScope.user.suserGradeCd === 'UR0001'"> -->
              <template v-if="['RS0301', 'RS0302', 'RS0303', 'RS0304'].includes(resultVo.srequestAssignTypeCd)">
                <tr>
                  <th class="first" scope="row">처리내용</th>
                  <td colspan="5">
                    <textarea id="txtAssignIpCnt" v-model="strtContents" rows="6" />
                  </td>
                </tr>
                <tr>
                  <th class="first" scope="row">배정 IP</th>
                  <td colspan="5">
                    <textarea v-model="sassigncontents" rows="5" />
                  </td>
                </tr>
                <tr class="last">
                  <th class="first" scope="row">배정 IP개수 (/24)</th>
                  <td colspan="5">
                    <textarea v-model="nassignIpCnt" class="w-90" @input="validateNumberInput" /> 개(/24 단위)
                  </td>
                </tr>
              </template>
              <!-- </template> -->

              <template v-else>
                <tr>
                  <th class="first" scope="row">처리내용</th>
                  <td colspan="5">
                    <textarea id="txtAssignIpCnt" v-model="strtContents" type="textarea" rows="6" />
                  </td>
                </tr>
                <tr>
                  <th class="first" scope="row">배정 IP</th>
                  <td colspan="5">
                    <textarea v-model="sassigncontents" type="textarea" rows="5" />

                  </td></tr>
                <tr class="last">
                  <th class="first" scope="row">배정 IP개수 (/24)</th>
                  <td colspan="5">
                    <textarea v-model="nassignIpCnt" maxlength="5" class="w-90" @input="validateNumberInput" />
                  </td>
                </tr>
              </template>

              <el-input id="sapyUserId" type="hidden" :value="resultVo.sapyUserId" />
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <!-- 배정 resultVo.srequestAssignTypeCd === 'RS0301' -->
        <el-button size="mini" class="el-icon-document-checked float-left" @click="fnIpAssign()">{{ $t('배정') }}</el-button>
        <el-button size="mini" @click="fnApproveIpAssignApy()">{{ $t('승인') }}</el-button>
        <el-button size="mini" @click="fnRejectIpAssignApy()">{{ $t('반송') }}</el-button>

        <el-button size="mini" class="el-icon-edit-outline" @click="close()">{{ $t('수정') }}</el-button>
        <el-button size="mini" class="el-icon-delete" @click="fnDeleteIpAssignApy()">{{ $t('삭제') }}</el-button>
        <el-button size="mini" @click="fnUpdateStrtContents()">{{ $t('처리내용 수정') }}</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalAssignApyDetail'

export function onFocusInput() {
  return document.getElementById('txtAssignIpCnt').focus()
}

export default {

  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: null,
      sessionScope: null,
      strtContents: '',
      sassigncontents: '',
      nassignIpCnt: '',
    }
  },
  computed: {

  },
  mounted() {
  },
  methods: {
    validateNumberInput() {
      const regExp = /[^0-9]/g
      this.nassignIpCnt = this.nassignIpCnt.replace(regExp, '')
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      this.resultVo = model.row
      const { strtContents, sassigncontents, nassignIpCnt } = this.resultVo
       this.strtContents = strtContents
       this.sassigncontents = sassigncontents
       this.nassignIpCnt = nassignIpCnt
    },
    fnIpAssign() {
      if (this.strtContents === null || this.strtContents === '') {
        this.$message('처리내용을 입력하세요.')
        onFocusInput()
        return
      }
      this.$confirm('배정 하시겠습니까?', '배정 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          var sessionUserId = this.sessionScope.user.suserId
          const { nrequestAssignSeq, strtUserId, smodifyId } = this.resultVo
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: nrequestAssignSeq,
            srequestAssignTypeCd: 'RS0304',
            strtContents: this.strtContents,
            // strtUserId: sessionUserId,
            // smodifyId: sessionUserId,
          }
          const res = await apiRequestJson(ipmsJsonApis.updateAssignApyTxn, TbRequestAssignMstVo)
          if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `배정에 성공했습니다.` })
            // 미배정현황 page route
            this.$$router.push({ path: '/ipAssignMng/ipunAllocatedStatus' })
          }
        } catch (error) {
          this.$message.error({ message: `배정에 실패했습니다.` })
          console.log(error)
        }
      })
    },
    fnApproveIpAssignApy() {
      if (this.strtContents === null || this.strtContents === '') {
        this.$message('처리내용을 입력하세요.')
        onFocusInput()
        return
      }

       this.$confirm('IP 배정신청을 승인 하시겠습니까?', '승인 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          var sessionUserId = this.sessionScope.user.suserId
          const { nrequestAssignSeq, strtContents, strtUserId, smodifyId, screateId, } = this.resultVo
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: nrequestAssignSeq,
            srequestAssignTypeCd: 'RS0302',
            strtContents: this.strtContents,
            // strtUserId: sessionUserId,
            // smodifyId: sessionUserId,
            screateId: screateId,
            sassigncontents: this.sassigncontents,
            nassignIpCnt: this.nassignIpCnt,
          }
          const res = await apiRequestJson(ipmsJsonApis.updateAssignApyTxn, TbRequestAssignMstVo)
          if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `승인되었습니다.` })
            this.$emit('reloadData')
          }
        } catch (error) {
          this.$message.error({ message: `승인에 실패했습니다.` })
          console.log(error)
        }
      })
    },
    fnRejectIpAssignApy() {
      this.$confirm('IP 배정신청을 반송 하시겠습니까?', '반송 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          var sessionUserId = this.sessionScope.user.suserId
          const { nrequestAssignSeq, strtUserId, smodifyId, screateId } = this.resultVo
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: nrequestAssignSeq,
            srequestAssignTypeCd: 'RS0303',
            strtContents: this.strtContents,
            // strtUserId: sessionUserId,
            // smodifyId: sessionUserId,
            screateId: screateId,
            sassigncontents: this.sassigncontents,
            nassignIpCnt: this.nassignIpCnt,
          }
          const res = await apiRequestModel(ipmsModelApis.updateAssignApyTxn, TbRequestAssignMstVo)
           if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `정상적으로 반송되었습니다.` })
            this.$emit('reloadData')
            }
          } catch (error) {
            this.$message.error({ message: `반송에 실패했습니다.` })
            console.log(error)
          }
        })
    },
   async fnUpdateStrtContents() {
      if (this.strtContents === null || this.strtContents === '') {
        this.$message('처리내용을 입력하세요.')
        onFocusInput()
        return
      }

      try {
          var sessionUserId = this.sessionScope.user.suserId
          const { nrequestAssignSeq, strtUserId, smodifyId, screateId } = this.resultVo
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: nrequestAssignSeq,
            strtContents: this.strtContents,
            // smodifyId: sessionUserId,
            sassigncontents: this.sassigncontents,
            nassignIpCnt: this.nassignIpCnt,
          }
          const res = await apiRequestModel(ipmsModelApis.updateAssignApyTxn, TbRequestAssignMstVo)
           if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `처리내용이 정상적으로 수정되었습니다.` })
            this.$emit('reloadData')
            }
          } catch (error) {
            this.$message.error({ message: `반송에 실패했습니다.` })
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
</style>
