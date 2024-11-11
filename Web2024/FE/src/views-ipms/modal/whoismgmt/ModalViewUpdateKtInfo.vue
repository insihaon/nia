<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="이용 기관 관리"
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
      <table>
        <tbody>
          <tr class="top">
            <th>한글 기관명(한 단어)</th>
            <td>
              <el-input
                v-model="resultVo.sorgname"
                size="small"
                maxlength="200"
              />
            </td>
          </tr>

          <tr>
            <th>한글 주소</th>
            <td>
              <el-input
                v-model="resultVo.sadmAddr"
                size="small"
                disabled
              >
                <el-button type="primary" size="small" round @click="fnViewSeachAddrPop('kt')">주소검색</el-button>
              </el-input>
            </td>
          </tr>
          <tr>
            <th>한글 상세 주소</th>
            <td>
              <el-input
                v-model="resultVo.sadmAddrDetail"
                title="한글 상세 주소"
                disabled
                size="small"
              />
            </td>
          </tr>

          <tr>
            <th>우편 번호</th>
            <td>
              <el-input
                v-model="resultVo.sadmZipcode"
                title="우편번호 앞자리"
                disabled
                size="small"
              />
            </td>
          </tr>

          <tr>
            <th>영문 기관명</th>
            <td>
              <el-input
                v-model="resultVo.seorgname"
                maxlength="200"
                size="small"
              />
            </td>
          </tr>

          <tr>
            <th>영문 주소</th>
            <td>
              <el-input
                v-model="resultVo.sadmEaddr"
                size="small"
              />
            </td>
          </tr>

          <tr>
            <th>영문 상세 주소</th>
            <td>
              <el-input
                id="ktInfoEaddrDetail"
                v-model="resultVo.sadmEaddrDetail"
                class="txt w98"
                title="영문 상세 주소"
                size="small"
              />
            </td>
          </tr>

          <tr>
            <th>전화번호</th>
            <td>
              <span class="ml3">+ 82</span>
              <span class="ml5 mr5">-</span>
              <el-input
                v-model="formattedPhone"
                size="small"
                maxlength="50"
              />
              <em>(예: 2-222-2222)</em>
            </td>
          </tr>

          <tr class="last">
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
      <el-button type="primary" size="small" round @click.native="fnKeywordDel()">저장</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalViewUpdateKtInfo'

export default {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      selectedRow: null,
       tableColumns: [
        { prop: 'suserorggb', label: '고객명', width: 200, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '대체기준', align: 'center', width: 200, sortable: true, columnVisible: true, showOverflow: true },
      ],
      resultVo: {},
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
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const param = { pageUnit, pageIndex }
      try {
       this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewUpdateKtInfo, param)
        this.pagination.data = res.resultListVo.tbWhoisKeywordVos ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    fnKeywordDel() { /* 삭제 */

    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
