let instance = null

export class LocationHistory {
  static get instance() {
    return new LocationHistory()
  }

  #history = []
  constructor() {
    if (instance) {
      return instance
    }
    instance = window.locationHistory = this

    this.self = this
  }

  get last() {
    const history = this.#history
    return history[history.length - 2]
  }

  get current() {
    const history = this.#history
    return history[history.length - 1]
  }

  get h() {
    const history = this.#history
    return history.map(h => h.fullPath)
  }

  clear() {
    const history = this.#history
    return history.splice(0)
  }

  push(from) {
    const history = this.#history
    history.push(from)
  }

  pop() {
    const history = this.#history
    return history.splice(-1, 1)
  }

  update({ fromHistory, from, to }) {
    const history = this.#history
    if (fromHistory && history.length > 0) {
      this.pop()
    } else {
      this.pop()
      this.push(from)
      this.push(to)
    }
  }
}
