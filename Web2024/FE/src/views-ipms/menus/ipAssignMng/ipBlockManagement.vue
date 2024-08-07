<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="onLoadIpBlockData"
      @handle-export-excel="exportExcel"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-table-height="'calc(100% - 120px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-data="IpBlockData"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-max-select="1"
        :prop-on-click="onClcikRow"
        @update:propSelected="selectedCheckItems"
      >
        <template slot="text-description">
          <span>
            IP 블록관리 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-document-add" @click="handleOpenAddIpBlock('', 'create')">신규생성</el-button>
            <el-button size="mini" icon="el-icon-plus" @click="handleOpenAddIpBlock('', 'generate')">추가생성</el-button>
            <el-button size="mini" icon="el-icon-tickets" @click="handleOpenIpBlockDetail('', 'detail')">상세</el-button>
            <el-button size="mini" icon="el-icon-edit-outline" @click="handleOpenIpBlockDetail('', 'edit')">수정</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalIpBlockDetail ref="ModalIpBlockDetail" @reloadData="onLoadIpBlockData" />
    <ModalAddIpBlock ref="ModalAddIpBlock" @reloadData="onLoadIpBlockData" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import axios from 'axios'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpBlockDetail from '@/views-ipms/modal/ModalIpBlockDetail.vue'
import ModalAddIpBlock from '@/views-ipms/modal/ModalAddIpBlock.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
const routeName = 'IpBlockManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDetail, ModalAddIpBlock },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SipCreateType', props: {} },
        { key: 'GenerationDegree', props: {} },
        { key: 'IpAddress', props: {} },
        { key: 'SsvcLineType', props: { label: '서비스망' } },
        { key: 'DateRange', props: {} },
        { key: 'SortType', props: {} },
      ],
      tableColumns: [
        { prop: 'nipBlockMstSeq', label: '', align: 'center', propIsCheckBox: true, columnVisible: false, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, propIsCheckBox: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateSeqNm', label: '생성차수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP 블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nclassCnt', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateSeqCd', label: '생성차수코드', align: 'center', sortable: true, columnVisible: false, showOverflow: true },
        { prop: 'sipVersionTypeNm', label: '', align: 'center', sortable: true, columnVisible: false, showOverflow: true }
      ],
      IpBlockData: [],
      selectedRow: [],
      selectedChecks: [],
      requestParam: null
    }
  },
  mounted() {
    this.onLoadIpBlockData()
  },
  methods: {
   async onLoadIpBlockData(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListCrtIPMst, requestParameter)
        this.IpBlockData = res?.result.data
        this.requestParam = requestParameter
        this.$nextTick(() => {
          this.$refs.compTable.$refs.table.selection.push(this.IpBlockData[0])
        })
      } catch (error) {
        console.error(error)
      }
    },
    selectedCheckItems(row) {
      this.selectedChecks = row[0]
      if (row.length === 2) {
        this.selectedChecks = row[1]
      }
    },
    onClcikRow(row) {
      this.selectedRow = row
      this.$refs.ModalIpBlockDetail.open({ row })
    },
    handleOpenIpBlockDetail(row, type) {
      if (this.selectedChecks.length === 0) {
        this.$message('데이터를 선택해주세요')
        return
      } else {
        row = this.selectedChecks
      }
      this.$refs.ModalIpBlockDetail.open({ row, type })
    },
    handleOpenAddIpBlock(row, type) {
      row = this.selectedChecks
       if (this.selectedChecks.length === 0) {
        this.$message('데이터를 선택해주세요')
        return
      } else {
        row = this.selectedChecks
      }
      this.$refs.ModalAddIpBlock.open({ row, type })
    },
    /* async exportExcel() {
      try {
        const response = await axios.post(
          `${this.baseContext}/ipmgmt/createmgmt/viewListCrtIPMstExcel.json`,
          this.requestParam
        )

        const fileUrl = `${this.baseContext}/downloadExcelFile.excel?fileName=${response.data.fileName}`
        const link = document.createElement('a')
        link.href = fileUrl
        link.download = response.data.fileName
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)

        this.$message.info({ message: '엑셀 다운로드가 성공적으로 완료되었습니다.' })
      } catch (error) {
        console.error('Error exporting excel:', error)
        this.$message.error({ message: '엑셀 다운로드 중 오류가 발생했습니다.' })
      }
    } */
  }
}
</script>
<style lang="scss" scoped></style>
