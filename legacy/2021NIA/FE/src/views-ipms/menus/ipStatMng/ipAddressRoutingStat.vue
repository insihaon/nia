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
        :prop-column="tableColumns"
        :prop-data="resultList"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            IP주소 라우팅 비교/점검
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

const routeName = 'IpAddressRoutingStat'

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
        {
          key: 'SsvcLineType', props: { isAllLvl1: false, lvl: 3, defaultValueLvl1: 'CL0001',
          propsLvlOptions: {
              1: [
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
                { label: 'MOBILE', value: 'CL0003' }
              ]
            }
          }
        },
        { key: 'ServiceOrg', props: { limit: 3 } },
        { key: 'SipCreateType', props: {} },
        { key: 'DatePicker', props: { } },
      ],
      columns: []
    }
  },
  computed: {
    tableColumns() {
      // const columns = getStatColumn('routing', this.svcList)
      return this.columns
    }
  },
  mounted () {
    setTimeout(async() => {
      await this.fnviewListIntgrmSvcStat()
    }, 10)
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnviewListIntgrmSvcStat(requestParameter)
    },
    async fnviewListIntgrmSvcStat(requestParameter = null) {
      /*
      ssvcLineTypeCd: CL0001
      ssvcGroupCd:
      sassignTypeCd:
      sassignTypeCdMultiStr:
      sipCreateTypeCd: CT0001
      searchBgnDe: 2024-09-29
      hidDate: 2024-09-29
      */
     const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewListIntgrmSvcStat, parameter)
        if (res.data.resultStatus === 'SUCCESS') {
          this.svcList = JSON.parse(res.data.svcList)
          this.resultList = JSON.parse(res.data.result)
          this.columns = [].concat(...getStatColumn('routing', this.svcList))
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
        this.exportExcelByElementId('element-table', 'IP주소 라우팅 비교_점검 통계 현황')
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
