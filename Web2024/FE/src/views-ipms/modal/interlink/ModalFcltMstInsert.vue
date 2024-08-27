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
      <!-- TACS관리 / IP주소 라우팅 비교/점검 > 조직별 장비 정보관리 > 신규생성 -->
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        조직 장비 등록
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <table class="tbl_data entry">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위</th>
                <td v-if="fnType === 'update'" colspan="3">
                  {{ formData.ssvcLineTypeNm }} - {{ formData.ssvcGroupNm }} - {{ formData.ssvcObjNm }}
                </td>
                <td v-else colspan="3">
                  <ul>
                    <SsvcLineType
                      ref="ssvcLineType"
                      class="SsvcLineType"
                      label=""
                      :lvl="3"
                      :is-all-lvl1="false"
                      :props-lvl-options="{
                        1: [
                          { label: 'KORNET', value: 'CL0001' },
                          { label: 'PREMIUM', value: 'CL0002' },
                          { label: 'MOBILE', value: 'CL0003' }
                        ]
                      }"
                      @update-value="onChangeSsvcLineType"
                    />
                  </ul>
                </td>
              </tr>
              <tr>
                <th scope="row">장비타입</th>
                <td colspan="3">
                  <select v-model="formData.sfcltType" name="insertSfcltType" class="w99">
                    <option value="">선택</option>
                    <option
                      v-for="(item, index) in sfcltTypes"
                      :key="index"
                      :value="item.code"
                      :label="item.name"
                    />
                  </select>
                </td>
              </tr>
              <tr>
                <th class="first" scope="row">장비IP</th>
                <td>
                  <input
                    v-model="formData.pipFcltInet"
                    type="text"
                    class="txt w96"
                    name="insertPipFcltInet"
                    title="IP 주소 입력창"
                    maxlength="43"
                  />
                </td>
                <th scope="row">장비PORT</th>
                <td>
                  <select v-model="formData.nportFclt" class="w99">
                    <option value="">선택</option>
                    <option value="23">TELNET(23)</option>
                    <option value="22">SSH(22)</option>
                  </select>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">장비프롬프트명</th>
                <td>
                  <input v-model="formData.sfcltPromptNm" type="text" class="txt w96" />
                </td>
                <th scope="row">장비모델명</th>
                <td>
                  <input v-model="formData.sfcltModelNm" type="text" class="txt w96" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="fnType === 'insert'" icon="el-icon-edit-outline" type="primary" size="mini" @click="fnInsertTacsFcltMst">등록</el-button>
        <el-button v-if="fnType === 'update'" icon="el-icon-edit-outline" type="primary" size="mini" @click="fnUpdateTacsFcltMst">수정</el-button>
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import SsvcLineType from '@/views-ipms/conditionComponents/SsvcLineType'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalFcltMstInsert'

export default {
  name: routeName,
  components: { SsvcLineType },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      fnType: 'insert',
      defaultFormData: {
        ssvcLineTypeCd: '',
        ssvcGroupCd: '',
        ssvcObjCd: '',
        pipFcltInet: '',
        nportFclt: '',
        sfcltType: '',
        sfcltPromptNm: '',
        sfcltModelNm: ''
      },
      formData: {},
      sfcltTypes: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.formData = this._cloneDeep(this.defaultFormData)
      this.fnViewInsertTacsFcltMst()
      this.fnType = model.fnType
      if (this.fnType === 'update') {
        this.formData = this._cloneDeep(model.row)
      }
    },
    onClose() { },
    onChangeSsvcLineType(ssvcArr) {
      ssvcArr.forEach(v => { this.$set(this.formData, v.key, v.value) })
    },
    async fnViewInsertTacsFcltMst() {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewInsertTacsFcltMst, {})
        this.sfcltTypes = res.sfcltTypes
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertTacsFcltMst() {
      if (!this.fnCheckValidate('insert')) return
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, pipFcltInet, nportFclt, sfcltType, sfcltPromptNm, sfcltModelNm } = this.formData
      const params = {
        ssvcLineTypeCd,
        ssvcGroupCd,
        ssvcObjCd,
        pipFcltInet,
        nportFclt,
        sfcltType,
        sfcltPromptNm,
        sfcltModelNm
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.insertTacsFcltMst, params)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '조직 장비 등록이 정상적으로 처리되었습니다')
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
    async fnUpdateTacsFcltMst() {
      if (!this.fnCheckValidate('update')) return
      const { ntacsFcltMstSeq, nlvlBasSeq, ssvcLineTypeCd, pipFcltInet, nportFclt, sfcltType, sfcltPromptNm, sfcltModelNm } = this.formData
      const params = {
        nlvlBasSeq,
        ssvcLineTypeCd,
        pipFcltInet,
        nportFclt,
        sfcltType,
        sfcltPromptNm,
        sfcltModelNm
      }
      if (ntacsFcltMstSeq !== '' && ntacsFcltMstSeq !== null) {
        Object.assign(params, { ntacsFcltMstSeq })
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.updateTacsFcltMst, params)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '조직 장비 수정이 정상적으로 처리되었습니다')
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
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, pipFcltInet, nportFclt, sfcltType, sfcltPromptNm, sfcltModelNm } = this.formData
      if (type === 'insert' && (ssvcLineTypeCd === null || ssvcLineTypeCd === '')) {
        onMessagePopup(this, '서비스망을 선택하세요')
        return false
      }
      if (type === 'insert' && (ssvcGroupCd === null || ssvcGroupCd === '')) {
        onMessagePopup(this, '본부를 선택하세요')
        return false
      }
      if (type === 'insert' && (ssvcObjCd === null || ssvcObjCd === '')) {
        onMessagePopup(this, '노드를 선택하세요')
        return false
      }
      if (pipFcltInet === null || pipFcltInet === '') {
        onMessagePopup(this, '장비IP를 입력하세요')
        return false
      }
      if (sfcltType === null || sfcltType === '') {
        onMessagePopup(this, '장비타입을 선택하세요')
        return false
      }
      if (nportFclt === null || nportFclt === '') {
        onMessagePopup(this, '장비PORT을 선택하세요')
        return false
      }
      if (sfcltPromptNm === null || sfcltPromptNm === '') {
        onMessagePopup(this, '장비프롬프트명을 입력하세요')
        return false
      }
      if (sfcltModelNm === null || sfcltModelNm === '') {
        onMessagePopup(this, '장비모델명을 입력하세요')
        return false
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
