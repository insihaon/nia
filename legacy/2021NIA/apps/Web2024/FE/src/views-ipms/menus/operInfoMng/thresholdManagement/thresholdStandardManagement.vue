<template>
  <el-row class="w-100 h-100" :gutter="10">
    <div id="content">
      <div class="content_result">
        <div class="section_tit">
          <h3> IP 배정 임계치 관리</h3>
        </div>
        <el-col :span="8">
          <h4> 지역별 IP 보유 임계치 </h4>
          <table class="tbl_list">
            <thead>
              <tr>
                <th>할당율</th>
                <th>부호</th>
                <th>할당 임계치</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>할당IP/ 배정IP * 100</td>
                <td> &gt; </td>
                <td>
                  <el-input v-model="alctn_thrsh" style="width : 80%" /> &#37;
                </td>
              </tr>
            </tbody>
            <thead>
              <tr>
                <th>미할당 / 24율</th>
                <th>부호</th>
                <th>미할당 임계치</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>할당IP/ 배정IP * 100</td>
                <td>&lt;</td>
                <td>
                  <el-input v-model="mldng_thrsh" style="width : 80%" /> &#37;
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area">
            <a class="button_bk my-sm-1" href="#">
              <span>적용</span>
            </a>
          </div>
        </el-col>
        <el-col :span="8">
          <h4> 신인증 IP 최적화 임계치 </h4>
          <table class="tbl_list">
            <thead>
              <tr>
                <th>최적화 대상 L3 비율</th>
                <th>부호</th>
                <th>최적화 대상 L3 임계치</th>
              </tr>
            </thead>
            <tbody>
              <tr height="110">
                <td> 대상 L3 수 / 전체 L3 * 100</td>
                <td>&lt;</td>
                <td>
                  <el-input v-model="alctn_thrsh" style="width : 80%" /> &#37;
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area">
            <a class="button_bk my-sm-1" href="#">
              <span>적용</span>
            </a>
          </div>
        </el-col>
        <el-col :span="8">
          <h4> 감사티켓 수 임계치 </h4>
          <table class="tbl_list">
            <thead>
              <tr>
                <th>티켓수</th>
                <th>부호</th>
                <th>감사 불일치 임계치</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>감사불일치 IP 블럭수 / 배정이하블럭(할당포함) 수</td>
                <td>&lt;</td>
                <td>
                  <el-input v-model="ip_block" style="width : 80%" /> &#37;
                </td>
              </tr>
            </tbody>
            <thead>
              <tr>
                <th>IP 현행화율</th>
                <th>부호</th>
                <th>IP 현행화율 임계치</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>IP 현행화율</td>
                <td> &lt; </td>
                <td>
                  <el-input v-model="allocation_ip" style="width : 80%" /> &#37;
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area">
            <a class="button_bk my-sm-1" href="#">
              <span>적용</span>
            </a>
          </div>
        </el-col>
      </div>

      <div class="content_result">
        <div class="section_tit">
          <h3>DHCP 임계치 관리</h3>
        </div>
        <el-col :span="24">
          <h4> DHCP 임계치</h4>
          <table class="tbl_list">
            <thead>
              <tr>
                <th>기준일</th>
                <th>최대 할당율</th>
                <th>최소 할당율</th>
                <th>최소 IP수</th>
                <th>최소 IP BLOCK수</th>
                <th>수집 기준일</th>
                <th>비고</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>2014-10-20</td>
                <td><el-input v-model="maxAlloc" style="width : 80%" /> &#37;</td>
                <td>
                  <el-input v-model="minAlloc" style="width : 80%" /> &#37;
                </td>
                <td>
                  <el-input v-model="min_ip" style="width : 80%" /> 개
                </td>
                <td>
                  <el-input v-model="min_ip_block" style="width : 80%" /> 개
                </td>
                <td>
                  <el-input v-model="collection_date" style="width : 70%" /> 일(Days)
                </td>
                <td>
                  <el-input v-model="remark" style="width : 80%" />
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btn_area">
            <a class="button_bk my-sm-1" href="#">
              <span>적용</span>
            </a>
          </div>
        </el-col>
      </div>
    </div>
  </el-row>

</template>
<script>
import { Base } from '@/min/Base.min'
const routeName = 'ThresholdStandardManagement'

export default {
  name: routeName,
  components: { },
  extends: Base,
  data() {
    return {
      name: routeName,
      src: `webpack:///${__filename.replace(/\\/g, '/').replace(/\?.*$/, '')}`,
      alctn_thrsh: '',
      mldng_thrsh: '',
      allocation_ip: '',
      ip_block: '',
      maxAlloc: '',
      minAlloc: '',
      min_ip: '',
      min_ip_block: '',
      collection_date: '',
      remark: '',
    }
  },
  methods: {
    handleSearch(params) {
      /* const res = await api(params) */
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
