<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label style="width : 100px">
      PROTOCOL
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
    >
      <el-option
        v-for="(option, i) in [
          { label: 'ICMP', value: 'I' },
          { label: 'TCP', value: 'T' },
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

const routeName = 'Protocol'
export default {
  name: routeName,
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: 'I'
    }
  },
  // sprotocolTypeCd
  computed: {
  },
  methods: {
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'sprotocolTypeCd', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
