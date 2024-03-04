<template>
  <div :class="{ [name]: true }">
    <LeftBar class="h-full">
      <template slot="top-container">
        <filterBar position="TOP">
          <template slot="function-container">
            <div class="filter-container">
              <div class="title">IP망</div>
              <div class="filter-group">
                <template v-for="(filter, keyName, i) in filterGroup.filters">
                  <div :key="keyName" class="item-title mr-2">
                    {{ filter.filterTitle || '' }}
                  </div>
                  <ul :key="i">
                    <!-- :class="{'filterBtn': !filterIconList.includes(keyName), 'filterIcon d-flex':filterIconList.includes(keyName)}" -->
                    <li
                      v-for="(item, index) in filter.getArray()"
                      :key="index"
                      class="checkItem d-flex items-center checked ml-1"
                      :style="{'background-color': item.hex, 'color': item.color }"
                      @click="onClickFilterItem(filter.filterName , item.code)"
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
        />
        <!-- top-container content -->
      </template>
      <template slot="bottom-container">
        <filterBar position="BOTTOM">
          <template slot="function-container">
            <div class="filter-container">
              <div class="title">전송망</div>
              <div />
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
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import LeftBar from '@/layout/components/gridTemplate/LeftBar'
import filterBar from '@/layout/components/filterBar'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import BaseFilterGroup from '@/filters/baseFilterGroup'

import { apiIpAlarmList, apiTransmissionAlarmList } from '@/api/nia'
const routeName = 'NiaMain'

export default {
  name: routeName,
  components: { CompAgGrid, LeftBar, filterBar },
  extends: Base,
  data() {
    return {
      name: routeName,
      filterGroup: '',
      ipNetworkList: [],
      transmissionNetworkList: []

    }
  },
  computed: {
    ipAgGrid() {
      const columns = [
        { type: '', prop: 'ticket_id', name: 'TICKET_ID', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_generation_time', name: '최초 장애 발생시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return this.formatterTimeStamp(row.ticket_generation_time, 'YYYY/MM/DD-HH:mm:ss') } },
        { type: '', prop: 'status', name: '상태', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: this.getStatus },
        { type: '', prop: 'ticket_type', name: 'ALARM TYPE', width: 150, alignItems: 'center', fixed: false, suppressMenu: true, formatter: this.getAlarmType },
        { type: '', prop: 'root_cause_type', name: '장애유형', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 원인', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'total_related_alarm_cnt', name: '근원알람개수', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ip_addr', name: 'ip_addr', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: '', name: 'SOP', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: '', name: '장애대응', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false, onDoesExternalFilterPass: this.onIpDoesExternalFilterPass }
      return { options, columns, data: this.ipNetworkList }
    },
    transmissionAgGrid() {
      const columns = [
        { type: '', prop: 'ticket_id', name: 'TICKET_ID', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'cluster_no', name: '클러스터 No.', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_generation_time', name: '최초 장애 발생시간', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'status', name: '상태', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_type', name: 'ALARM TYPE', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'root_cause_type', name: '장애유형', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ticket_rca_result_dtl_code', name: '장애 원인', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'total_related_alarm_cnt', name: '근원알람개수', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'ip_addr', name: 'ip_addr', width: 100, alignItems: 'center', fixed: false, suppressMenu: true }
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return { options, columns, data: this.transmissionNetworkList }
    }
  },
  beforeMount () {
    // this.filterGroup = new BaseFilterGroup(this, { onFilterChanged: this.onFilterChanged, isCheckBox: false })
  },
  mounted () {
    this.$nextTick(async() => {
      await this.onLoadIpAlarmList()
      await this.onLoadTransmissionAlarmList()
      this.filterGroup = new BaseFilterGroup(this, { onFilterChanged: this.onFilterChanged, isCheckBox: false })
      this.setIPFilterGroup()
    })
  },
  methods: {
    setIPFilterGroup() {
      const listName = 'ipNetworkList'
      const btnOption = { isMultiSelect: false, allItem: true, ifAllthenOtherUncheck: true, listName }
      const iconOption = { allItem: true, ifAllthenOtherUncheck: true, listName }

      this.filterGroup.addFilter('ip_status', '상태', this.CONSTANTS.nia.status_type, btnOption) // 누적레벨
    },
    onFilterChanged(changedFilter, code) {
      console.log('onFilterChanged')
      this.$refs.ipAgGrid.externalFilterChanged({ name: this.name })
    },
    onClickFilterItem(name, code) {
      this.filterGroup.onItemClick(name, code)
      this.$forceUpdate()
    },
    onIpDoesExternalFilterPass(externalFilter, node) {
      if (!this.filterGroup.filters) return true
    },
    async onLoadIpAlarmList() {
      try {
        const res = await apiIpAlarmList()
        this.ipNetworkList = res?.result
      } catch (error) {
        this.error(error)
      }
    },
    async onLoadTransmissionAlarmList() {
      try {
        const res = await apiTransmissionAlarmList()
        this.ipNetwotransmissionNetworkListrkList = res?.result
      } catch (error) {
        this.error(error)
      }
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
    getAlarmType(row, col, value, index) {
      let result = ''
      switch (row.ticket_type) {
        case 'RT':
        result = '장애'
          break
        case 'FTT':
        result = '비장애'
          break
        case 'PF':
        result = '광레벨'
          break
        case 'ATT2':
        result = '이상 트래픽'
          break
        case 'NTT':
        result = '유해 트래픽'
          break
        case 'NFTT':
        result = '장비부하장애'
          break

        default:
          break
      }
      return result
    }
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
      // .item-title {

      // }
      ul {
        display: flex;
      }
    }
  }
}
</style>
