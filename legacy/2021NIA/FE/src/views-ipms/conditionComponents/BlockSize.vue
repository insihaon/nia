<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      Block 크기
    </label>
    <el-select
      v-model="values"
      multiple
      collapse-tags
      size="mini"
      @change="handleChange()"
    >
      <el-option label="전체" value=""><span class="w-100 h-100 d-inline-block" @click="handleClickAll">전체</span></el-option>
      <el-option
        v-for="value in options"
        :key="value "
        :label="value"
        :value="value"
      />
    </el-select>
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'
import commonFunctionMixin from '@/mixin/commonFunctionMixin'

const routeName = 'BlockSize'
export default {
  name: routeName,
  extends: Base,
  mixins: [commonFunctionMixin],
  props: {
    limit: {
      type: Number,
      default: 5
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      parameterKey: 'sblockSizeMultiStr',
      values: [],
      options: [...Array(33).keys()],
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(op => op).filter(v => v !== '')
    },
  },
  methods: {
    handleChange() {
      this.updateSelectionWithAll()
      this.onCheckLimit('계위')

      this.emitEventToParent(this.getParameter())
    },
    handleClickAll() {
      this.toggleAll()
      this.onCheckLimit('계위')
    },
    getParameter() {
      return [{ key: this.parameterKey, value: this.values.join(';') }]
    },
    setParameter(params) {
      this.values = params[this.parameterKey]?.split(';') ?? []
    }

  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
