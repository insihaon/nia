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

        const searchConditionHeight = (searchEl?.$el?.offsetHeight || searchEl?.offsetHeight) + 60 /* mergin size */
        const containerHeight = containerEl?.$el?.offsetHeight || containerEl?.offsetHeight

        if (searchConditionHeight && containerHeight && tableEl) {
          this.tableHeight = containerHeight - searchConditionHeight - 65 // 20: (margin, padding)
          const tableElement = tableEl.$el ?? tableEl
          tableElement.style.height = `${this.tableHeight}px`
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
