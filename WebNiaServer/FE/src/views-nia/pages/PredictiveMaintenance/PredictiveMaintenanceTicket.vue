<template>
  <div :class="[{ [name]: true }]" class="d-flex" style="height: calc(100vh - 134px);">
    <div class="p-2" style="width: 700px;">
      <div class="top-container d-flex justify-between">
        <div class="title">
          <el-icon class="el-icon-tickets pr-1" /> 광모듈 장애 예측
        </div>
        <div class="h-100 d-flex items-center function-panel">
          <div class="d-flex px-2">
            <el-checkbox v-model="isExceptOnlyRoadm">원홉 캐리어 제외 보기</el-checkbox>
            <el-checkbox v-model="isShowClearTicket">마감 전표 보기</el-checkbox>
          </div>
          <el-col class="total-count fr">
            <span class="total">TOTAL: {{ getTicketList.length || 0 }}</span>
          </el-col>
        </div>
      </div>
      <div ref="ticketPanel" class="ticket-panel" style="height: calc(100% - 55px)">
        <el-card
          v-for="row in getTicketList"
          :key="row.ticketno"
          shadow="hover"
          :class="{'filp-open': ticketCtrl[row.ticketno] && ticketCtrl[row.ticketno].isOpen || false}"
          :style="{'border':row.ticketno === selectedTicket.ticketno?'2px solid red':'0px solid'}"
          @dblclick.native="onDoubleClickTicket(row)"
          @click.native="openDetail(row)"
        >
          <div class="ticket d-flex" :style="{'height': !otherOptions.fold || ticketCtrl[row.ticketno].isOpen ? '140px': '110px'}">
            <div class="section-l">
              <div class="ticket-id">전표 번호 #.{{ row.ticketno }}</div>
              <div class="alarm-title">{{ row.reason }} </div>
              <div :class="{'d-none': otherOptions.fold && !ticketCtrl[row.ticketno].isOpen}">
                <span>경보 생성 날짜 :{{ getFormatterTime(row.issue_date) }}</span>
                <span>경보 마감 날짜 :{{ row.clear_date ? getFormatterTime(row.clear_date): '' }}</span>
              </div>
            </div>
            <div class="section-r">
              <div class="d-flex justify-between items-center pl-3">
                <div class="d-flex items-start text-rotate" style="font-weight: 700;flex-direction: column;width: 300px;">
                  <div><span style="font-weight:900">캐리어명: </span>{{ row.trunk_name }}</div>
                  <div><span style="font-weight:900">선로방향:</span> {{ row.direction ==='UP' ? '상향': '하향' }}</div>
                </div>
                <div style="font-weight: 700;">
                  <span><v-icon :name="row.direction === 'UP'? 'arrow-left': 'arrow-right'" style="width: 40px; stroke-width: 4;color: red;" /></span>
                </div>
              </div>
              <div class="middle d-flex items-center w-100 mt-2 justify-center text-rotate">
                <div>
                  캐리어내 Node Total Deviation : {{ getToFixed(row.node_total_deviation) }} dB (전표 발행일 기준)
                </div>
              </div>
              <div style="padding-top: 5px">
                <el-button type="normal" round style="padding: 5px 10px" @click="handleReviewOpinion(row)">
                  <i class="el-icon-document-copy" />
                  검토 의견<span style="font-size: 11px;color: darkslategray;">({{ row.opinion_cnt }})</span>
                  <img v-if="!row.is_check_modal && isShowOpinionNew(row)" src="@/assets/icon/icons_new.png" style="width: 18px;position: absolute;bottom: 20px;">
                </el-button>
              </div>
            </div>
          </div>
          <div class="failure-node">
            <div class="d-flex top items-center">
              <div class="w-100">조치 필요 구간</div>
              <i class="el-icon-close" style="font-size:17px; cursor: pointer;" @click.stop="onClickCloseDetail(row)" />
            </div>
            <div v-for="(item, index) in azList(row)" :key="index" class="w-100 d-flex az-row">
              <div class="d-flex w-50">
                <div class="pl-2">
                  <div>{{ item.sysnamea }}</div>
                </div>
              </div>
              <div class="d-flex w-50" style="border-left: solid 1px grey">
                <div class="pl-2">
                  <div>{{ item.sysnamez }}</div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <div style="width: calc(100% - 600px)" class="h-100 p-2">
      <div class="info-filter-container" style="height: 45px">
        <el-radio-group v-model="topologyValue">
          <el-radio-button label="rx_tx">Rx / Tx</el-radio-button>
          <el-radio-button label="span_gain">Span Loss / Node Gain</el-radio-button>
          <el-radio-button label="ntd">Node Total Deviation</el-radio-button>
        </el-radio-group>
      </div>
      <div class="trunkName">{{ selectedTicket.trunk_name ? '캐리어명: ' + selectedTicket.trunk_name : '' }}</div>
      <div style="position: relative; height: calc(100% - 85px);">
        <Comp2DTopology
          ref="topology2d"
          style="height: 100% !important;"
          :ticket="selectedTicket"
          :node-info-option="topologyValue"
          @loadList="loadList"
          @selectedTopologyItem="onChangeSelectedTopologyItem"
        />
        <div v-if="!selectedTicket.ticketno" class="topology-empty-overlay">
          <i class="el-icon-connection" />
          <p>전표를 더블클릭하면 토폴로지가 표시됩니다.</p>
        </div>
      </div>
      <ModalPredictiveReviewOpinion ref="modalPredictiveReviewOpinion" :fullscreen="isViewport('<', 'sm')" />
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import { Base } from '@/min/Base.min.js'
import { apiRcaRequest } from '@/api/nia'
import Comp2DTopology from './component/Map2D.vue'
import moment from 'moment'
import { getMbaTimeStamp } from '@/views-nia/js/common-function'
import ModalPredictiveReviewOpinion from '@/views-nia/pages/PredictiveMaintenance/modal/ModalPredictiveReviewOpinion'

