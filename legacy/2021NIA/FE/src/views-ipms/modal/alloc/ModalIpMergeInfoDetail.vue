<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="IP병합가능 상세 목록"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <div v-loading="vLoading" class="popupContentTable">
      <div class="popupContentTableTitle">IP병합가능 상세 목록</div>
      <el-row>
        <el-col :span="24">
          <compTable
            ref="compTable"
            :prop-name="name"
            :prop-data="pagination.data"
            :prop-table-height="'100%'"
            :prop-column="tableColumns"
            :prop-is-pgination="false"
            :prop-is-check-box="true"
            :prop-max-select="pagination.data.length"
            prop-grid-menu-id="inputSpeed"
            :prop-grid-indx="1"
            :prop-is-cell-click-check="true"
            :prop-is-multi-check="true"
            :prop-on-select="handleClickTableCheck"
            @update:propCellClick="handleClickCell"
          />
        </el-col>
      </el-row>
    </div>
    <div class="popupContentTableBottom">
      <!-- 병합처리 기능 추가 validation 체크 후 ModalIpAssignMerge -->
      <el-button type="primary" icon="el-icon-close" size="small" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import { onMessagePopup } from '@/utils'
import CompTable from '@/components/elTable/CompTable.vue'

import { ipmsModelApis, apiRequestModel } from '@/api/ipms'

