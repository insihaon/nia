<template>
  <el-row class="w-100 h-100" :class="{'px-12': !isDashboard}">
    <CompInquiryPannel
      ref="ipAssign"
      :items="searchItems"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 배정 조회결과
          </span>
        </template>
      </comptable></el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import CompInquiryPannel from '@/views-ipms/components/CompInquiryPannel.vue'
const routeName = 'IpAssign'

export default {
  name: routeName,
  components: { CompTable, CompInquiryPannel },
  extends: Base,
  props: {
    isDashboard: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '배정상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      tableDatas: [],
      searchItems: [
        { label: '계위', type: 'select', multiple: false, model: 'grade', options: [] },
        { label: '계위2', type: 'select', multiple: false, model: 'grade2', options: [] },
        { label: '계위3', type: 'select', multiple: false, model: 'grade3', options: [] },
        { label: '생성차수', type: 'select', multiple: false, model: 'src_ip',
          options: [
            { label: 'temp', value: 10 }
          ] },
        { label: 'IP주소', type: 'select', multiple: false, model: 'dst_nren_name', setting: { allOption: { toggle: true } }, options: [] },
        { label: '서비스망', type: 'select', multiple: false, model: 'top_n',
          options: [
            { label: '전체', value: 'ALL' },
            { label: '30', value: 30 },
            { label: '50', value: 50 },
            { label: '100', value: 100 },
          ] },
        { label: '작업일자', type: 'date', multiple: false, model: 'dst_ip' },
        { label: '정렬', type: 'select', multiple: false, model: 'dst_ip',
          options: [
            { label: 'IP', value: 'IP' },
            { label: 'BitMak', value: 'BitMak' },
            { label: '작업일자', value: 'workDay' },
          ] },
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
