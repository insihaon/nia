<template>
  <div :class="{ [name]: true }">
    <LeftBar class="h-full">
      <template slot="leftbar-container">
        <div class="h-20 text-center mt-1" style="z-index: 1;">
          <span class="font-bold text-lg whitespace-nowrap">AI관제 시스템 처리량</span>
          <div class="d-flex p-2 justify-center items-center">
            <span class="font-semibold whitespace-nowrap pr-2">검색</span>
            <el-radio-group v-model="systemChartCondition.dayType" size="mini" class="d-flex">
              <el-radio-button label="DAY">일별</el-radio-button>
              <el-radio-button label="MONTH">월별</el-radio-button>
            </el-radio-group>
            <el-date-picker
              v-model="systemChartCondition.date"
              style="width: 150px;"
              size="mini"
              :type="systemChartCondition.dayType ==='DAY' ? 'date':'month'"
            />
            <el-button icon="el-icon-search" size="mini" style="padding: 7px 7px;" @click="onLoadDashboardStatistics()" />
          </div>
        </div>
        <hr />
        <div style="height: calc(70% - 5rem);">
          <CompChart :options="ticketOptions" class="relative h-64" style="top: -2rem;" />
          <CompChart :options="collectOptions" class="relative h-72" style="top: -7rem;" />
          <CompChart :options="servingOptions" class="relative h-64" style="top: -12rem;" />
        </div>
        <hr />
        <div class="h-20 text-center">
          <span class="font-bold text-lg whitespace-nowrap p-2">자가 처리 현황</span>
          <div class="d-flex p-2 justify-center items-center">
            <span class="font-semibold whitespace-nowrap pr-2">검색</span>
            <el-radio-group v-model="selfChartCondition.statisticsType" size="mini" class="d-flex">
              <el-radio-button label="hour">시간별</el-radio-button>
              <el-radio-button label="day">일별</el-radio-button>
              <el-radio-button label="month">월별</el-radio-button>
            </el-radio-group>
            <el-date-picker
              v-model="selfChartCondition.date"
              style="width: 150px;"
              size="mini"
              :type="getSelfProDateType()"
            />
            <el-button icon="el-icon-search" size="mini" style="padding: 7px 7px;" @click="onLoadSelfProcessStatistics()" />
          </div>
        </div>
        <hr />
        <div style="height: calc(30% - 5rem);">
          <CompChart :options="selfProcessOptions" class="h-full" @click="onClickChart" />
        </div>
      </template>
      <template slot="top-container">
        <filterBar position="TOP">
          <template slot="function-container">
            <div class="filter-container">
              <div class="title">IP-SDN</div>
              <div class="filter-group">
                <template v-for="(filter, keyName) in ipFilterGroup.filters">
                  <div v-if="filter.filterTitle" :key="filter.filterTitle" class="item-title ml-2">
                    {{ filter.filterTitle || '' }}
                  </div>
                  <ul v-if="keyName" :key="keyName">
                    <!-- :class="{'filterBtn': !filterIconList.includes(keyName), 'filterIcon d-flex':filterIconList.includes(keyName)}" -->
                    <li
                      v-for="(item, index) in filter.getArray()"
                      :key="index"
                      class="checkItem d-flex items-center checked ml-1"
                      :style="{'background-color': item.hex, 'color': item.color }"
                      @click="onClickFilterItem('ip', filter.filterName , item.code)"
                    >
                      <i :class="item.selected ? 'el-icon-success': 'el-icon-circle-check'" />
                      <div class="filter-text">{{ item.text + '(' + item.count + ')' }}</div>
                    </li>
                  </ul>
                </template>
              </div>
            </div>
          </template>
        </filterBar>
        <CompAgGrid
          ref="ipAgGrid"
          v-model="ipAgGrid"
          class="w-100 flex-fill"
          @rowClicked="selectedTicket"
        />
        <!-- top-container content -->
      </template>
      <template slot="bottom-container">
        <filterBar position="BOTTOM">
          <template slot="function-container">
            <div class="filter-container">
              <div class="title">전송망</div>
              <div class="filter-group">
                <template v-for="(filter, keyName) in transFilterGroup.filters">
                  <div v-if="filter.filterTitle" :key="filter.filterTitle" class="item-title ml-2">
                    {{ filter.filterTitle || '' }}
                  </div>
                  <ul v-if="keyName" :key="keyName">
                    <!-- :class="{'filterBtn': !filterIconList.includes(keyName), 'filterIcon d-flex':filterIconList.includes(keyName)}" -->
                    <li
                      v-for="(item, index) in filter.getArray()"
                      :key="index"
                      class="checkItem d-flex items-center checked ml-1"
                      :style="{'background-color': item.hex, 'color': item.color }"
                      @click="onClickFilterItem('trans', filter.filterName , item.code)"
                    >
                      <i :class="item.selected ? 'el-icon-success': 'el-icon-circle-check'" />
                      <div class="filter-text">{{ item.text + '(' + item.count + ')' }}</div>
                    </li>
                  </ul>
                </template>
              </div>
            </div>
          </template>
        </filterBar>
        <CompAgGrid
          ref="transmissionAgGrid"
          v-model="transmissionAgGrid"
          class="w-100 flex-fill"
        />
      </template>
    </LeftBar>
    <ModalFIN ref="ModalFIN" />
    <ModalNTF ref="ModalNTF" />
    <ModalSopList ref="ModalSopList" />
    <ModalConfigTest ref="ModalConfigTest" />
    <ModalAiResponse ref="ModalAiResponse" />
    <ModalSelfProcessList ref="ModalSelfProcessList" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import LeftBar from '@/layout/components/gridTemplate/LeftBar'
