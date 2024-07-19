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
      IpBlockData: [
      {
        nipBlockMstSeq: '1',
        sipCreateTypeNm: '공인',
        sipCreateSeqNm: 'M2020140001',
        ssvcLineTypeNm: 'MOBILE',
        pipPrefix: '192.168.0.0/24',
        sfirstAddr: '192.168.0.1',
        slastAddr: '192.168.0.254',
        nclassCnt: 256,
        dmodifyDt: '2023-07-15',
        sipCreateSeqCd: 'M2020140123',
        sipVersionTypeNm: 'CV0001'
      },
      {
        nipBlockMstSeq: '2',
        sipCreateTypeNm: '사설',
        sipCreateSeqNm: 'M2020140123',
        ssvcLineTypeNm: 'KORNET',
        pipPrefix: '10.0.0.0/24',
        sfirstAddr: '10.0.0.1',
        slastAddr: '10.0.0.254',
        nclassCnt: 256,
        dmodifyDt: '2023-07-16',
        sipCreateSeqCd: 'M2020140124',
        sipVersionTypeNm: 'CV0002'
      },
      {
        nipBlockMstSeq: '3',
        sipCreateTypeNm: '사설',
        sipCreateSeqNm: 'M2020143258',
        ssvcLineTypeNm: 'PREMIUM',
        pipPrefix: '10.0.0.0/24',
        sfirstAddr: '10.0.0.1',
        slastAddr: '10.0.0.254',
        nclassCnt: 256,
        dmodifyDt: '2023-07-16',
        sipCreateSeqCd: 'M2020140125',
        sipVersionTypeNm: 'CV0001'
      }
    ],
      selectedRow: [],
      selectedChecks: [],
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
    selectedCheckItems(row) {
      this.selectedChecks = row
    },
    onClcikRow(row) {
      this.selectedRow = row
      // this.handleOpenIpBlockDetail(row)
      this.$refs.ModalIpBlockDetail.open({ row })
    },
      handleOpenIpBlockDetail(row, type) {
    if (this.selectedChecks.length === 0) {
      row = this.IpBlockData[0]
    } else {
      row = this.selectedChecks[1]
    }
      this.$refs.ModalIpBlockDetail.open({ row, type })
    },
    handleOpenAddIpBlock(row, type) {
      row = this.selectedChecks
       if (this.selectedChecks.length === 0) {
        row = this.IpBlockData[0]
      } else {
        row = this.selectedChecks[1]
      }
      this.$refs.ModalAddIpBlock.open({ row, type })
    }
  },
}
</script>
<style lang="scss" scoped></style>
