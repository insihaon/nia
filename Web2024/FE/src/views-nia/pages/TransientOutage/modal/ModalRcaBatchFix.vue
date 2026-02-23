<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.minWidth + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        :loading="loading"
        class="untact-modal h-100"
        :class="{ [name]: true}"
      >
        <span slot="title" style="transform: skew(0.03deg);">
          <i class="el-icon-s-tools" /> 조치사항 입력
          <hr>
        </span>
        <div>
          <span style="font-weight: 700;font-size: 17px">고장 조치확인</span>
          <el-input
            v-model="fixProcess"
            placeholder="케이블 교체 후 서비스 정상 확인"
            maxlength="120"
            show-word-limit
          />
        </div>
        <span slot="footer" class="dialog-footer" style="text-align: right">
          <hr>
          <el-button type="primary" plain @click="handleClickFix()">확인</el-button>
          <el-button class="gray-btn" @click="close()">닫기</el-button>
        </span>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import { Modal } from '@/min/Modal.min'
import elDragDialog from '@/directive/el-drag-dialog'
// import moment from 'moment'

const routeName = 'ModalRcaBatchFIx'

const _component = {
  name: routeName,
  components: { },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      fixProcess: '',
      fixValue: ''
    }
  },
  computed: {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.minWidth = 400
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
    },
    handleClickFix() {
      this.fixValue = this.fixProcess
      this.close()
    },
    onClose() {
      this.$emit('closeModal', this.fixValue)
      this.fixProcess = ''
      this.fixValue = ''
    }
  }
}
export default _component
</script>

<style lang="scss">

</style>
