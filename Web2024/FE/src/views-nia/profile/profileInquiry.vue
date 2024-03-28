<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="trafficAnalysis"
      :ag-grid="trafficAgGrid"
      :items="searchItems"
      :is-excel="true"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearch"
      @onChangePage="onChangePage"
      @searchClear="searchClear"
    />
    <ModalProfileDetail ref="ModalProfileDetail" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CellRenderHyperlink from '@/views-nia/components/cellRenderer/CellRenderHyperlink'
import ModalProfileDetail from '@/views-nia/modal/ModalProfileDetail'
import { apiSelectProfileList } from '@/api/nia'

const routeName = 'ProfileInquiry'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, CellRenderHyperlink, ModalProfileDetail },
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
      selectedRow: [],
      trafficData: [],
      searchItems: [
        { label: '제목', type: 'input', model: 'profile_title', placeholder: '제목을 검색하세요' },
        { label: '네트워크', type: 'input', model: 'network_type', placeholder: '인터페이스를 검색하세요' },
        { label: '장애대응', type: 'input', model: 'processing_template', placeholder: '인터페이스를 검색하세요' }
      ],
      searchModel: {
        profile_title: '',
        network_type: '',
        processing_template: ''
      },
    }
  },

  computed: {
    trafficAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'rownum', name: '번호', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'profile_title', name: '제목', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center',
          cellRendererFramework: 'CellRenderHyperlink', cellRendererParams: { type: 'profileDetail', action: this.handleOpenModalDetail.bind(this) } },
        { type: '', prop: 'network_type', name: '네트워크', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'processing_template', name: '장애대응', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'auto_process_info', name: '자동처리기간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'chng_datetime', name: '수정일', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.trafficData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadTrafficList()
  },
  methods: {
    onSortedChange(param) {
       this.onLoadTrafficList()
    },
    onClickSearch(params) {
      this.onLoadTrafficList(params)
    },
    async onLoadTrafficList() {
      const target = { vue: this.$refs.trafficAnalysis }
      this.openLoading(target)
      const param = {
        profile_title: this.searchModel.profile_title,
        network_type: this.searchModel.network_type,
        processing_template: this.searchModel.processing_template,
      }
      try {
        const res = await apiSelectProfileList(param)
        this.trafficData = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadSopList()
    },
    searchClear() {
      this.searchModel = {}
    },
    handleOpenModalDetail(row, type) {
      this.$refs.ModalProfileDetail.open({ row, type })
    },
  },
}
</script>

