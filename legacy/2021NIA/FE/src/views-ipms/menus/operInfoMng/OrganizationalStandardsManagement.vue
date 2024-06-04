<template>
  <!-- content -->
  <el-row style="width: 100%; padding-top: 10px">
    <el-col :span="24">
      조직기준 관리
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'OrganizationalStandardsManagement'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '노드국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '오더번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '접수일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '희망일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '고객명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상품명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '이용목적', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
