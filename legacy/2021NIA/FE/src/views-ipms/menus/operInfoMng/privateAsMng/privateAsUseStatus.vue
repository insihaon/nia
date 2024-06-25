<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            사설AS 사용현황 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'PrivateAsUseStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: 'AS번호', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '고객명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '신청일', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '요청자', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '노드1 명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '노드1 전용번호', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '노드2 명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '노드2 전용번호', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '최종수정자', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '최종수정일', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'ApplyStatus', props: { label: '이용목적', prop_parameterKey: 'ssvcUseTypeCd' } },
        { key: 'UsageYN', props: { label: '상태', prop_parameterKey: 'srequestAsTypeCd' } },
        { key: 'DateRange', props: { label: '최종수정기간' } },
      ]
    }
  },
  methods: {
    handleSearch(params) {
      /* const res = await api(params) */
    }
  }
}
</script>
<style lang="css" scoped>
</style>
