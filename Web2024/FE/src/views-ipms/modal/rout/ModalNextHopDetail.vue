<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="spageType !== 'link' ? 'Nexthop 상세 정보' : '링크 상세 정보'"
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
    <!-- 시설/회선 정보 -->
    <div v-if="tbIpInfoVo.ssubscnealiasType === 'SE' && spageType === 'link'" class="popupContentTable">
      <div class="popupContentTableTitle">시설/회선 정보</div>
      <table>
        <colgroup>
          <col width="13%" /><col width="20%" />
          <col width="13%" /><col width="20%" />
        </colgroup>
        <thead>
          <tr>
            <th colspan="2">시설 정보</th>
            <th colspan="2">회선 정보</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th>본부</th>
            <td>{{ tbIpInfoVo.ssvcGroupNm }}</td>
            <th>서비스대분류</th>
            <td>{{ tbIpInfoVo.sssvcMgroupNm }}</td>
          </tr>
          <tr>
            <th>노드국</th>
            <td>{{ tbIpInfoVo.ssvcObjNm }}</td>
            <th>상품명</th>
            <td>{{ tbIpInfoVo.ssvcLgroupNm }}</td>
          </tr>
          <tr>
            <th>수용국</th>
            <td>{{ tbIpInfoVo.sofficename }}</td>
            <th>사업용여부</th>
            <td>{{ tbIpInfoVo.ssvcUseTypeNm }}</td>
          </tr>
          <tr>
            <th>장비명</th>
            <td>{{ tbIpInfoVo.ssubscnealias }}</td>
            <th>전용번호</th>
            <td>{{ tbIpInfoVo.sllnum }}</td>
          </tr>
          <tr>
            <th>모델명</th>
            <td>{{ tbIpInfoVo.smodelname }}</td>
            <th>SAID</th>
            <td>{{ tbIpInfoVo.ssaid }}</td>
          </tr>
          <tr>
            <th></th>
            <td></td>
            <th>고객명</th>
            <td>{{ tbIpInfoVo.scustName }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 시설 정보 -->
    <div v-if="tbIpInfoVo.ssubscnealiasType !== 'SE' && spageType !== 'link'" class="popupContentTable">
      <div class="popupContentTableTitle">시설 정보</div>
      <table>
        <colgroup>
          <col width="13%" /><col width="20%" />
        </colgroup>
        <thead>
          <tr>
            <th colspan="2">시설 정보</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th>본부</th>
            <td>{{ tbIpInfoVo.ssvcGroupNm }}</td>
          </tr>
          <tr>
            <th>노드국</th>
            <td>{{ tbIpInfoVo.ssvcObjNm }}</td>
          </tr>
          <tr>
            <th>수용국</th>
            <td>{{ tbIpInfoVo.sofficename }}</td>
          </tr>
          <tr>
            <th>장비명</th>
            <td>{{ tbIpInfoVo.ssubscnealias }}</td>
          </tr>
          <tr>
            <th>모델명</th>
            <td>{{ tbIpInfoVo.smodelname }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 링크 정보 -->
    <div v-if="spageType === 'link'" class="popupContentTable">
      <div class="popupContentTableTitle">링크 정보</div>
      <table>
        <colgroup>
          <col width="13%" /><col width="20%" />
          <col width="13%" /><col width="20%" />
        </colgroup>
        <thead>
          <tr>
            <th colspan="2">자국 링크 정보</th>
            <th colspan="2">대국 링크 정보</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th>장비명</th>
            <td>{{ tbIpInfoVo.sanealias }}</td>
            <th>장비명</th>
            <td>{{ tbIpInfoVo.sznealias }}</td>
          </tr>
          <tr>
            <th>장비IP</th>
            <td>{{ tbIpInfoVo.samstip }}</td>
            <th>장비IP</th>
            <td>{{ tbIpInfoVo.szmstip }}</td>
          </tr>
          <tr>
            <th>IF명</th>
            <td>{{ tbIpInfoVo.saifname }}</td>
            <th>IF명</th>
            <td>{{ tbIpInfoVo.szifname }}</td>
          </tr>
          <tr>
            <th>수용국명</th>
            <td>{{ tbIpInfoVo.saofficescodeNm }}</td>
            <th>수용국명</th>
            <td>{{ tbIpInfoVo.szofficescodeNm }}</td>
          </tr>
          <tr>
            <th>SAID</th>
            <td colspan="3">{{ tbIpInfoVo.ssaid }}</td>
          </tr>
          <tr>
            <th>전용번호</th>
            <td colspan="3">{{ tbIpInfoVo.sllnum }}</td>
          </tr>
          <tr>
            <th>수용회선명</th>
            <td colspan="3">{{ tbIpInfoVo.sconnAlias }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="mini" class="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalNextHopDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      spageType: '',
      ipInfoVo: {},
      tbIpInfoVo: {
        ssubscnealiasType: '',
        ssvcGroupNm: '', // 본부
        sssvcMgroupNm: '', // 서비스대분류
        ssvcObjNm: '', // 노드국
        ssvcLgroupNm: '', // 상품명
        sofficename: '', // 수용국
        ssvcUseTypeNm: '', // 사업용여부
        ssubscnealias: '', // 장비명
        sllnum: '', // 전용번호
        smodelname: '', // 모델명
        ssaid: '', // SAID
        scustName: '', // 고객명
        sanealias: '', // 자국 링크 장비명
        sznealias: '', // 대국 링크 장비명
        samstip: '', // 자국 링크 장비IP
        szmstip: '', // 대국 링크 장비IP
        saifname: '', // 자국 링크 IF명
        szifname: '', // 대국 링크 IF명
        saofficescodeNm: '', // 자국 링크 수용국명
        szofficescodeNm: '', // 대국 링크 수용국명
        sconnAlias: '', // 수용회선명
      }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      // this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.spageType) {
        this.spageType = model.spageType
      }
      if (model.row) {
        this.ipInfoVo = model.row
        this.fnViewDetailNexthop()
      }
    },
    onClose() {
    },
    async fnViewDetailNexthop() {
      const { sipNexthop, ssvcLineTypeCd, proutingIpPrefix, nroutingIpBitmask } = this.ipInfoVo
      const ipInfoVo = { sipNexthop, ssvcLineTypeCd, proutingIpPrefix, nroutingIpBitmask, spageType: this.spageType }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailNextHop, ipInfoVo)
        this.tbIpInfoVo = res.result.data
      } catch (error) {
        this.error(error)
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
