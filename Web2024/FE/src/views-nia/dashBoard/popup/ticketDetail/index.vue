<template>
  <div :class="{ [name]: true, 'w-full h-full': true }">
    <el-row>
      <el-card shadow="never" :body-style="{'padding': '10px'}">
        <div slot="header">
          <div>
            <span><i class="el-icon-document" /> 전표 상세 정보</span>
          </div>
        </div>
        <el-row>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>TICKET ID</label>
            <el-input v-model="selectedRow.ticket_id" readonly />
          </el-col>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>장애 유형</label>
            <el-input v-model="selectedRow.root_cause_type" readonly />
          </el-col>
          <el-col :span="8" class="p-1 d-flex flex-column items-start">
            <label>장애 원인</label>
            <el-input v-model="selectedRow.ticket_rca_result_dtl_code" readonly />
          </el-col>
        </el-row>
      </el-card>
    </el-row>
    <el-row>
      <el-card shadow="never" :body-style="{'padding': '10px'}" class="mt-2">
        <div slot="header">
          <div>
            <span><i class="el-icon-document" /> 연관 알람 리스트</span>
          </div>
        </div>
        <el-row>
          <el-col>
            <CompAgGrid ref="alarmGrid" v-model="alarmByTicketAgGrid" class="w-100 flex-fill" style="height: 480px" />
          </el-col>
        </el-row>
      </el-card>
    </el-row>
    <el-row>
      <el-col align="right" class="mt-1">
        <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">{{ $t('exit') }}</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { Base } from '@/min/Base'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiTransmissionTicketDetail } from '@/api/nia'

import _ from 'lodash'

const routeName = 'ticketDetail'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid },
  extends: Base,
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      }
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      containerLoading: false,
      selectedRow: null,
      alarmList: []
    }
  },
  computed: {
    alarmByTicketAgGrid() {
      const columns = [
        { type: '', prop: '', name: 'TICKET_ID', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, formatter: () => { return this.selectedRow.ticket_id || '' } },
        { type: '', prop: '', name: '타입', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, cellStyle: params => params.data.al_ticket_id ? { color: 'red', 'font-weight': 800 } : null, formatter: (row) => { return row.al_ticket_id ? '근원' : '' } },
        { type: '', prop: 'alarmno', name: '알람번호', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return row.alarmno ?? '-' } },
        { type: '', prop: 'alarmtime', name: '장애 발생시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return this.formatterTimeStamp(row.alarmtime, 'YYYY/MM/DD-HH:mm:ss') } },
        { type: '', prop: 'receivetime', name: '시스템 수신시간', width: 200, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return this.formatterTimeStamp(row.receivetime, 'YYYY/MM/DD-HH:mm:ss') } },
        { type: '', prop: 'sysname', name: '장비명', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'equiptype', name: '장비종류', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmloc', name: '인터페이스명', width: 200, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'alarmmsg', name: '장애정보', width: 150, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'unit', name: '유니트', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'cleartime', name: '마감시간', width: 100, alignItems: 'center', fixed: false, suppressMenu: true, formatter: (row) => { return this.formatterTimeStamp(row.cleartime, 'YYYY/MM/DD-HH:mm:ss') } },
        { type: '', prop: 'ptpname', name: 'ptp명', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
        { type: '', prop: 'slot', name: 'slot', width: 100, alignItems: 'center', fixed: false, suppressMenu: true },
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return { options, columns, data: this.alarmList }
    },

  },
  created () {
    this.selectedRow = this.wdata.params
  },
  mounted() {
    this.$nextTick(async() => {
     await this.onLoadAlarmList()
    })
  },
  methods: {
    async onLoadAlarmList() {
      try {
        const res = await apiTransmissionTicketDetail({ TICKET_ID: this.selectedRow.ticket_id })
        this.alarmList = res.result ?? []
      } catch (error) {
        this.error(error)
      }
    }
  },
}
</script>

<style lang="scss" scoped>
::v-deep .el-input .el-input__inner {
  border: solid 0px;
  border-bottom: solid 1px lightgray;
  border-radius: 0px;
}
</style>
