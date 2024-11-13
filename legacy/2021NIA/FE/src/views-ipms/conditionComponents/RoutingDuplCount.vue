<template>
  <fragment>
    <th>
      라우팅 중복 개수
    </th>
    <td>
      <el-select
        v-model="value"
        allow-create
        filterable
        default-first-option
        @change="handleChange"
      >
        <el-option v-for="cnt in cntList" :key="cnt" :value="cnt" :label="cnt" />
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
      value: '',
      parameterKey: 'nsummaryCnt',
      cntList: ['0', '1', '2', '3', '4', '5~', '10~']
    }
  },
  methods: {
    handleChange(value) { /* override */
      const regex = /^\d+~?$/
      if (!regex.test(value)) {
        onMessagePopup(this, '값은 숫자 단일 값이거나 "숫자~" 으로 입력해주세요.')
        this.value = ''
        return
      }
      this.emitEventToParent(this.getParameter())
    },
  }
}
</script>
<style lang="scss" scoped>
</style>
