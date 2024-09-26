<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="syslogRules"
      :ag-grid="authAgGrid"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @selectedRow="(row) => handleOpenModalDetail(row, 'EDIT')"
      @handleClickSearch="onClickSearch"
      @searchClear="searchClear"
      @onDebugTest="autoTest"
    >
      <template slot="add-function">
        <el-button type="info" size="mini" icon="el-icon-edit" @click="handleOpenModalDetail('', 'OPEN')">등록</el-button>
      </template>
    </CompInquiryPannel>
    <ModalSyslogRules ref="ModalSyslogRules" @syslogRuleEdit="onLoadSyslogRuleList()" />
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiSyslogRuleList } from '@/api/nia'
import ModalSyslogRules from '@/views-nia/modal/ModalSyslogRules.vue'

const routeName = 'SyslogRuleHistoryInquiry'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, ModalSyslogRules },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11,
      },
      selectedRow: [],
      ruleData: [],
      searchItems: [
        { label: '규칙명', type: 'select', model: 'syslog_rule_nm', options: [] },
        { label: '발생 키워드 ', type: 'input', model: 'occur_str' },
        { label: '제외 키워드', type: 'input', model: 'occur_except_str' },
      ],
      searchModel: {
        syslog_rule_nm: '',
        occur_str: '',
        occur_except_str: '',
      },
    }
  },
  computed: {
    authAgGrid() {
      const options = {
        name: this.name + 'table1',
        rowGroupPanel: false,
        rowSelection: 'multiple',
        rowMultiSelection: false,
      }
      const columns = [
        { type: '', prop: 'syslog_rule_id', name: 'RULE ID', minWidth: 100, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'syslog_rule_nm', name: 'RULE NAME', minWidth: 100, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_str1', name: '발생 키워드1', minWidth: 100, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_str2', name: '발생 키워드2', minWidth: 100, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_str3', name: '발생 키워드3', minWidth: 100, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_except_str1', name: '제외 키워드1', minWidth: 100, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_except_str2', name: '제외 키워드2', minWidth: 100, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_except_str3', name: '제외 키워드3', minWidth: 100, flex: 0, suppressMenu: true, sortable: true, filterable: false },
        { type: '', prop: 'use_yn', name: '사용 여부', minWidth: 100, flex: 0, suppressMenu: true, sortable: true, filterable: true },
      ]
      return {
        options,
        columns,
        data: this.ruleData,
        getRightClickMenuItems: () => {
          return []
        },
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.loadRuleOptions()
      this.onLoadSyslogRuleList()
    })
  },
  methods: {
    onClickSearch(params) {
      this.onLoadSyslogRuleList(params)
    },
    async loadRuleOptions() {
      try {
        const res = await apiSyslogRuleList({})
        const ruleOp = res.result.map((item) => ({ label: item.syslog_rule_nm, value: item.syslog_rule_nm }))
        this.searchItems.forEach(item => {
          if (item.model === 'syslog_rule_nm') {
            item.options = ruleOp
          }
        })
      } catch (error) {
        console.error(error)
      }
    },
    async onLoadSyslogRuleList() {
      const target = { vue: this.$refs.syslogRules }
      this.openLoading(target)
      const param = {
        syslog_rule_nm: this.searchModel.syslog_rule_nm,
        occur_str: this.searchModel.occur_str,
        occur_except_str: this.searchModel.occur_except_str,
        limit: this.paginationInfo.pageSize,
        page: this.paginationInfo.currentPage,
      }
      try {
        const res = await apiSyslogRuleList(param)
        this.ruleData = res?.result
        this.paginationInfo.totalCount = res.total
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSize) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleOpenModalDetail(rows, type) {
      this.$refs.ModalSyslogRules.open({ row: rows[0], type })
    },
    searchClear() {
      this.searchModel = {}
      this.onLoadSyslogRuleList()
    },
    async autoTest() {
      const { assert, wait, onLoadSyslogRuleList, query } = this
      query.writer = 'daejeon'
      await onLoadSyslogRuleList()
      assert(this.ruleData.length > 0)
      window.ref.ModalSyslogRules.open({ type: 'OPEN' })
      await wait(1000)
      window.ref.ModalSyslogRules.close()
      await wait(1000)
      window.ref.ModalSyslogRules.insertSyslogRule()
      await wait(1000)
      document.querySelector(Base.confirmBtn).click()
      await wait(1000)
    },
  },
}
</script>

<style lang="scss" scoped>
.SyslogRuleHistoryInquiry {
  ::v-deep .ag-cell-value {
    cursor: pointer !important;
  }
}
</style>
