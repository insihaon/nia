<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            WHOIS 정보 변경 신청 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'WhoisInfoChange'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '신청번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '기관명(변경전)', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '기관명(변경후)', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'IpAddress', isShowSelecteBox: false },
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
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
