<template>
  <el-col :class="{ [name]: true }" :xs="24" :sm="12" :md="12" :lg="8" :xl="6">
    <label>
      {{ label }}
    </label>
    <el-input v-model="word" size="mini" clearable @change="handleChangeWord" />
  </el-col>
</template>
<script>
import { Base } from '@/min/Base.min'

const routeName = 'InputType'
export default {
  name: routeName,
  extends: Base,
  props: {
    value: {
      type: String,
      default: ''
    },
    exceptOptions: {
      type: Object,
      default() { return {} }
    }
  },
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      word: ''
    }
  },
  computed: {
    localValue: {
      get() {
        return this.value
      },
      set(newValue) {
        this.$emit('set-value', newValue)
        this.$emit('update-value', [{ key: this.exceptOptions.componentKey, value: newValue }])
      }
    },
    label() {
      return this.exceptOptions.label
    }
  },
  methods: {
    handleChangeWord() {
      this.$emit('update-value', [{ key: this.exceptOptions.componentKey, value: this.word }])
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
