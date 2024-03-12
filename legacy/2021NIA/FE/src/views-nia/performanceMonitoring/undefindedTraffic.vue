<template>
  <div :class="{ [name]: true }">
    <div class="common-padding">
      <el-tabs class="h-100" type="border-card" style="flex:1">
        <el-tab-pane label="국가">
          <div class="h-100 flex-fill" sytle="height: 100%">
            <CompInquiryPannel
              ref="trafficNation"
              :ag-grid="nationAgGrid"
              :is-button-slot="false"
              :items="searchNationItems"
              :search-model.sync="searchModel"
              :pagination-info="paginationInfo"
              class="w-100 flex-fill"
              :style="{ height : 'calc(100vh - 200px)'}"
              @cellClicked="cellTemp"
              @sortChanged="sortTemp"
            />
          </div>
        </el-tab-pane>
        <el-tab-pane label="이용기관">
          <div class="h-100 flex-fill" sytle="height: 100%">
            <CompInquiryPannel
              ref="trafficAgency"
              :ag-grid="agencyAgGrid"
              :is-button-slot="false"
              :items="searchAgencyItems"
              :search-model.sync="searchModel"
              :pagination-info="paginationInfo"
              class="w-100 flex-fill"
              :style="{ height : 'calc(100vh - 200px)'}"
              @cellClicked="cellTemp"
              @sortChanged="sortTemp"
            />
          </div>
        </el-tab-pane>
        <el-tab-pane label="어플리케이션">
          <div class="h-100 flex-fill" sytle="height: 100%">
            <CompInquiryPannel
              ref="trafficApp"
              :ag-grid="appAgGrid"
              :is-button-slot="false"
              :items="searchAppItems"
              :search-model.sync="searchModel"
              :pagination-info="paginationInfo"
              class="w-100 flex-fill"
              :style="{ height : 'calc(100vh - 200px)'}"
              @cellClicked="cellTemp"
              @sortChanged="sortTemp"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
// import { apiSelectAuthHistList, apiUpdateApiAuth, apiUpdateApiAuthProc } from '@/api/dataHub'
import { AppOptions } from '@/class/appOptions'
import { apiSelectUnidentifiedNationList, apiSelectUnidentifiedAgencyList, apiSelectUnidentifiedAppList } from '@/api/nia'

const routeName = 'UndefindedTraffic'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      selectedRow: [],
       nationData: [],
       agencyData: [],
       appData: [],
      searchNationItems: [
       { label: '국가명', type: 'input', model: 'nation_name', placeholder: '국가명을 검색하세요' },
       { label: '국가코드', type: 'input', model: 'nation_code', placeholder: '국가코드를 검색하세요' },
      ],
      searchAgencyItems: [
       { label: '이용기관명', type: 'input', model: 'agency_name', placeholder: '이용기관명을 검색하세요' },
       { label: 'IP주소', type: 'input', model: 'ip_addr', placeholder: 'IP주소를 검색하세요' },
      ],
        searchAppItems: [
       { label: '어플리케이션명', type: 'input', model: 'app_name', placeholder: '어플리케이션명을 검색하세요' },
       { label: 'PORT번호', type: 'input', model: 'port_number', placeholder: 'PORT번호를 검색하세요' },
      ],
      searchModel: {
        nation_name: '',
        nation_code: '',
        agency_name: '',
        ip_addr: '',
        app_name: '',
        port_number: '',
      },
      sortInfo: {}
    }
  },

  computed: {
    nationAgGrid() {
      const options = {
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'country_name', name: '국가명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'koica', name: '국가코드', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
      ]
      return { options, columns, data: this.nationData, getRightClickMenuItems: () => { return [] } }
    },
     agencyAgGrid() {
      const options = {
        name: this.name + 'table2', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'nren_name', name: '이용기관명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'nren_ip', name: 'IP주소', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
      ]
      return { options, columns, data: this.agencyData, getRightClickMenuItems: () => { return [] } }
    },
    appAgGrid() {
      const options = {
        name: this.name + 'table3', rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'protocol', name: '어플리케이션명', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'port_num', name: 'PORT 번호', minWidth: 100, flex: 0, suppressMenu: true, alignItems: 'center' },
      ]
      return { options, columns, data: this.appData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadNationList()
    this.onLoadAgencyList()
    this.onLoadAppList()
  },
  methods: {
    cellTemp() {},
    sortTemp() {},
    onSortedChange(param) {
       this.sortInfo = []
       this.onLoadNationList()
      //  this.onLoadAgencyList()
    },
    // onClickSearchAuth(params) {
    //   this.onLoadNationList(params)
    // },
    async onLoadNationList(params) {
      const target = { vue: this.$refs.trafficNation }
      this.openLoading(target)
      try {
        const res = await apiSelectUnidentifiedNationList()
        this.nationData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async onLoadAgencyList(params) {
      const target = { vue: this.$refs.trafficAgency }
      this.openLoading(target)
      try {
        const res = await apiSelectUnidentifiedAgencyList()
        this.agencyData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    async onLoadAppList(params) {
      const target = { vue: this.$refs.trafficApp }
      this.openLoading(target)
      try {
        const res = await apiSelectUnidentifiedAppList()
        this.appData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

  },
}
</script>

<style lang="scss" scoped>
.UndefindedTraffic{

}

</style>
