<template>
  <div class="cell-container" :class="{ [name]: true }">
    <div class="button-panel">
      <el-popover
        ref="popover"
        placement="bottom"
        trigger="click"
      >
        <el-table :data="rowData">
          <el-table-column property="api_name" label="API명" />
          <el-table-column property="api_desc" label="시스템" />
          <el-table-column property="api_key" label="API 키" />
          <el-table-column property="kong_host" label="Kong 호스트" />
        </el-table>
        <div class="h-100" style="text-align: right; padding-top: 15px">
          <el-button class="popover-btn" size="mini" @click="handleClipboard('api_key')">{{ 'API_key' }}</el-button>
          <el-button class="popover-btn" size="mini" @click="handleClipboard('curl')">{{ 'Command' }}</el-button>
        </div>

      </el-popover>
      <el-button v-if="params.type === 'api'" v-popover:popover class="common-btn" size="mini" :disabled="!isBtnCheck" plain @click="openModal(params, 'api')"> API KEY</el-button>
      <el-button v-if="params.key === 'doubleKey'" :type="isType" size="mini" :disabled="!isDisabledBtn" plain @click.native="openModal(params, 'cancel')"> 신청취소 </el-button>
      <el-button v-if="params.key === 'revoke'" :type="isType" size="mini" plain :disabled="!hasApplycode" @click.native="openModal(params, 'cancel')"> 권한회수 </el-button>
    </div>
  </div>
</template>

  <script>
  import { Base } from '@/min/Base.min'
  import Vue from 'vue'
  import { mapState } from 'vuex'
  import clip from '@/utils/clipboard'
  import { AppOptions } from '@/class/appOptions'
  const routeName = 'CellRenderAPIbuttons'

  export default Vue.extend({

    extends: Base,
    data() {
      return {
        name: routeName,
        rowData: []
      }
    },
    moutned() {
    },
    computed: {
     ...mapState({
        blackDtlList: (state) => state.user.blackDtlList,
        userUid: (state) => state.user.info.Uid,
        roles: (state) => state.user.roles,
     }),
      hasNoAddGrant() {
       return AppOptions.instance.isGod || this.blackDtlList.includes('btnAdd')
      },
      isAPIkey() {
       return this.params.data.status_cd === this.CONSTANTS.authManagement.GRANT.label
      },
      isBtnCheck() {
      const check = !!this.roles.includes('ROLE_ADMIN')
      if (check) {
        return this.params.data.exec_mode_cd === this.CONSTANTS.apiAlarm.onDemand.state && this.isAPIkey
      } else {
        return this.hasUid && this.params.data.exec_mode_cd === this.CONSTANTS.apiAlarm.onDemand.state && this.isAPIkey
      }
      },
      hasUid() {
        return this.userUid === this.params.data.reg_user
      },
      hasApplycode() {
       return this.hasNoAddGrant && this.isAPIkey
      },
      isDisabledBtn() {
       return this.params.data.status_cd === this.CONSTANTS.authManagement.APPLY.label
      },
      isType() {
        switch (this.params.name) {
          default: return 'danger'
        }
      },
      hubUrl () {
        return this.$store.state.app.server.kongUrl
      },
    },
    methods: {
      openModal(params, type) {
        params.action(params.data, type)
        this.rowData = [params.data]
      },
      handleClipboard(type) {
        const reqestParam = this.rowData[0].req_param ?? ''
        const curlMessage =
        `curl -d ` + `'${reqestParam}'` + ` -X POST ` + this.hubUrl +
        ` -H "Content-Type:application/json"` +
        ` -H "apikey: ${this.rowData[0].api_key}"` +
        ` -H "Host: ${this.rowData[0].kong_host}"`
        let target
        if (type === 'api_key') {
          target = this.rowData[0].api_key
        } else {
          target = curlMessage
        }
        if (!target) {
          this.$message({
            message: '클립보드로 복사 할 데이터가 없습니다.',
            type: 'error'
          })
          return
        }
          const text =
          typeof target === 'object' ? JSON.stringify(target) : target.toString()
        clip(text, event)
      }
    },

  })
  </script>
  <style lang="scss" scoped>
    .popover-btn{
      background: #303775 !important;
      color: whitesmoke !important;
    }
  </style>
