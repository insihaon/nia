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
        <div class="pt-1 font-bold text-base" style="white-space: nowrap;   overflow: hidden;">시스템 현황 모니터링</div>
        <div class="ml-2 d-flex font-bold items-center gap-x-2">
          <div
            v-for="system in niaProcess"
            :key="system.name"
            :style="{ color: system.status === 'Y' ? 'lime' : 'red' }"
            :class="{ blinking: system.status === 'N' }"
            style="border-right: solid 1px #ffffff82;padding-right: 5px; white-space: nowrap; overflow: hidden;"
          >
            {{ system.name }}
          </div>
        </div>
      </div>
    </div>
    <div class="notice-container flex items-center mr-4 text-base pt-1">
      <!-- <el-divider direction="vertical" />
      <div class="notice-content">
        <span>2023.2.30 12:00</span>
        <span>서구 C 기지국 신설로 인한 경보 테스트 작업이 있습니다.</span>
      </div> -->
      <div v-show="$store.getters.windows.length > 0" class="statusBarWrap mr-3">
        <el-menu mode="horizontal" class="statusBarMenu">
          <el-submenu index="0" popper-class="statusBarMenuLI">
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
                <i class="el-icon-document" /> <span>{{ window.name }}</span> <i v-show="window.windowState !== 'minimize' && window.zindex === 1000" class="el-icon-view" />
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
          실시간 포함 5분 미만 스케줄링 -> max 5분
          5분주기 스케줄링 -> max 10분
          수집 주기 max초과 시 연동이상으로 판단한다.
        */
        aiIpSdnTrafficeKey: { name: 'LinkTraffic', status: 'N', cycle: 1 /* * 60 * 1000 */ }, // 연동 주기 1분
        aiIpSdnSflowKey: { name: 'Sflow', status: 'N', cycle: 5 /* * 60 * 1000 */ },
        aiIpSdnSyslogKey: { name: 'Syslog', status: 'N', cycle: 5 /* * 60 * 1000 */ },
        aiIpSdnNodeFactorKey: { name: 'NodeFactor', status: 'N', cycle: 1 /* * 60 * 1000 */ },
        // aiTrafficResultKey: { name: 'TRAFFIC', status: 'N', cycle: 1 /* * 60 * 1000 */ },
        aiTrafficNoxKey: { name: 'AI_Traffic_유해', status: 'N', cycle: 1 /* * 60 * 1000 */ },
        aiTrafficAnoKey: { name: 'AI_Traffic_이상', status: 'N', cycle: 1 /* * 60 * 1000 */ },
        aiIpPerfKey: { name: 'IPSDN_Perf', status: 'N', cycle: 5 /* * 60 * 1000 */ }, // 연동 주기 5분
        aiIpResourceIfKey: { name: 'IPSDN_ResourceIf', status: 'N', cycle: this.moment().set({ hour: 2, minute: 40, second: 0, millisecond: 0 }) }, // 매일 2시 40분
        aiIpResourceKey: { name: 'IPSDN_Resource', status: 'N', cycle: this.moment().set({ hour: 2, minute: 30, second: 0, millisecond: 0 }) }, // 매일 2시 30분
      },
      monitoringInterval: null
    }
  },
  mounted() {
    this.initProcess()
    this.subscribeEvent()
    window.bbar = Object.assign(window.bbar || {}, { header: this })
  },
  beforeDestroy() {
    clearInterval(this.monitoringInterval)
    this.removeWsEventListener(this.CONSTANTS.channels.SYSTEM_MONITORING.name, this.onReceivedSystemMonitoring)
  },
  methods: {
    subscribeEvent() {
      this.addWsEventListener(this.CONSTANTS.channels.SYSTEM_MONITORING.name, this.onReceivedSystemMonitoring)
    },
    onReceivedSystemMonitoring({ channelName, socketMessage }) {
      if (channelName !== 'SYSTEM_MONITORING') return

      const data = JSON.parse(socketMessage.message)
      AppOptions.instance.useWsLog && this.log('RECEIVED SIBSCRIBE SYSTEM_MONITORING EVENT: ', data)
      this.setCurrentProcess(data)
    },
    async initProcess() {
      try {
        const res = await apiSystemMonitoring()
        this.setCurrentProcess(res.result)
      } catch (error) {
        this.error(error)
      }
    },
    setCurrentProcess(processList) {
      processList.forEach(d => { this.niaProcess[d.key_name]['collectionTime'] = d.yd_date ?? null })
      const monitoringKeys = Object.keys(this.niaProcess)
      monitoringKeys.forEach(key => {
        this.niaProcess[key].status = this.checkKeyStatus(this.niaProcess[key])
      })
      console.log()
    },
    checkKeyStatus(processObj) {
      const { cycle, collectionTime: lastCollectedTime } = processObj
      const currentTime = new Date()
      const collectionTime = new Date(lastCollectedTime)

      const timeDifference = currentTime - collectionTime

      if (typeof cycle !== 'number') {
        const dayDiff = cycle.diff(lastCollectedTime, 'days')
        const miniteDiff = cycle.diff(lastCollectedTime, 'minutes')
        return dayDiff > 1 || miniteDiff > 10 ? 'N' : 'Y'
      } else if (cycle < 5) {
        const minuteThreshold = 5 * 60 * 1000 // 5 minutes
        return timeDifference > minuteThreshold ? 'N' : 'Y'
      } else if (cycle >= 5) {
        const dailyThreshold = 10 * 60 * 1000 // 10 minutes
        return timeDifference > dailyThreshold ? 'N' : 'Y'
      }
      return 'Y'
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
      height: 14px;
      line-height: 18px;
      text-align: center;
      padding:0 5px;
      border-radius: 3px;
      font-size:12px;
      font-weight: 400;
      right:5px; top:5px;
      /* transform: translate(30px, 20px); */
    }
  }
}
</style>
