<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="대체 키워드 관리"
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
    <div class="popupContentTable mb-2">
      <table>
        <tr>
          <th>
            <label>고객명</label>
          </th>
          <td>
            <el-input v-model="sorgname_search" size="small" @keyup.enter.native="fnViewListIpAllocMstByMain()"></el-input>
          </td>
          <td>
            <el-button size="small" round type="primary" @click="fnViewListWhoisKeywordMstNew()"> 조회 </el-button>
          </td>
        </tr>
      </table>
      <div class="popupContentTableTitle">등록</div>
      <table>
        <tr>
          <th>
            <label>고객명</label>
          </th>
          <td>
            <el-input v-model="sorgname_insert" size="small"></el-input>
          </td>
          <th>
            <label>대체 기준</label>
          </th>
          <td>
            <el-select v-model="sreplace_cd" size="small">
              <el-option
                v-for="item in suserorggbOp"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <td>
            <el-button size="small" round type="primary" @click="fnInsertWhoisKeywordSubmit()"> 저장 </el-button>
          </td>
        </tr>
      </table>
    </div>
    <el-col :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-max-select="pagination.data.length"
        :prop-is-check-box="true"
        :prop-on-select="handleClickTableCheck"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
      </compTable>
      <ModalInsertKeyword ref="ModalInsertKeyword" />
    </el-col>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-delete" round @click.native="fnKeywordDel()">삭제</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalInsertKeyword from '@/views-ipms/modal/whoismgmt/ModalInsertKeyword.vue'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'

const routeName = 'ModalViewListWhoisKeywordMstNew'

export default {
  name: routeName,
  components: { CompTable, ModalInsertKeyword },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      selectedRow: null,
       tableColumns: [
        { prop: 'sorgname', label: '고객명', width: 450, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sreplace_nm', label: '대체기준', align: 'center', width: 450, sortable: true, columnVisible: true, showOverflow: true },
      ],
      suserorggbOp: [
        { label: '고객명 일치', value: '10' },
        { label: '고객명 포함', value: '20' },
      ],
      sorgname_search: '',
      sorgname_insert: '',
      sreplace_cd: '10',
      selectedRows: []
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      this.sreplace_cd = '10'
      setTimeout(() => {
        this.fnViewListWhoisKeywordMstNew()
      }, 10)
    },
    handleClickTableCheck(all, cur) {
      this.selectedRows = all
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListWhoisKeywordMstNew()
    },
    async fnViewListWhoisKeywordMstNew() {
      const param = {
        sorgname: this.sorgname_search
      }
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(param, { pageUnit, pageIndex })
      try {
       this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListWhoisKeywordMstNew, param)
        this.pagination.data = res.result?.data ?? []
        this.pagination.total = res.result?.totalCount ?? 0
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async fnInsertWhoisKeywordSubmit() { /* 저장 */
      if (this.sorgname_insert === '') {
        onMessagePopup(this, '고객명을 입력하세요.')
        return
      }
      if (this.sreplace_cd === '') {
        onMessagePopup(this, '대체기준을 선택하세요.')
        return
      }
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const tbWhoisKeywordVo = {
          sorgname: this.sorgname_insert,
          sreplace_cd: this.sreplace_cd,
          sreplace_nm: this.sreplace_cd === '10' ? '고객명 일치' : '고객명 포함'
        }
        const res = await apiRequestJson(ipmsJsonApis.insertWhoisKeywordNew, tbWhoisKeywordVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '정상적으로 등록되었습니다.')
          this.fnViewListWhoisKeywordMstNew()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async fnKeywordDel() { /* 삭제 */
        if (this.selectedRows.length === 0) {
        onMessagePopup(this, '삭제할 대상이 없습니다.')
        return
      }
       const target = ({ vue: this.$refs.content })
      try {
        const tbWhoisKeywordListVo = {
          tbWhoisKeywordVos: []
        }
        this.selectedRows.forEach((item) => {
          tbWhoisKeywordListVo.tbWhoisKeywordVos.push({ nwhoisKeywordSeq: item.nwhoisKeywordSeq })
        })
        const res = await apiRequestJson(ipmsJsonApis.deleteWhoisKeywordNew, tbWhoisKeywordListVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '정상적으로 삭제되었습니다')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
        this.openLoading(target)
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onClose() {
      this.sorgname_search = ''
      this.sorgname_insert = ''
      this.sreplace_cd = ''
      this.selectedRow = []
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
