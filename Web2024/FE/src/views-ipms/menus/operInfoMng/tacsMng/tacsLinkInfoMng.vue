<template>
  <el-row class="w-100 h-100">
    <div class="content_result">
      <div class="section_tit">
        <h3>TACS 연동 정보</h3>
      </div>
      <table class="tbl_data entry">
        <tbody>
          <tr class="top">
            <th scope="row" class="first">연결 ID</th>
            <td> <input :value="resultVo.connId" class="txt" type="text"></td>
            <th scope="row">연결 PW</th>
            <td> <input :value="resultVo.connPw" class="txt" type="text"></td>
          </tr>
          <tr>
            <th scope="row" class="first">연결 OGW IP</th>
            <td> <input :value="resultVo.connOgwIp" class="txt" type="text"></td>
            <th scope="row">연결 OGW PORT</th>
            <td> <input :value="resultVo.connOgwPort" class="txt" type="password"></td>
          </tr>
          <tr>
            <th class="first" scope="row">연결 MAC 주소</th>
            <td colspan="3"> <input :value="resultVo.connMacAddr" class="txt" type="text"></td>
          </tr>
          <tr>
            <th class="last" scope="row">이메일주소</th>
            <td colspan="3"> <input :value="resultVo.smailAddress" class="txt" type="text"></td>
          </tr>
        </tbody>
      </table>
      <div class="btn_area float-right my-1">
        <el-button icon="el-icon-edit-outline" size="mini" @click="fnUpdateTacsConnBas">수정</el-button>
      </div>
    </div>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
import { ipmsModelApis, apiRequestModel, ipmsJsonApis, apiRequestJson } from '@/api/ipms'
import { onMessagePopup } from '@/utils'

const routeName = 'TacsLinkInfoManagement'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      resultVo: {
        ntacsConnBasSeq: '',
        connId: '',
        connPw: '',
        connOgwIp: '',
        connOgwPort: '',
        connMacAddr: '',
        smailAddress: '',
      }
    }
  },
  mounted () {
    this.fnViewTacsConnBas()
  },
  methods: {
    async fnViewTacsConnBas() {
      try {
        const res = await apiRequestModel(ipmsModelApis.viewTacsConnBas, {})
        this.resultVo = res.result.data
      } catch (error) {
        this.error(error)
      }
    },
    fnUpdateTacsConnBas() {
      this.confirm('TACS연동 정보 수정 시 문제가 발생할 수 있습니다.<br>정말 수정하시겠습니까?', '알림', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
        dangerouslyUseHTMLString: true
      }).then(async() => {
        try {
          const { ntacsConnBasSeq, connId, connPw, connOgwIp, connOgwPort, connMacAddr, smailAddress } = this.resultVo
          const params = { ntacsConnBasSeq, connId, connPw, connOgwIp, connOgwPort, connMacAddr, smailAddress }
          const res = await apiRequestJson(ipmsJsonApis.updateTacsConnBas, params)
          if (res.commonMsg === 'SUCCESS') {
            onMessagePopup(this, 'TACS연동 정보 수정이 정상적으로 처리되었습니다.')
          } else {
            onMessagePopup(this, res.commonMsg)
          }
        } catch (error) {
          this.$alert('요청 실패 하였습니다.', '알림', {
            confirmButtonText: '확인',
          })
          console.error(error)
        }
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
