<template>
  <el-dialog :visible.sync="dialogVisible" custom-class="medium_dialog" center :modal="true" :show-close="true" :before-close="cancel" :close-on-click-modal="false" :close-on-press-escape="false" :append-to-body="true" title="트렌드분석">
    <el-row :gutter="15">
      <el-col :span="12">
        <el-card class="box-card" shadow="never">
          <div slot="header">
            <span class="cardTitle">Trend</span>
          </div>
          <div>
            <LineChart ref="trendAnalyChart" :c-data="trendChartData" :c-options="trendChartOptions" class="trendAnalyChart"></LineChart>
          </div>
          <div class="legend">(단위 : {{ item == 'cpu' ? '%' : 'Mbps' }})</div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card" shadow="never">
          <div slot="header">
            <span class="cardTitle">Weekly</span>
          </div>
          <div>
            <LineChart ref="weeklyAnalyChart" :c-data="weeklyChartData" :c-options="weeklyChartOptions" class="trendAnalyChart"></LineChart>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <span slot="footer" class="dialog-footer">
      <el-button type="info" size="small" @click="cancel()"><i class="el-icon-close"></i> 닫기</el-button>
    </span>
  </el-dialog>
</template>

<script>
const routerName = 'popupTrendAnalysis'

export default {
  name: routerName,
  components: {
    LineChart: () => import('@/views-nia/dashBoard/library/LineChart'),
  },
  props: [],
  data() {
    return {
      dialogVisible: false,
      trendChartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          xAxes: [
            {
              ticks: {
                fontSize: 11,
                minRotation: 0,
                maxRotation: 0,
                fontColor: '#000000',
                fontFamily: 'ktb',
                callback: function (value, index, labels) {
                  return value.substring(0, 6) + ' '
                },
              },
              gridLines: {
                display: true,
              },
            },
          ],
          yAxes: [
            {
              ticks: {
                beginAtZero: false,
                stepSize: 0,
                fontSize: 11,
              },
              gridLines: {
                display: true,
              },
            },
          ],
        },
        elements: { line: { tension: 0, fill: false }, point: { radius: 0 } },
        animation: { duration: 0 },
        legend: { display: false },
        layout: {
          padding: {
            left: 0,
            right: 10,
            top: 20,
            bottom: 10,
          },
        },
        tooltips: {
          callbacks: {
            label: (tooltipItems, data) => {
              return data.datasets[0].label + ' : ' + data.datasets[0].data[tooltipItems.index]
            },
          },
        },
      },
      weeklyChartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          xAxes: [
            {
              ticks: {
                fontSize: 11,
                minRotation: 0,
                maxRotation: 0,
                fontColor: '#000000',
                fontFamily: 'ktb',
                callback: function (value, index, labels) {
                  return value
                },
              },
              gridLines: {
                display: true,
              },
            },
          ],
          yAxes: [
            {
              ticks: {
                beginAtZero: false,
                stepSize: 0,
                fontSize: 11,
              },
              gridLines: {
                display: true,
              },
            },
          ],
        },
        elements: { line: { tension: 0, fill: false }, point: { radius: 0 } },
        animation: { duration: 0 },
        legend: { display: false },
        layout: {
          padding: {
            left: 0,
            right: 10,
            top: 20,
            bottom: 10,
          },
        },
      },
      trendChartData: {
        labels: [],
        datasets: [
          {
            label: 'Trend',
            borderColor: 'red',
            borderWidth: 2,
            backgroundColor: 'red',
            data: [],
            radius: 0,
            spanGaps: true,
          },
        ],
      },
      weeklyChartData: {
        labels: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        datasets: [
          {
            label: 'Weekly',
            borderColor: 'red',
            borderWidth: 2,
            backgroundColor: 'red',
            data: [],
            radius: 0,
            spanGaps: true,
          },
        ],
      },
      item: '',
    }
  },
  computed: {},
  created: function () {},
  mounted(data) {},
  destroyed() {},
  methods: {
    setVisible: function (params) {
      this.loading = false
      this.item = params.item
      console.log(params)
      this.trendChartData.labels = params.trend.trendParseDate
      this.trendChartData.datasets[0].data = []
      this.weeklyChartData.datasets[0].data = []
      this.trendChartData.datasets[0].data = params.trend.trendData
      for (var i = 0; i < params.weekly.length; i++) {
        this.weeklyChartData.datasets[0].data.push(params.weekly[i].cnt === 0 ? 0 : (params.weekly[i].data / params.weekly[i].cnt).toFixed(2))
      }
      console.log(this.weeklyChartData.datasets[0].data)
      setTimeout(() => {
        if (this.$refs.trendAnalyChart) {
          this.$refs.trendAnalyChart.chartUpdate()
          this.$refs.trendAnalyChart.optionUpdate()
        }
        if (this.$refs.weeklyAnalyChart) {
          this.$refs.weeklyAnalyChart.chartUpdate()
          this.$refs.weeklyAnalyChart.optionUpdate()
        }
      }, 0)
      this.dialogVisible = true
    },
    cancel: function (done) {
      this.dialogVisible = false
      this.loading = true
      this.tableData = []
    },
  },
}
</script>
<style scoped>
/* 챠트 */
.trendAnalyPopup {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.trendAnalyPopup > ul {
  position: relative;
  width: 100%;
  height: 100%;
  display: inline-block;
  text-align: center;
  margin-top: 20px;
}

.trendAnalyPopup > ul > li {
  position: relative;
  width: 46%;
  height: 100%;
  display: inline-block;
  text-align: center;
}

.trendAnalyPopup > ul > li > span {
  position: relative;
  text-align: center;
  font-size: 12px;
  transform: rotate(-0.03deg);
  font-weight: 600;
  letter-spacing: -1px;
  display: inline-block;
  background-color: #07acc5;
  width: 60px;
  height: 35px;
  line-height: 35px;
  color: #ffffff;
}
.trendAnalyPopup > ul > li > div.trendAnalyChart {
  position: relative;
  top: 10px;
  left: 0;
  right: 0;
  bottom: 0;
  height: 300px;
  width: 100%;
  display: inline-block;
}
.cardTitle {
  display: inline-block;
  padding-top: 10px !important;
  font-size: 16px;
  width: 100%;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  padding-bottom: 10px;
}
.cardTitle i {
  background-color: #ffffff;
  color: #00aac3;
  padding: 1px 4px;
  border-radius: 3px;
  border: 2px solid #00aac3;
  font-weight: 600;
}

.legend {
  position: absolute;
  right: 40px;
  top: 60px;
  font-weight: 600;
  transform: rotate(-0.03deg);
  font-size: 10px;
  color: #003f33;
}
</style>
