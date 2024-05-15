<template>
  <div :class="{ [name]: true }">
    <div class="search-container" :class="{'flex-column': isViewport('<=', 'sm')}">
      <div v-if="!isMobile" class="title"><i class="el-icon-pie-chart pr-2" />자가 처리 관제 화면</div>
      <div class="d-flex items-center" :class="{ 'flex-wrap': isMobile }">
        <el-radio-group v-model="selfChartCondition.statisticsType" size="mini" @change="onLoadSelfProcessStatistics()">
          <el-radio-button label="hour">시간별</el-radio-button>
          <el-radio-button label="day">일별</el-radio-button>
          <el-radio-button label="month">월별</el-radio-button>
        </el-radio-group>
        <div class="d-flex items-center">
          <el-button icon="el-icon-caret-left" size="mini" @click="onChangeDate('minus')" />
          <el-date-picker
            v-model="selfChartCondition.date"
            :type="getSelfProDateType()"
            size="mini"
          />
          <el-button icon="el-icon-caret-right" size="mini" @click="onChangeDate('plus')" />
          <el-button icon="el-icon-search" size="mini" style="padding: 7px 7px;" @click="onLoadSelfProcessStatistics()" />
        </div>
      </div>
    </div>
    <div class="chart-container">
      <CompChart :options="selfProcessOptions" class="w-100 h-100" @click="onClickChart" />
    </div>
    <ModalSelfProcessDetail ref="ModalSelfProcessDetail" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import { apiSelfProcessStatistics, apiSelfProcessList } from '@/api/nia'
import CompChart from '@/components/chart/CompChart.vue'
import dialogOpenMixin from '@/mixin/dialogOpenMixin'
import ModalSelfProcessDetail from '@/views-nia/modal/ModalSelfProcessDetail.vue'

const routeName = 'SelfProcessingDashboard'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompChart, ModalSelfProcessDetail },
  extends: Base,
  mixins: [dialogOpenMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selfStatistics: [],
      selfProcessInfo: {},
      selfChartCondition: {
        statisticsType: 'hour',
        date: this.moment().format('YYYY-MM-DD')
      },
    }
  },
  computed: {
    selfProcessOptions() {
      const selfStatistics = this.selfStatistics
      return {
        legend: {
          data: ['자가최적화 총 발생', '자가최적화 건 수', '자가회복 총 발생', '자가회복 건 수'],
          // top: '5%'
        },
        title: {
          // text: '자가 최적화/자가 회복',
          // left: 'center',
          textStyle: {
            // fontSize: 13
          }
        },
        dataZoom: [{ type: 'inside' }, { type: 'slider' }],
        tooltip: {},
        xAxis: {
          type: 'category',
          data: selfStatistics.map(v => v.series_time),
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: function (value, index) {
                let result = value
                if (value >= 1000) {
                  result = (value / 1000) + 'K'
                } else {
                  result = value.toString()
                }
                return result
              }
          }
        },
        series: [
          {
            name: '자가최적화 총 발생',
            type: 'bar',
            data: this.selfStatistics.map(v => v.so_totalcount)
          },
          {
            name: '자가최적화 건 수',
            type: 'bar',

            data: this.selfStatistics.map(v => v.so_count)
          },
          {
            name: '자가회복 총 발생',
            type: 'bar',
            data: this.selfStatistics.map(v => v.st_totalcount)
          },
          {
            name: '자가회복 건 수',
            type: 'bar',
            data: this.selfStatistics.map(v => v.st_count)
          },
        ]
      }
    }
  },
  watch: {
    'selfChartCondition.date'() {
      this.onLoadSelfProcessStatistics()
    }
  },
  mounted() {
    if (this.$route.query?.ticket_id || this.$route.query?.alarmno) {
      this.loadSelfProcessInfo()
      //
    }
    this.onLoadSelfProcessStatistics()
  },
  methods: {
    async onLoadSelfProcessStatistics() {
      const { statisticsType: STATISTICS_TYPE, date } = this.selfChartCondition
      const SERIES_TYPE = STATISTICS_TYPE === 'hour' ? 'day' : this.getSelfProDateType()
      try {
        const resSelfProcess = await apiSelfProcessStatistics({ STATISTICS_TYPE, SERIES_TYPE, DATE: this.moment(date).add(1, 'd') })
        this.selfStatistics = resSelfProcess.result ?? []
      } catch (error) {
        this.error(error)
      }
    },
    async loadSelfProcessInfo() {
      const key = this.$route.query?.ticket_id ? 'ticket_id' : 'alarmno'
      const value = this.$route.query?.ticket_id ?? this.$route.query?.alarmno
      try {
        const res = await apiSelfProcessList({ [key]: value })
        this.selfProcessInfo = res.result[0] ?? null
        this.$set(this.selfProcessInfo, 'SELF_PROCESS_GROUP', this.$route.query?.self_process_group)
      } catch (error) {
        this.error(error)
      } finally {
        this.$refs.ModalSelfProcessDetail.open({ row: this.selfProcessInfo })
      }
    },
    onChangeDate(type) {
      const { statisticsType, date } = this.selfChartCondition
      if (statisticsType === 'hour') {
        if (type === 'plus') {
          this.$set(this.selfChartCondition, 'date', this.moment(date).add(1, 'd'))
        } else {
          this.$set(this.selfChartCondition, 'date', this.moment(date).subtract(1, 'd'))
        }
      } else if (statisticsType === 'day') {
        if (type === 'plus') {
          this.$set(this.selfChartCondition, 'date', this.moment(date).add(1, 'M'))
        } else {
          this.$set(this.selfChartCondition, 'date', this.moment(date).subtract(1, 'M'))
        }
      } else {
        if (type === 'plus') {
          this.$set(this.selfChartCondition, 'date', this.moment(date).add(1, 'y'))
        } else {
          this.$set(this.selfChartCondition, 'date', this.moment(date).subtract(1, 'y'))
        }
      }
    },
    onClickChart(e) {
      const params = {
        DATE_TYPE: this.selfChartCondition.statisticsType,
        DATE_TIME: e.name,
        SELF_PROCESS_GROUP: e.seriesName.includes('최적화') ? 'SO' : 'ST'
      }
      const pageTitle = params.SELF_PROCESS_GROUP === 'SO' ? '자가 최적화 이력조회' : '자가 회복 이력조회'
      this.fn_openWindow('selfProcessList', params, null, pageTitle)
    },
    getSelfProDateType() {
      let type = 'date'
      switch (this.selfChartCondition.statisticsType) {
        case 'hour':
          type = 'date'
          break
        case 'day':
          type = 'month'
          break
        case 'month':
          type = 'year'
          break
        default:
          break
      }
      return type
    },
  },
}
</script>
<style lang="scss" scoped>
.search-container {
  // height: 80px;
  display: flex;
  padding: 5px 15px;
  justify-content: space-between;
  .title {
    font-size: 20px;
    font-weight: 700;
  }
}
.chart-container {
  display: flex;
  height: calc(100% - 80px);
}
</style>
