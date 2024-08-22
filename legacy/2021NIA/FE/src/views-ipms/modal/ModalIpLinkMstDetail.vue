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
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        운용정보 상세정보
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <div class="tit_group">
            <h4 class="mt5">상세정보</h4>
          </div>
          <table class="tbl_data mt5" summary="링크 정보">
            <caption>링크 정보</caption>
            <colgroup>
              <col width="20%" />
              <col width="27%" />
              <col width="23%" />
              <col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">링크IP블록</th>
                <td colspan="3">{{ resultVo.pifSerialIp }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 수용국</th>
                <td>{{ resultVo.saofficescodeNm }}</td>
                <th scope="row">대국 수용국</th>
                <td>{{ resultVo.szofficescodeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 장비명</th>
                <td>{{ resultVo.sanealias }}</td>
                <th scope="row">대국 장비명</th>
                <td>{{ resultVo.sznealias }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 장비IP</th>
                <td>{{ resultVo.samstip }}</td>
                <th scope="row">대국 장비IP</th>
                <td>{{ resultVo.szmstip }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">자국 IF명</th>
                <td>{{ resultVo.saifname }}</td>
                <th scope="row">대국 IF명</th>
                <td>{{ resultVo.szifname }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">SAID</th>
                <td>{{ resultVo.ssaid }}</td>
                <th class="first" scope="row">전용번호</th>
                <td>{{ resultVo.sllnum }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">수용회선명</th>
                <td colspan="3">{{ resultVo.sconnalias }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close">수정</el-button> <!-- btnUpdateLinkInfo -->
        <el-button size="mini" class="el-icon-close">삭제</el-button> <!-- btnDeleteLinkInfo -->
        <el-button size="mini" class="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
      </div>
    </el-dialog>
    <ModelWhoInfoDetail ref="ModelWhoInfoDetail" />
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'
import ModelWhoInfoDetail from '@/views-ipms/modal/whois/ModelWhoInfoDetail.vue'

import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalIpLinkMstDetail'

export default {
  name: routeName,
  components: { ModelWhoInfoDetail },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      defaultResultVo: {
        pifSerialIp: '',
        saofficescodeNm: '',
        szofficescodeNm: '',
        sanealias: '',
        sznealias: '',
        samstip: '',
        szmstip: '',
        saifname: '',
        szifname: '',
        ssaid: '',
        sllnum: '',
        sconnalias: '',
        allocCnt: ''
      },
      resultVo: {}
    }
  },
  mounted () {
    this.tbIpInfoVo = this._cloneDeep(this.defaultTbIpInfoVo)
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
      if (model?.nipLinkMstSeq) {
        this.fnViewDetailIPLinkMst(model.nipLinkMstSeq)
      }
    },
    onClose() {
      this.resultVo = this._cloneDeep(this.defaultResultVo)
    },
    async fnViewDetailIPLinkMst(nipLinkMstSeq) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailIPLinkMst, { nipLinkMstSeq })
        this.resultVo = res.result.data
      } catch (error) {
       this.error(error)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
