<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="usageStatus"
      :ag-grid="reqUsageApiAgGrid"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearchReqUsageApi"
      @keyupEnter="onClickSearchReqUsageApi"
      @searchClear="handleDataSearchClear"
      @sortedChange="onSortedChange"
      @selectedRow="selectedItems"
    >
      <template #inquireButton>
        <span> API 서비스에 대한 사용현황입니다</span>
      </template>
    </DataHubComponent>
    <ModalApiDetail ref="modalApiDetail" :fullscreen="isViewport('<', 'sm')" />
  </div>

</template>
<script>
import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
import ModalApiDetail from '@/views-dataHub/modal/ModalApiDetail'
import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
import CellRenderAPIbuttons from '@/views-dataHub/components/cellRenderer/CellRenderAPIbuttons'
import { apiSelectAuthHistList, apiUpdateApiAuthProc, apiSelectApiList } from '@/api/dataHub'

const routeName = 'ApiUsageStatus'
export default {
  name: routeName,
      // eslint-disable-next-line vue/no-unused-components
  components: { DataHubComponent, CellRenderButtons, CellRenderAPIbuttons, hyperLinkTextRender, ModalApiDetail },
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
      reqUsageApiData: [],
    searchItems: [
      { label: '연동 시스템', type: 'input', model: 'system_nm', placeholder: '연동시스템을 검색하세요', },
      { label: 'API명', type: 'select', size: 8, model: 'api_id', multiple: false, placeholder: 'API명을 검색하세요', setting: { allOption: { toggle: true } }, options: null

      },
      { label: '상태', type: 'select', size: 8, model: 'status_cd', placeholder: '상태를 선택하세요', multiple: true, setting: { allOption: { toggle: true } }, options: [
       { label: '신청', value: 'APPLY' }, { label: '승인', value: 'GRANT' }, { label: '반려', value: 'REJECT' }, { label: '회수', value: 'REVOKE' }]
      },
    ],
    searchModel: {
      api_id: '',
      status_cd: [],
      system_nm: ''
    },
    sortInfo: {},
    selectApiList: [],
    apiList: [],
    selectedRow: []
    }
  },

  computed: {
    reqUsageApiAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'rank', name: '번호', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_name', name: 'API명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
        cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenModalDetail.bind(this) } },
        { type: '', prop: 'system_nm', name: '연동시스템명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_desc', name: '사용 용도', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'status_cd', name: '상태', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'exec_mode_cd', name: '구분', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'update_time', name: '마지막 연동 시간', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: '_', name: '기능', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', cellRendererFramework: 'CellRenderAPIbuttons', cellRendererParams: { type: 'api', key: 'revoke', action: this.openPopover.bind(this) } },
      ]
      return { options, columns, data: this.reqUsageApiData, getRightClickMenuItems: () => { return [] } }
    }
  },
  mounted() {
    this.onLoadList()
    this.onLoadApiList()
  },
  methods: {
    async onLoadApiList() {
      const res = await apiSelectApiList()
      this.apiList = res?.result

      this.selectApiList = this.apiList.map(item => ({ label: item.api_name, value: item.api_id }))
      const tableNmItem = this.searchItems.find(item => item.model === 'api_id')
      if (tableNmItem) {
        tableNmItem.options = this.selectApiList
      }
    },
    onSortedChange(param) {
       this.sortInfo = param
       this.onLoadList()
    },
    selectedItems(param) {
      this.selectedRow = param
    },
    handleDataSearchClear() {
      this.searchModel.api_id = ''
      this.searchModel.status_cd = []
      this.searchModel.system_nm = ''
      this.onLoadList()
    },
      openPopover(data, type) {
      if (type === 'api') {
        this.visible = !this.visible
      } else {
        this.handleAuth(data, type)
      }
    },
    onClickSearchReqUsageApi(params) {
      this.onLoadList(params)
    },
    async onLoadList(params) {
      const target = ({ vue: this.$refs.usageStatus })
      this.openLoading(target)
      try {
      const param = {
        api_id: this.searchModel.api_id,
        system_nm: this.searchModel.system_nm,
        status_cd: this.searchModel.status_cd,
        exec_mode_cd: 'O',
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
       }
      const res = await apiSelectAuthHistList(param)
      this.reqUsageApiData = res?.result
      this.paginationInfo.totalCount = res.total
      this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
    } catch (error) {
        console.error(error)
      } finally {
          this.closeLoading(target)
      }
    },
      async handleAuth(data, type) {
        const confirmMessage = `요청된 권한을 회수 하시겠습니까?`
        const successMessage = `권한 회수 되었습니다.`
        const errorMessage = `권한 회수에 실패했습니다.`
        this.$confirm(confirmMessage, `API 권한 회수`, {
          confirmButtonText: '예',
          cancelButtonText: '아니오',
          type: 'warning'

        }).then(async () => {
          const param = {
            api_id: data.api_id,
            api_name: data.api_name,
            api_url: data.api_url,
            system_id: data.system_id,
            hist_serial: data.hist_serial,
            status_cd: 'REVOKE'
          }
            try {
                const res = await apiUpdateApiAuthProc(param)
                if (res.result) {
                  this.$message.success({ message: successMessage })
                }
          } catch (error) {
            this.$message.error({ message: errorMessage })
            console.log(error)
          } finally {
            this.onLoadList()
          }
        })
    },
     handleOpenModalDetail(type, row) {
      this.$refs.modalApiDetail.open({ type, row })
    },
  }
}
</script>
<style lang="scss" scoped>

.ApiUsageStatus{
  .pop-over{
    margin-top: 15vh !important;
  }
}
</style>
