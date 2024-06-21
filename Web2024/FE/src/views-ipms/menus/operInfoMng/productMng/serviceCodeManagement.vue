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
            서비스 코드
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
const routeName = 'ServiceCodeManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'NeOSS연동 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수정', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'IncludeYN', props: { label: 'NeOSS연동 여부', parameterKey: 'sneossDdYn' } },
        { key: 'InputType', props: { label: '서비스', propsParameterKey: 'sassignTypeNm' } },
        { key: 'InputType', props: { label: '코드명', propsParameterKey: 'sassignTypeCd' } },
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
