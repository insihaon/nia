<template>
  <div :class="{ [name]: true }" class="common-padding common-font" style="display: flex;flex-direction: column;">
    <div class="search-container" :style="setContainerHeight">
      <div v-if="isSearch" class="mainWrap">
        <div class="contentWrap subContentWrap">
          <!-- 조회 옵션상자 -->
          <div :model="searchModel" class="optionBox">
            <div class="optionBoxContent">
              <div v-for="(item, index) in items" :key="index" class="optionItem">
                <label>
                  <i :class="item.icon || ''" />
                  {{ item.label }}
                </label>
                <el-col :size="item.size">
                  <el-input
                    v-if="item.type === 'input'"
                    v-model="searchModel[item.model]"
                    type="text"
                    clearable
                    :placeholder="item.placeholder"
                    @keyup.native.enter="$emit('keyupEnter', searchModel)"
                  />

                  <CompCheckSelector
                    v-if="item.type === 'select' && item.multiple"
                    v-model="searchModel[item.model]"
                    :item="item"
                    :search-model.sync="searchModel[item.model]"
                  />

                  <el-select
                    v-if="item.type === 'select' && !item.multiple"
                    v-model="searchModel[item.model]"
                    collapse-tags
                    filterable
                    clearable
                    :placeholder="item.placeholder"
                    reserve-keyword
                    remote
                  >
                    <el-option
                      v-for="(option, i) in item.options"
                      :key="i"
                      :label="option.label"
                      :value="option.value"
                    />
                  </el-select>

                  <OrgSelect
                    v-if="item.type === 'orgSelect'"
                    v-model="searchModel[item.model]"
                    :item="item"
                    :search-model.sync="searchModel[item.model]"
                    @orgChange="(orgLvl)=> $emit('orgChange', orgLvl)"
                  />

                  <el-date-picker
                    v-if="item.type === 'date'"
                    v-model="searchModel[item.model]"
                    type="daterange"
                    start-placeholder="시작 일자"
                    end-placeholder="종료 일자"
                    :default-time="['00:00:00','23:59:59']"
                  />

                  <el-date-picker
                    v-if="item.type === 'dateTime'"
                    v-model="searchModel[item.model]"
                    type="datetimerange"
                    range-separator="to"
                    start-placeholder="Start date"
                    end-placeholder="End date"
                  />

                </el-col>
                <el-row class="d-flex flex-column w-50" style="justify-content: end; color : rgb(50, 49, 49)">
                  <slot name="searchCaption" />
                </el-row>
              </div>
            </div>
            <div class="optionBoxButtons" style="margin: 5px 8px 3px 3px; float: right">
              <el-button v-if="isExcel" type="button" size="mini" class="excel-form-export" icon="el-icon-download" @click="handleExcel">
                엑셀 저장
              </el-button>
              <el-button v-if="title !== '데이터셋' && title !== '노드 정보 조회'" class="btn-r" type="info" size="mini" @click="onClickSearchButton">
                <i class="el-icon-search" />
              </el-button>
              <el-button class="btn-r" style="background : rgba(128, 128, 128, 0.604)" type="info" size="mini" @click="handleSearchClear">
                <i type="info" class="el-icon-refresh-right" style="font-weight: 800; width: 100%;" />
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- agGrid -->
    <div v-if="Object.keys(agGrid).length > 0 " class="d-flex flex-column agGridShell" :style="{ flex: 1}">
      <div class="d-flex flex-column w-full text-right font-bold">
        <slot name="inquireButton" />
        <span class="mr-2">검색결과 : {{ totalCount }}건 </span>
      </div>
      <CompAgGrid
        ref="compSearchEquip"
        v-model="agGrid"
        v-loading="isGridLoading"
        class="w-100"
        :style="{'height': getHeight()}"
        style="min-height: 20px"
        :pagination-info="paginationInfo"
        @pageChange="handlePageChange"
        @changeSelectedRows="(value)=> $emit('selectedRow', value)"
        @cellClicked="(value) => $emit('cellClicked', value)"
        @sortChanged="onSortChanged"
      />
      <slot name="button-area" />
    </div>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CompCheckSelector from '@/views-dataHub/components/CompCheckSelector'
import OrgSelect from '@/views-dataHub/components/OrgSelect'
// import ComponentTesterMixins from '@/test/ComponentTesterMixins'

