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
        IP블록배정
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
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
              <col width="10%" />
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
                <!-- <td> {{ item.beforeSsvcLineTypeNm }}</td>
                <td> {{ item.beforeSsvcGroupNm }}</td>
                <td> {{ item.beforeSsvcObjNm }}</td>
                <td> {{ item.sipCreateTypeNm }}</td>
                <td> {{ item.sassignTypeNm }}</td>
                <td> {{ item.pipPrefix }}</td>
                <td> {{ item.sassignLevelNm }}</td>
                <td v-if="false">{{ item.sipVersionTypeCd }}</td>
                <td v-if="false">{{ item.nipAssignMstSeq }}</td> -->
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
                <!-- <td> {{ item.afterSsvcLineTypeNm }}</td>
                <td> {{ item.afterSsvcGroupNm }}</td>
                <td> {{ item.afterSsvcObjNm }}</td>
                <td v-if="false"> {{ item.updSsvcLineTypeCd }}</td>
                <td v-if="false"> {{ item.updSsvcGroupCd }}</td>
                <td v-if="false"> {{ item.updSsvcObjCd }}</td>
                <td v-if="false"> {{ item.updSassignLevelCd }}</td>
                <td v-if="false"> {{ item.updSassignTypeCd }}</td> -->
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result mt5" style="padding-top: 7px;">
          <h4>변경 사유</h4>
          <table class="tbl_data entry" summary="변경후">
            <caption>변경사유 선택</caption>
            <colgroup>
              <col width="39%" /><col width="61%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">변경 사유</th>
                <td>
                  <div class="inline-block w99">
                    <el-select id="sCommentType" v-model="sCommentType" class="" name="sCommentType" disabled>
                      <!-- <option value="typ001" <c:if test ="${fn:trim(resultVo.sCommentType) eq 'typ001'}">selected="selected"</c:if>>기업고객 설변에 따른 노드 이전</option>
                                    <option value="typ002" <c:if test ="${fn:trim(resultVo.sCommentType) eq 'typ002'}">selected="selected"</c:if>>기업고객 해지에 원소속 노드 반납</option>
                                    <option value="typ003" <c:if test ="${fn:trim(resultVo.sCommentType) eq 'typ003'}">selected="selected"</c:if>>DB 정비에 따른 조치</option>
                                    <option value="typ004" <c:if test ="${fn:trim(resultVo.sCommentType) eq 'typ004'}">selected="selected"</c:if>>IP주소 재분배/조정</option>
                                    <option value="typ005" <c:if test ="${fn:trim(resultVo.sCommentType) eq 'typ005'}">selected="selected"</c:if>>단순 반납 실수</option>
                                    <option value="typ006" <c:if test ="${fn:trim(resultVo.sCommentType) eq 'typ006'}">selected="selected"</c:if>>기타</option> -->
                    </el-select>
                  </div>
                </td>
              </tr>
              <tr>
                <th scope="row">세부 사유</th>
                <td>
                  <div class="inline-block w99">
                    <textarea id="sComment" rows="2" class="w98" name="sComment" disabled><c:out value="${resultVo.sComment}"/></textarea>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { apiRequestModel, ipmsModelApis, apiRequestJson, ipmsJsonApis } from '@/api/ipms'

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
      selectedRow: null,
      sCommentType: '',
      tbIpAssignMstListVo: [],
      resultAsgnList: null
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
      this.resultAsgnList = model.row
    },
    //  async fnViewUpdateAsgnIPMst(tbIpAssignMstListVo) {
    //   try {
    //     const res = await apiRequestModel(ipmsModelApis.viewUpdateAsgnIPMst, tbIpAssignMstListVo ?? tbIpAssignMstListVo)
    //     this.tbIpAssignMstListVo = res?.result?.data
    //     this.resultAsgnList = this.tbIpAssignMstListVo[0]
    //   } catch (error) {
    //     console.error(error)
    //   }
    // },

    onClose() { },
  },
}
</script>
<style lang="scss" scoped>
  .ModalNodeTransferDetail{
    .el-select {
      width: 100%;
    }
  }
</style>
