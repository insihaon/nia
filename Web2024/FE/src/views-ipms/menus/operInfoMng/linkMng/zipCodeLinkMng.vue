<template>
  <el-row class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="12">
          <label>Upload 성공 여부</label>
          <el-select
            v-model="succVal"
            collapse-tags
            size="mini"
          >
            <el-option
              v-for="(option, i) in [
                { label: '전체', value: '' },
                { label: '성공', value: 'success' },
                { label: '실패', value: 'fail' },
              ]"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-col>
        <el-col class="d-flex" :span="12">
          <label>Upload 일자</label>
          <el-date-picker
            v-model="dateVal"
            type="daterange"
            size="mini"
            start-placeholder="시작일"
            end-placeholder="종료일"
          />
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
    <div class="content_result my-2">
      <div class="section_tit">
        <span>Upload</span>
        <em> (업로드시 5분 이상 소요될 수 있습니다.) </em> </div>
      <table class="tbl_list">
        <thead>
          <tr>
            <th class="first" style="width: 20%">파일 Upload</th>
            <th scope="col" style="text-align : left; background : none">
              <input type="file" size="500">
              <el-button class="float-right"> Upload</el-button>
            </th>
          </tr>
        </thead>
      </table>
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
            조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'ZipCodeLinkMng'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        succVal: '',
        dateVal: [],
        tableColumns: [
          { prop: '', label: 'Upload 파일명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'Upload 일자', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '등록자', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'Upload 성공 여부', align: 'center', columnVisible: true, showOverflow: true },
        ],
    }
  },
  methods: {
    onClickSearch(requestParameter) {
       /*
      const param = { status: this.succVal, searchBgDe, searchEndDe }
      const res = await api(param)
      */
    }
  }
}
</script>
<style lang="css" scoped>
</style>
