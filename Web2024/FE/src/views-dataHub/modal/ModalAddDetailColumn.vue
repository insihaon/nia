<template>
  <div :class="{[name]: true}">
    <transition :name="animation">
      <modal
        name="editMonitoringExcludeAlarm"
        transition="pop-out"
        class="resize-modal"
        :width="600"
        :height="500"
        :resizable="true"
        :draggable="false"
        @before-open="beforeOpen"
      >
        <div class="modal-top-container">
          <i class="el-icon-close close" @click="$modal.hide('editMonitoringExcludeAlarm')" />
          <span slot="title">
            <i class="el-icon-bell pr-1" style="font-size: 18px;font-weight: 800;" />
            테이블 정보 - {{ modelParam }}
            <hr>
          </span>
        </div>

        <div class="modal-body-container flex flex-column">
          <!-- <div class="filter-container h-100"> -->
          <CompAgGrid
            ref="CompTemplateTable"
            v-model="newDataAgGrid"
            class="flex-fill w-100 h-100"
            @changeSelectedRows="onSelectedRows"
          />
          <!-- </div> -->
        </div>

        <div class="modal-footer-container">
          <hr>
          <el-row class="w-100 h-100">
            <el-button v-if="routeName === 'CreateDataSet'" size="small" plain type="primary" @click.native="onClickColItem">
              선택 추가
            </el-button>
            <el-button size="small" type="info" @click="$modal.hide('editMonitoringExcludeAlarm')">
              {{ $t('exit') }}
            </el-button>
          </el-row>
        </div>

      </modal>
    </transition>
  </div>
</template>

<script>
// import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import _ from 'lodash'
import { param } from '@/utils'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiSelectDataSetColumnList, apiSelectDataCatalogList } from '@/api/dataHub'

const routeName = 'ModalAddDetailColumn'

export default {
  name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { CompAgGrid },
  directives: { /* elDragDialog */ },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
      tableData: [],
      modelParam: {},
      tableParam: {},
      routeName: '',
      selectedItem: [],
      newData: [],
    }
  },
  computed: {
    newDataAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: this.isMultiple(), rowGroupPanel: false, rowHeight: 35, rowSelection: 'multiple', rowMultiSelection: this.isMultiple(), suppressRowClickSelection: true,
        }
        const columns = [
          { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'data_type', name: '타입', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left' },
          { type: '', prop: 'create_time', name: '수정일시', minWidth: 150, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
          { type: '', prop: 'metadata_desc', name: '설명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        ]
        return { options, columns, data: this.tableData }
      },
    tableClass() {
      return 'tableClass'
    }
  },
  watch: {
  },
  mounted() {
  },
  methods: {
      onOpenPopup(ticketRow) {
      this.contentView = false
      this.$modal.show('editMonitoringExcludeAlarm', { ticketRow: ticketRow })
    },
    beforeOpen(event) {
      const { params } = event
      this.type = event.params.type
      this.routeName = event.params.routeName
      this.modelParam = event.params.params.table_nm
      this.tableParam = params
      if (this.routeName !== 'CreateDataSet') {
        this.loadColList(this.tableParam)
      } else {
          this.newData = event.params.params
          this.tableData = event.params.params
          this.modelParam = event.params.params[0].table_nm
      }
        setTimeout(() => {
      this.setSelectedRows()
    }, 100)
    },
    onClickColItem() {
      this.$emit('selectedNewItem', this.selectedItem, 'modalSelected')
      // this.close()
    },
    async loadColList(event) {
      const param = {
        table_nm: event.params.table_nm
      }
      try {
        const res = await apiSelectDataCatalogList(param)
        this.newData = res?.result
        this.tableData = res?.result
      } catch (error) {
        console.error(error)
      } finally {
        /*  */
      }
    },
    onSelectedRows(param) {
      this.selectedItem = param
    },
    isMultiple() {
      if (this.routeName === 'CreateDataSet') {
        return true
      } else {
        return false
      }
    },
    setSelectedRows() {
    if (this.newData && this.newData.length > 0 && this.tableData && this.tableData.length > 0) {
      this.$nextTick(() => {
        const gridApi = this.$refs.CompTemplateTable.gridApi

        this.newData.forEach(newDataRow => {
          const matchingRow = this.tableData.find(tableDataRow => tableDataRow.column_nm === newDataRow.column_nm && tableDataRow.table_nm === newDataRow.table_nm)

          if (matchingRow) {
            gridApi.forEachNode(node => {
              if (node.data.column_nm === matchingRow.column_nm && node.data.data_type === matchingRow.data_type) {
                node.setSelected(true)
              }
            })
          }
        })
      })
    }
    },
    onClose() { },
    onSubmit() {
        console.log('submit!')
      }
    }
  }
</script>

<style lang="scss" scoped>
@import "~@/styles/dataHub.scss";
.ModalAddDetailColumn {
  font-family: "NanumSquare";
  ::v-deep .el-dialog__body {
    height: 500px !important;
  }
  ::v-deep .CompAgGrid {
    border: 1px solid #d6d6d6;
    .ag-header-container{
      background: #e8ecf0;
    }
    .ag-header-cell-label {
      // display: block !important;
      text-align: left !important;
    }
    .ag-theme-material .ag-checked::after {
      color: #363636bd !important;
      font-size: 13px;
    }
    .ag-theme-material .ag-checkbox-input-wrapper {
      font-size: 13px !important;
      border-radius: 2px !important;
      width: 13px !important;
    }
  }
  .tableClass{
    font-size: 14px;
    font-weight: 900;
    color: #000;
  }

  .ag-header-group-cell-label,
    .ag-header-cell-label {
    display: block !important;
    text-align: left !important;
  }
  .ag-row-odd{
    transform: none !important;
  }

  .ag-row-even{
    transform: none !important;
  }
}
</style>

