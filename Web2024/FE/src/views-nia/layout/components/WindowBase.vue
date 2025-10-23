<template>
  <div
    ref="dragItem"
    class="dragItem nia"
    tabindex="0"
    :class="{ minimize: wdata.windowState === 'minimize', dragItemFocus: wdata.zindex === 1000 }"
    :style="{
      left: wdata.x + 'px',
      top: wdata.y + 'px',
      width: wdata.width + 'px',
      height: wdata.height + 'px',
      backgroundColor: wdata.backgroundColor,
      zIndex: wdata.zindex,
    }"
    @focus="focusIn"
  >
    <div class="dragItemHeader" @dblclick="maximizeWindow()">
      <div class="dragItemHeaderTitle" @mousedown="dragItemMouseDownHandler($event, $store.state.showWidgetList)">
        <span v-if="wdata.showTitle" style="pointer-events: none"> <i class="el-icon-menu" /> {{ wdata.name }} </span>
      </div>

      <transition name="el-zoom-in-center">
        <el-tooltip v-if="showAlarmFocusModeBtn" class="item" effect="dark" :content="isFocusWindow ? '집중경보 대상입니다' : '집중경보 대상이 아닙니다'" placement="top">
          <i v-if="isFocusWindow" class="circleBtn">🟢</i>
          <i v-else class="circleBtn" @click="changeFocusTarget">🔴</i>
        </el-tooltip>
        <el-tooltip v-else-if="showQuestionModeBtn" class="item" effect="dark" content="클릭시 집중경보로 전환됩니다" placement="top">
          <i class="circleBtn" @click="changeFocusTarget">🟤</i>
        </el-tooltip>
      </transition>

      <!-- 창크기 최소화 -->
      <transition name="el-zoom-in-center">
        <el-tooltip class="item" effect="dark" content="창접기" placement="top">
          <i v-if="wdata.windowState != 'minimize'" class="el-icon-minus minimizeBtn" @click="minimizeWindow()" />
        </el-tooltip>
      </transition>

      <!-- 창크기 최대로 -->
      <transition v-if="wdata.resizeble" name="el-zoom-in-center">
        {{ wdata.windowState }}
        <el-tooltip class="item" effect="dark" :content="wdata.windowState == 'normal' ? '창 최대화' : '창 이전상태로'" placement="top">
          <i v-if="wdata.windowState == 'maximize'" class="el-icon-copy-document maximizeBtn" @click="restoreWindow()" />
          <i v-if="wdata.windowState != 'maximize'" class="el-icon-full-screen maximizeBtn" @click="maximizeWindow()" />
        </el-tooltip>
      </transition>

      <!-- 닫기 버튼 -->
      <transition name="el-zoom-in-center">
        <el-tooltip class="item" effect="dark" content="창 닫기" placement="top">
          <i class="el-icon-close closeBtn" @click="closeWindowHandler()" />
        </el-tooltip>
      </transition>
    </div>
    <!-- 컨텐츠 -->
    <div class="dragItemContent">
      <transition name="el-fade-in">
        <component :is="component" v-if="component" ref="childComponent" :wdata="wdata" :type="wdata.type" @update:wdataParams="updateParams" @windowClose="closeEventHandler" @callback="callback" />
      </transition>
    </div>

    <!-- <div class="dragItemFooter">
      <div class="dragItemFooterFunction">
        <slot name="footer-funtion" />
        <el-button class="closeBtn" type="info" size="mini" icon="el-icon-close" @click="closeWindowHandler()">닫기</el-button>
      </div>
    </div> -->

    <!-- 좌측 리사이즈 -->
    <transition name="el-fade-in">
      <i v-if="wdata.resizeble" class="resizeHandler_l" @mousedown="resizeItemMouseDownHandler_l($event, $store.state.showWidgetList)" />
    </transition>

    <!-- 우측 리사이즈 -->
    <transition name="el-fade-in">
      <i v-if="wdata.resizeble" class="resizeHandler_r" @mousedown="resizeItemMouseDownHandler_r($event, $store.state.showWidgetList)" />
    </transition>

    <!-- 좌측 하단 리사이즈 -->
    <transition name="el-fade-in">
      <i v-if="wdata.resizeble" class="resizeHandler_b_l" @mousedown="resizeItemMouseDownHandler_b_l($event, $store.state.showWidgetList)" />
    </transition>

    <!-- 하단 리사이즈 -->
    <transition name="el-fade-in">
      <i v-if="wdata.resizeble" class="resizeHandler_b" @mousedown="resizeItemMouseDownHandler_b($event, $store.state.showWidgetList)" />
    </transition>

    <!-- 우측 하단 리사이즈 -->
    <transition name="el-fade-in">
      <i v-if="wdata.resizeble" class="resizeHandler" @mousedown="resizeItemMouseDownHandler($event, $store.state.showWidgetList)" />
    </transition>

    <el-dialog title="주의" :visible.sync="closeWindowDialog" width="400px" center append-to-body>
      <span style="text-align: center; display: inline-block; width: 100%">{{ wdata.name }} 윈도우를 닫습니다.</span>
      <span slot="footer" class="dialog-footer">
        <el-button round size="small" icon="el-icon-close" @click="closeWindowDialog = 'false'">취소</el-button>
        <el-button round size="small" icon="el-icon-check" @click="closeWindowHandler">닫기</el-button>
      </span>
    </el-dialog>

    <el-dialog title="도움말" :visible.sync="closeHelpDEskWindowDialog" width="400px" center append-to-body>
      <iframe src="./helpdesk/constworkMain_QnA/index.htm" />
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'

