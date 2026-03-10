<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="미배정 블록 상세"
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
            <label>계위</label>
          </th>
          <td>
            <div v-if="tableDatas !== null">
              {{ tableDatas.ssvcLineTypeNm }} - {{ tableDatas.ssvcGroupNm }}
            </div>
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
        :prop-table-height="500"
        :prop-column="tableColumns"
        :prop-max-select="pagination.data.length"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            미배정 블록 상세 정보
          </span>
        </template>
      </compTable>
    </el-col>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>
<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalNotAssignDetail'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      selectedRow: null,
       tableColumns: [
        { prop: 'sipCreateSeqCd', label: '생성차수', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nclassCnt', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
        formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              attrs: {
                round: true, // Adding the round option
                plain: true,
                type: 'primary'
              },
              props: {
                size: 'mini'
              },
              on: { click: () => {
                this.close()
                this.$store.dispatch('ipms/setToParam', { value: row.pipPrefix })
                this.$router.push('/ipAssignMng/ipAssign')
              }
            } }, '배정')
          }
        },
      ],
      tableDatas: [],
      row: null,
      type: null
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model.row) {
        this.row = model.row
        this.type = model.type
        setTimeout(() => {
          this.fnViewDetailUnAssignIP(this.row, this.type)
        }, 10)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewDetailUnAssignIP(this.row, this.type)
    },
    async fnViewDetailUnAssignIP(row, type) {
      const { ssvcLineTypeNm, ssvcGroupNm, nlvlMstSeq } = row
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const target = ({ vue: this.$refs.compTable })
      const param = {
        nlvlMstSeq: nlvlMstSeq,
        sipVersionTypeCd: 'CV0001',
        sassignLevelCd: type,
        ssvcLineTypeNm: ssvcLineTypeNm,
        ssvcGroupNm: ssvcGroupNm,
      }
      Object.assign(param, { pageUnit, pageIndex, })
      try {
       this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewDetailUnAssignIP, param)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
        this.tableDatas = res.result.data[0]
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onClose() {
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
