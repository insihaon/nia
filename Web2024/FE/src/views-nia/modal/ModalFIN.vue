<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.maxHeight + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          마감 처리
          <hr>
        </span>
        <div class="d-flex flex-column h-100 rounded justify-between p-3">
          <div class="shadow-sm p-1 mt-2">
            <span class="title"><i class="el-icon-tickets" />조치 SOP</span>
            <div class="d-flex p-2">
              <div v-for="item in actionForm" :key="item.model">
                <el-select v-model="finSop[item.model]" :placeholder="item.label">
                  <el-option
                    v-for="op in item.options"
                    :key="op.value"
                    :label="op.label"
                    :value="op.value"
                  />
                </el-select>
              </div>
              <el-button size="small" class="ml-1" @click.native="$refs.ModalSopMng.open()">
                편집
              </el-button>
            </div>
            <div class="px-2 input">
              <el-input v-model="etcContent" placeholder="기타 조치내용 입력" />
            </div>
          </div>
          <div class="shadow-sm p-1 mt-3">
            <span class="title"><i class="el-icon-tickets" />AI 결과 피드백</span>
            <div class="p-2">
              <el-radio v-model="aiFeedback" label="0">일치</el-radio>
              <el-radio v-model="aiFeedback" label="1">불일치</el-radio>
            </div>
            <div class="p-2">
              <el-date-picker
                v-model="period"
                type="datetimerange"
                range-separator="To"
                start-placeholder="시작 시간"
                end-placeholder="종료 시간"
                :disabled="aiFeedback === '0'"
              />
            </div>
          </div>
          <div class="shadow-sm p-1 mt-3 input">
            <span class="title"><i class="el-icon-tickets" />피드백 내용</span>
            <el-input v-model="fault_type_content" :disabled="aiFeedback === '0'" placeholder="AI 결과 피드백 내용 입력" />
          </div>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" @click.native="onClickFin()">
            마감
          </el-button>
          <el-button size="small" plain class="close-btn" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
        <ModalSopMng ref="ModalSopMng" />
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import { apiSelectSopCode } from '@/api/nia'
import ModalSopMng from '@/views-nia/modal/ModalSopMng'
import sopHistory from '@/views-nia/alarmMonitoring/sopHistory.vue'

const routeName = 'ModalFIN'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { sopHistory, ModalSopMng },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      selectedRow: null,
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
        { label: '조치내용', model: 'faultDetail', options: this.selectOption.content }
      ]
    }
  },
  mounted () {
    this.onLoadSopCodeList()
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 750
      // this.domElement.maxHeight = 1600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
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
    onClickSopEdit() {

    },
    onClickFin() {
     /*
    service: tools.constants.Service.rca,
    action: tools.constants.Action.CHANGE_TICKET_STATUS,
    eventType: 'REQUEST_CHANGE_TICKET_STATUS',
      ticket_id: ticket.ticket_id,
    status: tools.constants.TicketStatus.FIN.code,
      ticket_type: ticket.ticket_type,
      sop_id : ticket.sop_id,
      detail: ticket.handling_fin_content,
    ai_accuracy: $scope.ai_accuracy,
    fault_classify: $scope.opinion.faultClassify,
    fault_type: $scope.opinion.faultType,
    fault_detail_content: $scope.opinion.faultDetail,
    etc_content: $scope.opinion.etcContent,
    fault_type_content: $scope.ai_accuracy == 1 ? $scope.fault_type_content : null,
    start_time:  $scope.ai_accuracy == 1 ? $scope.startTime : null,
    end_time:  $scope.ai_accuracy == 1 ? $scope.endTime : null,
    handling_fin_user: tools.store.userName
*/
      if (this.aiFeedback == null) {
        this.$alert('AI 결과 피드백 여부를 선택해 주십시오.', '알림', {
          confirmButtonText: '확인'
        })
      }
    },
    getFinParam() {
      const param = {
        eventType: 'REQUEST_CHANGE_TICKET_STATUS',
        status: 'FIN',
        ai_accuracy: this.ai_accuracy,
        etc_content: this.etcContent,
        fault_type_content: this.ai_accuracy === 1 ? this.fault_type_content : null,
        start_time: this.ai_accuracy === 1 ? this.period[0] : null,
        end_time: this.ai_accuracy === 1 ? this.period[1] : null,
        handling_fin_user: this.$store.state.user.name
      }
      Object.assign(param, this.finSop)

      if (this.dataType === 'TICKET') {
        const { ticket_id, ticket_type, sop_id, handling_fin_content } = this.selectRow
        Object.assign(param, { ticket_id, ticket_type, sop_id, detail: handling_fin_content })
      } else if (this.dataType === 'SYSLOG') {
        const { alarmno, node_num, node_nm, alarmloc, alarmmsg, etc, ip_addr, alarmtime } = this.selectedRow
        Object.assign(param, {
          alarmno, alarmtime: this.moment(alarmtime).format('YYYY-MM-DD HH:mm:ss'), node_num, node_nm,
          alarmloc: alarmloc || '', alarmmsg, etc, ip_addr })
      }
    },
    setSopCode() {
      const codeKeys = Object.keys(this.codeKeys)
      codeKeys.forEach(key => {
        const sopCodeObj = this.sopCodeList.find(d => d.fault_gb === this.codeKeys[key])
        this.selectOption[key] = sopCodeObj?.code_arr?.map(v => { return { value: v } })
      })
    },
    onClose() { /* for Override */ },
    }
  }
</script>

<style lang="scss" scoped>
.input {
  ::v-deep .el-input .el-input__inner {
    border: solid 0px;
    border-bottom: solid 1px lightgray;
    border-radius: 0px;
  }
}
</style>
