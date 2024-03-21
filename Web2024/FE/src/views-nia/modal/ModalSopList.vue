<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.maxHeight + `px`"
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
            ref="search"
            :ag-grid="dataSetAgGrid"
            :pagination-info="paginationInfo"
            title="장비 검색"
            :items.sync="searchAiItems"
            class="w-100 h-100 flex-fill"
            @handleClickSearch="onClickSearch"
            @onChangePage="onChangePage"
            @selectedRow="onClickRow"
          />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain class="close-btn" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
        <ModalSopDetail ref="ModalSopDetail" />
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
import ModalSopDetail from './ModalSopDetail.vue'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiSopList } from '@/api/nia'

const routeName = 'ModalSopList'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CellRenderDataSetButtons, CompInquiryPannel, ModalSopDetail },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      sopList: [],
      visible: false,
      selectedRow: null,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      searchAiItems: [
          { label: '티켓번호', type: 'input', size: 8, model: 'TICKET_ID', placeholder: '티켓번호를 검색하세요' },
          { label: 'DATE', type: 'date', size: 4, model: 'DATE', placeholder: '' },
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
      this.domElement.maxWidth = 1500
      this.domElement.maxHeight = 1600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
      this.onLoadSopList()
    },
    onClickSearch(searchModel) {
      this.onLoadSopList()
    },
    onClickRow(rows) {
      this.$refs.ModalSopDetail.open({ row: rows[0] })
    },
    async onLoadSopList() {
      const { pageSize: limit, currentPage: page } = this.paginationInfo
      const param = { TICKET_TYPE: this.selectedRow.ticket_type, limit, page }
      const searchModel = this.$refs?.search?.searchModel ?? {}
      if (searchModel?.TICKET_ID) {
        this._merge(param, { TICKET_ID: this.$refs.search.searchModel.TICKET_ID })
      }
      if (searchModel?.DATE) {
        const dateTime = this.$refs.search.searchModel.DATE
        this._merge(param, { START_DATE: dateTime[0], END_DATE: dateTime[1] })
      }
      try {
        const res = await apiSopList(param)
        this.sopList = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        this.error(error)
      }
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadSopList()
    },
    onClose() { /* for Override */ },
    }

  }
</script>

<style lang="scss" scoped>

</style>
