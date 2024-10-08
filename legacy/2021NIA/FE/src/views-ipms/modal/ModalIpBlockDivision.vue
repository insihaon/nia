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
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP블록분할
        <hr>
      </span>
      <div id="content" class="layer w-100 h-100">
        <div class="content_result mt0">
          <h4>IP 블록 기본 정보</h4>
          <table class="tbl_data mt5">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">계위</th>
                <td>{{ resultVo.ssvcLineTypeNm }} - {{ resultVo.ssvcGroupNm }} - {{ resultVo.ssvcObjNm }}</td>
                <th scope="row">할당상태</th>
                <td>{{ resultVo.sassignLevelNm }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">IP 버전</th>
                <td>{{ resultVo.sipVersionTypeNm }}</td>
                <th scope="row">IP 주소</th>
                <td>{{ resultVo.pipPrefix }}</td>
              </tr>
              <tr>
                <th class="first" scope="row">시작 IP</th>
                <td>{{ resultVo.sfirstAddr }}</td>
                <th scope="row">끝 IP</th>
                <td>{{ resultVo.slastAddr }}</td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">총 IP 수</th>
                <td>{{ formattedNcnt }}</td>
                <th scope="row">단위블록수</th>
                <td>{{ formattedNclassCnt }}</td>
              </tr>
            </tbody>
          </table>
          <!-- <input type="hidden" id="baseNipAssignMstSeq" :value="resultVo.nipAssignMstSeq" />
          <input type="hidden" id="baseNlvlMstSeq" :value="resultVo.nlvlMstSeq" />
          <input type="hidden" id="baseSipVersionTypeCd" :value="resultVo.sipVersionTypeCd" />
          <input type="hidden" id="basePipPrefix" :value="resultVo.pipPrefix" />
          <input type="hidden" id="baseSassignLevelCd" :value="resultVo.sassignLevelCd" /> -->
        </div>

        <div class="content_result">
          <h4>분할 정보</h4>
          <table class="tbl_data entry mt5">
            <colgroup>
              <col width="15%" />
              <col width="85%" />
            </colgroup>
            <tbody>
              <tr class="top">
                <th class="first" scope="row">분할단위</th>
                <td>
                  <input v-model="baseBitmask" type="text" class="txt" style="width: 100px;height: 21px;" maxlength="3" />
                  <span class="ml-2" style="font-weight: 600;">BitMask</span>
                  <span class="ml-2">
                    <el-button size="mini" @click="fnAppendDivAsgnIPMst()">분할</el-button>
                  </span>
                </td>
              </tr>
              <tr class="last">
                <th class="first" scope="row">비고</th>
                <td>
                  <textarea v-model="divScomment" class="w98" rows="3" maxlength="4000"></textarea>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="content_result">
          <h4>분할 예정 정보</h4>
          <table id="baseTable" class="tbl_list mt5" summary="분할 예정 정보">
            <caption>분할 예정 정보</caption>
            <colgroup>
              <col width="6%" />
              <col width="15%" />
              <col width="15%" />
              <col width="20%" />
              <col width="12%" />
              <col width="10%" />
              <col width="12%" />
              <col width="10%" />
            </colgroup>
            <thead>
              <tr>
                <th class="first" scope="col"><input v-model="selectAll" type="checkbox" class="check" @change="toggleAll" /></th>
                <th scope="col">IP블록</th>
                <th scope="col">시작 IP</th>
                <th scope="col">끝 IP</th>
                <th scope="col" title="단위블록수(IPV4:/24, IPV6:/64)">단위블록수</th>
                <th scope="col">BitMask</th>
                <th scope="col">분할단위</th>
                <th scope="col">분할</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(info, index) in divisionInfo" :key="index">
                <td class="first"><input v-model="info.selected" type="checkbox" class="check" /></td>
                <td class="ellipsis">{{ info.pipPrefix }}</td>
                <td class="ellipsis">{{ info.sfirstAddr }}</td>
                <td class="ellipsis">{{ info.slastAddr }}</td>
                <td class="ellipsis">{{ info.nclassCnt }}</td>
                <td class="ellipsis">{{ info.nbitmask }}</td>
                <td><input v-model="info.nsubnetmask" type="text" class="txt" maxlength="3" style="width: 100px;height: 21px;" /></td>
                <td class="btn_text">
                  <el-button size="mini" @click.prevent="fnAppendSubDivAlocIPMst(info)">분할</el-button>
                </td>
              </tr>
            </tbody>
          </table>

          <!-- <div class="btn_area mt10">
            <span>
              <a href="#none" @click.prevent="mergeDivision">
                <input type="image" src="/resources/images/content/btn_merge_off.gif" alt="병합" />
              </a>
            </span>
            <span>
              <a href="#none" @click.prevent="confirmDivision">
                <input type="image" src="/resources/images/content/btn_psubmit_off.gif" alt="분할확정" />
              </a>
            </span>
            <span>
              <a href="#none" @click.prevent="closeDivision">
                <input type="image" src="/resources/images/content/btn_close_off.gif" alt="닫기" />
              </a>
            </span>
          </div> -->
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="fnDivMergeBtnClick">병합</el-button>
        <el-button size="mini" icon="el-icon-menu" @click="fnInsertListDivAsgnIPMst">분할확정</el-button>
        <el-button size="mini" type="primary" class="el-icon-close" @click.native="close()">
          닫기
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils/index'
// import CompTable from '@/components/elTable/CompTable.vue'
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
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.isViewTypeSeonbeonjang = model.isSeonbeonjang ?? false
      if (model.result) {
        this.$set(this, 'resultVo', model.result)
      }
      this.init()
    },
    onClose() {
      this.divisionInfo = []
    },
    init() {
      let bitMask = ''
      const sipVersionTypeCd = this.resultVo.sipVersionTypeCd
      const nbitmask = parseInt(this.resultVo.nbitmask)
      if (sipVersionTypeCd === 'CV0001') {
        bitMask = (nbitmask + 1) >= 32 ? 32 : nbitmask + 1
      } else if (sipVersionTypeCd === 'CV0002') {
        bitMask = (nbitmask + 1) >= 128 ? 128 : nbitmask + 1
      }
      this.baseBitmask = bitMask
    },
    isCheckSubnet(bitmask, nsubnetmask, sipVersionTypeCd) {
      let result = true
      if (nsubnetmask <= bitmask) {
        onMessagePopup(this, 'SubNet이 BitMask 보다 작거나 같을 수 없습니다.')
        result = false
      } else {
        if ((bitmask + 8) < nsubnetmask) {
          onMessagePopup(this, 'SubNet이 기준정보의 BitMask 보다 8Bit 이상 분할이 불가합니다.')
          result = false
        } else if (sipVersionTypeCd === 'CV0001') {
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
      return result
    },
    async fnAppendDivAsgnIPMst() {
      const pipPrefix = this.resultVo.pipPrefix
      const sipVersionTypeCd = this.resultVo.sipVersionTypeCd
      const bitmask = parseInt(this.resultVo.nbitmask)
      const nsubnetmask = parseInt(this.baseBitmask)
      if (!this.isCheckSubnet(bitmask, nsubnetmask, sipVersionTypeCd)) {
        return
      }
      const tbIpAssignMstVo = { pipPrefix, sipVersionTypeCd, nsubnetmask }
      const divInfo = await this.getMergeNsubnetmask(tbIpAssignMstVo)
      this.divisionInfo = this._cloneDeep(divInfo)
    },
    async fnAppendSubDivAlocIPMst(subRow) {
      const nsubnetmask = parseInt(subRow.nsubnetmask)
      const { nbitmask, pipPrefix, sipVersionTypeCd } = subRow
      if (!this.isCheckSubnet(nbitmask, nsubnetmask, sipVersionTypeCd)) {
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
    /* nsubnetmask 셋하는 부분 공통화 */
    async getMergeNsubnetmask(tbIpAssignMstVo) {
      let divInfo
      try {
        const res = await apiRequestJson(ipmsJsonApis.appendDivAsgnIPMst, tbIpAssignMstVo)
        divInfo = res.tbIpAssignMstVos
        divInfo.forEach(row => {
          if (row.sipVersionTypeCd === 'CV0001') {
            row['nsubnetmask'] = (row.nbitmask + 1) >= 32 ? 32 : row.nbitmask + 1
          } else if (row.sipVersionTypeCd === 'CV0002') {
            row['nsubnetmask'] = (row.nbitmask + 1) >= 128 ? 128 : row.nbitmask + 1
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
        menuType: 'Aloc',
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
