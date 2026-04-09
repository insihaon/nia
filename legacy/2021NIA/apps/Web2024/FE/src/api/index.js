import * as memebers from '@/api/member'
import * as post from '@/api/post'
import * as oasis from '@/api/oasis'
import * as oasisAlarm from '@/api/oasis-alarm'
import * as topas from '@/api/topas'
import * as rca from '@/api/rca'

const api = [
  memebers.group,
  post.group,
  oasis.group,
  oasisAlarm.group,
  topas.group,
  rca.group
]
export default api
