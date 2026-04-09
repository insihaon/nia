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
        class="untact-modal h-100"
        :class="{ [name]: true}"
      >
        <span slot="title" style="transform: skew(0.03deg);">
          <i class="el-icon-check" /> TT일괄처리
          <hr>
        </span>
        <comp-ag-grid id="mbaProcess" ref="batchGrid" v-model="batchGridData" class=" miniGridHeader" @changeSelectedRows="onChangeSelectedRows" />
        <span slot="footer" class="dialog-footer" style="text-align: right">
          <hr>
          <el-button class="gray-btn" :disabled="selectedRows.length === 0" @click="handleClickProcess('ACK', '인지')">
            <i class="el-icon-check" style="color: #F7AA17;font-weight: bold;" />
            인지</el-button>
          <el-button class="gray-btn" :disabled="selectedRows.length === 0" @click="openFixModal()">
            <i class="el-icon-check" style="color: #2421c1;font-weight: bold;" />
            조치
          </el-button>
          <el-button class="gray-btn" :disabled="selectedRows.length === 0" @click="handleClickProcess('FIN', '마감')">
            <i class="el-icon-check" style="color: #52A43A;font-weight: bold;" />
            마감
          </el-button>
          <el-button class="gray-btn" @click="close()">닫기</el-button>
        </span>
        <ModalRcaBatchFix ref="modalRcaBatchFix" @closeModal="closeFixModal" />
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import { Modal } from '@/min/Modal.min.js'
import elDragDialog from '@/directive/el-drag-dialog'
import { apiRcaRequest } from '@/api/nia'
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
        { type: '', prop: 'receivetime', name: '마감일시', width: '5rem', alignItems: 'center', sortable: true, format: (row) => { return row.receivetime ? this.toStringTime(row.receivetime, 'YYYY-MM-DD HH:mm:ss') : '' } },
        { type: '', prop: 'sysname', name: '시스템명', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'site', name: '발생국소', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'causesite', name: '원인국소', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'brokencause', name: '고장원인', width: '5rem', alignItems: 'center', sortable: true },
        { type: '', prop: 'responsibility', name: '책임분류', width: '5rem', alignItems: 'center', sortable: true },
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
    handleClickProcess(processType, typeText) {
      this.confirm(`선택한 항목을 ${typeText} 하시겠습니까? <br /> 확인을 선택하면 데이터가 저장됩니다.`, '메시지 창', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        dangerouslyUseHTMLString: true
      }).then(async() => {
        const list = this._cloneDeep(this.batchTicketList)
        const selectedRows = this.$refs.batchGrid?.gridApi.getSelectedRows()
        list.forEach(row => {
          if (selectedRows.map(v => v.ticketno).includes(row.ticketno)) {
            switch (processType) {
              case 'ACK':
                row['causesite'] = '공군작전지역국통사통'
                row['brokencause'] = '회선시험: 전송로시험'
                row['responsibility'] = '고객사유: 회선시험'
                break
              case 'FIX':
                row['fixed'] = this.fixed
                break
              case 'FIN':
                row.receivetime = new Date().getTime()
                break
              default:
                break
            }
          }
        })
        this.batchTicketList = [].concat(list)
        this.setScroll(processType)
        this.$message({ type: 'success', message: `${typeText} 되었습니다.` })
        this.selectedRows = []
      }).catch(() => {})
    },
    setScroll(processType) {
      const el = document.querySelector('#mbaProcess').getElementsByClassName('ag-body-horizontal-scroll-viewport')[0]
      if (processType === 'ACK' || processType === 'FIX') {
        el.scrollLeft = 900
      } else {
        el.scrollLeft = 455
      }
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

<style lang="scss">

</style>
