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
        :prop-data="resultList"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 서비스별 통계 현황 조회결과
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
import { getStatColumn } from '@/views-ipms/js/common-function'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'IpStatByService'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      svcList: [],
      resultList: [],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 1, multi: [1], limit: { 1: 5, 2: null, 3: null } } },
        { key: 'IpAddress', props: { label: 'IP 버전', isShowInput: false } },
        { key: 'SipCreateType', props: {} },
        { key: 'DatePicker', props: {} },
      ],
    }
  },
  computed: {
    tableColumns() {
      const columns = getStatColumn('service', this.svcList)
      return columns
    }
  },
  mounted () {
    this.fnViewListSvcStat()
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnViewListSvcStat(requestParameter)
    },
    async fnViewListSvcStat(params = null) {
      const defaultParam = {
          pageIndex: 1,
          pageUnit: 0,
          pageSize: 0,
          firstIndex: 1,
          lastIndex: 10,
          recordCountPerPage: 10,
          rowNo: 0,
      }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListSvcStat, params ?? defaultParam)
        this.svcList = JSON.parse(res.data.svcLineList)
        this.resultList = JSON.parse(res.data.result)
      } catch (error) {
        this.error(error)
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
