<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="예외 상세 정보"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">예외처리</div>
      <table>
        <colgroup>
          <col width="20%" /><col width="80%" />
        </colgroup>
        <tbody>
          <tr>
            <th>예외처리 유형</th>
            <td class="text-left">{{ resultVo.sexcpt_nm }}</td>
          </tr>
          <tr>
            <th>예외처리<br />세부사유</th>
            <td colspan="14">
              <textarea v-model="resultVo.sexcpt_rsn" rows="3" readonly />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
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
