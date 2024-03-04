const ALL = { text: '전체', code: 'All', value: 'All' }

function objectToArray(obj) {
  return Object.entries(obj).map(i => i[1])
}

export class BaseFilter {
  constructor({ constants, parent, filterName, filterTitle, options }) {
    this.filterGroup = parent
    this.filterName = filterName
    this.filterTitle = filterTitle
    this.options = options = Object.assign({ isMultiSelect: true, allItem: true, ifAllthenOtherUncheck: false, isCheckBox: true }, options)

    const array = objectToArray(constants)
    if (options.allItem) {
      if (array[0]?.code !== ALL.code) {
        array.splice(0, 0, ALL)
      }
    }
    this.dataArray = array.map(v => Object.assign({ selected: false, count: 0 }, v))
    if (options.allItem && options.isMultiSelect && this.isOtherNotChecked()) {
      this.onAllClick(true)
    }
    if (options?.listName) {
      this.countRows(parent.refVue[options.listName])
    }
  }

  load(config) {
    const { constants } = config
    const data = this.toObject(constants)
    this.getArray().forEach(c => {
      c.selected = data[c.code]?.selected ?? c.selected
    })
    // this.filterGroup.onFilterChanged(this.filterName)
  }

  save() {
    return {
      filterName: this.filterName,
      options: this.options,
      constants: this.getArray()
    }
  }

  toObject(array = this.getArray()) {
    return array.reduce((acc, cur, idx) => { acc[cur.code] = cur; return acc }, {})
  }

  getItem(code) {
    return this.getArray().filter(i => code === i.code)[0]
  }

  getArrayItem(index) {
    return this.dataArray[index]
  }

  getArray() {
    return this.dataArray
  }

  clearCheck() {
    this.getArray().forEach(item => { item.selected = false })
  }

  onItemClick(code) {
    if (!this.options.isMultiSelect) this.clearCheck()
    this.options.allItem && (code === ALL.code) ? this.onAllClick() : this.onOtherClick(code)
    if (this.options.allItem && this.options.ifAllthenOtherUncheck && this.isOtherNotChecked()) {
      this.getArrayItem(0).selected = true
    }
    this.filterGroup.onFilterChanged(this.filterName, code)
  }

  onAllClick(force) {
    if (!this.isCheckBox) this.getArrayItem(0).selected = !this.getArrayItem(0).selected

    const val = force ?? this.getArrayItem(0).selected
    this.getArray().forEach(item => {
      if (this.options.ifAllthenOtherUncheck) {
        item.code === ALL.code ? item.selected = val : item.selected = !val
      } else {
        item.selected = val
      }
    })
  }

  onOtherClick(code) {
    const [item] = this.getArray().filter(i => code === i.code)
    if (!item) return

    if (!this.isCheckBox) item.selected = !item.selected

    if (this.options.allItem) {
      if (this.isOtherAllChecked()) {
        if (this.options.ifAllthenOtherUncheck) this.clearCheck()
        this.getArrayItem(0).selected = true
      } else {
        this.getArrayItem(0).selected = this.isOtherAllChecked()
      }
    } else if (this.options.isMultiSelect === false) {
      this.getItem(code).selected = true
    }
  }

  isOtherNotChecked(data = this.dataArray) {
    return !data.filter(item => item.code !== ALL.code).some(item => item.selected)
  }

  isOtherAllChecked(data = this.dataArray) {
    return data.filter(item => item.code !== ALL.code).every(item => item.selected)
  }

  // topas 전용 코드. 나중에 baseFilter 를 상속 받아서 topasFilter에서 구현하면 됨
  // 지금은 filterGroup 에서 에러 안 나도록 프로토타입만 생성함
  // countRows() { }
  countRows(list) {
    let sum = 0
    this.dataArray.map(function(item) {
      if (item.code !== ALL.code && !item.fnCount) {
        item.count = 0
      } else {
        if (item.code === ALL.code) list ? item.count = list.length : ''
        else {
          if (item.fnCount instanceof Function && !!list) {
            item.count = list.filter(row => item.fnCount(row)).length
            sum += item.count
          } else {
            if (list) {
              switch (item.fnCount) {
                case 'etc':
                  item.count = list.length - sum
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

export default BaseFilter
