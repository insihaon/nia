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
      <!-- TACS관리 / IP주소 라우팅 비교/점검 > 장비별 명령어 정보관리 > 신규생성 -->
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        장비 명령어 {{ fnType === 'insert' ? '등록' : '수정' }}
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data entry">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">장비타입</th>
                <td colspan="3">
                  <input
                    v-model="formData.sfcltType"
                    :disabled="fnType === 'update'"
                    type="text"
                    class="txt w50"
                    maxlength="50"
                  />
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">장비명령어</th>
                <td colspan="3">
                  <input
                    v-model="formData.sfcltCmd"
                    type="text"
                    class="txt w97"
                    maxlength="250"
                  />
                </td>
              </tr>
              <template v-if="viewType === 'tacsMng'">
                <tr class="last">
                  <th class="first" scope="row">할당판단문구</th>
                  <td>
                    <input
                      v-model="formData.sparseContent"
                      type="text"
                      class="txt w97"
                      maxlength="250"
                    />
                  </td>
                  <th scope="row">할당가능여부</th>
                  <td>
                    <select
                      v-model="formData.savailYn"
                      class="w99"
                    >
                      <option value="">선택</option>
                      <option value="Y">할당가능</option>
                      <option value="N">할당불가능</option>
                    </select>
                  </td>
                </tr>
                <tr v-if="fnType === 'update'" class="last">
                  <th class="first" scope="row">사용여부</th>
                  <td colspan="3">
                    <select
                      v-model="formData.suseYn"
                      style="width: 30%"
                    >
                      <option value="Y">사용</option>
                      <option value="N">미사용</option>
                    </select>
                  </td>
                </tr>
              </template>
              <template v-else>
                <tr class="last">
                  <th class="first" scope="row">순서</th>
                  <td>
                    <input
                      v-model="formData.npriority"
                      type="text"
                      class="txt w97"
                      maxlength="250"
                    />
                  </td>
                  <th scope="row">사용여부</th>
                  <td>
                    <select
                      v-model="formData.suseYn"
                      class="w99"
                    >
                      <option value="">선택</option>
                      <option value="Y">Y</option>
                      <option value="N">N</option>
                    </select>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="fnType === 'insert'" icon="el-icon-edit-outline" type="primary" size="mini" @click="fnInsertFcltMst">등록</el-button>
        <el-button v-if="fnType === 'update'" icon="el-icon-edit-outline" type="primary" size="mini" @click="fnUpdateFcltMst">수정</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalFcltCmdMstInsert'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: 'tacsMng',
      fnType: 'insert',
      defaultFormData: {
        sfcltType: '',
        sfcltCmd: '',
        sparseContent: '',
        savailYn: '',
        suseYn: 'Y',
        npriority: ''
      },
      formData: {},
      sfcltTypes: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 800
    },
    onOpen(model, actionMode) {
      this.formData = this._cloneDeep(this.defaultFormData)
      this.viewType = model.viewType
      this.fnType = model.fnType
      if (this.fnType === 'update') {
        this.formData = this._cloneDeep(model.row)
      }
    },
    onClose() { },
    getParameterByType() {
      const { sfcltType, sfcltCmd, sparseContent, savailYn, suseYn, npriority } = this.formData
      const params = { sfcltType, sfcltCmd }
      if (this.viewType === 'tacsMng') {
        Object.assign(params, { sparseContent, savailYn })
        this.fnType === 'update' && Object.assign(params, { suseYn })
        if (this.formData.ntacsFcltCmdMstSeq !== '' && this.formData.ntacsFcltCmdMstSeq !== null) {
          Object.assign(params, { ntacsFcltCmdMstSeq: this.formData.ntacsFcltCmdMstSeq })
        }
      } else {
        Object.assign(params, { suseYn, npriority })
        this.fnType === 'update' && Object.assign(params, { nfcltCmdMstSeq: this.formData.nfcltCmdMstSeq })
      }
      return params
    },
    async fnInsertFcltMst() {
      if (!this.fnCheckValidate('insert')) return
      try {
        const apiStr = this.viewType === 'tacsMng' ? 'insertTacsFcltCmdMst' : 'insertFcltCmdMst'
        const res = await apiRequestJson(ipmsJsonApis[apiStr], this.getParameterByType())
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '장비 명령어 등록이 정상적으로 처리되었습니다')
          this.formData = this._cloneDeep(this.defaultFormData)
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnUpdateFcltMst() {
      if (!this.fnCheckValidate('update')) return
      try {
        const apiStr = this.viewType === 'tacsMng' ? 'updateTacsFcltCmdMst' : 'updateFcltCmdMst'
        const res = await apiRequestJson(ipmsJsonApis[apiStr], this.getParameterByType())
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '장비 명령어 수정이 정상적으로 처리되었습니다')
          this.formData = this._cloneDeep(this.defaultFormData)
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnCheckValidate(type) {
      const { sfcltType, sfcltCmd, sparseContent, savailYn, npriority } = this.formData
      if (this.fnType === 'insert' && (sfcltType === null || sfcltType === '')) {
        onMessagePopup(this, '장비타입을 선택하세요')
        return false
      }
      if (sfcltCmd === null || sfcltCmd === '') {
        onMessagePopup(this, '장비명령어를 입력하세요')
        return false
      }
      if (this.viewType === 'ipRouting' && (npriority === null || npriority === '')) {
        onMessagePopup(this, '순서를 입력하세요')
          return false
      }
      if (this.viewType === 'tacsMng') {
        if (sparseContent === null || sparseContent === '') {
          onMessagePopup(this, '할당판단문구를 입력하세요')
          return false
        }
        if (this.fnType === 'insert' && (savailYn === null || savailYn === '')) {
          onMessagePopup(this, '할당가능여부를 선택하세요')
          return false
        }
      }
      return true
    }
  },
}
</script>
<style lang="scss" scoped>
.SsvcLineType ::v-deep {
  width: 100%;
  display: flex;
  label {
    margin-left: 0px !important;
  }
}
</style>
