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
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            라우팅 연동 이력 조회결과
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
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'RoutingLinkHistStatus'

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
        { prop: 'ptelnetIp', label: '장비IP', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'shostNm', label: '장비프롬프트명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sprocessYn', label: '진행여부', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sresultMsg', label: '결과메세지', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'screateId', label: '요청ID', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'dcreateDt', label: '요청일시', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => this.moment(row.dcreateDt).format('YYYY-MM-DD HH:mm:ss') },
        { prop: 'smodifyId', label: '완료ID', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '완료일시', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => this.moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') },
      ],

      sresultMsgOptions: [],
    }
  },
  computed: {
    componentList() {
      return [
        { key: 'InputType', props: { label: '장비 IP', valueType: 'ip', prop_parameterKey: 'ptelnetIp' } },
        { key: 'InputType', props: { label: '장비프롬프트명', prop_parameterKey: 'shostNm' } },
        { key: 'UsageYN', props: { label: '진행여부', prop_parameterKey: 'sprocessYn',
          defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'Y', value: 'Y' },
              { label: 'N', value: 'N' },
            ]
          }
        },
        { key: 'DateRange', props: { label: '요청/완료일시' } },
        { key: 'ApplyStatus', props: { label: '결과메시지', prop_parameterKey: 'sresultMsg', prop_options: this.sresultMsgOptions } },
      ]
    }
  },
  mounted () {
    this.fnSelectSresultMsg()
    this.fnViewListRoutHistMst()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListRoutHistMst(requestParameter)
    },
    async fnSelectSresultMsg() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSresultMsg, {})
        const options = res.result.data.map(v => { return { label: v.sresult_msg, value: v.sresult_msg } })
        options.unshift({ label: '전체', value: '' })
        this.$set(this, 'sresultMsgOptions', options)
      } catch (error) {
        this.error(error)
      }
    },
    async fnViewListRoutHistMst(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListRoutHistMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListRoutHistMst()
    },
  }
}
</script>
<style lang="css" scoped>
</style>
