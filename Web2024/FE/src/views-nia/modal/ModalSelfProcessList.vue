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
          자가 {{ getPageType }} 이력조회
          <hr>
        </span>
        <div class="d-flex flex-column h-100">
          <CompInquiryPannel
            ref="search"
            :ag-grid="selfProcessAgGrid"
            :pagination-info="paginationInfo"
            :items.sync="searchItems"
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
        <ModalSelfProcessDetail ref="ModalSelfProcessDetail" />
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
import ModalSelfProcessDetail from './ModalSelfProcessDetail.vue'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiSelfProcessList } from '@/api/nia'
import { getTicketType } from '@/views-nia/js/commonFormat'

const routeName = 'ModalSopList'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CellRenderDataSetButtons, CompInquiryPannel, ModalSelfProcessDetail },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selfProcessList: [],
      visible: false,
      selectedRow: null,
      receviedParams: {},
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      searchOptions: {
      },
      recoveryType: [
        { label: '비장애성', value: 'F' },
        { label: 'SYSLOG', value: 'S' }
      ],
      optimizationType: [
        { label: '이상트래픽', value: 'A' },
        { label: '유해트래픽', value: 'N' }
      ],
      closType: [
        { label: '자동', value: 'A' },
        { label: '수동', value: 'M' }
      ],
      closStatus: [
        { label: '마감', value: 'Y' },
        { label: '발생', value: 'N' }
      ],
    }
  },
  computed: {
    selfProcessAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
      const columns = [
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'occur_time', name: '발생시간', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, format: (row) => { return row.occur_time ? this.formatterTimeStamp(row.occur_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
        { type: '', prop: 'clos_time', name: '마감시간', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, format: (row) => { return row.clos_time ? this.formatterTimeStamp(row.clos_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
        { type: '', prop: 'self_process_type', name: '알람종류', width: 120, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, format: getTicketType },
        { type: '', prop: 'fault_detail_content', name: '조치내용', suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'clos_type', name: '마감종류', width: 80, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, format: (row) => { return row.clos_type === 'A' ? '자동' : '수동' } },
        { type: '', prop: 'clos_status', name: '마감여부', width: 80, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, format: (row) => { return row.clos_status === 'Y' ? '마감' : '발생' }, cellStyle: (params) => { return params.value === 'Y' ? { color: 'red' } : { color: 'blue' } } },
      ]
      return { options, columns, data: this.selfProcessList }
    },
    getPageType() {
      return this.receviedParams?.SELF_PROCESS_GROUP === 'SO' ? '최적화' : '회복' || ''
    },
    searchItems() {
      return [
          { label: '종류', type: 'select', multiple: false, size: 4, model: 'SELF_PROCESS_TYPE', placeholder: '', options: this.getPageType === '최적화' ? this.optimizationType : this.recoveryType },
          { label: '마감종류', type: 'select', multiple: false, size: 4, model: 'CLOS_TYPE', placeholder: '', options: this.closType },
          { label: '마감상태', type: 'select', multiple: false, size: 4, model: 'CLOS_STATUS', placeholder: '', options: this.closStatus },
          // { label: '조회기간', type: 'date', size: 4, model: 'DATE', placeholder: '' },
      ]
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 1000
      this.closeOnClickModal = false
    },
    onOpen(params, actionMode) {
      this.receviedParams = params
      this.onLoadselfProcessList()
    },
    onClickSearch(searchModel) {
      this.onLoadselfProcessList()
    },
    onClickRow(rows) {
      this.$refs.ModalSelfProcessDetail.open({ row: this._merge(rows[0], this.receviedParams) })
    },
    async onLoadselfProcessList() {
      const { pageSize: limit, currentPage: page } = this.paginationInfo
      const params = this._cloneDeep(this.receviedParams)
      this._merge(params, { limit, page })
      const searchModel = this.$refs?.search?.searchModel ?? {}
      if (Object.keys(searchModel).length !== 0) {
        this._merge(params, searchModel)
      }
      try {
        const res = await apiSelfProcessList(params)
        this.selfProcessList = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        this.error(error)
      }
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadselfProcessList()
    },
    onClose() { /* for Override */ },
    },

  }
</script>

<style lang="scss" scoped>
.CompInquiryPannel ::v-deep {
  div.subContentWrap div.optionBox>div.optionBoxContent>div.optionItem>label {
    min-width: 50px;
  }
}
</style>
