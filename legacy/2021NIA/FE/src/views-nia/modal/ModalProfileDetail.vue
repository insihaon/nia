<template>
  <div>
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="false"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-edit-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-user mr-2" style="font-size: 17px" />
          {{ '조치프로파일 상세보기' }}
          <hr />
        </span>
        <table class="basic">
          <th class="disable">프로파일제목</th>
          <td colspan="3">
            <el-input v-model="profile_title" />
          </td>
          <tr>
            <th>프로파일설명</th>
            <td colspan="3" class="disable">
              <el-input v-model="profile_desc" type="textarea" />
            </td>
          </tr>
          <tr>
            <th>네트워크구분</th>
            <td colspan="3">
              <el-radio-group v-model="network_type" size="mini" class="d-flex">
                <el-radio label="전송">KOREN(전송)</el-radio>
                <el-radio label="IP">KOREN(IP)</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <th>장애대응구분</th>
            <td colspan="3">
              <el-radio-group v-model="processing_template" size="mini" class="d-flex">
                <el-radio label="recovery">자가회복</el-radio>
                <el-radio label="construction">공사</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <th>자동처리기간</th>
            <td colspan="3" style="text-align: left">
              <el-date-picker v-model="autoProcTime" type="daterange" range-separator="~" start-placeholder="시작일" end-placeholder="종료일" :disabled="auto_process_check" />
            &nbsp;
              <el-checkbox v-model="auto_process_check" label="상시" />
            </td>
          </tr>
          <tr>
            <th>Ticket 유형</th>
            <td>
              <el-select v-model="ticket_type">
                <el-option v-for="item in ticketType" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
            <th>장애 유형</th>
            <td>
              <el-select v-model="process_type">
                <el-option v-for="item in alarmType" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
          </tr>
          <th colspan="4" class="line-class">자동처리 대상 등록</th>
          <tr>
            <th>노드명</th>
            <td colspan="3">
              <el-select v-model="selectNode" style="min-width: 70%">
                <el-option v-for="item in nodeName" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
              <el-button size="mideum" style="float: right" plain round type="info" @click="handleInsertNode()">추가 </el-button>
            </td>
          </tr>
          <tr>
            <td colspan="4">
              <el-table class="node-table" :data="tableData" style="width: 100%;">
                <el-table-column prop="name" label="노드명" />
                <el-table-column width="100%">
                  <template slot-scope="scope">
                    <el-button size="mini" plain round type="danger" @click="handleDeleteNode(scope.$index, scope.row)"><i class="el-icon-delete" /></el-button>
                  </template>
                </el-table-column>
              </el-table>
            </td>
          </tr>
          <tr>
            <th>자동회복 처리</th>
            <td>
              <el-select v-model="auto_recovery" multiple>
                <el-option v-for="item in procOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </td>
            <th>메일 자동 발송</th>
            <td>
              <el-checkbox v-model="email_check" />
            </td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" type="info" @click.native="modeChange()">
            {{ btnMode }}
          </el-button>
          <el-button v-if="viewType === 'profileDetail'" size="small" type="danger" plain @click.native="handleDeleteProfile()">
            {{ '삭제' }}
          </el-button>
          <el-button class="exit-btn" size="small" @click.native="close()">
            {{ $t('exit') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import {
  apiTicketTypeCode,
  apiAlarmTypeCode,
  apiProfileNodeCode,
  apiProfileRecoveryList,
  apiInsertProfileRecovery,
  apiDeleteProfileRecovery,
  apiInsertProfileList,
  apiDeleteProfileList,
  apiUpdateProfileList,
} from '@/api/nia'

const routeName = 'ModalProfileDetail'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      title: '',
      rowInfo: {},
      profile_title: '',
      profile_desc: '',
      network_type: '',
      processing_template: '',
      autoProcTime: [],
      ticketType: [],
      ticket_type: '',
      alarmType: [],
      process_type: '',
      auto_recovery: '',
      auto_process_check: false,
      email_check: false,
      tableData: [],
      procOptions: [
        { value: 'ACK', label: '인지' },
        { value: 'FIN', label: '마감' },
        { value: 'AUTO_FIN', label: '자동마감' },
      ],
      ticketData: [],
      nodeName: [],
      selectNode: '',
    }
  },
  computed: {
    ...mapState({
      viewport: (state) => state.app.viewport,
      username: (state) => state.user.name,
    }),
    loginUsername() {
      const userNM = this.username ? this.username.replace(/.$/, '*') : 'UNKONWN'
      return userNM
    },
    btnMode() {
      return this.viewType === 'profileDetail' ? '저장' : '등록'
    },
  },
  watch: {
    network_type: function (newVal, oldVal) {
      if (newVal !== oldVal) {
        this.onLoadNodeCode()
        this.onLoadTicketCode()
      }
    },
    ticket_type: function (newVal, oldVal) {
      if (newVal !== oldVal) {
        this.onLoadAlarmCode()
      }
    },
  },
  mounted() {},
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.rowInfo = this._cloneDeep(model.row)
      if (this.viewType === 'OPEN') {
        this.profile_title = ''
        this.profile_desc = ''
        this.network_type = ''
        this.processing_template = ''
        this.autoProcTime = []
        this.ticket_type = ''
        this.process_type = ''
      } else {
        this.profile_title = this.rowInfo.profile_title
        this.profile_desc = this.rowInfo.profile_desc
        this.network_type = this.rowInfo.network_type
        this.processing_template = this.rowInfo.processing_template
        this.auto_process_check = this.rowInfo.auto_process_check
        this.email_check = this.rowInfo.email_check

        this.autoProcTime = [this.rowInfo.auto_process_start_datetime, this.rowInfo.auto_process_end_datetime].filter((time) => time !== null)
      }
      this.onLoadTicketCode()
      this.onLoadAlarmCode()
      this.onLoadNodeCode()
      this.onLoadNodeRecovery()
    },
    modeChange() {
      if (this.viewType === 'profileDetail') {
        this.handleUpdateProfile()
      } else {
        this.handleInsertProfile()
      }
    },
    /* 티켓 유형 코드 리스트*/
    async onLoadTicketCode() {
      const param = {
        ticket_gb: this.network_type,
      }
      try {
        const res = await apiTicketTypeCode(param)
        const codeData = res.result.map((item) => ({
          label: item.ticket_type,
          value: item.ticket_type,
          ticket_gb: item.ticket_gb,
        }))
        if (this.network_type === '전송') {
          this.ticketType = codeData.filter((item) => item.type === 'POTN')
        } else {
          this.ticketType = codeData.filter((item) => item.type === 'SWITCH')
        }
        this.ticketType = codeData
        const matchingItem = this.ticketType.find((item) => item.label === this.rowInfo.ticket_type)
        this.ticket_type = matchingItem?.value
      } catch (error) {
        console.error(error)
      }
    },
    /* 장애 유형 코드 리스트 */
    async onLoadAlarmCode() {
      const param = {
        ticket_type: this.ticket_type,
      }
      try {
        const res = await apiAlarmTypeCode(param)
        const codeData = res.result.map((item) => ({
          label: item.fail_type,
          value: item.fail_type,
        }))
        this.alarmType = codeData
        const matchingItem = this.alarmType.find((item) => item.label === this.rowInfo.process_type)
        this.process_type = matchingItem?.value
      } catch (error) {
        console.error(error)
      }
    },
    /* 노드명 코드 리스트  */
    async onLoadNodeCode() {
      try {
        const res = await apiProfileNodeCode()
        const codeData = res.result.map((item) => ({
          label: item.root_cause_sysnamea,
          value: item.root_cause_sysnamea,
          type: item.type,
        }))
        if (this.network_type === '전송') {
          this.nodeName = codeData.filter((item) => item.type === '전송')
        } else {
          this.nodeName = codeData.filter((item) => item.type === 'IP')
        }
      } catch (error) {
        console.error(error)
      }
    },
    /* 노드명 조회 */
    async onLoadNodeRecovery() {
      const param = {
        profile_num: this.rowInfo.profile_num,
      }
      try {
        const res = await apiProfileRecoveryList(param)
        const recoveryData = res.result.map((item) => ({
          name: item.node_id,
          profile_num: item.profile_num,
          serial_num: item.serial_num,
        }))
        this.tableData = recoveryData
      } catch (error) {
        console.error(error)
      }
    },
    /* 노드 등록 */
    handleInsertNode() {
      const selectNode = this.selectNode
      if (selectNode === '') {
        this.$message('노드명을 선택하세요')
        return false
      }
      this.$confirm(`${this.selectNode} 노드를 등록 하시겠습니까?`, '노드 등록', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'info',
      }).then(async () => {
        const isNameExists = this.tableData.some((item) => item.name === selectNode)
        if (!isNameExists) {
          const newNode = { name: selectNode }
          this.tableData.push(newNode)
          try {
            const param = {
              node_id: selectNode,
              profile_num: this.rowInfo.profile_num,
            }
            const res = await apiInsertProfileRecovery(param)
            if (res.success) {
              this.$message('등록 되었습니다.')
              this.onLoadNodeRecovery()
              this.selectNode = ''
            }
          } catch (error) {
            this.$message.error({ message: `등록에 실패했습니다.` })
            console.error(error)
          }
        } else {
          return false
        }
      })
    },
    /* 노드 삭제 */
    handleDeleteNode(index, row) {
      this.$confirm(`${row.name} 노드를 삭제 하시겠습니까?`, '노드 삭제', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warning',
      }).then(async () => {
        this.tableData = this.tableData.filter((item, idx) => {
          return item.name !== row.name
        })
        try {
          const param = {
            node_id: row.name,
            profile_num: row.profile_num,
          }
          const res = await apiDeleteProfileRecovery(param)
          if (res.success) {
            this.$message('삭제 되었습니다.')
            this.onLoadNodeRecovery()
          }
        } catch (error) {
          this.$message.error({ message: `등록에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    /* 프로파일 등록 */
    handleInsertProfile() {
      if (!this.auto_process_check && this.autoProcTime.length === 0) {
        this.$message('자동 처리 기간을 설정하세요')
        return false
      }
      this.confirm('등록하시겠습니까?', '조치프로파일 등록', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        try {
          const param = {
            profile_title: this.profile_title,
            profile_desc: this.profile_desc,
            network_type: this.network_type,
            processing_template: this.processing_template,
            auto_process_check: this.auto_process_check,
            auto_process_start_datetime: this.autoProcTime[0] ?? null,
            auto_process_end_datetime: this.autoProcTime[1] ?? null,
            ticket_type: this.ticket_type,
            process_type: this.process_type,
            auto_recovery: this.auto_recovery,
            email_check: this.email_check,
          }
          const insertRes = await apiInsertProfileList(param)
          if (insertRes.success) {
            this.$message('등록 되었습니다.')
            this.$emit('systemEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `등록에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    /* 프로파일 삭제 */
    handleDeleteProfile() {
      this.confirm('삭제하시겠습니까?', '조치프로파일 삭제', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        const param = { profile_num: this.rowInfo.profile_num }
        try {
          const res = await apiDeleteProfileList(param)

          if (res.success) {
            this.$message('삭제 되었습니다.')
            this.$emit('systemEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `삭제에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    /* 프로파일 수정 */
    handleUpdateProfile() {
      this.confirm('저장하시겠습니까?', '조치프로파일 수정', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        try {
          const param = {
            profile_num: this.rowInfo.profile_num,
            profile_title: this.profile_title,
            profile_desc: this.profile_desc,
            network_type: this.network_type,
            processing_template: this.processing_template,
            auto_process_check: this.auto_process_check,
            auto_process_start_datetime: this.autoProcTime[0] ?? null,
            auto_process_end_datetime: this.autoProcTime[1] ?? null,
            ticket_type: this.ticket_type,
            process_type: this.process_type,
            auto_recovery: this.auto_recovery,
            email_check: this.email_check,
          }
          const res = await apiUpdateProfileList(param)
          if (res.success) {
            this.$message('수정 되었습니다.')
            this.$emit('systemEdit')
            this.close()
          }
        } catch (error) {
          this.$message.error({ message: `수정에 실패했습니다.` })
          console.error(error)
        }
      })
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
.ModalProfileDetail {
  .line-class {
    font-weight: bold;
    font-size: 15px;
    text-align: center !important;
  }

  .el-dialog {
    border: 2px solid $nia-primary;
    box-shadow: 0 1px 5px 0 rgb(0 0 0 / 27%);
    border-radius: 7px;
    height: auto;
    min-width: 600px !important;
  }

  ::v-deep {
    .node-table th {
      background: #fff;
      border: none;
    }
    td {
      background: #fff;
      border-bottom: 0px;
    }

  }

}
</style>
