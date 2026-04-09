<template>
  <el-dialog :visible.sync="dialogVisible" custom-class="medium_dialog" center :modal="true" :show-close="true" :close-on-click-modal="false" :close-on-press-escape="false" :append-to-body="true" title="데이터 목록">
    <div :class="{ [name]: true }" style="height: 470px">
      <div class="d-flex flex-column h-full">
        <!-- prettier-ignore -->
        <CompInquiryPannel
          ref="nttTicketDataListGrid"
          :ag-grid="nttTicketDataListAgGrid"
          :is-button-slot="false"
          :is-excel="false"
          :is-search="false"
          :is-grid-loading="loading"
          :pagination-info="nttTicketDataListPaginationInfo"
          class="w-100 h-100"
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

const routeName = 'nttTicketDataList'
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
      nttTicketDataListList: [],
      nttTicketDataListPaginationInfo: {
        currentPage: 1, // 현재 페이지
        pageSize: 100, // 페이지당 항목 수
        totalCount: 0, // 총 항목 수
        totalPages: null, // 전체 페이지 수
      },
      selectedRow: [],
    }
  },
  computed: {
    nttTicketDataListAgGrid() {
      const options = { name: this.name, checkable: false, rowGroupPanel: false, rowSelection: 'multiple', rowMultiSelection: false }
      const columns = [
        { type: '', prop: 'src_ip', name: '출발 IP', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
        { type: '', prop: 'src_port', name: '출발 포트', width: 150, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
        { type: '', prop: 'dst_ip', name: '도착 IP', width: 200, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
        { type: '', prop: 'dst_port', name: '도착 포트', width: 130, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
        { type: '', prop: 'packet_bytes', name: 'packet bytes', width: 130, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
        // { type: '', prop: 'collect_timestamp', name: '수집시간', width: 130, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false, flex: 1 },
      ]
      return {
        options,
        columns,
        data: this.nttTicketDataListList,
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

        this.nttTicketDataListList = this.selectedRow
        this.nttTicketDataListPaginationInfo.totalCount = this.selectedRow.length
        this.nttTicketDataListList.totalPages = Math.ceil(this.nttTicketDataListList.totalCount / this.nttTicketDataListPaginationInfo.pageSize)
      })
    },

    onClose() {
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
  width: 800px;
  border-radius: 20px;
}
</style>
