<template>
  <div :class="{ [name]: true }">
    <div class="alarm-container flex flex-nowrap ml-3">
      <div v-if="isViewport('>', 'md')" class="alarm-title flex items-center flex-nowrap whitespace-nowrap mx-2" style="min-width: 93px">
        <img src="@/assets/icon/attention.png" style="height: 22px; width: 22px; filter: contrast(0) brightness(2)" />
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
        <div class="pt-1 font-bold text-base" style="white-space: nowrap; overflow: hidden">시스템 현황 모니터링</div>
        <div class="ml-2 d-flex font-bold items-center gap-x-2">
          <span v-for="system in niaProcess" v-show="systemMonitoringMap[system.name].show" :key="system.name">
            <div :style="{ color: system.status }" class="system-tooltip" style="border-right: solid 1px #ffffff82; padding-right: 5px; white-space: nowrap">
              <div :class="{ blinking: system.status !== 'lime' }">{{ system.name }}</div>
              <div class="tooltip-text" v-html="system.tooltip" />
            </div>
          </span>
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
              <el-menu-item :key="window.id" :class="{ focusWindow: window.windowState !== 'minimize' && window.zindex == 1000 }" class="statusBarWindowItem" @click="windowSelection(window)">
                <i class="el-icon-document" /> <span>{{ window.name }}</span>
                <i class="el-icon-close statusBarWindowItemCloseBtn" @click="$store.dispatch('mdi/removeWindow', window.id)" />
              </el-menu-item>
            </template>
          </el-submenu>
        </el-menu>
      </div>
      <div v-if="isViewport('>', 'md')" class="notice-title flex items-center">
        <el-tooltip class="item" effect="light" content="시스템 모니터링 필터" placement="top">
          <i class="el-icon-s-tools m_icon" style="cursor: pointer; margin-right: 5px" @click="openSystemMonitoringModal" />
        </el-tooltip>
      </div>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'BottomBar'
import { apiSystemMonitoring } from '@/api/nia'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import { mapState } from 'vuex'

