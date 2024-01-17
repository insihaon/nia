<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div>
      <el-popover
        placement="bottom"
        width="400"
        trigger="click"
      >
        <el-table :data="popOverData">
          <el-table-column width="150" property="table_nm" label="table_nm" />
          <el-table-column width="100" property="column_nm" label="column_nm" />
          <el-table-column width="100" property="data_type" label="data_type" />
        </el-table>
        <!-- <CompAgGrid
        ref="CompTemplateTable"
        v-model="popOverAgGrid"
        class="w-100 h-100"
        @click="openPopOver()"
        /> -->
        <el-button v-if="params.detailButton.key === 'detail'" slot="reference" :type="'primary'" size="mini" plain @click.native="openPopOver(params)"> {{ params.detailButton.name }}</el-button>
        <el-button v-if="params.cancleButton.key === 'cancle'" slot="reference" :type="'danger'" size="mini" :disabled="isDisabledBtn" plain @click.native="openModal(params)"> {{ params.cancleButton.name }} </el-button>
      </el-popover>
    </div>
  </div>
</template>

  <script>
  import { Base } from '@/min/Base.min'
  import Vue from 'vue'
  import CompAgGrid from '@/components/aggrid_/CompAgGrid.vue'
  import { apiSelectDataSetPopOver } from '@/api/dataHub'

  const routeName = 'CellRenderDataSetButtons'

  export default Vue.extend({
    name: routeName,
    // eslint-disable-next-line vue/no-unused-components
    components: { CompAgGrid, apiSelectDataSetPopOver },
    extends: Base,
    data() {
      return {
        name: routeName,
        dataSetTableData: {},
        popOverData: []
      }
    },
    computed: {
      popOverAgGrid() {
      const options = {
        name: this.name + 'table1', checkable: false, rowGroupPanel: false, rowHeight: 40, rowSelection: 'multiple', rowMultiSelection: false
      }
      const columns = [
      { type: '', prop: 'table_nm', name: '테이블', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
      { type: '', prop: 'column_nm', name: '컬럼', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false },
      { type: '', prop: 'data_type', name: '타입', minWidth: 100, flex: 1, suppressMenu: true, alignItems: 'center', sortable: true, filterable: false }
      ]
      return { options, columns, data: this.dataSetTableData, getRightClickMenuItems: () => { return [] } }
    },
      isDisabledBtn() {
         return this.params.data.status_cd === this.CONSTANTS.authDataSet.GRANT.code || this.params.data.status_cd === this.CONSTANTS.authDataSet.REJECT.code
      },
      // eslint-disable-next-line vue/return-in-computed-property
      isChangeBtn() {
        // switch (this.params.name) {
        //   case '상세정보' : return 'primary'
        //   case '요청취소' : return 'danger'
        //   default: return 'danger'
        // }
      },

    },
    moutned() {
    },
    methods: {
      openModal(params) {
        params.action(params.type, params.data)
      },
      async openPopOver(params) {
        this.dataSetTableData = params.data
        const param = {
          dataset_id: this.dataSetTableData.dataset_id
        }
        try {
          const res = await apiSelectDataSetPopOver(param)
          this.popOverData = res?.result
        } catch (error) {
          console.log(error)
        }
      },
    }
  })
  </script>
  <style lang="scss" scoped>
  @import "~@/styles/dataHub.scss";
    .el-button{
      padding-bottom: 5px !important;
      height: 20px !important;
      font-size: 12px !important;
      line-height: 5px;
    }

  </style>
