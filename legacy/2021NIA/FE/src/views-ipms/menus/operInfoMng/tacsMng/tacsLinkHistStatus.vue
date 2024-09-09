<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="fnViewListTacsConnHist"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        :prop-highlight-cell="onCheckSavailYn"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
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
      tableColumns: [
          { prop: 'pipFcltInet', label: '장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'sfcltPromptNm', label: '장비프롬프트명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'pipPrefix', label: '조회IP블럭', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'savailYn', label: 'IP중복여부', align: 'center', columnVisible: true, showOverflow: true,
            formatter: (row) => {
              if (row?.savailYn === '' || row?.savailYn === null) {
                return ''
              } else {
                return row.savailYn === 'Y' ? '중복' : '중복아님'
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
        { key: 'InputType', props: { label: '조회IP블럭', prop_parameterKey: 'pipPrifix' } },
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
    this.fnViewListTacsConnHist()
  },
  methods: {
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
    async fnViewListTacsConnHist(requestParameter) {
      const params = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListTacsConnHist, params)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      }
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
    color: red !important;
  }
}
.highlight_blue_color {
  .cell {
    color: blue !important;
  }
}
</style>
