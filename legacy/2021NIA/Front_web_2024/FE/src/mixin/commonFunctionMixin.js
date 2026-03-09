import { onMessagePopup } from '@/utils/index'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'

var commonFunctionMixin = {
  data() {
    return {
      parameterKey: null,
      values: null,
      options: []
    }
  },
  computed: {
    fullOptions() { /* override */
      return []
    }
  },
  mounted() {
    this.init()
    Eventbus.$on(EventType.setSavedParameter, (params) => {
      this.setParameter(params)
    })
    Eventbus.$on(EventType.resetCondition, () => { this.onResetParameter() })
  },
  beforeDestroy() {
    Eventbus.$off(EventType.setSavedParameter)
    Eventbus.$off(EventType.resetCondition)
  },
  methods: {
    onResetParameter() { /* override */
      this.value = ''
    },
    init() { /* override */
      if (this.prop_parameterKey && this.prop_parameterKey !== null) {
        this.parameterKey = this.prop_parameterKey
      }
      if (this.prop_options && this.prop_options !== null) {
        this.options = this.prop_options
      }
      this.emitEventToParent(this.getParameter())
    },
    handleChange() { /* override */
      this.emitEventToParent(this.getParameter())
    },
    getParameter() {
      return this.parameterKey ? [{ key: this.parameterKey, value: this.value }] : []
    },
    emitEventToParent(params) { /* params : [{key, value}, {}...] */
      this.$emit('update-value', params)
    },
    /*
     multi option이 있는 selectBox 처리
    */
    updateSelectionWithAll() {
      if (this.values.length === this.fullOptions.length && !this.values.includes('')) {
        this.values.push('')
      } else if (this.values.includes('') && this.values.length !== this.fullOptions.length + 1) {
        this.values = this.values.filter(value => value !== '')
      }
    },
    toggleAll() {
      this.values = this.values.includes('') ? [] : ['', ...this.fullOptions]
    },
    handleDropdownVisibility(isVisible) {
      if (!isVisible) {
        // 드롭다운이 닫힐 때 실행
        if (this.limit !== null && this.values.length > this.limit) {
          this.$message.error({ message: `${this.limit}개까지 선택 가능합니다.` })
          this.$nextTick(() => {
            // 드롭다운을 다시 열기
            this.$refs.multiSelect?.toggleMenu()
            this.$store.dispatch('ipms/setDropdownVisibility', true)
          })
        } else {
          this.$store.dispatch('ipms/setDropdownVisibility', false)
        }
      }
    },
    setParameter(params) { /* override */
      this.value = params[this.parameterKey] ?? ''
    }
  },
}

export default commonFunctionMixin
