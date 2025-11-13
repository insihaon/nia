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
          :search-model.sync="sopSearchModel"
          :is-excel-save-server="true"
          :items="searchSopItems"
          :is-grid-loading="loading"
          :pagination-info="sopPaginationInfo"
          class="w-100 h-100"
          @handleClickSearch="() => onClickSearch('TICKET')"
          @onChangePage="(curPage) => onChangePage(curPage, 'TICKET')"
          @rowClicked="(row) => onClickRow(row, 'TICKET')"
          @searchClear="() => onLoadSopHistList()"
          @selectChange="selectChange"
          @savedExcel="onSaveExcel"
        />
      </el-tab-pane>
      <el-tab-pane label="SYSLOG" name="syslog" class="h-full">
        <CompInquiryPannel
          ref="syslogSearch"
          :ag-grid="syslogAgGrid"
          :is-button-slot="false"
          :is-excel="false"
          :search-model.sync="syslogSearchModel"
          :is-excel-save-server="false"
          :items="searchSyslogItems"
          :is-grid-loading="syslogLoading"
          :pagination-info="syslogPaginationInfo"
          class="w-100 h-100"
          @handleClickSearch="() => onClickSearch('SYSLOG')"
          @onChangePage="(curPage) => onChangePage(curPage, 'SYSLOG')"
          @rowClicked="(row) => onClickRow(row, 'SYSLOG')"
          @searchClear="() => onLoadSyslogHistList()"
        />
      </el-tab-pane>
    </el-tabs>
    <CompAgGrid v-show="false" ref="excelGrid" v-model="excelGrid" />
    <ModalSopDetail ref="ModalSopDetail" @reload="onReload" />
  </div>
</template>
<script>
import { Base } from '@/min/Base'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectSopHistList, apiSopSyslogHistList, apiSyslogEquipmentList, apiSyslogInterfaceList, apiSelectNodeList, apiSelectLinkList } from '@/api/nia'
import { getAlarmType, getTicketStatus, getSopAiAccuracy } from '@/views-nia/js/commonFormat'
import ModalSopDetail from '@/views-nia/modal/ModalSopDetail.vue'
import { mapState } from 'vuex'
import constants from '@/min/constants'
import { getChatbotTicketData, getWindowActionList, getInvisibleSpanParameter, getNiaRouterPathByName, showNumberText } from '@/views-nia/js/commonNiaFunction'
import niaObserverMixin from '@/mixin/niaObserverMixin'

