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
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            조회 결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'WhoisInfoOpenManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: 'No', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '시작 IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '마지막 IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '기관명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '네트워크이름', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '작업종류', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '변경일시', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '등록현황', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '입력구분', align: 'center', columnVisible: true, showOverflow: true },
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
      ]
    }
  },
  methods: {
      handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  }
}
</script>
<style lang="css" scoped>
</style>
