import Encrypt from '@/assets/libs/Encrypt'
import _ from 'lodash'
const { param2Obj, reload } = require('@/utils')
const SALT_KEY = '__SALT_KEY__'
const STORAGE_KEY = 'options'
const APP_DEV = getEnv(process.env.VUE_APP_DEV, false)
const NODE_ENV = process.env.NODE_ENV
const NODE_ENV_DEV = NODE_ENV === 'development'
const STORAGE = NODE_ENV_DEV ? window.localStorage : window.sessionStorage

export class Storage {
  reloadProperties = 'dark,project,baseURL'.split(/[\s,]+/)
  _data = {}
  constructor() {
    Encrypt.register(this, APP_DEV)
    this.self = this
    this._data = {
      isProd: NODE_ENV === 'production',
      debug: NODE_ENV_DEV && APP_DEV,
    }
  }

  get debugOrDev() {
    return this.debug || NODE_ENV_DEV
  }

  reset(options) {
    STORAGE.removeItem(STORAGE_KEY)
    if (options && Object.keys(options) > 0) this.update(options, true)
    else reload(true)
  }

  _defineProp = function () {
    for (const key in this._data) {
      Object.defineProperty(this, key, {
        get: function () {
          return this._data[key]
        },
        set: function (newValue) {
          this.update({ [key]: newValue })
        },
      })
    }
  }

  _save = function () {
    if (!APP_DEV) {
      return
    }
    const save = () => STORAGE.setItem(STORAGE_KEY, JSON.stringify(this._data))
    const save2 = () => STORAGE.setItem(STORAGE_KEY, Encrypt.toEncrypt(this._data))
    if (this.debug) {
      save()
    } else {
      save2()
    }
  }

  _load = function () {
    if (!APP_DEV) {
      return
    }
    const load = () => JSON.parse(STORAGE.getItem(STORAGE_KEY))
    const load2 = () => Encrypt.toDecrypt(STORAGE.getItem(STORAGE_KEY))
    let loaded
    try {
      loaded = load()
    } catch (error) {
      loaded = load2()
    }
    this._save()

    _.merge(this._data, loaded, param2Obj(location.search))
  }

  autoReload(newOptions) {
    const properties = this.reloadProperties
    if (JSON.stringify(newOptions, ['baseURL']) !== '{}') {
      this.updateUseWebSocket()
      this._save()
    }

    if (JSON.stringify(newOptions, properties) !== '{}') {
      const projectChange = !!newOptions.project
      reload(projectChange)
    }
  }

  update(newOptions = {}, reload = true) {
    Object.assign(this._data, newOptions)
    this._save()
    reload && this.autoReload(newOptions)
  }

  merge(newOptions = {}) {
    _.merge(this._data, newOptions)
    this._save()
    this.autoReload(newOptions)
  }

  readEnv(key, defalutValue) {
    return getEnv(key, defalutValue)
  }
}

function getEnv(key, defalutValue) {
  try {
    return  JSON.parse(key) || defalutValue
  } catch (error) {
    return defalutValue
  }
}
