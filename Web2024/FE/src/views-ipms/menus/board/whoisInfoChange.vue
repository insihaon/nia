<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListWhoisModReq"
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
            WHOIS 정보 변경 신청 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-document-add" @click="fnViewDetailWhoisMod('', 'create')">신청</el-button>
          </div>
        </template>
      </compTable>
      <ModalDetailWhoisMod ref="ModalDetailWhoisMod" />
      <ModalRegWhoisModReq ref="ModalRegWhoisModReq" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalDetailWhoisMod from '@/views-ipms/modal/notice/ModalDetailWhoisMod.vue'
import ModalRegWhoisModReq from '@/views-ipms/modal/notice/ModalRegWhoisModReq.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'WhoisInfoChange'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalDetailWhoisMod, ModalRegWhoisModReq },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'nmodify_apply_seq', label: '신청번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sBefOrgName', label: '기관명(변경전)', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sAftOrgName', label: '기관명(변경후)', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sApplyNm', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dApplyDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dApprovalDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sStatNm', label: '상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'IpAddress', props: { isShowSelectBox: false } },
        {
          key: 'ApplyStatus', props: {
          prop_parameterKey: 'searchCnd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '신청', value: '10' },
              { label: '승인', value: '20' },
              { label: '반려', value: '30' },
            ]
          }
        },
        {
          key: 'SortType', props: {
            sortTypeDefaultVal: 'DAPPLY_DT',
            label: '정렬조건', prop_options: [
              { label: '신청일', value: 'DAPPLY_DT' },
              { label: '요청번호', value: 'NMODIFY_APPLY_SEQ' },
              { label: '처리일', value: 'DAPPROVAL_DT' },
            ]
          }
        },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListWhoisModReq()
  },
  methods: {
   async fnViewListWhoisModReq(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListWhoisModReq, requestParameter)
        this.resultListVo = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
    onClcikRow(row, type) {
      this.fnViewDetailWhoisMod(row, 'detail')
    },
    async fnViewDetailWhoisMod(row, type) {
      if (type === 'detail') {
        try {
          if (row.nmodify_apply_seq === '' || row.nmodify_apply_seq === null) {
            return
          }
            const tbWhoisModifyVo = {
              nmodify_apply_seq: row.nmodify_apply_seq
            }
            const res = await apiRequestModel(ipmsModelApis.viewDetailWhoisModReq, tbWhoisModifyVo)
            this.$refs.ModalDetailWhoisMod.open({ row: res.result.data, type: type })
          } catch (error) {
            console.error(error)
        }
      } else {
        this.$refs.ModalRegWhoisModReq.open()
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
