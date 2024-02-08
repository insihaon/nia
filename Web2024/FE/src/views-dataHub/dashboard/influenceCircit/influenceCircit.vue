<template>
  <div :class="{ [name]: true }" style="height: 100%">
    <template>
      <div style="display:flex; flex-direction:column;" class="h-100">
        <el-collapse
          v-model="conditionBarOnOff"
          class="influenceCircitCollapse common-padding"
          style="height: auto !important"
          @change="handleCollapse"
        >
          <el-collapse-item name="1">
            <DataHubComponent
              ref="selectApi"
              :ag-grid="mainTableModel"
              :items="searchItems"
              :form-wrap="true"
              :search-model.sync="searchModel"
              :pagination-info="paginationInfoApi"
              class="w-100"
              style="height:100%"
              @orgChange="orgChange"
              @searchClear="handleApiSearchClear"
              @handleClickSearch="onClickSearchApi"
              @keyupEnter="onClickSearchApi"
            />
          </el-collapse-item>
        </el-collapse>
        <div style="height: 100%; width: 100%;">
          <div
            class="tags-view-container common-padding"
          >
            <div class="tag-view-wrapper">
              <el-tag
                v-for="(tag, index) of influenceCircitTagDataList"
                :key="index"
                closable
                :class="{clicked:tag.tagData.nescode === currentTagCode}"
                :disable-transitions="false"
                @close="closeTag(tag)"
                @click="tagChange(tag)"
                @click.middle.native="closeTag(tag)"
              >
                {{ tag.tagData.nealias }}
              </el-tag>
            </div>
          </div>
          <div
            style="height: calc(100% - 40px)"
            class="common-padding"
          >
            <influenceCircitContent
              v-for="(tag, index) of influenceCircitTagDataList"
              v-show="tag.tagData.nescode === currentTagCode"
              :key="tag.tagData.nescode"
              ref="content"
              :content-index="index"
              :tag="tag"
              @e2eNodeClick="e2eNodeClick"
            />
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import { apiSELECT_VW_ORG_MST_HQ, apiGET_ORG_LIST, apiGET_OFFICE_LIST, apiSELECT_NEA_LIST } from '@/api/dataHub'
import influenceCircitContent from '@/views-dataHub/dashboard/influenceCircit/influenceCircitContent'
import influenceCircitHyperLinkRenderer from '@/views-dataHub/dashboard/influenceCircit/subModule/influenceCircitHyperLinkRenderer'
import EventBus from '@/utils/event-bus'
import { defaultZero } from '@/views-dataHub/commonFormat'
import CompTreeSelector from '@/components/selector/CompTreeSelector'

const defaultSearchModel = {
  searchTerm: '',
  resultCount: 20,
  ORG: {
    HEAD: '',
    CENTER: '',
    TEAM: '',
    OFFICE: ''
  }
}

