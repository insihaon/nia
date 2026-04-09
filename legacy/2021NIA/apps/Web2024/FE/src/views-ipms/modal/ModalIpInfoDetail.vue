<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP블록 상세 정보"
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
    <div v-loading="vLoading" class="popupContentTable">
      <div class="popupContentTableTitle">IP 블록 정보
        <el-button
          v-if="type === 'Aloc'"
          size="mini"
          type="primary"
          style="position: absolute;right: 25px;margin-top:7px"
          round
          plain
          @click="fnViewDetailWhois()"
        >
          WHOIS
        </el-button>
      </div>
      <table>
        <colgroup>
          <col width="13%" /><col width="21%" />
          <col width="13%" /><col width="20%" />
          <col width="13%" /><col width="20%" />
        </colgroup>
        <thead>
          <tr>
            <th colspan="2">IP 정보</th>
            <th colspan="2">시설 정보</th>
            <th colspan="2">회선  정보</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th>시작 IP</th>
            <td>{{ tbIpInfoVo.sfirstAddr }}</td>
            <th>본부</th>
            <td>{{ tbIpInfoVo.ssvcGroupNm }}</td>
            <th>서비스대분류</th>
            <td>{{ tbIpInfoVo.sssvcMgroupNm }}</td>
          </tr>
          <tr>
            <th>끝 IP</th>
            <td>{{ tbIpInfoVo.slastAddr }}</td>
            <th>노드국</th>
            <td>{{ tbIpInfoVo.ssvcObjNm }}</td>
            <th>상품명</th>
            <td>{{ tbIpInfoVo.ssvcLgroupNm }}</td>
          </tr>
          <tr>
            <th>BitMask</th>
            <td>{{ tbIpInfoVo.nbitmask }}</td>
            <th>수용국</th>
            <td>{{ tbIpInfoVo.sicisofficesname }}</td>
            <th>사업용여부</th>
            <td>{{ tbIpInfoVo.ssvcUseTypeNm }}</td>
          </tr>
          <tr>
            <th>공인/사설</th>
            <td>{{ tbIpInfoVo.sipCreateTypeNm }}</td>
            <th>장비명</th>
            <td>{{ tbIpInfoVo.ssubscnealias }}</td>
            <th>서비스</th>
            <td>{{ tbIpInfoVo.sassignTypeNm }}</td>
          </tr>
          <tr>
            <th>서비스망</th>
            <td>{{ tbIpInfoVo.ssvcLineTypeNm }}</td>
            <th>모델명</th>
            <td>{{ tbIpInfoVo.smodelname }}</td>
            <th>전용번호</th>
            <td>{{ tbIpInfoVo.sllnum }}</td>
          </tr>
          <tr>
            <th>할당상태</th>
            <td>{{ tbIpInfoVo.sassignLevelNm }}</td>
            <th>I/F명</th>
            <td>{{ tbIpInfoVo.ssubsclgipportdescription }}</td>
            <th>SAID</th>
            <td>{{ tbIpInfoVo.ssaid }}</td>
          </tr>
          <tr>
            <th>감사상태</th>
            <td>{{ tbIpInfoVo.svalidCheck }}</td>
            <th>수용회선명</th>
            <td>{{ tbIpInfoVo.sconnAlias }}</td>
            <th>고객명</th>
            <td>{{ tbIpInfoVo.scustName }}</td>
          </tr>
          <tr>
            <th>비고</th>
            <td class="text-left" colspan="5">{{ tbIpInfoVo.scomment }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-close" size="small" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
    <ModelWhoInfoDetail ref="ModelWhoInfoDetail" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'
import ModelWhoInfoDetail from '@/views-ipms/modal/whois/ModelWhoInfoDetail.vue'

import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalIpInfoDetail'

export default {
  name: routeName,
  components: { ModelWhoInfoDetail },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      vLoading: false,
      type: '',
      defaultTbIpInfoVo: {
        sfirstAddr: '',
        ssvcGroupNm: '',
        sssvcMgroupNm: '',
        slastAddr: '',
        ssvcObjNm: '',
        ssvcLgroupNm: '',
        nbitmask: '',
        sicisofficesname: '',
        ssvcUseTypeNm: '',
        sipCreateTypeNm: '',
        ssubscnealias: '',
        sassignTypeNm: '',
        ssvcLineTypeNm: '',
        smodelname: '',
        sllnum: '',
        sassignLevelNm: '',
        ssubsclgipportdescription: '',
        ssaid: '',
        svalidCheck: '',
        sconnAlias: '',
        scustName: '',
        scomment: ''
      },
      tbIpInfoVo: {}
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
      if (model?.type) {
        this.type = model.type
      }
      if (model?.tbIpInfoVo) {
        this.tbIpInfoVo = model?.tbIpInfoVo
      } else {
        // this.close()
      }
    },
    onClose() {
      this.tbIpInfoVo = this._cloneDeep(this.defaultTbIpInfoVo)
    },
    async fnViewDetailWhois() {
      // Your logic for handling this function
      const { sipVersionTypeCd, pipPrefix, nipAssignMstSeq } = this.tbIpInfoVo
      let query = pipPrefix
      if (sipVersionTypeCd === 'CV0001') {
        query = pipPrefix.substring(0, pipPrefix.indexOf('/'))
      }
      const searchVo = { query, nipAssignMstSeq }
      try {
        this.vLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewDetailWhois, searchVo)
        if (res.result.resultVo.commonMsg === 'SUCCESS') {
          this.$refs.ModelWhoInfoDetail.open({ response: res })
        } else {
          onMessagePopup(this, res?.result?.resultVo?.commonMsg || '')
        }
      } catch (error) {
       this.error(error)
      } finally {
        this.vLoading = false
      }
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
