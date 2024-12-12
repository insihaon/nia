<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="`${isViewWire ? '유선' : '무선'} IP Community ${fnType === 'insert' ? '등록' : '수정'}`"
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
      <table v-if="isViewWire && fnType === 'insert'">
        <colgroup>
          <col width="20%" />
          <col width="80%" />
        </colgroup>
        <tr>
          <SsvcLineType
            ref="ssvcLineType"
            class="SsvcLineType"
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
        </tr>
      </table>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tbody>
          <tr v-if="isViewWire && fnType === 'update'">
            <th>계위</th>
            <td>
              {{ formData.ssvcLineTypeNm }} - {{ formData.ssvcGroupNm }} - {{ formData.ssvcObjNm }}
            </td>
          </tr>
          <tr>
            <th>구분</th>
            <td colspan="3">
              <el-select v-model="formData.skindCd" class="w-30" :disabled="fnType === 'update'">
                <el-option v-for="item in skindCdOptions" :key="item.value" :value="item.value" :label="item.label" />
              </el-select>
            </td>
          </tr>
          <tr>
            <th>Community</th>
            <td>
              <el-input
                v-model="formData.scommunity"
                type="text"
                maxlength="300"
                :disabled="(isViewWire && formData.skindCd !== 'KORNET') || (!isViewWire && formData.skindCd !== 'COMMU')"
              />
            </td>
            <template v-if="isViewWire">
              <th>Nexthop</th>
              <td>
                <el-input v-model="formData.snexthop" type="text" maxlength="250" :disabled="formData.skindCd !== 'PREMIUM'" />
              </td>
            </template>
            <template v-else>
              <th>IP블록</th>
              <td>
                <el-input v-model="formData.pipPrefix" type="text" maxlength="250" :disabled="formData.skindCd !== 'NOCOMMU'" />
              </td>
            </template>
          </tr>
          <tr v-if="!isViewWire">
            <th>서비스</th>
            <td colspan="3">
              <el-input v-model="formData.sserviceNm" type="text" class="txt w-100" maxlength="250" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button v-if="fnType === 'insert'" icon="el-icon-document-add" type="primary" size="small" round @click="fnInsertWireMst">등록</el-button>
      <el-button v-if="fnType === 'update'" icon="el-icon-edit" type="primary" size="small" round @click="fnUpdateWireMst">수정</el-button>
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

const routeName = 'ModalWireMstInsert'

export default {
  name: routeName,
  components: { SsvcLineType },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: 'wire',
      fnType: 'insert',
      svcLineListVo: {
        tbLvlBasVos: [], // Placeholder for your items data
      },
      defaultFormData: {
        ssvcLineTypeCd: '',
        ssvcGroupCd: '',
        ssvcObjCd: '',
        skindCd: '',
        scommunity: '',
        snexthop: '',
        sserviceNm: '',
        pipPrefix: ''
      },
      formData: { },
    }
  },
  computed: {
    isViewWire() {
      return this.viewType === 'wire'
    },
    skindCdOptions() {
      if (this.viewType === 'wire') {
        return [
          { label: '전체', value: '' },
          { label: 'Community', value: 'KORNET' },
          { label: 'Nexthop', value: 'PREMIUM' },
        ]
      } else if (this.viewType === 'mobile') {
        return [
          { label: '전체', value: '' },
          { label: 'Community', value: 'COMMU' },
          { label: 'No-Community', value: 'NOCOMMU' },
        ]
      } else {
        return []
      }
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
      if (this.fnType === 'update') {
        this.formData = this._cloneDeep(model.row)
        if (this.viewType === 'mobile') {
          this.formData['scommunity'] = model.row.scommu
        }
      }
    },
    onClose() { },
    onChangeSsvcLineType(ssvcArr) {
      ssvcArr.forEach(v => { this.$set(this.formData, v.key, v.value) })
    },
    getInsertParam() {
      let params = {}
      if (this.isViewWire) {
        const { skindCd, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, scommunity, snexthop } = this.formData
        params = { skindCd, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd }
        Object.assign(params, { [skindCd === 'KORNET' ? 'scommunity' : 'snexthop']: skindCd === 'KORNET' ? scommunity : snexthop })
      } else { // wireless
        const { skindCd, scommunity, sserviceNm, pipPrefix } = this.formData
        const skindNm = this.skindCdOptions.find(item => item.value === skindCd).label
        params = { skindCd, scommu: scommunity, sserviceNm, skindNm }
        Object.assign(params, { [skindCd === 'COMMU' ? 'scommu' : 'pipPrefix']: skindCd === 'COMMU' ? scommunity : pipPrefix })
      }
      return params
    },
    getUpdateParam() {
      let params = {}
      if (this.isViewWire) {
        const { nwireIpCommuSeq, skindCd, scommunity, snexthop } = this.formData
        params = { nwireIpCommuSeq, skindCd }
        Object.assign(params, { [skindCd === 'KORNET' ? 'scommunity' : 'snexthop']: skindCd === 'KORNET' ? scommunity : snexthop })
      } else {
        const { nmobileIpCommuSeq, skindCd, skindNm, scommunity, sserviceNm, pipPrefix } = this.formData
        params = { nmobileIpCommuSeq, skindCd, skindNm, sserviceNm }
        Object.assign(params, { [skindCd === 'COMMU' ? 'scommu' : 'pipPrefix']: skindCd === 'COMMU' ? scommunity : pipPrefix })
      }
      return params
    },
    async fnInsertWireMst() {
      if (!this.fnCheckValidate()) return
      try {
        const apiKey = this.isViewWire ? 'insertWireMst' : 'insertMobileMst'
        const res = await apiRequestJson(ipmsJsonApis[apiKey], this.getInsertParam())
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, `${this.isViewWire ? '유선' : '무선'}IP 사전 정보 등록이 정상적으로 처리되었습니다`)
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
    async fnUpdateWireMst() {
      if (!this.fnCheckValidate()) return
      try {
        const apiKey = this.isViewWire ? 'updateWireMst' : 'updateMobileMst'
        const res = await apiRequestJson(ipmsJsonApis[apiKey], this.getUpdateParam())
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, `${this.isViewWire ? '유선' : '무선'}IP 사전 정보 수정이 정상적으로 처리되었습니다`)
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
    fnCheckValidate() {
      const { skindCd, scommunity, snexthop } = this.formData
      if (skindCd === '' || skindCd === null) {
        onMessagePopup(this, '구분을 선택하세요')
        return false
      }
      if (skindCd === 'KORNET' && (scommunity === '' || scommunity === null)) {
        onMessagePopup(this, 'community을 입력하세요')
        return false
      }
      if (skindCd === 'PREMIUM' && (snexthop === '' || snexthop === null)) {
        onMessagePopup(this, 'Nexthop을 입력하세요')
        return false
      }
      return true
    }
  }
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
