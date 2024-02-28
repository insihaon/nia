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
      @cellClicked="cellTemp"
      @sortChanged="sortTemp"
    />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
// import { apiSelectAuthHistList, apiUpdateApiAuth, apiUpdateApiAuthProc } from '@/api/dataHub'
import { AppOptions } from '@/class/appOptions'

const routeName = 'TrafficAnalysisInstitution'
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
        { label: 'Ticket', type: 'select', multiple: true, placeholder: '티켓 종류를 선택하세요', model: 'ticket', icon: 'el-icon-setting', setting: { allOption: { toggle: true } },
          options:
          [
            { label: '장애', value: 'alarm' },
            { label: '광레벨', value: 'level' },
            { label: '이상트래픽', value: 'traffic1' },
            { label: '유해트래픽', value: 'traffic2' },
            { label: '장비부하장애', value: 'traffic3' },
          ],
        },
        { label: '상태', type: 'select', multiple: true, placeholder: '경보 상태를 선택하세요', model: 'status_cd', icon: 'el-icon-warning', setting: { allOption: { toggle: true } },
          options:
          [
            { label: '발생', value: 'OCCUR' },
            { label: '인지', value: 'RECOGNIZE' },
            { label: '마감', value: 'CLOSE' },
            { label: '자동 마감', value: 'CLOSE-A' },
          ],
        },
          { label: '장비명', type: 'input', multiple: true, placeholder: 'SEARCH', icon: 'el-icon-search', model: 'equipment' },
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
        { type: '', prop: 'model_name', name: 'Ticket', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'left' },
        { type: '', prop: 'start_date', name: '상태', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: false },
        { type: '', prop: 'end_date', name: '최초 발생 장애 시간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.authData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadtrafficList()
  },
  methods: {
    cellTemp() {},
    sortTemp() {},
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