const routeName = 'ModalIpMergeInfoDetail'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      pagination: this.setDefaultPagination(),
      blinkIndexs: [],
      vLoading: false,
      selectedRow: null,
      tableColumns: [
        { prop: 'ssvcLineTypeNm', label: '서비스망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcGroupNm', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssvcObjNm', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sipCreateTypeNm', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignTypeNm', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'pipPrefix', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sassignLevelNm', label: '할당상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nipAllocMstCnt', label: '회선', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'dmodifyDt', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.dmodifyDt ? this.moment(row.dmodifyDt).format('YYYY-MM-DD HH:mm:ss') : '' } },
        { prop: 'sllnum', label: '전용번호', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'ssubscnealias', label: '장비명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        // { prop: 'ssubsclgipportdescription', label: 'I/F명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        // { prop: 'snull0Yn', label: 'Summary 포함 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.snull0Yn?.length === 0 ? '-' : row.snull0Yn } },
        // { prop: 'sintgrmYn', label: 'DB-라우팅 일치 여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'nsummaryCnt', label: '라우팅 중복 개수', align: 'center', sortable: true, columnVisible: true, showOverflow: true,
          formatter: (row, col, value, index) => {
            if (row.nsummaryCnt?.length === 0) {
              return ''
            } else {
              return this.$createElement('el-button', {
                attrs: {
                  round: true, // Adding the round option
                  plain: true,
                  type: row.nsummaryCnt > 0 ? 'danger' : 'primary'
                },
                on: { click: () => {
                  // this.$refs.ModalDetailSummary.open({ row })
              } } }, row.nsummaryCnt)
            }
          }
        },
        { prop: 'scomment', label: '비고', align: 'center', sortable: true, columnVisible: true, showOverflow: true, formatter: (row) => { return row.scomment?.length > 0 ? 'Y' : 'N' } },
      ],

    }
  },
  mounted () {
    this.pagination.pageSize = 100
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1600
    },
    onOpen(model, actionMode) {
      if (model?.row) {
        this.selectedRow = model.row
        this.fnViewMergeDetailList()
      }
    },
    onClose() {
      this.pagination.data = []
      this.pagination.total = 0
      this.blinkIndexs = []
    },
    async fnViewMergeDetailList() {
      const parameter = { groupId: this.selectedRow.groupId }
      const { pageSize: pageUnit, currentPage: pageIndex } = this.pagination
      Object.assign(parameter, { pageUnit, pageIndex })
      try {
        this.vLoading = true
        const res = await apiRequestModel(ipmsModelApis.viewListIpAllocMst, parameter)
        this.pagination.data = res.result.data ?? []
        this.pagination.total = res.result.totalCount
      } catch (error) {
       this.error(error)
      } finally {
        this.vLoading = false
      }
    },
    handleClickTableCheck(all, cur) {
      this.selectedRows = all
      this.addHighlightMergePossibleRows()
    },
    handleClickCell(params) {
      this.selectedRows = params?.all || []
      this.addHighlightMergePossibleRows()
    },
    addHighlightMergePossibleRows() {
      const el = document.querySelectorAll('.el-dialog__body .el-table__body-wrapper tr')
      if (el !== null && el.length > 0) {
        for (let i = 0; i < el.length; i++) {
          el[i].classList.remove('blinking')
        }
      }
      const checkedIndex = this.selectedRows.map(row => row.index)
      const blinkIndexs = this.getExpandedBlock(checkedIndex)
      this.blinkIndexs = blinkIndexs
      setTimeout(() => {
        if (el !== null && el.length > 0) {
          for (let i = 0; i < el.length; i++) {
            if (this.blinkIndexs.includes(i + 1)) {
              el[i].classList.add('blinking')
            }
          }
        }
      }, 100)
    },
    /**
       * 입력 배열이 포함되는 2의 제곱수 길이의 구간을 반환한다.
       * 단, 입력이 이미 해당 구간 전체와 같다면 한 단계 더 확장한다.
       *
       * @param {number[]} arr - 숫자 배열 (하나 이상의 원소가 있다고 가정)
       * @returns {number[]} - 확장된 구간 배열
       */
      getExpandedBlock(arr) {
      if (!arr.length) return [] // 빈 배열이면 그대로 반환

      // 입력 배열을 정렬하여 중복 없이 확인(여기서는 정렬만)
      const sorted = [...arr].sort((a, b) => a - b)
      const min = sorted[0]
      const max = sorted[sorted.length - 1]

      // helper: candidate 블록이 입력과 "완전히 같은 연속 수열"인지 판별
      function isFullBlock(start, blockSize) {
        // 입력 길이와 블록 길이가 같고, 모든 원소가 start부터 연속되는지 확인
        if (sorted.length !== blockSize) return false
        for (let i = 0; i < blockSize; i++) {
          if (sorted[i] !== start + i) return false
        }
        return true
      }
      // 2,4,8,16,... 등 2의 제곱수 크기의 블록에 대해 반복
      for (let blockSize = 2; ; blockSize *= 2) {
        // 주어진 blockSize에 대해 “허용된 구간”은
        // [1 + blockSize * k, 1 + blockSize * k + blockSize - 1] (k>=0) 형태이다.
        // 입력의 최소값(min)이 포함되는 블록은 k = floor((min-1)/blockSize)로 구할 수 있다.
        const k = Math.floor((min - 1) / blockSize)
        const start = 1 + blockSize * k
        const end = start + blockSize - 1
        // 입력의 최대값(max)까지 포함하는지 확인
        if (end < max) {
          // 현재 blockSize로는 입력 전체를 포함할 수 없으므로 다음 크기로 진행
          continue
        }
        // candidate 블록 생성 (연속된 숫자 배열)
        const candidate = Array.from({ length: blockSize }, (_, i) => start + i)
        // 만약 입력 배열이 이미 candidate 전체와 같다면 한 단계 더 확장한다.
        if (isFullBlock(start, blockSize)) {
          continue // 다음 blockSize (즉, blockSize*2)로 확장
        }
        // candidate 블록이 입력을 포함하면서, 입력이 완전한 블록이 아니라면 candidate 반환
        return candidate
      }
    }
  },
}
</script>
<style lang="scss">
@keyframes blink {
  0% {
    background-color: #00d9ff30;
  }
  100% {
  }
}
.blinking {
  animation: blink 1s infinite ease-in-out alternate;
}
</style>
