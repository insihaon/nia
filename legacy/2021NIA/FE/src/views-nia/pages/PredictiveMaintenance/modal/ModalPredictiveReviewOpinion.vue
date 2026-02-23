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
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-tickets" /> 광모듈 장애 예측 검토 의견
          <hr>
        </span>
        <comp-ag-grid ref="reviewGrid" v-model="reviewGrid" style="height: calc(100% - 80px);" />
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
                <el-button class="completeBtn" size="medium" @click="handleInsertOperatorResult('등록')">등록</el-button>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>

        <div slot="footer" class="dialog-footer">
          <hr>
          <!-- <el-button class="completeBtn" size="medium" @click="handleInsertOperatorResult()">
            등록
          </el-button> -->
          <el-button size="medium" class="closeBtn" @click="close()">
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
import UntactCellRenderButtons from '@/views-nia/components/cellRenderer/UntactCellRenderButtons'

const routeName = 'ModalPredictiveReviewOpinion'

const _component = {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, UntactCellRenderButtons },
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
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowHeight: 25 }
      const columns = [
        { type: '', prop: 'handling_user', name: '작성자', width: 120 },
        { type: '', prop: 'handling_dept', name: '소속', width: 180 },
        { type: '', prop: 'handling_content', name: '의견', width: 480 },
        { type: '', prop: 'handling_time', name: '작성 시각', width: 200, format: (row) => { return this.moment(row.handling_time).format('YYYY-MM-DD HH:mm:ss') } },
        { type: '', prop: '-', name: '기능', width: 170, cellRendererFramework: 'UntactCellRenderButtons', cellRendererParams: { action: this.onClickEditButton.bind(this) } }
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
        handling_dept: this.untact.authority?.workarea || '', // 담당 부서
        handling_agency: this.untact.authority?.workarea?.split(' ')[0] || '', // 소속 본부,
        handling_content: ''
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
        this.reviewList = res?.data
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
        cancelButtonText: '취소'
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
  font-family: 'NanumSquare';
  transform: skew(0.03deg);
  .el-dialog {
    border: 1px solid #043644;
    border-bottom: 11px solid #043644d6;
    box-shadow: 0 1px 5px 0 rgb(0 0 0 / 27%);
    border-radius: 5px;
    height: 440px;
  }
  .el-dialog__header {
    transform: skew(0.03deg);
    span {
      font-size: 15px;
      font-weight: 800;
    }
  }
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
      transform: rotate(0.03deg);
    }
    .textarea .el-form-item {
      width: 100%;
      display: inline-flex;
      .el-form-item__content {
        display: flex;
        width: calc(100% - 40px);
        button.completeBtn {
          margin-left: 5px;
          background-color: #043644;
          color: white;
          border: 1px solid #043644;
        }
      }
    }
    .el-textarea__inner {
      resize: none;
      height: 65px;
    }
  }
  .el-dialog__footer {
    height: 60px !important;
    padding: 0 20px;

    button.closeBtn {
      color: #043644;
      background-color: white;
      border: 1px solid #043644;
    }
    button:not(.is-disable):hover {
      color: #fff;
      background-color: #043644;
    }
  }
}
</style>
