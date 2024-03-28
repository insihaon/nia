<template>
  <div>
    <!-- <transition :name="animation"> -->
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
        <hr>
      </span>
      <table class="basic">
        <th class="disable">프로파일제목</th>
        <td colspan="3">
          <el-input v-model="rowInfo.profile_title" />
        </td>
        <tr>
          <th>프로파일설명</th>
          <td colspan="3" class="disable">
            <el-input v-model="rowInfo.profile_desc" type="textarea" />
          </td>
        </tr>
        <tr>
          <th>네트워크구분</th>
          <td colspan="3">
            <el-radio-group v-model="rowInfo.network_type" size="mini" class="d-flex">
              <el-radio label="전송">KOREN(전송)</el-radio>
              <el-radio label="IP">KOREN(IP)</el-radio>
            </el-radio-group>
          </td>
        </tr>
        <tr>
          <th>장애대응구분</th>
          <td colspan="3">
            <el-radio-group v-model="rowInfo.processing_template" size="mini" class="d-flex">
              <el-radio label="recovery">자가회복</el-radio>
              <el-radio label="construction">공사</el-radio>
            </el-radio-group>
          </td>
        </tr>
        <tr>
          <th>자동처리기간</th>
          <td colspan="3" style="text-align : left">
            <el-date-picker
              v-model="autoProcTime"
              type="daterange"
              range-separator="~"
              start-placeholder="시작일"
              end-placeholder="종료일"
              :disabled="rowInfo.auto_process_check"
            />
            &nbsp;
            <el-checkbox v-model="rowInfo.auto_process_check" label="상시" />
          </td>
        </tr>
        <tr>
          <th>Ticket 유형</th>
          <td>
            <el-select
              v-model="rowInfo.ticket_type"
            >
              <el-option
                v-for="item in ticketType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <th>장애 유형</th>
          <td>
            <el-select
              v-model="rowInfo.process_type"
            >
              <el-option
                v-for="item in alarmType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
        </tr>
        <th colspan="4" class="line-class">자동처리 대상 등록</th>
        <tr>
          <th>노드명</th>
          <td colspan="3">
            <el-select
              v-model="selectNode"
              style="min-width:70%;"
            >
              <el-option
                v-for="item in nodeName"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-button
              size="mideum"
              style="float:right"
              plain
              round
              type="info"
              @click="insertAgencyIpData()"
            >추가
            </el-button>
          </td>
        </tr>
        <tr>
          <td colspan="4">
            <el-table
              :data="tableData"
              style="width: 100%"
            >
              <el-table-column
                prop="name"
                label="노드명"
              />
              <el-table-column
                prop="profile_num"
                label="프로필 번호"
              />
              <el-table-column
                width="100%"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    plain
                    round
                    type="danger"
                    @click="handleDelete(scope.$index, scope.row)"
                  ><i class="el-icon-delete" /></el-button>
                </template>
              </el-table-column>
            </el-table>
          </td>
        </tr>
        <tr>
          <th>자동회복 처리</th>
          <td>
            <el-select v-model="rowInfo.auto_recovery">
              <el-option
                v-for="item in procOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <th>메일 자동 발송</th>
          <td>
            <el-checkbox v-model="rowInfo.email_check" />
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" type="info" plain @click.native="updateProfileSetting()">
          {{ '저장' }}
        </el-button>
        <el-button size="small" type="danger" plain @click.native="close()">
          {{ '삭제' }}
        </el-button>
        <el-button class="exit-btn" size="small" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
    <!-- </transition> -->
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import { apiTicketTypeCode, apiAlarmTypeCode, apiProfileNodeCode, apiProfileRecoveryList, apiInsertProfileNodeName } from '@/api/nia'

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
      autoProcTime: [],
      ticketType: '',
      alarmType: [],
      tableData: [{
          name: 'Temp',
        }, {
          name: 'Temp',
        }],
        procOptions: [
           { value: 'ACK', label: '인지' },
           { value: 'FIN', label: '마감' },
           { value: 'AUTO_FIN', label: '자동마감' }
        ],
      ticketData: [],
      nodeName: [],
      selectNode: ''
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
  },
    watch: {
    'rowInfo.network_type': function(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.onLoadNodeCode()
        this.onLoadTicketCode()
      }
    }
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.rowInfo = this._cloneDeep(model.row)

      this.autoProcTime = [
        this.rowInfo.auto_process_start_datetime,
        this.rowInfo.auto_process_end_datetime
      ].filter(time => time !== null)

      this.onLoadTicketCode()
      this.onLoadAlarmCode()
      this.onLoadNodeCode()
      this.onLoadNodeRecovery()
    },
    async onLoadTicketCode() {
      const param = {
        ticket_gb: this.rowInfo.network_type,
      }
      try {
        const res = await apiTicketTypeCode(param)
         const codeData = res.result.map(item => ({
          label: item.ticket_type,
          value: item.ticket_type,
          ticket_gb: item.ticket_gb
        }))
          if (this.rowInfo.network_type === '전송') {
            this.ticketType = codeData.filter(item => item.type === 'POTN')
          } else {
            this.ticketType = codeData.filter(item => item.type === 'SWITCH')
          }
        this.ticketType = codeData
      } catch (error) {
        console.error(error)
      } finally {
        /*  */
      }
    },
      async onLoadAlarmCode() {
      const param = {
        ticket_type: this.rowInfo.ticket_type,
      }
      try {
        const res = await apiAlarmTypeCode(param)
        const codeData = res.result.map(item => ({
          label: item.fail_type,
          value: item.fail_type
        }))
        this.alarmType = codeData
      } catch (error) {
        console.error(error)
      } finally {
        /*  */
      }
    },
    async onLoadNodeCode() {
    try {
      const res = await apiProfileNodeCode()
      const codeData = res.result.map(item => ({
        label: item.root_cause_sysnamea,
        value: item.root_cause_sysnamea,
        type: item.type
      }))
        if (this.rowInfo.network_type === '전송') {
          this.nodeName = codeData.filter(item => item.type === '전송')
        } else {
          this.nodeName = codeData.filter(item => item.type === 'IP')
        }
      } catch (error) {
        console.error(error)
      } finally {
        /*  */
      }
    },
    async onLoadNodeRecovery() {
    const param = {
      profile_num: this.rowInfo.profile_num,
    }
    try {
      const res = await apiProfileRecoveryList(param)
      const recoveryData = res.result.map(item => ({
        name: item.node_id,
        profile_num: item.profile_num
      }))
      this.tableData = recoveryData
      } catch (error) {
        console.error(error)
      } finally {
        /*  */
      }
    },
    insertAgencyIpData() {
      const selectNode = this.selectNode // 공백 제거
      if (selectNode === '') {
        this.$message('노드명을 선택하세요')
        return false
      }
      const isNameExists = this.tableData.some(item => item.name === selectNode)
      if (!isNameExists) {
        const newNode = { name: selectNode }
        this.tableData.push(newNode)
      } else {
        return false
      }
    },
    handleDelete(index, row) {
    this.$confirm(`${row.name} 노드를 삭제 하시겠습니까?`, '삭제 메세지', {
      confirmButtonText: '확인',
      cancelButtonText: '취소',
      type: 'warning'
    }).then(() => {
      this.tableData = this.tableData.filter((item, idx) => {
        return item.name !== row.name
      })
    }
    )
  },
    onClose() {
      /* for Override */
    },
    updateProfileSetting() {
      console.log('submit!')
    }
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

}
</style>
