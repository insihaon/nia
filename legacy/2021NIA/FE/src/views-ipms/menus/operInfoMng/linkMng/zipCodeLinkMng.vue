<template>
  <el-row ref="container" class="w-100 h-100">
    <div ref="searchCondition" class="optionBox">
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
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="fnUploadView()">
            조회
          </el-button>
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-refresh">
            초기화
          </el-button>
          <slot name="add-function" />
        </el-col>
      </el-row>
      <div class="content_result my-2">
        <div class="section_tit">
          <span>Upload</span>
          <em> (업로드시 5분 이상 소요될 수 있습니다.) </em> </div>
        <table class="tbl_list">
          <thead>
            <tr>
              <th class="first" style="width: 20%">파일 Upload</th>
              <th scope="col" style="text-align : left; background : none">
                <input type="file" size="500" @change="handleFileUpload">
                <el-button class="float-right" type="primary" size="mini" @click="uploadAjax">Upload</el-button>
              </th>
            </tr>
          </thead>
        </table>
      </div>
    </div>
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        :prop-table-height="'calc(100% - 80px)'"
        :prop-data="tableDatas"
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
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'ZipCodeLinkMng'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  mixins: [tableHeightMixin],
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        succVal: '',
        dateVal: [],
        file: null,
        tableColumns: [
          { prop: 'rowNo', label: 'No', width: 50, align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'file_name', label: 'Upload 파일명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'Upload 일자', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row.regist_date ? this.moment(row.regist_date).format('YYYY-MM-DD HH:mm:ss') : '' } },
          { prop: 'regist_id', label: '등록자', align: 'center', columnVisible: true, showOverflow: true },
          { prop: 'status', label: 'Upload 성공 여부', align: 'center', columnVisible: true, showOverflow: true },
        ],
        tableDatas: [],
    }
  },
  mounted () {
    this.fnUploadView()
  },
  methods: {
    async fnUploadView() {
      const [searchBgDe, searchEndDe] = this.dateVal
      const param = { status: this.succVal, searchBgDe: searchBgDe ? this.moment(searchBgDe).format('YYYY-MM-DD') : '', searchEndDe: searchEndDe ? this.moment(searchEndDe).format('YYYY-MM-DD') : '' }
      try {
        const res = await apiRequestModel(ipmsModelApis.uploadView, param)
        this.tableDatas = res?.result?.data ?? []
      } catch (error) {
        this.error(error)
      }
    },
    async uploadAjax() {
      if (this.file === null) {
        onMessagePopup(this, '선택된 파일이 없습니다.')
        return
      }
      try {
        // opermgmt/uploadmgmt/upload.ajax
        const res = await apiRequestJson(ipmsJsonApis.upload, { file: this.file })
        if (res.commonMsg === 'SUCCESS') {
          this.fnUploadView()
        }
      } catch (error) {
        this.error(error)
      }
    },
    handleFileUpload(e) {
      this.file = e.target.files[0]
    }
  }
}
</script>
<style lang="css" scoped>
</style>
