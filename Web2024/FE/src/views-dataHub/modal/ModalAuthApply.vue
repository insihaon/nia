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
        class="datahub-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2" style="font-size: 17px;" />
          API 서비스 사용 권한 신청
          <hr>
        </span>
        <table class="basic">
          <th>API</th>
          <td class="disable">{{ rowInfo.api_name }}</td>
          <tr>
            <th>구분</th>
            <td class="disable">
              {{ rowInfo.exec_mode_cd }}
            </td>
          </tr>
          <tr>
            <th>접근 URL</th>
            <td :class="isDisable"> <el-input v-model="rowInfo.api_url" /> </td>
          </tr>
          <tr>
            <th>신청일시</th>
            <td class="disable"> {{ applicationDate }} </td>
          </tr>
          <tr>
            <th>만료일</th>
            <td>
              <el-date-picker
                v-model="expirationDate"
                type="date"
              />
            </td>
          </tr>
          <tr>
            <th>연동 시스템</th>
            <td class="disable">
              <el-select v-model="selectedLinkSystem" placeholder="연동시스템을 선택하세요">
                <el-option
                  v-for="(system, i) in linkSystem"
                  :key="i"
                  :label="system.label"
                  :value="system.value"
                />
              </el-select>
              <span class="link-description">{{ '※ 신규시스템은 연동시스템 등록 후에 신청가능합니다' }}</span>
            </td>
          </tr>
          <tr>
            <th>사용 용도</th>
            <td colspan="3">
              <el-input v-model="rowInfo.usage" type="textarea" />
            </td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain class="dataHub-button" @click.native="insertApiAuth()">
            {{ $t('apply') }}
          </el-button>
          <el-button size="small" plain class="dataHub-button" @click.native="onClickCencel()">
            {{ $t('cancel') }}
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

  <script>
  import elDragDialog from '@/directive/el-drag-dialog'
  import { Modal } from '@/min/Modal.min'
  import moment from 'moment'
  import { apiInsertApiAuthProc, apiSelectLinkSystemTableList, apiSelectAuthHistList } from '@/api/dataHub'
  import { formatterTime } from '@/views-dataHub/commonFormat'
  import EventBus from '@/utils/event-bus'
  import { mapState } from 'vuex'
  const routeName = 'ModalAuthApply'

  export default {
    name: routeName,
    directives: { elDragDialog },
    extends: Modal,

    data() {
      return {
        name: routeName,
        src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        rowInfo: {},
        linkSystem: [],
        selectedLinkSystem: '',
        currentTime: '',
        applyUser: '',
        expirationDate: '',
        applicationDate: this.toStringTime(new Date(), 'YYYY-MM-DD'),
      }
    },
    computed: {
      ...mapState({
      userInfo: (state) => state.user.info,
    }),

      isDisable() {
          const isUrl = this.rowInfo.exec_mode_cd !== 'Batch' ? 'disable-area' : ''
          return isUrl
      }
    },
    mounted() {
    },
    methods: {
    async selectTableList() {
      const res = await apiSelectLinkSystemTableList()
      this.linkSystem = res.result.map(item => ({ label: item.system_nm, value: item.system_id }))
    },
      onCreated() {
        Modal.methods.onCreated.call(this)
        this.closeOnClickModal = false
      },
      onOpen(model, actionMode) {
        this.selectTableList()
        this.rowInfo = ''
        this.selectedLinkSystem = ''
        this.currentTime = moment().format('YYYY-MM-DD HH:mm:ss')
        this.applyUser = this.$store.state.user.name
        this.rowInfo = this._cloneDeep(model.row)
      },
      onClickCencel() {
        this.$confirm('변경된 데이터가 저장되지 않았습니다.<br />계속하시겠습니까?', '취소', {
          confirmButtonText: '예',
          cancelButtonText: '아니오',
          type: 'warning',
          dangerouslyUseHTMLString: true
        }).then(() => {
          this.close()
        })
      },
      onClose() { /* for Override */ },
      async insertApiAuth() {
         if (!this.linkSystem || !this.rowInfo.usage) {
            this.$message({
              message: '연동 시스템과 사용 용도를 입력해주세요.',
              type: 'error',
            })
            return false
          }
          if (this.expirationDate === '') {
            const currentDate = new Date()
            currentDate.setFullYear(currentDate.getFullYear() + 1)
            this.expirationDate = currentDate.toISOString().slice(0, 10)
          } else {
            const expirationDate = new Date(this.expirationDate)
            const year = String(expirationDate.getFullYear())
            const month = String(expirationDate.getMonth() + 1).padStart(2, '0')
            const day = String(expirationDate.getDate()).padStart(2, '0')
            this.expirationDate = `${year}-${month}-${day}`
          }
          const param = {
            api_id: this.rowInfo.api_id,
            system_id: this.selectedLinkSystem,
            status_cd: ['APPLY'],
          }
          try {
            const overlappinMessage = `중복된 신청내역이 있습니다.\n 계속 진행하시겠습니까?\n(만료일 : ${this.expirationDate})`
            const confirm = `권한신청 하시겠습니까?\n(만료일 : ${this.expirationDate})`

            const res = await apiSelectAuthHistList(param)
            const messageVal = res?.result.length > 0 ? overlappinMessage : confirm

          this.$confirm(messageVal, 'API 권한신청', {
            confirmButtonText: ' 예',
            cancelButtonText: '아니오',
            type: 'success',
          })
          .then(async () => {
            const param = {
              api_id: this.rowInfo.api_id,
              status_cd: 'APPLY',
              api_desc: this.rowInfo.usage,
              api_url: this.rowInfo.api_url,
              reg_user: this.userInfo.Uid,
              expird_date: formatterTime(this.expirationDate),
              system_id: this.selectedLinkSystem
            }
            try {
              const res = await apiInsertApiAuthProc(param)
              if (res.result > 0) {
                this.$message({
                  message: '권한 신청이 완료 되었습니다.',
                  type: 'success'
                })
                EventBus.$emit('refreshAuthApplyData')
                this.close()
              } else {
                this.$message({
                  message: '중복된 신청 이력이 있습니다.',
                  type: 'error',
                  showClose: true
                })
              }
              this.expirationDate = ''
            } catch (error) {
              console.error(error)
              this.$message('오류가 발생했습니다.')
            }
          })
          .catch(() => {
            this.$message('권한 신청이 취소되었습니다.')
          })
          } catch (error) {
              console.error(error)
        }
      },
      }

  }
</script>

<style lang="scss" scoped>
  .ModalAuthApply{
    .disable-area{
      color: #999;
      pointer-events: none;
    }

    .link-description{
      margin-left: 8px;
      color: gray;
      font-size: 13px;
      font-style: italic;
    }
  }

</style>

