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
  } catch (e) {
    console.error(e)
    return window.atob(str)
  }
}

function generateUUID() {
  var d = new Date().getTime();
  var uuid = 'xxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = (d + Math.random() * 16) % 16 | 0;
      d = Math.floor(d / 16);
      var randomChar = (c === 'x' ? r : (r & 0x7) | 0x8).toString(16);
      return Math.random() > 0.5 ? randomChar.toUpperCase() : randomChar;
  });
  return uuid;
}

 class Encrypt {
  constructor(keys, data) {
    function generateKey() {
      return generateUUID()
    }

    this._key = generateKey()
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
    return rsa.encrypt(this._key)
  }

  get data() {
    return Tea.encrypt(this._data, this._key)
  }

  data_base64() {
    return Base64.encode(this.data)
  }

  decrypt(ciphertext, key) {
    return atou(Tea.decrypt(ciphertext, key))
  }
  
  // from tools
  static toEncrypt(data) {
    if(typeof data !== 'string') {
      data = JSON.stringify(data)
    }
    const encrypt = new Encrypt(null, data)
    const encryptText = encrypt.key + encrypt.data
    return encryptText
  }

  static encryptHttp(data = {}, encrypt = true, isDevProfile = false) {
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
    const key = encryptData.substring(0, 16)
    const encryptText = encryptData.substring(16, encryptData.length)
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

  static register(inst){
    const UUID_KEY = 'uuid'
    const STORAGE = window.localStorage
    const uuid = Encrypt.toDecrypt(STORAGE.getItem(UUID_KEY)) || `${String(Date.now())} ${generateUUID()}`
    const [d, u] = uuid.split(' ')
    if (Date.now() - Number(d) > 1000 * 60 * 60) {
      STORAGE.removeItem(UUID_KEY)
    } else {
      inst.uuid = u
      STORAGE.setItem(UUID_KEY, Encrypt.toEncrypt(uuid))
    }
    !!inst.uuid && (window[`.${inst.uuid}`] = inst)
  }

  // from tools
  static testEncryptToDecrypt() {
    const data = {
      name: 'kim',
      age: 22,
    }
    const encrypt = new Encrypt(null, JSON.stringify(data))
    const encryptText = encrypt.key + encrypt.data
    const key = encryptText.substring(0, 16)
    const decryptText = encrypt.decrypt(encryptText.substring(16, encryptText.length), key)
    console.log(decryptText)
  }

  static testEncryptToDecrypt2() {
    const data = {
      name: 'kim',
      age: 22,
    }
    const encryptText = Encrypt.toEncrypt(data)
    const decryptText = Encrypt.toDecrypt(encryptText2)
    console.log(decryptText)
  }

}

export default Encrypt