<template>
  <div :class="{ [name]: true }" class="common-padding common-font" style="display: flex;flex-direction: column;">
    <div class="search-container" :style="setContainerHeight">
      <div v-if="isSearch" class="mainWrap">
        <div class="contentWrap subContentWrap">
          <!-- 조회 옵션상자 -->
          <div :model="searchModel" class="optionBox">
            <div class="optionBoxContent">
              <div v-for="(item, index) in items" :key="index" class="optionItem">
                <label>{{ item.label }}</label>
                <div>
                  <el-col>
                    <el-input
                      v-if="item.type === 'input'"
                      v-model="searchModel[item.model]"
                      type="text"
                      clearable
                      style="width : 230px"
                      :placeholder="item.placeholder"
                      @keyup.native.enter="$emit('keyupEnter', searchModel)"
                    />

                    <CompCheckSelector
                      v-if="item.type === 'select' && item.multiple"
                      v-model="searchModel[item.model]"
                      :item="item"
                      :search-model.sync="searchModel[item.model]"
                      :style="{ width: '230px' }"
                    />

                    <el-select
                      v-if="item.type === 'select' && !item.multiple"
                      v-model="searchModel[item.model]"
                      collapse-tags
                      :style="{ width: '230px' }"
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
                        :style="{ width: '100%' }"
                      />
                    </el-select>

                    <OrgSelect
                      v-if="item.type === 'orgSelect'"
                      v-model="searchModel[item.model]"
                      :item="item"
                      :search-model.sync="searchModel[item.model]"
                      :style="{ width: '100%' }"
                      @orgChange="(orgLvl)=> $emit('orgChange', orgLvl)"
                    />

                    <el-date-picker
                      v-if="item.type === 'date'"
                      v-model="searchModel[item.model]"
                      type="daterange"
                      style="width : 230px"
                      start-placeholder="시작 일자"
                      end-placeholder="종료 일자"
                      :default-time="['00:00:00','23:59:59']"
                    />
                  </el-col>

                </div>
                <el-row class="d-flex flex-column w-50" style="justify-content: end; color : rgb(50, 49, 49)">
                  <slot name="searchCaption" />
                </el-row>
              </div>
            </div>
            <div class="optionBoxButtons" style="margin: 5px 8px 3px 3px; float: right">
              <el-button v-if="title !== '데이터셋' && title !== '노드 정보 조회'" class="btn-r" type="info" size="mini" @click="onClickSearchButton">
                {{ '조회' }}
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
        <el-row class="d-flex flex-column w-50" style="justify-content: end; relative;margin-top: -20px;">
          <slot name="inquireButton" />
        </el-row>
        <el-row class="d-flex flex-column w-50" style="text-align: right">
          <span>검색결과 : {{ totalCount }}건 </span>
        </el-row>
      </div>

      <CompAgGrid
        ref="CompTemplateTable"
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

    <!-- API JSON Editor -->
    <div v-if="isJson" class="dataComponents h-100">
      <div class="float-left w-100 h-80">
        <span>API 요청 데이터를 JSON 형태로 확인할 수 있습니다</span>
        <v-jsoneditor v-model="jsonData.reqJsonData.json" :options="jsonOptions" :plus="true" class="json-box left w-100" />
      </div>
      <div class="button-container">
        <el-button type="info" plain style="font-size: small; margin :0 8px" size="small" @click.native="$emit('handleRequest',jsonData)">요청</el-button>
      </div>
      <div class="w-100 h-80">
        <span> API 응답 데이터를 JSON 형태로 확인할 수 있습니다</span>
        <v-jsoneditor v-model="jsonData.resJsonData.json" :options="jsonOptions" :plus="true" class="json-box right w-100" />
      </div>
    </div>

    <!-- grafana -->
    <div v-if="isShowGraph" class="grafana-container" :style="setContainerHeight">
      <slot name="grafana" />
    </div>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import CompCheckSelector from '@/views-dataHub/components/CompCheckSelector'
