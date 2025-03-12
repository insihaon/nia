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
        <template v-if="menuType === 'manualAloc'">
          <div class="popupContentTableTitle">시설용IP 블록 내 할당대역 목록</div>
          <!-- 할당대역 목록 compTable 추가 -->
          <template v-if="ipAllocOperMstVo.sassignLevelCd === 'IA0004'">
            <div class="popupContentTableTitle">수동 입력 할당</div>
            <table>
              <tr>
                <th>배정상태</th>
                <td colspan="3" class="textcenter">
                  <template v-if="ipAllocOperMstVo">
                    {{ ipAllocOperMstVo.sassignLevelNm }}
                  </template>
                </td>
              </tr>
              <tr>
                <th>IP주소</th>
                <td class="textcenter">
                  <el-input v-model="alocInfo.network" disabled style="width: 200px;margin-right: 5px" />
                  <el-input v-model="alocInfo.host" style="width: 200px;" /> /
                  <!-- <el-button type="primary" size="small" round class="ml-2" @click="handleClickIpBlockCheck">할당</el-button> -->
                </td>
                <th>할당 Bitmask</th>
                <td>
                  <el-input v-model="alocInfo.nbitmask" style="width: 100px;" />
                </td>
              </tr>
            </table>
          </template>
          <!-- 시설용 IP블록 내 할당 대역 리스트, 수동입력 할당 -->
        </template>
        <template v-if="menuType !== 'manualAloc'">
          <div class="popupContentTableTitle">IP 할당 정보</div>
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
                <td colspan="8" class="textcenter">조회된 결과 목록이 존재하지 않습니다.</td>
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
        </template>
        <template v-if="menuType === 'autoAloc'">
          <div class="popupContentTableTitle">운용정보관리(링크) data</div>
          <div style="overflow-y: auto;">
            <table>
              <tr>
                <th>링크IP블록</th>
                <th>자국 수용국명</th>
                <th>자국 장비명</th>
                <th>자국 장비IP</th>
                <th>자국 IF명</th>
                <th>대국 수용국명</th>
                <th>대국 장비명</th>
                <th>대국 장비IP</th>
                <th>대국 IF명</th>
                <th>SAID</th>
                <th>전용번호</th>
                <th>수용회선명</th>
                <th>작업일자</th>
              </tr>
              <tr></tr>
            </table>
          </div>
        </template>
      </div>
      <div class="popupContentTableBottom">
        <el-button v-if="ipAllocOperMstVo" type="primary" icon="el-icon-check" size="small" round @click="fnViewCheckTacsIpBlock_">IP블럭 중복체크</el-button>
        <template v-if="ipAllocOperMstVo.sassignLevelCd === 'IA0004'">
          <el-button v-if="menuType === 'autoAloc'" type="primary" icon="el-icon-menu" size="small" round @click="fnViewCheckTacsIpBlock_('AUTO_ALLOC')">IPv6로 자동할당</el-button>
          <el-button type="primary" size="small" icon="el-icon-menu" round @click="handleClickIpBlockCheck">할당</el-button>
          <!--
            시설용 IP 수동 할당의 경우 시설지정 할당 팝업 fnAlocCallBtnClick
            v4-> v6 자동 할당일 경우 ip블럭 중복체크
           -->
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
      menuType: 'Aloc', /* Aloc, autoAloc(시설용 IPv4-> v6 자동할당), manualAloc (시설용 IPv6 수동할당) */
      receivedRow: null,
      resultList: [],
      ipAllocOperMstVo: {},
      tbRoutChkMstVo: {},
      alocInfo: {
        network: '',
        host: '',
        nbitmask: '',
      }
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
          this._merge(this.alocInfo, this.splitIPv6(model.row.pipPrefix))
          this.alocInfo.nbitmask = model.row.nbitmask
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
        const { ssvcGroupCd, ssvcObjCd } = this.ipAllocOperMstVo
        if (ssvcGroupCd === '000000' || ssvcObjCd === '000000') {
          const { ssvcGroupCd, ssvcGroupNm, ssvcObjCd, ssvcObjNm } = this.receivedRow
          this._merge(this.ipAllocOperMstVo, { ssvcGroupCd, ssvcGroupNm, ssvcObjCd, ssvcObjNm })
        }
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
    fnViewCheckTacsIpBlock_(typeFlag = null) {
      fnViewCheckTacsIpBlock(this, [this.receivedRow], typeFlag)
    },
    fnAlocCallBtnClick() {
      this.$emit('alocCallBtnClick', this.receivedRow)
      this.close()
    },
    async handleClickIpBlockCheck() {
      /*
      if : 시설용 ipv6일 때 서비스 호출 sipVersionTypeCd === 'CV0002' && this.ipAllocOperMstVo.isFaciliteis

      else : 할당 시설정보 셋팅화면 호출 => this.fnAlocCallBtnClick()
      */
     const ipAllocOperMstVo = this.ipAllocOperMstVo
     console.log()
      const { sipCreateTypeCd, ssvcLineTypeCd, sipCreateSeqCd, sipVersionTypeCd, isFacilities } = this.ipAllocOperMstVo
      if (sipVersionTypeCd === 'CV0002' && isFacilities) {
        const { network, host, nbitmask } = this.alocInfo
        const pipPrefix = this.compressIPv6(network + host + '/' + nbitmask)
        const IpAllocOperMstVo = {
          sipCreateTypeCd,
          ssvcLineTypeCd,
          sipCreateSeqCd,
          sipVersionTypeCd,
          pipPrefix, // new pipPrefix
        }
        try {
          /*
          2025-03-06 cidr 검증 및 중복 ip검증(db insert되는지)
          시설용 ipv6일 때 서비스 호출.
          */
          const res = await apiRequestJson(ipmsJsonApis.appendCrtIPAllocMst, IpAllocOperMstVo)
          console.log(res)
          // res.commonMsg 성공일 경우
          // 할당 정보 입력 팝업
          if (res.commonMsg !== 'SUCCESS') {
            onMessagePopup(this, res.commonMsg)
            return
          } else {
            this.fnAlocCallBtnClick()
          }
        } catch (error) {
          this.error(error)
        }
      } else {
        this.fnAlocCallBtnClick()
      }
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
    },
    // v4-> v6 주소 전환(시설용 ipv4자동할당시 사용)
    convertIPv4ToIPv6(ip, ipv4Mask) {
      // IPv4 주소를 점(.)을 기준으로 분리
      const parts = ip.split('.')
      if (parts.length !== 4) {
        throw new Error('잘못된 IPv4 주소 형식입니다.')
      }
      // 마지막 옥텟을 10진수에서 16진수(대문자)로 변환
      const lastHex = parseInt(parts[3], 10).toString(16).toUpperCase()
      // 첫 세 옥텟은 그대로 사용하고 마지막 옥텟은 변환된 값으로 대체
      const ipv6Segments = parts.slice(0, 3).concat(lastHex)
      // 고정된 IPv6 /64 프리픽스 (단축 표기법 사용)
      const prefix = '2400::'
      const ipv6Address = prefix + ipv6Segments.join(':')
      // IPv6 프리픽스 길이는 IPv4 마스크에 96을 더한 값
      const ipv6Mask = ipv4Mask + 96
      return `${ipv6Address} /${ipv6Mask}`
    },
    expandIPv6(address) {
      const parts = address.split('::')
      const left = parts[0] ? parts[0].split(':') : []
      const right = parts.length > 1 && parts[1] ? parts[1].split(':') : []
      const missing = 8 - (left.length + right.length)
      const zeros = Array(missing).fill('0')
      const groups = left.concat(zeros, right)
      return groups.map(g => g.padStart(4, '0'))
    },
    splitIPv6(ipWithMask) {
      const [ip, maskStr] = ipWithMask.split('/')
      const mask = parseInt(maskStr, 10)
      const groups = this.expandIPv6(ip)
      const fullHex = groups.join('')
      const netDigits = Math.floor(mask / 4)
      const networkHex = fullHex.slice(0, netDigits)
      let hostHex = fullHex.slice(netDigits)
      // 네트워크 부분은 floor(mask/4) 자리로 나누고,
      // 완전한 4자리 그룹은 minimal하게(불필요한 선행 0 제거) 변환하되,
      // partial 그룹(마지막 그룹)이면 원본 그대로 사용
      const netGroups = []
      const netCompleteCount = Math.floor(networkHex.length / 4)
      const netRem = networkHex.length % 4
      for (let i = 0; i < netCompleteCount; i++) {
        let grp = networkHex.slice(i * 4, i * 4 + 4)
        grp = parseInt(grp, 16).toString(16)
        if (networkHex.slice(i * 4, i * 4 + 4) === '0000') {
          grp = '0000'
        }
        netGroups.push(grp)
      }
      if (netRem > 0) {
        netGroups.push(networkHex.slice(netCompleteCount * 4))
      }
      let netFormatted = netGroups.join(':')
      if (networkHex.length % 4 === 0 && hostHex.length > 0) {
        netFormatted += ':'
      }
      // 호스트 부분은, 만약 네트워크가 partial로 끝났다면,
      // 해당 그룹의 남은 자리(4 - (networkHex.length mod 4))를 첫 그룹으로 사용하고,
      // 그 이후 나머지를 4자리씩 분리하여 minimal하게 변환
      const hostGroups = []
      const firstGroupLen = (networkHex.length % 4 === 0) ? 0 : (4 - (networkHex.length % 4))
      if (firstGroupLen > 0) {
        hostGroups.push(hostHex.slice(0, firstGroupLen))
        hostHex = hostHex.slice(firstGroupLen)
      }
      const hostCompleteCount = Math.floor(hostHex.length / 4)
      const hostRem = hostHex.length % 4
      for (let i = 0; i < hostCompleteCount; i++) {
        let grp = hostHex.slice(i * 4, i * 4 + 4)
        grp = parseInt(grp, 16).toString(16)
        if (hostHex.slice(i * 4, i * 4 + 4) === '0000') {
          grp = '0000'
        }
        hostGroups.push(grp)
      }
      if (hostRem > 0) {
        hostGroups.push(hostHex.slice(hostCompleteCount * 4))
      }
      const hostFormatted = hostGroups.join(':')
      return { network: netFormatted, host: hostFormatted }
    },
    compressIPv6(ip) {
      // 주소와 넷마스크 분리 (넷마스크가 없으면 undefined)
      const [address, mask] = ip.split('/')
      // 콜론(:)으로 각 그룹 분리
      let groups = address.split(':')
      // 각 그룹의 선행 0 제거 (빈 문자열이 되면 '0'으로 처리)
      groups = groups.map(group => group.replace(/^0+/, '') || '0')
      // 연속된 '0' 그룹 중 가장 긴 부분 찾기
      let bestStart = -1
      let bestLen = 0
      let currentStart = -1
      let currentLen = 0
      for (let i = 0; i < groups.length; i++) {
        if (groups[i] === '0') {
          if (currentStart === -1) {
            currentStart = i
            currentLen = 1
          } else {
            currentLen++
          }
        } else {
          if (currentLen > bestLen) {
            bestLen = currentLen
            bestStart = currentStart
          }
          currentStart = -1
          currentLen = 0
        }
      }
      // 끝까지 '0'가 연속되었을 경우 체크
      if (currentLen > bestLen) {
        bestLen = currentLen
        bestStart = currentStart
      }
      // 단일 '0' 그룹은 압축하지 않음 (압축은 2개 이상의 연속된 '0' 그룹에만 적용)
      if (bestLen < 2) {
        bestStart = -1
      }
      // 결과 문자열 생성: 압축할 부분이 있는 경우 '::'로 대체
      let result = ''
      if (bestStart !== -1) {
        // 압축 시작 전까지의 그룹
        const before = groups.slice(0, bestStart).join(':')
        // 압축 이후의 그룹
        const after = groups.slice(bestStart + bestLen).join(':')
        result = before + '::' + after
        // 양쪽 끝에 불필요한 콜론이 있다면 정리 (예: '::' 앞뒤에 빈 문자열이 있을 수 있음)
        result = result.replace(/^:$/, '::').replace(/:{3,}/, '::')
      } else {
        result = groups.join(':')
      }
      // 넷마스크가 있다면 다시 붙임
      if (mask !== undefined) {
        result += '/' + mask
      }
      return result
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
