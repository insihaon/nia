<template>
  <div>
    <!-- <transition :name="animation"> -->
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
        국가별 트래픽 분석 및 Top N
        <hr />
      </span>
      <div class="d-flex flex-column h-100" style="height:100%">
        <!-- <CompAgGrid
          ref="CompTemplateTable"
          v-model="dataSetAgGrid"
          class="flex-fill w-100 h-100"
        /> -->
        <CompInquiryPannel
          ref="selectApi"
          :ag-grid="dataSetAgGrid"
          title="장비 검색"
          :items.sync="searchApiItems"
          class="w-100 h-100 flex-fill"
          @handleClickSearch="onClickSearchApi"
        />
      </div>
      <div />
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" plain type="info" class="aam-button" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
    <!-- </transition> -->
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import { param } from '@/utils'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'

import CellRenderDataSetButtons from '@/views-dataHub/components/cellRenderer/CellRenderDataSetButtons'
import { apiSelectDataSetHistList, apiDeleteDataSetHistList } from '@/api/dataHub'
import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'

const routeName = 'CompTrafficAnalysisModal'

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
      searchApiItems: [
          { label: 'API명', type: 'input', size: 8, model: 'api_name', placeholder: 'API명을 검색하세요' },
          { label: '연동방식', type: 'select', size: 8, model: 'exec_mode_cd', placeholder: '연동방식을 선택하세요', multiple: true, setting: { allOption: { toggle: true } }, options: [
          { label: 'On-Demand', value: 'O' }, { label: 'Batch', value: 'B' }]
        },
      ],
        dataSetList: [
        {
          model_name: '상황방명',
          key: 1,
          start_date: '2022-09-06',
          end_date: '2022-09-07',
          alarm_count: '1',
          live_channel_spread_count: 1,
          alarm_edit_record_count: 1,
          group: '그룹',
          registration_date: '등록일자',
          explanation: '설명'
        },
        {
          model_name: '상황',
          key: 2,
          start_date: '2022-09-06',
          end_date: '2022-09-07',
          alarm_count: '2',
          live_channel_spread_count: 2,
          alarm_edit_record_count: 2,
          group: '그룹',
          registration_date: '등록일자',
          explanation: '설명'
        },
       ],

    }
  },
  computed: {
    dataSetAgGrid() {
      const options = { name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false }
        const columns = [
          { type: '', prop: 'model_name', name: 'API 데이터명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'key', name: '요청 일시', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'start_date', name: '상태', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false,
           cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'reject', action: this.openRejectMessage.bind(this) } },
          { type: '', prop: 'user', name: '요청자', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'dataset_desc', name: '용도 및 요청 설명', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'exec_mode_cd_desc', name: '연동 방법', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'fixed_delay', name: '요청 주기', minWidth: 250, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: '_', name: '기능', minWidth: 50, suppressMenu: true, alignItems: 'left', cellRendererFramework: 'CellRenderDataSetButtons', cellRendererParams: { action: this.handelCancelApply.bind(this), detailButton: { name: '상세정보', key: 'detail' }, cancleButton: { name: '요청취소', key: 'cancle' } } },
        ]
        return { options, columns, data: this.dataSetList }
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    openRejectMessage() {
      this.isVisible = !this.isVisible
    },
    // async onLoadList() {
    //   try {
    //     const res = await apiSelectDataSetHistList()
    //     this.dataSetList = res?.result
    //   } catch (error) {
    //     console.log(error)
    //   }
    // },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.maxHeight = 500
      this.closeOnClickModal = false
    },
    onOpen(model, actionMode) {
      this.modalParam = model?.modalEditInfo[0]
      console.log(model)
      this.onLoadList()
    },
    onClickSearchApi(params) {
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
    handelCancelApply(type, data) {
       this.$confirm('생성 요청을 취소 하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warning'
      }).then(async() => {
        try {
          const params = {
          dataset_id: data.dataset_id
        }
        const res = await apiDeleteDataSetHistList(params)
          if (res.result) {
            this.$message('요청 취소 처리 되었습니다.')
          }
          this.onLoadList()
        } catch (error) {
          console.log(error)
        }
      })
    },
    onClose() { /* for Override */ },
    onSubmit() {
        console.log('submit!')
      }
    }

  }
</script>

<style lang="scss" scope>
@import "~@/styles/dataHub.scss";

.CompTrafficAnalysisModal {
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

