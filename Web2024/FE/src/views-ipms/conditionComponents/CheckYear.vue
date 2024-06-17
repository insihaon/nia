<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      조회년도
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
    >
      <el-option
        v-for="(option, i) in [
          { label: '2024', value: '2024' },
          { label: '2023', value: '2023' },
        ]"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'CheckYear'
export default {
  name: routeName,
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '2024'
    }
  },
  computed: {
  },
  methods: {
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'yyyy', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
