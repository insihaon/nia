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
        노드 상세보기
        <hr>
      </span>
      <table class="basic">
        <tr>
          <th>노드ID</th>
          <td class="disable">{{ rowInfo.node_id }}</td>
          <th>노드명</th>
          <td class="disable">{{ rowInfo.node_nm }}</td>
        </tr>
        <tr>
          <th>모델명</th>
          <td class="disable">{{ rowInfo.model_id }}</td>
          <th>대표IP</th>
          <td class="disable">{{ rowInfo.ip_addr }}</td>
        </tr>
        <tr>
          <th>MAC주소</th>
          <td class="disable">{{ rowInfo.mac_addr }}</td>
          <th>사용여부</th>
          <td colspan="3" class="disable">{{ rowInfo.admin_yn_info }}</td>
        </tr>
        <tr>
          <th colspan="4" class="line-class">NetConf</th>
        </tr>
        <tr>
          <th>PORT</th>
          <td class="disable">{{ rowInfo.phone_number }}</td>
          <th>ID</th>
          <td class="disable">{{ rowInfo.email }}</td>
        </tr>
        <tr>
          <th colspan="1">PW</th>
          <td colspan="3" class="disable">{{ rowInfo.phone_number }}</td>
        </tr>
        <tr>
          <th colspan="4" class="line-class">좌표</th>
        </tr>
        <tr>
          <th>위도</th>
          <td class="disable">{{ rowInfo.phone_number }}</td>
          <th>경도</th>
          <td class="disable">{{ rowInfo.email }}</td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" icon="el-icon-close" @click.native="close()">
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

const routeName = 'ModalNodeDetail'

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
      this.domElement.maxWidth = 550
    },
    onOpen(model, actionMode) {
      this.viewType = model.type
      this.rowInfo = this._cloneDeep(model.row)
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
    text-align: center;
  }
}
</style>
