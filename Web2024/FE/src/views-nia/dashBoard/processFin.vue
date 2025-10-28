<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-100 rounded justify-between">
      <el-card shadow="never" style="height: 100px; flex-shrink: 0" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> 조치 SOP</span>
        </div>
        <el-row>
          <el-col v-for="item in actionForm" :key="item.model" :span="7">
            <el-select v-model="finSop[item.model]" :placeholder="item.label" size="mini">
              <el-option v-for="op in item.options" :key="op.value" :label="op.label" :value="op.value" />
            </el-select>
          </el-col>
          <el-col :span="3">
            <el-button class="edit" size="mini" type="primary" icon="el-icon-edit-outline" @click.native="$refs.ModalSopMng.open()"> 편집 </el-button>
          </el-col>
        </el-row>
      </el-card>
      <el-card shadow="never" style="height: 350px" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> SOP 조치 상세내용</span>
        </div>
        <el-row>
          <el-col>
            <el-input v-model="etcContent" :rows="4" type="textarea" placeholder="기타 조치내용 입력" />
          </el-col>
        </el-row>
        <span style="color: red; float: right">SOP 조치 상세내용 입력은 시스템 기능 개선에 큰 도움이 됩니다</span>
      </el-card>
      <el-card shadow="never" style="height: 100%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> AI 결과 피드백</span>
        </div>
        <el-row class="p-2 d-flex"> <input v-model="aiFeedback" style="margin-right: 5px" type="radio" value="0" />일치 <input v-model="aiFeedback" style="margin-left: 5px; margin-right: 5px" type="radio" value="1" />불일치 </el-row>

        <el-row class="p-2 d-flex">
          <el-date-picker v-model="period" type="datetimerange" range-separator="To" start-placeholder="시작 시간" end-placeholder="종료 시간" :disabled="aiFeedback === '0'" />
        </el-row>
        <el-row class="p-2">
          <el-col style="text-align: left"><i class="el-icon-document" /> AI 결과 피드백 상세내용</el-col>
          <el-col>
            <el-input v-model="fault_type_content" :rows="6" type="textarea" :disabled="aiFeedback === '0'" placeholder="AI 결과 피드백 내용 입력" />
          </el-col>
        </el-row>
      </el-card>
      <el-row>
        <el-col align="right" class="mt-2">
          <el-button size="mini" type="primary" class="el-icon-edit-outline" @click.native="onClickFin()"> 마감 </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click.native="$emit('windowClose')">
            {{ $t('exit') }}
          </el-button>
        </el-col>
      </el-row>
    </div>
    <ModalSopMng ref="ModalSopMng" />
  </div>
</template>
<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import { apiSelectSopCode, apiSendMQ } from '@/api/nia'
import ModalSopMng from '@/views-nia/modal/ModalSopMng'
import constants from '@/min/constants'
import { getChatbotTicketData, getWindowActionList, loadFirstSopData } from '@/views-nia/js/commonNiaFunction'
import { mapState } from 'vuex'
import niaObserverMixin from '@/mixin/niaObserverMixin'
import { getInvisibleSpanParameter, getNiaRouterPathByName, showNumberText } from '@/views-nia/js/commonNiaFunction'

