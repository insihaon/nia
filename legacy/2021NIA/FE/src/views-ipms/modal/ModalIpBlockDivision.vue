<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP블록분할"
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
    <div class="popupContentTable">
      <div class="popupContentTableTitle">IP 블록 기본 정보</div>
      <table v-loading="tableLoading">
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tbody>
          <tr>
            <th>계위</th>
            <td>{{ resultVo.ssvcLineTypeNm }} - {{ resultVo.ssvcGroupNm }} - {{ resultVo.ssvcObjNm }}</td>
            <th>할당상태</th>
            <td>{{ resultVo.sassignLevelNm }}</td>
          </tr>
          <tr>
            <th>IP 버전</th>
            <td>{{ resultVo.sipVersionTypeNm }}</td>
            <th>IP 주소</th>
            <td>{{ resultVo.pipPrefix }}</td>
          </tr>
          <tr>
            <th>시작 IP</th>
            <td>{{ resultVo.sfirstAddr }}</td>
            <th>끝 IP</th>
            <td>{{ resultVo.slastAddr }}</td>
          </tr>
          <tr>
            <th>총 IP 수</th>
            <td>{{ formattedNcnt }}</td>
            <th>단위블록수</th>
            <td>{{ formattedNclassCnt }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTable">
      <div class="popupContentTableTitle">분할 정보</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="85%" />
        </colgroup>
        <tbody>
          <tr>
            <th>분할단위</th>
            <td class="textflex align-left">
              <el-input v-model="baseBitmask" type="text" size="small" round maxlength="3" style="width: 200px" />
              <span style="font-weight: 600;"> BitMask</span>
              <el-button type="primary" size="small" round class="ml-1" @click="fnAppendDivAsgnIPMst()">분할</el-button>
            </td>
          </tr>
          <tr>
            <th>비고</th>
            <td>
              <textarea v-model="divScomment" rows="3" maxlength="4000"></textarea>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTable">
      <div class="popupContentTableTitle">신규 분할</div>
      <table>
        <colgroup>
          <col width="15%" />
          <col width="85%" />
        </colgroup>
        <tbody>
          <tr>
            <th>IP대역</th>
            <td colspan="4" class="text-center">
              {{ resultVo.pipPrefix }}
            </td>
          </tr>
          <tr>
            <th>bitmask</th>
            <td class="textflex align-center">
              <el-input v-model="finalBit" type="text" size="small" round maxlength="3" style="width: 200px" />bit
            </td>
            <th>개수</th>
            <td class="textflex align-center">
              <el-input v-model="finalCount" type="text" size="small" round maxlength="3" style="width: 200px" />개
            </td>
            <td>
              <el-button type="primary" size="small" round class="ml-1" @click="fnNewAppendDivAsgnIPMst()">분할</el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="popupContentTable textcenter">
      <div class="popupContentTableTitle">분할 예정 정보</div>
      <div>
        <table>
          <!-- <colgroup>
            <col width="6%" />
            <col width="15%" />
            <col width="15%" />
            <col width="20%" />
            <col width="12%" />
            <col width="10%" />
            <col width="12%" />
            <col width="10%" />
          </colgroup> -->
          <thead>
            <tr>
              <th><el-checkbox v-model="selectAll" @change="toggleAll" /></th>
              <th>IP블록</th>
              <th>시작 IP</th>
              <th>끝 IP</th>
              <th title="단위블록수(IPV4:/24, IPV6:/64)">단위블록수</th>
              <th>BitMask</th>
              <th>분할단위</th>
              <th>분할</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(info, index) in divisionInfo" :key="index">
              <td><el-checkbox v-model="info.selected" /></td>
              <td>{{ info.pipPrefix }}</td>
              <td>{{ info.sfirstAddr }}</td>
              <td>{{ info.slastAddr }}</td>
              <td>{{ (info.nclassCnt).toLocaleString() }}</td>
              <td>{{ info.nbitmask }}</td>
              <td><el-input v-model="info.nsubnetmask" type="text" class="txt" maxlength="3" style="width: 100px;height: 21px;" /></td>
              <td>
                <el-button type="danger" size="small" icon="el-icon-scissors" circle @click.prevent="fnAppendSubDivAlocIPMst(info)" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-menu" round @click="fnDivMergeBtnClick">병합</el-button>
      <el-button type="primary" size="small" icon="el-icon-menu" round @click="fnInsertListDivAsgnIPMst">분할확정</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">닫기</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
import { ipmsModelApis, ipmsJsonApis, apiRequestModel, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalIpBlockDivision'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      typeFlag: '',
      finalBit: '',
      finalCount: '',
      tableLoading: false,
      resultVo: {
        ssvcLineTypeNm: '',
        ssvcGroupNm: '',
        ssvcObjNm: '',
        sassignLevelNm: '',
        sipVersionTypeNm: '',
        pipPrefix: '',
        sfirstAddr: '',
        slastAddr: '',
        ncnt: 0,
        nclassCnt: 0,
        nipAssignMstSeq: '',
        nlvlMstSeq: '',
        sipVersionTypeCd: '',
        sassignLevelCd: '',
      },
      baseBitmask: '',
      divScomment: '',
      divisionInfo: [],
      selectAll: false,
      isViewTypeSeonbeonjang: false,
    }
  },
  computed: {
    formattedNcnt() {
      return new Intl.NumberFormat().format(this.resultVo.ncnt)
    },
    formattedNclassCnt() {
      return new Intl.NumberFormat().format(this.resultVo.nclassCnt)
    },
  },
  methods: {
    async fnNewAppendDivAsgnIPMst() {
      if (this.finalBit === '' || this.finalBit === null) {
        onMessagePopup(this, '분할 bit 수를 입력하세요.')
        return
      }
      if (this.finalCount === '' || this.finalCount === null) {
        onMessagePopup(this, '분할 개수를 입력하세요.')
        return
      }
      const sipVersionTypeCd = this.resultVo.sipVersionTypeCd
      if (this.typeFlag === 'Asgn') {
        if (sipVersionTypeCd === 'CV0001') {
          if (this.finalBit > 24) {
            onMessagePopup(this, 'IPv4는 24Bit 이상 배정분할 할 수 없습니다.')
            return
          }
        } else if (sipVersionTypeCd === 'CV0002') {
          if (this.finalBit > 64) {
            onMessagePopup(this, 'IPv6는 64Bit 이상 배정분할 할 수 없습니다.')
            return
          }
        }
      } else { /* 할당 또는 선번장 화면처리 */
        if (sipVersionTypeCd === 'CV0001') {
          if (this.finalBit > 32) {
            onMessagePopup(this, 'IPv4 일 경우 SubNet이 32보다 클 수  없습니다.')
            return
          }
        } else if (sipVersionTypeCd === 'CV0002') {
          if (this.finalBit > 128) {
            onMessagePopup(this, 'IPv6 일 경우 SubNet이 128보다 클 수  없습니다.')
            return
          }
        }
      }
      const initial_bit = this.resultVo.nbitmask
      const final_bit = this.finalBit // 사용자 입력 bitmask(최종 만들어질 서브넷마스크)
      const final_count = this.finalCount // 사용자 입력 최종 bitmask 갯수
      // 1번만 분할하는 갯수를 구함
      const req_obj = {} // ex: { final_bit-1: 분할 수, final_bit-2: 분할 수 ... }
      let div_bit = final_bit - 1
      let req_count = Math.floor(final_count / 2) + (final_count % 2)
      while (req_count > 0) {
        Object.assign(req_obj, { [div_bit]: req_count })
        if (req_count === 1) req_count = 0
        div_bit = div_bit - 1 // 분할예정 bit
        req_count = Math.floor(req_count / 2) + (req_count % 2)
      }

      // 1. 1번 분할 (oneDivCount: 1번만 분할되는 갯수)
      const req_obj_keys = Object.keys(req_obj)
      const oneDivCount = parseInt(req_obj_keys[0]) - initial_bit
      // 1차 분할정보 get
      let divInfo = await this.getDivSubnetmask()

      // 첫 번째 서브넷만 1번씩 계속 분할
      for (const num of [...Array(oneDivCount).keys()]) {
        const tbIpAssignMstVo = divInfo[0]
        const divMergeInfo = await this.getMergeNsubnetmask(tbIpAssignMstVo)
        const cloneDivisionInfo = this._cloneDeep(divInfo)
        const chkRowindex = cloneDivisionInfo.findIndex(orgRow => orgRow.pipPrefix === tbIpAssignMstVo.pipPrefix)
        if (chkRowindex !== -1) {
          cloneDivisionInfo.splice(chkRowindex, 1, ...divMergeInfo)
        }
        divInfo = cloneDivisionInfo
      }
      // 2. n번 분할 ex) final_bit: 24, final_count: 10, req_obj: {20: 1, 21: 2, 22: 3, 23: 5}
      for (const value of Object.values(req_obj)) {
        // value: 분할 횟수
        if (value !== 1) {
          for (const i of [...Array(value).keys()].reverse()) {
            // this._orderBy(divInfo, ['desc', 'nbitmask'])
            const divMergeInfo = await this.getMergeNsubnetmask(divInfo[i])
            await divInfo.splice(i, 1, ...divMergeInfo)
          }
        }
      }
      this.divisionInfo = divInfo
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 900
    },
    async onOpen(model, actionMode) {
      this.isViewTypeSeonbeonjang = model.isSeonbeonjang ?? false
      if (model.row) {
        this.typeFlag = model.typeFlag !== null ? model.typeFlag : ''
        await this.fnViewInsertDivAsgnIPMst(model.row, model.typeFlag)
      }
      this.init()
    },
    onClose() {
      this.divisionInfo = []
    },
    init() {
      this.finalBit = ''
      this.finalCount = ''
      let bitMask = ''
      const sipVersionTypeCd = this.resultVo.sipVersionTypeCd
      const nbitmask = parseInt(this.resultVo.nbitmask)
      if (sipVersionTypeCd === 'CV0001') {
        const limitBit = this.typeFlag === 'Asgn' ? 24 : 32
        bitMask = (nbitmask + 1) >= limitBit ? limitBit : nbitmask + 1
      } else if (sipVersionTypeCd === 'CV0002') {
        const limitBit = this.typeFlag === 'Asgn' ? 64 : 128
        bitMask = (nbitmask + 1) >= limitBit ? limitBit : nbitmask + 1
      }
      this.baseBitmask = bitMask
    },
    isCheckSubnet(nsubnetmask, sipVersionTypeCd) {
      let result = true
      const bitmask = parseInt(this.resultVo.nbitmask)
      if (nsubnetmask <= bitmask) {
        onMessagePopup(this, 'SubNet이 BitMask 보다 작거나 같을 수 없습니다.')
        result = false
      } else {
        if ((bitmask + 8) < nsubnetmask) {
          onMessagePopup(this, 'SubNet이 기준정보의 BitMask 보다 8Bit 이상 분할이 불가합니다.')
          result = false
        } else if (this.typeFlag === 'Asgn') {
          if (sipVersionTypeCd === 'CV0001') {
            if (nsubnetmask > 24) {
              onMessagePopup(this, 'IPv4는 24Bit 이상 배정분할 할 수 없습니다.')
              result = false
            }
          } else if (sipVersionTypeCd === 'CV0002') {
            if (nsubnetmask > 64) {
              onMessagePopup(this, 'IPv6는 64Bit 이상 배정분할 할 수 없습니다.')
              result = false
            }
          }
        } else { /* 할당 또는 선번장 화면처리 */
          if (sipVersionTypeCd === 'CV0001') {
            if (nsubnetmask > 32) {
              onMessagePopup(this, 'IPv4 일 경우 SubNet이 32보다 클 수  없습니다.')
              result = false
            }
          } else if (sipVersionTypeCd === 'CV0002') {
            if (nsubnetmask > 128) {
              onMessagePopup(this, 'IPv6 일 경우 SubNet이 128보다 클 수  없습니다.')
              result = false
            }
          }
        }
      }
      return result
    },
    async fnViewInsertDivAsgnIPMst(row, typeFlag = null) {
      const params = { nipAssignMstSeq: row.nipAssignMstSeq }
      if (typeFlag !== null) {
        Object.assign(params, { typeFlag })
      }
      try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewInsertDivAsgnIPMst, params)
        if (res.result.data) {
          this.$set(this, 'resultVo', res.result.data)
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    async getDivSubnetmask() {
      const pipPrefix = this.resultVo.pipPrefix
      const sipVersionTypeCd = this.resultVo.sipVersionTypeCd
      // const bitmask = parseInt(this.resultVo.nbitmask)
      const nsubnetmask = parseInt(this.baseBitmask)
      if (!this.isCheckSubnet(nsubnetmask, sipVersionTypeCd)) {
        return
      }
      const tbIpAssignMstVo = { pipPrefix, sipVersionTypeCd, nsubnetmask }
      const divInfo = await this.getMergeNsubnetmask(tbIpAssignMstVo)
      return divInfo
    },
    async fnAppendDivAsgnIPMst() {
      const divInfo = await this.getDivSubnetmask()
      this.divisionInfo = this._cloneDeep(divInfo)
    },
    async fnAppendSubDivAlocIPMst(subRow) {
      const nsubnetmask = parseInt(subRow.nsubnetmask)
      const { nbitmask, pipPrefix, sipVersionTypeCd } = subRow
      if (!this.isCheckSubnet(nsubnetmask, sipVersionTypeCd)) {
        return
      }
      const tbIpAssignMstVo = { pipPrefix, sipVersionTypeCd, nsubnetmask }
      const divInfo = await this.getMergeNsubnetmask(tbIpAssignMstVo)
      const cloneDivisionInfo = this._cloneDeep(this.divisionInfo)
      const chkRowindex = cloneDivisionInfo.findIndex(orgRow => orgRow.pipPrefix === subRow.pipPrefix)

      if (chkRowindex !== -1) {
        cloneDivisionInfo.splice(chkRowindex, 1, ...divInfo)
      }
      this.divisionInfo = cloneDivisionInfo
    },
    /* nsubnetmask set 공통화 */
    async getMergeNsubnetmask(tbIpAssignMstVo) {
      let divInfo
      try {
        const res = await apiRequestJson(ipmsJsonApis.appendDivAsgnIPMst, tbIpAssignMstVo)
        divInfo = res.tbIpAssignMstVos
        divInfo.forEach(row => {
          if (row.sipVersionTypeCd === 'CV0001') {
            const limitBit = this.typeFlag === 'Asgn' ? 24 : 32
            row['nsubnetmask'] = (row.nbitmask + 1) >= limitBit ? limitBit : row.nbitmask + 1
          } else if (row.sipVersionTypeCd === 'CV0002') {
            const limitBit = this.typeFlag === 'Asgn' ? 64 : 128
            row['nsubnetmask'] = (row.nbitmask + 1) >= limitBit ? limitBit : row.nbitmask + 1
          }
        })
      } catch (error) {
        this.error(error)
      }
      return divInfo
    },
     async fnDivMergeBtnClick() {
      const mergedList = this.divisionInfo.filter(row => row.selected)
      if (mergedList.length === 0) {
        onMessagePopup(this, '병합할 대상이 없습니다.')
        return
      } else if (mergedList.length === 1) {
        onMessagePopup(this, '병합할 대상은 최소 2개이상 선택해 주시기 바랍니다.')
        return
      }
      const checkedMergeList = []
      const tbIpAssignMstListVo = { tbIpAssignMstVos: [] }
      mergedList.forEach(row => {
        checkedMergeList.push(row)
        tbIpAssignMstListVo.tbIpAssignMstVos.push({ pipPrefix: row.pipPrefix, sipVersionTypeCd: row.sipVersionTypeCd })
      })
      try {
        const res = await apiRequestJson(ipmsJsonApis.appendMergeDivAsgnIPMst, tbIpAssignMstListVo)
        if (res.commonMsg === 'SUCCESS') {
          const { nbitmask, sipVersionTypeCd } = res
          if (sipVersionTypeCd === 'CV0001') {
            res.nsubnetmask = (res.nbitmask + 1) >= 32 ? 32 : nbitmask + 1
          } else if (sipVersionTypeCd === 'CV0002') {
            res.nsubnetmask = (res.nbitmask + 1) >= 128 ? 128 : nbitmask + 1
          }
          const cloneDivisionInfo = this._cloneDeep(this.divisionInfo)
          const mergeFirstIndex = cloneDivisionInfo.findIndex(row => row.pipPrefix === checkedMergeList[0].pipPrefix)
          if (mergeFirstIndex !== -1) {
            cloneDivisionInfo.splice(mergeFirstIndex, checkedMergeList.length, res)
          }
          cloneDivisionInfo.forEach(v => { v.selected = false })
          this.divisionInfo = cloneDivisionInfo
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        this.error(error)
      }
    },
    async fnInsertListDivAsgnIPMst() {
      const confirmList = this.divisionInfo
      if (confirmList.length === 0) {
        onMessagePopup(this, '분할확정 대상 정보가 없습니다.')
        return
      } else if (confirmList.length === 1) {
        onMessagePopup(this, '분할확정 대상 정보가 기본 정보와 같습니다.')
        return
      }
      const { nipAssignMstSeq, nlvlMstSeq, sipVersionTypeCd, sassignLevelCd } = this.resultVo
      const tbIpAssignMstComplexVo = {
        menuType: this.typeFlag,
        srcIpAssignMstVo: { nipAssignMstSeq, nlvlMstSeq, sipVersionTypeCd, sassignLevelCd },
        destIpAssignMstVos: []
      }
        confirmList.forEach(row => {
          const { pipPrefix, sipVersionTypeCd } = row
          tbIpAssignMstComplexVo.destIpAssignMstVos.push({
            pipPrefix,
            sipVersionTypeCd,
            screateId: this.$store.state.user.info.suserId,
            smodifyId: this.$store.state.user.info.suserId,
            scomment: this.divScomment
          })
        })
        try {
          const res = await apiRequestJson(ipmsJsonApis.insertListDivAsgnIPMst, tbIpAssignMstComplexVo)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, 'IP블록 분할이 정상적으로 처리되었습니다.')
            this.$emit('reload')
            this.close()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
    },
    closeDivision() {
      // Implement the functionality for closing the division
    },
    toggleAll() {
      this.divisionInfo.forEach(info => { info['selected'] = this.selectAll })
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
