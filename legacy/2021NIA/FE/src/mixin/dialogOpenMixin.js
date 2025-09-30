import constants from '@/min/constants'
import store from '@/store'

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
          component: () => import('@/views-nia/dashBoard/aiResponse'),
          pageTitle: 'AI 장애대응',
          width: '1100',
          height: '750',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.aiResponse.parameterKey
        },
        aiResponse2: {
          component: () => import('@/views-nia/dashBoard/aiResponse2'),
          pageTitle: 'AI 장애대응(신규)',
          width: '1100',
          height: '750',
          resizeble: true,
        },
        requestForAction: {
          component: () => import('@/views-nia/dashBoard/requestForAction'),
          pageTitle: '상황전파',
          width: '1200',
          height: '1100',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.requestForAction.parameterKey
        },
        processFin: {
          component: () => import('@/views-nia/dashBoard/processFin'),
          pageTitle: '마감 처리',
          width: '600',
          height: '700',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.processFin.parameterKey
        },
        configTest: {
          component: () => import('@/views-nia/dashBoard/configTest'),
          pageTitle: '조치',
          width: '750',
          height: '660',
          resizeble: true,
          chatbotParameterKeyName: 'configTest'
        },
        snapShot: {
          component: () => import('@/views-nia/dashBoard/snapShot'),
          pageTitle: '데이터 스냅샷',
          width: '600',
          height: '310',
          resizeble: true,
        },
        sopHistory: {
          component: () => import('@/views-nia/alarmMonitoring/sopHistory'),
          pageTitle: 'SOP 이력 조회',
          width: '1200',
          height: '900',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.sopHistory.parameterKey
        },
        selfProcessList: {
          component: () => import('@/views-nia/dashBoard/selfProcessList'),
          pageTitle: '자가 최적화 이력조회',
          width: '1000',
          height: '800',
          resizeble: true,
        },
        ticketDetail: {
          component: () => import('@/views-nia/dashBoard/ticketDetail'),
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
          component: () => import('@/views-nia/dashBoard/niaTopology'),
          pageTitle: '토폴로지',
          width: '1200',
          height: '785',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.niaTopology.parameterKey
        },
        systemMonitoringFilter: {
          component: () => import('@/views-nia/dashBoard/systemMonitoringFilter'),
          pageTitle: '시스템 모니터링 필터',
          width: '600',
          height: '380',
          resizeble: true,
        },
        disabilityStatusHistoryManagement: {
          component: () => import('@/views-nia/alarmMonitoring/disabilityStatusHistoryManagement'),
          pageTitle: '장애현황 및 이력관리',
          width: '1200',
          height: '900',
          resizeble: true,
          chatbotParameterKeyName: constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.parameterKey
        },
        chatbot: {
          component: () => import('@/views-nia/dashBoard/chatbot'),
          pageTitle: '어시스턴트',
          width: '600',
          height: '900',
          resizeble: true,
          positionBottomLeft: true,
          notDuplicate: true
        },
        pathSwitch: {
          component: () => import('@/views-nia/dashBoard/pathSwitch'),
          pageTitle: '포트변경',
          width: '800',
          height: '500',
          resizeble: true,
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
      tmpWindowData.width = customStyle.width || this.isMobile ? window.innerWidth : this.dialogList[dialogNm]['width']
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
