<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="authManagement"
      :ag-grid="authAgGrid"
      :is-button-slot="true"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @handleClickSearch="onClickSearchAuth"
      @selectedRow="selectedItems"
      @keyupEnter="onClickSearchAuth"
      @searchClear="handleDataSearchClear"
      @sortedChange="onSortedChange"
    >
      <template slot="button-container">
        <div class="button-panel my-1">
          <el-button type="danger" size="mini" plain :disabled="!btnApply || !hasNoAddGrant" @click.native="rejectAuth('reject')">선택 반려</el-button>
          <el-button type="primary" class="float-right common-btn" size="mini" plain :disabled="!btnApply || selectedRow.length < 1 || !hasNoAddGrant " @click.native="handleAuthApply('grant')">선택 승인</el-button>
        </div>
      </template>

      <template #inquireButton>
        <span> API 서비스에 대한 사용신청이 가능합니다</span>
      </template>
    </DataHubComponent>
    <ModalApiDetail ref="modalApiDetail" :fullscreen="isViewport('<', 'sm')" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import ModalApiDetail from '@/views-dataHub/modal/ModalApiDetail'
import CellRenderButtons from '@/views-dataHub/components/cellRenderer/CellRenderButtons'
import CellRenderAPIbuttons from '@/views-dataHub/components/cellRenderer/CellRenderAPIbuttons'
import { apiSelectAuthHistList, apiUpdateApiAuth, apiUpdateApiAuthProc } from '@/api/dataHub'
import hyperLinkTextRender from '@/views-dataHub/layout/components/cellRenderer/hyperLinkTextRender'
import { mapState } from 'vuex'
import { AppOptions } from '@/class/appOptions'

const routeName = 'AuthManagement'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { DataHubComponent, CellRenderButtons, CellRenderAPIbuttons, ModalApiDetail, hyperLinkTextRender },
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
      authData: [],
      searchItems: [
        { label: 'API명', type: 'input', model: 'api_name', placeholder: 'API명을 검색하세요' },
        { label: '상태', type: 'select', multiple: true, placeholder: '상태를 선택하세요', model: 'status_cd', setting: { allOption: { toggle: true } },
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
    ...mapState({
      blackDtlList: (state) => state.user.blackDtlList
    }),
    hasNoAddGrant() {
      return AppOptions.instance.isGod || this.blackDtlList.includes('btnAdd')
    },
    btnApply() {
      return this.selectedRow[0]?.status_cd === this.CONSTANTS.authManagement.APPLY.label
    },
    authAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: true, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'api_name', name: 'API명', minWidth: 30, flex: 0, suppressMenu: true, alignItems: 'left',
         cellRendererFramework: 'hyperLinkTextRender', cellRendererParams: { type: 'detail', action: this.handleOpenModalDetail.bind(this) } },
        { type: '', prop: 'status_cd', name: '상태', minWidth: 40, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'api_desc', name: '사용 용도', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'exec_mode_cd', name: '연동 방식', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: 'create_time', name: '요청 시간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false, cellRendererFramework: 'stateRender' },
        { type: '', prop: 'update_time', name: '업데이트 시간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false },
        { type: '', prop: 'expird_date', name: '사용만료 시간', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: true },
        { type: '', prop: '_', name: '기능', minWidth: 50, flex: 0, suppressMenu: true, alignItems: 'left', sortable: false, cellRendererFramework: 'CellRenderAPIbuttons', cellRendererParams: { type: 'api', key: 'revoke', action: this.openPopover.bind(this) } },
      ]
    if (this.appOptions.debug) {
      columns.unshift({ type: '', prop: 'api_id', name: 'API_ID(degubMode 전용)', minWidth: 20, flex: 0, suppressMenu: true, alignItems: 'left', sortable: true, filterable: false })
    }

      return { options, columns, data: this.authData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadHistAuthList()
  },
  methods: {
    onSortedChange(param) {
       this.sortInfo = []
       this.onLoadHistAuthList()
      },
    handleDataSearchClear() {
      this.searchModel.api_name = ''
      this.searchModel.status_cd = []
      this.searchModel.create_time = []
      this.searchModel.expird_date = []
      this.onLoadHistAuthList()
    },
    openPopover(data, type) {
      if (type === 'api') {
        this.visible = !this.visible
      } else {
        this.handleAuth(data, type)
      }
    },
    onClickSearchAuth(params) {
      this.onLoadHistAuthList(params)
    },
    async onLoadHistAuthList(params) {
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
        const res = await apiSelectAuthHistList(param)
        this.authData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    selectedItems(param) {
      this.selectedRow = param
    },
    async handleAuth(data, type) {
        const confirmMessage = `요청된 권한을 회수하시겠습니까?`
        const successMessage = `권한 회수 되었습니다.`
        const errorMessage = `권한 회수에 실패했습니다.`

        this.$confirm(confirmMessage, `API 권한 회수`, {
          confirmButtonText: '예',
          cancelButtonText: '아니오',
          type: 'success'
        }).then(async () => {
                const param = {
                  api_id: data.api_id,
                  api_name: data.api_name,
                  api_url: data.api_url,
                  system_id: data.system_id,
                  hist_serial: data.hist_serial,
                  status_cd: 'REVOKE'
              }
          try {
                const res = await apiUpdateApiAuthProc(param)
                if (res.result) {
                  this.$message.success({ message: successMessage })
                }
          } catch (error) {
            this.$message.error({ message: errorMessage })
            console.log(error)
          } finally {
            this.onLoadHistAuthList()
          }
        })
    },
    async handleAuthApply() {
    const confirmMessage = `요청된 권한을 승인하시겠습니까?`
    const successMessage = `권한 승인 되었습니다.`
    const errorMessage = `권한 승인에 실패했습니다.`

    this.$confirm(confirmMessage, `API 권한 승인`, {
      confirmButtonText: '예',
      cancelButtonText: '아니오',
      type: 'success'
    }).then(async () => {
      try {
        for (const item of this.selectedRow) {
          const param = {
            api_id: item.api_id,
            api_name: item.api_name,
            api_url: item.api_url,
            system_id: item.system_id,
            hist_serial: item.hist_serial,
            status_cd: 'GRANT'
          }
          const res = await apiUpdateApiAuthProc(param)
          if (res.result) {
            this.$message.success({ message: successMessage })
          }
        }
        this.onLoadHistAuthList() // 모든 API 호출이 완료된 후에 호출
      } catch (error) {
        this.$message.error({ message: errorMessage })
        console.log(error)
      }
    })
  },

    refreshData() {
      this.selectedRow = []
    },
    rejectAuth(key) {
    const rejectCd = 'REJECT'
    this.$prompt("내용을 입력하고 '확인'을 선택하면 반려 작업을 수행합니다.", 'API 권한', {
      confirmButtonText: '확인',
      cancelButtonText: '취소',
      inputPattern: '',
      inputErrorMessage: '값을 입력하세요.',
    })
    .then(async ({ value }) => {
      try {
        for (const item of this.selectedRow) {
        const params = {
          hist_serial: item?.hist_serial,
          status_cd: rejectCd,
          refuse_msg: value
        }
        const res = await apiUpdateApiAuth(params)
        if (res.result) {
          this.$message.success('반려 처리 되었습니다.')
          this.selectedCol = []
          this.onLoadHistAuthList()
        }
      }
      } catch (error) {
        this.$message.error('반려 처리에 실패했습니다.')
        console.error(error)
      }
    })
  },

    handleOpenModalDetail(type, row) {
      this.$refs.modalApiDetail.open({ type, row })
    },
  },
}
</script>
