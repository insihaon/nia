<template>
  <div :class="{ [name]: true }">
    <CompInquiryPannel
      ref="syslogRules"
      :ag-grid="authAgGrid"
      :items="searchItems"
      :search-model.sync="searchModel"
      :pagination-info="paginationInfo"
      class="w-100 h-100"
      @selectedRow="(row)=> handleOpenModalDetail(row,'EDIT')"
    >
      <template slot="button-area">
        <div class="button-panel">
          <el-button class="float-right" size="mini" type="info" @click="handleOpenModalDetail('', 'OPEN')">등록</el-button>
        </div>
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

const routeName = 'syslogRuleHistoryInquiry'
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
        pagerCount: 11
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
        name: this.name + 'table1', rowGroupPanel: false, rowHeight: 30, rowSelection: 'multiple', rowMultiSelection: false,
      }
      const columns = [
        { type: '', prop: 'syslog_rule_id', name: 'RULE ID', minWidth: 30, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'syslog_rule_nm', name: 'RULE NAME', minWidth: 30, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_str1', name: '발생 키워드1', minWidth: 30, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_str2', name: '발생 키워드2', minWidth: 30, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_str3', name: '발생 키워드3', minWidth: 30, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_except_str1', name: '제외 키워드1', minWidth: 30, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_except_str2', name: '제외 키워드2', minWidth: 30, flex: 0, suppressMenu: true, sortable: true },
        { type: '', prop: 'occur_except_str3', name: '제외 키워드3', minWidth: 40, flex: 0, suppressMenu: true, sortable: true, filterable: false },
        { type: '', prop: 'use_yn', name: '사용 여부', minWidth: 50, flex: 0, suppressMenu: true, sortable: true, filterable: true },
      ]
      return { options, columns, data: this.ruleData, getRightClickMenuItems: () => { return [] } }
    },
  },
  mounted() {
    this.onLoadSyslogRuleList()
  },
  methods: {
    onClickSearch(params) {
      this.onLoadSyslogRuleList(params)
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
        const ruleNameData = res.result.map((item) => ({
          label: item.syslog_rule_nm,
          value: item.syslog_rule_nm,
        }))
        this.searchItems[0].options = ruleNameData
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
  }
}
</script>
