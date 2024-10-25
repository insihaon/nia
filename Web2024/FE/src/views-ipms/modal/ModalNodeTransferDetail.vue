<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="노드이전상세"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="true"
    :append-to-body="true"
    :modal="modal"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">조회결과(변경 전 정보)</div>
      <table>
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

    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">변경 후 계위 정보</div>
      <table>
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
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">변경 사유</div>
      <table>
        <tbody>
          <tr>
            <th>변경 사유</th>
            <td class="flex">
              <el-select v-model="resultVo.sCommentType" size="small" disabled>
                <el-option
                  v-for="item in sCommentOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>세부 사유</th>
            <td class="flex">
              <textarea v-model="resultVo.sComment" size="small" rows="2" type="textarea" readonly></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <template v-if="ownerYn === 'Y'">
        <template v-if="adminYn === 'Y'">
          <el-button v-if="isShowBtn" type="primary" size="small" round @click="fnCancelBtnClick()">반려</el-button>
          <el-button v-if="isShowBtn" type="primary" size="small" round @click="fnUpdateconfirmBtnClick()">승인</el-button>
        </template>
        <el-button v-if="isShowBtn" type="primary" size="small" round @click="fnDeleteBtnClick()">신청취소</el-button>
      </template>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
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
      resultVo: null,
      isLoading: false,
      sCommentOptions: [
        { value: 'typ001', label: '기업고객 설변에 따른 노드 이전' },
        { value: 'typ002', label: '기업고객 해지에 원소속 노드 반납' },
        { value: 'typ003', label: 'DB 정비에 따른 조치' },
        { value: 'typ004', label: 'IP주소 재분배/조정' },
        { value: 'typ005', label: '단순 반납 실수' },
        { value: 'typ006', label: '기타' }
      ],
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
      this.$confirm('반려 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
          const param = { seq: this.resultVo.seq }
          try {
            this.isLoading = true
           const res = await apiRequestJson(ipmsJsonApis.viewCancelNode, param)
           if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '노드 이전 신청이 정상적으로 반려되었습니다.')
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          } finally {
            this.isLoading = false
          }
        })
    },
    fnUpdateconfirmBtnClick() {
       this.$confirm('승인 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
          const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, nipAssignMstSeq,
          pipPrefix, sipVersionTypeCd, seq, sCommentType, sComment } = this.resultVo
          const tbIpAssignMstComplexVo = {
            srcIpAssignMstVo: {
              ssvcLineTypeCd,
              ssvcGroupCd,
              ssvcObjCd,
              sassignLevelCd: 'IA0003',
              sassignTypeCd: 'SA0000',
              scomment: `${sCommentType}\n${sComment}`,
              typeFlag: ''
            },
            destIpAssignMstVos: [
              {
                nipAssignMstSeq,
                pipPrefix,
                sipVersionTypeCd,
                typeFlag: 'assign',
                menuType: 'NodeReq'
              }
            ],
            nodeMgmtVo: {
              seq: seq
            }
          }
          try {
            this.isLoading = true
            const res = await apiRequestJson(ipmsJsonApis.confirmNode, tbIpAssignMstComplexVo)
            if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, 'IP블록 배정이 정상적으로 처리되었습니다.')
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          } finally {
            this.isLoading = false
          }
        })
    },
      fnDeleteBtnClick() { // 신청취소
      this.$confirm('신청취소 하시겠습니까?', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          this.isLoading = true
          const res = await apiRequestJson(ipmsJsonApis.viewDeleteNode, { seq: this.resultVo.seq })
           if (res.commonMsg === 'SUCCESS') {
              onMessagePopup(this, '노드 이전 신청이 정상적으로 삭제되었습니다.')
              this.$emit('reload')
              this.close()
            } else {
              onMessagePopup(this, res.commonMsg)
            }
          } catch (error) {
            this.error(error)
          } finally {
            this.isLoading = false
          }
        })
    },
    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
</style>
