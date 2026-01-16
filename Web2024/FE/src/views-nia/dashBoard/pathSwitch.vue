<template>
  <el-dialog :visible.sync="dialogVisible" custom-class="medium_dialog" center :modal="true" :show-close="true" :close-on-click-modal="false" :close-on-press-escape="false" :append-to-body="true" title="포트변경" destroy-on-close>
    <div :class="{ [name]: true }" style="height: 470px">
      <div class="d-flex flex-column h-full">
        <!-- prettier-ignore -->
        <CompInquiryPannel
          ref="pathSwitchGrid"
          :ag-grid="pathSwitchAgGrid"
          :is-button-slot="false"
          :is-excel="false"
          :is-search="false"
          :is-grid-loading="loading"
          :pagination-info="pathSwitchPaginationInfo"
          class="w-100 h-100"
          @rowClicked="rowClicked"
        />
        <el-row style="flex: 0 0 35px">
          <el-col align="right" class="mt-2"> </el-col>
        </el-row>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { Base } from '@/min/Base'
import _ from 'lodash'
import CompInquiryPannel from '@/views-nia/components/CompInquiryPannel'
import { apiRemote, apiSelectPortList } from '@/api/nia'
// import { nextMessage } from '@/store/modules/chatbot.js'

const routeName = 'pathSwitch'
export default {
  name: routeName,
  // eslint-disable-next-line vue/no-unused-components
  components: {
    CompInquiryPannel,
  },
  extends: Base,
  mixins: [],
  props: {
    wdata: {
      type: Object,
      default() {
        return {}
      },
    },
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      loading: false,
      dialogVisible: false,
      pathSwitchList: [],
      pathSwitchPaginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      selectedRow: [],
      item: {},
    }
  },
  computed: {
    pathSwitchAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
      const columns = [
        { type: '', prop: 'node_id', name: '노드ID', width: 150, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
        { type: '', prop: 'if_id', name: 'IF ID', width: 130, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
        { type: '', prop: 'isSwitch', name: '우회가능여부', width: 130, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
      ]
      return {
        options,
        columns,
        data: this.pathSwitchList,
        getRightClickMenuItems: () => {
          return []
        },
      }
    },
  },
  created() {},
  mounted() {},
  methods: {
    setVisible() {
      this.dialogVisible = true
      this.$nextTick(() => {
        this.selectedRow = this.wdata.selectedRow
        this.item = this.wdata.item
        this.onLoadPathSwitchList()
      })
    },

    rowClicked(row) {
      this.highlightRow(row)

      if (row.data.isSwitch !== 'Y') {
        this.$alert(`우회가능하지 않은 포트입니다.`, '우회불가능', {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          customClass: 'nia-message-box',
        })
      }

      this.$confirm('선택하신 포트로 파라미터를 설정하시겠습니까?', '경로설정', {
        confirmButtonText: '실행',
        cancelButtonText: '취소',
        dangerouslyUseHTMLString: true,
        customClass: 'nia-message-box',
      }).then(() => {
        this.$emit('actionPathSwitch', row.data)
        this.onClose()
      })
    },

    highlightRow(row) {
      const agGridElement = this.$refs.pathSwitchGrid.$refs.compSearchEquip.$el

      this.$refs.pathSwitchGrid.$refs.compSearchEquip.gridApi.forEachNode((node) => {
        const rowIndex = node.rowIndex
        const rowElement = agGridElement.querySelector(`.ag-center-cols-clipper .ag-row[row-index="${rowIndex}"]`)
        rowElement && rowElement.classList.remove('highlight-row')

        if (row.data.node_id === node.data.node_id && row.data.if_id === node.data.if_id) {
          rowElement && rowElement.classList.add('highlight-row')
        }
      })
    },

    async onLoadPathSwitchList() {
      try {
        this.loading = true
        const res = await apiSelectPortList({ node_id: this.selectedRow.node_id || this.selectedRow.node_nm })

        this.pathSwitchList = res.result.map((r) => {
          r.isSwitch = 'Y'
          return r
        })
        this.pathSwitchPaginationInfo.totalCount = res.total
        this.pathSwitchPaginationInfo.totalPages = Math.ceil(this.pathSwitchPaginationInfo.totalCount / this.pathSwitchPaginationInfo.pageSize) // 전체 페이지 수 계산
        this.loading = false
      } catch (error) {
        this.error(error)
      }
    },

    onClose() {
      this.dialogVisible = false
      /* for Override */
    },
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/animation.scss';

::v-deep .el-dialog__header {
  background: #05092e;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;

  .el-dialog__title {
    color: white;
  }

  .el-dialog__close {
    color: white;
  }
}

::v-deep .el-dialog {
  margin-top: 15vh !important;
  width: 600px;
  border-radius: 20px;
}
</style>
