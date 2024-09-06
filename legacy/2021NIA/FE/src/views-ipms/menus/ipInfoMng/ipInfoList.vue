<template>
  <el-row ref="container" class="w-100 h-100">
    <div v-if="!isDashboard" ref="searchCondition" class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex">
          <el-select v-model="option" size="mini">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-input
            v-model="value"
            placeholder="IP주소 또는 회선번호, SAID를 입력해 주세요."
            clearable
            size="mini"
            @change="onCheckValidation"
          />
          <el-button
            class="btn-r ml-1"
            type="info"
            size="mini"
            icon="el-icon-search"
            style="background-color: #3a3a3a;"
            @click="fnMainSeachBtnClick()"
          >
            조회
          </el-button>
          <el-button
            type="button"
            size="mini"
            class="export-excel"
            icon="el-icon-download"
            @click="handleClickExcelBtn"
          >
            엑셀 저장
          </el-button>
        </el-col>
      </el-row>
    </div>
    <el-col ref="tableContainer">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="ipBlockColumns"
        :prop-data="resultList"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="ipInfoList"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 블록 정보
          </span>
        </template>
      </CompTable>
    </el-col>
    <ModalIpInfoDetail ref="ModalIpInfoDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { onMessagePopup } from '@/utils'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalIpInfoDetail from '@/views-ipms/modal/ModalIpInfoDetail'

const routeName = 'IpInfoList'

export default {
  name: routeName,
  components: { CompTable, ModalIpInfoDetail },
  extends: Base,
  mixins: [tableHeightMixin],
  props: {
    isDashboard: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      options: [],
      option: 'CV0001',
      value: '14.32',
      ipBlockColumns: [
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sfirstAddr', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slastAddr', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nbitmask', label: 'BitMask', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssaid', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sconnAlias', label: '수용회선명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '_detail', label: '상세', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            return this.$createElement('el-button', {
              on: { click: () => {
                this.fnViewDetailIpAlloc(row)
                // ipmgmt/allocmgmt/viewDetailIpAllocMstByMain.model
                // this.$refs.ModalIpInfoDetail.open({ row })
            } } }, '상세')
          }
        },
      ],
      resultList: []
    }
  },
  watch: {
    '$route'() {
      if (this.ipms.toParams !== null) {
        const { option, value } = this.ipms.toParams
        this.option = option
        this.value = value
        this.$store.dispatch('ipms/setToParam', null)
        this.fnMainSeachBtnClick()
      }
    }
  },
  created () {
    this.options = this.CONSTANTS.ipms.ipInfoOptions
  },
  methods: {
    onCheckValidation(val) {
      if (['IPv4', 'IPv6'].includes(this.option)) {
        const reVal = val.replace(/[^0-9\\.]/g, '')
        this.value = reVal
      }
    },
    async fnMainSeachBtnClick() {
      if (this.value === '') {
        onMessagePopup(this, '검색어를  입력하세요.')
        return
      }
      const ipInfoVo = { pageIndex: 1, pageUnit: '10', searchWrd: this.option }
      if (this.option === 'CV0001' || this.option === 'CV0002') {
        Object.assign(ipInfoVo, { sfirstAddr: this.value, sipVersionTypeCd: this.option })
      } else {
        let key = ''
        switch (this.option) {
          case 'SAID':
            key = 'ssaid'
            break
          case 'SLLNUM':
          key = 'sllnum'
            break
          case 'SCONNALIAS':
          key = 'sconnAlias'
            break
          default:
            break
        }
        Object.assign(ipInfoVo, { [key]: this.value })
      }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewListIpAllocMstByMain, ipInfoVo)
        this.resultList = res?.result?.resultListVo ?? []
      } catch (error) {
        this.error(error)
      }
    },
    async handleClickExcelBtn() {
      /* legacy param
      {
        pageIndex: 1
        pageUnit: 10
        pageSize: 10
        searchWrd: CV0001
        sconnAlias:
      }
      /ipmgmt/allocmgmt/viewListIpAllocMstByMainExcel.json
      */
      /* try {
        const res = await apiExcel('/ipmgmt/allocmgmt/viewListIpAllocMstByMainExcel.json')
      } catch (error) {
        this.error(error)
      } */
    },
    onOpen(model, actionMode) {
      this.selectedRow = model?.row ?? null
      if (this.selectedRow !== null) {
        const { nipAssignMstSeq, nipAllocMstSeq } = this.selectedRow
        this.fnViewDetailIpAlloc({ nipAssignMstSeq, nipAllocMstSeq })
      }
    },
    async fnViewDetailIpAlloc(row) {
      const { nipAssignMstSeq, nipAllocMstSeq } = row
      try {
        const res = await apiRequestModel(ipmsModelApis.viewDetailIpAllocMstByMain, { nipAssignMstSeq, nipAllocMstSeq })
        // this.tbIpInfoVo = res.result.data
        if (res.result.data) {
          this.$refs.ModalIpInfoDetail.open({ tbIpInfoVo: res.result.data, type: 'Aloc' })
        }
      } catch (error) {
        this.error(error)
      }
    },
  },
}
</script>
<style lang="css" scoped></style>
