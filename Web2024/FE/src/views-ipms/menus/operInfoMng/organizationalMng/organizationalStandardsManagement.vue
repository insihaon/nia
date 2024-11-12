<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: 100%"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
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
      pagination: this.setDefaultPagination(),
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
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListOrgBas(requestParameter)
    },
    async fnViewListOrgBas(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListOrgBas, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListOrgBas()
    },
  },
}
</script>
