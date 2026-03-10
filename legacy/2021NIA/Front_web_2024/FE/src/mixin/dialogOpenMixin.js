import constants from '@/min/constants'
import store from '@/store'

export const chatbotTitle = '어시스턴트'

var dialogOpenMixin = {
  data() {
    return {
      dialogPage: {
        dialogNm: '',
        component: null,
        top: null,
        width: null,
        visible: false,
        fullscreen: false,
        center: false
      },
      dialogList: {
        aiResponse: {
          component: () => import('@/views-nia/dashBoard/popup/aiResponse/index'),
          pageTitle: 'AI 장애대응',
          width: '1100',
          height: '750',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.aiResponse.parameterKey
        },
        aiResponse_ATT_AI: {
          component: () => import('@/views-nia/dashBoard/popup/aiResponse/aiResponse_ATT_AI'),
          pageTitle: 'AI 이상트래픽 장애대응',
          width: '1100',
          height: '750',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.aiResponse_ATT_AI.parameterKey
        },
        aiResponse_NTT_AI: {
          component: () => import('@/views-nia/dashBoard/popup/aiResponse/aiResponse_NTT_AI'),
          pageTitle: '유해트래픽 AI 장애대응',
          width: '1800',
          height: '800',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.aiResponse_NTT_AI.parameterKey
        },
        requestForAction: {
          component: () => import('@/views-nia/dashBoard/popup/requestForAction/index'),
          pageTitle: '상황전파',
          width: '1200',
          height: '1100',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.requestForAction.parameterKey
        },
        processFin: {
          component: () => import('@/views-nia/dashBoard/popup/processFin/index'),
          pageTitle: '마감',
          width: '600',
          height: '700',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.processFin.parameterKey
        },
        configTest: {
          component: () => import('@/views-nia/dashBoard/popup/configTest/index'),
          pageTitle: '조치',
          width: '750',
          height: '660',
          resizeble: true,
          chatbotParameterKeyName: 'configTest'
        },
        snapShot: {
          component: () => import('@/views-nia/dashBoard/popup/snapShot/index'),
          pageTitle: '데이터 스냅샷',
          width: '800',
          height: '310',
          resizeble: true,
        },
        sopHistory: {
          component: () => import('@/views-nia/pages/alarmMonitoring/sopHistory'),
          pageTitle: 'SOP 이력 조회',
          width: '1200',
          height: '900',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.sopHistory.parameterKey
        },
        selfProcessList: {
          component: () => import('@/views-nia/dashBoard/popup/selfProcessList/index'),
          pageTitle: '자가 최적화 이력조회',
          width: '1000',
          height: '800',
          resizeble: true,
        },
        ticketDetail: {
          component: () => import('@/views-nia/dashBoard/popup/ticketDetail/index'),
          pageTitle: '전표 상세내역',
          width: '1200',
          height: '770',
          resizeble: true,
        },
        jsonSettingPopup: {
          component: () => import('@/test/popup/jsonSettingPopup'),
          pageTitle: 'jsonSettingPopup',
          width: '1200',
          height: '650',
          resizeble: true
        },
        niaTopology: {
          component: () => import('@/views-nia/dashBoard/popup/niaTopology/index'),
          pageTitle: '토폴로지',
          width: '1200',
          height: '785',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.niaTopology.parameterKey
        },
        systemMonitoringFilter: {
          component: () => import('@/views-nia/layout/sideBar/popup/systemMonitoringFilter'),
          pageTitle: '시스템 모니터링 필터',
          width: '600',
          height: '380',
          resizeble: true,
        },
        disabilityStatusHistoryManagement: {
          component: () => import('@/views-nia/pages/alarmMonitoring/disabilityStatusHistoryManagement'),
          pageTitle: '장애이력',
          width: '1200',
          height: '900',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.parameterKey
        },
        chatbot: {
          component: () => import('@/views-nia/dashBoard/popup/chatbot/index'),
          pageTitle: chatbotTitle,
          width: '600',
          height: '900',
          resizeble: true,
          positionBottomLeft: true,
          notDuplicate: true
        },
      }
    }
  },
  methods: {
    fn_openWindow(dialogNm, data, callback, customStyle = {}) {
      var tmpWindowData = Object.assign({}, store.getters.window_param)

      tmpWindowData.id = customStyle.id || new Date().getTime()
      tmpWindowData.name = customStyle.name || this.dialogList[dialogNm]['pageTitle']
      tmpWindowData.target = this.dialogList[dialogNm]['component']
      tmpWindowData.dialogNm = customStyle.dialogNm || dialogNm
      tmpWindowData.type = customStyle.type || this.dialogList[dialogNm]
      tmpWindowData.width = customStyle.width || (this.isMobile ? window.innerWidth : this.dialogList[dialogNm]['width'])
      tmpWindowData.height = customStyle.height || this.dialogList[dialogNm]['height']
      tmpWindowData.minWidth = customStyle.minWidth || this.dialogList[dialogNm]['minWidth']
      tmpWindowData.minHeight = customStyle.minHeight || this.dialogList[dialogNm]['minHeight']
      tmpWindowData.resizeble = customStyle.resizeble || this.dialogList[dialogNm]['resizeble']
      tmpWindowData.notDuplicate = customStyle.notDuplicate || this.dialogList[dialogNm]['notDuplicate']
      tmpWindowData.callback = callback || null
      tmpWindowData.isModal = true
      tmpWindowData.chatbotParameterKeyName = this.dialogList[dialogNm]['chatbotParameterKeyName'] || dialogNm

      if (this.dialogList[dialogNm]['positionBottomRight']) {
        tmpWindowData.x = window.innerWidth - tmpWindowData.width - 10
        tmpWindowData.y = window.innerHeight - tmpWindowData.height - 60
      } else {
        tmpWindowData.x = (window.innerWidth - tmpWindowData.width) * 0.5 + (this.$store.getters.windows.length - 1) * 20
        tmpWindowData.y = (window.innerHeight - tmpWindowData.height) * 0.5 + (this.$store.getters.windows.length - 1) * 20
      }

      if (this.dialogList[dialogNm]['positionBottomLeft']) {
        tmpWindowData.x = 10
        tmpWindowData.y = window.innerHeight - tmpWindowData.height - 60
      } else {
        tmpWindowData.x = (window.innerWidth - tmpWindowData.width) * 0.5 + (this.$store.getters.windows.length - 1) * 20
        tmpWindowData.y = (window.innerHeight - tmpWindowData.height) * 0.5 + (this.$store.getters.windows.length - 1) * 20
      }

      if (tmpWindowData.x < 0) {
        tmpWindowData.x = 15
      }
      if (this.isMobile) {
        tmpWindowData.x = 0
      }
      if (tmpWindowData.y < 0) {
        tmpWindowData.y = 85
      }

      if (customStyle.addX) tmpWindowData.x = tmpWindowData.x + customStyle.addX
      if (customStyle.addY) tmpWindowData.y = tmpWindowData.y + customStyle.addY

      tmpWindowData.params = Object.assign({}, data)

      const chatbot = store.state.mdi.windows.find((w) => w.name === chatbotTitle)
      if (chatbot) {
        const chatbotWidth = Number(chatbot.width) + 20
        if (tmpWindowData.x < chatbotWidth) tmpWindowData.x = chatbotWidth
      }

      this.$store.dispatch('mdi/addWindow', tmpWindowData)
    },

    fn_openDialog(dialogNm, pageTitle, fullscreen, center, callback) {
      try {
        this.dialogPage = this.dialogList[dialogNm]
        this.dialogPage.dialogNm = dialogNm
        this.dialogPage.fullscreen = !!fullscreen
        this.dialogPage.fullscreen = this.isMobile ? true : this.dialogPage.fullscreen
        this.dialogPage.center = !!center
        this.dialogPage.callback = callback || null
        if (pageTitle) {
          this.dialogPage.pageTitle = pageTitle
        }

        this.dialogPage.visible = true

        // if (this.dialogPage.loading) {
        //   this.v_loading.visible = true

        //   setTimeout(() => {
        //     this.dialogPage.visible = true
        //   }, 100)
        // } else {
        //   this.dialogPage.visible = true
        // }
      } catch (e) {
        this.dialogPage.visible = false
        this.alert(this.message.dialog.openRetry, this.messageType.error)
      }
    },

    fn_closeDialog() {
      this.dialogPage.visible = false
    }
  }
}

export default dialogOpenMixin
