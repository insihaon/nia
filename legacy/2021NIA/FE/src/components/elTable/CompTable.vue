/* * version 1.0 * * Copyright ⓒ 2017 kt corp. All rights reserved. * * This is a proprietary software of kt corp, and you may not use this file except in * compliance with license
agreement with kt corp. Any redistribution or use of this * software, with or without modification shall be strictly prohibited without prior written * approval of kt corp, and the
copyright notice above does not evidence any actual or * intended publication of such software. */
<template>
  <div v-loading="propLoading" class="compTable">
    <div class="tableThum">
      <div class="d-flex items-baseline">
        <i class="el-icon-document mr-1" />
        <slot name="text-description" />
        <span class="countNum">( 총 {{ propIsPagination != false ? propPaginationData.total : propData.length }} 건 )</span>
        <i class="el-icon-s-tools ml-1 mt-1" @click="fn_click_settings" />
      </div>
      <slot name="add-features" />
    </div>
    <el-table
      ref="table"
      class="cursor_pointer"
      size="mini"
      :cell-class-name="propHighlightCell"
      fit
      border
      highlight-current-row
      :data="propData"
      :span-method="propSpanMethod"
      :height="propTableHeight"
      :row-class-name="propHighlight"
      @cell-click="fn_cell_click"
      @row-dblclick="propOnDblClick"
      @row-click="propOnClick"
      @row-contextmenu="fn_contextmenu"
      @select="fn_select"
      @select-all="fn_select"
      @header-dragend="headerDragend"
    >
      <!-- @header-contextmenu="fn_headerContextmenu(...arguments, propGridMenuId, propGridIndx, propColumn, propSetColunm)"
      @header-dragend="fn_headerDragend(...arguments, propGridMenuId, propGridIndx, propColumn, propSetColunm)" -->
      <el-table-column v-if="propIsCheckBox" :index="0" type="selection" align="center" width="35" />
      <el-table-column
        v-for="(item, index) in columnDefs"
        v-if="item.columnVisible"
        :key="index"
        :type="item.type"
        :width="item.width"
        :align="item.align"
        :prop="item.prop"
        :label="item.label"
        :fixed="item.fixed"
        :sortable="item.sortable"
        :show-overflow-tooltip="item.showOverflow"
        :formatter="item.formatter"
      >
        <template v-if="item.children">
          <el-table-column
            v-for="(citem, cindex) in item.children"
            v-if="citem.columnVisible"
            :key="cindex"
            :type="citem.type"
            :width="citem.width"
            :align="citem.align"
            :prop="citem.prop"
            :label="citem.label"
            :fixed="citem.fixed"
            :sortable="citem.sortable"
            :show-overflow-tooltip="citem.showOverflow"
            :formatter="citem.formatter"
          />
        </template>
      </el-table-column>
    </el-table>
    <div v-if="propIsPagination != false" style="text-align: center; margin-top: 10px">
      <el-pagination
        :current-page.sync="propPaginationData.currentPage"
        :total="propPaginationData.total"
        :page-size="propPaginationData.pageSize"
        layout="sizes, prev, pager, next"
        @current-change="propOnPageChange"
        @size-change="fn_onPageSizeChange"
      />
      <!-- :page-sizes="pageSizes" -->
    </div>
    <vue-simple-context-menu ref="propTableContext" element-id="propTable" :options="propRClickOptions" @option-clicked="propOnRClick" />
    <SettingTableOptions ref="SettingTableOptions" :prop_name="propName" :prop_columns="propColumn" />
  </div>
</template>

<script>
/* eslint-disable */
import { Base } from '@/min/Base.min'
import 'vue-simple-context-menu/dist/vue-simple-context-menu.css'
import { array_equals } from '@/utils'
import SettingTableOptions from '@/components/elTable/SettingTableOptions'

const routeName = 'CompTable'

