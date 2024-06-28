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
            사용자 권한 신청
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

const routeName = 'AuthApply'

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
        { prop: '', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'InputSearchDetail',
          props: {
            label: '소속조직',
            modalName: 'ModalOrgSearch',
            valueName: 'sFullOrgNm',
            prop_parameterKey: { sposDeptOrgId: 'sktOrgId', sporDdptOrgNm: 'sFullOrgNm' },
            isReadOnly: true
          }
        },
        { key: 'InputType', props: { label: '사용자', prop_parameterKey: 'suerNm' } },
        { key: 'ApplyStatus', props: { label: '진행상태', prop_parameterKey: 'nrequestTypeCd' } },
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
