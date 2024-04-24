<template>
  <div :class="{ [name]: true }" class="common-padding common-font" style="display: flex;flex-direction: column;">
    <div class="search-container" :style="setContainerHeight">
      <div v-if="isSearch" class="optionBox">
        <!-- 조회 옵션상자 -->
        <el-row class="optionRow">
          <el-col v-for="(item, index) in items" :key="index" class="optionCol" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
            <label>
              {{ item.label }}
            </label>
            <div>
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
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" align="center" class="searchBtnGroup">
            <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="onClickSearchButton">
              검색
            </el-button>
            <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh" @click="handleSearchClear">
              초기화
            </el-button>
            <el-button v-if="isExcel" type="button" size="mini" class="excel-form-export" icon="el-icon-download" @click="handleExcel">
              엑셀 저장
            </el-button>
          </el-col>
        </el-row>
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

const routeName = 'CompInquiryPannel'
export default {
  name: routeName,
  components: { CompAgGrid, CompCheckSelector },
  extends: Base,
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
</style>
 <style lang="css" scoped>
  @import "~@/assets/css/nia_style_main.css";
</style>

