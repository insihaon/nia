<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      ref="DynamicComponent"
      :component-keys="componentList"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 블록크기별 통계 현황
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

const routeName = 'ipStatByBlockSize'

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
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 1, multi: [1], limit: { 1: 5, 2: null, 3: null } } },
        { key: 'BlockSize', props: {} },
        { key: 'IpAddress', props: { label: 'IP 버전', isShowInput: false } },
        { key: 'SipCreateType', props: {} },
        { key: 'DatePicker', props: {} },
      ],
      tableColumns: [
        {
          prop: '', label: '', children: [
            { prop: '', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '본부', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '공인/사설', align: 'center', columnVisible: true, showOverflow: true },
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
          prop: '', label: '/24', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '/25', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '/26', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '/27', children: [
            { prop: '', label: '총수량', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '미할당', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '/28', children: [
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
