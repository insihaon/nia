<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListIpLinkMst"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleClickRow"
      >
        <template slot="text-description">
          <span>
            운용정보(링크) 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
    <ModalIpLinkMstDetail ref="ModalIpLinkMstDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalIpLinkMstDetail from '@/views-ipms/modal/ModalIpLinkMstDetail.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'operInfoLinkManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalIpLinkMstDetail },
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
        { prop: 'pifSerialIp', label: '링크IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'saofficescodeNm', label: '자국 수용국명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sanealias', label: '자국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'samstip', label: '자국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'saifname', label: '자국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szofficescodeNm', label: '대국 수용국명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szofficescode', label: '대국 장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szmstip', label: '대국 장비IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'szifname', label: '대국 IF명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssaid', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sconnalias', label: '수용회선명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row?.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD') : '' } },
        { prop: '', label: '삭제', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: []
    }
  },
  methods: {
    async fnViewListIpLinkMst(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListIpLinkMst, requestParameter)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      }
    },
    handleClickRow(row) {
      this.$refs.ModalIpLinkMstDetail.open({ nipLinkMstSeq: row.nipLinkMstSeq })
    }
  }
}
</script>
<style lang="scss" scoped></style>
