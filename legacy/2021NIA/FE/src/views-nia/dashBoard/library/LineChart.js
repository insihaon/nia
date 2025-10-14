import { Line } from 'vue-chartjs'

export default {
    extends: Line,
    props: {
        cData: {
            type: Object,
            default: null
        },
        cVerLine: {
            type: Object,
            default: () => { }
        }
    },

    data() {
        return {
            startX: null,
            chartOptions: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    xAxes: [{
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
                    }],
                    yAxes: [{
                        ticks: {
                            beginAtZero: false,
                            fontSize: 10,
                            fontColor: '#000000',
                            fontFamily: 'ktb',
                            callback: function (value, index, labels) {
                                return value
                            },
                            gridLines: {
                                display: true,
                            },
                        },
                    }],
                },
                elements: { line: { tension: 0, fill: false }, point: { radius: 0 } },
                animation: { duration: 0 },
                legend: {
                    display: true,
                    position: 'top',
                    align: 'left',
                    labels: {
                        usePointStyle: false,
                        padding: 20,
                        boxWidth: 12,
                    },
                    padding: 0,
                },
                layout: {
                    padding: {
                        bottom: 10,
                    },
                },

                plugins: {
                    zoom: {
                        pan: {
                            enabled: true,
                            mode: 'x',
                        },
                        zoom: {
                            enabled: true,
                            mode: 'xy',
                            drag: {
                                enabled: true,
                                borderColor: 'rgba(54, 162, 235, 0.3)', // 드래그 박스 테두리색
                                borderWidth: 1,
                                backgroundColor: 'rgba(54, 162, 235, 0.15)', // 드래그 영역 배경색
                            },
                            speed: 1,
                            sensitivity: 3, // 확대 민감도
                        },
                    }
                }

            },
        }
    },
    methods: {
        chartUpdate() {
            this.$data._chart.update()
            this.reRenderChart()
        },
        clear() {
            this.$data._chart.clear()
        },
        reRenderChart() {
            // 1. 옵션 병합 (V2에서는 cVerLine을 옵션에 넣는 방식은 유효)
            const chartOptions = {
                ...this.chartOptions,
                cVerLine: this.cVerLine,
            }

            // 2. 파괴
            if (this.$data._chart) {
                this.$data._chart.destroy()
            }

            // 3. 플러그인과 함께 차트 렌더링
            this.renderChart(this.cData, chartOptions)

            const chart = this.$data._chart
            // ✅ 초기 스케일 저장
            this.originalScales = {
                x: { min: chart.scales['x-axis-0'].min, max: chart.scales['x-axis-0'].max },
                y: { min: chart.scales['y-axis-0'].min, max: chart.scales['y-axis-0'].max }
            }

            // 캔버스에 커스텀 이벤트 리스너 추가
            const canvas = this.$data._chart.canvas
            canvas.addEventListener('mousedown', this.onMouseDown)
            canvas.addEventListener('mouseup', this.onMouseUp)
        },
        onMouseDown(e) {
            this.startX = e.offsetX
        },
        onMouseUp(e) {
            const endX = e.offsetX
            if (this.startX !== null) {
                if (endX < this.startX) {
                    this.$data._chart.options.plugins.zoom.zoom.drag = false
                    this.resetZoom()
                    this.$data._chart.update()
                    this.$data._chart.options.plugins.zoom.zoom.drag = true
                }
            }
            this.startX = null
        },
        resetZoom() {
            const chart = this.$data._chart
            if (!chart || !this.originalScales) return

            // ✅ 원래 범위 복원
            chart.options.scales.xAxes[0].ticks.min = this.originalScales.x.min
            chart.options.scales.xAxes[0].ticks.max = this.originalScales.x.max
            chart.options.scales.yAxes[0].ticks.min = this.originalScales.y.min
            chart.options.scales.yAxes[0].ticks.max = this.originalScales.y.max

            chart.update()
        }
    },

    mounted() {
        this.reRenderChart()
    },
    beforeDestroy() {
        if (this.$data._chart) {
            this.$data._chart.destroy()
        }
    },
    clear() {
        this.$data._chart.clear()
    },
}
