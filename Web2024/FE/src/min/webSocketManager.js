import { AppOptions } from '@/class/appOptions'
import { wait } from '@/utils'
import { getToken } from '@/utils/auth'
import { setInterval } from 'core-js'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs'
import CONSTANTS, { objectToArray } from './constants'
import moment from 'moment'
import store from '@/store'
import Vue from 'vue'

// eslint-disable-next-line no-constant-condition
const log = AppOptions.instance.useWsLog ? console.log : () => {}
const error = AppOptions.instance.debug === true ? console.error.bind(null) : () => { }

let instance = null
const retryMax = JSON.parse(process.env.VUE_APP_WS_RETRY_MAX || 30)
const retryInterval = 1000 * JSON.parse(process.env.VUE_APP_WS_RETRY_INTERVAL || 10)
const channels = objectToArray(CONSTANTS.channels).filter(channel => channel.enable === true)
// eslint-disable-next-line no-return-assign
channels.forEach(s => s.url = s.url || `/sub/${s.name}`)

class WebSocketManager {
  static get instance() {
    return new WebSocketManager()
  }

  constructor() {
    if (!getToken()) { return }
    if (instance) {
      return instance
    }
    instance = window.ws = this

    this.retry = 0
    this.stompClient = null
    this.messages = [] // temp
    this.heartbeats = []
    this.heartbeatsFail = {}
    this.heartbeatsTimer = null
    this.token = null
    this.enableStompLog = false
    this.eventListeners = []
    this.waitingEventListeners = []
    this.url = null // connect 시점에 url을 가져와야 정확하기 때문에 connect() 으로 옮김
  }

  isConnect() {
    return this.stompClient != null && this.stompClient.connected
  }

  async connect() {
    this.url = AppOptions.instance.wsUrl

    if (this.url.includes('/mock')) {
      error('ws connect error: /mock')
      return
    }

    if (AppOptions.instance.useWebsocket !== true) {
      error('ws connect error: useWebsocket=', AppOptions.instance.useWebsocket)
      return
    }

    const token = this.token = await getToken()
    if (!token) {
      error('ws connect error: token=', token)
      return
    }

    if (!this.isConnect()) {
      if (++this.retry > retryMax) {
        error(`최대 접속 시도 횟수 초과 오류. ${this.retry} 회 시도`)
        return
      }

      log(`[socket connect] 접속을 시도합니다. ${this.retry} 회 시도`)
      const { url } = this
      const socket = new SockJS(url)
      socket.onopen = () => log('[socket onopen]')
      socket.onmessage = e => log('[socket onmessage]', e.data)
      socket.onclose = this.onclose.bind(this)

      this.stompClient = Stomp.over(socket)
      this.stompClient.debug = this.enabletompLog ? this.stompClient.debug : () => {}
      this.stompClient.connect({ token }, onConnect.bind(this), onError.bind(this))

      // #region connect TEST
      // eslint-disable-next-line no-constant-condition
      if (false) {
        var jwt = getToken()
        var header = { 'token': jwt }
        log('-------before', header)
        var sock = new SockJS('http://127.0.0.1:8070/ws-stomp')
        var mystomp = Stomp.over(sock)
        mystomp.connect(header, onConnect.bind(this), onError.bind(this))
        log('-------after')
      }
      // #endregion
    } else {
      this.subscribe()
    }

    // eslint-disable-next-line no-inner-declarations
    function onConnect(frame) {
      log('Connected: ' + frame)
      this.retry = 0
      this.subscribe()
    }

    // eslint-disable-next-line no-inner-declarations
    function onError(error) {
      error(error)
      this.waitAndConnect()
    }
  }

  async waitAndConnect() {
    if (retryInterval === 0) {
      return
    }

    await wait(retryInterval).then(() => {
      this.disconnect()
      this.connect()
    })
  }

  stopHeartbeatCheck() {
    if (this.heartbeatsTimer) {
      clearInterval(this.heartbeatsTimer)
    }
  }

  startHeartbeatCheck() {
    this.stopHeartbeatCheck()

    let diff = 0 // 서버 로컬간 시간 차이
    const self = this
    this.heartbeatsTimer = setInterval(() => {
      const last = self.heartbeats[0]
      if (last) {
        const lastDate = new Date(new Date(last).getTime())
        const now = new Date(new Date().getTime() - diff)
        if (!diff) {
          diff = now.getTime() - lastDate.getTime()
        }
        // console.log(`diff=${diff}, (now - lastDate)=${now - lastDate}`)
        if (now - lastDate > 10 * 1000) {
          self.heartbeatsFail[last] = moment(now).format('YYYY-MM-DD HH:mm:ss')
          store.dispatch('app/setHeartbeatsFail', self.heartbeatsFail)
        }
      }
    }, 1000 * 10)
  }

  async runCommand(data) {
    const { message } = data
    const json = JSON.parse(message)
    const { command } = json

    if (!command) {
      return
    }

    switch (command) {
      case 'RELOAD':
        setTimeout(() => { window.location.reload() }, 50)
        break
      case 'LOGOUT':
        {
          const project = AppOptions.instance.project
          const { untact, uid } = store.getters
          const { subuserid, privilege, sender } = json
          if (project === 'untact') {
            if (subuserid === uid && privilege === untact.authority.privilege && sender !== this.stompClient.ws.url) {
              Vue.prototype.$alert('"동일계정 동시 로그인(다중접속)을 금지" 정책에 따라 로그아웃 됩니다.')
              await wait(5000)
              await store.dispatch('user/logout')
            }
          }
        }
        break
    }
  }

