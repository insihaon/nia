<template>
  <div :class="{ [name]: true, 'h-100': true }">
    <el-tabs v-model="tapCurrent" class="h-full">
      <el-tab-pane label="TICKET" name="ticket" class="h-full">
        <CompInquiryPannel
          ref="ticketSearch"
          :ag-grid="sopAgGrid"
          :is-button-slot="false"
          title="SOP 리스트"
          :is-excel="true"
          :is-excel-save-server="true"
          :items="searchSopItems"
          :is-grid-loading="loading"
          :pagination-info="sopPaginationInfo"
          class="w-100 h-100"
          @handleClickSearch="() => onClickSearch('TICKET')"
          @onChangePage="(curPage) => onChangePage(curPage, 'TICKET')"
          @rowClicked="(row) => onClickRow(row, 'TICKET')"
          @searchClear="() => onLoadSopHistList()"
          @savedExcel="onSaveExcel"
        />
      </el-tab-pane>
      <el-tab-pane label="SYSLOG" name="syslog" class="h-full">
        <CompInquiryPannel
          ref="syslogSearch"
          :ag-grid="syslogAgGrid"
          :is-button-slot="false"
          :search-model.sync="syslogSearchModel"
          :is-grid-loading="syslogLoading"
          :items="searchSyslogItems"
          :pagination-info="syslogPaginationInfo"
          class="w-100 h-100"
          @handleClickSearch="() => onClickSearch('SYSLOG')"
          @onChangePage="(curPage) => onChangePage(curPage, 'SYSLOG')"
          @rowClicked="(row) => onClickRow(row, 'SYSLOG')"
          @searchClear="() => onLoadSyslogHistList()"
        />
      </el-tab-pane>
    </el-tabs>
    <CompAgGrid
      v-show="false"
      ref="excelGrid"
      v-model="excelGrid"
    />
    <ModalSopDetail ref="ModalSopDetail" @reload="onReload" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectSopHistList, apiSopSyslogHistList, apiEquipmentList, apiInterfaceList } from '@/api/nia'
import { getAlarmType, getTicketStatus, getSopAiAccuracy } from '@/views-nia/js/commonFormat'
import ModalSopDetail from '@/views-nia/modal/ModalSopDetail.vue'

