<template>
  <div v-if="isDashboard" class="dashboard-notice">
    <div class="d-flex">
      <h3 class="noti-title">공지사항</h3>
      <div class="content-title">[긴급] IPMS 시스템 관련공지 모두가 주목해야할 공지사항입니다.</div>
    </div>
    <div class="d-flex">
      <div class="date">2021-04-09</div>
      <div class="btn-container">
        <img src="@/assets/images/ipms/content/btn_noti_up_off.gif" alt="이전글" title="이전글" />
        <img src="@/assets/images/ipms/content/btn_noti_down_off.gif" alt="다음글" title="다음글" />
      </div>
      <div class="more">
        <img src="@/assets/images/ipms/content/btn_more_off.gif" alt="더보기" title="더보기" />
      </div>
    </div>
  </div>
  <el-row v-else class="w-100 h-100 pt-4 px-12">
    <el-col :span="24" class="h-100">
      <CompInquiryPannel :items="searchItems" :search-model.sync="searchModel" />
      <compTable :prop-column="tableColumns" :prop-is-pagination="false" :prop-is-check-box="false" prop-grid-menu-id="inputSpeed" :prop-grid-indx="1" />
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import CompInquiryPannel from '@/views-ipms/components/CompInquiryPannel'
const routeName = 'Notice'

export default {
  name: routeName,
  components: { CompTable, CompInquiryPannel },
  extends: Base,
  props: {
    isDashboard: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      tableColumns: [
        { prop: '', label: 'SEQ', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '유형', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '등록일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '팝업게시시작일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '팝업게시종료일', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '조회건수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      searchItems: [
        {
          label: '공지구분', type: 'select', model: 'gubun', placeholder: '구분을 선택하세요', options: [
            { label: '전체', value: 'all' },
            { label: '일반', value: 'normal' },
            { label: '긴급', value: 'warning' },
          ]
        },
        {
          label: '조회조건', type: 'select', model: 'condition', placeholder: '조회조건을 선택하세요', options: [
          { label: '제목', value: 'title' },
          { label: '내용', value: 'content' },
          { label: '작성자', value: 'writer' },
          ]
        },
        { label: '검색', type: 'input', model: 'word', placeholder: '검색어를 입력하세요' },
        { label: '등록기간', type: 'date', model: 'period', placeholder: ' 검색하세요' },
      ],
      searchModel: {
        org: '',
        user: '',
        status: '',
      },
    }
  },
}
</script>
<style lang="scss" scoped>
div {
  display: flex;
  align-items: center;
}
.dashboard-notice {
  height: 45px;
  display: flex;
  justify-content: space-between;
  .content-title {
    font-size: 15px;
    font-weight: bold;
    padding: 0px 10px;
  }
  .date {
    font-size: 15px;
    padding: 0px 10px;
  }
  .btn-container {
    padding: 0px 8px;
    img {
      padding: 0px 3px;
    }
  }
}
</style>
