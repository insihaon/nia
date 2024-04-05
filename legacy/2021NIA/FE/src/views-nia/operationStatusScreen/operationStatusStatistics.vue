<template>
  <div :class="{ [name]: true }">
    <div class="search-container">
      <div class="title"><i class="el-icon-pie-chart pr-2" />AI 관제 시스템 처리량</div>
      <div>
        <el-radio-group v-model="systemChartCondition.dayType">
          <el-radio-button label="DAY">일별</el-radio-button>
          <el-radio-button label="MONTH">월별</el-radio-button>
        </el-radio-group>
        <el-date-picker
          v-model="systemChartCondition.date"
          :type="systemChartCondition.dayType ==='DAY' ? 'date':'month'"
        />
        <el-button class="h-fit" icon="el-icon-search" @click="onLoadStatistics()" />
      </div>
    </div>
    <div class="chart-container">
      <div class="chart h-100 w-1/3">
        <CompChart :options="ticketOptions" class="h-60" />
        <div class="table-conatiner">
          <table>
            <tbody>
              <tr v-for="item in ticketTitle" :key="item.key">
                <td>{{ item.name }}</td>
                <td>{{ getLocaleString(statistics[item.key]) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="chart h-100 w-1/3">
        <CompChart :options="collectOptions" class="h-60" />
        <div class="table-conatiner">
          <table>
            <tbody>
              <tr v-for="item in collectTitle" :key="item.key">
                <td>{{ item.name }}</td>
                <td>{{ getLocaleString(statistics[item.key]) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="chart h-100 w-1/3">
        <CompChart :options="servingOptions" class="h-60" />
        <div class="table-conatiner">
          <table>
            <tbody>
              <tr v-for="item in servingTitle" :key="item.key">
                <td>{{ item.name }}</td>
                <td>{{ getLocaleString(statistics[item.key]) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import { apiDashboardStatistics } from '@/api/nia'
import CompChart from '@/components/chart/CompChart.vue'

const routeName = 'OperationStatusStatistics'
export default {
  name: routeName,
  components: { CompChart },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      systemChartCondition: {
        dayType: 'DAY',
        date: this.moment().subtract(1, 'd').format('YYYY-MM-DD')
      },
      statistics: {},
      ticketTitle: [
        { name: '장애', key: 'ticket_rt_cnt' },
        { name: '광레벨\n저하', key: 'ticket_pf_cnt' },
        { name: '이상\n트래픽', key: 'ticket_att_cnt' },
        { name: '유해\n트래픽', key: 'ticket_ntt_cnt' },
      ],
      collectTitle: [
      { name: '광레벨\n수집', key: 'trans_perf_cnt' },
      { name: '전송\n경보수집', key: 'trans_alarm_cnt' },
      { name: 'IP시설\n연동', key: 'ip_resource_cnt' },
      { name: 'IP경보\n연동', key: 'ip_alarm_cnt' },
      { name: 'IP트래픽\n연동', key: 'ip_perf_cnt' },
      { name: 'IP SFlow\n연동', key: 'ip_sflow_cnt' }
      ],
      servingTitle: [
        { name: '시설\n연동', key: 'link_total_resource_cnt' },
        { name: '경보\n연동', key: 'link_total_alarm_cnt' },
        { name: '트래픽\n연동', key: 'link_ip_perf_cnt' },
        { name: '광레벨\n연동', key: 'link_trans_perf_cnt' }
      ],

    }
  },
  computed: {
    ticketOptions() {
      const keyByTitle = this.ticketTitle
      return this.getDefaultChartOptions('티켓 발생량', keyByTitle)
    },
    collectOptions() {
      const keyByTitle = this.collectTitle
      return this.getDefaultChartOptions('데이터 수집량', keyByTitle)
    },
    servingOptions() {
      const keyByTitle = this.servingTitle
      return this.getDefaultChartOptions('데이터 제공량(데이터레이크 연계량)', keyByTitle)
    },
  },
  async mounted() {
    await this.onLoadStatistics()
  },
  methods: {
    async onLoadStatistics() {
      const { dayType: DATE_TYPE, date } = this.systemChartCondition
      const formatStr = DATE_TYPE === 'DAY' ? 'YYYY-MM-DD' : 'YYYY-MM'
      const defaultSt = {
        trans_alarm_cnt: 0,
        link_ip_perf_cnt: 0,
        link_total_alarm_cnt: 0,
        ticket_ntt_cnt: 0,
        ticket_att_cnt: 0,
        ip_alarm_cnt: 0,
        ticket_rt_cnt: 0,
        link_total_resource_cnt: 0,
        trans_perf_cnt: 0,
        ip_resource_cnt: 0,
        ticket_pf_cnt: 0,
        ip_perf_cnt: 0,
        ip_sflow_cnt: 0,
        link_trans_perf_cnt: 0
      }
      try {
        const res = await apiDashboardStatistics({ DATE_TYPE, SEARCH_DATE: this.moment(date).format(formatStr) })
        this.statistics = res.result[0] ?? defaultSt
      } catch (error) {
        this.error(error)
      }
    },
    getLocaleString(value) {
      return value?.toLocaleString()
    },
    getDefaultChartOptions(title, keyByTitle) {
      return {
        title: {
          text: title,
          left: 'center',
        },
        xAxis: {
          type: 'category',
          data: keyByTitle.map(v => v.name),
          axisLabel: {
            formatter: function (value, index) {
              let result = value
            if (value >= 1000000) {
              result = (value / 1000000) + 'M'
            } else if (value >= 1000) {
              result = (value / 1000) + 'K'
            } else {
              result = value.toString()
            }
              return result
            }
          }
          // axisLabel: { interval: 0, rotate: 20, fontWeight: 'bold', },
        },
        yAxis: {
          type: 'value',
          data: keyByTitle.map(v => v.name),
          // axisLabel: {
          //   formatter: function (value, index) {
          //     let result = value
          //   if (value >= 1000000) {
          //     result = (value / 1000000) + 'M'
          //   } else if (value >= 1000) {
          //     result = (value / 1000) + 'K'
          //   } else {
          //     result = value.toString()
          //   }
          //     return result
          //   }
          // }
        },
        series: [
          {
            type: 'bar',
            label: {
              show: true,
              width: 20,
              position: 'top',
              fontWeight: 'bold',
              formatter: (param) => {
                return param.data?.toLocaleString()
              },
            },
            barWidth: '50',
            itemStyle: {
              color: '#6149c7',
              borderWidth: 1,
              borderColor: '#189ec0',
              borderRadius: [0, 5, 5, 0]
            },
            data: keyByTitle.map(v => { return this.statistics[v.key] === null ? 0 : this.statistics[v.key] }),

          }
        ]
      }
    },
  }
}
</script>
<style lang="scss" scoped>
.search-container {
  height: 80px;
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
  .table-conatiner {
    height: 35%;
    padding: 15px;
    border-radius: 15px;
    margin: 5px 10px 10px 10px;
    background-color: #1c2b40;
    table {
      width: 100%;
      height: 100%;
      color: #dadada;
      th, td {
        border: dotted;
        font-size: 15px;
        text-align: center;
        border-style: dotted;
      }
    }
  }
}
</style>
