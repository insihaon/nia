<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="linkSystem"
      title="연동 시스템 조회"
      :ag-grid="authAgGrid"
      :is-button-slot="true"
      :items="searchItems"
      :search-model="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearchSystem"
      @searchClear="onClearSearch"
      @selectedRow="selectedItems"
      @keyupEnter="onClickSearchSystem"
      @sortedChange="onSortedChange"
    >
      <template slot="button-container">
        <div class="button-panel my-1">
          <el-button type="danger" size="mini" :disabled="!hasNoAddGrant || selectedRows.length < 1" plain @click="handleDeleteData()">선택 삭제</el-button>
          <el-button size="mini" :disabled="!hasNoAddGrant" class="common-btn fr" @click="handleOpenEditModal({type : 'create' })">생성</el-button>
        </div>
      </template>

      <template #inquireButton>
        <span>API 연동시스템 관련 설정이 가능합니다</span>
      </template>
    </DataHubComponent>

    <ModalLinkSystemEdit ref="ModalLinkSystemEdit" :fullscreen="isViewport('<', 'sm')" @systemEdit="refreshData" />

  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
import { apiSelectLinkSystemList, apiDeleteLinkSystemListProc } from '@/api/dataHub'
import ModalLinkSystemEdit from '@/views-dataHub/modal/ModalLinkSystemEdit'
import { mapState } from 'vuex'
import { AppOptions } from '@/class/appOptions'

const routeName = 'LinkSystemManagement'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { DataHubComponent, CellRenderButtons, ModalLinkSystemEdit },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 30, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectedRows: [],
      linkData: [],
      searchItems: [{ label: '연동 시스템', type: 'input', model: 'system_nm', placeholder: '연동시스템명을 검색하세요', }],
      searchModel: {
        system_nm: ''
      },
      sortInfo: {}
    }
  },
  computed: {
      ...mapState({
        blackDtlList: (state) => state.user.blackDtlList
     }),
    authAgGrid() {
      const options = { name: this.name, checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true, }
      const columns = [
        { type: '', prop: 'system_nm', name: '연동 시스템명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'create_time', name: '등록일시', minWidth: 150, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'system_desc', name: '설명', minWidth: 150, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: '_', name: '기능', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, cellRendererFramework: 'CellRenderButtons', cellRendererParams: { key: 'delete', title: 'edit', action: this.handleOpenEditModal.bind(this) } }
      ]

      return { options, columns, data: this.linkData, getRightClickMenuItems: () => { return [] } }
    },
    hasNoAddGrant() {
      return AppOptions.instance.isGod || this.blackDtlList.includes('btnAdd')
    },
  },
  mounted() {
    this.onLoadList()
  },
  methods: {
    onSortedChange(param) {
       this.sortInfo = param
       this.onLoadList()
      },
    onClickSearchSystem(params) {
      this.onLoadList(params)
    },
    onClearSearch() {
      this.searchModel.system_nm = ''
      this.onLoadList()
    },
    async onLoadList(params) {
    const target = ({ vue: this.$refs.linkSystem })
    this.openLoading(target)
    const param = {
      system_nm: this.searchModel.system_nm,
      limit: this.paginationInfo.pageSize,
      page: this.paginationInfo.currentPage,
      sort_column_name: this.sortInfo.colId,
      sort_type: this.sortInfo.sort
    }
    try {
      const res = await apiSelectLinkSystemList(param)
      this.linkData = res?.result
      this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

  refreshData() {
      this.onLoadList()

      this.selectedRows = []
    },
    selectedItems(param) {
      this.selectedRows = param
    },
    handleDeleteData(row) {
    let res = ''
    const rowData = Array.isArray(row) ? row : [row]
    this.$confirm(`연동시스템을 삭제 하시겠습니까?`, '삭제 메세지', {
      confirmButtonText: '확인',
      cancelButtonText: '취소',
    }).then(async () => {
      try {
      for (const v of this.selectedRows.length > 0 ? this.selectedRows : rowData) {
        const param = {
          system_id: v.system_id ?? rowData
        }
        res = await apiDeleteLinkSystemListProc(param)
      }
      if (res.success) {
        this.$message.success({ message: `삭제되었습니다.` })
      }
      this.onLoadList()
      this.selectedRows = []
    } catch (error) {
        this.$message.error('삭제에 실패했습니다.')
        console.log(error)
      }
    })
  },
    handleOpenEditModal(row, type) {
        if (type === 'delete') {
          this.handleDeleteData(row, type)
        } else {
          this.$refs.ModalLinkSystemEdit.open({ row: row, type: type, grant: 'admin' })
        }
    }
  }
}
</script>

<style lang="scss" scoped>
 .LinkSystemManagement{
   .el-form-item__label{
     width: 80px !important;
     margin-right: 8px !important;
    }
  }

</style>

