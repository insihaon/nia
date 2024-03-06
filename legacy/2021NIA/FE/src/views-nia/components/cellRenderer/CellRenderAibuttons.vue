<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="button-panel">
      <div class="common-btn" size="mini" @click="openModal(params)">
        <i class="el-icon-circle-check" /> {{ getLable }}
      </div>
    </div>
  </div>
</template>
<script>
import { Base } from '@/min/Base.min'
import Vue from 'vue'
const routeName = 'CellRenderAibuttons'

export default Vue.extend({
  extends: Base,
  data() {
    return {
      name: routeName,
    }
  },
  moutned() {
  },
  computed: {
    getLable() {
      let result = ''
      if (this.params.type === 'sop') {
        result = 'SOP'
      } else if (this.params.type === 'alarm') {
        result = '장애 대응'
      }
      return result
    }
  },
  methods: {
    openModal(params) {
      params.action(params.data, params.type)
    },
  },
})
</script>
<style lang="scss" scoped>
.CellRenderAibuttons{
  &:hover{
    color: red !important;
  }
}
</style>