const routeName = 'PredictiveMaintenanceTicket'

export default {
  name: routeName,
  components: { Comp2DTopology, ModalPredictiveReviewOpinion },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      filterGroup: Vue.observable({}),
      clickedNode: null,
      clickedLink: null,
      isShowClearTicket: false,
      isExceptOnlyRoadm: false,
      otherOptions: Vue.observable({}),
      ticketCtrl: {},
      pmmTicketList: [],
      userReadList: {},
      selectedTicket: {},
      mapClickedData: null,
      topologyValue: 'rx_tx'
    }
  },
  computed: {
    getTicketList() {
      const searchText = this.rcaTicket.fullFilterText
      let list = this._cloneDeep(this.rcaTicket.pmmTicketList?.rows || this.pmmTicketList || [])

      if (this.isShowClearTicket) {
        list = list.filter(row => row.clear_date)
      } else {
        list = list.filter(row => !row.clear_date)
      }

      return list.filter(row => {
        let clear = true
        let notOneHop = true

        if (this.isShowClearTicket) clear = row.clear_date
        if (this.isExceptOnlyRoadm) notOneHop = row.roadm_cnt > 2

        return clear && notOneHop && JSON.stringify(row).includes(searchText)
      })
    }
  },
  mounted() {
    this.loadTicketList()
    this.setReadOpinionByUser()
  },
  methods: {
    azList(row) {
      return this.ticketCtrl[row.ticketno]?.azList ?? []
    },
    async loadTicketList() {
      const target = ({ vue: this.$refs.ticketPanel })
      try {
        this.openLoading(target, { background: '#dadddf' })
        const res = await apiRcaRequest('SELECT_TICKET_PMM_LIST')
        this.pmmTicketList = res?.result || []
        this.$store.dispatch('rcaTicket/insertTicketPmmList', this.pmmTicketList)
        this.initTicketCtrl()
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async openDetail(ticket) {
      if (this.ticketCtrl[ticket.ticketno].isOpen) {
        return
      }
      if (!this.ticketCtrl[ticket.ticketno]) {
        this.initTicketCtrl(ticket)
      }
      this.loadAzList(ticket)
    },
    async loadAzList(ticket) { // 전표 유형1 : NTD변화량이 -+2 이상일 경우 조치필요 구간
      const ticketCtrl = this._cloneDeep(this.ticketCtrl)
      ticketCtrl[ticket.ticketno].isOpen = true

      if (ticket.reason === '광레벨 장기 변화 예측 경보') {
        ticketCtrl[ticket.ticketno].azList = []
        this.ticketCtrl = ticketCtrl
        return
      }
      try {
        let nodeList = []
        // const res = await apiRcaRequest('SELECT_PMM_PRECEDING_NODE_LIST', { TICKETNO: ticket.ticketno, DIRECTION: ticket.direction })
        const timeStamp = getMbaTimeStamp()
        await apiRcaRequest('SELECT_PMM_TOPOLOGY_LIST', { TRUNK_NAME: ticket.trunk_name || '', DIRECTION: ticket.direction || '', TIME_STAMP: timeStamp }).then(async(result) => {
          nodeList = result?.result || []
        })

        if (ticket.direction === 'UP') {
          nodeList = nodeList.sort((a, b) => b.routenum - a.routenum)
        }
        nodeList.forEach((node, idx, arr) => {
          if (idx + 1 < arr.length) {
            const diff = Math.abs(((arr[idx + 1].node_total_deviation) - arr[idx].node_total_deviation).toFixed(1))
            Object.assign(arr[idx + 1], { ntdDiff: diff })
            if (diff >= 2) {
              ticketCtrl[ticket.ticketno].azList.push({ sysnamea: arr[idx].sysname, sysnamez: arr[idx + 1].sysname })
            }
          }
        })
        if (ticketCtrl[ticket.ticketno].azList.length === 0) {
          if (nodeList.length > 0) {
            const maxObj = nodeList.reduce((prev, value) => {
              return (prev?.ntdDiff ?? 0) >= (value?.ntdDiff ?? 0) ? prev : value
            })
            const maxIdx = nodeList.findIndex(v => v.routenum === maxObj.routenum)
            ticketCtrl[ticket.ticketno].azList.push({ sysnamea: nodeList[maxIdx - 1].sysname, sysnamez: nodeList[maxIdx].sysname })
          }
        }
      } catch (error) {
        this.error(error)
      } finally {
        /*  object는 dom에서 update를 감지하지 못하기 때문에 덮어 씌워주는 코드를 추가함 */
        this.ticketCtrl = ticketCtrl
      }
    },
    onDoubleClickTicket(row) {
      this.selectedTicket = row
      this.topologyValue = 'rx_tx'
    },
    onChangeSelectedTopologyItem(e) {
      this.mapClickedData = e
      if (e.target_type !== 'node') {
        return
      }
      this.openNewTab({ sysname: e.d.sysname, direction: e.d.direction })
    },
    openNewTab(param) {
      // // open url 파라미터 전달
      // const { sysname, direction } = param
      // const url = `${window.location.origin}/#/pmm/chart`
      // var windowFeatures = 'width=600,height=400,toolbar=no,location=no,menubar=no,scrollbars=yes,resizable=yes'
      // window.sessionStorage.setItem('pmmchart', JSON.stringify({ viewingPeriod: '1day', sysname, direction, trunk_name: this.selectedTicket.trunk_name }))
      // const win = window.open(url, '_blank', windowFeatures)
      // win?.focus()
    },
    loadList() {
      // 토폴로지 데이터 로드 완료 콜백
    },
    async loadRepeaterSlot() {
      const ticket = this.selectedTicket
      try {
        const res = await apiRcaRequest('SELECT_MBA_REPEATER_SLOT_DATA', { TRUNK_NAME: ticket.trunk_name }, '/selectOne')
        Object.assign(this.selectedTicket, { rsspuSlot: res?.result?.rsspuslot })
      } catch (error) {
        this.error(error)
      }
    },
    setReadOpinionByUser() {
      const readOpinionByUser = JSON.parse(window.localStorage.getItem('ReadOpinionByUser')) ?? null
      if (readOpinionByUser !== null) {
        this.readOpinionByUser = readOpinionByUser[this.$store.state.user.uid] ?? {}
      }
    },
    isShowOpinionNew(ticket) {
      if (!this.readOpinionByUser) {
        return !!ticket?.new_list
      }
      if (ticket?.new_list?.length > 0 && !this.readOpinionByUser[ticket.ticketno]) {
        return true
      } else if (ticket?.new_list?.filter(x => !this.readOpinionByUser[ticket.ticketno]?.readList?.includes(x))?.length > 0 || this.moment(ticket.last_handling_time).isAfter(this.readOpinionByUser[ticket.ticketno]?.readTime)) {
        return true
      } else {
        return false
      }
    },
    handleReviewOpinion(ticket) {
      ticket.is_check_modal = true
      this.$forceUpdate()
      this.$refs.modalPredictiveReviewOpinion.open(ticket)
    },
    onClickCloseDetail(ticket) {
      const ticketCtrl = this._cloneDeep(this.ticketCtrl)
      ticketCtrl[ticket.ticketno].isOpen = false
      this.ticketCtrl = ticketCtrl
    },
    initTicketCtrl(ticket = null) {
      if (ticket !== null) {
        Object.assign(this.ticketCtrl, { [ticket.ticket_id]: { isOpen: false, type: ticket.ticket_type, azList: [] } })
      } else {
        this.pmmTicketList.forEach(row => { Object.assign(this.ticketCtrl, { [row.ticketno]: { isOpen: false, type: row.ticket_type, azList: [] } }) })
      }
    },
    getToFixed(ntd) {
      return ntd?.toFixed(1) ?? ''
    },
    exportExcel(tableRef) {
      const ref = this.$refs
      const timeFormat = this.toStringTime(new Date(), 'YYMMDDHHmmss')
      const title = '알람 리스트'
      ref[tableRef].exportCsv(`${title}_${timeFormat}`)
    },
    getFormatStatus(status) {
      let code = status
      if (code === 'AUTO_FIN' || code === 'NATURE') {
        code = 'FIN'
      }
      return this.CONSTANTS.RcaTicketStatus[code]?.text ?? ''
    },
    getFormatterTime(time) {
      return moment(time).format('YYYY-MM-DD')
    },
    getRowStyle(params) {
      const level = params.data.value_level
      let bgColor = ''
      switch (level) {
        case 'critical':
          bgColor = '#f56c6c'
          break
        case 'major':
          bgColor = '#fdb025'
          break
        case 'minor':
          bgColor = '#fde66b'
          break
        case 'warning':
          bgColor = '#8bd6e3'
          break
        case 'clear':
          bgColor = '#8bd1375c'
          break
        default:
          break
      }
      // if (params.data.value_level === 'critical') { return { backgroundColor: '#ffb4b4' }
      return { backgroundColor: bgColor }
    }
  }
}
</script>

<style lang="scss">
.PredictiveMaintenanceTicket {
  font-family: 'NanumSquare';
  transform: rotate(-0.03deg);
  background-color: rgb(246, 246, 246);
  .close .el-collapse-item__header {
    border-radius: 15px;
  }
  .el-collapse-item__header {
    height: 35px;
    transition: all 0.25s;
    border-bottom: solid 1px lightgray;
    border-radius: 15px 15px 0px 0px;
  }
  .el-collapse-item__wrap {
    border-radius: 0 0 15px 15px;
  }
  .el-collapse-item__content {
    padding-bottom: 0px;
  }
  .el-collapse-item__arrow {
    font-weight: 900;
  }
  .el-card__body {
    width: 100%;
    // height:120px;
    display: flex;
    flex-direction: column;
    padding: 0px;
  }
  .el-card {
    border-radius: 20px;
    margin-bottom: 10px;
  }
  .rca-alarm-tab {
    background: #fff;
    border-radius: 0 0 17px 17px;
    margin: 0px 2px;
    .el-tabs__header {
      margin: 0px;
    }
    .el-tabs__content {
      height: calc(100% - 40px);
    }
  }
}
</style>
<style lang="scss" scoped>
.top-container {
  font-size: 20px;
  padding: 8px;
  margin-bottom: 10px;
  border-radius: 10px;
  box-shadow: 1px 3px 5px 5px #93939326;
  i {
    font-size: 20px;
  }
  .title {
    font-size: 19px;
    font-weight: 800;
  }
  .total {
    font-size: 15px;
    font-family: 'agGridMaterial';
    padding-left: 10px;
    font-weight: 900;
    color: black;
  }
  .function-panel {
    padding: 0px 5px;
    .total-count {
      border-left: solid 1px lightgray;
    }
    ::v-deep.el-checkbox__label {
      font-weight: 700;
      transform: rotate(0.03deg);
    }
    .el-tooltip {
      margin : 0px 5px;
      &:hover {
        opacity: 0.5;
      }
    }
  }
}
::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  font-weight: 800;
  background: #043644;
}
::v-deep .el-radio-button__inner {
  font-weight: 800;
  &:hover {
    color: #a4a4a4;
  }
}
.text-rotate {
  transform: rotate(0.03deg);
}

