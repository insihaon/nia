<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    ref="allocInsertModal"
    v-el-drag-dialog
    title="IP블록할당"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">할당 정보</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tbody>
          <tr>
            <th scope="row">할당구분</th>
            <td>
              <el-radio-group v-model="allocType" size="small">
                <el-radio v-show="sFlgAllocType === 'N' && vAllocType !=='allocTel'" label="allocNe">시설</el-radio>
                <el-radio v-show="sFlgAllocType === 'N' && vAllocType !=='allocNe'" label="allocLn">링크</el-radio>
                <el-radio v-show="sFlgAllocType !== 'N' && vAllocType ==='allocTel'" label="allocTel">회선</el-radio>
              </el-radio-group>
            </td>
            <th>{{ infoLabel }}</th>
            <td class="d-flex">
              <el-select
                v-if="allocType === 'allocTel'"
                v-model="srhSvcTypeCd"
                size="small"
                style="width: 120px"
              >
                <el-option label="전용번호" value="sllnum" />
                <el-option label="SAID" value="ssaid" />
              </el-select>
              <el-input
                v-model="infoValue"
                size="small"
                @input="formatInput"
              >
                <template #suffix>
                  <el-button
                    size="small"
                    icon="el-icon-search"
                    class="font-weight-bolder"
                    style="font-size: larger;border: none;background: none;"
                    @click="handleClickInfoButton"
                  />
                </template>
              </el-input>
            </td>
          </tr>
          <tr v-if="allocType !== 'allocLn'">
            <th>수용국</th>
            <td>{{ srcIpAllocMst.sofficename }}</td>
            <th>{{ allocType === 'allocNe' ? '장비': '시설' }}명</th>
            <td>{{ srcIpAllocMst.ssubscnealias }}</td>
          </tr>
          <tr v-if="allocType === 'allocTel'">
            <th>장비대표 IP</th>
            <td>{{ srcIpAllocMst.ssubscmstip }}</td>
            <th>I/F명</th>
            <td>{{ srcIpAllocMst.ssubsclgipportdescription }}</td>
          </tr>
          <tr v-if="allocType === 'allocTel'">
            <th scope="row">수용회선명</th>
            <td>{{ srcIpAllocMst.sconnalias }}</td>
            <th scope="row">고객명</th>
            <td>{{ srcIpAllocMst.scustname }}</td>
          </tr>
          <tr v-if="allocType === 'allocNe'">
            <th>전용번호</th>
            <td>
              <el-input v-model="srcIpAllocMst.sllnum" size="small" />
            </td>
            <th>모델명</th>
            <td>{{ srcIpAllocMst.smodelname }}</td>
          </tr>
          <tr v-if="allocType === 'allocNe'">
            <th>I/F명</th>
            <td colspan="3">
              <el-input v-model="srcIpAllocMst.ssubsclgipportdescription" size="small" />
            </td>
          </tr>
          <template v-if="allocType === 'allocLn'">
            <tr id="linkArea1">
              <th scope="row">자국 수용국</th>
              <td>{{ srcIpAllocMst.saofficesname }}</td>
              <th scope="row">대국 수용국</th>
              <td>{{ srcIpAllocMst.szaofficesname }}</td>
            </tr>
            <tr id="linkArea2">
              <th scope="row">자국 장비명</th>
              <td>{{ srcIpAllocMst.sanealias }}</td>
              <th scope="row">대국 장비명</th>
              <td>{{ srcIpAllocMst.sznealias }}</td>
            </tr>
            <tr id="linkArea3">
              <th scope="row">자국 IP</th>
              <td>{{ srcIpAllocMst.samstip }}</td>
              <th scope="row">대국 IP</th>
              <td>{{ srcIpAllocMst.szmstip }}</td>
            </tr>
            <tr id="linkArea4">
              <th scope="row">자국 IF명</th>
              <td>{{ srcIpAllocMst.saifname }}</td>
              <th scope="row">대국 IF명</th>
              <td>{{ srcIpAllocMst.szifname }}</td>
            </tr>
            <tr id="linkArea5">
              <th scope="row">전용번호</th>
              <td id="tdSllnum" colspan="3">{{ srcIpAllocMst.sllnum }}</td>
            </tr>
          </template>
          <tr>
            <th>비고</th>
            <td colspan="3">
              <textarea v-model="srcIpAllocMst.scomment" class="w98" rows="3" maxlength="4000" />
            </td>
          </tr>
          <!-- Add the rest of the table rows here following the same pattern -->
        </tbody>
      </table>
    </div>
    <div v-if="allocType === 'allocTel'" class="popupContentTable textcenter">
      <div class="popupContentTableTitle">할당 정보</div>
      <div class="scroll_area">
        <table>
          <thead>
            <tr>
              <th>서비스망</th>
              <th>노드</th>
              <th>공인/사설</th>
              <th>서비스</th>
              <th>IP블록</th>
              <th>GW IP</th>
              <th title="단위블록수(IPV4:/24, IPV6:/64)">단위블록수</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in allocTargetList" :key="index">
              <td>{{ item.ssvcLineTypeNm }}</td>
              <td>{{ item.ssvcObjNm }}</td>
              <td>{{ item.sipCreateTypeNm }}</td>
              <td>{{ item.sassignTypeNm }}</td>
              <td>{{ item.pipPrefix }}</td>
              <td>{{ item.sgatewayip }}</td>
              <td> {{ formatNumber(item.nclassCnt) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">할당 대상 정보</div>
      <div>
        <table>
          <!-- <colgroup>
                <col width="9%" /><col width="10%" /><col width="9%" /><col width="10%" /><col width="23%" /><col width="27%" /><col width="12%" />
              </colgroup> -->
          <thead>
            <tr>
              <th>서비스망</th>
              <th>노드</th>
              <th>공인/사설</th>
              <th>서비스</th>
              <th>IP블록</th>
              <th>GW IP</th>
              <th>단위블록수</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!ipAllocOperMstVos.length">
              <td colspan="8">조회된 결과 목록이 존재하지 않습니다.</td>
            </tr>
            <tr v-for="(item, index) in ipAllocOperMstVos" :key="index" :class="{'subbg': index % 2 !== 0, 'last': index === ipAllocOperMstVos.length - 1}">
              <td>{{ item.ssvcLineTypeNm }}</td>
              <td>{{ item.ssvcObjNm }}</td>
              <td>{{ item.sipCreateTypeNm }}</td>
              <td>{{ item.sassignTypeNm }}</td>
              <td>{{ item.pipPrefix }}</td>
              <td class="textflex">
                <el-select v-model="item.gwipType" size="mini" style="width: 90px" @change="fnSetGwip(item, index)">
                  <el-option value="direct" label="직접입력" />
                  <el-option value="first" label="시작IP" />
                  <el-option value="last" label="끝IP" />
                </el-select>
                <!-- sfirstAddrGwip, slastAddrGwip -->
                <el-input v-model="item.sgatewayip" size="mini" type="text" @keyup="(e)=> checkInput(e, 'IPonly')" />
              </td>
              <td>{{ formatNumber(item.nclassCnt) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-thumb" round @click="fnViewAllocCheckTacsIpBlock()">할당</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click="close()">닫기</el-button>
    </div>
    <ModalCheckTacsIpBlock ref="ModalCheckTacsIpBlock" />
    <ModalFacilityInformation ref="ModalFacilityInformation" @selected-value="setSelectedRow" />
    <ModalLinkInformation ref="ModalLinkInformation" @selected-value="setSelectedRow" />
    <ModalIpAllocCircuitDetail ref="ModalIpAllocCircuitDetail" @selected-value="setSelectedRow" @closeCircuitDetail="loadDetailSubSvcMst" />
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { checkInput } from '@/views-ipms/js/common-function'

import ModalFacilityInformation from '@/views-ipms/modal/search/ModalFacilityInformation.vue'
import ModalLinkInformation from '@/views-ipms/modal/search/ModalLinkInformation.vue'
import ModalIpAllocCircuitDetail from '@/views-ipms/modal/alloc/ModalIpAllocCircuitDetail.vue'
import ModalCheckTacsIpBlock from '@/views-ipms/modal/ModalCheckTacsIpBlock.vue'

import { ipmsJsonApis, apiRequestJson, ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalIpAllocInsert'

export default {
  name: routeName,
  components: { ModalFacilityInformation, ModalLinkInformation, ModalIpAllocCircuitDetail, ModalCheckTacsIpBlock },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      checkInput,
      // allocParam: {},
      infoValue: '',
      menuType: 'Aloc',
      sFlgAllocType: 'N',
      vAllocType: 'allocNe',
      gwipType: 'last',
      returnFlag: null,
      allocTargetList: [], // allocTel일 경우 조회
      ipAllocOperMstVos: [], // 할당 처리할 rows 정보
      tbRoutChkMstVo: null, // 라우팅 정보 현행화 업데이트를 위한 정보(IP주소 라우팅 비교/점검)
      srcIpAllocMst: {
        sllnum: '',
        scomment: '',
        sicisofficescode: '',
        smodelname: '',
        ssubscmstip: '',
        ssubscnealias: '',
        ssubscnnescode: '',
        ssubsclgipportdescription: ''
      },
      allocType: 'allocNe',
      srhSvcTypeCd: 'sllnum',
      // srchSvcNum: '',
      infoByAllocType: {
        allocNe: { label: '시설정보', modalName: 'ModalFacilityInformation', valueName: 'ssubscnealias', paramKey: {
          sofficename: 'sofficename',
          ssubscnealias: 'ssubscnealias',
          smodelname: 'smodelname',
          ssubscmstip: 'ssubscmstip', // ui x
          ssubscnnescode: 'ssubscnnescode' // ui x
        } },
        allocLn: { label: '링크정보', modalName: 'ModalLinkInformation', valueName: 'sanealias', paramKey: {
          saofficesname: 'saofficescodeNm',
          saofficescode: 'saofficescode',
          szaofficesname: 'szofficescodeNm',
          sanealias: 'sanealias',
          sznealias: 'sznealias',
          samstip: 'samstip',
          szmstip: 'szmstip',
          saifname: 'saifname',
          szifname: 'szifname',
          sllnum: 'sllnum',
          nipLinkMstSeq: 'nipLinkMstSeq',
          sconnAlias: 'sconnAlias'
        } },
        allocTel: { label: '회선정보', modalName: 'ModalIpAllocCircuitDetail', valueName: 'sllnum', paramKey: {
          nipAllocMstSeq: 'nipAllocMstSeq',
          sofficename: 'sofficename', // 수용국
          ssubscnealias: 'ssubscnealias', // 시설명
          ssubscmstip: 'ssubscmstip', // 장비대표IP
          ssubsclgipportdescription: 'ssubsclgipportdescription', // I/F 명
          sconnalias: 'sconnalias', // 수용회선명
          scustname: 'scustname'// 고객명
        } },
      },
      ipCheckCommonMsg: ''
    }
  },
  computed: {
    infoModalName() {
      return this.infoByAllocType[this.allocType].modalName
    },
    infoLabel() {
      return this.infoByAllocType[this.allocType].label
    },
    infoValueName() {
      return this.infoByAllocType[this.allocType].valueName
    },
    infoModalParameterKey() {
      return this.infoByAllocType[this.allocType].paramKey
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.ipAllocOperMstVos) {
        model.ipAllocOperMstVos.forEach(row => {
          // GW IP내 정보 setting
          row['gwipType'] = 'last'
          row['sgatewayip'] = row.slastAddrGwip
      })
        this.ipAllocOperMstVos = model.ipAllocOperMstVos
      }
      if (model.tbRoutChkMstVo) {
        this.tbRoutChkMstVo = model.tbRoutChkMstVo
        this.menuType = model.menuType
      }
      this.init()
    },
    onClose() {
      this.infoValue = ''
      this.menuType = 'Aloc'
    },
    init() {
      Object.keys(this.infoModalParameterKey).forEach(key => {
        this.srcIpAllocMst[key] = ''
      })

      const { sneossDdYn, ssvcLineTypeCd, nbitmask } = this.ipAllocOperMstVos[0]
      this.sFlgAllocType = sneossDdYn
      this.vAllocType = 'allocNe'
      if (['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd) && [30, 29].includes(nbitmask)) {
        this.vAllocType = 'allocLn'
      }
      if (this.sFlgAllocType !== 'N') {
        this.vAllocType = 'allocTel'
      }
      this.allocType = this.vAllocType
    },
    formatInput(val) {
      if (this.allocType === 'allocTel') {
        this.infoValue = val.replace(/\D/g, '')
      }
    },
    searchSvcNum() {
      // Implement search functionality
    },
    fnSetGwip(item, index) {
      const gwipType = item.gwipType
      let value = ''
      switch (gwipType) {
        case 'first':
          value = item.sfirstAddrGwip
          break
        case 'last':
          value = item.slastAddrGwip
          break
        default:
          break
      }
      item['sgatewayip'] = value
      // this.$set(item, 'addrGwip', value)
      this.$set(this.ipAllocOperMstVos, index, item)
      // this.$forceUpdate()
      // Implement the function to set GW IP
    },
    // checkInput(item, type) {
    //   // 레거시 common function 생성
    // },
    formatNumber(num) {
      // Convert the number to a string
      let numStr = num.toString()
      // If there is a decimal point, trim trailing zeros
      if (numStr.indexOf('.') !== -1) {
          // Use a regular expression to remove trailing zeros
          numStr = numStr.replace(/0+$/, '')
          // If the decimal point is the last character, remove it
          if (numStr[numStr.length - 1] === '.') {
            numStr = numStr.slice(0, -1)
          }
      }
      // Convert the string back to a number
      return parseFloat(numStr)
    },
    handleClickInfoButton() {
      // const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd } = this.ipAllocOperMstVos[0]
      const ipBlockMstVo = this.ipAllocOperMstVos[0]

      if (this.allocType === 'allocTel') {
        if (this.infoValue.length <= 4) {
          onMessagePopup(this, '회선정보 조회값을 입력해주세요.')
          return
        } else {
          Object.assign(ipBlockMstVo, { sllnum: null, ssaid: null })
          ipBlockMstVo[this.srhSvcTypeCd] = this.infoValue // sllnum or ssaid setting
        }
      }
      this.$refs[this.infoModalName].open({ ipBlockMstVo, inputText: this.infoValue })
    },
    async loadDetailSubSvcMst() { // 회선할당정보 조회
      try {
        const ipBlockMstVo = { sllnum: '', ssaid: '' }
        ipBlockMstVo[this.srhSvcTypeCd] = this.infoValue

        const res = await apiRequestJson(ipmsJsonApis.selectDetailSubSvcMstList, ipBlockMstVo)
        this.allocTargetList = res.ipAllocOperMstVos
      } catch (error) {
        this.error(error)
      }
    },
    setSelectedRow(params) {
      const { row, returnFlag } = params
      this.infoValue = row[this.infoValueName]
      this.selectedRow = row
      this.returnFlag = returnFlag

      Object.keys(this.infoModalParameterKey).forEach(key => {
        // if (row[this.infoModalParameterKey[key]] !== null) {
        this.srcIpAllocMst[key] = row[this.infoModalParameterKey[key]]
        // }
      })
      // this.infoModalParameterKey
    },
    async fnViewAllocCheckTacsIpBlock() {
      if (!this.returnFlag) {
        onMessagePopup(this, '할당 할 회선/시설/링크 정보를 선택해주세요.')
        return
      }
      if (this.ipAllocOperMstVos.length > 1) {
        this.confirm('다수의 IP블럭할당의 경우 IP블럭의 중복체크를 할 수 없습니다. 그래도 할당진행 하시겠습니까?', '알림', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'warning',
        }).then(() => {
          this.fnInsertConfirmBtnClick()
        })
      } else {
        const { ssvcLineTypeCd, sipVersionTypeCd } = this.ipAllocOperMstVos[0]
        if (!['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd) || sipVersionTypeCd !== 'CV0001') {
          this.fnInsertConfirmBtnClick()
        } else {
          // Tacs Ip중복 체크 api 요청
          this.$refs.ModalCheckTacsIpBlock.open({ row: this.ipAllocOperMstVos[0], typeFlag: 'ALLOC' })
        }
      }
      /* ip블럭 중복체크 확인 */
      /* 할당 */
      // / ssubscnescode
      // Implement the function to check TACS IP block
    },
    async fnInsertConfirmBtnClick() {
    let srcIpAllocMstVo = {}
    if (this.allocType === 'allocNe' && this.allocType === this.returnFlag) { // 시설
      const { sicisofficescode, smodelname, ssubscnealias, ssubscmstip, ssubscnnescode, sllnum, scomment } = this.srcIpAllocMst
      const ssubsclgipportdescription = this.srcIpAllocMst.ssubsclgipportdescription

      srcIpAllocMstVo = { sicisofficescode, ssubscnealias, smodelname, ssubscmstip, ssubscnnescode, sllnum, ssubsclgipportdescription, scomment }
    } else if (this.allocType === 'allocLn' && this.allocType === this.returnFlag) { // 링크
      const {
        saofficescode: sicisofficescode,
        sanealias: ssubscnealias,
        samstip: ssubscmstip,
        saifname: ssubsclgipportdescription,
        sllnum,
        nipLinkMstSeq,
        sconnAlias,
        scomment,
      } = this.srcIpAllocMst
      srcIpAllocMstVo = {
        sicisofficescode, // 자국수용국
        ssubscnealias, // 자국 장비명
        ssubscmstip, // 자국 대표IP
        ssubsclgipportdescription, // 자국IF명
        sllnum, // 전용번호
        nipLinkMstSeq, // 전용번호
        sconnAlias, // 수용회선명
        scomment
      }
    } else if (this.allocType === 'allocTel' && this.vAllocType === this.returnFlag) {
      srcIpAllocMstVo = { nipAllocMstSeq: this.srcIpAllocMst.nipAllocMstSeq, scomment: this.srcIpAllocMst.scomment }
    } else {
      onMessagePopup(this, '할당 할 회선/시설/링크 정보를 선택해주세요.')
      return
    }

    let chkGwipFlag = 'N'
      // 데이터 확인용
    const THIS = this
     const destIpAllocMstVos = []
     this.ipAllocOperMstVos.forEach(row => {
      /*
        IPv6 시설용 할당할 경우 2/3계위 추가 -> alloc테이블에 2/3계위 컬럼 추가 필요(검토)
        ssvcGroupCd, ssvcObjCd
      */
        const { nipAssignMstSeq, sgatewayip, sassignTypeCd, sipCreateTypeCd, ssvcLineTypeCd, nlvlMstSeq, nipAllocMstCnt, nipmsSvcSeq } = row
        const ipAllocOperMstVo = { nipAssignMstSeq, sgatewayip, sassignTypeCd, sipCreateTypeCd, ssvcLineTypeCd, nlvlMstSeq, nipAllocMstCnt, nipmsSvcSeq }
        if (sgatewayip === '') {
          chkGwipFlag = 'Y'
          return
        }
        destIpAllocMstVos.push(ipAllocOperMstVo)
     })
     if (chkGwipFlag !== 'N') {
      onMessagePopup(this, 'GW IP를 입력해주세요(필수).')
      return
     }
     const ipAllocMstComplexVo = { srcIpAllocMstVo, destIpAllocMstVos, menuType: this.menuType }
     const target = ({ vue: this.$refs.allocInsertModal })
     try {
      this.openLoading(target, { text: '할당 처리 중입니다.' })
      const res = { commonMsg: 'SUCCESS' }
      await apiRequestJson(ipmsJsonApis.insertAlcIPMst, ipAllocMstComplexVo)
      if (this.menuType === 'Rout') {
        await this.fnUpdateStatusMst(res.ipAllocOperMstVo)
      } else {
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, 'IP블록 할당이 정상적으로 처리되었습니다.')
          this.$emit('reload')
          this.close()
          return true
        } else {
          onMessagePopup(this, res.commonMsg)
          return false
        }
      }
      return res
     } catch (error) {
      this.error(error)
     } finally {
      this.closeLoading(target)
     }
    },
    async fnUpdateStatusMst(ipAllocOperMstVo) { // IP주소 라우팅 현황 update
      const sdbIntgrmRsltCd = ipAllocOperMstVo.commonMsg === 'SUCCESS' ? 'DR0004' : 'DR0003'
      const tbRoutChkMstVo = Object.assign({}, this.tbRoutChkMstVo, { sdbIntgrmRsltCd })
      /* try {
        // 레거시 url: ipmgmt/routmgmt/updateStatusMst.json
        const res = await apiRequestJson(ipmsJsonApis.updateStatusMst, tbRoutChkMstVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, 'IP블록 할당이 정상적으로 처리되었습니다.')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      } */
    }
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-loading-spinner {
  flex-direction: column;
  align-items: center;
}
// .scroll_table {
//   colgroup {
//       width: 100%;
//   }

//   thead {
//       display: table;
//       width: 100%;
//       table-layout: fixed;
//   }

//   tbody {
//       display: block;
//       max-height: 200px;
//       overflow-y: scroll;
//       width: 100%;
//   }

//   tbody tr {
//       display: table;
//       table-layout: fixed;
//       width: 100%;
//   }
//   thead th, tbody td {
//       // text-align: left;
//       // padding: 8px;
//       border: 1px solid #ddd;
//       overflow: hidden; /* Ensures no text overflow */
//       white-space: nowrap;
//       text-overflow: ellipsis;
//   }
// }
</style>
