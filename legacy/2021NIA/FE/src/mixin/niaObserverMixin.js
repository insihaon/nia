import { mapState } from 'vuex'
import constants from '@/min/constants'

var niaObserverMixin = {
    data() {
        return {

        }
    },
    computed: {
        ...mapState({
            lastFocusPopupName: (state) => state.chatbot.lastFocusPopup.name,
            currentMode: (state) => state.chatbot.currentMode,
        }),

        isFocusModeButNotFocus() {
            return this.currentMode === 'alarmFocusMode' && !this.$parent.isFocusWindow
        }
    },
    watch: {
        lastFocusPopupName(nVal, oVal) {
            if (typeof this.popupShowCommand === 'function') {
                if (this.isFocusModeButNotFocus) {
                    return
                }

                for (const key of Object.keys(constants.nia.chatbotKeyMap)) {
                    if (constants.nia.chatbotKeyMap[key].parameterKey === this.name) {
                        if (nVal === this.name) {
                            this.popupShowCommand()
                            return
                        }
                    }
                }
            } else {
                throw new Error('why not exist popupShowCommand')
            }
        }
    },
    methods: {

    }
}

export default niaObserverMixin
