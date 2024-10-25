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
        :prop-column="tableColumns"
        :prop-data="resultList"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 블록크기별 통계 현황
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

const routeName = 'IpStatByBlockSize'

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
      loading: false,
      resultList: [],
      componentList: [
        { key: 'SsvcLineType', props: { isAllLvl1: false, lvl: 1, defaultValueLvl1: 'CL0001' } },
        { key: 'BlockSize', props: {} },
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
      this.fnViewListBlockSizeStat()
    }, 10)
  },
  methods: {
    handleSearch(requestParameter) {
      this.fnViewListBlockSizeStat(requestParameter)
    },
    async fnViewListBlockSizeStat(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      try {
        this.loading = true
        const res = await apiRequestModel(ipmsModelApis.viewListBlockSizeStat, parameter)
        if (res.data.resultStatus === 'SUCCESS') {
          this.svcList = JSON.parse(res.data.blockSizeCdsList)
          this.resultList = JSON.parse(res.data.result)
          this.columns = [].concat(...getStatColumn('blockSize', this.svcList))
        }
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
