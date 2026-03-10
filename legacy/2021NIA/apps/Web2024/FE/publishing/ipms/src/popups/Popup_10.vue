<template>
  <el-dialog
    title="IP블록 중복체크 조회 결과"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div class="popupContentTable">
      <div class="popupContentTableTitle">중복체크 결과</div>
      <table>
        <tr>
          <th>계위</th>
          <td colspan="3">KORNET - 강남/서부NW운용본부 - 양재국사</td>
        </tr>
        <tr>
          <th>서비스</th>
          <td>-</td>
          <th>공인/사설</th>
          <td>공인</td>
        </tr>
        <tr>
          <th>대상IP블럭</th>
          <td>14.32.36.0/24</td>
          <th>할당가능여부</th>
          <td>할당가능</td>
        </tr>
        <tr>
          <th>참고내용</th>
          <td colspan="3">할당가능여부는 조회명령어 결과의 조합기준이며, 할당불가능시에도 운용자의 판단(책임)에 따라 할당이 가능합니다.</td>
        </tr>
      </table>

      <div class="popupContentTableTitle">접속장비</div>
      <table style="margin: 10px 0">
        <tr>
          <th>장비IP/장비타입</th>
          <td>210.183.28.150 / KORNET-JUNIPER</td>
          <th>접속결과</th>
          <td>요청실패(릴레이가 없거나 망문제)</td>
        </tr>
        <tr>
          <th>IP블럭 중복여부</th>
          <td colspan="3">중복아님</td>
        </tr>
        <tr>
          <th>조회명령어 결과</th>
          <td colspan="3">
            <textarea style="min-height:50px;">조회결과없음</textarea>
          </td>
        </tr>
        <tr>
          <th>조회명령어1</th>
          <td colspan="3">show router route-table 112.161.251/32 longer | match "No. of Routers: 0"</td>
        </tr>
        <tr>
          <th>IP블럭 중복 여부1</th>
          <td colspan="3">중복아님</td>
        </tr>
        <tr>
          <th>조회명령어 결과1</th>
          <td colspan="3">
            <textarea style="min-height:50px;">조회결과없음</textarea>
          </td>
        </tr>
        <tr>
          <th>조회명령어2</th>
          <td colspan="3">show router route-table 112.161.251/32 longer | match "No. of Routers: 0"</td>
        </tr>
        <tr>
          <th>IP블럭 중복 여부2</th>
          <td colspan="3">중복아님</td>
        </tr>
        <tr>
          <th>조회명령어 결과2</th>
          <td colspan="3">
            <textarea style="min-height:50px;">조회결과없음</textarea>
          </td>
        </tr>
      </table>
    </div>
    <div class="popupContentTableBottom">
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
