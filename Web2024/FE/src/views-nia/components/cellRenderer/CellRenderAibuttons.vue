<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="button-panel">
      <div :class="linkType" size="mini" @click="openModal(params)">
        <i :class="classType" /> {{ getLable }}
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
      } else {
        result = '수정/삭제'
      }
      return result
    },
    classType() {
      let result = ''
      if (this.params.type === 'editAgency' || this.params.type === 'editApp') {
        result = 'el-icon-edit'
      } else {
        result = 'el-icon-circle-check'
      }
      return result
    },
    linkType() {
      let result = ''
      if (this.params.type === 'editAgency' || this.params.type === 'editApp') {
        result = 'edit-class'
      } else {
        result = 'sop-class'
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
    cursor: pointer
  }

  .edit-class {
    text-decoration: underline;
    color: blue !important;
    }
  .edit-class:hover {
    color: rgb(23, 162, 255) !important;
  }
}
</style>
