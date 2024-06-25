<template>
  <el-row class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="6">
          <label>사용자등급</label>
          <el-select
            v-model="userGradeValue"
            collapse-tags
            size="mini"
            class="w-100"
          >
            <el-option
              v-for="(option, i) in userGradeOptions"
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
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="true"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            권한메뉴관계 정보
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'MenuAuthManagement'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '메뉴명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '메뉴 ID', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '화면 URL', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '메뉴권한 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      userGradeOptions: [
        { label: '시스템관리자', value: 1 },
        { label: '서비스망 관리자', value: 2 },
        { label: '본부운용자', value: 3 },
        { label: '노드운용자', value: 4 },
        { label: '조회자', value: 5 },
      ],
      userGradeValue: 1,
      inquiryOptions: [
        { label: '메뉴명', value: 1 },
        { label: '메뉴ID', value: 2 },
      ],
      inquiryValue: 1,
      inquiryOptions2: '',
    }
  },
  methods: {
    onClickSearch() {
      /*
      const param = {
         userGradeValue: this.userGradeValue,
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
