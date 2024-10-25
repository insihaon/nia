<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="'조직 장비 '+(fnType === 'insert' ? '등록': '수정')"
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
    <div class="popupContentTable">
      <table v-if="fnType === 'insert'">
        <colgroup>
          <col width="15%" />
          <col width="85%" />
        </colgroup>
        <tr>
          <SsvcLineType
            ref="ssvcLineType"
            class="SsvcLineType"
            :lvl="3"
            prop-colspan="3"
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
        </tr>
      </table>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tbody>
          <tr>
            <template v-if="fnType === 'update'">
              <th>계위</th>
              <td colspan="3">
                {{ formData.ssvcLineTypeNm }} - {{ formData.ssvcGroupNm }} - {{ formData.ssvcObjNm }}
              </td>
            </template>
          </tr>
          <tr>
            <th>장비타입</th>
            <td :colspan="viewType === 'tacsMng' ?'3': '1'">
              <el-select v-model="formData.sfcltType">
                <el-option value="">선택</el-option>
                <el-option
                  v-for="(item, index) in sfcltTypes"
                  :key="index"
                  :value="item.code"
                  :label="item.name"
                />
              </el-select>
            </td>
            <template v-if="viewType === 'ipRouting'">
              <th>사용여부</th>
              <td>
                <el-select v-model="formData.suseYn">
                  <el-option value="">선택</el-option>
                  <el-option
                    v-for="(item, index) in [{ code: 'Y', name: 'Y' }, { code: 'N', name: 'N' }]"
                    :key="index"
                    :value="item.code"
                    :label="item.name"
                  />
                </el-select>
              </td>
            </template>
          </tr>
          <tr>
            <th>장비IP</th>
            <td>
              <el-input v-model="formData.pipFcltInet" type="text" maxlength="43" />
            </td>
            <th>장비PORT</th>
            <td>
              <el-select v-model="formData.nportFclt">
                <el-option value="">선택</el-option>
                <el-option :value="23" label="TELNET(23)" />
                <el-option :value="22" label="SSH(22)" />
              </el-select>
            </td>
          </tr>
          <tr v-if="viewType === 'ipRouting'">
            <th>상위장비IP</th>
            <td>
              <el-input v-model="formData.pipUpperFclt" type="text" maxlength="43" />
            </td>
            <th>PEER장비IP</th>
            <td>
              <el-input v-model="formData.pipPeerFclt" type="text" maxlength="43" />
            </td>
          </tr>
          <tr>
            <th>장비프롬프트명</th>
            <td>
              <el-input v-model="formData.sfcltPromptNm" type="text" />
            </td>
            <th>장비모델명</th>
            <td>
              <el-input v-model="formData.sfcltModelNm" type="text" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button v-if="fnType === 'insert'" icon="el-icon-document-add" type="primary" size="small" round @click="fnInsertFcltMst">등록</el-button>
      <el-button v-if="fnType === 'update'" icon="el-icon-edit" type="primary" size="small" round @click="fnUpdateFcltMst">수정</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
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
      viewType: 'tacsMng', // tacsMng or ipRouting
      fnType: 'insert',
      defaultFormData: {
        ssvcLineTypeCd: '',
        ssvcGroupCd: '',
        ssvcObjCd: '',
        pipFcltInet: '',
        pipUpperFclt: '',
        pipPeerFclt: '',
        nportFclt: '',
        sfcltType: '',
        sfcltPromptNm: '',
        sfcltModelNm: '',
        suseYn: ''
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
      this.viewType = model.viewType
      this.fnType = model.fnType
      this.fnViewInsertTacsFcltMst()
      if (this.fnType === 'update') {
        this.formData = this._cloneDeep(model.row)
        if (this.viewType === 'ipRouting') {
          this.formData['pipFcltInet'] = model.row.ptelnetIp
          this.formData['sfcltPromptNm'] = model.row.shostNm
        }
      }
    },
    onClose() { },
    onChangeSsvcLineType(ssvcArr) {
      ssvcArr.forEach(v => { this.$set(this.formData, v.key, v.value) })
    },
    async fnViewInsertTacsFcltMst() {
      try {
        const apiStr = this.viewType === 'ipRouting' ? 'viewInsertFcltMst' : 'viewInsertTacsFcltMst'
        const res = await apiRequestModel(ipmsModelApis[apiStr], {})
        this.sfcltTypes = res.sfcltTypes
      } catch (error) {
        this.error(error)
      }
    },
    getParameterByType() {
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, pipFcltInet, nportFclt, sfcltType, sfcltPromptNm, sfcltModelNm, suseYn, pipUpperFclt, pipPeerFclt } = this.formData
      const params = {
        [this.viewType === 'tacsMng' ? 'pipFcltInet' : 'ptelnetIp']: pipFcltInet,
        nportFclt,
        sfcltType,
        [this.viewType === 'tacsMng' ? 'sfcltPromptNm' : 'shostNm']: sfcltPromptNm,
        sfcltModelNm
      }
      if (this.viewType === 'ipRouting') {
        Object.assign(params, { pipUpperFclt, pipPeerFclt, suseYn })
      }
      if (this.fnType === 'insert') {
        Object.assign(params, { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd })
      }
      if (this.fnType === 'update') {
        if (this.formData.ntacsFcltMstSeq !== '' && this.formData.ntacsFcltMstSeq !== null) {
          Object.assign(params, { ntacsFcltMstSeq: this.formData.ntacsFcltMstSeq })
        }
        if (this.formData.nfcltMstSeq !== '' && this.formData.nfcltMstSeq !== null) {
          Object.assign(params, { nfcltMstSeq: this.formData.nfcltMstSeq })
        }
        Object.assign(params, { nlvlBasSeq: this.formData.nlvlBasSeq })
      }
      return params
    },
    async fnInsertFcltMst() {
      if (!this.fnCheckValidate('insert')) return
      try {
        const apiStr = this.viewType === 'ipRouting' ? 'insertFcltMst' : 'insertTacsFcltMst'
        const res = await apiRequestJson(ipmsJsonApis[apiStr], this.getParameterByType())
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
    async fnUpdateFcltMst() {
      if (!this.fnCheckValidate('update')) return
      try {
        const apiStr = this.viewType === 'ipRouting' ? 'updateFcltMst' : 'updateTacsFcltMst'
        const res = await apiRequestJson(ipmsJsonApis[apiStr], this.getParameterByType())
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
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, pipFcltInet, nportFclt, sfcltType, sfcltPromptNm, sfcltModelNm, suseYn, pipUpperFclt, pipPeerFclt } = this.formData
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
      if (sfcltType === null || sfcltType === '') {
        onMessagePopup(this, '장비타입을 선택하세요')
        return false
      }
      if (this.viewType === 'ipRouting' && (suseYn === null || suseYn === '')) {
        onMessagePopup(this, '사용여부를 선택하세요')
        return false
      }
      if (pipFcltInet === null || pipFcltInet === '') {
        onMessagePopup(this, '장비IP를 입력하세요')
        return false
      }
      if (nportFclt === null || nportFclt === '') {
        onMessagePopup(this, '장비PORT을 선택하세요')
        return false
      }
      if (this.viewType === 'ipRouting') {
        if (pipUpperFclt === null || pipUpperFclt === '') {
          onMessagePopup(this, '상위장비IP를 입력하세요')
          return false
        }
        if (pipPeerFclt === null || pipPeerFclt === '') {
          onMessagePopup(this, 'PEER장비IP를 입력하세요')
          return false
        }
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
