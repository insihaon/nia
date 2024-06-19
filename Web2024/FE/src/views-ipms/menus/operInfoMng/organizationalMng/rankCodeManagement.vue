<template>
  <el-row class="w-100 h-100">
    <!-- <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    /> -->
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="8">
          <label>외부연동유형</label>
          <el-select
            v-model="linkValue"
            collapse-tags
            size="mini"
          >
            <el-option
              v-for="(option, i) in [
                { label: '전체', value: '' },
                { label: '미분류', value: 'mibuntryu' },
                { label: 'NEOSS_CODE', value: 'NEOSS_CODE' },
                { label: 'NEOSS_DATA', value: 'NEOSS_DATA' },
                { label: 'IFOMS_DATA', value: 'IFOMS_DATA' },
                { label: '통합NMS_DATA', value: 'NMS_DATA' },
                { label: '수작업데이터', value: 'data' },
                { label: 'IDC_DATA', value: 'IDC_DATA' },
                { label: 'IDMS_DATA', value: 'IDMS_DATA' },
              ]"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-col>
        <el-col class="d-flex" :span="8">
          <label>계위명</label>
          <el-input v-model="sipCreateValue" size="mini" clearable @change="handleChangeWord" />
        </el-col>
        <el-col class="d-flex" :span="8">
          <label>코드명</label>
          <el-input v-model="codeValue" size="mini" clearable @change="handleChangeWord" />
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
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            계위코드
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'RankCodeManagement'

export default {
  name: routeName,
  components: { CompTable },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: '코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '계위명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '구분코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '외부연동 유형', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '수정', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'UsageYN', props: { label: '외부연동유형' } },
        { key: 'InputType', props: { label: '계위명' } },
        { key: 'InputType', props: { label: '코드명' } },
      ],
      linkValue: '',
      sipCreateValue: '',
      codeValue: ''
    }
  },
  methods: {
    onClickSearch() {
      /*
      const param = {
         linkValue: this.linkValue,
         sipCreateValue: this.sipCreateValue,
         codeValue: this.codeValue,
        }
      const res = await api(param)
      */
    }
  }
}
</script>
<style lang="css" scoped>
</style>