export default {
  name: routeName,
  src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
  components: {},
  extends: Base,
  mixins: [dialogOpenMixin],
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

      monitoringInterval: null,
    }
  },

  computed: {
    ...mapState({
      niaProcess: (state) => state.systemMonitoring.niaProcess,
      systemMonitoringMap: (state) => state.systemMonitoring.systemMonitoringMap,
    }),
  },

  created() {},

  mounted() {
    this.getSystemMonitoringData()
    this.setIntervalGetSystemMonitoringData()
    this.subscribeEvent()
    window.bbar = Object.assign(window.bbar || {}, { header: this })

    this.$store.dispatch('systemMonitoring/checkSaveState')
  },
  beforeDestroy() {
    clearInterval(this.monitoringInterval)
  },

  methods: {
    setIntervalGetSystemMonitoringData() {
      this.monitoringInterval = setInterval(() => {
        this.getSystemMonitoringData()
      }, 3 * 60 * 1000)
    },

    subscribeEvent() {},
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
          this.$store.dispatch('systemMonitoring/setNiaProcess', { processKey: d.key_name, mapKey: 'firstTime', value: d.yd_date ?? null })
          // this.niaProcess[d.key_name]['firstTime'] = d.yd_date ?? null
        } else {
          const modifiedKeyName = d.key_name.slice(2)
          const firstKey = modifiedKeyName.charAt(0).toLowerCase() + modifiedKeyName.slice(1)
          if (firstKey in this.niaProcess) {
            if ('secondStep' in this.niaProcess[firstKey]) {
              this.$store.dispatch('systemMonitoring/setNiaProcess', { processKey: firstKey, mapKey: 'secondTime', value: d.yd_date ?? null })
              this.$store.dispatch('systemMonitoring/setNiaProcess', { processKey: firstKey, mapKey: 'secondCycle', value: this.niaProcess[firstKey]['secondStep'].cycle })
              // this.niaProcess[firstKey]['secondTime'] = d.yd_date ?? null
              // this.niaProcess[firstKey]['secondCycle'] = this.niaProcess[firstKey]['secondStep'].cycle
            } else {
              throw new Error('firstStep, secondStep이 둘다 없는 Process는 에러입니다.')
            }
          }
        }
      }

      const monitoringKeys = Object.keys(this.niaProcess)
      for (const key of monitoringKeys) {
        this.$store.dispatch('systemMonitoring/setNiaProcess', { processKey: key, mapKey: 'status', value: this.checkKeyStatus(this.niaProcess[key]) })
        // this.niaProcess[key].status = this.checkKeyStatus(this.niaProcess[key])
      }
    },

    calculateDateDiff(date1, date2, unit = 'days') {
      const d1 = new Date(date1)
      const d2 = new Date(date2)
      const diffMs = d2 - d1

      switch (unit) {
        case 'days':
          return Math.floor(diffMs / (1000 * 60 * 60 * 24))
        case 'minutes':
          return Math.floor(diffMs / (1000 * 60))
        case 'hours':
          return Math.floor(diffMs / (1000 * 60 * 60))
        default:
          return diffMs
      }
    },

    checkKeyStatus(processObj) {
      const { cycle: firstCycle, secondCycle, secondTime, firstTime } = processObj
      const currentDate = new Date()
      const firstDate = firstTime ? new Date(firstTime) : null
      const secondDate = secondTime ? new Date(secondTime) : null

      if (typeof firstCycle !== 'number') {
        const testcycle = this.moment().set({ hour: 2, minute: 40, second: 0, millisecond: 0 })
        const dayDiff = this.calculateDateDiff(firstCycle, firstTime, 'days')
        const miniteDiff = this.calculateDateDiff(firstCycle, firstTime, 'minutes')
        // const dayDiff = firstCycle.diff(firstTime, 'days')
        // const miniteDiff = firstCycle.diff(firstTime, 'minutes')

        if (dayDiff <= 1 && miniteDiff <= 10) {
          const secondDayDiff = this.calculateDateDiff(secondCycle, secondTime, 'days')
          const secondMiniteDiff = this.calculateDateDiff(secondCycle, secondTime, 'minutes')
          // const secondDayDiff = secondCycle.diff(secondTime, 'days')
          // const secondMiniteDiff = secondCycle.diff(secondTime, 'minutes')
          return secondDayDiff > 1 || secondMiniteDiff > 10 ? 'orange' : 'lime'
        } else {
          return 'red'
        }
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
      }
    },
    windowSelection(window) {
      if (window.windowState === 'minimize') {
        this.$set(window, 'windowState', 'normal')
      }
      this.$store.dispatch('mdi/bringToFrontWindow', window.id)
    },
    openSystemMonitoringModal() {
      this.fn_openWindow('systemMonitoringFilter')
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
@import '~@/styles/animation.scss';

.BottomBar {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */

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

    @keyframes blink {
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
    display: flex;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 5px;
    box-sizing: border-box;
    height: 40px;

    ::v-deep .el-submenu .el-submenu__title {
      height: 40px;
      display: flex;
      border-bottom: 0px;
      align-items: center;
      background: #1e293b;
      color: white;
      border: solid 1px #797676;
      border-radius: 5px;
    }
    .badgeItem {
      position: absolute;
      background-color: #fff;
      color: #141414;
      height: 18px;
      line-height: 18px;
      text-align: center;
      padding: 0 5px;
      border-radius: 15px;
      font-size: 12px;
      font-weight: 400;
      right: 5px;
      top: 9px;
      /* transform: translate(30px, 20px); */
    }

    ::v-deep .el-menu--horizontal {
      .el-submenu {
        .el-submenu__icon-arrow {
          margin-right: 10px !important;
        }
      }
    }
  }
}
</style>

<style lang="scss">
.el-menu--horizontal.statusBarMenuNiaBottomBar {
  ul.el-menu.el-menu--popup {
    background: transparent !important;
    border-radius: 15px !important;
    top: 17px !important;

    li.statusBarWindowItem {
      text-align: left !important;
      font-size: 14px !important;
      font-family: NotoSansKR;
      background-color: #fff !important;
      border: rgb(232, 232, 232) solid 1px;
      margin-bottom: 10px;
      height: 39px !important;

      &:hover {
        // opacity: 0.8;
        background: rgb(232, 232, 232) !important;
      }

      &.focusWindow {
        background-color: rgb(30, 41, 59) !important;
        color: white !important;
      }
    }
  }
}
</style>
