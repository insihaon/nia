<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="회선 상세정보"
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
    <!-- 회선 정보 -->
    <div class="popupContentTable">
      <div class="popupContentTableTitle">회선 정보</div>
      <table>
        <colgroup>
          <col width="14%" />
          <col width="20%" />
          <col width="13%" />
          <col width="20%" />
          <col width="13%" />
          <col width="20%" />
        </colgroup>
        <tbody>
          <tr>
            <th>고객명</th>
            <td>{{ resultVo.scustname }}</td>
            <th>전용번호</th>
            <td>{{ resultVo.sllnum }}</td>
            <th>SAID</th>
            <td>{{ resultVo.ssaid }}</td>
          </tr>
          <tr>
            <th>상품명</th>
            <td>{{ resultVo.sipmsSvcNm }}</td>
            <th>수용국</th>
            <td>{{ resultVo.sofficename }}</td>
            <th>KT 사업용여부</th>
            <td>{{ resultVo.ssvcUseTypeNm }}</td>
          </tr>
          <tr>
            <th>설치장소(지번)</th>
            <td colspan="5">{{ resultVo.sinstalladdress }}</td>
          </tr>
          <tr>
            <th>설치장소(도로명)</th>
            <td colspan="5">{{ resultVo.sinstallroadaddress }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 청약 정보 -->
    <div class="popupContentTable">
      <div class="popupContentTableTitle">청약 정보</div>
      <table>
        <colgroup>
          <col width="14%" />
          <col width="20%" />
          <col width="13%" />
          <col width="20%" />
          <col width="13%" />
          <col width="20%" />
        </colgroup>
        <tbody>
          <tr>
            <th>오더번호</th>
            <td>{{ resultVo.sordernum }}</td>
            <th>희망일</th>
            <td>{{ resultVo.sodrhopedt }}</td>
            <th>접수일</th>
            <td>{{ resultVo.sodrregdt }}</td>
          </tr>
          <tr>
            <th>접수자 참고사항</th>
            <td colspan="5">{{ resultVo.sreportOpinion }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 물리시설 정보 -->
    <div class="popupContentTable">
      <div class="popupContentTableTitle">물리시설 정보</div>
      <table>
        <colgroup>
          <col width="20%" />
          <col width="27%" />
          <col width="23%" />
          <col width="30%" />
        </colgroup>
        <tbody>
          <tr>
            <th>시설명</th>
            <td>{{ resultVo.ssubscnealias }}</td>
            <th>I/F명</th>
            <td>{{ resultVo.ssubsclgipportdescription }}</td>
          </tr>
          <tr>
            <th>장비대표 IP</th>
            <td>{{ resultVo.ssubscmstip }}</td>
            <th>대국 인터페이스 IP</th>
            <td>{{ resultVo.ssubscrouterserialip }}</td>
          </tr>
          <tr>
            <th>자국 인터페이스 IP</th>
            <td>{{ resultVo.ssubsclgipportip }}</td>
            <th>수용회선명</th>
            <td>{{ resultVo.sconnalias }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" ize="small" icon="el-icon-close" round @click="close()">
        닫기
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalIpAllocCircuitDetail'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {}
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.fnViewDetailSubSvcMst(model.ipBlockMstVo)
    },
    onClose() {
      this.$emit('selected-value', { selectedRow: this.resultVo, returnFlag: 'allocTel' })
      this.$emit('closeCircuitDetail')
    },
    async fnViewDetailSubSvcMst(ipBlockMstVo = null) {
      if (ipBlockMstVo === null) return
     let params
     if (ipBlockMstVo?.nipAllocMstSeq || ipBlockMstVo?.nipLinkMstSeq) { /* case 1. 할당상세pop */
      const { nipAllocMstSeq, nipLinkMstSeq } = ipBlockMstVo
      params = { nipAllocMstSeq, nipLinkMstSeq }
     } else { /* case 2. 할당처리pop > 회선 정보 */
      const { sllnum, ssaid, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd } = ipBlockMstVo
      params = { sllnum, ssaid, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd }
     }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailSubSvcMst, params)
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
