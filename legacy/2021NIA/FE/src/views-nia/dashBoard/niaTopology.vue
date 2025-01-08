<template>
  <div :class="{ [name]: true, 'h-100': true }">
    <div layout="row" flex layout-fill ng-cloak>
      <div class="map-area" style="display: flex; flex-direction: column; width: 100%; height: 100%;">
        <!-- v-show="!singlePage" -->
        <div class="underline" style="display: flex; flex-direction: column; flex-wrap: wrap;">
          <div class="rightContent" style="z-index:9999">
            <div class="ttSetting" >
              <!-- style="overflow: hidden;" -->
              <ul>
                <li
                  v-if="isVisibleView('LAYERED')"
                  class="refresh image_dark_Converter tooltip-hover"
                  @click="broadcastMenuEvent('reload')"
                >
                  <span class="tooltip-text">새로고침</span>
                </li>
                <li
                  v-if="isVisibleView('LAYERED')"
                  style="font-size:20px;"
                  class="tooltip-hover"
                  @click="broadcastMenuEvent('changeType')"
                >
                  <font-awesome-icon style="color:gray" :icon="['fas', 'retweet']" />
                  <span class="tooltip-text">토폴로지 타입 변경</span>
                </li>
                <li
                  v-if="isVisibleView('LAYERED')"
                  style="font-size:20px;"
                  class="tooltip-hover"
                  @click="broadcastMenuEvent('resetZoom')"
                >
                  <font-awesome-icon style="color:gray" :icon="['fas', 'globe']" />
                  <span class="tooltip-text">줌 전체보기</span>
                </li>
                <li
                  v-if="isVisibleView('LAYERED')"
                  class="nodeblink image_dark_Converter tooltip-hover"
                  @click="broadcastMenuEvent('nodeZoomTest')"
                >
                  <span class="tooltip-text">노드 줌인 테스트</span>
                </li>
                <li
                  style="font-size:20px;"
                  class="tooltip-hover"
                  @click="broadcastMenuEvent('updateTopology')"
                >
                  <font-awesome-icon style="color:gray" :icon="['fas', 'save']" />
                  <span class="tooltip-text">저장</span>
                </li>
                <li
                  v-if="isVisibleView('LAYERED')"
                  class="traffic image_dark_Converter tooltip-hover"
                  @click="broadcastMenuEvent('linkZoomTest')"
                >
                  <span class="tooltip-text">링크 줌인 테스트</span>
                </li>
                <li
                  v-if="isVisibleView('LAYERED')"
                  class="nodetitle image_dark_Converter tooltip-hover"
                  @click="broadcastMenuEvent('toggleLabel')"
                >
                  <span class="tooltip-text">라벨 토글</span>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div layout="row" layout-sm="row" layout-fill="" style="position: relative;">
          <div id="componentWapperId" ref="componentWapper" class="componentWapper" style="overflow:hidden; position: relative">
            <div class="background"></div>
            <svg id="topology_container" viewBox="0 0 1920 1080" class="jctx-host jctx-id-foo">
              <defs>
                <marker id="arrow" viewBox="0 0 10 10" orient="auto" markerWidth="3" markerHeight="3" refX="23" refY="5">
                  <path fill="red" d="M 0 0 L 10 5 L 0 10 z"></path>
                </marker>
              </defs>
            </svg>
            <div id="templateUnitArea"></div>
            <div id="templateAgentListArea" style="display: none;">
              <div class="properties flex-column">
                <div class="flex-item site">
                  <div>이용기관</div>
                  <div class="table-box-wrap">
                    <div class="table-box">
                      <table>
                        <caption>아래는 스크롤 테이블 입니다.</caption>
                        <colgroup>
                          <col width="100px">
                          <col width="200px">
                          <col width="200px">
                        </colgroup>
                        <thead>
                        </thead>
                        <tbody>
                          <tr>
                            <th scope="col" style="width: 100px;">NO.</th>
                            <th scope="col" style="width: 200px;">SITE</th>
                            <th scope="col" style="width: 200px;">I/F</th>
                          </tr>
                          <tr
                            v-for="(agency, index) in sortedAgencyList"
                            :key="agency.nren_name"
                            :class="{ 'animation-blink': ticket.is_organ_system && ticket.root_cause_sysnamez === agency.nren_name }"
                          >
                            <td>{{ index + 1 }}</td>
                            <td>{{ agency.nren_name }}</td>
                            <td>{{ agency.node_int }}</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>

    <div id="template-2">
      <div class="gripper"></div>
      <div class="properties flex-column" style="display: none;">
        <div class="flex-item node" style="display: none;">
          <div>NODE INFORMATION</div>
          <table class="tg">
            <thead>
              <tr class="alias">
                <th>ALIAS</th>
                <th>Device1</th>
                <th>Device2</th>
              </tr>
            </thead>
            <tr class="id">
              <td>ID</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="ip">
              <td>IP</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="type">
              <td>TYPE</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="mac">
              <td>MAC</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="alarm">
              <td>ALARM</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="port">
              <td>PORT</td>
              <td></td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item link">
          <div>LINK INFOMATION</div>
          <table>
            <tr class="link_alias">
              <td>ALIAS</td>
              <td></td>
            </tr>
            <tr class="speed">
              <td>SPEED</td>
              <td></td>
            </tr>
            <tr class="status">
              <td>STATUS</td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item unit">
          <div id="template-POTN">
            <div class="template POTN">
              <div class="title-wapper">
                <div class="left">POTN <span class="potnSysname">${시스템명}</span></div>
              </div>
              <div class="table-wapper">
                <table style="font-size: 10px; width: 25em; word-break: break-all; line-height: 15px; display: table-cell; text-align: center;">
                  <tbody style="height: 15em;">
                    <!--<tr>-->
                    <!--<td colspan="5">FAN</td>-->
                    <!--<td colspan="6">FAN</td>-->
                    <!--<td colspan="5">FAN</td>-->
                    <!--</tr>-->
                    <tr>
                      <td rowspan="2" style="height: 6em;">OMU A</td>
                      <td class="S1 S01" rowspan="4">S01</td>
                      <td class="S2 S02" rowspan="4">S02</td>
                      <td class="S3 S03" rowspan="4">S03</td>
                      <td class="S4 S04" colspan="2" rowspan="4">S04</td>
                      <td class="S5 S05" rowspan="4">S05</td>
                      <td class="S11" rowspan="4">OXCU A</td>
                      <td class="S12" colspan="2" rowspan="4">OXCU B</td>
                      <td class="S6 S06" colspan="2" rowspan="4">S06</td>
                      <td class="S7 S07" rowspan="4">S07</td>
                      <td class="S8 S08" rowspan="4">S08</td>
                      <td class="S9 S09" rowspan="4">S09</td>
                      <td class="S10" rowspan="4">S10</td>
                    </tr>
                    <tr>
                    </tr>
                    <tr>
                      <td rowspan="2" style="height: 6em;">OMU B</td>
                    </tr>
                    <tr>
                    </tr>
                    <!--<tr>-->
                    <!--<td colspan="5">FAN</td>-->
                    <!--<td colspan="6">FAN</td>-->
                    <!--<td colspan="5">FAN</td>-->
                    <!--</tr>-->
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div id="template-3">
      <div class="gripper"></div>
      <div class="properties flex-column" style="display: none;">
        <div class="flex-item node" style="display: none;">
          <div>NODE INFORMATION</div>
          <table class="tg">
            <thead>
              <tr class="alias">
                <th>ALIAS</th>
                <th>Device1</th>
                <th>Device2</th>
              </tr>
            </thead>
            <tr class="id">
              <td>ID</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="ip">
              <td>IP</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="type">
              <td>TYPE</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="mac">
              <td>MAC</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="alarm">
              <td>ALARM</td>
              <td></td>
              <td></td>
            </tr>
            <tr class="port">
              <td>PORT</td>
              <td></td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item link">
          <div>LINK INFOMATION</div>
          <table>
            <tr class="link_alias">
              <td>ALIAS</td>
              <td></td>
            </tr>
            <tr class="speed">
              <td>SPEED</td>
              <td></td>
            </tr>
            <tr class="status">
              <td>STATUS</td>
              <td></td>
            </tr>
          </table>
        </div>
        <div class="flex-item unit">
          <div id="template-ROADM">
            <div class="template ROADM">
              <div class="title-wapper">
                <div class="left">ROADM  <span class="roadmSysname">${시스템명}</span> </div>
              </div>
              <div class="table-wapper">
                <table style="font-size: 10px; width: 25em; word-break: break-all; line-height: 15px; display: table-cell; text-align: center;">
                  <tbody>
                    <tr class="slot-table" style="height: 15em;">
                      <td class="S1 S01">S1</td>
                      <td class="S2 S02">S2</td>
                      <td class="S3 S03">S3</td>
                      <td class="S4 S04">S4</td>
                      <td class="S5 S05">S5</td>
                      <td class="S6 S06">S6</td>
                      <td class="S7 S07">S7</td>
                      <td class="S8 S08">S8</td>
                      <td class="S9 S09">S9</td>
                      <td class="S10">S10</td>
                      <td class="S11">S11</td>
                      <td class="S12">S12</td>
                      <td class="S13">S13</td>
                      <td class="S14">S14</td>
                      <td class="S15">S15</td>
                      <td class="S16">S16</td>
                      <td class="S17" style="display: none;"></td>
                      <td class="S18" style="display: none;"></td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import axios from 'axios'
