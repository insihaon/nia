import moment from 'moment'

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
export function getTicketType(row, col, value, index) {
  let result = ''
  switch (value) {
    case 'A':
      result = '이상트래픽'
      break
    case 'N':
      result = '유해트래픽'
      break
    case 'S':
      result = 'SYSLOG'
      break
    case 'F':
      result = '비장애'
      break
  }
  return result
}
