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
        IP 배정신청 {{ viewType === 'detail' ? '상세정보' : '수정' }}
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
                <td v-if="!isEdit" colspan="5">{{ resultVo.stitle }}</td>
                <td v-else colspan="5"><input v-model="resultVo.stitle" type="text" class="txt w98" maxlength="30"></td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">서비스망</th>
                <td>{{ resultVo.ssvcLineTypeNm }}</td>
                <th scope="row">본부</th>
                <td>{{ resultVo.ssvcGroupNm }}</td>
                <th scope="row">노드</th>
                <td>{{ resultVo.ssvcObjNm }}</td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">신청자</th>
                <td>{{ resultVo.sapyUserNm }}</td>
                <th scope="row">신청일</th>
                <td>
                  <el-date-picker v-model="resultVo.dapyDt" type="datetime" size="mini" readonly format="yyyy-MM-dd HH:mm:ss" />
                </td>
                <th scope="row">상태</th>
                <td>{{ resultVo.srequestAssignTypeNm }}</td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">소속부서</th>
                <td colspan="5">{{ resultVo.sapyUserFullDeptOrgNm }}</td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">요청 IP개수 (/24)</th>
                <td v-if="!isEdit" colspan="5">{{ resultVo.napyIpCnt }}</td>
                <td v-else colspan="5"><input v-model="resultVo.napyIpCnt" type="text" style="width: 80%" maxlength="5"> 개(/24 단위)</td>
              </tr>
              <tr class="view">
                <th class="first" scope="row">신청내용</th>
                <td colspan="5">
                  <textarea v-model="resultVo.scontents" size="mini" type="textarea" rows="10" :readonly="!isEdit"></textarea>
                </td>
              </tr>
              <template v-if="!isEdit">
                <tr class="view">
                  <th class="first" scope="row">처리일시</th>
                  <td colspan="5">
                    <el-date-picker v-model="resultVo.dtrtDt" size="mini" type="datetime" readonly format="yyyy-MM-dd HH:mm:ss" />
                  </td>
                </tr>
                <template v-if="suserGradeCd === 'UR0001'">
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
                </template>
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
                      <textarea v-model="sassigncontents" />

                    </td></tr>
                  <tr class="last">
                    <th class="first" scope="row">배정 IP개수 (/24)</th>
                    <td colspan="5">
                      <textarea v-model="nassignIpCnt" maxlength="5" class="w-90" @input="validateNumberInput" />
                    </td>
                  </tr>
                </template>
              </template>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <template v-if="isAdmin && !isEdit">
          <el-button v-if="!isAssignAndReject" size="mini" type="primary" class="el-icon-document-checked float-left" @click="fnIpAssign()">배정</el-button>
          <el-button v-if="isStatusApply" size="mini" type="primary" @click="fnApproveIpAssignApy()">승인</el-button>
          <el-button v-if="isStatusApply" size="mini" type="primary" @click="fnRejectIpAssignApy()">반송</el-button>
        </template>
        <template v-if="resultVo.sapyUserId === suserId && isStatusApply && !isEdit">
          <el-button size="mini" class="el-icon-edit-outline" type="primary" @click="viewType = 'edit'"> 수정</el-button>
          <el-button size="mini" class="el-icon-delete" type="primary" @click="fnDeleteIpAssignApy()"> 삭제</el-button>
        </template>

        <el-button v-if="!isStatusApply && isAdmin" size="mini" @click="fnUpdateStrtContents('modify')">처리내용 수정</el-button>
        <el-button v-if="isEdit" size="mini" class="el-icon-edit-outline" type="primary" @click="fnUpdateStrtContents('regist')"> 등록</el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'
import { mapState } from 'vuex'

