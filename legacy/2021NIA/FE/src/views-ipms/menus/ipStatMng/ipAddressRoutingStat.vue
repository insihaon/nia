<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      ref="DynamicComponent"
      :component-keys="componentList"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="300"
        :prop-column="tableColumns"
        :prop-is-pagination="false"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            IP주소 라우팅 비교/점검
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>
</template>
<script>
import { Base } from '@/min/Base.min'
import CompTable from '@/components/elTable/CompTable.vue'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'

const routeName = 'IpAdressRoutingCompare'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        {
          key: 'SsvcLineType', props: { lvl: 3, defaultValueLvl1: 'CL0001',
          propsLvlOptions: {
              1: [
                { label: 'KORNET', value: 'CL0001' },
                { label: 'PREMIUM', value: 'CL0002' },
                { label: 'MOBILE', value: 'CL0003' }
              ]
            }
          }
        },
        { key: 'ServiceOrg', props: { limit: 3 } },
        { key: 'SipCreateType', props: {} },
        // 조회일자 추가
      ],
      tableColumns: [
        {
          prop: '', label: '계위', children: [
            { prop: '', label: '서비스망', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '본부', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '노드', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '공인/사설', align: 'center', columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '전체', children: [
            { prop: '', label: '총합', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '일치', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '현행화율', align: 'center', columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '기업고객(고정)', children: [
            { prop: '', label: '총합', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '일치', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '현행화율', align: 'center', columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '홈고객(유동)', children: [
            { prop: '', label: '총합', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '일치', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '현행화율', align: 'center', columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        },
        {
          prop: '', label: '홈고객(고정)', children: [
            { prop: '', label: '총합', align: 'center', columnVisible: true, showOverflow: true },
            { prop: '', label: '일치', align: 'center', sortable: true, columnVisible: true, showOverflow: true },
            { prop: '', label: '현행화율', align: 'center', columnVisible: true, showOverflow: true },
          ],
          align: 'center',
          columnVisible: true,
          showOverflow: true,
        }
      ],
    }
  },
}
</script>
<style lang="css" scoped></style>
