<template>
  <el-row class="w-100 h-100" :class="{'px-12': !isDashboard}">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable :prop-table-height="300" :prop-column="tableColumns" :prop-is-pagination="false" :prop-is-check-box="false" prop-grid-menu-id="inputSpeed" :prop-grid-indx="1">
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
const routeName = 'VpnIpStatue'

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
        { key: 'ServiceOrg', props: { multi: false } },
        { key: 'IpAddress', props: { value: 'CV0001' } },
        { key: 'SipCreateType', props: {} },
        // 보유
        // 상품
        { key: 'InputType', props: { label: '계약자명', propsParameterKey: 'scustname' } },
        { key: 'SOffice', props: {} }
      ],
      tableColumns: [
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '계약자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상품명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '보유', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
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
<style lang="css" scoped></style>
