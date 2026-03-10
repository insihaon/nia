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
        @selectedRow="selectedApiRow"
        @keyupEnter="onClickSearchApi"
        @searchClear="handleApiSearchClear"
        @sortedChange="onSortedChangeApi"
      >
        <template #inquireButton>
          <span> API 서비스에 대한 사용신청이 가능합니다</span>
        </template>
      </DataHubComponent>

      <DataHubComponent
        ref="selectHistoryAuth"
        :ag-grid="applyHistoryAgGrid"
        title="신청 이력"
        :items="searchApplyHistoryItems"
        :search-model.sync="searchApplyHistoryModel"
        :pagination-info="paginationInfoHist"
        class="w-50 h-100 flex-fill"
        @selectedRow="selectedApiHistRow"
        @handleClickSearch="onClickSearchHist"
        @searchClear="handleApplyHistorySearchClear"
        @sortedChange="onSortedChangeHist"
      >
        <template #inquireButton>
          <span> API 서비스 사용신청 이력을 확인할 수 있습니다</span>
        </template>
      </DataHubComponent>
    </div>
    <ModalAuthApply ref="ModalAuthApply" />
    <ModalSelectDataSet ref="ModalSelectDataSet" />
  </div>
</template>
<script>

  import { Base } from '@/min/Base.min'
  import ModalAuthApply from '@/views-dataHub/modal/ModalAuthApply'
  import ModalSelectDataSet from '@/views-dataHub/modal/ModalSelectDataSet'
  import DataHubComponent from '@/views-dataHub/components/CompTemplate'
  import CellRenderAPIbuttons from '@/views-dataHub/components/cellRenderer/CellRenderAPIbuttons'
  import CellRenderApplybuttons from '@/views-dataHub/components/cellRenderer/CellRenderApplybuttons'
  import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
  import { apiSelectInfoList, apiSelectAuthHistList, apiDeleteAuthHistList } from '@/api/dataHub'
  import Eventbus from '@/utils/event-bus'
  import { mapState } from 'vuex'
const routeName = 'ApiUseAuthApply'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { DataHubComponent, CellRenderAPIbuttons, ModalAuthApply, ModalSelectDataSet, hyperLinkTextRender, CellRenderApplybuttons },
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
          pageSize: 10, // 페이지당 항목 수
          totalCount: 0, // 총 항목 수
          totalPages: null, // 전체 페이지 수
          pagerCount: 11
        },
        selectApiData: [],
        applyHistoryData: [],
        selectedItem: [],
        selectedHistItem: [],
        searchApiItems: [
          { label: 'API명', type: 'input', size: 8, model: 'api_name', placeholder: 'API명을 검색하세요' },
          { label: '연동방식', type: 'select', size: 8, model: 'exec_mode_cd', placeholder: '연동방식을 선택하세요', multiple: true, setting: { allOption: { toggle: true } }, options: [
           { label: 'On-Demand', value: 'O' }, { label: 'Batch', value: 'B' }]
        },
        ],
        searchApplyHistoryItems: [
          { label: '조회기간', type: 'date', model: 'check_date' },
        ],
        searchApiModel: {
        api_name: '',
        api_url: []
      },
      searchApplyHistoryModel: {
        check_date: []
      },
      sortInfo: {}
      }
    },
    computed: {
      checkState() {
        return this.blackDtlList
      },
      ...mapState({
        blackDtlList: (state) => state.user.blackDtlList
      }),
      selectApiAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
        }
        const columns = [
          { type: '', prop: 'api_name', name: 'API 명', minWidth: 30, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false, },
          { type: '', prop: 'api_url', name: '접근 URL', minWidth: 80, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'exec_mode_cd', name: '연동 방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'api_desc', name: '설명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: '_', name: '기능', minWidth: 30, flex: 1, suppressMenu: true, alignItems: 'left', cellRendererFramework: 'CellRenderApplybuttons', sortable: false,
            cellRendererParams: { useApply: { name: '권한신청', type: 'auth', action: this.handleOpenModalDetail.bind(this) }, selectDataSet: { name: 'API 데이터', type: 'dataSet'/* , action: this.handleOpenModalDetail.bind(this) */ } }
          },
        ]
        if (this.appOptions.debug) {
          columns.unshift({ type: '', prop: 'api_id', name: 'API_ID(degubMode 전용)', minWidth: 30, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false })
        }
        return { options, columns, data: this.selectApiData, getRightClickMenuItems: () => { return [] } }
      },
      applyHistoryAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
        }
        const columns = [
          { type: '', prop: 'api_name', name: 'API 명', minWidth: 50, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'create_time', name: '시간', minWidth: 80, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false, },
          { type: '', prop: 'status_cd', name: '상태', minWidth: 30, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
          cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'reject', action: this.openRejectMessage.bind(this) } },
          { type: '', prop: 'system_nm', name: '연동 시스템', minWidth: 80, flex: 1, suppressMenu: true, alignItems: 'left' },
          { type: '', prop: 'api_url', name: 'API URL', minWidth: 80, flex: 1, suppressMenu: true, alignItems: 'left' },
          { type: '', prop: 'api_desc', name: '사용 용도', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'expird_date', name: '만료일', minWidth: 80, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', name: '기능', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: false, cellRendererFramework: 'CellRenderAPIbuttons', cellRendererParams: { name: 'API KEY', type: 'api', key: 'doubleKey', action: this.openPopover.bind(this) } },
        ]

        const sortData = this.applyHistoryData?.filter((col) => {
            return col.api_id === this.selectedItem[0]?.api_id
        })
        return { options, columns, data: sortData, getRightClickMenuItems: () => { return [] } }
      },
    },
    mounted() {
    Eventbus.$on('refreshAuthApplyData', () => {
      this.refreshData()
    })
      this.onLoadList()
    },
    beforeDestroy() {
    Eventbus.$off('refreshAuthApplyData')
  },
