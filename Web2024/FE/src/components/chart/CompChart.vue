<template>
  <div :class="{ [name]: true }">
    <ECharts
      ref="chart"
      class="chart"
      :loading="chartLoading"
      :option="chartOptions"
      :loading-opts="{ text: '조회 중입니다. 최대 1분정도 소요될 수 있습니다.' }"
      :events="[
        ['click', (e) => $emit('click', e )],
        ['dblclick', (e) => $emit('dblclick', e)],
      ]"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'CompChart'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { },
  extends: Base,
  props: {
    options: {
      type: Object,
      default() { return {} }
    },
    chartLoading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName
    }
  },
  computed: {
    chartOptions() {
      const defaultOptions = {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'bar'
          }
        ]
      }

      return this.options ?? defaultOptions
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
</style>
