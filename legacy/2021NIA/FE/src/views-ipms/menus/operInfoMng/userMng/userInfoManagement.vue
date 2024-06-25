<template>
  <el-row ref="container" class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="6">
          <AuthLevel
            label="사용자 권한등급"
            class="w-100 d-flex"
            @update-value="setParameterKey"
          />
        </el-col>
        <el-col class="d-flex" :span="6">
          <InputSearchDetail
            ref="searchDetail"
            label="소속조직"
            modal-name="ModalOrgSearch"
            value-name="sFullOrgNm"
            :prop_parameter-key="{ sposDeptOrgId: 'sktOrgId', sporEdptOrgNm: 'sFullOrgNm' }"
            :is-read-only="true"
            class="w-100 d-flex"
            @update-value="setParameterKey"
          />
        </el-col>

        <el-col class="d-flex" :span="6">
          <label>재직상태</label>
          <el-select
            v-model="statusValue"
            collapse-tags
            size="mini"
          >
            <el-option
              v-for="(option, i) in officeStatus"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-col>
        <el-col class="d-flex" :span="6">
          <label>사용자명</label>
          <el-input v-model="nameValue" size="mini" clearable />
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
    <el-col style="height: calc(100% - 160px);" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            사용자 정보 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import InputSearchDetail from '@/views-ipms/conditionComponents/InputSearchDetail.vue'
import AuthLevel from '@/views-ipms/conditionComponents/AuthLevel.vue'
const routeName = 'UserInfoManagement'

export default {
  name: routeName,
  components: { CompTable, InputSearchDetail, AuthLevel },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '사용자명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '소속조직', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '재직상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '권한등급', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수정', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      officeStatus: [
        { label: '전체', value: '' },
        { label: '미분류', value: 'US0000' },
        { label: '재직', value: 'US0001' },
        { label: '퇴직', value: 'US0002' },
        { label: '휴직', value: 'US0003' },
        { label: '유급휴가', value: 'US0004' },
      ],
      statusValue: '',
      nameValue: '',
      requestParameter: {}
    }
  },
  methods: {
    onClickSearch() {
      Object.assign(this.requestParameter, { 'suserSttusCd': this.statusValue, 'suserNm': this.nameValue })
      console.log(this.requestParameter)
      /*
      const res = await api(this.requestParameter)
      */
    },
    setParameterKey(params) {
      params.forEach(item => { this.requestParameter[item.key] = item.value })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
