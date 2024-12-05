<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="KT 대체 정보 수정"
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
    <div class="popupContentTable">
      <table v-loading="tableLoading">
        <tbody>
          <tr>
            <th>한글 기관명(한 단어)</th>
            <td>
              <el-input v-model="resultVo.sorgname" size="small" maxlength="200" />
            </td>
          </tr>

          <tr>
            <th>한글 주소</th>
            <td class="textflex">
              <el-input v-model="resultVo.sadmAddr" size="small" disabled />
              <el-button type="primary" size="small" round @click="fnViewSeachAddrPop('kt')">주소검색</el-button>
            </td>
          </tr>
          <tr>
            <th>한글 상세 주소</th>
            <td>
              <el-input v-model="resultVo.sadmAddrDetail" disabled size="small" />
            </td>
          </tr>
          <tr>
            <th>우편 번호</th>
            <td>
              <el-input v-model="resultVo.sadmZipcode" disabled size="small" />
            </td>
          </tr>
          <tr>
            <th>영문 기관명</th>
            <td>
              <el-input v-model="resultVo.seorgname" maxlength="200" size="small" />
            </td>
          </tr>
          <tr>
            <th>영문 주소</th>
            <td>
              <el-input v-model="resultVo.sadmEaddr" size="small" />
            </td>
          </tr>
          <tr>
            <th>영문 상세 주소</th>
            <td>
              <el-input
                v-model="resultVo.sadmEaddrDetail"
                title="영문 상세 주소"
                size="small"
              />
            </td>
          </tr>
          <tr>
            <th>전화번호</th>
            <td class="textflex" style="white-space: nowrap;">
              <span>+ 82</span>
              <span>-</span>
              <el-input
                v-model="resultVo.sadmDpphone"
                size="small"
                maxlength="50"
              />
              <em style="font-size: 12px;">(예: 2-222-2222)</em>
            </td>
          </tr>
          <tr>
            <th>전자우편주소</th>
            <td>
              <el-input
                id="ktInfoEmail"
                v-model="resultVo.sadmEmail"
                size="small"
                maxlength="100"
              />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-check" round @click.native="fnSaveKtInfo()">저장</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
    <ModalSearchZipCode ref="ModalSearchZipCode" @setAddrForm="setAddrForm" />
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'
import ModalSearchZipCode from '@/views-ipms/modal/notice/ModalSearchZipCode.vue'

const routeName = 'ModalViewUpdateKtInfo'

export default {
  name: routeName,
  components: { ModalSearchZipCode },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableLoading: false,
      pagination: this.setDefaultPagination(),
      selectedRow: null,
       tableColumns: [
        { prop: 'suserorggb', label: '고객명', width: 200, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '대체기준', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
      ],
      resultVo: {
        sorgname: '',
        sadmAddr: '',
        sadmAddrDetail: '',
        sadmZipcode: '',
        seorgname: '',
        sadmEaddr: '',
        sadmEaddrDetail: '',
        sadmDpphone: '',
        sadmEmail: '',
      },
      suserorggbOp: [
        { label: '고객명 일치', value: '10' },
        { label: '고객명 포함', value: '20' },
      ],
      suserorggb: '',
      sorgname: '',
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      setTimeout(() => {
        this.fnViewListWhoisKeywordMstNew()
      }, 10)
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListWhoisKeywordMstNew()
    },
    async fnViewListWhoisKeywordMstNew() {
      try {
        this.tableLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewUpdateKtInfo)
        this.resultVo = res.result.data ?? []
      } catch (error) {
        console.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    async fnSaveKtInfo() { /* KT 대체 정보 수정 */
      if (this.resultVo.sorgname === '') {
        onMessagePopup(this, '한글기관명을 입력하세요.')
        return
      }

      if (this.resultVo.saddr === '') {
        onMessagePopup(this, '한글주소를 입력하세요.')
        return
      }

      if (this.resultVo.szipcode === '') {
        onMessagePopup(this, '우편주소를 입력하세요.')
        return
      }

      if (this.resultVo.seorgname === '') {
        onMessagePopup(this, '영문기관명을 입력하세요.')
        return
      }

      if (this.resultVo.seaddr === '') {
        onMessagePopup(this, '영문주소를 입력하세요.')
        return
      }

      try {
        this.tableLoading = true
        const ktInfoVo = {
          sorgname: this.resultVo.sorgname,
          saddr: this.resultVo.saddr,
          sadmAddr: this.resultVo.sadmAddr,
          saddrDetail: this.resultVo.saddrDetail,
          sadmAddrDetail: this.resultVo.sadmAddrDetail,
          szipcode: this.resultVo.szipcode,
          sadmZipcode: this.resultVo.sadmZipcode,
          seorgname: this.resultVo.seorgname,
          sadmEorgname: this.resultVo.sadmEorgname,
          seaddr: this.resultVo.seaddr,
          sadmEaddr: this.resultVo.sadmEaddr,
          seaddrDetail: this.resultVo.seaddrDetail,
          sadmEaddrDetail: this.resultVo.sadmEaddrDetail,
          sadmDpphone: this.resultVo.sadmDpphone,
          sadmEmail: this.resultVo.sadmEmail
        }
        const res = await apiRequestJson(ipmsJsonApis.updateKtInfo, ktInfoVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, 'KT 대체 정보가 정상적으로 수정되었습니다')
           this.fnViewListWhoisKeywordMstNew()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.tableLoading = false
      }
    },
    fnViewSeachAddrPop(type) { /* 주소 검색 */
      this.$refs.ModalSearchZipCode.open({ type: type })
    },
    setAddrForm(Addr) {
      const addrObj = {
        sadmAddr: Addr.newkaddr, // 새 한글주소
        sadmAddrDetail: Addr.detailAddress, // 디테일 주소
        sadmZipcode: Addr.zipcode, // 우편번호
        sadmEaddr: Addr.eaddr // 새 영어주소
      }
      Object.assign(this.resultVo, addrObj)
    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
