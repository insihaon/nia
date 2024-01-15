import { AppOptions } from '@/class/appOptions'
import '@/utils/extend'
import Cookies from 'js-cookie'
import _ from 'lodash'
import moment from 'moment'
export const _var = { moment }

export function findIntersection(arr1, arr2) {
  return arr1.reduce((result, item) => {
      if (arr2.includes(item)) {
          result.push(item)
      }
      return result
  }, [])
}

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (typeof time === 'string') {
      if (/^[0-9]+$/.test(time)) {
        // support "1548221490638"
        time = parseInt(time)
      } else {
        // support safari
        // https://stackoverflow.com/questions/4310953/invalid-date-in-safari
        time = time.replace(new RegExp(/-/gm), '/')
      }
    }

    if (typeof time === 'number' && time.toString().length === 10) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    z: date.getMilliseconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{([ymdhisza])+}/g, (result, key) => {
    const value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['일', '월', '화', '수', '목', '금', '토'][value]
    }
    return value.toString().padStart(2, '0')
  })
  return time_str
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '30초 미만'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '분 미만'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '시간 미만'
  } else if (diff < 3600 * 24 * 2) {
    return '하루 미만'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '월' +
      d.getDate() +
      '일' +
      d.getHours() +
      '시' +
      d.getMinutes() +
      '분'
    )
  }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function getQueryObject(url) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * @param {string} input value
 * @returns {number} output value
 */
export function byteLength(str) {
  // returns the byte length of an utf8 string
  let s = str.length
  for (var i = str.length - 1; i >= 0; i--) {
    const code = str.charCodeAt(i)
    if (code > 0x7f && code <= 0x7ff) s++
    else if (code > 0x7ff && code <= 0xffff) s += 2
    if (code >= 0xdc00 && code <= 0xdfff) i--
  }
  return s
}

/**
 * @param {Array} actual
 * @returns {Array}
 */
export function cleanArray(actual) {
  const newArray = []
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i])
    }
  }
  return newArray
}

/**
 * @param {Object} json
 * @returns {Array}
 */
export function param(json) {
  if (!json) return ''
  return cleanArray(
    Object.keys(json).map(key => {
      if (json[key] === undefined) return ''
      return encodeURIComponent(key) + '=' + encodeURIComponent(json[key])
    })
  ).join('&')
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url) {
  const param = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!param) {
    return {}
  }
  const obj = {}
  const search = param.replace(/#\/.*$/, '')
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      try {
        obj[name] = JSON.parse(val)
      } catch (error) {
        obj[name] = val
      }
    }
  })
  return obj
}

/**
 * @param {string} val
 * @returns {string}
 */
