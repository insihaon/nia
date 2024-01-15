import { LocationHistory } from '@/class/locationHistory'
import { AppOptions } from '@/class/appOptions'

let instance = null

/*
  * 모바일에서 backbutton 눌렀을 경우 fullscreen 모달을 닫히게 하고, 뒤로가기 이벤트를 무시한다
  * safari 와 chrome 가 다를 수 있다. chrome 기준으로 검증함
 */
export class ModalManager {
  static get instance() {
    return new ModalManager()
  }

  constructor() {
    if (instance) {
      return instance
    }
    instance = window.modalManager = this

    this.modals = {
      // [$route.path] : [ modal1, modal2 ...]
      // $route.path 값이 null 이거나, size가 0일 경우 back
    }
    const self = this.self = this

    window.onpopstate = function (e) {
      /*
        [#onpopstate 문제]
          현재 특정 상황에서 (ex: 페이지의 컴포넌트안의 버튼에서 이벤트를 받아서 모달을 호출하는 경우)
          모달을 열면 onpopstate가 자동으로 동작해서 모달이 자동으로 닫혀버리는 오류가 발생해서
          그런 경우에는 강제로 setTimeout을 줘서 1초 후에 visible을 true로 변경하는 설정을 해줬음.
          만약 이 오류의 정확한 원인을 파악해서 수정되었다면 [#onpopstate 문제]로 검색해서 관련코드를 삭제해야함.
      */
      e.preventDefault()
      e.stopPropagation()
      e.stopImmediatePropagation()

      const find = self.last()
      if (find) {
        self.pop(find)
        find.close()
      }
    }
  }

  // unused
  toArray() {
    const { modals } = this
    const array = []
    for (const key in modals) {
      const value = modals[key]
      value && array.push(...value)
    }
    return array
  }

  // backbutton 사용을 enable 하는 조건, 즉 true 반환 시에만 backbutton이 동작한다
  filter(modal) {
    if (!modal) return false

    // 모바일이 아닌 환경에서 테스트 하려면 true
    const isDesktopTest = AppOptions.instance.debug === true
    if (isDesktopTest) {
      return modal.visible
    } else {
      const { browserInfo } = modal
      return browserInfo && browserInfo.mobile && modal.visible
    }
  }

  last(path = LocationHistory.instance.current?.fullPath) {
    const { self, modals } = this
    const pathModals = modals[path] || []
    const find = pathModals.find(m => self.filter(m))
    return find
  }

  findModals(view) {
    const { path } = view.$route
    return this.modals[path] || []
  }

  findIndex(modal, path) {
    const { modals } = this
    path = path || modal.$route.path
    const pathModals = modals[path] || []
    const find = pathModals.findIndex(m => m === modal)
    return find
  }

  push(modal) {
    const { self, modals } = this
    const { path } = modal.$route
    const pathModals = modals[path] = modals[path] || []
    if (self.filter(modal) && self.findIndex(modal) < 0) {
      pathModals.splice(0, 0, modal)
      history.pushState(null, null, null)
      return true
    }
    return false
  }

  pop(modal, path) {
    const { self, modals } = this
    path = path || modal.$route.path
    const pathModals = modals[path] || []
    const index = self.findIndex(modal, path)
    // const $elModal = pathModals[index] ? pathModals[index].$el.getElementsByClassName('v-modal')[0] : null
    if (index >= 0) {
      // if ($elModal) $elModal.remove()
      return pathModals.splice(index, 1)
    }
    return null
  }
}
