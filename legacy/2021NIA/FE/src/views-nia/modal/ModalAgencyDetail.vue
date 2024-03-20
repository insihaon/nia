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
        <td class="disable">{{ agencyData.nren_id }}</td>
        <th>기관명</th>
        <td class="disable">{{ rowInfo.nren_name }}</td>
        <tr>
          <th>노드명</th>
          <td class="disable">{{ rowInfo.nodeId }}</td>
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
            <el-input v-model="rowInfo.email">
              {{ rowInfo.email }}
            </el-input>
          </td>
        </tr>
        <tr>
          <th colspan="1">IP등록</th>
          <td colspan="3" class="disable">
            <el-input v-model="rowInfo.link_desc" style="width:80%; min-width:70%;float:left">
              {{ rowInfo.link_desc }}
            </el-input>
            <el-button
              size="mideum"
              style="float:right"
              plain
            >추가
            </el-button>
          </td>
        </tr>
        <tr>
          <td colspan="4" class="disable">
            {{ rowInfo.nren_ip }}
            <el-button type="danger" icon="el-icon-delete" circle />
          </td>
        </tr>
      </table>

      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click.native="close()">
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
import { apiSelectAgencyIfIdList } from '@/api/nia'

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
      agencyData: []
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
.ModalNodeDetail {
  .line-class {
    font-weight: bold;
    font-size: 15px;
  }
}
</style>
