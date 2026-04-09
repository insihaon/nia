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
        :class="{ [name]: true}"
      >
        <span slot="title">
          TT일괄처리
        </span>
        <comp-ag-grid id="mbaProcess" ref="batchGrid" v-model="batchGridData" style="height: 500px" class=" miniGridHeader" @changeSelectedRows="onChangeSelectedRows" />
        <div slot="footer" class="dialog-footer">
          <el-button size="mini" type="primary" :disabled="selectedRows.length === 0" @click="handleClickProcess('ACK', '인지')">
            <i class="el-icon-check" />
            인지</el-button>
          <el-button size="mini" type="primary" :disabled="selectedRows.length === 0" @click="openFixModal()">
            <i class="el-icon-edit" />
            조치
          </el-button>
          <el-button size="mini" type="primary" :disabled="selectedRows.length === 0" @click="handleClickProcess('FIN', '마감')">
            <i class="el-icon-check" />
            마감
          </el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click="close()">닫기</el-button>
        </div>
        <ModalRcaBatchFix ref="modalRcaBatchFix" @closeModal="closeFixModal" />
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import { Modal } from '@/min/Modal.min.js'
import elDragDialog from '@/directive/el-drag-dialog'
import { apiRcaRequest, apiUserProcess } from '@/api/nia'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import ModalRcaBatchFix from '@/views-nia/pages/TransientOutage/modal/ModalRcaBatchFix'
// import moment from 'moment'

const routeName = 'ModalRcaBatchProcessing'

const _component = {
  name: routeName,
  components: { CompAgGrid, ModalRcaBatchFix },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      batchTicketList: [],
      selectedRows: []
    }
  },
  computed: {
    columns() {
      return [
        { type: '', prop: 'ticketno', name: 'TT No.', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'alarmtime', name: '발생일시', width: '5rem', alignItems: 'center', sortable: true, format: (row) => { return this.toStringTime(row.alarmtime, 'YYYY-MM-DD HH:mm:ss') } },
        { type: '', prop: 'receivetime', name: '마감일시', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'sysname', name: '시스템명', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'site', name: '발생국소', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'status', name: '상태', width: '4rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'fixed', name: '수리내용', width: '5rem', alignItems: 'center', sortable: true }
      ]
    },
    batchGridData() {
      const { columns } = this
      const options = { name: this.name, checkable: true, height: '300px', rowSelection: 'multiple' }
      return { options, columns, data: this.batchTicketList }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.minWidth = 1000
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.ticket = model.ticket
      this.loadBatchTicketList()
    },
    onChangeSelectedRows(rows) {
      this.selectedRows = this.$refs?.batchGrid?.gridApi?.getSelectedRows() || []
    },
    async loadBatchTicketList() {
      try {
        const res = await apiRcaRequest('SELECT_TICKET_ALARM_BATCH_LIST', { TICKET_ID: this.ticket.ticket_id })
        this.batchTicketList = res.result || []
      } catch (error) {
        this.error(error)
      }
    },
    getUserInfo() {
      const { user } = this.$store.state
      return {
        HANDLING_USER: user.name,
        HANDLING_DEPT: user.info?.deptName || '',
        HANDLING_AGENCY: user.info?.agencyName || ''
      }
    },
    buildParam(row, processType) {
      const userInfo = this.getUserInfo()
      const base = {
        TICKET_ID: row.ticket_id,
        TICKET_TYPE: row.ticket_type || 'MomentaryBreakoff',
        STATUS: processType === 'FIX' ? 'ACK' : processType,
        RCA_ACCURACY: '',
        HANDLING_CONTENT: ''
      }
      switch (processType) {
        case 'ACK':
          Object.assign(base, userInfo)
          break
        case 'FIX':
          Object.assign(base, userInfo, {
            HANDLING_CONTENT: this.fixed || ''
          })
          break
        case 'FIN':
          Object.assign(base, {
            HANDLING_FIN_USER: userInfo.HANDLING_USER,
            HANDLING_FIN_DEPT: userInfo.HANDLING_DEPT,
            HANDLING_FIN_AGENCY: userInfo.HANDLING_AGENCY,
            HANDLING_FIN_CONTENT: ''
          })
          break
      }
      return base
    },
    handleClickProcess(processType, typeText) {
      const selectedRows = this.$refs.batchGrid?.gridApi.getSelectedRows() || []
      if (selectedRows.length === 0) return

      this.confirm(
        `선택한 ${selectedRows.length}건을 ${typeText} 하시겠습니까?<br/>확인을 선택하면 데이터가 저장됩니다.`,
        '메시지 창',
        { confirmButtonText: '확인', cancelButtonText: '취소', customClass: 'nia-message-box', dangerouslyUseHTMLString: true }
      ).then(async() => {
        try {
          for (const row of selectedRows) {
            const param = this.buildParam(row, processType)
            await apiUserProcess('USER_PROCESS_MBA', param)
          }
          this.$message({ type: 'success', message: `${selectedRows.length}건 ${typeText} 처리되었습니다.` })
          await this.loadBatchTicketList()
          this.selectedRows = []
        } catch (error) {
          this.$message({ type: 'error', message: `${typeText} 처리 중 오류가 발생했습니다.` })
          this.debug && this.error(error)
        }
      }).catch(() => {})
    },
    openFixModal() {
      this.$refs.modalRcaBatchFix.open()
    },
    closeFixModal(fixed) {
      this.fixed = fixed
      this.handleClickProcess('FIX', '조치')
    }
  }
}
export default _component
</script>

<style lang="scss" scoped>
</style>
