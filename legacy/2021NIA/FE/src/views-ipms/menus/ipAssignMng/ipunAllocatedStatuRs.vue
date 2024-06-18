<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      ref="DynamicComponent"
      :component-keys="componentList"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 미배정 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import CompInquiryPannel from '@/views-ipms/components/CompInquiryPannel.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
const routeName = 'IpunAllocatedStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '미배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '예비배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      tableDatas: [],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 2 } },
      ]
    }
  },
}
</script>
