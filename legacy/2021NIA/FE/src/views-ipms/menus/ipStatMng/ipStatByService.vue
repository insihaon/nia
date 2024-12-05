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
        style="height: calc(100% - 40px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-loading.sync="loading"
        :prop-data="resultList"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        @savedExcel="handleClickExcelDownloadBtn"
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
      loading: false,
      svcList: [],
      resultList: [],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 1, multi: [1], limit: { 1: 5, 2: null, 3: null } } },
        { key: 'IpAddress', props: { label: 'IP 버전', isShowInput: false } },
        { key: 'SipCreateType', props: {} },
        { key: 'DatePicker', props: {} },
      ],
      columns: []
    }
  },
  computed: {
    tableColumns() {
      return this.columns
    }
  },
  mounted () {
    setTimeout(async() => {
      this.fnViewListSvcStat()
    }, 10)
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnViewListSvcStat(requestParameter)
    },
    async fnViewListSvcStat(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewListSvcStat, parameter)
        if (res.result.data.resultStatus === 'SUCCESS') {
          this.svcList = JSON.parse(res.result.data.svcLineList)
          this.resultList = JSON.parse(res.result.data.result)
          this.columns = [].concat(...getStatColumn('service', this.svcList))
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    },
    handleClickExcelDownloadBtn() {
      try {
        this.loading = true
        this.exportExcelByElementId('element-table', 'IP서비스별통계현황')
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
