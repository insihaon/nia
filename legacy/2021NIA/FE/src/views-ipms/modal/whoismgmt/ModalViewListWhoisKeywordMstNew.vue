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
        <tr>
          <th>
            <label>고객명</label>
          </th>
          <td>
            <div>
              <el-input v-model="sorgname"></el-input>
            </div>
          </td>
          <td>
            <el-button class="float-right my-2" size="small" type="primary"> 조회 </el-button>
          </td>
        </tr>
      </table>
      <table>
        <tr>
          <th>
            <label>고객명</label>
          </th>
          <td>
            <div>
              <el-input v-model="sorgname"></el-input>
            </div>
          </td>
          <th>
            <label>대체 기준</label>
          </th>
          <td>
            <div>
              <el-select
                v-model="suserorggb"
                size="small"
              >
                <el-option
                  v-for="item in suserorggbOp"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </td>
          <td>
            <el-button class="float-right my-2" size="small" type="primary"> 저장 </el-button>
          </td>
        </tr>
      </table>
    </div>
    <el-col :span="24" class="my-2">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-max-select="pagination.data.length"
        :prop-is-check-box="true"
        :text-des="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
      </compTable>
      <ModalInsertKeyword ref="ModalInsertKeyword" />
    </el-col>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round @click.native="fnKeywordDel()">삭제</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalInsertKeyword from '@/views-ipms/modal/whoismgmt/ModalInsertKeyword.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

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
        { prop: 'suserorggb', label: '고객명', width: 400, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '대체기준', align: 'center', width: 400, sortable: true, columnVisible: true, showOverflow: true },
      ],
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
        const res = await apiRequestModel(ipmsModelApis.viewListWhoisKeywordMstNew, param)
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
