<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="onLoadIpBlockData"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 120px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        :prop-data="IpBlockData"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-max-select="1"
        :prop-on-click="onClcikRow"
        @update:propRadioSelected="selectedRowItems"
        @update:propColIndex="handleColumnIndexChange"
        @update:propSelectRow="handleRowChange"
      >
        <template slot="text-description">
          <span>
            IP 블록관리 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-document-add" @click="handleOpenTableDetail('', 'create')">신규생성</el-button>
            <el-button size="mini" icon="el-icon-plus" @click="handleOpenTableDetail('', 'generate')">추가생성</el-button>
            <el-button size="mini" icon="el-icon-tickets" @click="handleOpenTableDetail('', 'detail')">상세</el-button>
            <el-button size="mini" icon="el-icon-edit-outline" @click="handleOpenTableDetail('', 'edit')">수정</el-button>
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
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpBlockDetail from '@/views-ipms/modal/ModalIpBlockDetail.vue'
import ModalAddIpBlock from '@/views-ipms/modal/ModalAddIpBlock.vue'
// import { } from '@/api/ipms'
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
        { prop: 'sipCreateSeq', label: '생성차수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipServiceNetNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipBlock', label: 'IP 블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipStartIp', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipEndIp', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'unitBlockCount', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'workDate', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      IpBlockData: [
        {
          nipBlockMstSeq: '1',
          sipCreateTypeNm: '공인',
          sipCreateSeq: 'M2020140001',
          sipServiceNetNm: 'MOBILE',
          sipBlock: '192.168.0.0/24',
          sipStartIp: '192.168.0.1',
          sipEndIp: '192.168.0.254',
          unitBlockCount: 256,
          workDate: '2023-07-15'
        },
        {
          nipBlockMstSeq: '2',
          sipCreateTypeNm: '사설',
          sipCreateSeq: 'M2020140123',
          sipServiceNetNm: 'KORNET',
          sipBlock: '10.0.0.0/24',
          sipStartIp: '10.0.0.1',
          sipEndIp: '10.0.0.254',
          unitBlockCount: 256,
          workDate: '2023-07-16'
        },
        {
          nipBlockMstSeq: '3',
          sipCreateTypeNm: '사설',
          sipCreateSeq: 'M2020143258',
          sipServiceNetNm: 'PREMIUM',
          sipBlock: '10.0.0.0/24',
          sipStartIp: '10.0.0.1',
          sipEndIp: '10.0.0.254',
          unitBlockCount: 256,
          workDate: '2023-07-16'
        }
      ],
      selectedItems: {},
       selectedColumnIndex: null,
      selectedRow: null,
    }
  },
  mounted() {
    // this.onLoadIpBlockData()
  },
  methods: {
   async onLoadIpBlockData(requestParameter) {
      /* try {
        const res = await api(requestParameter)
        this.IpBlockData = res?.result
      } catch (error) {
        console.error(error)
      } */
    },
    selectedRowItems(row) { // radio 클릭시 row 정보
      this.selectedItems = row
    },
     handleColumnIndexChange(columnIndex) { // cell index
      this.selectedColumnIndex = columnIndex
    },
    handleRowChange(row) {
      this.selectedRow = row
    },
    onClcikRow(row) {
       if (this.selectedColumnIndex === 0) {
        return
      }
      this.handleOpenTableDetail(row)
    },
    handleOpenTableDetail(row, type) {
      if (row === null || row === '') {
        row = this.IpBlockData[0]
      }
      if (type === 'create' || type === 'generate') {
        this.$refs.ModalAddIpBlock.open({ row, type })
      } else {
        this.$refs.ModalIpBlockDetail.open({ row, type })
      }
    }
  },
}
</script>
<style lang="scss" scoped></style>
