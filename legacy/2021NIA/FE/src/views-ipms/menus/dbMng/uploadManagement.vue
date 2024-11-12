<template>
  <el-row class="w-100 h-100">
    <div class="searchOptionWrap">
      <table>
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
                { label: '성공', value: 'Y' },
                { label: '실패', value: 'N' }
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
            <el-button type="info" size="mini" icon="el-icon-refresh" round @click="handleRefresh()">
              초기화
            </el-button>
            <el-button type="primary" size="mini" icon="el-icon-search" round @click="fnViewIpUploadMst()">
              조회
            </el-button>
          </div>
        </td>
      </table>
    </div>
    <el-col style="height: calc(100% - 150px);" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 100px);"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-data="tableDatas"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="fnViewDetailIpMst"
      >
        <template slot="text-description">
          <span>
            조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px;">
            <el-button type="primary" size="mini" round @click="$refs.ModalUploadInsert.open()">양식 다운로드 및 업로드</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalUploadInsert ref="ModalUploadInsert" />
    <ModalUploadDetail ref="ModalUploadDetail" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalUploadInsert from '@/views-ipms/modal/upload/ModalUploadInsert.vue'
import ModalUploadDetail from '@/views-ipms/modal/upload/ModalUploadDetail.vue'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'UploadManagement'

export default {
  name: routeName,
  components: { CompTable, ModalUploadInsert, ModalUploadDetail },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      succVal: '',
      dateVal: [],
      tableColumns: [
        { prop: 'rowNo', label: 'No', width: 40, align: 'center', sortable: false, columnVisible: true, showOverflow: true },
        { prop: 'sFileNm', label: 'Upload 파일명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: 'Upload 일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'screateId', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sSsucessYn', label: 'Upload 성공 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: []
    }
  },
  methods: {
    handleRefresh() {
      this.succVal = ''
      this.dateVal = []
      this.fnViewIpUploadMst()
    },
    async fnViewIpUploadMst() {
      const [searchBgDe, searchEndDe] = this.dateVal
      const param = { sSuccessYn: this.succVal, searchBgDe: searchBgDe ? this.moment(searchBgDe).format('YYYY-MM-DD') : '', searchEndDe: searchEndDe ? this.moment(searchEndDe).format('YYYY-MM-DD') : '' }
      try {
        const res = await apiRequestModel(ipmsModelApis.viewIpUploadMst, param)
        this.tableDatas = res.result.data
      } catch (error) {
        this.error(error)
      }
    },
    fnViewDetailIpMst(row) {
      this.$refs.ModalUploadDetail.open({ seq: row.seq })
    }
  },
}
</script>
<style lang="css" scoped></style>
