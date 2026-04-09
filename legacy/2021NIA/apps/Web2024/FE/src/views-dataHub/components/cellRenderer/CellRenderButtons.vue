<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="button-panel">
      <el-button v-if="params.name != null" :type="isChangeBtn" size="mini" :disabled="!hasOndemand" plain @click.native="openModal(params)"> {{ hasRetryCode }} </el-button>
      <el-button v-if="params.type === 'accept'" :type="isBtn === '가능' ? 'primary' : 'danger'" size="mini" :disabled="isDisabledBtn" plain @click.native="openModal(params)"> {{ isBtn }} </el-button>
      <el-button v-if="params.title === 'edit'" class="common-btn" size="mini" plain :disabled="!hasNoAddGrant" @click.native="openModal(params, 'edit')"> {{ '수정' }} </el-button>
      <el-button v-if="params.key === 'add'" class="common-btn" size="mini" plain :disabled="disableCheckAdd" @click.native="openModal(params)"> {{ '추가' }} </el-button>
      <el-button v-if="params.key === 'delete'" type="danger" size="mini" :disabled="!hasNoAddGrant" plain @click.native="openModal(params, 'delete')"> {{ '삭제' }} </el-button>
      <el-button v-if="params.key === 'grant'" class="common-btn" :disabled="!btnDisabled" size="mini" plain @click.native="openModal(params, 'grant')"> {{ params.title }} </el-button>
      <el-button v-if="params.type === 'reject'" type="danger" :disabled="!btnDisabled" size="mini" plain @click.native="openModal(params, 'reject')">  {{ '반려' }}  </el-button>
    </div>
  </div>
</template>

  <script>
  import { Base } from '@/min/Base.min'
  import Vue from 'vue'
  import { mapState } from 'vuex'
  import { AppOptions } from '@/class/appOptions'

  const routeName = 'CellRenderButtons'

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
     hasRetryCode() {
       return this.params.data.retry_proc ? this.CONSTANTS.apiAlarm.retry.label : this.CONSTANTS.apiAlarm.onDemand.label
     },
      hasNoAddGrant() {
        return AppOptions.instance.isGod || this.blackDtlList.includes('btnAdd')
      },
      isDisabledBtn() {
         return this.params.data.exec_mode_cd === this.CONSTANTS.apiAlarm.onDemand.state || this.params.data.isReprocessing === true
      },
      btnDisabled() {
        return this.hasNoAddGrant && this.params.data.status_cd_desc === this.CONSTANTS.authDataSet.APPLY.label
      },
      disableCheckAdd() {
        return this.params.newData.some(newDataRow => newDataRow.metadata_seq === this.params.data.metadata_seq)
      },
      hasOndemand() {
        return this.hasNoAddGrant && this.params.data.retry_proc !== this.CONSTANTS.apiAlarm.retry.code && this.params.data.exec_mode_cd === this.CONSTANTS.apiAlarm.onDemand.state
      },
      isChangeBtn() {
        switch (this.params.name) {
          case '수정' : return 'info'
          case '승인' : return 'primary'
          case '추가' : return 'info'
          case '상세정보' : return 'info'
          case '재처리' : return 'success'
          case '재처리 성공' : return 'info'
          case '삭제' : return 'danger'
          default: return 'danger'
        }
      },
      isBtn() {
        return this.params.data.isReprocessing ? '불가' : '가능'
      }

    },
    moutned() {
    },
    methods: {
      openModal(params, type) {
        params.action(params.data, type)
      },
    }
  })
  </script>
  <style lang="scss" scoped>

  </style>
