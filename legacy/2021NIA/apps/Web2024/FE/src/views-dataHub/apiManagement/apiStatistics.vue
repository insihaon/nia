<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="selectApiStaistics"
      :ag-grid="selectStatisticsAgGrid"
      title="요청 API 통계"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfoSuccess"
      class=" w-100 h-50"
      @handleClickSearch="onClickSearchApiStatistics"
      @searchClear="handleApiStatisticsClear"
      @sortedChange="onSortedChangeSuccess"
    >
      <template #inquireButton>
        <span> API 요청 성공 통계</span>
      </template>
    </DataHubComponent>

    <DataHubComponent
      ref="selectApiAlarmStaistics"
      :ag-grid="selectAlarmStatisticsAgGrid"
      :pagination-info="paginationInfoFail"
      :is-search="false"
      title="요청 API 오류 통계"
      class=" w-100 h-50"
      @sortedChange="onSortedChangeFail"
    >
      <template #inquireButton>
        <span> API 요청 실패 통계</span>
      </template>
    </DataHubComponent>

  </div>
</template>

<script>

import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import { apiSelectInfoList, apiSelectSuccessCountList, apiSelectFailCountList, apiSelectApiList } from '@/api/dataHub'

const routeName = 'ApiStatistics'
export default {
  name: routeName,
  components: { DataHubComponent },
  extends: Base,

  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfoSuccess: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      paginationInfoFail: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectStatisticsData: [],
      selectAlarmStatisticsData: [],
      searchItems: [
        { label: '조회기간', type: 'date', size: 8, model: 'check_date' },
          { label: 'API명', type: 'select', size: 8, model: 'api_id', placeholder: 'API명을 검색하세요', multiple: false, setting: { allOption: { toggle: true } }, options: []
        },
      ],
      searchModel: {
        api_id: '',
        check_date: []
      },
      sortInfo: {},
      selectApiList: [],
    }
  },

  computed: {
    selectStatisticsAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
        }
        const columns = [
          { type: '', prop: 'create_time', name: '연동일자', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'api_name', name: 'API명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'exec_mode_cd', name: '연동방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'proc_count', name: '연동 건수', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left' },
          { type: '', prop: 'system_count', name: '연동 시스템 수', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },

        ]
         if (this.appOptions.debug) {
          columns.unshift({ type: '', prop: 'api_id', name: 'API_ID(degubMode 전용)', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false })
          columns.unshift({ type: '', prop: 'api_url', name: 'API_URL(degubMode 전용)', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false })
        }

        return { options, columns, data: this.selectStatisticsData, getRightClickMenuItems: () => { return [] } }
      },
    selectAlarmStatisticsAgGrid() {
        const options = {
          name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
        }
        const columns = [
          { type: '', prop: 'create_time', name: '연동일자', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'api_name', name: 'API명', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'exec_mode_cd', name: '연동방식', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
          { type: '', prop: 'proc_count', name: '연동 건수', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left' },
          { type: '', prop: 'system_count', name: '연동 시스템 수', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        ]

        if (this.appOptions.debug) {
          columns.unshift({ type: '', prop: 'api_id', name: 'API_ID(degubMode 전용)', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false })
        }

        return { options, columns, data: this.selectAlarmStatisticsData, getRightClickMenuItems: () => { return [] } }
      },
  },
  watch: {
  },
  mounted() {
    this.onLoadCountList()
    this.onLoadCountAlarmList()
    this.onLoadDataSetList()
  },
  methods: {
    async onLoadDataSetList() {
      const res = await apiSelectInfoList()
      this.dataSetList = res?.result

      this.selectDataSetList = res.result.map(item => ({ label: item.api_name, value: item.api_id }))
      const tableNmItem = this.searchItems.find(item => item.model === 'api_id')
      if (tableNmItem) {
        tableNmItem.options = this.selectDataSetList
      }
    },
    onSortedChangeSuccess(param) {
       this.sortInfo = param
       this.onLoadCountList()
      },
      onSortedChangeFail(param) {
       this.sortInfo = param
       this.onLoadCountAlarmList()
      },
    handleApiStatisticsClear() {
      this.searchModel.api_id = ''
      this.searchModel.check_date = []
      this.onClickSearchApiStatistics()
    },
    onClickSearchApiStatistics(params) {
      this.onLoadCountList(params)
      this.onLoadCountAlarmList(params)
    },
    async onLoadCountList(params) {
      const target = ({ vue: this.$refs.selectApiStaistics })
      this.openLoading(target)
      const defaultDate = null
      const start_time = this.searchModel.check_date && this.searchModel.check_date[0]
      ? this.formatterDateTime(null, null, this.searchModel.check_date[0])
      : defaultDate

      const end_time = this.searchModel.check_date && this.searchModel.check_date[1]
      ? this.formatterDateTime(null, null, this.searchModel.check_date[1])
      : defaultDate
      const param = {
        api_id: this.searchModel.api_id,
        start_date: start_time,
        end_date: end_time,
        limit: this.paginationInfoSuccess.pageSize,
        page: this.paginationInfoSuccess.currentPage,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
      }
        try {
          const res = await apiSelectSuccessCountList(param)
            this.selectStatisticsData = res?.result
            this.paginationInfoSuccess.totalCount = res.total
            this.paginationInfoSuccess.totalPages = Math.ceil(this.paginationInfoSuccess.totalCount / this.paginationInfoSuccess.pageSize) // 전체 페이지 수 계산
        } catch (error) {
            console.error(error)
        } finally {
          this.closeLoading(target)
        }
    },
    async onLoadCountAlarmList(params) {
      const target = ({ vue: this.$refs.selectApiAlarmStaistics })
      this.openLoading(target)
      const defaultDate = null
      const start_time = this.searchModel.check_date && this.searchModel.check_date[0]
      ? this.formatterDateTime(null, null, this.searchModel.check_date[0])
      : defaultDate

      const end_time = this.searchModel.check_date && this.searchModel.check_date[1]
      ? this.formatterDateTime(null, null, this.searchModel.check_date[1])
      : defaultDate
      const param = {
        api_id: this.searchModel.api_id,
        start_date: start_time,
        end_date: end_time,
        limit: this.paginationInfoFail.pageSize,
        page: this.paginationInfoFail.currentPage,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
      }
        try {
          const res = await apiSelectFailCountList(param)
          this.selectAlarmStatisticsData = res?.result
          this.paginationInfoFail.totalCount = res.total
          this.paginationInfoFail.totalPages = Math.ceil(this.paginationInfoFail.totalCount / this.paginationInfoFail.pageSize) // 전체 페이지 수 계산
        } catch (error) {
          console.error(error)
        } finally {
          this.closeLoading(target)
        }
    }
}

  }
</script>

<style lang="scss" scoped>
.apiStatistics {
  font-family: "NanumSquare";
}
</style>

