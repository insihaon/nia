import moment from 'moment'
import store from '@/store'

export function getMbaTimeStamp() {
  const currentMinute = new Date().getMinutes()
  let hours = new Date().getHours()
  let minute
  if (currentMinute > 5 && currentMinute <= 20) {
    minute = 0
  } else if (currentMinute > 20 && currentMinute <= 35) {
    minute = 15
  } else if (currentMinute > 35 && currentMinute <= 50) {
    minute = 30
  } else {
    if (currentMinute <= 5) {
      hours = -1
    }
    minute = 45
  }
  return moment().set({ 'hours': hours, 'minute': minute }).format('YYYY-MM-DD HH:mm:00')
}
