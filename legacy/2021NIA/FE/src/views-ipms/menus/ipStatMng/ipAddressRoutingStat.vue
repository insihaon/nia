<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
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

const routeName = 'ipAddressRoutingStat'

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
        {
          key: 'SsvcLineType', props: { lvl: 3, defaultValueLvl1: 'CL0001',
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
      ]
    }
  },
  computed: {
    tableColumns() {
      const columns = getStatColumn('routing', this.svcList)
      return columns
    }
  },
  mounted () {
    this.fnviewListIntgrmSvcStat()
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnviewListIntgrmSvcStat(requestParameter)
    },
    async fnviewListIntgrmSvcStat(params = null) {
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
        const res = await apiRequestModel(ipmsModelApis.viewListIntgrmSvcStat, params ?? defaultParam)
        this.svcList = JSON.parse(res.data.svcList)
        this.resultList = JSON.parse(res.data.result)
      } catch (error) {
        this.error(error)
      }
    }
  },
}
</script>
<style lang="css" scoped></style>
