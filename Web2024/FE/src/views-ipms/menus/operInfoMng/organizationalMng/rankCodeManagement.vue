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
        :prop-column="tableColumns"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            계위코드 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="handleOpenModal('create')">가상 국사/조직등록</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalTbLvlCd ref="ModalTbLvlCd" @reload="fnViewListOrgBas" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import ModalTbLvlCd from '@/views-ipms/modal/orgmgt/ModalTbLvlCd.vue'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'RankCodeManagement'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalTbLvlCd },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'slvlCd', label: '코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slvlNm', label: '계위명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sorgOfficeFlagYn', label: '구분코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sexLinkUseTypeNm', label: '외부연동 유형', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'scomment', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수정', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            if (row.sexLinkUseTypeCd === 'CE0006') {
              return this.$createElement('el-button', {
                attrs: {
                  round: true, // Adding the round option
                  plain: true,
                  type: 'primary',
                  size: 'mini'
                },
                on: { click: () => {
                  this.handleOpenModal('edit', row.slvlCd)
                } } }, '수정')
            } else {
              return this.$createElement('span', { style: 'color: red' }, '불가')
            }
          }
        },
      ],
      componentList: [
        { key: 'ExtrnLnkgs', props: { label: '외부연동유형' } },
        { key: 'InputType', props: { label: '계위명', prop_parameterKey: 'searchWrd' } },
        { key: 'InputType', props: { label: '코드명', prop_parameterKey: 'slvlCd' } },
      ],
      searchWrd: '',
    }
  },
  mounted() {
    this.fnViewListOrgBas()
  },
  methods: {
    handleSearch(requestParameter) {
      this.pagination.currentPage = 1
      this.fnViewListOrgBas(requestParameter)
    },
    async fnViewListOrgBas(requestParameter = null) {
      const parameter = requestParameter ?? this.$refs.searchCondition.requestParameter
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListTbLvlCdVo, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListOrgBas()
    },
    handleOpenModal(type, slvlCd = null) {
      const params = { type }
      if (type === 'edit') {
        Object.assign(params, { slvlCd })
      }
      this.$refs.ModalTbLvlCd.open(params)
    }
  }
}
</script>
