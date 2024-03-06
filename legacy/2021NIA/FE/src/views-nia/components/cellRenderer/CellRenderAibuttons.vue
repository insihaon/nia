<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="button-panel">
      <div v-if="params.type === 'sop'" class="common-btn" size="mini" @click="openModal(params, 'sop')">
        <i class="el-icon-circle-check sop-icon" /> SOP </div>
      <div v-if="params.type === 'alarm'" size="mini" @click.native="openModal(params, 'alarm')">
        <i class="el-icon-circle-check sop-icon" /> 장애 대응
      </div>
    </div>
  </div>
</template>

  <script>
  import { Base } from '@/min/Base.min'
  import Vue from 'vue'
  import { AppOptions } from '@/class/appOptions'
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
    computed: {},
    methods: {
      openModal(params, type) {
        params.action(params.data, type)
      },
    },

  })
  </script>
  <style lang="scss" scoped>

.CellRenderAibuttons{
  .sop-icon{

  }
  &:hover{
    color: red !important;
  }
}

  </style>
