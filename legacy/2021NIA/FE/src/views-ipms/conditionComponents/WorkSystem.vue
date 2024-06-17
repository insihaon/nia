<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      작업시스템
    </label>
    <el-select
      v-model="value"
      collapse-tags
      size="mini"
      @change="handleChange"
    >
      <el-option
        v-for="(option, i) in options"
        :key="i"
        :label="option.label"
        :value="option.value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'WorkSystem'
export default {
  name: routeName,
  extends: Base,
  props: {
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: '',
      options: [
        {
          value: '',
          label: '전체',
        },
        {
          value: 'IPMS',
          label: 'IPMS',
        },
        {
          value: 'NeOSS',
          label: 'NeOSS',
        },
        {
          value: 'SDN',
          label: 'SDN',
        },

      ],
    }
  },
  // sworkSystem
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'sworkSystem', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
