<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            조직정보 조회결과
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

const routeName = 'OrganizationalStandardsManagement'

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
        { prop: '', label: '조직아이디', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '조직명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상위조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'UsageYN', props: { label: '조직 사용여부', prop_parameterKey: 'sipmsOrgYn' } },
        { key: 'InputType', props: { label: '조직명' } },
      ]
    }
  },
  methods: {
    handleSearch(params) {
      console.log(params)
      /* const res = await api(params) */
    }
  },
}
</script>
<style lang="css" scoped>
</style>
