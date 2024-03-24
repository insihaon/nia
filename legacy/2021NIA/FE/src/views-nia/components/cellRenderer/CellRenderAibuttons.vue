<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div v-if="isShow()" class="button-panel">
      <div :class="linkType" size="mini" @click="openModal(params)">
        <i :class="{['el-icon-'+params.icon] : true}" /> {{ getLable }}
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
  computed: {
    getLable() {
      return this.params?.name ?? ''
    },
    linkType() {
      if (this.params.type.includes('edit')) {
        return 'edit-class'
      } else {
        return `${this.params.type}-class`
      }
    }
  },
  methods: {
    isShow() {
      const { data, type } = this.params
      if (data.ticket_type === 'SYSLOG' && type === 'alarm') {
        return false
      }
      return true
    },
    openModal(params) {
      params.action(params.data, params.type)
    },
  },
})
</script>
<style lang="scss" scoped>
.CellRenderAibuttons {
  &:hover{
    color: red !important;
    cursor: pointer
  }
  .edit-class {
    text-decoration: underline;
    color: blue !important;
  }
  &:hover{
    color: rgb(23, 162, 255) !important;
  }
  .node-mng-class {
    color: rgb(86, 84, 84) !important;
    font-size: 15px !important;
    font-weight: 400;
  }
  &:hover {
    text-decoration: underline;
    color: rgb(86, 84, 84) !important;
  }
}
</style>
