<template>
  <el-row>
    <!-- DynamicComponentLoader  -->
    <el-col>
      <div>IP 블록 정보</div>
      <CompTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="ipBlockColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="ipInfoList"
        :prop-grid-indx="1"
      />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'IpInfoMultiList'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipBlockColumns: [
        { prop: '', label: '검색 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP 주소', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'BitMask', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상세', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
