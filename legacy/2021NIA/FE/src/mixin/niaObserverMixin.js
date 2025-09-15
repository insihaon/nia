import { mapState } from 'vuex'
import constants from '@/min/constants'

var niaObserverMixin = {
    data() {
        return {

        }
    },
    computed: {
        ...mapState({
            lastFocusModuleName: (state) => state.chatbot.lastFocusModule.name,
        }),
    },
    watch: {
        lastFocusModuleName(nVal, oVal) {
            if (typeof this.popupShowCommand === 'function') {
                for (const key of Object.keys(constants.nia.chatbotKeyMap)) {
                    if (constants.nia.chatbotKeyMap[key].parameterKey === this.name) {
                        if (nVal === this.name) {
                            this.popupShowCommand()
                            return
                        }
                    }
                }
            }
        }
    },
}

export default niaObserverMixin
