<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    :title="viewTitle+'검색'"
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
        <th>
          <label>
            {{ label }} 명
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
          <el-button type="primary" size="small" icon="el-icon-search" round @click="handleClickSearch()">조회</el-button>
        </td>
      </table>
    </div>
    <compTable
      ref="compTable"
      :prop-name="name"
      :prop-data="tableDatas"
      :prop-column="tableColumns"
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
          {{ viewTitle }} 조회결과
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

const routeName = 'ModalEntireOrgSearch'

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
      viewTitle: null
    }
  },
  computed: {
    label() {
      let columnLabel = ''
        if (this.viewTitle === '노드' || this.viewTitle === '주노드') {
          columnLabel = '노드'
        } else {
          columnLabel = '센터/지역본부'
        }
        return columnLabel
      },
    tableColumns() {
        let labelFrifix = ''

        switch (this.viewTitle) {
          case '노드':
            labelFrifix = '국사'
            break
          case '주노드':
            labelFrifix = '국사'
            break
          default:
            labelFrifix = '센터/지역본부'
        }
      return [
        { prop: 'slvlCd', label: `${labelFrifix} ID`, width: 150, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'slvlNm', label: `${labelFrifix} 명`, align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ]
    }
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
      if (model.viewTitle) {
        this.viewTitle = model?.viewTitle
      }
    },
    onClose() {
      if (this.selectedRow !== null) {
        if (this.viewTitle !== null) {
          this.$emit('selected-value', this.selectedRow, this.viewTitle)
        } else {
          this.$emit('selected-value', this.selectedRow)
        }
        this.searchTxt = ''
        this.tableDatas = []
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
    handleClickSearch() {
      switch (this.viewTitle) {
        case '센터/지역본부':
          this.fnSelectSearchLvlCd('Y')
          break
        case '노드':
          this.fnSelectSearchLvlCd('N')
          break
        case '주노드':
          this.fnSelectSearchLvlCd('N')
          break
        default:
          this.fnSelectSearchLvlCd('N')
          break
      }
    },
    async fnSelectSearchLvlCd(flag) {
      let sorgOfficeFlagYn

      if (flag === 'Y') {
        sorgOfficeFlagYn = 'Y'
        } else {
        sorgOfficeFlagYn = 'N'
      }

      const param = { searchWrd: this.searchTxt, sorgOfficeFlagYn: sorgOfficeFlagYn }
      try {
        const res = await apiRequestJson(ipmsJsonApis.selectSearchLvlCd, param)
        this.tableDatas = res.tbLvlCdVos
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
