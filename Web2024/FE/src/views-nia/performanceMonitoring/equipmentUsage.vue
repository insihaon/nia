<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="equipmentUsage"
      :ag-grid="usageAgGrid"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearch"
      @onChangePage="(curPage) => onChangePage(curPage)"
      @searchClear="searchClear"
      @onDebugTest="autoTest"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectEquipAmountUsedList } from '@/api/nia'

const routeName = 'EquipmentUsage'

function getFilterCalc(row, col, value, index) {
  return value === -1 ? '수집실패' : value + ' %'
}

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
      equitmentsData: [],
      searchItems: [
        { label: '감시방식', type: 'select', placeholder: '상태를 선택하세요', model: 'monitoring_type',
        options: [
            { label: 'OnDemand', value: 'OnDemand' },
            { label: '실시간', value: 'live' },
        ] },
        { label: '장비', type: 'input', model: 'node_nm', placeholder: '장비명을 검색하세요', disabled: false },
        { label: 'CPU >=', type: 'input', model: 'cpu_usage', placeholder: 'CPU를 검색하세요', disabled: false },
        { label: 'Memory >=', type: 'input', model: 'mem_usage', placeholder: 'Memory를 검색하세요', disabled: false },
        { label: '수집기간', type: 'dateTime', model: 'date_time', disabled: false },
      ],
      monitoring_type: 'OnDemand',
      searchModel: {
        monitoring_type: 'OnDemand',
        node_nm: '',
        cpu_usage: '',
        mem_usage: '',
      },
    }
  },
  computed: {
    usageAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'measured_datetime', name: '수집기간', minWidth: 80, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'node_nm', name: '장비명', minWidth: 80, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'cpu_usage', name: 'CPU 사용량', minWidth: 80, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true, format: getFilterCalc },
        { type: '', prop: 'mem_usage', name: 'MEM 사용량', minWidth: 80, flex: 0, suppressMenu: true, alignItems: 'center', valueFormatter: this.formatPercentage, format: getFilterCalc },
      ]

      return { options, columns, data: this.equitmentsData, getRightClickMenuItems: () => { return [] } }
    }
  },
  watch: {
    'searchModel.monitoring_type'(nVal) {
      // this.searchItems.forEach(item => {
      //   if(item.model !== 'search_type') {
      //     item.disabled = (nVal === 'OnDemand')
      //   }
      // })
      this.monitoring_type = nVal
    }
  },
  mounted() {
    this.onLoadUsageList()
    this.autoTest()
  },
  methods: {
     async autoTest() {
      const { assert, wait, onLoadUsageList, query } = this
      query.writer = 'daejeon'
      await onLoadUsageList()
      assert(this.equitmentsData.length > 0)
      await wait(1000)
    },
    onClickSearch(params) {
      this.onLoadUsageList(params)
    },
    async onLoadUsageList(vue = {}) {
      const target = { vue: this.$refs.equipmentUsage }
      this.openLoading(target)
      const param = {
        node_nm: this.searchModel.node_nm,
        cpu_usage: this.searchModel.cpu_usage / 100,
        mem_usage: this.searchModel.mem_usage / 100,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
      }
      const dateTime = this.$refs.equipmentUsage.searchModel.date_time
      if (dateTime) {
        this._merge(param, { start_date: dateTime[0], end_date: dateTime[1] })
      }
      try {
        const res = await apiSelectEquipAmountUsedList(param)
        this.equitmentsData = res?.result

        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadUsageList()
    },
    searchClear(searchModel) {
      this.searchModel = Object.assign(searchModel, { monitoring_type: this.monitoring_type })
      this.onLoadUsageList()
    }
  },
}
</script>

