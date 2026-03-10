<template>
  <el-option :label="label" :value="value" />
</template>

<script>
export default {
  name: 'CompItem',
  props: {
    // index: {
    //   type: Number,
    //   default: 0
    // },
    source: { // here is: {value: '101군사우체국'}
      type: Object,
      default() {
        return {}
      }
    }
  },
  computed: {
    label() {
      const { source } = this
      return source.name ?? source.value
    },
    value() {
      const { source } = this
      return source.value
    }
  }
}
</script>

