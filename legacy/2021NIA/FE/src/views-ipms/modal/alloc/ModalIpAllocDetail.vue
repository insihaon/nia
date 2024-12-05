<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP할당 상세정보"
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
    <div v-loading="vLoading">
      <div class="popupContentTable">
        <div class="popupContentTableTitle">IP 블록 정보</div>
        <table>
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <th>계위</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.ssvcLineTypeNm }} - {{ ipAllocOperMstVo.ssvcGroupNm }} - {{ ipAllocOperMstVo.ssvcObjNm }}
              </template>
            </td>
            <th>공인/사설</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.sipCreateTypeNm }}
              </template>
            </td>
          </tr>
          <tr>
            <th>할당상태</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.sassignLevelNm }}
              </template>
            </td>
            <th>서비스</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.sassignTypeNm }}
              </template>
            </td>
          </tr>
          <tr>
            <th>IP 버전</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.sipVersionTypeNm }}
              </template>
            </td>
            <th>IP 주소</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.pipPrefix }}
              </template>
            </td>
          </tr>
          <tr>
            <th>다중회선</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.nipAllocMstCnt }}
              </template>
            </td>
            <th>감사상태</th>
            <td>
              <template v-if="ipAllocOperMstVo">
                {{ ipAllocOperMstVo.svalidCheck }}
              </template>
            </td>
          </tr>
          <tr class="last">
            <th>비고</th>
            <td colspan="3">
              <textarea v-model="ipAllocOperMstVo.scomment" style="min-height: 50px" rows="3" maxlength="4000"></textarea>
            </td>
          </tr>
        </table>
        <div class="popupContentTableBottom">
          <el-button type="primary" size="small" icon="el-icon-edit" round @click="fnScommentUpdateClick">수정</el-button>
        </div>
        <div class="popupContentTableTitle">IP 블록 세부 정보</div>
        <table>
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tbody>
            <tr>
              <th>시작 IP</th>
              <td>
                <template v-if="ipAllocOperMstVo">
                  {{ ipAllocOperMstVo.sfirstAddr }}
                </template>
              </td>
              <th>끝 IP</th>
              <td>
                <template v-if="ipAllocOperMstVo">
                  {{ ipAllocOperMstVo.slastAddr }}
                </template>
              </td>
            </tr>
            <tr>
              <th>총 IP 수</th>
              <td>
                <template v-if="ipAllocOperMstVo">
                  {{ formatNumber(ipAllocOperMstVo.ncnt) }}
                </template>
              </td>
              <th>단위블록수</th>
              <td>
                <template v-if="ipAllocOperMstVo">
                  {{ formatNumber(ipAllocOperMstVo.nclassCnt, 0, 10) }}
                </template>
              </td>
            </tr>
            <tr>
              <th>사용 IP 수</th>
              <td>
                <template v-if="ipAllocOperMstVo">
                  {{ formatNumber(ipAllocOperMstVo.nuseIpCnt) }}
                </template>
              </td>
              <th>가용 IP 수</th>
              <td>
                <template v-if="ipAllocOperMstVo">
                  {{ formatNumber(ipAllocOperMstVo.nfreeIpCnt) }}
                </template>
              </td>
            </tr>
          </tbody>
        </table>
        <div class="popupContentTableTitle textcenter">IP 할당 정보</div>
        <table>
          <thead>
            <tr>
              <th>수용국</th>
              <th>장비명</th>
              <th>대표 IP</th>
              <th>I/F명</th>
              <th>수용회선명</th>
              <th>전용번호</th>
              <th>해지</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="ipAllocOperMstVo.sassignLevelCd === 'IA0004' || ipAllocOperMstVo.nipAllocMstSeq === null">
              <td colspan="8">조회된 결과 목록이 존재하지 않습니다.</td>
            </tr>
            <template v-else>
              <tr
                v-for="(item, index) in resultList"
                :key="item.nipAllocMstSeq"
                style="cursor: pointer;"
                :class="{'subbg': index % 2 !== 0, 'last': index === ipAllocOperMstVo.length - 1}"
              >
                <td :title="item.sofficename" @click="fnViewSubDetailAlcIPMst(item)">
                  {{ item.sofficename }}
                </td>
                <td :title="item.ssubscnealias" @click="fnViewSubDetailAlcIPMst(item)">
                  {{ item.ssubscnealias }}
                </td>
                <td :title="item.ssubscmstip" @click="fnViewSubDetailAlcIPMst(item)">
                  {{ item.ssubscmstip }}
                </td>
                <td :title="item.ssubsclgipportdescription" @click="fnViewSubDetailAlcIPMst(item)">
                  {{ item.ssubsclgipportdescription }}
                </td>
                <td :title="item.sconnAlias" @click="fnViewSubDetailAlcIPMst(item)">
                  {{ item.sconnAlias }}
                </td>
                <td :title="item.sllnum" @click="fnViewSubDetailAlcIPMst(item)">
                  {{ item.sllnum }}
                </td>
                <td class="btn_text">
                  <el-button type="primary" size="small" round plain @click="fnDeleteAlcIPMstClick(item)">
                    해지
                  </el-button>
                </td>
              </tr>
            </template>
          </tbody>
        </table>
      </div>
      <div class="popupContentTableBottom">
        <el-button v-if="ipAllocOperMstVo" type="primary" icon="el-icon-check" size="small" round @click="fnViewCheckTacsIpBlock_">IP블럭 중복체크</el-button>
        <template v-if="ipAllocOperMstVo.sassignLevelCd === 'IA0004'">
          <el-button type="primary" size="small" icon="el-icon-menu" round @click="fnAlocCallBtnClick">할당</el-button>
          <el-button type="primary" size="small" icon="el-icon-menu" round @click="fnRetUpdateConfirmClick">반납</el-button>
        </template>
        <el-button type="primary" icon="el-icon-close" size="small" round @click="close()">
          닫기
        </el-button>
      </div>
    </div>
    <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
    <ModalIpAllocCircuitDetail ref="ModalIpAllocCircuitDetail" />
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import ModalCheckTacsIpBlock from '@/views-ipms/modal/ModalCheckTacsIpBlock.vue'
import ModalIpAllocCircuitDetail from '@/views-ipms/modal/alloc/ModalIpAllocCircuitDetail.vue'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils'
import { fnViewCheckTacsIpBlock } from '@/views-ipms/js/common-function'

