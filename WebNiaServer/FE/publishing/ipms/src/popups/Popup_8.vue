<template>
  <el-dialog
    title="IP블록관리 상세정보"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <div class="popupContentTableTitle">IP블록관리 상세정보</div>
      <table>
        <tr>
          <th>공인/사설</th>
          <td>공인</td>
          <th>생성차수</th>
          <td>K2054845154</td>
        </tr>
        <tr>
          <th>서비스망</th>
          <td>KORNET</td>
          <th>작업일시</th>
          <td>2013-11-14 00:00:00</td>
        </tr>
        <tr>
          <th>IP 버전</th>
          <td>IPv4</td>
          <th>IP주소</th>
          <td>192.0.0.254</td>
        </tr>
        <tr>
          <th>비고</th>
          <td colspan="3">Mig / Start : 14.43.255.255 / End : 14.43.255.255 /</td>
        </tr>
      </table>

      <div class="popupContentTableTitle">IP 블록 세부 정보</div>
      <table>
        <tr>
          <th>시작 IP</th>
          <td>255.0.0.0</td>
          <th>끝 IP</th>
          <td>255.0.0.0</td>
        </tr>
        <tr>
          <th>총 IP 수</th>
          <td>13,000</td>
          <th>단위블록수</th>
          <td>512</td>
        </tr>
        <tr>
          <th>사용 IP 수</th>
          <td>162,343</td>
          <th>가용 IP 수</th>
          <td>162,343</td>
        </tr>
      </table>

    </div>
    <div class="popupContentTableBottom">
      <el-button type="primary" icon="el-icon-edit" size="small" round>수정</el-button>
      <el-button type="primary" icon="el-icon-delete" size="small" round>삭제</el-button>
      <el-button type="primary" icon="el-icon-close" size="small" round @click="dialogVisible=false">닫기</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: 'Popup4',
  data() {
    return {
      dialogVisible: false
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    show() {
      this.dialogVisible = true
    },
    handleRemove(file, fileList) {
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleExceed(files, fileList) {
    },
    beforeRemove(file, fileList) {
    }
  }
}
</script>
<style scoped>
</style>
