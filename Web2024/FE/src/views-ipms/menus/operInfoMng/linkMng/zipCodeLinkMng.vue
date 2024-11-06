<template>
  <el-row ref="container" class="w-100 h-100">
    <div ref="searchCondition" class="searchOptionWrap">
      <table>
        <tr>
          <th>
            <label>Upload 성공 여부</label>
          </th>
          <td>
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
          </td>
          <th>
            <label>Upload 일자</label>
          </th>
          <td>
            <el-date-picker
              v-model="dateVal"
              type="daterange"
              size="mini"
              start-placeholder="시작일"
              end-placeholder="종료일"
            />
          </td>
          <td>
            <div class="searchBtnWrap">
              <el-button type="info" size="mini" icon="el-icon-refresh" round>
                초기화
              </el-button>
              <el-button type="primary" size="mini" icon="el-icon-search" round @click="fnUploadView()">
                조회
              </el-button>
            </div>
          </td>
        </tr>
      </table>
      <table>
        <tr>
          <th class="first" style="width: 20%">파일 Upload</th>
          <td>
            (업로드시 5분 이상 소요될 수 있습니다.)
            <input type="file" size="500" @change="handleFileUpload">
            <el-button type="primary" size="mini" round @click="uploadAjax">Upload</el-button>
          </td>
        </tr>
      </table>
    </div>
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
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
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-download" type="primary" size="mini" round @click="fnDownloadData">우체국 데이터 다운로드</el-button>
          </div>
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

import { getToken } from '@/utils/auth'
import { AppOptions } from '@/class/appOptions'
import axios from 'axios'

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
      pagination: this.setDefaultPagination(),
      succVal: '',
      dateVal: [],
      uploadFile: null,
      tableColumns: [
        { prop: 'file_name', label: 'Upload 파일명', align: 'center', columnVisible: true, showOverflow: true },
        { prop: '', label: 'Upload 일자', align: 'center', columnVisible: true, showOverflow: true, formatter: (row) => { return row.regist_date ? this.moment(row.regist_date).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'regist_id', label: '등록자', align: 'center', columnVisible: true, showOverflow: true },
        { prop: 'status', label: 'Upload 성공 여부', align: 'center', columnVisible: true, showOverflow: true },
      ],
    }
  },
  mounted () {
    this.fnUploadView()
  },
  methods: {
    async fnUploadView() {
      const [searchBgDe, searchEndDe] = this.dateVal
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      const param = {
        status: this.succVal,
        searchBgDe: searchBgDe ? this.moment(searchBgDe).format('YYYY-MM-DD') : '',
        searchEndDe: searchEndDe ? this.moment(searchEndDe).format('YYYY-MM-DD') : '',
        pageUnit,
        pageIndex
        }
      try {
        const res = await apiRequestModel(ipmsModelApis.uploadView, param)
        this.pagination.data = res?.result?.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      }
    },
    fnDownloadData() {
      const url = 'http://www.epost.go.kr/search/zipcode/areacdAddressDown.jsp'
      window.open(url)
    },
    async uploadAjax() {
      // opermgmt/uploadmgmt/upload.ajax
      if (this.uploadFile === null) {
        onMessagePopup(this, '선택된 파일이 없습니다.')
        return
      }
      const formData = new FormData()
      formData.append('file', this.uploadFile)
       try {
        // opermgmt/uploadmgmt/upload
        // 오류남 컨트롤러 내부로직 확인필요함
        const res = await apiRequestJson(ipmsJsonApis.uploadmgmtUpload, formData)
        console.log(res)
        this.fnUploadView()
      } catch (error) {
        this.error(error)
      }
    },
    handleFileUpload(e) {
      this.uploadFile = e.target.files[0]
    }
  }
}
</script>
<style lang="css" scoped>
</style>
