<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="nodeInfoManagement"
      :items="searchItems"
      :search-model="searchModel"
      :is-show-graph="true"
      title="노드 정보 조회"
      class="w-100 h-50"
      @searchClear="handleSearchClear"
    >
      <template #grafana>
        <div style="border: 1px solid rgb(172, 169, 169);margin-bottom: 10px;">
          <iframe
            :src="
              `${grafanaUrl}`
                + dashboardMap.TOTAL
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.nowTimestamp}&to=${twoFourTimeStampMap.nowTimestamp}`
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="100%"
            height="350vh"
            frameborder="0"
          />
        </div>

        <div v-for="(target, index) in searchModel.target" :key="index">
          <iframe
            v-for="(dashboardURL, index2) in dashboardMap[target]"
            :key="index2"
            :src="
              `${grafanaUrl}`
                + `${dashboardURL}`
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + '&var-job=ap01&var-job=ap02&var-job=apigw01&var-job=apigw02&var-job=apigw03&var-job=apigw04&var-job=bdata01&var-job=bdata02&var-job=bdata03&var-job=d02&var-job=portal01&var-job=w01&var-job=w02'
                + (hasVariable(dashboardURL) ? nodeVariable : '')
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="100%"
            height="400vh"
            frameborder="0"
          />
        </div>
      </template>
    </DataHubComponent>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import DataHubComponent from '@/views-dataHub/components/CompTemplate'
import { makeGrafanaVariable, grafanaDashboardUrlMap, grafanaSettings } from '@/views-dataHub/grafanaManager'
const routeName = 'NodeInfoManagement'
import _ from 'lodash'

export default {
    name: routeName,
    components: { DataHubComponent },
    extends: Base,
    data() {
        return {
          name: routeName,
          src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
          searchItems: [
            {
              label: '노드', type: 'select', size: 8, multiple: false, model: 'nodeString',
              options: [
                { label: 'ap01', value: 'ap01' },
                { label: 'ap02', value: 'ap02' },
                { label: 'apigw01', value: 'apigw01' },
                { label: 'apigw02', value: 'apigw02' },
                { label: 'apigw03', value: 'apigw03' },
                { label: 'apigw04', value: 'apigw04' },
                { label: 'bdata01', value: 'bdata01' },
                { label: 'bdata02', value: 'bdata02' },
                { label: 'bdata03', value: 'bdata03' },
                { label: 'd02', value: 'd02' },
                { label: 'portal01', value: 'portal01' },
                { label: 'w01', value: 'w01' },
                { label: 'w02', value: 'w02' },
              ]
            },
            {
              label: '자원', type: 'select', size: 8, multiple: true, model: 'target',
              options: [
                { label: 'CPU', value: 'CPU' }, { label: 'DISK', value: 'DISK' }, { label: 'MEMORY', value: 'MEMORY' }
              ]
            }

          ],
          searchModel: {
            nodeString: 'ap01',
            target: ['CPU', 'DISK', 'MEMORY']
          },

          twoFourTimeStampMap: {
            fromTimestamp: new Date().getTime() - 24 * 60 * 60 * 1000,
            toTimestamp: new Date().getTime(),
            nowTimestamp: new Date().getTime()
          }

        }
  },

  computed: {
    dashboardMap() {
      return {
              TOTAL: grafanaDashboardUrlMap.nodeExporter.nodeTotal,
              CPU: [grafanaDashboardUrlMap.nodeExporter.nodeCpu, grafanaDashboardUrlMap.nodeExporter.currentTotalCpu, grafanaDashboardUrlMap.nodeExporter.realTimeCpu],
              DISK: [grafanaDashboardUrlMap.nodeExporter.nodeDisk, grafanaDashboardUrlMap.nodeExporter.currentTotalDisk, grafanaDashboardUrlMap.nodeExporter.realTimeDisk],
              MEMORY: [grafanaDashboardUrlMap.nodeExporter.nodeMemory, grafanaDashboardUrlMap.nodeExporter.currentTotalMemory, grafanaDashboardUrlMap.nodeExporter.realTimeMemory]
           }
    },
    grafanaUrl () {
      return this.$store.state.app.server?.grafanaUrl || ''
    },

    nodeVariable() {
      return makeGrafanaVariable(false, 'job_solo', this.searchModel.nodeString)
    },

    getGrafanaRefreshTime() {
      return grafanaSettings.refreshTime
    }

  },

  async mounted() {

  },

  methods: {
    hasVariable(dashboardURL) {
      const hasVariableUrl = [
        grafanaDashboardUrlMap.nodeExporter.realTimeCpu,
        grafanaDashboardUrlMap.nodeExporter.realTimeDisk,
        grafanaDashboardUrlMap.nodeExporter.realTimeMemory
      ]
      return hasVariableUrl.includes(dashboardURL)
    },

    handleSearchClear() {
      this.searchModel.nodeString = this.searchItems.find((item) => item.model === 'nodeString').options[0].value
      this.refreashFrame()
    },

    refreashFrame() {
      this.twoFourTimeStampMap = {
        fromTimestamp: new Date().getTime() - 24 * 60 * 60 * 1000,
        toTimestamp: new Date().getTime(),
        nowTimestamp: new Date().getTime()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.NodeInfoManagement {
    overflow: scroll;
  .CompTemplate::v-deep {
    #grafana {
      height: 100%;
      padding: 5px;
      .garafana-section {
        padding: 5px 15px;
      }
    }
  }
}
</style>