import filterBar from '@/layout/components/filterBar'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CompChart from '@/components/chart/CompChart.vue'
import BaseFilterGroup from '@/filters/baseFilterGroup'
import CellRenderAibuttons from '@/views-nia/components/cellRenderer/CellRenderAibuttons'
import ModalNTF from '@/views-nia/modal/ModalNTF'
import ModalFIN from '@/views-nia/modal/ModalFIN'
import ModalConfigTest from '@/views-nia/modal/ModalConfigTest'
import ModalSopList from '@/views-nia/modal/ModalSopList'
import ModalAiResponse from '@/views-nia/modal/ModalAiResponse'
import ModalSelfProcessList from '@/views-nia/modal/ModalSelfProcessList'
import { apiIpAlarmList, apiTransmissionAlarmList, apiDashboardStatistics, apiSelfProcessStatistics } from '@/api/nia'
import { getAlarmType } from '@/views-nia/js/commonFormat'
const routeName = 'NiaMain'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CompChart, LeftBar, filterBar, ModalFIN, ModalNTF, ModalConfigTest, ModalSopList, CellRenderAibuttons, ModalAiResponse, ModalSelfProcessList },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipFilterGroup: '',
      transFilterGroup: '',
      ipNetworkList: [],
      transmissionNetworkList: [],
      selectedItem: [],
      systemChartCondition: {
        dayType: 'DAY',
        date: this.moment().subtract(1, 'd').format('YYYY-MM-DD')
      },
      selfChartCondition: {
        statisticsType: 'hour',
        date: this.moment().format('YYYY-MM-DD')
      },
      statistics: {},
      selfStatistics: [],
      chartloading: false

    }
  },
  computed: {
    ipAgGrid() {
      const columns = [
        { type: '', prop: 'alarmno', name: '알람번호', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_id', name: 'TICKET_ID', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmtime', name: '장애 발생시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return this.formatterTimeStamp(row.ticket_generation_time, 'YYYY/MM/DD-HH:mm:ss') } },
        { type: '', prop: 'status', name: '상태', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: this.getStatus, cellStyle: this.getCellStyle, },
        { type: '', prop: '', name: '마감', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '', icon: 'edit-outline', type: 'FIN', action: this.handleOpenEditModal.bind(this) } },
        { type: '', prop: '', name: '시험', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '', icon: 'edit-outline', type: 'CONFIG_TEST', action: this.handleOpenEditModal.bind(this) } },
        { type: '', prop: '', name: 'SOP이력', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: 'SOP', icon: 'circle-check', type: 'SOP', action: this.handleOpenEditModal.bind(this) } },
        { type: '', prop: '', name: '장애대응', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '장애대응', icon: 'circle-check', type: 'ALARM', action: this.handleOpenEditModal.bind(this) } },
        { type: '', prop: '', name: '상황전파', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '조치요청', icon: 'circle-check', type: 'NTF', action: this.handleOpenEditModal.bind(this) } },
        { type: '', prop: 'ticket_type', name: '전표 유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true, formatter: getAlarmType },
        { type: '', prop: 'root_cause_type', name: '장애유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg', name: '장애정보', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg_original', name: '알람 원본메시지', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_code', name: '장애내용', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 원인', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'node_num', name: '장비ID', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'node_nm', name: '장비명', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmloc', name: '인터페이스명', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'total_related_alarm_cnt', name: '근원알람개수', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ip_addr', name: 'ip_addr', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return { options, columns, data: this.ipNetworkList, onDoesExternalFilterPass: (externalFilter, node) => { return this.onDoesExternalFilterPass(externalFilter, node, 'ip') } }
    },
    transmissionAgGrid() {
      const columns = [
        { type: '', prop: 'alarmno', name: '알람 번호', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'sysname', name: '장비명', width: 160, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmtime', name: '알람 발생시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmloc', name: '인터페이스명', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg', name: '알람 메시지', width: 120, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: '', name: '마감', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '', icon: 'edit-outline', type: 'FIN', action: this.handleOpenEditModal.bind(this) } },
        // { type: '', prop: '', name: 'SOP이력', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: 'SOP', icon: 'circle-check', type: 'SOP', action: this.handleOpenEditModal.bind(this) } },
        // { type: '', prop: '', name: '장애대응', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '장애대응', icon: 'circle-check', type: 'alarm', action: this.handleOpenEditModal.bind(this) } },
        { type: '', prop: '', name: '상황전파', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, cellRendererFramework: 'CellRenderAibuttons', cellRendererParams: { name: '조치요청', icon: 'circle-check', type: 'NTF', action: this.handleOpenEditModal.bind(this) } },
        // { type: '', prop: 'ticket_id', name: 'RCA 전표', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        // { type: '', prop: 'ticket_type', name: '전표 유형', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: getAlarmType },
        // { type: '', prop: 'status', name: '상태', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: this.getStatus, cellStyle: this.getCellStyle },
 ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return { options, columns, data: this.transmissionNetworkList, onDoesExternalFilterPass: (externalFilter, node) => { return this.onDoesExternalFilterPass(externalFilter, node, 'trans') } }
    },
    ticketOptions() {
      const keyByTitle = [
        { name: '장애', key: 'ticket_rt_cnt' },
        { name: '광레벨\n저하', key: 'ticket_pf_cnt' },
        { name: '이상\n트래픽', key: 'ticket_att_cnt' },
        { name: '유해\n트래픽', key: 'ticket_ntt_cnt' },
      ]
      return this.getDefaultChartOptions('티켓 발생량', keyByTitle.reverse())
    },
    collectOptions() {
      const keyByTitle = [
      { name: '광레벨\n수집', key: 'trans_perf_cnt' },
      { name: '전송\n경보수집', key: 'trans_alarm_cnt' },
      { name: 'IP시설\n연동', key: 'ip_resource_cnt' },
      { name: 'IP경보\n연동', key: 'ip_alarm_cnt' },
      { name: 'IP트래픽\n연동', key: 'ip_perf_cnt' },
      { name: 'IP SFlow\n연동', key: 'ip_sflow_cnt' }
      ]
      return this.getDefaultChartOptions('데이터 수집량', keyByTitle.reverse())
    },
    servingOptions() {
      const keyByTitle = [
        { name: '시설\n연동', key: 'link_total_resource_cnt' },
        { name: '경보\n연동', key: 'link_total_alarm_cnt' },
        { name: '트래픽\n연동', key: 'link_ip_perf_cnt' },
        { name: '광레벨\n연동', key: 'link_trans_perf_cnt' }
      ]
      return this.getDefaultChartOptions('데이터 제공량(데이터레이크 연계량)', keyByTitle.reverse())
    },
    selfProcessOptions() {
      const selfStatistics = this.selfStatistics
      return {
        legend: {
          data: ['자가최적화 총 발생', '자가최적화 건 수', '자가회복 총 발생', '자가회복 건 수'],
          top: '5%'
        },
        title: {
          // text: '자가 최적화/자가 회복',
          // left: 'center',
          textStyle: {
            fontSize: 13
          }
        },
        dataZoom: [{ type: 'inside' }, { type: 'slider' }],
        tooltip: {},
        xAxis: {
          type: 'category',
          data: selfStatistics.map(v => v.series_time),
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: function (value, index) {
                let result = value
                if (value >= 1000) {
                  result = (value / 1000) + 'K'
                } else {
                  result = value.toString()
                }
                return result
              }
          }
        },
        series: [
          {
            name: '자가최적화 총 발생',
            type: 'bar',
            data: this.selfStatistics.map(v => v.so_totalcount)
          },
          {
            name: '자가최적화 건 수',
            type: 'bar',

            data: this.selfStatistics.map(v => v.so_count)
          },
          {
            name: '자가회복 총 발생',
            type: 'bar',
            data: this.selfStatistics.map(v => v.st_totalcount)
          },
          {
            name: '자가회복 건 수',
            type: 'bar',
            data: this.selfStatistics.map(v => v.st_count)
          },
        ]
      }
    }
  },
  async mounted () {
    await this.onLoadDashboardStatistics()
    await this.onLoadSelfProcessStatistics()

    this.$nextTick(async() => {
      await this.onLoadIpAlarmList()
      await this.onLoadTransmissionAlarmList()

      this.ipFilterGroup = new BaseFilterGroup(this, { onFilterChanged: () => this.onFilterChanged('ip'), isCheckBox: false })
      this.setIPFilterGroup()

      this.transFilterGroup = new BaseFilterGroup(this, { onFilterChanged: () => this.onFilterChanged('trans'), isCheckBox: false })
      this.setTransFilterGroup()
    })
  },
  methods: {
    onClickChart(e) {
      const params = {
        DATE_TYPE: this.selfChartCondition.statisticsType,
        DATE_TIME: e.name,
        SELF_PROCESS_GROUP: e.seriesName.includes('최적화') ? 'SO' : 'ST'
      }
      this.$refs.ModalSelfProcessList.open(params)
    },
    setIPFilterGroup() {
      const listName = 'ipNetworkList'
      const btnOption = { isMultiSelect: true, allItem: true, ifAllthenOtherUncheck: true, listName }

      this.ipFilterGroup.addFilter('ipStatus', '상태', this.CONSTANTS.nia.statusType, btnOption) // 상태
      this.ipFilterGroup.addFilter('ipType', 'TYPE', this.CONSTANTS.nia.ipType, btnOption) // ip망 장애 종류
      this.ipFilterGroup.addFilter('ipAlarmType', '알람 종류', this.CONSTANTS.nia.ipAlarmType, btnOption) // ip망 알람 종류
    },
    setTransFilterGroup() {
      const listName = 'transmissionNetworkList'
      const btnOption = { isMultiSelect: true, allItem: true, ifAllthenOtherUncheck: true, listName }

      // this.transFilterGroup.addFilter('transStatus', '상태', this.CONSTANTS.nia.statusType, btnOption) // 상태
      this.transFilterGroup.addFilter('transType', 'TYPE', this.CONSTANTS.nia.transType, btnOption) // 전송망 장애 종류
    },
    onFilterChanged(type) {
      this.$refs[type === 'ip' ? 'ipAgGrid' : 'transmissionAgGrid'].externalFilterChanged({ name: this.name })
    },
    onClickFilterItem(filterType, name, code) {
      if (filterType === 'ip') {
        this.ipFilterGroup.onItemClick(name, code)
      } else {
        this.transFilterGroup.onItemClick(name, code)
      }
      this.$forceUpdate()
    },
    onDoesExternalFilterPass(externalFilter, node, gridType) {
      const filterGroup = gridType === 'ip' ? 'ipFilterGroup' : 'transFilterGroup'
      if (!this[filterGroup].filters) return true

      const { data: row } = node
      let resMultiCondition = true
      const multiFilterKeys = Object.keys(this[filterGroup].filters).filter(key => this[filterGroup].filters[key].options.isMultiSelect)

      resMultiCondition = multiFilterKeys.map(mkey => {
        return this[filterGroup].filters[mkey].dataArray.some(item => {
          if (item.code !== 'All') {
            if (typeof item.fnFilter !== 'function') {
              return
            } else if (item.selected) {
              return item.fnFilter(row)
            }
          } else return item.selected
        })
      }).every(res => res)
      return resMultiCondition
    },
    async onLoadIpAlarmList() {
      try {
        const res = await apiIpAlarmList()
        this.ipNetworkList = res?.result
        this.$store.dispatch('nia/insertIpNetworkList', this.ipNetworkList)
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadTransmissionAlarmList() {
      try {
        const res = await apiTransmissionAlarmList()
        this.transmissionNetworkList = res?.result
        this.$store.dispatch('nia/insertTransmissionNetworkList', this.transmissionNetworkList)
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadDashboardStatistics() {
      const { dayType: DATE_TYPE, date } = this.systemChartCondition
      const formatStr = DATE_TYPE === 'DAY' ? 'YYYY-MM-DD' : 'YYYY-MM'
      const defaultSt = {
        trans_alarm_cnt: 0,
        link_ip_perf_cnt: 0,
        link_total_alarm_cnt: 0,
        ticket_ntt_cnt: 0,
        ticket_att_cnt: 0,
        ip_alarm_cnt: 0,
        ticket_rt_cnt: 0,
        link_total_resource_cnt: 0,
        trans_perf_cnt: 0,
        ip_resource_cnt: 0,
        ticket_pf_cnt: 0,
        ip_perf_cnt: 0,
        ip_sflow_cnt: 0,
        link_trans_perf_cnt: 0
      }
      try {
        const res = await apiDashboardStatistics({ DATE_TYPE, SEARCH_DATE: this.moment(date).format(formatStr) })
        this.statistics = res.result[0] ?? defaultSt
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadSelfProcessStatistics() {
      const { statisticsType: STATISTICS_TYPE, date } = this.selfChartCondition
      const SERIES_TYPE = STATISTICS_TYPE === 'hour' ? 'day' : this.getSelfProDateType()
      try {
        const resSelfProcess = await apiSelfProcessStatistics({ STATISTICS_TYPE, SERIES_TYPE, DATE: this.moment(date).add(1, 'd') })
        this.selfStatistics = resSelfProcess.result ?? []
      } catch (error) {
        this.error(error)
      }
    },
    getDefaultChartOptions(title, keyByTitle) {
      return {
        title: {
          text: title,
          left: 'center',
          top: 40,
          textStyle: {
            fontSize: 13
          }
          // subtext: 'Living Expenses in Shenzhen'
        },
        xAxis: {
          // type: 'category',
          type: 'value',
          // data: keyByTitle.map(v => v.name),
          axisLabel: {
            formatter: function (value, index) {
              let result = value
            if (value >= 1000000) {
              result = (value / 1000000) + 'M'
            } else if (value >= 1000) {
              result = (value / 1000) + 'K'
            } else {
              result = value.toString()
            }
              return result
            }
          }
          // axisLabel: { interval: 0, rotate: 20, fontWeight: 'bold', },
        },
        yAxis: {
          // type: 'value',
          type: 'category',
          data: keyByTitle.map(v => v.name),
          // axisLabel: {
          //   formatter: function (value, index) {
          //     let result = value
          //   if (value >= 1000000) {
          //     result = (value / 1000000) + 'M'
          //   } else if (value >= 1000) {
          //     result = (value / 1000) + 'K'
          //   } else {
          //     result = value.toString()
          //   }
          //     return result
          //   }
          // }
        },
        series: [
          {
            type: 'bar',
            label: {
              show: true,
              width: 20,
              position: 'right',
              fontWeight: 'bold',
              formatter: (param) => {
                return param.data.toLocaleString()
              },
            },
            barWidth: '20',
            itemStyle: {
              color: '#6149c7',
              borderWidth: 1,
              borderColor: '#189ec0',
              borderRadius: [0, 5, 5, 0]
            },
            data: keyByTitle.map(v => { return this.statistics[v.key] === null ? 0 : this.statistics[v.key] }),

          }
        ]
      }
    },
    getSelfProDateType() {
      let type = 'date'
      switch (this.selfChartCondition.statisticsType) {
        case 'hour':
          type = 'date'
          break
        case 'day':
          type = 'month'
          break
        case 'month':
          type = 'year'
          break
        default:
          break
      }
      return type
    },
    getStatus(row, col, value, index) {
      let result = ''
      switch (row.status) {
        case 'INIT':
        result = '발생'
          break
        case 'ACK':
        result = '인지'
          break
        case 'FIN':
        result = '마감'
          break
        case 'AUTO_FIN':
        result = '자동마감'

          break
        default:
          break
      }
      return result
    },
    getCellStyle(params) {
      let color = ''
      let background = ''
      switch (params.value) {
        case 'INIT':
          background = '#b14948'; color = '#fff'; break
        case 'ACK':
          background = '#f7aa17'; break
        case 'FIN':
          background = '#52a43a'; break
        case 'AUTO_FIN':
          background = '#adcc1e'; break
        default:
          break
      }
      return { 'background-color': background, color: color, 'font-weight': 600 }
    },
    selectedTicket(param) {
      this.selectedItem = param
    },
    handleOpenEditModal(row, type) {
      const param = { row, type }
      if (type === 'SOP') {
        this.$refs.ModalSopList.open(param)
      } else if (type === 'NTF') {
        this.$refs.ModalNTF.open(param)
      } else if (type === 'ALARM') {
        this.$refs.ModalAiResponse.open(param)
      } else if (type === 'FIN') {
        this.$refs.ModalFIN.open(param)
      } else if (type === 'CONFIG_TEST') {
        this.$refs.ModalConfigTest.open(param)
      }
    },

  }
}
</script>
<style lang="scss" scoped>
@import "~@/styles/variables.scss";

.NiaMain {
  ::v-deep .splitter-pane {
    display: flex;
    flex-direction: column;
  }
  .filter-container {
    height: 100%;
    display: flex;
    align-items: center;
    .title {
      height: 100%;
      display: flex;
      font-size: 18px;
      font-weight: 600;
      padding: 0px 10px;
      align-items: center;
      letter-spacing: 1.2px;
    }
    .filter-group {
      display: flex;
      margin-left: 10px;
      .split {
        &:before {
          content: "|";
          padding-left: 5px;
          font-weight: 700;
        }
      }
      .item-title {
        border-radius: solid 1px;
        font-weight: 600;
        .filter-text {
          white-space: nowrap;
        }
      }
      ul {
        display: flex;
      }
    }
  }
}
</style>
