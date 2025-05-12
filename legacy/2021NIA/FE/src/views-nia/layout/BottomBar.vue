<template>
  <div :class="{ [name]: true }">
    <div class="alarm-container flex flex-nowrap ml-3">
      <div v-if="isViewport('>', 'md')" class="alarm-title flex items-center flex-nowrap whitespace-nowrap mx-2" style="min-width: 93px;">
        <img src="@/assets/icon/attention.png" style="height: 22px;width: 22px;filter: contrast(0) brightness(2)">
        <span class="text-lg font-bold pl-1 pt-1">경보 현황</span>
      </div>
      <div class="alarm-content flex flex-nowrap items-center ml-2 text-sm">
        <span class="pt-1 whitespace-nowrap">IP Alarm</span>
        <span class="text-3xl text-red-600 font-bold mx-1">{{ $store.state.nia.ipNetworkList.length }}</span>
        <span class="pt-1">건</span>
        <el-divider direction="vertical" />
        <span class="pt-1 whitespace-nowrap">전송 Alarm</span>
        <span class="text-3xl text-red-600 font-bold mx-1">{{ $store.state.nia.transmissionNetworkList.length }}</span>
        <span class="pt-1">건</span>
      </div>
      <div class="alarm-count-list flex flex-nowrap ml-4 pt-1 text-base">
        <div class="pt-1 font-bold text-base" style="white-space: nowrap; overflow: hidden;">시스템 현황 모니터링</div>
        <div class="ml-2 d-flex font-bold items-center gap-x-2">
          <div
            v-for="system in niaProcess"
            :key="system.name"
            :style="{ color: system.status }"
            class="system-tooltip"
            style="border-right: solid 1px #ffffff82;padding-right: 5px; white-space: nowrap;"
          >
            <div :class="{ blinking: system.status !== 'lime' }">{{ system.name }}</div>
            <div class="tooltip-text" v-html="system.tooltip" />
          </div>
        </div>
      </div>
    </div>
    <div class="notice-container flex items-center mr-4 text-base pt-1">
      <div v-show="$store.getters.windows.length > 0" class="statusBarWrap mr-3">
        <el-menu mode="horizontal" class="statusBarMenu">
          <el-submenu index="0" popper-class="statusBarMenuNiaBottomBar">
            <template slot="title">
              <i class="el-icon-files" /> <span>창목록</span><span class="badgeItem">{{ $store.getters.windows.length }}</span>
            </template>
            <template v-for="window in $store.getters.windows">
              <el-menu-item
                :key="window.id"
                :class="{ focusWindow: window.windowState !== 'minimize' && window.zindex == 1000 }"
                class="statusBarWindowItem"
                @click="windowSelection(window)"
              >
                <!-- :style="{'background-color': window.windowState !== 'minimize' && window.zindex === 1000 ? 'rgb(30, 41, 59) !important' : '#fff !important'}" -->
                <i class="el-icon-document" /> <span>{{ window.name }}</span>
                <!-- <i v-show="window.windowState !== 'minimize' && window.zindex === 1000" class="el-icon-view" /> -->
                <i class="el-icon-close statusBarWindowItemCloseBtn" @click="$store.dispatch('mdi/removeWindow', window.id)" />
              </el-menu-item>
            </template>
          </el-submenu>
        </el-menu>
      </div>
      <div v-if="isViewport('>', 'md')" class="notice-title flex items-center">
        <el-tooltip class="item" effect="light" content="서비스 준비중입니다." placement="top">
          <i class="el-icon-s-tools m_icon" />
        </el-tooltip>
      </div>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'BottomBar'
import { AppOptions } from '@/class/appOptions'
import { apiSystemMonitoring } from '@/api/nia'

