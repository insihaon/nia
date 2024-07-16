<template>
  <div>
    <el-dialog
      v-if="animationVisible"
      v-el-drag-dialog
      :visible.sync="visible"
      :width="domElement.maxWidth + `px`"
      :fullscreen.sync="fullscreen"
      :modal-append-to-body="true"
      :append-to-body="true"
      :close-on-click-modal="closeOnClickModal"
      :loading="loading"
      class="ipms-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i class="el-icon-document mr-2" style="font-size: 17px" />
        IP블록정보 조회
        <hr>
      </span>
      <el-row ref="container" class="w-100 h-100">
        <DynamicComponentLoader
          ref="searchCondition"
          :component-keys="componentList"
          @handle-search="handleSearch"
        />
        <el-col ref="tableContainer" :span="24">
          <compTable
            :prop-table-height="300"
            :prop-data="tableDatas"
            :prop-column="tableColumns"
            :prop-is-pagination="true"
            prop-grid-menu-id="inputSpeed"
            :prop-grid-indx="1"
            :prop-on-click="handleClickRow"
          >
            <template slot="text-description">
              <span>
                IP 블록 목록
              </span>
            </template>
          </compTable>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" @click="handleClickChoice()">선택</el-button>
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
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'

const routeName = 'ModalIpBlockInfomation'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      selectedRow: null,
      tempVal: '',
      componentList: [
        { key: 'SsvcLineType', props: { lvl: 1,
          propsLvlOptions: {
              1: [
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
              ]
            }
          }
        },
        { key: 'LineInformation', props: {
            isAllOption: true, defaultValue: '',
            prop_options: [
              {
                value: 'llnum',
                label: '전용번호',
                txtKey: 'sllnum'
              },
              {
                value: 'said',
                label: 'SAID',
                txtKey: 'ssaid'
            }]
          }
        },
        { key: 'ServiceOrg', props: { defaultValue: 'SA0001', isAllOption: false, isMulti: false, prop_options: [{ label: '기업고객(고정)', value: 'SA0001' }] } },
        { key: 'InputType', props: { label: 'BitMask', prop_parameterKey: 'nbitmask' } },
        { key: 'IpAddress', props: {} },
        { key: 'IpBlockStatus', props: { label: '할당상태', prop_options: [
          { label: '서비스배정[미할당]', value: 'IA0004' },
          { label: '할당예약', value: 'IA0005' },
          { label: '할당', value: 'IA0006' },
        ] } },
        { key: 'InputType', props: { label: '회선수', prop_parameterKey: 'nipAllocMstCnt' } },
        { key: 'SortType', props: {} },
      ],
      tableColumns: [
        { prop: 'mang', label: '망', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'org', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'node', label: '노드', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'type', label: '공인/사설', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'service', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'block', label: '블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'status', label: '작업상태', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'allocCnt', label: '회선수', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: 'date', label: '작업일자', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
      ],
      tableDatas: [
        { mang: 'KORNET', org: '강남/서부...', node: '안양국사', type: '공인', service: '기업고객(고정)', block: '14.34.85.32/29', status: '할당', allocCnt: 1, date: '2023-04-11' }
      ]
    }
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 1500
    },
    onOpen(model, actionMode) {
    },
    onClose() {
      if (this.selectedRow !== null) {
        this.$emit('selected-value', this.selectedRow)
      }
    },
    handleSearch(requestParameter) {
      Object.assign(requestParameter, {
        sAlcSrchTypeCd: 'order',
        sexSvcCd: '400040055420',
        sicisofficescode: 'R00919',
        sscvUseTypeCd: 'SU0002'
      })
      console.log(requestParameter)
    },
    handleClickRow(row) {
      this.selectedRow = row
    },
    handleClickChoice() {
      if (this.selectedRow === null) {
        this.$alert('선택된 목록이 없습니다. 선택하여 주시기 바랍니다.', '알림', { confirmButtonText: '확인' })
        return
      }
      this.close()
    }
  },
}
</script>
<style lang="scss" scoped>
::v-deep div.optionRow label {
  min-width: 130px;
  width: 130px;
}
</style>
