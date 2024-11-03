<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="fnViewListOrgBas"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="resultListVo"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
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
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

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
        { prop: 'sktOrgId', label: '조직아이디', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sFullOrgNm', label: '조직명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'supKtOrgNm', label: '상위조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'shqOrgNm', label: '본부조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipmsOrgYn', label: '조직 사용여부', align: 'center', sortable: true, columnVisible: false, showOverflow: true },
      ],
      componentList: [
        { key: 'UsageYN', props: { label: '조직 사용여부', prop_parameterKey: 'sipmsOrgYn' } },
        { key: 'InputType', props: { label: '조직명' } },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListOrgBas()
  },
  methods: {
    async fnViewListOrgBas(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListOrgBas, requestParameter)
        this.resultListVo = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
  },
}
</script>
