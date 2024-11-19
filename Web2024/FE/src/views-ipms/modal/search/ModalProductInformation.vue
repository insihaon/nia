<template>
  <el-dialog
    v-if="animationVisible"
    id="ipms"
    v-el-drag-dialog
    title="상품정보조회"
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
    <div class="popupContentTable mb-1">
      <DynamicComponentLoader
        class="dynamic-container"
        :is-show-profile="false"
        :is-show-collapse="false"
        :component-keys="componentList"
        @handle-search="handleSearch"
      />
    </div>
    <compTable
      ref="compTable"
      :prop-name="name"
      :prop-table-height="300"
      :prop-column="tableColumns"
      :prop-is-pagination="false"
      :prop-is-check-box="false"
      prop-grid-menu-id="inputSpeed"
      :prop-grid-indx="1"
      :prop-on-click="handleClickRow"
      :prop-on-dbl-click="handleDbClickRow"
    >
      <template slot="text-description">
        <span>
          상품 목록
        </span>
      </template>
    </compTable>
    <div class="popupContentTableBottom">
      <el-button type="primary" size="small" icon="el-icon-edit" round @click="handleSelect()">선택</el-button>
      <el-button type="primary" size="small" icon="el-icon-close" round @click.native="close()">
        {{ $t('exit') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import { Modal } from '@/min/Modal.min'
import CompTable from '@/components/elTable/CompTable.vue'
import { onMessagePopup } from '@/utils/index'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'

const routeName = 'ModalProductInformation'

export default {
  name: routeName,
  components: { DynamicComponentLoader, CompTable },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      componentList: [
        //  { key: 'SsvcLineType', props: { label: '분류', lvl: 3 } },
        // 이용 목적
         { key: 'InputType', props: { label: '상품명' } },
         { key: 'ServiceOrg', props: { } },
         { key: 'IncludeYN', props: { } },
      ],
       tableColumns: [
        { prop: '', label: '구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        {
          prop: '', label: '서비스분류', children: [
            { prop: '', label: '대분류', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '소분류', align: 'center', columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        { prop: '', label: '상품명', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '이용목적', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'NeOSS상품코드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '사용여부', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1200
    },
    onOpen(model, actionMode) {
    },
    onClose() {
      if (this.selectedRow !== null) {
        this.$emit('selected-value', { row: this.selectedRow })
      }
    },
    handleSelect() {
      if (this.selectedRow === null) {
        onMessagePopup(this, '선택된 목록이 없습니다. 선택하여 주시기 바랍니다.')
        return
      }
      this.close()
    },
    handleSearch(requestParameter) {
      console.log(requestParameter)
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
.dynamic-container ::v-deep {
  .optionItem {
    display: flex;
  }
}
</style>
