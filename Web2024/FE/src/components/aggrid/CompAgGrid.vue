<template>
  <div ref="aggrid" class="component-container" :class="{ [name]: true,[value.options.theme ||'']: true }" :style="customStyle">
    <el-row class="aggrid-row" style="display:flex; height: 100%; flex-direction:column">
      <ag-grid-vue
        ref="aggridvue"
        :group-remove-single-children="eval('value.options?.groupRemoveSingleChildren')"
        :locale-text="localeText"
        :style="{height: `${eval('value.options?.height') || '100%'}`}"
        style="flex:1"
        class="ag-theme-material"
        :class="{}"
        :row-data="value.data"
        :get-row-node-id="eval('value.options?.getRowNodeId')"
        :default-col-def="defaultColDef"
        :column-defs="columnDefs"
        :post-sort="eval('value.options?.postSort')"
        :suppress-drag-leave-hides-columns="true"
        :suppress-make-column-visible-after-un-group="true"
        :row-group-panel-show="rowGroupPanel"
        :suppress-row-click-selection="eval('value.options?.suppressRowClickSelection')"
        :enable-range-selection="true"
        :row-selection="eval('value.options?.rowSelection')"
        :row-multi-select-with-click="eval('value.options?.rowMultiSelection')"
        :animate-rows="true"
        :side-bar="false"
        :row-buffer="rowBuffer"
        :tooltip-show-delay="0"
        :enable-browser-tooltips="true"
        :framework-components="frameworkComponents"
        :cell-flash-delay="2000"
        :cell-fade-delay="500"
        :header-height="eval('value.options?.headerHeight ?? 35')"
        :row-height="eval('value.options?.rowHeight ?? 30')"
        :auto-group-column-def="eval('value.options?.autogroupColumnDef')"
        :pagination="eval('value.options?.pagination')"
        :pagination-page-size="eval('paginationModel.pageSize')"
        :pagination-auto-page-size="eval('value.options?.autoPageSize ?? false')"
        :pagination-number-formatter="eval('value.options?.paginationNumberFormatter')"
        :is-external-filter-present="isExternalFilterPresent"
        :does-external-filter-pass="doesExternalFilterPass"
        :get-context-menu-items="value.getRightClickMenuItems"
        :default-excel-export-params="defaultExcelExportParams"
        :default-csv-export-params="defaultCsvExportParams"
        :get-row-style="getRowStyle"
        :get-row-class="getRowClass"
        :group-default-expanded="eval('value.options?.groupDefaultExpanded ')"
        :suppress-agg-func-in-header="true"
        :group-include-footer="eval('value.options?.footer?.group')"
        :group-include-total-footer="eval('value.options?.footer?.total')"
        :row-deselection="true"
        :multi-sort-key="eval('value.options?.multiSortKey')"
        :overlay-no-rows-template="eval('value.options?.overlayNoRowsText')"
        :excel-styles="excelStyles"
        :is-row-selectable="isRowSelectable"
        :row-model-type="rowModelType"
        @pagination-changed="onPaginationChanged"
        @grid-ready="onGridReady"
        @firstDataRendered="firstDataRendered"
        @sort-changed="onSortChanged"
        @column-resized="onColumnResized"
        @column-visible="onColumnVisible"
        @column-pivot-changed="onColumnPivotChanged"
        @column-row-group-changed="onColumnRowGroupChanged"
        @column-value-changed="onColumnValueChanged"
        @column-moved="onColumnMoved"
        @column-pinned="onColumnPinned"
        @cell-value-changed="onCellValueChanged"
        @row-selected="getSelectedRows"
        @row-clicked="onRowClicked"
        @row-double-clicked="onRowDoubleClicked"
        @cell-clicked="onCellClicked"
        @check="onCheckRow"
      />
      <el-pagination
        v-if="Object.keys(paginationInfo).length > 0"
        small
        :layout="pageLayout"
        :total="paginationInfo.totalCount"
        :page-sizes="paginationInfo.pageSizes"
        :page-size="paginationInfo.pageSize"
        :pager-count="paginationInfo.pagerCount"
        :current-page="paginationInfo.currentPage"
        background-color="rgb(249, 249, 249);"
        style="margin: 0 auto;"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
      <!-- pagerCount : 홀수 -->
    </el-row>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import { AgGridVue } from 'ag-grid-vue'
