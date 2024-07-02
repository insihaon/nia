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
            <td> <input :value="connectId" class="txt" type="text"></td>
            <th scope="row">연결 PW</th>
            <td> <input :value="connectPw" class="txt" type="text"></td>
          </tr>
          <tr>
            <th scope="row" class="first">연결 OGW IP</th>
            <td> <input :value="ogwIp" class="txt" type="text"></td>
            <th scope="row">연결 OGW PORT</th>
            <td> <input :value="ogwPort" class="txt" type="text"></td>
          </tr>
          <tr>
            <th class="first" scope="row">연결 MAC 주소</th>
            <td colspan="3"> <input :value="connectMac" class="txt" type="text"></td>
          </tr>
          <tr>
            <th class="last" scope="row">이메일주소</th>
            <td colspan="3"> <input :value="connectEmail" class="txt" type="text"></td>
          </tr>
        </tbody>
      </table>
      <div class="btn_area float-right my-1">
        <a href="#none">
          <img src="@/assets/images/ipms/content/btn_modify_off.gif">
        </a>
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
        tableColumns: [
          { prop: '', label: '연동 ID', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '시스템 명', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '연동 형태', align: 'center', columnVisible: true, showOverflow: true },
          { prop: '', label: '사용여부', align: 'center', columnVisible: true, showOverflow: true },
        ],
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
      connectId: 'adsf',
      connectPw: '*****',
      ogwIp: '121.170.206.150',
      ogwPort: '1300',
      connectMac: '54-9F-35-14-1F-A4',
      connectEmail: 'dimmby@codejone.com',
    }
  },
  methods: {
      handleSearch(requestParameter) {
      console.log(requestParameter)
    }
  }
}
</script>
<style lang="css" scoped>
</style>
