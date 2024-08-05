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
        IP블록할당
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <div class="content_result mt0">
          <h4>할당 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">할당구분</th>
                <td class="view">
                  <el-radio-group v-model="allocType" size="mini">
                    <el-radio v-show="sFlgAllocType === 'N' && vAllocType !=='allocTel'" label="allocNe">시설</el-radio>
                    <el-radio v-show="sFlgAllocType === 'N' && vAllocType !=='allocNe'" label="allocLn">링크</el-radio>
                    <el-radio v-show="sFlgAllocType !== 'N' && vAllocType ==='allocTel'" label="allocTel">회선</el-radio>
                  </el-radio-group>
                </td>
                <th class="first">{{ infoLabel }}</th>
                <td class="view d-flex">
                  <el-select
                    v-if="allocType === 'allocTel'"
                    v-model="srhSvcTypeCd"
                    size="mini"
                    style="width: 120px"
                  >
                    <el-option label="전용번호" value="sllnum" />
                    <el-option label="SAID" value="ssaid" />
                  </el-select>
                  <el-input
                    v-model="infoValue"
                    size="mini"
                    @input="formatInput"
                  >
                    <template #suffix>
                      <el-button
                        size="mini"
                        icon="el-icon-search"
                        class="font-weight-bolder"
                        style="font-size: larger;border: none"
                        @click="handleClickInfoButton"
                      />
                    </template>
                  </el-input>
                </td>
              </tr>
              <tr v-if="allocType !== 'allocLn'" id="svcArea1">
                <th class="first">수용국</th>
                <td class="view">{{ srcIpAllocMst.sofficename }}</td>
                <th class="first">{{ allocType === 'allocNe' ? '장비': '시설' }}명</th>
                <td class="view">{{ srcIpAllocMst.ssubscnealias }}</td>
              </tr>
              <tr v-if="allocType === 'allocTel'" id="svcArea2">
                <th class="first">장비대표 IP</th>
                <td class="view">{{ srcIpAllocMst.ssubscmstip }}</td>
                <th class="first">I/F명</th>
                <td class="view">{{ srcIpAllocMst.ssubsclgipportdescription }}</td>
              </tr>
              <tr v-if="allocType === 'allocTel'" id="svcArea3">
                <th class="first" scope="row">수용회선명</th>
                <td class="view">{{ srcIpAllocMst.sconnAlias }}</td>
                <th scope="row">고객명</th>
                <td class="view">{{ srcIpAllocMst.scustName }}</td>
              </tr>
              <tr v-if="allocType === 'allocNe'" id="svcArea4">
                <th class="first">전용번호</th>
                <td class="view">
                  <el-input v-model="srcIpAllocMst.sllnum" size="mini" />
                </td>
                <th class="first">모델명</th>
                <td class="view">{{ srcIpAllocMst.smodelname }}</td>
              </tr>
              <tr v-if="allocType === 'allocNe'" id="svcArea5">
                <th class="first">I/F명</th>
                <td colspan="3" class="view">
                  <el-input v-model="srcIpAllocMst.ssubsclgipportdescription" size="mini" />
                </td>
              </tr>
              <template v-if="allocType === 'allocLn'">
                <tr id="linkArea1">
                  <th class="first" scope="row">자국 수용국</th>
                  <td class="view">{{ srcIpAllocMst.saofficesname }}</td>
                  <th scope="row">대국 수용국</th>
                  <td class="view">{{ srcIpAllocMst.szaofficesname }}</td>
                </tr>
                <tr id="linkArea2">
                  <th class="first" scope="row">자국 장비명</th>
                  <td class="view">{{ srcIpAllocMst.sanealias }}</td>
                  <th scope="row">대국 장비명</th>
                  <td class="view">{{ srcIpAllocMst.sznealias }}</td>
                </tr>
                <tr id="linkArea3">
                  <th class="first" scope="row">자국 IP</th>
                  <td class="view">{{ srcIpAllocMst.samstip }}</td>
                  <th scope="row">대국 IP</th>
                  <td class="view">{{ srcIpAllocMst.szmstip }}</td>
                </tr>
                <tr id="linkArea4">
                  <th class="first" scope="row">자국 IF명</th>
                  <td class="view">{{ srcIpAllocMst.saifname }}</td>
                  <th scope="row">대국 IF명</th>
                  <td class="view">{{ srcIpAllocMst.szifname }}</td>
                </tr>
                <tr id="linkArea5">
                  <th class="first" scope="row">전용번호</th>
                  <td id="tdSllnum" colspan="3" class="view">{{ srcIpAllocMst.sllnum }}</td>
                </tr>
              </template>
              <tr>
                <th class="first">비고</th>
                <td colspan="3" class="view">
                  <textarea v-model="srcIpAllocMst.scomment" class="w98" rows="3" maxlength="4000" />
                </td>
              </tr>
              <!-- Add the rest of the table rows here following the same pattern -->
            </tbody>
          </table>
        </div>

        <div v-if="allocType === 'allocTel'" class="content_result">
          <div class="tit_group">
            <h4>할당 정보</h4>
          </div>
          <table id="insertBaseTable" class="tbl_list mt5">
            <colgroup>
              <col width="9%" />
              <col width="10%" />
              <col width="9%" />
              <col width="10%" />
              <col width="23%" />
              <col width="27%" />
              <col width="12%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">서비스망</th>
                <th scope="col">노드</th>
                <th scope="col">공인/사설</th>
                <th scope="col">서비스</th>
                <th scope="col">IP블록</th>
                <th scope="col">GW IP</th>
                <th scope="col" title="단위블록수(IPV4:/24, IPV6:/64)">단위블록수</th>
              </tr>
            </thead>
            <tbody>
              <!-- <tr
                v-for="(item, index) in allocTargetList"
                :key="index"
                :class="{ subbg: index % 2 !== 0, last: index === allocTargetList.length - 1 }"
              >
                <td class="first ellipsis" :title="item.ssvcLineTypeNm">{{ item.ssvcLineTypeNm }}</td>
                <td class="ellipsis" :title="item.ssvcObjNm">{{ item.ssvcObjNm }}</td>
                <td class="ellipsis" :title="item.sipCreateTypeNm">{{ item.sipCreateTypeNm }}</td>
                <td class="ellipsis" :title="item.sassignTypeNm">{{ item.sassignTypeNm }}</td>
                <td class="ellipsis" :title="item.pipPrefix">{{ item.pipPrefix }}</td>
                <td>
                  <div class="inline-block w33">
                    <select v-model="item.gwipType" @change="fnSetGwip(item)">
                      <option value="direct">직접입력</option>
                      <option value="first">시작IP</option>
                      <option value="last">끝IP</option>
                    </select>
                  </div>
                  <div class="inline-block w60">
                    <input
                      v-model="item.slastAddrGwip"
                      type="text"
                      class="txt w95"
                      @keyup="checkInput($event, 'IPonly')"
                    />
                  </div>
                </td>
                <td class="ellipsis" :title="formatNumber(item.nclassCnt)">
                  {{ formatNumber(item.nclassCnt) }}
                </td>
              </tr> -->
            </tbody>
          </table>
        </div>

        <div id="divSvcAloArea" class="content_result">
          <h4>할당 대상 정보</h4>
          <table id="baseTable" class="tbl_list mt5" summary="할당 대상 정보">
            <colgroup>
              <col width="9%" /><col width="10%" /><col width="9%" /><col width="10%" /><col width="23%" /><col width="27%" /><col width="12%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col">서비스망</th>
                <th scope="col">노드</th>
                <th scope="col">공인/사설</th>
                <th scope="col">서비스</th>
                <th scope="col">IP블록</th>
                <th scope="col">GW IP</th>
                <th scope="col">단위블록수</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!ipAllocOperMstVos.length">
                <td class="first" colspan="8">조회된 결과 목록이 존재하지 않습니다.</td>
              </tr>
              <tr v-for="(item, index) in ipAllocOperMstVos" :key="index" :class="{'subbg': index % 2 !== 0, 'last': index === ipAllocOperMstVos.length - 1}">
                <td class="first ellipsis">{{ item.ssvcLineTypeNm }}</td>
                <td class="ellipsis">{{ item.ssvcObjNm }}</td>
                <td class="ellipsis">{{ item.sipCreateTypeNm }}</td>
                <td class="ellipsis">{{ item.sassignTypeNm }}</td>
                <td class="ellipsis">{{ item.pipPrefix }}</td>
                <td>
                  <div class="inline-block w33">
                    <select v-model="item.gwipType" @change="fnSetGwip(item, index)">
                      <option value="direct">직접입력</option>
                      <option value="first">시작IP</option>
                      <option value="last">끝IP</option>
                    </select>
                  </div>
                  <div class="inline-block d-flex items-center w60">
                    <!-- sfirstAddrGwip, slastAddrGwip -->
                    <input v-model="item.addrGwip" type="text" class="txt w95" style="height: 20px" @keyup="(e)=> checkInput(e, 'IPonly')" />
                  </div>
                </td>
                <td class="ellipsis">{{ formatNumber(item.nclassCnt) }}</td>
                <!-- <td style="display: none;">{{ item.nipAssignMstSeq }}</td>
                <td style="display: none;">{{ item.sassignTypeCd }}</td>
                <td style="display: none;">{{ item.sipCreateTypeCd }}</td>
                <td style="display: none;">{{ item.ssvcLineTypeCd }}</td>
                <td style="display: none;">{{ item.nlvlMstSeq }}</td>
                <td style="display: none;">{{ item.nipAllocMstCnt }}</td>
                <td style="display: none;">{{ item.slastAddrGwip }}</td>
                <td style="display: none;">{{ item.sfirstAddrGwip }}</td>
                <td style="display: none;">{{ item.nipmsSvcSeq }}</td>
                <td style="display: none;">{{ item.sipVersionTypeCd }}</td> -->
              </tr>
            </tbody>
          </table>

          <!-- <div class="btn_area mt10">
            <span>
              <a href="#none" @click="fnViewAllocCheckTacsIpBlock">
                <input type="image" src="path_to_image/btn_delegate_off.gif" value="배정" @mouseover="menuOver" @mouseout="menuOut" />
              </a>
            </span>
            <span>
              <a href="#none" @click="fnInsertAlcCloseBtnClick">
                <input type="image" src="path_to_image/btn_close_off.gif" value="닫기" @mouseover="menuOver" @mouseout="menuOut" />
              </a>
            </span>
          </div> -->
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="fnViewAllocCheckTacsIpBlock()">
          할당
        </el-button>
        <el-button size="mini" class="el-icon-close" @click="close()">
          닫기
        </el-button>
      </div>
    </el-dialog>
    <ModalFacilityInformation ref="ModalFacilityInformation" @selected-value="setSelectedRow" />
    <ModalLinkInformation ref="ModalLinkInformation" @selected-value="setSelectedRow" />
    <ModalIpAllocCircuitDetail ref="ModalIpAllocCircuitDetail" @closeCircuitDetail="loadDetailSubSvcMst" />
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { checkInput } from '@/views-ipms/js/common-function'