const routeName = constants.nia.chatbotKeyMap.processFin.parameterKey

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { /* sopHistory, */ ModalSopMng },
  extends: Base,
  mixins: [niaObserverMixin],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      },
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      selectedRow: null,
      dataType: null,
      sopCodeList: [],
      selectOption: {
        gubun: [],
        type: [],
        content: [],
      },
      codeKeys: { gubun: '장애구분', type: '장애유형', content: '조치내용' },
      aiFeedback: '0',
      fault_type_content: '',
      etcContent: '',
      period: null,
      finSop: {
        fault_classify: null, // 장애구분
        fault_type: null, // 장애유형
        fault_detail_content: null, // 조치내용
      },
    }
  },
  computed: {
    // componentLoader() {
    //   return sopHistory
    // },
    actionForm() {
      return [
        { label: '장애구분', model: 'fault_classify', options: this.selectOption.gubun },
        { label: '장애유형', model: 'fault_type', options: this.selectOption.type },
        { label: '조치내용', model: 'fault_detail_content', options: this.selectOption.content },
      ]
    },
    ...mapState({
      processFinEventText: (state) => state.chatbot.routerParameter[constants.nia.chatbotKeyMap.processFin.parameterKey],
    }),
    isModal() {
      return !!this.wdata.params
    },
  },
  watch: {
    processFinEventText(nVal, oVal) {
      if (this.isModal) {
        switch (nVal) {
          case constants.nia.chatbotCommand.fin.action:
            this.onClickFin()
            break
        }
        this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.processFin.parameterKey })
      }
    },
  },
  created() {
    this.selectedRow = this.wdata?.params
  },
  async mounted() {
    await this.setTicketDataForChatbotTicketData()

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    async setTicketDataForChatbotTicketData(isSwitchingTicket) {
      await this.setFocusPopupParameter(isSwitchingTicket)

      await this.onLoadSopCodeList()
      await this.setSopCodeValue()
      this.setAiFeedBack()
    },

    async setSopCodeValue() {
      const firstSopData = await loadFirstSopData(this.selectedRow)

      if (firstSopData) {
        this.finSop.fault_classify = firstSopData.fault_classify
        this.finSop.fault_type = firstSopData.fault_type
        this.finSop.fault_detail_content = firstSopData.fault_detail_content
      }
    },

    async setFocusPopupParameter(isSwitchingTicket) {
      if (isSwitchingTicket) this.wdata.params.isChatbotGenerated = isSwitchingTicket
      const chatbotData = await getChatbotTicketData(this.wdata)
      if (chatbotData) {
        this.selectedRow = chatbotData
        this.$emit('update:wdataParams', chatbotData)

        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content: constants.nia.chatbotIcon.success + constants.nia.chatbotComment.parameterChange,
        })
      }
    },

    async popupShowCommand() {
      if (!this.isFocusModeButNotFocus) {
        this.$store.dispatch('chatbot/botPushAnswerMessage', {
          content:
            `<div class="chatbot-command-header">마감처리화면</div>
            조치 내역을 최종적으로 SOP에 기록하는 단계입니다.<br>
            장애에 대한 <b>최신 SOP 이력</b>으로 조치SOP 영역을 자동 설정했습니다. 정보를 확인하신 후에 <b>마감 처리</b>를 진행해 주시면 됩니다.<br>
            ${constants.nia.chatbotIcon.Information} <b>기타 조치내용</b>을 입력 해주시면 추후 시스템 기능 개선에 큰 도움이 됩니다.
            ${constants.nia.chatbotIcon.Information} 아직 <b>조치</b>가 이루어지지 않았다면 조치 화면으로 이동해 주세요.
            ${constants.nia.chatbotIcon.Information} 장애에 대해 더 상세한 정보를 알고 싶으시면, <b>티켓 상세 확인</b>도 도와드릴 수 있습니다.<br>
            ` +
            (await getWindowActionList(
              constants.nia.chatbotKeyMap.processFin.dialogNm,
              constants.nia.chatbotKeyMap.processFin.popupName,
              showNumberText(2, `${constants.nia.chatbotKeyMap.configTest.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), constants.nia.chatbotKeyMap.configTest.dialogNm, '')}<br>`) +
                showNumberText(3, `${constants.nia.chatbotCommand.focusModeCheckAlarm.label}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', constants.nia.chatbotCommand.focusModeCheckAlarm.action)}<br>`)
            )),
        })
      }
    },
    setAiFeedBack() {
      if (this.selectedRow) {
        if (['0', '1'].includes(this.selectedRow.ai_accuracy)) {
          this.aiFeedback = this.selectedRow.ai_accuracy
        } else {
          this.aiFeedback = '0'
        }
      }
    },

    async onLoadSopCodeList() {
      try {
        const res = await apiSelectSopCode({ IS_OPTION: true })
        this.sopCodeList = res?.result
        this.setSopCode()
      } catch (error) {
        this.error(error)
      }
    },
    onClickSopEdit() {},
    onClickFin() {
      if (this.aiFeedback == null) {
        this.$alert('AI 결과 피드백 여부를 선택해 주십시오.', '알림', {
          confirmButtonText: '확인',
          customClass: 'nia-message-box',
        })
        return
      }
      if (this.etcContent.length === 0) {
        this.$alert('기타 조치내용 입력을 확인해주세요', '알림', {
          confirmButtonText: '확인',
          customClass: 'nia-message-box',
        })
        return
      }
      this.$confirm('확인 버튼 클릭 시 해당 티켓은 최종 마감처리 됩니다.', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        customClass: 'nia-message-box',
      }).then(async () => {
        const param = this.getFinParam()
        if (this.selectedTicket) {
          Object.assign(param, { ticket_id: this.selectedTicket.ticket_id })
        }
        try {
          const res = await apiSendMQ('fin', param)
          if (res.success) {
            this.$alert('마감 처리 되었습니다.', '알림', {
              confirmButtonText: '확인',
              dangerouslyUseHTMLString: true,
              customClass: 'nia-message-box',
            })

            if (!this.isFocusModeButNotFocus) {
              this.$store.dispatch('chatbot/botPushAnswerMessage', {
                content: `${constants.nia.chatbotIcon.success} ${constants.nia.chatbotKeyMap.processFin.popupName}에서 성공적으로 ${constants.nia.chatbotCommand.fin.label}했습니다.`,
                callBack: this.popupShowCommand,
              })
            }
          }
        } catch (error) {
          this.$alert('저장에 실패하였습니다.', '알림', {
            confirmButtonText: '확인',
          })
          console.error(error)
        }
      })
    },
    getFinParam() {
      const finType = this.selectedRow.ticket_type === 'SYSLOG' ? 'SYSLOG' : 'TICKET'
      const param = {
        eventType: `REQUEST_CHANGE_${finType}_STATUS`,
        status: 'FIN',
        ai_accuracy: this.aiFeedback,
        etc_content: this.etcContent,
        fault_type_content: this.aiFeedback === '1' ? this.fault_type_content : null,
        start_time: this.aiFeedback === '1' && this.period ? this.period[0] : null,
        end_time: this.aiFeedback === '1' && this.period ? this.period[1] : null,
        handling_fin_user: this.$store.state.user.name,
      }
      Object.assign(param, this.finSop)

      if (finType === 'TICKET') {
        const { ticket_id, ticket_type, sop_id, handling_fin_content } = this.selectedRow
        Object.assign(param, { ticket_id, ticket_type, sop_id, detail: handling_fin_content })
      } else if (finType === 'SYSLOG') {
        const { alarmno, node_num, node_nm, alarmloc, alarmmsg, etc, ip_addr, alarmtime } = this.selectedRow
        Object.assign(param, {
          alarmno,
          alarmtime: this.moment(alarmtime).format('YYYY-MM-DD HH:mm:ss'),
          node_num,
          node_nm,
          alarmloc: alarmloc || '',
          alarmmsg,
          etc,
          ip_addr,
        })
      }
      return param
    },
    setSopCode() {
      const codeKeys = Object.keys(this.codeKeys)
      codeKeys.forEach((key) => {
        const sopCodeObj = this.sopCodeList.find((d) => d.fault_gb === this.codeKeys[key])
        this.selectOption[key] = sopCodeObj?.code_arr?.map((v) => {
          return { value: v }
        })
      })
    },
    onClose() {
      /* for Override */
    },
  },
}
</script>

<style lang="scss" scoped>
::v-deep .el-select {
  width: 100% !important;
}

::v-deep .el-date-editor {
  width: 100% !important;
}

::v-deep .edit.el-button--mini {
  padding: 6px 10px;
}
::v-deep .el-input.is-disabled .el-input__inner {
  width: 100% !important;
  margin-left: 0px !important;
}
.input {
  ::v-deep .el-input .el-input__inner {
    border: solid 0px;
    border-bottom: solid 1px lightgray;
    border-radius: 0px;
  }
}
</style>
