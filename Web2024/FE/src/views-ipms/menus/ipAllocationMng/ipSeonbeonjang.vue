<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-row :gutter="20">
      <el-col :span="12">
        <compTable :prop-table-height="300" :prop-column="tableColumns" :prop-is-pagination="false" :prop-is-check-box="true" prop-grid-menu-id="inputSpeed" :prop-grid-indx="1">
          <template slot="text-description">
            <span>
              IP 선번장 조회결과
            </span>
          </template>
        </compTable>
      </el-col>
      <el-col :span="12">
        <compTable :prop-table-height="300" :prop-column="hostTableColumns" :prop-is-pagination="false" :prop-is-check-box="false" prop-grid-menu-id="inputSpeed" :prop-grid-indx="1">
          <template slot="text-description">
            <span>
              HOST 관리
            </span>
          </template>
        </compTable>
      </el-col>
    </el-row>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
const routeName = 'IpSeonbeonjang'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: {} },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { isMulti: false } },
        { key: 'IpAddress', props: { value: 'CV0001' } },
        { key: 'InputType', props: { label: 'BitMask', propsParameterKey: 'nbitmask' } },
        // 회선정보
        { key: 'InputType', props: { label: '용도', propsParameterKey: '' } },
        { key: 'DatePicker', props: { } },
        { key: 'InputSearchDetail',
          props: { label: '장비명', modalName: 'ModalFacilityInformation', componentKey: 'ssubscnealiasNe' } },
        { key: 'SortType', props: {} },
      ],
      tableColumns: [
        { prop: '', label: '구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
      hostTableColumns: [
        { prop: '', label: '수용국', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Host IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true }

      ]
    }
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>
<style lang="css" scoped></style>
