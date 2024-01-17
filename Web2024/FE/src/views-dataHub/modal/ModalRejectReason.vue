<template>
  <div>
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
        class="datahub-dialog"
        :class="{ [name]: true }"
      >
        <span slot="title">
          <i class="el-icon-document mr-2" style="font-size: 17px;" />
          반려 사유
          <hr>
        </span>

        <div class="d-flex flex-column h-100" style="height:100%">
          <div />
          <CompAgGrid
            ref="CompTemplateTable"
            v-model="newDataAgGrid"
            class="flex-fill w-100 h-100"
          />
        </div>

        <div slot="footer" class="dialog-footer">
          <el-button size="medium" plain type="info" class="aam-button" @click.native="close()">
            {{ $t('close') }}
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
import { param } from '@/utils'
import CompAgGrid from '@/components/aggrid_/CompAgGrid.vue'

const routeName = 'ModalRejectReason'

export default {
  name: routeName,
    components: {
    CompAgGrid,
  },

  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
        newData: [
       { table: 'API1', state: '반려', column: '권한이 아님', ex: '보통', RejectedTime: '2023-06-08 08:17:32' },
          { table: 'API2', state: '반려', column: '온디맨드임', ex: '경고', RejectedTime: '2023-06-08 08:17:40' },
          { table: 'API3', state: '반려', column: '온디맨드임', ex: '위험', RejectedTime: '2023-06-09 08:17:55' },
          { table: 'API4', state: '반려', column: 'API에러', ex: '위험', RejectedTime: '2023-06-10 09:17:50' },
          { table: 'API5', state: '반려', column: '외부에러', ex: '경고', RejectedTime: '2023-06-20 10:30:32' },
          { table: 'API5', state: '반려', column: '암튼 안됨미다', ex: '보통', RejectedTime: '2023-06-22 07:17:32' },
        ],
      modalParam: {}
    }
  },
  computed: {
    newDataAgGrid() {
      const options = { name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
        const columns = [
          { type: '', prop: 'table', name: 'API명', minWidth: 80, flex: 1, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'state', name: '상태', minWidth: 80, flex: 1, suppressMenu: true, alignItems: 'center' },
          { type: '', prop: 'column', name: '반려사유', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'ex', name: '설명', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
          { type: '', prop: 'RejectedTime', name: '수정일시', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'center', sortable: true, filterable: true },
        ]
        return { options, columns, data: this.newData }
      }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.minHeight = 700
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {},
    onClose() { /* for Override */ },
    onSubmit() {
        console.log('submit!')
      }
    }

  }
</script>

<style lang="scss" scope>
@import "~@/styles/dataHub.scss";

.ModalRejectReason {
  font-family: "NanumSquare";

  .el-dialog__body{
    height: 500px !important;
  }

  .CompAgGrid{
    border: 1px solid #d6d6d6;

    .ag-header-container{
      background: #e8ecf0;
    }
  }

}

</style>

