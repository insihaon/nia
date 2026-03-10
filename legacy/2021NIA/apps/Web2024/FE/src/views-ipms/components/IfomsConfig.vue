<template>
  <!-- content -->
  <el-row class="w-100 h-100 px-0">
    <el-col :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
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
const routeName = 'IfomsConfig'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '장비 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'I/F IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Description', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '감사', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
}
</script>
<style lang="scss" scoped></style>
