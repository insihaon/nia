<template>
  <div v-if="isDashboard" class="dashboard-notice">
    <div class="content-container d-flex">
      <h3 class="noti-title">공지사항</h3>
      <div id="rolling" class="rolling">
        <template v-for="(noti, index) in notices">
          <div id="roll-item" :key="index" class="roll-item" :style="{'transform':`translateY(${index * 22}px)`}">
            <span class="date">{{ noti.date }}</span>
            <span class="content-title">{{ noti.title }}</span>
          </div>
        </template>
      </div>
    </div>
    <div class="d-flex">
      <div class="btn-container">
        <img src="@/assets/images/ipms/content/btn_noti_up_off.gif" alt="이전글" title="이전글" @click="showPrevItem()" />
        <img src="@/assets/images/ipms/content/btn_noti_down_off.gif" alt="다음글" title="다음글" @click="showNextItem()" />
      </div>
      <div class="more">
        <i class="el-icon-plus" style="font-size: 16px;" @click="$router.push({ path: '/board/notice' })" />
      </div>
    </div>
  </div>
  <el-row v-else ref="container" class="w-100 h-100 pt-4 px-12">
    <DynamicComponentLoader
      ref="searchCondition"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            공지목록 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'

const routeName = 'Notice'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  mixins: [tableHeightMixin],
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
      componentList: [
        { key: 'NoticeGubun', props: { } },
        { key: 'BoardSearchCondition', props: { defaultValue: 'title' } },
        { key: 'DateRange', props: { label: '등록기간' } },
      ],
      interval: null,
      notices: [
        { title: '[긴급 1] IPMS 시스템 관련공지 모두가 주목해야할 공지사항입니다.', date: '2024-06-03' },
        { title: '[긴급 2] IPMS 시스템 관련공지 모두가 주목해야할 공지사항입니다.', date: '2024-06-04' },
        { title: '[긴급 3] IPMS 시스템 관련공지 모두가 주목해야할 공지사항입니다.', date: '2024-06-05' },
      ],
      currentIndex: 0
    }
  },
  mounted() {
    this.interval = setInterval(this.noticeScroll, 3000)
  },
  beforeDestroy() {
    clearInterval(this.interval) // 컴포넌트 파기 전에 setInterval 제거
  },
  methods: {
    handleSearch(requestParameter) {
      console.log(requestParameter)
    },
    noticeScroll() {
      this.currentIndex = (this.currentIndex + 1) % this.notices.length // Increment currentIndex circularly
      this.showItem(this.currentIndex)
    },
    showItem(index) {
      const items = document.querySelectorAll('#roll-item')
      items.forEach((item, i) => {
          item.style.transform = `translateY(${(i - index) * 22}px)`
      })
    },
    showPrevItem() {
      const currentIndex = this.currentIndex
      const notiLen = this.notices?.length
      this.currentIndex = (currentIndex - 1 + notiLen) % notiLen
      this.showItem(this.currentIndex)
    },
    showNextItem() {
      const currentIndex = this.currentIndex
      this.currentIndex = (currentIndex + 1) % this.notices.length
      this.showItem(this.currentIndex)
    }
  },
}
</script>
<style lang="scss" scoped>
.dashboard-notice {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .content-container {
    height: 22px;
    width: 650px;
    overflow: hidden;
    position: relative;
    .noti-title {
      width: 100px;
    }
    .rolling {
      height: 22px;
      width: 100%;
      overflow: hidden;
      position: relative;
      .roll-item {
        width: 100%;
        position: absolute;
        display: flex;
        justify-content: center;
        align-items: center;
        top: 0;
        left: 0;
        height: 22px;
        transition: transform 0.5s ease-in-out;
        &:hover {
          .content-title {
            text-decoration: underline;
          }
        }
      }
    }
  }
  .content-title {
    font-size: 15px;
    font-weight: bold;
    padding: 0px 10px;
  }
  .date {
    font-size: 15px;
    font-weight: bold;
    padding: 0px 10px;
  }
  .btn-container {
    display: flex;
    align-items: flex-start;
    padding: 0px 8px;
    img {
      padding: 0px 3px;
      &:hover {
        cursor: pointer;
        filter: grayscale(1);
      }
    }
  }
  .more {
    i {
      width: 20px;
      height: 20px;
      display: flex;
      border: solid 1px;
      align-items: center;
      justify-content: center;
      &:hover {
        cursor: pointer;
        color: red;
      }
    }
  }
}

</style>
