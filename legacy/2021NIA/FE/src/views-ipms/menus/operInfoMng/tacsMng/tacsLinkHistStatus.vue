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
            연동 이력 조회결과
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
const routeName = 'TacsLinkHistStatus'

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
          { prop: '', label: '조회IP블럭', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: 'IP중복여부', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '결과메세지', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '사용자ID', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '접속일시', align: 'center', columnVisible: true, showOverflow: true },
        ],
      componentList: [
        { key: 'InputType', props: { label: '장비 IP', prop_parameterKey: 'pipFcltInet' } },
        { key: 'InputType', props: { label: '장비프롬프트명', prop_parameterKey: 'sfcltPromptNm' } },
        { key: 'InputType', props: { label: '조회IP블럭', prop_parameterKey: 'pipPrifix' } },
        { key: 'ApplyStatus', props: { label: 'IP중복여부', prop_parameterKey: 'savailYn',
          defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: '중복 아님', value: 'Y' },
              { label: '중복', value: 'N' },
            ]
          }
        },
        { key: 'InputType', props: { label: '사용자ID', prop_parameterKey: 'screateId' } },
        { key: 'DateRange', props: { label: '접속일자' } },
        {
          key: 'ApplyStatus', props: {
            label: '결과메시지',
            prop_parameterKey: 'sresultMsg',
            defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: '요청 실패(릴레이가 없거나 망문제)', value: '요청 실패(릴레이가 없거나 망문제)' },
              { label: '로그인 실패', value: '로그인 실패' },
              { label: '등록된 장비가 없음', value: '등록된 장비가 없음' },
              { label: '알수 없는 에러', value: '알수 없는 에러' },
              { label: '성공적으로 처리되었습니다.', value: '성공적으로 처리되었습니다.' },
            ]
          }
        }
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
