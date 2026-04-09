import { Doughnut } from 'vue-chartjs'

export const centerTextAndCustomTitlePlugin = {
    id: 'centerTextPlugin',
    beforeDraw: (chart) => {
        const { ctx, chartArea, config } = chart
        const total = chart.data.datasets[0].data.reduce((a, b) => a + b, 0)

        // 1. 도넛의 중앙 좌표 계산 (범례를 제외한 도넛만의 중심)
        const centerX = (chartArea.left + chartArea.right) / 2
        const centerY = (chartArea.top + chartArea.bottom) / 2

        ctx.save()

        // --- [추가] 차트 제목 그리기 (도넛 중앙에 맞춤) ---
        // 외부 options.customTitle.text가 있으면 그것을 사용, 없으면 기본값 사용
        const customTitleText = chart.options.customTitle?.text || ''
        if (chart.options.customTitle?.display !== false) {
            ctx.font = 'bold 16px sans-serif'
            ctx.fillStyle = '#000'
            ctx.textAlign = 'center'
            // chartArea.top 위쪽 공간에 제목 배치
            ctx.fillText(customTitleText, centerX, chartArea.top - 20)
        }

        // --- 중앙 텍스트 (전체 / 수치) ---
        if (chart.options.isShowCenterText && total > 0) {
            const fontSize = (chart.height / 160).toFixed(2)
            ctx.textAlign = 'center'
            ctx.textBaseline = 'middle'
            ctx.fillStyle = '#333'

            // '전체' 레이블
            ctx.font = `${fontSize}em sans-serif`
            ctx.fillText('전체', centerX, centerY - 12)

            // 합계 수치
            const totalValue = total.toLocaleString()
            ctx.font = `bold ${(fontSize * 1.2).toFixed(2)}em sans-serif`
            ctx.fillText(totalValue, centerX, centerY + 12)
        }

        ctx.restore()
    },
}

const baseDoughnutConfig = {
    extends: Doughnut,
    props: {
        chartData: { type: Object, required: true },
        // 부모 컴포넌트에서 :options="xxx" 로 전달받는 값
        options: { type: Object, default: () => ({}) },
    },
    methods: {
        _cloneOptionsPreservingFunctions(externalOptions) {
            // 1. 깊은 복사를 통해 원본 훼손 방지
            const cloned = JSON.parse(JSON.stringify(externalOptions || {}))

            // 2. 함수형 옵션(범례 라벨 생성 등) 복구
            const generateLabelsFn = externalOptions?.legend?.labels?.generateLabels
            if (generateLabelsFn) {
                if (!cloned.legend) cloned.legend = {}
                if (!cloned.legend.labels) cloned.legend.labels = {}
                cloned.legend.labels.generateLabels = generateLabelsFn
            }

            /**
             * 3. 내부 필수 옵션 강제 적용 및 병합
             * 외부에서 넣은 옵션이 있더라도 도넛-제목 정렬을 위해 필요한 설정들입니다.
             */
            return {
                ...cloned, // 외부에서 전달받은 옵션을 기본으로 함
                responsive: true,
                maintainAspectRatio: false,
                legend: {
                    ...cloned.legend,
                },
                title: {
                    ...cloned.title,
                    display: false // 플러그인에서 제목을 그리므로 기본 기능은 해제
                },
                layout: {
                    ...cloned.layout,
                    padding: {
                        top: 50, // 제목 공간
                        bottom: 20,
                        right: 10,
                        left: 10,
                        ...cloned.layout?.padding // 외부에서 패딩을 줬다면 덮어쓰기
                    }
                }
            }
        },
        renderMyChart() {
            // 항상 최신 props 값을 복제하여 렌더링
            const finalOptions = this._cloneOptionsPreservingFunctions(this.options)
            this.renderChart(JSON.parse(JSON.stringify(this.chartData)), finalOptions)
        }
    },
    mounted() {
        this.addPlugin(centerTextAndCustomTitlePlugin)
        this.renderMyChart()
    },
    watch: {
        // 데이터나 옵션이 변하면 다시 렌더링
        chartData: { deep: true, handler() { this.renderMyChart() } },
        options: { deep: true, handler() { this.renderMyChart() } },
    }
}

export const getChatbotDonutChart = () => ({ ...baseDoughnutConfig })
