<template>
  <div :class="{ [name]: true }">
    <div class="alarm-container flex flex-nowrap ml-3">
      <!-- 경보 현황 섹션 -->
      <div class="alarm-section flex flex-nowrap items-center">
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
      </div>

      <!-- 구분선 -->
      <div class="section-divider mx-4" />

      <!-- 시스템 현황 모니터링 섹션 -->
      <div class="monitoring-section flex flex-nowrap items-center">
        <div class="monitoring-title font-bold text-base" style="white-space: nowrap; overflow: hidden; height: 32px;">시스템 현황 모니터링</div>

        <!-- AI연동 그룹 -->
        <template v-if="groupedProcesses['AI연동'] && groupedProcesses['AI연동'].length > 0">
          <div class="ml-4 flex flex-nowrap items-center gap-x-2 process-group">
            <span class="group-badge group-badge-collect">AI연동</span>
            <div class="d-flex flex-nowrap font-bold items-center gap-x-2">
              <span v-for="system in groupedProcesses['AI연동']" :key="system.name">
                <div :style="{ color: system.status }" class="system-tooltip" style="border-right: solid 1px #ffffff82; padding-right: 5px; white-space: nowrap">
                  <div :class="{ blinking: system.status !== 'lime' }">{{ system.name }}</div>
                  <div class="tooltip-text" v-html="system.tooltip" />
                </div>
              </span>
            </div>
          </div>
        </template>

        <!-- 수집/전송 그룹 -->
        <template v-if="groupedProcesses['수집/전송'] && groupedProcesses['수집/전송'].length > 0">
          <div class="ml-4 flex flex-nowrap items-center gap-x-2 process-group">
            <span class="group-badge group-badge-collect">수집/전송</span>
            <div class="d-flex flex-nowrap font-bold items-center gap-x-2">
              <span v-for="system in groupedProcesses['수집/전송']" :key="system.name">
                <div :style="{ color: system.status }" class="system-tooltip" style="border-right: solid 1px #ffffff82; padding-right: 5px; white-space: nowrap">
                  <div :class="{ blinking: system.status !== 'lime' }">{{ system.name }}</div>
                  <div class="tooltip-text" v-html="system.tooltip" />
                </div>
              </span>
            </div>
          </div>
        </template>
      </div>
    </div>
    <div class="notice-container flex items-center mr-4 text-base pt-1">
      <div v-show="$store.getters.windows.length > 0" class="statusBarWrap mr-3">
        <el-menu mode="horizontal" class="statusBarMenu">
          <el-submenu index="0" popper-class="statusBarMenuNiaBottomBar">
            <template slot="title">
              <i class="el-icon-files" /> <span>창목록</span><span class="badgeItem">{{ $store.getters.windows.length }}</span>
            </template>
            <el-menu-item v-for="window in $store.getters.windows" :key="window.id" :class="{ focusWindow: window.windowState !== 'minimize' && window.zindex == 1000 }" class="statusBarWindowItem" @click="windowSelection(window)">
              <i class="el-icon-document" /> <span>{{ window.name }}</span>
              <i class="el-icon-close statusBarWindowItemCloseBtn" @click="$store.dispatch('mdi/removeWindow', window.id)" />
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </div>
      <div v-if="isViewport('>', 'md')" class="notice-title flex items-center">
        <el-tooltip class="item" effect="light" content="시스템 모니터링 필터" placement="top">
          <i class="el-icon-s-tools m_icon" :style="{ color: simulationStatus === 'ON' ? 'red' : '#fff' }" style="cursor: pointer; margin-right: 5px" @click="openSystemMonitoringModal" />
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
import moment from 'moment'

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
      simulationStatus: (state) => state.chatbot.simulationStatus,
    }),
    groupedProcesses() {
      const groups = {
        'AI연동': [],
        '수집/전송': []
      }

      Object.values(this.niaProcess).forEach(system => {
        const mapItem = this.systemMonitoringMap[system.name]
        if (mapItem && mapItem.show) {
          const group = mapItem.group || '수집/전송'
          if (groups[group]) {
            groups[group].push(system)
          }
        }
      })

      return groups
    },
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
      // API 응답 데이터를 key_name으로 매핑
      const processMap = {}
      for (const d of processList) {
        processMap[d.key_name] = {
          time: d.yd_date ?? null,
          cycle: d.normal_time_limit ?? null
        }
      }

      // 각 프로세스의 steps 배열을 업데이트
      const monitoringKeys = Object.keys(this.niaProcess)
      for (const processKey of monitoringKeys) {
        const process = this.niaProcess[processKey]
        if (process.steps) {
          for (let i = 0; i < process.steps.length; i++) {
            const step = process.steps[i]
            if (processMap[step.key]) {
              this.$store.dispatch('systemMonitoring/setNiaProcessStep', {
                processKey,
                stepIndex: i,
                stepData: {
                  time: processMap[step.key].time,
                  cycle: processMap[step.key].cycle
                }
              })
            }
          }
          // 상태 계산
          this.$store.dispatch('systemMonitoring/setNiaProcess', {
            processKey,
            mapKey: 'status',
            value: this.checkKeyStatus(process)
          })
        }
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

    parseNormalTimeLimit(normal_time_limit) {
      // 1) 숫자 → ms duration
      if (/^\d+$/.test(normal_time_limit)) {
        return {
          type: 'duration',
          value: Number(normal_time_limit) // ms
        }
      }

      // 2) HH:mm 또는 HH:mm:ss → daily fixed time
      if (/^\d{2}:\d{2}(:\d{2})?$/.test(normal_time_limit)) {
        const [hour, minute, second = '0'] = normal_time_limit.split(':')

        const timeMoment = moment().set({
          hour: Number(hour),
          minute: Number(minute),
          second: Number(second),
          millisecond: 0
        })

        const x = moment().set({ hour: 0, minute: 15, second: 0, millisecond: 0 })

        return timeMoment
      }

      // 3) 예외
      throw new Error(`Invalid normal_time_limit format: ${normal_time_limit}`)
    },

    getCycle(cycle) {
      if (/^\d+$/.test(cycle)) {
        return Number(cycle)
      } else {
        return this.parseNormalTimeLimit(cycle)
      }
    },

    checkKeyStatus(processObj) {
      if (!processObj.steps || processObj.steps.length === 0) {
        return 'red'
      }

      // step 인덱스에 따른 색상 배열 (자동으로 색상 지정)
      const statusColors = ['red', 'orange', 'yellow', 'skyblue', 'blue', 'purple']
      const currentDate = new Date()

      // 각 step을 순차적으로 체크
      for (let i = 0; i < processObj.steps.length; i++) {
        const step = processObj.steps[i]
        const stepTime = step.time ? new Date(step.time) : null
        const stepCycle = step.cycle ? this.getCycle(step.cycle) : null

        // step에 시간 정보가 없으면 실패
        if (!stepTime) {
          // step 인덱스에 해당하는 색상 반환 (없으면 마지막 색상)
          return statusColors[i] || statusColors[statusColors.length - 1]
        }

        // step에 cycle 정보가 없으면 다음 step 체크
        if (!stepCycle) {
          continue
        }

        // duration 타입 (숫자)인 경우
        if (typeof stepCycle === 'number') {
          const timeDifference = currentDate - stepTime
          if (timeDifference >= stepCycle) {
            // step 인덱스에 해당하는 색상 반환
            return statusColors[i] || statusColors[statusColors.length - 1]
          }
        } else {
          // daily fixed time 타입 (moment 객체)인 경우
          const dayDiff = this.calculateDateDiff(stepCycle, stepTime, 'days')
          const minuteDiff = this.calculateDateDiff(stepCycle, stepTime, 'minutes')

          if (dayDiff > 1 || minuteDiff > 10) {
            // step 인덱스에 해당하는 색상 반환
            return statusColors[i] || statusColors[statusColors.length - 1]
          }
        }
      }

      // 모든 step이 정상이면 lime
      return 'lime'
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
      line-height: 1.6;
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

  .alarm-container {
    display: flex;
    align-items: center;
    height: 100%;
    line-height: 1;
  }

  .alarm-section {
    display: flex;
    align-items: center;
    height: 100%;
    padding: 0 12px;
    border-radius: 8px;
    background-color: rgba(0, 0, 0, 0.1);
    margin-right: 8px;
    line-height: 1;
  }

  .alarm-title,
  .alarm-content {
    line-height: 1;
  }

  .section-divider {
    width: 2px;
    height: 40px;
    background: linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.3), transparent);
    align-self: center;
    border-radius: 1px;
  }

  .monitoring-section {
    display: flex;
    align-items: center;
    height: 100%;
    line-height: 1;
    padding: 0 8px;
    border-radius: 8px;
    background-color: rgba(0, 0, 0, 0.1);
    margin-left: 8px;
  }

  .monitoring-title {
    line-height: 1;
    height: fit-content;
    display: flex;
    align-items: center;
    padding-top: 0;
  }

  .process-group {
    display: flex;
    flex-wrap: nowrap;
    align-items: center;
    padding: 6px 10px;
    border-radius: 6px;
    background-color: rgba(0, 0, 0, 0.15);
    backdrop-filter: blur(4px);
    height: 36px;
    line-height: 1;
    white-space: nowrap;
    overflow: hidden;
  }

  .group-badge {
    font-size: 15px;
    font-weight: 700;
    padding: 5px 12px;
    border-radius: 5px;
    white-space: nowrap;
    text-transform: none;
    letter-spacing: 0.3px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    border: 1.5px solid;
    min-width: 60px;
    text-align: center;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    line-height: 1;
    height: 26px;
    vertical-align: middle;
  }

  .group-badge-ai {
    background-color: rgba(239, 68, 68, 0.85);
    color: #ffffff;
    border-color: rgba(239, 68, 68, 1);
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  }

  .group-badge-collect {
    background-color: rgba(59, 130, 246, 0.85);
    color: #ffffff;
    border-color: rgba(59, 130, 246, 1);
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  }

  .system-tooltip {
    line-height: 1;
    height: fit-content;
    display: flex;
    align-items: center;
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
