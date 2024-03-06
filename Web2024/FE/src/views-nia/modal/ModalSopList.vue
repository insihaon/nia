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
          <i class="el-icon-document mr-2 text-base" />
          SOP 리스트
          <hr>
        </span>
        <div class="d-flex flex-column h-100">
          <CompInquiryPannel
            ref="selectApi"
            :ag-grid="dataSetAgGrid"
            title="장비 검색"
            :items.sync="searchAiItems"
            class="w-100 h-100 flex-fill"
            @handleClickSearch="onClickSearchAi"
          />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain type="info" class="close-btn" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiSopInquiryList } from '@/api/nia'

const routeName = 'ModalSopList'

export default {
  name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { CompAgGrid, CellRenderDataSetButtons, CompInquiryPannel },

  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
      sopList: [],
      visible: false,
      selectedRow: null,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        limit: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      searchAiItems: [
          { label: '티켓번호', type: 'input', size: 8, model: 'api_name', placeholder: '티켓번호를 검색하세요' },
          { label: 'FROM', type: 'date', size: 4, model: 'exec_mode_cd', placeholder: '연동방식을 선택하세요' },
          { label: 'TO', type: 'date', size: 4, model: 'exec_mode_cd', placeholder: '연동방식을 선택하세요' },
      ],
    }
  },
  computed: {
    dataSetAgGrid() {
      const options = { name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
        const columns = [
          { type: '', prop: 'ticket_id', name: '티켓번호', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'ticket_type', name: '티켓유형', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'ticket_result', name: '장애내용', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_classify', name: '장애구분', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_type', name: '장애유형', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_detail_content', name: '조치내용', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'etc_content', name: '기타사항', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'fault_time', name: '발생시간', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss') } },
          { type: '', prop: 'handling_fin_time', name: '마감시간', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return this.formatterTimeStamp(row.handling_fin_time, 'YYYY/MM/DD-HH:mm:ss') } },
          { type: '', prop: 'handling_fin_user', name: '마감자', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'root_cause_sysnamea', name: '노드A', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'root_cause_sysnamez', name: '노드Z', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'ip_addra', name: '마스터 IP', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'root_cause_porta', name: '장비I/F', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },

        ]
        return { options, columns, data: this.sopList }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxHeight = 1600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
      this.onLoadSopList()
    },
    onClickSearchAi(params) {
      this.onLoadSopList(params)
    },
     async onLoadSopList() {
      const { limit, currentPage } = this.paginationInfo
      try {
        const res = await apiSopInquiryList({ TICKET_TYPE: this.selectedRow.ticket_type, LIMIT: limit, PAGE: currentPage })
        this.sopList = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        this.error(error)
      }
    },
    onClose() { /* for Override */ },
    }

  }
</script>

<style lang="scss" scope>
@import "~@/styles/dataHub.scss";

.ModalSopList {
  font-family: "NanumSquare";

  .el-dialog__body{
    height: 700px !important;
  }

  .CompAgGrid{
    border: 1px solid #d6d6d6;

    .ag-header-container{
      background: #e8ecf0;
    }
  }

}

</style>
./ModalSopList.vue
