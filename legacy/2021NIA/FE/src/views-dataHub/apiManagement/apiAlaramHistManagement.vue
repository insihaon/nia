<template>
  <div :class="{ [name]: true }">
    <div class="main-layout">
      <DataHubComponent
        ref="selectApi"
        :ag-grid="selectApiAgGrid"
        title="API 조회"
        :items="searchApiItems"
        :search-model.sync="searchApiModel"
        :pagination-info="paginationInfoApi"
        class="w-50 h-100 flex-fill"
        @handleClickSearch="onClickSearchApi"
        @selectedRow="selectedAPIRow"
        @keyupEnter="onClickSearchApi"
        @searchClear="handleApiSearchClear"
        @sortedChange="onSortedChangeApi"
      >
        <template #inquireButton>
          <span> API 서비스에 대한 조회가 가능합니다 </span>
        </template>
      </DataHubComponent>

      <DataHubComponent
        ref="selectHistory"
        :ag-grid="selectHistoryAgGrid"
        :ag-grid-list="selectHistoryAgGrid"
        title=" API 오류 이력 조회"
        :items="searchHistoryItems"
        :search-model.sync="searchHistoryModel"
        :pagination-info="paginationInfoHist"
        class=" w-50 h-100 flex-fill"
        @handleClickSearch="onClickSearchHist"
        @keyupEnter="onClickSearchApi"
        @searchClear="handleHistorySearchClear"
        @sortedChange="onSortedChangeHist"
      >
        <template #inquireButton>
          <span> API 사용에 대한 오류이력 조회가 가능합니다</span>
        </template>
      </DataHubComponent>
    </div>
    <ModalApiDetail ref="modalApiDetail" :fullscreen="isViewport('<', 'sm')" />
  </div>
</template>

<script>

import { Base } from '@/min/Base.min'
import ModalApiDetail from '@/views-dataHub/modal/ModalApiDetail'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
import CellRenderDetailbuttons from '@/views-dataHub/components/cellRenderer/CellRenderDetailbuttons'
import { apiSelectInfoList, apiSelectHistList, apiReprocessingAPIData, apiUpdateApiRetryStatus, apiUpdateApiRetryProc } from '@/api/dataHub'
import { mapState } from 'vuex'

