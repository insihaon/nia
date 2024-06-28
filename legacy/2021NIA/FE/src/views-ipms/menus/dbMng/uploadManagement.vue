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
                { label: '성공', value: 'Y' },
                { label: '실패', value: 'N' },
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
    <el-col style="height: calc(100% - 150px);" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
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

const routeName = 'UploadManagement'

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
        { prop: '', label: 'No', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Upload 파일명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Upload 일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'Upload 성공 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
    }
  },
  methods: {
    onClickSearch() {
      const [searchBgDe, searchEndDe] = this.dateVal
      const param = { sSuccessYn: this.succVal, searchBgDe: searchBgDe ? this.moment(searchBgDe).format('YYYY-MM-DD') : '', searchEndDe: searchEndDe ? this.moment(searchEndDe).format('YYYY-MM-DD') : '' }
      console.log(param)
      /*
      const res = await api(param)
      */
    }
  },
}
</script>
<style lang="css" scoped></style>
