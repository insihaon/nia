<template>
  <div class="component-container" :class="{ [name]: true }">
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
          <i class="el-icon-tickets mr-2 text-base" /> 광모듈 장애 예측 검토 의견
        </span>
        <comp-ag-grid ref="reviewGrid" v-model="reviewGrid" style="height: 300px;" />
        <hr>
        <el-row style="height: 70px;">
          <el-form
            ref="loadSearchFilter"
            :model="infoForm"
            class="h-100"
          >
            <el-col :span="24" class="textarea">
              <el-form-item label="의견">
                <el-input
                  v-model="infoForm.handling_content"
                  placeholder="내용을 입력해주세요."
                  type="textarea"
                  clearable
                />
                <el-button type="primary" size="mini" @click="handleInsertOperatorResult('등록')">등록</el-button>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>

        <div slot="footer" class="dialog-footer">
          <el-button size="mini" type="info" icon="el-icon-close" @click="close()">
            닫기
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import { Modal } from '@/min/Modal.min.js'
import { apiRcaRequest } from '@/api/nia'
import elDragDialog from '@/directive/el-drag-dialog'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import RcaCellRenderButtons from '@/views-nia/components/cellRenderer/RcaCellRenderButtons'

const routeName = 'ModalPredictiveReviewOpinion'

const _component = {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, RcaCellRenderButtons },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedTicket: {},
      infoForm: {
        handling_content: ''
      },
      reviewList: []
    }
  },
  computed: {
    reviewGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      const columns = [
        { type: '', prop: 'handling_user', name: '작성자', width: 120 },
        { type: '', prop: 'handling_dept', name: '소속', width: 180 },
        { type: '', prop: 'handling_content', name: '의견', width: 480 },
        { type: '', prop: 'handling_time', name: '작성 시각', width: 200, format: (row) => { return this.moment(row.handling_time).format('YYYY-MM-DD HH:mm:ss') } },
        { type: '', prop: '-', name: '기능', width: 170, cellRendererFramework: 'RcaCellRenderButtons', cellRendererParams: { action: this.onClickEditButton.bind(this) } }
      ]
      return { options, columns, data: this.reviewList || [] }
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.minWidth = 1200
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedTicket = model
      const info = {
        ticketno: model.ticketno,
        issue_date: model.issue_date,
        handling_user: this.$store.state.user.name,
        handling_dept: this.rcaTicket.authority?.workarea || '', // 담당 부서
        handling_agency: this.rcaTicket.authority?.workarea?.split(' ')[0] || '', // 소속 본부,
        handling_content: '',
        seqnum: null
      }
      this.infoForm = this._cloneDeep(info)
      this.loadReviewList()
    },
    onClose() {
    },
    async loadReviewList() {
      const { ticketno, issue_date } = this.selectedTicket
      try {
        const res = await apiRcaRequest('SELECT_MBA_PREDICTIVE_REVIEW', { ticketno, issue_date })
        this.reviewList = res?.result
      } catch (error) {
        this.debug && this.error(error)
      } finally {
        this.onSaveReadList()
      }
    },
    onClickEditButton(process, row) {
      if ((process === '수정' || process === '삭제') && this.$store.state.user.name !== row.handling_user) {
        this.$message({ type: 'info', message: '글의 작성자만 수정/삭제가 가능합니다.' })
        return
      }
      if (process === '수정') {
        Object.assign(this.infoForm, row)
      } else {
        this.handleInsertOperatorResult(process, row)
      }
    },
    handleInsertOperatorResult(process, row) { // process = 등록, 수정, 삭제
      let _process = process
      if (process === '등록' && this.reviewList.map(v => v.seqnum).includes(this.infoForm.seqnum)) {
        _process = '수정'
      }
      this.$confirm(`검토 의견을 ${_process} 하시겠습니까?`, '검토 의견', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        customClass: 'nia-message-box',
      }).then(async() => {
        try {
          const form = this.infoForm
          this.infoForm.handling_time = this.moment().format('YYYY-MM-DD HH:mm:ss')

          const res = await apiRcaRequest(`${_process === '삭제' ? 'DELETE' : 'UPDATE'}_MBA_PREDICTIVE_REVIEW`, _process === '삭제' ? { seqnum: row.seqnum } : this.infoForm, '/modify')
          if (res.success) {
            this.$message({ type: 'success', message: '저장되었습니다.' })
            this.onSaveReadList()
          } else {
            this.$message({ type: 'error', message: '등록이 실패하였습니다.' })
          }
          this.infoForm.seqnum = null
          this.infoForm.handling_content = ''
        } catch (error) {
          this.debug && this.error(error)
        } finally {
          this.loadReviewList()
        }
      }).catch(() => {})
    },
    onSaveReadList() {
      const readOpinionByUser = JSON.parse(window.localStorage.getItem('ReadOpinionByUser')) ?? {}
      let readTicketsByUser = {}
      if (readOpinionByUser[this.$store.state.user.uid]) {
        readTicketsByUser = readOpinionByUser[this.$store.state.user.uid] ?? {}
      }
      const { ticketno } = this.selectedTicket
      if (readTicketsByUser[ticketno]) {
        readTicketsByUser[ticketno].readList = this.reviewList.map(row => row.seqnum)
        readTicketsByUser[ticketno].readTime = this.moment().format('YYYY-MM-DD HH:mm:ss')
      } else {
        Object.assign(readTicketsByUser, { [ticketno]: { readTime: this.moment().format('YYYY-MM-DD HH:mm:ss'), readList: this.reviewList.map(row => row.seqnum) } })
      }
      window.localStorage.setItem('ReadOpinionByUser', JSON.stringify(Object.assign(readOpinionByUser, { [this.$store.state.user.uid]: readTicketsByUser })))
    }
  }
}
export default _component
</script>

<style lang="scss" scoped>
.ModalPredictiveReviewOpinion::v-deep {
  .el-dialog__body {
    height: calc(100% - 130px);
    padding: 0 15px;
    .el-col {
      padding: 0 10px;
    }
    .ag-cell {
      text-align: center;
    }
    .el-form-item__label {
      font-weight: 800;
      display: flex;
      align-items: center;
    }
    .textarea .el-form-item {
      width: 100%;
      display: inline-flex;
      .el-form-item__content {
        display: flex;
        width: calc(100% - 40px);
        button.completeBtn {
          margin-left: 5px;
        }
      }
    }
    .el-textarea__inner {
      resize: none;
      height: 65px;
    }
  }
}
</style>
