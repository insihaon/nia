<template>
  <div class="d-flex items-center">
    매
    <el-select v-model="period" size="mini" class="d-flex ml-1" @change="onEmitValue">
      <el-option
        v-for="op in periodOptions"
        :key="op.value"
        :label="op.label"
        :value="op.value"
      />
    </el-select>
    <div v-if="period === 'year'" class="d-flex items-center ml-1">
      <el-select v-model="month" size="mini" @change="onEmitValue">
        <el-option
          v-for="mon in monthOptions"
          :key="mon"
          :label="mon"
          :value="mon"
        />
      </el-select>
      월
    </div>
    <div v-if="['year', 'month'].includes(period)" class="d-flex items-center ml-1">
      <el-select v-model="day" size="mini" @change="onEmitValue">
        <el-option
          v-for="dayOp in dayOptions"
          :key="dayOp"
          :label="dayOp"
          :value="dayOp"
        />
      </el-select>
      일
    </div>
    <div v-if="['year', 'month', 'day'].includes(period)" class="d-flex items-center ml-1">
      <el-select v-model="hour" size="mini" @change="onEmitValue">
        <el-option
          v-for="hourOp in hourOptions"
          :key="hourOp"
          :label="hourOp"
          :value="hourOp"
        />
      </el-select>
      <span>:</span>
    </div>
    <div v-if="['year', 'month', 'day', 'hour'].includes(period)" class="d-flex items-center ml-1">
      <el-select v-model="minute" size="mini" @change="onEmitValue">
        <el-option
          v-for="minuteOp in minuteOptions"
          :key="minuteOp"
          :label="minuteOp"
          :value="minuteOp"
        />
      </el-select>
      <span v-if="period === 'hour'">분</span>
    </div>
    <span v-if="period !== 'minute'">마다</span>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'CompCron'

export default {
  name: routeName,
  components: { },
  extends: Base,
  props: {
    /* 연동 주기
0 30 23 * * ? */
speriod: {
  type: String,
  default: ''
}
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      periodOptions: [
        { label: '분', value: 'minute' },
        { label: '시간', value: 'hour' },
        { label: '일', value: 'day' },
        { label: '월', value: 'month' },
        { label: '년', value: 'year' },
      ],
      monthOptions: Array.from({ length: 12 }, (_, i) => i + 1),
      hourOptions: Array.from({ length: 24 }, (_, i) => this.padZero(i)),
      minuteOptions: Array.from({ length: 60 }, (_, i) => this.padZero(i)),
      period: 'year',
      month: '12',
      day: '1',
      hour: '00',
      minute: '00',
      cronMean: ['second', 'minute', 'hour', 'day', 'month', '요일'],
      cronValue: null,
    }
  },
  computed: {
    dayOptions() {
      if (this.month === 2) {
        return Array.from({ length: 29 }, (_, i) => i + 1)
      } else if ([4, 6, 9, 11].includes(this.month)) {
        return Array.from({ length: 30 }, (_, i) => i + 1)
      } else {
        return Array.from({ length: 31 }, (_, i) => i + 1)
      }
    }
  },
  mounted () {
    this.setValue()
  },
  methods: {
    padZero(value) {
      return value.toString().padStart(2, '0')
    },
    setValue() {
      if (this.speriod !== '' && this.speriod !== null) {
        const cronArr = this.speriod.split(' ')
        for (const [i, cronVal] of cronArr.entries()) {
          if (cronVal === '*') {
            this.period = this.cronMean[i]
            return
          } else if (this[this.cronMean[i]]) {
            this[this.cronMean[i]] = cronVal.toString().padStart(2, '0')
          }
        }
      }
    },
    onEmitValue() {
      // parseInt(this.hour, 10)
      // this.period 에 따라 * 처리 해야함
      let cron = `0 ${parseInt(this.minute, 10)} ${parseInt(this.hour, 10)} ${this.day} ${this.month} ?`
      const periodIdx = this.cronMean.findIndex(v => v === this.period)
      const cronArr = cron.split(' ')
      if (periodIdx !== -1) {
        cronArr.forEach((value, i) => {
          if (i >= periodIdx && i < cronArr.length - 1) {
            cronArr[i] = '*'
          }
        })
      }
      cron = cronArr.join(' ') // 값 확인
      this.$emit('updateCron', cron)
    }
  }
}
</script>
<style lang="css" scoped>
</style>
