<template>
  <el-row ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="fnViewListOrgBas"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="resultListVo"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-enabled-excel-down="false"
      >
        <template slot="text-description">
          <span>
            계위코드 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewupdateLvlCd('', 'create')">가상 국사/조직등록</el-button>
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
                  this.fnViewupdateLvlCd(row.slvlCd, 'edit')
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
      resultListVo: []
    }
  },
  mounted() {
    this.fnViewListOrgBas()
  },
  methods: {
    async fnViewListOrgBas(requestParameter) {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListTbLvlCdVo, requestParameter)
        this.resultListVo = res?.result.data
      } catch (error) {
        console.error(error)
      }
    },
    async fnViewupdateLvlCd(slvlCd, type) {
      if (slvlCd === '' || slvlCd === null) {
        this.$refs.ModalTbLvlCd.open({ type: type })
        return
      }
        const TbLvlCdVo = {
          slvlCd: slvlCd
        }
        const res = await apiRequestModel(ipmsModelApis.viewUpdateTbLvlCdVo, TbLvlCdVo)
        if (res.result.data) {
          this.$refs.ModalTbLvlCd.open({ row: res.result.data, type: type })
        }
    }
  }
}
</script>
