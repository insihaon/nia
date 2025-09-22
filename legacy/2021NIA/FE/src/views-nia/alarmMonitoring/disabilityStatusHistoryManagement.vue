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
      @handleClickSearch="() => onLoadAlarmCurHistList()"
      @sortChanged="onChangeSort"
      @selectChange="selectChange"
      @searchClear="() => onLoadAlarmCurHistList()"
      @onChangePage="onChangePage"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiAlarmCurAndHistList, apiSelectNodeList, apiSelectLinkList } from '@/api/nia'
import { mapState } from 'vuex'
import constants from '@/min/constants'
import { getAlarmFocusTicketData, getWindowActionList } from '@/views-nia/js/commonNiaFunction'

import niaObserverMixin from '@/mixin/niaObserverMixin'

const routeName = constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.parameterKey

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
  extends: Base,
  mixins: [niaObserverMixin],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      },
    },
  },

  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      alarmCurHistList: [],
      equipmentOptionList: [],
      interfaceOptionList: [],
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11,
      },
      searchModel: {
        TICKET_ID: '',
        TICKET_TYPE: '',
        NODE_NM: '',
        ALARMLOC: '',
        INIT_START_DATE: '',
        INIT_END_DATE: '',
        CLOSE_START_DATE: '',
        CLOSE_END_DATE: '',
      },
    }
  },

  computed: {
    alarmAgGrid() {
      const options = { name: this.name, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true }
      const columns = [
        { type: '', prop: 'ticket_id', name: '티켓번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'root_cause_sysnamea', name: '장비명A', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'root_cause_porta', name: '포트명A', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'root_cause_sysnamez', name: '장비명Z', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'root_cause_portz', name: '포트명Z', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'parent_ticket_id', name: '병합티켓번호', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
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
            return row.fault_time ? this.formatterTimeStamp(row.fault_time, 'YYYY/MM/DD-HH:mm:ss') : ''
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
            return row.handling_fin_time ? this.formatterTimeStamp(row.handling_fin_time, 'YYYY/MM/DD-HH:mm:ss') : ''
          },
        },
        { type: '', prop: 'ticket_type', name: '장애 구분', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
        { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 내용', width: 100, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
      ]
      return {
        options,
        columns,
        data: this.alarmCurHistList,
        getRightClickMenuItems: () => {
          return []
        },
      }
    },
    searchItems() {
      return [
        { label: '티켓번호', type: 'input', size: 8, model: 'TICKET_ID', placeholder: 'TICKET_ID를 검색하세요' },
        {
          label: '티켓유형',
          type: 'select',
          size: 8,
          model: 'TICKET_TYPE',
          setting: { allOption: { toggle: true }, placeholder: 'TICKET 유형을 선택하세요' },
          options: [
            { label: '장비장애(RT)', value: 'RT' },
            { label: '유해트래픽(NTT)', value: 'NTT' },
            { label: '이상트래픽(ATT2)', value: 'ATT2' },
          ],
        },
        { label: '장비명', type: 'select', size: 8, model: 'NODE_NM', setting: { allOption: { toggle: true } }, options: this.equipmentOptionList },
        { label: 'SRC I/F', type: 'select', size: 8, model: 'ALARMLOC', setting: { allOption: { toggle: true } }, options: this.interfaceOptionList },
        { label: '발생시간', type: 'date', size: 8, model: 'INIT_DATE' },
        { label: '마감시간', type: 'date', size: 8, model: 'FIN_DATE' },
      ]
    },
    ...mapState({
      disabilityStatusHistoryManagementEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },

    chatbotCommand() {
      return constants.nia.chatbotCommand
    },
    chatbotKeyMap() {
      return constants.nia.chatbotKeyMap
    },
  },
  watch: {
    disabilityStatusHistoryManagementEventText(nVal, oVal) {
      if (this.isModal) {
        if (nVal === constants.nia.chatbotCommand.search.action) {
          this.onLoadAlarmCurHistList()
        }
        if (nVal === constants.nia.chatbotCommand.refresh.action) {
          this.$refs.inquiry.handleSearchClear()
        }

        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.parameterKey })
      }
    },
  },
  async mounted() {
    const ticketData = await getAlarmFocusTicketData(this.wdata)
    if (ticketData) {
      this.selectedRow = ticketData
      this.$emit('update:wdataParams', ticketData)
    }

    this.$nextTick(() => {
      this.setSelectedOptions()
      this.onLoadAlarmCurHistList()
      this.popupShowCommand()
    })
  },
  methods: {
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
      this.searchModel.ALARMLOC = 'ALL'
    },

    async setSelectedOptions() {
      try {
        const equipRes = await apiSelectNodeList()
        this.equipmentOptionList = equipRes?.result.map((v) => {
          return { label: v.node_id, value: v.node_id }
        })
        this.equipmentOptionList.unshift({ label: '전체', value: 'ALL' })
        this.searchModel.NODE_NM = this.searchModel.NODE_NM || 'ALL'

        const ifRes = await apiSelectLinkList()
        this.interfaceOptionList = ifRes?.result.map((v) => {
          return { label: v.src_if_id, value: v.src_if_id }
        })
        this.interfaceOptionList.unshift({ label: '전체', value: 'ALL' })
        this.searchModel.ALARMLOC = this.searchModel.ALARMLOC || 'ALL'
      } catch (error) {
        this.error(error)
      }
    },
    async popupShowCommand() {
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: await getWindowActionList(constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.dialogNm, constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.popupName),
        })
      }
    },
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
      if (param.NODE_NM === 'ALL') {
        param.NODE_NM = ''
      }

      if (param.ALARMLOC === 'ALL') {
        param.ALARMLOC = ''
      }
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
<style lang="scss" scoped>
.DisabilityStatusHistoryManagement {
  height: 100%;
}
</style>
