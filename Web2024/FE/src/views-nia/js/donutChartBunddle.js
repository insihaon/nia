import { Doughnut } from 'vue-chartjs'

export const chatbotCenterTextPlugin = {
    id: 'centerTextPlugin', // 플러그인 ID
    beforeDraw: (chart) => {
        const total = chart.data.datasets[0].data.reduce((a, b) => a + b, 0)

        if (total > 0) {
            const { ctx, width, height } = chart
            ctx.restore()

            // 1. 텍스트 스타일 설정
            const fontSize = (height / 150).toFixed(2)
            ctx.font = `${fontSize}em sans-serif`
            ctx.textBaseline = 'middle'
            ctx.fillStyle = '#333' // 텍스트 색상

            // 2. 텍스트 내용 정의
            const totalLabel = '전체'
            const totalValue = total.toLocaleString()

            // 3. 중앙 좌표 계산
            const centerX = width / 2 + 62
            const centerY = height / 2 + 15

            // 4. 레이블 그리기 (윗줄)
            let text = totalLabel
            let textX = Math.round(centerX - ctx.measureText(text).width / 2)
            ctx.fillText(text, textX, centerY - 5) // 윗줄 렌더링

            // 5. 값 그리기 (아랫줄)
            text = totalValue
            ctx.font = `${(fontSize * 0.9).toFixed(2)}em sans-serif`
            textX = Math.round(centerX - ctx.measureText(text).width / 2)
            ctx.fillText(text, textX, centerY + 15) // 아랫줄 렌더링

            ctx.save()
        }
    },
}

export const getChatbotDonutChart = () => {
    return {
        extends: Doughnut,
        props: {
            chartData: {
                type: Object,
                required: true,
            },
            options: {
                type: Object,
                default: () => ({}),
            },
        },
        mounted() {
            // 이곳은 donutChart의 mounted
            const clonedData = this._cloneChartData(this.chartData)
            const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
            this.renderChart(clonedData, clonedOptions)
        },
        watch: {
            chartData: {
                deep: true,
                handler(newVal) {
                    const clonedData = this._cloneChartData(newVal)
                    const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
                    this.renderChart(clonedData, clonedOptions)
                },
            },
            options: {
                deep: true,
                handler(newVal) {
                    const clonedData = this._cloneChartData(this.chartData)
                    const clonedOptions = this._cloneOptionsPreservingFunctions(newVal)
                    this.renderChart(clonedData, clonedOptions)
                },
            },
        },
        methods: {
            _cloneOptionsPreservingFunctions(options) {
                const generateLabelsFn = options && options.legend && options.legend.labels && options.legend.labels.generateLabels
                const clonedOptions = JSON.parse(JSON.stringify(options || {}))
                if (generateLabelsFn) {
                    if (!clonedOptions.legend) clonedOptions.legend = {}
                    if (!clonedOptions.legend.labels) clonedOptions.legend.labels = {}
                    clonedOptions.legend.labels.generateLabels = generateLabelsFn
                }
                return clonedOptions
            },
            _cloneChartData(data) {
                return JSON.parse(JSON.stringify(data))
            },
            optionUpdate() {
                const clonedData = this._cloneChartData(this.chartData)
                const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
                this.renderChart(clonedData, clonedOptions)
            },
            chartUpdate() {
                const clonedData = this._cloneChartData(this.chartData)
                const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
                this.renderChart(clonedData, clonedOptions)
            },
        },
    }
}

export const getAiResponseNttDonutChart = () => {
    return {
        extends: Doughnut,
        props: {
            chartData: {
                type: Object,
                required: true,
            },
            options: {
                type: Object,
                default: () => ({}),
            },
        },
        mounted() {
            // 이곳은 donutChart의 mounted
            const clonedData = this._cloneChartData(this.chartData)
            const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
            this.renderChart(clonedData, clonedOptions)
        },
        watch: {
            chartData: {
                deep: true,
                handler(newVal) {
                    const clonedData = this._cloneChartData(newVal)
                    const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
                    this.renderChart(clonedData, clonedOptions)
                },
            },
            options: {
                deep: true,
                handler(newVal) {
                    const clonedData = this._cloneChartData(this.chartData)
                    const clonedOptions = this._cloneOptionsPreservingFunctions(newVal)
                    this.renderChart(clonedData, clonedOptions)
                },
            },
        },
        methods: {
            _cloneOptionsPreservingFunctions(options) {
                const generateLabelsFn = options && options.legend && options.legend.labels && options.legend.labels.generateLabels
                const clonedOptions = JSON.parse(JSON.stringify(options || {}))
                if (generateLabelsFn) {
                    if (!clonedOptions.legend) clonedOptions.legend = {}
                    if (!clonedOptions.legend.labels) clonedOptions.legend.labels = {}
                    clonedOptions.legend.labels.generateLabels = generateLabelsFn
                }
                return clonedOptions
            },
            _cloneChartData(data) {
                return JSON.parse(JSON.stringify(data))
            },
            optionUpdate() {
                const clonedData = this._cloneChartData(this.chartData)
                const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
                this.renderChart(clonedData, clonedOptions)
            },
            chartUpdate() {
                const clonedData = this._cloneChartData(this.chartData)
                const clonedOptions = this._cloneOptionsPreservingFunctions(this.options)
                this.renderChart(clonedData, clonedOptions)
            },
        },
    }
}