const routeName = 'ModalIpAllocDetail'

export default {
  name: routeName,
  components: { ModalIpAllocCircuitDetail, ModalCheckTacsIpBlock },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      vLoading: false,
      menuType: 'Aloc',
      receivedRow: null,
      resultList: [],
      ipAllocOperMstVo: {},
      tbRoutChkMstVo: {}
    }
  },
  computed: {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.$nextTick(() => {
        if (model.row) {
          this.receivedRow = model.row
          this.fnViewDetailAlcIPMst({ nipAssignMstSeq: model.row.nipAssignMstSeq })
        }
        if (model.menuType) {
          this.menuType = model.menuType
          this.tbRoutChkMstVo = { nlvlMstSeq: model.row.nlvlMstSeq, nroutingChkMstSeq: model.row.nroutingChkMstSeq }
        }
      })
    },
    onClose() {
      this.menuType = 'Aloc'
    },
    async fnViewDetailAlcIPMst(param) {
      try {
        /*
        IP주소 라우팅 비교/점검 > IP해지 (/ipmgmt/routmgmt/viewPopDetailAlclPMst.model)

        파라미터 및 url을 viewDetailAlcIPMst과 동일하게 처리하여도 문제 없음을 확인함
        추후 문제 발생할 경우 화면별로 분기처리가 필요함
        */
        this.vLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewDetailAlcIPMst, param)
        this.resultList = res.result.data
        this.ipAllocOperMstVo = this.resultList[0]
      } catch (error) {
        this.error(error)
      } finally {
        this.vLoading = false
      }
    },
    formatNumber(value, minFractionDigits = 0, maxFractionDigits = 0) {
      return new Intl.NumberFormat('en-US', {
        minimumFractionDigits: minFractionDigits,
        maximumFractionDigits: maxFractionDigits
      }).format(value)
    },
    async fnScommentUpdateClick() {
      const tbIpAssignMstComplexVo = {
        srcIpAssignMstVo: { scomment: this.ipAllocOperMstVo.scomment },
        destIpAssignMstVos: [{ nipAssignMstSeq: this.ipAllocOperMstVo.nipAssignMstSeq }],
        menuType: this.menuType
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.updateScommentAsgnIPMst, tbIpAssignMstComplexVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '비고 수정이 정상적으로 처리되었습니다.')
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnViewSubDetailAlcIPMst(row) {
      const { nipAllocMstSeq, nipLinkMstSeq } = row
      const ipBlockMstVo = { nipAllocMstSeq, nipLinkMstSeq }
      if (nipLinkMstSeq === null || nipLinkMstSeq === '') {
        // 회선상세정보
        this.$refs.ModalIpAllocCircuitDetail.open({ ipBlockMstVo })
      } else {
        // 링크마스터 상세조회
      }
    },
    fnDeleteAlcIPMstClick(delRow) {
      // Implement your delete logic here
      // /ipmgmt/allocmgmt/deletAlcIPMst.json
      // this.resultList = res.result.data
      // this.ipAllocOperMstVo
      /*

    {
    "srcIpAllocMstVo": {
        "nipAssignMstSeq": "56711",
        "nipAllocMstSeq": "643143"
      },
      "destIpAllocMstVos": [
          {
            "nipAssignMstSeq": "56711",
            "nipAllocMstSeq": "643143",
            "nwhoisSeq": "366843"
          }
        ]
    }
    {
    "srcIpAllocMstVo": {
        "nipAssignMstSeq": "56711",
        "nipAllocMstSeq": "713048"
    },
    "destIpAllocMstVos": [
        {
            "nipAssignMstSeq": "56711",
            "nipAllocMstSeq": "713048",
            "nwhoisSeq": "366843"
        }
      ]
    }
      */
      this.confirm('할당/예약 정보를 해지 하시겠습니까?', '알림', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
      }).then(async() => {
        const srcIpAllocMstVo = { nipAssignMstSeq: delRow.nipAssignMstSeq, nipAllocMstSeq: delRow.nipAllocMstSeq }
        const ipAllocOperMstVo = Object.assign({}, srcIpAllocMstVo, { nwhoisSeq: delRow.nwhoisSeq })
        if (this.menuType === 'Rout') {
         Object.assign(ipAllocOperMstVo, { sGubun: 'ROUTMGMT' })
        }
        const destIpAllocMstVos = [ipAllocOperMstVo]
         try {
           const res = await apiRequestJson(ipmsJsonApis.deletAlcIPMst, { srcIpAllocMstVo, destIpAllocMstVos, menuType: this.menuType })
           if (this.menuType === 'Rout') {
            this.fnUpdateStatusMst(res, delRow)
          } else {
             if (res.commonMsg === 'SUCCESS') {
               onMessagePopup(this, '해지가 정상적으로 처리되었습니다.')
               this.eventReload()
               this.close()
             } else {
               onMessagePopup(this, res.commonMsg)
             }
           }
         } catch (error) {
           this.error(error)
         }
      })
    },
    async fnUpdateStatusMst(res, delRow) { // IP주소 라우팅 현황 update
      const sdbIntgrmRsltCd = res.commonMsg === 'SUCCESS' ? 'DR0004' : 'DR0003'
      const tbRoutChkMstVo = Object.assign({}, this.tbRoutChkMstVo, { sdbIntgrmRsltCd })
      try {
        // 레거시 url: ipmgmt/routmgmt/updateStatusMst.json
        const res = await apiRequestJson(ipmsJsonApis.updateStatusMst, tbRoutChkMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '해지가 정상적으로 처리되었습니다.')
          this.eventReload()
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    fnViewCheckTacsIpBlock_() {
      fnViewCheckTacsIpBlock(this, [this.receivedRow])
    },
    fnAlocCallBtnClick() {
      this.$emit('alocCallBtnClick')
      this.close()
    },
    async fnRetUpdateConfirmClick() {
      /* sipCreateTypeCd: 기존 사설(CT0004) 은 유/무선공용으로 사용, 신규 사설(CT0005) 을 사설로 사용  */
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd, /* sassignLevelCd, */ sipCreateTypeCd, nipAssignMstSeq } = this.ipAllocOperMstVo
      const srcIpAssignMstVo = { ssvcLineTypeCd, sassignTypeCd: 'SA0000' }
      if (sipCreateTypeCd === 'CT0005' && this.menuType === 'Aloc') {
        Object.assign(srcIpAssignMstVo, { ssvcGroupCd, ssvcObjCd, sassignLevelCd: 'IA0001' /* 미배정 */ })
      } else {
        if (ssvcGroupCd === '000000') { // 서비스 망만 있는 경우
          Object.assign(srcIpAssignMstVo, { ssvcGroupCd, ssvcObjCd, sassignLevelCd: 'IA0002' })
        } else {
          if (ssvcObjCd === '000000') { // 서비스망 / 본부까지 있는 경우
            Object.assign(srcIpAssignMstVo, { ssvcGroupCd: '000000', ssvcObjCd, sassignLevelCd: 'IA0002' })
          } else { // 서비스망 / 본부 / 노드까지 있는 경우
            Object.assign(srcIpAssignMstVo, { ssvcGroupCd: ssvcGroupCd, ssvcObjCd: '000000', sassignLevelCd: 'IA0003' })
          }
        }
      }
      const tbIpAssignMstComplexVo = {
        srcIpAssignMstVo,
        destIpAssignMstVos: [/* tbIpAssignMstVo */{ nipAssignMstSeq, typeFlag: 'return' /* 배정-반납 */, menuType: this.menuType }]
      }
      try {
        const res = await apiRequestJson(ipmsJsonApis.updateAsgnIPMst, tbIpAssignMstComplexVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, 'IP블록 반납이 정상적으로 처리되었습니다.')
          this.eventReload()
          this.close()
        }
      } catch (error) {
        this.error(error)
      }
    },
    eventReload() {
      this.$emit('reload')
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
