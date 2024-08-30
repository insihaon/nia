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
        노드이전상세
        <hr>
      </span>

      <div id="content" class="layer info">

        <div class="content_result">
          <h4 class="mt5">조회결과(변경 전 정보)</h4>
          <table id="contentBeforeTable" class="tbl_list mt5">
            <colgroup>
              <col width="10%" />
              <col width="20%" />
              <col width="10%" />
              <col width="10%" />
              <col width="20%" />
              <col width="15%" />
              <col width="15%" />
            </colgroup>
          </table>

          <table id="baseTable" class="tbl_list my-3" summary="목록">
            <caption>목록</caption>
            <colgroup>
              <col width="10%" />
              <col width="20%" />
              <col width="10%" />
              <col width="10%" />
              <col width="20%" />
              <col width="12%" />
              <col width="16%" />
            </colgroup>
            <thead>
              <tr>
                <th>서비스망</th>
                <th>본부</th>
                <th>노드</th>
                <th>공인/사설</th>
                <th>서비스</th>
                <th>IP블록</th>
                <th>배정상태</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td> {{ resultVo.beforeSsvcLineTypeNm }}</td>
                <td> {{ resultVo.beforeSsvcGroupNm }}</td>
                <td> {{ resultVo.beforeSsvcObjNm }}</td>
                <td> {{ resultVo.sipCreateTypeNm }}</td>
                <td> {{ resultVo.sassignTypeNm }}</td>
                <td> {{ resultVo.pipPrefix }}</td>
                <td> {{ resultVo.sassignLevelNm }}</td>
                <td v-if="false">{{ resultVo.sipVersionTypeCd }}</td>
                <td v-if="false">{{ resultVo.nipAssignMstSeq }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>변경 후 계위 정보</h4>
          <table id="contentAfterTable" class="tbl_list mt5" summary="조회결과">
            <colgroup>
              <col width="33%" />
              <col width="33%" />
              <col width="33%" />
            </colgroup>
            <thead>
              <th>서비스망</th>
              <th>본부</th>
              <th>노드</th>
            </thead>
            <tbody>
              <tr>
                <td> {{ resultVo.afterSsvcLineTypeNm }}</td>
                <td> {{ resultVo.afterSsvcGroupNm }}</td>
                <td> {{ resultVo.afterSsvcObjNm }}</td>
                <td v-if="false"> {{ resultVo.updSsvcLineTypeCd }}</td>
                <td v-if="false"> {{ resultVo.updSsvcGroupCd }}</td>
                <td v-if="false"> {{ resultVo.updSsvcObjCd }}</td>
                <td v-if="false"> {{ resultVo.updSassignLevelCd }}</td>
                <td v-if="false"> {{ resultVo.updSassignTypeCd }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result mt5" style="padding-top: 7px;">
          <h4>변경 사유</h4>
          <table class="tbl_data entry" summary="변경후">
            <caption>변경사유 선택</caption>
            <colgroup>
              <col width="39%" /><col width="70%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">변경 사유</th>
                <td>
                  <div class="inline-block w-100">
                    <el-select id="sCommentType" v-model="resultVo.sCommentType" class="w-100" size="mini" name="sCommentType" disabled>
                      <el-option value="typ001">기업고객 설변에 따른 노드 이전</el-option>
                      <el-option value="typ002">기업고객 해지에 원소속 노드 반납</el-option>
                      <el-option value="typ003">DB 정비에 따른 조치</el-option>
                      <el-option value="typ004">IP주소 재분배/조정</el-option>
                      <el-option value="typ005">단순 반납 실수</el-option>
                      <el-option value="typ006">기타</el-option>
                    </el-select>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">세부 사유</th>
                <td>
                  <div class="inline-block w-100">
                    <textarea id="sComment" v-model="resultVo.sComment" size="mini" rows="2" type="textarea" class="w98" disabled></textarea>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <template v-if="ownerYn === 'Y'">
          <template v-if="adminYn === 'Y'">
            <el-button v-if="isShowBtn" size="mini" @click="fnCancelBtnClick()">{{ $t('반려') }}</el-button>
            <el-button v-if="isShowBtn" size="mini" @click="fnUpdateconfirmBtnClick()">{{ $t('승인') }}</el-button>
          </template>
          <el-button v-if="isShowBtn" size="mini" @click="fnDeleteBtnClick()">{{ $t('신청취소') }}</el-button>
        </template>
        <el-button size="mini" class="el-icon-close" @click="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { mapState } from 'vuex'

const routeName = 'ModalNodeTransferDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: null
    }
  },
  computed: {
    ...mapState({
      adminYn: state => state.ipms.adminYn,
      ownerYn: state => state.ipms.ownerYn,
    }),
    isShowBtn() { /* 신청취소 disabled */
      return this.resultVo.progressStatus === 'nod001'
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
      this.resultVo = model.row
    },
    fnCancelBtnClick() {
      this.$confirm('반려 하시겠습니까?', '반려 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          const param = { seq: this.resultVo.seq }
          res = await apiRequestModel(ipmsModelApis.viewCancelNode, param)
           if (res.commonMsg === 'SUCCESS') {
            this.$message.success({ message: `노드 이전 신청이 정상적으로 반려되었습니다.` })
            this.$emit('reload')
            this.close()
            }
          } catch (error) {
            this.$message.error({ message: `${res.commonMsg}` })
            console.log(error)
          }
        })
    },
    fnUpdateconfirmBtnClick() {
       this.$confirm('승인 하시겠습니까?', '노드 이전 승인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, nipAssignMstSeq,
          pipPrefix, sipVersionTypeCd, seq, sCommentType, sComment } = this.resultVo
          const tbIpAssignMstComplexVo = {
            srcIpAssignMstVo: {
              ssvcLineTypeCd: ssvcLineTypeCd,
              ssvcGroupCd: ssvcGroupCd,
              ssvcObjCd: ssvcObjCd,
              sassignLevelCd: 'IA0003',
              sassignTypeCd: 'SA0000',
              scomment: `${sCommentType}\n${sComment}`,
              typeFlag: ''
            },
            destIpAssignMstVos: [
              {
                nipAssignMstSeq: nipAssignMstSeq,
                pipPrefix: pipPrefix,
                sipVersionTypeCd: sipVersionTypeCd,
                typeFlag: 'assign',
                menuType: 'NodeReq'
              }
            ],
            nodeMgmtVo: {
              seq: seq
            }
          }
           res = await apiRequestModel(ipmsModelApis.confirmNode, tbIpAssignMstComplexVo)
           if (res.commonMsg) {
            this.$message.success({ message: `${res.commonMsg}` })
            this.$emit('reload')
            this.close()
            }
          } catch (error) {
            this.$message.error({ message: `${res.commonMsg}` })
            console.log(error)
          }
        })
    },
      fnDeleteBtnClick() { // 신청취소
      // this.$confirm('정말 삭제 하시겠습니까?', {
      //   confirmButtonText: '확인',
      //   cancelButtonText: '취소'
      // }).then(async() => {
      //   try {
      //     const nodeVo = { seq: this.resultVo.seq }
      //     const res = await '' /* apiRequestModel(ipmsModelApis.viewDeletenode, nodeVo) */
      //      if (res.commonMsg === 'SUCCESS') {
      //       this.$message.success({ message: `노드 이전 신청이 정상적으로 삭제되었습니다.` })
      //         this.$emit('reload')
      //          this.close()
      //       }
      //     } catch (error) {
      //       this.$message.error({ message: `삭제에 실패했습니다.` })
      //       console.log(error)
      //     }
      //   })
    },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>

</style>
