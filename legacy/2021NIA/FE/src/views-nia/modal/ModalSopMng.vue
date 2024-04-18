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
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          조치 SOP 관리
          <hr />
        </span>
        <div class="d-flex flex-column h-100 rounded justify-center" style="border: solid 1px #1e293b">
          <CompInquiryPannel
            ref="inquiry"
            :ag-grid="sopEditGrid"
            :is-excel="true"
            :items="searchItems"
            :pagination-info="paginationInfo"
            :search-model.sync="searchModel"
            class="w-100 h-100"
            @cellClicked="onClickCell"
            @handleClickSearch="onLoadSopCodeList()"
            @searchClear="searchClear"
          />
          <!-- @handleClickSearch="onClickSearch"
            @onChangePage="onChangePage"
            @searchClear="searchClear" -->
        </div>
        <div slot="footer" class="dialog-footer">
          <hr />
          <el-button size="small" plain class="mt-2" @click.native="$refs.ModalSopMngEdit.open({ type: 'add' })"> 등록 </el-button>
          <el-button size="small" plain class="mt-2" @click.native="onClickSopDelete()"> 삭제 </el-button>
          <el-button size="small" plain class="close-btn mt-2" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
        <ModalSopMngEdit ref="ModalSopMngEdit" @onClose="onCloseEdit" />
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import ModalSopMngEdit from '@/views-nia/modal/ModalSopMngEdit'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'

import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiSelectSopCode, apiDeleteSop } from '@/api/nia'
import { getTicketStatus } from '@/views-nia/js/commonFormat'

const routeName = 'ModalSopMng'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CellRenderDataSetButtons, CompInquiryPannel, ModalSopMngEdit },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11,
      },
      selectedRow: null,
      totalCount: 0,
      viewType: 'TICKET',
      sopCodeList: [],
      visible: false,
      searchItems: [
        { label: '등록자', type: 'input', model: 'USER_ID', size: 12 },
        { label: '조치 SOP', type: 'input', model: 'FAULT_GB' },
        { label: '조치 유형', type: 'input', model: 'FAULT_TYPE' },
        { label: '등록일', type: 'date', model: 'period' },
      ],
      searchModel: {
        period: null,
        USER_ID: '',
        FAULT_GB: '',
        FAULT_TYPE: '',
      },
    }
  },
  computed: {
    sopEditGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
      const columns = [
        { type: '', prop: 'user_id', name: '등록자', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_gb', name: '조치 SOP', width: 160, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_type', name: '항목', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        {
          type: '',
          prop: 'edit',
          name: '편집',
          width: 100,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: false,
          cellRenderer: (params) => {
            return `<button class='el-icon-edit' />`
          },
        },
        {
          type: '',
          prop: 'reg_time',
          name: '등록일',
          width: 100,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: false,
          formatter: (row) => {
            return this.formatterTimeStamp(row.reg_time, 'YYYY/MM/DD-HH:mm:ss')
          },
        },
      ]
      return { options, columns, data: this.sopCodeList }
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 1200
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.onLoadSopCodeList()
    },
    async onLoadSopCodeList() {
      try {
        const res = await apiSelectSopCode()
        this.sopCodeList = res?.result
        this.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        this.error(error)
      }
    },
    onClickCell(params) {
      if (params.colDef.prop === 'edit') {
        this.$refs.ModalSopMngEdit.open({ row: params.data, type: 'modify' })
      }
    },
    onCloseEdit(isSuccess) {
      if (isSuccess) {
        this.onLoadSopCodeList()
      }
    },
    searchClear() {
      this.searchModel = { period: null, USER_ID: '', FAULT_GB: '', FAULT_TYPE: '' }
    },
    onClickSopDelete() {
      const selectedRows = this.$refs.inquiry?.$refs?.compSearchEquip?.selectedRows
      if (selectedRows.length === 0) {
        this.alert('삭제할 항목을 선택하세요.')
      }
      const deleteRow = selectedRows[0]
      const { seq: SEQ } = deleteRow
      this.confirm(`삭제 하시겠습니까?`, '알림', {
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
      }).then(async () => {
        try {
          const res = await apiDeleteSop({ SEQ })
          if (res.success) {
            this.alert('삭제 되었습니다.')
            this.onLoadSopCodeList()
          }
        } catch (error) {
          this.alert('삭제 실패 하였습니다.')
          console.error(error)
        }
      })
    },
    alert(message) {
      this.$alert(message, '알림', { confirmButtonText: '확인' })
    },
    onClose() {
      /* for Override */
    },
  },
}
</script>
<style lang="scss" scoped>
// .ModalSopDetail ::v-deep {
//   .el-dialog__body {
//     height: 400px;
//   }
// }
// ::v-deep .el-form-item__label {
//   width: 90px;
//   margin-left: 5px;
//   line-height: 20px;
// }
// ::v-deep .el-form-item__content {
//   width: calc(100% - 90px);
// }
</style>
