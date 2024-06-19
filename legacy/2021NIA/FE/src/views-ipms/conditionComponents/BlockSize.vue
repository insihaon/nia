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
      @change="handleChange"
    >
      <el-option label="전체" value="ALL"><span class="w-100 h-100 d-inline-block" @click="toggleAll">전체</span></el-option>
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
import { onMessagePopup } from '@/utils/index'

const routeName = 'BlockSize'
export default {
  name: routeName,
  extends: Base,
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
      values: [],
      options: [...Array(33).keys()],
    }
  },
  computed: {
    fullOptions() {
      return this.options.map(op => op).filter(v => v !== 'ALL')
    },
  },
  // sblockSizeMulti
  methods: {
    handleChange() {
      if (this.values.length === this.fullOptions.length && !this.value.includes('ALL')) {
        this.values.push('ALL')
      } else if (this.values.includes('ALL') && this.values.length !== this.fullOptions.length + 1) {
        this.values = this.values?.filter(v => v !== 'ALL')
      }
      this.onCheckLimit()

      this.$emit('update-value', [{ key: 'sblockSizeMultiStr', value: this.value.join(';') }])
    },
    toggleAll() {
      this.value = this.values.includes('ALL') ? [] : ['ALL', ...this.fullOptions]
      this.onCheckLimit()
    },
    onCheckLimit() {
      if (this.values.length > this.limit) {
        onMessagePopup(this, `계위는 최대 ${this.limit}개까지 선택 가능합니다.`)
        this.values = []
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.el-select {
  width: 100%;
}
</style>
