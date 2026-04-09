import Vue from 'vue'

let EventBus = window.eventBus
if (!EventBus) {
  EventBus = new Vue()
  window.eventBus = EventBus
}

export default EventBus
