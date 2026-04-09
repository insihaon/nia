import _ from 'lodash'

let instance = null

export default class Hook {
  static get instance() {
    return new Hook()
  }

  #timer = null
  #mouseDown = false
  #mouseMove = false
  #keyDown = false
  downKeyCount = 0
  constructor() {
    if (instance) {
      return instance
    }
    instance = window.hook = this

    this.self = this
    this.runTimers = {}

    const { self } = this

    window.onmousedown = function(e) {
      self.#mouseDown = true
    }

    window.onmouseup = function(e) {
      self.#mouseDown = false
    }

    function isValidateKey(e) {
      const { which, repeat, altKey, ctrlKey } = e
      return !altKey && !ctrlKey && repeat === false && ((which >= 48 && which <= 90) || which === 229)
    }

    window.onkeydown = function(e) {
      if (isValidateKey(e)) {
        self.downKeyCount++
        self.#keyDown = true
      }
    }

    window.onkeyup = function(e) {
      const evt = e
      _.debounce(() => {
        if (isValidateKey(evt) && --self.downKeyCount <= 0) {
          self.#keyDown = false
          self.downKeyCount = 0
        }
      }, 200)()
    }

    window.onmousemove = function(e) {
      self.#mouseMove = true
      clearTimeout(self.#timer)
      self.#timer = setTimeout(() => {
        self.#mouseMove = false
      }, 200)
    }
  }

  // get downKeyCount() {
  //   return this.downKeyCount
  // }

  isIdle() {
    return !this.#mouseDown && !this.#mouseMove && !this.#keyDown
  }

  run(fn, name, interval = 200) {
    const { self, runTimers } = this
    const timer = runTimers[name]

    clearTimeout(timer)
    if (!self.isIdle()) {
      runTimers[name] = setTimeout(self.run.bind(self, fn, name, interval), interval)
      return
    }

    delete runTimers[name]
    fn()
  }

  runThen(name, interval = 200) {
    const { self } = this
    return new Promise(resolve => {
      self.run(resolve, name, interval)
    })
  }
}
