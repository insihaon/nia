<template>
  <div :class="{ [name]: true }">
    <div class="d-flex flex-column h-100 rounded justify-between">
      <el-card shadow="never" :body-style="{'padding': '10px'}">
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
      <el-card shadow="never" :body-style="{'padding': '10px'}">
        <div slot="header">
          <span><i class="el-icon-document" /> AI 결과 피드백</span>
        </div>
        <el-row class="p-2 d-flex">
          <el-radio v-model="aiFeedback" label="0">일치</el-radio>
          <el-radio v-model="aiFeedback" label="1">불일치</el-radio>
        </el-row>
        <el-row class="p-2 d-flex">
          <el-date-picker v-model="period" type="datetimerange" range-separator="To" start-placeholder="시작 시간" end-placeholder="종료 시간" :disabled="aiFeedback === '0'" />
        </el-row>
      </el-card>
      <el-card shadow="never" :body-style="{'padding': '10px'}">
        <div slot="header">
          <span><i class="el-icon-document" /> 피드백 내용</span>
        </div>
        <el-row>
          <el-col>
            <el-input v-model="fault_type_content" :disabled="aiFeedback === '0'" placeholder="AI 결과 피드백 내용 입력" />
          </el-col>
        </el-row>
      </el-card>
      <el-row>
        <el-col align="right" class="mt-2">
          <el-button size="mini" type="primary" class="el-icon-edit-outline" @click.native="onClickFin()">
            {{ selectedRow.status == 'FIN' || selectedRow.status == 'AUTO_FIN' ? '수정' : '마감' }}
          </el-button>
          <el-button size="mini" type="info" class="close-btn" icon="el-icon-close" @click.native="$emit('windowClose')">
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
import sopHistory from '@/views-nia/alarmMonitoring/sopHistory.vue'

const routeName = 'processFin'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { sopHistory, ModalSopMng },
  extends: Base,
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      }
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
      aiFeedback: null,
      fault_type_content: '',
      etcContent: '',
      period: null,
      finSop: {
        faultClassify: '',
        faultType: '',
        faultDetail: '',
      },
    }
  },
  computed: {
    componentLoader() {
      return sopHistory
    },
    actionForm() {
      return [
        { label: '장애구분', model: 'faultClassify', options: this.selectOption.gubun },
        { label: '장애유형', model: 'faultType', options: this.selectOption.type },
        { label: '조치내용', model: 'faultDetail', options: this.selectOption.content },
      ]
    },
  },
  created () {
    this.selectedRow = this.wdata?.params
  },
  mounted() {
    this.onLoadSopCodeList()
  },
  methods: {
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
        })
        return
      }
      this.confirm('확인 버튼 클릭 시 해당 티켓은 최종 마감처리 됩니다.', '알림', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
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
      const finType = this.selectedRow.ticket_type === 'SYSLOG' ? 'SYSLOG' : 'TICKET'
      const param = {
        eventType: `REQUEST_CHANGE_${finType}_STATUS`,
        status: 'FIN',
        ai_accuracy: this.ai_accuracy,
        etc_content: this.etcContent,
        fault_type_content: this.ai_accuracy === 1 ? this.fault_type_content : null,
        start_time: this.ai_accuracy === 1 ? this.period[0] : null,
        end_time: this.ai_accuracy === 1 ? this.period[1] : null,
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
  width: 100%;
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
