<template>
  <fragment>
    <th>
      라우팅 중복 개수
    </th>
    <td>
      <el-select
        v-model="values"
        multiple
        filterable
        collapse-tags
        size="small"
        @change="handleChange"
      >
        <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="toggleAll()">전체</span></el-option>
        <el-option v-for="cnt in options" :key="cnt" :value="cnt" :label="cnt" />
      </el-select>
    </td>
  </fragment>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'
import { onMessagePopup } from '@/utils'

const routeName = 'RoutingDuplCount'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    valueType: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      values: [],
      // parameterKey: 'nsummaryCnt',
      options: ['0', '1', '2', '3', '4', '5~', '6', '7', '8', '9', '10~']
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(op => op).filter(v => v !== '')
    },
  },
  methods: {
    handleChange(value) { /* override */
      this.updateSelectionWithAll()
      this.emitEventToParent(this.getParameter())
    },
    getParameter() {
     const params = [{ key: 'nsummaryCntMulti', value: this.values.filter(v => v !== '' && v !== '5~' && v !== '10~') }]
     if (this.values.includes('5~') || this.values.includes('10~')) {
        let nsummaryCnt
        if (this.values.includes('10~')) {
          nsummaryCnt = '10'
        }
        if (this.values.includes('5~')) {
          nsummaryCnt = '5'
        }
        /* nsummaryCnt: 해당 값의 이상인 데이터를 조회 (ex. 5이면 NSUMMARY_CNT >= 5) */
        params.push({ key: 'summaryCnt', value: nsummaryCnt })
      } else {
        params.push({ key: 'summaryCnt', value: null })
      }
      return params
    },
  }
}
</script>
<style lang="scss" scoped>
</style>