const routeName = 'ModalAssignApyDetail'

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
      sessionScope: null,
      strtContents: '',
      sassigncontents: '',
      nassignIpCnt: '',
      viewType: '',

    }
  },
  computed: {
    ...mapState({
      adminYn: state => state.ipms.adminYn,
      suserId: state => state.user.info.suserId,
      suserGradeCd: state => state.user.info.suserGradeCd,
    }),
    isEdit() {
      return this.viewType === 'edit'
    },
    isAdmin() {
      return this.suserGradeCd === 'UR0001'
    },
    isStatusApply() {
      return this.resultVo.srequestAssignTypeCd === 'RS0301'
    },
    isAssignAndReject() { // 반려 , 배정 상태
      return this.resultVo.srequestAssignTypeCd === 'RS0303' || this.resultVo.srequestAssignTypeCd === 'RS0304'
    }
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
      this.viewType = model.type
      this.resultVo = model.row
      const { strtContents, sassigncontents, nassignIpCnt } = this.resultVo
       this.strtContents = strtContents
       this.sassigncontents = sassigncontents
       this.nassignIpCnt = nassignIpCnt
    },
    fnIpAssign() {
      if (this.strtContents === null || this.strtContents === '') {
        onMessagePopup(this, '처리내용을 입력하세요.')
        return
      }
      this.$confirm('배정 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          const { nrequestAssignSeq } = this.resultVo
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: nrequestAssignSeq,
            srequestAssignTypeCd: 'RS0304',
            strtContents: this.strtContents,
            strtUserId: this.$store.state.user.info.suserId,
            smodifyId: this.$store.state.user.info.suserId,
          }
           res = await apiRequestJson(ipmsJsonApis.updateAssignApyTxn, TbRequestAssignMstVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '배정에 성공했습니다.')
            this.close()
            // 미배정현황 page route
            const { ssvcLineTypeCd, ssvcGroupCd } = this.resultVo
            this.$store.dispatch('ipms/setToParam', { value: { ssvcLineTypeCd, ssvcGroupCd } })
            this.$router.push({ path: '/ipAssignMng/ipunAllocatedStatus' })
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
    fnApproveIpAssignApy() {
      if (this.strtContents === null || this.strtContents === '') {
        onMessagePopup(this, '처리내용을 입력하세요.')
        return
      }

       this.$confirm('IP 배정신청을 승인 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          const { nrequestAssignSeq, screateId } = this.resultVo
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: nrequestAssignSeq,
            srequestAssignTypeCd: 'RS0302',
            strtContents: this.strtContents,
            strtUserId: this.suserId,
            smodifyId: this.suserId,
            screateId: screateId,
            sassigncontents: this.sassigncontents,
            nassignIpCnt: this.nassignIpCnt,
          }
           res = await apiRequestJson(ipmsJsonApis.updateAssignApyTxn, TbRequestAssignMstVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, 'IP 배정신청을 정상적으로 승인 하였습니다.')
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
    fnRejectIpAssignApy() {
      this.$confirm('IP 배정신청을 반송 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          const { nrequestAssignSeq, strtUserId, smodifyId, screateId } = this.resultVo
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: nrequestAssignSeq,
            srequestAssignTypeCd: 'RS0303',
            strtContents: this.strtContents,
            strtUserId: this.$store.state.user.info.suserId,
            smodifyId: this.$store.state.user.info.suserId,
            screateId: screateId,
            sassigncontents: this.sassigncontents,
            nassignIpCnt: this.nassignIpCnt,
          }
          res = await apiRequestJson(ipmsJsonApis.updateAssignApyTxn, TbRequestAssignMstVo)
           if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, 'IP 배정신청을 정상적으로 반려 하였습니다.')
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          }
        })
    },
   async fnUpdateStrtContents(type) {
      if (type === 'edit' && this.resultVo.scontents === null || this.resultVo.scontents === '') {
        onMessagePopup(this, '신청내용을 입력하세요.')
        return
      }
      if (type === 'modify' && this.strtContents === null || this.strtContents === '') {
        onMessagePopup(this, '처리내용을 입력하세요.')
        return
      }
      const { nrequestAssignSeq, stitle, napyIpCnt, scontents } = this.resultVo
      const TbRequestAssignMstVo = { nrequestAssignSeq, smodifyId: this.suserId }
      if (type === 'edit') {
        const { stitle, napyIpCnt, scontents } = this.resultVo
        Object.assign(TbRequestAssignMstVo, { stitle, scontents, napyIpCnt })
      } else if (type === 'modify') {
        Object.assign(TbRequestAssignMstVo, { strtContents: this.strtContents, sassigncontents: this.sassigncontents, nassignIpCnt: this.nassignIpCnt })
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.updateAssignApyTxn, TbRequestAssignMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '처리내용이 정상적으로 수정되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnDeleteIpAssignApy() {
      this.$confirm('IP 배정신청을 삭제 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
           const res = await apiRequestJson(ipmsJsonApis.deleteAssignApyTxn, { nrequestAssignSeq: this.resultVo.nrequestAssignSeq })
           if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '배정신청을 정상적으로 삭제 하였습니다.')
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
          } catch (error) {
            this.error(error)
          }
        })
    },
    onClose() {
      this.strtContents = ''
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
