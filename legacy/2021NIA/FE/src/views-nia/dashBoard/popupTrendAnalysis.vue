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
.medium_dialog .el-dialog__body {
  /* el-dialog__body 내부의 레이아웃 컨테이너(el-row)에 대한 기본 설정 */
  padding-top: 5px; /* 상단 여백 조정 */
  padding-bottom: 5px;
  height: auto; /* 내용에 맞게 높이 자동 조정 */
  min-height: 350px; /* 차트 높이를 고려한 최소 높이 */
}

/* el-row를 사용하여 전체 레이아웃 컨테이너 스타일링 (기존 .trendAnalyPopup > ul 역할) */
.el-row {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex; /* flexbox를 사용하여 내부 el-col을 정렬 */
  justify-content: center; /* 가운데 정렬 */
  margin-top: 20px !important; /* 기존 ul의 margin-top: 20px 반영 */
}

.el-col {
  /* position: relative; */ /* el-col 자체에는 일반적으로 적용하지 않음 */
  height: 100%;
  text-align: center;
  /* el-col의 너비는 span 속성으로 제어됩니다 (span="12" = 50%) */
}

/* el-card 스타일 조정 */
.box-card {
  height: 100%; /* 부모 el-col 높이 상속 */
  text-align: left; /* 내부 요소 정렬을 위해 왼쪽 정렬로 변경 */
}

/* el-card 헤더의 span.cardTitle 스타일 (기존 .trendAnalyPopup > ul > li > span 역할) */
.box-card .el-card__header {
  padding: 0;
  border-bottom: none;
}

.box-card .cardTitle {
  position: relative;
  text-align: center;
  font-size: 12px;
  /* transform: rotate(-0.03deg); */ /* 미세한 회전은 생략 */
  font-weight: 600;
  letter-spacing: -1px;
  display: inline-block;
  background-color: #07acc5;
  width: 60px;
  height: 35px;
  line-height: 35px;
  color: #ffffff;
  /* 카드 헤더 내부에서 위치 조정이 필요할 수 있습니다. */
}

/* LineChart 컴포넌트 영역 스타일링 (기존 .trendAnalyPopup > ul > li > div.trendAnalyChart 역할) */
.trendAnalyChart {
  position: relative;
  /* top: 10px; */ /* el-card body 내부에서 상단 여백으로 대체하는 것이 좋습니다. */
  height: 300px; /* 기존 height: 300px 반영 */
  width: 100%;
  /* display: inline-block; */
}

/* 단위 표시 legend 스타일 */
.legend {
  position: relative;
  font-size: 12px;
  margin-top: 10px;
  text-align: right;
  padding-right: 15px;
}

/* el-card__body에 상단 여백 추가 (trendAnalyChart의 top: 10px 대체) */
.box-card .el-card__body {
  padding-top: 10px;
}
</style>

<style scoped>
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
