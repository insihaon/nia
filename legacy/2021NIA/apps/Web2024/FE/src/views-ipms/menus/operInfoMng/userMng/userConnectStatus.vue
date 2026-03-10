<template>
  <el-row class="w-100 h-100">
    <div class="searchOptionWrap">
      <table>
        <th>
          <label>사용자 접속결과</label>
        </th>
        <td>
          <el-select
            v-model="requestParameter.suserConnResultTypeCd"
            size="mini"
            class="w-100"
          >
            <el-option
              v-for="(option, i) in useConnResOp"
              :key="i"
              :label="option.name"
              :value="option.code"
            />
          </el-select>
        </td>
        <InputSearchDetail
          ref="searchDetail"
          label="소속조직"
          modal-name="ModalOrgSearch"
          value-name="sFullOrgNm"
          :prop_parameter-key="{ sposDeptOrgId: 'sktOrgId', sporDeptOrgNm: 'sFullOrgNm' }"
          :is-read-only="true"
          class="w-100 d-flex"
          @update-value="setParameterKey"
        />
        <th>
          <label>사용자명</label>
        </th>
        <td>
          <el-input v-model="requestParameter.searchWrd" size="mini" clearable />
        </td>
        <th>
          <label>로그인 시간</label>
        </th>
        <td>
          <el-date-picker
            v-model="loginTime"
            type="datetimerange"
            size="mini"
            start-placeholder="시작일"
            end-placeholder="종료일"
          />
        </td>
        <td colspan="8">
          <div class="searchBtnWrap">
            <el-button type="info" size="mini" icon="el-icon-refresh" round @click="handleRefresh()">
              초기화
            </el-button>
            <el-button type="primary" size="mini" icon="el-icon-search" round @click="handleSearch()">
              조회
            </el-button>
          </div>
        </td>
      </table>
    </div>
    <compTable
      ref="compTable"
      style="height: calc(100% - 120px)"
      :prop-name="name"
      :prop-table-height="'100%'"
      :prop-data="pagination.data"
      :prop-pagination-data.sync="pagination"
      :prop-column="tableColumns"
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
          사용자 접속현황 조회결과
        </span>
      </template>
    </compTable>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import Eventbus from '@/utils/event-bus'
import { EventType } from '@/min/types'
import CompTable from '@/components/elTable/CompTable.vue'
import InputSearchDetail from '@/views-ipms/conditionComponents/InputSearchDetail.vue'
import moment from 'moment'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'UserConnectStatus'

export default {
  name: routeName,
  components: { CompTable, InputSearchDetail },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'suserId', label: '사용자ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserNm', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sposDeptOrgNm', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dloginDt', label: '로그인시간', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => row.dloginDt ? moment(row.dloginDt).format('YYYY-MM-DD HH:mm:ss') : '' },
        { prop: 'dlogoutDt', label: '로그아웃시간', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => row.dlogoutDt ? moment(row.dlogoutDt).format('YYYY-MM-DD HH:mm:ss') : '' },
        { prop: 'suserHndsetId', label: '접속 IP주소', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserConnResltTypeNm', label: '로그인결과', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      useConnResOp: [
        { label: '전체', value: '' },
      ],
      requestParameter: {
        suserConnResultTypeCd: '',
        searchWrd: '',
      },
      loginTime: []
    }
  },
  mounted() {
    this.onLoadUserConnResultCd()
    this.fnViewListTbUserConnHist()
  },
  methods: {
    handleRefresh() {
      Eventbus.$emit(EventType.resetCondition)
      this.requestParameter = {
        suserConnResultTypeCd: '',
        searchWrd: '',
      }
      this.loginTime = []
      this.fnViewListTbUserConnHist()
    },
    async onLoadUserConnResultCd() {
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectListUserConnResultCds, {})
        this.useConnResOp = res.result.data
        this.useConnResOp.unshift({ name: '전체', code: '' })
      } catch (error) {
        this.error(error)
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.fnViewListTbUserConnHist()
    },
    async fnViewListTbUserConnHist() {
      const [bgTime, endTime] = this.loginTime
      const parameter = this._cloneDeep(this.requestParameter)
      if (bgTime && endTime) {
        Object.assign(parameter, {
          searchBgnDe: this.moment(bgTime).format('YYYY-MM-DD HH:mm'),
          searchBgnHour: bgTime?.getHours() ?? '00',
          searchBgnMinute: bgTime?.getMinutes() ?? '00',
          searchEndDe: this.moment(endTime).format('YYYY-MM-DD HH:mm:59'),
          searchEndHour: endTime?.getHours() ?? '00',
          searchEndMinute: endTime?.getMinutes() ?? '00',
        })
      }
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListTbUserConnHist, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListTbUserConnHist()
    },
    setParameterKey(params) {
      params.forEach(item => { this.requestParameter[item.key] = item.value })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
