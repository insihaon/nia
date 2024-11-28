<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="예외 처리 관리"
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
          <col width="20%" />
          <col width="80%" />
        </colgroup>
        <tbody>
          <tr>
            <th>예외처리 유형</th>
            <td class="text-left">
              <el-select v-model="sexcptCd" popper-class="exceptOption">
                <el-option value="">전체</el-option>
                <el-option v-for="(item, index) in exceptionTypes" :key="index" :value="item.value">
                  {{ item.label }}
                </el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>
              예외처리<br />세부사유
            </th>
            <td colspan="14">
              <textarea id="sexcptRsn" v-model="sexcptRsn" rows="3"></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-warning-outline" round @click="fnInsertListExcptMst()">예외처리</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'

import { ipmsModelApis, ipmsJsonApis, apiRequestModel, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalRoutChkExcptMst'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      sexcptCd: '',
      sexcptRsn: '',
      exceptionTypes: [],
      chkListStr: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.tbRoutChkMstVo) {
        this.chkListStr = this._cloneDeep(model.tbRoutChkMstVo.chkListStr)
        this.fnViewPopRoutChkExceptMst(model.tbRoutChkMstVo)
      }
    },
    onClose() {
      this.chkListStr = []
      this.sexcptCd = ''
      this.sexcptRsn = ''
    },
    async fnViewPopRoutChkExceptMst(tbRoutChkMstVo) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewPopRoutChkExceptMst, tbRoutChkMstVo)
        const typeTemp = res.result.data
        this.exceptionTypes = typeTemp.map(item => { return { label: item.name, value: item.code } })
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertListExcptMst() {
      if (!this.sexcptCd || this.sexcptCd.length === 0) {
        onMessagePopup(this, '예외처리 유형을 선택해 주세요.')
        return
      }
      if (!this.sexcptRsn || this.sexcptRsn.length === 0) {
        onMessagePopup(this, '예외처리 세부사유를 입력해 주세요.')
        return
      }
      const tbRoutChkMstVo = {
        chkListStr: this.chkListStr,
        sexcpt_yn: 'Y',
        sexcpt_cd: this.sexcptCd,
        sexcpt_rsn: this.sexcptRsn
      }
      try {
        this.loading = true
        const res = await apiRequestJson(ipmsJsonApis.insertListExcptMst, tbRoutChkMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '예외처리가 정상적으로 처리되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
