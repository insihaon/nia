<template>
  <div style="height: 100%" :class="{[name]: true}">
    <splitpanes
      :horizontal="false"
      class="default-theme"
    >
      <pane class="leftPane" max-size="25">
        <div style="height: 125px">
          <p class="leftContainerTitle dashIcon_02">
            Project: <span style="color: red">{{ appOptions.project }}</span> <strong>컴포넌트</strong>테스터
          </p>
          <el-button type="info" style="background-color:#7a7b8d; width: 100%">
            컴포넌트 리스트
          </el-button>
          <div style="display:flex; margin-top: 10px">
            <label style="text-wrap: nowrap; margin-right: 10px">검색</label>
            <el-input
              v-model="searchText"
              type="text"
              clearable
              placeholder="검색어를 입력하세요"
            />
          </div>
        </div>
        <div style="height: calc(100% - 135px)">
          <el-card class="treeCard" :style="{ position: 'relative' }">
            <div
              style="
                position: absolute;
                height: 100%;
                overflow-y: scroll;
                left: 0px;
                top: 0;
                right: 0px;
                box-sizing: border-box;
                "
            >
              <el-tree
                v-if="defaultComponentTreeKey.length > 0"
                ref="elTree"
                node-key="componentPath"
                icon-class="el-icon-arrow-right"
                accordion
                empty-text="데이터가 없습니다."
                :data="treeData"
                :draggable="false"
                :current-node-key="defaultComponentTreeKey"
                :show-checkbox="false"
                :render-content="renderContent"
                :expand-on-click-node="false"
                :props="defaultTreeProps"
                @node-click="nodeClick"
                @node-expand="nodeExpand"
              />
            </div>
          </el-card>
          <el-tabs type="border-card" style="flex:1">
            <el-tab-pane label="Props" style="height: 100%">
              <div style="height: 100%; overflow-y: auto; border-top: 1px solid">
                <div class="componentOptionTitleBox">
                  <span style="margin-right: 10px">컴포넌트 Props</span><span style="color: red;">※ 0.5초 딜레이가 있습니다.</span>
                </div>
                <div style="display: flex; flex-wrap:wrap; padding-top: 10px">
                  <div
                    v-for="(testPropKey, index) of Object.keys(currentComponentConfig.testProps)"
                    v-show="testPropKey !== 'propChangeIndex'"
                    :key="index"
                    style="width: 100%"
                  >
                    <TypeComponent
                      v-if="testPropKey !== 'propChangeIndex'"
                      :prop-key="testPropKey"
                      :prop-data.sync="currentComponentConfig.testProps[testPropKey]"
                      @changeDataValue="changeTestProps"
                    />
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="Event" style="height: 100%">
              <div style="height: 100%; overflow-y: auto; border-top: 1px solid; padding: 10px">
                <div class="componentOptionTitleBox">
                  <span style="margin-right: 10px">컴포넌트 Events</span>
                  <div>
                    <el-button size="mini" @click="eventStateReset">상태 초기화</el-button>
                  </div>
                </div>
                <div style="height: calc(100% - 40px)">
                  <div
                    v-for="(emitConfigElementKey, index) of Object.keys(currentComponentConfig.emitConfig)"
                    :key="index"
                    style="
                      border: 1px solid; width: 100%; display: inline-block; padding: 10px;
                      max-height: 300px; overflow-y: auto
                      "
                  >
                    <div>
                      {{ emitConfigElementKey }} [ 발생횟수 : {{ currentComponentConfig.emitConfig[emitConfigElementKey].emitCount }} ]
                    </div>
                    <div style="margin-top: 10px">
                      전달된 Event Data
                      <div
                        v-for="(emitParam, emitParamIndex) in currentComponentConfig.emitConfig[emitConfigElementKey].emitParamList"
                        :key="emitParamIndex"
                      >
                        <TypeComponent
                          class="emitParamsTypeComponent"
                          :prop-key="String(emitParamIndex + '번째 param')"
                          :prop-data.sync="currentComponentConfig.emitConfig[emitConfigElementKey].emitParamList[emitParamIndex]"
                          :json-editor-disabled="true"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </pane>
      <pane class="rightPane">
        <el-row type="flex" align="bottom" style="min-height:70px; padding: 10px;">
          <div style="display:flex; width: 100%">
            <span style="margin-right: 15px">
              <el-breadcrumb v-if="!!currentComponentConfig.selectedComponent.componentAlias" separator-class="el-icon-arrow-right icon-bold">
                <el-breadcrumb-item v-for="(item, index) in currentComponentConfig.selectedComponent.componentPath.split('/')" :key="index">
                  <span v-if="index === 0" style="color: white; font-weight: bold; background-color: rgb(122, 123, 141); font-size: 20px">컴포넌트 경로</span>
                  <span class="selectionTreeItem">
                    {{ item }}
                  </span>
                </el-breadcrumb-item>
              </el-breadcrumb>
            </span>
            <el-tooltip :disabled="isAutoTestExist" content="컴포넌트 테스트를 위해서는 autotestFuction에 테스트할 컴포넌트를 등록하세요" placement="bottom">
              <el-button style="position:relative; bottom: 10px" :disabled="!isAutoTestExist" @click="onAutoTest">컴포넌트 자동 테스트</el-button>
            </el-tooltip>
            <div style="text-align: right; flex:1">
              <el-tooltip content="컴포넌트의 Prop과 Event를 저장합니다." placement="bottom">
                <el-button
                  :style="{'background-color': currentComponentConfig.isSaveStatus ? 'orange' : 'yellow'}"
                  style="position:relative; bottom: 10px; color: blue"
                  @click="saveComponentState"
                >상태저장</el-button>
              </el-tooltip>
            </div>
          </div>
        </el-row>
        <el-row style="height: calc(100% - 100px); border: 1px solid; padding: 10px;" align="middle">
          <div ref="componentDiv" style="height: 100%">
            <el-col :span="24" style="height: 100%">
              <div style="height: 100%; overflow-y: auto">
                <component
                  :is="currentComponentConfig.selectedComponent.component"
                  ref="testComponent"
                  :style="{height: 'calc(100% - 25px)'}"
                  v-bind.sync="currentComponentConfig.testProps"
                  @initComponentData="setInitCurrentComponentData"
                  @devEmit="changeEmitState"
                />
              </div>
            </el-col>
          </div>
        </el-row>
      </pane>
    </splitpanes>

    <WorkControlModalTemplate
      v-for="window in $store.getters.windows"
      :key="window.id"
      :type="window.type"
      :wdata="window"
      :target="window.target"
    />
  </div>
