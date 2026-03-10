<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="metaData"
      :ag-grid="catalogueAgGrid"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      :is-button-slot="true"
      class="w-100 h-100"
      @handleClickSearch="onLoadList"
      @selectedRow="selectedItems"
      @keyupEnter="onLoadList"
      @searchClear="handleMetaDataSearchClear"
      @sortedChange="onSortedChange"
    >
      <template #inquireButton>
        <span> 메타데이터 조회가 가능합니다 </span>
      </template>
    </DataHubComponent>
    <CompApplyModal ref="CompApplyModal" :fullscreen="isViewport('<', 'sm')" />
    <!-- <ModalAddDetailColumn ref="ModalAddDetailColumn" /> -->
  </div>
</template>

<script>

import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
import CompApplyModal from '@/views-dataHub/modal/CompApplyModal'
import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
import ModalAddDetailColumn from '@/views-dataHub/modal/ModalAddDetailColumn'
import { apiSelectDataCatalogList } from '@/api/dataHub'
const routeName = 'MetaData'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { DataHubComponent, CellRenderButtons, CompApplyModal, hyperLinkTextRender, ModalAddDetailColumn },
  extends: Base,

  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      catalogueData: [],
      searchItems: [
      { label: '테이블명', type: 'input', size: 6, placeholder: '테이블명을 검색하세요', model: 'table_nm' },
      { label: '컬럼명', type: 'input', size: 6, placeholder: '컬럼명을 검색하세요', model: 'column_nm' },
      { label: '설명', type: 'input', size: 6, placeholder: '설명을 검색하세요', model: 'metadata_desc' },
      ],
      searchModel: {
        table_nm: '',
        column_nm: '',
        metadata_desc: ''
      },
      selectedData: [],
      selectTableDataList: [],
      sortInfo: {}
    }
  },

  computed: {
    catalogueAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', headerCheckboxSelection: true, checkboxSelection: true, showDisabledCheckboxes: true,
        }
      const columns = [
          { type: '', prop: 'metadata_seq', name: '테이블ID', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
          cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenTableModalDetail.bind(this) }, },
          { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
          cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenColumnEditModal.bind(this) }, },
          { type: '', prop: 'data_type', name: '타입', minWidth: 200, flex: 1, suppressMenu: true, alignItems: 'left' },
          { type: '', prop: 'metadata_desc', name: '설명', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'create_time', name: '수정일시', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        ]
        return { options, columns, data: this.catalogueData, getRightClickMenuItems: () => { return [] } }
      },
      isSelectedRow() {
        return this.selectedData.length > 0
      },
  },
  watch: {
  },
  mounted() {
    this.onLoadList()
  },

  created() {
  },
  methods: {
    onSortedChange(param) {
       this.sortInfo = param
       this.onLoadList()
      },
    handleMetaDataSearchClear() {
      this.searchModel.table_nm = ''
      this.searchModel.column_nm = ''
      this.searchModel.metadata_desc = ''
      this.onLoadList()
    },
    async onLoadList(params) {
      const target = ({ vue: this.$refs.metaData })
      this.openLoading(target)
      const param = {
        table_nm: this.searchModel.table_nm,
        column_nm: this.searchModel.column_nm,
        metadata_desc: this.searchModel.metadata_desc,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
      }
    try {
      const res = await apiSelectDataCatalogList(param)
      this.catalogueData = res?.result
      this.paginationInfo.totalCount = res.total // 총 항목 수 설정
      this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    refreshData() {
      this.selectedRow = []
    },
    selectedItems(param) {
      this.selectedData = param
    },
    handleOpenColumnEditModal(type, row) {
      this.$refs.CompApplyModal.open({ row: row, type: type, grant: 'user' })
    },
    async handleOpenTableModalDetail(type, row) {
      const params = {
        table_nm: row.table_nm
      }
      this.$modal.show('editMonitoringExcludeAlarm', { type, params, routeName })
    },
  }

  }
</script>

