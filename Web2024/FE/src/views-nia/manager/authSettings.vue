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
import { } from '@/api/nia'

const routeName = 'AuthSettings'
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
      authData: [],
      searchItems: [
        { label: 'Top N', type: 'select', multiple: false, placeholder: '', model: 'rank_order', icon: 'el-icon-warning', setting: { allOption: { toggle: true } } },
        { label: '권한 설정', type: 'select', multiple: true, model: 'status_cd', setting: { allOption: { toggle: true } },
          options:
          [
            { label: '사용자', value: 'OCCUR' },
            { label: '담당자', value: 'RECOGNIZE' },
            { label: '관리자', value: 'CLOSE' },

          ],
        },
      ],
      searchModel: {
        api_name: '',
        status_cd: '',
      },
      sortInfo: {}
    }
  },

  computed: {
    authAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'model_name', name: 'name', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'start_date', name: 'ID', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'end_date', name: '분류', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'end_date', name: 'PHONE_NUMBER', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'end_date', name: 'EMAIL', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'end_date', name: '마지막 접속시간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'end_date', name: '권한선택', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: '', name: '', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
      ]
      return { options, columns, data: this.authData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadAuthList()
  },
  methods: {
    cellTemp() {},
    sortTemp() {},
    onSortedChange(param) {
       this.sortInfo = []
       this.onLoadAuthList()
    },

    // onClickSearchAuth(params) {
    //   this.onLoadAuthList(params)
    // },
    async onLoadAuthList() {
     const { pageSize: limit, currentPage: page } = this.paginationInfo
       const param = {
        src_protocol: this.searchModel.src_protocol,
        src_port: this.searchModel.src_port,
        dst_protocol: this.searchModel.dst_protocol,
        dst_port: this.searchModel.dst_port,
        // rank_order: this.searchModel?.rank_order,
        pageSize: limit,
        currentPage: page
       }
      try {
        const res = await ''
        this.trafficData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
       /*  */
      }
    },
  },
}
</script>
l
