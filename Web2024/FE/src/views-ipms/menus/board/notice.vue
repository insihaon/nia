<template>
  <div v-if="isDashboard" class="notice-wrap">
    <div class="notice-title">
      <span>공지사항</span>
      <div class="notice-link-btn" @click="$router.push({ path: '/board/notice' })" />
    </div>
    <div class="notice-content">
      <el-carousel
        height="34px"
        direction="vertical"
        :autoplay="true"
        :interval="3000"
        indicator-position="none"
        arrow="always"
      >
        <el-carousel-item v-for="noti in notice" :key="noti.seq">
          <div class="notice-content-item" @click="handleClickNoticeDetail(noti.seq)">
            <span>[ {{ noti.sboardTypeSubNm }} ]</span>
            <span>{{ noti.dcreateDt.split(' ')[0] }}</span>
            <span>{{ noti.sboardTitle }}</span>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
    <ModalNoticeDetail ref="ModalNoticeDetail" />
  </div>
  <el-row v-else ref="container" class="w-100 h-100">
    <DynamicComponentLoader
      ref="searchCondition"
      :prop-name="name"
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col ref="tableContainer" :span="24">
      <compTable
        ref="compTable"
        style="height: calc(100% - 80px)"
        :prop-name="name"
        :prop-table-height="'100%'"
        :prop-column="tableColumns"
        :prop-data="pagination.data"
        :prop-pagination-data.sync="pagination"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
        :prop-on-click="hadleClickNoticeDetail"
        :prop-on-page-change="handleChangeCurPage"
        :prop-on-page-size-change="handleChangeCurPage"
      >
        <template slot="text-description">
          <span>
            공지목록 조회결과
          </span>
        </template>
        <template slot="add-features">
          <div style="margin-top: 10px">
            <el-button icon="el-icon-document-add" type="primary" size="mini" round @click="fnViewInsertNotice()">글쓰기</el-button>
          </div>
        </template>
      </compTable>
    </el-col>
    <ModalNoticeDetail ref="ModalNoticeDetail" @openUpdate="onOpenUpdate" @reload="fnViewListNotice()" />
    <ModalNoticeInsert ref="ModalNoticeInsert" @reload="fnViewListNotice()" />
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import tableHeightMixin from '@/mixin/tableHeightMixin'
import { ipmsModelApis, apiRequestModel } from '@/api/ipms'
import ModalNoticeInsert from '@/views-ipms/modal/notice/ModalNoticeInsert.vue'
import ModalNoticeDetail from '@/views-ipms/modal/notice/ModalNoticeDetail.vue'

const routeName = 'Notice'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader, ModalNoticeInsert, ModalNoticeDetail },
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
      pagination: this.setDefaultPagination(),
      tableColumns: [
        { prop: 'seq', label: 'SEQ', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sboardTypeSubNm', label: '유형', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sboardTitle', label: '제목', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'smodifyNm', label: '등록자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dcreateDt', label: '등록일', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dcreateDt ? this.moment(row.dcreateDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'dnotiStartDt', label: '팝업게시시작일', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dnotiStartDt ? this.moment(row.dnotiStartDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'dnotiEndDt', label: '팝업게시종료일', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dnotiEndDt ? this.moment(row.dnotiEndDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'nreadCnt', label: '조회건수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      componentList: [
        { key: 'NoticeGubun', props: { } },
        { key: 'BoardSearchCondition', props: { defaultValue: 'title' } },
        { key: 'DateRange', props: { label: '등록기간' } },
      ],
      interval: null,
      currentIndex: 0,
      noticeList: []
    }
  },
  computed: {
    notice() {
      return this.noticeList
    }
  },
  mounted() {
    // if (this.isDashboard) {
    //   this.interval = setInterval(this.noticeScroll, 3000)
    // }
    setTimeout(() => {
      this.fnViewListNotice()
    }, 100)
  },
  beforeDestroy() {
    clearInterval(this.interval) // 컴포넌트 파기 전에 setInterval 제거
  },
  methods: {
    handleSearch(requestParameter) {
      // console.log(requestParameter)
      this.fnViewListNotice()
    },
    hadleClickNoticeDetail(row) {
      this.handleClickNoticeDetail(row.seq)
    },
    async fnViewListNotice(requestParameter = null) {
      /* 대시보드 공지사항 조회 조건 확인필요함 */
      const parameter = requestParameter ?? this.$refs?.searchCondition?.requestParameter ?? { searchCnd: 'title', sboardTypeSubCd: 'BM0002' }
      const target = ({ vue: this.$refs.compTable })
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.openLoading(target)
        const res = await apiRequestModel(ipmsModelApis.viewListNotice, parameter)
        this.pagination.data = res.result.data ?? []
        this.noticeList = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleChangeCurPage(v) {
      if (v) this.pagination.currentPage = v
      this.fnViewListNotice()
    },
    fnViewInsertNotice() {
      this.$refs.ModalNoticeInsert.open()
    },
    onOpenUpdate(seq) {
      this.$refs.ModalNoticeInsert.open({ viewType: 'U', notiSeq: seq })
    },
    handleClickNoticeDetail(seq) {
      setTimeout(() => {
        this.$refs.ModalNoticeDetail.open({ param: { seq }, isDashboard: this.isDashboard })
      }, 100)
    },
    noticeScroll() {
      this.currentIndex = (this.currentIndex + 1) % (this.noticeList.length || 0) // Increment currentIndex circularly
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
      const notiLen = this.noticeList?.length
      this.currentIndex = (currentIndex - 1 + notiLen) % notiLen
      this.showItem(this.currentIndex)
    },
    showNextItem() {
      const currentIndex = this.currentIndex
      this.currentIndex = (currentIndex + 1) % this.noticeList.length
      this.showItem(this.currentIndex)
    }
  },
}
</script>
<style lang="scss" scoped>
// .dashboard-notice {
//   height: 60px;
//   display: flex;
//   padding: 20px;
//   margin-bottom: 20px;
//   align-items: center;
//   justify-content: space-between;
//   border-top: solid 1px #cbcbcb;
//   border-bottom: solid 1px #cbcbcb;
//   .content-container {
//     height: 22px;
//     width: 800px;
//     overflow: hidden;
//     position: relative;
//     .noti-title {
//       width: 100px;
//     }
//     .rolling {
//       height: 22px;
//       width: 100%;
//       overflow: hidden;
//       position: relative;
//       .roll-item {
//         width: 100%;
//         position: absolute;
//         display: flex;
//         justify-content: flex-start;
//         align-items: center;
//         top: 0;
//         left: 0;
//         height: 22px;
//         transition: transform 0.5s ease-in-out;
//         &:hover {
//           .content-title {
//             text-decoration: underline;
//           }
//         }
//       }
//     }
//   }
//   .content-title {
//     font-size: 15px;
//     font-weight: bold;
//     padding: 0px 10px;
//   }
//   .date {
//     font-size: 15px;
//     font-weight: bold;
//     padding: 0px 10px;
//   }
//   .btn-container {
//     display: flex;
//     align-items: flex-start;
//     padding: 0px 8px;
//     img {
//       padding: 0px 3px;
//       &:hover {
//         cursor: pointer;
//         filter: grayscale(1);
//       }
//     }
//   }
//   .more {
//     i {
//       width: 20px;
//       height: 20px;
//       display: flex;
//       border: solid 1px;
//       align-items: center;
//       justify-content: center;
//       &:hover {
//         cursor: pointer;
//         color: red;
//       }
//     }
//   }
// }

</style>