methods: {
    onSortedChangeApi(param) {
       this.sortInfo = param
       this.onLoadList()
      },
    onSortedChangeHist(param) {
       this.sortInfo = param
       this.onLoadHistAuthList()
      },
    handleApiSearchClear() {
      this.applyHistoryData = []
      this.searchApiModel.api_name = ''
      this.searchApiModel.exec_mode_cd = []
      this.onLoadList()
    },
    handleApplyHistorySearchClear() {
      this.applyHistoryData = []
      this.searchApplyHistoryModel.check_date = []
      this.onClickSearchHist()
    },
    openPopover(data, type) {
      if (type === 'api') {
        this.visible = !this.visible
      } else {
        this.cancleApplyData(data)
      }
    },
    openRejectMessage() {
      this.visible = !this.visible
    },
    onClickSearchApi(params) {
      this.onLoadList(params)
    },
    onClickSearchHist(params) {
      this.onLoadHistAuthList(params)
    },
    selectedApiRow(param) {
      this.selectedItem = param
      this.onLoadHistAuthList()
    },
    selectedApiHistRow(param) {
      this.selectedHistItem = param
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
      this.paginationInfoApi.totalCount = res.total
      this.paginationInfoApi.totalPages = Math.ceil(this.paginationInfoApi.totalCount / this.paginationInfoApi.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async onLoadHistAuthList(params) {
      console.log(this.searchHistoryModel)
      const target = ({ vue: this.$refs.selectHistoryAuth })
      this.openLoading(target)
      const defaultDate = null
      const create_time = this.searchApplyHistoryModel.check_date && this.searchApplyHistoryModel.check_date[0]
      ? this.formatterDateTime(null, null, this.searchApplyHistoryModel.check_date[0])
      : defaultDate

      const end_time = this.searchApplyHistoryModel.check_date && this.searchApplyHistoryModel.check_date[1]
      ? this.formatterDateTime(null, null, this.searchApplyHistoryModel.check_date[1])
      : defaultDate
      const param = {
        api_id: this.selectedItem[0]?.api_id,
        start_create_time: create_time,
        end_create_time: end_time,
      limit: this.paginationInfoHist.pageSize,
      page: this.paginationInfoHist.currentPage,
      sort_column_name: this.sortInfo.colId,
      sort_type: this.sortInfo.sort
      }
    try {
      const res = await apiSelectAuthHistList(param)
      this.applyHistoryData = res?.result
      this.paginationInfoHist.totalCount = res.total
      this.paginationInfoHist.totalPages = Math.ceil(this.paginationInfoHist.totalCount / this.paginationInfoHist.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
      handleOpenModalDetail(type, row) {
        if (type === 'auth') {
          this.$refs.ModalAuthApply.open({ type, row })
        }
      },
      cancleApplyData(params) {
        this.$confirm('신청을 취소 하시겠습니까?', '신청 취소', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
        }).then(async() => {
          try {
              const param = { hist_serial: params.hist_serial }
              const res = await apiDeleteAuthHistList(param)
                 if (res.result === 1) {
                  this.$message.error({ message: `신청취소 되었습니다.` })
                 } else {
                   this.$message.success({ message: `취소 처리가 실패 되었습니다.` })
                 }
                 this.onLoadHistAuthList()
          } catch (error) {
            console.log(error)
          }
        })
    },
      refreshData() {
        this.onLoadList()
        this.onLoadHistAuthList()
      }
    }
  }
</script>

<style lang="scss" scoped>
   .ApiUseAuthApply{
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

