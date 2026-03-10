import Vue from 'vue'
import Clipboard from 'clipboard'

function clipboardSuccess() {
  Vue.prototype.$message({
    message: 'Copy successfully',
    type: 'success',
    duration: 1500
  })
}

function clipboardError() {
  Vue.prototype.$message({
    message: 'Copy failed',
    type: 'error'
  })
}

export default function handleClipboard(text, event = null) {
  let el
  if (!event) {
    el = document.createElement('button')
    el.setAttribute('data-clipboard-text', text)
    event = new Event('click')
    event.delegateTarget = el
  } else {
    el = event.target
  }
  const clipboard = new Clipboard(el, {
    text: () => text
  })
  clipboard.on('success', () => {
    clipboardSuccess()
    clipboard.destroy()
  })
  clipboard.on('error', () => {
    clipboardError()
    clipboard.destroy()
  })

  clipboard.onClick(event)
}
