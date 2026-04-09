import Encrypt from '@/assets/libs/Encrypt.min'

class Cipher {
  keys = null

  setKeys(keys) {
    this.keys = keys
  }

  // rsa encrypt
  enc(text) {
    let data = text
    if (this.keys != null) {
      const encrypt = new Encrypt(this.keys, text)
      data = Object.assign({
        key: encrypt.rsakey,
        value: encrypt.data_base64()
      })
    }
    return data
  }
}

export default Cipher
