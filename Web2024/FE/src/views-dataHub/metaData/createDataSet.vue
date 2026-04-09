<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="dataSet"
      :ag-grid="selectDataAgGrid"
      :items="searchItems"
      :search-model="searchModel"
      :pagination-info="paginationInfoDataSet"
      class=" w-100 h-50"
      @cellClicked="selectedColItem"
      @handleClickSearch="onClickSearchDataSet"
      @keyupEnter="onClickSearchDataSet"
      @searchClear="handleDataSetSearchClear"
      @sortedChange="onSortedChange"
    >
      <template #inquireButton>
        <span> API 데이터 생성요청시 추가 가능한 데이터 내역입니다</span>
      </template>
    </DataHubComponent>
    <DataHubComponent
      ref="newDataSet"
      :ag-grid="newDataAgGrid"
      :pagination-info="paginationInfoNewDataSet"
      :is-search="false"
      :is-button-slot="true"
      title="신규 API 데이터"
      class="w-100 h-50"
      @cellClicked="selectedColItem"
      @selectedRow="selectedNewRow"
      @selectedNewCol="changeSelected"
    >
      <template #inquireButton>
        <span> API 데이터 생성 요청시 추가한 내역입니다</span>
      </template>

      <template slot="button-container">
        <div class="button-panel my-1">
          <el-button type="danger" size="mini" plain :disabled="selectedNewTable.length < 1" @click="checkedDeleteData()">선택 삭제</el-button>
          <el-button type="info" size="mini" plain class="float-right" @click.native="handleOpenEditModal($refs.ModalCreateDataHist, true)"> <span>요청 이력</span> </el-button>
          <el-button size="mini" class="float-right common-btn" :disabled="selectedNewTable.length < 1" @click.native="handleOpenEditModal($refs.ModalRequestData, true)"><span>생성 요청</span></el-button>
        </div>
      </template>
    </DataHubComponent>
    <ModalRequestData ref="ModalRequestData" :fullscreen="isViewport('<', 'sm')" @clearData="handleDataclear" />
    <ModalCreateDataHist ref="ModalCreateDataHist" :fullscreen="isViewport('<', 'sm')" />
    <!-- <ModalAddDetailColumn ref="ModalAddDetailColumn" :fullscreen="isViewport('<', 'sm')" @selectedNewItem="selectedColNewItem" /> -->
  </div>
