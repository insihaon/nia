<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable :prop-column="tableColumns" :prop-is-pagination="false" :prop-is-check-box="false" prop-grid-menu-id="inputSpeed" :prop-grid-indx="1">
        <template slot="text-description">
          <span>
            IP 배정신청 조회결과
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

const routeName = 'IpResourceAssignmentApply'

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
        { prop: '', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
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