import CustomTooltip from './CustomTooltip'
import { AG_GRID_LOCALE_KR } from './locale.kr'
import { AG_GRID_LOCALE_EN } from './locale.en'
import { deepCloneFilter, exportToFile, array_equals } from '@/utils'
import Hook from '@/class/hook'
import _ from 'lodash'

export const _var = { array_equals }

import ComponentTesterMixins from '@/test/ComponentTesterMixins'

const EVENTS = {
  /**
   * @Deprecated : 주의 사용 할 수 없음, 사용해도 이벤트 발생 안함
   */
  selectionChanged: () => {},
  rowSelectionChanged: 'rowSelectionChanged',
  rowDoubleClicked: 'rowDoubleClicked',
  rowDoubleClickedEvent: 'rowDoubleClickedEvent',
  rowClicked: 'rowClicked',
  rowClickedEvent: 'rowClickedEvent',
  cellClicked: 'cellClicked',
  changeSelectedRows: 'changeSelectedRows',
  changeRowChecked: 'changeRowChecked',
  sortChanged: 'sortChanged',
  check: 'check',
  paginationPageLode: 'paginationPageLode',
  gridWidth: 'gridWidth'
}

/*
  valueGetter - row 순서대로 번호를 줌
*/

/*
  [Extension]
  + copy/paste(enable-range-selection) : https://www.ag-grid.com/vue-grid/clipboard/#using-enablecelltextselection
    suppress-row-click-selection
      1. true 일 경우
        + row 가 아닌 single cell 단위로 복사하가 된다.
        + aggrid.gridApi.getSelectedRows() = [] 가 된다.
      2. false 일 경우
        + checkbox 없이 선택이 가능.  rowSelection: 'single', 'multiple' 상관없다
        + aggrid.gridApi.getSelectedRows() = 선택된 row data 가 들어간다
  + side-bar : https://www.ag-grid.com/vue-grid/side-bar/
  + context-menu : https://www.ag-grid.com/vue-grid/context-menu/#example-context-menu
*/
// const consoleError = window.console.error

const routeName = 'CompAgGrid'
const verticalScrollSeletor = 'div.ag-root-wrapper-body.ag-layout-normal > div.ag-root.ag-layout-normal > div.ag-body-viewport.ag-layout-normal'
// const horizonScrollSeletor = 'div.ag-root-wrapper-body.ag-layout-normal > div.ag-root.ag-layout-normal > div.ag-body-viewport.ag-layout-normal > div.ag-center-cols-clipper > div'

