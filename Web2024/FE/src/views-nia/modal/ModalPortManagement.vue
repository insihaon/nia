<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.minHeight + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="false"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-s-tools mr-2 text-base" />
          포트 관리
          <hr>
        </span>

        <div class="des-class mr-2">
          <table class="basic">
            <th>노드명</th>
            <td class="disable">
              {{ selectedRow.node_nm }}
            </td>
            <th>대표IP</th>
            <td class="disable">
              {{ selectedRow.ip_addr }}
            </td>
          </table>
        </div>

        <div class="d-flex flex-column h-100">
          <CompInquiryPannel
            ref="portList"
            :ag-grid="dataSetAgGrid"
            :pagination-info="paginationInfo"
            :is-search="false"
            class="w-100 h-100 flex-fill"
            @handleClickSearch="onClickSearch"
            @onChangePage="onChangePage"
          />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain class="close-btn" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
    <ModalPortEdit ref="ModalPortEdit" @systemEdit="onLoadNodeList()" />
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import ModalPortEdit from '@/views-nia/modal/ModalPortEdit'
import CellRenderAibuttons from '@/views-nia/components/cellRenderer/CellRenderAibuttons'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiSelectPortList } from '@/api/nia'

const routeName = 'ModalPortManagement'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CompInquiryPannel, CellRenderAibuttons, ModalPortEdit },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      portList: [],
      visible: false,
      selectedRow: null,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
    }
  },
  computed: {
    dataSetAgGrid() {
      const options = { name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
        const columns = [
          { type: '', prop: 'rownum', name: '번호', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'if_id', name: 'I/F_ID', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false,
          cellRendererFramework: 'CellRenderHyperlink', cellRendererParams: { type: 'portEdit', action: this.handleOpenEditModal.bind(this) } },
          { type: '', prop: 'if_nm', name: 'I/F명', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'if_type', name: 'I/F타입', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'ip_addr', name: 'IP주소', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'if_speed', name: '대역폭(Gbps)', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'if_oper_status_info', name: '사용유무', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'chng_datetime', name: '수정일', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        ]
        return { options, columns, data: this.portList }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxHeight = 1600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = this._cloneDeep(model.row)
      this.onLoadNodeList()
    },
    onClickSearch(searchModel) {
      this.onLoadNodeList()
    },
    async onLoadNodeList() {
      const { pageSize: limit, currentPage: page } = this.paginationInfo
      const param = { node_id: this.selectedRow.node_id, limit, page }
      try {
        const res = await apiSelectPortList(param)
        this.portList = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        this.error(error)
      }
    },
    handleOpenEditModal(row, type) {
      this.$refs.ModalPortEdit.open({ row: row, type: type })
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadNodeList()
    },
  },
}
</script>
