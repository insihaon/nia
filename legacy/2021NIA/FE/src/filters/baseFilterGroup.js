import BaseFilter from './baseFilter'

class BaseFilterGroup {
  filters = {}

  constructor(refVue, handlers = { onFilterChanged: null }) {
    this.self = this
    this.refVue = refVue
    this.handlers = handlers
  }

  load(config) {
    const THIS = this
    const filters = config?.filters
    if (!filters) {
      THIS.save()
    } else {
      filters.forEach(f => {
        const filter = THIS.getFilter(f.filterName)
        filter.load(f)
      })
    }
    setTimeout(() => {
      THIS.onFilterChanged()
    }, 500)
  }

  save() {
    const filters = []
    for (const key in this.filters) {
      if (this.filters[key]) {
        filters.push(Object.assign({}, this.getFilter(key).save()))
      }
    }
    return { filters }
  }

  getFilter(filterName) {
    return this.filters[filterName]
  }

  // init() {
  //   const { refVue, vueDataName, filters } = this
  //   if (vueDataName) {
  //     refVue.$watch(vueDataName, (n) => {
  //       Object.keys(filters).forEach(filterName => {
  //         filters[filterName].countRows(n)
  //       })
  //     })
  //   }
  // }

  addFilter(filterName, constants, options) {
    if (!this.filters[filterName]) {
      this.filters[filterName] = new BaseFilter({ constants, parent: this, filterName, options })
    }
  }

  removeFilter(filterName) {
    delete this.filters[filterName]
  }

  onItemClick(filterName, code) {
    this.filters[filterName].onItemClick(code)
  }

  onFilterChanged(changedFilterName, code) {
    this.handlers['onFilterChanged'] && this.handlers['onFilterChanged'](this, this.getFilter(changedFilterName), code)
  }

  getCurrentState() {
    const currentState = {}
    for (const key in this.filters) {
      if (this.filters[key].options.isMultiSelect) {
        const arr = this.filters[key].getArray().map(function(v) {
          return v.item === undefined ? { code: v.code, selected: v.selected } : { code: v.code, selected: v.selected, item: v.item }
        })
        currentState[key] = arr
      }
    }
    return currentState
  }
}

export default BaseFilterGroup
