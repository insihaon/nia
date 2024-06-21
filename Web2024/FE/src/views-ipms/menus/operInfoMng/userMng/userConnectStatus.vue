<template>
  <el-row class="w-100 h-100">
    <div class="optionBox">
      <el-row class="optionRow">
        <el-col class="d-flex" :span="6">
          <label>사용자 접속결과</label>
          <el-select
            v-model="connectValue"
            collapse-tags
            size="mini"
          >
            <el-option
              v-for="(option, i) in authOptions"
              :key="i"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-col>
        <el-col class="d-flex" :span="6">
          <label>소속조직</label>
          <div class="w-100" @click="handleOpenSearchModal()">
            <el-input
              v-model="orgnzVal"
              size="mini"
              clearable
            >
              <template #suffix>
                <el-button
                  size="mini"
                  style="font-size: larger;"
                >
                  <i class="el-icon-search font-weight-bolder"></i>
                </el-button>
              </template>
            </el-input>
          </div>
        </el-col>
        <el-col class="d-flex" :span="6">
          <label>사용자명</label>
          <el-input v-model="nameValue" size="mini" clearable />
        </el-col>

        <el-col class="d-flex" :span="6">
          <label>로그인 시간</label>
          <el-date-picker
            v-model="loginTime"
            type="daterange"
            size="mini"
            start-placeholder="시작일"
            end-placeholder="종료일"
            @change="handleChange"
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
      <ModalOrgSearch ref="ModalOrgSearch" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import ModalOrgSearch from '@/views-ipms/modal/ModalOrgSearch.vue'
const routeName = 'UserConnectStatus'

export default {
  name: routeName,
  components: { CompTable, ModalOrgSearch },
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
      connectValue: '',
      orgnzVal: '',
      nameValue: '',
      loginTime: []
    }
  },
  methods: {
    handleOpenSearchModal() {
      this.$refs.ModalOrgSearch.open({ row: 'row' })
    },
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
