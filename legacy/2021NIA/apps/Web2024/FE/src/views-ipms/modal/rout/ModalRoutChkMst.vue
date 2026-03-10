<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="라우팅 수집/DB 비교 시작"
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
    <div class="popupContentTable textcenter">
      <div style="padding-bottom: 10px;">
        <span style="font-size: 20px; font-weight: bold;">
          아래 [시작] 버튼을 클릭하시고 창을 닫지 말고 잠시 기다려 주십시오.(1~3분 소요)
        </span>
      </div>
      <div class="popupContentTableTitle">[ {{ ssvcNms.ssvcLineTypeNm }} - {{ ssvcNms.ssvcGroupNm }} - {{ ssvcNms.ssvcObjNm }} ] 라우팅 수집/DB 비교 시작</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="10%" />
          <col width="70%" />
        </colgroup>
        <thead>
          <tr>
            <th>장비타입</th>
            <th>명령어순서</th>
            <th>장비명령어</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="tbFcltCmdMstVos.length === 0">
            <td colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
          </tr>
          <template v-else>
            <tr v-for="(item, index) in tbFcltCmdMstVos" :key="index">
              <td>{{ item.sfcltType }}</td>
              <td>{{ item.npriority }}</td>
              <td>{{ item.sfcltCmd }}</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-video-play" round @click="fnInsertListRoutChkMst">시작</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'

import { ipmsModelApis, ipmsJsonApis, apiRequestModel, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalRoutChkMst'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      ssvcCds: {
        ssvcLineTypeCd: '',
        ssvcGroupCd: '',
        ssvcObjCd: ''
      },
      ssvcNms: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: ''
      },
      tbFcltCmdMstVos: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1300
    },
    onOpen(model, actionMode) {
      if (model.ssvcCds) {
        this.ssvcCds = model.ssvcCds
      }
      if (model.ssvcNms) {
        this.ssvcNms = model.ssvcNms
      }
      this.fnViewPopRoutChkMst()
    },
    onClose() {
    },
    async fnViewPopRoutChkMst() {
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd } = this.ssvcCds
      const { ssvcLineTypeNm, ssvcGroupNm, ssvcObjNm } = this.ssvcNms
      const param = { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, ssvcLineTypeNm, ssvcGroupNm, ssvcObjNm }
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewPopRoutChkMst, param)
        this.tbFcltCmdMstVos = res.result.data ?? []
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    },
    async fnInsertListRoutChkMst() {
      if (this.tbFcltCmdMstVos.length === 0) {
        onMessagePopup(this, '조직별 장비 명령어가 존재하지 않습니다. 관리자에게 문의하세요')
        return
      }
      const tbRoutChkMstVo = this.ssvcCds
      try {
        this.loading = true
        this.$emit('reload')
        const res = await apiRequestJson(ipmsJsonApis.routInsertListRoutChkMst, tbRoutChkMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '라우팅 수집/DB 비교가 정상적으로 처리되었습니다.')
        } else {
          onMessagePopup(this, res.commonMsg)
        }
        this.close()
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
