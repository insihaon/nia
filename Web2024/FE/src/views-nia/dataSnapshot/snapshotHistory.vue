<template>
  <div :class="{ [name]: true, 'w-100':true }">
    <div class="common-padding">
      <div class="search-container">
        <div class="optionBox">
          <!-- 조회 옵션상자 -->
          <el-row class="optionRow" :class="{ 'd-flex flex-column': isModal }">
            <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
              <div class="optionItem">
                <label> 제목 </label>
                <div>
                  <el-input
                    v-model="registItem.title"
                    size="mini"
                    type="text"
                    :style="{'width': isModal? '200px': '100%'}"
                    placeholder="제목을 입력하세요"
                    clearable
                  />
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
              <div class="optionItem">
                <label> 분류 </label>
                <div>
                  <el-radio-group v-model="registItem.type" size="mini" :class="{'flex-column': isMobile}">
                    <el-radio
                      v-for="option in categoryOptions"
                      v-if="option.show"
                      :key="option.value"
                      :label="option.value"
                      @change="onChangeSnapshotType"
                    >{{ option.label }}</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </el-col>
            <el-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
              <div class="optionItem">
                <label> 기간 </label>
                <div>
                  <el-date-picker
                    v-model="registItem.period"
                    type="datetimerange"
                    size="mini"
                    start-placeholder="시작 일자"
                    end-placeholder="종료 일자"
                    :default-time="['00:00:00','23:59:59']"
                  />
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" align="center" class="searchBtnGroup">
              <el-button class="btn-r" type="info" size="mini" icon="el-icon-folder-opened" @click="onClickSnapshot()">
                저장
              </el-button>
            </el-col>
          </el-row>
        </div>
      </div>
      <CompInquiryPannel
        v-if="isShowHist"
        ref="snapshotAgGrid"
        :ag-grid="snapshotAgGrid"
        :pagination-info="paginationInfo"
        class="w-100"
        :style="{'height': isModal ? '300px':'' }"
        :is-search="false"
        :is-modal="true"
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
    selectedTicket: {
      type: Object,
      default() { return null }
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
      registItem: {
        title: '',
        type: 'test',
        period: [Date.today(0), Date.today()],
      },
      options: ['장애', '이상트래픽', '유해 트래픽', '장비부하 장애', '광신호이상', '시설', '시험데이터'],
    }
  },
  computed: {
    snapshotAgGrid() {
      const options = {
        name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false, suppressRowClickSelection: true,
      }
      const columns = [
        { type: '', prop: 'title', name: '제목', width: 100, suppressMenu: true, alignItems: 'center' },
        { type: '', prop: 'start_time', name: '시작 시간', width: 150, suppressMenu: true, alignItems: 'center', format: (row) => { return row.start_time ? this.formatterTimeStamp(row.start_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
        { type: '', prop: 'end_time', name: '종료 시간', width: 150, suppressMenu: true, alignItems: 'center', format: (row) => { return row.end_time ? this.formatterTimeStamp(row.end_time, 'YYYY/MM/DD-HH:mm:ss') : '' } },
        { type: '', prop: '', name: '다운로드', width: 50, suppressMenu: true, alignItems: 'center', cellRendererFramework: 'CellRenderSnapshot', cellRendererParams: { type: 'downroadUrl', action: this.onClickDownload.bind(this) } },
        { type: '', prop: '', name: '삭제', width: 50, suppressMenu: true, alignItems: 'center', cellRendererFramework: 'CellRenderSnapshot', cellRendererParams: { type: 'delete', action: this.onDeleteSnapshot.bind(this) } },
      ]
      return { options, columns, data: this.snapshotHistList }
    },
    categoryOptions() {
      const isTicketNull = this.selectedTicket === null
      return [
      { value: 'fault', label: '장애', show: isTicketNull },
      { value: 'ticket-att2', label: '이상 트래픽', show: true },
      { value: 'ticket-ntt', label: '유해 트래픽', show: true },
      { value: 'ticket-nftt', label: '장비부하 장애', show: true },
      { value: 'perf', label: '광신호이상', show: isTicketNull },
      { value: 'resources', label: '시설', show: isTicketNull },
      { value: 'test', label: '시험', show: isTicketNull }
    ]
  }
  },
  watch: {
    selectedTicket(nVal, oVal) {
      this.setRadioType()
    }
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
    setRadioType() {
      if (this.isModal) {
        let registType = ''
        switch (this.selectedTicket.ticket_type) {
          case 'ATT2':
          registType = 'ticket-att2'
            break
          case 'NTT':
          registType = 'ticket-ntt'
            break
          case 'NFTT':
          registType = 'ticket-nftt'
            break
          default:
            break
        }
        this.registItem.type = registType
      } else {
        this.onLoadSnapshotList()
      }
    },
    async onLoadSnapshotList() {
      const target = { vue: this.$refs.trafficAnalysis }
      this.isShowHist && this.openLoading(target)
      try {
        const res = await apiSelectSnapshotList({ EVENT_GB: this.registItem.type })
        this.snapshotHistList = res?.result
        this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.isShowHist && this.closeLoading(target)
      }
    },
    onClickSnapshot() {
      this.confirm('저장 하시겠습니까?', '데이터 스냅샷', {
        confirmButtonText: '저장',
        cancelButtonText: '취소',
      }).then(async () => {
        const param = {
          eventType: 'REQUEST_DATA_SNAPSHOT',
          title: this.registItem.title,
          startTime: this.registItem.period[0]?.getTime(),
          endTime: this.registItem.period[1]?.getTime(),
          detail: this.registItem.type,
          // ticket_id: this.selectedTicket ? this.selectedTicket.ticket_id : null
        }
        if (this.selectedTicket) {
          Object.assign(param, { ticket_id: this.selectedTicket.ticket_id })
        }
        try {
            const res = await apiSendMQ('dataSnapshot', param)
            if (res.success) {
              this.$alert('저장 되었습니다.', '알림', {
                confirmButtonText: '확인'
              })
              this.onLoadSnapshotList()
            }
        } catch (error) {
          this.$alert('저장에 실패하였습니다.', '알림', {
            confirmButtonText: '확인'
          })
          console.error(error)
        }
      })
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
::v-deep div.subContentWrap div.optionBox>div.optionBoxContent>div.optionItem>label {
  min-width: 70px;
}
::v-deep .CompAgGrid {
  height: 340px !important;
}
::v-deep .el-date-editor--datetimerange.el-input__inner {
  width: 300px;
}
</style>

