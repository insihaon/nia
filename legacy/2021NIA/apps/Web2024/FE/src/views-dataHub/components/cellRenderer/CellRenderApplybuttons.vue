<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="button-panel">
      <el-button v-if="params.useApply.name != null" class="common-btn" size="mini" @click.native="openModal(params.useApply.name)"> {{ params.useApply.name }} </el-button>
      <el-button v-if="params.selectDataSet.name != null" :disabled="hasOndemand" :type="isType(params.selectDataSet.name)" size="mini" plain @click="handleRouteButton">
        {{ params.selectDataSet.name }}
      </el-button>
    </div>
  </div>
</template>

<script>
import { Base } from '@/min/Base.min'
import Vue from 'vue'
import { mapState } from 'vuex'
import { AppOptions } from '@/class/appOptions'

const routeName = 'CellRenderApplybuttons'

export default Vue.extend({
  extends: Base,
  data() {
    return {
      name: routeName
    }
  },
  computed: {
     ...mapState({
        blackDtlList: (state) => state.user.blackDtlList
     }),
     hasNoAddGrant() {
       return AppOptions.instance.isGod !== true || !this.blackDtlList.includes('btnAdd')
     },
     hasOndemand() {
       return this.params.data.exec_mode_cd !== this.CONSTANTS.apiAlarm.onDemand.state
     }
  },
  mounted() {
  },
  methods: {
    handleRouteButton() {
    if (this.params.data.exec_mode_cd === this.CONSTANTS.apiAlarm.batch.state) {
      return false
    }
    this.$router.push({ name: 'SelectDataSet', query: this.params.data })
    },
    isType(param) {
      switch (param) {
        case '권한신청' : return 'primary'
        case 'API 데이터' : return 'info'
        default: return 'info'
      }
    },
    openModal(params) {
      const useApply = this.params.useApply
      const selectDataSet = this.params.selectDataSet
      params === '권한신청' ? this.params.useApply.action(useApply.type, this.params.data) : selectDataSet.action(selectDataSet.type, this.params.data)
    },
  }
})
</script>
