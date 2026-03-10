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
          <th>API</th>
          <td class="disable">{{ rowInfo.api_name }}</td>
          <tr>
            <th>방식 </th>
            <td class="disable">{{ rowInfo.exec_mode_cd }}</td>
          </tr>
          <tr>
            <th>접근 URL</th>
            <td class="disable">{{ rowInfo.api_url }}</td>
          </tr>
          <tr>
            <th>발생일시</th>
            <td class="disable">{{ rowInfo.create_time }}</td>
          </tr>
          <tr>
            <th>연동 시스템</th>
            <td colspan="3" class="disable">{{ rowInfo.system_nm }}</td>
          </tr>
        </table>
        <div slot="footer" class="dialog-footer">
          <el-button size="medium" @click.native="close()">
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

const routeName = 'ModalApiDetail'

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
      this.title = this.viewType === 'alarm' ? 'API 오류 상세정보' : 'API 상세정보'
      this.rowInfo = this._cloneDeep(model.row)
    },
    onClose() { /* for Override */ },
    onSubmit() {
        console.log('submit!')
      }
    }

  }
</script>