</template>

<script>
import TypeComponent from '@/test/TypeComponent'
import WorkControlModalTemplate from '@/components/WorkControlModalTemplate/index'
import { Base } from '@/min/Base.min'
import { mapState } from 'vuex'
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
import _ from 'lodash'
import { testerConstants } from '@/test/commonTester.js'
import { AutoTestManager } from '@/test/autoTest/autoTestManager'

const routeName = 'ComponentTestPage'

const defaultEmitConfigElement = {
  emitCount: 0, emitParamList: []
}

const defaultCurrentComponentConfig = {
  // 현재 선택된 컴포넌트 정보
  selectedComponent: {}, // 선택된 컴포넌트 정보
  testProps: { propChangeIndex: 0 }, // 테스트할 때 사용되는 props (propChangeIndex는 변경을 감지할 값이다.)
  emitConfig: { /* 구조 : [특정 emit명] : defaultEmitConfigElement */ },
  existComponentAutoTest: testerConstants.notExist,
  isSaveStatus: false
}

export default {
  name: routeName,
  components: { TypeComponent, WorkControlModalTemplate, Splitpanes, Pane },
  extends: Base,
  data() {
    return {
      name: routeName,
      defaultTreeProps: {
        label: 'componentAlias',
        children: 'children'
      },

      currentComponentConfig: _.cloneDeep(defaultCurrentComponentConfig),
      searchText: ''
    }
  },

  computed: {
      ...mapState({
        testComponentList: state => state.testComponentTreeDataStore.testComponentList,
        componentTreeData: state => state.testComponentTreeDataStore.componentTreeData,
        defaultComponentTreeKey: state => state.testComponentTreeDataStore.defaultComponentTreeKey,
        savedComponentConfigMap: state => state.testComponentPersisted.savedComponentConfigMap
      }),

    treeData() {
      if (this.searchText.length > 0) {
        return this.componentTreeData.filter(treeData => treeData.componentAlias.toLowerCase().includes(this.searchText.toLowerCase()))
      } else {
        return this.componentTreeData
      }
    },

    isAutoTestExist() {
      return this.currentComponentConfig.existComponentAutoTest === testerConstants.exist
    },

    componentFilePath() {
      const fileName = this.currentComponentConfig.selectedComponent.component.__file
      return this.getComponentPath(fileName)
    }
  },

  watch: {
    async '$route.params.componentName'() {
      await this.setCurrentComponent()
    },

    async componentTreeData() {
      // 새로고침으로 컴포넌트를 로딩했을때
      await this.setCurrentComponent()
    },
  },

  async mounted() {
    window.v = this
    // 전체 컴포넌트 리스트 셋팅
    this.$store.dispatch('testComponentTreeDataStore/initTestComponentList')
  },

  destroyed() { },

  methods: {
    getComponentPath(fileName) {
      fileName = fileName.replaceAll('\/', '_')
      const componentPath = fileName.replaceAll('\.vue', '')
      return componentPath
    },

    changeEmitState(data) {
      this.propChangeIndexUp()

      const existEmitElementToggle = Object.keys(this.currentComponentConfig.emitConfig).find((emitConfigElementKey) => {
        return emitConfigElementKey === data.emitKey
      })

      if (!existEmitElementToggle) {
        this.currentComponentConfig.emitConfig[data.emitKey] = _.cloneDeep(defaultEmitConfigElement)
      }
      this.currentComponentConfig.emitConfig[data.emitKey].emitCount++
      this.currentComponentConfig.emitConfig[data.emitKey].emitParamList.push(data.param)

      // 변경된 상태저장
      this.saveStatus()
    },

    changeTestProps(param) {
      // 1. Prop 변경
      this.$set(this.currentComponentConfig.testProps, param.objectKey, param.dataValue)

      // 2. Prop 변경 인식
      this.propChangeIndexUp()

      // 3. 변경된 Prop을 새로 적용시키기 위해 컴포넌트 재호출
      this.debounceReload(this)

      // 변경된 상태저장
      this.saveStatus()
    },

    debounceReload: _.debounce((THIS) => {
      THIS.reloadComponent()
    }, 500),

    reloadComponent() {
      const tempComponent = this.currentComponentConfig.selectedComponent.component
      this.currentComponentConfig.selectedComponent.component = undefined
      const loading = this.$loading({ target: this.$refs.componentDiv })
      setTimeout(() => {
        this.currentComponentConfig.selectedComponent.component = tempComponent
        loading.close()
      }, 1000)
    },

    // Tree Node Rendering
    renderContent(h, { node, data, store }) {
      node.isLeaf = !(data.children && data.children.length > 0)
      return (
        <span class='custom-tree-node'>
          <span>{node.data.componentAlias}</span>
        </span>
      )
    },

    propChangeIndexUp() {
      ++this.currentComponentConfig.testProps.propChangeIndex
    },

    setInitCurrentComponentData(data) {
      if (data.propMap) {
        this.notExistSamplePropsDefaultValueSet(data.propMap)
      }
    },

    // Tree 클릭 이벤트
    async nodeClick(data, node) {
      if (this.$route.params.componentName && this.$route.params.componentName.length > 0) {
        if (this.$route.params.componentName === data.componentAlias) {
          return
        }
      }

      this.$router.push({ path: '/ComponentTestPage/' + data.componentAlias })
    },

    resetCurrentComponentConfig() {
      this.currentComponentConfig = _.cloneDeep(defaultCurrentComponentConfig)
      this.currentComponentConfig.selectedComponent = null
      AutoTestManager.autoTestManagerReset()
    },

    eventStateReset() {
      const temp = _.cloneDeep(this.currentComponentConfig.emitConfig)
      this.currentComponentConfig.emitConfig = {}
      Object.keys(temp).forEach((emitConfigElementKey) => {
        this.$set(this.currentComponentConfig.emitConfig, emitConfigElementKey, _.cloneDeep(defaultEmitConfigElement))
      })

      this.saveStatus()
    },

    notExistSamplePropsDefaultValueSet(propMap) {
      Object.keys(propMap).forEach((propKey) => {
        if (!Object.hasOwnProperty.call(this.currentComponentConfig.testProps, propKey)) {
          this.currentComponentConfig.testProps[propKey] = propMap[propKey]
        }
      })

      this.propChangeIndexUp()
    },

    // Tree 확장 이벤트
    nodeExpand() { },

    async setCurrentComponent() {
      this.resetCurrentComponentConfig() // 이전 컴포넌트의 설정을 초기화 시킨다.

      let currentComponent
      if (this.componentTreeData.length > 0) {
        if (this.$route.params.componentName && this.$route.params.componentName.length > 0) {
          const findComponent = this.componentTreeData.find((treeData) => treeData.componentAlias === this.$route.params.componentName)
          if (findComponent) {
            currentComponent = this.currentComponentConfig.selectedComponent = findComponent
          }
        }
        const realComponent = currentComponent || this.componentTreeData[0]
        const fileName = realComponent.component.__file
        const filePath = this.getComponentPath(fileName)

        if (Object.hasOwnProperty.call(this.savedComponentConfigMap, filePath)) {
          this.currentComponentConfig = this.savedComponentConfigMap[filePath]
        } else {
          this.loadSampleJsonData()
        }
        this.currentComponentConfig.selectedComponent = realComponent
        this.autoTestExistCheck()
        this.$store.commit('testComponentTreeDataStore/SET_DEFAULT_COMPONENT_TREE_KEY', this.currentComponentConfig.selectedComponent.componentPath)
      }
    },

    autoTestExistCheck() {
      if (Object.hasOwnProperty.call(AutoTestManager.instance.autoTestFunctionMap, this.componentFilePath)) {
        this.currentComponentConfig.existComponentAutoTest = testerConstants.exist
      }
    },

    loadSampleJsonData() {
      try {
        const sampleData = require(`./jsonData/${this.componentFilePath}.json`)
        if (sampleData) {
          Object.keys(sampleData).forEach((sampleDataKey) => {
            this.currentComponentConfig.testProps[sampleDataKey] = sampleData[sampleDataKey]
          })
        }
      } catch (e) {
        console.error(this.componentFilePath + '은 sample Json Data가 없습니다.')
      }
    },

    onAutoTest(clickEvent, allToggle) {
      if (this.currentComponentConfig.existComponentAutoTest === testerConstants.notExist) {
        this.$message.error({ message: `해당 컴포넌트는 autoTest가 등록되지 않았습니다.` })
      } else {
        AutoTestManager.instance.runAutoTest(this.componentFilePath, this, this.$refs.testComponent)
      }
    },

    saveComponentState() {
      const isSaveStatus = this.currentComponentConfig.isSaveStatus = !this.currentComponentConfig.isSaveStatus
      if (isSaveStatus) {
        this.saveStatus()
      } else {
        this.$store.commit('testComponentPersisted/DEL_SAVED_COMPONENT_DATA', this.componentFilePath)
      }
    },

    saveStatus() {
      const isSaveStatus = this.currentComponentConfig.isSaveStatus
      if (isSaveStatus) {
        this.$store.commit('testComponentPersisted/SAVE_COMPONENT_DATA', { path: this.componentFilePath, data: this.currentComponentConfig })
      }
    }
  },

}
</script>

