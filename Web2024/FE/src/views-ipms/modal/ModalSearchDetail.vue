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
      class="nia-edit-dialog"
      :class="{ [name]: true }"
    >
      <span slot="title">
        <i :class="{'el-icon-edit': viewType === 'OPEN', 'el-icon-document': viewType !== 'OPEN'}" class="mr-2" style="font-size: 17px" />
        {{ title }}
        <hr>
      </span>
      <el-row class="w-100 h-100">
        <DynamicComponentLoader
          :component-keys="componentList"
          @handle-search="handleSearch"
        />
        <el-col :span="24">
          <compTable :prop-table-height="300" :prop-column="tableColumns" :prop-is-pagination="false" :prop-is-check-box="false" prop-grid-menu-id="inputSpeed" :prop-grid-indx="1">
            <template slot="text-description">
              <span>
                상품
              </span>
            </template>
          </compTable>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" icon="el-icon-edit" @click="onClickData()">
          {{ '선택' }}
        </el-button>
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
import { mapState } from 'vuex'

const routeName = 'ModalSearchDetail'

export default {
  name: routeName,
  components: {
    DynamicComponentLoader, CompTable
  },
  directives: { elDragDialog },
  extends: Modal,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      viewType: '',
      rowInfo: {},
      componentList: [
        //  { key: 'SsvcLineType', props: { label: '분류', lvl: 3, multi: [2] } },
        //  { key: 'InputType', props: { label: '상품명', } },
        //  { key: 'ServiceOrg', props: { label: '서비스', multi: false } },
        //  { key: 'IncludeYN', props: { label: '사용여부', } },
      ],
       tableColumns: [
        { prop: '', label: '구분', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '서비스', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: 'IP블록', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '용도', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
        { prop: '', label: '분할', align: 'center', sortable: true, columnVisible: true, showOverflow: true }
      ],
    }
  },
  computed: {
    ...mapState({
      viewport: (state) => state.app.viewport,
    }),
    title() {
      return this.label === '장비명' ? '장비정보조회' : '상품정보조회'
    },
  },
  watch: {
  },
  mounted() {
  },
  methods: {
    onCreated() {
      Modal.methods.onCreated.call(this)
      this.closeOnClickModal = false
      this.domElement.maxWidth = 700
    },
    onOpen(model, actionMode) {
      this.label = model.label
      this.rowInfo = this._cloneDeep(model.row)
    },
    onClickData() {

    },
    onClose() {
    },
    handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  },
}
</script>

<style lang="scss" scoped>
@import '~@/styles/variables.scss';

</style>
