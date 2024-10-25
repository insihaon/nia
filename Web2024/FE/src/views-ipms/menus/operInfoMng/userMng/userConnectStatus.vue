<template>
  <el-row class="w-100 h-100">
    <div class="searchOptionWrap">
      <table>
        <th>
          <label>사용자 접속결과</label>
        </th>
        <td>
          <el-select
            v-model="connectValue"
            collapse-tags
            size="mini"
            class="w-100"
          >
            <el-option
              v-for="(option, i) in useConnResOp"
              :key="i"
              :label="option.label"
              :value="option.value"
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
          <el-input v-model="nameValue" size="mini" clearable />
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
            <el-button type="info" size="mini" icon="el-icon-refresh" round>
              초기화
            </el-button>
            <el-button type="primary" size="mini" icon="el-icon-search" round @click="fnViewListTbUserConnHist()">
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
      :prop-column="tableColumns"
      :prop-data="resultListVo"
      :prop-is-pagination="true"
      :prop-is-check-box="false"
      prop-grid-menu-id="inputSpeed"
      :prop-grid-indx="1"
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
import CompTable from '@/components/elTable/CompTable.vue'
import InputSearchDetail from '@/views-ipms/conditionComponents/InputSearchDetail.vue'
import moment from 'moment'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'UserConnectStatus'

export default {
  name: routeName,
  components: { CompTable, InputSearchDetail },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'suserId', label: '사용자ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserNm', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sposDeptOrgNm', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dloginDt', label: '로그인시간', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => moment(row.dmodifyDt).format('YYYY-MM-DD') },
        { prop: 'dlogoutDt', label: '로그아웃시간', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => moment(row.dmodifyDt).format('YYYY-MM-DD') },
        { prop: 'suserHndsetId', label: '접속 IP주소', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserConnResltTypeNm', label: '로그인결과', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      useConnResOp: [
        { label: '전체', value: '' },
        { label: '미분류', value: 'UC0000' },
        { label: '성공', value: 'UC0001' },
        { label: 'IP인증실패(단말변경 신청요망)', value: 'UC0002' },
      ],
      connectValue: '',
      nameValue: '',
      loginTime: [],
      requestParameter: {},
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListTbUserConnHist()
  },
  methods: {
    async fnViewListTbUserConnHist() {
      const [bgTime, endTime] = this.loginTime
      const params = {
        searchBgnDe: '',
        searchBgnHour: bgTime?.getHours() ?? '00',
        searchBgnMinute: bgTime?.getMinutes() ?? '00',
        searchEndDe: '',
        searchEndHour: endTime?.getHours() ?? '00',
        searchEndMinute: endTime?.getMinutes() ?? '00',
        searchWrd: this.nameValue,
        suserConnResultTypeCd: this.connectValue
      }
      Object.assign(this.requestParameter, params)
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListTbUserConnHist, this.requestParameter)
        this.resultListVo = res?.result?.data
      } catch (error) {
        console.error(error)
      }
    },
    setParameterKey(params) {
      params.forEach(item => { this.requestParameter[item.key] = item.value })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
