<template>
  <div>
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
        {{ '이용기관 상세보기' }}
        <hr>
      </span>
      <table class="basic">
        <th>기관ID</th>
        <td class="disable">{{ rowInfo.nren_id }}</td>
        <th>기관명</th>
        <td class="disable">{{ rowInfo.nren_name }}</td>
        <tr>
          <th>노드명</th>
          <td class="disable">{{ rowInfo.node_id }}</td>
          <th>IF명</th>
          <td class="disable">{{ rowInfo.if_id }}</td>
        </tr>
        <tr>
          <th>고객ID</th>
          <td class="disable">
            <el-input v-model="rowInfo.customer_id">
              {{ rowInfo.customer_id }}
            </el-input>
          </td>
          <th>대역폭(Gbps)</th>
          <td class="disable">{{ rowInfo.bandwidth }}</td>
        </tr>
        <tr>
          <th colspan="1">담당Email</th>
          <td colspan="3" class="disable">
            <el-input
              v-model="rowInfo.email"
              clearable
            />
          </td>
        </tr>
        <tr>
          <th colspan="1">IP등록</th>
          <td colspan="3" class="disable">
            <el-input
              v-model="ip_num"
              clearable
              style="width:80%; min-width:70%;float:left"
            />
            <el-button
              size="mideum"
              style="float:right"
              plain
              @click="insertAgencyIpData()"
            >추가
            </el-button>
          </td>
        </tr>
        <tr>
          <th colspan="1">IP</th>
          <td colspan="3" class="disable">
            <el-upload
              class="ip-list"
              action="#"
              :on-remove="handleRemove"
              :before-remove="beforeRemove"
              multiple
              :limit="3"
              :file-list="ipList"
            />
          </td>
        </tr>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click.native="editAgencyData()">
          {{ '저장' }}
        </el-button>
        <el-button class="exit-btn" size="medium" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { mapState } from 'vuex'
import { apiSelectAgencyIfIdList, apiSelectAgencyIpList, apiInsertAgencyIpList, apiDeleteAgencyIpList, apiUpdateAgencyList } from '@/api/nia'

const routeName = 'ModalAgencyDetail'

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
      vlanOptions: [
        {
          value: 'True',
          label: 'True',
        },
        {
          value: 'False',
          label: 'False',
        },
      ],
      tagOptions: [
        {
          value: 'True',
          label: 'True',
        },
        {
          value: 'False',
          label: 'False',
        },
      ],
      agencyData: [],
      agencyIpData: [],
      ip_num: ''

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
    ipList() {
    return this.agencyIpData.map(item => ({ name: item.nren_ip }))
  }

  },
  watch: {},
  mounted() {},
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.rowInfo = this._cloneDeep(model.row)
      this.onLoadTrafficList()
      this.onLoadAgencyIpList()
    },
      async onLoadTrafficList() {
      const target = { vue: this.$refs.trafficAnalysis }
      this.openLoading(target)
      const param = {
        node_name: this.rowInfo.node_id
      }
      try {
        const res = await apiSelectAgencyIfIdList(param)
        this.agencyData = res?.result
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
      async onLoadAgencyIpList() {
      const target = { vue: this.$refs.agency }
      this.openLoading(target)
      const param = {
        nren_id: this.rowInfo.nren_id,
        nren_name: this.rowInfo.nren_name,
        node_id: this.rowInfo.node_id,
      }
      try {
        const res = await apiSelectAgencyIpList(param)
        this.agencyIpData = res?.result
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
      async insertAgencyIpData() { // IP 등록
        if (this.ip_num === '') {
          this.message('ip를 입력해주세요')
          return false
        }
          const param = {
            nren_id: this.rowInfo.nren_id,
            nren_name: this.rowInfo.nren_name,
            nren_ip: this.ip_num
          }
          try {
              const res = await apiInsertAgencyIpList(param)
              if (res.success) {
                this.$message('등록 되었습니다.')
                this.ip_num = ''
                this.onLoadAgencyIpList()
              }
          } catch (error) {
            this.$message.error({ message: `등록에 실패했습니다.` })
            console.error(error)
          }
      },
    beforeRemove(file, fileList) { // IP 삭제 알림 메시지
      return this.$confirm(`${file.name} IP를 삭제하시겠습니까 ?`, 'IP 삭제 확인', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
    },
    async handleRemove(file, fileList) { // IP리스트 삭제
      const isfileName = file.name
      if (isfileName !== null) {
        const param = {
            nren_id: this.rowInfo.nren_id,
            nren_name: this.rowInfo.nren_name,
            nren_ip: file.name
        }
        try {
       const res = await apiDeleteAgencyIpList(param)
        if (res.success) {
          this.$message.success({ message: `삭제되었습니다.` })
        }
      } catch (error) {
          this.$message.error('삭제에 실패했습니다.')
          console.log(error)
        }
      } else {
        return false
      }
    },
      editAgencyData() {
        this.confirm('수정하시겠습니까?', '수정', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'success'
        }).then(async () => {
          const param = {
            nren_id: this.rowInfo.nren_id,
            customer_id: this.rowInfo.customer_id,
            email: this.rowInfo.email,
          }
          try {
              const res = await apiUpdateAgencyList(param)
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
    onClose() {
      /* for Override */
    },
    onSubmit() {
      console.log('submit!')
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
.ModalAgencyDetail {

}
</style>
