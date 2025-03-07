import './cipher/base64.js'
import './cipher/utf8.js'
import './cipher/rsa/jsbn.js'
import './cipher/rsa/prng4.js'
import './cipher/rsa/rng.js'
import Tea from './cipher/tea-block'
import RSAKey from './cipher/rsa/rsa'
import Base64 from './cipher/base64'


/**
 * Tiny Encryption Algorithm
 * https://m.blog.naver.com/PostView.nhn?blogId=higesan&logNo=80133787861&proxyReferer=https:%2F%2Fwww.google.co.kr%2F
 * https://link.springer.com/content/pdf/10.1007/3-540-60590-8_29.pdf
 *
 * XXTEA
 */

// base64 encoding using utf-8 character set
function utoa(str) {
  return window.btoa(unescape(encodeURIComponent(str)))
}

// Parse the base64 string using the utf-8 character set
function atou(str) {
  try {
    return decodeURIComponent(escape(window.atob(str)))
  } catch (e1) {
    // console.error(e)
    try {
      return window.atob(str)
    } catch (e2) {
      return ''
    }
  }
}

const idx = 5
const len = 8
const minLen = 16
const pad = 'utils/cipher/tea/TEA'.replaceAll('/', '.')

function generateUUID(len = 16) {
  const chars = "fcMBoPtdqn3xG9Jih50lv4j1gmyYSN7TCeFAHL2IUVOsK86z/WEpXQkDZwabuRr"; 
    let uuid = Array(len)
        .fill('x')
        .join('')
        .replace(/[xy]/g, function () {
            const randomIndex = Math.floor(Math.random() * chars.length);
            return chars[randomIndex];
        });
    return uuid;
}

function padKey(plainKey) {
  let key = plainKey;

  while (key.length < minLen) {
      key += pad;
  }

  return key.substring(0, minLen);
}

 class Encrypt {
  constructor(keys, data) {
    this._key = generateUUID(len)
    this._keys = keys
    this._data = utoa(data)
  }

  get key() {
    return this._key
  }

  set key(key) {
    this._key = key
  }

  get rsakey() {
    let rsa = new RSAKey()
    rsa.setPublic(this._keys.m, this._keys.e)
    return rsa.encrypt(padKey(this._key))
  }

  get data() {
    return Tea.encrypt(this._data, padKey(this._key))
  }

  data_base64() {
    return Base64.encode(this.data)
  }

  decrypt(ciphertext, key) {
    return atou(Tea.decrypt(ciphertext, padKey(key)))
  }
  
  // from tools
  static toEncrypt(data) {
    if(typeof data !== 'string') {
      data = JSON.stringify(data)
    }
    const encrypt = new Encrypt(null, data)
    const s = Math.min(encrypt.data.length, idx);
    const encryptText = encrypt.data.slice(0, s) + encrypt.key + encrypt.data.slice(s);
    // const decrypt = Encrypt.toDecrypt(encryptText)
    return encryptText
  }

  static encryptHttp(data = {}, encrypt = true) {
    let encryptText = null
    if (encrypt) {
      encryptText = Encrypt.toEncrypt(data)
      data = {
        data: encryptText || data,
      }
    } 
    data.encrypt = Encrypt.toEncrypt(encryptText != null)

    // const decrypt = Encrypt.toDecrypt(config.data.encrypt)
    // console.log(decrypt)
    return data
  }
  
  // from tools
  static toDecrypt(encryptData) {
    if (encryptData == null) {
      return null
    }

    const encrypt = new Encrypt(null, null)
    const s = idx;
    const e = s + len;
    const key = encryptData.substring(s, e);
    const encryptText = encryptData.substring(0, s) + encryptData.substring(e);
    const decrypt = encrypt.decrypt(encryptText, key)
    try {
      return JSON.parse(decrypt)
    } catch (error) {
      return decrypt
    }
  }

  static decryptHttp(res) {
    if (res != null && res.result != null && res.encrypt === true) {
      res.result = Encrypt.toDecrypt(res.result)
      res.encrypt = false
    }
    return res
  }

  static register(inst, opt){
    if(!inst) return
    const className = (inst && inst.constructor && inst.constructor.name)  || ''
    const varName = className.replace(/^\w/, (c) => c.toLowerCase())
    const UUID_KEY = `!${varName}`
    const STORAGE = window.localStorage
    let uuid = Encrypt.toDecrypt(STORAGE.getItem(UUID_KEY)) 
    
    if (uuid === null || Date.now() - Number(uuid.split(' ')[0]) > 1000 * 60 * 60) {
      uuid = `${String(Date.now())} ${generateUUID()}`
      STORAGE.setItem(UUID_KEY, Encrypt.toEncrypt(uuid))
    }

    const [d, u] = uuid.split(' ')
    if(u) {
      inst.uuid = u
      window[`.${inst.uuid}`] = inst
    }

    if(opt) {
      window[varName] = inst
    }
  }
}
export default Encrypt