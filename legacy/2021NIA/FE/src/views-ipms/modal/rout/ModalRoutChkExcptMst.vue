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
        예외 처리 관리
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
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th scope="row">예외처리 유형</th>
                <td>
                  <select v-model="sexcptCd">
                    <option value="">전체</option>
                    <option v-for="(item, index) in exceptionTypes" :key="index" :value="item.value">
                      {{ item.label }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">
                  예외처리<br />세부사유
                </th>
                <td colspan="14">
                  <textarea id="sexcptRsn" v-model="sexcptRsn" rows="3" class="w99"></textarea>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="fninsertListExcptMst()">예외처리</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
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
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
