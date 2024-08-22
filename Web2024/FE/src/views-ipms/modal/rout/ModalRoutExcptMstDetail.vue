<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      id="ipms"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="true"
      :append-to-body="true"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        예외 상세 정보
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result" style="margin-top: 0px;">
          <div class="section_tit">
            <h3>예외처리</h3>
          </div>
          <table class="tbl_list mt5" summary="Except">
            <caption>예외처리</caption>
            <colgroup>
              <col width="20%" /><col width="80%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th scope="row">예외처리 유형</th>
                <td style="text-align: left;">{{ resultVo.sexcpt_nm }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">예외처리<br />세부사유</th>
                <td colspan="14">
                  <textarea v-model="resultVo.sexcpt_rsn" rows="3" class="w99" name="sexcptRsn" readonly />
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
import { onMessagePopup } from '@/utils'

import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalRoutExcptMstDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {
        sexcpt_nm: '', // 예외처리 유형
        sexcpt_rsn: '', // 예외처리 세부사유
      },
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      // this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.nipAssignMstSeq) {
        this.fnViewExcptDetail(model.nipAssignMstSeq)
      }
    },
    onClose() {
    },
    async fnViewExcptDetail(nipAssignMstSeq) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailRoutExcptMst, { nipAssignMstSeq })
        this.resultVo = res.result.data
      } catch (error) {
        this.error(error)
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
