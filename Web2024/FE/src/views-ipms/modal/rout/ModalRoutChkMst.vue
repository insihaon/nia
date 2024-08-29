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
        라우팅 수집/DB 비교 시작
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result" style="margin-top: 0px;">
          <div style="padding-bottom: 10px;">
            <span style="font-size: 20px; font-weight: bold;">
              아래 [시작] 버튼을 클릭하시고 창을 닫지 말고 잠시 기다려 주십시오.(1~3분 소요)
            </span>
          </div>
          <h4>
            [ {{ searchVo.ssvcLineTypeNm }} - {{ searchVo.ssvcGroupNm }} - {{ searchVo.ssvcObjNm }} ] 라우팅 수집/DB 비교 시작
          </h4>
          <table class="tbl_list mt5">
            <caption>조직별 장비별 명령어</caption>
            <colgroup>
              <col width="20%" />
              <col width="10%" />
              <col width="70%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">장비타입</th>
                <th scope="col">명령어순서</th>
                <th scope="col">장비명령어</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="tbFcltCmdMstVos.length === 0" class="subbg last">
                <td class="first" colspan="5">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <template v-else>
                <tr
                  v-for="(item, index) in tbFcltCmdMstVos"
                  :key="index"
                >
                  <td class="first" :title="item.sfcltType">{{ item.sfcltType }}</td>
                  <td class="first" :title="item.npriority">{{ item.npriority }}</td>
                  <td class="first" :title="item.sfcltCmd">{{ item.sfcltCmd }}</td>
                </tr>
              </template>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="fnInsertListRoutChkMst">시작</el-button>
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
      searchVo: {
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
      this.domElement.maxWidth = 1500
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
        const res = await apiRequestModel(ipmsModelApis.viewPopRoutChkMst, param)
        this.tbFcltCmdMstVos = res.result.data ?? []
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertListRoutChkMst() {
      if (this.tbFcltCmdMstVos.length === 0) {
        onMessagePopup(this, '조직별 장비 명령어가 존재하지 않습니다. 관리자에게 문의하세요')
        return
      }
      const tbRoutChkMstVo = this.ssvcCds
      try {
        const res = await apiRequestJson(ipmsJsonApis.routInsertListRoutChkMst, tbRoutChkMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '라우팅 수집/DB 비교가 정상적으로 처리되었습니다.')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
