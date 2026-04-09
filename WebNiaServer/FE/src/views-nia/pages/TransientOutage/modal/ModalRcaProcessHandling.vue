<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.minWidth + `px`"
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
          TT {{ getTitle }}
        </span>
        <el-row>
          <el-col
            v-for="(item, index) in inputForm"
            v-show="item.show"
            :key="index"
            :span="8"
            class="info-container pb-1 pl-3"
          >
            <div class="title">{{ item.label }}</div>
            <div class="info">{{ item.type === 'time' ? moment(ticket[item.prop]).format('YY-MM-DD HH:mm:ss'):ticket[item.prop] }}</div>
          </el-col>

          <el-col class="box-container">
            <div class="az-title">근원장애 System/Port 정보</div>
            <comp-ag-grid ref="ackSystemAgGrid" v-model="ackSystemGridData" style="height: 200px" class="miniGridHeader" />
          </el-col>

          <el-form
            ref="processForm"
            :model="userInput"
            :rules="EDIT_RULES"
            class="h-100"
            style="padding: 0 15px;"
          >
            <el-col class="box-container">
              <el-col :span="24" class="flex">
                <el-form-item :prop="processType === 'ack' ?'handling_content':''" class="w-100">
                  <el-input
                    v-model="userInput.handling_content"
                    type="textarea"
                    :autosize="{ minRows: 2, maxRows: 2}"
                    :placeholder="(processType === 'ack' && ticket.status === 'INIT') ? '인지 관련사항을 입력해 주십시오.':'메모가 없습니다.'"
                    maxlength="120"
                    show-word-limit
                    required
                  />
                </el-form-item>
              </el-col>
              <el-col v-if="processType === 'fin'" :span="24">
                <el-form-item label="마감 관련사항" prop="handling_fin_content" class="w-100">
                  <el-input
                    ref="handling_fin_content"
                    v-model="userInput.handling_fin_content"
                    prop="handling_fin_content"
                    type="textarea"
                    :autosize="{ minRows: 2, maxRows: 2}"
                    placeholder="마감 관련 내용 기입"
                    maxlength="120"
                    show-word-limit
                    required
                  />
                </el-form-item>
              </el-col>
            </el-col>
          </el-form>
        </el-row>

        <div slot="footer" class="dialog-footer">
          <el-button size="mini" type="primary" @click="handleClickProcess">{{ processType ==='ack'? '인지':'마감' }}처리</el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click="close()">닫기</el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>
<script>
import { Modal } from '@/min/Modal.min.js'
import elDragDialog from '@/directive/el-drag-dialog'
import { apiUserProcess, apiRcaRequest } from '@/api/nia'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { rulesProcessEdit } from '@/utils/validate'

const routeName = 'ModalRcaProcessHandling'

