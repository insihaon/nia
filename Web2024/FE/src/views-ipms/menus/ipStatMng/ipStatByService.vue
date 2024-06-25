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
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 서비스별 통계 현황 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'ipStatByService'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 1, multi: [1], limit: { 1: 5, 2: null, 3: null } } },
        { key: 'IpAddress', props: { label: 'IP 버전', isShowInput: false } },
        { key: 'SipCreateType', props: {} },
        { key: 'DatePicker', props: {} },
      ],
      tableColumns: [
        {
          prop: '', label: '', children: [
            { prop: '', label: '공인/사설', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '전체', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: 'KOREAN', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: 'PREMINUM', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: 'MOBILE', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: 'GNS', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: 'VPN', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        }
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
