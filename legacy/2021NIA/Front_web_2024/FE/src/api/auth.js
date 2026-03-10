import http from '@/min/http'
import { getApiData } from '@/utils'
import tools from '@/class/tools'

export const _ = { tools }
export const { filePath, group } = getApiData(__filename, [])

// 외부 API 서버 테스트
// curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://127.0.0.1:8070/signin?id=dimmby&password=dimmby'
export async function apiLogin(data) {
  data.id = data.id ?? data.username
  // console.log(`apiLogin: data=`, data)
  data = await encrypt(data)
  return http({
    url: '/signin',
    method: 'post',
    filePath: filePath,
    sqlId: 'apiLogin',
    // params: data, // config.query
    data: data, // config.body
    encrypt: true
  })
}

export function apiAuthOtp(otp) {
  return http({
    url: '/otpCheck',
    method: 'post',
    filePath: filePath,
    data: { otp: otp } // config.body
  })
}

async function encrypt(data) {
  try {
    const response = await http.post('getkey')
    const { result } = response
    tools.cipher.setKeys(result)
    data = { ...tools.cipher.enc(JSON.stringify(data)), m: result.m }
  } catch (error) {
    console.error('인증 실패: ', error)
  }
  return { data: data }
}

export function apiGetInfo(token) {
  return http({
    url: '/user',
    method: 'get',
    filePath: filePath,
    sqlId: 'apiGetInfo',
    params: { token }
  })
}

export function apiLogout() {
  return http({
    url: '/signout',
    filePath: filePath,
    sqlId: 'apiLogout',
    method: 'post'
  })
}

/* RMS 회원가입 AuthController:L224 */
export async function apiSignUp(data) {
  data = await encrypt(data)
  return http({
    url: '/rms/signup',
    filePath: filePath,
    sqlId: 'apiSignUp',
    method: 'post',
    data: data
  })
}
/* NIA 회원가입 */
export async function apiNiaInsertUser(data) {
  data = await encrypt(data)
  return http({
    url: '/nia/insertUser',
    filePath: filePath,
    sqlId: 'apiUserInsert',
    method: 'post',
    data: data
  })
}
/* NIA 정보수정 */
export async function apiNiaUpsertUser(data) {
  data = await encrypt(data)
  return http({
    url: '/nia/upsertUser',
    filePath: filePath,
    sqlId: 'apiUserUpsert',
    method: 'post',
    data: data
  })
}
export async function apiNiaDeleteUser(data) {
  data = await encrypt(data)
  return http({
    url: '/deleteAccount',
    filePath: filePath,
    sqlId: 'apiUserDelete',
    method: 'post',
    data: data
  })
}

export async function apiChangeRmsUserPassword(data) {
  data = await encrypt(data)
  return http({
    url: '/rms/changepwd',
    filePath: filePath,
    sqlId: 'apiChangeRmsUserPassword',
    method: 'post',
    data: data
  })
}

export async function apiGetUserIDFromUserNm(param) {
  return http({
    url: '/nms/userinfo',
    filePath: filePath,
    sqlId: 'nmsuserinfo',
    method: 'post',
    data: param
  })
}

// unused
// export function getToken() {
//   return request({
//     url: '/token',
//     method: 'get'
//   })
// }
