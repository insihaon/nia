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
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        연동 정보 {{ viewType === 'detail' ? '상세' : '수정' }}
        <hr>
      </span>
      <div id="content" class="layer">
        <table class="tbl_data entry">
          <caption></caption>
          <colgroup>
            <col width="13%" />
            <col width="34%" />
            <col width="13%" />
            <col width="12%" />
            <col width="13%" />
            <col width="15%" />
          </colgroup>
          <tbody>
            <tr class="top">
              <th class="first">인터페이스 ID</th>
              <td colspan="3" class="view">{{ resultVo.sifId }}</td>
              <th>시스템명</th>
              <td class="view">{{ resultVo.ssystemNm }}</td>
            </tr>
            <tr>
              <th class="first">스크립트 명</th>
              <td colspan="3" class="view">{{ resultVo.sscriptNm }}</td>
              <th>제공 시스템</th>
              <td class="view">{{ resultVo.sproviderSys }}</td>
            </tr>
            <tr>
              <th class="first">테이블 이름</th>
              <td class="view">{{ resultVo.stableNm }}</td>
              <th>연동형태</th>
              <td class="view">{{ resultVo.linktype }}</td>
              <th>사용 여부</th>
              <td v-if="viewType === 'detail'">
                {{ resultVo.sopstate }}
              </td>
              <td v-if="viewType === 'update'">
                <select v-model="resultVo.sopstate">
                  <option value="U">사용</option>
                  <option value="N">미사용</option>
                </select>
              </td>
            </tr>
            <tr>
              <th class="first">연동 주기</th>
              <td v-if="viewType === 'detail'" colspan="5">
                <div id="cronBatch" class="cron">{{ resultVo.speriod }}</div>
              </td>
              <td v-if="viewType === 'update'" colspan="5">
                <div id="cronBatch" class="cron">
                  <CompCron ref="compCron" :speriod="resultVo.speriod" @updateCron="onUpdateCron" />
                </div>
              </td>
            </tr>
            <tr class="last">
              <th class="first">비고</th>
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
      <div slot="footer" class="dialog-footer">
        <el-button v-if="viewType === 'detail'" size="mini" class="el-icon-edit-outline" @click="viewType = 'update'">수정</el-button>
        <el-button v-else-if="viewType === 'update'" size="mini" class="el-icon-edit-outline" @click="fnUpdateTbBatchSvcBas">등록</el-button>
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
        if (res.tbBatchSvcBasVo.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '연동정보가 정상적으로 수정되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.tbBatchSvcBasVo.commonMsg)
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
