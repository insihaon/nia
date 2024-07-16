<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="onLoadIpBlockData"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-radio-box="true"
        :prop-data="IpBlockData"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleOpenTableDetail"
        @update:propRadioSelected="selectedRowItems"
      >
        <template slot="text-description">
          <span>
            IP 블록관리 조회결과
          </span>
        </template>
      </compTable>
      <template slot="button-container">
        <div class="my-1">
          <el-button type="info" size="small" icon="el-icon-edit" @click="handleOpenTable('', 'create')">등록</el-button>
        </div>
      </template>
    </el-col>
    <ModalIpBlockDetail ref="ModalIpBlockDetail" @reloadData="onLoadIpBlockData" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalIpBlockDetail from '@/views-ipms/modal/ModalIpBlockDetail.vue'
// import { } from '@/api/ipms'
const routeName = 'IpBlockManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpBlockDetail },
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
      selectedItems: {}
    }
  },
  mounted() {
    // this.onLoadIpBlockData()
  },
  methods: {
   async onLoadIpBlockData(requestParameter) {
       const param = {
        searchBgnDe: requestParameter.searchBgnDe !== '' ? requestParameter.searchBgnDe : null,
        searchEndDe: requestParameter.searchEndDe !== '' ? requestParameter.searchEndDe : null,
        searchWrd: requestParameter.searchWrd,
        sipCreateSeqCd: requestParameter.sipCreateSeqCd,
        sipCreateTypeCd: requestParameter.sipCreateTypeCd,
        sipVersionTypeCd: requestParameter.sipVersionTypeCd,
        sortOrdr: requestParameter.sortOrdr,
        sortType: requestParameter.sortType,
        ssvcLineTypeCd: requestParameter.ssvcLineTypeCd,
      }
       try {
        const res = await /*  */(param)
        this.IpBlockData = res?.result
        } catch (error) {
          console.error(error)
      }
    },
    selectedRowItems(row) {
      this.selectedItems = row
    },
    handleOpenTableDetail(row) {
      this.$refs.ModalIpBlockDetail.open({ row: row })
    },
  },
}
</script>
<style lang="scss" scoped></style>
