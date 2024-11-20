<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="metaData"
      :ag-grid="catalogueAgGrid"
      :items="searchItems"
      :search-model="searchModel"
      :pagination-info="paginationInfo"
      :is-button-slot="true"
      :is-form-button="true"
      class="w-100 h-100"
      @handleClickSearch="onClickSearchMetaData"
      @selectedRow="selectedItems"
      @keyupEnter="onClickSearchMetaData"
      @searchClear="handleApiSearchClear"
      @sortedChange="onSortedChange"
    >

      <template #inquireButton>
        <div style="width: 100%; display: flex; padding : 5px 0;">
          <el-button plain size="mini" type="info" icon="el-icon-edit pr-2" style="margin-right : 5px;" :disabled="!hasNoAddGrant" @click="handleExcelFormExport()">
            양식다운로드
          </el-button>

          <form method="post" enctype="multipart/form-data" accept-charset="UTF-8" @submit.prevent="uploadFile">
            <input ref="fileInput" type="file" accept=".csv" style="display: none;" @change="handleFileChange">
            <el-button type="button" size="mini" class="excel-form-export" :disabled="!hasNoAddGrant" style="background: #1F7245ff; color: white; padding: 9px 13px;" icon="el-icon-download" @click="openFilePicker">
              엑셀 업로드
            </el-button>
          </form>
        </div>
      </template>

      <template slot="button-container">
        <div class="button-panel my-1">
          <el-button type="danger" size="mini" class="float-left" plain :disabled="!hasNoAddGrant || selectedData.length < 1" @click.native="handleDeleteData()">삭제</el-button>
          <el-button size="mini" class="float-right common-btn" :disabled="!hasNoAddGrant || selectedData.length > 0" @click.native="handleOpenColumnEditModal(row, 'apply')">등록</el-button>
        </div>
      </template>

    </DataHubComponent>
    <CompApplyModal ref="CompApplyModal" :fullscreen="isViewport('<', 'sm')" @systemEdit="refreshData()" />
    <!-- <ModalAddDetailColumn ref="ModalAddDetailColumn" /> -->
  </div>
</template>

<script>

import { Base } from '@/min/Base.min'
import { param } from '@/utils'
import { mapState } from 'vuex'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
import CompApplyModal from '@/views-dataHub/modal/CompApplyModal'
import { apiSelectDataCatalogList, apiDeleteDataCatalogList, apiSelectDataTableList } from '@/api/dataHub'
import { getToken } from '@/utils/auth'
import ModalAddDetailColumn from '@/views-dataHub/modal/ModalAddDetailColumn'
import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
import { AppOptions } from '@/class/appOptions'
import axios from 'axios'

const routeName = 'MetaDataManagement'
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
          { label: '컬럼', type: 'input', model: 'column_nm', placeholder: '컬럼을 검색하세요' },
          { label: '설명', type: 'input', model: 'metadata_desc', placeholder: '설명을 검색하세요' }
      ],
      searchModel: {
        table_nm: '',
        column_nm: '',
        metadata_desc: '',
      },
      selectedData: [],
      selectTableDataList: [],
      sortInfo: {}
    }
  },

  computed: {
    ...mapState({
      blackDtlList: (state) => state.user.blackDtlList
    }),
    catalogueAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', headerCheckboxSelection: true, suppressRowClickSelection: true, checkboxSelection: true, showDisabledCheckboxes: true,
        }
      const columns = [
          { type: '', prop: 'metadata_seq', name: '테이블ID', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
          cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenTableModalDetail.bind(this) }, },
          { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'data_type', name: '타입', minWidth: 200, flex: 0, suppressMenu: true, alignItems: 'left' },
          { type: '', prop: 'metadata_desc', name: '설명', minWidth: 150, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'create_time', name: '수정일시', minWidth: 150, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: '_', name: '기능', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', cellRendererFramework: 'CellRenderButtons', sortable: false, cellRendererParams: { title: 'edit', key: 'delete', action: this.isModeChange.bind(this) } },
        ]
        return { options, columns, data: this.catalogueData, getRightClickMenuItems: () => { return [] } }
      },
      isSelectedRow() {
        return this.selectedData.length < 1
      },
      hasNoAddGrant() {
        return AppOptions.instance.isGod || this.blackDtlList.includes('btnAdd')
      }
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
    isModeChange(row, type) {
      if (type === 'edit') {
        this.handleOpenColumnEditModal(row, 'edit')
      } else {
        this.handleDeleteData()
      }
    },
    openFilePicker() {
      this.$refs.fileInput.click()
    },
    handleFileChange(event) {
      const file = event.target.files[0]
      if (file) {
        this.uploadFile(file)
        console.log(`Selected File: ${file.name}`)
      }
    },
    async uploadFile(file) {
      const formData = new FormData()
      formData.append('file', file)

      try {
        await axios.post(`${AppOptions.instance.baseURL.replace(/\/$/, '')}/upload_meta`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data; charset=UTF-8',
            'X-AUTH-TOKEN': getToken()
          },
        })
        this.$message.info({ message: `업로드 작업을 성공했습니다.` })
      } catch (error) {
        console.error('Error uploading file:', error)
        this.$message.error({ message: `업로드 작업을 실패했습니다.` })
      }
      this.onLoadList()
    },
    handleApiSearchClear() {
      this.searchModel.table_nm = ''
      this.searchModel.column_nm = ''
      this.searchModel.metadata_desc = ''
      this.onLoadList()
    },
    onClickSearchMetaData(params) {
        this.onLoadList(params)
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
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize)
      } catch (error) {
          console.error(error)
        } finally {
          this.closeLoading(target)
        }
      },
      handleDeleteData(data) {
      this.$confirm('데이터를 삭제 하시겠습니까?', '삭제 메세지', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          if (this.selectedData.length > 0) {
          for (const v of this.selectedData) {
          const param = { metadata_seq: v.metadata_seq }
          const res = await apiDeleteDataCatalogList(param)
           if (res.result) {
             this.$message.success({ message: `삭제되었습니다.` })
            }
          }
            this.selectedData = []
            this.onLoadList()
          }
          } catch (error) {
            this.$message.error({ message: `삭제에 실패했습니다.` })
            console.log(error)
          }
        })
      },
    handleOpenColumnEditModal(row, type) {
      this.$refs.CompApplyModal.open({ row: row, type: type, grant: 'admin' })
    },
    refreshData() {
      this.onLoadList()

      this.selectedData = []
    },
    selectedItems(param) {
      this.selectedData = param
    },
    handleExcelFormExport() {
      const dirName = 'static'
      const fileName = 'metaData_dataForm.csv'

      const downloadUrl = `${AppOptions.instance.baseURL}downloadFile/${dirName}/${fileName}`

      window.open(downloadUrl, 'metaData_dataForm')
    },
    async handleOpenTableModalDetail(type, row) {
      const param = {
        table_nm: row.table_nm
      }
      const res = await apiSelectDataCatalogList(param)
      const params = res?.result
      this.$modal.show('editMonitoringExcludeAlarm', { type, params, routeName })
    },
    },
    handleExcelUpload() {

    }
  }
</script>

<style lang="scss">

.MetaDataManagement {
  font-family: "NanumSquare";

 .itemSlot{
    float: right !important;
  }

  .inquireButton > span{
    line-height: 50px !important;
    width: 100px !important;
  }

  .excel-form-export:hover{
    background: rgba(137, 174, 154, 0.952) !important;
  }
}

</style>

