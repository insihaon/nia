<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="'장비 명령어'+(fnType === 'insert' ? '등록' : '수정')"
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
    <div class="popupContentTable">
      <table>
        <tbody>
          <tr>
            <th>장비타입</th>
            <td colspan="3">
              <el-input
                v-model="formData.sfcltType"
                :disabled="fnType === 'update'"
                type="text"
                maxlength="50"
              />
            </td>
          </tr>
          <tr>
            <th>장비명령어</th>
            <td colspan="3">
              <el-input
                v-model="formData.sfcltCmd"
                type="text"
                maxlength="250"
              />
            </td>
          </tr>
          <template v-if="viewType === 'tacsMng'">
            <tr>
              <th>할당판단문구</th>
              <td>
                <el-input
                  v-model="formData.sparseContent"
                  type="text"
                  maxlength="250"
                />
              </td>
              <th>할당가능여부</th>
              <td>
                <el-select v-model="formData.savailYn" popper-class="savailYn">
                  <el-option value="" label="선택" />
                  <el-option value="Y" label="할당가능" />
                  <el-option value="N" label="할당불가능" />
                </el-select>
              </td>
            </tr>
            <tr v-if="fnType === 'update'">
              <th>사용여부</th>
              <td colspan="3">
                <el-select v-model="formData.suseYn" style="width: 30%" popper-class="suseYn">
                  <el-option value="Y" label="사용" />
                  <el-option value="N" label="미사용" />
                </el-select>
              </td>
            </tr>
          </template>
          <template v-else>
            <tr>
              <th>순서</th>
              <td>
                <el-input
                  v-model="formData.npriority"
                  type="text"
                  maxlength="250"
                />
              </td>
              <th>사용여부</th>
              <td>
                <el-select v-model="formData.suseYn" popper-class="suseYn">
                  <el-option value="" label="선택" />
                  <el-option value="Y" label="Y" />
                  <el-option value="N" label="N" />
                </el-select>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button v-if="fnType === 'insert'" type="primary" size="small" icon="el-icon-document-add" round @click="fnInsertFcltMst">등록</el-button>
      <el-button v-if="fnType === 'update'" type="primary" size="small" icon="el-icon-edit" round @click="fnUpdateFcltMst">수정</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
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