export default {
  name: routeName,
  components: { AgGridVue },
  extends: Base,
  mixins: [ComponentTesterMixins],
  props: {
    value: {
      type: Object,
      default() {
        return {}
      }
    },
    paginationInfo: {
      type: Object,
      default() { return {} }
    }

  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      defaultColDef: null,
      columnDefs: null,
      gridApi: null,
      gridColumnApi: null,
      rowBuffer: 10,
      savedColumnState: null,
      defaultExcelExportParams: { fileName: 'excel' },
      defaultCsvExportParams: { fileName: 'csv' },
      frameworkComponents: { customTooltip: CustomTooltip },
      externalFilter: '',
      selectedRows: [],
      // autoGroupColumnDef: {},
      excelStyles: null,
      clickedCell: null,
      rowModelType: 'clientSide',
      tempY: 0
    }
  },
  computed: {
    pageLayout() {
      let defaultsLayout = 'prev, pager, next'
      if (!this.paginationInfo) {
        return defaultsLayout
      } else {
        if (this.paginationInfo.customLayout) {
          return this.paginationInfo.customLayout
        } else {
          if (this.paginationInfo.isJumper) {
            defaultsLayout += ', jumper'
          }

          if (this.paginationInfo.pageSizes && this.paginationInfo.pageSizes.length > 1) {
            defaultsLayout += ', sizes'
          }
          return defaultsLayout
        }
      }
    },

    customStyle() {
      return {
        '--cell-padding': this.value.options?.cellPadding ?? '23px'
      }
    },
    localeText() {
      const locale = this.value.options?.locale ?? this.$i18n.locale
      if (locale === 'en') {
        return AG_GRID_LOCALE_EN
      } else {
        return AG_GRID_LOCALE_KR
      }
    },
    rows() {
      return this.getNodes()
    },
    rowNodes() {
      const array = []
      this.gridApi.forEachNode(node => array.push(node))
      return array
    },
    columns() {
      return this.columnDefs
    },
    rowGroupPanel() {
      let panelOption = this.value.options?.rowGroupPanel
      panelOption = panelOption ? 'always' : 'never'
      return panelOption
    },
    scrollTop: {
      set(val) {
        try {
          this.$refs.aggridvue.$el.querySelector(verticalScrollSeletor).scrollTop = val
          this.tempY = val
        } catch (error) {
          console.log(error)
         }
      },
      get() {
        return this.$refs.aggridvue.$el.querySelector(verticalScrollSeletor).scrollTop ?? 0
      }
    },
    paginationModel() {
      if (this.value.options.paginationModel) {
        return this.value.options.paginationModel
      } else {
        return {
          pageSize: this.gridApi?.paginationGetPageSize(),
          totalPage: this.gridApi?.paginationGetTotalPages(),
          currentPage: this.gridApi?.paginationGetCurrentPage()
        }
      }

      // return this.value.options.paginationModel || {
      //   pageSize: this.gridApi?.paginationGetPageSize(),
      //   totalPage: this.gridApi?.paginationGetTotalPages(),
      //   currentPage: this.gridApi?.paginationGetCurrentPage()
      // }
    }
  },
  watch: {
    'value.data'(n, o) {
      if (this.value.options?.scrollPosition) {
        if (n === o) return
        const currentScrollTop = this.$refs.aggridvue.$el.querySelector(verticalScrollSeletor).scrollTop
        this.$nextTick().then(() => {
          this.scrollTop = currentScrollTop
          this.gridApi?.redrawRows()
        })
      }
    },
    'value.columns'(n, o) {
      if (n === o) return
      if (JSON.stringify(this.oldColumns) === JSON.stringify(n)) return

      // language, locale 변경 시 체크박스가 지워지는 오류를 막기 위해 컬럼을 재설정 한다
      this.columnDefs = []
      this.oldColumns = null

      const THIS = this
      setTimeout(() => {
        THIS.updateColumnDefs()
      }, 100)
    },
  },
  created() {
    this.excelStyles = [
      { id: 'header',
        alignment: {
          horizontal: 'Center'
        },
        interior: {
          color: '#d8d8d8',
          pattern: 'Solid',
          patternColor: undefined
        },
        font: {
          bold: true,
          color: '#000000',
          size: 14
        }
      }
    ]
  },
  mounted() {
    this.updateColumnDefs()
    // window.console.error = consoleError
    setTimeout(() => {
      this.$nextTick(() => {
        this.onGridWidth()

        if (this.value.api) {
          this.rowModelType = 'serverSide'
          this.setServerSideDataSource()
        }
      })
    }, 500)
  },
  beforeDestroy() {
  },
  beforeMount() {
    this.defaultColDef = this._merge({
      minWidth: 30,
      sortable: true,
      filter: true,
      resizable: this.value.options?.resizable ?? true,
      tooltipComponent: 'customTooltip',
      cellClassRules: this.value.options?.rules
      // enableRowGroup: true, // grouping boolean 값
      // rowGroup: false
    }, this.tableOptions)
    // this.autoGroupColumnDef = {
    //   cellRendererParams: {
    //     innerRenderer: (params) => {
    //       return params.value
    //     }
    //   }
    // }
    // window.console.error = () => {}
  },
  methods: {
    handleSizeChange(changedSize) {
      this.paginationInfo.pageSize = changedSize
      this.devEmit('emitPagingCommonEvent', changedSize) // 페이징 관련 공통 이벤트
      this.devEmit('pageSizeChange', changedSize) // 페이지 사이즈 변경 이벤트
    },

    handlePageChange(newPage) {
      this.paginationInfo.currentPage = newPage
      this.devEmit('emitPagingCommonEvent', newPage) // 페이징 관련 공통 이벤트
      this.devEmit('pageChange', this.paginationInfo.currentPage) // 특정 페이지로 이동할 때 데이터 다시 가져오기
    },

    isRowSelectable(rowNode) {
      // todo: 나중에 구현하도록
      // https://plnkr.co/edit/?p=preview&preview
      return true
    },
    setColumnDefs(newColumnDefs) {
      this.gridApi?.setColumnDefs(newColumnDefs)
    },
    updateColumnDefs() {
      if (array_equals(this.value.columns, this.oldColumns)) { return }

      const options = this.value.options
      const columns = this.value.columns
      this.oldColumns = _.cloneDeep(columns)

      if (options && options.checkable && columns[0].headerName !== 'checkAll') {
        columns.splice(0, 0, { type: '', prop: 'checkAll', headerName: 'checkAll', filter: false, headerCheckboxSelection: true, headerCheckboxSelectionFilteredOnly: true, checkboxSelection: true, maxWidth: 50, resizable: false })
      }

      if (options && options.rightClickExcelName) {
        this.defaultExcelExportParams.fileName = options.rightClickExcelName
        this.defaultCsvExportParams.fileName = options.rightClickExcelName
      }

      const nomalize = (c, hasChildren) => {
        c.headerName = c.headerName || c.label || c.name || c.prop
        c.field = c.field || c.prop
        c.tooltipField = c.field
        c.tooltipComponentParams = { color: 'red' }
        c.pinned = c.fixed
        c.align = c.alignItems
        c.cellClass = `td-${c.field}-class ${c.fill ? 'fill' : ''}`
        c.headerClass = `th-${c.field}-class` + ((hasChildren && !c.children) ? ' ' + 'mergedHeaderGroupColumn' : '')
        const formatter = c.format || c.formatter || c.cellRendererParams?.fnFormatter
        let agFormatter
        if (formatter) {
          agFormatter = (params) => {
            return formatter.call(this, params.data, params.column.colId, params.value)
          }
        }
        c.valueFormatter = agFormatter
        c.hide = (c.show === false)
        c.width = (typeof c.width === 'number') ? c.width : Number(c.width)
        c.enableRowGroup = c.enableRowGroup === undefined ? true : c.enableRowGroup
        c.cellStyle = Object.assign(c.cellStyle ?? {}, { textAlign: c.alignItems })
      }

      const convertRecusive = (columns) => {
        const hasChildren = !!columns.find((c) => c.children)

        // prettier-ignore
        columns.forEach(c => {
          nomalize(c, hasChildren)
          c.children && convertRecusive(c.children)
        })
      }

      convertRecusive(columns)

      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.columnDefs = columns
    },
    eval(p) {
      // eslint-disable-next-line no-eval
      return eval('this.' + p)
    },
    selectNode(fn) {
      const api = this.gridApi
      const rowNode = this.findRowNode(fn)
      api.selectNode(rowNode)
    },
    scrollToNode(fn, select) {
      const api = this.gridApi
      const rowNode = this.findRowNode(fn)
      api.ensureNodeVisible(rowNode)
      select && api.selectNode(rowNode)
    },
    findRowNode(fn) {
      let rowNode
      this.gridApi.forEachNode(node => {
        if (fn(node.data)) {
          rowNode = node
        }
      })
      return rowNode
    },
    getNodes(filter = null) {
      const array = []
      this.gridApi.forEachNodeAfterFilterAndSort(node => array.push(deepCloneFilter(node.data, filter)))
      return array
    },
    getAllNodes() {
      const nodes = []
      this.gridApi.forEachNode((rowNode, index) => {
        nodes.push(rowNode)
      })
      return nodes
    },
    onSelectionChanged($event) {
      // 이벤트 핸들러 샘플
      this.selectedRows = [].concat(...this.gridApi.getSelectedRows())
      this.devEmit(EVENTS.rowSelectionChanged, this.selectedRows)
    },
    onRowDoubleClicked($event) {
      const selectedNodeds = this.gridApi.getSelectedNodes()
      const selectedData = selectedNodeds.map(node => node.data)
      copyToClipboard(this.clickedCell.value)
      this.devEmit(EVENTS.rowDoubleClicked, this.value.options.rowMultiSelection ? [$event.data] : selectedData)
      this.devEmit(EVENTS.rowDoubleClickedEvent, Object.assign($event, this.clickedCell))
    },
    printFilterAndSort() {
      this.log('### api.forEachNodeAfterFilterAndSort() ###')
      this.gridApi.forEachNodeAfterFilterAndSort(this.printNode)
    },
    printNode(node, index) {
      if (node.group) {
        this.log(index + ' -> group: ' + node.key)
      } else {
        this.log(index + ' -> data: ', node.data)
      }
    },
    getRowStyle(params) {
      if (!this.value.options?.getRowStyle) return
      else return this.value.options?.getRowStyle(params)
    },
    getRowClass(params) {
      if (!this.value.options?.getRowClass) return
      else return this.value.options?.getRowClass(params)
    },
    openLoading() {
      this.$refs.refAgGrid.loading = true
    },
    closeLoading() {
      this.$refs.refAgGrid.loading = false
    },
    getSelectedRows(param) {
      const selectedNodeds = this.gridApi.getSelectedNodes()
      const selectedData = selectedNodeds.map(node => node.data)
      this.devEmit(EVENTS.changeSelectedRows, selectedData, param)
      this.devEmit(EVENTS.changeRowChecked, param)
      return selectedData
    },
    sizeToFit() {
      this.gridApi?.sizeColumnsToFit()
    },
    autoSizeAll(skipHeader = false) {
      var allColumnIds = []
      this.gridColumnApi.getAllColumns().forEach(function(column) {
        allColumnIds.push(column.colId)
      })
      this.gridColumnApi.autoSizeColumns(allColumnIds, skipHeader)
    },
    saveColumnState() {
      var allState = this.gridColumnApi.getColumnState()
      var currentState = allState.map(function(state) {
        return { ...state /* rowGroup: undefined, rowGroupIndex: undefined*/ }
      })
      const name = this.value.options?.name
      if (name) {
        const savedColumnState = JSON.parse(window.localStorage['savedColumnState'] || '{}')
        savedColumnState[name] = currentState
        window.localStorage['savedColumnState'] = JSON.stringify(savedColumnState)
      } else {
        this.savedColumnState = currentState
      }
      // this.log('order and visibility state saved', currentState)
    },
    restoreColumnState() {
      const name = this.value.options?.name
      let savedColumnState = this.savedColumnState
      if (name) {
        savedColumnState = JSON.parse(window.localStorage['savedColumnState'] || '{}')[name]
      }
      if (!savedColumnState) {
        this.log('no order and visibility state to restore by, you must save order and visibility state first')
        return
      }

      // 전체 선택의 경우 항상 index 0
      const index = savedColumnState.findIndex(item => item.colId === 'checkAll')
      if (index !== -1) { // 조건을 만족하는 항목을 찾은 경우
        const itemToMove = savedColumnState[index]
        savedColumnState.splice(index, 1) // 해당 아이템을 배열에서 제거합니다.
        savedColumnState.unshift(itemToMove) // 아이템을 배열의 맨 앞에 추가합니다.
      }

      this.gridColumnApi.applyColumnState({
        state: savedColumnState,
        applyOrder: true
      })

      if (this.gridColumnApi?.columnController?.groupAutoColumns) {
        // 25.1.0 > 28.2.0 업데이트했는데 columnController 가 없다.
        this.gridColumnApi.moveColumn('ag-Grid-AutoColumn', 0)
      } else {
        this.gridColumnApi.autoSizeColumn('ag-Grid-AutoColumn')
      }
      this.log('column state restored')
    },
    resetColumnState() {
      this.gridColumnApi.resetColumnState()
    },
    sort() {
      this.gridApi.onSortChanged()
    },
    getExportParams(option = {}) {
      const params = Object.assign({
        suppressQuotes: undefined, // undefined or true
        columnSeparator: undefined, // undefined or tab('\t') or "|"
        customHeader: undefined,
        customFooter: undefined,
        fileName: this.value.options?.name,
        processCellCallback: (cell) => {
          const colDef = cell.column.getColDef()
          // try to reuse valueFormatter from the colDef
          if (colDef.valueFormatter) {
            const valueFormatterParams = {
              ...cell,
              data: cell.node.data,
              node: cell.node,
              colDef: cell.column.getColDef()
            }
            return colDef.valueFormatter(valueFormatterParams)
          }
          return cell.value
        }
      }, option)
      if (params.suppressQuotes || params.columnSeparator) {
        console.warn('참고: 비표준 따옴표 또는 구분 기호가 있는 파일을 다운로드 중입니다. Excel에서 올바르게 렌더링되지 않을 수 있습니다.')
      }
      return params
    },
    exportCsv(fileName) {
      const params = this.getExportParams({
        fileName: fileName
      })
      this.gridApi.exportDataAsCsv(params)
    },
    exportExcel(fileName, add_param = {}) {
      const params = this.getExportParams(Object.assign({ fileName: fileName }, add_param))
      this.gridApi.exportDataAsExcel(params)
    },
    exportTxt(fileName) {
      const params = this.getExportParams({
        suppressQuotes: true,
        columnSeparator: '\t'
      })
      const txt = this.gridApi.getDataAsCsv(params)
      exportToFile(txt, `${fileName}.txt`)
    },
    async redrawRows() {
      const THIS = this
      return Hook.instance.runThen('redraw', 200).then(() => {
        try {
          THIS.gridApi.redrawRows()
          THIS.sort()
        } catch (error) {
          // 예외 처리
        }
      })
    },
    copyRows() {
      if (!this.gridApi.copySelectedRowsToClipboard) {
        console.error('ag-grid-community 는 클립보드 복사를 지원하지 않습니다.')
        return
      }
      this.gridApi.copySelectedRowsToClipboard()
    },
    copyRange() {
      if (!this.gridApi.copySelectedRangeToClipboard) {
        console.error('ag-grid-community 는 클립보드 복사를 지원하지 않습니다.')
        return
      }
      this.gridApi.copySelectedRangeToClipboard()
    },
    setFilter(value) {
      this.gridApi.setQuickFilter(value)
    },
    clearFilter() {
      this.gridApi.setQuickFilter('')
    },
    onGridReady(params) {
      this.gridApi = params.api
      this.gridColumnApi = params.columnApi

      // 이벤트 핸들러 샘플
      this.gridApi.addEventListener('selectionChanged', this.onSelectionChanged)
      // this.gridApi.addEventListener('rowDoubleClicked', this.onRowDoubleClicked)
      this.restoreColumnState()
    },
    firstDataRendered() {
      if (this.value.options?.defaultRowSelection) {
        this.gridApi.getRowNode(this.value.options.defaultRowSelection).setSelected(true)
      }
    },
    onColumnChanged(e) {
      this.saveColumnState()
    },
    onSortChanged(e) {
      this.onColumnChanged()
      // this.log('Event Sort Changed', e);
      const sortedColumns = e.columnApi.getColumnState().filter(col => col.sort)
      this.devEmit(EVENTS.sortChanged, sortedColumns)
    },
    onColumnResized(e) {
      this.onColumnChanged()
      // this.log('Event Column Resized', e);
    },
    onColumnVisible(e) {
      this.onColumnChanged()
      // this.log('Event Column Visible', e);
    },
    onColumnPivotChanged(e) {
      this.onColumnChanged()
      // this.log('Event Pivot Changed', e);
    },
    onColumnRowGroupChanged(e) {
      this.onColumnChanged()
      // this.log('Event Row Group Changed', e);
    },
    onColumnValueChanged(e) {
      this.onColumnChanged()
      // this.log('Event Value Changed', e);
    },
    onColumnMoved(e) {
      this.onColumnChanged()
      // this.log('Event Column Moved', e);
    },
    onColumnPinned(e) {
      this.onColumnChanged()
      // this.log('Event Column Pinned', e);
    },
    onCellValueChanged(params) {
      this.onColumnChanged()
      // this.log('Callback onCellValueChanged:', params)
    },
    isExternalFilterPresent() { return true },
    externalFilterChanged(newValue) {
      this.externalFilter = newValue
      this.gridApi.onFilterChanged()
    },
    doesExternalFilterPass(node) {
      return !this.value.onDoesExternalFilterPass || this.value.onDoesExternalFilterPass(this.externalFilter, node)
    },
    onCheckRow(row) {
      this.devEmit(EVENTS.check, row)
    },
    onRowClicked(row) {
      this.devEmit(EVENTS.rowClicked, row)
    },
    onCellClicked(event) {
      this.devEmit(EVENTS.cellClicked, event)
      this.clickedCell = {
        column: event.column,
        value: event.value
      }
    },
    onPaginationChanged() {
      this.devEmit(EVENTS.paginationPageLode, this.gridApi)
    },
    onGridWidth() {
      const width = this.$refs.aggrid?.clientWidth
      this.devEmit(EVENTS.gridWidth, width)
    },
    onRemoveSelected() {
      const selectedData = this.gridApi.getSelectedRows()
      this.gridApi.applyTransaction({ remove: selectedData })
    },

    deSelectAll() {
      this.gridApi.deselectAll()
    },

    onClickPageBtn(Fn) {
      if (!this.value.options.paginationModel) {
        Fn && this.gridApi[Fn]()
        this.devEmit('pageChange', this.paginationModel)
      } else {
        this.value.options.paginationModel.currentPage++
        this.$forceUpdate()
      }
    }
  }
}

