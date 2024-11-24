import Cookies from 'js-cookie'
import Encrypt from '@/assets/libs/Encrypt.min'
import { AppOptions } from '@/class/appOptions'

const TOKEN_KEY = 'X-AUTH-TOKEN'
const IPSDN_TOKEN_KEY = 'X-AUTH-IP-TOKEN'
const INFO_KEY = 'X-AUTH-INFO'
const isLocalStorage = process.env.VUE_APP_AUTH_LOCAL
const STORAGE = isLocalStorage ? window.localStorage : window.sessionStorage
export function getToken() {
  const data = STORAGE.getItem(TOKEN_KEY)
  if (data) {
    return data
  } else {
    return ''
  }
}

export function getIpsdnToken() {
  const data = STORAGE.getItem(IPSDN_TOKEN_KEY)
  if (data) {
    return data
  } else {
    return ''
  }
}

export function setToken(token) {
  token = AppOptions.instance.debug ? token : Encrypt.toEncrypt(token)
  STORAGE.setItem(TOKEN_KEY, token)
}
export function setIpsdnToken(token) {
  token = AppOptions.instance.debug ? token : Encrypt.toEncrypt(token)
  STORAGE.setItem(IPSDN_TOKEN_KEY, token)
}

export function removeToken() {
  return STORAGE.removeItem(TOKEN_KEY)
}

export function getInfo() {
  const data = STORAGE.getItem(INFO_KEY)
  if (data) {
    const raw = data.startsWith('{')
    const info = raw ? JSON.parse(data) : Encrypt.toDecrypt(data)
    if (AppOptions.instance.debug !== raw) {
      setInfo(info)
    }
    return info
  } else {
    return ''
  }
}

export function setInfo(info) {
  info = AppOptions.instance.debug ? JSON.stringify(info) : Encrypt.toEncrypt(info)
  STORAGE.setItem(INFO_KEY, info)
}

export function removeInfo() {
  return STORAGE.removeItem(INFO_KEY)
}

Object.assign(window, { getToken, removeToken, getInfo, removeInfo })
