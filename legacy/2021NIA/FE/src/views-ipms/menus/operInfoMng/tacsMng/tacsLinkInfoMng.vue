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
            <td> <input :value="connId" class="txt" type="text"></td>
            <th scope="row">연결 PW</th>
            <td> <input :value="connPw" class="txt" type="text"></td>
          </tr>
          <tr>
            <th scope="row" class="first">연결 OGW IP</th>
            <td> <input :value="connOgwIp" class="txt" type="text"></td>
            <th scope="row">연결 OGW PORT</th>
            <td> <input :value="connOgwPortPort" class="txt" type="text"></td>
          </tr>
          <tr>
            <th class="first" scope="row">연결 MAC 주소</th>
            <td colspan="3"> <input :value="connMacAddr" class="txt" type="text"></td>
          </tr>
          <tr>
            <th class="last" scope="row">이메일주소</th>
            <td colspan="3"> <input :value="smailAddress" class="txt" type="text"></td>
          </tr>
        </tbody>
      </table>
      <div class="btn_area float-right my-1">
        <el-button @click="handleModify">
          <img src="@/assets/images/ipms/content/btn_modify_off.gif">
        </el-button>
      </div>
    </div>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'TacsLinkInfoManagement'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      componentList: [
        {
          key: 'BoardSearchCondition', props: {
            defaultValue: '',
            prop_options: [
              { label: '전체', value: '' },
              { label: '연동 ID', value: 'siId' },
              { label: '시스템 명', value: 'credateId' },
            ]
          }
        }
      ],
      connId: 'adsf',
      connPw: 'gosy690',
      connOgwIp: '121.170.206.150',
      connOgwPortPort: '1300',
      connMacAddr: '54-9F-35-14-1F-A4',
      smailAddress: 'asdf@kt.com',
    }
  },
  methods: {
    handleModify() {
      this.confirm('TACS연동 정보 수정 시 문제가 발생할 수 있습니다.<br>정말 수정하시겠습니까?', '알림', {
        cancelButtonText: '취소',
        confirmButtonText: '확인',
      }).then(async() => {
        try {
          const { connId, connPw, connOgwIp, connOgwPortPort, connMacAddr, smailAddress } = this
          const params = { connId, connPw, connOgwIp, connOgwPortPort, connMacAddr, smailAddress }
          console.log(params)
          // const res = await api(param)
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
