import Cookies from 'js-cookie'
import Encrypt from '@/assets/libs/Encrypt.min'
import { AppOptions } from '@/class/appOptions'

const TOKEN_KEY = 'X-AUTH-TOKEN'
const IPSDN_TOKEN_KEY = 'X-AUTH-IP-TOKEN'
const INFO_KEY = 'X-AUTH-INFO'
const isUseCookie = process.env.VUE_USE_AUTH_COOKIE ?? true

export function getToken() {
  const data = isUseCookie ? Cookies.get(TOKEN_KEY) : window.sessionStorage.getItem(TOKEN_KEY)
  if (data) {
    return data
  } else {
    return ''
  }
}
export function getIpsdnToken() {
  const data = isUseCookie ? Cookies.get(IPSDN_TOKEN_KEY) : window.sessionStorage.getItem(IPSDN_TOKEN_KEY)
  if (data) {
    return data
  } else {
    return ''
  }
}

export function setToken(token) {
  token = AppOptions.instance.debug ? token : Encrypt.toEncrypt(token)
  isUseCookie ? Cookies.set(TOKEN_KEY, token) : window.sessionStorage.setItem(TOKEN_KEY, token)
}
export function setIpsdnToken(token) {
  token = AppOptions.instance.debug ? token : Encrypt.toEncrypt(token)
  isUseCookie ? Cookies.set(IPSDN_TOKEN_KEY, token) : window.sessionStorage.setItem(IPSDN_TOKEN_KEY, token)
}

export function removeToken() {
  return isUseCookie ? Cookies.remove(TOKEN_KEY) : window.sessionStorage.removeItem(TOKEN_KEY)
}

export function getInfo() {
  const data = Cookies.get(INFO_KEY)
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
  return Cookies.set(INFO_KEY, AppOptions.instance.debug ? info : Encrypt.toEncrypt(info))
}

export function removeInfo() {
  return Cookies.remove(INFO_KEY)
}

Object.assign(window, { getToken, removeToken, getInfo, removeInfo })
