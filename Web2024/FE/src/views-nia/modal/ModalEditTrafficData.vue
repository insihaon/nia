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
          <td v-if="viewType === 'editAgency'" class="disable">{{ rowInfo.nren_name }}</td>
          <td v-if="viewType === 'editApp'" class="disable">{{ rowInfo.protocol }}</td>
          <tr>
            <th>{{ titleNameB }}</th>
            <td v-if="viewType === 'editAgency'">{{ rowInfo.nren_ip }}</td>
            <td v-if="viewType === 'editApp'">{{ rowInfo.port_num }}</td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button size="medium" @click.native="close()">
            {{ '수정' }}
          </el-button>
          <el-button size="medium" @click.native="close()">
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
    },
    onClose() { /* for Override */ },
    onSubmit() {
        console.log('submit!')
      }
    }
  }
</script>

