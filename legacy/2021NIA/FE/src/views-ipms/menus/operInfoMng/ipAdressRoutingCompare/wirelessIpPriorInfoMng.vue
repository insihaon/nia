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
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="handleClickCell"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
        @update:propSelected="handleClickCheck"
      >
        <template slot="text-description">
          <span>
            무선IP 사전 정보관리 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button type="primary" size="mini" round @click="fnRoutingCollectMst">무선 전체 라우팅 수집</el-button>
            <el-button type="primary" size="mini" round @click="handleClickSummaryMng">Summary 관리</el-button>
            <el-button type="primary" size="mini" round @click="$refs.ModalUploadMst.open()" @reload="fnViewListMobileMst">텍스트 파일 업로드</el-button>
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="handleClickRegist">Community 개별 등록</el-button>
            <el-button icon="el-icon-document-delete" type="primary" size="mini" round @click="fnDeleteMobileMst()">삭제</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalWireMstInsert ref="ModalWireMstInsert" @reload="fnViewListMobileMst" />
    <ModalSummaryMst ref="ModalSummaryMst" />
    <ModalUploadMst ref="ModalUploadMst" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import ModalWireMstInsert from '@/views-ipms/modal/interlink/ModalWireMstInsert.vue'
import ModalSummaryMst from '@/views-ipms/modal/interlink/ModalSummaryMst.vue'
import ModalUploadMst from '@/views-ipms/modal/interlink/ModalUploadMst.vue'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'WirelessIpPriorInfoManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalWireMstInsert, ModalSummaryMst, ModalUploadMst },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'skindNm', label: '구분', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'sserviceNm', label: '서비스', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'scommu', label: 'Community', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP 블록', align: 'center', columnVisible: true, showOverflow: true }
      ],
      componentList: [
        { key: 'ApplyStatus', props: { label: '구분', prop_parameterKey: 'skindCd',
          defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'Community', value: 'COMMU' },
              { label: 'No-Community', value: 'NOCOMMU' },
            ]
          }
        },
        { key: 'InputType', props: { label: 'Community/IP블록', prop_parameterKey: 'scommu' } },
        { key: 'InputType', props: { label: '서비스', prop_parameterKey: 'sserviceNm' } },
      ],
      delRows: null
    }
  },
  mounted () {
    this.fnViewListMobileMst()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListMobileMst(requestParameter)
    },
    async fnViewListMobileMst(requestParameter) {
      const parameter = requestParameter ?? this.$refs?.searchCondition?.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListMobileMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListMobileMst()
    },
    handleClickCheck(rows) {
      this.delRows = rows
    },
    handleClickCell(row) {
      this.$refs.ModalWireMstInsert.open({ viewType: 'mobile', fnType: 'update', row })
    },
    handleClickRegist() {
      this.$refs.ModalWireMstInsert.open({ viewType: 'mobile', fnType: 'insert' })
    },
    fnDeleteMobileMst() {
      if (this.delRows === null || this.delRows.length === 0) {
        onMessagePopup(this, '삭제할 정보를 선택 후 삭제 가능합니다.')
      }
      const delList = []
      this.delRows.forEach(row => { delList.push(row.nmobileIpCommuSeq) })
      this.confirm('해당 무선IP 사전 정보를 정말 삭제하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warnning'
      }).then(async () => {
        try {
          const res = await apiRequestJson(ipmsJsonApis.deleteMobileMst, { delList })
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '무선IP Community 정보 삭제가 정상적으로 처리되었습니다.')
            this.delRows = null
            this.fnViewListMobileMst()
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        }
      })
    },
    fnRoutingCollectMst() {
      this.confirm('무선 전체 라우팅 수집을 시작하시겠습니까?', '확인', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warnning'
      }).then(async () => {
        const target = ({ vue: this })
        try {
          this.openLoading(target)
          const res = await apiRequestJson(ipmsJsonApis.intgrInsertListRoutChkMst)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, '라우팅 수집/DB 비교가 정상적으로 처리되었습니다.')
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.error(error)
        } finally {
          this.closeLoading(target)
        }
      })
    },
    handleClickSummaryMng() {
      this.$refs.ModalSummaryMst.open()
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
