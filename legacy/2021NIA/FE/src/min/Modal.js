import { LocationHistory } from '@/class/locationHistory'
import { ModalManager } from '@/class/modalManager'
import { Base } from '@/min/Base.min'

const Modal = {
  extends: Base,
  props: {
    value: {
      default: () => { return {} }
    },
    fullscreen: {
      default: false
    }
  },
  data() {
    // const defaultQuery = {}
    // const defaultResult = {}
    return {
      name: 'modal',
      // query: Object.assign({}, this.query, defaultQuery),
      // result: Object.assign({}, this.result, defaultResult),
      // query: {},
      // result: {},
      domElement: Object.assign({}, this.domElement, { minWidth: 500, padding: 100 }),
      modal: true,
      visible: false,
      useAnimation: true,
      animation: 'bounceRight', // 'bounce', 'bounceDown', 'bounceLeft', 'bounceRight', 'bounceUp', 'fade', 'fadeDown', 'fadeDownBig', 'fadeLeft', 'fadeLeftBig', 'fadeRight', 'fadeRightBig', 'fadeUp', 'fadeUpBig', 'flip', 'flipX', 'flipY', 'rotate', 'rotateDownLeft', 'rotateDownRight', 'rotateUpLeft', 'rotateUpRight', 'slideDown', 'slideLeft', 'slideRight', 'slideUp', 'zoom', 'zoomDown', 'zoomLeft', 'zoomRight', 'zoomUp'
      modalAppendToBody: false, // Body 태그에 dialog를 붙여서 생성함. default: false, modal.js에서 제외하고 변경하지 말 것
      appendToBody: false, // 중첩된 다이얼로그 시 inner에 true를 세팅한다.
      tagviewIsTop: true, // 모달 오픈 상태에서 메뉴를 사용할 수 있게 최상위로 설정, default: true
      closeOnClickModal: true, // back 클릭 시 모달 닫힘 설정, default: true
      loading: false, // loading default: false
      rules: {},
      actionMode: null,
      C_ACTION_MODE: {
        detail: { code: 'detail', text: 'Detail Infomation', api: this.wait },
        update: { code: 'update', text: 'Edit', api: this.wait },
        create: { code: 'create', text: 'Create', api: this.wait },
        delete: { code: 'delete', text: 'Delete', api: this.wait },
        default: () => this.C_ACTION_MODE.update
      },
      C_DEFAULT_MODEL: {},
      topAlways: true
    }
  },
  watch: {
    fullscreen(n, o) {
      if (n === o) return
      // if (this.visible && this.name === this.$route.name) {

      if (this.visible) {
        this.changeOrderBackdrop(n, 'fullscreen')
      }
    },
    visible(n, o) {
      if (n === o) return

      // z-index 를 조정해서 tagviewIsTop = true 일 때 다른 메뉴로 전환이 가능하다.
      // 단, fullscreen 일 경우에는 불가능하도록 한다.

      if (n === true) {
        // this.saveViewData()  // 문제 없으면 삭제 예정
        this.activeView('visible changed')
      } else {
        // this.removeViewData()  // 문제 없으면 삭제 예정
        this.onClose()
        this.$store.dispatch('app/removeViewData', this.name)
        ModalManager.instance.pop(this)
        const parent = ModalManager.instance.last() || this.$parent
        parent && parent.activeView && parent.activeView(`child close`)
      }

      this.$emit('onVisibleChanged', n)
      this.changeOrderBackdrop(n && this.fullscreen, 'visible')
    }
  },
  created() {
    this.onCreated()
    this.setMaxWidth()
  },
  activated() {
    // this.trace(`Modal activated ${this.name}`)
  },
  deactivated() {
    // if (!this.visible) return
    // this.trace(`Modal deactivated ${this.name}`)
  },
  beforeDestroy() {
    // 주의, 여기서 this.$route 는 이미 변경된 값이다.
    // A.vue 창을 닫고 B.vue로 이동 할 경우 $route.path 는 B.vue가 되어 버린다.
    // 이런 이유로 이전 path을 다시 세팅해준다
    ModalManager.instance.pop(this, LocationHistory.instance.current.path)
    // this.removeViewData()  // 문제 없으면 삭제 예정
  },
  beforeRouteLeave(to, from, next) {
    // 호출안됨
    // this.trace(`Modal beforeRouteLeave ${this.name}`)
  },
  mounted() {
    // 생성 시 호출됨
    // this.trace(`Modal mounted ${this.name}`)
  },
  updated() {
    // data 변경 시 호출됨
    this.trace(`Modal updated ${this.name}`)
  },
  computed: {
    model: {
      get: function () { return this.result },
      set: function (newValue) { this.result = newValue }
    },
    dataForm() {
      // Vue Test Utils(VTU)로 Vue 컴포넌트 단위(Unit) 테스트  시 clearValidate 값이 undefined 로 발생하는 에러 방지
      const ref = this.$refs['dataForm']
      return Object.assign({ validate: this.wait, clearValidate: this.wait }, ref)
    },
    formWidth() {
      return `${this.domElement.minWidth - this.domElement.padding}px`
    },
    animationVisible() {
      return !this.useAnimation || this.visible
    },
    typeOf() {
      return 'Modal'
    },
    typeOfModal() {
      return true
    }
  },
  methods: {
    setMaxWidth() {
      if (this.isMobile || !this.domElement.maxWidth) {
        this.domElement.maxWidth = window.innerWidth
      }
    },
    bringToFront(options) {
      const fn = options.beforeClose
      return Object.assign(options, {
        beforeClose: (action, instance, done) => {
          const useAnimation = this.useAnimation
          this.useAnimation = false
          this.visible = false
          this.$nextTick(() => {
            this.visible = true
            this.useAnimation = useAnimation
            ModalManager.instance.push(this)
            this.$nextTick(() => {
              this.activeView()
              if (fn) fn(action, instance, done)
              else done()
            })
          })
        }
      })
    },
    changeOrderBackdrop(onTop, when) {
      if (this.topAlways) {
        return
      }
      const last = ModalManager.instance.last()
      if (!last) {
        onTop = false
      } else if (this !== last) {
        return
      }
      const zIndex = onTop ? '2000' : '1'
      const mainElement = document.querySelector('.app-main')
      mainElement && (mainElement.style.zIndex = zIndex)
      // const { name, visible, fullscreen } = this
      // this.trace(`changeOrderBackdrop: name=${name}, onTop=${onTop}, when=${when}, zIndex=${zIndex}, visible=${visible}, fullscreen=${fullscreen}`)
    },
    onCreated() {
      this.actionMode = this.C_ACTION_MODE.update
    },
    onActiveView() {
      this.changeOrderBackdrop(this.visible && this.fullscreen, 'onActiveView')
    },
    onDeactiveView() { /* for Override */ },
    activeView(when) {
      const self = this
      const { name, typeOf, _inactive } = self
      if (typeOf !== 'Modal' || !name || _inactive === true) return
      if (ModalManager.instance.last() !== this) return
      // if (!this.visible) return  // 사용하면 탭 전환 시 modal 위에 modal을 path 하게 된다. 그래서 위 코드로 대체함
      if (this.limitedDev) {
        this.onActiveView()
        return
      }

      this.debug && this.trace(`Modal activeView: name=${this.name}, when=${when}, _inactive=${_inactive}`)

      let v = window.v = this
      window.vd = { ...v.$data }
      window.vdq = { ...v.query }

      v = window.r = this.routeTo
      window.rd = { ...v.$data }
      window.rdq = { ...v.query }

      v = window.p = this.$parent && this.$parent.typeOf ? this.$parent : this
      window.pd = { ...v.$data }
      window.pdq = { ...v.query }

      this.onActiveView()

      // 모달은 아래에서 리턴된다.
      // 모달 active 시점과 fullsize 변경 시점에서 visible 변경시 일어나는 zindex 작업을 해 줘야 한다.
      // 단 위 작업은 보일 때만 해줘면 될 듯...
      // if (this.name !== this.$route.name) {
      //   return
      // }

      // console.log(modal, base, Base.methods.activeView)

      // Base.methods.activeView.apply(this, arguments)
    },
    getActionMode(code) {
      return this.C_ACTION_MODE[code] || this.C_ACTION_MODE.default()
    },
    open(model, actionModeCode = this.C_ACTION_MODE.default().code) {
      const actionMode = this.actionMode = this.getActionMode(actionModeCode)
      if (!model || actionMode === this.C_ACTION_MODE.create) {
        this.model = this.C_DEFAULT_MODEL
      } else {
        this.model = this._cloneDeep(model)
      }
      this.visible = true
      ModalManager.instance.push(this)
      this.clearValidate()
      this.onOpen(model, actionMode)
    },
    clearValidate() {
      this.$nextTick(() => {
        this.dataForm.clearValidate()
      })
    },
    close() {
      this.visible = false
    },
    onOpen(model, actionMode) { /* for Override */ },
    onClose() { /* for Override */ },
    validateAndRequest(actionMode = this.actionMode) {
      actionMode = typeof actionMode === 'string' ? this.getActionMode(actionMode) : actionMode
      this.dataForm.validate((valid) => {
        if (valid) {
          this.request(actionMode)
        }
      })
    },
    request(actionMode = this.actionMode) {
      actionMode = typeof actionMode === 'string' ? this.getActionMode(actionMode) : actionMode
      const data = Object.assign({}, this.model)
      const api = actionMode.api
      return api(data).then(() => {
        this.$emit('onDataChanged', actionMode.code, data)
        // v-model 과 동기화를 위해서는 아래 코드가 필요하다
        // this.$emit('input', data)
        this.close()
      })
    },
    loadViewData() {
      if (!this.openedViewData) return
      const data = this.openedViewData[this.name]
      if (data) {
        Object.assign(this.$data, data)
        this.open(data.model, data.actionModeCode)
      }
    },
    saveViewData() {
      this.$store.dispatch('app/putViewData', this.getViewData())
    },
    getViewData() {
      const { actionMode, model, src, query } = this
      return { [this.name]: this._cloneDeep({ actionMode, model, src, timestamp: new Date(), query }) }
    }
  }
}

export { Modal }

