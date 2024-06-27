<template>
  <!-- content -->
  <el-row class="w-100 h-100 px-0">
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-is-pgination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'NewCertificationOptimizationIP'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        {
          prop: '',
          label: '회선정보',
          children: [
            { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: 'SER', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: 'L3', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '시설명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '시설 대표 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '',
          label: '수집 정보(평균)',
          children: [
            { prop: '', label: '블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: 'IP수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '최대할당율', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '최소할당율', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '',
          label: '상태',
          children: [
            { prop: '', label: '상한경보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '하한경보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '관리', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
