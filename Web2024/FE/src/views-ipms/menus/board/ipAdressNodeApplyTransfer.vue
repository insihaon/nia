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
            조회결과
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

const routeName = 'IpAdressNodeApplyTransfer'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        {
          prop: '',
          label: '변경전',
          children: [
            { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '',
          label: '변경후',
          children: [
            { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        { prop: '', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'IpAddress', props: { isShowSelectBox: false } },
        { key: 'ApplyStatus', props: { prop_parameterKey: 'progressStatus' } },
        {
          key: 'SortType', props: {
            label: '등록기간',
            sortTypeDefaultVal: 'dcreate_dt',
            prop_options: [
              { label: '신청일', value: 'dcreate_dt' },
              { label: '신청번호', value: 'seq' },
              { label: '처리일시', value: 'dcomplete_dt' },
            ]
          }
        },
      ],
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
