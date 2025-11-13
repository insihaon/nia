<template>
  <el-dialog :visible.sync="dialogVisible" custom-class="medium_dialog" center :modal="true" :show-close="true" :close-on-click-modal="false" :close-on-press-escape="false" :append-to-body="true" title="포트변경">
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
      this.$confirm('선택하신 포트로 경로를 변경하시겠습니까?', '포트변경', {
        confirmButtonText: '실행',
        cancelButtonText: '취소',
        dangerouslyUseHTMLString: true,
        customClass: 'nia-message-box',
      }).then(() => {
        this.actionPortSwitch('chngport', row)

        // this.$alert('성공적으로 포트가 변경되었습니다.', '알림', {
        //   confirmButtonText: '변경',
        //   customClass: 'nia-message-box',
        // })

        // this.$store.dispatch('chatbot/botPushAnswerMessage', {
        //   content:
        //     `성공적으로 포트가 변경되었습니다.` +
        //     nextMessage +
        //     `

        //     1. 마감처리
        //     2. SOP 확인
        //     `,
        // })
      })
    },

    async actionPortSwitch(remoteControl, row) {
      const { nodeName, ipAddr, ifname } = this.item
      if (!ipAddr) {
        this.$alert('해당 장비의 IP가 존재하지 않습니다.')
        return
      }

      const newIfId = row.if_id

      const res = await apiRemote(remoteControl, {
        ip: ipAddr,
        param: `nodename=${nodeName}&ifname=${ifname}`,
        user_id: this.$store.state.user.info.uid,
      })

      if (res.success) {
        this.$alert('성공적으로 명령이 실행되었습니다.', '성공', {
          confirmButtonText: '확인',
        })

        const param = {
          uid: this.$store.state.user.info.uid,
          remoteControl: this.remoteControl,
          nodeName: this.item.nodeName,
          ifname: this.item.ifname,
        }

        this.$emit('saveLocalStorage', param)
      } else {
        this.$alert('명령 실행이 실패했습니다.', '실패', {
          confirmButtonText: '확인',
        })
      }
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

.aiResponse_ATT_AIB {
  caret-color: transparent; /* 깜빡이는 커서 숨김 */
}
</style>
