<template>
  <el-row class="w-100 h-100" :class="{'px-12': !isDashboard}">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
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
            IP 할당 조회결과
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
const routeName = 'IpAllocation'

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
        { key: 'SsvcLineType', props: { lvl: 3, multi: [2] } },
        { key: 'SOffice', props: {} },
        { key: 'SipCreateType', props: {} },
        { key: 'ServiceOrg', props: { limit: 10 } },
        { key: 'IpAddress', props: { value: 'CV0001' } },
        { key: 'InputType', props: { label: 'BitMask', propsParameterKey: 'bitMask' } },
        // 회선정보(select)
        // 할당상태
        { key: 'DateRange', props: { } },
        { key: 'InputType', props: { label: '장비명', propsParameterKey: 'equipment_nm' } },
        { key: 'SortType', props: { } },
        { key: 'IncludeYN', props: { label: 'Summary 포함 여부', parameterKey: 'snull0Yn' } },
        { key: 'IncludeYN', props: { label: 'DB-라우팅 일치여부', parameterKey: 'sintgrmYn' } },
        { key: 'InputType', props: { label: '라우팅 중복 개수', propsParameterKey: 'nsummaryCnt' } },
      ],
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
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>
<style lang="scss" scoped></style>
