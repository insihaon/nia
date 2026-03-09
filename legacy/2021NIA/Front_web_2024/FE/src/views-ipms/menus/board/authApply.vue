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
        :prop-column="tableColumns"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-dbl-click="fnViewDbClickUserAuth"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            사용자 권한 신청 정보 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewDetailGrant('', 'U')">등록</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalDetailUserAuth ref="ModalDetailUserAuth" @reload="fnViewListUserAuthSubs($refs.searchCondition.requestParameter)" />
    <ModalInsertUserAuth ref="ModalInsertUserAuth" @reload="fnViewListUserAuthSubs($refs.searchCondition.requestParameter)" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalDetailUserAuth from '@/views-ipms/modal/notice/ModalDetailUserAuth.vue'
import ModalInsertUserAuth from '@/views-ipms/modal/notice/ModalInsertUserAuth.vue'

const routeName = 'AuthApply'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalDetailUserAuth, ModalInsertUserAuth },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'grantSeq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserNm', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sposDeptFullNm', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dcreateDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => this.moment(row.dcreateDt).format('YYYY-MM-DD') },
        { prop: 'nrequestTypeNm', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'InputSearchDetail',
          props: {
            label: '소속조직',
            modalName: 'ModalOrgSearch',
            valueName: 'sorgNm',
            prop_parameterKey: { sposDeptOrgId: 'sktOrgId', sposDeptOrgNm: 'sorgNm' },
            isReadOnly: true
          }
        },
        { key: 'InputType', props: { label: '사용자', prop_parameterKey: 'suserNm' } },
        { key: 'ApplyStatus', props: { label: '진행상태', prop_parameterKey: 'nrequestTypeCd' } },
      ],
    }
  },
  mounted() {
    this.fnViewListUserAuthSubs()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListUserAuthSubs(requestParameter)
    },
    async fnViewListUserAuthSubs(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListUserAuthSubs, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListUserAuthSubs()
    },
    fnViewDbClickUserAuth(row) {
      this.fnViewDetailGrant(row, 'detail')
    },
    async fnViewDetailGrant(row, type) {
      if (type === 'detail') {
        this.$refs.ModalDetailUserAuth.open({ row: row, type: type })
      } else {
        this.$refs.ModalInsertUserAuth.open({ type: type })
      }
    }
  },
}
</script>

