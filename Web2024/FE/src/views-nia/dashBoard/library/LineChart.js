import { Line } from 'vue-chartjs'

export default {
    extends: Line,
    props: {
        cData: {
            type: Object,
            default: null
        },
        cOptions: {
            type: Object,
            default: null
        }
    },

    data() {
        return {

        }
    },
    methods: {
        chartUpdate() {
            this.$data._chart.update()
        },
        optionUpdate() {
            this.renderChart(this.cData, this.cOptions)
        },
        clear() {
            this.$data._chart.clear()
        }
    },

    mounted() {
        this.renderChart(this.cData, this.cOptions)
    },
    beforeDestroy() {
        if (this.$data._chart) {
            this.$data._chart.destroy()
        }
    }
}