</template>

  <script>

    import { Base } from '@/min/Base.min'
    import DataHubComponent from '@/views-dataHub/components/CompTemplate'
    import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
    import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
    import ModalCreateDataHist from '@/views-dataHub/modal/ModalCreateDataHist'
    import ModalRequestData from '@/views-dataHub/modal/ModalRequestData'
    import ModalAddDetailColumn from '@/views-dataHub/modal/ModalAddDetailColumn'
    import { Modal } from '@/min/Modal.min'
    import { apiSelectDataCatalogList, apiSelectDataTableList } from '@/api/dataHub'
    import { mapState } from 'vuex'
    import { AppOptions } from '@/class/appOptions'
    import EventBus from '@/utils/event-bus'

    const routeName = 'CreateDataSet'
    export default {
    name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { DataHubComponent, CellRenderButtons, ModalCreateDataHist, ModalRequestData, hyperLinkTextRender, ModalAddDetailColumn },
    extends: Base,
    data() {
      return {
        name: routeName,
        src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        paginationInfoDataSet: {
        currentPage: 1, // 현재 페이지
        pageSize: 30, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
        paginationInfoNewDataSet: {
        totalCount: 0
      },
        selectData: [],
        newData: [],
        searchItems: [
          { label: '테이블명', type: 'input', multiple: true, placeholder: '테이블명을 검색하세요', model: 'table_nm' },
          { label: '컬럼명', type: 'input', placeholder: '컬럼명을 검색하세요', model: 'column_nm' },
          { label: '설명', type: 'input', placeholder: '설명을 검색하세요', model: 'metadata_desc' },
        ],
        searchModel: {
        table_nm: '',
        column_nm: '',
        metadata_desc: ''
      },
        selectedTable: [],
        selectedCol: {},
        selectedNewTable: [],
        selectTableDataList: [],
        checkedDeleteList: [],
        sortInfo: {}
      }
    },
    computed: {
     ...mapState({
        blackDtlList: (state) => state.user.blackDtlList
     }),
    selectDataAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'single', rowMultiSelection: false
      }
      const columns = [
        { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
         cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenTableModalDetail.bind(this.selectedCol) }, },
        { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'data_type', name: '타입', minWidth: 200, flex: 1, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: 'metadata_desc', name: '설명', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'update_time', name: '수정일시', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: '_', name: '기능', minWidth: 50, flex: 1, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true,
        cellRendererFramework: 'CellRenderButtons', cellRendererParams: { key: 'add', newData: this.newData, action: this.changeSelected.bind(this) }
        }
      ]
        return { options, columns, data: this.selectData, getRightClickMenuItems: () => { return [] } }
      },
      newDataAgGrid() {
        const options = {
        name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
        cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenTableModalDetail.bind(this) }, },
        { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'data_type', name: '타입', minWidth: 200, flex: 1, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: 'metadata_desc', name: '설명', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'update_time', name: '수정일시', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: '_', name: '기능', minWidth: 50, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true,
        cellRendererFramework: 'CellRenderButtons', cellRendererParams: { key: 'delete', action: this.handleDeleteData.bind(this) }
        }
      ]
        return { options, columns, data: this.newData, getRightClickMenuItems: () => { return [] } }
      },
      hasNoAddGrant() {
        return AppOptions.instance.isGod || this.blackDtlList.includes('btnAdd')
      }
    },
    watch: {
    },
    mounted() {
      this.onLoadList()
      this.selectTableList()
    },
    created() {
      EventBus.$on('selectedNewCol', this.changeSelected)
    },
    beforeDestroy() {
      EventBus.$off('selectedNewCol', this.changeSelected)
    },
    methods: {
      onSortedChange(param) {
       this.sortInfo = param
       this.onLoadList()
      },
      handleDataSetSearchClear() {
        this.searchModel.table_nm = ''
        this.searchModel.column_nm = ''
        this.searchModel.metadata_desc = ''
        this.onLoadList()
      },
    async selectTableList() {
      const res = await apiSelectDataTableList()
      this.selectTableDataList = res.result.map(item => ({ label: item.table_nm, value: item.table_nm }))
      // this.selectTableDataList.unshift({ label: '전체', value: 'ALL' }) // unshift() => 배열 맨앞에 추가

      const tableNmItem = this.searchItems.find(item => item.model === 'table_nm')
      if (tableNmItem) {
        tableNmItem.options = this.selectTableDataList
      }
    },
    selectedColItem(params) { // @cellClicked
      const colId = params.column.colId
      if (colId === 'table_nm') {
      const rowNode = params.node
      if (rowNode.isSelected()) {
          rowNode.setSelected(false)
      } else {
        rowNode.setSelected(true)
      }
    }
      this.selectedCol = params
    },

    selectedColNewItem(params, type) {
      this.changeSelected(params, type)
    },
    checkedDeleteData() {
      this.handleDeleteData(this.selectedNewTable)
    },
    onClickSearchDataSet(params) {
      this.onLoadList(params)
    },
    async onLoadList(params) {
      const target = ({ vue: this.$refs.dataSet })
      this.openLoading(target)
        const param = {
        table_nm: this.searchModel.table_nm,
        column_nm: this.searchModel.column_nm,
        metadata_desc: this.searchModel.metadata_desc,
        limit: this.paginationInfoDataSet.pageSize,
        page: this.paginationInfoDataSet.currentPage,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
      }
    try {
      const res = await apiSelectDataCatalogList(param)
      this.selectData = res?.result
      this.paginationInfoDataSet.totalCount = res.total // 총 항목 수 설정
      this.paginationInfoDataSet.totalPages = Math.ceil(this.paginationInfoDataSet.totalCount / this.paginationInfoDataSet.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    selectedNewRow(param) {
      this.selectedNewTable = param
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleOpenEditModal(ref, param = {}) { // alarm Open 모달
      ref.open({ param: param, modalEditInfo: this.selectedNewTable })
    },
    async handleOpenTableModalDetail(type, row) {
      const param = {
        table_nm: row.table_nm
      }
      const res = await apiSelectDataCatalogList(param)
      const params = res?.result
      const columnColId = this.selectedCol.column.colId
      // this.$refs.ModalAddDetailColumn.open({ type, params, columnColId, routeName, newData: this.newData })
      this.$modal.show('editMonitoringExcludeAlarm', { type, params, columnColId, routeName, newData: this.newData })
    },
    handleDataclear(data) {
      this.newData = data
      this.paginationInfoNewDataSet.totalCount = 0
    },
    changeSelected(param, type) { // @selectedRow\
      this.selectedTable = []
      if (!Array.isArray(param)) {
        this.selectedTable.push(param)
      } else {
        this.selectedTable = param
      }
      this.selectedTable.forEach(row => {
        const isNewData = this.newData.some(newDataRow => newDataRow.metadata_seq === row.metadata_seq)

      if (!isNewData) {
        this.newData.push({ ...row })

        setTimeout(() => {
          this.$refs.newDataSet.$refs.CompTemplateTable.gridApi.selectAll()
        }, 100)
        this.paginationInfoNewDataSet.totalCount = this.newData.length
        }
      })
        this.$modal.hide('editMonitoringExcludeAlarm')
    },
    handleDeleteData(param) {
      this.checkedDeleteList = []
      if (!Array.isArray(param)) {
        this.checkedDeleteList.push(param)
      } else {
        this.checkedDeleteList = param
      }
      this.$confirm(this.checkedDeleteList.length + '건의 데이터를 요청 취소 하시겠습니까?', '신규 API 데이터', {
      confirmButtonText: '확인',
      cancelButtonText: '취소',
      type: 'info'
    }).then(async() => {
      try {
        this.newData = this.newData.filter((newDataItem) => {
          return !this.checkedDeleteList.some((deleteItem) => {
            return deleteItem.metadata_seq === newDataItem.metadata_seq
          })
        })

        setTimeout(() => {
        this.$refs.newDataSet.$refs.CompTemplateTable.gridApi.selectAll()
      }, 100)
      this.$message({ message: `취소되었습니다` })
      } catch (error) {
        this.$message.error({ message: `데이터 취소에 실패했습니다.` })
        console.log(error)
      }
      this.paginationInfoNewDataSet.totalCount = this.newData.length
    })
    }
    }
    }
  </script>

  <style lang="scss" scope>

  .button-panel{

  }

  </style>
