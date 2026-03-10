<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="viewTitle+' 검색'"
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
    <div class="popupContentTable">
      <table>
        <tr>
          <th>
            <label>
              {{ viewTitle === '수용국' ? viewTitle : '운용조직' }} 명
            </label>
          </th>
          <td>
            <el-input
              v-model="searchTxt"
              size="mini"
              clearable
              @keyup.enter.native="handleClickSearch()"
            />
          </td>
          <td>
            <el-button type="primary" size="small" icon="el-icon-search" round @click="handleClickSearch()">
              조회
            </el-button>
          </td>
        </tr>
      </table>
    </div>
    <compTable
      ref="compTable"
      :prop-name="name"
      :prop-data="tableDatas"
      :prop-table-height="300"
      :prop-column.sync="tableColumns"
      :prop-is-pagination="false"
      :prop-is-check-box="true"
      :prop-is-cell-click-check="true"
      prop-grid-menu-id="inputSpeed"
      :prop-grid-indx="1"
      :prop-enabled-excel-down="false"
      :prop-on-click="handleClickRow"
      :prop-on-dbl-click="handleDbClickRow"
    >
      <template slot="text-description">
        <span>
          운용팀 조회결과
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

const routeName = 'ModalOrgSearch'

export default {
  name: routeName,
  components: { CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewTitle: '운용팀',
      typeByAZ: null,
      searchTxt: '',
      selectedRow: null,
      tableDatas: [],
      cdKey: 'slvlCd',
      nmKey: 'slvlNm',
    }
  },
  computed: {
    tableColumns() {
      let labelPrefix = '운용조직'
      if (this.viewTitle === '수용국') {
        labelPrefix = '수용국'
      }
      return [
        //  { prop: 'sktOrgId', label: `${labelPrefix} ID`, width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: this.cdKey /* labelPrefix === '수용국' ? 'slvlCd' : 'sktOrgId' */, label: `${labelPrefix} ID`, width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: this.nmKey /* labelPrefix === '수용국' ? 'slvlNm' : 'sFullOrgNm' */, label: `${labelPrefix} 명`, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ]
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1000
    },
    onOpen(model, actionMode) {
      if (model?.viewTitle) {
        this.viewTitle = model.viewTitle
        this.typeByAZ = model.type
      }
    },
    onClose() {
      this.searchTxt = ''
      this.tableDatas = []
      if (this.selectedRow !== null) {
        // const keyValues = Object.keys(this.selectedRow).map(key=>{ return { key , value: this.selectedRow[v] }})
        // { label: this.selectedRow['sFullOrgNm'], value: this.selectedRow['sktOrgId'] }
        if (this.typeByAZ !== null) {
          this.$emit('selected-value', { row: this.selectedRow, type: this.typeByAZ })
        } else {
          this.$emit('selected-value', { row: this.selectedRow })
        }
        this.selectedRow = null
      }
    },
    handleSelect() {
      if (this.selectedRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      this.close()
    },
    handleClickSearch() {
      if (this.viewTitle === '수용국') {
        this.fnSelectSearchLvlCd()
      } else {
        this.fnSelectSearchOrgCd()
      }
    },
    async fnSelectSearchLvlCd() {
      const param = { searchWrd: this.searchTxt, sorgOfficeFlagYn: 'N' }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSearchLvlCd, param)
        this.tableDatas = res.tbLvlCdVos
        this.cdKey = 'slvlCd'
        this.nmKey = 'slvlNm'
      } catch (error) {
        this.error(error)
      }
    },
    async fnSelectSearchOrgCd() {
       const param = { searchWrd: this.searchTxt, sorgOfficeFlagYn: 'N' }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSearchOrgBas, param)
        this.tableDatas = res.tbOrgBasVos
        this.cdKey = 'sktOrgId'
        this.nmKey = 'sFullOrgNm'
      } catch (error) {
        this.error(error)
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
::v-deep .el-table__body-wrapper {
  height: 100% !important;
}
</style>
