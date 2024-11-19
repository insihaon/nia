<template>

  <el-row class="w-100 h-100">
    <div class="content_result my-2">
      <table class="tbl_data">
        <tbody>
          <tr class="top last">
            <template v-for="(item, index) in tableItems">
              <th :key="'th-' + index">{{ item.title }}</th>
              <td :key="'td-' + index">{{ item.value }} 건</td>
            </template>
          </tr>
        </tbody>
      </table>
    </div>
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-column="tableColumns"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-dbl-click="hadleClickWhoisEdit"
        :prop-on-select="handleClickTableCheck"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @savedExcel="handleClickExcelDownloadBtn"
      >
        <template slot="text-description">
          <span>
            조회 결과
          </span>
        </template>
        <template slot="add-features">
          <div class="add-features">
            <el-button type="primary" size="mini" icon="el-icon-s-tools" round @click="fnVieListWhoisKeywordMst1()">이용기관 관리</el-button>
            <el-button type="primary" size="mini" icon="el-icon-s-tools" round @click="fnVieListWhoisKeywordMst2()">대체 키워드 관리</el-button>
            <el-button type="primary" size="mini" icon="el-icon-s-tools" round @click="fnViewUpdateKtInfo()">KT 대체 정보 관리</el-button>
            <el-button type="primary" size="mini" icon="el-icon-s-tools" round @click="fnDbMatch()">DB 현행화 전송</el-button>
            <el-button type="primary" size="mini" icon="el-icon-delete" round @click="fnDeleteListWhois()">삭제</el-button>
          </div>
        </template>
      </compTable>
      <ModalRegWhois ref="ModalRegWhois" />
      <ModalViewListWhoisKeywordMst ref="ModalViewListWhoisKeywordMst" />
      <ModalViewListWhoisKeywordMstNew ref="ModalViewListWhoisKeywordMstNew" />
      <ModalViewUpdateKtInfo ref="ModalViewUpdateKtInfo" />
      <ModalViewListWhoisDbMatchMst ref="ModalViewListWhoisDbMatchMst" />
    </el-col>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalRegWhois from '@/views-ipms/modal/whois/ModalRegWhois.vue'
import { ipmsModelApis, apiRequestModel, apiRequestJson, ipmsJsonApis } from '@/api/ipms'
import ModalViewListWhoisKeywordMst from '@/views-ipms/modal/whoismgmt/ModalViewListWhoisKeywordMst.vue'
import ModalViewListWhoisKeywordMstNew from '@/views-ipms/modal/whoismgmt/ModalViewListWhoisKeywordMstNew.vue'
import ModalViewUpdateKtInfo from '@/views-ipms/modal/whoismgmt/ModalViewUpdateKtInfo.vue'
import ModalViewListWhoisDbMatchMst from '@/views-ipms/modal/whoismgmt/ModalViewListWhoisDbMatchMst.vue'
import { downloadExcel } from '@/views-ipms/js/common-function'

