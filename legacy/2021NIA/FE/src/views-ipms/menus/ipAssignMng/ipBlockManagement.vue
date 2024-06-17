<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 블록관리 조회결과
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
const routeName = 'IpBlockManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SipCreateType', props: {} },
        { key: 'GenerationDegree', props: {} },
        { key: 'IpAddress', props: { } },
        { key: 'SsvcLineType', props: { label: '서비스망' } },
        { key: 'DateRange', props: { value: [] } },
        { key: 'SortType', props: { } },
      ],
      tableColumns: [
        { prop: '', label: '공인/사설', align: 'center', sortable: true, propIsCheckBox: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '생성차수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP 블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ]
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>
<style lang="scss" scoped></style>
