<template>
  <div style="height: 100%" :class="{[name]: true}">
    <el-container style="height: 100%">
      <el-aside width="350px" style="padding: 30px 0 0 20px;">
        <div class="leftContainer">
          <p class="leftContainerTitle dashIcon_02">
            Project: <span style="color: red">{{ appOptions.project }}</span> <strong>컴포넌트</strong>테스터
          </p>
          <el-button type="info" style="background-color:#7a7b8d;">
            컴포넌트 리스트
          </el-button>
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
                v-if="testComponentConfig.defaultComponentTreeKey.length > 0"
                ref="elTree"
                node-key="componentPath"
                icon-class="el-icon-arrow-right"
                accordion
                empty-text="데이터가 없습니다."
                :data="testComponentConfig.componentTreeData"
                :draggable="false"
                :current-node-key="testComponentConfig.defaultComponentTreeKey"
                :show-checkbox="false"
                :render-content="renderContent"
                :expand-on-click-node="false"
                :props="defaultTreeProps"
                @node-click="nodeClick"
                @node-expand="nodeExpand"
              />
            </div>
          </el-card>
        </div>
      </el-aside>

      <el-container>
        <el-main class="rightContainer">
          <el-row type="flex" align="bottom" style="min-height:70px; padding: 10px 0px 10px 0px;">
            <el-col :span="20">
              <el-breadcrumb v-if="!!currentComponentConfig.selectedComponent.componentAlias" separator-class="el-icon-arrow-right icon-bold">
                <el-breadcrumb-item v-for="(item, index) in currentComponentConfig.selectedComponent.componentPath.split('/')" :key="index">
                  <span v-if="index === 0" style="color: blue; font-weight: bold">[ 파일 경로 ]</span>
                  <span class="selectionTreeItem">
                    {{ item }}
                  </span>
                </el-breadcrumb-item>
              </el-breadcrumb>
            </el-col>
          </el-row>
          <el-row style="height: calc(100% - 100px); border: 1px solid; padding: 10px;" align="middle">
            <el-col :span="24" style="height: 100%">
              <div ref="testComponentBox" style="height: 33%; overflow-y: auto">
                <span>컴포넌트 {{ currentComponentConfig.selectedComponent.componentAlias }}</span>
                <component
                  :is="currentComponentConfig.selectedComponent.component"
                  ref="testComponent"
                  v-bind.sync="currentComponentConfig.testProps"
                  @emitComponentData="setInitCurrentComponentData"
                  @runEmit="setEmitState"
                />
              </div>
              <div style="height: 33%; overflow-y: auto; border-top: 1px solid">
                <span>컴포넌트 Props</span>
                <div style="display: flex; flex-wrap:wrap; padding-top: 10px">
                  <div
                    v-for="(testPropKey, index) of Object.keys(currentComponentConfig.testProps)"
                    v-show="testPropKey !== 'propChangeIndex'"
                    :key="index"
                    style="width: 50% !important"
                  >
                    <TypeComponent
                      :prop-key="testPropKey"
                      :prop-data.sync="currentComponentConfig.testProps[testPropKey]"
                      @changeDataValue="changeTestProps"
                    />
                  </div>
                </div>
              </div>
              <div style="height: 33%; overflow-y: auto; border-top: 1px solid">
                <div style="height: 30px">
                  <span>컴포넌트 Events</span>
                  <div style="float:right">
                    <el-button size="mini" @click="eventStateReset">상태 초기화</el-button>
                  </div>
                </div>
                <div style="height: calc(100% - 30px)">
                  <div
                    v-for="(emitConfigElementKey, index) of Object.keys(currentComponentConfig.emitConfig)"
                    :key="index"
                    style="border: 1px solid; width: 45%; display: inline-block; margin-right:5%; padding: 10px;
                    max-height: 300px; overflow-y: auto
                    "
                  >
                    <div>
                      {{ emitConfigElementKey }} [ 발생횟수 : {{ currentComponentConfig.emitConfig[emitConfigElementKey].emitCount }} ]
                    </div>

                    <div style="margin-top: 10px">
                      emitParam 리스트
                      <div
                        v-for="(emitParam, emitParamIndex) in currentComponentConfig.emitConfig[emitConfigElementKey].emitParamList"
                        :key="emitParamIndex"
                      >
                        <TypeComponent
                          :prop-key="String(emitParamIndex + '번째 param')"
                          :prop-data.sync="currentComponentConfig.emitConfig[emitConfigElementKey].emitParamList[emitParamIndex]"
                          :json-editor-disabled="true"
                        />
                        <!-- @changeDataValue="changeTestProps" -->
                      </div>

                    </div>

                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
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
import router from '@/router/index.js'
import TypeComponent from '@/test/TypeComponent'
import WorkControlModalTemplate from '@/components/WorkControlModalTemplate/index'
import { Base } from '@/min/Base.min'
import _ from 'lodash'
const routeName = 'ComponentTestPage'

const defaultEmitConfigElement = {
  emitCount: 0, emitParamList: []
}

