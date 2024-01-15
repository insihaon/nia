
if (typeof Date.prototype.addSeconds === 'undefined') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.addSeconds = function(sec) {
    this.setSeconds(this.getSeconds() + parseInt(sec))
    return this
  }
}

if (typeof Date.prototype.addDays === 'undefined') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.addDays = function(days) {
    this.setDate(this.getDate() + parseInt(days))
    return this
  }
}

if (typeof Date.prototype.addMonth === 'undefined') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.addMonth = function(month = 0) {
    month = (this.getMonth() + parseInt(month))
    this.setMonth(month)
    return this
  }
}

if (typeof Date.prototype.dayStart === 'undefined') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.dayStart = function() {
    return new Date(this.getFullYear(), this.getMonth(), this.getDate())
  }
}

if (typeof Date.prototype.dayEnd === 'undefined') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.dayEnd = function() {
    return new Date(this.getFullYear(), this.getMonth(), this.getDate(), 23, 59, 59)
  }
}

if (typeof Date.prototype.monthStart === 'undefined') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.monthStart = function() {
    return new Date(this.getFullYear(), this.getMonth())
  }
}

if (typeof Date.prototype.monthEnd === 'undefined') {
  // eslint-disable-next-line no-extend-native
  Date.prototype.monthEnd = function() {
    return new Date(this.getFullYear(), this.getMonth() + 1).addSeconds(-1)
  }
}

// ////////////////////////////////////
// v.getWeekPeriod(week = 0)
// ////////////////////////////////////
// if (typeof Date.prototype.week === 'undefined') {
//   // eslint-disable-next-line no-extend-native
//   Date.prototype.week = function(prevWeeks = 0) {
//     var [startDay, endDay] = [new Date(moment().day('Sunday')).addDays(-7 * prevWeeks), new Date(moment().day('Saturday')).addDays(-7 * prevWeeks)]
//     return [moment(startDay).format('YYYY-MM-DD 00:00:00'), moment(endDay).format('YYYY-MM-DD 23:59:59')]
//   }
// }

// ////////////////////////////////////
// v.getMonthPeriod(month = 0)
// ////////////////////////////////////
// if (typeof Date.prototype.addMonth === 'undefined') {
//   // eslint-disable-next-line no-extend-native
//   Date.prototype.addMonth = function(months = 0) {
//     // this.setMonth(this.getMonth() + parseInt(months))
//     var now = new Date()
//     const startDay = new Date(now.getFullYear(), now.getMonth() + months)
//     const endDay = new Date(new Date(now.getFullYear(), now.getMonth() + 1 + months, 0).getTime() + 1000 * 60 * 60 * 24 - 1)
//     return [startDay, endDay]
//   }
// }

if (typeof Date.today === 'undefined') {
  Date.today = function(tick = 1000 * 60 * 60 * 24 - 1) {
    var now = new Date()
    return new Date(new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime() + tick)
  }
}

if (typeof String.capitalize === 'undefined') {
  // eslint-disable-next-line no-extend-native
  String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1)
  }
}

if (typeof String.prototype.trim === 'undefined') {
  // eslint-disable-next-line no-extend-native
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, '')
  }
}

if (typeof String.prototype.toCamel === 'undefined') {
  // "modified-time".toCamel() -> "modifiedTime"
  // eslint-disable-next-line no-extend-native
  String.prototype.toCamel = function() {
    return this.replace(/(\-[a-z])/g, function($1) { return $1.toUpperCase().replace('-', '') })
  }
}

if (typeof String.prototype.toKebab === 'undefined') {
  // "modifiedTime".toKebab() -> "modified-time"
  // eslint-disable-next-line no-extend-native
  String.prototype.toKebab = function() {
    return this.replace(/([A-Z])/g, function($1) { return '-' + $1.toLowerCase() })
  }
}

if (typeof String.prototype.toUnderscore === 'undefined') {
  // "modifiedTime".toUnderscore() -> "modified_time"
  // eslint-disable-next-line no-extend-native
  String.prototype.toUnderscore = function() {
    return this.replace(/([A-Z])/g, function($1) { return '_' + $1.toLowerCase() })
  }
}
