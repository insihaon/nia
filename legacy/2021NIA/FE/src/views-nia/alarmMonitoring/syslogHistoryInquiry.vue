<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="syslogHist"
      :ag-grid="syslogAgGrid"
      :is-excel="true"
      :title="titleName"
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
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiSyslogHistList, apiSyslogEquipmentList } from '@/api/nia'

const routeName = 'SyslogHistoryInquiry'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, CompAgGrid },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      titleName: 'SYSLOG_HIST',
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      syslogData: [],
      searchItems: [
        { label: 'FROM', type: 'basicDate', model: 'start_date' },
        { label: 'TO', type: 'basicDate', model: 'end_date' },
        { label: '알람번호', type: 'input', model: 'alarmno' },
        { label: '장비ID', type: 'input', model: 'node_num' },
        { label: '장비명', type: 'select', model: 'node_nm', options: [] },
        { label: '장애내용', type: 'input', model: 'alarmmsg' }
      ],
      searchModel: {
        alarmno: '',
        node_num: '',
        node_nm: '',
        alarmmsg: '',
        start_date: '',
        end_date: ''
      }
    }
  },
  computed: {
    syslogAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'syslog_alarmtime', name: '경보 발생 시간', minWidth: 50, flex: 0, suppressMenu: true, },
        { type: '', prop: 'alarmno', name: '알람 번호', minWidth: 50, flex: 0, suppressMenu: true, sortable: false, filterable: false },
        { type: '', prop: 'node_num', name: '장비 ID', minWidth: 50, flex: 0, suppressMenu: true, sortable: false, filterable: false },
        { type: '', prop: 'node_nm', name: '장비명', minWidth: 50, flex: 0, suppressMenu: true, sortable: false, filterable: false },
        { type: '', prop: 'alarmmsg', name: '장애내용', minWidth: 50, flex: 0, suppressMenu: true, sortable: false, filterable: false },
        { type: '', prop: 'alarmloc', name: '인터페이스명', minWidth: 50, flex: 0, suppressMenu: true, sortable: false, filterable: false },
        { type: '', prop: 'etc', name: 'SYSLOG 원본 메시지', minWidth: 50, flex: 0, suppressMenu: true, sortable: false, filterable: false },
        { type: '', prop: 'status', name: '상태', minWidth: 50, flex: 0, suppressMenu: true, sortable: false, filterable: true, cellStyle: this.getCellStyle },
      ]
      return { options, columns, data: this.syslogData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadSyslogHistList()
    this.onLoadEquipmentList()
  },
  methods: {
    getCellStyle(params) {
      let color = ''
      if (params.value === '자동 마감') {
         color = 'red'
      } else {
         color = 'blue'
      }
      return { color: color }
    },
    onClickSearch(params) {
      this.onLoadSyslogHistList(params)
    },
    async onLoadEquipmentList() {
      try {
        const res = await apiSyslogEquipmentList()
        const nodeData = res.result.map((item) => ({
          label: item.node_nm,
          value: item.node_nm,
        }))
        this.searchItems[4].options = nodeData
      } catch (error) {
        console.error(error)
      }
    },
    async onLoadSyslogHistList() {
      const target = { vue: this.$refs.syslogHist }
      this.openLoading(target)
      const param = {
        start_date: this.searchModel.start_date !== '' ? this.searchModel.start_date : null,
        end_date: this.searchModel.end_date !== '' ? this.searchModel.end_date : null,
        alarmno: this.searchModel.alarmno,
        node_nm: this.searchModel.node_nm,
        node_num: this.searchModel.node_num,
        alarmmsg: this.searchModel.alarmmsg,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
      }
      try {
        const res = await apiSyslogHistList(param)
        this.syslogData = res?.result
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
      this.onLoadSyslogHistList()
    },
    searchClear() {
      this.searchModel = {}
      this.onLoadSyslogHistList()
    }
  }
}
</script>
