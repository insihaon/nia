<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <div class="d-flex flex-column h-100 rounded justify-between">
      <el-card shadow="never" style="height: 85%" :body-style="{ padding: '10px' }">
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
        <el-row class="px-2 input">
          <el-input v-model="etcContent" placeholder="기타 조치내용 입력" />
        </el-row>
      </el-card>
      <el-card shadow="never" style="height: 90%" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> AI 결과 피드백</span>
        </div>
        <el-row class="p-2 d-flex"> <input v-model="aiFeedback" style="margin-right: 5px" type="radio" value="0" />일치 <input v-model="aiFeedback" style="margin-left: 5px; margin-right: 5px" type="radio" value="1" />불일치 </el-row>

        <el-row class="p-2 d-flex">
          <el-date-picker v-model="period" type="datetimerange" range-separator="To" start-placeholder="시작 시간" end-placeholder="종료 시간" :disabled="aiFeedback === '0'" />
        </el-row>
      </el-card>
      <el-card shadow="never" class="h-100" :body-style="{ padding: '10px' }">
        <div slot="header">
          <span><i class="el-icon-document" /> 피드백 내용</span>
        </div>
        <el-row>
          <el-col>
            <el-input v-model="fault_type_content" :rows="4" type="textarea" :disabled="aiFeedback === '0'" placeholder="AI 결과 피드백 내용 입력" />
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
import { getHiddenParameter, getNiaRouterPathByName } from '@/views-nia/js/commonNiaFunction'
import { mapState } from 'vuex'
import niaObserverMixin from '@/mixin/niaObserverMixin'

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
        fault_classify: null,
        fault_type: null,
        fault_detail_content: null,
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
    // disabledFin() {
    //   return this.selectedRow?.status === 'FIN' || this.selectedRow?.status === 'AUTO_FIN'
    // },
  },
  watch: {
    processFinEventText(nVal, oVal) {
      if (nVal.includes('fin')) {
        this.onClickFin()
      }
      this.$store.commit('chatbot/CLEAR_ROUTER_PARAMETER', { name: constants.nia.chatbotKeyMap.processFin.parameterKey })
    },
  },
  created() {
    this.selectedRow = this.wdata?.params
    this.setAiFeedBack()
  },
  mounted() {
    this.onLoadSopCodeList()

    this.$nextTick(() => {
      this.popupShowCommand()
    })
  },
  methods: {
    popupShowCommand() {
      this.$store.dispatch('chatbot/botPushAnswerMessage', {
        content: `<b>${constants.nia.chatbotKeyMap.processFin.popupName}화면에서 활용가능한 명령어입니다.</b>

        1. ${constants.nia.chatbotCommand.fin.label}${getHiddenParameter(getNiaRouterPathByName('NiaMain'), constants.nia.chatbotKeyMap.processFin.dialogNm, 'fin')}
        `,
      })
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

            this.$store.dispatch('chatbot/botPushAnswerMessage', {
              content: `${constants.nia.chatbotIcon.success} ${constants.nia.chatbotKeyMap.processFin.popupName}에서 성공적으로 ${constants.nia.chatbotCommand.fin.label}했습니다.`,
            })
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
      /* TICKET 마감처리 참고 파라미터 (레거시)
      {
        "service": "rca",
        "action": "CHANGE_TICKET_STATUS",
        "eventType": "REQUEST_CHANGE_TICKET_STATUS",
        "ticket_id": "85207",
        "status": "FIN",
        "ticket_type": "RT",
        "ai_accuracy": "0",
        "fault_classify": "",
        "fault_type": "",
        "fault_detail_content": "",
        "etc_content": "",
        "fault_type_content": null,
        "start_time": null,
        "end_time": null,
        "handling_fin_user": "NIA ADMIN"
      }
      */
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
