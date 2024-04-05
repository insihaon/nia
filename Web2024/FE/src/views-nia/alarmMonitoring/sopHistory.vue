<template>
  <div :class="{ [name]: true }">
    <el-tabs v-model="tapCurrent" class="h-full">
      <el-tab-pane label="TICKET" name="ticket" class="h-full">
        <CompInquiryPannel
          ref="ticketSearch"
          :ag-grid="sopAgGrid"
          :is-button-slot="false"
          :items="searchSopItems"
          :pagination-info="sopPaginationInfo"
          class="w-100 h-100"
          @handleClickSearch="()=> onClickSearch('TICKET')"
          @onChangePage="(curPage) => onChangePage(curPage, 'TICKET')"
          @selectedRow="(row)=> onClickRow(row,'TICKET')"
          @searchClear="()=> onLoadSopHistList()"
        />
      </el-tab-pane>
      <el-tab-pane label="SYSLOG" name="syslog" class="h-full">
        <CompInquiryPannel
          ref="syslogSearch"
          :ag-grid="syslogAgGrid"
          :is-button-slot="false"
          :search-model.sync="syslogSearchModel"
          :items="searchSyslogItems"
          :pagination-info="syslogPaginationInfo"
          class="w-100 h-100"
          @handleClickSearch="()=> onClickSearch('SYSLOG')"
          @onChangePage="(curPage) => onChangePage(curPage, 'SYSLOG')"
          @selectedRow="(row)=> onClickRow(row, 'SYSLOG')"
          @searchClear="()=> onLoadSyslogHistList()"
        />
      </el-tab-pane>
    </el-tabs>

    <ModalSopDetail ref="ModalSopDetail" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectSopHistList, apiSopSyslogHistList, apiEquipmentList, apiInterfaceList } from '@/api/nia'
import { getAlarmType, getTicketStatus } from '@/views-nia/js/commonFormat'
import ModalSopDetail from '@/views-nia/modal/ModalSopDetail.vue'

