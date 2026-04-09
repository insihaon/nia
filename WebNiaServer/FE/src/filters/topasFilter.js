export class TopasFilter {
  constructor(constantsObject, parent, propertyName, options = { multiSingle: 'multi', allItem: true }) {
    this.parent = parent
    this.propertyName = propertyName
    this.options = options

    const array = parent.refVue.objectToArray(constantsObject)
    if (options.allItem) {
      array.splice(0, 0, { text: '전체', code: '전체', index: 'All', value: 'All' })
    }
    this._data = array.map(v => Object.assign({}, v, { selected: false }, { count: 0 }))
    if (options.multiSingle === 'multi') {
      this._data[0].index === 'All' ? this._data[0].selected = true : this._data[0].selected = false
    } else if (options.multiSingle === 'single') {
      this._data = array.map(v => Object.assign({}, v, { count: 0 }))
    }
  }

  getObject() {
    return this._data
  }

  onItemClick(index) {
    const filters = this.parent.filters
    if (this.options.multiSingle === 'multi') {
      (this.options.allItem && (index === 'All' || index === 0)) ? this.onAllClick() : this.onFilterClick(index)
    } else {
      this.parent.changeSetItemSelected(this.propertyName, index)
      this.parent.refVue.$store.dispatch('rcaTicket/changeSelectedSingleButton', this._data[index])
    }
    this.parent.refVue.$refs.aggrid.externalFilterChanged({ name: 'TopasFilter', data: filters })
    this.parent.onFilterChanged()
  }

  onAllClick() {
    if (Array.isArray(this._data)) {
      this._data.forEach(item => {
        item.index !== 'All' ? item.selected = false : item.selected = true
      })
    }
  }

  // resetFilter() {
  //   this._data.forEach(item => {
  //     if (item.value === 0) item.selected = true
  //   })
  // }

  onFilterClick(index) {
    this._data[index].selected = !this._data[index].selected
    if (this.options.allItem) this._data[0].selected = this.getIsAllChecked()
  }

  getIsAllChecked(data = this._data) {
    if (Array.isArray(data)) {
      return !data.filter(item => item.value !== 'All').some(item => item.selected)
    } else {
      return !Object.keys(data).filter(item => item !== 'All').some(item => data[item].selected)
    }
  }

  countRows(refData) {
    let sum = 0
    this._data.map(function(item) {
      if (item.index !== 'All' && !item.fnCount) item.count = 0
      else {
        if (item.index === 'All') refData ? item.count = refData.length : ''
        else {
          if (item.fnCount instanceof Function && !!refData) {
            item.count = refData.filter(row => item.fnCount(row)).length
            sum += item.count
          } else {
            if (refData) {
              switch (item.fnCount) {
                case 'etc':
                  item.count = refData.length - sum
                  break

                default:
                  break
              }
            }
          }
        }
      }
    })
  }

  // prune(obj) {
  //   return _.filterDeep(obj, (v) => !_.isUndefined(v))
  // }
}

export default TopasFilter
