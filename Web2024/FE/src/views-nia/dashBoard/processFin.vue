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
            <el-button class="edit" size="mini" type="primary" icon="el-icon-edit-outline" @click.native="openSopMng"> 편집 </el-button>
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
          <el-button size="mini" type="primary" class="el-icon-edit-outline" @click.native="onClickFin()"> 마감처리 </el-button>
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
import { apiSelectSopCode, apiSendMQ, apiSelectRcaNttTicketDetailInfo } from '@/api/nia'
import ModalSopMng from '@/views-nia/modal/ModalSopMng'
import constants from '@/min/constants'
import { getChatbotTicketData, getWindowActionList, loadLatestSopData } from '@/views-nia/js/commonNiaFunction'
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
          case constants.nia.chatbotCommand.sopEdit.action:
            this.openSopMng()
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
    openSopMng() {
      this.$refs.ModalSopMng.open()
    },

    async setTicketDataForChatbotTicketData(isSwitchingTicket) {
      await this.setFocusPopupParameter(isSwitchingTicket)

      await this.onLoadSopCodeList()
      await this.setSopCodeValue()
      this.setAiFeedBack()
    },

    async setSopCodeValue() {
      // 1. 로컬 스토리지에 조치 내역이 있으면, 조치내역을 셋팅한다.
      const remoteControl = localStorage.getItem('lastRemoteHistory' + '_' + this.$store.state.user.info.uid + '_' + this.selectedRow.node_nm + '_' + this.selectedRow.alarmloc)
      if (remoteControl) {
        const cleanRemoteControl = remoteControl.replace(/"/g, '').replace(/\s/g, '')
        switch (cleanRemoteControl) {
          case 'shoutdown':
            this.finSop.fault_classify = '포트장애'
            this.finSop.fault_type = '포트불량'
            this.finSop.fault_detail_content = '포트다운'
            break
          case 'noshut':
            this.finSop.fault_classify = '비장애'
            this.finSop.fault_type = '포트다운'
            this.finSop.fault_detail_content = '포트리셋'
            break
          case 'chngport':
            this.finSop.fault_classify = '링크장애'
            this.finSop.fault_type = 'config 불일치'
            this.finSop.fault_detail_content = '포트변경'
            break
        }

        return
      }

      // 2. 가장 최근의 SOP 이력이 있을 경우 가장 최근 SOP이력을 셋팅한다.
      const latestSopData = await loadLatestSopData(this.selectedRow)
      if (latestSopData) {
        this.finSop.fault_classify = latestSopData.fault_classify
        this.finSop.fault_type = latestSopData.fault_type
        this.finSop.fault_detail_content = latestSopData.fault_detail_content

        return
      }

      // 3. 각 장애유형별로 기본 값을 셋팅한다.
      const ticketType = this.selectedRow.ticket_type
      switch (ticketType) {
        case 'ATT2':
        case 'ATT2_AI': // 포트변경
          this.finSop.fault_classify = '구성변경'
          this.finSop.fault_type = '포트장애'
          this.finSop.fault_detail_content = '포트변경'
          break
        case 'NTT':
        case 'NTT_AI': // 포트다운
          this.finSop.fault_classify = '포트불량'
          this.finSop.fault_type = '포트장애'
          this.finSop.fault_detail_content = '포트다운'
          break
        case 'RT': // 포트리셋
          if (this.selectedRow.alarmmsg === 'PORT_DOWN') {
            this.finSop.fault_classify = '포트불량'
            this.finSop.fault_type = '포트장애'
            this.finSop.fault_detail_content = '포트리셋'
          }
          break
        case 'SYSLOG': // 포트리셋
          this.finSop.fault_classify = '포트불량'
          this.finSop.fault_type = '포트장애'
          this.finSop.fault_detail_content = '포트리셋'
          break
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
            '<div class="chatbot-command-header">마감 팝업 안내</div>' +
            '<div class="chatbot-message-body">' +
              '원격으로 조치한 장비에 대하여 SOP이력을 남기는 팝업입니다.' +
              '<br><br>' +
              constants.nia.chatbotIcon.Information + '조치 SOP 정보를 자동으로 설정했습니다.' +
              '<div class="chatbot-process">' +
                constants.nia.chatbotContent.processHeaderText + '<br><br>' +
                '1. <b>조치 SOP</b> 확인 → 2. <b>조치 SOP</b> 조정' +
                '<br>→ 3. <b>조치 상세내용</b> 입력 → 4. <b>마감처리</b>' +
              '</div>' +
            '</div>' +
            (await getWindowActionList(constants.nia.chatbotKeyMap.processFin.dialogNm, constants.nia.chatbotKeyMap.processFin.popupName,
              showNumberText(3, `${constants.nia.chatbotKeyMap.sopHistory.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', constants.nia.chatbotKeyMap.sopHistory.dialogNm)}`) +
              showNumberText(4, `${constants.nia.chatbotKeyMap.sopHistory.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', constants.nia.chatbotKeyMap.sopHistory.dialogNm)}`) +
              showNumberText(5, `${constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.popupName}${getInvisibleSpanParameter(getNiaRouterPathByName('NiaMain'), '', constants.nia.chatbotKeyMap.disabilityStatusHistoryManagement.dialogNm)}`)
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
      /* if (this.etcContent.length === 0) {
        this.$alert('기타 조치내용 입력을 확인해주세요', '알림', {
          confirmButtonText: '확인',
          customClass: 'nia-message-box',
        })
        return
      } */
      this.$confirm('확인 버튼 클릭 시 해당 티켓은 최종 마감처리 됩니다.', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        customClass: 'nia-message-box',
      }).then(async () => {
        const param = await this.getFinParam()
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

            // if (!this.isFocusModeButNotFocus) {
            //   this.$store.dispatch('chatbot/botPushAnswerMessage', {
            //     content: `${constants.nia.chatbotIcon.success} ${constants.nia.chatbotKeyMap.processFin.popupName}에서 성공적으로 ${constants.nia.chatbotCommand.fin.label}했습니다.`,
            //     callBack: this.popupShowCommand,
            //   })
            // }
          }
        } catch (error) {
          this.$alert('저장에 실패하였습니다.', '알림', {
            confirmButtonText: '확인',
          })
          console.error(error)
        }
      })
    },
    async getFinParam() {
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
