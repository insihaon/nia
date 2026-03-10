<template>
  <div>
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="`80%`"
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
          API 데이터 조회
          <hr>
        </span>
        <div class="dialog-body">
          <SelectDataSet ref="selectDataSet" style="width: 1000px; height: 800px; justify-content: center;" />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button size="small" plain type="danger" class="dataHub-button" @click="onClose">
            닫기
          </el-button>
        </div>
      </el-dialog>
    </transition>
  </div>
</template>

  <script>
  import elDragDialog from '@/directive/el-drag-dialog'
  import { Modal } from '@/min/Modal.min'
  import moment from 'moment'
  import { formatterTime } from '@/views-dataHub/commonFormat'
  import EventBus from '@/utils/event-bus'
  import SelectDataSet from '@/views-dataHub/metaData/selectDataSet.vue'

  const routeName = 'ModalSelectDataSet'

  export default {
    name: routeName,
    directives: { elDragDialog },
    components: {
      // eslint-disable-next-line vue/no-unused-components
      SelectDataSet
    },
    extends: Modal,
    data() {
      return {
        name: routeName,
        src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      }
    },
    computed: {
    },
    mounted() {
    },
    methods: {
      onCreated() {
      Modal.methods.onCreated.call(this)

      this.domElement.minWidth = 1500

      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.previewNotice = model
    },
    onClose() {
      this.close()
    },
      }
    }
</script>

<style lang="scss">
.ModalSelectDataSet {
  margin-top: -10vh;

  .el-dialog {
    .el-dialog__body {
      max-height: 600px;
      padding: 10px 20px;
  .dialog-body{
    height: 500px;
    display: flex;
    justify-content: center;
  }
    }

    .dialog-footer{
      margin-top: 10px
    }
  }

}

</style>