<style lang="scss" scoped>
#app{
  padding:0; margin:0;
}

.ComponentTestPage{
  .el-tree::v-deep{
    .el-tree-node.is-current{
      z-index: 1000;
      background-color: #7a7b8d;
      .el-tree-node__content{
        background-color: #7a7b8d;
        color: white
      }
    }
    .el-tree-node:hover {
      background-color: #7a7b8d;
      .el-tree-node__content{
        background-color: #7a7b8d;
        color:white
      }
    }
  }

  .componentPropTestElement{
    width: 50%;
    display: inline-block;
  }

  .el-loading-mask{
    z-index: 3;
  }
  .leftPane{
    display: flex;
    height: 100%;
    flex-direction: column;
    padding: 10px;
  }

  .rightPane{
    padding: 10px 20px 0px 20px;
  }
  p.leftPaneTitle{
    font-size:20px;
    color:#222222;
    letter-spacing: -0.5px;
    margin-bottom:8px;
    margin-top:0;
    font-weight: 200;
    background-repeat: no-repeat;
    background-position: 0 center;
    text-indent: 28px;
  }
  p.dashIcon_02{
    background-image: url("/../assets/images/dash_icon_title_01.png");
    background-position: 0 1px;
  }

  .leftPane .treeCard{
    height: 40%;
    max-height: 500px
  }
  .leftPane>.searchOptionCard{
    height:130px;
  }

  .leftPane>.searchOptionCard table{
    border-collapse: collapse; margin-bottom:5px;
  }
  .leftPane>.searchOptionCard table th{
    white-space: nowrap; text-align: center; font-size:14px; font-weight: 400; letter-spacing: -0.5px; min-width: 100px;
  }
  .leftPane>.searchOptionCard table th,
  .leftPane>.searchOptionCard table td{
    /* border:1px solid #EEEEEE; */
    padding:4px 0px;
  }
  .selectionTreeItem{
    font-size:16px;
    font-weight: 600;
  }
  .selectionTreeItem>i{
    background-color:#68747999;
    color:#FFFFFF;
    border-radius: 100%;
    padding:5px;
    font-size:13px;
  }

  .splitpanes__pane{
    background: white;
  }

  .el-tabs::v-deep{
    height: calc(100% - 500px);
    min-height: 60%;
    .el-tabs__content{
      height:calc(100% - 40px);
    }
  }

  .emitParamsTypeComponent::v-deep{
    .el-input{
      min-width: 130px !important;
      max-width: 130px !important;
    }
  }

  .componentOptionTitleBox{
    height: 30px;
    display: flex;
    margin-bottom: 10px;
  }

}

</style>
<style>
.cmdDashboardGrid{
  border:1px solid #999999 !important;
  border-radius:5px;
  overflow: hidden;
}
.cmdDashboardGrid>div.el-table__header-wrapper>table.el-table__header>thead>tr>th{
  background-color:#7a7b8d !important; font-size:12px !important;
}
.cmdDashboardGrid>div.el-table__header-wrapper>table.el-table__header>thead>tr>th>div{
  color:#FFFFFF !important;
  white-space: nowrap; line-height:18px !important;
}
.cmdDashboardGrid>div.el-table__body-wrapper>table.el-table__body>tbody>tr{
  border:10px solid #000000 !important;
}
.cmdDashboardGrid>div.el-table__body-wrapper>table.el-table__body>tbody>tr.el-table__row>td{
  font-size:12px; color:#000000 !important;
  /* -webkit-user-select: none; */
}
.cmdDashboardGrid>div.el-table__body-wrapper>table.el-table__body>tbody>tr.el-table__row>td>div.cell{
  color:#000000 !important;  line-height:18px !important; font-size:14px !important; font-weight: 600 !important;
}

</style>
