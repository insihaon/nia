<template>
  <div :class="{ [name]: true }">
    <LeftBar class="h-full">
      <template slot="top-container">
        <filterBar position="TOP">
          <template slot="function-container">
            <div class="title-container">IP망</div>
          </template>
        </filterBar>
        <CompAgGrid
          ref="ipAgGrid"
          v-model="ipAgGrid"
          class="w-100 flex-fill"
        />
        <!-- top-container content -->
      </template>
      <template slot="bottom-container">
        <filterBar position="BOTTOM">
          <template slot="function-container">
            <div class="title-container">전송망</div>
          </template>
        </filterBar>
        <CompAgGrid
          ref="transmissionAgGrid"
          v-model="transmissionAgGrid"
          class="w-100 flex-fill"
        />
      </template>
    </LeftBar>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import LeftBar from '@/layout/components/gridTemplate/LeftBar'
import filterBar from '@/layout/components/filterBar'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiIpAlarmList } from '@/api/nia'

const routeName = 'NiaMain'

export default {
  name: routeName,
  components: { CompAgGrid, LeftBar, filterBar },
  extends: Base,
  data() {
    return {
      name: routeName,
      ipNetworkList: [],
      transmissionNetworkList: []

    }
  },
  computed: {
    ipAgGrid() {
      const columns = [
      { type: '', prop: 'ticket_id', name: 'TICKET_ID', width: 100, alignItems: 'center', fixed: false, sortable: false, suppressMenu: true },
      ]
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return { options, columns, data: this.ipNetworkList }
    },
    transmissionAgGrid() {
      const columns = []
      const options = { name: this.name, checkable: false, rowGroupPanel: false }
      return { options, columns, data: this.transmissionNetworkList }
    }
  },
  mounted () {
    this.onLoadIpAlarmList()
  },
  methods: {
    async onLoadIpAlarmList() {
      try {
        const res = await apiIpAlarmList()
        this.ipNetworkList = res?.result
      } catch (error) {
        this.error(error)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import "~@/styles/variables.scss";

.NiaMain {
  ::v-deep .splitter-pane {
    display: flex;
    flex-direction: column;
  }
  .title-container {
    font-weight: 700;
    font-size: 16px;
  }
}
</style>
