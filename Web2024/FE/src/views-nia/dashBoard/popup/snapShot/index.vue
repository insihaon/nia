<template>
  <div :class="{ [name]: true, 'w-full h-full': true }">
    <div class="d-flex" style="flex-direction: column;">
      <CompInquiryPannel
        ref="CompInquiryPannel"
        :items="searchItems"
        :search-model.sync="searchModel"
        class="w-100"
        style="height: 180px"
        :hide-size-change-btn="true"
        :hide-search-btn="true"
        :hide-refresh-btn="true"
      >
        <template slot="add-function">
          <el-button type="info" size="mini" icon="el-icon-lock" @click="onClickSaveSnapshot">저장</el-button>
        </template>
      </CompInquiryPannel>
      <div v-show="isShowHist" class="common-padding">
        <CompAgGrid
          ref="snapshotAgGrid"
          v-model="snapshotAgGrid"
          class="w-100 flex-fill"
          style="height: 300px"
        />
        <!-- :pagination-info="paginationInfo"
          @pageChange="onChangePage" -->
      </div>
    </div>
    <el-row>
      <el-col align="right" >
        <div class="my-4 mx-4">
          <el-button size="mini" type="info" plain class="btn-r" @click.native="onClickShowHistBtn">  {{ isShowHist ? '닫기' : '이력 보기' }} </el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompAgGrid from '@/components/aggrid/CompAgGrid.vue'
import { apiDeleteSnapshot, apiSelectSnapshotList, apiSendMQ } from '@/api/nia'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
// import snapshotHistory from '@/views-nia/pages/dataSnapshot/snapshotHistory.vue'
import CellRenderSnapshot from '@/views-nia/components/cellRenderer/CellRenderSnapshot'
import _ from 'lodash'

const routeName = 'snapShot'

export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: { CompAgGrid, CompInquiryPannel, CellRenderSnapshot },
  directives: { elDragDialog },
  extends: Modal,
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      }
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      visible: false,
      ticketInfo: {},
      isShowHist: false,
      snapshotHistList: [],
      paginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 50, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
        pagerCount: 11
      },
      searchItems: [
        { label: '제목', size: 24, type: 'input', multiple: false, placeholder: '제목을 입력하세요', icon: 'el-icon-search', model: 'title' },
        {
          label: '분류', type: 'radio', size: 24, model: 'type', options: [
            { value: 'ticket-att2', label: '이상 트래픽' },
            { value: 'ticket-ntt', label: '유해 트래픽' },
            { value: 'ticket-nftt', label: '장비부하 장애' },
        ] },
        { label: '기간', type: 'date', size: 24, model: 'period', disabledCheckBoxShow: false, disabled: false }
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
        { type: '', prop: 'title', name: '제목', width: 100, suppressMenu: true, alignItems: 'center', flex: 1 },
        { type: '', prop: 'start_time', name: '시작 시간', width: 150, suppressMenu: true, alignItems: 'center', format: (row) => { return row.start_time ? this.formatterTimeStamp(row.start_time, 'YYYY/MM/DD-HH:mm:ss') : '' }, flex: 1 },
        { type: '', prop: 'end_time', name: '종료 시간', width: 150, suppressMenu: true, alignItems: 'center', format: (row) => { return row.end_time ? this.formatterTimeStamp(row.end_time, 'YYYY/MM/DD-HH:mm:ss') : '' }, flex: 1 },
        { type: '', prop: '', name: '다운로드', width: 50, suppressMenu: true, alignItems: 'center', cellRendererFramework: 'CellRenderSnapshot', cellRendererParams: { type: 'downroadUrl', action: this.onClickDownload.bind(this) }, flex: 1 },
        { type: '', prop: '', name: '삭제', width: 50, suppressMenu: true, alignItems: 'center', cellRendererFramework: 'CellRenderSnapshot', cellRendererParams: { type: 'delete', action: this.onDeleteSnapshot.bind(this) }, flex: 1 },
      ]
      return { options, columns, data: this.snapshotHistList, getRightClickMenuItems: () => { return [] } }
    },
    periodItem() {
      return this.searchItems.find((i) => i.model === 'period')
    }
  },
  watch: {
    isShowHist(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.$set(this.wdata, 'width', newVal ? 700 : 600)
        this.$set(this.wdata, 'height', newVal ? 640 : 310)

        let x = (window.innerWidth - this.wdata.width) * 0.5 + (this.$store.getters.windows.length - 1) * 20
        let y = (window.innerHeight - this.wdata.height) * 0.5 + (this.$store.getters.windows.length - 1) * 20

        if (x < 0) {
          x = 15
        }
        if (y < 0) {
          y = 85
        }
        this.$set(this.wdata, 'x', x)
        this.$set(this.wdata, 'y', y)
      }
    },
  },
  created () {
    this.ticketInfo = this.wdata.params
    this.setRadioType()
  },
  methods: {
    onClickShowHistBtn() {
      this.isShowHist = !this.isShowHist
      this.onLoadSnapshotList()
    },

    setRadioType() {
      switch (this.ticketInfo.ticket_type) {
        case 'ATT2': this.searchModel.type = 'ticket-att2'; break
        case 'NTT': this.searchModel.type = 'ticket-ntt'; break
        case 'NFTT': this.searchModel.type = 'ticket-nftt'; break
      }
    },

    onClickDownload(row) {
      const path = this.$store.state.app.server.snapshotUrl + row.eventno
      window.open(path, '_blank')
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

    onClickSaveSnapshot() {
      if (this.periodItem.disabled) {
        this.$alert('기간을 활성화해야 저장이 가능합니다.', '저장실패')
        return
      }

      this.confirm('저장 하시겠습니까?', '데이터 스냅샷', {
        confirmButtonText: '저장',
        cancelButtonText: '취소',
      }).then(async () => {
        const param = {
          eventType: 'REQUEST_DATA_SNAPSHOT',
          title: this.searchModel.title,
          startTime: this.searchModel.period[0]?.getTime(),
          endTime: this.searchModel.period[1]?.getTime(),
          detail: this.searchModel.type,
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
            // START_DATE: this.periodItem.disabled ? null : this.searchModel.period[0],
            // END_DATE: this.periodItem.disabled ? null : this.searchModel.period[1],
          }
        )
        this.snapshotHistList = res?.result
        // this.paginationInfo.totalCount = res.total // 총 항목 수 설정
        // this.paginationInfo.totalPages = Math.ceil(this.paginationInfo.totalCount / this.paginationInfo.pageSizes) // 전체 페이지 수 계산
      } catch (error) {
        console.error(error)
      } finally {
        this.closeLoading(target)
      }
    },

    onChangePage(curPage) {
      this.paginationInfo.currentPage = curPage
      this.onLoadSnapshotList()
    },
  },
}
</script>

<style lang="scss" scoped></style>
