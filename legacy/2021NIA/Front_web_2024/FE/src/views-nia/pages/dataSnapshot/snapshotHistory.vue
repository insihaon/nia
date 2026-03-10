<template>
  <div :class="{ [name]: true, 'w-100':true }">
    <div class="common-padding">
      <CompInquiryPannel
        ref="CompInquiryPannel"
        :ag-grid="snapshotAgGrid"
        :items="searchItems"
        :search-model.sync="searchModel"
        :pagination-info="paginationInfo"
        class="w-100 h-100"
        @handleClickSearch="onClickSearch"
        @onChangePage="onChangePage"
      />
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import CellRenderSnapshot from '@/views-nia/components/cellRenderer/CellRenderSnapshot'
import { apiSelectSnapshotList, apiDeleteSnapshot, apiSendMQ } from '@/api/nia'

const routeName = 'SnapshotHistory'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompInquiryPannel, CellRenderSnapshot },
  extends: Base,
  props: {
    isModal: {
      type: Boolean,
      default: false
    },
    /* hist or save */
    isShowHist: {
      type: Boolean,
      default: true
    },
  },
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
      snapshotHistList: [],
      searchItems: [
        { label: '제목', size: 6, type: 'input', multiple: false, placeholder: '제목을 입력하세요', icon: 'el-icon-search', model: 'title' },
        {
          label: '분류', type: 'select', size: 6, model: 'type', placeholder: '분류를 선택하세요', options: [
            { value: 'fault', label: '장애' },
            { value: 'ticket-att2', label: '이상 트래픽' },
            { value: 'ticket-ntt', label: '유해 트래픽' },
            { value: 'ticket-nftt', label: '장비부하 장애' },
            { value: 'perf', label: '광신호이상' },
            { value: 'resources', label: '시설' },
            { value: 'test', label: '시험데이터' }
        ] },
        { label: '기간', type: 'date', size: 6, model: 'period', disabledCheckBoxShow: true, disabled: true }
      ],
      searchModel: {
        title: '',
        type: 'test',
        period: [Date.today(0), Date.today()]
      },
    }
  },
  computed: {
    snapshotAgGrid() {
      const options = {
        name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'title', name: '제목', width: 200, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'start_time', name: '시작 시간', width: 200, suppressMenu: true, alignItems: 'center', format: (row) => { return row.start_time ? this.formatterTimeStamp(row.start_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
        { type: '', prop: 'end_time', name: '종료 시간', width: 200, suppressMenu: true, alignItems: 'center', format: (row) => { return row.end_time ? this.formatterTimeStamp(row.end_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
        { type: '', prop: '', name: '다운로드', width: 50, suppressMenu: true, alignItems: 'center', cellRendererFramework: 'CellRenderSnapshot', cellRendererParams: { type: 'downroadUrl', action: this.onClickDownload.bind(this) } },
        { type: '', prop: '', name: '삭제', width: 50, suppressMenu: true, alignItems: 'center', cellRendererFramework: 'CellRenderSnapshot', cellRendererParams: { type: 'delete', action: this.onDeleteSnapshot.bind(this) } },
      ]
      return { options, columns, data: this.snapshotHistList, getRightClickMenuItems: () => { return [] } }
    },
    periodItem() {
      return this.searchItems.find((i) => i.model === 'period')
    }

  },
  watch: {
  },
  mounted() {
    this.onLoadSnapshotList()
  },
  methods: {
    onSortedChange(param) {
      this.onLoadSnapshotList()
    },
    onClickSearch(params) {
      this.onLoadSnapshotList(params)
    },
    async onLoadSnapshotList() {
      if (!this.searchModel.type || this.searchModel.type.length === 0) {
        this.$alert('분류를 선택해야합니다.', '검색실패')
        return
      }

      const target = { vue: this.$refs.CompInquiryPannel }
      this.openLoading(target)
      try {
        const res = await apiSelectSnapshotList(
          {
            EVENT_GB: this.searchModel.type,
            TITLE: this.searchModel.title,
            START_DATE: this.periodItem.disabled ? null : this.searchModel.period[0],
            END_DATE: this.periodItem.disabled ? null : this.searchModel.period[1],
          }
        )
        this.snapshotHistList = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    onDeleteSnapshot(row) {
      this.confirm('삭제 하시겠습니까?', '알림', {
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
      }).then(async () => {
        try {
            const res = await apiDeleteSnapshot({ EVENT_NO: row.eventno })
            if (res.success) {
              this.$alert('삭제 되었습니다.', '알림', {
                confirmButtonText: '확인'
              })
              this.onLoadSnapshotList()
            }
        } catch (error) {
          this.$alert('삭제에 실패하였습니다.', '알림', {
            confirmButtonText: '확인'
          })
          console.error(error)
        }
      })
    },
    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadSnapshotList()
    },
    onChangeSnapshotType() {
      this.onLoadSnapshotList()
    },
    onClickDownload(row) {
      const path = this.$store.state.app.server.snapshotUrl + row.eventno
      window.open(path, '_blank')
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';
::v-deep div.subContentWrap div.optionBox>div.optionBoxContent>div.optionItem>label {
  min-width: 70px;
}
::v-deep .CompAgGrid {
  height: 340px !important;
}
::v-deep .el-date-editor--datetimerange.el-input__inner {
  width: 300px;
}

input[type="radio"] {
 margin-right: 5px !important;
}

</style>