export default {
  name: routeName,
  components: {
    VueSimpleContextMenu: () => import('vue-simple-context-menu'),
    SettingTableOptions
  },
  extends: Base,
  props: {
    propName: { type: String, default: '' },
    propColumn: { type: Array, default: [{ prop: '-', label: '-' }] }, //컬럼
    propData: {
      type: Array,
      default: () => {
        return []
      },
    }, //데이터
    propSpanMethod: { type: Function, default: () => {} }, // 행, 열 span 처리
    propIsCheckBox: { type: Boolean, default: false }, //체크박스 유무
    propOnSelect: { type: Function, default: () => {} }, //체크시
    propMaxSelect: { type: Number, default: 0 }, //체크갯수 최대
    propSelected: {
      type: Array,
      default: () => {
        return []
      },
    }, //체크된 데이터
    propIsPagination: { type: Boolean, default: false }, //페이징 유무
    propPaginationData: {
      type: Object,
      default: () => {
        return {}
      },
    }, //페이징 데이터
    propOnPageSizeChange: { type: Function, default: () => {} }, //테이블 사이즈 변경 액션
    propOnPageChange: { type: Function, default: () => {} }, //테이블 페이지 변경 액션
    propHighlight: {
      type: Function,
      default: () => {
        return ''
      },
    }, //하이라이트 조건과 클래스
    propHighlightCell: {
      type: Function,
      default: () => {
        return ''
      },
    }, // 하이라이트 셀 조건과 클래스
    propTableHeight: { type: String | Number, default: 300 }, //테이블 상하너비
    propOnDblClick: { type: Function, default: () => {} }, //더블클릭액션
    propOnClick: { type: Function, default: () => {} }, //클릭액션
    propOnCellClick: { type: Function, default: () => {} }, //셀클릭액션
    propIsCellClickCheck: { type: Boolean, default: false },
    propIsRClick: { type: Boolean, default: false }, //우클릭 액션 유무
    propRClickOptions: {
      type: Array,
      default: () => {
        return [{ name: '-', number: 0 }]
      },
    }, //우클릭 메뉴 옵션
    propOnRClick: { type: Function, default: () => {} }, //우클릭 메뉴 액션
    propIsinfiScroll: { type: Boolean, default: false },
    propLoading: { type: Boolean, default: false },
    propGridMenuId: { type: String, default: '' },
    propGridIndx: { type: Number, default: 1 },
    propSetColunm: { type: Boolean, default: false },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableSelectTemp: [],
      columnDefs: null,
      savedColumnState: null,
      resizeObserver: null
    }
  },
  watch: {
    propColumn(n, o) {
      if(array_equals(n, o)) return

      const THIS = this
      setTimeout(() => {
        THIS.updateColumnDefs()
      }, 100)
    }
  },
  mounted () {
    setTimeout(() => {
      this.init()
    }, 10)
    setTimeout(() => {
      this.setDefaultWidth()
    }, 100)
  },
  methods: {
    headerDragend(newWidth, oldWidth, column, event) {
      let savedColumnState
      const name = this.propName
      if(name) {
        savedColumnState = JSON.parse(window.localStorage['savedColumnState'] || '{}')
      }
      const currentState = savedColumnState[name] ? savedColumnState[name] : this.propColumn
      currentState.forEach(col => {
        if(col.prop === column.property) {
          col.width = newWidth
        }
        if(col.children) {
          col.children.forEach(chCol => {
            if(chCol.prop === column.property) {
              chCol.width = newWidth
            }
          }) 
        }
      })
      savedColumnState[name] = currentState
      window.localStorage['savedColumnState'] = JSON.stringify(savedColumnState)
    },
    init() {
      this.updateColumnDefs()
    },
    setDefaultWidth(){
      this.$refs.table.columns.forEach(col => {
        const el = document.querySelector(`th.${col.id}`)
        if(el) {
          this.headerDragend(el.clientWidth, null, col)
        }
      })
    },
    updateColumnDefs() {
      const name = this.propName
      let savedColumnState
      if(name) {
        savedColumnState = JSON.parse(window.localStorage['savedColumnState'] || '{}')[name]
      }
      if (!savedColumnState) {
        savedColumnState = this.propColumn
      }
      if (!savedColumnState || !name) {
        this.error('no order and visibility state to restore by, you must save order and visibility state first')
        return
      }
      this.columnDefs = savedColumnState
    },
    fn_select(all, current) {
      if (!this.propIsCheckBox) {
        return
      }
      this.propOnSelect(all, current)

      if (this.propMaxSelect == 0) {
        this.tableSelectTemp = all
      } else if (this.propMaxSelect == 1) {
        if (current == undefined) {
          this.$refs.table.clearSelection()
          this.tableSelectTemp = []
        } else {
          this.$refs.table.clearSelection()
          if (all.length > 0) {
            this.$refs.table.selection.push(current)
          }
          this.tableSelectTemp = all
        }
      } else {
        if (current == undefined) {
          this.$refs.table.clearSelection()
          this.tableSelectTemp = []
        } else {
          if (this.propMaxSelect < all.length) {
            this.alert('최대' + this.propMaxSelect + '개까지 선택 가능합니다.')
            this.$refs.table.clearSelection()
            this.tableSelectTemp.forEach((element) => {
              this.$refs.table.selection.push(element)
            })
          } else {
            this.tableSelectTemp = all
            
          }
        }
      }
      this.$emit('update:propSelected', this.tableSelectTemp)
    },
    fn_contextmenu(row, colunm, event) {
      if (!this.propIsRClick) {
        return
      }
      event.preventDefault()
      this.$refs.propTableContext.showMenu(event, row)
      var contextmenu = document.querySelector('#facilContextmenu')
      contextmenu.style.left = event.clientX + 'px'
      contextmenu.style.top = event.clientY + 'px'
    },
    fn_cell_click(row, column, cell, event) {
      if(this.propIsCellClickCheck) {
        this.$refs.table.clearSelection()
        this.$refs.table.selection.push(row)
      }
      if(column.index === 0) return
      this.$emit('update:propCellClick', { row, column })
    },
    fn_onPageSizeChange(pageSize) {
      var temp_pagin = this.propPaginationData
      temp_pagin.pageSize = pageSize
      temp_pagin.currentPage = 1
      this.$emit('update:propPaginationData', temp_pagin)
      this.propOnPageSizeChange(temp_pagin.currentPage)
    },
    fn_click_settings() {
      this.$refs.SettingTableOptions.open()
    },
  },
}
</script>
<style lang="scss" scoped>
::v-deep .el-loading-spinner {
  left: 50%;
}
.el-icon-s-tools:hover {
  opacity: 0.6;
  cursor: pointer;
}
</style>
