<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="사용자 검색"
    :visible.sync="visible"
    :width="domElement.maxWidth + `px`"
    :fullscreen.sync="fullscreen"
    :modal-append-to-body="true"
    :append-to-body="true"
    :modal="modal"
    :close-on-click-modal="closeOnClickModal"
    :loading="loading"
    class="ipms-dialog"
    :class="{ [name]: true }"
  >
    <div class="popupContentTable mb-2">
      <table>
        <tr>
          <th>
            <label>
              사용자명
            </label>
          </th>
          <td>
            <el-input v-model="searchTxt" size="small" clearable @keyup.enter.native="fnSearchUser()" />
          </td>
          <td>
            <el-button type="primary" size="small" icon="el-icon-search" round @click="fnSearchUser()">조회</el-button>
          </td>
        </tr>
      </table>
    </div>
    <compTable
      ref="compTable"
      :prop-name="name"
      :prop-data="tableDatas"
      :prop-table-height="300"
      :prop-column="tableColumns"
      :prop-is-pagination="false"
      :prop-is-check-box="true"
      :prop-is-cell-click-check="true"
      prop-grid-menu-id="inputSpeed"
      :prop-grid-indx="1"
      :prop-on-click="handleClickRow"
      :prop-on-dbl-click="handleDbClickRow"
    >
      <template slot="text-description">
        <span>
          사용자 정보 조회결과
        </span>
      </template>
    </compTable>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-edit" round @click="handleSelect()">선택</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">{{ $t('exit') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { onMessagePopup } from '@/utils/index'
import { ipmsJsonApis, apiRequestJson } from '@/api/ipms'

const routeName = 'ModalSearchUserId'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      typeByAZ: null,
      searchTxt: '',
      selectedRow: null,
      tableDatas: [],
      viewTitle: null,
      tableColumns: [
        { prop: 'suserId', label: '사용자ID', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'suserNm', label: '사용자명', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'sposDeptFullNm', label: '소속조직', width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ]
    }
  },
  computed: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {

    },
    onClose() {
      this.searchTxt = ''
      this.tableDatas = []
      if (this.selectedRow !== null) {
        this.$emit('selected-value', { row: this.selectedRow })
        this.selectedRow = null
      }
      this.viewTitle = null
    },
    handleSelect() {
      if (this.selectedRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      this.close()
    },
    async fnSearchUser() {
      if (this.searchTxt.length < 2) {
        onMessagePopup(this, '검색어는 2글자 이상 입력하세요.')
        return
      }
      const target = ({ vue: this.$refs.compTable })
      const userVo = { suserNm: this.searchTxt, suserSttusCd: 'US0001' }
      try {
        this.openLoading(target)
        const res = await apiRequestJson(ipmsJsonApis.selectSearchTbUserBas, userVo)
        this.tableDatas = res.tbUserBasVos
      } catch (error) {
        this.error(error)
      } finally {
        this.closeLoading(target)
      }
    },
    handleClickRow(row) {
      this.selectedRow = row
    },
    handleDbClickRow(row) {
      this.selectedRow = row
      this.close()
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