export default {
  name: routeName,
  components: { TypeComponent, WorkControlModalTemplate },
  extends: Base,
  data() {
    return {
      name: routeName,
      defaultTreeProps: {
        label: 'componentAlias',
        children: 'children'
      },

      duplicateCheckFileObject: {},
      testComponentConfig: {
        // test용 컴포넌트 정보들
        testComponentList: [], //
        componentTreeData: [],
        defaultComponentTreeKey: ''
      },

      currentComponentConfig: {
        // 현재 선택된 컴포넌트 정보
        selectedComponent: {}, // 선택된 컴포넌트 정보
        testProps: { propChangeIndex: 0 }, // 테스트할 때 사용되는 props (propChangeIndex는 변경을 감지할 값이다.)
        emitConfig: {
          /*
            구조 : [특정 emit명] : defaultEmitConfigElement
          */
        },
      }
    }
  },

  async mounted() {
    window.v = this

    // 컴포넌트 셋팅
    await this.setComponentList()
    this.componentListMakeTreeData()
    this.setDefaultComponent()
  },

  destroyed() { },

  methods: {
    eventStateReset() {
      const temp = _.cloneDeep(this.currentComponentConfig.emitConfig)
      this.currentComponentConfig.emitConfig = {}
      Object.keys(temp).forEach((emitConfigElementKey) => {
        this.$set(this.currentComponentConfig.emitConfig, emitConfigElementKey, _.cloneDeep(defaultEmitConfigElement))
      })
    },

    setEmitState(data) {
      this.propChangeIndexUp()

      const existEmitElementToggle = Object.keys(this.currentComponentConfig.emitConfig).find((emitConfigElementKey) => {
        return emitConfigElementKey === data.emitKey
      })

      if (!existEmitElementToggle) {
        this.currentComponentConfig.emitConfig[data.emitKey] = _.cloneDeep(defaultEmitConfigElement)
      }
      this.currentComponentConfig.emitConfig[data.emitKey].emitCount++
      this.currentComponentConfig.emitConfig[data.emitKey].emitParamList.push(data.param)
    },

    changeTestProps(param) {
      // 1. Prop 변경
      this.$set(this.currentComponentConfig.testProps, param.objectKey, param.dataValue)

      // 2. Prop 변경 인식
      setTimeout(() => {
        this.propChangeIndexUp()
      }, 1000)

      // 3. 변경된 Prop을 새로 적용시키기 위해 컴포넌트 재호출
      this.reloadComponent()
    },

    reloadComponent() {
      const tempComponentCache = this.currentComponentConfig.selectedComponent.component
      this.currentComponentConfig.selectedComponent.component = undefined
      const loading = this.$loading({ target: this.$refs.testComponentBox })
      setTimeout(() => {
        this.currentComponentConfig.selectedComponent.component = tempComponentCache
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
        this.initTestProps(data.propMap)
      }

      if (data.emitKeys) {
        data.emitKeys.forEach((k) => {
          this.currentComponentConfig.emitConfig[k] = _.cloneDeep(defaultEmitConfigElement)
        })
      }
    },

    // Tree 클릭 이벤트
    nodeClick(data, node) {
      this.currentComponentConfig.selectedComponent = data
    },

    initTestProps(propMap) {
      this.currentComponentConfig.testProps = { propChangeIndex: 0 }

      Object.keys(propMap).forEach((propKey) => {
        this.currentComponentConfig.testProps[propKey] = propMap[propKey]
      })
    },

    // Tree 확장 이벤트
    nodeExpand() { },

    setDefaultComponent() {
      this.currentComponentConfig.selectedComponent = this.testComponentConfig.componentTreeData[0]
      this.testComponentConfig.defaultComponentTreeKey = this.currentComponentConfig.selectedComponent.componentPath
    },

    async setComponentList() {
        this.duplicateCheckFileObject = {}
        this.testComponentConfig.testComponentList = []

        const testRoutes = [...router.options.routes, ...router.options.routes2]
        for (const route of testRoutes) {
          this.recursiveSetComponent(route.component)

          if (route.children) {
            for (const child of route.children) {
              const childComponent = await child.component()
              this.recursiveSetComponent(childComponent.default)
            }
          }
        }
    },

    recursiveSetComponent(component) {
      if (!Object.hasOwnProperty.call(this.duplicateCheckFileObject, component.__file)) {
        this.duplicateCheckFileObject[component.__file] = component
        if (component.mixins) {
          const testComponent = component.mixins.find(mixin => mixin.__file === 'src/test/ComponentTesterMixins.vue')
          testComponent && this.testComponentConfig.testComponentList.push(component)
        }

        if (component.components) {
          Object.keys(component.components).forEach((childComponent) => {
            this.recursiveSetComponent(component.components[childComponent])
          })
        }
      }
    },

    componentListMakeTreeData() {
      this.testComponentConfig.componentTreeData = []

      this.testComponentConfig.testComponentList.forEach((testComponent) => {
        this.testComponentConfig.componentTreeData.push({
            componentPath: testComponent.__file,
            componentAlias: testComponent.__file.split('/').pop(),
            component: testComponent
          })
      })
    }
  }
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
      color:white;
    }
    .el-tree-node__content:hover {
      &:hover{
        background-color: #7a7b8d;
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
  .leftContainer{
    display: flex;
    flex-direction: column;
    gap:10px;
  }
  p.leftContainerTitle{
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

  .leftContainer>.treeCard{
    height:calc(100vh - 435px );
  }
  .leftContainer>.searchOptionCard{
    height:130px;
  }

  .leftContainer>.searchOptionCard table{
    border-collapse: collapse; margin-bottom:5px;
  }
  .leftContainer>.searchOptionCard table th{
    white-space: nowrap; text-align: center; font-size:14px; font-weight: 400; letter-spacing: -0.5px; min-width: 100px;
  }
  .leftContainer>.searchOptionCard table th,
  .leftContainer>.searchOptionCard table td{
    /* border:1px solid #EEEEEE; */
    padding:4px 0px;
  }

  .rightContainer{
    padding:10px 20px 0px 20px !important;
    height: 100%;
    width: 100%;
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
  -webkit-user-select: none;
}
.cmdDashboardGrid>div.el-table__body-wrapper>table.el-table__body>tbody>tr.el-table__row>td>div.cell{
  color:#000000 !important;  line-height:18px !important; font-size:14px !important; font-weight: 600 !important;
}

</style>ㅑ
