<template>
  <el-row class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
          <label>
            IP 주소
          </label>
          <el-input
            v-model="searchWrd"
            size="mini"
            @change="onCheckValidation"
          />
          <el-button
            class="btn-r ml-1"
            type="info"
            size="mini"
            icon="el-icon-search"
            style="background-color: #3a3a3a;"
            @click="handleSearch()"
          >
            조회
          </el-button>
        </el-col>
      </el-row>
    </div>
    <el-col style="height: calc(100% - 75px)">
      <CompTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="ipBlockColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="ipInfoList"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP 블록 정보
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'IpInfoMultiList'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      ipBlockColumns: [
        { prop: '', label: '검색 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP 주소', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'BitMask', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'SAID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '상세', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      searchWrd: '',
      requestParameter: {}
    }
  },
  methods: {
    handleSearch() {
      /*
        const res = await api({ searchWrd: this.searchWrd })
      */
    },
    onCheckValidation(val) {
      const reVal = val.replace(/[^0-9\\.]/g, '')
      this.searchWrd = reVal
    }
  },
}
</script>
<style lang="scss" scoped></style>
