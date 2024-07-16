<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
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
    <ModalNotAssignDetail ref="ModalNotAssignDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalNotAssignDetail from '@/views-ipms/modal/ModalNotAssignDetail.vue'
const routeName = 'IpunAllocatedStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalNotAssignDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableDatas: [
        { mang: 'KORNET', org: '------', notAssign: 65801, assign: 1015 }
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 2 } },
      ]
    }
  },
  computed: {
    tableColumns() {
      const _THIS = this
      return [
        { prop: 'mang', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'org', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'notAssign', label: '미배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
        formatter: (row, col, value, index) => {
            if (row.notAssign > 0) {
              return this.$createElement('el-button', {
                on: { click: () => {
                  this.$refs.ModalNotAssignDetail.open({ row })
              } } }, row.notAssign)
            }
          }
        },
        { prop: 'assign', label: '예비배정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            if (row.assign > 0) {
              return this.$createElement('el-button', {
                on: { click: () => {
                  this.$refs.ModalNotAssignDetail.open({ row })
              } } }, row.assign)
            }
          }
        }
      ]
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    },
  }
}
</script>
