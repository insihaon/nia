<template>
  <div :class="{ [name]: true, 'h-100': true }">
    <div layout="row" flex layout-fill ng-cloak>
      <div class="map-area" style="display: flex; flex-direction: column; width: 100%; height: 100%">
        <div class="underline" style="display: flex; flex-direction: column; flex-wrap: wrap">
          <div class="rightContent" style="z-index: 9999">
            <div class="ttSetting">
              <ul>
                <li v-if="isVisibleView('LAYERED')" class="refresh image_dark_Converter tooltip-hover" @click="broadcastMenuEvent('reload')">
                  <span class="tooltip-text">새로고침</span>
                </li>
                <li v-if="isVisibleView('LAYERED')" style="font-size: 20px" class="tooltip-hover" @click="broadcastMenuEvent('changeType')">
                  <font-awesome-icon style="color: gray" :icon="['fas', 'retweet']" />
                  <span class="tooltip-text">토폴로지 타입 변경</span>
                </li>
                <li v-if="isVisibleView('LAYERED')" style="font-size: 20px" class="tooltip-hover" @click="broadcastMenuEvent('resetZoom')">
                  <font-awesome-icon style="color: gray" :icon="['fas', 'globe']" />
                  <span class="tooltip-text">줌 전체보기</span>
                </li>
                <li v-if="isVisibleView('LAYERED')" class="nodeblink image_dark_Converter tooltip-hover" @click="broadcastMenuEvent('nodeZoomTest')">
                  <span class="tooltip-text">노드 줌인 테스트</span>
                </li>
                <li v-if="isDebug" style="font-size: 20px" class="tooltip-hover" @click="broadcastMenuEvent('updateTopology')">
                  <font-awesome-icon style="color: gray" :icon="['fas', 'save']" />
                  <span class="tooltip-text">저장</span>
                </li>
                <li v-if="isVisibleView('LAYERED')" class="traffic image_dark_Converter tooltip-hover" @click="broadcastMenuEvent('linkZoomTest')">
                  <span class="tooltip-text">링크 줌인 테스트</span>
                </li>
                <li v-if="isVisibleView('LAYERED')" class="nodetitle image_dark_Converter tooltip-hover" @click="broadcastMenuEvent('toggleLabel')">
                  <span class="tooltip-text">라벨 토글</span>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div layout="row" layout-sm="row" layout-fill="" style="position: relative">
          <div id="componentWapperId" ref="componentWapper" class="componentWapper" style="overflow: hidden; position: relative">
            <div class="background"></div>
            <svg id="topology_container" viewBox="0 0 1920 1080" class="jctx-host jctx-id-foo">
              <defs>
                <marker id="arrow" viewBox="0 0 10 10" orient="auto" markerWidth="3" markerHeight="3" refX="23" refY="5">
                  <path fill="red" d="M 0 0 L 10 5 L 0 10 z"></path>
                </marker>
              </defs>
            </svg>
            <div id="templateUnitArea"></div>
            <div id="templateAgentListArea" style="display: none">
              <div class="properties flex-column">
                <div class="flex-item site">
                  <div>이용기관</div>
                  <div class="table-box-wrap">
                    <div class="table-box">
                      <table>
                        <caption>
                          아래는 스크롤 테이블 입니다.
                        </caption>
                        <colgroup>
                          <col width="100px" />
                          <col width="200px" />
                          <col width="200px" />
                        </colgroup>
                        <thead></thead>
                        <tbody>
                          <tr>
                            <th scope="col" style="width: 100px">NO.</th>
                            <th scope="col" style="width: 200px">SITE</th>
                            <th scope="col" style="width: 200px">I/F</th>
                          </tr>
                          <tr v-for="(agency, index) in sortedAgencyList" :key="agency.nren_id" :class="{ 'animation-blink': paramTickets.some((t) => t.root_cause_sysnamez === agency.nren_name) }">
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

    <niaTopologyTemplalate />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import {
  apiSelectNiaAlarmList,
  apiSelectNiaCableAlarmList,
  apiSelectNiaAbnormalTraffic2List,
  apiSelectNiaBadTraffic2List,
  apiNTTTrafficChart,
  apiSelectNiaAbnormalTrafficList,
  apiSelectNiaTopologyCableList,
  apiSelectNiaPfTopologyCableList,
  apiSelectNiaAgencyList,
  apiSelectTopologyNodeList,
  apiSelectTopologyLinkList,
  apiUpdateNodePosition,
  apiIpAlarmList,
  apiSelectSyslogAlarmList,
} from '@/api/nia'
import niaTopologyTemplalate from './niaTopologyConfig/niaTopologyTemplate.vue'
import { mapState } from 'vuex'
import constants from '@/min/constants'
import { getAlarmFocusTicketData, getWindowActionList } from '@/views-nia/js/commonNiaFunction'
import nia_topology_data from '@/views-nia/dashBoard/niaTopologyConfig/json/nia_topology_data'
import niaObserverMixin from '@/mixin/niaObserverMixin'

