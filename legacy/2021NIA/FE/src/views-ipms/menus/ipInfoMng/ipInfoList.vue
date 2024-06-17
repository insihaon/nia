<template>
  <!-- content -->
  <div id="wrap">
    <div id="container" class="main">
      <!-- search-->
      <div class="search_result">
        <fieldset>
          <legend>검색하기</legend>
          <el-select v-model="value">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <span>
            <input type="text" class="txt" placeholder="IP주소 또는 회선번호, SAID를 입력해 주세요." title="검색어 입력창" />
          </span>
          <span>
            <el-button>
              <img src="@/assets/images/ipms/content/btn_main_search_off.gif" alt="검색" />
            </el-button>
          </span>
        </fieldset>
      </div>
      <el-row class="p-4">
        <div>IP 블록 정보</div>
        <CompTable
          :prop-table-height="300"
          :prop-column="ipBlockColumns"
          :prop-is-pagination="false"
          :prop-is-check-box="false"
          prop-grid-menu-id="ipInfoList"
          :prop-grid-indx="1"
        />
      </el-row>
      <el-row>
        <div>IP Host 정보</div>
        <CompTable
          :prop-table-height="300"
          :prop-column="ipHostColumns"
          :prop-is-pagination="false"
          :prop-is-check-box="false"
          prop-grid-menu-id="ipInfoList"
          :prop-grid-indx="1"
        />
      </el-row>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'IpInfoList'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      options: [
        {
          value: 'IPv4',
          label: 'IPv4',
        },
        {
          value: 'IPv6',
          label: 'IPv6',
        },
        {
          value: 'SAID',
          label: 'SAID',
        },
        {
          value: '전용번호',
          label: '전용번호',
        },
      ],
      value: 'IPv4',
      ipBlockColumns: [
        { prop: '', label: 'IP 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시설 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '회선 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      ipHostColumns: [
        { prop: '', label: 'IP 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시설 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '회선 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
