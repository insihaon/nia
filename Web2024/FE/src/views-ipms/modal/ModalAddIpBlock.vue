<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="false"
      :append-to-body="true"
      :modal="modal"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP블록생성
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <h4>IP 블록 생성</h4>
        <table class="form">
          <tr>
            <th>공인/사설</th>
            <td><el-select v-model="selectedRow.sipCreateTypeNm" :disabled="isDisabled" /></td>
            <th>생성차수</th>
            <td>
              <el-select v-model="selectedRow.sipCreateSeqNm" disabled />
            </td>
          </tr>
          <tr>
            <th>서비스망</th>
            <td>
              <el-select v-model="selectedRow.sipServiceNetNm" />
            </td>
            <th>IP 버전</th>
            <td> <el-select v-model="selectedRow.dmodifyDt" :disabled="isDisabled" /></td>
          </tr>
          <tr class="last">
            <th>비고</th>
            <td colspan="3">
              <el-input v-model="description" type="textarea"></el-input>
            </td>
          </tr>
          <tr>
            <th>IP 주소</th>
            <td colspan="3">
              <el-input
                v-model="sipCreateSeqNm"
                style="width: 80%; float: left"
                type="text"
              />
              <el-button size="small" style="float: left; margin: 3px 0 0 5px;" type="info" @click="close()"> 추가 </el-button>
            </td>
          </tr>
        </table>

        <el-row>
          <el-col :span="24">
            <compTable
              :prop-data="tableDatas"
              :prop-table-height="200"
              :prop-column="tableColumns"
              :prop-is-pagination="false"
              :prop-is-check-box="false"
              prop-grid-menu-id="inputSpeed"
              :prop-grid-indx="1"
            >
              <template slot="text-description">
                <span>
                  IP 할당 정보
                </span>
              </template>
              <template slot="button-container">
                <div class="my-1">
                  <el-button size="mini" icon="el-icon-edit" @click="resetData()">초기화</el-button>
                  <el-button size="mini" icon="el-icon-edit" @click="close()">등록</el-button>
                </div>
              </template>
            </compTable>
          </el-col>
        </el-row>

        <h4>IP 블록 처리결과</h4>
        <el-input v-model="ipBlockResult" type="textarea" />
      </el-row>

      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="info" class="el-icon-close" @click.native="close()">
          {{ $t('exit') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'

const routeName = 'ModalAddIpBlock'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      sipCreateSeqNm: '',
      scomment: '',
      type: 'create',
      IpBlockDetail: [],
      tableDatas: [],
      ipBlockResult: '',
      description: '',
      viewType: '',
      tableColumns: [
        { prop: 'mang', label: '순번', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'mang', label: 'IP 블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'mang', label: '시작 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'mang', label: '끝 IP', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'mang', label: '단위블록수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'mang', label: '총 IP수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'mang', label: '삭제', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ]
    }
  },
  computed: {
    isDisabled() {
      return this.viewType !== 'create'
    }
  },
  mounted() {
    // this.onloadIpDetailList
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
     this.selectedRow = model.row
     this.viewType = model.type
    },
    onClose() {},
    onloadIpDetailList() {
     /*  const { key: seq } = this.selectedRow
      const param = seq
      try {
        const res = await apiSelectIpDetailList(param)
        this.IpBlockDetail = res?.result
      } catch (error) {
        console.error(error)
      } */
    },
    resetData() {

    }
  },
}
</script>
<style lang="scss" scoped>
.dynamic-container ::v-deep {
  .optionItem {
    display: flex;
  }
}
</style>
