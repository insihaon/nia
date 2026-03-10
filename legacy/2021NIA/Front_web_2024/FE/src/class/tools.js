import Cipher from '@/class/cipher'

const instance = null

class Tools {
  #cipher = null
  constructor() {
    if (instance) {
      return instance
    }
  }

  static get instance() {
    return new Tools()
  }

  get cipher() {
    if (this.#cipher == null) {
      this.#cipher = new Cipher()
    }
    return this.#cipher
  }
}

const tools = Tools.instance

if (process.env.NODE_ENV === 'development') {
  window.tools = tools
}
export default tools
