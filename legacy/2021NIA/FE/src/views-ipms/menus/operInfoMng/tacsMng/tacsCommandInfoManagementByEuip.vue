<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: 100%"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-is-cell-click-check="true"
        :prop-max-select="1"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-click="handleClickCell"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            장비별 명령어 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="handleClickProcessBtn('insert')">신규생성</el-button>
            <el-button icon="el-icon-edit" type="primary" size="mini" round @click="handleClickProcessBtn('update')">수정</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalFcltCmdMstInsert ref="ModalFcltCmdMstInsert" @reload="fnViewListTacsFcltCmdMst" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalFcltCmdMstInsert from '@/views-ipms/modal/interlink/ModalFcltCmdMstInsert.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'

import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'TacsCommandInfoManagementByEquip'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalFcltCmdMstInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'sfcltType', label: '장비타입', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sfcltCmd', label: '장비명령어', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sparseContent', label: '할당판단문구', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'suseYn', label: '할당가능여부', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'npriority', label: '명령어순서', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'suseYn', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
      ],
      sfcltTypes: [],
      selectedRow: null,
      componentList: [
        { key: 'ApplyStatus', props: { label: '장비타입', prop_parameterKey: 'sfcltType', prop_options: this.sfcltTypes } },
        { key: 'InputType', props: { label: '장비명령어' } },
        { key: 'UsageYN', props: { label: '사용여부', prop_parameterKey: 'suseYn' } }
      ]
    }
  },
  mounted () {
    this.fnTacsSelectListCommonCode()
    setTimeout(() => {
      this.fnViewListTacsFcltCmdMst()
    })
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListTacsFcltCmdMst(requestParameter)
    },
    async fnTacsSelectListCommonCode() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectListTacsSfcltTypes, {})
        let sfcltTypeTemp = res.result.data
        sfcltTypeTemp = sfcltTypeTemp.map(v => { return { value: v.code, label: v.name } })
        sfcltTypeTemp.unshift({ label: '전체', value: '' })
        this.$set(this, 'sfcltTypes', sfcltTypeTemp)
      } catch (error) {
        this.error(error)
      }
    },
    async fnViewListTacsFcltCmdMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListTacsFcltCmdMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
        setTimeout(() => {
          this.$refs?.compTable?.$refs?.table?.selection.push(this.tableDatas[0])
          this.selectedRow = this.pagination.data[0]
        }, 10)
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListTacsFcltCmdMst()
    },
    handleClickCell(row) {
      this.selectedRow = row
      this.$refs.ModalFcltCmdMstInsert.open({ viewType: 'tacsMng', fnType: 'update', row })
    },
    handleClickProcessBtn(type) {
      const params = { viewType: 'tacsMng', fnType: type }
      if (type === 'update') {
        Object.assign(params, { row: this.selectedRow })
      }
      this.$refs.ModalFcltCmdMstInsert.open(params)
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
