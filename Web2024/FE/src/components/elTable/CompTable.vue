/* * version 1.0 * * Copyright ⓒ 2017 kt corp. All rights reserved. * * This is a proprietary software of kt corp, and you may not use this file except in * compliance with license
agreement with kt corp. Any redistribution or use of this * software, with or without modification shall be strictly prohibited without prior written * approval of kt corp, and the
copyright notice above does not evidence any actual or * intended publication of such software. */
<template>
  <div v-loading="propLoading">
    <div class="tableThum">
      <div v-if="propIsPagination != false"><i class="el-icon-document" /> 총 {{ propPaginationData.total }} 건</div>
      <div v-if="propIsPagination == false"><i class="el-icon-document" /> 총 {{ propData.length }} 건</div>
    </div>

    <el-table
      ref="table"
      class="cursor_pointer"
      size="mini"
      :cell-class-name="propHighlightCell"
      border
      fit
      highlight-current-row
      :data="propData"
      :height="propTableHeight"
      :row-class-name="propHighlight"
      @row-dblclick="propOnDblClick"
      @row-click="propOnClick"
      @row-contextmenu="fn_contextmenu"
      @select="fn_select"
      @select-all="fn_select"
      @header-contextmenu="fn_headerContextmenu(...arguments, propGridMenuId, propGridIndx, propColumn, propSetColunm)"
      @header-dragend="fn_headerDragend(...arguments, propGridMenuId, propGridIndx, propColumn, propSetColunm)"
    >
      <el-table-column v-if="propIsCheckBox" type="selection" align="center" width="35" />
      <el-table-column
        v-for="(item, index) in propColumn"
        v-if="item.columnVisible"
        :key="index"
        :type="item.type"
        :width="item.width + 30"
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
        :page-sizes="pageSizes"
        :page-size="propPaginationData.pageSize"
        layout="sizes, prev, pager, next"
        @current-change="propOnPageChange"
        @size-change="fn_onPageSizeChange"
      />
    </div>
    <vue-simple-context-menu ref="propTableContext" element-id="propTable" :options="propRClickOptions" @option-clicked="propOnRClick" />
  </div>
</template>

<script>
/* eslint-disable */
import 'vue-simple-context-menu/dist/vue-simple-context-menu.css'

const routeName = 'CompTable'

export default {
  name: routeName,
  components: {
    VueSimpleContextMenu: () => import('vue-simple-context-menu'),
  },
  props: {
    propColumn: { type: Array, default: [{ prop: '-', label: '-' }] }, //컬럼
    propData: {
      type: Array,
      default: () => {
        return []
      },
    }, //데이터
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
    propTableHeight: { type: Number, default: 500 }, //테이블 상하너비
    propOnDblClick: { type: Function, default: () => {} }, //더블클릭액션
    propOnClick: { type: Function, default: () => {} }, //클릭액션
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
    }
  },
  computed: {
  },
  methods: {
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
    fn_onPageSizeChange(pageSize) {
      var temp_pagin = this.propPaginationData
      temp_pagin.pageSize = pageSize
      temp_pagin.currentPage = 1
      this.$emit('update:propPaginationData', temp_pagin)
      this.propOnPageSizeChange(temp_pagin.currentPage)
    },
  },
  watch: {},
}
</script>
<style scoped></style>
