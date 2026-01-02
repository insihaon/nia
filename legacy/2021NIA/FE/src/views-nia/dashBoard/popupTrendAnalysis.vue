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
          <div class="legend">(단위 : 'Mbps')</div>
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
    }
  },
  computed: {},
  created: function () {},
  mounted(data) {},
  destroyed() {},
  methods: {
    setVisible: function (params) {
      this.loading = false
      this.trendChartData.labels = params.trend.trendParseDate
      this.trendChartData.datasets[0].data = []
      this.trendChartData.datasets[0].data = params.trend.trendData
      this.weeklyChartData.datasets[0].data = []
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
.box-card {
  background: #ffffff;
  border: none;
  border-radius: 10px;
  box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.05), 0 2px 8px rgba(0, 0, 0, 0.08);
}

.cardTitle {
  display: inline-block;
  padding: 16px 0;
  font-size: 18px;
  font-weight: 600;
  width: 100%;
  color: #0f172a;
  letter-spacing: -0.4px;
  border-bottom: 1px solid #e2e8f0;
  position: relative;
}
.cardTitle::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 40px;
  height: 2px;
  background: #374151;
}

.trendAnalyChart {
  position: relative;
  height: 300px;
  width: 100%;
  padding: 20px 0;
}

.legend {
  position: absolute;
  left: 450px;
  top: 130px;
  font-weight: 500;
  font-size: 11px;
  color: #64748b;
  background: #ffffff;
  padding: 6px 12px;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}
</style>