import 'ag-grid-enterprise'
import { LicenseManager } from 'ag-grid-enterprise'
import { GridOptionsWrapper, GridApi } from 'ag-grid-community'
import { copyToClipboard } from 'quasar'

LicenseManager.prototype.outputMissingLicenseKey = () => {}
GridOptionsWrapper.prototype.checkProperties = () => {}
GridApi.prototype.setSortModel = function(sortModel, source) {
  // console.warn('AG Grid: as of version 24.0.0, setSortModel() is deprecated, sort information is now part of Column State. Please use columnApi.applyColumnState() instead.')
  var columnState = []
  if (sortModel) {
    sortModel.forEach(function(item, index) {
      columnState.push({
        colId: item.colId,
        sort: item.sort,
        sortIndex: index
      })
    })
  }
  this.columnController.applyColumnState({ state: columnState, defaultState: { sort: null } })
}
GridApi.prototype.getSortModel = function() {
  // console.warn('AG Grid: as of version 24.0.0, getSortModel() is deprecated, sort information is now part of Column State. Please use columnApi.getColumnState() instead.')
  var columnState = this.columnController.getColumnState()
  var filteredStates = columnState.filter(function(item) { return item.sort != null })
  var indexes = {}
  filteredStates.forEach(function(state) {
    var id = state.colId
    var sortIndex = state.sortIndex
    indexes[id] = sortIndex
  })
  var res = filteredStates.map(function(s) {
    return { colId: s.colId, sort: s.sort }
  })
  res.sort(function(a, b) { return indexes[a.colId] - indexes[b.colId] })
  return res
}
</script>

