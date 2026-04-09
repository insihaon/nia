export class Filter {
  constructor(constantsObject, parent, propertyName) {
    this.parent = parent
    this.propertyName = propertyName

    const array = this.parent.refVue.objectToArray(constantsObject)
    array.splice(0, 0, { text: '전체', code: 'All', index: 'All', value: 'All' })
    this._data = array.map(v => Object.assign({}, v, { selected: true }, { count: 0 }))
  }

  getObject() {
    return this._data
  }

  onItemClick(index) {
    (index === 'All' || index === 0) ? this.onAllClick() : this.onFilterClick(index)
  }

  onAllClick() {
    const val = !this._data[0].selected
    if (Array.isArray(this._data)) {
      this._data.forEach(item => {
        item.selected = val
      })
    } else {
      Object.keys(this._data).forEach(item => {
        this._data[item].selected = val
      })
    }
  }

  // resetFilter() {
  //   this._data.forEach(item => {
  //     item.selected = true
  //   })
  // }

  onFilterClick(index) {
    this._data[index].selected = !this._data[index].selected
    this._data[0].selected = this.getIsAllChecked()
    // this._data[0].selected = this.getIsAllChecked()
  }

  getIsAllChecked(data = this._data) {
    if (Array.isArray(data)) {
      return data.filter(item => item.value !== 'All').every(item => item.selected)
    } else {
      return Object.keys(data).filter(item => item !== 'All').every(item => data[item].selected)
    }
  }

  countRows(refData) {
    let sum = 0
    this._data.map(function(item) {
      if (item.index !== 'All' && !item.fnCount) item.count = 0
      else {
        if (item.index === 'All') item.count = refData.length
        else {
          if (item.fnCount instanceof Function && !!refData) {
            item.count = refData.filter(row => item.fnCount(row)).length
            sum += item.count
          } else {
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
    })
  }

  // prune(obj) {
  //   return _.filterDeep(obj, (v) => !_.isUndefined(v))
  // }
}

export default Filter
