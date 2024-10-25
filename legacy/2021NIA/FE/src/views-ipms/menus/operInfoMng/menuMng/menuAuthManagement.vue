<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="fnViewListAuthMng"
    />
    <el-col :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="resultListVo"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            권한메뉴관계 정보 조회결과
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
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'MenuAuthManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'smenuNm', label: '메뉴명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'smenuId', label: '메뉴 ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sscrnUrlAdr', label: '화면 URL', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'smenuAutUseYn', label: '메뉴권한 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
      { key: 'AuthLevel', props: { isAllOption: false, defaultValue: 'UR0001' } },
      { key: 'BoardSearchCondition', props: { defaultValue: 'smenuNm', prop_options: [
        { label: '메뉴명', value: 'smenuNm' },
        { label: '메뉴ID', value: 'smenuId' },
        ] }
      }],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListAuthMng()
  },
  methods: {
    async fnViewListAuthMng(requestParameter) {
       try {
        const res = await apiRequestModel(ipmsModelApis.viewListMenuAuth, requestParameter)
        this.resultListVo = res?.result?.data
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
