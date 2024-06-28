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
            운용정보(링크) 조회결과
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

const routeName = 'operInfoFacilityLinkManagement'

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
        { key: 'InputType', props: { prop_parameterKey: 'searchWrd', label: '링크IP블록' } },
        { key: 'SOffice', props: { prop_parameterKey: 'srssofficescode' } },
        { key: 'InputType', props: { prop_parameterKey: 'smodelNm', label: '장비명' } },
        { key: 'InputType', props: { prop_parameterKey: 'sifNm', label: 'IF명' } },
        { key: 'LineInformation', props: {} },
        { key: 'InputType', props: { prop_parameterKey: 'sconnalias', label: '수용회선명' } },
        { key: 'SortType', props: { } },
      ],
      tableColumns: [
        { prop: '', label: '링크IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '자국 수용국명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '자국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '자국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '자국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '대국 수용국명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '대국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '대국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '대국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수용회선명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '삭제', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
  methods: {
    handleSearch(params) {
      console.log(params)
      /* const res = await api(params) */
    }
  }
}
</script>
<style lang="css" scoped></style>
