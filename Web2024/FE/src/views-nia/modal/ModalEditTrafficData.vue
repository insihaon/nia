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
          <i class="el-icon-user mr-2" style="font-size: 17px;" />
          {{ titleMode }}
          <hr>
        </span>
        <table class="basic">
          <th>{{ titleNameA }}</th>
          <td v-if="viewType === 'editAgency'" class="disable">
            <el-select v-model="agency_value">
              <el-option
                v-for="item in agencyList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </td>
          <td v-if="viewType === 'editApp'" class="disable">
            <el-input v-model="protocol" />
          </td>
          <tr>
            <th>{{ titleNameB }}</th>
            <td v-if="viewType === 'editAgency'">
              <el-input v-model="update_nren_ip" /></td>
            <td v-if="viewType === 'editApp'">
              <el-input v-model="port_num" /></td>
          </tr>
          <tr v-if="viewType === 'editApp'">
            <th>{{ '설명' }}</th>
            <td>
              <el-input
                v-model="description"
                type="textarea"
              />
            </td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button size="medium" @click.native="changeEditMode()">
            {{ '수정' }}
          </el-button>
          <el-button size="medium" @click.native="changeDeleteMode()">
            {{ '삭제' }}
          </el-button>
          <el-button class="exit-btn" size="medium" @click.native="close()">
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
import { apiSelectAgencyCodeList, apiUpdateAppIpList, apiUpdateAgencyIpList, apiDeleteAgencyIpList, apiDeleteAppIpList } from '@/api/nia'

const routeName = 'ModalEditTrafficData'

export default {
  name: routeName,
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      rowInfo: {},
      agencyList: [],
      agency_value: '',
      update_nren_ip: '',
      protocol: '',
      port_num: '',
      description: ''
    }
  },
  computed: {
    ...mapState({
      viewport: state => state.app.viewport,
      username: state => state.user.name,
    }),
    titleMode() {
      if (this.viewType === 'editAgency') {
        return '이용기관 수정'
      } else {
        return '어플리케이션 수정'
      }
    },
    titleNameA() {
      if (this.viewType === 'editAgency') {
        return '이용기관'
      } else {
        return '프로토콜'
      }
    },
    titleNameB() {
      if (this.viewType === 'editAgency') {
        return 'IP 주소'
      } else {
        return '포트'
      }
    }
  },
  watch: {
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
      this.update_nren_ip = this.rowInfo.nren_ip
      this.protocol = this.rowInfo.protocol
      this.port_num = this.rowInfo.port_num
      this.onloadAgencyCodeList()
    },
    async onloadAgencyCodeList() {
      try {
        const res = await apiSelectAgencyCodeList()
        this.selectCodeData = res.result.map(item => ({ label: item.name, value: item.id }))
        this.agencyList = this.selectCodeData
        const matchingItem = this.agencyList.find(item => item.label === this.rowInfo.nren_name)
        this.agency_value = matchingItem.value
      } catch (error) {
          console.error(error)
        } finally {
          // this.closeLoading(target)
        }
    },
     updateAgencyIpData() {
        this.confirm('수정하시겠습니까?', '이용기관 IP 수정', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
         const selectedItem = this.agencyList?.find(item => item.value === this.agency_value)
          const param = {
            nren_id: this.agency_value,
            nren_name: selectedItem?.label,
            nren_ip: this.rowInfo.nren_ip,
            update_nren_ip: this.update_nren_ip
          }
          try {
              const res = await apiUpdateAgencyIpList(param)
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
    updateAppIpData() {
        this.confirm('수정하시겠습니까?', '어플리케이션 수정', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
          const param = {
            mapp_serial: this.rowInfo.mapp_serial,
            // protocol: this.protocol,
            description: this.description
          }
          try {
              const res = await apiUpdateAppIpList(param)
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
      deleteAgencyIpData() {
        this.confirm('삭제하시겠습니까?', '이용기관 삭제', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
        const selectedItem = this.agencyList?.find(item => item.value === this.agency_value)
          const param = {
            nren_id: this.agency_value,
            nren_name: selectedItem?.label,
            nren_ip: this.rowInfo.nren_ip,
          }
          try {
              const res = await apiDeleteAgencyIpList(param)
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
      deleteAppIpData() {
        this.confirm('삭제하시겠습니까?', '어플리케이션 삭제', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
          const param = {
            mapp_serial: this.rowInfo.mapp_serial,
          }
          try {
              const res = await apiDeleteAppIpList(param)
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
    changeEditMode() {
      if (this.viewType === 'editAgency') {
        this.updateAgencyIpData()
      } else {
        this.updateAppIpData()
      }
    },
    changeDeleteMode() {
      if (this.viewType === 'editAgency') {
        this.deleteAgencyIpData()
      } else {
        this.deleteAppIpData()
      }
    },
    onClose() { /* for Override */ },
    }
  }
</script>

