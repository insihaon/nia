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
        class="nia-dialog"
        :class="{ [name]: true}"
      >
        <span slot="title">
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
        <div slot="footer" class="dialog-footer">
          <el-button size="mini" type="primary" @click="handleClickFix()">확인</el-button>
          <el-button size="mini" type="info" icon="el-icon-close" @click="close()">닫기</el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

<script>
import { Modal } from '@/min/Modal.min'
import elDragDialog from '@/directive/el-drag-dialog'
// import moment from 'moment'

const routeName = 'ModalRcaBatchFix'

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
      if (!this.fixProcess) {
        this.$message({ type: 'warning', message: '조치사항을 입력해 주세요.' })
        return
      }
      this.$emit('closeModal', this.fixProcess)
      this.close()
    },
    onClose() {
      this.fixProcess = ''
    }
  }
}
export default _component
</script>

<style lang="scss" scoped>
</style>
