<template>
  <el-row class="w-100 h-100">
    <DynamicComponentLoader
      :component-keys="componentList"
      @handle-search="handleSearch"
    />
    <el-col :span="24">
      <compTable
        :prop-table-height="'calc(100% - 80px)'"
        :prop-column="tableColumns"
        :prop-is-pagination="true"
        :prop-is-check-box="false"
        prop-grid-menu-id="inputSpeed"
        :prop-grid-indx="1"
      >
        <template slot="text-description">
          <span>
            라우팅 연동 이력 조회결과
          </span>
        </template>
      </compTable>
    </el-col>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import DynamicComponentLoader from '@/views-ipms/components/DynamicComponentLoader.vue'
import CompTable from '@/components/elTable/CompTable.vue'
const routeName = 'RoutingLinkHistStatus'

export default {
  name: routeName,
  components: { CompTable, DynamicComponentLoader },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
        tableColumns: [
          { prop: '', label: '장비IP', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '장비프롬프트명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '진행여부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '결과메세지', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '요청ID', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '요청일시', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '완료ID', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '완료일시', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'InputType', props: { label: '장비 IP', prop_parameterKey: 'ptelnetIp' } },
        { key: 'InputType', props: { label: '장비프롬프트명', prop_parameterKey: 'shostNm' } },
        { key: 'UsageYN', props: { label: '진행여부', prop_parameterKey: 'sprocessYn',
          defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: 'Y', value: 'Y' },
              { label: 'N', value: 'N' },
            ]
          }
        },
        { key: 'DateRange', props: { label: '요청/완료일시' } },
        {
          key: 'ApplyStatus', props: {
            label: '결과메시지',
            prop_parameterKey: 'sresultMsg',
            defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: '(TACS)알수 없는 에러', value: '(TACS)알수 없는 에러' },
              { label: '진행중인 작업이 있어서 중지되었습니다.', value: '진행중인 작업이 있어서 중지되었습니다.' },
              { label: '(IPMS-CHECK) 데이터가 없습니다.', value: '(IPMS-CHECK) 데이터가 없습니다.' },
              { label: '라우팅 정보 수집 중', value: '라우팅 정보 수집 중' },
              { label: '(TACS)로그인 실패', value: '(TACS)로그인 실패' },
              { label: '성공', value: '성공' },
              { label: '(TACS)등록된 장비가 없음', value: '(TACS)등록된 장비가 없음' },
            ]
          }
        },
        // { key: 'InputType', props: { label: '조회IP블럭', prop_parameterKey: 'pipPrifix' } },
        // { key: 'InputType', props: { label: '사용자ID', prop_parameterKey: 'screateId' } },
      ]
    }
  },
  methods: {
      handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  }
}
</script>
<style lang="css" scoped>
</style>
