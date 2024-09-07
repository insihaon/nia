<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListTbBatchSvcBas"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleClickCell"
      >
        <template slot="text-description">
          <span>
            배치 연동 정보 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalTbBatchSvcBasDetail ref="ModalTbBatchSvcBasDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalTbBatchSvcBasDetail from '@/views-ipms/modal/interlink/ModalTbBatchSvcBasDetail.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'BatchLinkInformation'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalTbBatchSvcBasDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: 'sifId', label: '연동 ID', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'ssystemNm', label: '시스템 명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'linktype', label: '연동 형태', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'sopstate', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        {
          key: 'BoardSearchCondition', props: {
            defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: '연동 ID', value: 'sifId' },
              { label: '시스템 명', value: 'ssystemNm' },
            ]
          }
        }
      ],
      tableDatas: [],
    }
  },
  // 수정: updateTbBatchSvcBas
  mounted () {
    this.fnViewListTbBatchSvcBas()
  },
  methods: {
    async fnViewListTbBatchSvcBas(requestParameter) {
      const params = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListTbBatchSvcBas, params)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      }
    },
    handleClickCell(row) {
      this.$refs.ModalTbBatchSvcBasDetail.open({ viewType: 'mobile', fnType: 'update', row })
    },
  }
}
</script>
<style lang="css" scoped>
</style>
