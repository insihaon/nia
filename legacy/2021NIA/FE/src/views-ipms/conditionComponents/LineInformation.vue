<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      회선정보
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
    <el-input v-model="word" size="mini" clearable @change="handleChangeWord" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'LineInformation'
export default {
  name: routeName,
  extends: Base,
  props: {
    propOptions: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      value: 'llnum',
      word: '',
      options: [
        {
          value: 'llnum',
          label: '전용번호',
        },
        {
          value: 'said',
          label: 'SAID',
        },
        {
          value: 'ordernum',
          label: '오더번호',
        },
      ],
    }
  },
  created () {
    if (this.propOptions !== null) {
      this.options = this.propOptions
    }
  },
  // llSrchTypeCd
  methods: {
    handleChange() {
      this.$emit('update-value', [{ key: 'llSrchTypeCd', value: this.value }])
    },
    handleChangeWord() {
      this.$emit('update-value', [{ key: 'llSrchVal', value: this.value }])
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 200px;
}
</style>
