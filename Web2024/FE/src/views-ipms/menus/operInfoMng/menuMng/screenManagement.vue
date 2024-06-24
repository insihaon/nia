<template>
  <el-row class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="6">
          <label>화면유형</label>
          <el-select
            v-model="channelValue"
            collapse-tags
            size="mini"
            class="w-100"
          >
            <el-option
              v-for="(option, i) in channelOptions"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-col>
        <el-col class="d-flex" :span="6">
          <label>조회조건</label>
          <el-select
            v-model="inquiryValue"
            collapse-tags
            size="mini"
            class="w-100"
          >
            <el-option
              v-for="(option, i) in inquiryOptions"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
          <el-input v-model="inquiryOptions2" size="mini" clearable />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" align="center" class="searchBtnGroup">
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="onClickSearch()">
            조회
          </el-button>
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh">
            초기화
          </el-button>
          <slot name="add-function" />
        </el-col>
      </el-row>
    </div>
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            화면
          </span>
        </template>
      </compTable>
      <ModalOrgSearch ref="ModalOrgSearch" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalOrgSearch from '@/views-ipms/modal/ModalOrgSearch.vue'
const routeName = 'ScreenManagement'

export default {
  name: routeName,
  components: { CompTable, ModalOrgSearch },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '화면ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '화면명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '화면 URL 정보', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '화면유형', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '설계화면 ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '화면사용여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      channelOptions: [
        { label: '전체', value: 1 },
        { label: '미분류', value: 2 },
        { label: 'MAIN', value: 3 },
        { label: 'SUB', value: 4 },
        { label: 'POPUP', value: 5 },
      ],
      channelValue: 1,
      inquiryOptions: [
        { label: '화면ID', value: 1 },
        { label: '화면명', value: 2 },
        { label: '설계화면', value: 3 },
      ],
      inquiryValue: 1,
      inquiryOptions2: '',
    }
  },
  methods: {
    onClickSearch() {
      /*
      const param = {
         channelValue: this.channelValue,
         inquiryValue: this.inquiryValue,
         inquiryOptions2: this.inquiryOptions2,
        }
      const res = await api(param)
      */
    }
  }
}
</script>
<style lang="css" scoped>
</style>