export default {
  name: routeName,
  src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
  components: {},
  extends: Base,
  data() {
    return {
      name: routeName,
      totalAlarmCnt: 0,
      alarmLevel: [],
      filterGroup: '',
      profileCount: { TELECOM: 0, CRISIS: 0, LTE: 0, FIVEG: 0, VIP: 0 },
      alarmList: [
        { name: 'CRITICAL', count: 3, color: 'red' },
        { name: 'MAJOR', count: 8, color: 'orange' },
        { name: 'MINOR', count: 11, color: 'yellow' },
        { name: 'WARNING', count: 20, color: 'skyblue' },
      ],

      niaProcess: {
        /*
          ai가 붙은 것은 전송주기를 파악하며 collection이 있으면 수집주기 파악용으로 사용.
          수집, 전송 주기 max초과 시 연동이상으로 판단한다.
        */

        ipSdnTrafficeKey: {
          name: 'LinkTraffic', status: 'red', cycle: 5 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : 수집 실패<br>
            <span style="color:orange">주황</span> : 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
          secondStep: {
            key: 'aiIpSdnTrafficeKey',
            cycle: 5 * 60 * 1000,
          }
        },
        ipSdnSflowKey: {
          name: 'Sflow', status: 'red', cycle: 10 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : 수집 실패<br>
            <span style="color:orange">주황</span> : 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
          secondStep: {
            key: 'aiIpSdnSflowKey',
            cycle: 10 * 60 * 1000,
          }
        },
        ipSdnSyslogKey: {
          name: 'Syslog', status: 'red', cycle: 10 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : 수집 실패<br>
            <span style="color:orange">주황</span> : 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
          secondStep: {
            key: 'aiIpSdnSyslogKey',
            cycle: 10 * 60 * 1000,
          }
        },
        ipSdnNodeFactorKey: {
          name: 'NodeFactor', status: 'red', cycle: 10 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : 수집 실패<br>
            <span style="color:orange">주황</span> : 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
          secondStep: {
            key: 'aiIpSdnNodeFactorKey',
            cycle: 10 * 60 * 1000,
          }
        },
        aiTrafficNoxKey: {
          name: 'AI_Traffic_유해', status: 'red', cycle: 5 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : AI 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
        },
        aiTrafficAnoKey: {
          name: 'AI_Traffic_이상', status: 'red', cycle: 5 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : AI 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
        },
        ipPerfKey: {
          name: 'IPSDN_Perf', status: 'red', cycle: 10 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : 수집 실패<br>
            <span style="color:orange">주황</span> : 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
          secondStep: {
            key: 'aiIpPerfKey',
            cycle: 10 * 60 * 1000,
          }
        },
        ipResourceIfKey: {
          name: 'IPSDN_ResourceIf', status: 'red', cycle: this.moment().set({ hour: 0, minute: 15, second: 0, millisecond: 0 }), // 매일 2시 40분
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : 수집 실패 <br>
            <span style="color:orange">주황</span> : 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
          secondStep: {
            key: 'aiIpResourceIfKey',
            cycle: this.moment().set({ hour: 2, minute: 40, second: 0, millisecond: 0 })
          }
        },
        ipResourceKey: {
          name: 'IPSDN_Resource', status: 'red', cycle: this.moment().set({ hour: 0, minute: 5, second: 0, millisecond: 0 }), // 매일 2시 30분
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : 수집 실패<br>
            <span style="color:orange">주황</span> : 연동 실패<br>
            <span style="color:lime">초록</span> : AI 연동 성공
            </div>
          `,
          secondStep: {
            key: 'aiIpResourceKey',
            cycle: this.moment().set({ hour: 2, minute: 30, second: 0, millisecond: 0 })
          }
        },
        emsSipcKey: {
          name: 'EMS_SIPC', status: 'red', cycle: 18 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : EMS SIPC 수집 실패<br>
            <span style="color:lime">초록</span> : EMS SIPC 수집 성공
            </div>
          `,
        },
        emsPmKey: {
          name: 'EMS_PM', status: 'red', cycle: 18 * 60 * 1000,
          tooltip: `
            <div style="font-size: 13px; text-align: left">
            <span style="color:red">빨강</span> : EMS PM 수집 실패<br>
            <span style="color:lime">초록</span> : EMS PM 수집 성공
            </div>
          `,
        },
      },
      monitoringInterval: null
    }
  },
  mounted() {
    this.getSystemMonitoringData()
    this.setIntervalGetSystemMonitoringData()
    this.subscribeEvent()
    window.bbar = Object.assign(window.bbar || {}, { header: this })
  },
  beforeDestroy() {
    clearInterval(this.monitoringInterval)
    // this.removeWsEventListener(this.CONSTANTS.channels.SYSTEM_MONITORING.name, this.onReceivedSystemMonitoring)
  },
  methods: {
    setIntervalGetSystemMonitoringData() {
      this.monitoringInterval = setInterval(() => {
        this.getSystemMonitoringData()
      }, 3 * 60 * 1000)
    },

    subscribeEvent() {
      // this.addWsEventListener(this.CONSTANTS.channels.SYSTEM_MONITORING.name, this.onReceivedSystemMonitoring)
    },
    // onReceivedSystemMonitoring({ channelName, socketMessage }) {
    //   if (channelName !== 'SYSTEM_MONITORING') return

    //   const data = JSON.parse(socketMessage.message)
    //   AppOptions.instance.useWsLog && this.log('RECEIVED SIBSCRIBE SYSTEM_MONITORING EVENT: ', data)
    //   this.setCurrentProcess(data)
    // },
    async getSystemMonitoringData() {
      try {
        const res = await apiSystemMonitoring()
        this.setCurrentProcess(res.result)
      } catch (error) {
        this.error(error)
      }
    },
    setCurrentProcess(processList) {
      for (const d of processList) {
        if (this.niaProcess[d.key_name]) {
          this.niaProcess[d.key_name]['firstTime'] = d.yd_date ?? null
        } else {
          const modifiedKeyName = d.key_name.slice(2)
          const secondKey = modifiedKeyName.charAt(0).toLowerCase() + modifiedKeyName.slice(1)
          if (secondKey in this.niaProcess) {
            if ('secondStep' in this.niaProcess[secondKey]) {
              this.niaProcess[secondKey]['secondTime'] = d.yd_date ?? null
              this.niaProcess[secondKey]['secondCycle'] = this.niaProcess[secondKey]['secondStep'].cycle
            } else {
              throw new Error('firstStep, secondStep이 둘다 없는 Process는 에러입니다.')
            }
          }
        }
      }

      const monitoringKeys = Object.keys(this.niaProcess)
      for (const key of monitoringKeys) {
        this.niaProcess[key].status = this.checkKeyStatus(this.niaProcess[key])
      }
    },
    checkKeyStatus(processObj) {
      const { cycle: firstCycle, secondCycle, secondTime, firstTime } = processObj
      const currentDate = new Date()
      const firstDate = firstTime ? new Date(firstTime) : null
      const secondDate = secondTime ? new Date(secondTime) : null

      if (typeof firstCycle !== 'number') {
        const dayDiff = firstCycle.diff(firstTime, 'days')
        const miniteDiff = firstCycle.diff(firstTime, 'minutes')

        if (dayDiff <= 1 && miniteDiff <= 10) {
          const secondDayDiff = secondCycle.diff(secondTime, 'days')
          const secondMiniteDiff = secondCycle.diff(secondTime, 'minutes')
          return secondDayDiff > 1 || secondMiniteDiff > 10 ? 'orange' : 'lime'
        } else {
          return 'red'
        }

        // if (dayDiff > 1 || miniteDiff > 10) {
        //   const secondDayDiff = secondCycle.diff(secondTime, 'days')
        //   const secondMiniteDiff = secondCycle.diff(secondTime, 'minutes')
        //   return secondDayDiff > 1 || secondMiniteDiff > 10 ? 'red' : 'orange'
        // } else {
        //   return 'lime'
        // }
      } else {
        const firstDateTimeDifference = currentDate - firstDate
        if (firstDateTimeDifference < firstCycle) {
          if (secondTime != null && secondDate != null) {
            const secondTimeDifference = currentDate - secondDate
            return secondTimeDifference > secondCycle ? 'orange' : 'lime'
          } else {
            return 'lime'
          }
        } else {
          return 'red'
        }

        // if (firstDateTimeDifference > firstCycle) {
        //   if (secondTime != null && secondDate != null) {
        //     const secondTimeDifference = currentDate - secondDate
        //     return secondTimeDifference > secondCycle ? 'red' : 'orange'
        //   } else {
        //     return 'red'
        //   }
        // } else {
        //   return 'lime'
        // }
      }
    },
    windowSelection(window) {
      if (window.windowState === 'minimize') {
        this.$set(window, 'windowState', 'normal')
      }
      this.$store.dispatch('mdi/bringToFrontWindow', window.id)
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
@import '~@/styles/animation.scss';

.BottomBar{
  .system-tooltip {
    position: relative;
    display: inline-block;
    cursor: pointer;

    .tooltip-text {
      visibility: hidden;
      background-color: black;
      color: #fff;
      text-align: center;
      padding: 5px;
      border-radius: 5px;
      position: absolute;
      bottom: 150%;
      left: 50%;
      transform: translateX(-50%);
      white-space: nowrap;
      opacity: 0;
      transition: opacity 0.3s;
    }

    &:hover .tooltip-text {
        visibility: visible;
        opacity: 1;
    }

      .blinking {
      animation: blink 1.5s ease-in-out infinite alternate;
    }

    @keyframes blink{
      0% {
        opacity: 0.3;
      }
      100% {
        opacity: 1;
      }
    }
  }
}

.system-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px;
  display: inline-block;
}

.BottomBar {
  width: 100%;
  height: 50px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: $aiTemplateDefault;
  .m_icon {
    font-size: 21px;
  }
  .statusBarWrap {
    display:flex;
    background-color:rgba(0,0,0,0.5);
    border-radius: 5px;
    box-sizing: border-box;
    height:40px;

    ::v-deep .el-submenu .el-submenu__title {
      height:40px;
      display: flex;
      border-bottom: 0px;
      align-items: center;
      background: #1e293b;
      color: white;
      border: solid 1px #797676;
      border-radius: 5px;
    }
    .badgeItem{
      position: absolute;
      background-color:#fff;
      color:#141414;
      height: 18px;
      line-height: 18px;
      text-align: center;
      padding:0 5px;
      border-radius: 15px;
      font-size:12px;
      font-weight: 400;
      right:5px; top:9px;
      /* transform: translate(30px, 20px); */
    }

    ::v-deep .el-menu--horizontal {
      .el-submenu{
        .el-submenu__icon-arrow{
          margin-right: 10px !important;
        }
      }
    }
  }
}
</style>

<style lang="scss">
.el-menu--horizontal.statusBarMenuNiaBottomBar{

  ul.el-menu.el-menu--popup{
    background: transparent !important;
    border-radius: 15px !important;
    top: 17px !important;

    li.statusBarWindowItem{
      text-align: left !important;
      font-size: 14px !important;
      font-family: NotoSansKR;
      background-color: #fff !important;
      border: rgb(232, 232, 232) solid 1px;
      margin-bottom: 10px;
      height: 39px !important;

      &:hover{
        // opacity: 0.8;
        background: rgb(232, 232, 232) !important;
      }

      &.focusWindow{
        background-color: rgb(30, 41, 59) !important;
        color: white !important;
      }
    }
  }
}
</style>
