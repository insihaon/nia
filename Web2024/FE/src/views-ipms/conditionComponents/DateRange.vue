<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      {{ label }}
    </label>
    <el-date-picker
      v-model="localValue"
      type="daterange"
      size="mini"
      start-placeholder="시작일"
      end-placeholder="종료일"
      @change="handleChange"
    />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'DateRange'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: Array,
      default: () => { return [] }
    },
    label: {
      type: String,
      default: '작업일자'
    },
    componentKey: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      localValue: []
    }
  },
  computed: {
  },
  methods: {
   handleChange() {
      const [searchBgnDe, searchEndDe] = this.localValue

      this.$emit('set-value', this.localValue)
      this.$emit('update-value', [{
        key: this.componentKey,
        value: [
          { key: 'searchBgnDe', value: this.moment(searchBgnDe).format('YYYY-MM-DD') },
          { key: 'searchEndDe', value: this.moment(searchEndDe).format('YYYY-MM-DD') }
        ]
      }])
   }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-range-editor.el-input__inner {
  width: 100%;
}
</style>