const routeName = 'SopHistory'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, ModalSopDetail },
  extends: Base,
  props: {
    row: {
      type: Object,
      default() { return null }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tapCurrent: 'ticket',
      sopHistList: [],
      syslogHistList: [],
      sopPaginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      syslogPaginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectedRow: [],
      searchSopItems: [
          { label: '티켓번호', type: 'input', size: 8, model: 'TICKET_ID' },
          { label: 'DATE', type: 'date', size: 4, model: 'DATE' },
      ],
      syslogSearchModel: {
        NODE_NM: '',
        ALARMLOC: '',
        STATUS: '',
        START_DATE: '',
        END_DATE: ''
      },
      equipmentOptionList: [],
      interfaceOptionList: [],
    }
  },

  computed: {
    sopAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
        const columns = [
          { type: '', prop: 'ticket_id', name: '티켓번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'ticket_type', name: '티켓유형', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, format: getAlarmType },
          { type: '', prop: 'ticket_result', name: '장애내용', width: 160, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_classify', name: '장애구분', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_type', name: '장애유형', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'etc_content', name: '기타사항', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'fault_time', name: '발생시간', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss') } },
          { type: '', prop: 'handling_fin_time', name: '마감시간', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return this.formatterTimeStamp(row.handling_fin_time, 'YYYY/MM/DD-HH:mm:ss') } },
          { type: '', prop: 'handling_fin_user', name: '마감자', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'root_cause_sysnamea', name: '노드A', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'root_cause_sysnamez', name: '노드Z', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'ip_addra', name: '마스터 IP', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'root_cause_porta', name: '장비I/F', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        ]
        return { options, columns, data: this.sopHistList }
    },
    syslogAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
        const columns = [
          { type: '', prop: 'alarmno', name: '알람번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'node_nm', name: '장비명', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'alarmloc', name: '인터페이스', width: 160, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_classify', name: '장애구분', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_type', name: '장애유형', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'etc_content', name: '기타조치내용', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'etc', name: 'syslog 원본메시지', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'alarm_occur_time', name: '발생시간', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss') } },
          { type: '', prop: 'handling_fin_time', name: '마감시간', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, formatter: (row) => { return this.formatterTimeStamp(row.handling_fin_time, 'YYYY/MM/DD-HH:mm:ss') } },
          { type: '', prop: 'handling_fin_user', name: '마감자', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
          { type: '', prop: 'status', name: '처리상태', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, format: getTicketStatus }

        ]
        return { options, columns, data: this.syslogHistList }
    },
    searchSyslogItems() {
      const searchItems = [
        { label: '장비명', type: 'select', size: 4, model: 'NODE_NM', setting: { allOption: { toggle: true } },
          options: this.equipmentOptionList },
        { label: 'I/F', type: 'select', size: 4, model: 'ALARMLOC', setting: { allOption: { toggle: true } },
          options: this.interfaceOptionList },
        { label: '상태', type: 'select', size: 6, model: 'STATUS', setting: { allOption: { toggle: true } },
          options: [
            { label: '', value: '전체' },
            { label: '자동', value: '자동' },
            { label: '수동', value: '수동' }
          ] },
        { label: 'DATE', type: 'date', size: 6, model: 'DATE', placeholder: '' },
      ]
      return searchItems
    }
  },
  mounted() {
    this.setSelectedOptions()
    this.onLoadSopHistList()
    this.onLoadSyslogHistList()
  },
  methods: {
    async setSelectedOptions() {
      try {
        const equipRes = await apiEquipmentList()
        this.equipmentOptionList = equipRes?.result.map(v => { return { label: v.value, value: v.value } })
        const ifRes = await apiInterfaceList()
        this.interfaceOptionList = ifRes?.result.map(v => { return { label: v.value, value: v.value } })
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadSopHistList() {
      const { pageSize: limit, currentPage: page } = this.sopPaginationInfo
      const param = { limit, page }
      const searchModel = this.$refs?.ticketSearch?.searchModel ?? {}
      if (searchModel?.TICKET_ID) {
        this._merge(param, { TICKET_ID: this.$refs.ticketSearch.searchModel.TICKET_ID })
      }
      if (searchModel?.DATE) {
        const dateTime = this.$refs.ticketSearch.searchModel.DATE
        this._merge(param, { START_DATE: dateTime[0], END_DATE: dateTime[1] })
      }
      if (this.row !== null) {
        this._merge(param, { TICKET_TYPE: this.row.ticket_type })
      }
      try {
        const res = await apiSelectSopHistList(param)
        this.sopHistList = res?.result
        this.sopPaginationInfo.totalCount = res.total
        this.sopPaginationInfo.totalPages = Math.ceil(this.sopPaginationInfo.totalCount / this.sopPaginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      }
    },
    async onLoadSyslogHistList() {
      const { pageSize: limit, currentPage: page } = this.syslogPaginationInfo
      const param = { limit, page, ISHISTORY: true }
      const searchModel = this.$refs?.syslogSearch?.searchModel ?? {}
      if (searchModel?.DATE) {
        const dateTime = this.$refs.syslogSearch.searchModel.DATE
        this._merge(param, { START_DATE: dateTime[0], END_DATE: dateTime[1] })
      }
      this._merge(param, this.syslogSearchModel)
      try {
        const res = await apiSopSyslogHistList(param)
        this.syslogHistList = res?.result
        this.syslogPaginationInfo.totalCount = res.total
        this.syslogPaginationInfo.totalPages = Math.ceil(this.syslogPaginationInfo.totalCount / this.syslogPaginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      }
    },
    onChangePage(curPage, type) {
      if (type === 'TICKET') {
        this.sopPaginationInfo.currentPage = curPage
        this.onLoadSopHistList()
      } else {
        this.syslogPaginationInfo.currentPage = curPage
        this.onLoadSyslogHistList()
      }
    },
    onClickSearch(searchType) {
      if (searchType === 'TICKET') {
        this.onLoadSopHistList()
      } else {
        this.onLoadSyslogHistList()
      }
    },
    onClickRow(rows, type) {
      this.$refs.ModalSopDetail.open({ row: rows[0], type })
    },
  },
}
</script>
<style lang="scss" scoped>

</style>
