<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      {{ label }}
    </label>
    <el-date-picker
      v-model="value"
      type="date"
      size="mini"
      @change="handleChange"
    />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'DatePicker'
export default {
  name: routeName,
  extends: Base,
  props: {
    defaultValue: {
      type: String,
      default: null
    },
    label: {
      type: String,
      default: '조회일자'
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: this.moment().subtract(1, 'd')
    }
  },
  methods: {
    handleChange() {
      this.$emit('update-value', { key: 'searchSingleDate', value: this.moment(this.value).format('YYYY-MM-DD') })
   }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-date-editor {
  width: 100%;
}
</style>