const routeName = 'sopHistory' /* constants.nia.chatbotKeyMap.sopHistory.parameterKey */

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, ModalSopDetail, CompAgGrid },
  extends: Base,
  mixins: [niaObserverMixin],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      },
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

      sopSearchModel: {
        TICKET_ID: '',
        DATE: [],
        NODE_NM: '',
        ALARMLOC: '',
      },
      syslogSearchModel: {
        ALARM_NO: '',
        NODE_NM: '',
        ALARMLOC: '',
        STATUS: '',
        DATE: [],
      },
      equipmentOptionList: [],
      interfaceOptionList: [],
      equipmentSyslogOptionList: [],
      interfaceSyslogOptionList: [],
      excelList: [],
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
        { type: '', prop: 'ai_accuracy', name: 'AI 결과 피드백', width: 100, formatter: getSopAiAccuracy },
      ]
      return {
        options,
        columns,
        data: this.sopHistList,
        getRightClickMenuItems: () => {
          return []
        },
      }
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
    searchSopItems() {
      return [
        { label: '티켓번호', type: 'input', size: 5, model: 'TICKET_ID' },
        { label: 'DATE', type: 'date', size: 8, model: 'DATE' },
        { label: '장비명', type: 'select', size: 6, model: 'NODE_NM', setting: { allOption: { toggle: true } }, options: this.equipmentOptionList },
        { label: 'I/F', type: 'select', size: 5, model: 'ALARMLOC', setting: { allOption: { toggle: true } }, options: this.interfaceOptionList },
      ]
    },
    searchSyslogItems() {
      return [
        { label: '알람번호', type: 'input', size: 8, model: 'ALARM_NO' },
        { label: '장비명', type: 'select', size: 8, model: 'NODE_NM', setting: { allOption: { toggle: true } }, options: this.equipmentSyslogOptionList },
        { label: 'I/F', type: 'select', size: 8, model: 'ALARMLOC', setting: { allOption: { toggle: true } }, options: this.interfaceSyslogOptionList },
        {
          label: '상태',
          type: 'select',
          size: 8,
          model: 'STATUS',
          setting: { allOption: { toggle: true } },
          options: [
            { label: '전체', value: 'ALL' },
            { label: '자동', value: 'AUTO_FIN' },
            { label: '수동', value: 'FIN' },
          ],
        },
        { label: 'DATE', type: 'date', size: 8, model: 'DATE', placeholder: '' },
      ]
    },
    ...mapState({
      sopHistoryEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.sopHistory.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },
  },
  watch: {
    sopHistoryEventText(nVal, oVal) {
      if (this.isModal) {
        switch (nVal) {
          case constants.nia.chatbotCommand.saveExcel.action:
            this.onSaveExcel()
            break
          case constants.nia.chatbotCommand.tabSwitching.action:
            this.tabSwitching()
            break
        }

        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.sopHistory.parameterKey })
      }
    },
  },
  created() {
    this.selectedRow = this.wdata.params
  },
  async mounted() {
    await this.setTicketDataForChatbotTicketData()
    this.popupShowCommand()
  },
  methods: {
    async setTicketDataForChatbotTicketData(isSwitchingTicket) {
      if (isSwitchingTicket) this.wdata.params.isChatbotGenerated = isSwitchingTicket
      const chatbotData = await getChatbotTicketData(this.wdata)
      if (chatbotData) {
        this.selectedRow = chatbotData
        this.$emit('update:wdataParams', chatbotData)

        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: constants.nia.chatbotIcon.success + constants.nia.chatbotComment.parameterChange,
        })
      }

      this.tapCurrent = this.selectedRow?.ticket_type === 'SYSLOG' ? 'syslog' : 'ticket'
      this.sopSearchModel.NODE_NM = this.selectedRow.node_nm
      this.syslogSearchModel = { NODE_NM: this.selectedRow.node_nm }

      this.setDefaultTime()

      this.$nextTick(() => {
        this.setSelectedOptions()
        this.setSelectedSyslogOptions()
        this.onLoadSopHistList()
        this.onLoadSyslogHistList()
      })
    },

    setDefaultTime() {
      const end = new Date()
      // 1. end 날짜를 현재 날짜의 23:59:59.999로 설정합니다.
      // 이렇게 하면 오늘 하루 전체를 포함하게 됩니다.
      end.setHours(23, 59, 59, 999)

      // 2. start 날짜를 end 날짜(오늘) 기준 7일 전으로 설정합니다.
      const start = new Date(end.getTime() - 7 * 24 * 60 * 60 * 1000)

      // 3. start 날짜를 7일 전 날짜의 00:00:00.000로 강제 설정합니다.
      start.setHours(0, 0, 0, 0)

      // 7일 전 00:00:00 KST
      const startTimeKST = `${start.getFullYear()}-${String(start.getMonth() + 1).padStart(2, '0')}-${String(start.getDate()).padStart(2, '0')} 00:00:00`
      // 오늘 23:59:59 KST
      const endTimeKST = `${end.getFullYear()}-${String(end.getMonth() + 1).padStart(2, '0')}-${String(end.getDate()).padStart(2, '0')} 23:59:59.999`

      // 4. 로직 적용
      if (!this.sopSearchModel?.DATE || this.sopSearchModel.DATE.length === 0) {
        // ISOString은 UTC 기준 시간으로 변환되므로,
        // 원하시는 시간대(로컬 시간)로 정확히 표시하려면 서버/프론트엔드에서 처리 방식에 따라 다를 수 있습니다.
        // 여기서는 강제 셋팅된 로컬 시간을 ISOString으로 변환합니다.
        this.sopSearchModel.DATE = [startTimeKST, endTimeKST]
      }

      if (!this.syslogSearchModel?.DATE || this.syslogSearchModel.DATE.length === 0) {
        this.syslogSearchModel.DATE = [startTimeKST, endTimeKST]
      }
    },

    selectChange(map) {
      if (map.model === 'NODE_NM') {
        this.chainSetInterfaceOptionList()
      }
    },

    async chainSetInterfaceOptionList() {
      const ifRes = await apiSelectLinkList({ src_node_id: this.sopSearchModel.NODE_NM === 'ALL' ? '' : this.sopSearchModel.NODE_NM })
      this.interfaceOptionList = ifRes?.result.map((v) => {
        return { label: v.src_if_id, value: v.src_if_id }
      })
      this.interfaceOptionList.unshift({ label: '전체', value: 'ALL' })
      this.sopSearchModel.ALARMLOC = 'ALL'
    },

    async popupShowCommand() {
      if (this.isModal && !this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content:
            `<div class="chatbot-command-header">SOP이력조회 화면</div>
            장애 장비에 대한 과거 마감처리 이력을 표시합니다.<br>
            ${constants.nia.chatbotIcon.Information}탭 종류, 장비명, I/F는 현재 장애 정보를 기준으로 자동셋팅했습니다.
            ${constants.nia.chatbotIcon.Information} 조치가 필요하시면 <b>조치 화면</b>으로 이동해 주세요.
            ${constants.nia.chatbotIcon.Information} 장애에 대해 더 상세한 정보를 알고 싶으시면, <b>티켓 상세 확인</b>도 도와드릴 수 있습니다.<br>
            ` +
            (await getWindowActionList(
              constants.nia.chatbotKeyMap.sopHistory.dialogNm,
              constants.nia.chatbotKeyMap.sopHistory.popupName,
              showNumberText(3, `${constants.nia.chatbotKeyMap.configTest.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), constants.nia.chatbotKeyMap.configTest.dialogNm, '')}`) +
                showNumberText(4, `${constants.nia.chatbotCommand.focusModeCheckAlarm.label}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', constants.nia.chatbotCommand.focusModeCheckAlarm.action)}`)
            )),
        })
      }
    },
    tabSwitching() {
      if (this.tapCurrent === 'ticket') {
        this.tapCurrent = 'syslog'
      } else {
        this.tapCurrent = 'ticket'
      }

      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: `${constants.nia.chatbotIcon.success} ${constants.nia.chatbotKeyMap.sopHistory.popupName}에서 성공적으로 ${constants.nia.chatbotCommand.tabSwitching.label}을 했습니다.<br> `,
          callBack: this.popupShowCommand,
        })
      }
    },

    async setSelectedOptions() {
      try {
        const equipRes = await apiSelectNodeList()
        this.equipmentOptionList = equipRes?.result.map((v) => {
          return { label: v.node_id, value: v.node_id }
        })
        this.equipmentOptionList.unshift({ label: '전체', value: 'ALL' })

        const ifRes = await apiSelectLinkList()
        this.interfaceOptionList = ifRes?.result.map((v) => {
          return { label: v.src_if_id, value: v.src_if_id }
        })
        this.interfaceOptionList.unshift({ label: '전체', value: 'ALL' })
      } catch (error) {
        this.error(error)
      }
    },
    async setSelectedSyslogOptions() {
      try {
        const equipRes = await apiSyslogEquipmentList()
        this.equipmentSyslogOptionList = equipRes?.result.map((v) => {
          return { label: v.value, value: v.value }
        })
        const ifRes = await apiSyslogInterfaceList()
        this.interfaceSyslogOptionList = ifRes?.result.map((v) => {
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
      if (searchModel?.NODE_NM) {
        this._merge(param, { NODE_NM: this.$refs.ticketSearch.searchModel.NODE_NM })
      }
      if (searchModel?.ALARMLOC) {
        this._merge(param, { ALARMLOC: this.$refs.ticketSearch.searchModel.ALARMLOC })
      }
      if (searchModel?.DATE) {
        const dateTime = this.$refs.ticketSearch.searchModel.DATE
        this._merge(param, { START_DATE: dateTime[0], END_DATE: dateTime[1] })
      }
      if (this.row !== null) {
        this._merge(param, { TICKET_TYPE: this.row.ticket_type })
      }
      if (param.NODE_NM === 'ALL') {
        param.NODE_NM = null
      }

      if (param.ALARMLOC === 'ALL') {
        param.ALARMLOC = null
      }

      return param
    },
    async onLoadSopHistList() {
      try {
        this.loading = true
        const res = await apiSelectSopHistList(this.getSopHistParam())

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
          type: 'info',
        })
          .then(async () => {
            limit = 50000
            await this.exportExcel(limit)
          })
          .catch((action) => {})
      } else {
        await this.exportExcel(limit)
      }
    },
    async exportExcel(limit) {
      const param = Object.assign(this.getSopHistParam(), { limit, page: 1 })
      const target = { vue: this }
      try {
        this.openLoading(target, { text: '다운로드 중입니다.' })
        const res = await apiSelectSopHistList(param)
        this.excelList = res?.result
        setTimeout(() => {
          this.$refs.excelGrid.exportExcel(`SOP 리스트_${this.toStringTime(new Date(), 'YYMMDD')}`)
          if (!this.isFocusModeButNotFocus) {
            this.$store.dispatch('chatbot/botPushAnswerMessage', {
              content: `${constants.nia.chatbotIcon.success} ${constants.nia.chatbotKeyMap.sopHistory.popupName}에서 성공적으로 ${constants.nia.chatbotCommand.saveExcel.label}을 했습니다.`,
              callBack: this.popupShowCommand,
            })
          }
        }, 10)
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
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
    },
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-loading-spinner {
  flex-direction: column;
  align-items: center;
}
</style>