const routeName = 'CompInquiryPannel'
export default {
  name: routeName,
  components: { CompAgGrid, CompCheckSelector, OrgSelect },
  extends: Base,
  // mixins: [ComponentTesterMixins],
  props: {
    paginationInfo: {
      type: Object,
      default: () => {
        return {
          currentPage: 1,
          pageSize: 50,
          totalCount: null,
          totalPages: null,
          pagerCount: null
        }
      }
    },
    isModal: {
      type: Boolean,
      default: false
    },
    agGrid: {
      type: Object,
      default: () => { return {} }
    },
    isGridLoading: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: null
    },
    isSearch: {
      type: Boolean,
      default: true
    },
    isExcel: {
      type: Boolean,
      default: false
    },
    items: {
      type: Array,
      default() { return [] }
    },
    searchModel: {
      type: Object,
      default() { return {} }
    },

    customSearchContainerHeight: {
      type: String,
      default() { return null }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedItem: [],
    }
  },
  computed: {
    setContainerHeight() {
      let height
      if (!this.customSearchContainerHeight) {
        if (this.isSearch) {
          height = this.title === null ? 60 : 72
        } else {
          height = this.title === null ? 0 : 5
        }
      } else {
        height = parseInt(this.customSearchContainerHeight)
      }

      return { '--common-search-height': height + 'px' }
    },
    totalCount() {
      return this.humanNumber(this.paginationInfo?.totalCount)
    }
  },
  watch: {
  },
  mounted() {
  },
  methods:
  {
    getHeight() {
      return this.$parent?.$parent?.name?.includes('Modal') || this.isModal ? '500px' : '100%'
    },
    onSortChanged(sortedColumns) {
      const sortColInfo = sortedColumns?.length > 0 ? sortedColumns[0] : {}
      this.$emit('sortedChange', sortColInfo)
    },
    handleSearchClear() {
      const modelKeys = Object.keys(this.searchModel)
      modelKeys.forEach(key => {
        this.searchModel[key] = ''
      })
      this.$emit('searchClear', this.searchModel)
    },
    onClickSearchButton() {
      this.paginationInfo.currentPage = 1
      this.$emit('handleClickSearch', this.searchModel)
    },
    prevPage() {
      if (this.paginationInfo.currentPage > 1) {
        this.paginationInfo.currentPage--
        this.emit('onChangePage', this.paginationInfo.currentPage) // 이전 페이지로 이동할 때 데이터 다시 가져오기
      }
    },
    nextPage() {
      if (this.paginationInfo.currentPage < this.paginationInfo.totalPages) {
        this.paginationInfo.currentPage++
        this.$emit('onChangePage', this.paginationInfo.currentPage) // 다음 페이지로 이동할 때 데이터 다시 가져오기
      }
    },
    handlePageChange(newPage) {
      if (newPage) {
        this.paginationInfo.currentPage = newPage
        this.$emit('onChangePage', this.paginationInfo.currentPage) // 특정 페이지로 이동할 때 데이터 다시 가져오기
      }
    },
    refreshData() {
      this.selectedItem = []
    },
    handleExcel() {
      const name = `${this.title}_${this.toStringTime(new Date(), 'YYMMDD')}`
      this.$refs.compSearchEquip.exportExcel(name)
    }
   }
 }
</script>

<style lang="scss">
.nia {
  .CompInquiryPannel {
    font-family: "NotoSansKR";
    z-index: 0;
    .el-input--medium {
      font-size: 12px;
      .el-input__inner {
        height: 25px;
        line-height: 36px;
      }
    }
    .el-input__suffix {
      top: 3px;
    }
    .el-input,
    .el-input__clear {
      line-height: 30px !important;
    }
    .el-range-editor--medium {
      height: 30px;
      line-height: 36px;
      .el-range-input{
        font-size: 11px;
      }
      .el-range__icon,
      .el-range__close-icon {
        line-height: 19px;
      }
      .el-range-separator {
        line-height: 17px;
      }
    }
    .el-select__input.is-medium {
      display: none !important;
    }
    .result-cnt span{
      font-size : 11px
    }
    .CompAgGrid {
      display: inline-block;
      min-width: 50% !important;
      .ag-cell{
        font-size: 12px !important;
        font-weight: 200;
      }
      .ag-cell-value{
        color:#000000 !important;
        // padding-top: 5px !important;
      }
      .ag-header{
        background-color: #fff;
        min-height: 20px !important;
      }
      .ag-header-cell{
        font-size: 13px;
        font-weight: 600;
      }
      .ag-body-viewport {
        border-bottom: 1px solid #e8eaec;
      }
      .button-container{
        display: flex;
        justify-content: space-between;
      }
      .el-input__icon el-icon-circle-close{
        line-height: 26px !important;
      }
    }
    .search-container {
      height: auto;
    }

    .excel-form-export{
      background: rgb(34, 123, 74);
      color: white;
      padding: 9px 13px;
        &:hover{
          background: rgba(137, 174, 154, 0.952) !important;
        }
    }
  }
}
</style>
 <style lang="css" scoped>
  @import "~@/assets/css/nia_style_main.css";
</style>

