<template>
  <div :class="{ [name]: true }">
    <div class="alarm-container flex flex-nowrap ml-3">
      <div v-if="isViewport('>', 'md')" class="alarm-title flex items-center mx-2">
        <img src="@/assets/icon/attention.png" style="height: 22px; width: 22px; filter: contrast(0) brightness(2)">
        <span class="text-lg font-bold pl-1 pt-1">경보리스트</span>
      </div>
      <div class="alarm-content flex flex-nowrap items-center ml-2 text-sm">
        <span class="pt-1">총</span>
        <span class="text-3xl text-red-600 font-bold mx-1">42</span>
        <span class="pt-1">건의 경보가 발생되었습니다.</span>
      </div>

      <div v-if="isViewport('>', 'sm')" class="alarm-count-list flex flex-nowrap ml-4 pt-1 text-base">
        <el-col
          v-for="a in alarmList"
          :key="a.$index"
          class="flex flex-col items-center font-bold ml-3"
        >
          <div class="text-xl" :style="{color: a.color}">{{ a.count }}</div>
          <div class="text-xs">{{ a.name }}</div>
        </el-col>
      </div>
    </div>
    <div class="notice-container flex items-center mr-4 text-base pt-1">
      <div v-if="isViewport('>', 'md')" class="notice-title flex items-center">
        <i class="el-icon-chat-line-square m-2 mt-1 m_icon" />
        <span class="ml-2 text-lg">공지사항</span>
      </div>
      <el-divider direction="vertical" />
      <div class="notice-content">
        <span>2023.2.30 12:00</span>
        <span>서구 C 기지국 신설로 인한 경보 테스트 작업이 있습니다.</span>
      </div>
    </div>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
const routeName = 'BottomBar'

export default {
  name: routeName,
  components: { },
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
        { name: 'WARNING', count: 20, color: 'skyblue' }
      ]
    }
  },
  mounted() {
    window.bbar = Object.assign(window.bbar || {}, { header: this })
  },
  methods: {
    handleChangeExpandType(param) {
      this.$emit('changeSize', param)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";
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
}
</style>
