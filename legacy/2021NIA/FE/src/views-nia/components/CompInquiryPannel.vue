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
                    style="width : 230px;"

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
                </el-col>
                <el-row class="d-flex flex-column w-50" style="justify-content: end; color : rgb(50, 49, 49)">
                  <slot name="searchCaption" />
                </el-row>
              </div>
            </div>
            <div class="optionBoxButtons" style="margin: 5px 8px 3px 3px; float: right">
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
      <div class="d-flex" style="padding-top:20px">
        <!-- <el-row class="d-flex flex-column w-50" style="justify-content: end; relative;margin-top: -20px;">
          <slot name="inquireButton" />
          <el-row class="d-flex flex-column w-50" style="text-align: right">
            <span>검색결과 : {{ totalCount }}건 </span>
          </el-row>
        </el-row> -->
      </div>

      <CompAgGrid
        ref="compSearchEquip"
        v-model="agGrid"
        class="w-100 h-100"
        style="min-height: 20px"
        :pagination-info="paginationInfo"
        @pageChange="handlePageChange"
        @changeSelectedRows="(value)=> $emit('selectedRow', value)"
        @cellClicked="(value) => $emit('cellClicked', value)"
        @sortChanged="onSortChanged"
      />
      <slot name="button-container" />
    </div>

    <!-- grafana -->
    <!-- <div v-if="isShowGraph" class="grafana-container" :style="setContainerHeight">
      <slot name="grafana" />
    </div> -->
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
    agGrid: {
      type: Object,
      default: () => { return {} }
    },
    title: {
      type: String,
      default: null
    },
    jsonData: {
      type: Object,
      default: () => { return {} }
    },
    isSearch: {
      type: Boolean,
      default: true
    },
    isFormButton: {
      type: Boolean,
      default: true
    },
    isButtonSlot: {
      type: Boolean,
      default: false
    },
    // isShowGraph: {
    //   type: Boolean,
    //   default: false
    // },
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
  methods: {
    onSortChanged(sortedColumns) {
      const sortColInfo = sortedColumns?.length > 0 ? sortedColumns[0] : {}
      this.$emit('sortedChange', sortColInfo)
    },
    handleSearchClear() {
      this.$emit('searchClear', this.searchModel)
      this.$emit('jsonClear', this.jsonData)
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

   }
 }
</script>

<style lang="scss">

.nia{
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

    .el-range-editor--medium {
      height: 25px;
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

    .el-select__input.is-medium{
      display: none !important;
    }

    .result-cnt span{
      font-size : 11px
    }

    .CompAgGrid{
      display: inline-block;
      min-width: 50% !important;

      .ag-cell{
        font-size: 12px !important;
        font-weight: 200;
      }

      .ag-cell-value{
        color:#000000 !important;
        padding-top: 5px !important;
      }

      .ag-header{
        background-color: #fff;
        min-height: 20px !important;
        // height: 50px !important;
        // border-top:1px solid #2e3574;
        border:1px solid #2e3574;
      }

      .ag-Grid-row{
        height: 20px !important;
      }

      .ag-header-cell{
        font-size: 13px;
        font-weight: 600;

      }

      .ag-header-row{
        // margin-top: 3px !important;
        // top: 1px !important;
      }

      .ag-body-viewport {
        border-bottom: 1px solid #e8eaec;
      }

      .button-container{
        display: flex;
        justify-content: space-between;
      }

    }
    .search-container {
      height: auto;
    }

    }
}
</style>
 <style lang="css" scoped>
  @import "~@/assets/css/nia_style_main.css";
</style>

