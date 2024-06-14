<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      작업일자
    </label>
    <el-date-picker
      v-model="localValue"
      type="daterange"
      size="mini"
      start-placeholder="시작일"
      end-placeholder="종료일"
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
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
    }
  },
  computed: {
    localValue: {
      get() {
        return this.value
      },
      set(newValue) {
        const [searchBgnDe, searchEndDe] = newValue
        this.$emit('set-value', newValue)
        this.$emit('update-value', [
          { key: 'searchBgnDe', value: this.moment(searchBgnDe).format('YYYY-MM-DD') },
          { key: 'searchEndDe', value: this.moment(searchEndDe).format('YYYY-MM-DD') }
        ])
      }
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-range-editor.el-input__inner {
  width: 100%;
}
</style>
