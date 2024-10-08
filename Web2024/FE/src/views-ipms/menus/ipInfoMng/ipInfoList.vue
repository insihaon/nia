<template>
  <el-row ref="container" class="w-100 h-100">
    <div v-if="!isDashboard" ref="searchCondition" class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex">
          <el-select v-model="option" size="mini" @change="searchTagArr = []">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <div class="search-wrapper" @click="$refs.input.focus()">
            <div
              v-for="(tag, index) in searchTagArr"
              :key="index"
              class="tag-item"
            >
              <div>{{ tag }}</div>
              <i class="el-icon-close ml-1" style="font-size: 13px;" @click="removeSearchTag(index)" />
            </div>
            <input
              ref="input"
              v-model="searchTagStr"
              class="input-tag"
              type="text"
              @input="onCheckValidation"
              @keyup.space="onKeyUpSpace"
              @keyup.delete="onKeyUpBackspace"
              @keyup.enter="fnViewListIpAllocMstByMain()"
            />
          </div>
          <el-button
            class="btn-r ml-1"
            type="info"
            size="mini"
            icon="el-icon-search"
            style="background-color: #3a3a3a;"
            @click="handleClickSearch()"
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
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="ipInfoList"
        :prop-grid-indx="1"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
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
      searchTagArr: [],
      searchTagStr: '',
      value: '',
      option: 'CV0001',
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
      pagination: this.setDefaultPagination()
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        if (this.ipms.toParams !== null) {
          const { option, value } = this.ipms.toParams
          this.option = option
          // this.value = value
          this.searchTagStr = value
          this.$store.dispatch('ipms/setToParam', null)
          this.fnViewListIpAllocMstByMain()
        }
      },
      immediate: true
    }
  },
  created () {
    this.options = this.CONSTANTS.ipms.ipInfoOptions
  },
  methods: {
    onCheckValidation(val) {
      if (['CV0001', 'CV0002'].includes(this.option)) {
        const reVal = this.searchTagStr.replace(/[^0-9\\.]/g, '')
        this.searchTagStr = reVal
      }
    },
    handleClickSearch() {
      if (['CV0001', 'CV0002'].includes(this.option) && this.searchTagArr.length > 1) {
        this.fnMultiIpInfo()
      } else {
        this.fnViewListIpAllocMstByMain()
      }
    },
    async fnMultiIpInfo() {
      try {
        const searchWrd = this.searchTagArr.toString()
        const res = await apiRequestModel(ipmsModelApis.viewListMultiIpInfo, { searchWrd })
        this.resultList = res?.result?.data ?? []
      } catch (error) {
        this.error(error)
      }
    },
    async fnViewListIpAllocMstByMain() {
      const THIS = this
      if (this.searchTagStr === '' && this.searchTagArr.length === 0) {
        onMessagePopup(this, '검색어를  입력하세요.')
        return
      }
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const ipInfoVo = { pageIndex, pageUnit, searchWrd: this.option }
      if (this.option === 'CV0001' || this.option === 'CV0002') {
        Object.assign(ipInfoVo, { sfirstAddr: this.searchTagArr[0] ?? this.searchTagStr, sipVersionTypeCd: this.option })
      } else {
        let key = ''
        switch (this.option) {
          case 'SAID':
            // key = 'ssaid'
            key = 'ssaids'
            break
          case 'SLLNUM':
          // key = 'sllnum'
          key = 'sllnums'
            break
          case 'SCONNALIAS':
          // key = 'sconnAlias'
          key = 'sconnAliass'
            break
          default:
            break
        }
        Object.assign(ipInfoVo, { [key]: this.searchTagArr.length > 1 ? this.searchTagArr : [this.searchTagStr] })
      }
      const target = ({ vue: this.$refs.compTable })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListIpAllocMstByMain, ipInfoVo)
        this.pagination.data = res?.result?.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListIpAllocMstByMain()
    },
    onKeyUpSpace() {
      let str = this._cloneDeep(this.searchTagStr)
      if (this.option === 'SCONNALIAS') return
      if (['CV0001', 'CV0002'].includes(this.option)) {
        str = this.searchTagStr.replaceAll(' ', '')
      } else {
        str = this.searchTagStr.replaceAll(/[^0-9]/g, '')
      }
      if (!this.searchTagArr.includes(str) && str.length > 0) {
        this.searchTagArr.push(str)
      }
      this.searchTagStr = ''
    },
    onKeyUpBackspace() {
      if (this.searchTagStr.length === 0) {
        this.searchTagArr.pop()
      }
    },
    removeSearchTag(index) {
      this.searchTagArr
      if (index >= 0 && index < this.searchTagArr.length) {
        this.searchTagArr.splice(index, 1)
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
<style lang="scss" scoped>
.is-tag.el-input__inner ::v-deep {
  border-left: 0px;
}
.el-input-group__prepend {
  background-color: #fff;
}
.search-wrapper {
  border-radius: 5px;
  display: flex;
  width: 100%;
  align-items: center;
  border: solid 1px #dddfe6;
  .tag-item {
    display: flex;
    font-weight: 700;
    color: #e31818;
    padding: 0px 3px;
    margin-left: 4px;
    border-radius: 2px;
    width: fit-content;
    align-items: baseline;
    background: #e3181829;
    justify-content: space-between;
    i:hover {
      color: black;
      cursor: pointer;
    }
  }
  input {
    outline-color: #fff !important;
  }
}
</style>