const routeName = 'SopHistory'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, ModalSopDetail, CompAgGrid },
  extends: Base,
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      }
    },
    row: {
      type: Object,
      default() {
        return null
      },
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      syslogLoading: false,
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
        pagerCount: 11,
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
        END_DATE: '',
      },
      equipmentOptionList: [],
      interfaceOptionList: [],
      excelList: []
    }
  },

  computed: {
    excelGrid() {
      const grid = this._cloneDeep(this.sopAgGrid)
      grid.data = this.excelList
      return grid
    },
    sopAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
      const columns = [
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 150, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'ticket_type', name: '티켓유형', width: 130, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, format: getAlarmType },
        { type: '', prop: 'ticket_result', name: '장애내용', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_classify', name: '장애구분', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_type', name: '장애유형', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'etc_content', name: '기타사항', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        {
          type: '',
          prop: 'fault_time',
          name: '발생시간',
          width: 250,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: true,
          formatter: (row) => {
            return row.fault_time ? this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss') : row.fault_time
          },
        },
        {
          type: '',
          prop: 'handling_fin_time',
          name: '마감시간',
          width: 250,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: true,
          formatter: (row) => {
            return row.handling_fin_time ? this.formatterTimeStamp(row.handling_fin_time, 'YYYY/MM/DD-HH:mm:ss') : row.handling_fin_time
          },
        },
        { type: '', prop: 'handling_fin_user', name: '마감자', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'root_cause_sysnamea', name: '노드A', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'root_cause_sysnamez', name: '노드Z', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'ip_addra', name: '마스터 IP', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'root_cause_porta', name: '장비I/F', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'ai_accuracy', name: 'AI 결과 피드백', width: 100, formatter: getSopAiAccuracy }
      ]
      return { options, columns, data: this.sopHistList, getRightClickMenuItems: () => { return [] } }
    },
    syslogAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
      const columns = [
        { type: '', prop: 'alarmno', name: '알람번호', width: 150, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'node_nm', name: '장비명', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'alarmloc', name: '인터페이스', width: 160, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_classify', name: '장애구분', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_type', name: '장애유형', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'fault_detail_content', name: '조치내용', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'etc_content', name: '기타조치내용', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'etc', name: 'syslog 원본메시지', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        {
          type: '',
          prop: 'alarm_occur_time',
          name: '발생시간',
          width: 250,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: true,
          formatter: (row) => {
            return row.alarm_occur_time ? this.formatterTimeStamp(row.alarm_occur_time, 'YYYY/MM/DD-HH:mm:ss') : ''
          },
        },
        {
          type: '',
          prop: 'handling_fin_time',
          name: '마감시간',
          width: 250,
          suppressMenu: true,
          alignItems: 'center',
          sortable: true,
          filterable: true,
          formatter: (row) => {
            return row.handling_fin_time ? this.formatterTimeStamp(row.handling_fin_time, 'YYYY/MM/DD-HH:mm:ss') : row.handling_fin_time
          },
        },
        { type: '', prop: 'handling_fin_user', name: '마감자', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        { type: '', prop: 'status', name: '처리상태', width: 250, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true, format: getTicketStatus },
      ]
      return { options, columns, data: this.syslogHistList }
    },
    searchSyslogItems() {
      const searchItems = [
        { label: '장비명', type: 'select', size: 4, model: 'NODE_NM', setting: { allOption: { toggle: true } }, options: this.equipmentOptionList },
        { label: 'I/F', type: 'select', size: 4, model: 'ALARMLOC', setting: { allOption: { toggle: true } }, options: this.interfaceOptionList },
        {
          label: '상태',
          type: 'select',
          size: 6,
          model: 'STATUS',
          setting: { allOption: { toggle: true } },
          options: [
            { label: '전체', value: 'ALL' },
            { label: '자동', value: 'AUTO_FIN' },
            { label: '수동', value: 'FIN' },
          ],
        },
        { label: 'DATE', type: 'date', size: 6, model: 'DATE', placeholder: '' },
      ]
      return searchItems
    },
  },
  created() {
    this.selectedRow = this.wdata?.params
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
        this.equipmentOptionList = equipRes?.result.map((v) => {
          return { label: v.value, value: v.value }
        })
        const ifRes = await apiInterfaceList()
        this.interfaceOptionList = ifRes?.result.map((v) => {
          return { label: v.value, value: v.value }
        })
      } catch (error) {
        this.error(error)
      }
    },
    getSopHistParam() {
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
      return param
    },
    async onLoadSopHistList() {
      const THIS = this
      const param = this.getSopHistParam()
      try {
        this.loading = true
        const res = await apiSelectSopHistList(param)
        this.sopHistList = res?.result
        this.sopPaginationInfo.totalCount = res.total
        this.sopPaginationInfo.totalPages = Math.ceil(this.sopPaginationInfo.totalCount / this.sopPaginationInfo.pageSize) // 전체 페이지 수 계산
        this.loading = false
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadSyslogHistList() {
      const { pageSize: limit, currentPage: page } = this.syslogPaginationInfo
      const param = { limit, page, ISHISTORY: true }
      const searchModel = this.$refs?.syslogSearch?.searchModel ?? {}
      this._merge(param, this.syslogSearchModel)
      if (searchModel?.DATE) {
        const dateTime = this.$refs.syslogSearch.searchModel.DATE
        this._merge(param, { START_DATE: dateTime[0], END_DATE: dateTime[1] })
      }
      searchModel.STATUS === 'ALL' && delete param.STATUS
      this.syslogLoading = true
      try {
        const res = await apiSopSyslogHistList(param)
        this.syslogHistList = res?.result
        this.syslogPaginationInfo.totalCount = res.total
        this.syslogPaginationInfo.totalPages = Math.ceil(this.syslogPaginationInfo.totalCount / this.syslogPaginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.syslogLoading = false
      }
    },
    onReload(type) {
      if (type === 'TICKET') {
        this.onLoadSopHistList()
      } else {
        this.onLoadSyslogHistList()
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
    onClickRow(row, type) {
      this.$refs.ModalSopDetail.open({ row: row.data, type })
    },
    async onSaveExcel() {
      let limit = this.sopPaginationInfo.totalCount
      if (limit > 50000) {
        await this.$confirm('데이터가 50,000건을 초과하였습니다. <br/> 50,000건 까지만 출력합니다.', '메시지 창', {
          confirmButtonText: '확인',
          dangerouslyUseHTMLString: true,
          type: 'info'
        }).then(async() => {
          limit = 50000
          await this.exportExcel(limit)
        }).catch((action) => {
        })
      } else {
        await this.exportExcel(limit)
      }
    },
    async exportExcel(limit) {
      const param = Object.assign(this.getSopHistParam(), { limit, page: 1 })
      const target = ({ vue: this })
      try {
        this.openLoading(target, { text: '다운로드 중입니다.' })
        const res = await apiSelectSopHistList(param)
        this.excelList = res?.result
        setTimeout(() => {
          this.$refs.excelGrid.exportExcel(`SOP 리스트_${this.toStringTime(new Date(), 'YYMMDD')}`)
          this.closeLoading(target)
        }, 10)
      } catch (error) {
        this.error(error)
      }
    },
    async autoTest() {
      const { assert, wait, onLoadSopHistList, onLoadSyslogHistList } = this
        if (this.tapCurrent === 'ticket') {
          await onLoadSopHistList()
          await wait(1000)
          assert(this.sopHistList.length > 0)
        } else {
          await onLoadSyslogHistList()
          await wait(1000)
          assert(this.syslogHistList.length > 0)
        }
      }
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-loading-spinner {
  flex-direction: column;
  align-items: center;
}
</style>
