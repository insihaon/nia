<template>
  <div :class="{ [name]: true }">
    <transition :name="animation">
      <el-dialog
        v-if="animationVisible"
        v-el-drag-dialog
        :visible.sync="visible"
        :width="domElement.maxWidth + `px`"
        :height="domElement.minHeight + `px`"
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
          AI 장애대응
          <hr>
        </span>
        <div class="d-flex flex-column h-full">
          <CompInquiryPannel
            ref="selectApi"
            :ag-grid="dataSetAgGrid"
            class="w-100 h-100 flex-fill"
            @handleClickSearch="onClickSearchAlarm"
          />
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
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'

const routeName = 'ModalAiResponse'

export default {
  name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { CompAgGrid, CompInquiryPannel },

  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
      visible: false,
    }
  },
  computed: {
    dataSetAgGrid() {
      const options = { name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
        const columns = [
          { type: '', prop: 'ticket_id', name: '티켓번호', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'root_cause_sysnamea', name: '장비ID', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'root_cause_sysnamea', name: '장비이름', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'ip_addra', name: '마스터IP', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'root_cause_porta', name: '조치내용', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'ticket_type', name: '기타사항', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'request_time', name: '발생시간', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'receive_time', name: '마감시간', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'detail', name: '마감자', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'fixed_delay', name: '노드A', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'fixed_delay', name: '노드Z', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'fixed_delay', name: '마스터 IP', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'root_cause_porta', name: '장비I/F', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },

        ]
        return { options, columns, data: this.dataSetList }
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxHeight = 600
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.modalParam = model
      // this.onLoadList()
    },
    onClickSearchAlarm(params) {
      this.onLoadList(params)
    },
    async onLoadList(params) {
      const target = ({ vue: this.$refs.selectApi })
      this.openLoading(target)
    try {
        const res = ''
        this.dataSetList = res?.result
    } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onClose() { /* for Override */ },
    }

  }
</script>

<style lang="scss" scope>

.aiDisabilityResponse {
  font-family: "NanumSquare";

  .el-dialog{
    width: 1800px !important;
    min-width: 800px !important;
  }

  .el-dialog__body{
    height: 700px !important;
  }

  .CompAgGrid{
    border: 1px solid #d6d6d6;

    .ag-header-container{
      background: #e8ecf0;
    }
  }

}

</style>

