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
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleClickCell"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @update:propSelected="handleClickCheck"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            장비별 명령어 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="handleClickRegist()">신규생성</el-button>
            <el-button icon="el-icon-document-delete" type="primary" size="mini" round @click="fnDeleteFcltCmdMst()">삭제</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalFcltCmdMstInsert ref="ModalFcltCmdMstInsert" @reload="fnViewListFcltCmdMst" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import { onMessagePopup } from '@/utils/index'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalFcltCmdMstInsert from '@/views-ipms/modal/interlink/ModalFcltCmdMstInsert.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { downloadExcel } from '@/views-ipms/js/common-function'

import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'CommandInfoManagementByEquip'

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
        { prop: 'sfcltType', label: '장비타입', align: 'center', width: 450, columnVisible: true, showOverflow: true },
        { prop: 'sfcltCmd', label: '장비명령어', align: 'center', width: 990, columnVisible: true, showOverflow: true },
        { prop: 'npriority', label: '명령어순서', align: 'center', width: 170, columnVisible: true, showOverflow: true },
        { prop: 'suseYn', label: '사용여부', align: 'center', width: 170, columnVisible: true, showOverflow: true },
      ],
      tableDatas: [],
      sfcltTypes: [],
      delRows: []
    }
  },
  computed: {
    componentList() {
      return [
        { key: 'EquipmentType', props: { prop_parameterKey: 'sfcltType', prop_options: this.sfcltTypes } },
        { key: 'InputType', props: { label: '장비명령어' } },
        { key: 'UsageYN', props: { label: '사용여부', prop_parameterKey: 'suseYn' } }
      ]
    }
  },
  mounted () {
    this.fnSelectListCommonCode()
    // setTimeout(() => {
    // }, 100);
    this.fnViewListFcltCmdMst()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListFcltCmdMst(requestParameter)
    },
    async fnSelectListCommonCode() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectListSfcltTypes, {})
        let sfcltTypeTemp = res.result.data
        sfcltTypeTemp = sfcltTypeTemp.map(v => { return { value: v.code, label: v.name } })
        sfcltTypeTemp.unshift({ label: '전체', value: '' })
        this.$set(this, 'sfcltTypes', sfcltTypeTemp)
      } catch (error) {
        this.error(error)
      }
    },
    async fnViewListFcltCmdMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListFcltCmdMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListFcltCmdMst()
    },
    handleClickCell(row) {
      this.$refs.ModalFcltCmdMstInsert.open({ viewType: 'ipRouting', fnType: 'update', row })
    },
    handleClickCheck(rows) {
      this.delRows = rows
    },
    handleClickRegist() {
      this.$refs.ModalFcltCmdMstInsert.open({ viewType: 'ipRouting', fnType: 'insert' })
    },
    async fnDeleteFcltCmdMst() {
      const delRows = this.delRows
      if (delRows.length === 0) {
        onMessagePopup(this, '삭제할 정보를 선택 후 삭제 가능합니다.')
        return
      }
      const delList = []
      delRows.forEach(row => { delList.push(row.nfcltCmdMstSeq) })
      this.confirm('해당 장비의 명령어 정보를 정말 삭제하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warnning'
      }).then(async () => {
        try {
          const res = await apiRequestJson(ipmsJsonApis.deleteFcltCmdMst, { delList })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '장비별 명령어 정보 삭제가 정상적으로 처리되었습니다.')
            this.fnViewListFcltCmdMst()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListFcltCmdMstExcel')
    }
  }
}
</script>
<style lang="css" scoped>
</style>