  subscribe() {
    this.startHeartbeatCheck()
    channels.forEach(channel => {
      if (!channel.name || this.isSubscribed(channel.name)) {
        return true
      }
      this.stompClient.subscribe(channel.url, (message) => {
        try {
          const data = JSON.parse(message.body)
          try {
            log(`>>> ${channel.name} received message, userCount=${data.userCount}, sessionCount=${data.sessionCount}\n`, JSON.parse(data.message))
          // eslint-disable-next-line no-empty
          } catch (error) {
          }

          switch (channel.name) {
            case 'COMMAND':
              this.runCommand(data)
              break
            case 'HEARTBEAT':
              {
                const timestamp = data.message
                const last = this.heartbeats[0] || timestamp
                this.heartbeats.unshift(timestamp)
                this.heartbeats.splice(10)
                if (new Date(last) - new Date(timestamp) > 10 * 1000) {
                  throw new Error(`Network Connect Detected (HEARTBEAT): lastTimestamp=${last}`)
                }
              }
              break
            default:
              if (CONSTANTS.channels[channel.name]) {
                this.messages.unshift({ 'type': data.type, 'sender': data.sender, 'message': data.message })
              } else {
                throw new Error(`Undefined subscribe destinations`, channel)
              }
          }

          this.fireEvent({
            channelName: channel.name,
            socketMessage: data
          })
        } catch (e) {
          error(e)
        }
      }, { id: channel.name })
    })

    this.addWaitingEventListeners()
  }

  async sendMessage(channelName = 'UNKNOWN', data = '', type = 'BROADCAST') {
    const token = this.token = await getToken()
    const destination = '/pub/emit'
    const headers = { 'token': token }
    const body = JSON.stringify({ type: type, channelName: channelName, sender: this.stompClient.ws.url, message: JSON.stringify(data) })
    this.stompClient.send(destination, headers, body)
  }

  async sendReload(data = {}) {
    this.sendMessage('COMMAND', { command: 'RELOAD', ...data })
  }

  async sendLogout(data = {}) {
    this.sendMessage('COMMAND', { command: 'LOGOUT', ...data, sender: this.stompClient.ws.url })
  }

  onclose() {
    log('[socket onclose] 서버와 접속이 끊어졌습니다.')
    this.disconnect()

    if (instance) {
      this.connect()
    }
  }

  disconnect() {
    if (this.stompClient !== null) {
      const subscriptions = this.stompClient.subscriptions
      Object.keys(subscriptions).forEach(subscription => {
        this.stompClient.unsubscribe(subscription)	// 구독해제, unsubscribe(id, headers)
      })

      const ws = this.stompClient.ws
      if (ws !== null) {
        ws.close()
      }

      this.stompClient.disconnect()
      this.stompClient = null
    }
  }

  addWaitingEventListeners(listener) {
    if (!this.stompClient) {
      return
    }

    this.waitingEventListeners.forEach(l => {
      this.addEventListener(l.channelName, l.eventHandler, l.owner)
    })
    this.waitingEventListeners.splice(0)
  }

  isSubscribed(channelName) {
    const subscriptions = this.stompClient.subscriptions
    return subscriptions[channelName]
  }

  addEventListener(channelName, eventHandler, owner) {
    if (!this.stompClient) {
      error('ws addEventListener error: stompClient=', this.stompClient)
      this.waitingEventListeners.push(listener)
      return
    }

    var listener = {
      channelName,
      eventHandler,
      owner
    }

    if (this.isSubscribed(channelName)) {
      this.eventListeners.push(listener)
    } else {
      this.waitingEventListeners.push(listener)
    }
  }

  removeEventListener(channelName, eventHandler, owner) {
    if (!this.isConnect()) return
    this.eventListeners = this.eventListeners.filter(function(item) {
      return !(channelName === item.channelName && eventHandler === item.eventHandler && (!owner || owner === item.owner))
    })
  }

  removeAllEventListener(owner) {
    if (!this.isConnect()) return
    if (!owner) return
    this.eventListeners = this.eventListeners.filter(function(item) {
      return !(owner === item.owner)
    })
  }

  dispatchEvent(event) {
    if (!this.isConnect()) return
    for (var i = 0; i < this.eventListeners.length; i++) {
      if (event.channelName === null || event.channelName === this.eventListeners[i].channelName) {
        this.eventListeners[i].eventHandler(event)
      }
    }
  }

  fireEvent(event) {
    this.dispatchEvent(event)
  }

  dispose() {
    if (instance) {
      instance.disconnect()
      instance = window.ws = null
    }
  }
}

// #region singleton TEST
// eslint-disable-next-line no-constant-condition
if (false) {
  const a = new WebSocketManager()
  const b = new WebSocketManager()
  if (a !== b) {
    debugger
  }
  WebSocketManager.dispose()
  log('after dispose : a === b', a === b, a, b)
  const c = new WebSocketManager()
  log('after c = new WebSocketManager()', a === b, a === c, a, b, c)
  debugger
}
// #endregion

// #region dispose TEST
// eslint-disable-next-line no-constant-condition
if (false) {
  new WebSocketManager()
  setTimeout(() => {
    WebSocketManager.dispose()
  }, 2000)
}
// #endregion

export default WebSocketManager