const routeName = 'WhoisInfoOpenManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalRegWhois, ModalViewListWhoisKeywordMst, ModalViewListWhoisKeywordMstNew, ModalViewUpdateKtInfo, ModalViewListWhoisDbMatchMst },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
        tableColumns: [
          { prop: 'rowNo', label: 'No', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'sfirstAddr', label: '시작 IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'slastAddr', label: '마지막 IP', align: 'center', columnVisible: true, showOverflow: true },
          { label: '노드', align: 'center', columnVisible: true, showOverflow: true, formatter: (row, col, value, index) => this.formatNode(row) },
          { prop: 'sassignTypeNm', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'sorgname', label: '기관명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'snetNm', label: '네트워크이름', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'swhoisRequestTypeNm', label: '작업종류', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'dmodifyDt', label: '변경일시', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
          { prop: 'swhoisTranStatusNm', label: '등록현황', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'stransKind', label: '입력구분', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'SsvcLineType', props: { label: '계위 정보', lvl: 3 } },
        { key: 'InputType', props: { label: '사용기관명', prop_parameterKey: 'sorgname' } },
         {
          key: 'ApplyStatus', props: {
          label: '등록현황',
          prop_parameterKey: 'swhoisresultCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: 'WHOIS 전송 대기(APP-Kmic간 통신오류)', value: '01' },
              { label: 'WHOIS 전송(Kmic-Whois 간 통신오류)', value: '02' },
              { label: 'WHOIS 반송', value: '03' },
              { label: 'WHOIS 등록완료', value: '04' },
              { label: 'WHOIS 전송완료(Whois 응답 수신오류)', value: '05' },
            ]
          }
        },
        { key: 'ServiceOrg', props: { isMulti: false, sortTypeDefaultVal: 'ALL' } },
        { key: 'InputType', props: { label: 'IP 주소', prop_parameterKey: 'searchWrd' } },
        {
          key: 'ApplyStatus', props: {
          label: '작업종류',
          prop_parameterKey: 'srequestCd',
          prop_options: [
              { label: '전체', value: '' },
              { label: '신규 신청서', value: 'RES001' },
              { label: '추가 신청서', value: 'RES002' },
              { label: '삭제 신청서', value: 'RES003' },
              { label: '변경 신청서', value: 'RES004' },
            ]
          }
        },
        { key: 'DateRange', props: { label: '변경일' } },
        { key: 'ApplyStatus', props: { label: '입력구분',
          prop_parameterKey: 'stransKind',
          prop_options: [
              { label: '전체', value: '' },
              { label: '시스템 입력', value: 'SYSTEM' },
              { label: '관리자 입력', value: 'ADMIN' },
              { label: '사용자 입력', value: 'USER' },
            ]
          }
        }
      ],
      tableItems: [
        { title: '전송대기', value: '275' },
        { title: '전송', value: '13' },
        { title: '전송완료', value: '0' },
        { title: '반송', value: '9' },
        { title: '등록완료', value: '287091' }
      ],
      selectedChecks: [],
      selectedChecksItem: null,
    }
  },
  mounted() {
    this.fnViewListWhois()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListWhois(requestParameter)
    },
    async fnViewListWhois(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListWhois, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    formatNode(row) {
     return `${row.ssvcLineTypeNm || ''} - ${row.ssvcGroupNm || ''} - ${row.ssvcObjNm || ''}`
    },
    hadleClickWhoisEdit(row) {
      this.fnViewRegWhois(row)
    },
    fnViewRegWhois(row) {
      this.$refs.ModalRegWhois.open({ row: row })
    },
    handleClickTableCheck(all, cur) {
      this.selectedChecksItem = cur
      this.selectedChecks = all
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListWhois()
    },
    fnVieListWhoisKeywordMst1() { /* 이용기관 관리  */
      this.$refs.ModalViewListWhoisKeywordMst.open()
    },
    fnVieListWhoisKeywordMst2() { /* 대채 키워드 관리  */
      this.$refs.ModalViewListWhoisKeywordMstNew.open()
    },
    fnViewUpdateKtInfo() { /* KT 대체 정보 관리 */
      this.$refs.ModalViewUpdateKtInfo.open()
    },
    fnDbMatch() { /* DB 현행화 전송 */
      this.$refs.ModalViewListWhoisDbMatchMst.open()
    },
    fnDeleteListWhois() {
      this.$confirm('삭제 하시겠습니까?', 'WHO IS 정보 삭제', {
        confirmButtonText: '확인',
        cancelButtonText: '취소'
      }).then(async() => {
        let res
        try {
          const TbRequestAssignMstVo = {
            nrequestAssignSeq: this.resultVo.nrequestAssignSeq,
          }
           res = await apiRequestJson(ipmsJsonApis.deleteAssignApyTxn, TbRequestAssignMstVo)
           if (res.commonMsg === 'SUCCESS') {
            this.$message.success('배정신청을 정상적으로 삭제 하였습니다.')
            this.$emit('reload')
            this.close()
            }
          } catch (error) {
            this.$message.error({ message: `${res.commonMsg}` })
            console.log(error)
          }
        })
    },
    handleClickExcelDownloadBtn() {
      downloadExcel(this, 'viewListWhoisExcel')
    }
  }
}
</script>
<style lang="css" scoped>
</style>
