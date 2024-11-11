<template>
  <el-row ref="container" class="w-100 h-100">
    <div class="searchOptionWrap">
      <table>
        <tr>
          <AuthLevel
            v-model="requestParameter.suserGradeCd"
            label="사용자 권한등급"
            class="w-100 d-flex"
            @update-value="setParameterKey"
          />
          <InputSearchDetail
            ref="searchDetail"
            v-model="requestParameter.orgValue"
            label="소속조직"
            modal-name="ModalOrgSearch"
            value-name="sFullOrgNm"
            :prop_parameter-key="{ sposDeptOrgId: 'sktOrgId', sposDeptOrgNm: 'sFullOrgNm' }"
            :is-read-only="true"
            class="w-100 d-flex"
            @update-value="setParameterKey"
          />
          <th>
            <label>재직상태</label>
          </th>
          <td>
            <el-select
              v-model="requestParameter.suserSttusCd"
              size="mini"
            >
              <el-option
                v-for="(option, i) in officeStatus"
                :key="i"
                :label="option.label"
                :value="option.value"
              />
            </el-select>
          </td>
          <th>
            <label>사용자명</label>
          </th>
          <td>
            <el-input v-model="requestParameter.suserNm" size="mini" clearable />
          </td>
        </tr>
        <tr>
          <td colspan="8">
            <div class="searchBtnWrap">
              <el-button type="info" icon="el-icon-refresh" size="mini" round @click="handleRefresh()">
                초기화
              </el-button>
              <el-button type="primary" icon="el-icon-search" size="mini" round @click="handleSearch()">
                조회
              </el-button>
            </div>
          </td>
        </tr>
      </table>
    </div>
    <el-col style="height: calc(100% - 160px);" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            사용자 정보 조회결과
          </span>
        </template>
      </compTable>
      <ModalDetailTbUserBas ref="ModalDetailTbUserBas" @reload="fnViewListSvcLineType" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import CompTable from '@/components/elTable/CompTable.vue'
import InputSearchDetail from '@/views-ipms/conditionComponents/InputSearchDetail.vue'
import AuthLevel from '@/views-ipms/conditionComponents/AuthLevel.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalDetailTbUserBas from '@/views-ipms/modal/usermgmt/ModalDetailTbUserBas.vue'
const routeName = 'UserInfoManagement'

export default {
  name: routeName,
  components: { CompTable, InputSearchDetail, AuthLevel, ModalDetailTbUserBas },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'suserNm', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sposDeptFullNm', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserSttusNm', label: '재직상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserGradeNm', label: '권한등급', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              attrs: {
                round: true, // Adding the round option
                plain: true,
                type: 'primary',
                size: 'mini'
              },
              on: { click: () => {
                this.fnViewUpdateUserBas(row)
              } } }, '수정')
            }
        },
      ],
      officeStatus: [
        { label: '전체', value: '' },
        { label: '미분류', value: 'US0000' },
        { label: '재직', value: 'US0001' },
        { label: '퇴직', value: 'US0002' },
        { label: '휴직', value: 'US0003' },
        { label: '유급휴가', value: 'US0004' },
      ],
      requestParameter: {
        suserGradeCd: '',
        orgValue: '',
        suserSttusCd: '',
        suserNm: '',
      },
    }
  },
  mounted() {
   this.fnViewListSvcLineType()
  },
  methods: {
    handleRefresh() {
      Eventbus.$emit(EventType.resetCondition)
      this.requestParameter = {
        suserGradeCd: '',
        orgValue: '',
        suserSttusCd: '',
        suserNm: '',
      }
      this.fnViewListSvcLineType()
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.fnViewListSvcLineType()
    },
    async fnViewListSvcLineType() {
      const parameter = this._cloneDeep(this.requestParameter)
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListTbUserBas, parameter)
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
      this.fnViewListSvcLineType()
    },
    setParameterKey(params) {
      params.forEach(item => { this.requestParameter[item.key] = item.value })
    },
    async fnViewUpdateUserBas(row) {
      this.$refs.ModalDetailTbUserBas.open({ row: row })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