.trunkName {
  position: absolute;
  z-index: 1;
  color: #fff;
  right: 33px;
  top: 70px;
  font-size: 18px;
}
.topology-empty-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: rgba(3, 44, 55, 0.85);
  border-radius: 17px 17px 0 0;
  z-index: 1;
  color: #7b9aa5;
  i {
    font-size: 48px;
    margin-bottom: 16px;
  }
  p {
    font-size: 15px;
    font-weight: 600;
      letter-spacing: 0.5px;
    }
}
.ticket-panel {
  padding: 20px;
  overflow-y: scroll;
  border-radius: 20px;
  background: #dadddf;
  transition-duration: 0.5s;

  .ticket {
    font-family: 'NanumSquare';
    transform: rotate(-0.03deg);
    transition: all 0.2s;
    border-radius: 20px;
    transform-style: preserve-3d;
    .section-l {
      border-radius: 18px 0px 0px 0px;
      padding: 10px;
      width: 185px;
      text-align: left;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      font-size: 12px;
      font-weight: 600;
      background-color: #ffcd2a;
      div {
        transform: rotate(-0.03deg);
        span {
          white-space:nowrap;
        }
      }
      .ticket-id {
        padding: 5px 0px;
      }
      .alarm-title {
        height: 70px;
        font-size: 20px;
        font-weight: 700;
        transition: all .25s;
      }
    }
    .section-r {
      width: calc(100% - 160px);
      padding: 10px;
      background-color: #fff;
      display: flex;
      flex-direction: column;
      justify-content: center;
      border-radius: 0px 18px 0px 0px;
      .cableA, .cableZ {
        width: 100%;
        height: 24px;
        display: flex;
        align-items: center;
        span.cable {
          width: 16px;
          height: 16px;
          color: #fff;
          font-size: 11px;
          font-weight: 800;
          text-align: center;
          // padding-left: 15px;
          border-radius: 100%;
          background-color: #f9423e;
        }
        .node {
          width: 273px;
          height: 16px;
          padding: 0px 8px;
          color: #ce5c5b;
          font-weight: 800;
          font-size: 12px;
          margin-left: 10px;
          border-radius: 8px;
          background-color: #7a7c8d38;
          transform: rotate(-0.03deg);
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          text-align: center;
          &:hover {
            text-overflow: clip;
          }
        }
        &:after {
          content: "";
          top: 26px;
          margin-left: 7px;
          height: 16px;
          position: absolute;
          border-right: 2px dotted #f9423e;
        }
      }
      .trunk {
        display: flex;
        align-items: center;
        font-size: 14px;
        font-weight: 800;
        span:nth-child(1) {
          width: 170px;
          overflow: hidden;
          padding-right: 6px;
          white-space: nowrap;
          text-overflow: ellipsis;
          &:hover {
            white-space: normal;
          }
        }
        span {
          transform: rotate(-0.03deg);
        }
      }
      .middle {
        font-size: 14px;
        font-weight: 600;
        height: 35px;
        border-top: solid 1px #d4d7d9;
        border-bottom: solid 1px #d4d7d9;
        span {
          font-size: 20px;
          color: #345035;
        }
        div {
          display: flex;
          transform: rotate(-0.03deg);
          height: 100%;
          align-items: center;
          padding: 5px 7px;
        }
      }
      .bottom {
        font-size: 13px;
        font-weight: 800;
        color: #666;
        padding: 10px;
        justify-content: space-between;
        div {
          transform: rotate(-0.03deg);
        }
        .alarm-time {
          color: #003f33;
          padding-top: 5px;
        }
      }
      button:hover {
        color: #fff;
        background-color: #043644e8;
        span span{
          color: #fff !important;
        }
      }
      .el-button {
        color: black;
      }
    }
    &:hover {
      height: 140px !important;
      .alarm-title {
        font-size: 22px;
      }
    }
  }
  .failure-node {
    height: 0px;
    overflow-y: auto;
    overflow-x: hidden;
    // transform: rotateX(180deg) translateZ(3px);
    transition-property: all;
    transition-duration: 0.6s;
    transform: rotateX(90deg);
    transform-origin: top;
    .top {
      text-align: center;
      font-weight: 700;
      padding: 3px;
      background-color: rgb(241 241 241);
      i:hover {
        transform: rotate(90deg);
        transition-duration: 0.2s;
      }
      div {
        transform: skew(0.3deg);
      }
    }
    .az-row {
      padding: 5px 15px;
      transform: skew(0.3deg);
      &:hover {
        background: rgba(158, 158, 158, 0.678);
      }
    }
  }
  .filp-open .failure-node {
    height: 150px;
    transform: rotateX(0deg) translateZ(0)
  }
  .filp-open .ticket {
    transform: translateZ(0);
  }
}
</style>