export function html2Text(val) {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

/**
 * Merges two objects, giving the last one precedence
 * @param {Object} target
 * @param {(Object|Array)} source
 * @returns {Object}
 */
export function objectMerge(target, source) {
  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach(property => {
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

/**
 * @param {HTMLElement} element
 * @param {string} className
 */
export function toggleClass(element, className) {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString =
      classString.substr(0, nameIndex) +
      classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

/**
 * @param {string} type
 * @returns {Date}
 */
export function getTime(type) {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90
  } else {
    return new Date(new Date().toDateString())
  }
}

/**
 * @param {Function} func
 * @param {number} wait
 * @param {boolean} immediate
 * @return {*}
 */
export function debounce(func, wait, immediate) {
  let timeout, args, context, timestamp, result

  const later = function() {
    // 마지막 트리거 시간 간격에 따라
    const last = +new Date() - timestamp

    // 래핑 된 함수가 마지막으로 호출 된 시간이 설정된 시간 간격보다 작습니다
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // immediate===true로 설정하면 시작 경계가 이미 호출되었으므로 여기서 호출 할 필요가 없습니다
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function(...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 지연이없는 경우 지연을 재설정하십시오
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param {Object} source
 * @returns {Object}
 */
export const deepClone = _.cloneDeep
// export function deepClone(source) {
//   if (!source && typeof source !== 'object') {
//     throw new Error('error arguments', 'deepClone')
//   }
//   const targetObj = source.constructor === Array ? [] : {}
//   Object.keys(source).forEach(keys => {
//     if (source[keys] && typeof source[keys] === 'object') {
//       if(source[keys] instanceof Date) {
//         targetObj[keys] = new Date(source[keys].valueOf())
//       } else {
//         targetObj[keys] = deepClone(source[keys])
//       }
//     } else {
//       targetObj[keys] = source[keys]
//     }
//   })
//   return targetObj
// }

export function clone(obj) {
  return JSON.parse(JSON.stringify(obj))
}

/**
 * @param {Array} arr
 * @returns {Array}
 */
export function uniqueArr(arr) {
  return Array.from(new Set(arr))
}

/**
 * @returns {string}
 */
export function createUniqueString() {
  const timestamp = +new Date() + ''
  const randomNum = parseInt((1 + Math.random()) * 65536) + ''
  return (+(randomNum + timestamp)).toString(32)
}

/**
 * Check if an element has a class
 * @param {HTMLElement} elm
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele, cls) {
  return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}

/**
 * Add class to element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function addClass(ele, cls) {
  if (!hasClass(ele, cls)) ele.className += ' ' + cls
}

/**
 * Remove class from element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function removeClass(ele, cls) {
  if (hasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
    ele.className = ele.className.replace(reg, ' ')
  }
}

export function wait(ms = 0, success = true) {
  return new Promise((resolve, reject) => setTimeout(success ? resolve : reject, ms))
}

export function waitSuccess(value = { code: 20000 }, ms) {
  return wait(ms, true).then(() => value)
}

export function waitFail(value, ms) {
  return wait(ms, false).catch(() => value)
}

export function humanReadable(value, surfix = '', bp = 1024, fixed = 1) {
  var units = ['', 'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y'].map(
    (u) => u + surfix
  )
  var i = 0
  while (value >= bp) {
    value /= bp
    ++i
  }
  return value.toFixed(fixed) + ' ' + units[i]
}

export function humanReadableFileSize(size, bp = 1024) {
  var units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  var i = 0
  while (size >= bp) {
    size /= bp
    ++i
  }
  return size.toFixed(1) + ' ' + units[i]
}

export function humanReadableDuration(seconds) {
  if (seconds < 60) {
    return `${seconds}초`
  } else if (seconds > 60 && seconds < 60 * 60) {
    return `${parseInt(seconds / 60)}분`
  } else if (seconds > 60 * 60 && seconds < 60 * 60 * 24) {
    return `${parseInt(seconds / (60 * 60))}시`
  } else if (seconds > 60 * 60 * 24) {
    return `${parseInt(seconds / (60 * 60 * 24))}일`
  } else return `${seconds}`
}

export function humanReadableTime(sec) {
  const years = Math.floor(sec / 31536000)
  const days = Math.floor((sec % 31536000) / 86400)
  const hours = Math.floor(((sec % 31536000) % 86400) / 3600)
  const minutes = Math.floor((((sec % 31536000) % 86400) % 3600) / 60)
  const seconds = (((sec % 31536000) % 86400) % 3600) % 60
  return { years, days, hours, minutes, seconds }
}

export function exportToFile(data, filename = 'console.json', maxByte = 70 * 1024 * 1024) {
  return new Promise(resolve => {
    if (!data) {
      console.error('Console.save: No data')
      resolve()
    }

    if (typeof data === 'object') {
      let temp = JSON.stringify(data, undefined, 4)
      if (temp.length > maxByte) {
        try {
          // BcN Mock 수집 시 resultObject 에 데이터가 담겨오는 경우가 있어 처리한다.
          const result = Array.isArray(data.data.result) ? data.data.result : data.data.result.resultObject
          const resultRows = result.length
          const resultSize = temp.length
          try {
            const index = Math.floor(resultRows * maxByte / resultSize)
            result.splice(index)
          } catch (error) {
            result.splice(Math.min(100, Math.floor(resultRows * 0.1)))
          } finally {
            temp = JSON.stringify(data, undefined, 4)
          }
        } catch (e) {
          console.log(`${filename} 크기를 ${temp.length} => ${maxByte} 로 줄이는데 실패했습니다.`, data, e)
        }
      }
      data = temp
    }

    var blob = new Blob([data], { type: 'text/json' })
    var e = document.createEvent('MouseEvents')
    var a = document.createElement('a')

    a.download = filename
    a.href = window.URL.createObjectURL(blob)
    a.dataset.downloadurl = ['text/json', a.download, a.href].join(':')
    e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
    a.dispatchEvent(e)
    setTimeout(() => {
      resolve()
    }, 200)
  })
}

export function getApiData(fileName, apis) {
  const filePath = fileName.replace(/\\/g, '/')
  const prefix = filePath.replace(/\.\w+$/g, '').split('/').slice(-1).pop().capitalize()
  const group = createApiGroup(prefix, apis)

  return {
    filePath,
    group
  }
}

export function createApiGroup(name, apis) {
  return {
    id: name,
    label: name,
    children: apis.map(a => createApiNode(a))
  }
}

export function createApiNode(api) {
  return {
    id: api.name,
    label: api.name,
    fn: api
  }
}

const arrViewport = [
  { px: 0, type: 'xs' },
  { px: 768 - 1, type: 'sm' },
  { px: 992 - 1, type: 'md' },
  { px: 1200 - 1, type: 'lg' },
  { px: 1920 - 1, type: 'xl' }
]
var arrViewportReverse = arrViewport.slice().reverse()

export function pxToViewport(width) {
  width = width < 0 ? 0 : width
  return arrViewport.filter(i => i.px < width).slice(-1)[0].type
}

export function getViewportArr(operator, viewport) {
  if (['>', '<', '>=', '<=', '=='].includes(operator) === false) {
    throw new Error('정의된 연산자가 아닙니다.')
  }

  if (operator === '==') {
    return [viewport]
  }

  const hasEqual = operator.includes('=')
  const array = operator.includes('>') ? arrViewportReverse : arrViewport
  let skip = false
  return array.reduce((acc, item) => {
    const { type } = item

    if (type === viewport && hasEqual === false) {
      skip = true
    }

    if (skip === false) {
      acc.push(type)
    }

    if (type === viewport) {
      skip = true
    }

    return acc
  }, [])
}

// console.log('getViewportArr TEST 1', getViewportArr('>', 'sm'))
// console.log('getViewportArr TEST 2', getViewportArr('>=', 'sm'))
// console.log('getViewportArr TEST 3', getViewportArr('==', 'sm'))
// console.log('getViewportArr TEST 4', getViewportArr('<', 'sm'))
// console.log('getViewportArr TEST 5', getViewportArr('<=', 'sm'))

export function toJson(obj, filter = null, space = 2) {
  if (typeof obj === 'function') {
    obj = obj()
  }
  if (filter) {
    switch (typeof filter) {
      case 'string':
        filter = filter.split(/[\s,]+/)
        break
      case 'function':
        // filter = (key, value) => {}
        break
      default:
        if (filter instanceof RegExp) {
          const regExp = filter
          filter = (key, value) => {
            if (key) return regExp.test(key) ? value : undefined
            else return value
          }
        } else if (Array.isArray(filter)) {
          // filter = ['key1', 'key2']
        } else {
          filter = null
        }
        break
    }
  }
  return JSON.stringify(deepClone(obj), filter, space)
}

export function deepCloneFilter(obj, filter = null) {
  return JSON.parse(toJson(obj, filter))
}

export function simplize(obj, filter = null) {
  // Object 를 받아 filter 에 해당하는 필드로 deepClone 출력, 만약 필드 값이 undefined 인 경우 delete 됨
  return JSON.parse(toJson(obj, filter))
}

export function assert(value) {
  if (!value) {
    throw new Error('ASSERT', value)
  }
}

export function assertEquals(v1, v2) {
  if (v1 !== v2) {
    throw new Error('ASSERT', v1, v2)
  }
}

export function json2Csv(objArray, separator = ',', header = true, quote = false) {
  const array = typeof objArray !== 'object' ? JSON.parse(objArray) : objArray

  let str = ''
  let line = ''

  if (header) {
    if (quote) {
      for (const index in array[0]) {
        const value = index + ''
        line += '"' + value.replace(/"/g, '""') + `"${separator}`
      }
    } else {
      for (const index in array[0]) {
        line += index + `${separator}`
      }
    }

    line = line.slice(0, -1)
    str += line + '\r\n'
  }

  for (let i = 0; i < array.length; i++) {
    let line = ''

    if (quote) {
      for (const index in array[i]) {
        const value = array[i][index] + ''
        line += '"' + value.replace(/"/g, '""') + `"${separator}`
      }
    } else {
      for (const index in array[i]) {
        line += array[i][index] + `${separator}`
      }
    }

    line = line.slice(0, -1)
    str += line + '\r\n'
  }
  return str
}

export function sortObjectKeys(obj) {
  return Object.keys(obj).sort().reduce((acc, key) => {
    acc[key] = obj[key]
    return acc
  }, {})
}

export function getJsonfileName(url, config) {
  const { data, fileIndex } = config
  let { command, sqlId } = config
  const paramString = objectToStringKey(data)
  const { project } = AppOptions.instance
  let fileName = `${url.replace(/^\/(bcn\/)?/, '').replace(/\//gi, '_')}$${paramString}.json`

  if (project === 'untact') {
    if (sqlId?.includes('selectRcaUntactTicketList')) {
      // 실제파일명: rcaRequest_selectList#selectRcaUntactTicketList$.json
      // 반환파일명: rcaRequest_selectList_selectRcaUntactTicketList#selectRcaUntactTicketList$.json 파일을 찾을 수 없습니다.
      // url '/rcaRequest/selectList/selectRcaUntactTicketList'
      // sqlId 'selectRcaUntactTicketList'
      // command 'rcaRequest_selectList'
      console.log()
    }

    if (/(topasRequest|rcaRequest)$/.test(command)) {
      command = `${command}_${sqlId}`
    }

    if (/(topasRequest|rcaRequest).*$/.test(command)) {
      sqlId = (sqlId || '').replace('.json', '')
      fileName = `${command}#${sqlId || ''}$${paramString}.json`
    } else {
      fileName = `${url}$${''}.json`
    }
  }

  if (window.fileIndex && fileIndex !== undefined) {
    const index = Number(fileIndex).toString().padStart(4, '0')
    fileName = `[${window.ejbFile}.${index}]${fileName}`
  }

  return encodeURI(fileName)
}

export function objectToStringKey(obj = {}) {
  return Object.values(sortObjectKeys(obj)).map(value => {
    try {
      if (!value) return null
      else if (typeof value === 'function') return null

      else if (typeof value === 'object' && !Array.isArray(value)) {
        if (value.map) {
          value = JSON.stringify(value.map).replace(/:/g, 'ZZZZ').replace(/,/g, 'YYYY').replace(/\W/g, '').replace(/YYYY/g, ',').replace(/ZZZZ/g, '=')
        } else return null
      }
      const arr = String(value).match(/[^\s$%#@]+/g) || []
      return arr.join('').substring(0, 200)
    } catch (error) {
      console.error(error)
      return null
    }
  }).filter(value => value !== null).join('%').substring(0, 2000)
}

export function humanNumber(number) {
  if (!number) {
    return 0
  }
  let x = null
  let x1 = null
  let x2 = null
  if (typeof number === 'number') {
    number = (String)(number)
  }

  // number = number.toFixed(2) + ''
  x = number.split('.')
  x1 = x[0]
  x2 = x.length > 1 ? '.' + x[1] : ''
  var rgx = /(\d+)(\d{3})/
  while (rgx.test(x1)) {
    x1 = x1.replace(rgx, '$1' + ',' + '$2')
  }
  return x1 + x2
}

export function changeDownloadFileHost(url, host) {
  if (!url) return null
  // url = url || 'http://127.0.0.1:8070/downloadFile/20210805/apiSelectOasisTicketList%23selectOasisTicketList$.json'
  const path = url.replace(/(http|https):\/\/[^/]+/, '')
  return `${host || AppOptions.instance.baseURL || location.origin}${path}`
}

export const _isEqual = _.isEqual // object_equals, array_equals 을 대체 가능할 것 같음
export const array_equals = object_equals

export function object_equals(x, y) {
  if (x === y) return true
  // if both x and y are null or undefined and exactly the same

  if (!(x instanceof Object) || !(y instanceof Object)) return false
  // if they are not strictly equal, they both need to be Objects

  if (x.constructor !== y.constructor) return false
  // they must have the exact same prototype chain, the closest we can do is
  // test there constructor.

  for (var p in x) {
    // eslint-disable-next-line no-prototype-builtins
    if (!x.hasOwnProperty(p)) continue
    // other properties were tested using x.constructor === y.constructor

    // eslint-disable-next-line no-prototype-builtins
    if (!y.hasOwnProperty(p)) return false
    // allows to compare x[ p ] and y[ p ] when set to undefined

    if (x[ p ] === y[ p ]) continue
    // if they have the same strict value or identity then they are equal

    if (typeof (x[ p ]) !== 'object') return false
    // Numbers, Strings, Functions, Booleans must be strictly equal

    if (!object_equals(x[ p ], y[ p ])) return false
    // Objects and Arrays must be tested recursively
  }

  for (p in y) {
    // eslint-disable-next-line no-prototype-builtins
    if (y.hasOwnProperty(p) && !x.hasOwnProperty(p)) { return false }
  }
  // allows x[ p ] to be set to undefined

  return true
}

export function safeArray(array) {
  return array ?? []
}

export function safeString(string) {
  return string ?? ''
}

export function safeObjcet(object) {
  return object ?? {}
}

export function exceptionLoginFail(parent) {
  parent.$alert('계정이 존재하지 않거나 아이디 또는 비밀번호가 정확하지 않습니다.', '로그인', {
    confirmButtonText: 'OK'
  }).then(() => {
    setTimeout(() => {
      parent.loading = false
    }, 500)
  })
}

export function exceptionWarning(parent, param) {
  const num = param === '마감' ? '10' : '8'
  const warningText = param === '인지' ? `내용 메시지를 ${num}자 이상 입력하세요` : `메시지를 ${num}자 이상 입력하세요`
  parent.$alert(`${param} ${warningText}`, '메시지 창', {
    confirmButtonText: '확인'
  }).then(() => {
    setTimeout(() => {
      parent.loading = true
    }, 500)
  })
}

export function exceptionAuthWarning(parent) {
  parent.$alert(`권한 신청중입니다. 지역권한은 집중운용센터
    전국권한은 망관제 센터에 문의하시기 바랍니다.`, '메시지 창', {
    confirmButtonText: '확인'
  }).then(() => {
    setTimeout(() => {
      parent.loading = true
    }, 500)
  })
}

export function alertInputOtpInfo(parent) {
  parent.$prompt('Please input OTP', 'OTP', {
    confirmButtonText: 'OK',
    cancelButtonText: 'Cancel'
  })
}

export function onDownloadChrome(projectName) {
  var path = ''
  if (projectName === 'oasis') path = 'http://' + window.location.host + '/v1/downloadFile/static/'
  else if (projectName === 'untact') path = window.location.protocol + '//' + window.location.host + '/external/static/'

  var a = document.createElement('A')
  var fileName = 'ChromeStandaloneSetup'

  if ((navigator.userAgent.indexOf('WOW64') !== -1 ||
  navigator.userAgent.indexOf('Win64') !== -1)) {
    fileName = fileName + '64' + '.exe'
  } else {
    fileName = fileName + '.exe'
  }
  a.href = path + fileName
  a.download = fileName

  AppOptions.instance.debug && console.log('DownloadUrl : ', a.href)

  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
}
/**
 * APP를 다시 로드 한다
 * @param  {boolean} logout 로그아웃 수행
 */
export function reload(logout = false) {
  setTimeout(() => {
    if (logout) {
      Cookies.remove('X-AUTH-TOKEN')
      window.location.href = window.location.origin
    } else {
      window.location.reload()
    }
  }, 50)
}

export function onDownloadSopImage(name) {
  const path = 'http://' + window.location.host + '/v1/downloadFile/sop/'

  var a = document.createElement('A')
  var fileName = name + '.png'

  a.href = path + fileName
  a.download = fileName

  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
}

export function isEmptyObject(object) {
  return Object.keys(object).length === 0 && object.constructor === Object
}

export function getDateTimeFormatter(format = 'YYYY-MM-DD HH:mm:ss') {
  return function formatterDateTime(row, col, value, index) {
    value = value instanceof Date ? value : new Date(value)
    return moment(value).format(format)
  }
}