<style lang="scss" scope>
.CompAgGrid {
  // GRID HEADER Text Padding 제거
  .ag-theme-material .ag-header-cell,
  .ag-theme-material .ag-header-group-cell {
    &:not(.th-checkAll-class) {
      padding-left: 0 !important;
      padding-right: 0 !important;
    }
  }

  .ag-header-cell-label {
      display: flex !important;
      align-items: center;
      justify-content: center !important;
    }

  .ag-header-cell {
    color: black;
    font-size: 1em;
    font-weight: 800;
  }

  .ag-group-expanded:not(.ag-hidden){
    height:100% !important;
    display: block !important;
  }

  .ag-group-child-count{
    height:100% !important;
  }

  .ag-theme-material .ag-header-cell,
  .ag-theme-material .ag-header-group-cell {
    &:not(.th-checkAll-class) {
      // padding-left: 2em !important;
      // padding-right: 2em !important;
    }
  }

  .ag-header-group-cell{
    color: black;
    font-size: 1em;
    font-weight: 800;
    background-color:#f8f8f9 !important;
    border:1px solid #e8eaec;
    transition: all 0.25s;

    .ag-header-group-cell-label {
      display: grid;
      color: black;
    }
  }

  .ag-header-row {
    color: black;
    font-size: 12px;
    font-weight: 800;

  }

  .ag-header {
    text-align: center !important;
  }

  .ag-body-viewport{
    border-bottom: 3px solid rgb(232, 234, 236);
  }

  .ag-cell {
    font-family: "굴림", "SUIT-Medium", "Spoqa Han Sans Neo", "sans-serif"  !important;
    font-weight: normal;
    color:#505050;
    font-size: 11.5px;
    padding: 0 var(--cell-padding) !important;
    line-height: 30px !important;

    &.fill {
      padding: 0 !important;
    }
  }

  .ag-full-width-row{
    .ag-cell-wrapper.ag-row-group{
      color:black
    }
  }

  .ag-center-cols-viewport  {
    text-align: center;
  }

  // 우클릭 메뉴 스타일
  .ag-menu-option-part.ag-menu-option-text{
    font-size:14px;
  }

  /*
    아래 .ag-header-row.ag-header-row-column와 mergedHeaderGroupColumn은
    group화된 header가 있을 경우 나머지의 이이를 group과 맞춰주기 위함이다.
  */
  .ag-header-row.ag-header-row-column{
    overflow: visible;
  }

  .mergedHeaderGroupColumn{
    height: 200%;
    top: -100%;
    z-index: 9999;
    background-color: #FFFFFF;
  }

  .el-pagination{

    .el-pagination__sizes{
      .el-input__inner{
        height:22px;
      }

      .el-input--mini .el-input__icon{
        line-height: 22px;
      }
    }

  }
}

</style>

