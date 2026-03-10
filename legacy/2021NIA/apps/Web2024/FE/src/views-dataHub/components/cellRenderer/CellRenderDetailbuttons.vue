<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="button-panel">
      <el-button v-if="params.name != null" :type="isType" size="mini" class="common-btn" :disabled="isDisabledBtn" @click.native="openModal(params)"> {{ params.name }} </el-button>
    </div>
  </div>
</template>

  <script>
  import { Base } from '@/min/Base.min'
  import Vue from 'vue'

  const routeName = 'CellRenderDetailbuttons'

  export default Vue.extend({
    extends: Base,
    data() {
      return {
        name: routeName
      }
    },
    moutned() {
    },
    computed: {
      isDisabledBtn() {
       return this.params.data.status_cd_desc === this.CONSTANTS.authManagement.GRANT.code || this.params.data.status_cd_desc === this.CONSTANTS.authManagement.REJECT.code
      },
      isType() {
        switch (this.params.name) {
          case '신청 취소' : return 'danger'
          default: return 'info'
        }
      }
    },
    methods: {
      openModal(params) {
        params.action(params.type, params.data)
      }
    }
  })
  </script>
  <style lang="scss" scoped>
  </style>