/* eslint-disable */
export default {
  name: 'WindowBase',
  props: ['wdata', 'type', 'target'],
  data() {
    return {
      component: null,
      componentLoader: null,
      dragActiveItem: null,
      resizeActiveItem: null,
      dragActiveItemClickPoint: { x: 0, y: 0 },
      closeWindowDialog: false,
      bounds: { x: 0, y: 0, width: 0, height: 0 },
      closeHelpDEskWindowDialog: false,
    }
  },
  watch: {
    wdata: function (value) {
      this.component = this.target
    },

    alarmFocusTicketData(nVal, oVal) {
      // 과거에는 감시 대상이였는데, 현재는 감시대상이 아니게 되었다면,
      if (this.showAlarmFocusModeBtn && oVal.ticket_id === this.getCurrentWindowTicketId && nVal.ticket_id !== this.getCurrentWindowTicketId) {
        console.log('감시대상이 변경되었네요! dialogNm : ' + this.wdata.dialogNm)
        // (1) 현재 윈도우에 셋팅된 티켓 데이터 this.wdata.params.ticket를 바꾸고
        let key = this.getParamTicketKey
        let optionsData = {}
        if (key === 'current') {
          optionsData = nVal
        } else if (key === 'tickets') {
          optinosData = [nVal]
        } else {
          throw error('/??')
        }

        this.$store.dispatch('mdi/setWindowOptions', {
          id: this.wdata.id,
          options: { params: optionsData },
        })

        // (2) 자식 popup의 함수를 실행시켜서 기존 데이터를 reset하고, 새로운 티켓 정보로 다시 셋팅되도록 지시한다.
        this.$refs.childComponent.setTicketDataForAlarmFocusTicketData(true)
      }
    },
  },
  computed: {
    ...mapState({
      currentMode: (state) => state.chatbot.currentMode,
      alarmFocusMode_chatMessages: (state) => state.chatbot.alarmFocusMode_chatMessages,
      alarmFocusTicketData: (state) => state.chatbot.alarmFocusTicketData,
    }),

    isFocusWindow() {
      if (this.showAlarmFocusModeBtn) {
        if (this.alarmFocusTicketData.ticket_type === 'SYSLOG') {
          return this.alarmFocusTicketData?.alarmno === this.getCurrentWindowAlarmno
        } else {
          return this.alarmFocusTicketData?.ticket_id === this.getCurrentWindowTicketId
        }
      } else {
        return false
      }
    },

    getParamTicketKey() {
      if (this.wdata.params.tickets) {
        return 'tickets'
      } else {
        return 'current'
      }
    },

    getCurrentWindowAlarmno() {
      // fn_openWindow 열때 설정되는 Param
      switch (this.getParamTicketKey) {
        case 'tickets': // niaTopology 전체보기 일때
          return this.wdata.params[this.getParamTicketKey][0].alarmno
        case 'current': // 가장 일반적인 popup열 때
          return this.wdata.params.alarmno
      }
    },

    getCurrentWindowTicketId() {
      // fn_openWindow 열때 설정되는 Param
      switch (this.getParamTicketKey) {
        case 'tickets': // niaTopology 전체보기 일때
          return this.wdata.params[this.getParamTicketKey][0].ticket_id
        case 'current': // 가장 일반적인 popup열 때
          return this.wdata.params.ticket_id
      }
    },

    showAlarmFocusModeBtn() {
      return this.currentMode === 'alarmFocusMode' && this.wdata.dialogNm != 'chatbot'
    },

    showQuestionModeBtn() {
      return this.currentMode === 'questionMode' && this.wdata.dialogNm != 'chatbot'
    },
  },
  created() {
    window.addEventListener('resize', this.browserResizeHandler)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.browserResizeHandler)
  },
  mounted() {},
  methods: {
    changeFocusTarget() {
      window.changeFocusAlertMode(this.wdata.params)
    },

    updateParams(params) {
      this.wdata.params = params
    },

    browserResizeHandler(e) {
      var areaWidth = window.innerWidth
      var areaHeight = window.innerHeight
      if (this.wdata.x > areaWidth - 50) {
        this.$set(this.wdata, 'x', areaWidth - 50)
      }
      if (this.wdata.y > areaHeight - 40) {
        this.$set(this.wdata, 'y', areaHeight - 40)
      }
    },
    callback(data) {
      if (this.wdata.callback === null) return
      this.wdata.callback(data)
    },
    focusIn() {
      this.$store.dispatch('mdi/bringToFrontWindow', this.wdata.id)
    },
    restoreWindow() {
      this.$set(this.wdata, 'windowState', 'normal')
      this.$set(this.wdata, 'x', this.bounds.x)
      this.$set(this.wdata, 'y', this.bounds.y)
      this.$set(this.wdata, 'width', this.bounds.width)
      this.$set(this.wdata, 'height', this.bounds.height)
    },
    minimizeWindow() {
      this.$set(this.wdata, 'windowState', 'minimize')
    },
    showHelpDesk() {
      console.log('도움말을 보여주세요')
      this.closeHelpDEskWindowDialog = true
      //constworkMain_QnA
    },
    maximizeWindow() {
      if (this.wdata.resizeble == false) {
        return
      }

      if (this.wdata.windowState == 'normal') {
        this.$set(this.bounds, 'x', this.wdata.x)
        this.$set(this.bounds, 'y', this.wdata.y)
        this.$set(this.bounds, 'width', this.wdata.width)
        this.$set(this.bounds, 'height', this.wdata.height)

        this.$set(this.wdata, 'windowState', 'maximize')

        var areaWidth = window.innerWidth
        var areaHeight = window.innerHeight

        this.$set(this.wdata, 'x', 10)
        this.$set(this.wdata, 'y', 70)
        this.$set(this.wdata, 'width', areaWidth - (10 + 10))
        this.$set(this.wdata, 'height', areaHeight - (90 + 10 + 50))
      } else if (this.wdata.windowState == 'maximize') {
        this.$set(this.wdata, 'windowState', 'normal')
        this.$set(this.wdata, 'x', this.bounds.x)
        this.$set(this.wdata, 'y', this.bounds.y)
        this.$set(this.wdata, 'width', this.bounds.width)
        this.$set(this.wdata, 'height', this.bounds.height)
      }

      // 해당 윈도우의 스케일을 사용할 경우
      if (this.wdata.scale !== 'none') {
        if (this.$refs.childComponent.windowResizeHandler) {
          setTimeout(() => {
            this.$refs.childComponent.windowResizeHandler(this.wdata.id)
          }, 100)
        } else {
        }
      }
    },
    /***************************************************************************/
    // 리사이즈 마우스 다운 핸들러(하단)
    resizeItemMouseDownHandler_b: function (event, edit) {
      if (!event.target.classList.contains('resizeHandler_b')) {
        return
      }
      if (this.wdata.windowState == 'minimize') {
        return
      }

      this.$set(this.wdata, 'windowState', 'normal')
      this.resizeActiveItem = event.target.parentElement
      var posy = parseInt(this.resizeActiveItem.style.height)
      document.addEventListener('mousemove', this.resizeItemMouseMoveHandler_b)
      document.addEventListener('mouseup', this.resizeItemMouseUpHandler_b)
    },
    // 윈도우 리스이즈 핸들러(좌측 하단)
    resizeItemMouseMoveHandler_b: function (event) {
      if (this.resizeActiveItem != null) {
        var topMargin = 10
        var tmpX = event.pageX // - this.wdata.x;
        var tmpY = event.pageY - this.wdata.y - topMargin // ;

        var minW = this.wdata.minWidth
        var minH = this.wdata.minHeight
        var maxW = this.wdata.maxWidth
        var maxH = this.wdata.maxHeight
        var bPadding = 10
        var rPadding = 10

        if (tmpY < 30) {
          tmpY = 30
        }
        this.$set(this.wdata, 'height', tmpY + 10)

        // 해당 윈도우의 스케일을 사용할 경우
        if (this.wdata.scale !== 'none') {
          if (this.$refs.childComponent.windowResizeHandler) {
            this.$refs.childComponent.windowResizeHandler(this.wdata.id)
          } else {
          }
        }
      }
    },
    // 윈도우 리사이즈 마우스업 핸들러(좌측 하단)
    resizeItemMouseUpHandler_b: function (event) {
      document.removeEventListener('mousemove', this.resizeItemMouseMoveHandler_b)
      document.removeEventListener('mouseup', this.resizeItemMouseUpHandler_b)
      this.resizeActiveItem.classList.remove('draggable')
      this.resizeActiveItem = null
    },

    /***************************************************************************/
    //리사이즈 마우스 다운 핸들러(좌측)
    resizeItemMouseDownHandler_l: function (event, edit) {
      if (!event.target.classList.contains('resizeHandler_l')) {
        return
      }
      if (this.wdata.windowState == 'minimize') {
        return
      }

      this.$set(this.wdata, 'windowState', 'normal')
      this.resizeActiveItem = event.target.parentElement
      var posx = parseInt(this.resizeActiveItem.style.width)
      var posy = parseInt(this.resizeActiveItem.style.height)
      document.addEventListener('mousemove', this.resizeItemMouseMoveHandler_l)
      document.addEventListener('mouseup', this.resizeItemMouseUpHandler_l)
    },
    // 윈도우 리스이즈 핸들러(좌측)
    resizeItemMouseMoveHandler_l: function (event) {
      if (this.resizeActiveItem != null) {
        var topMargin = 10
        var tmpX = event.pageX // - this.wdata.x;
        var tmpY = event.pageY - this.wdata.y - topMargin // ;

        var areaWidth = window.innerWidth
        var areaHeight = window.innerHeight

        var minW = this.wdata.minWidth
        var minH = this.wdata.minHeight
        var maxW = this.wdata.maxWidth
        var maxH = this.wdata.maxHeight
        var bPadding = 10
        var rPadding = 10
        var tmpW = this.wdata.width - (event.pageX - this.wdata.x)

        if (tmpW < 250) {
          tmpW = 250
        } else {
          this.$set(this.wdata, 'x', tmpX)
        }
        this.$set(this.wdata, 'width', tmpW)

        // 해당 윈도우의 시케일을 사용할 경우
        if (this.wdata.scale !== 'none') {
          if (this.$refs.childComponent.windowResizeHandler) {
            this.$refs.childComponent.windowResizeHandler(this.wdata.id)
          } else {
          }
        }
      }
    },
    // 윈도우 리사이즈 마우스업 핸들러(좌측)
    resizeItemMouseUpHandler_l: function (event) {
      document.removeEventListener('mousemove', this.resizeItemMouseMoveHandler_l)
      document.removeEventListener('mouseup', this.resizeItemMouseUpHandler_l)
      this.resizeActiveItem.classList.remove('draggable')
      this.resizeActiveItem = null
    },
    /***************************************************************************/
    //리사이즈 마우스 다운 핸들러(좌측 하단)
    resizeItemMouseDownHandler_b_l: function (event, edit) {
      if (!event.target.classList.contains('resizeHandler_b_l')) {
        return
      }
      if (this.wdata.windowState == 'minimize') {
        return
      }

      this.$set(this.wdata, 'windowState', 'normal')
      this.resizeActiveItem = event.target.parentElement
      var posx = parseInt(this.resizeActiveItem.style.width)
      var posy = parseInt(this.resizeActiveItem.style.height)
      document.addEventListener('mousemove', this.resizeItemMouseMoveHandler_b_l)
      document.addEventListener('mouseup', this.resizeItemMouseUpHandler_b_l)
    },
    // 윈도우 리스이즈 핸들러(좌측 하단)
    resizeItemMouseMoveHandler_b_l: function (event) {
      if (this.resizeActiveItem != null) {
        var topMargin = 10
        var tmpX = event.pageX // - this.wdata.x;
        var tmpY = event.pageY - this.wdata.y - topMargin // ;

        var areaWidth = window.innerWidth
        var areaHeight = window.innerHeight

        // tmpX = Math.round( tmpX * 0.1 ) * 10;
        // tmpY = Math.round( tmpY * 0.1 ) * 10;

        var minW = this.wdata.minWidth
        var minH = this.wdata.minHeight
        var maxW = this.wdata.maxWidth
        var maxH = this.wdata.maxHeight
        var bPadding = 10
        var rPadding = 10
        var tmpW = this.wdata.width - (event.pageX - this.wdata.x)

        if (tmpW < 200) {
          tmpW = 200
        } else {
          this.$set(this.wdata, 'x', tmpX)
        }
        this.$set(this.wdata, 'width', tmpW)

        if (tmpY < 30) {
          tmpY = 30
        }

        this.$set(this.wdata, 'height', tmpY + 10)

        // if(tmpX<minW){tmpX=minW;}
        // if(tmpY>maxW){tmpX=maxW;}
        // if(tmpY<minH){tmpY=minH;}
        // if(tmpY>maxH){tmpY=maxH;}

        // if( this.wdata.x + tmpX > areaWidth - rPadding){
        //   tmpX = areaWidth -rPadding - this.wdata.x;
        // }
        // if( this.wdata.y + tmpY > areaHeight - bPadding){
        //   tmpY = areaHeight - bPadding - this.wdata.y;
        // }
        // this.$set(this.wdata, "x", tmpX);
        // this.$set(this.wdata, "y", tmpY);
        // this.$set(this.wdata, "width", tmpX);
        // this.$set(this.wdata, "height", tmpY);

        // 해당 윈도우의 시케일을 사용할 경우
        if (this.wdata.scale !== 'none') {
          if (this.$refs.childComponent.windowResizeHandler) {
            this.$refs.childComponent.windowResizeHandler(this.wdata.id)
          } else {
          }
        }
      }
    },
    // 윈도우 리사이즈 마우스업 핸들러(좌측 하단)
    resizeItemMouseUpHandler_b_l: function (event) {
      document.removeEventListener('mousemove', this.resizeItemMouseMoveHandler_b_l)
      document.removeEventListener('mouseup', this.resizeItemMouseUpHandler_b_l)
      this.resizeActiveItem.classList.remove('draggable')
      this.resizeActiveItem = null
    },
    /***************************************************************************/
    // 리사이즈 마우스 다운 핸들러(우측)
    resizeItemMouseDownHandler_r: function (event, edit) {
      if (!event.target.classList.contains('resizeHandler_r')) {
        return
      }
      if (this.wdata.windowState == 'minimize') {
        return
      }
      this.$set(this.wdata, 'windowState', 'normal')
      this.resizeActiveItem = event.target.parentElement
      var posx = parseInt(this.resizeActiveItem.style.width)
      var posy = parseInt(this.resizeActiveItem.style.height)
      document.addEventListener('mousemove', this.resizeItemMouseMoveHandler_r)
      document.addEventListener('mouseup', this.resizeItemMouseUpHandler_r)
    },
    // 윈도우 리스이즈 핸들러
    resizeItemMouseMoveHandler_r: function (event) {
      if (this.resizeActiveItem != null) {
        var topMargin = 10
        var tmpX = event.pageX - this.wdata.x

        var areaWidth = window.innerWidth
        var areaHeight = window.innerHeight

        tmpX = Math.round(tmpX * 0.1) * 10

        var minW = this.wdata.minWidth
        var minH = this.wdata.minHeight
        var maxW = this.wdata.maxWidth
        var maxH = this.wdata.maxHeight
        var bPadding = 10
        var rPadding = 10

        if (tmpX < 250) {
          tmpX = minW
        }

        if (this.wdata.x + tmpX > areaWidth - rPadding) {
          tmpX = areaWidth - rPadding - this.wdata.x
        }
        this.$set(this.wdata, 'width', tmpX)

        // 해당 윈도우의 시케일을 사용할 경우
        if (this.wdata.scale !== 'none') {
          if (this.$refs.childComponent.windowResizeHandler) {
            this.$refs.childComponent.windowResizeHandler(this.wdata.id)
          } else {
          }
        }
      }
    },
    // 윈도우 리사이즈 마우스업 핸들러
    resizeItemMouseUpHandler_r: function (event) {
      document.removeEventListener('mousemove', this.resizeItemMouseMoveHandler_r)
      document.removeEventListener('mouseup', this.resizeItemMouseUpHandler_r)
      this.resizeActiveItem.classList.remove('draggable')
      this.resizeActiveItem = null
    },
    /***************************************************************************/
    //리사이즈 마우스 다운 핸들러(우측 하단)
    resizeItemMouseDownHandler: function (event, edit) {
      if (!event.target.classList.contains('resizeHandler')) {
        return
      }
      if (this.wdata.windowState == 'minimize') {
        return
      }
      this.$set(this.wdata, 'windowState', 'normal')
      this.resizeActiveItem = event.target.parentElement
      var posx = parseInt(this.resizeActiveItem.style.width)
      var posy = parseInt(this.resizeActiveItem.style.height)
      document.addEventListener('mousemove', this.resizeItemMouseMoveHandler)
      document.addEventListener('mouseup', this.resizeItemMouseUpHandler)
    },
    // 윈도우 리스이즈 핸들러
    resizeItemMouseMoveHandler: function (event) {
      if (this.resizeActiveItem != null) {
        var topMargin = 10
        var tmpX = event.pageX - this.wdata.x
        var tmpY = event.pageY - this.wdata.y - topMargin

        var areaWidth = window.innerWidth
        var areaHeight = window.innerHeight

        tmpX = Math.round(tmpX * 0.1) * 10
        tmpY = Math.round(tmpY * 0.1) * 10

        var minW = this.wdata.minWidth
        var minH = this.wdata.minHeight
        var maxW = this.wdata.maxWidth
        var maxH = this.wdata.maxHeight
        var bPadding = 10
        var rPadding = 10

        if (tmpX < minW) {
          tmpX = minW
        }
        if (tmpY > maxW) {
          tmpX = maxW
        }
        if (tmpY < minH) {
          tmpY = minH
        }
        if (tmpY > maxH) {
          tmpY = maxH
        }

        if (this.wdata.x + tmpX > areaWidth - rPadding) {
          tmpX = areaWidth - rPadding - this.wdata.x
        }
        if (this.wdata.y + tmpY > areaHeight - bPadding) {
          tmpY = areaHeight - bPadding - this.wdata.y
        }
        this.$set(this.wdata, 'width', tmpX)
        this.$set(this.wdata, 'height', tmpY)

        // 해당 윈도우의 시케일을 사용할 경우
        if (this.wdata.scale !== 'none') {
          if (this.$refs.childComponent.windowResizeHandler) {
            this.$refs.childComponent.windowResizeHandler(this.wdata.id)
          } else {
          }
        }
      }
    },
    // 윈도우 리사이즈 마우스업 핸들러
    resizeItemMouseUpHandler: function (event) {
      document.removeEventListener('mousemove', this.resizeItemMouseMoveHandler)
      document.removeEventListener('mouseup', this.resizeItemMouseUpHandler)
      this.resizeActiveItem.classList.remove('draggable')
      this.resizeActiveItem = null
    },

    // 윈도우 이동 마우스 다운 핸들러
    dragItemMouseDownHandler: function (event, edit) {
      try {
        // 드래그영역의 마우스 클릭이 아닐경우
        if (!event.target.classList.contains('dragItemHeaderTitle')) {
          return
        }
        this.dragActiveItem = event.target.parentElement.parentElement
        var posx = parseInt(this.dragActiveItem.style.width)
        var posy = parseInt(this.dragActiveItem.style.height)
        posx = Math.round(posx * 0.1) * 10
        posy = Math.round(posy * 0.1) * 10

        this.dragActiveItemClickPoint.x = event.offsetX
        this.dragActiveItemClickPoint.y = event.offsetY

        document.addEventListener('mousemove', this.dragItemMouseMoveHandler)
        document.addEventListener('mouseup', this.dragItemMouseUpHandler)
      } catch (error) {
        console.log('ERROR')
      }
    },
    // 윈도우 드래그 핸들러
    dragItemMouseMoveHandler: function (event) {
      if (this.dragActiveItem !== null) {
        var tmpX = event.pageX - this.dragActiveItemClickPoint.x
        var tmpY = event.pageY - this.dragActiveItemClickPoint.y
        var tmpWidth = 0
        var tmpHeight = 0
        if (this.wdata.windowState == 'minimize') {
          tmpWidth = 200
          tmpHeight = 44
        } else if (this.wdata.windowState === 'normal') {
          tmpWidth = parseInt(this.dragActiveItem.style.width)
          tmpHeight = parseInt(this.dragActiveItem.style.height)
        }
        var areaWidth = window.innerWidth
        var areaHeight = window.innerHeight
        if (tmpX < 10) {
          tmpX = 10
        } else if (tmpX + 40 >= areaWidth - 10) {
          tmpX = areaWidth - 40 - 10
        }
        // else if( tmpX + tmpWidth >= areaWidth -10){
        //   tmpX = areaWidth - tmpWidth - 10;
        // }
        if (tmpY < 70) {
          tmpY = 70
        } else if (tmpY + 40 >= areaHeight - 10) {
          tmpY = areaHeight - 40 - 10
        }
        // else if( tmpY + tmpHeight >= areaHeight - 10){
        //   tmpY = areaHeight - tmpHeight - 10;
        // }
      }
      tmpX = Math.round(tmpX * 0.1) * 10
      tmpY = Math.round(tmpY * 0.1) * 10

      this.$set(this.wdata, 'x', tmpX)
      this.$set(this.wdata, 'y', tmpY)
    },
    // 윈도우 마우스업 핸들러
    dragItemMouseUpHandler: function (event) {
      document.removeEventListener('mousemove', this.dragItemMouseMoveHandler)
      document.removeEventListener('mouseup', this.dragItemMOuseUpHandler)
      this.dragActiveItem = null
      // if( this.wdata.scale !== 'none'){
      //   if( this.$refs.childComponent.windowPositionUpdateHandler != undefined){
      //     this.$refs.childComponent.windowPositionUpdateHandler(this.wdata.id);
      //   }else{

      //   }
      // }
    },
    // 타이틀바의 윈도우 닫기 버튼과 컨펌창 닫기 버튼 이벤트 핸들러
    closeWindowHandler: function () {
      this.closeWindowDialog = false
      this.$store.dispatch('mdi/removeWindow', this.wdata.id)
    },
    // 컨텐츠에서 올라오는 윈도우 닫기 이벤트 핸들러
    closeEventHandler(check) {
      if (check == true) {
        this.closeWindowDialog = true
      } else {
        this.$store.dispatch('mdi/removeWindow', this.wdata.id)
      }
    },
  },
  mounted() {
    this.component = this.target
  },
}
</script>

