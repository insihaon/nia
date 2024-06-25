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
            사설AS 조회결과
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

const routeName = 'PrivateAssignmentrReturn'

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
        { prop: '', label: 'AS번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '고객명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '요청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드1 명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드1 전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드2 명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드2 전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
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
