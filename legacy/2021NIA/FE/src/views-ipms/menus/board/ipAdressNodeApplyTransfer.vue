<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="fnViewListIpBlockMst"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-data="resultListVo"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
      >
        <template slot="text-description">
          <span>
            조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-document-add" @click="fnViewInsertNode()">신청</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalNodeTransferDetail ref="ModalNodeTransferDetail" @reload="fnViewListIpBlockMst()" />
    <ModalNodeTransferInsert ref="ModalNodeTransferInsert" @reload="fnViewListIpBlockMst()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalNodeTransferDetail from '@/views-ipms/modal/ModalNodeTransferDetail.vue'
import ModalNodeTransferInsert from '@/views-ipms/modal/ModalNodeTransferInsert.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import moment from 'moment'

const routeName = 'IpAdressNodeApplyTransfer'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalNodeTransferDetail, ModalNodeTransferInsert },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'seq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        {
          prop: 'defore',
          label: '변경전',
          children: [
            { prop: 'beforeSsvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'beforeSsvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'beforeSsvcObjCd', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: 'after',
          label: '변경후',
          children: [
            { prop: 'afterSsvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'afterSsvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: 'afterSsvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          sortable: true,
          columnVisible: true,
          showOverflow: true,
        },
        { prop: 'sUserNm', label: '신청자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dCreateDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row) => moment(row.dCreateDt).format('YYYY-MM-DD')
        },
        { prop: 'dCompleteDt', label: '처리일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row) => moment(row.dCompleteDt).format('YYYY-MM-DD')
         },
        { prop: 'progressStatusNm', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'IpAddress', props: { isShowSelectBox: false } },
        { key: 'ApplyStatus', props: { prop_parameterKey: 'progressStatus' } },
        {
          key: 'SortType', props: {
            label: '등록기간',
            sortTypeDefaultVal: 'dcreate_dt',
            prop_options: [
              { label: '신청일', value: 'dcreate_dt' },
              { label: '신청번호', value: 'seq' },
              { label: '처리일시', value: 'dcomplete_dt' },
            ]
          }
        },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListIpBlockMst()
  },
  methods: {
    async fnViewListIpBlockMst(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListNode, requestParameter)
        this.resultListVo = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
     onClcikRow(row) {
       this.fnViewDetailNode(row)
    },
     async fnViewDetailNode(row) {
      try {
        const { seq } = row
        const nodeVo = { seq: seq }
        const res = await apiRequestModel(ipmsModelApis.viewDetailNode, nodeVo)
        if (res.result.data) {
          this.$refs.ModalNodeTransferDetail.open({ row: res.result.data })
        }
      } catch (error) {
        console.error(error)
      }
    },
    async fnViewInsertNode() {
       try {
        const serachVo = {
          pageUnit: '5',
          pageIndex: '1',
        }
        const res = await apiRequestModel(ipmsModelApis.viewInsertNode, serachVo)
        if (res.result.data) {
          this.$refs.ModalNodeTransferInsert.open({ row: res.result.data })
        }
      } catch (error) {
        console.error(error)
      }
      this.$refs.ModalNodeTransferInsert.open()
    },
  }
}
</script>
<style lang="scss" scoped>
</style>
