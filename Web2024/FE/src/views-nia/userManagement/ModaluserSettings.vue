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
      class="datahub-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-user mr-2" style="font-size: 17px;" />
        {{ title }}
        <hr>
      </span>
      <table class="basic">
        <th>아이디</th>
        <td class="disable">{{ rowInfo.user_id }}</td>
        <tr>
          <th>이름</th>
          <td class="disable">{{ loginUsername }}</td>
        </tr>
        <tr>
          <th>연락처</th>
          <td class="disable">{{ rowInfo.phone_number }}</td>
        </tr>
        <tr>
          <th>e-mail</th>
          <td class="disable">{{ rowInfo.email }}</td>
        </tr>
        <tr>
          <th>분류</th>
          <td colspan="3" class="disable">{{ rowInfo.classification }}</td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button class="common-btn" size="medium" @click.native="close()">
          {{ '정보 수정' }}
        </el-button>
        <el-button size="medium" @click.native="close()">
          {{ '계정 삭제' }}
        </el-button>
        <el-button size="medium" @click.native="close()">
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

const routeName = 'ModaluserSettings'

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
      rowInfo: {
        user_id: 'user123',
        phone_number: '010-1234-5678',
        email: 'uesr123@codejone.com',
        classification: 'NOC',

      },
    }
  },
  computed: {
    ...mapState({
      viewport: state => state.app.viewport,
      username: state => state.user.name,
    }),
    loginUsername() {
      const userNM = this.username ? this.username.replace(/.$/, '*') : 'UNKONWN'
      return userNM
    },
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
      this.title = '사용자 설정'
      // this.rowInfo = this._cloneDeep(model.row)
    },
    onClose() { /* for Override */ },
    onSubmit() {
        console.log('submit!')
      }
    }

  }
</script>

