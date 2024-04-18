<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.maxHeight + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="false"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          SOP 이력 조회
          <hr />
        </span>
        <div class="d-flex flex-column h-100">
          <component :is="componentLoader" :row="selectedRow" class="h-full" />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain class="close-btn" @click.native="close()">
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
import _ from 'lodash'
import sopHistory from '@/views-nia/alarmMonitoring/sopHistory.vue'

const routeName = 'ModalSopList'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { sopHistory },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      selectedRow: null,
    }
  },
  computed: {
    componentLoader() {
      return sopHistory
    },
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 1500
      this.domElement.maxHeight = 1600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row
    },
    onClose() {
      /* for Override */
    },
  },
}
</script>

<style lang="scss" scoped>
::v-deep .CompInquiryPannel {
  height: 500px !important;
  div.subContentWrap div.optionBox > div.optionBoxContent > div.optionItem > label {
    min-width: 50px;
  }
}
</style>
