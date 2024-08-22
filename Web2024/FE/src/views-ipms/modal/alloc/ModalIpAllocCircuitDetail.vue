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
        회선 상세정보
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <!-- 회선 정보 -->
        <div class="content_result mt0">
          <h4>회선 정보</h4>
          <table class="tbl_data mt5">
            <caption>회선 정보</caption>
            <colgroup>
              <col width="14%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">고객명</th>
                <td>{{ resultVo.scustname }}</td>
                <th scope="row">전용번호</th>
                <td>{{ resultVo.sllnum }}</td>
                <th scope="row">SAID</th>
                <td>{{ resultVo.ssaid }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">상품명</th>
                <td>{{ resultVo.sipmsSvcNm }}</td>
                <th scope="row">수용국</th>
                <td>{{ resultVo.sofficename }}</td>
                <th scope="row">KT 사업용여부</th>
                <td>{{ resultVo.ssvcUseTypeNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">설치장소(지번)</th>
                <td colspan="5">{{ resultVo.sinstalladdress }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">설치장소(도로명)</th>
                <td colspan="5">{{ resultVo.sinstallroadaddress }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 청약 정보 -->
        <div id="ordSection" class="content_result">
          <h4>청약 정보</h4>
          <table class="tbl_data mt5">
            <caption>청약 정보</caption>
            <colgroup>
              <col width="14%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
              <col width="13%" />
              <col width="20%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">오더번호</th>
                <td>{{ resultVo.sordernum }}</td>
                <th scope="row">희망일</th>
                <td>{{ resultVo.sodrhopedt }}</td>
                <th scope="row">접수일</th>
                <td>{{ resultVo.sodrregdt }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">접수자 참고사항</th>
                <td colspan="5">{{ resultVo.sreportOpinion }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 물리시설 정보 -->
        <div class="content_result">
          <h4>물리시설 정보</h4>
          <table class="tbl_data mt5" summary="물리시설 정보">
            <caption>물리시설 정보</caption>
            <colgroup>
              <col width="20%" />
              <col width="27%" />
              <col width="23%" />
              <col width="30%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">시설명</th>
                <td>{{ resultVo.ssubscnealias }}</td>
                <th scope="row">I/F명</th>
                <td>{{ resultVo.ssubsclgipportdescription }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">장비대표 IP</th>
                <td>{{ resultVo.ssubscmstip }}</td>
                <th scope="row">대국 인터페이스 IP</th>
                <td>{{ resultVo.ssubscrouterserialip }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">자국 인터페이스 IP</th>
                <td>{{ resultVo.ssubsclgipportip }}</td>
                <th scope="row">수용회선명</th>
                <td>{{ resultVo.sconnalias }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" class="el-icon-close" @click="close()">
          닫기
        </el-button>
      </div>
    </el-dialog>
  </div>
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
      // /ipmgmt/allocmgmt/viewDetailSubSvcMst.model
      if (ipBlockMstVo === null) return
      /*
      레거시 url: ipmgmt/allocmgmt/viewDetailSubSvcMst.ajax
      회선정보조회
        case 1. 할당상세pop
          ipBlockMstVo: { nipAllocMstSeq , nipLinkMstSeq }
        case 2. 할당처리pop > 회선 정보
          ipBlockMstVo: { sllnum, ssaid, ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd }
      */
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailSubSvcMst, ipBlockMstVo)
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
