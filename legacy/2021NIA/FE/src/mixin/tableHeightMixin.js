var tableHeightMixin = {
  data() {
    return {
      tableHeight: 0,
    }
  },
  mounted() {
    this.updateTableHeight()
    window.addEventListener('resize', this.updateTableHeight)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateTableHeight)
  },
  methods: {
    updateTableHeight() {
      setTimeout(() => {
        const searchEl = this.$refs.searchCondition
        const containerEl = this.$refs.container
        const tableEl = this.$refs.tableContainer

        const searchConditionHeight = searchEl?.$el?.offsetHeight
        const containerHeight = containerEl?.$el?.offsetHeight

        if (searchConditionHeight && containerHeight && tableEl) {
          this.tableHeight = containerHeight - searchConditionHeight - 65 // 20: (margin, padding)
          tableEl.$el.style.height = `${this.tableHeight}px`
        }
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
