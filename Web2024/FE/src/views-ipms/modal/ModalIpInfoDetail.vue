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
        IP블록 상세 정보
        <hr>
      </span>
      <div id="content" class="layer">
        <div class="content_result mt0">
          <div class="tit_group">
            <h4 class="mt5">IP 블록 정보</h4>
            <div class="btn_area">
              <span v-if="type === 'Alloc'">
                <el-button
                  size="mini"
                  type="primary"
                  class="mb-1"
                  @click="fnViewDetailWhois()"
                >
                  WHOIS
                </el-button>
              </span>
            </div>
          </div>
          <table class="tbl_list mt5" summary="조회결과">
            <caption>조회결과</caption>
            <colgroup>
              <col width="13%" /><col width="21%" />
              <col width="13%" /><col width="20%" />
              <col width="13%" /><col width="20%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col" colspan="2">IP 정보</th>
                <th scope="col" colspan="2">시설 정보</th>
                <th scope="col" colspan="2">회선  정보</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th class="first" scope="row">시작 IP</th>
                <td>{{ tbIpInfoVo.sfirstAddr }}</td>
                <th scope="row">본부</th>
                <td>{{ tbIpInfoVo.ssvcGroupNm }}</td>
                <th scope="row">서비스대분류</th>
                <td>{{ tbIpInfoVo.sssvcMgroupNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">끝 IP</th>
                <td>{{ tbIpInfoVo.slastAddr }}</td>
                <th scope="row">노드국</th>
                <td>{{ tbIpInfoVo.ssvcObjNm }}</td>
                <th scope="row">상품명</th>
                <td>{{ tbIpInfoVo.ssvcLgroupNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">BitMask</th>
                <td>{{ tbIpInfoVo.nbitmask }}</td>
                <th scope="row">수용국</th>
                <td>{{ tbIpInfoVo.sicisofficesname }}</td>
                <th scope="row">사업용여부</th>
                <td>{{ tbIpInfoVo.ssvcUseTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">공인/사설</th>
                <td>{{ tbIpInfoVo.sipCreateTypeNm }}</td>
                <th scope="row">장비명</th>
                <td>{{ tbIpInfoVo.ssubscnealias }}</td>
                <th scope="row">서비스</th>
                <td>{{ tbIpInfoVo.sassignTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">서비스망</th>
                <td>{{ tbIpInfoVo.ssvcLineTypeNm }}</td>
                <th scope="row">모델명</th>
                <td>{{ tbIpInfoVo.smodelname }}</td>
                <th scope="row">전용번호</th>
                <td>{{ tbIpInfoVo.sllnum }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">할당상태</th>
                <td>{{ tbIpInfoVo.sassignLevelNm }}</td>
                <th scope="row">I/F명</th>
                <td>{{ tbIpInfoVo.ssubsclgipportdescription }}</td>
                <th scope="row">SAID</th>
                <td>{{ tbIpInfoVo.ssaid }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">감사상태</th>
                <td>{{ tbIpInfoVo.svalidCheck }}</td>
                <th scope="row">수용회선명</th>
                <td>{{ tbIpInfoVo.sconnAlias }}</td>
                <th scope="row">고객명</th>
                <td>{{ tbIpInfoVo.scustName }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td class="left" colspan="5">{{ tbIpInfoVo.scomment }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
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
import ModelWhoInfoDetail from '@/views-ipms/modal/whois/ModelWhoInfoDetail'

import { apiModel } from '@/api/ipms'

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
      type: '',
      tbIpInfoVo: {
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
      }
    }
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
        this.close()
      }
    },
    onClose() {
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
        const res = await apiModel('/linkmgmt/socketmgmt/viewDetailWhois', searchVo)
        if (res.result.resultVo.commonMsg === 'SUCCESS') {
          this.$refs.ModelWhoInfoDetail.open({ response: res })
        } else {
          onMessagePopup(this, res?.result?.resultVo?.commonMsg || '')
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
