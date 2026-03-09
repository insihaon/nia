import moment from 'moment'

export function getTagExpiredDate() {
  return moment(new Date().setHours(new Date().getHours() + 1)).format('YYYY-MM-DD HH:mm:ss')
}

export function getDateTimeFormatter(format = 'YYYY-MM-DD HH:mm:ss') {
  return function formatterDateTime(row, col, value, index) {
    if (!value) return null

    value = value instanceof Date ? value : new Date(value)
    return moment(value).format(format)
  }
}

export function formatterTime(value, fmt = 'YYYY-MM-DD HH:mm:ss') {
  if (value) {
    return moment(value).format(fmt)
  } else return ''
}

export function defaultZero(row, col, value, index) {
  return value || 0
}

