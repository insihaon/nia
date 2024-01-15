import Filter from './filter'
import TopasFilter from './topasFilter'
// import _ from 'lodash'

class FilterGroup {
  filters = {}
  filterData = {}

  constructor(refVue, propsName, name = 'Filter') {
    this.self = this
    this.refVue = refVue
    this.propsName = propsName
    this.name = name
    this.init()
  }

  get() {
    return this.filters
  }

  init() {
    const { refVue, propsName, filters } = this

    if (propsName) {
      refVue.$watch(propsName, (n) => {
        Object.keys(filters).forEach(key => {
          filters[key].countRows(n)
        })
      })
    }
  }
  addFilter(propertyName, constants, option) {
    // const { refVue } = this
    if (!this.filters[propertyName]) {
      if (this.name === 'Topas') {
        this.filterData = new TopasFilter(constants, this, propertyName, option)
      } else {
        this.filterData = new Filter(constants, this, propertyName)
      }
      this.filters[propertyName] = this.filterData
    }
  }

  removeFilter(propertyName) {
    delete this.filters[propertyName]
  }

  onFilterChanged() {
    // this.saveFilterState()
    this.refVue.checkedFilter = this.getFilterStateArr()
  }

  changeSetItemSelected(propertyName, index) {
    for (const key in this.filters) {
      if (this.filters[key].options.multiSingle === 'single') {
        if (key === propertyName && propertyName) {
          this.filters[key]._data.forEach(item => {
            item.index === index ? item.selected = true : item.selected = false
          })
        } else {
          this.filters[key]._data.forEach(item => { item.selected = false })
        }
      }
    }
  }

  // onClickOrgTree(newValue) {
  //   if (this.refVue.$refs.aggrid)
  //   this.refVue.$refs.aggrid.externalFilterChanged({ name: 'TopasFilter', data: this.filters, andFilterData: newValue })
  // }

  getCurrentState() {
    const currentState = {}
    for (const key in this.filters) {
      if (this.filters[key].options.multiSingle !== 'single') {
        const arr = this.filters[key]._data.map(function(v) {
          return v.item === undefined ? { code: v.code, selected: v.selected } : { code: v.code, selected: v.selected, item: v.item }
        })
        currentState[key] = arr
      }
    }
    return currentState
  }

  // saveFilterState() {
  //   const name = this.refVue.name
  //   var currentState = this.getCurrentState()
  //   const savedFilterState = JSON.parse(window.localStorage['savedFilterState'] || '{}')
  //   if (name) {
  //     // const filters = _.filter(this.filters, v => typeof v === 'object')
  //     savedFilterState[name] = currentState
  //     window.localStorage['savedFilterState'] = JSON.stringify(savedFilterState)
  //   }
  // }

  // loadFilterState() {
  //   const name = this.refVue.name
  //   const savedFilterState = JSON.parse(window.localStorage['savedFilterState'] || '{}')
  //   for (const key in this.filters) {
  //     if (this.filters[key].options.multiSingle !== 'single') {
  //       if (savedFilterState[name]) {
  //         const filterArr = this.filters[key]._data
  //         const filterState = savedFilterState[name][key]
  //         this.filters[key]._data = _.merge(filterArr, filterState)
  //       } else {
  //         for (const key in this.filters) {
  //           if (key) {
  //             this.filters[key].resetFilter()
  //           }
  //         }
  //       }
  //     }
  //   }
  // }

  getFilterStateArr() {
    const filters = this.filters
    const status = {}
    for (const key in filters) {
      if (filters[key]) {
        const checkedFilter = filters[key]._data.filter(d => d.selected)
        status[key] = checkedFilter.map(d => d.text)
      }
    }
    return status
  }

  removeFilterState() {
    window.localStorage.removeItem('savedFilterState')
  }

  resetFilterState(vueName) {
    const savedFilterState = {}
    savedFilterState[vueName] = null
    window.localStorage['savedFilterState'] = JSON.stringify(savedFilterState)
    // this.loadFilterState()
  }

  getArray(propertyName) {
    const { filters, refVue } = this
    return refVue.objectToArray(filters[propertyName])
  }
}

export default FilterGroup
