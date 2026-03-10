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
    <div ref="content">
      <div class="popupContentTable">
        <table>
          <tr>
            <th>
              <label>기관 구분</label>
            </th>
            <td>
              <div>
                <el-select
                  v-model="suserorggb"
                  size="small"
                  @keyup.enter.native="fnViewListWhoisKeywordMst()"
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
            <th>
              <label>기관명</label>
            </th>
            <td>
              <div>
                <el-input v-model="sorgname" @keyup.enter.native="fnViewListWhoisKeywordMst()"></el-input>
              </div>
            </td>
            <td>
              <el-button class="float-right my-2" type="primary" size="small" round @click="fnViewListWhoisKeywordMst()">조회</el-button>
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
          :prop-table-height="'100%'"
          :prop-column="tableColumns"
          :prop-on-select="handleClickTableCheck"
          :prop-max-select="pagination.data.length"
          :prop-is-check-box="true"
          prop-grid-menu-id="inputSpeed"
          :prop-grid-indx="1"
          :prop-enabled-excel-down="false"
          :prop-on-page-change="handleChangeCurPage"
          :prop-on-page-size-change="handleChangeCurPage"
        >
        </compTable>
        <ModalInsertKeyword ref="ModalInsertKeyword" />
      </el-col>
    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" round icon="el-icon-check" @click.native="fnViewKeywordInsert()">등록</el-button>
      <el-button type="primary" size="small" round icon="el-icon-delete" @click.native="fnKeywordDel()">삭제</el-button>
      <el-button type="primary" size="small" round icon="el-icon-close" @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalInsertKeyword from '@/views-ipms/modal/whoismgmt/ModalInsertKeyword.vue'
import { apiRequestJson, ipmsJsonApis, apiRequestModel, ipmsModelApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'ModalViewListWhoisKeywordMst'

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
       tableColumns: [
        { prop: 'suserorggb', label: '기관구분', width: 500, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgname', label: '기관명', align: 'center', width: 500, sortable: true, columnVisible: true, showOverflow: true },
      ],
      suserorggbOp: [
        { label: 'CORPORATION', value: 'CORPORATION' },
        { label: 'PUBLIC_INSTITUTION', value: 'PUBLIC_INSTITUTION' },
        { label: 'APT_HOME', value: 'APT_HOME' },
        { label: 'CAMPUS', value: 'CAMPUS' },
        { label: 'HOSPITAL', value: 'HOSPITAL' },
        { label: 'PCROOM', value: 'PCROOM' },
        { label: 'OTHERS', value: 'OTHERS' },
      ],
      suserorggb: '',
      sorgname: '',
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
      this.sorgname = ''
      this.suserorggb = ''
      this.pagination = this.setDefaultPagination()
      setTimeout(() => {
        this.fnViewListWhoisKeywordMst()
      }, 10)
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListWhoisKeywordMst()
    },
    handleClickTableCheck(all, cur) {
      this.selectedRows = all
    },
    async fnViewListWhoisKeywordMst() {
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const param = {
        sorgname: this.sorgname,
        suserorggb: this.suserorggb
      }
       Object.assign(param, { pageUnit, pageIndex })
      try {
       this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListWhoisKeywordMst, param)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    fnViewKeywordInsert() { /* 등록 */
      this.$refs.ModalInsertKeyword.open()
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
        const res = await apiRequestJson(ipmsJsonApis.deleteWhoisKeyword, tbWhoisKeywordListVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '정상적으로 삭제되었습니다')
          this.$emit('reload')
          this.close()
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onClose() {
      this.suserorggb = ''
      this.sorgname = ''
      this.selectedRows = []
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
