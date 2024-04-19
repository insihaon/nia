<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="trafficAnalysis"
      :ag-grid="trafficAgGrid"
      :is-button-slot="false"
      :items="searchItems"
      :is-grid-loading="loading"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearch"
      @onChangePage="(curPage) => onChangePage(curPage)"
      @searchClear="searchClear"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiTrafficAgencyList, apiSelectAgencyCodeList } from '@/api/nia'

const routeName = 'TrafficAnalysisInstitution'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        // pagerCount: 11
      },
      selectedRow: [],
       trafficData: [],
        searchItems: [
        { label: '이용기관(S)', type: 'select', multiple: false, placeholder: '이용기관을 선택하세요', model: 'src_nren_name', icon: 'el-icon-setting', setting: { allOption: { toggle: true } }, options: [] },
        { label: 'IP(S)', type: 'input', multiple: false, placeholder: 'SEARCH', icon: 'el-icon-search', model: 'src_ip' },
        { label: '이용기관(D)', type: 'select', multiple: false, placeholder: '이용기관을 선택하세요', model: 'dst_nren_name', icon: 'el-icon-warning', setting: { allOption: { toggle: true } }, options: [] },
        { label: 'IP(D)', type: 'input', multiple: false, placeholder: 'SEARCH', icon: 'el-icon-search', model: 'dst_ip' },
        { label: 'Top N', type: 'select', multiple: false, placeholder: '', model: 'top_n', icon: 'el-icon-warning',
          options: [
            { label: '10', value: 10 },
            { label: '30', value: 30 },
            { label: '50', value: 50 },
            { label: '100', value: 100 },
          ], }
      ],
      searchModel: {
        src_nren_name: [],
        src_ip: '',
        dst_nren_name: [],
        dst_ip: '',
        top_n: '',
      },
      selectCodeData: []
    }
  },

  computed: {
    trafficAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'row_num', name: 'Rank', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'src_nren_name', name: '이용기관(Soruce)', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'src_ip', name: 'IP(Soruce)', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'dst_nren_name', name: '이용기관(Destination)', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'dst_ip', name: 'IP(Destination)', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'packet_bytes', name: 'Mbyte', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.trafficData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadTrafficList()
    this.onloadAgencyCodeList()
  },
  methods: {
    onClickSearch(params) {
      this.onLoadTrafficList(params)
    },
    async onLoadTrafficList() {
       const param = {
        src_nren_name: this.searchModel.src_nren_name,
        src_ip: this.searchModel.src_ip,
        dst_nren_name: this.searchModel.dst_nren_name,
        dst_ip: this.searchModel.dst_ip,
        top_n: this.searchModel.top_n,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
       }
      try {
        this.loading = true
        const res = await apiTrafficAgencyList(param)
        this.trafficData = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    },

    async onloadAgencyCodeList() {
      try {
        const res = await apiSelectAgencyCodeList()
        this.selectCodeData = res.result.map(item => ({ label: item.name, value: item.id }))
        this.searchItems[0].options = this.selectCodeData
        this.searchItems[2].options = this.selectCodeData
      } catch (error) {
        console.error(error)
      }
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadTrafficList()
    },
    searchClear() {
      this.searchModel = {}
      this.onLoadTrafficList()
    }
  },
}
</script>

