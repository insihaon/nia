<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="trafficAnalysis"
      :ag-grid="usageAgGrid"
      :is-button-slot="false"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearch"
      @onChangePage="onChangePage"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectEquipAmountUsedList } from '@/api/nia'
import { AppOptions } from '@/class/appOptions'

const routeName = 'EquipmentUsage'
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
        { label: '감시방식', type: 'select', multiple: true, placeholder: '상태를 선택하세요', model: 'monitoring_type', setting: { allOption: { toggle: true } },
        options: [
            { label: 'OnDemand', value: 'OnDemand' },
            { label: '실시간', value: 'live' },
        ] },
        { label: '장비', type: 'input', model: 'equipment', placeholder: '장비명을 검색하세요' },
        { label: 'CPU >=', type: 'input', model: 'cpu_usage' },
        { label: 'Memory >=', type: 'input', model: 'mem_usage' },
        { label: '시작일시', type: 'date', model: 'datetime' },
        { label: '종료일시', type: 'date', model: 'datetime' },
      ],
      searchModel: {
        monitoring_type: [],
        cpu_usage: '',
        mem_usage: '',
        datetime: [],
      },
      sortInfo: {}
    }
  },

  computed: {
    usageAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'measured_datetime', name: '수집기간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'node_nm', name: '장비명', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'cpu_usage', name: 'CPU 사용량', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'mem_usage', name: 'MEM 사용량', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center' },
      ]
      return { options, columns, data: this.equitmentsData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadUsageList()
  },
  methods: {
    cellTemp() {},
    sortTemp() {},
    onSortedChange(param) {
       this.sortInfo = []
       this.onLoadUsageList()
    },
    onClickSearch(params) {
      this.onLoadUsageList(params)
    },
    async onLoadUsageList() {
      const target = { vue: this.$refs.trafficAnalysis }
      this.openLoading(target)
      const param = {
        node_nm: this.searchModel.node_nm,
        cpu_usage: this.searchModel.cpu_usage,
        mem_usage: this.searchModel.node_nm
      }
      if (this.searchModel?.datetime) {
        const dateTime = this.$refs?.trafficAnalysis.searchModel.datetime
        this._merge(param, { start_date: dateTime[0], end_date: dateTime[1] })
      }
      try {
        const res = await apiSelectEquipAmountUsedList(param)
        this.equitmentsData = res?.result
        this.paginationInfo.totalCount = res.total
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
  },
}
</script>

