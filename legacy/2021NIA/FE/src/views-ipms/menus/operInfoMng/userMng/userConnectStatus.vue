<template>
  <el-row class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="6">
          <label>사용자 접속결과</label>
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
        </el-col>
        <el-col class="d-flex" :span="6">
          <InputSearchDetail
            ref="searchDetail"
            label="소속조직"
            modal-name="ModalOrgSearch"
            value-name="sFullOrgNm"
            :prop_parameter-key="{ sposDeptOrgId: 'sktOrgId', sporEdptOrgNm: 'sFullOrgNm' }"
            :is-read-only="true"
            class="w-100 d-flex"
            @update-value="setParameterKey"
          />
        </el-col>
        <el-col class="d-flex" :span="6">
          <label>사용자명</label>
          <el-input v-model="nameValue" size="mini" clearable />
        </el-col>

        <el-col class="d-flex" :span="6">
          <label>로그인 시간</label>
          <el-date-picker
            v-model="loginTime"
            type="datetimerange"
            size="mini"
            start-placeholder="시작일"
            end-placeholder="종료일"
          />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" align="center" class="searchBtnGroup">
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="handleSearch()">
            조회
          </el-button>
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh">
            초기화
          </el-button>
          <slot name="add-function" />
        </el-col>
      </el-row>
    </div>
    <el-col :span="24" style="height: calc(100% - 160px)">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
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
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import InputSearchDetail from '@/views-ipms/conditionComponents/InputSearchDetail.vue'
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
        { prop: '', label: '사용자ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '로그인시간', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '로그아웃시간', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '접속 IP주소', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '로그인결과', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
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
      requestParameter: {}
    }
  },
  methods: {
    handleSearch() {
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
      console.log(this.requestParameter)
      /*
      const res = await api(this.requestParameter)
      */
    },
    setParameterKey(params) {
      params.forEach(item => { this.requestParameter[item.key] = item.value })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
