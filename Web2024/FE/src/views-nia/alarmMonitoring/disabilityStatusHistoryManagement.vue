<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="inquiry"
      :ag-grid="alarmAgGrid"
      :is-button-slot="false"
      :items="searchItems"
      :is-grid-loading="loading"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="()=> onLoadAlarmCurHistList()"
      @sortChanged="onChangeSort"
      @searchClear="()=> onLoadAlarmCurHistList()"
      @onChangePage="onChangePage"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiAlarmCurAndHistList } from '@/api/nia'

const routeName = 'DisabilityStatusHistoryManagement'
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
      alarmCurHistList: [],
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      searchItems: [
        { label: 'TICKET ID', type: 'input', size: 8, model: 'TICKET_ID' },
        { label: 'TICKET 유형', type: 'select', size: 8, model: 'TICKET_TYPE', setting: { allOption: { toggle: true } },
        options: [
          { label: '장비장애(RT)', value: 'RT' },
          { label: '유해트래픽(NTT)', value: 'NTT' },
          { label: '이상트래픽(ATT2)', value: 'ATT2' },
        ] },
        { label: '발생시간', type: 'date', size: 8, model: 'INIT_DATE' },
        { label: '마감시간', type: 'date', size: 8, model: 'FIN_DATE' },
      ],
      searchModel: {
        TICKET_ID: '',
        TICKET_TYPE: '',
        INIT_START_DATE: '',
        INIT_END_DATE: '',
        CLOSE_START_DATE: '',
        CLOSE_END_DATE: ''
      },
    }
  },

  computed: {
    alarmAgGrid() {
      const options = { name: this.name, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true }
      const columns = [
      { type: '', prop: 'ticket_id', name: '티켓번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
      { type: '', prop: 'parent_ticket_id', name: '병합티켓번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
      { type: '', prop: 'fault_time', name: '발생시간', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return row.fault_time ? this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
      { type: '', prop: 'handling_fin_time', name: '마감시간', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return row.handling_fin_time ? this.formatterTimeStamp(row.handling_fin_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
      { type: '', prop: 'ticket_type', name: '장애 구분', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
      { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 내용', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
    ]
      return { options, columns, data: this.alarmCurHistList }
    },
  },
  mounted() {
    this.onLoadAlarmCurHistList()
  },
  methods: {
    onChangeSort(param) {
       this.onLoadAlarmCurHistList()
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadAlarmCurHistList()
    },
    async onLoadAlarmCurHistList() {
      const { pageSize: limit, currentPage: page } = this.paginationInfo
      const param = { limit, page }

      const searchModel = this.$refs?.inquiry?.searchModel ?? {}
      if (searchModel?.INIT_DATE) {
        const dateTime = searchModel.INIT_DATE
        this._merge(this.searchModel, { INIT_START_DATE: dateTime[0], INIT_END_DATE: dateTime[1] })
      } else {
        this._merge(this.searchModel, { INIT_START_DATE: null, INIT_END_DATE: null })
      }
      if (searchModel?.FIN_DATE) {
        const dateTime = searchModel.FIN_DATE
        this._merge(this.searchModel, { FIN_START_DATE: dateTime[0], FIN_END_DATE: dateTime[1] })
      } else {
        this._merge(this.searchModel, { FIN_START_DATE: null, FIN_END_DATE: null })
      }
      this._merge(param, this.searchModel)
      try {
        this.loading = true
        const res = await apiAlarmCurAndHistList(param)
        this.alarmCurHistList = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
  },
}
</script>
l
