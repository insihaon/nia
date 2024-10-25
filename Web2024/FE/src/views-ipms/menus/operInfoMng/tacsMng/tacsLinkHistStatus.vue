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
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        :prop-highlight-cell="onCheckSavailYn"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            연동 이력 조회결과
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
const routeName = 'TacsLinkHistStatus'

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
          { prop: 'pipFcltInet', label: '장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'sfcltPromptNm', label: '장비프롬프트명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'pipPrefix', label: '조회IP블럭', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'savailYn', label: 'IP중복여부', align: 'center', columnVisible: true, showOverflow: true,
            formatter: (row) => {
              if (row?.savailYn === '' || row?.savailYn === null) {
                return ''
              } else {
                return row.savailYn === 'N' ? '중복' : '중복아님'
              }
            }
          },
          { prop: 'sresultMsg', label: '결과메세지', align: 'center', columnVisible: true, showOverflow: true, },
          { prop: 'smodifyId', label: '사용자ID', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'dcreateDt', label: '접속일시', align: 'center', columnVisible: true, showOverflow: true },
      ],
      tableDatas: [],
      sresultMsgOptions: [],
    }
  },
  computed: {
    componentList() {
      return [
        { key: 'InputType', props: { label: '장비 IP', prop_parameterKey: 'pipFcltInet' } },
        { key: 'InputType', props: { label: '장비프롬프트명', prop_parameterKey: 'sfcltPromptNm' } },
        { key: 'InputType', props: { label: '조회IP블럭', prop_parameterKey: 'pipPrefix' } },
        { key: 'ApplyStatus', props: { label: 'IP중복여부', prop_parameterKey: 'savailYn',
            prop_options: [
              { label: '전체', value: '' },
              { label: '중복 아님', value: 'Y' },
              { label: '중복', value: 'N' },
            ]
          }
        },
        { key: 'InputType', props: { label: '사용자ID', prop_parameterKey: 'screateId' } },
        { key: 'DateRange', props: { label: '접속일자' } },
        {
          key: 'ApplyStatus', props: {
            label: '결과메시지',
            prop_parameterKey: 'sresultMsg',
            prop_options: this.sresultMsgOptions
          }
        }
      ]
    }
  },
  mounted () {
    this.fnSelectSresultMsg()
    setTimeout(() => {
      this.fnViewListTacsConnHist()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListTacsConnHist(requestParameter)
    },
    async fnSelectSresultMsg() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectTacsSresultMsg, {})
        const options = res.result.data.map(v => { return { label: v.sresult_msg, value: v.sresult_msg } })
        options.unshift({ label: '전체', value: '' })
        this.$set(this, 'sresultMsgOptions', options)
      } catch (error) {
        this.error(error)
      }
    },
    async fnViewListTacsConnHist(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListTacsConnHist, parameter)
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
      this.fnViewListTacsConnHist()
    },
    onCheckSavailYn({ row, column, rowIndex, columnIndex }) {
      if (column.property === 'savailYn') {
        if (row.savailYn === 'Y') { // 중복아님
          return 'highlight_blue_color'
        } else if (row.savailYn === 'N') { // 중복
          return 'highlight_red_color'
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .highlight_red_color {
  .cell {
    font-weight: bold;
    color: #ff4646 !important;
  }
}
::v-deep  .highlight_blue_color {
  .cell {
    font-weight: bold;
    color: #009aff !important;
  }
}
</style>