const routeName = 'ApiAlaramHistManagement'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { DataHubComponent, CellRenderButtons, ModalApiDetail, CellRenderDetailbuttons },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfoApi: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      paginationInfoHist: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectApiData: [],
      selectHistoryData: [],
      selectedItem: [],
      searchApiItems: [
      { label: 'API명', type: 'input', size: 8, model: 'api_name', placeholder: 'API명을 검색하세요' },
        { label: '연동방식', type: 'select', size: 8, placeholder: '연동방식을 선택하세요', model: 'exec_mode_cd', multiple: true, setting: { allOption: { toggle: true } }, options: [
           { label: 'On-Demand', value: 'O' }, { label: 'Batch', value: 'B' }]
        },
      ],
      searchHistoryItems: [
      { label: '조회기간', type: 'date', size: 8, model: 'check_date' }
      ],
      searchApiModel: {
        api_name: '',
        exec_mode_cd: []
      },
      searchHistoryModel: {
        check_date: []
      },
      sortInfo: {}
    }
  },
  computed: {
    ...mapState({
      dataHub: (state) => state.dataHub,
     }),
    selectApiAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
      }
      const columns = [
        { type: '', prop: 'api_name', name: 'API 명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'exec_mode_cd', name: '연동방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_url', name: '접근 URL', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_desc', name: '설명', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false }]
      return { options, columns, data: this.selectApiData, getRightClickMenuItems: () => { return [] } }
    },
    selectHistoryAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
      }
      const columns = [
        { type: '', prop: 'create_time', name: '연동 일자', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_name', name: 'API 명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'exec_mode_cd', name: '연동방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'result_msg', name: '연동 결과 정보', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: '', name: '기능', minWidth: 50, suppressMenu: true, sortable: false, alignItems: 'left', cellRendererFramework: 'CellRenderDetailbuttons', cellRendererParams: { name: '상세정보', type: 'alarm', action: this.handleOpenModalDetail.bind(this) } },
        { type: '', prop: '', name: '', minWidth: 50, flex: 1, suppressMenu: true, sortable: false, alignItems: 'left', cellRendererFramework: 'CellRenderButtons', cellRendererParams: { name: '재처리', key: 'reprocess', action: this.handleReprocessingData.bind(this) } },
      ]

        if (this.appOptions.debug) {
          columns.unshift({ type: '', prop: 'proc_serial', name: 'PROC_SERIAL(degubMode 전용)', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: false })
        }

        const isFail = this.selectHistoryData.filter((col) => {
          return col.result_cd_desc === 'FAIL'
        })

      return { options, columns: columns, data: isFail, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onClickSearchApi()
  },
  methods: {
    onSortedChangeApi(param) {
       this.sortInfo = param
       this.onLoadList()
      },
    onSortedChangeHist(param) {
       this.sortInfo = param
       this.onLoadHistList()
      },
    handleApiSearchClear() {
      this.searchApiModel.api_name = ''
      this.searchApiModel.exec_mode_cd = []
      this.onClickSearchApi()
    },
    handleHistorySearchClear() {
      this.selectHistoryData = []
      this.searchHistoryModel.check_date = []
      this.onClickSearchHist()
    },
    onClickSearchApi(params) {
      this.onLoadList(params)
    },
    onClickSearchHist(params) {
      this.onLoadHistList(params)
    },
    async onLoadList(params) {
      const target = ({ vue: this.$refs.selectApi })
      this.openLoading(target)
      const param = {
        api_name: this.searchApiModel.api_name,
        exec_mode_cd: this.searchApiModel.exec_mode_cd,
        limit: this.paginationInfoApi.pageSize,
        page: this.paginationInfoApi.currentPage,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
       }
    try {
        const res = await apiSelectInfoList(param)
        this.selectApiData = res?.result
        this.paginationInfoApi.totalCount = res.total // 총 항목 수 설정
        this.paginationInfoApi.totalPages = Math.ceil(this.paginationInfoApi.totalCount / this.paginationInfoApi.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    selectedAPIRow(param) {
      this.selectedItem = param
      this.onLoadHistList()
    },
    async onLoadHistList(params) {
      const target = ({ vue: this.$refs.selectHistory })
      this.openLoading(target)
      const defaultDate = null
      const param = { api_id: this.selectedItem[0]?.api_id,
      result_cd: 'F',
      start_date: this.formatterDateTime(null, null, this.searchHistoryModel.check_date[0] ? this.searchHistoryModel.check_date[0] : defaultDate),
      end_date: this.formatterDateTime(null, null, this.searchHistoryModel.check_date[1] ? this.searchHistoryModel.check_date[1] : defaultDate),
      limit: this.paginationInfoHist.pageSize,
      page: this.paginationInfoHist.currentPage,
      sort_column_name: this.sortInfo.colId,
      sort_type: this.sortInfo.sort
      }
      try {
          const res = await apiSelectHistList(param)
          this.selectHistoryData = res?.result
          this.paginationInfoHist.totalCount = res.total
          this.paginationInfoHist.totalPages = Math.ceil(this.paginationInfoHist.totalCount / this.paginationInfoHist.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

    async handleReprocessingData(row, type) {
      const rowData = row
      try {
        await this.confirm('재처리하시겠습니까?', '재처리 확인', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        })
        const param = {
          serialNum: row.proc_serial,
          targetUrl: row.targeturl,
          requestParam: row.request_param
        }
        const res = await apiUpdateApiRetryProc(param)
        if (res.result) {
          this.$message({
            type: 'success',
            message: '재처리 성공했습니다.'
          })
        }
      } catch (error) {
        console.error('재처리 에러:', error)
        this.$message({
          type: 'error',
          message: '재처리에 실패했습니다.'
        })
      } finally {
         this.onLoadHistList()
      }
    },
    handleOpenModalDetail(type, row) {
      this.$refs.modalApiDetail.open({ type, row })
    },

  }
}
</script>

<style lang="scss" scoped>
.ApiAlaramHistManagement {
  .main-layout{
    display: flex ;
    flex-direction: row !important;
    height: 100% !important;
  }
    .ag-header-cell-label {
     .th--class{
       display: flex !important;
     }
  }
}

</style>