<style scoped>
div.minimize {
  width: 300px !important;
  height: 40px !important;
  border-radius: 22px !important;
  transition: border-radius 0.25s, width 0.25s, height 0.5s;
  transition-timing-function: ease-out;
  box-sizing: border-box;
  display: none;
}
div.minimize > .dragItemContent {
  display: none;
}
div.dragItem {
  position: fixed;
  overflow: hidden;
  background-color: #ffffff;
  /* user-select: none; */
  border: 3px solid #999999;
  box-sizing: border-box;
  animation-duration: 0.5s;
  animation-name: fadein;
  border-radius: 15px;
}
div.dragItemFocus {
  outline: none;
  border-color: #3d4048;
  box-shadow: 10px 10px 30px rgba(0, 0, 0, 0.25);
}
@keyframes fadein {
  from {
    transform: scale(1.1);
    filter: alpha(opacity=0);
  }
  to {
    transform: scale(1);
    filter: alpha(opacity=100);
  }
}
div.dragItem > div.dragItemHeader {
  position: relative;
  height: 36px;
  line-height: 38px;
  z-index: 2;
  background-color: rgba(255, 255, 255, 0);
  transition: all 0.5s;
  box-sizing: border-box;
  border-bottom: 1px solid #999999;
}

div.dragItem:focus > div.dragItemHeader {
  background-color: rgba(255, 255, 255, 1);
}
div.dragItem > div.dragItemHeader:hover {
  background-color: rgba(255, 255, 255, 1);
  transition: all 0.5s;
}
div.dragItem > div.dragItemHeader > div.dragItemHeaderTitle {
  position: absolute;
  display: flex;
  align-items: center;
  width: 100%;
  height: 38px;
  left: 0;
  text-align: left;
  text-indent: 6px;
  line-height: 34px;
  cursor: pointer;
  font-weight: 400;
  color: #4e575e;
}
div.dragItem > div.dragItemHeader > div.dragItemHeaderTitle > span {
  /* transform: skew(0.028deg);  */
  display: inline-block;
  font-weight: 600;
  font-size: 15px;
}
div.dragItem > div.dragItemHeader > div.dragItemHeaderTitle > span > i {
  position: relative;
  top: 1px;
}

