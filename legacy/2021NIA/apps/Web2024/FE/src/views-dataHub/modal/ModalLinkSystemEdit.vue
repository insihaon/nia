
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
          {{ title }}
          <hr>
        </span>
        <table class="basic">
          <th>연동시스템명</th>
          <td :class="isEdit">
            <el-input v-model="rowInfo.system_nm " />
          </td>
          <tr>
            <th>등록일시</th>
            <td class="edit">{{ rowInfo.create_time }}</td>
          </tr>
          <tr>
            <th>수정일시</th>
            <td class="edit">{{ rowInfo.update_time }}</td>
          </tr>
          <tr>
            <th>연동시스템 설명</th>
            <td class="edit">
              <el-input v-model="rowInfo.system_desc" />
            </td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" plain size="medium" @click.native="isMode()">
            {{ btnText }}
          </el-button>
          <el-button type="info" plain size="medium" @click.native="close()">
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
import { apiUpdateLinkSystem, apiInsertLinkSystemListProc } from '@/api/dataHub'
import moment from 'moment'

const routeName = 'ModalLinkSystemEdit'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { apiUpdateLinkSystem },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      rowInfo: {},
    }
  },
  computed: {
    title() {
      return this.model.type === 'edit' ? '연동시스템 수정' : '연동시스템 등록'
    },
    isEdit() {
      const isDisable = this.rowInfo === '' ? 'disable-area' : ''
      return isDisable
    },
    btnText() {
      const btnName = this.model.type === 'edit' ? '수정' : '등록'
      return btnName
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    isMode() {
      this.btnText === '수정' ? this.editInfo() : this.applyInfo()
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.viewType = model?.type
      if (this.viewType === 'edit') {
        this.rowInfo = this._cloneDeep(model?.row)
      } else {
          this.rowInfo = {
            manager_phone: '',
            manager_email: '',
            manager_id: '',
            system_nm: '',
            system_desc: '',
            create_time: moment(new Date().getTime()).format('YYYY-MM-DD HH:mm:00'),
          }
        }
    },
    onClose() { /* for Override */ },
    applyInfo() {
      const confirmMessage = '등록하시겠습니까?'

      this.confirm(confirmMessage, '연동시스템 등록', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success'
      }).then(async () => {
        const param = {
          system_nm: this.rowInfo?.system_nm,
          manager_id: this.rowInfo?.manager_id,
          manager_phone: this.rowInfo?.manager_phone,
          manager_email: this.rowInfo?.manager_email,
          system_desc: this.rowInfo?.system_desc,
        }
        try {
            const res = await apiInsertLinkSystemListProc(param)
            if (res.success) {
              this.$emit('systemEdit')
              this.$message('등록 되었습니다.')
              this.close()
            }
        } catch (error) {
          this.$message.error('등록에 실패했습니다.')
          console.error(error)
        }
      })
    },
    editInfo() {
      const confirmMessage = '수정하시겠습니까?'

      this.confirm(confirmMessage, '연동시스템 수정', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success'
      }).then(async () => {
        const param = {
          system_id: this.rowInfo?.system_id,
          system_nm: this.rowInfo?.system_nm,
          system_desc: this.rowInfo?.system_desc,
        }
        try {
            const res = await apiUpdateLinkSystem(param)
            if (res.success) {
              this.$emit('systemEdit')
              this.$message('수정 되었습니다.')
              this.close()
            }
        } catch (error) {
          this.$message.error('수정에 실패했습니다.')
          console.error(error)
        }
      })
    }

    }

  }
</script>