import OrgSelect from '@/views-dataHub/components/OrgSelect'
import VJsoneditor from 'v-jsoneditor/src/index'
import ComponentTesterMixins from '@/test/ComponentTesterMixins'

const routeName = 'CompTemplate'
export default {
  name: routeName,
  components: { CompAgGrid, CompCheckSelector, OrgSelect, VJsoneditor },
  extends: Base,
  mixins: [ComponentTesterMixins],
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
    isShowGraph: {
      type: Boolean,
      default: false
    },
    isJson: {
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
      searchContainerHeight: '0px',
      // eslint-disable-next-line vue/no-dupe-keys
      modelItems: {},
      selectedItem: [],
      clearModelItems: {},
      jsonOptions: { mode: 'code' },
      emitKeys: ['handleClickSearch'],
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
    getOffset() {
      return this.setContainerHeight['--common-search-height']
    },
    resultCount() {
      const dataLength = this.agGrid?.data?.length
       return dataLength
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
      this.devEmit('sortedChange', sortColInfo)
    },
    handleSearchClear() {
      this.devEmit('searchClear', this.searchModel)
      this.devEmit('jsonClear', this.jsonData)
    },
    onClickSearchButton() {
      this.paginationInfo.currentPage = 1
      this.devEmit('handleClickSearch', this.searchModel)
    },
    prevPage() {
      if (this.paginationInfo.currentPage > 1) {
        this.paginationInfo.currentPage--
        this.emit('handleClickSearch', this.paginationInfo.currentPage) // 이전 페이지로 이동할 때 데이터 다시 가져오기
      }
    },
    nextPage() {
      if (this.paginationInfo.currentPage < this.paginationInfo.totalPages) {
        this.paginationInfo.currentPage++
        this.devEmit('handleClickSearch', this.paginationInfo.currentPage) // 다음 페이지로 이동할 때 데이터 다시 가져오기
      }
    },
    handlePageChange(newPage) {
      this.paginationInfo.currentPage = newPage
      this.devEmit('handleClickSearch', this.paginationInfo.currentPage) // 특정 페이지로 이동할 때 데이터 다시 가져오기
    },
    refreshData() {
      this.selectedItem = []
    },
    onSelectedEditRows(param) {
    },
    subscribeEvent() {
    }
   }
 }
</script>

<style lang="scss">

.datahub{
  .CompTemplate {
    font-family: "NotoSansKR";
    z-index: 0;

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
        min-height: 30px !important;
        height: 50px !important;
        border-top:1px solid #2e3574;
        border-bottom:1px solid #2e3574;
      }

      .ag-Grid-row{
        height: 20px !important;
      }

      .ag-header-cell{
        font-size: 13px;
        font-weight: 600;

      }

      .ag-header-row{
        margin-top: 3px !important;
        top: 1px !important;
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

      .grafana-container {
        width: 100%;
        height: calc(100% - var(--common-search-height));
        .title {
          font-size: 18px;
          font-weight: 600;
          display: flex;
          justify-content: center;
        }
        .garafana-section {
          width: 100%;
          height: calc(100% - 30px);
          iframe {
            width: 100%;
            height: 100%;
          }
        }
      }

      .json-box{
        height: calc(100% - 35px) !important;
      }

      .jsoneditor {
        border: 1px solid #d3d3d3 !important;
      }

      .jsoneditor-menu{
        background-color: #05050567 !important;
        border-bottom: 1px solid #d3d3d3 !important;
      }

      .jsoneditor-poweredBy{
        display: none !important;
      }

      .max-btn{
        right: 10px !important;
      }

      .jsoneditor-redo{
        display: none;
      }

      .jsoneditor-undo{
        display: none;
      }
    }
}
</style>
 <style lang="css" scoped>
  @import "~@/assets/css/style_main.css";
</style>

