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
            조직계위정보
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
const routeName = 'OrgRankInfoManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
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
        { key: 'SsvcLineType', props: { lvl: 1, multi: [1], limit: { 1: 5, 2: null, 3: null } } },
        { key: 'InputType', props: { label: '노드명' } },
      ]
    }
  },
}
</script>
<style lang="css" scoped>
</style>
