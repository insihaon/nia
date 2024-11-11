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
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
        :prop-on-select="handleClickTableCheck"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            사용자 권한정보 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round :disabled="selectedChecks.length > 0" @click="fnViewInsertUserAuth('','I')">등록</el-button>
            <el-button icon="el-icon-delete" type="primary" size="mini" round @click="fnDeleteUserAuth()">삭제</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalMngUserAuth ref="ModalMngUserAuth" @reload="fnViewListUserAuth" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalMngUserAuth from '@/views-ipms/modal/grantmgmt/ModalMngUserAuth.vue'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import { onMessagePopup } from '@/utils/index'

const routeName = 'UserAuthManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalMngUserAuth },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      requestParameter: {},
      tableColumns: [
        { prop: 'suserNm', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sposDeptFullNm', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserGradeNm', label: '권한등급', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'tbLvlBasVo.ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'tbLvlBasVo.ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'tbLvlBasVo.ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        { key: 'AuthLevel', props: { } },
        {
          key: 'InputSearchDetail',
          props: {
            label: '소속조직',
            modalName: 'ModalOrgSearch',
            valueName: 'sFullOrgNm',
            prop_parameterKey: { sposDeptOrgId: 'sktOrgId', sposDeptOrgNm: 'sFullOrgNm' },
            isReadOnly: true
          }
        },
        { key: 'InputType', props: { label: '사용자', prop_parameterKey: 'suserNm' } },
      ],
      selectedChecks: [],
      selectedChecksItem: {}
    }
  },
  mounted() {
    setTimeout(() => {
      this.fnViewListUserAuth()
    }, 100)
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListUserAuth(requestParameter)
    },
    async fnViewListUserAuth(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const rankKeys = ['ssvcLineTypeCd', 'ssvcGroupCd', 'ssvcObjCd']
      const tbLvlBasVo = {}
      rankKeys.forEach(key => {
        Object.assign(tbLvlBasVo, { [key]: parameter[key] })
        delete parameter[key]
      })

      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { tbLvlBasVo }, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListUserAuth, parameter)
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
      this.fnViewListUserAuth()
    },
     handleClickTableCheck(all, cur) {
      this.selectedChecksItem = cur
      this.selectedChecks = all
    },
    onClcikRow(row) {
      if (row.suserId === '' || row.suserId === null) {
        return
      }
      this.fnViewInsertUserAuth(row, 'U')
    },
    async fnViewInsertUserAuth(row, type) {
      this.$refs.ModalMngUserAuth.open({ row, type })
    },
    async fnDeleteUserAuth() { /* 삭제 */
      const tbUserAuthListVo = { tbUserAuthTxnVos: [] }
      this.selectedChecks.forEach(row => {
        tbUserAuthListVo.tbUserAuthTxnVos.push({ nuserAutSeq: row.nuserAutSeq })
      })
      try {
        const res = await apiRequestJson(ipmsJsonApis.deleteUserAuthTxn, tbUserAuthListVo)
        if (res.commonMsg === 'SUCCESS') {
          onMessagePopup(this, '사용자 권한이 정상적으로 삭제 되었습니다.')
          this.fnViewListUserAuth()
          this.selectedChecks = []
        } else {
          onMessagePopup(this, res.commonMsg)
        }
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
