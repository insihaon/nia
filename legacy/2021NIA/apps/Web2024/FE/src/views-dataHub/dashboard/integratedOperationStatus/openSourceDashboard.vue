<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="openSourceDashboard"
      :items="searchItems"
      :search-model="searchModel"
      :is-show-graph="true"
      title="오픈소스 대시보드 조회"
      class="w-100 h-50"
      @searchClear="handleSearchClear"
    >
      <template #grafana>
        <div style="border: 1px solid rgb(172, 169, 169);margin-bottom: 10px; display:flex; flex-wrap:wrap">
          <iframe
            v-for="(dashboardKey, index) in dashboardKeyList"
            :key="index"
            :src="
              grafanaUrl
                + dashboardMap[searchModel.table][dashboardKey]
                + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
                + `&refresh=${getGrafanaRefreshTime}`
            "
            width="50%"
            height="200px"
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
import { grafanaDashboardUrlMap, grafanaSettings } from '@/views-dataHub/grafanaManager'
import _ from 'lodash'
const routeName = 'openSourceDashboard'
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
              label: '레이블', type: 'select', size: 8, model: 'table', options: [
                { value: 'sparkAndLogStash', label: 'spark & logstash' },
                { value: 'mongoDB', label: 'mongoDB' },
              ]
            },
          ],
          searchModel: {
            table: 'sparkAndLogStash'
          },
          selectCodeData: [],
          twoFourTimeStampMap: {
            fromTimestamp: new Date().getTime() - 24 * 60 * 60 * 1000,
            toTimestamp: new Date().getTime(),
            nowTimestamp: new Date().getTime()
          },

      }
    },

  computed: {
    grafanaUrl () {
      return this.$store.state.app.server?.grafanaUrl || ''
    },

    dashboardMap() {
      return grafanaDashboardUrlMap
    },

    dashboardKeyList() {
      return Object.keys(this.dashboardMap[this.searchModel.table])
    },

    getGrafanaRefreshTime() {
      return grafanaSettings.refreshTime
    }
  },

  mounted() {
  },

  methods: {
    handleSearchClear() {
      this.searchModel.table = this.searchItems.find((item) => item.model === 'table').options[0].value
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
.openSourceDashboard {
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

