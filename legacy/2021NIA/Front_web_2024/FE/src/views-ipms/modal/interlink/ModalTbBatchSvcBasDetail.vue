<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="'연동 정보'+(viewType === 'detail' ? '상세' : '수정')"
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
    <div class="popupContentTable">
      <table>
        <tbody>
          <tr>
            <th>인터페이스 ID</th>
            <td colspan="3">{{ resultVo.sifId }}</td>
            <th>시스템명</th>
            <td>{{ resultVo.ssystemNm }}</td>
          </tr>
          <tr>
            <th>스크립트 명</th>
            <td colspan="3">{{ resultVo.sscriptNm }}</td>
            <th>제공 시스템</th>
            <td>{{ resultVo.sproviderSys }}</td>
          </tr>
          <tr>
            <th>테이블 이름</th>
            <td>{{ resultVo.stableNm }}</td>
            <th>연동형태</th>
            <td>{{ resultVo.linktype }}</td>
            <th>사용 여부</th>
            <td v-if="viewType === 'detail'">
              {{ resultVo.sopstate }}
            </td>
            <td v-if="viewType === 'update'">
              <el-select v-model="resultVo.sopstate" size="small">
                <el-option value="U">사용</el-option>
                <el-option value="N">미사용</el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <th>연동 주기</th>
            <td v-if="viewType === 'detail'" colspan="5">
              <div>{{ resultVo.speriod }}</div>
            </td>
            <td v-if="viewType === 'update'" colspan="5">
              <div>
                <CompCron ref="compCron" :speriod="resultVo.speriod" @updateCron="onUpdateCron" />
              </div>
            </td>
          </tr>
          <tr class="last">
            <th>비고</th>
            <td v-if="viewType === 'detail'" colspan="5">
              {{ resultVo.scomment }}
            </td>
            <td v-if="viewType === 'update'" colspan="5">
              <textarea
                id="updateScomment"
                v-model="resultVo.scomment"
                class="w98"
                rows="3"
              ></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button v-if="viewType === 'detail'" type="primary" size="small" icon="el-icon-edit" round @click="viewType = 'update'">수정</el-button>
      <el-button v-else-if="viewType === 'update'" type="primary" size="small" icon="el-icon-document-add" round @click="fnUpdateTbBatchSvcBas">등록</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import CompCron from '@/views-ipms/menus/operInfoMng/linkMng/CompCron.vue'

const routeName = 'ModalTbBatchSvcBasDetail'

export default {
  name: routeName,
  components: { CompCron },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: 'detail',
      resultVo: {
        sscriptNm: '',
        sopstate: '',
        sifId: '',
        ssystemNm: '',
        sproviderSys: '',
        stableNm: '',
        speriod: '',
        linktype: '',
        scomment: '',
        batMstSeq: ''
      },
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.viewType = 'detail'
      if (model.row) {
        this.resultVo = model.row
      }
      // updateTbBatchSvcBas
    },
    onClose() { },
    async fnUpdateTbBatchSvcBas() {
      try {
        const { batMstSeq, sopstate, scomment, speriod } = this.resultVo
        const params = { batMstSeq, sopstate, scomment, speriod }
        const res = await apiRequestJson(ipmsJsonApis.updateTbBatchSvcBas, params)
        // URL: opermgmt/intgrmgmt/uploadMobileMst.ajax
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '연동정보가 정상적으로 수정되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    onUpdateCron(value) {
      this.$set(this.resultVo, 'speriod', value)
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
