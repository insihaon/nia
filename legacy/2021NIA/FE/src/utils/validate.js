/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'dev', 'dimmby']
  return valid_map.includes(str) || /^[a-zA-Z0-9]{6,}$/.test(str)
}

export function validateUsername(rule, value, callback) {
  if (!validUsername(value)) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}

export function validateNodeName(rule, value, callback) {
  if (value == null || value === '') {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}

export const rulesUsername = () => [
  { message: 'ID is required', required: true, trigger: 'blur' },
  { message: 'ID is not correct', validator: validateUsername, trigger: 'blur' }
]
export const rulesPassword = () => [
  { message: 'Password is required', required: true },
  { message: 'Password is not correct', pattern: /^.{3,16}$/ }
]
export const rulesRequire = (name) => [
  { required: true, message: `${name} is required` }
]
export const rulesDataType = (type) => [
  { message: `Invalid Type`, type: type }
]
export const rulesLength = (min = 0, max) => [
  { message: `Invalid Length`, min, max }
]
export const rulesNumber = () => [
  { message: 'Data is Only Number', pattern: /^[0-9]+$/ }
]
export const rulesTelephone = () => [
  { message: 'not in telephone number format.', pattern: /^\d{2,3}-\d{3,4}-\d{4}$/ }
]
export const rulesPhone = () => [
  { message: 'not in phone number format.', pattern: /^01(?:0|1|[6-9])-?([0-9]{3,4})-?([0-9]{4})$/ }
]
export const rulesEmptyEdit = () => [
  { message: '필수', required: true }
]
export const rulesProcessEdit = (colId) => [
  { message: `${colId === '_ack' ? '인지 관련사항을 입력해 주십시오.' : '마감 관련사항을 입력해 주십시오.'}`, required: true }
]
export const rulesCoreAlarmEdit = () => [
  { message: '필수', required: true }
]
export const rulesId = () => [
  { message: 'Do not use Korean', pattern: /^[a-zA-Z0-9]*$/ }
]
export const rulesProtocol = () => [
  { message: 'Data is only English', pattern: /^[a-zA-Z]*$/ }
]
export const rulesIp = () => [
  { message: 'Data is only IP', pattern: /^([0-9]{1,3})[.]([0-9]{1,3})[.]([0-9]{1,3})[.]([0-9]{1,3})$/ }
]
export const rulesNodeName = () => [
  { message: 'Please enter the node name', validator: validateNodeName, trigger: 'blur' }
]
