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
          pageTitle: '조치 요청서',
          width: '1200',
          height: '1100',
          resizeble: true,
        },
        processFin: {
          component: () => import('@/views-nia/dashBoard/processFin'),
          pageTitle: '마감 처리',
          width: '600',
          height: '550',
          resizeble: true,
        },
        configTest: {
          component: () => import('@/views-nia/dashBoard/configTest'),
          pageTitle: '시험',
          width: '750',
          height: '660',
          resizeble: true,
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
          resizeble: true
        },
        systemMonitoringFilter: {
          component: () => import('@/views-nia/dashBoard/systemMonitoringFilter'),
          pageTitle: '시스템 모니터링 필터',
          width: '600',
          height: '380',
          resizeble: true,
        },
        chatbot: {
          component: () => import('@/views-nia/dashBoard/chatbot'),
          pageTitle: 'chatbot',
          width: '600',
          height: '700',
          resizeble: true,
          positionBottomRight: true
        },
      }
    }
  },
  methods: {
    fn_openWindow(dialogNm, data, callback, customPageTitle) {
      var tmpWindowData = Object.assign({}, this.$store.getters.window_param)

      tmpWindowData.id = new Date().getTime()
      tmpWindowData.name = customPageTitle || this.dialogList[dialogNm]['pageTitle']
      tmpWindowData.target = this.dialogList[dialogNm]['component']
      tmpWindowData.dialogNm = dialogNm
      // tmpWindowData.type = this.dialogList[dialogNm]
      tmpWindowData.width = this.isMobile ? window.innerWidth : this.dialogList[dialogNm]['width']
      tmpWindowData.height = this.dialogList[dialogNm]['height']
      tmpWindowData.minWidth = this.dialogList[dialogNm]['minWidth']
      tmpWindowData.minHeight = this.dialogList[dialogNm]['minHeight']
      tmpWindowData.resizeble = this.dialogList[dialogNm]['resizeble']
      tmpWindowData.callback = callback || null

      var isPositionBottomRight = this.dialogList[dialogNm]['positionBottomRight']
      if (isPositionBottomRight) {
        tmpWindowData.x = window.innerWidth - tmpWindowData.width - 10
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
