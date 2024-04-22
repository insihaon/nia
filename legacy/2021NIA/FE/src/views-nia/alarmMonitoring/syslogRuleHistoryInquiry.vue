<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="trafficAnalysis"
      :ag-grid="authAgGrid"
      :is-button-slot="false"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
// import { apiSelectAuthHistList, apiUpdateApiAuth, apiUpdateApiAuthProc } from '@/api/dataHub'
import { AppOptions } from '@/class/appOptions'

const routeName = 'syslogRuleHistoryInquiry'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectedRow: [],
       authData: [
        {
          model_name: '상황방명',
          key: 1,
          start_date: '2022-09-06',
          end_date: '2022-09-07',
        },
        {
          model_name: '상황',
          key: 2,
          start_date: '2022-09-06',
          end_date: '2022-09-07',
        },
       ],
      searchItems: [
        { label: 'Filter', type: 'select', multiple: true, placeholder: '티켓 종류를 선택하세요', model: 'ticket', setting: { allOption: { toggle: true } },
          options:
          [
            { label: '신청', value: 'APPLY' },
            { label: '승인', value: 'GRANT' },
            { label: '반려', value: 'REJECT' },
          ],
        },
        { label: '상태', type: 'select', multiple: true, placeholder: '경보 상태를 선택하세요', model: 'status_cd', setting: { allOption: { toggle: true } },
          options:
          [
            { label: '신청', value: 'APPLY' },
            { label: '승인', value: 'GRANT' },
            { label: '반려', value: 'REJECT' },
          ],
        },
        { label: '요청시간', type: 'date', model: 'create_time' },
        { label: '만료시간', type: 'date', model: 'expird_date' },
      ],
      searchModel: {
        api_name: '',
        status_cd: [],
        create_time: [],
        expird_date: [],
      },
      sortInfo: {}
    }
  },

  computed: {
    authAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'model_name', name: 'API명', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: 'start_date', name: '상태', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: false },
        { type: '', prop: 'end_date', name: '사용 용도', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.authData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadtrafficList()
  },
  methods: {
    onSortedChange(param) {
       this.sortInfo = []
       this.onLoadtrafficList()
    },
    // onClickSearchAuth(params) {
    //   this.onLoadtrafficList(params)
    // },
    async onLoadtrafficList(params) {
      const target = { vue: this.$refs.authManagement }
      this.openLoading(target)
      const defaultDate = null
      const param = {
        api_name: this.searchModel.api_name,
        status_cd: this.searchModel.status_cd,
        start_create_time: this.formatterDateTime(null, null, this.searchModel.create_time[0] ? this.searchModel.create_time[0] : defaultDate),
        end_create_time: this.formatterDateTime(null, null, this.searchModel.create_time[1] ? this.searchModel.create_time[1] : defaultDate),
        start_expird_date: this.formatterDateTime(null, null, this.searchModel.expird_date[0] ? this.searchModel.expird_date[0] : defaultDate),
        end_expird_date: this.formatterDateTime(null, null, this.searchModel.expird_date[1] ? this.searchModel.expird_date[1] : defaultDate),
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
        sort_column_name: this.sortInfo.colId,
        sort_type: this.sortInfo.sort
      }
      try {
        const res = ''/* await apiSelectAuthHistList(param) */
        this.authData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

    // handleOpenModalDetail(type, row) {
    //   this.$refs.modalApiDetail.open({ type, row })
    // },
  },
}
</script>
l
