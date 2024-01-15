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
          테이블 정보 - {{ modelParam }}
          <hr>
        </span>

        <div class="d-flex flex-column h-100" style="height:100%">
          <CompAgGrid
            ref="CompTemplateTable"
            v-model="newDataAgGrid"
            class="flex-fill w-100 h-100"
            @changeSelectedRows="onSelectedRows"
          />
        </div>

        <div slot="footer" class="dialog-footer">
          <el-button v-if="routeName === 'CreateDataSet'" size="small" plain type="primary" @click.native="onClickColItem">
            선택 추가
          </el-button>
          <el-button size="small" plain type="info" @click.native="close()">
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
import { param } from '@/utils'
import CompAgGrid from '@/components/AgGrid/CompAgGrid.vue'
import { apiSelectDataSetColumnList } from '@/api/dataHub'

const routeName = 'ModalAddDetailColumn'

export default {
  name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { CompAgGrid },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      type: true,
      tableData: [],
      modelParam: {},
      routeName: '',
      selectedItem: [],
      newData: []
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
    onClickColItem() {
      this.$emit('selectedNewItem', this.selectedItem, 'modalSelected')
      this.close()
    },
    onSelectedRows(param) {
      this.selectedItem = param
    },
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.domElement.minHeight = 700
      this.closeOnClickModal = false
    },
    onOpen(param, actionMode) {
      this.newData = param.newData
      this.modelParam = param.params[0].table_nm
      this.tableData = param.params
      this.routeName = param.routeName
      setTimeout(() => {
      this.setSelectedRows()
    }, 100)
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
    onClose() { /* for Override */ },
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
}
</style>

