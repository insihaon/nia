<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="trafficAnalysis"
      :ag-grid="trafficAgGrid"
      :items="searchItems"
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
import { apiSelectInOutTrafficList } from '@/api/nia'

const routeName = 'TrafficAnalysisPort'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
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
        { label: '감시방식', type: 'select', placeholder: '상태를 선택하세요', model: 'search_type', options: [
            { label: 'OnDemand', value: 'OnDemand' },
            { label: '실시간', value: 'live' },
        ] },
        { label: '장비', type: 'input', model: 'node_name', placeholder: '장비명을 검색하세요', disabled: false },
        { label: '인터페이스', type: 'input', model: 'if_name', placeholder: '인터페이스를 검색하세요', disabled: false },
        { label: '시작일시', type: 'date', model: 'datetime', disabled: false },
        { label: '종료일시', type: 'date', model: 'datetime', disabled: false },

      ],
      searchModel: {
        search_type: 'OnDemand',
        node_name: '',
        if_name: '',
      }
    }
  },

  computed: {
    trafficAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'measured_datetime', name: '수집시간', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'node_name', name: '장비명', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'if_name', name: '인터페이스명', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'if_num', name: '인터페이스 번호', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'tx_bit_rate', name: '발신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'rx_bit_rate', name: '수신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'tx_pkt_rate', name: '발신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'rx_pkt_rate', name: '수신 bps', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.trafficData, getRightClickMenuItems: () => { return [] } }
    }
  },
  watch: {
    'searchModel.search_type'(n) {
      this.searchItems.forEach(item => {
        if (item.model !== 'search_type') {
          item.disabled = n === 'live'
          this.onSearchLive()
        }
      })
    }
  },
  mounted() {
    this.onLoadTrafficList()
  },
  methods: {
    onClickSearch(params) {
      this.onLoadTrafficList(params)
    },
    onSearchLive() {
      setTimeout(() => {
        this.onLoadTrafficList()
      }, 60 * 1000)
    },
    async onLoadTrafficList() {
      const target = { vue: this.$refs.trafficAnalysis }
      this.openLoading(target)
      const param = {
        node_name: this.searchModel.node_name,
        if_name: this.searchModel.if_name,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
      }
      if (this.searchModel?.datetime) {
        const dateTime = this.$refs?.trafficAnalysis.searchModel.datetime
        this._merge(param, { start_date: dateTime[0], end_date: dateTime[1] })
      }
      try {
        const res = await apiSelectInOutTrafficList(param)
        this.trafficData = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
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
  }
}
</script>

