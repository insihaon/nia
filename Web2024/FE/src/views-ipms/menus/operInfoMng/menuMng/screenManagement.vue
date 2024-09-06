<template>
  <el-row class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="6">
          <label>화면유형</label>
          <el-select
            v-model="scrnType"
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
            size="mini"
            class="w-100"
            @change="handleChangeCondition"
          >
            <el-option
              v-for="(option, i) in inquiryOptions"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
          <el-input v-model="inquiryTxt" size="mini" clearable />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" align="center" class="searchBtnGroup">
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="handleSearch()">
            조회
          </el-button>
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh">
            초기화
          </el-button>
          <slot name="add-function" />
        </el-col>
      </el-row>
    </div>
    <el-col :span="24" style="height: calc(100% - 160px)">
      <compTable
        ref="compTable"
        :prop-name="name"
        :prop-table-height="'calc(100% - 80px)'"
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
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'ScreenManagement'

export default {
  name: routeName,
  components: { CompTable },
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
        { label: '전체', value: '' },
        { label: '미분류', value: 'UD0000' },
        { label: 'MAIN', value: 'UD0001' },
        { label: 'SUB', value: 'UD0002' },
        { label: 'POPUP', value: 'UD0003' },
      ],
      scrnType: '',
      inquiryOptions: [
        { label: '화면ID', value: 'sscrnId' },
        { label: '화면명', value: 'sscrnNm' },
        { label: '설계화면', value: 'sdgnScrnId' },
      ],
      inquiryValue: 'sscrnId',
      inquiryTxt: '',
    }
  },
  methods: {
    handleSearch() {
      const params = {
        sscrnTypeCd: this.scrnType,
        searchCnd: this.inquiryValue,
        searchWrd: this.inquiryTxt,
      }
      console.log(params)
      /*
      const res = await api(param)
      */
    },
    handleChangeCondition() {
      this.inquiryTxt = ''
    }
  }
}
</script>
<style lang="css" scoped>
</style>