div.dragItem > div.dragItemHeader > i.questionBtn {
  position: absolute;
  right: 110px;
  top: 3px;
  color: #333333;
  font-size: 18px;
  padding: 6px;
  cursor: pointer;
  transition: all 0.25s;
}
div.dragItem > div.dragItemHeader > i.questionBtn:hover {
  background-color: rgbga(0, 0, 0, 0);
  transform: scale(2);
  transition: all 0.25s;
}

div.dragItem > div.dragItemHeader > i.circleBtn {
  position: absolute;
  right: 105px;
  top: 0px;

  font-size: 15px;
  padding: 0px 10px 0px 10px;
  cursor: pointer;
  font-style: normal;
  transition: all 0.25s;
}

div.dragItem > div.dragItemHeader > i.minimizeBtn {
  position: absolute;
  right: 75px;
  top: 3px;
  color: #333333;
  font-size: 11px;
  padding: 10px;
  cursor: pointer;
  transition: all 0.25s;
}
div.dragItem > div.dragItemHeader > i.minimizeBtn:hover {
  background-color: rgbga(0, 0, 0, 0);
  transform: scale(2);
  transition: all 0.25s;
}
div.dragItem > div.dragItemHeader > i.maximizeBtn {
  position: absolute;
  right: 40px;
  top: 3px;
  color: #333333;
  font-size: 11px;
  padding: 10px;
  cursor: pointer;
  transition: all 0.25s;
}
div.dragItem > div.dragItemHeader > i.maximizeBtn:hover {
  background-color: rgba(0, 0, 0, 0);
  transform: scale(2);
  transition: all 0.25s;
}
div.dragItem > div.dragItemHeader > i.closeBtn {
  position: absolute;
  right: 5px;
  top: 3px;
  color: #333333;
  font-size: 11px;
  padding: 10px;
  cursor: pointer;
  transition: all 0.25s;
}
div.dragItem > div.dragItemHeader > i.closeBtn:hover {
  background-color: rgba(0, 0, 0, 0);
  transform: scale(2);
  transition: all 0.25s;
}
div.dragItem > div.dragItemContent {
  margin: 0px;
  font-size: 13px;
  text-align: center;
  position: absolute;
  left: 0;
  right: 0;
  top: 36px;
  bottom: 0px;
  overflow: auto;
  transition: opacity 0.25s;
  box-sizing: border-box;
  padding: 10px;
  background-color: #f2f2f2;
}
/*******************************************/
/* 좌측 리사이즈 버튼 */
div.dragItem i.resizeHandler_l {
  position: absolute;
  left: 0px;
  bottom: 20px;
  width: 20px;
  top: 20px;
  text-align: center;
  font-size: 11px;
  cursor: pointer;
  color: #ffffff !important;
  cursor: e-resize;
  z-index: 20;
  border-left: 5px solid rgba(0, 0, 0, 0);
  transition: all 0.55s;
}
div.dragItem i.resizeHandler_l::before {
  display: inline-block;
  transform: rotate(45deg);
}
div.dragItem i.resizeHandler_l:hover {
  color: #000000;
  transition: all 0.5s;
  /* border-left:5px solid #3d4048; */
  border-image: linear-gradient(to bottom, rgba(0, 0, 0, 0), #3d4048, rgba(0, 0, 0, 0));
  border-image-slice: 1;
}
div.dragItem i.resizeHandler_l:hover::before {
  color: #000000;
}
/*******************************************/
/* 우측 리사이즈 버튼*/
div.dragItem i.resizeHandler_r {
  position: absolute;
  right: 0px;
  bottom: 20px;
  top: 20px;
  text-align: center;
  font-size: 11px;
  cursor: pointer;
  color: #ffffff !important;
  cursor: e-resize;
  z-index: 20;
  border-right: 5px solid rgba(0, 0, 0, 0);
  transition: all 0.55s;
}
div.dragItem i.resizeHandler_r::before {
  display: inline-block;
  transform: rotate(-45deg);
}
div.dragItem i.resizeHandler_r:hover {
  color: #000000;
  transition: all 0.5s;
  /* border-right:5px solid #3d4048; */
  border-image: linear-gradient(to bottom, rgba(0, 0, 0, 0), #3d4048, rgba(0, 0, 0, 0));
  border-image-slice: 1;
}
.minimize i.resizeHandler_r {
  pointer-events: none;
  touch-action: none;
}
div.dragItem i.resizeHandler_r:hover::before {
  color: #000000;
}
/*******************************************/

/*******************************************/
/* 우측 하단 리사이즈 버튼 */
div.dragItem i.resizeHandler {
  position: absolute;
  right: 0px;
  bottom: 0px;
  width: 20px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  font-size: 11px;
  cursor: pointer;
  color: #ffffff !important;
  cursor: nwse-resize;
  z-index: 2;
  border-bottom: 5px solid rgba(0, 0, 0, 0);
  border-right: 5px solid rgba(0, 0, 0, 0);
  transition: all 0.55s;
  border-radius: 0 0 10px 0;
}
div.dragItem i.resizeHandler::before {
  display: inline-block;
  transform: rotate(-45deg);
}
div.dragItem i.resizeHandler:hover {
  color: #000000;
  transition: all 0.5s;
  border-bottom: 5px solid #3d4048;
  border-right: 5px solid #3d4048;
}
.minimize i.resizeHandler {
  pointer-events: none;
  touch-action: none;
}
div.dragItem i.resizeHandler:hover::before {
  color: #000000;
}
/*******************************************/
/* 좌측하단 리사이즈 버튼 */
div.dragItem i.resizeHandler_b_l {
  position: absolute;
  left: 0px;
  bottom: 0px;
  width: 20px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  font-size: 11px;
  cursor: pointer;
  color: #ffffff !important;
  cursor: ne-resize;
  z-index: 2;
  border-bottom: 5px solid rgba(0, 0, 0, 0);
  border-left: 5px solid rgba(0, 0, 0, 0);
  transition: all 0.55s;
  border-radius: 0 0 0 10px;
}
div.dragItem i.resizeHandler_b_l::before {
  display: inline-block;
  transform: rotate(45deg);
}
div.dragItem i.resizeHandler_b_l:hover {
  color: #000000;
  transition: all 0.5s;
  border-bottom: 5px solid #3d4048;
  border-left: 5px solid #3d4048;
}
div.dragItem i.resizeHandler_b_l:hover::before {
  color: #000000;
}
/*******************************************/
/* 하단 리사이즈 버튼 */
div.dragItem i.resizeHandler_b {
  position: absolute;
  left: 20px;
  bottom: 0px;
  right: 20px;
  height: 10px;
  line-height: 20px;
  text-align: center;
  font-size: 11px;
  cursor: pointer;
  color: #ffffff !important;
  cursor: n-resize;
  z-index: 10;
  border-bottom: 5px solid rgba(0, 0, 0, 0);
  transition: all 0.55s;
}
div.dragItem i.resizeHandler_b::before {
  display: inline-block;
  transform: rotate(170deg);
}
div.dragItem i.resizeHandler_b:hover {
  color: #000000;
  transition: all 0.5s;
  /* border-bottom:5px solid #3d4048; */
  border-image: linear-gradient(to right, rgba(0, 0, 0, 0), #3d4048, rgba(0, 0, 0, 0));
  border-image-slice: 1;
}
div.dragItem i.resizeHandler_b:hover::before {
  color: #000000;
}
/*******************************************/

.draggable {
}
.draggable::before {
  content: '';
  display: inline-block;
  position: absolute;
  z-index: 2;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  border-radius: 4px;
  pointer-events: none;
}
.draggable > div.dragItemContent {
  display: none;
}
</style>
