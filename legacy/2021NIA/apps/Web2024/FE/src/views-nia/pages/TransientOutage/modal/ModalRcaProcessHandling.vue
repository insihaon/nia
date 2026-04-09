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
        class="untact-modal"
        :class="{ [name]: true , 'cable': (processType === 'fin' && isCable) || (!isMba && !isCable)}"
      >
        <span slot="title" style="transform: skew(0.03deg);">
          <i class="el-icon-check" /> TT {{ getTitle }}
          <hr>
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
            <div class="az-title">{{ isCable ?'장애코어': '근원장애' }} System/Port 정보</div>
            <comp-ag-grid ref="ackSystemAgGrid" v-model="ackSystemGridData" class="miniGridHeader" />
          </el-col>

          <el-col v-if="!isMba" class="ack-cable-system-info">
            <div class="az-title">근원장애 광케이블 리스트 정보</div>
            <comp-ag-grid ref="rootDisorderGridData" v-model="rootDisorderGridData" class="GridHeader" />
          </el-col>
          <el-form
            ref="processForm"
            :model="userInput"
            :rules="EDIT_RULES"
            class="h-100"
            style="padding: 0 15px;"
          >

            <el-col v-if="!isMba && !isCable" class="box-container">
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="pb-2 flex flex items-end">
                <div class="container-title pr-2">AI 분석 결과 항목</div>
                <div style="color: grey;">{{ ticket.root_cause_type }}</div>
              </el-col>
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
                <el-form-item label="AI 장애일치 분석 여부" style="display: flex;">
                  <el-radio-group v-model="userInput.accuracy" size="small">
                    <el-radio-button v-for="value in ['일치', '불일치']" :key="value" :label="value">
                      {{ value }}
                    </el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-col>

            <el-col v-if="(processType === 'fin' && !isMba) || (!isMba && !isCable)">
              <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12" class="pb-2 flex items-end">
                <div class="container-title pr-2">장애 상세 코드</div>
                <div style="color: grey;">{{ ticket.ticket_rca_result_code }}</div>
                <!-- TICKET_RCA_RESULT_CODE -->
              </el-col>
            </el-col>

            <el-col class="box-container">
              <el-col :span="24" class="pb-2 flex">
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

              <el-col v-if="(processType === 'fin' && isCable) || (!isMba && !isCable)" class="box-container">
                <div class="container-title pb-2">TOPAS TT 일괄처리 (TOPAS TT인지/확인처리)</div>
                <div class="d-flex" style="flex-direction: row; flex-wrap: wrap;">
                  <template v-for="item in classificationForm">
                    <div v-if="item.show" :key="item.label" class="d-flex items-center p-1"> <!-- :span="12" -->
                      <div class="process-title">{{ item.label }}</div>
                      <div v-for="childIndex in item.childNum" :key="childIndex">
                        <el-select
                          v-model="item.model[`lv${childIndex}`]"
                          :placeholder="`분류표${childIndex}`"
                          popper-class="reasonSelectMenu-popper-option"
                          class="process"
                          :disabled="isDisabledClassification(item.opName ,`lv${childIndex}`)"
                          @change="onChangeClassification(item.modelName, item.opName, `LV${childIndex+1}`)"
                        >
                          <el-option
                            v-for="option in item.options[`lv${childIndex}`]"
                            :key="option.item"
                            :label="option.item"
                            :value="option.item"
                          />
                        </el-select>
                      </div>
                    </div>
                  </template>
                </div>
              </el-col>
            </el-col>
          </el-form>
        </el-row>

        <span slot="footer" class="dialog-footer">
          <hr>
          <el-button class="gray-btn" @click="handleClickProcess">{{ processType ==='ack'? '인지':'마감' }}처리</el-button>
          <el-button class="gray-btn" @click="close()">취소</el-button>
        </span>
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
      isMba: false,
      isCable: false,
      processType: '',
      ticket: '',
      status: '',
      userInput: {
        accuracy: '',
        fault_type: '',
        handling_content: '',
        handling_fin_content: ''
      },
      EDIT_RULES: {
        // accuracy: rulesEmptyEdit(),
        handling_content: rulesProcessEdit('_ack'),
        handling_fin_content: rulesProcessEdit('_fin')
      },
      systemCoreList: [],
      rootDisorderList: [],
      handling_time: '',
      faultreason: { lv1: [], lv2: [], lv3: [] },
      faultcharge: { lv1: [], lv2: [] },
      reason: { lv1: '', lv2: '', lv3: '' },
      charge: { lv1: '', lv2: '' },
      clear: { lv1: '' },
      reasonOp: { lv1: { disabled: false }, lv2: { disabled: true }, lv3: { disabled: true } },
      chargeOp: { lv1: { disabled: false }, lv2: { disabled: true } },
      clearOp: { lv1: { disabled: false } },
    }
  },
  computed: {
    inputForm() {
      let ticketIdProp = this.isCable ? 'inspector_seq' : 'ticket_id'
      if (this.isCable) {
        ticketIdProp = 'inspector_seq'
      } else if (this.isMba) {
        ticketIdProp = 'ticket_id'
      } else {
        ticketIdProp = 'rca_ticketid'
      }
      return [
        { label: 'TT번호', prop: ticketIdProp, show: true },
        { label: '티켓발생시간', prop: 'generation_time', show: true, type: 'time' },
        { label: '장애발생시간', prop: 'fault_time', show: true, type: 'time' },
        { label: '클러스터 No.', prop: 'cluster_no', show: !this.isCable && !this.isMba },
        { label: '근원알람개수', prop: 'total_related_alarm_count', show: !this.isCable && !this.isMba },
        { label: '전체 알람 수', prop: 'total_alarm_count', show: this.isCable || !this.isMba },
        { label: '장애 광케이블명', prop: 'ocaname', show: this.isCable },
        { label: '장애코어수', prop: 'fault_cnt', show: this.isCable },
        { label: '감시코어수', prop: 'monitoring_cnt', show: this.isCable },
        { label: '총 코어수', prop: 'optcore_total_cnt', show: this.isCable },
        { label: '공사여부', prop: 'const_yn', show: this.isCable },
        { label: `${this.processType === 'ack' ? '인지' : '마감'} 담당자`, prop: 'handling_user', show: true },
        { label: '소속 본부', prop: 'handling_dept', show: true },
        { label: '마감시작시간', prop: 'handling_time', show: this.processType === 'fin' },
        { label: '소속 팀', prop: 'handling_agency', show: true }
      ]
    },
    classificationForm() {
      return [
        { label: '원인분류', childNum: 3, modelName: 'reason', model: this.reason, opName: 'reasonOp', options: this.faultreason, show: true, },
        { label: '시설분류', childNum: 2, modelName: 'charge', model: this.charge, opName: 'chargeOp', options: this.faultcharge, show: true },
        { label: '마감코드', childNum: 3, modelName: 'clear', model: this.clear, opName: 'clearOp', options: '', show: this.processType === 'fin' }
      ]
    },
    radioForm() {
      let label = ''
      let items = []
      if (this.isCable) {
        label = '선로장애 일치 여부'
        items = ['일치', '불일치']
      } else if (this.isMba) {
        label = '장애유형'
        items = ['회선순단', '공사', '시설(선로)피해', '미적용 경보']
      }
      return { label, items }
    },
    columns() {
      return
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
    rootDisorderGridData() {
      const columns = [
        { type: '', prop: 'ocaname', name: '광케이블명', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'locdescriptiona', name: 'A축 설치장소', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'locdescriptionz', name: 'Z축 설치장소', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: '_', name: '장애확률', width: '5rem', alignItems: 'center', sortable: true, format: (row) => { return `${row.cnt || '0' + '/' + row.rca_max_cnt || '0' + row.max_cnt || '0'}` } }
      ]
      const options = { name: this.name, checkable: false, height: '150px' }
      return { options, columns, data: this.rootDisorderList }
    },
    getTitle() {
      const { status } = this.ticket
      let result = ''
      let prefix = ''
      if (this.ticket?.ticket_type === 'CIT2') {
        prefix = '선로장애'
      }
      if (this.processType === 'ack') {
        result = status === 'INIT' ? '인지처리' : '인지수정'
      } else if (this.processType === 'fin') {
        result = status === 'FIN' ? '마감수정' : '마감처리'
      }
      return prefix + result
    }
  },
  mounted () {
    this.reason = { lv1: { value: '', disabled: false }, lv2: { value: '', disabled: true }, lv3: { value: '', disabled: true } }
    this.charge = { lv1: { value: '', disabled: false }, lv2: { value: '', disabled: true } }
    this.clear = { lv1: { value: '', disabled: false } }
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
      this.processType = model.processType /* INIT, ACK, FIN */
      this.systemCoreList = model?.systemCoreList

      this.setPageType()

      this.ticket['generationTime'] = this.toStringTime(model.generation_time, 'YYYY-MM-DD HH:mm:ss')
      this.ticket['faultTime'] = this.toStringTime(model.fault_time, 'YYYY-MM-DD HH:mm:ss')
      this.ticket['const_yn'] = model.const_no ? 'Y' : 'N'

      const { ticket } = this

      if (!(this.processType === 'ack' && ticket.status === 'INIT')) {
        this.loadSavedData()
      }
      if ((this.processType === 'fin' && this.isCable) || (!this.isMba && !this.isCable)) {
        this.loadFaultList('reason')
        this.loadFaultList('charge')
      }
      this.setUserInfo()
    },
    isDisabledClassification(opName, lv) {
      return this[opName][lv]?.disabled ?? true
    },
    onChangeClassification(type = null, opName = null, lv = 'LV1') {
      if (opName === null || this[opName][lv.toLowerCase()] === undefined) return

      this[opName][lv.toLocaleLowerCase()].disabled = !this[opName][lv.toLocaleLowerCase()].disabled
      this.loadFaultList(type, lv)
    },
    async loadFaultList(type = null, lv = 'LV1') {
      if (type === null || this[type][lv.toLowerCase()] === undefined) return
      try {
        let params = {}
        switch (lv) {
          case 'LV1':
            break
          case 'LV2':
            this.ticket.reason_level2 = ''
            this.ticket.reason_level3 = ''
            params = { LEVEL1: this[type].lv1 }
            break
          case 'LV3':
            this.ticket.reason_level3 = ''
            params = { LEVEL1: this[type].lv1, LEVEL2: this[type].lv2 }
            break
          default:
            break
        }
        const res = await apiRcaRequest(`SELECT_FAULT${type.toUpperCase()}_${lv}_LIST`, params)
        this[`fault${type}`][lv.toLowerCase()] = res.result
      } catch (error) {
        this.error(error)
      }
    },
    async loadSavedData() {
      let ticketIdProp
      if (this.isCable) {
        ticketIdProp = 'inspector_seq'
      } else if (this.isMba) {
        ticketIdProp = 'ticket_id'
      } else {
        ticketIdProp = 'rca_ticketid'
      }
      try {
        const param = {
          TICKET_TYPE: this.isCable ? 'CIT2' : 'RT',
          TICKET_ID: this.ticket[ticketIdProp],
          MAX_DAYS: 31
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
    setPageType() {
      const type = this.ticket.root_cause_type
      switch (type) {
        case 'MomentaryBreakoff':
          this.isMba = true
          break
        case 'CableCut':
          this.isCable = true
          break
        default:
          break
      }
    },
    onChangeSelectedVal() {
      this.$forceUpdate()
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
        handling_dept: user.information.deptName || '', // 소속 팀
        handling_agency: user.information.agencyName || '', // 소속본부,
        handling_time: this.toStringTime(new Date(), 'YYYY-MM-DD HH:mm:ss')
      }
      this._merge(this.ticket, userInfo)
    },
    getProcessParam() {
      const { ticket, userInput } = this
      const param = {
        TICKET_TYPE: ticket.ticket_type,
        TICKET_ID: ticket?.rca_ticketid ?? ticket.inspector_seq,
        STATUS: this.processType === 'ack' ? 'ACK' : 'FIN',
        // RCA_ACCURACY: userInput.accuracy,
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
      if (this.isCable) {
        Object.assign(param, {
          REASON_LEVEL1: this.reason.lv1,
          REASON_LEVEL2: this.reason.lv2,
          REASON_LEVEL3: this.reason.lv3,
          CHARGE_LEVEL1: this.charge.lv1,
          CHARGE_LEVEL2: this.charge.lv2
          // FAULT_TYPE: userInput.fault_type
        })
        if (this.processType === 'fin') {
          Object.assign(param, {
            FIN_LEVEL1: this.clear.lv1
          })
        }
      }
      this.processType === 'ack' ? Object.assign(param, ack_param) : Object.assign(param, fin_param)
      return param
    },
    isValidCheck() {
      let isValid = false
      this.$refs.processForm.validate((valid) => {
        if (!valid) {
          // alert(valid)
          isValid = true
          this.$message({ type: 'warning', message: `필수 값을 입력해주세요.` })
        }
      })
      return isValid
    },
    handleInspectorProcess() { // mq send
      if (this.isValidCheck()) { return }

      const processText = this.processType === 'ack' ? '인지' : '마감'
      this.confirm('확인을 선택하면 데이터가 저장됩니다.', `${processText} 하시겠습니까?`, {
        confirmButtonText: `${processText}처리`,
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const param = this.getProcessParam()
          await apiUserProcess(param, this.isMba ? 'mba' : 'inspecor')
          this.$message({ type: 'success', message: `${processText} 되었습니다.` })
        } catch (error) {
          this.debug && this.error(error)
        } finally {
          this.close()
        }
      }).catch(() => {})
    },
    handleClickProcess() { // db insert 23.02.16 기준 inspector 처리기능 x
      if (this.isValidCheck()) { return }

      const processText = this.processType === 'ack' ? '인지' : '마감'
      this.confirm('확인을 선택하면 데이터가 저장됩니다.', `${processText} 하시겠습니까?`, {
        confirmButtonText: `${processText}처리`,
        cancelButtonText: '취소'
      }).then(async() => {
        try {
          const param = this.getProcessParam()
          // const res = await apiRcaRequest('MERGE_HANDLE_STATUS_CURRENT', param, '/modify')
          await apiUserProcess(param, 'equip')
          // this.$message({ type: 'success', message: `${processText}처리 되었습니다.` })
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
    // 참고
  //   handleSendAiOperatorResult() {
  //     this.$confirm('확인을 선택하면 운용자 재검토 의견이 저장됩니다.', '운용자 재검토', {
  //       confirmButtonText: '확인',
  //       cancelButtonText: '취소'
  //     }).then(async() => {
  //       try {
  //         delete (this.result.ticket_generationtime)
  //         delete (this.result.fault_time)
  //         await apiUserProcess(this.result, 'aiOperator')
  //         // console.log('data ===> ' + param)
  //         this.$message({ type: 'success', message: '저장되었습니다.' })
  //       } catch (error) {
  //         this.debug && this.error(error)
  //       } finally {
  //         this.close()
  //       }
  //     }).catch(() => {})
  //   }
  // }
  }
}
export default _component
</script>

<style lang="scss">
@import "~@/styles/variables.scss";
.cable.ModalRcaProcessHandling .el-dialog {
  margin-top: 2rem !important;
  // height: 100%;
}
.ModalRcaProcessHandling {
  font-family: 'NanumSquare';
  transform: skew(0.03deg);

  // .el-dialog__body {
  //   height: calc(100% - 130px);
  // }
  .el-dialog__footer {
    text-align: right;
    .gray-btn {
      background-color: #2a394e;
      border-color: #2a394e;
      color: #fff;
      box-shadow: 0 2px 5px 0 rgb(0 0 0 / 26%);
      &:hover {
        opacity: 0.7;
      }
    }
  }
  .el-radio-button__inner {
    color: #fff;
    font-weight: 600;
    margin-right: 5px;
    background: black;
    transform: skew(0.03deg);
    box-shadow: 0 2px 5px 0 rgb(0 0 0 / 26%);
  }
  .process.el-select {
    width: 100px;
  }
  .process .el-input.is-disabled .el-input__inner {
    width: 100px !important;
    margin-left: 0px !important;
  }
  .is-active .el-radio-button__inner {
    color: #2f2f2f;
    border: solid 1px #e4e1e1;
    background-color: rgb(238,238,238);
  }
  .el-form-item__label {
    transform: skew(0.03deg);
  }
}

.reasonSelectMenu-popper-option {
    min-width: 100px !important;
  .el-select-dropdown__item {
    font-size:13px;
  }
}
</style>
<style lang="scss" scoped>
.info-container {
  div {
    transform: skew(0.03deg);
  }
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
  transform: skew(0.03deg);
}
.container-title {
  margin-top: 1em;
  font-weight: 800;
  transform: skew(0.03deg);
}
.miniGridHeader {
  .ag-header-row {
    height: 35px !important;
  }
  .ag-header {
    height: 35px !important;
    min-height: 35px !important;
  }
  .ag-header-cell {
    color: #000;
    font-size: 1em;
    font-weight: 800;
  }
  .ag-cell, .ag-full-width-row .ag-cell-wrapper.ag-row-group {
    font-weight: 700;
    font-family: 'NanumSquare';
    font-size: 12px;
    line-height: 25px !important;
  }
}
</style>
