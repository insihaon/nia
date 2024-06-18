<template>
  <el-row>
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col>
      <div>IP 이력관리 조회 결과</div>
      <CompTable
        :prop-table-height="300"
        :prop-column="ipBlockColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="ipInfoList"
        :prop-grid-indx="1"
      />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'

const routeName = 'IpHistManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        { key: 'ServiceOrg', props: { isMulti: false } },
        { key: 'SipCreateType', props: { } },
        { key: 'IpBlockStatus', props: { label: '할당상태' } },
        { key: 'CheckYear', props: { } },
        { key: 'IpAddress', props: { } },
        { key: 'InputType', props: { componentKey: 'nbitmask', label: 'BitMask' } },
        // 장비명
        { key: 'LineInformation', props: { } },
        { key: 'WorkSystem', props: {} },
        { key: 'DetailedWorkClassification', props: {} },
        { key: 'InputType', props: { componentKey: 'screateId', label: '작업자' } },
        { key: 'DateRange', props: {} },
      ],
      ipBlockColumns: [
        { prop: '', label: '검색 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP 주소', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'BitMask', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상세', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
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
