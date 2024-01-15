<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="collectionAnalysis"
      :items="searchItems"
      :search-model="searchModel"
      :is-show-graph="true"
      title="실시간 수집, 적재현황 조회"
      class="w-100 h-50"
      @searchClear="handleSearchClear"
    >
      <template #grafana>
        <div style="border: 1px solid rgb(172, 169, 169);margin-bottom: 10px; display:flex; flex-wrap:wrap">
          <iframe
            :src="
              grafanaUrl
                + dashboardMap.logStash.pipelineStatus
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + logstashTableVariable
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="100%"
            height="400px"
            frameborder="0"
          />
          <iframe
            :src="
              grafanaUrl
                + dashboardMap.logStash.totalEventsInOut
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + logstashTableVariable
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="30%"
            height="400px"
            frameborder="0"
          />
          <iframe
            :src="
              grafanaUrl
                + dashboardMap.logStash.eventsProcessTimes
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="70%"
            height="400px"
            frameborder="0"
          />
          <iframe
            :src="
              grafanaUrl
                + dashboardMap.kafka.bytesInOutPerTopic
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + kafkaTableVariable
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="100%"
            height="400px"
            frameborder="0"
          />
          <iframe
            :src="
              grafanaUrl
                + dashboardMap.kafka.messagesInPerTopic
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + kafkaTableVariable
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="50%"
            height="400px"
            frameborder="0"
          />
          <iframe
            :src="
              grafanaUrl
                + dashboardMap.kafka.bytesInPerTopic
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + kafkaTableVariable
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="50%"
            height="400px"
            frameborder="0"
          />
          <iframe
            :src="
              grafanaUrl
                + dashboardMap.kafka.kafkaLogSizeByTopic
                + `&theme=${dataHub.grafanaSetting.theme}`
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + kafkaTableVariable
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="100%"
            height="400px"
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
import { apiSelectApicodeList, apiSelectTbMetaDataSetMst } from '@/api/dataHub'
import { makeGrafanaVariable, grafanaDashboardUrlMap, grafanaSettings } from '@/views-dataHub/grafanaManager'
import _ from 'lodash'
const routeName = 'collectionAnalysis'
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
              label: '테이블', placeholder: '테이블명을 검색하세요', type: 'select', size: 8, model: 'table', multiple: false, options: []
            },
          ],
          searchModel: {
            table: 'tb_if_mst',
            code: ''
          },
          twoFourTimeStampMap: {
            fromTimestamp: new Date().getTime() - 24 * 60 * 60 * 1000,
            toTimestamp: new Date().getTime(),
            nowTimestamp: new Date().getTime()
          },

      }
    },

  computed: {
    dashboardMap() {
      return grafanaDashboardUrlMap
    },
    grafanaUrl () {
      return this.$store.state.app.server?.grafanaUrl || ''
    },

    logstashTableVariable() {
      return makeGrafanaVariable(true, 'pipeline', [this.searchModel.table]).toLowerCase()
    },

    kafkaTableVariable() {
      return makeGrafanaVariable(true, 'topic', [this.searchModel.table]).toLowerCase()
    },

    getGrafanaRefreshTime() {
      return grafanaSettings.refreshTime
    }

  },

  async mounted() {
    this.selectCodeList()
    this.loadTableMetaData()
  },

  methods: {
    async loadTableMetaData() {
    try {
      const res = await apiSelectTbMetaDataSetMst()
      const tableMetaData = res.result.map(item => ({ label: item.table_nm, value: item.table_nm }))
      const tableNmItem = this.searchItems.find(item => item.model === 'table')
      if (tableNmItem) {
        tableNmItem.options = tableMetaData
        this.searchModel.table = 'tb_if_mst'
      }
    } catch (error) {
        console.error(error)
      } finally {
        // this.closeLoading(target)
      }
    },

    async selectCodeList() {
      try {
        const res = await apiSelectApicodeList()
        const selectCodeData = res.result.map(item => ({ label: item.cd_nm, value: item.cd }))
        const tableNmItem = this.searchItems.find(item => item.model === 'code')
        if (tableNmItem) {
          tableNmItem.options = selectCodeData
          this.searchModel.code = tableNmItem.options[0].value
        }
      } catch (error) {
          console.error(error)
        } finally {
          // this.closeLoading(target)
        }
    },

    handleSearchClear() {
      const tableItem = this.searchItems.find((item) => item.model === 'table')
      this.searchModel.table = tableItem.options.length > 0 ? tableItem.options[0].value : ''
      this.refreashFrame()
    },

    refreashFrame() {
      // 시간이 초기화 되면서 src변경 => 그 이후 자동으로 frame 초기화
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
.collectionAnalysis {
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

