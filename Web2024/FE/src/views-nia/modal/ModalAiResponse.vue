<template>
  <div>
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
        <i class="el-icon-document mr-2" style="font-size: 17px;" />
        {{ title }}
        <hr>
      </span>
      <div class="d-flex flex-column h-100" style="height:100%">
        <CompInquiryPannel
          ref="selectApi"
          :ag-grid="dataSetAgGrid"
          class="w-100 h-100 flex-fill"
          @handleClickSearch="onClickSearchAlarm"
        />
      </div>
      <div />
      <div slot="footer" class="dialog-footer">
        <el-button size="small" plain type="info" class="close-btn" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiSelectDataSetHistList, apiDeleteDataSetHistList } from '@/api/dataHub'

const routeName = 'ModalAiResponse'

export default {
  name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { CompAgGrid, CellRenderDataSetButtons, apiSelectDataSetHistList, CompInquiryPannel },

  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
      visible: false,
      title: 'AI 장애대응',
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
      this.onLoadList()
    },
    onClickSearchAlarm(params) {
      this.onLoadList(params)
    },
    async onLoadList(params) {
      const target = ({ vue: this.$refs.selectApi })
      this.openLoading(target)
      const param = {
        limit: this.paginationInfoApi.pageSize,
        page: this.paginationInfoApi.currentPage,
        api_name: this.searchApiModel.api_name,
        exec_mode_cd: this.searchApiModel.exec_mode_cd,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
       }
    try {
        const res = ''
        this.dataSetList = res?.result
        this.paginationInfoApi.totalCount = res.total // 총 항목 수 설정
        this.paginationInfoApi.totalPages = Math.ceil(this.paginationInfoApi.totalCount / this.paginationInfoApi.pageSize) // 전체 페이지 수 계산
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
@import "~@/styles/dataHub.scss";

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