const _component = {
  name: routeName,
  components: { CompAgGrid },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      processType: '',
      ticket: '',
      status: '',
      userInput: {
        accuracy: '',
        handling_content: '',
        handling_fin_content: ''
      },
      EDIT_RULES: {
        handling_content: rulesProcessEdit('_ack'),
        handling_fin_content: rulesProcessEdit('_fin')
      },
      systemCoreList: []
    }
  },
  computed: {
    inputForm() {
      return [
        { label: 'TT번호', prop: 'ticket_id', show: true },
        { label: '티켓발생시간', prop: 'generation_time', show: true, type: 'time' },
        { label: '장애발생시간', prop: 'fault_time', show: true, type: 'time' },
        { label: '전체 알람 수', prop: 'total_alarm_count', show: true },
        { label: `${this.processType === 'ack' ? '인지' : '마감'} 담당자`, prop: 'handling_user', show: true },
        { label: '소속 팀', prop: 'handling_dept', show: true },
        { label: '마감시작시간', prop: 'handling_time', show: this.processType === 'fin' },
        { label: '분류', prop: 'handling_agency', show: true }
      ]
    },
    ackSystemGridData() {
      const columns = [
        { type: '', prop: 'root_cause_sysnamea', name: 'A측 시스템', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'root_cause_porta', name: 'A측 포트', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'root_cause_sysnamez', name: 'Z측 시스템', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'root_cause_portz', name: 'Z측 포트', width: '5rem', alignItems: 'center', sortable: true }
      ]
      const options = { name: this.name, checkable: false, height: '150px' }
      return { options, columns, data: this.systemCoreList }
    },
    getTitle() {
      const { status } = this.ticket
      let result = ''
      if (this.processType === 'ack') {
        result = status === 'INIT' ? '인지처리' : '인지수정'
      } else if (this.processType === 'fin') {
        result = status === 'FIN' ? '마감수정' : '마감처리'
      }
      return result
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.minWidth = 750
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.resetForm()
      this.ticket = model.ticket
      this.processType = model.processType
      this.systemCoreList = model?.systemCoreList

      this.ticket['generationTime'] = this.toStringTime(model.generation_time, 'YYYY-MM-DD HH:mm:ss')
      this.ticket['faultTime'] = this.toStringTime(model.fault_time, 'YYYY-MM-DD HH:mm:ss')

      if (!(this.processType === 'ack' && this.ticket.status === 'INIT')) {
        this.loadSavedData()
      }
      this.setUserInfo()
    },
    async loadSavedData() {
      try {
        const param = {
          TICKET_TYPE: 'RT',
          TICKET_ID: this.ticket.ticket_id,
          MAX_DAYS: 31,
          IS_MBA: true
        }
        const res = await apiRcaRequest('SELECT_TICKET_HANDLING_CURRENT_LIST', param)
        const [item] = res.result
        this._merge(this.ticket, item)
        this._merge(this.userInput, item)
      } catch (error) {
        this.error(error)
      } finally {
        this.setUserInfo()
      }
    },
    onClose() {
    },
    resetForm() {
      for (const key in this.userInput) {
        this.userInput[key] = ''
      }
    },
    setUserInfo() {
      const { user } = this.$store.state
      const userInfo = {
        handling_user: user.name,
        handling_dept: user.info?.deptName || '-', // 소속 팀
        handling_agency: user.info?.agencyName || '-', // 분류 (NOC/EMS)
        handling_time: this.toStringTime(new Date(), 'YYYY-MM-DD HH:mm:ss')
      }
      this._merge(this.ticket, userInfo)
    },
    getProcessParam() {
      const { ticket, userInput } = this
      const param = {
        TICKET_TYPE: ticket.ticket_type,
        TICKET_ID: ticket.ticket_id,
        STATUS: this.processType === 'ack' ? 'ACK' : 'FIN',
        RCA_ACCURACY: '',
        HANDLING_CONTENT: userInput.handling_content || ''
      }
      const ack_param = {
        HANDLING_USER: ticket.handling_user,
        HANDLING_DEPT: ticket.handling_dept,
        HANDLING_AGENCY: ticket.handling_agency
      }
      const fin_param = {
        HANDLING_FIN_USER: ticket.handling_user,
        HANDLING_FIN_DEPT: ticket.handling_dept,
        HANDLING_FIN_AGENCY: ticket.handling_agency,
        HANDLING_FIN_CONTENT: userInput.handling_fin_content || ''
      }
      this.processType === 'ack' ? Object.assign(param, ack_param) : Object.assign(param, fin_param)
      return param
    },
    isValidCheck() {
      let isValid = false
      this.$refs.processForm.validate((valid) => {
        if (!valid) {
          isValid = true
          this.$message({ type: 'warning', message: '필수 값을 입력해주세요.' })
        }
      })
      return isValid
    },
    handleClickProcess() {
      if (this.isValidCheck()) { return }

      const processText = this.processType === 'ack' ? '인지' : '마감'
      this.confirm('확인을 선택하면 데이터가 저장됩니다.', `${processText} 하시겠습니까?`, {
        confirmButtonText: `${processText}처리`,
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const param = this.getProcessParam()
          await apiUserProcess('USER_PROCESS_MBA', param)
          this.$message({
            type: 'info',
            message: '작업을 요청하였습니다. 3~5초 후 작업이 완료 될 예정입니다.'
          })
        } catch (error) {
          this.debug && this.error(error)
        } finally {
          this.close()
        }
      }).catch(() => {})
    }
  }
}
export default _component
</script>

<style lang="scss">
.ModalRcaProcessHandling {
  .process.el-select {
    width: 100px;
  }
  .process .el-input.is-disabled .el-input__inner {
    width: 100px !important;
    margin-left: 0px !important;
  }
}
</style>
<style lang="scss" scoped>
.info-container {
  .title {
    font-weight: 800;
  }
  .info {
    color: #009688;
  }
}
.box-container {
  padding: 10px 0px;
  .process-title {
    min-width: 50px;
    font-size: 12px;
    font-weight: 600;
  }
}
.az-title {
  font-size: 13px;
  font-weight: 800;
  padding: 0px 15px;
}
.container-title {
  margin-top: 1em;
  font-weight: 800;
}
</style>