const routeName = 'influenceCircit'
export default {
  name: routeName,
  components: {
    influenceCircitContent, DataHubComponent,
    // eslint-disable-next-line vue/no-unused-components
    influenceCircitHyperLinkRenderer,
    // eslint-disable-next-line vue/no-unused-components
    CompTreeSelector
  },
  extends: Base,
  data() {
    return {
      name: routeName,
      searchItems: [
        { label: '검색어', type: 'input', size: 18, model: 'searchTerm', placeholder: '장치명' },
        {
          label: '출력건수', type: 'select', size: 6, model: 'resultCount', options: [
            { label: '10건', value: 10 },
            { label: '20건', value: 20 },
            { label: '50건', value: 50 },
            { label: '100건', value: 100 },
          ]
        },
        {
          label: '관리센터/국사', type: 'orgSelect', size: 18, model: 'ORG',
          HEAD: { options: [] },
          CENTER: { options: [] },
          TEAM: { options: [] },
          OFFICE: { options: [] },
        }
      ],
      searchModel: Object.assign({}, defaultSearchModel),
      paginationInfoApi: {
        currentPage: 1, // 현재 페이지
        pageSize: 20, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      mainTableData: [],
      conditionBarOnOff: '1'
    }
  },

  computed: {
    mainTableModel() {
      const options = { name: this.name + 'mainTable', rowGroupPanel: false, rowHeight: 40, rowSelection: 'single', rowMultiSelection: true }
      const columns = [
        { prop: '선택', headerName: '선택', filter: false, checkboxSelection: true, width: 80, resizable: false },
        { type: '', prop: 'officenamescode', name: '관리국사', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'svcnetdescription', name: '서비스망', width: 120, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'maindescription', name: '대분류', width: 100, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'subdescription', name: '소분류', width: 150, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'description', name: '용도', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        { type: '', prop: 'modelname', name: '모델명', width: 180, suppressMenu: true, alignItems: 'left', sortable: true },
        {
          type: '', prop: 'nealias', name: '장치명', width: 180, suppressMenu: true, alignItems: 'left', sortable: true,
          cellRendererFramework: 'influenceCircitHyperLinkRenderer', cellRendererParams: { clickFn: (param) => { this.selectNealias(param) } }
        },
        {
          type: '', name: '수용고객', suppressMenu: true, sortable: true, alignItems: 'center',
          children: [
            { name: '고객', prop: 'nea_nes_cnt', width: 100, formatter: defaultZero },
            { name: '영향회선', prop: 'nea_line_cnt', width: 100, formatter: defaultZero },
            { name: '서비스', prop: 'nea_svc_cnt', width: 100, formatter: defaultZero },
            { name: '분류', prop: '', width: 100 },
          ]
        },
        {
          type: '', name: '영향고객', suppressMenu: true, sortable: true,
          children: [
            { name: '고객', prop: 'nea_relation_nes_cnt', width: 100, formatter: defaultZero },
            { name: '영향회선', prop: 'nea_relation_line_cnt', width: 100, formatter: defaultZero },
            { name: '서비스', prop: 'nea_relation_svc_cnt', width: 100, formatter: defaultZero },
          ]
        },
        {
          type: '', name: '장비위치', suppressMenu: true, sortable: true,
          children: [
            { name: '설치국사', prop: 'officename', width: 120 },
            { name: '주소', prop: 'full_address', width: 200 },
            { name: '빌딩', prop: 'bldgname', width: 120 },
            { name: '설치위치', prop: '', width: 100 },
          ]
        }
      ]
      return { options, columns, data: this.mainTableData }
    },

    influenceCircitTagDataList: {
      get() {
        return this.influenceCircitStore.influenceCircitTagData.tagList
      }
    },
    currentTagCode: {
      get() {
        return this.influenceCircitStore.influenceCircitTagData.currentTagCode
      }
    },

    orgItem() {
      return this.searchItems.find((item) => { return item.model === 'ORG' })
    }

  },

  watch: {
    currentTagCode(n, o) {
      if (n === o) return
      EventBus.$emit('onCollapseChanged', 0)
    },
  },

  async mounted() {
    this.tagContainerEl = document.querySelector('.tag-view-wrapper')
    this.tagContainerEl.addEventListener('wheel', this.handleScrollTagContainer)

    await this.$nextTick()
    await this.$loadScript('./extlib/mapTree/js/d3.min.js')
    await this.$loadScript('./extlib/mapTree/js/d3-tip.js')
    await this.$loadScript('./extlib/mapTree/e2e.js')

    this.loadHeadOrgList()
  },

  beforeDestroy() {
    this.tagContainerEl.removeEventListener('wheel', this.handleScrollTagContainer)
  },

  methods: {
    handleScrollTagContainer(event) {
      event.preventDefault()

      // 스크롤할 양 계산
      const delta = Math.max(-1, Math.min(1, (event.wheelDelta || -event.detail)))
      const scrollXAmount = delta * 30 // 스크롤할 양 (30은 임의의 값)

      // X 축으로 스크롤 적용하기
      this.tagContainerEl.scrollBy({
        top: 0,
        left: scrollXAmount,
        behavior: 'smooth' // 부드러운 스크롤 적용하기
      })
    },

    handleCollapse() {
      EventBus.$emit('onCollapseChanged', 750)
    },
    e2eNodeClick(param) {
      const validate = param?.data?.nescode && param?.data?.nealias
      if (!validate) {
        return
      }
      this.insertTab(param.data)
    },
    async handleApiSearchClear() {
      this.searchModel = Object.assign({}, defaultSearchModel)

      this.refreshTable()

      await this.$store.commit('influenceCircitStore/RESET_PAGE_TAG_DATA')
    },

    refreshTable() {
      this.mainTableData = []
      this.paginationInfoApi.totalPages = null
      this.paginationInfoApi.totalCount = 0
    },

    async onClickSearchApi(params) {
      this.onLoadList(params)
    },

    async closeTag(tag) {
      await this.$store.dispatch('influenceCircitStore/DELETE_INFLUENCECIRCIT_PAGE_TAG', tag.tagData)
    },
    async tagChange(tag) {
      await this.$store.commit('influenceCircitStore/SET_TAG_CURRENT_CODE', tag.tagData.nescode)
    },

    async loadHeadOrgList() {
      const res = await apiSELECT_VW_ORG_MST_HQ()
      this.orgItem.HEAD.options = res.result.map((v) => {
        return { label: v.orgname, value: v.orgscode }
      })
      this.addAllOrg(this.orgItem.HEAD.options, 'HEAD')
    },
    async orgChange(changedOrg) {
      let res
      switch (changedOrg) {
        case 'HEAD':
          res = await apiGET_ORG_LIST({
            ORGSCODE: this.searchModel.ORG.HEAD
          })
          this.orgItem.CENTER.options = res.result.map((v) => {
            return { label: v.orgname, value: v.orgscode }
          })
          this.addAllOrg(this.orgItem.CENTER.options, 'CENTER')
          this.orgChange('CENTER')
          break
        case 'CENTER':
          res = await apiGET_ORG_LIST({
            ORGSCODE: this.searchModel.ORG.CENTER
          })
          this.orgItem.TEAM.options = res.result.map((v) => {
            return { label: v.orgname, value: v.orgscode }
          })
          this.addAllOrg(this.orgItem.TEAM.options, 'TEAM')
          this.orgChange('TEAM')
          break
        case 'TEAM':
          res = await apiGET_OFFICE_LIST({
            ORGSCODE: this.searchModel.ORG.TEAM
          })
          this.orgItem.OFFICE.options = res.result.map((v) => {
            return { label: v.officename, value: v.officescode }
          })
          this.addAllOrg(this.orgItem.OFFICE.options, 'OFFICE')
          break
      }
    },

    addAllOrg(options, key) {
      options.unshift({ label: '전체', value: 'ALL' })
      this.searchModel.ORG[key] = 'ALL'
    },

    async onLoadList(params) {
      if ((this.searchModel.ORG.HEAD.length === 0 || this.searchModel.ORG.HEAD === 'ALL') && this.searchModel.searchTerm.length === 0) {
        this.$message('조건을 지정해주세요')
        return
      }

      const target = ({ vue: this.$refs.selectApi.$refs.CompTemplateTable })
      this.openLoading(target)

      this.paginationInfoApi.pageSize = this.searchModel.resultCount
      const param = {
        limit: this.paginationInfoApi.pageSize,
        page: this.paginationInfoApi.currentPage,

        SEARCHTEXT: this.searchModel.searchTerm.length > 0 ? this.searchModel.searchTerm : null,
        HEADORGSCODE: this.searchModel.ORG.HEAD.length > 0 ? this.searchModel.ORG.HEAD : null,
        CSCENTERCODE: this.searchModel.ORG.CENTER.length > 0 ? this.searchModel.ORG.CENTER : null,
        MGMTORGSCODE: this.searchModel.ORG.TEAM.length > 0 ? this.searchModel.ORG.TEAM : null,
        OFFICESCODE: this.searchModel.ORG.OFFICE.length > 0 ? this.searchModel.ORG.OFFICE : null,
      }

      try {
        const res = await apiSELECT_NEA_LIST(param)
        this.mainTableData = res?.result
        this.paginationInfoApi.totalCount = res.total // 총 항목 수 설정
        this.paginationInfoApi.totalPages = Math.ceil(this.paginationInfoApi.totalCount / this.paginationInfoApi.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

    selectNealias(param) {
      this.insertTab(param.data)
    },

    insertTab(data) {
      this.$store.dispatch('influenceCircitStore/SAVE_INFLUENCECIRCIT_PAGE_TAG_DATA', data)
    },

  }
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
.influenceCircit::v-deep {
  font-family: "NanumSquare";
  .body-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
  }

  .tag-view-wrapper{
    overflow-x: auto;
    overflow-y: hidden;
    white-space: nowrap;
  }

  .svg-container {
    font-size: 100px;
    margin-bottom: 20px;
    color: rgba(109, 106, 106, 0.89); /* 아이콘 색상 변경 */
  }

  .content{
    text-align: center;
    margin-bottom: 20px;
    padding-bottom: 20px;
    color:rgba(109, 106, 106, 0.89)
  }

  .tags-view-container{
    background-clip: content-box !important;

    &>span{
      user-select: none;
    }
    z-index: 0;
    position: relative;
    display: flex;
    margin-bottom : 00px;
    background: #969696;
    padding-top:0px;
    padding-bottom: 0px;
    white-space:nowrap;
    width: 100vw;
    height: 34px;
    transform-origin: top;

    display: block;
    overflow-x: auto;
    overflow-y: hidden;

    &::-webkit-scrollbar{
      display: none;
    }

    .el-tag + .el-tag {
      margin-left: 10px !important;
      margin-top: 0px  !important;
      margin-left: 0px;
    }
    .button-new-tag {
      margin-left: 10px;
      height: 32px;
      line-height: 30px;
      padding-top: 0;
      padding-bottom: 0;
    }
    .input-new-tag {
      width: 90px;
      margin-left: 10px;
      vertical-align: bottom;
    }

    .el-tag{
      margin-left: 10px;
      border: none;
      background-color: #c2c2c2 !important;
      color: black;
      font-weight: bold;
      backface-visibility: hidden;
      border-radius: 10px;
      opacity: 0.5;
      position: relative;

      overflow: hidden;
      text-overflow: ellipsis;
      transform-origin: bottom left;
      height: auto;
      overflow: hidden;
      margin-top: 3px !important;

      .el-tag__close.el-icon-close{
        position:absolute;
        display: inline-block;
        line-height: 32px;
        left:230px;
        color: white;
      }

      &.clicked, &:hover{
        background-color: #b3d1d6 !important;
        backface-visibility: hidden;
        opacity: 1.0;
        cursor: pointer;
      }
    }
  }

  .ag-header-group-cell-label{
    display: grid !important;
    text-align: center !important;
  }

  .influenceCircitCollapse{
    border: none;
    .CompTemplate{
      padding: 0px;
    }

    .el-collapse-item__header {
      width: 30px;
      height: 100%;
      margin-left: auto;
      margin-right: -30px;
      background: #b3d1d6;
      border-radius: 20px 20px 20px 20px;

      .el-collapse-item__arrow{
        color: #2e3574;
        font-weight: bold;
        font-size: 15px;
        transform: rotate(90deg);

        &.is-active{
          transform: rotate(-90deg);
        }
      }
    }

    .el-collapse-item {
      display:flex;
      flex-direction: column;
      height: 100%;
      &>div:first-child{
        order: 2;
        height: 30px;
      }

      &>div:nth-child(2){
        order: 1;
        height: 370px;
      }

      .el-collapse-item__content{
        height: 100%;
        padding-bottom: 0px;
      }
    }
  }

  .CompTemplate{
    .ag-header{
      min-height: 71px !important;
      height: 71px !important;
    }

    .ag-header-row{
      &.ag-header-row-column-group{
        margin-top: 0px !important;
      }

      &.ag-header-row-column{
        margin-top: 35px !important;
      }
    }
  }

  .ag-overlay-panel{
    .ag-overlay-wrapper{
      .ag-overlay-no-rows-center{
        display: none;
      }
    }
  }

}
</style>
