<template>
  <div :class="{ [name]: true }">
    <div class="main-layout">
      <DataHubComponent
        ref="selectApi"
        :ag-grid="selectApiAgGrid"
        :items="searchApiItems"
        :search-model.sync="searchApiModel"
        :pagination-info="paginationInfoApi"
        title=""
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
          <span> API 사용에 대한 이력 조회가 가능합니다 </span>
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
import { apiDataListTest, apiSelectInfoList, apiSelectHistList, apiSelectApicodeList } from '@/api/dataHub'
import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'

const routeName = 'ApiInfoHistManagement'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { DataHubComponent, CellRenderButtons, ModalApiDetail, apiDataListTest, hyperLinkTextRender, CellRenderDetailbuttons },
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
        { label: 'API명', type: 'input', placeholder: 'API명을 검색하세요', model: 'api_name' },
        { label: '연동방식', type: 'select', placeholder: '연동방식을 선택하세요', model: 'exec_mode_cd', multiple: true, setting: { allOption: { toggle: true } }, options: [
           { label: 'On-Demand', value: 'O' }, { label: 'Batch', value: 'B' }]
        },
      ],
      searchHistoryItems: [
        { label: '조회기간', type: 'date', size: 8, model: 'check_date' },
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
    selectApiAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
      }
      const columns = [
        { type: '', prop: 'api_name', name: 'API명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false, fill: false },
        { type: '', prop: 'exec_mode_cd', name: '연동방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_url', name: '접근 URL', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_desc', name: '설명', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
      ]

      return { options, columns, data: this.selectApiData, getRightClickMenuItems: () => { return [] } }
    },
    selectHistoryAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
      }
      const columns = [
        { type: '', prop: 'create_time', name: '시간', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_name', name: 'API명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'exec_mode_cd', name: '연동방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'result_cd_desc', name: '연동 결과 정보', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: '', name: '기능', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: false, filterable: false,
        cellRendererFramework: 'CellRenderDetailbuttons', cellRendererParams: { name: '상세정보', type: 'info', action: this.handleOpenModalDetail.bind(this) } }
      ]

          if (this.appOptions.debug) {
          columns.unshift({ type: '', prop: 'proc_serial', name: 'PROC_SERIAL(degubMode 전용)', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false })
        }

        const isSuccess = this.selectHistoryData.filter((col) => {
          return col.result_cd_desc === 'SUCCESS'
        })

      return { options, columns, data: isSuccess, getRightClickMenuItems: () => { return [] } }
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
      this.selectHistoryData = []
      this.onLoadList()
    },
    handleHistorySearchClear() {
      this.searchHistoryModel.check_date = []
      if (this.selectedItem[0]?.api_id) {
        this.onClickSearchHist()
      }
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
        limit: this.paginationInfoApi.pageSize,
        page: this.paginationInfoApi.currentPage,
        api_name: this.searchApiModel.api_name,
        exec_mode_cd: this.searchApiModel.exec_mode_cd,
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
      const target = ({ vue: this.$refs.selectApi })
      this.openLoading(target)
      const defaultDate = null
        const param = {
        api_id: this.selectedItem[0]?.api_id,
        result_cd: 'S',
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

    handleOpenModalDetail(type, row) {
      this.$refs.modalApiDetail.open({ type, row })
    },
  }
}
</script>

<style lang="scss" scoped>
   .ApiInfoHistManagement{
     padding : 0 40px !important;
    .main-layout{
      display: flex ;
      flex-direction: row !important;
      height: 100% !important;
    }

    .common-padding{
     padding : 15px 30px !important;
    }
   }
</style>
