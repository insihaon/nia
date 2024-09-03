<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="fnViewListReq"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
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
            요구사항 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div class="float-right">
            <el-button size="mini" icon="el-icon-document-add" @click="fnViewDetailReq('', 'create')">글쓰기</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalReqDetail ref="ModalReqDetail" @reload="fnViewListReq" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalReqDetail from '@/views-ipms/modal/notice/ModalReqDetail.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'IssueDataRequest'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalReqDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'seq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardDivision', label: '요청사항구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardTitle', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sUserNm', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardDcreateDt', label: '등록일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardDesireDate', label: '희망완료일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardExpectedDate', label: '완료예정일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardImportance', label: '중요도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'rboardProgress', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'ApplyStatus', props: {
          label: '요청사항 구분',
          prop_parameterKey: 'RboardDivision',
          prop_options: [
              { label: '전체', value: '' },
              { label: '오류 버그 수정', value: 'RES001' },
              { label: '기능 개발 요청', value: 'RES002' },
              { label: '자료 요청', value: 'RES003' },
              { label: '연동 요청', value: 'RES004' },
            ]
          }
        },
        { key: 'BoardSearchCondition', props: {} },
        {
          key: 'ApplyStatus', props: {
          label: '진행상태',
          prop_parameterKey: 'RboardProgress',
          prop_options: [
              { label: '전체', value: '' },
              { label: '요청사항 접수', value: 'RES005' },
              { label: '접수 반려', value: 'RES006' },
              { label: '조치 진행 중', value: 'RES006' },
              { label: '조치 완료', value: 'RES007' },
            ]
          }
        },
        { key: 'DateRange', props: { label: '등록기간' } },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListReq()
  },
  methods: {
    async fnViewListReq(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListReq, requestParameter)
        this.resultListVo = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
    onClcikRow(row, type) {
     this.fnViewDetailReq(row, 'detail')
    },
    async fnViewDetailReq(row, type) {
      if (type === 'detail') {
        try {
           const ReqBoardVo = {
              seq: row.seq
           }
          const res = await apiRequestModel(ipmsModelApis.viewDetailReq, ReqBoardVo)
          this.$refs.ModalReqDetail.open({ row: res.result.data, type: type })
        } catch (error) {
          console.error(error)
        }
      } else {
        this.$refs.ModalReqDetail.open({ type: type })
      }
    }
  },
}
</script>
<style lang="scss" scoped>
</style>
