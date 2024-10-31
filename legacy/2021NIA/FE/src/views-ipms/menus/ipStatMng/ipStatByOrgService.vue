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
        :prop-highlight="rowHighlight"
        :prop-data="resultList"
        :prop-span-method="spanBySum"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            IP 조직서비스별 통계 현황 조회결과
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

const routeName = 'IpStatByOrgService'

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
        { key: 'SsvcLineType', props: { lvl: 3 } },
        { key: 'IpAddress', props: { label: 'IP 버전', isShowInput: false } },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { limit: 3 } },
        { key: 'DatePicker', props: { prop_parameterKey: 'searchBgnDe' } },
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
      this.fnViewListOrgSvcStat()
    }, 10)
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnViewListOrgSvcStat(requestParameter)
    },
    async fnViewListOrgSvcStat(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewListOrgSvcStat, parameter)
        if (res.data.resultStatus === 'SUCCESS') {
          this.svcList = JSON.parse(res.data.svcList)
          this.resultList = JSON.parse(res.data.result)
          this.columns = [].concat(...getStatColumn('orgService', this.svcList))
        }
      } catch (error) {
        this.error(error)
      } finally {
        this.loading = false
      }
    },
    rowHighlight({ row, rowIndex }) {
      if (row.ssvc_line_type_nm === '소계') {
        return 'black-row'
      }
      if (row.ssvc_group_nm === '소계') {
        return 'darkgray-row'
      }
      if (row.ssvc_obj_nm === '소계') {
        return 'gray-row'
      }
    },
    spanBySum({ row, column, rowIndex, columnIndex }) {
      const isSumMang = row.ssvc_line_type_nm === '소계'
      const isSumOrg = row.ssvc_group_nm === '소계'
      const isSameMangOrg = row.ssvc_line_type_nm === row.ssvc_group_nm
      const isSameOrgNode = row.ssvc_group_nm === row.ssvc_obj_nm
      if (columnIndex === 0 && isSumMang && isSameMangOrg) {
        return { rowspan: 1, colspan: 3 }
      } else if ((columnIndex === 1 || columnIndex === 2) && isSumMang && isSameMangOrg) {
          return { rowspan: 0, colspan: 0 } /* 데이터가 밀리는 col을 지워줌. */
      } else if (columnIndex === 1 && isSumOrg && isSameOrgNode) {
          return { rowspan: 1, colspan: 2 }
      } else if (columnIndex === 2 && isSumOrg && isSameOrgNode) {
          return { rowspan: 0, colspan: 0 }
      }
    },
    handleClickExcelDownloadBtn() {
      try {
        this.loading = true
        this.exportExcelByElementId('element-table', 'IP조직서비스별통계현황')
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
::v-deep .gray-row {
  background: #5f5f5f;
}
::v-deep .darkgray-row {
  background: #434343;
}
::v-deep .black-row {
  font-weight: bold;
  background: #1c1c1c;
}
</style>
