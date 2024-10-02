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
      @handleClickSearch="onClickSearch"
      @onChangePage="onChangePage"
      @searchClear="searchClear"
    />

  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSelectUserList } from '@/api/nia'
import CellRenderSelectBox from '@/views-nia/components/cellRenderer/CellRenderSelectBox'
import { apiUpdateUserGrantList } from '@/api/nia'

const routeName = 'AuthSettings'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, CellRenderSelectBox },
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
      userData: [],
      searchItems: [
        { label: '이름 검색', type: 'input', multiple: false, placeholder: '', model: 'name', icon: 'el-icon-search' },
        { label: '권한 설정', type: 'select', multiple: true, model: 'lvl_value', icon: 'el-icon-setting',
          options:
          [
            { label: '사용자', value: '1' },
            { label: '담당자', value: '3' },
            { label: '관리자', value: '7' },
          ],
        },
      ],
      searchModel: {
        name: '',
        lvl_value: [],
      },
      paramAuth: []
    }
  },
  computed: {
    authAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 35, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'name', name: '이름', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'id', name: 'ID', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: false },
        { type: '', prop: 'agency_name', name: '분류', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'phone_number', name: 'PHONE_NUMBER', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'email', name: 'EMAIL', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'last_login', name: '마지막 접속시간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true },
        { type: '', prop: 'end_date', name: '권한선택', width: 200, suppressMenu: true, alignItems: 'center', sortable: false, filterable: true,
          cellRendererFramework: 'CellRenderSelectBox', cellRendererParams: { type: 'auth', name: '저장', action: this.setUserAuth.bind(this) } }

      ]
      return { options, columns, data: this.userData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadAuthList()
  },
  methods: {
     onClickSearch(params) {
      this.onLoadAuthList(params)
    },
     onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
    },
    async onLoadAuthList() {
     const { pageSize: limit, currentPage: page } = this.paginationInfo
       const param = {
        NAME: this.searchModel.name,
        LVL_VALUE: this.searchModel.lvl_value,
        pageSize: limit,
        currentPage: page
       }
      try {
        const res = await apiSelectUserList(param)
        this.userData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      }
    },
    searchClear() {
     this.searchModel = {}
    },
    setUserAuth(row, type, auth) {
      this.confirm('권한을 수정하시겠습니까?', '권한 수정', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'success',
      }).then(async () => {
        try {
          const lvl_value = auth.reduce((accum, current) => {
            return accum + current
          })
          const param = {
            id: row.id,
            name: row.name,
            lvl_value: lvl_value
          }
          const res = await apiUpdateUserGrantList(param)
          if (res.success) {
            this.$message('수정 되었습니다.')
            this.onLoadAuthList()
          }
        } catch (error) {
          this.$message.error({ message: `수정에 실패했습니다.` })
          console.error(error)
        }
      })
    },
    async autoTest() {
      const { assert, wait, onLoadAuthList } = this
      await onLoadAuthList()
      assert(this.userData.length > 0)
      await wait(1000)
    }
  },
}
</script>

