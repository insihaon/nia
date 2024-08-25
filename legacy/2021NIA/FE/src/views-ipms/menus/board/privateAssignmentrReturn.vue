<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListPrivateAs"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-data="resultListVo"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
      >
        <template slot="text-description">
          <span>
            사설AS 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalDetailPrivateAs ref="ModalDetailPrivateAs" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalDetailPrivateAs from '@/views-ipms/modal/notice/ModalDetailPrivateAs.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'PrivateAssignmentrReturn'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalDetailPrivateAs },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'nrequestAsSeq', label: 'AS번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsCtm', label: '고객명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dcreateDt', label: '신청일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'screateNm', label: '요청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjNm1', label: '노드1 명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjLlnum1', label: '노드1 전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjNm2', label: '노드2 명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsObjLlnum2', label: '노드2 전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dapvDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAsTypeNm', label: '신청', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'BoardSearchCondition', props: {
            defaultValue: 'asNum',
            prop_options: [
              { label: 'AS번호', value: 'asNum' },
              { label: '고객명', value: 'asCtm' },
              { label: '요청자', value: 'credateId' },
            ]
          }
        },
        {
          key: 'ApplyStatus', props: {
          prop_parameterKey: 'srequestAsTypeCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '신청', value: 'RS0201' },
              { label: '승인', value: 'RS0202' },
              { label: '반려', value: 'RS0203' },
              { label: '배정', value: 'RS0204' },
            ]
          }
        },
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: 'DCREATE_DT',
            label: '정렬조건', prop_options: [
              { label: '신청일', value: 'DCREATE_DT' },
              { label: 'AS번호', value: 'NREQUEST_AS_SEQ' },
              { label: '처리일시', value: 'DAPV_DT' },
            ]
          }
        },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListPrivateAs()
  },
  methods: {
   async fnViewListPrivateAs(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListPrivateAs, requestParameter)
        this.resultListVo = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
    onClcikRow(row) {
      this.fnViewDetailPrvAs(row)
    },
    async fnViewDetailPrvAs(row) {
      if (row.nrequestAsApyTxnSeq === null || row.nrequestAsApyTxnSeq === '') {
        return
      }
      try {
        const tbRequestAsApyTxnVo = {
          nrequestAsApyTxnSeq: row.nrequestAsApyTxnSeq
        }
        const res = await apiRequestModel(ipmsModelApis.viewDetailPrivateAs, tbRequestAsApyTxnVo)
        this.$refs.ModalDetailPrivateAs.open({ row: res.result.data })
      } catch (error) {
        console.error(error)
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
