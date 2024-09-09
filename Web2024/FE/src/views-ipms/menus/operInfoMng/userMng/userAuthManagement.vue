<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            사용자 권한정보 조회결과
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

const routeName = 'UserAuthManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      requestParameter: {},
      tableColumns: [
        { prop: '', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '권한등급', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 3 } },
        { key: 'AuthLevel', props: { } },
        {
          key: 'InputSearchDetail',
          props: {
            label: '소속조직',
            modalName: 'ModalOrgSearch',
            valueName: 'sFullOrgNm',
            prop_parameterKey: { sposDeptOrgId: 'sktOrgId', sposDeptOrgNm: 'sFullOrgNm' },
            isReadOnly: true
          }
        },
        { key: 'InputType', props: { label: '사용자', prop_parameterKey: 'suserNm' } },
      ],
    }
  },
  methods: {
    handleSearch(compParams) {
      const rankKeys = ['ssvcLineTypeCd', 'ssvcGroupCd', 'ssvcObjCd']
      const params = {}
      rankKeys.forEach(key => {
        Object.assign(params, { [`tbLvlBasVo.${key}`]: compParams[key] })
        delete compParams[key]
      })
      Object.assign(this.requestParameter, params, compParams)

      /* 데이터 확인 console 추후 삭제 */
      console.log(this.requestParameter)

      // const res = await api(this.requestParameter)
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