const routeName = constants.nia.chatbotKeyMap.niaTopology.parameterKey
const roadm_slots = {
  '192.168.200.213': ['MRPA-A', 'MRPA-A', 'MRSA-2B', 'MRSA-2A', 'OCPMA-4', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK'],
  '192.168.200.220': ['MRPA-A', 'MRPA-A', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC2A-L', 'OM2C2A-L', 'OM2C2A-L', 'OM24A', 'OM24A', 'OM24A', 'OM24A', 'BLK', 'BLK'],
  '192.168.200.215': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'MRSA-2B', 'OCPMA-4', 'BLK', 'OTUC1A-L', 'OTUC1A-L', 'BLK', 'BLK', 'OTUC1A-L', 'OTUC1A-L', 'BLK', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK'],
  '192.168.200.216': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'MRSA-2B', 'OCPMA-4', 'BLK', 'OTUC1A-L', 'OTUC1A-L', 'BLK', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK'],
  '192.168.200.218': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'MRSA-2A', 'OCPMA-4', 'BLK', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK', 'BLK'],
  '192.168.200.210': ['MRPA-A', 'MRPA-A', 'MRSA-2A', 'OCPMA-4', 'OTUC1A-L', 'OTUC1A-L', 'OTUC1A-L', 'OTUC2A-L', 'OM2C2A-L', 'OM2C2A-L', 'OM24A', 'OM24A', 'BLK', 'BLK', 'BLK', 'BLK'],
}

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { niaTopologyTemplalate },
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
      map: null,
      topologyType: 3,
      mapData: null,
      slot: [],
      agencyList: [],
      paramTickets: {},
      paramShowFullTopology: false,
      filteredAgencyList: [],
      showNodeAllError: false, // 해당 Option을 true로 변경하면, 개별 ticket 장애에 대해서도 해당 노드에서 발생한 모든 경보의 총합을 표시

      ticketAlarmsType: 'RT',
      ticketPFAlarms: [],
      ticketRtAlarms: [],
      ticketAbnormalTrafficAlarms: [],
      ticketBadTrafficAlarms: [],
      query: {
        order: ['end_time', 'monitored_object'],
        limit: 500,
        page: 1,
      },
    }
  },
  computed: {
    sortedAgencyList() {
      return this.filteredAgencyList.slice().sort((a, b) => a.nren_name.localeCompare(b.nren_name))
    },

    isDebug() {
      return this.appOptions.debug
    },
    chatbotKeyMap() {
      return constants.nia.chatbotKeyMap
    },
    chatbotCommand() {
      return constants.nia.chatbotCommand
    },
    ...mapState({
      topologyEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.niaTopology.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },
  },
  watch: {
    topologyEventText(nVal, oVal) {
      if (this.isModal) {
        switch (nVal) {
          case this.chatbotCommand.refresh.action:
            this.broadcastMenuEvent('reload')
            break
          case this.chatbotCommand.topologyTypeChange.action:
            this.broadcastMenuEvent('changeType')
            break
          case this.chatbotCommand.wholeZoom.action:
            this.broadcastMenuEvent('resetZoom')
            break
          case this.chatbotCommand.nodeZoomTest.action:
            this.broadcastMenuEvent('nodeZoomTest')
            break
          case this.chatbotCommand.save.action:
            this.broadcastMenuEvent('updateTopology')
            break
          case this.chatbotCommand.linkZoomTest.action:
            this.broadcastMenuEvent('linkZoomTest')
            break
          case this.chatbotCommand.labelToggle.action:
            this.broadcastMenuEvent('toggleLabel')
            break
        }

        if (!nVal) {
          this.popupShowCommand()
        }

        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: this.chatbotKeyMap.niaTopology.parameterKey })
      }
    },
  },
  created() {
    const keys = Object.keys(this.wdata?.params)
    const isAllNumericKeys = keys.every((key, index) => {
      return String(index) === key
    })

    this.paramTickets = isAllNumericKeys ? Object.values(this.wdata?.params) : [this.wdata?.params]
    this.paramShowFullTopology = isAllNumericKeys

    const async = false
    this.addScript(['./extlib/map2d/lib/index_nia_bundle.js'], async)
  },
  async mounted() {
    await this.setTicketDataForAlarmFocusTicketData()

    setTimeout(() => {
      this.onInit()
      this.popupShowCommand()
    }, 500)
  },
  methods: {
    async setTicketDataForAlarmFocusTicketData(isChatbotGenerated) {
      if (isChatbotGenerated) this.wdata.params.isChatbotGenerated = isChatbotGenerated
      const ticketData = await getAlarmFocusTicketData(this.wdata)
      if (ticketData) {
        this.paramTickets = [ticketData]
        this.paramShowFullTopology = false
        this.$emit('update:wdataParams', ticketData)
      }

      // this.topologyConstruct()
      this.initMap()
    },

    async popupShowCommand() {
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: await getWindowActionList(constants.nia.chatbotKeyMap.niaTopology.dialogNm, constants.nia.chatbotKeyMap.niaTopology.popupName),
        })
      }
    },
    // topologyConstruct() {
    //   const defaultMapFile = jsons[this.topologyType - 1] || 'data_nia_ip_3.json'
    //   if (!jsons.includes(localStorage['last_map'])) {
    //     localStorage['last_map'] = defaultMapFile
    //   }
    // },

    isVisibleView() {
      return true
    },

    broadcastMenuEvent(param) {
      if (!param) return
      switch (param) {
        case 'changeType':
          {
            const index = this.topologyType % 3
            this.topologyType = index + 1
            this.loadMapByFile()
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
            const node = this.map.data.nodes.find((v) => v.device_name === '수원성균관대')
            this.map.zoomInByNode(node, 5, 1000)
          }
          break
        case 'linkZoomTest':
          {
            const node = this.map.data.links.find((v) => String(v.id) === '44')
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
          this.loadMapByFile()
          break
        case 'updateTopology':
          this.updateNodePosition()
          break
      }
    },

    updateNodePosition() {
      this.$confirm('토폴로지를 저장하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
      }).then(async () => {
        const TYPE = this.topologyType.toString()
        const NODES = this.map.getVisualNodeAll().map((n) => {
          return {
            node_type: n.device_type,
            node_id: n.id,
            node_image_path: n.image.path,
            fx: Math.floor(n.x || n.fx),
            fy: Math.floor(n.y || n.fy),
            topology_type: TYPE,
          }
        })

        const param = {
          TYPE: TYPE,
          NODES: NODES,
        }
        const res = await apiUpdateNodePosition(param)
        if (res.result === 1) {
          this.$message.success({ message: `저장에 성공하였습니다.` })
        } else {
          this.$message.success({ message: `저장에 실패하였습니다.` })
        }
      })
    },

    async initMap() {
      const THIS = this
      this.loadNiaAgencyList()
      setTimeout(async () => {
        if (!window.Map2d) {
          THIS.initMap()
          return
        }

        const map = (this.map = THIS.map = new window.Map2d())
        this.map.initialize()

        await this.loadMapByFile()

        // 이벤트 리스너 추가
        map.addEventListener(window.Map2d.eventType.selectChanged, (e) => {
          console.log('selected changed : ', e)

          if (e.target_type === 'svg') {
            // 배경클릭
            this.loadTemplate(null, 'svg')
          } else {
            this.displaySlotAlarm(this.map.select)
          }
        })
      }, 100)
    },

    onInit() {
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

    async loadMapByFile() {
      const configData = nia_topology_data['config' + this.topologyType]
      const result = (await this.compareNiaData()) || {}
      await this.loadMap({
        nodes: result.nodes,
        links: result.linkData,
        config: configData,
      })
      await this.loadTicketAlarm()
    },

    getEquipType(strResName) {
      if (strResName.endsWith('-7712')) {
        return '7712/5812'
      } else if (strResName.endsWith('-5812')) {
        return '7712/5812'
      } else if (strResName.endsWith('-n9k')) {
        return 'ngk'
      } else {
        return ''
      }
    },

    async loadTicketAlarm() {
      this.clearTicketAlarm()

      const res = await apiIpAlarmList()
      const allTicket = res.result
      const totalAlarms = allTicket.map((t) => {
        t.sysname = t.node_nm
        if (t.sysname) {
          t.equiptype = this.getEquipType(t.sysname)
        }
        return t
      })

      if (!this.paramShowFullTopology) {
        // 개별 경보 더블 클릭
        if (!this.paramTickets || this.paramTickets.length === 0) throw new Error('no Ticket')
        var [alarms] = await this.loadNiaAlarmList(this.paramTickets[0])
        var alarmLink = await this.loadNiaCableAlarmList(this.paramTickets[0])

        var alarmNode = null
        if (this.showNodeAllError) {
          const filterAlarms = totalAlarms.filter((a) => a.sysname === alarms.sysname)
          alarmNode = await this.updateBadgeCount(this.map.data, filterAlarms)
        } else {
          alarmNode = await this.updateBadgeCount(this.map.data, [alarms])
        }

        setTimeout(() => {
          this.zoom(alarmLink, alarmNode[0])
          this.map.draw(false)
        }, 1000)
      } else {
        // 토폴로지 전체보기
        await this.updateBadgeCount(this.map.data, totalAlarms)
        this.map.draw(false)
      }
    },

    zoom(alarmLink, alarmNode) {
      setTimeout(() => {
        if (alarmLink) {
          this.map.selectElement(alarmLink, 'link', true)
          this.map.zoomInByLink(alarmLink, 5, 750)
        } else if (alarmNode && alarmNode.visible) {
          this.map.selectElement(alarmNode, 'node', true)
          this.map.zoomInByNode(alarmNode, 5, 750)
          this.map.setInfomation('node', alarmNode, null, this.slot)
        } else {
          this.map.resetZoom(750)
        }
      }, 200)
    },

    resize() {
      // rt-alarm 테이블
      const rtAlarmTable = document.querySelector('table.alarm-table.rt-alarm')
      if (rtAlarmTable) {
        const rtAlarmTableChildren = rtAlarmTable.children
        for (const child of rtAlarmTableChildren) {
          child.style.width = `${rtAlarmTable.offsetWidth + rtAlarmTable.scrollLeft}px`
        }
      }

      // traffic-abnormal-alarm 테이블
      const trafficAbnormalTable = document.querySelector('table.alarm-table.traffic-abnormal-alarm')
      if (trafficAbnormalTable) {
        const trafficAbnormalChildren = trafficAbnormalTable.children
        for (const child of trafficAbnormalChildren) {
          child.style.width = `${trafficAbnormalTable.offsetWidth + trafficAbnormalTable.scrollLeft}px`
        }
      }

      // traffic-bad-alarm 테이블
      const trafficBadTable = document.querySelector('table.alarm-table.traffic-bad-alarm')
      if (trafficBadTable) {
        const trafficBadChildren = trafficBadTable.children
        for (const child of trafficBadChildren) {
          child.style.width = `${trafficBadTable.offsetWidth + trafficBadTable.scrollLeft}px`
        }
      }
      this.clickAlarmExpandBtn(null)
    },

    updateBadgeCount(data, alarms) {
      const alarmCnt = {}
      const syslogCnt = {}
      let related_alarm = alarms.find((v) => v.related_alarm === true)
      const alarmNodes = new Set()
      related_alarm = !related_alarm ? '' : related_alarm.sysname.split('-')[0]

      const syslogAlarm = alarms.filter((v) => v.ticket_type === 'SYSLOG')
      const notSyslogAlarm = alarms.filter((v) => v.ticket_type !== 'SYSLOG')

      notSyslogAlarm
        .map((alarm) => this.getAlarmSysname(alarm))
        .map(function (sysname) {
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

      // syslogCnt
      syslogAlarm
        .map((alarm) => this.getAlarmSysname(alarm))
        .map(function (sysname) {
          if (sysname in syslogCnt) {
            syslogCnt[sysname]++
          } else {
            syslogCnt[sysname] = 1
          }
        })
      const key2 = Object.keys(syslogCnt)

      for (var j = 0; j < key2.length; j++) {
        data.nodes.map(function (node) {
          if (node.id === key2[j]) {
            node.syslog_count = syslogCnt[key2[j]]
            alarmNodes.add(node)
          }
        })
      }

      return [...alarmNodes].sort(function (a, b) {
        a.alarm_count > b.alarm_count
      })
    },

    clearTicketAlarm() {
      const data = this.map.data
      data.nodes.forEach((node) => {
        delete node.alarm_count
        delete node.related_alarm
      })

      data.links.forEach((node) => {
        node.status = 1
      })

      this.map.draw(false)
    },

    async loadNiaAlarmList(ticket) {
      if (!ticket) throw new Error('not Ticket')

      switch (ticket.ticket_type) {
        case 'RT': {
          const param = {
            CLUSTERNO: ticket.clusterno,
            ALARMNO: ticket.alarmno,
          }
          const res = await apiSelectNiaAlarmList(param)
          this.ticketRtAlarms = res?.result
          this.ticketAlarmsType = 'RT'
          setTimeout(() => {
            const rtAlarmTable = document.querySelector('table.alarm-table.rt-alarm')
            if (rtAlarmTable) {
              rtAlarmTable.addEventListener('scroll', this.resize.bind(this))
            }
          }, 1000)
          if (res.result.length > 1) throw new Error('하나의 티켓에 경보는 하나여야합니다.')
          return res?.result
        }
        case 'PF': {
          const param = {
            TICKET_ID: ticket.ticket_id,
          }
          const res = await apiSelectNiaCableAlarmList(param)
          this.ticketPFAlarms = res?.result
          this.ticketAlarmsType = 'PF'
          if (res.result.length > 1) throw new Error('하나의 티켓에 경보는 하나여야합니다.')
          return res?.result
        }
        case 'ATT2': {
          const param = {
            TICKET_ID: ticket.ticket_id,
          }
          const res = await apiSelectNiaAbnormalTraffic2List(param)
          this.ticketAbnormalTrafficAlarms = res?.result
          this.ticketAlarmsType = 'ABNORMAL_TRAFFIC'
          setTimeout(() => {
            const trafficAbnormalTable = document.querySelector('table.alarm-table.traffic-abnormal-alarm')
            if (trafficAbnormalTable) {
              trafficAbnormalTable.addEventListener('scroll', this.resize.bind(this))
            }
          }, 1000)
          if (res.result.length > 1) throw new Error('하나의 티켓에 경보는 하나여야합니다.')
          return res?.result
        }
        case 'NTT': {
          const param = {
            TICKET_ID: ticket.ticket_id,
          }
          const res = await apiSelectNiaBadTraffic2List(param)
          this.ticketBadTrafficAlarms = res?.result
          this.ticketAlarmsType = 'BAD_TRAFFIC'
          setTimeout(() => {
            const trafficBadTable = document.querySelector('table.alarm-table.traffic-bad-alarm')
            if (trafficBadTable) {
              trafficBadTable.addEventListener('scroll', this.resize.bind(this))
            }
          }, 1000)
          if (res.result.length > 1) throw new Error('하나의 티켓에 경보는 하나여야합니다.')
          return res?.result
        }
        case 'TRAFFIC': {
          // 사실상 의미없음 사용될 일이 없음
          const param = {
            TICKET_ID: ticket.ticket_id,
          }

          const res = (await ticket.ticket_rca_result_code) === 'TRAFFIC_BAD_DETECTION' ? apiNTTTrafficChart(param) : apiSelectNiaAbnormalTrafficList(param)
          if (ticket.ticket_rca_result_code === 'TRAFFIC_BAD_DETECTION') {
            this.ticketBadTrafficAlarms = res?.result
            this.ticketAlarmsType = 'BAD_TRAFFIC'
            setTimeout(() => {
              const trafficBadTable = document.querySelector('table.alarm-table.traffic-bad-alarm')
              if (trafficBadTable) {
                trafficBadTable.addEventListener('scroll', this.resize.bind(this))
              }
            }, 1000)
          } else {
            this.ticketAbnormalTrafficAlarms = res?.result
            this.ticketAlarmsType = 'ABNORMAL_TRAFFIC'
            setTimeout(() => {
              const trafficAbnormalTable = document.querySelector('table.alarm-table.traffic-abnormal-alarm')
              if (trafficAbnormalTable) {
                trafficAbnormalTable.addEventListener('scroll', this.resize.bind(this))
              }
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
          if (res.result.length > 1) throw new Error('하나의 티켓에 경보는 하나여야합니다.')
          return res?.reuslt
        }
        case 'SYSLOG': {
          const param = {
            ALARMNO: ticket.alarmno,
          }
          const res = await apiSelectSyslogAlarmList(param)
          if (res.result.length > 1) throw new Error('하나의 티켓에 경보는 하나여야합니다.')
          return res?.result
        }
      }
    },

    zoomByAlarm(alarm) {
      const sysname = this.getAlarmSysname(alarm)
      const selector = document.getElementById('node_' + sysname)
      const invisible = selector.classList.contains('node_invisible')
      if (selector && !invisible) {
        const node = document.querySelector(selector)
        const data = node ? node.__data__ : null
        data && this.zoom(null, data)
      } else {
        this.$message.error({ message: `장비를 찾을 수 없습니다.` })
      }
    },

    async loadNiaCableAlarmList(ticket) {
      if (ticket.ticket_type === 'RT') {
        const param = {
          TICKET_ID: ticket.ticket_id,
        }
        const res = await apiSelectNiaTopologyCableList(param)
        const { root_cause_type, root_cause_sysnamea, root_cause_sysnamez } = ticket
        let causeLinks = []
        const data = this.map.data
        let sysnamea = null
        if (root_cause_sysnamea) {
          sysnamea = root_cause_sysnamea.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
        }

        let sysnamez = null
        if (root_cause_sysnamez) {
          sysnamez = root_cause_sysnamez.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
        }

        if (res?.result && res.result.length > 0) {
          const temp = res.result
            .sort((a, b) => a.routenum - b.routenum)
            .reduce((r, p, i) => {
              if (i < res.result.length - 1) {
                r.push({ source_id: p.sysname, target_id: (res.result[i + 1] || {}).sysname })
              }
              return r
            }, [])

          causeLinks = temp.map((item) => {
            let status = 0
            const link = data.links.find((v) => v.source_id === item.source_id && v.target_id === item.target_id)
            sysnamea && item.source_id === sysnamea && status--
            sysnamez && item.target_id === sysnamez && status--
            Object.assign(link, { status })
            return link
          })
        } else {
          if (root_cause_type === 'Linkcut') {
            const links = data.links.filter((v) => (v.source_id === sysnamea && v.target_id === sysnamez) || (v.source_id === sysnamez && v.target_id === sysnamea))
            links.forEach((link) => {
              link.status = -1
            })
            causeLinks = [...links]
          }
        }

        causeLinks.sort((a, b) => a.status - b.status)
        return causeLinks[0]
      } else if (ticket.ticket_type === 'PF') {
        const param = {
          TICKET_ID: ticket.ticket_id,
        }
        const res = apiSelectNiaPfTopologyCableList(param)
        const data = this.map.data
        const temp = res.result.map((v) => ({ source_id: v.sysnamea.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1'), target_id: v.sysnamez.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1') }))
        const causeLinks = temp.map((item) => {
          const link = data.links.find((v) => v.source_id === item.source_id && v.target_id === item.target_id)
          Object.assign(link, { status: 0 })
          return link
        })
        return causeLinks[0]
      } else {
        const { root_cause_sysnamea, root_cause_sysnamez } = ticket

        let causeLinks = []
        const data = this.map.data
        let sysnamea = null
        if (root_cause_sysnamea) {
          sysnamea = root_cause_sysnamea.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
        }
        let sysnamez = null
        if (root_cause_sysnamez) {
          sysnamez = root_cause_sysnamez.replace(/(\d+\.\d+\.\d+\.\d+)-\w+/gi, '$1')
        }
        const links = data.links.filter((v) => (v.source_id === sysnamea && v.target_id === sysnamez) || (v.source_id === sysnamez && v.target_id === sysnamea))
        links.forEach((link) => {
          link.status = -1
        })
        causeLinks = [...links]
        return causeLinks[0]
      }
    },

    loadMap(data) {
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          console.log('load!!!')
          this.map.options.load(data)
          resolve()
        }, 100)
      })
    },

    async compareNiaData(nodes = [], links = []) {
      return Promise.all([this.requestNodes(), this.requestLinks()]).then((values) => {
        const [nodeData, linkData] = values
        linkData.map((link1) => {
          const length = linkData.filter((link2) => {
            return link1.source_id === link2.target_id && link1.target_id === link2.source_id && link1.source_pau === link2.target_pau && link1.target_pau === link2.source_pau
          }).length
          link1.distance = !link1.equip_type && length > 0 ? 1 : null
        })
        return { nodes: nodeData, linkData }
      })
    },

    async requestNodes() {
      const param = {
        TYPE: this.topologyType.toString(),
      }
      const res = await apiSelectTopologyNodeList(param)
      return new Promise((resolve, reject) => {
        if (res?.result) {
          resolve(res?.result)
        } else {
          reject(res?.result)
        }
      })
    },

    async requestLinks() {
      const res = await apiSelectTopologyLinkList()
      return new Promise((resolve, reject) => {
        if (res?.result) {
          resolve(res?.result)
        } else {
          reject(res?.result)
        }
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

      const type = click === 'node' ? select.d.device_type : select.equiptype || this.map.data.nodes.find((v) => v.id === this.getAlarmSysname(select)).device_type

      const componentWapper = this.$refs.componentWapper
      Array.from(componentWapper.children).forEach((child) => {
        if (child.classList.contains('properties')) {
          child.remove()
        }
      })

      switch (type) {
        case '2':
        case 'POTN':
          document.getElementById('templateUnitArea').innerHTML = document.querySelector('#template-2').innerHTML

          document.querySelectorAll('.potnSysname').forEach((element) => {
            element.innerHTML = click === 'node' ? select.d.id : select.sysname
          })

          show('templateUnitArea', true)
          show('templateAgentListArea', false)
          break
        case '0':
        case 'ROADN':
          var roadmSlot
          document.getElementById('templateUnitArea').innerHTML = document.querySelector('#template-3').innerHTML
          if (click === 'node' && roadm_slots[select.d.id]) {
            roadmSlot = roadm_slots[select.d.id]
          } else if (click === 'alarm') {
            select = this.map.data.nodes.find((v) => v.id === this.getAlarmSysname(select))
            roadmSlot = roadm_slots[select.id]
          }

          if (roadmSlot) {
            for (var i = 0; i < roadmSlot.length; i++) {
              const cells = document.querySelectorAll('#templateUnitArea .table-wapper tr.slot-table > td')
              cells.forEach((cell, i) => {
                if (roadmSlot[i] !== undefined) {
                  cell.innerHTML = roadmSlot[i]
                }
              })
            }
            document.getElementsByClassName('S17')[0].style.display = roadmSlot.length < 17 ? 'none' : ''
            document.getElementsByClassName('S18')[0].style.display = roadmSlot.length < 17 ? 'none' : ''
          }
          document.querySelectorAll('.roadmSysname').forEach((element) => {
            element.innerHTML = click === 'node' ? select.d.id : select.id
          })
          show('templateUnitArea', true)
          show('templateAgentListArea', false)
          break
        default:
          // prettier-ignore
          (() => {
            const nodeId = click === 'node' ? select.d.id : select.id
            const list = this.agencyList[nodeId] || []
            this.filteredAgencyList = [...list]

            show('templateUnitArea', false)
            show('templateAgentListArea', true)
          })()
          break
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
      if (alarm.sysname == null) {
        return null
      }

      try {
        if (alarm.equiptype === '7712/5812') {
          return alarm.sysname
        } else if (alarm.sysname.includes('n9k') || alarm.sysname.includes('control') || alarm.sysname.includes('cxp') || alarm.sysname.includes('asr9k')) {
          return alarm.sysname
        } else {
          return alarm.sysname.split('-')[0]
        }
      } catch (e) {
        console.error(e)
      }
    },
  },
}
</script>
<style lang="scss" scoped>
@import './niaTopologyConfig/css/niaTopology_map3d.scss';
@import './niaTopologyConfig/css/niaTopology_monitoring_tt.scss';
@import './niaTopologyConfig/css/niaTopology_newTicket.css';

.niaTopology {
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
