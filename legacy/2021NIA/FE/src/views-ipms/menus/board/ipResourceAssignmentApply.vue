<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListAssignApyTxn"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-data="resultListVo"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
      >
        <template slot="text-description">
          <span>
            IP 배정신청 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-plus" @click="fnViewIPAssignApyPre()">배정신청</el-button>
          </div>
        </template>
      </compTable>
      <ModalAssignApyDetail ref="ModalAssignApyDetail" @reload="fnViewListAssignApyTxn()" />
      <ModalAssignApyInsert ref="ModalAssignApyInsert" @reload="fnViewListAssignApyTxn()" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalAssignApyDetail from '@/views-ipms/modal/ModalAssignApyDetail.vue'
import ModalAssignApyInsert from '@/views-ipms/modal/ModalAssignApyInsert.vue'

const routeName = 'IpResourceAssignmentApply'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalAssignApyDetail, ModalAssignApyInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'nrequestAssignSeq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'stitle', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sapyUserNm', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dapyDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'srequestAssignTypeNm', label: '상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dtrtDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        {
          key: 'ApplyStatus', props: {
          prop_parameterKey: 'srequestAssignTypeCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '신청', value: 'RS0301' },
              { label: '승인', value: 'RS0302' },
              { label: '반려', value: 'RS0303' },
              { label: '배정', value: 'RS0304' },
            ]
          }
        },
        { key: 'InputType', props: { label: '신청자' } },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListAssignApyTxn()
  },
  methods: {
    async fnViewListAssignApyTxn(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListAssignApyTxn, requestParameter)
        this.resultListVo = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
    onClcikRow(row) {
      this.fnViewDetailIpAssignApy(row, 'detail')
    },
    async fnViewDetailIpAssignApy(row, type) {
      try {
        const { nrequestAssignSeq } = row
        const TbRequestAssignMstVo = { nrequestAssignSeq: nrequestAssignSeq }
        const res = await apiRequestModel(ipmsModelApis.viewDetailAssignApyTxn, TbRequestAssignMstVo)
        if (res.result.data) {
          this.$refs.ModalAssignApyDetail.open({ row: res.result.data, type: type })
        }
      } catch (error) {
        console.error(error)
      }
    },
    fnViewIPAssignApyPre() {
      this.$refs.ModalAssignApyInsert.open()
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
