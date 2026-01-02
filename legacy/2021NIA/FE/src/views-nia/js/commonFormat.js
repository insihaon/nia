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

export function getModelType(row, col, value, index) {
  switch (row.ticket_type) {
    case 'ATT2': case 'NTT':
      return 'Model-A'
    case 'ATT2_AI': case 'NTT_AI':
      return 'Model-C'
    default:
      return '-'
  }
}

export function getAlarmType(row, col, value, index) {
  let result = ''
  switch (row.ticket_type) {
    case 'RT':
      result = '장애'
      break
    case 'FTT':
      result = '비장애'
      break
    case 'PF':
      result = '광레벨'
      break
    case 'ATT2': case 'ATT2_AI':
      result = '이상 트래픽'
      break
    case 'NTT': case 'NTT_AI':
      result = '유해 트래픽'
      break
    case 'NFTT':
      result = '장비부하장애'
      break
    case 'SYSLOG':
      result = 'SYSLOG'
      break
    default:
      break
  }
  return result
}
export function getTicketStatus(row, col, value, index) {
  let result = ''
  switch (value) {
    case 'INIT':
      result = '발생'
      break
    case 'FIN':
      result = '마감'
      break
    case 'AUTO_FIN':
      result = '자동 마감'
      break
    case null:
      result = '-'
      break
  }
  return result
}

export function getSopAiAccuracy(row, col, value, index) {
  let result = '-'
  switch (value) {
    case '0':
      result = '일치'
      break
    case '1':
      result = '불일치'
      break
    case null:
      result = '-'
      break
  }
  return result
}

export function getFormatByte(row, col, value, index) {
  const packetBytesInMbyte = value / (1024 * 1024) // set byte to Mbyte
  return packetBytesInMbyte.toLocaleString()
}

export function getFormatGbyte(row, col, value, index) { // set Gbyte
  const packetBytesInGbyte = value / (1024 * 1024 * 1024)
  const roundedGbyte = Math.round(packetBytesInGbyte * 100) / 100
  return roundedGbyte.toLocaleString()
}

export function getDecimalCalc(row, col, value, index) { // set Decimal point
  const packetPerSeconds = value.toLocaleString()
  return packetPerSeconds
}

export function makeAlertMessage(ticketData, isSop) {
  try {
    switch (ticketData.ticket_type) {
      case 'ATT2': case 'ATT2_AI':
        return `<span style="display: none">티켓ID: ${ticketData.ticket_id}</span><span style="display: none">티켓종류: ${ticketData.ticket_type}</span>이상트래픽 장애가 발생하였습니다.
        <span>노드: ${ticketData.node_nm}, 포트: ${ticketData.root_cause_porta} 장비에 대하여</span>
        <b style=color:red>집중경보</b>를 진행할 수 있습니다.`
      case 'NTT': case 'NTT_AI':
        return `<span style="display: none">티켓ID: ${ticketData.ticket_id}</span><span style="display: none">티켓종류: ${ticketData.ticket_type}</span>유해트래픽 장애가 발생하였습니다.
        해당 티켓에 대하여 <b style=color:red>집중경보</b>를 진행할 수 있습니다.`
      case 'RT': // 장애
        if (ticketData.alarmmsg === 'PORT_DOWN') {
          return `<span style="display: none">티켓ID: ${ticketData.ticket_id}</span><span style="display: none">티켓종류: ${ticketData.ticket_type}</span>PORT_DOWN 장애가 발생하였습니다.
            노드: ${ticketData.node_nm}, 포트: ${ticketData.root_cause_porta} 장비에 대하여
            <b style=color:red>집중경보</b>를 진행할 수 있습니다.`
        }
        break
      case 'SYSLOG':
        return `<span style="display: none">알람번호: ${ticketData.alarmno}</span><span style="display: none">티켓종류: ${ticketData.ticket_type}</span>Syslog 장애가 발생하였습니다.
        <span>노드: ${ticketData.node_nm}, 포트: ${ticketData.root_cause_porta} 장비에 대하여</span>
        <b style=color:red>집중경보</b>를 진행할 수 있습니다.`
    }

    throw new Error('유효하지 않은 TicketType')
  } catch (e) {
    console.error(e)
  }
}
