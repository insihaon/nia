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
        jsonSettingPopup: {
          component: () => import('@/test/popup/jsonSettingPopup'),
          pageTitle: 'jsonSettingPopup',
          width: '1200',
          height: '650',
          resizeble: true
        },

      }
    }
  },
  methods: {
    fn_openWindow(dialogNm, data, callback, pageTitle) {
      var tmpWindowData = Object.assign({}, this.$store.getters.window_param)

      tmpWindowData.id = new Date().getTime()
      tmpWindowData.name = this.dialogList[dialogNm]['pageTitle']
      tmpWindowData.target = this.dialogList[dialogNm]['component']
      // tmpWindowData.type = this.dialogList[dialogNm]
      tmpWindowData.width = this.dialogList[dialogNm]['width']
      tmpWindowData.height = this.dialogList[dialogNm]['height']
      tmpWindowData.minWidth = this.dialogList[dialogNm]['minWidth']
      tmpWindowData.minHeight = this.dialogList[dialogNm]['minHeight']
      tmpWindowData.resizeble = this.dialogList[dialogNm]['resizeble']
      tmpWindowData.callback = callback || null

      if (pageTitle) {
        tmpWindowData.name = pageTitle
      }

      tmpWindowData.x = (window.innerWidth - tmpWindowData.width) * 0.5 + (this.$store.getters.windows.length - 1) * 20
      tmpWindowData.y = (window.innerHeight - tmpWindowData.height) * 0.5 + (this.$store.getters.windows.length - 1) * 20

      if (tmpWindowData.x < 0) {
        tmpWindowData.x = 15
      }
      if (tmpWindowData.y < 0) {
        tmpWindowData.y = 85
      }

      tmpWindowData.params = Object.assign({}, data)

      this.$store.dispatch('workControlModal/addWindow', tmpWindowData)
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
