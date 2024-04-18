<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :fullscreen.sync="fullscreen"
        :modal-append-to-body="true"
        :append-to-body="true"
        :modal="modal"
        :close-on-click-modal="closeOnClickModal"
        class="nia-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2 text-base" />
          데이터 스냅샷
          <hr />
        </span>
        <div class="w-full h-full d-flex">
          <component :is="componentLoader" :selected-ticket="selectedRow" :is-modal="true" :is-show-hist="isShowHist" class="h-full" />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain class="btn-r" @click.native="isShowHist = !isShowHist"> 이력 {{ isShowHist ? '닫기' : '보기' }} </el-button>
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
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { formatterTime } from '@/views-nia/js/commonFormat'
import snapshotHistory from '@/views-nia/dataSnapshot/snapshotHistory.vue'

import _ from 'lodash'

const routeName = 'ModalSnapshot'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, snapshotHistory },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      containerLoading: false,
      selectedRow: null,
      isShowHist: false,
    }
  },
  computed: {
    componentLoader() {
      return snapshotHistory
    },
  },
  watch: {
    isShowHist(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.domElement.maxWidth = newVal ? 1200 : 600
      }
    },
  },
  mounted() {},
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxWidth = 600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.$nextTick(() => {
        this.selectedRow = model?.row
      })
    },
    onClose() {},
  },
}
</script>

<style lang="scss" scoped></style>