import {
  apiSelectNiaAlarmList, apiSelectNiaCableAlarmList, apiSelectNiaAbnormalTraffic2List,
  apiSelectNiaBadTraffic2List, apiSelectNiaBadTrafficList, apiSelectNiaAbnormalTrafficList,
  apiSelectNiaTopologyCableList, apiSelectNiaPfTopologyCableList, apiSelectNiaAgencyList
} from '@/api/nia'

const routeName = 'niaTopology'
const jsons = ['data_nia_ip_1.json', 'data_nia_ip_2.json', 'data_nia_ip_3.json']
const roadm_slots = {
    '192.168.200.213': ['MRPA-A', 'MRPA-A', 'MRSA-2B', 'MRSA-2A', 'OCPMA-4', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK'],
    '192.168.200.220': ['MRPA-A', 'MRPA-A', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC2A-L', 'OM2C2A-L', 'OM2C2A-L', 'OM24A', 'OM24A', 'OM24A', 'OM24A', 'BLK', 'BLK'],
    '192.168.200.215': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'MRSA-2B', 'OCPMA-4', 'BLK', 'OTUC1A-L', 'OTUC1A-L', 'BLK', 'BLK', 'OTUC1A-L', 'OTUC1A-L', 'BLK', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK'],
    '192.168.200.216': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'MRSA-2B', 'OCPMA-4', 'BLK', 'OTUC1A-L', 'OTUC1A-L', 'BLK', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK'],
    '192.168.200.218': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'MRSA-2A', 'OCPMA-4', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK'],
    '192.168.200.210': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'OCPMA-4', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC2A-L', 'OM2C2A-L', 'OM2C2A-L', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK']
}

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { },
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
      map: null,
      topologyType: 3,
      mapData: null,
      slot: [],
      agencyList: [],
      ticket: {},
      filteredAgencyList: [],

      ticketAlarmsType: 'RT',
      ticketPFAlarms: [],
      ticketRtAlarms: [],
      ticketAbnormalTrafficAlarms: [],
      ticketBadTrafficAlarms: [],
    }
  },
  computed: {
    sortedAgencyList() {
      return this.filteredAgencyList.slice().sort((a, b) =>
        a.nren_name.localeCompare(b.nren_name)
      )
    },
  },
  created() {
    this.selectedRow = this.wdata?.params

    const async = false
    this.addScript([
      './extlib/map2d/lib/index_nia_bundle.js'
    ], async)
  },
  mounted() {
    const ctrl = this
    ctrl.initMap()

    setTimeout(() => {
      this.onInit()
    }, 500)
  },
  methods: {
    isVisibleView() {
      return true
    },

    broadcastMenuEvent(param) {
      if (!param) return
        switch (param) {
          case 'properties':
              if (this.appOptions.debug) {
                  this.onMenuClick(param)
              }
              break
          case 'changeType':
              {
                const index = this.topologyType % jsons.length
                this.topologyType = index + 1
                const newFileName = jsons.at(index)
                newFileName && this.loadMapByFile(newFileName, true)
                console.log(`topologyType: ${this.topologyType}`)
              }
              break
          case 'reload':
              this.loadTicketAlarm()
              break
          case 'resetZoom':
              this.map.resetZoom(750)
              break
          case 'nodeZoomTest':
              {
                  const node = this.map.data.nodes.find(v => v.device_name === '수원성균관대')
                  this.map.zoomInByNode(node, 5, 1000)
              }
              break
          case 'linkZoomTest':
              {
                  const node = this.map.data.links.find(v => v.id === '44')
                  this.map.zoomInByLink(node, 5, 1000)
              }
              break
          case 'toggleLabel':
              {
                  const label = this.map.options.node.displayField === 'ip_addr' ? 'device_name' : 'ip_addr'
                  this.map.options.node.displayField = label
                  this.map.options.onChangeNodeDisplayField()
              }
              break
          case 'refresh':
              this.loadMapByFile(this.map.options.fileName, true)
              break
          case 'updateTopology':
              this.updateNodePosition()
              break
      }
    },

    async initMap() {
      const THIS = this
      this.loadNiaAgencyList()
      setTimeout(async() => {
        if (!window.Map2d) {
          THIS.initMap()
          return
        }

        const map = this.map = THIS.map = new window.Map2d()

        this.map.initialize()
        await this.loadMapByFile(jsons[this.topologyType - 1], true)

        map.addEventListener(window.Map2d.eventType.selectChanged, e => {
            console.log('selected changed : ', e)

            if (e.target_type === 'svg') {
                this.loadTemplate(null, 'svg')
            } else {
                this.displaySlotAlarm(this.map.select)
            }
        })
      }, 100)
    },

    onInit() {
      // document.getElementById('componentWapperId').appendChild(document.getElementById('template-3').content.cloneNode(true))
      document.getElementById('templateUnitArea').innerHTML = document.querySelector('#template-3').innerHTML
    },

    async loadNiaAgencyList() {
      const res = await apiSelectNiaAgencyList()
      if (res?.result) {
        const map = {}
        res.result.forEach((v, i) => {
          const array = map[v.node_id] || []
          array.push(v)
          map[v.node_id] = array
        })

        this.agencyList = { ...map }
      }
    },

    async loadMapByFile(fileName, isCompareData) {
      this.map.options.fileName = fileName
      return await this.loadMapData(fileName).then(async(data) => {
        if (isCompareData) {
            // 이전 코드
            // const result = await this.compareNiaData(data.nodes, data.links) || { }
            // data.nodes = result.nodes
            // data.links = result.linkData
        }
        await this.loadMap(data)
        this.loadTicketAlarm()
      })
    },

    async loadTicketAlarm(ticket) {
      let reload = false
      if (ticket == null) {
          ticket = this.ticket
          reload = true
      }

      if (ticket == null || Object.keys(ticket).length === 0) {
          return
      }

      this.clearTicketAlarm()
      if (reload || ticket && this.ticket !== ticket) {
          this.ticket = ticket

          var alarms = await this.loadNiaAlarmList(ticket)
          var alarmLink = await this.loadNiaCableAlarmList(ticket)

          var [alarmNode] = await this.updateBadgeCount(this.map.data, alarms)
          this.zoom(alarmLink, alarmNode)
          this.map.draw(false)
      }
    },

    updateBadgeCount (data, alarms) {
        const alarmCnt = {}
        let related_alarm = alarms.find(v => v.related_alarm === true)
        const alarmNodes = new Set()
        related_alarm = !related_alarm ? '' : related_alarm.sysname.split('-')[0]

        alarms.map(alarm => this.getAlarmSysname(alarm)).map(function (sysname) {
            if (sysname in alarmCnt) {
                alarmCnt[sysname]++
            } else {
                alarmCnt[sysname] = 1
            }
        })
        const key = Object.keys(alarmCnt)

        for (var i = 0; i < key.length; i++) {
            data.nodes.map(function (node) {
                if (node.id === key[i]) {
                    node.alarm_count = alarmCnt[key[i]]
                    alarmNodes.add(node)
                }
                if (node.id === related_alarm) {
                    node.related_alarm = true
                    alarmNodes.add(node)
                }
            })
        }

        return [...alarmNodes].sort(function (a, b) { a.alarm_count > b.alarm_count })
    },

    clearTicketAlarm() {
      const data = this.map.data
      data.nodes.forEach(node => {
          delete node.alarm_count
          delete node.related_alarm
      })

      data.links.forEach(node => {
          node.status = 1
      })

      this.map.draw(false)
    },

    async loadNiaAlarmList(ticket) {
      if (!ticket) return Promise.resolve()

      switch (ticket.ticket_type) {
        case 'RT':
          {
            const param = {
              CLUSTERNO: ticket.cluster_no,
            }
            const res = await apiSelectNiaAlarmList(param)
            this.ticketRtAlarms = res?.result
            this.ticketAlarmsType = 'RT'
            setTimeout(() => {
              // 이전 코드
              // $('table.alarm-table.rt-alarm').on('scroll', this.resize.bind(this))
            }, 1000)
            return res?.result
          }
        case 'PF':
          {
            const param = {
              TICKET_ID: ticket.ticket_id
            }
            const res = await apiSelectNiaCableAlarmList(param)
            this.ticketPFAlarms = res?.result
            this.ticketAlarmsType = 'PF'
            return res?.result
          }
        case 'ATT2':
          {
            const param = {
              TICKET_ID: ticket.ticket_id
            }
            const res = await apiSelectNiaAbnormalTraffic2List(param)
            this.ticketAbnormalTrafficAlarms = res?.result
            this.ticketAlarmsType = 'ABNORMAL_TRAFFIC'
            setTimeout(() => {
              // 이전 코드
              // $('table.alarm-table.traffic-abnormal-alarm').on('scroll', this.resize.bind(this))
            }, 1000)
            return res?.result
          }
        case 'NTT':
          {
            const param = {
              TICKET_ID: ticket.ticket_id
            }
            const res = await apiSelectNiaBadTraffic2List(param)
            this.ticketBadTrafficAlarms = res?.result
            this.ticketAlarmsType = 'BAD_TRAFFIC'
            setTimeout(() => {
                // 이전 코드
                // $('table.alarm-table.traffic-bad-alarm').on('scroll', this.resize.bind(this))
            }, 1000)
            return res?.result
          }
        case 'TRAFFIC':
          {
            const param = {
              TICKET_ID: ticket.ticket_id
            }
            const res = await ticket.ticket_rca_result_code === 'TRAFFIC_BAD_DETECTION' ? apiSelectNiaBadTrafficList(param) : apiSelectNiaAbnormalTrafficList(param)
            if (ticket.ticket_rca_result_code === 'TRAFFIC_BAD_DETECTION') {
              this.ticketBadTrafficAlarms = res?.result
              this.ticketAlarmsType = 'BAD_TRAFFIC'
              setTimeout(() => {
                  // 이전 코드
                  // $('table.alarm-table.traffic-bad-alarm').on('scroll', this.resize.bind(this))
              }, 1000)
            } else {
              this.ticketAbnormalTrafficAlarms = res?.result
              this.ticketAlarmsType = 'ABNORMAL_TRAFFIC'
              setTimeout(() => {
                        // 이전 코드
                        // $('table.alarm-table.traffic-abnormal-alarm').on('scroll', this.resize.bind(this))
              }, 1000)

              if (res?.result) {
                const [first] = res?.result
                if (first) {
                    this.zoomByAlarm(res?.result[0])
                } else {
                    this.map.resetZoom(750)
                }
              }
            }
            return res?.reuslt
          }
      }
    },

    zoomByAlarm(alarm) {
      const sysname = this.getAlarmSysname(alarm)
      const selector = document.getElementById('node_' + sysname)
      const invisible = selector.classList.contains('node_invisible')
      if (selector && !invisible) {
          // 이전 코드
          // const node = d3.select(selector).datum()
          // d3 라이브러리 없으므로 임시처리
          const node = this.d3.select(selector).datum()
          node && this.zoom(null, node)
      } else {
          this.tools.showToast('장비를 찾을 수 없습니다.', null, '알림', 1500)
      }
    },

    async loadNiaCableAlarmList(ticket) {
      const THIS = this
      if (this.ticket.ticket_type === 'RT') {
        const param = {
          TICKET_ID: ticket.ticket_id
        }
        const res = await apiSelectNiaTopologyCableList(param)
        const { root_cause_type, root_cause_sysnamea, root_cause_sysnamez } = this.ticket
        let causeLinks = []
        const data = this.map.data
        const sysnamea = root_cause_sysnamea.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
        const sysnamez = root_cause_sysnamez.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')

        if (res?.result && res.result.length > 0) {
            const temp = res.result.sort((a, b) => a.routenum - b.routenum).reduce((r, p, i) => {
                if (i < res.result.length - 1) {
                    r.push({ source_id: p.sysname, target_id: (res.result[i + 1] || {}).sysname })
                }
                return r
            }, [])

            causeLinks = temp.map(item => {
                let status = 0
                const link = data.links.find(v => v.source_id === item.source_id && v.target_id === item.target_id);
                (sysnamea && item.source_id === sysnamea) && status--
                (sysnamez && item.target_id === sysnamez) && status--
                Object.assign(link, { status })
                return link
            })
        } else {
          if (root_cause_type === 'Linkcut') {
              const links = data.links.filter(v =>
                  v.source_id === sysnamea && v.target_id === sysnamez ||
                  v.source_id === sysnamez && v.target_id === sysnamea)
              links.forEach(link => {
                  link.status = -1
              })
              causeLinks = [...links]
            }
          }

          causeLinks.sort((a, b) => a.status - b.status)
          return causeLinks[0]
        } else if (this.ticket.ticket_type === 'PF') {
          const param = {
            TICKET_ID: ticket.ticket_id
          }
          const res = apiSelectNiaPfTopologyCableList(param)
          const data = this.map.data
          const temp = res.result.map(v => ({ source_id: v.sysnamea.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1'), target_id: v.sysnamez.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1') }))
          const causeLinks = temp.map(item => {
              const link = data.links.find(v => v.source_id === item.source_id && v.target_id === item.target_id)
              Object.assign(link, { status: 0 })
              return link
          })
          return causeLinks[0]
        } else {
            const { root_cause_sysnamea, root_cause_sysnamez } = this.ticket

            let causeLinks = []
            const data = this.map.data
            const sysnamea = root_cause_sysnamea.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
            const sysnamez = root_cause_sysnamez.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
            const links = data.links.filter(v =>
                v.source_id === sysnamea && v.target_id === sysnamez ||
                v.source_id === sysnamez && v.target_id === sysnamea)
            links.forEach(link => { link.status = -1 })
            causeLinks = [...links]
            return causeLinks[0]
        }
    },

    async loadMapData(fileName) {
      try {
        const response = await axios.get(`/json/${fileName}`)
        this.mapData = response.data // 가져온 데이터를 mapData에 저장
        return this.mapData
      } catch (error) {
        console.error('Failed to load map data:', error)
      }
    },

    loadMap(data) {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          // this.tools.store.niaData = data
          console.log('load!!!')
          this.map.options.load(data)
          resolve()
        }, 100)
      })
    },

    async compareNiaData(nodes = [], links = []) {
      return Promise.all([this.requestNodes(), this.requestLinks(), nodes, links]).then((values) => {
          const [nodeData, linkData] = values
          linkData.map((link1) => {
              const length = linkData.filter((link2) => {
                  return (link1.source_id === link2.target_id && link1.target_id === link2.source_id && link1.source_pau === link2.target_pau && link1.target_pau === link2.source_pau)
              }).length
              link1.distance = (!link1.equip_type && length > 0) ? 1 : null
          })
          return { nodes: nodeData, linkData }
      })
    },

    requestNodes() {
      return new Promise((resolve, reject) => {
          const { tools } = this
          tools.http({
              service: tools.constants.Service.rca,
              action: 'SELECT_TOPOLOGY_NODE_LIST',
              TYPE: this.topologyType.toString()
          }, (result) => {
              if (result) { resolve(result) } else { reject(result) }
          })
      })
    },

    requestLinks() {
        return new Promise((resolve, reject) => {
            const { tools } = this
            tools.http({
                service: tools.constants.Service.rca,
                action: 'SELECT_TOPOLOGY_LINK_LIST'
            }, (result) => {
                if (result) { resolve(result) } else { reject(result) }
            })
        })
    },

    loadTemplate(select, click) {
      this.filteredAgencyList = []

      function show(id, visible) {
          document.getElementById(id).style.display = visible ? '' : 'none'
      }

      if (!select) {
          show('templateUnitArea', false)
          show('templateAgentListArea', false)
          return
      }

      const type = (click === 'node') ? select.d.device_type : select.equiptype || this.map.data.nodes.find(v => v.id === this.getAlarmSysname(select)).device_type

      // 수정해서 componentWapper 바로 밑에 properties가 있는 경우에만 삭제하도록 변경해야함
      // const componentWapper = this.$refs.componentWapper
      // const properties = componentWapper.querySelector('.properties')
      // if (properties) {
      //   properties.remove()
      // }

      if (type === '2' || type === 'POTN') {
          document.getElementById('templateUnitArea').innerHTML = document.querySelector('#template-2').innerHTML

          const elements = document.querySelectorAll('.potnSysname')
          elements.forEach(element => {
            element.innerHTML = this.click === 'node' ? this.select.d.id : this.select.sysname
          })

          show('templateUnitArea', true)
          show('templateAgentListArea', false)
      } else if (type === '0' || type === 'ROADM') {
          var roadmSlot
          document.getElementById('templateUnitArea').innerHTML = document.querySelector('#template-3').innerHTML
          if (click === 'node' && roadm_slots[select.d.id]) {
              roadmSlot = roadm_slots[select.d.id]
          } else if (click === 'alarm') {
              select = this.map.data.nodes.find(v => v.id === this.getAlarmSysname(select))
              roadmSlot = roadm_slots[select.id]
          }

          if (roadmSlot) {
              for (var i = 0; i < roadmSlot.length; i++) {
                  const cells = document.querySelectorAll('#templateUnitArea .table-wapper tr.slot-table > td')
                  cells.forEach((cell, i) => {
                    if (this.roadmSlot[i] !== undefined) {
                      cell.innerHTML = this.roadmSlot[i]
                    }
                  })
              }
              document.getElementsByClassName('S17')[0].style.display = roadmSlot.length < 17 ? 'none' : ''
              document.getElementsByClassName('S18')[0].style.display = roadmSlot.length < 17 ? 'none' : ''
          }
          const elements = document.querySelectorAll('.roadmSysname')
          elements.forEach(element => {
            element.innerHTML = this.click === 'node' ? this.select.d.id : this.select.id
          })
          show('templateUnitArea', true)
          show('templateAgentListArea', false)
      } else {
          const nodeId = (click === 'node') ? select.d.id : select.id
          const list = this.agencyList[nodeId] || []
          this.filteredAgencyList = [...list]
          // this.safeApply()

          show('templateUnitArea', false)
          show('templateAgentListArea', true)
      }
    },

    displaySlotAlarm(select) {
      this.slot = []
      const elements = document.querySelectorAll('.animation-blink')
      elements.forEach((el) => {
        el.classList.remove('animation-blink')
      })

      if (select.d.device_type) {
          this.loadTemplate(select, 'node')
      }
      const { ticket } = this
      if (ticket) {
          if (ticket.ticket_type === 'RT') {
              this.ticketRtAlarms.map(function (alarm) {
                  if (alarm.sysname.split('-')[0] === select.d.id && alarm.alarmloc) {
                      this.slot.push('.' + alarm.alarmloc.split('-')[0].replace('.', ''))
                  }
              })
          } else if (ticket.ticket_type === 'PF') {
              this.ticketPFAlarms.map(function (alarm) {
                  if (alarm.sysname.split('-')[0] === select.d.id && alarm.port) {
                      this.slot.push('.' + alarm.port.split('-')[0].replace('.', ''))
                  }
              })
          }
      }

      this.slot = this.slot.filter((v, i) => this.slot.indexOf(v) === i)
      return this.map.setInfomation('node', select.d, select.elements, this.slot)
    },

    getAlarmSysname(alarm) {
      if (alarm.equiptype === '7712/5812') {
          return alarm.sysname
      } else if (alarm.sysname.includes('n9k') || alarm.sysname.includes('control') || alarm.sysname.includes('cxp') || alarm.sysname.includes('asr9k')) {
          return alarm.sysname
      } else {
          return alarm.sysname.split('-')[0]
      }
    }
  },
}
</script>
<style lang="scss" scoped>
@import "./niaTopologyConfig/css/niaTopology_map3d.scss";
@import "./niaTopologyConfig/css/niaTopology_monitoring_tt.scss";
@import './niaTopologyConfig/css/niaTopology_newTicket.css';

.niaTopology{
  .tooltip-hover {
    position: relative;
    display: inline-block;
    border-bottom: 1px dotted black;
  }

  .tooltip-hover .tooltip-text {
    visibility: hidden;
    background-color: black;
    color: #fff;
    text-align: center;
    border-radius: 6px;
    padding: 0px 10px;
    font-size: 12px;
    width: auto;
    white-space: nowrap;

    /* Position the tooltip */
    position: absolute;
    z-index: 1;

    /* bottom */
    top: 100%;
    left: 50%;
    margin-left: -20px; /* Use half of the width (120/2 = 60), to center the tooltip */

  }

  .tooltip-hover:hover .tooltip-text {
    visibility: visible;
  }

  .invisible {
    display: none;
  }
}

</style>

<style>
/* #region [tooltip style]*/
.tooltip {
  line-height: 1;
  font-weight: bold;
  padding: 12px;
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  border-radius: 10px;
  z-index: 9999;
}

.tooltip strong.title {
  display: inline-block;
  width: 75px;
}
/* #endregion [tooltip style]*/
</style>
