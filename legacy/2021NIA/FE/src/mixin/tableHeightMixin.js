var tableHeightMixin = {
  data() {
    return {
      tableHeight: 0,
    }
  },
  mounted() {
    this.updateTableHeight()
    // window.addEventListener('resize', this.updateTableHeight)
  },
  beforeDestroy() {
    // window.removeEventListener('resize', this.updateTableHeight)
  },
  methods: {
    updateTableHeight() {
      setTimeout(() => {
        const searchConditionHeight = this.$refs.searchCondition.$el.offsetHeight
        const containerHeight = this.$refs.container.$el.offsetHeight

        this.tableHeight = containerHeight - searchConditionHeight - 65 // 20: (margin, padding)
        this.$refs.tableContainer.$el.style.height = `${this.tableHeight}px`
      }, 0)
    },
  },
  watch: {
    // Watch for changes in search condition layout height if it changes dynamically
    tableHeight() {
      this.updateTableHeight()
    },
  },
}

export default tableHeightMixin
