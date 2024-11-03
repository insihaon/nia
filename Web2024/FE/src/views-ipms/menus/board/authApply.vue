<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="fnViewListUserAuthSubs"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="resultListVo"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="onClcikRow"
        :prop-enabled-excel-down="false"
      >
        <template slot="text-description">
          <span>
            사용자 권한 신청 정보 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewDetailGrant('', 'U')">등록</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalDetailUserAuth ref="ModalDetailUserAuth" @reload="fnViewListUserAuthSubs()" />
    <ModalInsertUserAuth ref="ModalInsertUserAuth" @reload="fnViewListUserAuthSubs()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalDetailUserAuth from '@/views-ipms/modal/notice/ModalDetailUserAuth.vue'
import ModalInsertUserAuth from '@/views-ipms/modal/notice/ModalInsertUserAuth.vue'

const routeName = 'AuthApply'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalDetailUserAuth, ModalInsertUserAuth },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: 'grantSeq', label: '번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserNm', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sposDeptFullNm', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dcreateDt', label: '신청일시', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nrequestTypeNm', label: '진행상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        {
          key: 'InputSearchDetail',
          props: {
            label: '소속조직',
            modalName: 'ModalOrgSearch',
            valueName: 'sFullOrgNm',
            prop_parameterKey: { sposDeptOrgId: 'sktOrgId', sporDdptOrgNm: 'sFullOrgNm' },
            isReadOnly: true
          }
        },
        { key: 'InputType', props: { label: '사용자', prop_parameterKey: 'suerNm' } },
        { key: 'ApplyStatus', props: { label: '진행상태', prop_parameterKey: 'nrequestTypeCd' } },
      ],
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListUserAuthSubs()
  },
  methods: {
    async fnViewListUserAuthSubs(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListUserAuthSubs, requestParameter)
        this.resultListVo = res?.result?.data
      } catch (error) {
        console.error(error)
      }
    },
    onClcikRow(row) {
      this.fnViewDetailGrant(row, 'detail')
    },
    async fnViewDetailGrant(row, type) {
      if (type === 'detail') {
        try {
         const { suserId, grantSeq } = row
         const tbUserAuthVo = {
            suserId: suserId,
            grantSeq: grantSeq
         }
         const res = await apiRequestModel(ipmsModelApis.viewDetailUserAuthSubs, tbUserAuthVo)
         if (res) {
           const resultData = {
            ...res,
            grantSeq: grantSeq
           }
          this.$refs.ModalDetailUserAuth.open({ row: resultData, type: type })
         }
       } catch (error) {
         console.error(error)
       }
      } else {
        try {
         const { suserId } = row
         const tbUserAuthVo = {
           typeFlag: type
         }

         if (type === 'U') {
           this._merge(tbUserAuthVo, { suserId: suserId })
         }
         const res = await apiRequestModel(ipmsModelApis.viewInsertUserAuthSubs, tbUserAuthVo) /* viewInsertUserAuthSubs */
         if (res) {
           this.$refs.ModalInsertUserAuth.open({ row: res, type: type })
         }
       } catch (error) {
         console.error(error)
       }
      }
    }
  },
}
</script>

