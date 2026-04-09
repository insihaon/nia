<template>
  <div :class="{ [name]: true }">
    <DataHubComponent
      ref="ApiProvidingStatus"
      :items="searchItems"
      :search-model="searchModel"
      :is-show-graph="true"
      title="실시간 API 제공 조회"
      class="w-100 h-50"
      @searchClear="handleSearchClear"
    >
      <template #grafana>
        <div style="border: 1px solid rgb(172, 169, 169);margin-bottom: 10px;">
          <iframe
            v-for="(linkSystemName, index) in searchModel.linkSystem"
            :key="index"
            ref="myIfram"
            :src="`${grafanaUrl}/d-solo/hbUjFGiIz/grokexporterdashboard-sample?orgId=1&panelId=2`
              + `&var-LINK_SYSTEM=${linkSystemName}`
              + `&from=${twoFourTimeStampMap.fromTimestamp}&to=${twoFourTimeStampMap.toTimestamp}`
              + `&refresh=${getGrafanaRefreshTime}`
            "
            width="100%"
            height="350vh"
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
import { apiSELECT_TB_INTRL_SYSTEM_MST } from '@/api/dataHub'
import { grafanaSettings } from '@/views-dataHub/grafanaManager'
import _ from 'lodash'

const routeName = 'ApiProvidingStatus'
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
              label: '연동시스템명', type: 'select', size: 8, multiple: true,
              model: 'linkSystem', setting: { allOption: { toggle: true } },
              valueConsistsOf: 'LEAF_PRIORITY',
              options: []
            }
          ],
          searchModel: {
            linkSystem: ['SYSTEM01'],
          },

          twoFourTimeStampMap: {
            fromTimestamp: new Date().getTime() - 24 * 60 * 60 * 1000,
            toTimestamp: new Date().getTime(),
            nowTimestamp: new Date().getTime()
          },

          loading: false

        }
    },

  computed: {
    grafanaUrl () {
      return this.$store.state.app.server?.grafanaUrl || ''
    },

    nodeStringValue() {
      if (this.searchModel.nodeString.length > 0) {
        let node_text = ''

        this.searchModel.nodeString.forEach((j) => {
          node_text += '&var-job=' + j
        })

        console.log('url :::: node_text')

        return node_text
      } else {
        return ''
      }
    },
    getGrafanaRefreshTime() {
      return grafanaSettings.refreshTime
    }
  },

  async mounted() {
    this.loadLinksystemList()
  },

  methods: {
    async loadLinksystemList() {
      const linkSystemItem = this.searchItems.find((item) => {
        return item.model === 'linkSystem'
      })

      try {
        const res = await apiSELECT_TB_INTRL_SYSTEM_MST()
        linkSystemItem.options = res.result.map((r) => {
          return { label: r.system_nm, value: r.system_nm }
        })
        this.searchModel['linkSystem'] = ['SYSTEM01']

        linkSystemItem.options.unshift({ label: 'SYSTEM01', value: 'SYSTEM01' })
      } catch (e) {
        linkSystemItem.options = [
          { label: 'SYSTEM01', value: 'SYSTEM01' },
        ]
      }
    },

    handleSearchClear() {
      this.searchModel.linkSystem = [this.searchItems.find((item) => item.model === 'linkSystem').options[0].value]
      this.refreashFrame()
    },

    refreashFrame() {
      this.twoFourTimeStampMap = {
        fromTimestamp: new Date().getTime() - 24 * 60 * 60 * 1000,
        toTimestamp: new Date().getTime(),
        nowTimestamp: new Date().getTime()
      }
    },
  }
}
</script>
<style lang="scss" scoped>
.ApiProvidingStatus {
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

