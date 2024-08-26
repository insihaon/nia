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
                { label: '실패', value: 'N' }
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
          <el-button class="btn-r" type="info" size="mini" icon="el-icon-search" @click="fnViewIpUploadMst()">
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
          <div class="float-right">
            <el-button size="mini" @click="$refs.ModalUploadInsert.open()">양식 다운로드 및 업로드</el-button>
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
        { prop: 'rowNo', label: 'No', width: 40, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sFileNm', label: 'Upload 파일명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: 'Upload 일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'screateId', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sSsucessYn', label: 'Upload 성공 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: []
    }
  },
  methods: {
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