import ModalFacilityInformation from '@/views-ipms/modal/ModalFacilityInformation.vue'
import ModalLinkInformation from '@/views-ipms/modal/ModalLinkInformation.vue'
import ModalIpAllocCircuitDetail from '@/views-ipms/modal/alloc/ModalIpAllocCircuitDetail.vue'

const routeName = 'ModalIpAllocInsert'

export default {
  name: routeName,
  components: { ModalFacilityInformation, ModalLinkInformation, ModalIpAllocCircuitDetail },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      checkInput,
      // allocParam: {},
      infoValue: '',
      sFlgAllocType: 'N',
      vAllocType: 'allocNe',
      gwipType: 'last',
      returnFlag: null,
      allocTargetList: [], // allocTel일 경우 조회
      ipAllocOperMstVos: [], // 할당 처리할 rows 정보
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
          szaofficesname: 'szofficescodeNm',
          sanealias: 'sanealias',
          sznealias: 'sznealias',
          samstip: 'samstip',
          szmstip: 'szmstip',
          saifname: 'saifname',
          szifname: 'szifname',
          sllnum: 'sllnum',
          nipLinkMstSeq: 'nipLinkMstSeq', // ssubsclgipportdescription
          sconnAlias: 'sconnAlias'
        } },
        allocTel: { label: '회선정보', modalName: 'ModalIpAllocCircuitDetail', valueName: 'sllnum', paramKey: {
          nipAllocMstSeq: 'nipAllocMstSeq'
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
          row['gwipType'] = 'last'
          row['addrGwip'] = row.slastAddrGwip
      })
        this.ipAllocOperMstVos = model.ipAllocOperMstVos
      }
      this.init()
    },
    onClose() {
      this.infoValue = ''
    },
    init() {
      Object.keys(this.infoModalParameterKey).forEach(key => {
        this.srcIpAllocMst[key] = ''
      })

      const { sneossDdYn, ssvcLineTypeCd, nbitmask } = this.ipAllocOperMstVos[0]
      this.sFlgAllocType = sneossDdYn
      this.vAllocType = 'allocNe'
      if (['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd) && ['30', '29'].includes(nbitmask)) {
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
      item['addrGwip'] = value
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
      const { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd } = this.ipAllocOperMstVos[0]
      const ipBlockMstVo = { ssvcLineTypeCd, ssvcGroupCd, ssvcObjCd }

      if (this.allocType === 'allocTel') {
        if (this.infoValue.length <= 4) {
          onMessagePopup(this, '회선정보 조회값을 입력해주세요.')
          return
        } else {
          Object.assign(ipBlockMstVo, { sllnum: null, ssaid: null })
          ipBlockMstVo[this.srhSvcTypeCd] = this.infoValue // sllnum or ssaid setting
        }
      }
      this.$refs[this.infoModalName].open({ ipBlockMstVo })
    },
    async loadDetailSubSvcMst() { // 회선할당정보 조회
      try {
        // 레거시 url: ipmgmt/alloccmgmt/selectDetailSubSvcMstList.json
        /*
        const ipBlockMstVo = { sllnum: '', ssaid: '' }
        ipBlockMstVo[this.srhSvcTypeCd] = this.infoValue

        const res = await api(ipBlockMstVo)
        this.allocTargetList = res.ipAllocOperMstVos
         */
      } catch (error) {
        this.error(error)
      }
    },
    setSelectedRow(param) {
      const { selectedRow, returnFlag } = param
      this.infoValue = selectedRow[this.infoValueName]
      this.selectedRow = selectedRow
      this.returnFlag = returnFlag

      Object.keys(this.infoModalParameterKey).forEach(key => {
        this.srcIpAllocMst[key] = selectedRow[this.infoModalParameterKey[key]]
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
        if (['CL0001', 'CL0002', 'CL0003'].includes(ssvcLineTypeCd) || sipVersionTypeCd !== 'CV0001') {
          this.fnInsertConfirmBtnClick()
        } else {
          /* Tacs Ip중복 체크 api 요청
            const { nipAssignMstSeq } = this.ipAllocOperMstVos[0]
            try {
              const res = await apiCheckTacsIpBlock({ nipAssignMstSeq, typeFlag: 'ALLOC' })
              this.ipCheckCommonMsg = res.resultListVo.commonMsg
              this.$refs.ModalCheckTacsIpBlock.open(res)
            } catch (error) {
              this.error(error)
            }
            if(this.ipCheckCommonMsg !== 'SUCCESS') {
              this.confirm(this.ipCheckCommonMsg + '그래도 할당진행을 하시겠습니까?', '알림', {
                confirmButtonText: '확인',
                cancelButtonText: '취소',
                type: 'warning',
              }).then(() => {
                this.fnInsertConfirmBtnClick()
              })
            }
          */

        }
      }
      /* ip블럭 중복체크 확인 */
      /* 할당 */
      // / ssubscnescode
      // Implement the function to check TACS IP block
    },
    async fnInsertConfirmBtnClick() {
      /*
      시설, 링크 공통
      sicisofficescode
      ssubscnealias // 링크: 자국장비
      ssubscmstip // 링크: 자국 IP
      sllnum
      ssubsclgipportdescription

      시설
      smodelname
      ssubscnnescode

      링크
      nipLinkMstSeq
      sconnAlias

      회선
      nipAllocMstSeq

      최종 param
      - ipAllocMstComplexVo {
        srcIpAllocMstVo : {}
        destIpAllocMstVos: [ ipAllocOperMstVo, ipAllocOperMstVo ..]
      }
      */
    let srcIpAllocMstVo = {}
    if (this.allocType === 'allocNe' && this.allocType === this.returnFlag) { // 시설
      const { sicisofficescode, smodelname, ssubscnealias, ssubscmstip, ssubscnnescode, sllnum } = this.srcIpAllocMst
      const ssubsclgipportdescription = this.srcIpAllocMst.ssubsclgipportdescription

      srcIpAllocMstVo = { sicisofficescode, ssubscnealias, smodelname, ssubscmstip, ssubscnnescode, sllnum, ssubsclgipportdescription }
    } else if (this.allocType === 'allocLen' && this.allocType === this.returnFlag) { // 링크
      const {
        saofficesname: sicisofficescode,
        sanealias: ssubscnealias,
        samstip: ssubscmstip,
        saifname: ssubsclgipportdescription,
        sllnum,
        nipLinkMstSeq,
        sconnAlias
      } = this.srcIpAllocMst

      srcIpAllocMstVo = {
        sicisofficescode, // 자국수용국
        ssubscnealias, // 자국 장비명
        ssubscmstip, // 자국 대표IP
        ssubsclgipportdescription, // 자국IF명
        sllnum, // 전용번호
        nipLinkMstSeq, // 전용번호
        sconnAlias // 수용회선명
      }
    } else if (this.allocType === 'allocTel' && this.vAllocType === this.returnFlag) {
      srcIpAllocMstVo = { nipAllocMstSeq: this.srcIpAllocMst.nipAllocMstSeq }
    } else {
      onMessagePopup(this, '할당 할 회선/시설/링크 정보를 선택해주세요.')
      return
    }

     let chkGwipFlag = 'N'
     const destIpAllocMstVos = []

     this.ipAllocOperMstVos.forEach(row => {
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
     const ipAllocMstComplexVo = { srcIpAllocMstVo, destIpAllocMstVos }
     /*
     try {
      const res = await api(ipAllocMstComplexVo)
      // 레거시 url: ipmgmt/allocmgmt/insertAlcIPmst.json

      if(res.ipAllocOperMstVo.commonMsg === 'SUCCESS') {
        onMessagePopup(this, 'IP블록 할당이 정상적으로 처리되었습니다.')
        this.close()
      } else {
        onMessagePopup(this, res.ipAllocOperMstVo.commonMsg)
      }
     } catch (error) {
      this.error(error)
     }
     */
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
